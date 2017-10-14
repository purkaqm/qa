package com.powersteeringsoftware.core.adapters.measures.edit;

import org.testng.Assert;

import com.powersteeringsoftware.core.objects.measures.MeasureDataCollectionEnum;
import com.powersteeringsoftware.core.objects.measures.MeasureIndicatorTypeEnum;
import com.thoughtworks.selenium.DefaultSelenium;

public class MeasureInstanceEditPageAdapter extends AbstractMeasureEditPageAdapter{

	public MeasureInstanceEditPageAdapter(DefaultSelenium _driver){
		super(_driver);
	}


	/**
	 * Check if field is disabled
	 *
	 * @param location -
	 *            the location of the field, used by Selenium for checking
	 * @return true - if field is disabled, false - otherwise
	 */
	private  boolean isDisabledField(String location) {

		return new Boolean(driver.getEval("window.dojo.query('" + location
				+ "')[0].disabled"));
	}

	/**
	 * Check if field "Name" is disabled
	 */
	public  boolean isDisabledName() {
		return isDisabledField("#name");
	}

	/**
	 * Check if field "Description" is disabled
	 */
	public  boolean isDisabledDescription() {
		return isDisabledField("#description");
	}

	/**
	 * Check if field "Units" is disabled
	 */
	public  boolean isDisabledUnits() {
		return isDisabledField("#units");
	}

	/**
	 * Check if radio button group "Data Collection" is disabled
	 */
	public  boolean isDisabledDataCollection() {
		return (isDisabledField("#manual") && isDisabledField("#auto"));
	}

	/**
	 * Check if edit box "Formula" is disabled
	 */
	public  boolean isDisabledFormula() {
		return isDisabledField("#usrDispId");
	}

	/**
	 * Check if select box "display Format" is disabled. Also are checked select
	 * boxes (if it is possible) Scale and Precision.
	 */
	public  boolean isDisabledDisplayFormat() {
		boolean result = isDisabledField("#field_format");

		int displayFormat = getDisplayFormat();
		if (displayFormat != 1) {
			result &= isDisabledDisplayFormat_Precision();
		}
		if (displayFormat == 2) {
			result &= isDisabledDisplayFormat_Scale();
		}
		return result;
	}

	/**
	 * Check if select box "Scale" is disabled
	 */
	private  boolean isDisabledDisplayFormat_Scale() {
		return isDisabledField("#PropertySelection_0");
	}

	/**
	 * Check if select box "Precision" is disabled
	 */
	private  boolean isDisabledDisplayFormat_Precision() {
		return isDisabledField("#PropertySelection_1");
	}

	/**
	 * Check if select box "Effective Dates" is disabled. Also checked started
	 * date and end date (if it needed)
	 */
	public  boolean isDisabledEffectiveDates() {
		boolean result = isDisabledField("#effectiveDates");

		int effectiveDate = getEffectiveDates();
		if (effectiveDate == 0) {
			result &= isDisabledEffectiveDates_EndDate()
					&& isDisabledEffectiveDates_StartDate();
		}
		return result;
	}

	/**
	 * Check if start date date picker for select box "Effective Dates" is
	 * disabled
	 */
	 boolean isDisabledEffectiveDates_StartDate() {
		return isDisabledField("#startDate");
	}

	/**
	 * Check if end date date picker for select box "Effective Dates" is
	 * disabled
	 */
	 boolean isDisabledEffectiveDates_EndDate() {
		return isDisabledField("#endDate");
	}

