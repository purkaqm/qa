package com.powersteeringsoftware.core.adapters.measures.edit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;
import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.objects.measures.MeasureDataCollectionEnum;
import com.powersteeringsoftware.core.objects.measures.MeasureIndicatorTypeEnum;
import com.powersteeringsoftware.core.objects.measures.MeasureShedulesEnum;
import com.powersteeringsoftware.core.tc.PSTestCaseException;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.IGetEvalable;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.session.ObjectSessionException;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.thoughtworks.selenium.DefaultSelenium;

public abstract class AbstractMeasureEditPageAdapter {

	protected Logger log = Logger.getLogger(AbstractMeasureEditPageAdapter.class);

	protected DefaultSelenium driver;

	public AbstractMeasureEditPageAdapter(DefaultSelenium _driver){
		driver = _driver;
	}

	enum MeasureEditPageLocators71 implements ILocatorable,IGetEvalable  {
		THRESHOLD_VARIANCE_1("dom=window.dojo.byId('varianceInd')",		"window.dijit.byId('varianceInd').getValue()"),
		THRESHOLD_VARIANCE_2("dom=window.dojo.byId('varianceInd_0')",	"window.dijit.byId('varianceInd_0').getValue()"),
		THRESHOLD_VARIANCE_3("dom=window.dojo.byId('varianceInd_1')", 	"window.dijit.byId('varianceInd_1').getValue()"),
		THRESHOLD_VARIANCE_4("dom=window.dojo.byId('varianceInd_2')",	"window.dijit.byId('varianceInd_2').getValue()"),
		THRESHOLD_GOAL_1("dom=window.dojo.byId('goalInd')",				"window.dijit.byId('goalInd').getValue()"),
		THRESHOLD_GOAL_2("dom=window.dojo.byId('goalInd_0')",			"window.dijit.byId('goalInd_0').getValue()");

		String locator;
		String evaluateValueLocator;

		MeasureEditPageLocators71(String _locator, String _evaluateValueLocator){
			locator = _locator;
			evaluateValueLocator = _evaluateValueLocator;
		}

		public String getLocator() {
			return locator;
		}

		public String getGetValueLocator() {
			return evaluateValueLocator;
		}
	}


	/**
	 * Check if we have URL in browser equals to the page URL for Add Measure
	 * Template<br>
	 *
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 *
	 */
	public  boolean isLoadedPageEdit() {
		return BasicCommons.isPageURLContains(CoreProperties.getURLMTEdit());
	}


	/**
	 * Set data collection to Manual<br>
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 */
	public void setDataCollectionManual() {
		driver.click("manual");
	}

	/**
	 * Set data collection to Formula<br>
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 *
	 */
	public  void setDataCollectionFormula() {
		driver.click("auto");
	}

	/**
	 * Set measure name <br>
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 *
	 * @param name -
	 *            name of measure (template)
	 */
	public  void setName(String name) {
		if (StringUtils.isEmpty(name))
			throw new PSTestCaseException("Measure name can't be null or empty");

		driver.type("name", name);
	}

	/**
	 * Set measure description <b>Warning<b>. Before Using this method you must
	 * navigate to the page Edit\Add MEasure using methods:
	 * addNewMEasureTemplate() or editMEasureTemplate().
	 *
	 * @param description -
	 *            description of measure (template)
	 */
	public  void setDescription(String description) {
		driver.type("description", description);
	}

	/**
	 * Set measure units <br>
	 * <b>Warning<b>. Before Using this method you must navigate to the page
	 * Edit\Add MEasure using methods: addNewMEasureTemplate() or
	 * editMEasureTemplate().
	 *
	 *
	 * @param unit -
	 *            measure units
	 */
	public  void setUnits(String units) {
		Assert.assertNotSame(StringUtils.isEmpty(units), true,
				"Measure name can't be null or empty");
		driver.type(
				"dom= window.dojo.byId('units')", units);
	}

	/**
	 * Set Display format<br>
	 *
	 * @param displayFormat -
	 *            the display format:<br>
	 *            0 - Integer<br>
	 *            1 - Float<br>
	 *            2 - Monetary<br>
	 *            3 - Percent<br>
	 *
	 */
	public  void setDisplayFormat(int displayFormat) {
		driver.select(
				"dom=window.dojo.byId('field_format')",
				"value=" + displayFormat);
		switch (displayFormat) {
		case 1:
		case 3:
			setDisplayFormatPrecision(2);
			break;
		case 2:
			setDisplayFormatPrecision(2);
			setDisplayFormatScale(2);
			break;
		default:
			break;
		}
	}

	/**
	 * Set Precision for Float/Monetary format<br>
	 *
	 * @param precision
	 */
	public  void setDisplayFormatPrecision(int precision) {
		driver.select(
				"dom=window.dojo.byId('PropertySelection_1')",
				"value=" + precision);
	}

	/**
	 * Set Scale for Monetary Format<br>
	 *
	 * @param scale
	 */
	public  void setDisplayFormatScale(int scale) {
		driver.select(
				"dom= window.dojo.byId('PropertySelection_0')",
				"value=" + scale);
	}

