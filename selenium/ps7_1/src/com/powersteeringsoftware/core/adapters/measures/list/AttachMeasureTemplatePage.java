package com.powersteeringsoftware.core.adapters.measures.list;

import org.testng.Assert;
import com.powersteeringsoftware.core.enums.PowerSteeringVersions;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.thoughtworks.selenium.DefaultSelenium;

public class AttachMeasureTemplatePage extends AbstractMeasureListPageAdapter{

	public AttachMeasureTemplatePage(DefaultSelenium _driver){
		super(_driver);
	}

	public boolean isLoadedPageAttachMT() {
		try {
			String versionedHeader = "";
			if(TestSession.getApplicationVersionAsString().contains(PowerSteeringVersions.APPLICATION_VERSION_7_1.getVersion())){
				versionedHeader = "/\\s*Attach Library Measure\\s*/";
			} else if (TestSession.getApplicationVersionAsString().contains(PowerSteeringVersions.APPLICATION_VERSION_7_0.getVersion())){
				versionedHeader = "/\\s*Attach Measure Template\\s*/";
			}

			String script = "var params = window.dojo.query('em'); "
					+ " var regexp = "+versionedHeader+"; "
					+ " regexp.test(params[0].firstChild.nodeValue); ";
			return new Boolean(SeleniumDriverSingleton.getDriver().getEval(
					script));
		} catch (Exception e) {
			return false;
		}
	}

	public  void doActionEdit(String mtName) {
		doActionClick(mtName);
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		String domClick = "dom= window.dojo.query('#ps_widget_psMenuItem_1')[0];";
		seleniumDriver.click(domClick);
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public  void assertIsLoadedPageAttachMT() {
		Assert.assertEquals(isLoadedPageAttachMT(), true, "You are not on the page 'Attach MT'");
	}

	public void doActionDetach(String mtName) {
		doActionClick(mtName);
		driver.click("dom= window.dojo.byId('ps_widget_psMenuItem_3');");
		driver.click("dom= window.dojo.query('input[type=radio]')[0]");
		driver.click("dom=window.dojo.query('div .dijitDialogPaneContent input[value=\"Commit\"]')[0]");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void doActionDelete(String mtName) {
		doActionClick(mtName);
		driver.click("dom= window.dojo.byId('ps_widget_psMenuItem_3');");
		// select radiobutton "Delete All in popuped window
		driver.click("dom= window.dojo.query('input[type=radio]')[1]");
		// click "Submit" button in popuped window
		driver.click("dom=window.dojo.query('div .dijitDialogPaneContent input[value=\"Commit\"]')[0]");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public  String getURLSubstring() {
		return CoreProperties.getURLWIMeasureInstance();
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

	public void pushCreate(){
		throw new RuntimeException("Is not implemented!");
	}

}
