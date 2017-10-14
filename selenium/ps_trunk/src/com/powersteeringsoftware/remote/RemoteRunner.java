package com.powersteeringsoftware.remote;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.CLI;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import net.propero.rdp.Rdesktop;
import net.propero.rdp.RdesktopException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Permission;
import java.util.*;
import java.util.concurrent.*;

/**
 * Class for running command or tests via rdesktop (net.propero.rdp.Rdesktop)
 * User: szuev
 * Date: 25.07.11
 * Time: 14:15
 */
public class RemoteRunner {
    private static final String DEFAULT_SCREEN_RESOLUTION = "1440x2000";
    private static final String DEFAULT_HOST = "LOCALHOST";
    private static final String JAVA_COMMAND_PREFIX = "cmd /c java";
    private static final String JAVA_MIN_HEAP_SPACE_PREFIX = "-Xms256m";
    private static final String JAVA_MAX_HEAP_SPACE_PREFIX = "-Xmx1024m";
    private static final String ENCODIG_COMMAND_PREFIX = "-Dfile.encoding=";
    private static final String JAR_COMMAND_PREFIX = "-jar";
    private static final String AUTO_TEST_JAR = "autotests.jar";
    private static final String COMMAND_LOGS_FILE = RemoteRunner.class.getSimpleName() + ".txt";
    private static final String CMD_LOGOFF = " & logoff";


    private static String user;
    private static String password;
    private static String resolution;
    private static String host;
    private static String autoTestJar = AUTO_TEST_JAR;
    private static String domain;
    private static String command;
    private static Boolean useStartUpDirectory;
    private static String directory;
    private static File fileToRun;
    private static long timeout;


    public static void main(String[] args) {
        RemoteRunnerCLI cli = new RemoteRunnerCLI();
        List other = null;
        try {
            other = cli.doParse(args);
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
            System.exit(-1);
        }
        int exit = 0;
        try {
            PSLogger.info("Other options : " + other);
            beforeRunning(other);
            run();
        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
            exit = -1;
        } catch (RdesktopException r) {
            PSLogger.fatal(r.getMessage());
            exit = 1;
        } catch (TimeoutException t) {
            PSLogger.fatal("Timeout after " + timeout + "s");
            exit = 2;
        } catch (Exception e) {
            PSLogger.fatal("Exception while running");
            PSLogger.fatal(e);
            exit = 3;
        } finally {
            afterRunning();
        }
        System.exit(exit);
    }

    private static void run() throws ExecutionException, InterruptedException, TimeoutException, RdesktopException {
        Callable rdp = new Callable() {
            @Override
            public Object call() throws Exception {
                List args = toRDPArgs();
                PSLogger.info("Rdesktop command is " + args);
                class ExitError extends Error {
                    ExitError(String e) {
                        super(e);
                    }
                }
                SecurityManager my = new SecurityManager() {
                    @Override
                    public void checkExit(int status) {
                        PSLogger.debug("Try to exit!");
                        throw new ExitError(String.valueOf(status));
                    }

                    @Override
                    public void checkPermission(Permission perm) {
                        // allow anything.
                    }

                    @Override
                    public void checkPermission(Permission perm, Object context) {
                        // allow anything.
                    }
                };
                System.setSecurityManager(my);
                try {
                    Rdesktop.main((String[]) args.toArray(new String[args.size()]));
                } catch (ExitError se) {
                    int code = Integer.parseInt(se.getMessage());
                    PSLogger.info(Rdesktop.class.getName() + " exit code is " + code);
                    if (code != 0) throw new RdesktopException("Exit code is not zero :" + code);
                }
                return null;
            }
        };
        ExecutorService exec = Executors.newSingleThreadExecutor();
        exec.submit(rdp).get(timeout, TimeUnit.SECONDS);
        exec.shutdownNow();
    }

