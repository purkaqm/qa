package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;

public class IssuesListManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public IssuesListManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void goToNewIssuePage() throws InterruptedException{
		logger.info("Navigating to the Create New Issue page");

		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/Issues/.test(tbLinks[i].href)) {tbLinks[i];break;}}";

		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Issues page link not found");
		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");

		driver.click("dom=window.dojo.byId('AddIssueLink')");
		driver.waitForPageToLoad("30000");

	}

	public void goToIssue(String threadName){
		//TODO not implemented yet
	}

}
