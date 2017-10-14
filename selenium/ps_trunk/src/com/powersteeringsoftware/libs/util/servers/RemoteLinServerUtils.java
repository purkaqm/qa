package com.powersteeringsoftware.libs.util.servers;

import com.jcraft.jsch.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 05.05.2010
 * Time: 16:17:21
 * To change this template use File | Settings | File Templates.
 */
public class RemoteLinServerUtils {
    protected static int trynum = 0;
    private static final int TIMEOUT = 20 * 60; // sec
    private static final int TIME_STEP = 10; // sec
    private static final int ATTEMPT_NUM = 10;

    private static final int DEFAULT_SSH_PORT = 22;

    private String host;
    private String user;
    private String password;
    private int port;

    private static LogsChecker getLogsThread;

    private RemoteLinServerUtils(String host, String user, String passwd, int port) {
        this.host = host;
        this.user = user;
        this.password = passwd;
        this.port = port;
    }

    public static RemoteLinServerUtils getServer(String host, String user, String pwd) {
        return new RemoteLinServerUtils(host, user, pwd, DEFAULT_SSH_PORT);
    }

    public static RemoteLinServerUtils getServer() {
        if (CoreProperties.getApplicationServerHost() == null) return null;
        int port = DEFAULT_SSH_PORT;
        if (CoreProperties.getApplicationServerPort() != null) {
            port = Integer.valueOf(CoreProperties.getApplicationServerPort());
        }
        return new RemoteLinServerUtils(CoreProperties.getApplicationServerHost(),
                CoreProperties.getApplicationServerUser(),
                CoreProperties.getApplicationServerPassword(), port);
    }

    public static LogsChecker getLogChecker() {
        if (getLogsThread != null) return getLogsThread;
        if (CoreProperties.getApplicationServerLogs() == null) {
            PSLogger.info("Remote log pass is not specified");
            return null;
        }
        getLogsThread = new LogsChecker(getServer(CoreProperties.getApplicationServerHost(),
                CoreProperties.getApplicationServerUser(),
                CoreProperties.getApplicationServerPassword()), CoreProperties.getApplicationServerLogs());
        return getLogsThread;
    }

    public ExecResult exec(String cmd) {
        ExecResult res = exec(ATTEMPT_NUM, cmd);
        if (res == null) {
            PSLogger.fatal("Failed to execute cmd '" + cmd + "'. try last time.");
            return exec(1, cmd);
        }
        return res;
    }

