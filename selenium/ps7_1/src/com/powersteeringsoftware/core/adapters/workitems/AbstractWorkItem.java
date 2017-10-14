package com.powersteeringsoftware.core.adapters.workitems;

import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.enums.WIToolbarLocators;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;
import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Wait;

public abstract class AbstractWorkItem {

	public AbstractWorkItem(){
	}

	/**
	 * Check if loaded page 'Work Item:Summary'
	 *
	 * @return true - if page has been loaded, false - otherwise
	 */
	public boolean isLoadedPageWISummary() {
		return BasicCommons.isPageURLContains(CoreProperties.getURlWISummary());
	}

	/**
	 * Navigate page "Work Item:Summary" for passed WI UID
	 *
	 * @param wiUid -
	 *            the work item UID
	 * @return true - if page has been loaded, false - otherwise
	 */
	public boolean navigatePageSummary(String wiUid) {
		StringBuilder wiSummaryPage = new StringBuilder("");
		wiSummaryPage.append(CoreProperties.getURLServerHost());
		wiSummaryPage.append(CoreProperties.getURLContext());
		wiSummaryPage.append(CoreProperties.getURlWISummary());
		wiSummaryPage.append("?sp=");
		wiSummaryPage.append(wiUid);

		DefaultSelenium seleniuDriver = SeleniumDriverSingleton.getDriver();

		seleniuDriver.open(wiSummaryPage.toString());

		seleniuDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		return isLoadedPageWISummary();
	}


	/**
	 * Navigate page 'Manage Measures"<br>
	 * Before using this method you must navigate summary page for any project.
	 *
	 * @return true - if loaded page 'Manage Measures', false - otherwise
	 */
	public void navigatePageManageMeasure() {
		//assertIsLoadedPageWISummary();
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();

		Wait presentWait = new ElementBecomePresentWaiter(WIToolbarLocators.MEASURES.getLocator());
		presentWait.wait("Element hasn't become present. Element locator = "+WIToolbarLocators.MEASURES.getLocator(),CoreProperties.getWaitForElementToLoad());

		seleniumDriver.mouseUp(WIToolbarLocators.MEASURES.getLocator());
		String query = "dom= var nodeLength = window.dojo.query('[id*=ps_widget_psMenuItem_]').length;" +
				" nodeLength = (nodeLength==0)?nodeLength:nodeLength-1; " +
				" window.dojo.byId('ps_widget_psMenuItem_'+nodeLength);";
		seleniumDriver.click(query);
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Navigate page 'Manage Measures' to the work item with id
	 *
	 * @param wiUid
	 * @return
	 */
	public void navigatePageManageMeasure(String wiUid){
		navigatePageSummary(wiUid);
		navigatePageManageMeasure();

	}

	/**
	 * Navigate page 'Work Break Down'
	 *
	 * @param wiUid
	 * @return
	 */
	public boolean navigatePageWBS(String wiUid){
		String wbsURL = "project/WBS.epage?sp=";
		SeleniumDriverSingleton.getDriver().open(wbsURL+wiUid);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		return true;
	}

	public void navigatePageIssues(String wiUid){
		navigatePageSummary(wiUid);
		SeleniumDriverSingleton.getDriver().click(WIToolbarLocators.ISSUES.getLocator());
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

}