    /**
     * this is a local server actions:
     *
     * @param command
     */
    private static void beforeRunning(List params) throws IOException, ParseException {
        if (command == null) {
            command = JAVA_COMMAND_PREFIX + " " +
                    JAVA_MIN_HEAP_SPACE_PREFIX + " " + JAVA_MAX_HEAP_SPACE_PREFIX + " " + // hotfix for ie.
                    // ENCODIG_COMMAND_PREFIX + System.getProperty("file.encoding") + " " +
                    JAR_COMMAND_PREFIX + " " + autoTestJar + " " +
                    params.toString().replaceAll(",\\s+", " ").replace("[", "").replace("]", "");
        }
        PSLogger.info("Inner command is " + command);
        if (domain == null) {
            domain = isLocal() ? LocalServerUtils.getHostName() : host;
        }
        PSLogger.debug("Domain: " + domain);
        // get map if no user or password specified
        Map<String, Object> configUsers = null;
        if (user == null || password == null) {
            configUsers = CoreProperties.getClientUsers(domain);
            if (configUsers.isEmpty()) configUsers = null;
        }
        PSLogger.debug("Config user: " + configUsers);
        if (!isLocal()) {
            useStartUpDirectory = false;
            if (configUsers != null) { // select first user from list
                if (user == null) user = (String) configUsers.keySet().toArray()[0];
                password = (String) configUsers.get(user);
            }
            if (user == null || password == null)
                throw new ParseException("username and password should be specified");
            return;
        }

        // now local server actions:
        File dir;
        if (directory == null) {
            directory = CoreProperties.THIS_WORK_DIR;
        }
        if (!(dir = new File(directory)).exists() || !dir.isDirectory()) {
            throw new FileNotFoundException("Can't find directory " + directory);
        }
        PSLogger.info("Working directory is " + dir);
        File jar = new File(directory + File.separator + autoTestJar);
        if (command.contains(autoTestJar) && !jar.exists()) {
            throw new FileNotFoundException("Can't find " + autoTestJar + " : " + jar.getAbsolutePath());
        }

        if (configUsers != null) {
            if (user == null) {
                for (String u : configUsers.keySet()) {
                    if (LocalServerUtils.getLocalServer().getSessionId(u) == -1) { // select available user
                        user = u;
                        break;
                    }
                }
            }
            if (user != null)
                password = (String) configUsers.get(user);
        }

        if (user == null || password == null)
            throw new ParseException("username and password should be specified");

        if (!LocalServerUtils.getLocalServer().isUserExists(user)) {
            throw new NullPointerException("Can't find user '" + user + "'");
        }
        // kill session if exist
        LocalServerUtils.getLocalServer().killSession(user);

        if (useStartUpDirectory == null)
            useStartUpDirectory = LocalServerUtils.getLocalServer().isWin2k8();
        if (!useStartUpDirectory) { // then use -s to run tests
            command += CMD_LOGOFF;
            return;
        }

        // create bat file to run command (w2k8)
        String startupDir = LocalServerUtils.getLocalServer().getStartupLocationDir(user);
        PSLogger.info("Start Up directoty is " + startupDir);
        LocalServerUtils.deleteAllFilesFromDir(startupDir);

        String fileName = startupDir + File.separator + System.currentTimeMillis() + ".bat";
        PSLogger.info("Temp file is " + fileName);
        StringBuilder content = new StringBuilder();
        //content.append("@ECHO OFF").append("\n");
        content.append("cd /d ").append(dir.getAbsolutePath()).append("\n");
        content.append(command).append(" 1>").append(COMMAND_LOGS_FILE).append(" 2>&1").append(CMD_LOGOFF).append("\n");
        fileToRun = LocalServerUtils.createFile(fileName, content.toString());

    }

    private static void afterRunning() {
        PSLogger.debug("After running");
        System.setSecurityManager(null);
        if (!isLocal()) return;
        deleteFiles();
        LocalServerUtils.getLocalServer().killSession(user);
        try {
            PSLogger.debug("logs content : ");
            StringBuffer res = LocalServerUtils.readFile(directory + File.separator + COMMAND_LOGS_FILE, 1000);
            PSLogger.debug(res);
        } catch (IOException e) {
            PSLogger.error("After running:");
            PSLogger.fatal(e);
        }
    }

