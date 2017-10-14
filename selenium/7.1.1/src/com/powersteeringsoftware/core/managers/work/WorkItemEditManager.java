package com.powersteeringsoftware.core.managers.work;

import org.testng.Assert;

import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;

public class WorkItemEditManager {

	public static final String WORK_ITEM_SUMMARY_URL = "/project/Summary1.epage?sp=Ufs";

	protected WorkItem wi;

	public WorkItemEditManager(WorkItem _wi){
		wi=_wi;
	}

	public void navigateSummaryPage(){
		if(null==wi.getUid()){
			Assert.fail("Work item doesn't have udintificator.");
		}
		if(!isLoadedSummaryPage()){
			SeleniumDriverSingleton.getDriver().open(getSummaryPageURL());
		}
	}

	public String getSummaryPageURL(){
		return WORK_ITEM_SUMMARY_URL+wi.getUid();
	}

	public boolean isLoadedSummaryPage(){
		return false;
	}


}
