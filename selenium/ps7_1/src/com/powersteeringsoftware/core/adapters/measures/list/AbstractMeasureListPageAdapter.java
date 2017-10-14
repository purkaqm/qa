package com.powersteeringsoftware.core.adapters.measures.list;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.AbstractMeasureEditPageAdapter;
import com.powersteeringsoftware.core.tc.PSTestCaseException;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.StrUtil;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Utility class for executing common procedures for page Measure
 * Template:Library , such as:<br> - create measure template,<br> - open for
 * editing "Measure Template:Library",<br> - etc<br>
 *
 */
public abstract class AbstractMeasureListPageAdapter {
	protected Logger log = Logger.getLogger(AbstractMeasureListPageAdapter.class);

	DefaultSelenium driver;

	public AbstractMeasureListPageAdapter(DefaultSelenium _driver) {
		driver = _driver;
	}

//	/**
//	 * Page types with measure lists for managing with measures or measure
//	 * templates.
//	 */
//	public enum MeasureListType {
//		MANAGE_MEASURE(0),
//		ATTACH_MEASURE_TEMPLATE(1),
//		MEASURE_TEMPLATE_LIBRARY(2);
//
//		int index;
//
//		MeasureListType(int indx) {
//			index = indx;
//		}
//
//		public int getIndex() {
//			return index;
//		}
//	}

//	/**
//	 * Load page "Measure Template:Library" using main menu
//	 *
//	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
//	 * waitForPageToLoad(...) after this method.<br>
//	 * Before using this method you have to navigate to the page "Measure
//	 * Template: Library" Description generated by default.<br>
//	 *
//	 * @param seleniumDriver -
//	 *            selenium driver for perfoming selenium commands
//	 */
//	public boolean navigatePageMTLibrary() {
//		DefaultSelenium seleniumDriver = SeleniumDriverSingleton.getDriver();
//		seleniumDriver.click("dom= window.dojo.byId('BrowseShow');");
//		seleniumDriver.click("link=Measures Library");
//		seleniumDriver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
//		return isLoadedPageMTLibrary();
//	}



	/**
	 * Check if exist any measure template or measure with a passed name
	 * substring
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 * @param mtNameSubstr
	 * @param measureType -
	 *            measure page type, 0 is a measure, other values are measure
	 *            template
	 * @return true - if measure template with a passed substring exist, false -
	 *         in other cases
	 */
	public  boolean existBySubstring(String mtNameSubstr) {
		String mtId = getByNameIdForM(mtNameSubstr);

		return StringUtils.isEmpty(mtId) ? false : true;
	}

	/**
	 * Get ID for the measure template.<br>
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 * @param mtName -
	 *            Measure Template name
	 * @param strong -
	 *            this flag in future will separate mtName as a name and mtName
	 *            as a substring of the name.
	 *
	 * @return Measure Template ID or null - if something is wrong
	 */
	public abstract  String getByNameIdForM(String mtName);


	/**
	 * Get count of measure templates with name includes substring mtName.<br>
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 * @param mtName -
	 *            string for searching measure templates. If Measure Template
	 *            contains substring mtName in it name this MT will be counted
	 * @return count of measure templates that contain substring mtName
	 *
	 */
	public  int getByNameCountForM(String mtName) {
		String formatedName = StrUtil.quoteReqExp(mtName);
		log.debug("Formated name for " + mtName + " is " + formatedName);
		String script = " var params = window.dojo.query('#PSTable .titleColumnValue a[href*="
				+ CoreProperties.getURLMTAdd()
				+ "]'); "
				+ " var reg = /\\s*"
				+ formatedName
				+ "\\s*/; "
				+ " var count = 0; "
				+ " for(var i=0; i<params.length;i++){ "
				+ "  if(reg.test(params[i].firstChild.nodeValue)){ count++; } "
				+ "  } " + " count; ";
		return new Integer(driver.getEval(script));
	}



	/**
	 * Click on the drop-down icon for deleting, editing, view properties<br>
	 * Selecting "Delete", "Edit" or another operation must be done separetly
	 * after calling this method.<br>
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 * @param mName -
	 *            the name or a substring that is contained in measure or
	 *            measure template name. If we have several measure template
	 *            names with such substring result can surprise you. So use this
	 *            method carefully
	 *
	 */
	// public  boolean doActionMTClick(String mtName){
	public  boolean doActionClick(String mName) {
		Assert.assertEquals(existBySubstring(mName), true,
				"Can't select unexisted measure template " + mName);

		boolean result = true;

		String formatedName = StrUtil.quoteReqExp(mName);
		log.debug("Formated M name " + mName + " for RegExp evaluation is "
				+ formatedName);

		String hrefSubstr = getURLSubstring();

		String valueQuery = "dom= "
				+ " var params = window.dojo.query('#PSTable .titleColumnValue a[href*="
				+ hrefSubstr
				+ "]');"
				+ " var param; "
				+ " var reg = /\\s*"
				+ formatedName
				+ "\\s*/; "
				+ " for(var i=0; i<params.length;i++){ "
				+ " 	if(reg.test(params[i].firstChild.nodeValue)) { "
				+ " 		param = params[i]; "
				+ " } "
				+ " } "
				+ " window.dojo.query('a',param.parentNode)[1].attributes['id']; ";

		log.debug("Table query: " + valueQuery);
		String uid = driver.getValue(valueQuery);
		String clickPopup = "dom= window.dojo.query('#" + uid + "')[0]; ";
		driver.click(clickPopup);
		driver.mouseUp(clickPopup);
		log.debug("Action has been clicked.");
		return result;
	}

