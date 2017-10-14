package selenium.models;

import java.io.*;

import selenium.driver.MySeleniumDriver;
import test.service.*;

public class BrowserModel {
	private MySeleniumDriver mydriver;

	public BrowserModel(String browserStartCommand) throws Exception {
		this.mydriver = new MySeleniumDriver(Config.getInstance().getSeleniumHost(), Config.getInstance().getSeleniumPort(), browserStartCommand, Config.getInstance().getContextServer());
		this.mydriver.setSpeed("0");
		this.mydriver.start();
		this.mydriver.setTimeout("120000");
		this.mydriver.open("about:blank");
		this.mydriver.openWindow("about:blank","mainwindow");
		this.mydriver.selectWindow("mainwindow");
		this.mydriver.windowMaximize();
		this.mydriver.open(Config.getInstance().getContextPath());
		this.mydriver.deleteAllVisibleCookies();
		this.mydriver.open(Config.getInstance().getContextPath());
		this.mydriver.waitForPageToLoad("120000");
//		if ((browserStartCommand.equals("*iexplore")) | (browserStartCommand.equals("*iehta"))) {
//			this.mydriver = new MySeleniumDriver(Config.getInstance().getSeleniumHost(), Config.getInstance().getSeleniumPort(), browserStartCommand, Config.getInstance().getContextServer());
//			this.mydriver.start();
//			this.mydriver.setTimeout("120000");
//			this.mydriver.open("about:blank");
//			this.mydriver.openWindow("about:blank","mainwindow");
//			this.mydriver.selectWindow("mainwindow");
//			this.mydriver.windowMaximize();
//			this.mydriver.open(Config.getInstance().getContextPath());
//			this.mydriver.waitForPageToLoad("120000");
//		} else if ((browserStartCommand.equals("*firefox")) | (browserStartCommand.equals("*chrome"))) {
//			this.mydriver = new MySeleniumDriver(Config.getInstance().getSeleniumHost(), Config.getInstance().getSeleniumPort(), browserStartCommand, Config.getInstance().getContextServer());
//			this.mydriver.start();
//			this.mydriver.setTimeout("120000");
//			this.mydriver.openWindow(Config.getInstance().getContextPath(),"mainwindow");
//			this.mydriver.selectWindow("mainwindow");
//			this.mydriver.waitForPageToLoad("120000");
//			this.mydriver.deleteAllVisibleCookies();
//			this.mydriver.open(Config.getInstance().getContextPath());
//			//this.mydriver.waitForPageToLoad("120000");
//		} else {
//			throw new IllegalArgumentException("Unknown browser launcher: " + browserStartCommand);
//		}
	}

	public MySeleniumDriver getDriver() {
		return mydriver;
	}

	public void openContextPath() throws IOException, Exception {
		mydriver.open(Config.getInstance().getContextPath());
		mydriver.waitForPageToLoad("60000");
	}

	public void stop() {
		mydriver.stop();
	}
}