	public  boolean isDisabledScheduleReminder() {

		return !(driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[0];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[1];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[2];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[3];") && driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[4];"));
	}

	public  boolean isDisabledScheduleHistory() {

		return !(driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[0];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[1];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[2];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[3];") && driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup_0]')[4];"));
	}

	public  boolean isDisabledScheduleTest() {

		return !(driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup]')[0];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup]')[1];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup]')[2];")
				&& driver
						.isElementPresent("dom= window.dojo.query('[name=RadioGroup]')[3];") && driver
				.isElementPresent("dom= window.dojo.query('[name=RadioGroup]')[4];"));
	}

	public  boolean isDisabledIndicatorType() {
		return isDisabledField("#variance") && isDisabledField("#goal");
	}

	public  boolean isDisabledTargetStartDate() {
		return isDisabledField("#targetStartDate");
	}

	public  boolean isDisabledTargetEndDate() {
		return isDisabledField("#targetDate");
	}

	public  boolean isDisabledTargetValue() {
		return isDisabledField("#goalValue");
	}

	public  boolean isDisabledMessageRedGoal() {
		return isDisabledField("#TextField");
	}

	public  boolean isDisabledMessageYellowGoal() {
		return isDisabledField("#TextField_0");
	}

	public  boolean isDisabledMessageGreenGoal() {
		return isDisabledField("#TextField_1");
	}

	public  boolean isDisabledThreshold1Goal() {
		return isDisabledField("#achInd");
	}

	public  boolean isDisabledThreshold2Goal() {
		return isDisabledField("#achInd_0");
	}

	public  boolean isDisabledMessageRed1Variance() {
		return isDisabledField("#TextField_2");
	}

	public  boolean isDisabledMessageRed2Variance() {
		return isDisabledField("#TextField_10");
	}

	public  boolean isDisabledMessageYellow1Variance() {
		return isDisabledField("#TextField_4");
	}

	public  boolean isDisabledMessageYellow2Variance() {
		return isDisabledField("#TextField_8");
	}

	public  boolean isDisabledMessageGreenVariance() {
		return isDisabledField("#TextField_6");
	}

	public  boolean isDisabledThreshold1Variance() {
		return isDisabledField("#TextField_3");
	}

	public  boolean isDisabledThreshold2Variance() {
		return isDisabledField("#TextField_5");
	}

	public  boolean isDisabledThreshold3Variance() {
		return isDisabledField("#TextField_7");
	}

	public  boolean isDisabledThreshold4Variance() {
		return isDisabledField("#TextField_9");
	}

	/**
		 * Check if disabled all fields with messages
		 *
		 * @return true if all message fields are disabled, false - otherwise
		 *
		 * @deprecated - because doesn't work for variance
		 */
		public  boolean isDisabledMessages() {
			boolean result = true;
			MeasureIndicatorTypeEnum indicatorType = getIndicatorTypeType();
			switch (indicatorType) {
			case GOAL:
				result &= isDisabledMessageGreenGoal()
						&& isDisabledMessageRedGoal()
						&& isDisabledMessageYellowGoal();
				break;
	//		case VARIANCE:
	//			result &= isDisabledMessageGreenVariance()
	//					&& isDisabledMessageRed1Variance()
	//					&& isDisabledMessageRed2Variance()
	//					&& isDisabledMessageYellow1Variance()
	//					&& isDisabledMessageYellow2Variance();
	//			break;
			default:
				break;
			}

			return result;
		}

	/**
		 * Check if disabled all threshold fields in the page module "Indicator
		 * Type"
		 *
		 * @return true if all threshold fields are disabled, false - otherwise
		 * @deprecated - because doesn't work for variance
		 */
		public  boolean isDisabledThresholds() {
			boolean result = true;
			MeasureIndicatorTypeEnum indicatorType = getIndicatorTypeType();
			switch (indicatorType) {
			case GOAL:
				result &= isDisabledThreshold1Goal() && isDisabledThreshold2Goal();
				break;
	//		case VARIANCE:
	//			result &= isDisabledThreshold1Variance()
	//					&& isDisabledThreshold2Variance()
	//					&& isDisabledThreshold3Variance()
	//					&& isDisabledThreshold4Variance();
	//			break;
			default:
				break;
			}

			return result;
		}

	/**
	 * Check if all visible fields that can be locked - are enabled.
	 *
	 * @return true - if all fields (that can be locked) are disabled, false -
	 *         in other cases
	 */
	public  boolean isDisabledAllLockedFields() {
		boolean result = true;

		// Measure Details
		boolean disabledName = isDisabledName();
		boolean disabledDescription = isDisabledDescription();
		boolean disabledUnits = isDisabledUnits();
		result &= disabledName && disabledDescription && disabledUnits;

		if (!result) {
			return result;
		}

		// Data Collection
		boolean disabledDataCollection = isDisabledDataCollection();
		boolean disabledDisplayFormat = isDisabledDisplayFormat();
		boolean disabledEffectiveDates = isDisabledEffectiveDates();
		result &= disabledDataCollection && disabledDisplayFormat
				&& disabledEffectiveDates;

		MeasureDataCollectionEnum dataCollection = getDataCollectionType();
		if (dataCollection == MeasureDataCollectionEnum.FORMULA) {
			result &= isDisabledScheduleHistory() && isDisabledScheduleTest();
		} else if (dataCollection == MeasureDataCollectionEnum.MANUAL) {
			result &= isDisabledScheduleReminder();
		}

		if (!result) {
			return result;
		}

		// Indicator Type
		boolean disabledMessages = isDisabledMessages();
		boolean disabledThresholds = isDisabledThresholds();
		result &= disabledMessages && disabledThresholds;

		return result;
	}

	/**
	 * Check if all visible fields that can be locked - are enabled.
	 *
	 * @return true - if all fields (that can be locked) are enabled, false - in
	 *         other cases
	 */
	public  boolean isEnabledAllLockedFields() {
		boolean result = true;


		// Measure Details
		boolean disabledName = !isDisabledName();
		boolean disabledDescription = !isDisabledDescription();
		boolean disabledUnits = !isDisabledUnits();
		result &= disabledName && disabledDescription && disabledUnits;

		if (!result) {
			return result;
		}

		// Data Collection
		boolean disabledDataCollection = !isDisabledDataCollection();
		boolean disabledDisplayFormat = !isDisabledDisplayFormat();
		boolean disabledEffectiveDates = !isDisabledEffectiveDates();
		result &= disabledDataCollection && disabledDisplayFormat
				&& disabledEffectiveDates;

		if (!result) {
			return result;
		}

		MeasureDataCollectionEnum dataCollection = getDataCollectionType();
		if (dataCollection == MeasureDataCollectionEnum.FORMULA) {
			result &= !isDisabledScheduleHistory() && !isDisabledScheduleTest();
		} else if (dataCollection == MeasureDataCollectionEnum.MANUAL) {
			result &= !isDisabledScheduleReminder();
		}

		if (!result) {
			return result;
		}

		// Indicator Type
		boolean disabledMessages = !isDisabledMessages();
		boolean disabledThresholds = !isDisabledThresholds();
		result &= !disabledMessages && !disabledThresholds;

		return result;
	}


	/**
	 *
	 * @return true
	 */
	public  boolean isLoadedPageEditM() {
		return driver.isTextPresent("Edit Measure Instance");
	}

	public  void assertIsLoadedPageMEdit() {
		Assert.assertEquals(true, isLoadedPageEditM(),
				"You must navigate Measure:Add\\Edit page.");
	}

}
