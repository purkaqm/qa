package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;
import test.service.Config;

public class WorkTemplatesPage{
	public enum DATE_CONSTRAINTS {
		AS_SOON_AS_POSSIBLE,
		AS_LATE_AS_POSSIBLE,
		MUST_START_ON,
		MUST_FINISH_ON,
		START_NO_EARLIER_THEN,
		START_NO_LATER_THEN,
		FINISH_NO_EARLIER_THEN,
		FINISH_NO_LATER_THEN,
		NO_CHANGE
	}

	private MySeleniumDriver mydriver;
	private BrowserModel mybrowser;
	private String contextPath;

	public WorkTemplatesPage(BrowserModel browserModel) throws Exception {
		this.contextPath = Config.getInstance().getContextPath();
		this.mydriver = browserModel.getDriver();
		this.mybrowser = browserModel;
	}

	public boolean isThisWorkTemplatesPage() {
		return mydriver.getTitle().contains("Work Templates");
	}

	public boolean isJSPFrameWorkingOk() {
		return mydriver.isElementPresent("//body//iframe//form");
	}

	public void createNewWorkTemplate(WorkTemplateHolder wtBean) {
		//mydriver.open(contextPath + "/admin/all_templates.jsp");
		//mydriver.selectWindow("jspwindow");
		mydriver.waitForPageToLoad("30000");
		mydriver.click("//a[contains(@href,'new_wizard.jsp')]");
		mydriver.waitForPageToLoad("30000");
		if (mydriver.isElementPresent("//a[@href=\"javascript:showTab('basic')\"]")) {
			mydriver.click("//a[@href=\"javascript:showTab('basic')\"]");
			mydriver.waitForPageToLoad("30000");
		}
		mydriver.type("//input[@name='objectName']", wtBean.getWorkTemplateName());
		mydriver.type("//textarea[@name='objective']", wtBean.getWorkTemplateObjective());
		mydriver.click("//a[@href=\"javascript:postEvent('submit')\"]");
		mydriver.waitForPageToLoad("30000");
		//mydriver.close();
	}

	public void createRootGatedProject(WorkTemplateHolder wtHolder, WorkTemplateRootGatedProjectHolder wtrgpBean) throws Exception {
		//mydriver.open(contextPath + "/admin/all_templates.jsp");
		//mydriver.selectWindow("jspwindow");
		mydriver.waitForPageToLoad("30000");
		mydriver.click("//span[contains(text(),'" + wtHolder.getWorkTemplateName() + "')]/../a[contains(@href,'/templates/index_template.jsp')]");
		mydriver.waitForPageToLoad("30000");
		mydriver.click("//td[@class='bgConfirm']//a[contains(@href,'/projects/new_wizard.jsp')]");
		mydriver.waitForPageToLoad("30000");

		mydriver.click("//a[contains(@href,'Tollgate')]");
		mydriver.waitForPageToLoad("30000");

		mydriver.type("//input[@name='objectName']", wtrgpBean.getWorkName());
		mydriver.type("//textarea[@name='objective']", wtrgpBean.getObjectives());

		if (wtrgpBean.getUseStatusReporting()) {
			mydriver.click("//input[@name='com.cinteractive.ps3.work.UpdateFrequency.isAvailable']");
			mydriver.select("//select[@name='updateFrequency_options']", "value=" + wtrgpBean.getStatusReportingFrequency());
		}

		if (!(null == wtrgpBean.getInheritPermissions())) {
			if (wtrgpBean.getInheritPermissions()){
				mydriver.click("//input[@name='inheritPermissions' and @value='true']");
			} else {
				mydriver.click("//input[@name='inheritPermissions' and @value='false']");
			}
		}

		if (!(null == wtrgpBean.getInheritControls())) {
			if (wtrgpBean.getInheritControls()){
				mydriver.click("//input[@name='inheritControls' and @value='true']");
			} else {
				mydriver.click("//input[@name='inheritControls' and @value='false']");
			}
		}

		if (!(null == wtrgpBean.getWebFolder())) {
			if (wtrgpBean.getWebFolder()){
				mydriver.click("//input[@name='webdav.is_resource' and @value='true']");
			} else {
				mydriver.click("//input[@name='webdav.is_resource' and @value='false']");
			}
		}

		if (!(null == wtrgpBean.getControlCost())) {
			if (wtrgpBean.getControlCost()){
				mydriver.click("//input[@name='controlCost' and @value='true']");
			} else {
				mydriver.click("//input[@name='controlCost' and @value='false']");
			}
		}

		if (!(null == wtrgpBean.getManualSchedule())) {
			if (wtrgpBean.getManualSchedule()){
				mydriver.click("//input[@name='manualScheduling' and @value='true']");
			} else {
				mydriver.click("//input[@name='manualScheduling' and @value='false']");
			}
			//mydriver.close();
		}

		mydriver.click("//td[contains(text(),'" + wtrgpBean.getProcess() + "')]/..//input");

		//TODO Add changing the date constraints

		mydriver.click("//a[@href=\"javascript:postEvent('submit')\"]");
		mydriver.waitForPageToLoad("30000");

	}

}
