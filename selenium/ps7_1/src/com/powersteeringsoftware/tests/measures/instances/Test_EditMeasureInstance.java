package com.powersteeringsoftware.tests.measures.instances;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.edit.MeasureInstanceEditPageAdapter;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.objects.measures.MeasureShedulesEnum;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;

/**
 * Test editing measure.</br>
 *
 *
 * @author selyaev_ag
 *
 */
public class Test_EditMeasureInstance extends PSTest {
	Logger log = Logger.getLogger(Test_EditMeasureInstance.class);

	public Test_EditMeasureInstance(){
		name = "Edit measure";
	}

	public void run() {
		ManageMeasuresPageAdapter manageMeasuresAdapter = new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver());
		MeasureInstanceEditPageAdapter mEditAdapter = new MeasureInstanceEditPageAdapter(SeleniumDriverSingleton.getDriver());

		new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());

		manageMeasuresAdapter.pushCreate();

		// datas
		String mName = new TimeStampName("Measure").getTimeStampedName();
		String mDescription = mName +" description";
		String mUnits = "measure units";

		int displayFormat = 1; //Float
		int effectveDates = 4; //allways
		int scheduleDayInterval = 1; // first day
		String scheduleHour = "12:00 AM"; // oo:oo
		String effectiveDateStartDate="";
		String effectiveDateEndDate="";

		double threshold1 = 1000.0;
		double threshold2 = 1000.0;
		double threshold3 = 1000.0;
		double threshold4 = 1000.0;
//		String redMessage1 = "red 1";
//		String redMessage2 = "red 2";
//		String yellowMessage1 = "yellow 1";
//		String yellowMessage2 = "yellow 2";
//		String greenMessage = "green";
		//Measure Details
		mEditAdapter.setName(mName);
		mEditAdapter.setDescription(mDescription);
		mEditAdapter.setUnits(mUnits);
		//Collection Method
		mEditAdapter.setDataCollectionFormula(); //formula
		mEditAdapter.setDisplayFormat(displayFormat);
		mEditAdapter.setEffectiveDates(effectveDates, effectiveDateStartDate, effectiveDateEndDate);
		mEditAdapter.setScheduleTestDaily(scheduleDayInterval, scheduleHour);
		//Indicator Type
		mEditAdapter.setIndicatorTypeVariance();
		mEditAdapter.setThresholdVariance1(threshold1);
		mEditAdapter.setThresholdVariance2(threshold2);
		mEditAdapter.setThresholdVariance3(threshold3);
		mEditAdapter.setThresholdVariance4(threshold4);
//		MeasureEditPageCommons.setMessageRedVariance1(redMessage1);
//		MeasureEditPageCommons.setMessageRedVariance2(redMessage2);
//		MeasureEditPageCommons.setMessageYellowVariance1(yellowMessage1);
//		MeasureEditPageCommons.setMessageYellowVariance2(yellowMessage2);
//		MeasureEditPageCommons.setMessageGreenVariance(greenMessage);
		mEditAdapter.submitChangesWithUpperButtonOnCreating();
		//log.debug("T-MF-133 Step 3: Navigate manage page - is finished");

		/**
		 * Step 4: Edit created measure
		 */
		manageMeasuresAdapter.doActionEdit(mName);
		String _mName = new TimeStampName("Measure").getTimeStampedName();
		String _mDescription = "new Description";
		String _mUnits = "measure units 1";
		int _displayFormat = 0; //Integer
		int _effectveDates = 0; //absolute
		int _scheduleDayInterval = 2; // second day
		String _scheduleHour = "10:00 AM"; // 01:01
		String _effectiveDateStartDate="";
		String _effectiveDateEndDate="";
		double _threshold1 = 1001.1;
		double _threshold2 = 2001.1;
		double _threshold3 = 3001.1;
		double _threshold4 = 4001.1;
