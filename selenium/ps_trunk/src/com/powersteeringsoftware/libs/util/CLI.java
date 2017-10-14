package com.powersteeringsoftware.libs.util;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

/**
 * Parse command line arguments
 *
 * @author selyaev_ag
 */
public class CLI extends CommandLineOptions {

    public static final String CLI_OPTION_BROWSER = "browser";
    public static final String CLI_OPTION_SELENIUM_RC = "userc";
    public static final String CLI_OPTION_TEST_NG_EXCLUDE = "exclude";
    public static final String CLI_OPTION_TEST_NG_INCLUDE = "include";


    public String getBrowser() {
        if (!cli.hasOption(CLI_OPTION_BROWSER)) return null;
        return cli.getOptionValue(CLI_OPTION_BROWSER);
    }


    /**
     * getUseRcOption
     *
     * @return true by default
     */
    public boolean getUseRcOption() {
        if (!cli.hasOption(CLI_OPTION_SELENIUM_RC)) return true;
        return !cli.getOptionValue(CLI_OPTION_SELENIUM_RC).contains("false");
    }

    public String getTestsToExclude() {
        if (!cli.hasOption(CLI_OPTION_TEST_NG_EXCLUDE)) return "";
        return cli.getOptionValue(CLI_OPTION_TEST_NG_EXCLUDE);
    }

    public String getTestsToInclude() {
        if (!cli.hasOption(CLI_OPTION_TEST_NG_INCLUDE)) return "";
        return cli.getOptionValue(CLI_OPTION_TEST_NG_INCLUDE);
    }

    protected void setOptions() {
        super.setOptions();
        OptionBuilder.withArgName("brand");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("browser brand to be used (FF* or IE*) (if not specified than not run tests))");
        Option browser = OptionBuilder.create(CLI_OPTION_BROWSER);
        browser.setRequired(false);

        options.addOption(browser);
        OptionBuilder.withArgName("true or false");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("use selenium-rc or selenium-web-driver (default selenium-rc)");
        Option useRc = OptionBuilder.create(CLI_OPTION_SELENIUM_RC);
        useRc.setRequired(false);
        options.addOption(useRc);


        OptionBuilder.withArgName("list");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("list of tests to exclude (separated with ',')");
        Option exclude = OptionBuilder.create(CLI_OPTION_TEST_NG_EXCLUDE);
        exclude.setRequired(false);
        options.addOption(exclude);

        OptionBuilder.withArgName("list");
        OptionBuilder.hasArg();
        OptionBuilder.withDescription("list of tests to include (separated with ',')");
        Option include = OptionBuilder.create(CLI_OPTION_TEST_NG_INCLUDE);
        include.setRequired(false);
        options.addOption(include);
    }
}
