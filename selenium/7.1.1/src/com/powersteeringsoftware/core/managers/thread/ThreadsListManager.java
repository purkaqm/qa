package com.powersteeringsoftware.core.managers.thread;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Class works only for 7.1
 *
 * @author selyaev_ag
 *
 */
public class ThreadsListManager {
	public ThreadsListManager(){
	}

	public void goToNewThreadPage(){
		TestSession.assertIsApplicationVerison71();
		navigateDiscussionListPage();
		pushAddNewDiscussion();
	}

	private void navigateDiscussionListPage() {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/Discussions/.test(tbLinks[i].href)) {tbLinks[i];break;}}";
		//if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Documents page link not found");
		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void pushAddNewDiscussion() {
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('AddDiscussionLink')");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

}
