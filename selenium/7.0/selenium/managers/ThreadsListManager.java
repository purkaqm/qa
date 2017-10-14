package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;

public class ThreadsListManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public ThreadsListManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void goToNewThreadPage() throws InterruptedException{
		logger.info("Starting new thread");

		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/Discussions/.test(tbLinks[i].href)) {tbLinks[i];break;}}";

		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Documents page link not found");
		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");

		driver.click("dom=window.dojo.byId('AddDiscussionLink')");
		driver.waitForPageToLoad("30000");

	}

	public void goToThread(String threadName){
		//TODO not implemented yet
	}

}
