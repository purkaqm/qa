package com.powersteeringsoftware.mail;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import org.apache.commons.cli.*;

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.mail.PSResultProperties.*;

/**
 * Manager for sending mail (see class Mail)
 */
public class PSMailerManager {

    private PSMail mail;

    private PSMailerManager(String prop) throws IOException {
        this();
        load(prop);
    }

    public PSMailerManager() {
        mail = new PSMail();
    }

    public void setMailProperties() {
        mail.setMailer(MAIL_SENDER.getValue());
        mail.addReciever(emails != null ? emails : MAIL_RECEIVER.getValue());
        mail.setMailHost(MAIL_HOST.getValue());
        mail.setContentType(getType().isHtmlType());
    }

    public void sendSingle() throws MessagingException {
        setMailProperties();
        mail.addBodyLine("");
        mail.addBodyLine(PSResultFormatter.formatMailBody());
        mail.addBodyLine("");
        mail.setSubject(getSubject(false, Boolean.parseBoolean(TEST_RESULT.getValue()), Long.parseLong(TEST_START_LONG.getValue())));
        PSLogger.info("send email");
        mail.send();
    }

    public void sendFiles() throws IOException, MessagingException {
        File logDir = new File(parentDir);
        if (!logDir.exists()) {
            throw new FileNotFoundException("Log path has not found");
        }
        List<File> all = LocalServerUtils.listFiles(logDir);
        PSLogger.info("There are " + all.size() + " files in " + logDir);
        List<File> files = LocalServerUtils.listFiles(logDir, MAIL_PROPERTIES_PREFIX.getValue());
        PSLogger.info("Files to parse (" + files.size() + "): " + files);
        boolean group = files.size() != 1;
        if (files.size() == 0) {
            throw new FileNotFoundException("There are not any files in dir : " + logDir.getAbsolutePath());
        }
        boolean result = true;
        Long start = null;
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            PSLogger.info("Parse file #" + (i + 1) + ":" + file.getAbsolutePath());
            if (!file.exists()) {
                PSLogger.debug("Can't find file " + file + " to process");
                continue;
            }
            load(file);
            result &= Boolean.parseBoolean(TEST_RESULT.getValue());
            Long s = Long.parseLong(TEST_START_LONG.getValue());
            if (start == null) {
                start = s;
            }
            start = Math.min(s, start);
            mail.addBodyLine("");
            mail.addBodyLine(PSResultFormatter.formatMailBody(!many && group ? "Test #" + (i + 1) + ": " : ""));
            mail.addBodyLine("");
            if (many) {
                mail.setSubject(getSubject(false, Boolean.parseBoolean(TEST_RESULT.getValue()), Long.parseLong(TEST_START_LONG.getValue())));
                PSLogger.info("send email");
                mail.send();
                mail.clear();
                LocalServerUtils.renameFile(MAIL_PROPERTIES_BACK.getValue(), file);
            }
        }
        if (!many) {
            mail.setSubject(getSubject(group, result, start));
            PSLogger.info("send group email");
            mail.send();
        }

