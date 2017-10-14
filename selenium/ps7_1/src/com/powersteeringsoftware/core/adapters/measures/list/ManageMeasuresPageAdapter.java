package com.powersteeringsoftware.core.adapters.measures.list;

import org.testng.Assert;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.StrUtil;
import com.thoughtworks.selenium.DefaultSelenium;

/*
 * Page "Manage Measures" adapter
 */
public class ManageMeasuresPageAdapter extends AbstractMeasureListPageAdapter {

	public ManageMeasuresPageAdapter(DefaultSelenium _driver){
		super(_driver);
	}

	/**
	 * Check if page 'Manage Measures' has been loaded.<br>
	 * You mustn't use selenium method waitForPageToLoad() after this method.
	 *
	 * @return true - if page 'Manage Measures' has been loaded, false -
	 *         otherwise.
	 */
	public  boolean isLoadedPageManageMeasure() {
		// log.debug("isLoadedPageManageMeasure:"+CoreProperties.getPageTitleManageM());
		// return
		// SeleniumDriverSingleton.getDriver().isTextPresent(CoreProperties.getPageTitleManageM());
		try {
			String script = "var params = window.dojo.query('em'); "
					+ " var regexp = /Manage Measure/; "
					+ " regexp.test(params[0].firstChild.nodeValue); ";
			return new Boolean(SeleniumDriverSingleton.getDriver().getEval(
					script));
		} catch (Exception e) {
			return false;
		}
	}

	public  String getURLSubstring() {
		return CoreProperties.getURLWIMeasureInstance();
	}

	/**
	 * Assert if page 'Manage Measures' has been loaded<br>
	 * Method asserts isLoadedPageManageMeasure().<br>
	 * You mustn't use selenium method waitForPageToLoad() after this method.
	 */
	public  void assertIsLoadedPageManageMeasure() {
		Assert.assertEquals(isLoadedPageManageMeasure(), true,
				"You are not on the page 'Manage Measures'");
	}


	public void doActionEdit(String mtName) {
		doActionClick(mtName);
		String domClick = "dom= window.dojo.query('#ps_widget_psMenuItem_2')[0];";
		driver.click(domClick);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * You can invoke this method only on the page Manage Measure
	 */
	public  void pushAttach() {
		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
		seleniumDriver.click("dom= window.dojo.byId('Submit_3');");
		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void doActionDetach(String mtName) {
		doActionClick(mtName);
		driver.click("dom= window.dojo.query('#ps_widget_psMenuItem_4')[0];");
		//if (driver.isVisible("dom= window.dojo.byId('confirmDialogAttached')")) {
		//	driver.click("dom= window.dojo.byId('yesButtonAttached')");
		//} else if (driver.isVisible("dom= window.dojo.byId('confirmDialogAddHoc')")) {
			driver.click("dom= window.dojo.byId('yesButtonAddHoc')");
		//}
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public  void doActionDelete(String mtName) {
		doActionClick(mtName);
		driver.click("dom= window.dojo.query('#ps_widget_psMenuItem_4')[0];");
		//if (driver.isVisible("dom= window.dojo.byId('confirmDialogAttached')")) {
			driver.click("dom= window.dojo.byId('yesButtonAttached')");
//		} else if (driver.isVisible("dom= window.dojo.byId('confirmDialogAddHoc')")) {
//			driver.click("dom= window.dojo.byId('yesButtonAddHoc')");
//		}
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Click button "Add new"<br>
	 * Before using this method you should navigate page 'Manage Measures'.<br>
	 * You mustn't use selenium method waitForPageToLoad() after this method.
	 */
	public  void pushCreate() {
		driver.click("dom=window.dojo.query('[value=Define New]')[0];");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public String getByNameIdForM(String mtName) {
		String formatedName = StrUtil.quoteReqExp(mtName);
		log.debug("Formated name for " + mtName + " is " + formatedName);
		String hrefUrl = new String();

		hrefUrl = CoreProperties.getURLWIMeasureInstance();


		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();

		String script = "var params = window.dojo.query('#PSTable .titleColumnValue a[href*="
				+ hrefUrl
				+ "]');"
				+ "var reg = /\\s*"
				+ formatedName
				+ "\\s*/;"
				+ "for(var i=0; i<params.length;i++){"
				+ "if(reg.test(params[i].firstChild.nodeValue)) {"
				+ "        params[i].href;" + "    }" + "}";

		String href = seleniumDriver.getEval(script);

		if (href == null || href.equals("null")) {
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

}
