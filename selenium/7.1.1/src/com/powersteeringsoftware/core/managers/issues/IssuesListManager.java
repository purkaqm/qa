package com.powersteeringsoftware.core.managers.issues;

import org.apache.log4j.Logger;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class IssuesListManager {

	private Logger log = Logger.getLogger(IssuesListManager.class);

	public IssuesListManager(){
	}

	public void openEditNewIssuePage(){
		log.debug("Navigating to the Create New Issue page");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/Issues/.test(tbLinks[i].href)) {tbLinks[i];break;}}";

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		driver.click("dom=window.dojo.byId('AddIssueLink')");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

}