    private static void deleteFiles() {
        if (fileToRun != null && fileToRun.exists()) {
            if (fileToRun.delete()) {
                PSLogger.debug("File " + fileToRun + " is deleted");
            } else {
                throw new RuntimeException("Can't delete file " + fileToRun);
            }
        }
    }

    private static boolean isLocal() {
        return host != null && host.equals(DEFAULT_HOST);
    }

    private static List<String> toRDPArgs() {
        List<String> res = new ArrayList<String>();
        if (!useStartUpDirectory) {
            res.add("-s");
            res.add(command);
        }
        if (directory != null) {
            res.add("-c");
            res.add(directory);
        }
        res.add("-l");
        res.add("INFO");
        res.add("-g");
        res.add(resolution);
        res.add("-p");
        res.add(password);
        res.add("-u");
        res.add(user);
        res.add("-d");
        res.add(domain);
        res.add(host);
        return res;
    }

    private static class RemoteRunnerCLI extends CLI {
        public static final String CLI_OPTION_JAR = "jar";
        public static final String CLI_OPTION_RDP_HOST = "host";
        public static final String CLI_OPTION_RDP_DOMAIN = "domain";
        public static final String CLI_OPTION_RDP_USER = "user";
        public static final String CLI_OPTION_RDP_PASSWORD = "password";
        public static final String CLI_OPTION_RDP_SCREEN_RESOLUTION = "screen";
        public static final String CLI_OPTION_RDP_DIRECTORY = "directory";
        public static final String CLI_OPTION_RDP_DEBUG_COMMAND = "command";
        public static final String CLI_OPTION_RDP_USE_STARTUP = "startup";
        public static final String CLI_OPTION_RDP_TIMEOUT = "timeout";

        public List doParse(String[] args) throws ParseException {
            super.parse(args);
            System.out.println(cli.getArgList());
            CoreProperties.setPropertiesFile(cli.getOptionValue(CLI_OPTION_CORE_PROPERTIES));
            CoreProperties.loadProperties(false);
            List<String> list = new ArrayList<String>(Arrays.asList(args));
            user = cli.hasOption(CLI_OPTION_RDP_USER) ? cli.getOptionValue(CLI_OPTION_RDP_USER) : null;
            if (user != null && user.contains(",")) { // choose user from list
                String[] users = user.split("\\s*,\\s*");
                if (users.length != 7) throw new ParseException("Incorrect user list specified");
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2;
                user = users[day];
            }
            remove(list, CLI_OPTION_RDP_USER);
            autoTestJar = cli.hasOption(CLI_OPTION_JAR) ? cli.getOptionValue(CLI_OPTION_JAR) : AUTO_TEST_JAR;
            remove(list, CLI_OPTION_JAR);
            password = cli.hasOption(CLI_OPTION_RDP_PASSWORD) ? cli.getOptionValue(CLI_OPTION_RDP_PASSWORD) : null;
            remove(list, CLI_OPTION_RDP_PASSWORD);
            resolution = cli.hasOption(CLI_OPTION_RDP_SCREEN_RESOLUTION) ? cli.getOptionValue(CLI_OPTION_RDP_SCREEN_RESOLUTION) : DEFAULT_SCREEN_RESOLUTION;
            remove(list, CLI_OPTION_RDP_SCREEN_RESOLUTION);
            host = cli.hasOption(CLI_OPTION_RDP_HOST) ? cli.getOptionValue(CLI_OPTION_RDP_HOST).toUpperCase() : DEFAULT_HOST;
            remove(list, CLI_OPTION_RDP_HOST);
            domain = cli.hasOption(CLI_OPTION_RDP_DOMAIN) ? cli.getOptionValue(CLI_OPTION_RDP_DOMAIN) : null;
            remove(list, CLI_OPTION_RDP_DOMAIN);
            command = cli.hasOption(CLI_OPTION_RDP_DEBUG_COMMAND) ? cli.getOptionValue(CLI_OPTION_RDP_DEBUG_COMMAND) : null;
            remove(list, CLI_OPTION_RDP_DEBUG_COMMAND);
            useStartUpDirectory = cli.hasOption(CLI_OPTION_RDP_USE_STARTUP) ? Boolean.parseBoolean(cli.getOptionValue(CLI_OPTION_RDP_USE_STARTUP)) : null;
            remove(list, CLI_OPTION_RDP_USE_STARTUP);
            directory = cli.hasOption(CLI_OPTION_RDP_DIRECTORY) ? cli.getOptionValue(CLI_OPTION_RDP_DIRECTORY) : null;
            remove(list, CLI_OPTION_RDP_DIRECTORY);
            timeout = cli.hasOption(CLI_OPTION_RDP_TIMEOUT) ? Long.parseLong(cli.getOptionValue(CLI_OPTION_RDP_TIMEOUT)) : CoreProperties.getTestTimeoutInSec();
            remove(list, CLI_OPTION_RDP_TIMEOUT);
            return list;
        }