//		String _redMessage1 = "red 11";
//		String _redMessage2 = "red 21";
//		String _yellowMessage1 = "yellow 11";
//		String _yellowMessage2 = "yellow 21";
//		String _greenMessage = "green 1";

		//Measure Details
		mEditAdapter.setName(_mName);
		mEditAdapter.setDescription(_mDescription);
		mEditAdapter.setUnits(_mUnits);

		//Collection Method
		mEditAdapter.setDataCollectionFormula(); //formula
		mEditAdapter.setDisplayFormat(_displayFormat);
		mEditAdapter.setEffectiveDates(_effectveDates, _effectiveDateStartDate, _effectiveDateEndDate);
		mEditAdapter.setScheduleTestDaily(_scheduleDayInterval, _scheduleHour);

		//Indicator Type
		mEditAdapter.setThresholdVariance1(_threshold1);
		mEditAdapter.setThresholdVariance2(_threshold2);
		mEditAdapter.setThresholdVariance3(_threshold3);
		mEditAdapter.setThresholdVariance4(_threshold4);
//		MeasureEditPageCommons.setMessageRedVariance1(_redMessage1);
//		MeasureEditPageCommons.setMessageRedVariance2(_redMessage2);
//		MeasureEditPageCommons.setMessageYellowVariance1(_yellowMessage1);
//		MeasureEditPageCommons.setMessageYellowVariance2(_yellowMessage2);
//		MeasureEditPageCommons.setMessageGreenVariance(_greenMessage);

		mEditAdapter.submitChangesWithUpperButtonOnCreating();

		/**
		 * Step 5: Check edited values
		 */
		manageMeasuresAdapter.doActionEdit(_mName);

		Assert.assertEquals(mEditAdapter.getName().equals(_mName),true,"Field has not changed: Name");
		Assert.assertEquals(mEditAdapter.getDescription().equals(_mDescription),true,"Field has not changed: Description");
		Assert.assertEquals(mEditAdapter.getUnits().equals(_mUnits),true,"Field has not changed: Units");

		Assert.assertEquals(mEditAdapter.getDataCollectionType().getIndex(),1,"Field has not changed: Data Collection");
		Assert.assertEquals(mEditAdapter.getDisplayFormat(), _displayFormat,"Field has not changed: Display Format");
		Assert.assertEquals(mEditAdapter.getEffectiveDates(), _effectveDates,"Field has not changed: Effective Dates");
		Assert.assertEquals(mEditAdapter.getEffectiveDates(),_effectveDates, "Field has not changed: Effective Dates");

		Assert.assertEquals(mEditAdapter.getScheduleTestValue(),MeasureShedulesEnum.DAILY.getIndex());

		Assert.assertEquals(mEditAdapter.getThresholdVariance1(),_threshold1,"Different threshold values 1:");
		Assert.assertEquals(mEditAdapter.getThresholdVariance2(),_threshold2,"Different threshold values 2");
		Assert.assertEquals(mEditAdapter.getThresholdVariance3(),_threshold3,"Different threshold values 3");
		Assert.assertEquals(mEditAdapter.getThresholdVariance4(),_threshold4,"Different threshold values 4");
//		Assert.assertEquals(MeasureEditPageCommons.getMessageGreenVariance().equals(_greenMessage),true,"Different green messages");
//		Assert.assertEquals(MeasureEditPageCommons.getMessageRedVariance1().equals(_redMessage1),true,"Different red messages 1");
//		Assert.assertEquals(MeasureEditPageCommons.getMessageRedVariance2().equals(_redMessage2),true,"Different red messages 1");
//		Assert.assertEquals(MeasureEditPageCommons.getMessageYellowVariance1().equals(_yellowMessage1),true,"Different red messages 1");
//		Assert.assertEquals(MeasureEditPageCommons.getMessageYellowVariance2().equals(_yellowMessage2),true,"Different red messages 1");

		mEditAdapter.cancelChangesWithUpperButton();
	}
}

