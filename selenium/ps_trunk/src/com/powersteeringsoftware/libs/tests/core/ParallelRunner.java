package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.CommandLineOptions;
import com.powersteeringsoftware.libs.util.ResinJMXManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.mail.PSResultProperties;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.testng.TestListenerAdapter;
import org.testng.xml.Parser;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.02.12
 * Time: 15:00
 */
public class ParallelRunner extends AbstractRunner {
    private List<Integer> counts = new ArrayList<Integer>();
    private Integer refreshingTimeout;
    private Boolean random;

    public static void main(String[] args) {
        ParallelRunner runner = new ParallelRunner();
        try {
            runner.setProperties(args);
            PSLogger.info(CoreProperties.getPropertiesString());
        } catch (Exception e) {
            e.printStackTrace();
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
        runner.start();
    }

    protected void setProperties(String[] args) {
        super.setProperties(args);
        CoreProperties.setBrowser(BrowserTypes.ZERO);
    }

    protected void setEmailProperties() {
        super.setEmailProperties();
        PSResultProperties.setType(PSResultProperties.Type.LOADING);
        PSResultProperties.SUBJECT_RESULT.setValue(PSResultProperties.SUBJECT_LOADING);
        if (refreshingTimeout != null)
            PSResultProperties.TEST_LOADING_REFRESH_TIMEOUT.appendValue(String.valueOf(refreshingTimeout) + "min");
    }

    @Override
    protected TestListenerAdapter getTLA() {
        if (tla == null) tla = new PSParallelListener();
        return tla;
    }

    @Override
    protected ParallelCLI getCLI() {
        if (cli == null)
            cli = new ParallelCLI();
        return (ParallelCLI) cli;
    }

    @Override
    protected void endTests() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        sendEmail();
        String title = "Loading AutoTests Result. PS" + TestSession.getAppVersion().getValue();
        addLinksAndTitleToLog(title);
        System.exit(EXIT_OK);
    }

    @Override
    protected void runTests() {
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        try {
            XmlSuite initSuite = new Parser(CoreProperties.getTestNGFile()).parseToList().get(0);
            List<XmlTest> initTests = new ArrayList<XmlTest>(initSuite.getTests());


            for (Integer c : counts) {
                XmlSuite suite = (XmlSuite) initSuite.clone();
                suite.setParameters(new HashMap());
                suite.setName(suite.getName() + "(count=" + c + ")");
                if (c > XmlSuite.DEFAULT_DATA_PROVIDER_THREAD_COUNT)
                    suite.setDataProviderThreadCount(c);
                List<XmlTest> tests = new ArrayList<XmlTest>();
                for (XmlTest t : initTests) {
                    XmlTest test = (XmlTest) t.clone();
                    test.setClasses(t.getClasses());
                    test.setName(test.getName() + "[" + c + "]");
                    test.addParameter("count", String.valueOf(c));
                    if (random != null) {
                        test.addParameter("random", String.valueOf(random));
                    }
                    if (refreshingTimeout != null) {
                        test.addParameter("refresh-timeout", String.valueOf(refreshingTimeout));
                    }
                    tests.add(test);
                }
                suite.setTests(tests);
                suites.add(suite);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
        for (int i = 0; i < suites.size(); i++) {
            List<XmlSuite> toRun = new ArrayList<XmlSuite>();
            toRun.add(suites.get(i));
            PSLogger.debug(toRun.get(0).toXml());
            testng.setXmlSuites(toRun);
            testng.run();
            if (i != suites.size() - 1)
                setup();
        }
    }

    @Override
    protected void initTests() {
        try {
            setup();
            PSResultProperties.load(CoreProperties.getMailerProperties());
        } catch (Exception e) {
            e.printStackTrace();
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
    }

    protected void setup() {
        try {
            super.setup();
            ResinJMXManager.refresh();
            BasicCommons.logIn();
            SeleniumDriverFactory.stopAllSeleniumDrivers();
        } catch (Exception e) {
            e.printStackTrace();
            PSLogger.fatal(e);
            System.exit(EXIT_FAIL);
        }
    }

    private class ParallelCLI extends CommandLineOptions {

        public static final String CLI_OPTION_COUNT = "count";
        public static final String CLI_OPTION_REFRESH_TIMEOUT = "timeout";
        public static final String CLI_OPTION_USE_RANDOM = "random";

        protected void setOptions() {
            super.setOptions();

            OptionBuilder.withArgName("true or false");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("use random (default false)");
            Option random = OptionBuilder.create(CLI_OPTION_USE_RANDOM);
            random.setRequired(false);
            options.addOption(random);


            OptionBuilder.withArgName("list");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("list of threads count (separated with ',')");
            Option count = OptionBuilder.create(CLI_OPTION_COUNT);
            count.setRequired(true);
            options.addOption(count);

            OptionBuilder.withArgName("integer");
            OptionBuilder.hasArg();
            OptionBuilder.withDescription("time for refreshing (in minutes)");
            Option timeout = OptionBuilder.create(CLI_OPTION_REFRESH_TIMEOUT);
            timeout.setRequired(false);
            options.addOption(timeout);
        }

        public void parse(String[] args) throws ParseException {
            super.parse(args);
            for (String count : cli.getOptionValue(CLI_OPTION_COUNT).split("\\s*,\\s*")) {
                if (!count.matches("\\d+"))
                    throw new ParseException("Incorrect value for " + CLI_OPTION_COUNT + ": " + count);
                counts.add(Integer.parseInt(count));
            }
            if (cli.hasOption(CLI_OPTION_REFRESH_TIMEOUT)) {
                try {
                    refreshingTimeout = Integer.parseInt(cli.getOptionValue(CLI_OPTION_REFRESH_TIMEOUT));
                } catch (Exception e) {
                    throw new ParseException(e.getMessage());
                }
            }
            if (cli.hasOption(CLI_OPTION_USE_RANDOM)) {
                try {
                    random = Boolean.parseBoolean(cli.getOptionValue(CLI_OPTION_USE_RANDOM));
                } catch (Exception e) {
                    throw new ParseException(e.getMessage());
                }
            }
        }

    }
}
