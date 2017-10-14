package com.powersteeringsoftware.core.managers;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.AbstractMeasureEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.edit.MeasureTemplateEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.AbstractMeasureListPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.AttachMeasureTemplatePage;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.MeasuresLibraryPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.objects.measures.MeasureDataCollectionEnum;
import com.powersteeringsoftware.core.objects.measures.MeasureIndicatorTypeEnum;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;

public class MeasureManager {


	/**
	 * <p>Create Measure. You can select fields:
	 * -name,<br>
	 * -description,<br>
	 * -data,<br>
	 * -collection,<br>
	 * -indicator type.<br>
	 * </p>
	 * <p>You mustn't call Selenium API method waitForPageToLoad(...) after this method.</p>
	 * <p>Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".</p>
	 *
	 * @param mtName -
	 *            the name for new measure
	 *
	 * @param mtDescription - the measure description
	 *
	 * @param dataCollection -
	 *            the data collection of new measure template.
	 *
	 * @param indicatorType -
	 *            the indicator type of new measure template.
	 *
	 * @return name of the created measure/measure template
	 */
	private static String createM(String mtName,
			String mtDescription,
			MeasureDataCollectionEnum dataCollection,
			MeasureIndicatorTypeEnum indicatorType,
			AbstractMeasureListPageAdapter listAdapter,
			AbstractMeasureEditPageAdapter editAdapter) {

		//ManageMeasuresPageAdapter _manageMeasuresAdpater = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		listAdapter.pushCreate();

		if (dataCollection == MeasureDataCollectionEnum.MANUAL) {
			editAdapter.setDataCollectionManual();
		} else {
			editAdapter.setDataCollectionFormula();
		}

		if (indicatorType == MeasureIndicatorTypeEnum.GOAL) {
			editAdapter.setIndicatorTypeGoal();
			editAdapter.setThresholdGoal1(50);
			editAdapter.setThresholdGoal2(100);
		} else {
			editAdapter.setIndicatorTypeVariance();
			editAdapter.setThresholdVariance1(100);
			editAdapter.setThresholdVariance2(200);
			editAdapter.setThresholdVariance3(300);
			editAdapter.setThresholdVariance4(400);
		}

		editAdapter.setName(mtName);
		editAdapter.setDescription(mtDescription);
		editAdapter.submitChangesWithUpperButtonOnCreating();
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());

