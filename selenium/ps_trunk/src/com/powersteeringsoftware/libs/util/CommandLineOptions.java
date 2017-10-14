package com.powersteeringsoftware.libs.util;

import org.apache.commons.cli.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.02.12
 * Time: 16:51
 */
public class CommandLineOptions {
    /**
     * Command Line Option: points the core properties file and name
     */
    public static final String CLI_OPTION_CORE_PROPERTIES = "coreproperties";
    public static final String CLI_OPTION_RESTORE_DB = "restore";
    public static final String CLI_OPTION_RESTART_RESIN = "restart";
    public static final String CLI_OPTION_KILL_RESIN = "kill";
    public static final String CLI_OPTION_TEST_NG_FILE = "testng";
    public static final String CLI_OPTION_JOB_ID = "job";
    public static final String CLI_OPTION_EMAIL = "email";
    public static final String CLI_OPTION_EMAIL_SUBJECT_PREFIX = "subject";
    public static final String CLI_OPTION_USE_FF_PROFILE = "ffp";

    public static final String CLI_OPTION_CHROME_DRIVER = "driver";

    protected Options options;
    protected CommandLine cli;

    public CommandLineOptions() {
        setOptions();
    }

    public String getCorePropertiesFile() {
        return cli.getOptionValue(CLI_OPTION_CORE_PROPERTIES);
    }

    /**
     * getDoRestoreOption
     *
     * @return false by default
     */
    public boolean getDoRestoreOption() {
        if (!cli.hasOption(CLI_OPTION_RESTORE_DB)) return false;
        return cli.getOptionValue(CLI_OPTION_RESTORE_DB).contains("true");
    }

    public boolean getDoKillOption() {
        if (!cli.hasOption(CLI_OPTION_KILL_RESIN)) return false;
        return cli.getOptionValue(CLI_OPTION_KILL_RESIN).contains("true");
    }

    /**
     * getDoRestartOption
     *
     * @return false by default
     */
    public boolean getDoRestartOption() {
        if (!cli.hasOption(CLI_OPTION_RESTART_RESIN)) return false;
        return cli.getOptionValue(CLI_OPTION_RESTART_RESIN).contains("true");
    }


    public String getTestSuiteConfig() {
        if (!cli.hasOption(CLI_OPTION_TEST_NG_FILE)) return null;
        return cli.getOptionValue(CLI_OPTION_TEST_NG_FILE);
    }

    public Integer getJobId() {
        if (!cli.hasOption(CLI_OPTION_JOB_ID)) return null;
        String id = cli.getOptionValue(CLI_OPTION_JOB_ID);
        if (id.matches("\\d+")) {
            return Integer.parseInt(id);
        }
        return null;
    }

    public Boolean getDoEmail() {
        if (!cli.hasOption(CLI_OPTION_EMAIL)) return null;
        String emails = cli.getOptionValue(CLI_OPTION_EMAIL);
        if (emails.contains("@"))
            return true; // if emails specified then send emails, otherwise let email-deamon to do it
        return Boolean.parseBoolean(emails);
    }

    public String getEmailList() throws ParseException {
        if (!cli.hasOption(CLI_OPTION_EMAIL)) return null;
        String emails = cli.getOptionValue(CLI_OPTION_EMAIL);
        if (!emails.contains("@")) return null;
        StringBuffer res = new StringBuffer();
        for (String s : cli.getOptionValue(CLI_OPTION_EMAIL).split("\\s*,\\s*")) {
            if (!s.matches(".+@.+")) throw new ParseException("Incorrect value for " + CLI_OPTION_EMAIL + ": " + s);
            res.append(s).append(",");
        }
        return res.substring(0, res.length() - 1);
    }

    public String getEmailPrefix() {
        if (!cli.hasOption(CLI_OPTION_EMAIL_SUBJECT_PREFIX)) return null;
        return cli.getOptionValue(CLI_OPTION_EMAIL_SUBJECT_PREFIX);
    }

    public String getWebDriverFilename() {
        if (!cli.hasOption(CLI_OPTION_CHROME_DRIVER)) return null;
        return cli.getOptionValue(CLI_OPTION_CHROME_DRIVER);
    }

    public boolean useFFProfile() {
        return cli.hasOption(CLI_OPTION_USE_FF_PROFILE);
    }

    protected void setOptions() {
        options = new Options();
        OptionBuilder.withArgName("file");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("use given properties file (mandatory)");
        Option propFile = OptionBuilder.create(CLI_OPTION_CORE_PROPERTIES);
        propFile.setRequired(true);
        options.addOption(propFile);

        OptionBuilder.withArgName("true or false");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("restore db option (default false)");
        Option restore = OptionBuilder.create(CLI_OPTION_RESTORE_DB);
        restore.setRequired(false);
        options.addOption(restore);

        OptionBuilder.withArgName("true or false");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("restart resin option (default false)");
        Option restart = OptionBuilder.create(CLI_OPTION_RESTART_RESIN);
        restart.setRequired(false);
        options.addOption(restart);

        OptionBuilder.withArgName("true or false");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("kill resin process option (default false)");
        Option kill = OptionBuilder.create(CLI_OPTION_KILL_RESIN);
        kill.setRequired(false);
        options.addOption(kill);

        OptionBuilder.withArgName("file");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("use given testng xml file");
        Option testNGFile = OptionBuilder.create(CLI_OPTION_TEST_NG_FILE);
        testNGFile.setRequired(false);
        options.addOption(testNGFile);

        OptionBuilder.withArgName("integer");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("hudson job id. if specified send email after tests");
        Option jobId = OptionBuilder.create(CLI_OPTION_JOB_ID);
        jobId.setRequired(false);
        options.addOption(jobId);


        OptionBuilder.withArgName("address");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("email list to send test result or 'false' for disabling. separator for list is ','");
        Option email = OptionBuilder.create(CLI_OPTION_EMAIL);
        email.setRequired(false);
        options.addOption(email);

        OptionBuilder.withArgName("prefix");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("result email subject prefix");
        Option prefix = OptionBuilder.create(CLI_OPTION_EMAIL_SUBJECT_PREFIX);
        prefix.setRequired(false);
        options.addOption(prefix);

        OptionBuilder.withArgName("filename");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("file name for web-driver");
        Option dr = OptionBuilder.create(CLI_OPTION_CHROME_DRIVER);
        dr.setRequired(false);
        options.addOption(dr);

        Option ffProfile = new Option(CLI_OPTION_USE_FF_PROFILE, "if specified use ff profile");
        ffProfile.setRequired(false);
        options.addOption(ffProfile);

    }

    /**
     * Parse CLI string
     * print help throw if options are incorrect
     *
     * @param args - input array
     * @throws ParseException
     */
    public void parse(String[] args) throws ParseException {
        try {
            BasicParser parser = new BasicParser();
            cli = parser.parse(options, args);
        } catch (ParseException pe) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Please specify options:", options);
            throw pe;
        }
    }

}