    private ExecResult exec(int attemptNum, String cmd) {
        JSch jsch = new JSch();
        Session session = null;
        Channel channel = null;
        try {
            trynum++;
            int time = 0;
            PSLogger.debug("Try to execute: " + cmd + " on '" + user + "@" + host + ":" + password + "'");
            session = jsch.getSession(user, host, port);
            //session.setTimeout(TIMEOUT);
            session.setUserInfo(new MyUserInfo());
            session.connect();
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(cmd + " 2>&1");
            channel.setOutputStream(System.out);
            ((ChannelExec) channel).setErrStream(System.err);
            InputStream in = channel.getInputStream();
            channel.connect();
            String result = "";

            byte[] tmp = new byte[1024];
            while (time < TIMEOUT) {
                while (in.available() > 0 && time < TIMEOUT) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) break;
                    result = result + new String(tmp, 0, i);
                    Thread.sleep(1000);
                    time++;
                }
                if (channel.isClosed()) {
                    break;
                }
                Thread.sleep(1000);
                time++;
            }
            int exitcode = channel.getExitStatus();
            ExecResult res = new ExecResult(exitcode, result);
            //System.out.println(res.toString());
            channel.disconnect();
            session.disconnect();
            trynum = 0;
            if (time >= TIMEOUT) {
                PSLogger.error("Timeout (" + TIMEOUT + " sec) was reached while ssh exec on server " + host);
            }
            //PSLogger.debug(res);
            return res;
        } catch (Exception e) {
            if ((e instanceof SocketTimeoutException || e.getMessage().contains("SocketTimeoutException") ||
                    e.getMessage().contains("Connection timed out") ||
                    e.getMessage().contains("Connection refused")) && trynum < attemptNum) {
                PSLogger.debug(e.getMessage());
                PSLogger.debug("It seems that sshd was started yet, sleep " + TIME_STEP + " sec and try again");
                try {
                    Thread.sleep(TIME_STEP * 1000);
                } catch (InterruptedException e1) { /* some exp */ }
                return exec(attemptNum, cmd);
            }
            PSLogger.fatal(e);
            trynum = 0;
            return null;
        } finally {
            if (channel != null && channel.isConnected()) {
                try {
                    channel.disconnect();
                } catch (Exception e) { /*ignore*/ }
            }
            if (session != null && session.isConnected()) {
                try {
                    session.disconnect();
                } catch (Exception e) { /*ignore*/ }
            }
        }
    }

    public String getDate() {
        ExecResult res = exec(ATTEMPT_NUM, "date");
        if (res == null) {
            PSLogger.fatal("failed to get date");
            return null;
        }
        return res.getResult();
    }

    public String getThreadDump(int pid) {
        PSLogger.debug("Ger thread dump for process " + pid);
        ExecResult res = exec("jstack " + pid);
        if (res == null) {
            PSLogger.fatal("failed to get thread dump for " + pid);
            return null;
        }
        return res.getResult();
    }

    public List<Integer> getProcessList(String name) {
        PSLogger.debug("Ger process list for " + name);
        ExecResult res = exec("ps afx | grep " + name + " | grep -v grep | awk '{print $1}'");
        if (res == null) {
            PSLogger.fatal("failed to get process list f " + name);
            return null;
        }
        List<Integer> list = new ArrayList<Integer>();
        for (String s : res.getResult().split("\\n")) {
            if (s.trim().matches("\\d+")) {
                list.add(Integer.parseInt(s.trim()));
            }
        }
        PSLogger.debug("Processes '" + name + "':" + res);
        return list;
    }

    public void killProcess(int pid) {
        PSLogger.debug("kill process " + pid);
        ExecResult res = exec("kill -9 " + pid);
        PSLogger.debug(res.getResult());
        if (res.getExitCode() != 0) {
            PSLogger.error("can't kill " + pid);
        }
    }

    class MyUserInfo implements UserInfo {

        /**
         * @return Password
         */
        public final String getPassword() {
            return password;
        }

        /**
         * @param str String
         * @return true
         */
        public final boolean promptYesNo(final String str) {
            return true;
        }

        /**
         * @return null
         */
        public final String getPassphrase() {
            return null;
        }

        /**
         * @param message String
         * @return false
         */
        public final boolean promptPassphrase(final String message) {
            return false;
        }

        /**
         * @param message String
         * @return true
         */
        public final boolean promptPassword(final String message) {
            return true;
        }

        /**
         * @param message String
         */
        public final void showMessage(final String message) {
        }
    }

    public class ExecResult {
        private int exitcode;
        private String result;

        /**
         * Constructor
         *
         * @param exitcode
         * @param result
         */
        public ExecResult(final int exitcode, final String result) throws UnsupportedEncodingException {
            this.exitcode = exitcode;
            this.result = new String(result.getBytes(), "UTF-8");
        }

        /**
         * Get exit code
         *
         * @return exit code
         */
        public int getExitCode() {
            return exitcode;
        }

        /**
         * get command output
         *
         * @return output
         */
        public String getResult() {
            return result;
        }

        /**
         * Convert exit code and result to string
         *
         * @return exit code and result as string
         */
        public String toString() {
            return "Exit code is: " + exitcode + ", result is:\n" + result;
        }
    }


    public static class LogsChecker {
        private final static String LOG_PATTERN = "{log}";
        private final static String CLEAR_CMD = "cat /dev/null > " + LOG_PATTERN;
        private final static String CAT_CMD = "cat " + LOG_PATTERN;
        private final static String LS_CMD = "ls -la " + LOG_PATTERN;

        private RemoteLinServerUtils server;
        private String clearCmd;
        private String lsCmd;
        private String catCmd;
        private boolean isStarted;

        LogsChecker(RemoteLinServerUtils serv, String logs) {
            this.server = serv;
            clearCmd = CLEAR_CMD.replace(LOG_PATTERN, logs);
            catCmd = CAT_CMD.replace(LOG_PATTERN, logs);
            lsCmd = LS_CMD.replace(LOG_PATTERN, logs);
        }

        public synchronized void start() {
            if (isStarted) {
                PSLogger.debug(server.exec(lsCmd).getResult());
                return;
            }
            ExecResult res = server.exec(clearCmd);
            if (res == null || res.getExitCode() != 0) {
                PSLogger.error("Error while executing cmd '" + clearCmd + "'");
                return;
            }
            ExecResult ls = server.exec(lsCmd);
            PSLogger.debug(ls.getResult());
            isStarted = true;
        }

        /**
         * @return exception or null if all ok
         */
        public synchronized String checkLogsAndStop() {
            if (!isStarted) return null;
            ExecResult res = server.exec(catCmd);
            if (res == null) {
                PSLogger.error("Can't read file");
                return null;
            }
            if (res.getResult().trim().isEmpty()) {
                PSLogger.debug("Result is empty");
                isStarted = false;
                return null;
            }
            String sRes = res.getResult();
            PSLogger.debug(sRes);
            isStarted = false;
            return sRes;
        }
    }


}