	/**
	 * Set Effective Dates<br>
	 *
	 * @param effectiveDate
	 *            <br>
	 *            0 - absolute (from startDate to endDate)<br>
	 *            1 - active project<br>
	 *            2 - Project Lifetime<br>
	 *            3 - Baseline\Target Dates<br>
	 *            4 - Always<br>
	 * @param startDate -
	 *            Start date of effective period, format: 2009/01/20 (dataPicker
	 *            format)<br>
	 * @param endDate -
	 *            End date, format: 2009/01/20 (dataPicker format)<br>
	 */
	public  void setEffectiveDates(int effectiveDate, String startDate,
			String endDate) {
		driver.select(
				"dom=window.dojo.byId('effectiveDates')",
				"value=" + effectiveDate);
		if (effectiveDate == 0) {
			driver.type(
					"dom=window.dojo.byId('startDate')", startDate);
			driver.type(
					"dom=window.dojo.byId('endDate')", endDate);
		}
	}

	/**
	 * Set Reminder Schedule<br>
	 * You can use this method if and only of Data Collecton is set to Manual<br>
	 *
	 * @param reminderSchedule
	 *            <br>
	 *            0 - Never<br>
	 *            1 - Daily<br>
	 *            2 - Weekly<br>
	 *            3 - Monthly<br>
	 *            4 - Quarterly<br>
	 */
	public  void setScheduleReminder(int schedule) {
		Assert.assertEquals(isDataCollectionManual(), true,
				"Data collections must be Manual");
		Assert.assertEquals((schedule >= 0) && (schedule <= 4), true,
				"schedule value can be in period [0,4] only");

		driver.check(
				"dom= window.dojo.query('[name=RadioGroup_0]')[" + schedule
						+ "];");

	}

	/**
	 * Set Daily Reminder Schedule<br>
	 *
	 * @param day
	 *            <br>
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleReminderDaily(int day, String time) {
		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(1);
		driver.type(
				"dom= window.dojo.byId('dayInterval_0')", String.valueOf(day));
		driver.type(
				"dom= window.dojo.byId('PropertySelection_5')", time);
	}

	/**
	 * Set Weekly Reminder Schedule<br>
	 *
	 * @param day
	 *            <br>
	 * @param daysOfWeek -
	 *            array of week days(from 0 to 6) which remind will be send at,
	 *            example: {1,2,4}<br>
	 * @param time -
	 *            format "04:00 PM"<br>
	 *
	 */
	public  void setScheduleReminderWeekly(int week, int[] daysOfWeek,
			String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(2);
		driver.type(
				"dom= window.dojo.byId('weeklyFrequency_0')",
				String.valueOf(week));
		driver.type(
				"dom= window.dojo.byId('PropertySelection_0_1')", time);
		String inputIds[] = { "", "_0", "_1", "_2", "_3", "_4", "_5" };
		for (int i = 0; i < daysOfWeek.length; i++) {
			driver.click(
					"dom= window.dojo.byId('chckBxStPrmPrfxYYY_0Chkbx"
							+ inputIds[daysOfWeek[i]] + "')");
		}
	}

