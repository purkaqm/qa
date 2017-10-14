package test.service;

import java.net.*;
import org.apache.log4j.*;
import selenium.driver.MySeleniumDriver;

public class BrowserHelper {

	private BrowserHelper(){}

	public static synchronized MySeleniumDriver startBrowser(String sServer,
			String contextUrl, String browserLauncher, Logger logger) throws MalformedURLException{
		URL context = new URL(contextUrl);

		String sServerName = sServer.split(":")[0];
		Integer sServerPort = Integer.valueOf(sServer.split(":")[1]);

		String contextServerString = null;
		if (context.getPort() < 0) {
			contextServerString = context.getProtocol() + "://" + context.getAuthority() + "/";
		} else {
			contextServerString = context.getProtocol() + "://" + context.getAuthority() + ":" + context.getPort() + "/";
		}
		if (browserLauncher.equals("*firefox")) {
			logger.info("Launching the Firefox browser");
		} else if (browserLauncher.equals("*iexplore")) {
			logger.info("Launching the Internet Explorer browser");
		} else if (browserLauncher.equals("*chrome")) {
			logger.info("Launching the Firefox browser in CHROME mode");
		} else {
			throw new IllegalStateException("Unknown browser launcher mode: "
					+ browserLauncher + ". Only *firefox, *iexplore and *chrome are supported.");
		}

		MySeleniumDriver driver = new MySeleniumDriver(sServerName, sServerPort, browserLauncher, contextServerString);

		driver.setSpeed("0");

		driver.start();
		driver.setTimeout("120000");
		driver.open("about:blank");
		driver.openWindow("about:blank","mainwindow");
		driver.selectWindow("mainwindow");
		driver.windowMaximize();

		logger.info("Opening the context and clearing the cookies");
		driver.open(context.getPath());
		driver.deleteAllVisibleCookies();
		driver.open(context.getPath());
		driver.waitForPageToLoad("120000");

		return driver;

//		this.mailSubj = this.mailSubj + " - " + driver.getText("dom=window.dojo.query('div#login > h5')[0]");

	}

}
