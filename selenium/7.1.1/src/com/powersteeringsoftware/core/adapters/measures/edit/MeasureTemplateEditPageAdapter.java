package com.powersteeringsoftware.core.adapters.measures.edit;

import org.testng.Assert;

import com.powersteeringsoftware.core.objects.measures.MeasureIndicatorTypeEnum;
import com.powersteeringsoftware.core.objects.measures.MeasureLockedFieldsEnum;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTestCaseException;
import com.thoughtworks.selenium.DefaultSelenium;

public class MeasureTemplateEditPageAdapter extends AbstractMeasureEditPageAdapter {

	public MeasureTemplateEditPageAdapter(DefaultSelenium _driver){
		super(_driver);
	}

	/**
	 * Assert loading page Measure Add\edit.
	 *
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 */
	public void assertIsLoadedPageMTEdit() {
		Assert.assertEquals(true,
				isLoadedPageEditMT(),
				"You must navigate Measure Template:Add\\Edit page.");
	}

	/**
	 * Get owner index
	 *
	 * @return 0- if template owner, 1 - in other cases
	 */
	public  int getEvaluateAs() {
		assertIsLoadedPageMTEdit();

		Assert
				.assertNotSame(isTemplateOwner(), isProjectOwner(),
						"We can't have both checked or unchecked owners: project and template.");
		return (isTemplateOwner()) ? 0 : 1;
	}

	/**
	 * Check if locking checkbox is checked. This method can be run only on the
	 * page Measure Template: Edit/Add<br>
	 *
	 * @param checkboxNumber -
	 *            the checkbox number, can be one of the follows:<br>
	 * @return true - if checkbox is checked, false - in other cases.
	 */
	public  boolean isCheckedLockedCheckbox(
			MeasureLockedFieldsEnum lockedField) {
		assertIsLoadedPageMTEdit();
		return SeleniumDriverSingleton.getDriver().isChecked(
				lockedField.getFieldId());
	}

	/**
	 * Uncheck all locked fields
	 */
	public  void unlockAllFields() {
		assertIsLoadedPageMTEdit();

		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.NAME);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.DESCRIPTION);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.UNITS);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.DATA_COLLECTION);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.DISPLAY_FORMAT);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.EFFECTIVE_DATES);

		MeasureIndicatorTypeEnum it = getIndicatorTypeType();
		if (it == MeasureIndicatorTypeEnum.GOAL) {
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.GOAL_MESSAGES);
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.GOAL_THRESHOLDS);
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.START_DATE);
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.TARGET_DATE);
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.TARGET_VALUE);
		} else if (it == MeasureIndicatorTypeEnum.VARIANCE) {
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.VARIANCE_MESSAGES);
			setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.VARIANCE_THRESHOLDS);
		}


		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.REMINDER_SHEDULE);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.FORMULA);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.TEST_SCHEDULE);
		setLockingCheckboxUnchecked(MeasureLockedFieldsEnum.HISTORY_SCHEDULE);

	}


	/**
	 * Check locking checkbox by number. This method can be run only on the page
	 * Measure Template: Edit/Add<br>
	 *
	 * @param checkboxNumber -
	 *            the checkbox number, can be one of the follows:<br>
	 */
	private  void checkLockingCheckbox(MeasureLockedFieldsEnum lockedFields) {
		SeleniumDriverSingleton.getDriver().check(lockedFields.getFieldId());
	}

	/**
	 * Check locking checkbox
	 *
	 * @param checkboxNumber -
	 *            the checkbox number
	 */
	public  void setLockingCheckboxChecked(MeasureLockedFieldsEnum lockedField) {
		checkLockingCheckbox(lockedField);
	}

	/**
	 * Lock all visible fields
	 */
	public  void lockAllFields() {

		setLockingCheckboxChecked(MeasureLockedFieldsEnum.NAME);
		setLockingCheckboxChecked(MeasureLockedFieldsEnum.DESCRIPTION);
		setLockingCheckboxChecked(MeasureLockedFieldsEnum.UNITS);
		setLockingCheckboxChecked(MeasureLockedFieldsEnum.DATA_COLLECTION);
		setLockingCheckboxChecked(MeasureLockedFieldsEnum.DISPLAY_FORMAT);
		setLockingCheckboxChecked(MeasureLockedFieldsEnum.EFFECTIVE_DATES);

		try {
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.REMINDER_SHEDULE);
		} catch (Exception e) {
			log.error("Can't check locked field", e);
		}
		try {
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.FORMULA);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.TEST_SCHEDULE);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.HISTORY_SCHEDULE);
		} catch (Exception e) {
			log.error("Can't check locked field", e);
		}

		MeasureIndicatorTypeEnum it = getIndicatorTypeType();
		if (it == MeasureIndicatorTypeEnum.GOAL) {
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.GOAL_MESSAGES);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.GOAL_THRESHOLDS);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.START_DATE);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.TARGET_DATE);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.TARGET_VALUE);
		} else if (it == MeasureIndicatorTypeEnum.VARIANCE) {
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.VARIANCE_MESSAGES);
			setLockingCheckboxChecked(MeasureLockedFieldsEnum.VARIANCE_THRESHOLDS);
		}
	}

	public  boolean isTemplateOwner() {

		return SeleniumDriverSingleton.getDriver().isChecked(
				"dom= window.dojo.byId('template')");
	}

	public  boolean isProjectOwner() {

		return SeleniumDriverSingleton.getDriver().isChecked(
				"dom= window.dojo.byId('project')");
	}

	/**
	 *
	 * @return
	 */
	public  boolean isLoadedPageEditMT() {
		return driver.isTextPresent(
				"Edit Measure Template");
	}

	/**
	 * Set Evaluate as<br>
	 * We have to set data collection = formula before invoking this method
	 *
	 * @param owner:
	 *            0 - Template Owner, 1 - Project Owner
	 */
	public  void setEvaluateAs(int owner) {

		Assert.assertEquals(isDataCollectionFormula(), true,
				"We have to set data collection = formula");
		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		if (owner == 1) {
			driver.click(
					"dom=window.dojo.byId('template')");
		} else if (owner == 0) {
			driver.click(
					"dom=window.dojo.byId('project')");
		} else {
			throw new PSTestCaseException(
					"You can use only values 1 or 0 for parameter owner");
		}
	}

	/**
	 * Uncheck locking checkbox
	 *
	 * @param checkboxNumber -
	 *
	 */
	public  void setLockingCheckboxUnchecked(
			MeasureLockedFieldsEnum lockedField) {
		uncheckLockingCheckbox(lockedField);
	}


	/**
	 * Uncheck locking checkbox by number. This method can be run only on the
	 * page Measure Template: Edit/Add<br>
	 *
	 * @param checkboxNumber -
	 *            the checkbox number, can be one of the follows:<br>
	 */
	private  void uncheckLockingCheckbox(MeasureLockedFieldsEnum lockedFields) {
		driver.uncheck(lockedFields.getFieldId());
	}
}