	/**
	 * Set Monthly Reminder Schedule<br>
	 *
	 * @param isDateReminder
	 *            true - use date of month reminder -
	 *            setReminderScheduleMonthlyDate() false - use day of week
	 *            reminder - setReminderScheduleMonthlyDayOfWeek()
	 */
	public  void setScheduleReminderMonthly(boolean isDateReminder) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(3);
		if (isDateReminder) {
			setScheduleReminderMonthlyDate(2, 4, "04:00 PM");
		} else {
			setScheduleReminderMonthlyDayOfWeek(2, 3, 4, "04:00 PM");
		}
	}

	/**
	 * Set Monthly Reminder Schedule (day of month)<br>
	 *
	 * @param day
	 *            <br>
	 * @param month
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleReminderMonthlyDate(int day, int month,
			String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(3);
		driver.click(
				"dom=window.dojo.query('[name=monthOption_0]')[0]");
		driver.type(
				"dom=window.dojo.byId('dayOfMonthSelect_0')",
				String.valueOf(day));
		driver.type(
				"dom=window.dojo.byId('numOfMonthSelect_0')",
				String.valueOf(month));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_1_1')", time);
	}

	/**
	 * Set Monthly Reminder Schedule (day of week)<br>
	 *
	 * @param numDay -
	 *            num day of every month ( 1 - 1st 2 - 2nd 3 - 3rd 4 - 4th 5 -
	 *            Last
	 * @param dayName -
	 *            day of week 1 - Sunday 2 - Monday 3 - Tuesday 4 - Wednesday 5 -
	 *            Thursday 6 - Friday 7 - Saturday 8 - Day 9 - Weekday 10 -
	 *            Weekend day
	 * @param numMonth -
	 *            number of "every month" (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleReminderMonthlyDayOfWeek(int numDay,
			int weekDay, int numMonth, String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(3);
		driver.click(
				"dom=window.dojo.query('[name=monthOption_0]')[1]");
		String days[] = { "", "1st", "2nd", "3rd", "4th", "Last" };
		driver.type(
				"dom=window.dojo.byId('numDaySelect_0')", days[numDay]);
		String daysOfWeek[] = { "", "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Day", "Weekday",
				"Weekend day" };
		driver.type(
				"dom=window.dojo.byId('dayNameSelect_0')", daysOfWeek[weekDay]);

		driver.type(
				"dom=window.dojo.byId('numOfMonth2Select_0')",
				String.valueOf(numMonth));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_1_1')", time);
	}

	/**
	 * Set Quarterly Reminder Schedule<br>
	 *
	 * @param isDateReminder
	 *            true - use date of quarter reminder -
	 *            setReminderScheduleQuarterlyDate() false - use day of week
	 *            reminder - setReminderScheduleQuarterlyDayOfQuarter()
	 */
	public  void setScheduleReminderQuarterly(boolean isDateReminder) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(4);
		if (isDateReminder) {
			setScheduleReminderQuarterlyDate(2, 4, "04:00 PM");
		} else {
			setScheduleReminderQuarterlyDayOfQuarter(2, 3, 4, "04:00 PM");
		}
	}

	/**
	 * Set Quarterly Reminder Schedule (day of quarter)
	 *
	 * @param day
	 *            <br>
	 * @param quarter
	 *            (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleReminderQuarterlyDate(int day, int quarter,
			String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(4);
		driver.click(
				"dom=window.dojo.query('[name=quarterOption_0]')[0]");
		driver.type(
				"dom=window.dojo.byId('dayOfQuarterSelect_0')",
				String.valueOf(day));
		driver.type(
				"dom=window.dojo.byId('numOfQuarterSelect_0')",
				String.valueOf(quarter));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_4_0')", time);
	}

	/**
	 * Set Quarterly Reminder Schedule (day of quarter)
	 *
	 * @param numDay -
	 *            num day of every quarter 1 - 1st 2 - 2nd 3 - 3rd 4 - 4th 5 -
	 *            Last
	 * @param dayName -
	 *            day of week 1 - Sunday 2 - Monday 3 - Tuesday 4 - Wednesday 5 -
	 *            Thursday 6 - Friday 7 - Saturday 8 - Day 9 - Weekday 10 -
	 *            Weekend day
	 * @param numQuarter -
	 *            number of "every month" (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleReminderQuarterlyDayOfQuarter(int numDay,
			int weekDay, int numQuarter, String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleReminder(4);
		driver.click(
				"dom=window.dojo.query('[name=quarterOption_0]')[1]");

		String days[] = { "", "1st", "2nd", "3rd", "4th", "Last" };
		driver.type(
				"dom=window.dojo.byId('PropertySelection_2_0')", days[numDay]);

		String daysOfWeek[] = { "", "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Day", "Weekday",
				"Weekend day" };
		driver.type(
				"dom=window.dojo.byId('PropertySelection_3_0')",
				daysOfWeek[weekDay]);
		driver.type(
				"dom=window.dojo.byId('numOfQuarter2Select_0')",
				String.valueOf(numQuarter));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_4_0')", time);
	}

	/**
	 * Set Formula<br>
	 *
	 * @formula - see FormulaBuilder ^1
	 */
	public  void setFormula(String formula) {

		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		driver.type(
				"dom=window.dojo.byId('usrDispId')", formula);
	}



	/**
	 * Set Test Schedule<br>
	 *
	 * @param reminderSchedule
	 *            0 - Never 1 - Daily 2 - Weekly 3 - Monthly 4 - Quarterly
	 */
	private  void setScheduleTest(int schedule) {
		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");

		Assert.assertEquals((schedule >= 0) && (schedule <= 4), true,
				"schedule value can be in period [0,4] only");

		driver.click(
				"dom= window.dojo.query('[name=RadioGroup]')[" + schedule
						+ "];");
	}

	/**
	 * Set Daily Test Schedule<br>
	 *
	 * @param day
	 *            <br>
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestDaily(int day, String time) {
		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");

		setScheduleTest(1);

		driver.type(
				"dom= window.dojo.byId('dayInterval')", String.valueOf(day));

		typeScheduleTestTimeOfDay(time);

	}

	/**
	 * Set Weekly Test Schedule<br>
	 *
	 * @param day
	 *            <br>
	 * @param daysOfWeek -
	 *            array of week days(from 0 to 6) which remind will be send at,
	 *            example: {1,2,4}<br>
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestWeekly(int week, int[] daysOfWeek,
			String time) {

		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");

		setScheduleTest(2);
		driver.type(
				"dom= window.dojo.byId('weeklyFrequency')",
				String.valueOf(week));
		driver.type(
				"dom= window.dojo.byId('PropertySelection_0_0')", time);
		String inputIds[] = { "", "_0", "_1", "_2", "_3", "_4", "_5" };
		for (int i = 0; i < daysOfWeek.length; i++) {
			driver.click(
					"dom= window.dojo.byId('chckBxStPrmPrfxYYYChkbx_2"
							+ inputIds[daysOfWeek[i]] + "')");
		}
	}

	/**
	 * Set Monthly Test Schedule<br>
	 *
	 * @param isDateReminder
	 *            true - use date of month reminder -
	 *            setReminderScheduleMonthlyDate() false - use day of week
	 *            reminder - setReminderScheduleMonthlyDayOfWeek()
	 */
	public  void setScheduleTestMonthly(boolean isDateReminder) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleTest(2);
		if (isDateReminder) {
			setScheduleTestMonthlyDate(2, 4, "04:00 PM");
		} else {
			setScheduleTestMonthlyDayOfWeek(2, 3, 4, "04:00 PM");
		}
	}

	/**
	 * Set Monthly Test Schedule (day of month)<br>
	 *
	 * @param day
	 *            <br>
	 * @param month
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestMonthlyDate(int day, int month,
			String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleTest(3);
		driver.click(
				"dom=window.dojo.query('[name=monthOption_0]')[0]");
		driver
				.type("dom=window.dojo.byId('dayOfMonthSelect')",
						String.valueOf(day));
		driver.type(
				"dom=window.dojo.byId('numOfMonthSelect')",
				String.valueOf(month));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_1_0')", time);
	}

	/**
	 * Set Monthly Test Schedule (day of week)<br>
	 *
	 * @param numDay -
	 *            num day of every month ( 1 - 1st 2 - 2nd 3 - 3rd 4 - 4th 5 -
	 *            Last
	 * @param dayName -
	 *            day of week 1 - Sunday 2 - Monday 3 - Tuesday 4 - Wednesday 5 -
	 *            Thursday 6 - Friday 7 - Saturday 8 - Day 9 - Weekday 10 -
	 *            Weekend day
	 * @param numMonth -
	 *            number of "every month" (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestMonthlyDayOfWeek(int numDay, int weekDay,
			int numMonth, String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleTest(3);

		driver.click(
				"dom=window.dojo.query('[name=monthOption_0]')[1]");

		String days[] = { "", "1st", "2nd", "3rd", "4th", "Last" };
		driver.type(
				"dom=window.dojo.byId('numDaySelect')", days[numDay]);
		String daysOfWeek[] = { "", "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Day", "Weekday",
				"Weekend day" };
		driver.type(
				"dom=window.dojo.byId('dayNameSelect')", daysOfWeek[weekDay]);

		driver.type(
				"dom=window.dojo.byId('numOfMonth2Select')",
				String.valueOf(numMonth));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_1_0')", time);
	}

	/**
	 * Set Quarterly Reminder Schedule<br>
	 *
	 * @param isDateReminder
	 *            true - use date of quarter reminder -
	 *            setReminderScheduleQuarterlyDate() false - use day of week
	 *            reminder - setReminderScheduleQuarterlyDayOfQuarter()
	 */
	public  void setScheduleTestQuarterly(boolean isDateSchedule) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Formula");
		setScheduleTest(4);
		if (isDateSchedule) {
			setScheduleReminderQuarterlyDate(2, 4, "04:00 PM");
		} else {
			setScheduleReminderQuarterlyDayOfQuarter(2, 3, 4, "04:00 PM");
		}
	}

	/**
	 * Set Quarterly Test Schedule (day of quarter)
	 *
	 * @param day
	 *            <br>
	 * @param quarter
	 *            (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestQuarterlyDate(int day, int quarter,
			String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Formula");

		setScheduleTest(4);
		driver.click(
				"dom=window.dojo.query('[name=quarterOption_0]')[0]");
		driver.type(
				"dom=window.dojo.byId('dayOfQuarterSelect_0')",
				String.valueOf(day));
		driver.type(
				"dom=window.dojo.byId('numOfQuarterSelect_0')",
				String.valueOf(quarter));
		driver.type(
				"dom=window.dojo.byId('PropertySelection_4_0')", time);
	}

	/**
	 * Set Quarterly Test Schedule (day of quarter)
	 *
	 * @param numDay -
	 *            num day of every quarter 1 - 1st 2 - 2nd 3 - 3rd 4 - 4th 5 -
	 *            Last
	 * @param dayName -
	 *            day of week 1 - Sunday 2 - Monday 3 - Tuesday 4 - Wednesday 5 -
	 *            Thursday 6 - Friday 7 - Saturday 8 - Day 9 - Weekday 10 -
	 *            Weekend day
	 * @param numQuarter -
	 *            number of "every month" (from 1 to 4)
	 * @param time -
	 *            format "04:00 PM"<br>
	 */
	public  void setScheduleTestQuarterlyDayOfQuarter(int numDay,
			int weekDay, int numQuarter, String time) {

		Assert.assertSame(isDataCollectionManual(), true,
				"Data collections must be Manual");

		setScheduleTest(4);
		driver.click(
				"dom= window.dojo.query('[name=quarterOption_0]')[1]");

		String days[] = { "", "1st", "2nd", "3rd", "4th", "Last" };
		driver.type(
				"dom= window.dojo.byId('PropertySelection_2_0')", days[numDay]);

		String daysOfWeek[] = { "", "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Day", "Weekday",
				"Weekend day" };
		driver.type(
				"dom= window.dojo.byId('PropertySelection_3_0')",
				daysOfWeek[weekDay]);
		driver.type(
				"dom= window.dojo.byId('numOfQuarter2Select_0')",
				String.valueOf(numQuarter));
		driver.type(
				"dom= window.dojo.byId('PropertySelection_4_0')", time);
	}

	/**
	 * Set History Schedule<br>
	 *
	 * @param reminderSchedule
	 *            0 - Never 1 - Daily 2 - Weekly 3 - Monthly 4 - Quarterly
	 */
	private  void setScheduleHistory(int schedule) {

		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		Assert.assertEquals((schedule >= 0) && (schedule <= 4), true,
				"schedule value can be in period [0,4] only");
		driver.check(
				"dom= window.dojo.query('[name=RadioGroup_0]')[" + schedule
						+ "];");
	}

	/**
	 * Set Daily History Schedule<br>
	 *
	 */
	public  void setScheduleHistoryDaily() {
		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		setScheduleHistory(1);
		driver.type(
				"dom= window.dojo.byId('dayInterval_0')", "1");
		driver.type(
				"dom= window.dojo.byId('PropertySelection_5')", "04:00 PM");
	}

	/**
	 * Set Weekly History Schedule<br>
	 *
	 */
	public  void setScheduleHistoryWeekly() {
		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		setScheduleHistory(2);
		driver.type(
				"dom= window.dojo.byId('weeklyFrequency_0')", "1");
		driver.type(
				"dom= window.dojo.byId('PropertySelection_0_1')", "04:00 PM");
		driver.click(
				"dom= window.dojo.byId('chckBxStPrmPrfxYYY_0Chkbx_2')");
	}

	/**
	 * Set Monthly History Schedule<br>
	 *
	 */
	public  void setScheduleHistoryMonthly() {

		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		setScheduleHistory(3);
		driver.type(
				"dom= window.dojo.byId('dayOfMonthSelect_0')", "2");
		driver.type(
				"dom= window.dojo.byId('numOfMonthSelect_0')", "2");
		driver.type(
				"dom= window.dojo.byId('PropertySelection_1_1')", "04:00 PM");
	}

	/**
	 * Set Quarterly History Schedule<br>
	 *
	 */
	public  void setScheduleHistoryQuarterly() {

		Assert.assertSame(isDataCollectionFormula(), true,
				"Data collections must be Formula");
		setScheduleHistory(4);
		driver.type(
				"dom= window.dojo.byId('dayOfQuarterSelect_0')", "3");
		driver.type(
				"dom= window.dojo.byId('numOfQuarterSelect_0')", "4");
		driver.type(
				"dom= window.dojo.byId('PropertySelection_4_0')", "04:00 PM");
	}

	/**
	 * Set Indicator Type on the page 'Measure Template\Measure Edit\Add new'.<br>
	 *
	 * @param pageFieldId -
	 *            field id for selecting Indicator Type
	 *
	 * @see com.powersteeringsoftware.tests.core.pages.field.id.PageFieldIdsForMeasureTemplate *
	 */

	public  void setIndicatorTypeGoal() {

		driver.click(
				"dom= window.dojo.query('#goal')[0]");
	}

	public  void setIndicatorTypeVariance() {

		driver.click(
				"dom= window.dojo.query('#variance')[0]");
	}

	/**
	 * Set Red Message for Variance Indicator type<br>
	 *
	 */
	public  void setMessageRedVariance1(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_2')", message);
	}

	public  String getMessageRedVariance1() {

		return driver.getEval(
				"window.dojo.byId('TextField_2').value");
	}

	/**
	 * Set Yellow Message for Variance Indicator type<br>
	 *
	 */
	public  void setMessageYellowVariance1(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_3')", message);
	}

	public  String getMessageYellowVariance1() {

		return driver.getEval(
				"window.dojo.byId('TextField_3').value");
	}

	/**
	 * Set Green Message for Variance Indicator type<br>
	 *
	 */
	public  void setMessageGreenVariance(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_4')", message);
	}

	public  String getMessageGreenVariance() {

		return driver.getEval(
				"window.dojo.byId('TextField_4').value");
	}

	/**
	 * Set Yellow Message for Variance Indicator type<br>
	 *
	 */
	public  void setMessageYellowVariance2(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_5')", message);
	}

	public  String getMessageYellowVariance2() {

		return driver.getEval(
				"window.dojo.byId('TextField_5').value");
	}

	/**
	 * Set Red Message for Variance Indicator type<br>
	 *
	 */
	public  void setMessageRedVariance2(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_6')", message);
	}

	public  String getMessageRedVariance2() {

		return driver.getEval(
				"window.dojo.byId('TextField_6').value");
	}

	public  double getThresholdVariance1() {

		String value = "";
		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				value = driver.getEval("window.dojo.byId('TextField_3').value");
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				value = driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_1.getGetValueLocator());
			}
		} catch (ObjectSessionException e) {
			// TODO: handle exception
			log.warn("Error while getting threshold.", e);
		}
		return Double.parseDouble(value);
	}

	/**
	 * Set Threshold 1 for Variance Indicator type<br>
	 *
	 */
	public  void setThresholdVariance1(double value) {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('TextField_5')",
						String.valueOf(value));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_1.getLocator(),
						String.valueOf(value));
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Set Threshold 2 for Variance Indicator type<br>
	 *
	 */
	public  void setThresholdVariance2(double value) {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('TextField_5')",
						String.valueOf(value));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_2.getLocator(),
						String.valueOf(value));
			}
		} catch (Exception e) {
		}
	}



	public  Double getThresholdVariance2() {

		Double value = 0.0;
		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				value= new Double(driver.getEval(
						"window.dojo.byId('TextField_5').value"));
			} else  if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				value= new Double(driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_2.evaluateValueLocator));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;
	}

	/**
	 * Set Threshold 1 for Variance Indicator type<br>
	 *
	 */
	public  void setThresholdVariance3(double value) {


		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('TextField_7')",
						String.valueOf(value));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_3.getLocator(),
						String.valueOf(value));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public  double getThresholdVariance3() {

		Double value = 0.0;
		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				value= new Double(driver.getEval(
						"window.dojo.byId('TextField_7').value"));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				value= new Double(driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_3.getGetValueLocator()
						));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return value;

	}

	/**
	 * Set Threshold 1 for Variance Indicator type<br>
	 *
	 */
	public  void setThresholdVariance4(double value) {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('TextField_9')",
						String.valueOf(value));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_4.locator,
						String.valueOf(value));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public  double getThresholdVariance4() {

		String value = "0";

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				value= driver.getEval(
						"window.dojo.byId('TextField_9').value");
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				value= driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_VARIANCE_4.getGetValueLocator());
			}

			return Double.parseDouble(value);

		} catch (NumberFormatException e) {
			return 0;
		} catch (ObjectSessionException ose){
			return 0;
		}
	}

	/**
	 * Set Start Date for Goal Indicator type<br>
	 *
	 * @param date -
	 *            format: "2009/01/30" (dataPicker format)
	 */
	public  void setStartDate(String date) {

		driver.type(
				"dom= window.dojo.byId('targetStartDate')", date);
	}

	/**
	 * Set Target Date for Goal Indicator type<br>
	 *
	 * @param date -
	 *            format: "2009/01/30" (dataPicker format)
	 */
	public  void setTargetDate(String date) {

		driver.type(
				"dom= window.dojo.byId('targetDate')", date);
	}

	/**
	 * Set Target Value for Goal Indicator type<br>
	 *
	 */
	public  void setTargetValue(double value) {

		driver.type(
				"dom= window.dojo.byId('goalValue')", String.valueOf(value));
	}

	/**
	 * Set Red Message for Goal Indicator type<br>
	 *
	 */
	public  void setMessageRedGoal(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField')", message);
	}

	public  String getMessageRedGoal() {

		return driver.getEval(
				"window.dojo.byId('TextField').value");
	}

	/**
	 * Set Yellow Message for Goal Indicator type<br>
	 *
	 */
	public  void setMessageYellowGoal(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_0')", message);
	}

	public  String getMessageYellowGoal() {

		return driver.getEval(
				"window.dojo.byId('TextField_0').value");
	}

	/**
	 * Set Green Message for Goal Indicator type<br>
	 *
	 */
	public  void setMessageGreenGoal(String message) {

		driver.type(
				"dom= window.dojo.byId('TextField_1')", message);
	}

	public  String getMessageGreenGoal() {

		return driver.getEval(
				"window.dojo.byId('TextField_1').value");
	}

	/**
	 * Set High Threshold for Goal Indicator type<br>
	 *
	 */
	public  void setThresholdGoal1(double value) {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('achInd')",
						String.valueOf(value));
			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_GOAL_1.locator,
						String.valueOf(value));
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public  double getThresholdGoal1() {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				return new Double(driver.getEval(
						"window.dojo.byId('achInd').value"));

			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				return new Double(driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_GOAL_1.getLocator()));
			}

		} catch (Exception e) {
			log.warn("Error while getting threshold value",e);
		}

		return 0;
	}

	/**
	 * Set Low Threshold for Goal Indicator type<br>
	 *
	 */
	public  void setThresholdGoal2(double value) {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				driver.type(
						"dom= window.dojo.byId('achInd_0')",
						String.valueOf(value));

			} else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				driver.type(
						MeasureEditPageLocators71.THRESHOLD_GOAL_2.getLocator(),
						String.valueOf(value));
			}

		} catch (Exception e) {
			log.warn("Error while setting up threshold value",e);
		}

	}

	public  double getThresholdGoal2() {

		try {
			if (TestSession.getApplicationVersionAsString().contains("7.0")) {
				return new Double(driver.getEval(
						"window.dojo.byId('achInd_0').value"));

			}else if (TestSession.getApplicationVersionAsString().contains("7.1")) {
				return new Double(driver.getEval(
						MeasureEditPageLocators71.THRESHOLD_GOAL_2.getGetValueLocator()));
			}

		} catch (Exception e) {
			log.warn("Error while evaluating threshold value",e);
		}
		return 0;
	}

	/**
	 * Return data collection is Manual<br>
	 *
	 */
	public  boolean isDataCollectionManual() {

		return driver.isChecked(
				"dom= window.dojo.byId('manual')");
	}

	/**
	 * Return data collection is Formula<br>
	 *
	 */
	public  boolean isDataCollectionFormula() {

		return driver.isChecked(
				"dom= window.dojo.byId('auto')");
	}

	/**
	 * Get current data collection value
	 *
	 * @return - 0 -if data collection=manual, 1 - if data collection-auto
	 * @deprecated
	 */
	public  int getDataCollection() {

		boolean isManual = isDataCollectionManual();
		boolean isFomula = isDataCollectionFormula();
		Assert.assertNotSame(isManual, isFomula,
				"We can't have both manual and formula data collection types");
		return (isManual) ? 0 : 1;
	}

	/**
	 * Get current data collection value
	 *
	 * @return - DataCollection.Manual -if data collection=manual,
	 *         DataCollection.Formula - if data collection=formula
	 *
	 */
	public  MeasureDataCollectionEnum getDataCollectionType() {

		boolean isManual = isDataCollectionManual();
		boolean isFomula = isDataCollectionFormula();
		Assert.assertNotSame(isManual, isFomula,
				"We can't have both manual and formula data collection types");
		return (isManual) ? MeasureDataCollectionEnum.MANUAL
				: MeasureDataCollectionEnum.FORMULA;
	}

	/**
	 * Check if indicator type is goal
	 *
	 * @return true if indicator type is goal, false - otherwise
	 */
	public  boolean isIndicatorTypeGoal() {

		return driver.isChecked(
				"dom= window.dojo.byId('goal')");
	}

	/**
	 * Check if indicator type is goal
	 *
	 * @return true if indicator type is goal, false - otherwise
	 */
	public  boolean isIndicatorTypeVariance() {

		return driver.isChecked(
				"dom= window.dojo.byId('variance')");
	}

	/**
	 *
	 * @return 0 - if indicator type = goal, 1 - if indicator type = variance
	 */
	public  MeasureIndicatorTypeEnum getIndicatorTypeType() {

		boolean isGoal = isIndicatorTypeGoal();
		boolean isVariance = isIndicatorTypeVariance();
		// measure can't be both manual and formula
		Assert.assertNotSame(isGoal, isVariance,
				"We can't have both goal and variance indicator types");
		return (isGoal) ? MeasureIndicatorTypeEnum.GOAL
				: MeasureIndicatorTypeEnum.VARIANCE;
	}


	public  String getName() {

		DefaultSelenium seleniumDriver = driver;
		return seleniumDriver.getEval("window.dojo.byId('name').value");
	}

	/**
	 * Get description value
	 *
	 * @return description value
	 */
	public  String getDescription() {
		DefaultSelenium seleniumDriver = driver;
		return seleniumDriver.getEval("window.dojo.byId('description').value");
	}



	/**
	 * Cancel changes on the page with upper button "Cancel"<br>
	 * Page will reloaded but you mustn't use Selenium method
	 * waitForPageToLoad(...) after invoking current method
	 */
	public  void cancelChangesWithUpperButton() {

		log.debug("click upper button Cancel");
		driver.click(
				"dom=window.dojo.query('input[value=Cancel]')[0];");
		driver.waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Cancel changes on the page with bottom button "Cancel"<br>
	 * Page will reloaded but you mustn't use Selenium method
	 * waitForPageToLoad(...) after invoking current method
	 */
	public  void cancelChangesWithBottomButton() {

		log.debug("click upper bottom Cancel");
		driver.click(
				"dom=window.dojo.query('input[value=Cancel]')[1];");
		driver.waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Submit changes on the page with upper button "Submit"<br>
	 * Page will reloaded.<br>
	 * You mustn't use Selenium method waitForPageToLoad(...) after invoking
	 * current method
	 *
	 */
	public  void submitChangesWithUpperButtonOnCreating() {
		driver.click(
				"dom=window.dojo.query('#content input[value=Submit]')[0];");
		driver.waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Submit changes and push "Yes" in the dialog "Save Measure"\"Save Measure
	 * Template" that appears on pushing button "Submit".<br>
	 * Use this method only after editing existed measure template<br>
	 * Page will reloaded but you mustn't use Selenium method
	 * waitForPageToLoad(...) after invoking current method
	 *
	 */
	public  void submitChangesWithUpperButtonForEditedM() {
		driver.click("//input[@value='Submit']");
		String locator = "dom= window.dojo.query('#confirmDialog [value*=\"Yes\"]')[0];";
		driver.click(locator);
		driver.waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());
	}

	/**
	 * Submit changes and push "No" in the dialog "Save Measure"\"Save Measure
	 * Template" that appears on pushing button "Submit"<br>
	 * Use this method only after editing existed measure template<br>
	 * Page will reloaded but you mustn't use Selenium method
	 * waitForPageToLoad(...) after invoking current method
	 *
	 */
	public  void pushButtonNoInSaveDialog() {
		submitChangesWithUpperButtonOnCreating();
		String locator = "dom= window.dojo.query('#confirmDialog [value*=\"No\"]')[0];";
		driver.click(locator);
	}


	/**
	 * Assert if loaded measure/measure template editing/adding page
	 */
	public  void assertIsLoadedPageEdit() {
		Assert.assertEquals((isLoadedPageEdit()), true,
				"You must navigate MT or M add/edit page");
	}



	/**
	 * Get value for field Units
	 *
	 * @return
	 */
	public  String getUnits() {
		return driver.getEval("window.dojo.byId('units').value");
	}

	/**
	 * Get value for select box Display Format: 0-value<br>
	 * 1-float<br>
	 * 2-monetary<br>
	 * 3-percent<br>
	 *
	 * @return index of the selected display format
	 */
	public  int getDisplayFormat() {

		return new Integer(driver.getEval(
				"window.dojo.byId('field_format').value"));
	}

	/**
	 * Get value for select box Effective Dates
	 *
	 * 0 - Absolute<br>
	 * 1 - Active Project<br>
	 * 2 - Project Lifetime<br>
	 * 3 - Baseline/Target Dates<br>
	 * 4 - Always<br>
	 *
	 * @return index of the selected effective date
	 */
	public  int getEffectiveDates() {

		return new Integer(driver.getEval(
				"window.dojo.byId('effectiveDates').value"));
	}

	/**
	 * @return selected Reminder Schedule radio button<br>
	 *         0 - Never<br>
	 *         1 - Daily<br>
	 *         2 - Weekly<br>
	 *         3 - Monthly<br>
	 *         4 - Quarterly<br>
	 *         -1 - in other cases
	 */
	public  int getScheduleReminderValue() {

		Assert.assertEquals(isDataCollectionManual(), true,
				"You must select data collection = manual.");

		boolean isNever = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[0];");
		boolean isDaily = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[1];");
		boolean isWeekly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[2];");
		boolean isMonthly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[3];");
		boolean isQuarterly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[4];");

		Assert
				.assertEquals(
						isNever && isDaily && isWeekly && isMonthly
								&& isQuarterly,
						false,
						"We can't have schedules Never, Daily, Weekly, Monthly, Quarterly selected together");

		return (isNever) ? MeasureShedulesEnum.NEVER.getIndex()
				: (isDaily) ? MeasureShedulesEnum.DAILY.getIndex()
						: (isWeekly) ? MeasureShedulesEnum.WEEKLY.getIndex()
								: (isMonthly) ? MeasureShedulesEnum.MONTHLY
										.getIndex()
										: (isQuarterly) ? MeasureShedulesEnum.QUARTERLY
												.getIndex()
												: -1;

	}

	/**
	 * @return selected Test Schedule radio button<br>
	 *         0 - Never<br>
	 *         1 - Daily<br>
	 *         2 - Weekly<br>
	 *         3 - Monthly<br>
	 *         4 - Quarterly<br>
	 *         -1 - in other cases
	 */
	public  int getScheduleTestValue() {

		Assert.assertEquals(isDataCollectionFormula(), true,
				"You must select data collection = manual.");

		boolean isNever = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup]')[0];");
		boolean isDaily = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup]')[1];");
		boolean isWeekly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup]')[2];");
		boolean isMonthly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup]')[3];");
		boolean isQuarterly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup]')[4];");

		Assert
				.assertEquals(
						isNever && isDaily && isWeekly && isMonthly
								&& isQuarterly,
						false,
						"We can't have schedules Never, Daily, Weekly, Monthly, Quarterly selected together");

		return (isNever) ? MeasureShedulesEnum.NEVER.getIndex()
				: (isDaily) ? MeasureShedulesEnum.DAILY.getIndex()
						: (isWeekly) ? MeasureShedulesEnum.WEEKLY.getIndex()
								: (isMonthly) ? MeasureShedulesEnum.MONTHLY
										.getIndex()
										: (isQuarterly) ? MeasureShedulesEnum.QUARTERLY
												.getIndex()
												: -1;

	}

	/**
	 * @return selected History Schedule radio button<br>
	 *         0 - Never<br>
	 *         1 - Daily<br>
	 *         2 - Weekly<br>
	 *         3 - Monthly<br>
	 *         4 - Quarterly<br>
	 *         -1 - in other cases
	 */
	public  int getScheduleHistoryValue() {

		Assert.assertSame(isDataCollectionFormula(), true,
				"You must select data collection = manual.");

		boolean isNever = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[0];");
		boolean isDaily = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[1];");
		boolean isWeekly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[2];");
		boolean isMonthly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[3];");
		boolean isQuarterly = driver.isChecked(
				"dom= window.dojo.query('[name=RadioGroup_0]')[4];");

		Assert
				.assertEquals(
						isNever && isDaily && isWeekly && isMonthly
								&& isQuarterly,
						false,
						"We can't have schedules Never, Daily, Weekly, Monthly, Quarterly selected together");

		return (isNever) ? MeasureShedulesEnum.NEVER.getIndex()
				: (isDaily) ? MeasureShedulesEnum.DAILY.getIndex()
						: (isWeekly) ? MeasureShedulesEnum.WEEKLY.getIndex()
								: (isMonthly) ? MeasureShedulesEnum.MONTHLY
										.getIndex()
										: (isQuarterly) ? MeasureShedulesEnum.QUARTERLY
												.getIndex()
												: -1;
	}


	public  void typeScheduleTestTimeOfDay(String timeOfDay){
		driver.type("dom=window.dojo.byId('PropertySelection')",timeOfDay);
	}
}