        if (rename)
            LocalServerUtils.renameFiles(MAIL_PROPERTIES_BACK.getValue(), files);
    }

    private void test(String subject) throws MessagingException {
        mail.addBodyLine("");
        mail.addBodyLine("TEST");
        mail.addBodyLine("");
        mail.setSubject(subject);
        mail.send();
    }

    /**
     * format subject for letter
     *
     * @param group  - if true then group of tests will be send
     * @param result - if true - all tests success
     * @param start  - Long start time
     * @return String
     */
    private String getSubject(boolean group, boolean result, long start) {
        if (subj != null)
            SUBJECT_PREFIX.setValue(subj);
        if (SUBJECT_RESULT.getValue().isEmpty()) {
            if (Integer.parseInt(TEST_TEST_CASE_ALL_NUMBER.getValue()) == 0) {
                SUBJECT_RESULT.setValue(SUBJECT_NOTHING);
            } else if (result) {
                SUBJECT_RESULT.setValue(SUBJECT_SUCCESS);
            } else {
                SUBJECT_RESULT.setValue(SUBJECT_FAIL);
            }
        }
        StringBuffer subject = new StringBuffer();
        if (group) {
            subject.append(SUBJECT_PREFIX.getSurroundedValue());
            subject.append(SUBJECT_RESULT.getValue());
        } else {
            subject.append(ENVIRONMENT_PS_VERSION_SHORT.getSurroundedValue());

            subject.append(ENVIRONMENT_CLIENT_BROWSER_SHORT.getSurroundedValue());

            subject.append(SUBJECT_PREFIX.getSurroundedValue());

            subject.append(" ").append(SUBJECT_RESULT.getValue()).append(" ");
        }
        subject.append(new SimpleDateFormat(SUBJECT_DATE_FORMAT.getValue()).format(start));
        return subject.toString();
    }


    /**
     * @param args base url, subject, type to send
     * @throws Exception
     */
    private String parentDir;

    public static void main(String[] args) throws IOException, MessagingException {
        CoreProperties.loadProperties(false);
        PSMailerManager manager = new PSMailerManager(CoreProperties.getMailerProperties());
        if (System.getProperties().containsKey("TEST")) {
            manager.setMailProperties();
            manager.test(System.getProperty("TEST"));
            return;
        }
        //parse args:
        manager.parseArguments(args);

        try {
            manager.setMailProperties();
            manager.sendFiles();
        } catch (FileNotFoundException re) {
            PSLogger.warn(re.getMessage());
            System.out.println(re.getMessage());
        }
    }


    private static final String ARG_JOB_ID = "job";
    private static final String ARG_HELP = "help";
    private static final String ARG_MANY = "many";
    private static final String ARG_SUBJECT = "subject";
    private static final String ARG_TYPE = "type";
    private static final String ARG_EMAIL_LIST = "email";
    private static final String ARG_PARSE_DIR = "dir";
    private static final String ARG_RENAME = "rename";
    private boolean many;
    private boolean rename = true;
    private String subj;
    private String emails;

    private void parseArguments(String[] args) {
        Options options = new Options();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cli = null;
        addOption(options, ARG_JOB_ID, "job id or url with job id", "integer");
        addOption(options, ARG_SUBJECT, "subject", "email subject");
        addOption(options, ARG_TYPE, "type", "on of the following: " + Arrays.toString(Type.values()));
        addOption(options, ARG_EMAIL_LIST, "email list", "emails");
        addOption(options, ARG_PARSE_DIR, "directory name", "parent directory to parse properties files");
        addOption(options, ARG_RENAME, "boolean", "rename files if true");
        addOption(options, ARG_MANY, "send many emails");
        addOption(options, ARG_HELP, "help");

        try {
            BasicParser parser = new BasicParser();
            cli = parser.parse(options, args);
        } catch (ParseException pe) {
            formatter.printHelp("Please specify options:", options);
            System.exit(-1);
        }

        if (cli.hasOption(ARG_HELP)) {
            formatter.printHelp("Please specify options:", options);
            System.exit(0);
        }

        if (cli.hasOption(ARG_JOB_ID)) {
            //parse job id:
            String jobId;
            String val = cli.getOptionValue(ARG_JOB_ID);
            if (val.matches("\\d+")) {
                jobId = val;
            } else {
                if (!val.startsWith(CoreProperties.getHudsonUrl()))
                    throw new IllegalArgumentException("Incorrect hudson url specified: " + val);
                if (!val.matches(".*/\\d+/.*")) {
                    throw new IllegalArgumentException("Can't parse job id from specified url " + val);
                }
                jobId = val.replaceAll(".*/(\\d+)/.*", "$1");
            }
            PSLogger.info("Job id is " + jobId);
            ENVIRONMENT_TEST_URL_JOB_ID.setValue(jobId);
        }
        if (cli.hasOption(ARG_SUBJECT)) {
            // set subject:
            subj = cli.getOptionValue(ARG_SUBJECT);
            PSLogger.info("Subject is " + subj);
        }
        if (cli.hasOption(ARG_TYPE)) {
            setType(cli.getOptionValue(ARG_TYPE));
            PSLogger.info("Type is " + getType());
        }

        if (cli.hasOption(ARG_EMAIL_LIST)) {
            emails = cli.getOptionValue(ARG_EMAIL_LIST);
            PSLogger.info("Email list: " + emails);
        }
        if (cli.hasOption(ARG_PARSE_DIR)) {
            String _dir = cli.getOptionValue(ARG_PARSE_DIR);
            if (new File(_dir).exists()) {
                parentDir = _dir;
            } else {
                PSLogger.error("Incorrect parse dir specified");
                System.exit(-2);
            }
        } else {
            parentDir = CoreProperties.getLogMainPath();
        }
        PSLogger.info("Directory for parse: " + parentDir);
        this.many = cli.hasOption(ARG_MANY);
        if (cli.hasOption(ARG_RENAME)) {
            rename = Boolean.parseBoolean(cli.getOptionValue(ARG_RENAME));
        }

    }


    private void addOption(Options options, String name, String argName, String desc) {
        OptionBuilder.withArgName(argName);
        OptionBuilder.hasArg();
        OptionBuilder.withDescription(desc);
        Option opt = OptionBuilder.create(name);
        opt.setRequired(false);
        options.addOption(opt);
    }

    private void addOption(Options options, String name, String desc) {
        Option opt = new Option(name, desc);
        opt.setRequired(false);
        options.addOption(opt);
    }

}
