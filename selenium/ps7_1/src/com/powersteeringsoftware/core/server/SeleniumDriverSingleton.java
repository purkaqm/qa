package com.powersteeringsoftware.core.server;

import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.enums.BrowserTypes;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;


/**
 * @author  selyaev_ag
 */
public class SeleniumDriverSingleton {

	private static DefaultSelenium seleniumDriver = null;
	private static Logger log = Logger.getLogger(SeleniumDriverSingleton.class.getName());

	private SeleniumDriverSingleton() {
	}

	public static DefaultSelenium getDriver() {
		if(null==seleniumDriver){
			log.warn("Selenium driver should be initializated.");
			init(CoreProperties.getSeleniumServerHostname(),
					new Integer(CoreProperties.getSeleniumServerPortAsString()),
					CoreProperties.getDefaultBrowser(),
					CoreProperties.getURLServerHost(),
					CoreProperties.getURLContext());
		}

		return seleniumDriver;
	}

	/**
	 * Setup Selenium, start and open browser
	 * @param server
	 * @param port
	 * @param browser
	 * @param url
	 * @param context
	 * @return
	 */
	public static DefaultSelenium init(String server, int port,
			String browser, String url, String context) {
		if (null == seleniumDriver) {
			log.info("Parameters for testing have passed. Parameters are server:"
					+server+"; server:"+port+"; browser:"+browser+" url:"+url+"; context:"+context);
			seleniumDriver = new DefaultSelenium(server, port, browser, url+context);
		}else{
			log.info("Driver has set up already and can't be changed.");
		}

		seleniumDriver.start();
		seleniumDriver.setTimeout(String.valueOf(CoreProperties.getWaitForElementToLoad()*2));
		seleniumDriver.open("");
		seleniumDriver.deleteAllVisibleCookies();
		seleniumDriver.open("");
		seleniumDriver.setTimeout(CoreProperties.getWaitForElementToLoadAsString());
		return seleniumDriver;
	}

	/**
	 * Stop selenium and close browser
	 */
	public static void stop(){
		seleniumDriver.close();
		seleniumDriver.stop();
	}

	/**
	 * Set up and return Selenium driver.<br>
	 * Driver is setuped using CoreProperties
	 * @return DefaultSelenium
	 */
	public static DefaultSelenium setDriverUsingCoreProperties(){

		log.info("Selenium driver initialization started.");
		SeleniumDriverSingleton.init(CoreProperties.getSeleniumServerHostname(),
				CoreProperties.getSeleniumServerPort(),
				CoreProperties.getDefaultBrowser(),
				CoreProperties.getURLServerHost(),
				CoreProperties.getURLContext());
		log.info("Selenium driver is initialized.");

		return getDriver();
	}

	public static boolean isIE(){
		if( CoreProperties.getDefaultBrowser().equals(BrowserTypes.BROWSER_IE.getBrowser()) ||
		 CoreProperties.getDefaultBrowser().equals(BrowserTypes.BROWSER_IE_IEHTA.getBrowser()) ){
			return true;
		}
			return false;
	}

	public static boolean isFF(){
		if( CoreProperties.getDefaultBrowser().equals(BrowserTypes.BROWSER_FIREFOX.getBrowser()) ||
		 CoreProperties.getDefaultBrowser().equals(BrowserTypes.BROWSER_FIREFOX_CHROME.getBrowser()) ){
			return true;
		}
			return false;
	}

}