	/**
	 * Get unique substring of URL for page with measure (template or instance) list
	 *
	 * @return
	 */
	public abstract String getURLSubstring();



	/**
	 * Copy measure template
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" Description generated by default.<br>
	 *
	 * @param name -
	 *            the measure template name or a substring that is contained in
	 *            measure template name. If we have several measure template
	 *            names with such substring - result can surprise you. So use
	 *            this method carefully
	 *
	 * @param newName -
	 *            the new name for measure template
	 */
	public void doActionCopy(String name, String newName, AbstractMeasureEditPageAdapter _adapter ) {
		doActionClick(name);
		if ((StringUtils.isEmpty(name) || StringUtils.isEmpty(newName)))
			throw new PSTestCaseException(
					"Measure name and new name can't be null or empty.");
		driver.click("dom= window.dojo.query('#ps_widget_psMenuItem_2')[0];");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		_adapter.setName(newName);
		_adapter.submitChangesWithUpperButtonForEditedM();
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


//	/**
//	 * Create Measure Template with only filled fields: name, description, data
//	 * collection and indicator type.<br>
//	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
//	 * waitForPageToLoad(...) after this method.<br>
//	 * Before using this method you have to navigate to the page "Measure
//	 * Template: Library" Description generated by default.<br>
//	 *
//	 * @param name -
//	 *            the name for new measure template
//	 * @param dataCollection -
//	 *            the data collection of new measure template. If 0 - manual, 1 -
//	 *            formula.
//	 * @param indicatorType -
//	 *            the indicator type of new measure template. If 0 - manual, 1 -
//	 *            formula.
//	 * @return created measure/measure template name
//	 * @deprecated
//	 */
//	public  String createSimpleM(String mtName, String mtDescription,
//			int dataCollection, int indicatorType) {
//
//		assertIsLoadedPageMeasureLists();
//
//		// create measure template
//		pushAddNewMT();
//
//		if (dataCollection == 0) {
//			AbstractMeasureEditPageAdapter.setDataCollectionManual();
//		} else {
//			AbstractMeasureEditPageAdapter.setDataCollectionFormula();
//		}
//
//		if (indicatorType == 0) {
//			AbstractMeasureEditPageAdapter.setIndicatorTypeGoal();
//		} else {
//			AbstractMeasureEditPageAdapter.setIndicatorTypeVariance();
//		}
//
//		AbstractMeasureEditPageAdapter.setName(mtName);
//		AbstractMeasureEditPageAdapter.setDescription(mtDescription);
//		AbstractMeasureEditPageAdapter.submitChangesWithUpperButtonOnCreating();
//		SeleniumDriverSingleton.getDriver().waitForPageToLoad(
//				CoreProperties.getWaitForElementToLoadAsString());
//		return mtName;
//	}

//	/**
//	 * Create new measure template with name generated with method
//	 * getNewAutoMTName() You shouldn't (in fact you mustn't to do it) call
//	 * Selenium API method waitForPageToLoad(...) after this method.<br>
//	 * Before using this method you have to navigate to the page "Measure
//	 * Template: Library"<br>
//	 *
//	 * @param dataCollection -
//	 *            the data collection of new measure template. if 0 - manual,
//	 *            1-formula.
//	 *
//	 * @param indicatorType -
//	 *            the indicator type of new measure template. if 0 - goal, 1 -
//	 *            variance.
//	 *
//	 *
//	 * @return created measure/measure template name
//	 * @deprecated
//	 *
//	 */
//	public  String createSimpleM(int dataCollection, int indicatorType) {
//		String newMTName = AbstractMeasureEditPageAdapter.getNewAutoName();
//		createSimpleM(newMTName, AbstractMeasureEditPageAdapter.getAutoDescriptionForMT(newMTName), dataCollection,
//				indicatorType);
//		return newMTName;
//	}

//	/**
//	 * @deprecated
//	 */
//	public  String createSimpleMManualGoal() {
//		return createSimpleM(0, 0);
//	}
//
//	/**
//	 * @deprecated
//	 */
//	public  String createSimpleMFormulaGoal() {
//		return createSimpleM(1, 0);
//	}

//	/**
//	 * @deprecated
//	 */
//	public  String createSimpleMManualVariance() {
//		return createSimpleM(0, 1);
//	}
//
//	/**
//	 * @deprecated
//	 */
//	public  String createSimpleMFormulaVariance() {
//		return createSimpleM(1, 1);
//	}

	/**
	 * Delete measure template
	 *
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"<br>
	 *
	 * @param name -
	 *            the measure template name or a substring that is contained in
	 *            measure template name. If we have several measure template
	 *            names with such substring - result can surprise you. So use
	 *            this method carefully
	 */
	public  abstract void doActionDelete(String mtName);

	/**
	 * Delete measure template You shouldn't (in fact you mustn't to do it) call
	 * Selenium API method waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"<br>
	 *
	 * @param name -
	 *            the measure template name or a substring that is contained in
	 *            measure template name. If we have several measure template
	 *            names with such substring - result can surprise you. So use
	 *            this method carefully
	 */
	public abstract void doActionDetach(String mtName);


	/**
	 * Open measure template for editing by passed name.<br>
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"<br>
	 *
	 * @param name -
	 *            the measure template name or a substring that is contained in
	 *            measure template name. If we have several measure template
	 *            names with such substring - result can surprise you. So use
	 *            this method carefully
	 */
	public abstract void doActionEdit(String mtName);



	/**
	 * Delete all measure templates which have passed substring in their names.<br>
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"
	 *
	 * @param mtNameSubstr -
	 *            the substring in the measure template name.
	 * @return count of deleted measure templates.
	 */
	public  int deleteAllWithSubstringInName(String mtNameSubstr) {

		int count = 0;
		while (existBySubstring(mtNameSubstr)) {
			doActionDelete(mtNameSubstr);
			count++;
		}
		return count;
	}

	/**
	 * Open the page "Measure Template:Properties".<br>
	 * You shouldn't (in fact you mustn't to do it) call Selenium API method
	 * waitForPageToLoad(...) after this method.<br>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library"
	 *
	 * @param mtName -
	 *            the measure template name that for what we want to open page
	 *            "Measure Template:Properties"
	 */
	public  void doActionViewProperties(String mtName) {
		doActionClick(mtName);
		// click "View Properties"
		driver.click("dom= window.dojo.query('#ps_widget_psMenuItem_0')[0];");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

//	/**
//	 * Check if page MT:Library or WI:Manage Measures is loaded - pages with
//	 * list of MT or Measures
//	 *
//	 * @return true if page MT:Library or WI:Manage Measures is loaded, false -
//	 *         otherwise
//	 */
//	public  boolean isLoadedPageMeasureLists() {
//		return isLoadedPageMTLibrary() || isLoadedPageManageMeasure()
//				|| isLoadedPageAttachMT();
//	}

//	/**
//	 * Assert if page MT:Library or WI:Manage Measures is loaded - pages with
//	 * list of MT or Measures
//	 */
//	public  void assertIsLoadedPageMeasureLists() {
//		Assert.assertEquals(isLoadedPageMeasureLists(), true,
//				"You are not on the page MT:Library or WI:Manage Measures");
//	}




//	/**
//	 * @deprecated
//	 * @param mtName
//	 */
//	public  void attachMeasureTemplateByNameSubstring(String mtName) {
//		assertIsLoadedPageAttachMT();
//		String mId = MeasureListPagesAdapter.getByNameIdForM(mtName);
//		String query = "dom= var param = window.dojo.query('#PSTable [href*="
//				+ mId + "]')[0].parentNode.parentNode;"
//				+ "	window.dojo.query('td [id*=ImageSubmit]',param)[0];";
//		SeleniumDriverSingleton.getDriver().click(query);
//		SeleniumDriverSingleton.getDriver().waitForPageToLoad(
//				CoreProperties.getWaitForElementToLoadAsString());
//
//	}

//	/**
//	 * Get loaded page type
//	 *
//	 * @return page type
//	 */
//	public  MeasureListType getLoadedPageType() {
//		assertIsLoadedPageMeasureLists();
//		if (isLoadedPageAttachMT()) {
//			return MeasureListType.ATTACH_MEASURE_TEMPLATE;
//		} else if (isLoadedPageManageMeasure()) {
//			return MeasureListType.MANAGE_MEASURE;
//		} else if (isLoadedPageMTLibrary()) {
//			return MeasureListType.MEASURE_TEMPLATE_LIBRARY;
//		} else {
//			Assert.fail("No one page of measure lists type is loaded");
//		}
//		return null;
//	}

//	/**
//	 * Get substring which included in the URL. Each list page has it's own
//	 * substring (can have it's own substring)
//	 *
//	 * @param mType -
//	 *            measure page type that contained measure list
//	 * @return
//	 */
//	public  String getHrefSubstringByMType(MeasureListType mType) {
//		String hrefUrlSubstr = new String();
//		switch (mType) {
//		case MEASURE_TEMPLATE_LIBRARY:
//			hrefUrlSubstr = CoreProperties.getURLMTAdd();
//			break;
//		case ATTACH_MEASURE_TEMPLATE:
//			hrefUrlSubstr = CoreProperties.getURLMTAdd();
//			break;
//		case MANAGE_MEASURE:
//			hrefUrlSubstr = CoreProperties.getURLWIMeasureInstance();
//			break;
//		default:
//			Assert.fail("It has been passed wrong measure list type");
//			break;
//		}
//		return hrefUrlSubstr;
//	}

	public abstract void pushCreate();
}