package com.powersteeringsoftware.core.util;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Parse command line arguments
 *
 * @author selyaev_ag
 *
 */
public class CLI {

	/**
	 * Command Line Option: points the core properties file and name
	 */
	public static String CLI_OPTION_CORE_PROPERTIES = "coreproperties";
	public static String CLI_OPTION_CORE_PROPERTIES_SHORT = "cp";

	/**
	 * Command Line Option: points the testng file location and name
	 */
	public static String CLI_OPTION_TESTNG = "testng";
	public static String CLI_OPTION_TESTNG_SHORT = "tng";

	private String[] console_arguments;
	private String corePropertiesFile;
	private Options options;

	public CLI(String[] args) {
		setConsoleArguments(args);
		corePropertiesFile = "";
		options = new Options();
		setOptions();
	}

	public void setConsoleArguments(String[] args) {
		console_arguments = args.clone();
	}

	public String[] getConsoleArguments() {
		return console_arguments.clone();
	}

	public String getCorePropertiesFile(){
		return (null==corePropertiesFile)?"":corePropertiesFile;
	}

	private void setCorePropertieFile(String corePropFileName){
		this.corePropertiesFile = corePropFileName;
	}

	private Option setOptionCorePropFile(){
		Option optionCorePropFile = new Option(CLI_OPTION_CORE_PROPERTIES,
				CLI_OPTION_CORE_PROPERTIES_SHORT,
				true,
				"core property file name");
		optionCorePropFile.setRequired(true);
		optionCorePropFile.setOptionalArg(true);
		return optionCorePropFile;
	}

	private void setOptions(){
		options.addOption(setOptionCorePropFile());
	}

	/**
	 * Parse CLI string and return true if parsing ended successfully or false
	 * in other case
	 *
	 * @return true - parsing ended successfully , false - in other case
	 */
	public boolean parse() {
		try {

			BasicParser parser = new BasicParser();

			CommandLine cli = parser.parse(options, this.getConsoleArguments());

			Option[] options2 = cli.getOptions();

			for (int i = 0; i < options2.length; i++) {
				if (options2[i].getOpt().equals(CLI_OPTION_CORE_PROPERTIES)){
					setCorePropertieFile(options2[i].getValue(0));
				}
			}

		} catch (ParseException pe) {
			return false;
		}
		return true;
	}
}