		return mtName;
	}


	/**
	 * <p>
	 * Create new measure
	 * </p>
	 * <p>
	 * You mustn't invoke Selenium API method waitForPageToLoad(...) after
	 * invoking this method.
	 * </p>
	 * <p>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".
	 * </p>
	 *
	 * @param dataCollection -
	 *            the data collection of new measure template.
	 *
	 * @param indicatorType -
	 *            the indicator type of new measure template.
	 *
	 *
	 * @return created measure/measure template name
	 */
	private static String createM(MeasureDataCollectionEnum dataCollection,
			MeasureIndicatorTypeEnum indicatorType,
			AbstractMeasureListPageAdapter listAdapter,
			AbstractMeasureEditPageAdapter editAdapter){
		String newMTName = new TimeStampName("Measure").getTimeStampedName();
		createM(newMTName, "Auto description", dataCollection, indicatorType, listAdapter, editAdapter);
		return newMTName;
	}


	private static String createMeasure(String wiUid, MeasureDataCollectionEnum dataCollection, MeasureIndicatorTypeEnum indicatorType){
		new WISummaryAdapter().navigatePageManageMeasure(wiUid);
		return createM(dataCollection, indicatorType,
				new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()),
				new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()));
	}

	private static String createMeasure(String wiUid, String mName, String mDescr, MeasureDataCollectionEnum dataCollection, MeasureIndicatorTypeEnum indicatorType){
		new WISummaryAdapter().navigatePageManageMeasure(wiUid);
		return createM(mName, mDescr, dataCollection, indicatorType,
				new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()),
				new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver()));
	}

	private static String createMeasureTemplate(String mName, String mDescr, MeasureDataCollectionEnum dataCollection, MeasureIndicatorTypeEnum indicatorType){
		MainMenuAdapter.clickBrowseMeasureLibrary();
		return createM(mName, mDescr, dataCollection, indicatorType,
				new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver()),
				new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver()));
	}


	private static String createMeasureTemplate(MeasureDataCollectionEnum dataCollection, MeasureIndicatorTypeEnum indicatorType){
		MainMenuAdapter.clickBrowseMeasureLibrary();
		return createM(dataCollection, indicatorType,
				new MeasuresLibraryPageAdapter(SeleniumDriverSingleton.getDriver()),
				new MeasureTemplateEditPageAdapter(SeleniumDriverSingleton.getDriver()));
	}

	/**
	 * <p>
	 * Create measure with DataColection = Manual, IndicatorType = Goal
	 * </p>
	 * <p>
	 * You mustn't invoke Selenium API method waitForPageToLoad(...) after
	 * invoking this method.
	 * </p>
	 * <p>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".
	 * </p>
	 *
	 * @return created measure name
	 */
	public static String createMeasureManualGoal(String wiId){
		return createMeasure(wiId, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureManualGoal(String wiId, String mName, String mDescr ){
		return createMeasure(wiId, mName, mDescr, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureTemplateManualGoal(){
		return createMeasureTemplate(MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureTemplateManualGoal(String mName, String mDescr ){
		return createMeasureTemplate(mName, mDescr, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.GOAL);
	}

	/**
	 * <p>
	 * Create measure with DataColection = Formula, IndicatorType = Goal
	 * </p>
	 * <p>
	 * You mustn't invoke Selenium API method waitForPageToLoad(...) after
	 * invoking this method.
	 * </p>
	 * <p>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".
	 * </p>
	 *
	 * @return created measure name
	 */
	public static String createMeasureFormulaGoal(String wiId){
		return createMeasure(wiId, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureFormulaGoal(String wiId, String mName, String mDescr ){
		return createMeasure(wiId, mName, mDescr, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureTemplateFormulaGoal(){
		return createMeasureTemplate(MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.GOAL);
	}

	public static String createMeasureTemplateFormulaGoal(String mName, String mDescr ){
		return createMeasureTemplate(mName, mDescr, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.GOAL);
	}

	/**
	 * <p>
	 * Create measure with DataColection = Manual, IndicatorType = Variance
	 * </p>
	 * <p>
	 * You mustn't invoke Selenium API method waitForPageToLoad(...) after
	 * invoking this method.
	 * </p>
	 * <p>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".
	 * </p>
	 *
	 * @return created measure name
	 */
	public static String createMeasureManualVariance(String wiId){
		return createMeasure(wiId, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureManualVariance(String wiId, String mName, String mDescr ){
		return createMeasure(wiId, mName, mDescr, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureTemplateManualVariance(){
		return createMeasureTemplate(MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureTemplateManualVariance(String wiId, String mName, String mDescr ){
		return createMeasureTemplate(mName, mDescr, MeasureDataCollectionEnum.MANUAL, MeasureIndicatorTypeEnum.VARIANCE);
	}


	/**
	 * <p>
	 * Create measure with DataColection = Formula, IndicatorType = Variance
	 * </p>
	 * <p>
	 * You mustn't invoke Selenium API method waitForPageToLoad(...) after
	 * invoking this method.
	 * </p>
	 * <p>
	 * Before using this method you have to navigate to the page "Measure
	 * Template: Library" or to the page "Manage Measures".
	 * </p>
	 *
	 * @return created measure name
	 */
	public static String createMeasureFormulaVariance(String wiId){
		return createMeasure(wiId, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureFormulaVariance(String wiId, String mName, String mDescr ){
		return createMeasure(wiId, mName, mDescr, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureTemplateFormulaVariance(){
		return createMeasureTemplate(MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.VARIANCE);
	}

	public static String createMeasureTemplateFormulaVariance(String mName, String mDescr ){
		return createMeasureTemplate(mName, mDescr, MeasureDataCollectionEnum.FORMULA, MeasureIndicatorTypeEnum.VARIANCE);
	}


	/**
	 * Attach measure template to the project
	 *
	 * @param mtName
	 * @param wiId - work item UID
	 */
	public static void attachMeasureTemplateByName(String wiUid, String mtName){

		new WISummaryAdapter().navigatePageManageMeasure(wiUid);

		ManageMeasuresPageAdapter  _adapter = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		_adapter.pushAttach();

		String mId = new AttachMeasureTemplatePage(SeleniumDriverSingleton.getDriver()).getByNameIdForM(mtName);
		String query = "dom= var param = window.dojo.query('#PSTable [href*="+mId+"]')[0].parentNode.parentNode;" +
				"	window.dojo.query('td [id*=ImageSubmit]',param)[0];";
		SeleniumDriverSingleton.getDriver().click(query);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

}