        private void remove(List<String> list, String opt) {
            if (!cli.hasOption(opt)) return;
            int index = list.indexOf("-" + opt);
            if (index == -1) index = list.indexOf("--");
            list.remove(index);
            list.remove(index);
        }

        protected void setOptions() {
            super.setOptions();

            OptionBuilder.withArgName("file");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("auto-tests jar file name");
            Option jar = OptionBuilder.create(CLI_OPTION_JAR);
            jar.setRequired(false);
            options.addOption(jar);

            OptionBuilder.withArgName("address");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("remote or local host for RDP connection");
            Option host = OptionBuilder.create(CLI_OPTION_RDP_HOST);
            host.setRequired(false);
            options.addOption(host);

            OptionBuilder.withArgName("address");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("domain for RDP connection");
            Option domain = OptionBuilder.create(CLI_OPTION_RDP_DOMAIN);
            domain.setRequired(false);
            options.addOption(domain);

            OptionBuilder.withArgName("username");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("user name or user list for RDP connection (mandatory). user list should has 7 elements (for each day of the week). separator is ','");
            Option user = OptionBuilder.create(CLI_OPTION_RDP_USER);
            user.setRequired(false);
            options.addOption(user);

            OptionBuilder.withArgName("password");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("user password for RDP connection (mandatory)");
            Option password = OptionBuilder.create(CLI_OPTION_RDP_PASSWORD);
            password.setRequired(false);
            options.addOption(password);

            OptionBuilder.withArgName("geometry");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("screen resolution for RDP (format WxH)");
            Option resolution = OptionBuilder.create(CLI_OPTION_RDP_SCREEN_RESOLUTION);
            resolution.setRequired(false);
            options.addOption(resolution);

            OptionBuilder.withArgName("command");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("RDP Command to execute on specified host (its for debug)");
            Option cmd = OptionBuilder.create(CLI_OPTION_RDP_DEBUG_COMMAND);
            cmd.setRequired(false);
            options.addOption(cmd);

            OptionBuilder.withArgName("true or false");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("RDP Flag. if true use startup folder for running command. Otherwise /s switch");
            Option startup = OptionBuilder.create(CLI_OPTION_RDP_USE_STARTUP);
            startup.setRequired(false);
            options.addOption(startup);

            OptionBuilder.withArgName("directory");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("Directory to run this");
            Option dir = OptionBuilder.create(CLI_OPTION_RDP_DIRECTORY);
            dir.setRequired(false);
            options.addOption(dir);

            OptionBuilder.withArgName("long");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("Timeout in seconds for RDP session");
            Option timeout = OptionBuilder.create(CLI_OPTION_RDP_TIMEOUT);
            timeout.setRequired(false);
            options.addOption(timeout);

        }

    }
}
