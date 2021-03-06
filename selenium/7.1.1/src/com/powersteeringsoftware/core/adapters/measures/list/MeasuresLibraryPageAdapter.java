package com.powersteeringsoftware.core.adapters.measures.list;

import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTestCaseException;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class MeasuresLibraryPageAdapter extends AbstractMeasureListPageAdapter{

	public MeasuresLibraryPageAdapter(DefaultSelenium _driver){
		super(_driver);
	}

	/**
	 * Check if we have URL in browser equals to the page URL for Measure
	 * Template:Library.
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 */
	public  boolean isLoadedPageMTLibrary() {
		return BasicCommons.isPageURLContains(CoreProperties.getURLMTLibrary());
	}
	/**
	 * Method click button "Add new" on the page "Measure Template Library"<br>
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 */
	public  void pushAddNewMT() {
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		seleniumDriver.click("dom= window.dojo.byId('Submit_0');");
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public  String getURLSubstring() {
		return CoreProperties.getURLMTAdd();
	}

	public  void doActionEdit(String mtName) {
		doActionClick(mtName);
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		String domClick = "dom= window.dojo.query('#ps_widget_psMenuItem_1')[0];";
		seleniumDriver.click(domClick);
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void clickDelete(String mtName){
		doActionClick(mtName);
		driver.click("dom= window.dojo.byId('ps_widget_psMenuItem_3');");
	}

	public void doActionDetach(String mtName) {
		clickDelete(mtName);
		//if (driver.isVisible("dom= window.dojo.byId('confirmDialogAttached')")) {
		driver.click("dom=window.dojo.query('div .dijitDialogPaneContent input[value=\"Commit\"]')[0]");//
//		} else if (driver.isVisible("dom= window.dojo.byId('confirmDialogAddHoc')")) {
//			driver.click("dom= window.dojo.byId('yesButtonAddHoc')");
//		}
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	public void doActionDelete(String mtName) {
		clickDelete(mtName);
		// select radiobutton "Delete All in popuped window
		driver.click("dom= window.dojo.query('input[type=radio]')[1]");
		// click "Submit" button in popuped window
		driver.click("dom=window.dojo.query('div .dijitDialogPaneContent input[value=\"Commit\"]')[0]");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	/**
	 * Assert if we loaded page Measure Template Library.
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"
	 *
	 *
	 * @throws PSTestCaseException
	 *             in case we are not on the page Measure template Library.
	 *
	 */
	public  void assertIsLoadedPageMTLibrary() {
		Assert.assertEquals(isLoadedPageMTLibrary(), true,
				"You are not on the page 'Measure Template Library'");
	}


	public String getByNameIdForM(String mtName) {
		String script = "var params = window.dojo.query('#PSTable .titleColumnValue a[href*="
				+ "AddEditMeasure.epage]');"
				+ "var reg = '"+ mtName +"';"
				+ "for(var i=0; i<params.length;i++){"
				+ " if(params[i].firstChild.nodeValue.indexOf(reg)!=-1) {"
				+ "  params[i].href; }}";

		String href = driver.getEval(script);

		if (href == null || href.equals("null")) {
			log.warn("Href for " + mtName + " is null.");
			return null;
		}

		String[] firstSplit = href.split("\\?");
		// System.out.println(firstSplit[1]);
		firstSplit = firstSplit[1].split("sp=");
		// System.out.println(firstSplit[1]);
		firstSplit = firstSplit[1].split("&");
		// System.out.println(firstSplit[0]);
		firstSplit = firstSplit[0].split("U");
		// System.out.println(firstSplit[1]);
		String mtId = firstSplit[1];

		return mtId;
	}

	public  void pushCreate() {
		driver.click("dom=window.dojo.query('[value=Add New]')[0];");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}
}
