package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;

public class LoginManager{

	private MySeleniumDriver driver;
	private Logger logger;

	public LoginManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public String login(String userName, String password) throws InterruptedException {
		String pageTitle = null;
		String versionString = null;

		pageTitle = driver.getTitle();
		if (!(pageTitle.contains("Login")))
			throw new IllegalStateException("Wrong page: Login page expected but found " + pageTitle);

		versionString = driver.getText("dom=window.dojo.query('div#login > h5')[0]");

		logger.info("Logging in as " + userName + "/" + password);
		driver.type("dom=window.dojo.byId('user')", userName);
		driver.type("dom=window.dojo.byId('pass')", password);
		driver.click("dom=window.dojo.byId('Submit_0')");
		driver.waitForPageToLoad("300000");

		pageTitle = driver.getTitle();
		if (!(pageTitle.contains("Home")))
			throw new IllegalStateException("Wrong page: Home page expected but found " + pageTitle);

		return versionString;
	}

}
