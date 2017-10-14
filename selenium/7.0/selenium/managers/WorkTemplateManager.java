package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.*;

public class WorkTemplateManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public WorkTemplateManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void createNewWorkTemplate(WorkTemplate template, RootGatedProject rgP){
		logger.info("Creating a new work template '" + template.getName() + "' with gated project '" + rgP.getName() + "' as root");

		driver.click("dom=window.dojo.query('div#AdminMenu a[href*=WorkTemplates]')[0]");
		driver.waitForPageToLoad("30000");

		driver.selectFrame("dom=window.frames['jspFrame']");

		String jsQuery;

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*new_wizard.jsp\\?objectType=Template/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		//create new template
		driver.type("dom=document.getElementsByName('objectName')[0]", template.getName());
		//TODO type/choose the rest of the data for the create template form

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		driver.click("dom=window.document.getElementById('RelatedProjects').getElementsByTagName('table')[0].getElementsByTagName('a')[0]");
		driver.waitForPageToLoad("30000");

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/javascript:selectType\\('basic','Tollgate'\\)/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		//create gated root work

		driver.type("dom=document.getElementsByName('objectName')[0]", rgP.getName());
//		driver.type("dom=document.getElementsByName('objective')[0]", rgP.getObjective());

		if (rgP.isUseStatusReporting()) {
			driver.click("dom=document.getElementsByName('com.cinteractive.ps3.work.UpdateFrequency.isAvailable')[0]");
			if (rgP.getStatusReportingFrequency().equals(RootGatedProject.STATUS_REPORTING_FREQUENCY.NO_FREQUENCY))
				driver.select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=No Frequency");
			if (rgP.getStatusReportingFrequency().equals(RootGatedProject.STATUS_REPORTING_FREQUENCY.WEEKLY))
				driver.select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Weekly");
			if (rgP.getStatusReportingFrequency().equals(RootGatedProject.STATUS_REPORTING_FREQUENCY.BIWEEKLY))
				driver.select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Bi-Weekly");
			if (rgP.getStatusReportingFrequency().equals(RootGatedProject.STATUS_REPORTING_FREQUENCY.MONTHLY))
				driver.select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Monthly");
			if (rgP.getStatusReportingFrequency().equals(RootGatedProject.STATUS_REPORTING_FREQUENCY.QUARTERLY))
				driver.select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Quarterly");
		}

		if (!(null == rgP.isInheritPermissions())) {
			if (rgP.isInheritPermissions()){
				jsQuery = 	"var radios=document.getElementsByName('inheritPermissions');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			} else {
				jsQuery = 	"var radios=document.getElementsByName('inheritPermissions');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			}
		}

		if (!(null == rgP.isInheritControls())) {
			if (rgP.isInheritControls()){
				jsQuery = 	"var radios=document.getElementsByName('inheritControls');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			} else {
				jsQuery = 	"var radios=document.getElementsByName('inheritControls');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			}
		}

		if (!(null == rgP.isWebFolder())) {
			if (rgP.isWebFolder()){
				jsQuery = 	"var radios=document.getElementsByName('webdav.is_resource');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			} else {
				jsQuery = 	"var radios=document.getElementsByName('webdav.is_resource');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			}
		}

		if (!(null == rgP.isControlCost())) {
			if (rgP.isControlCost()){
				jsQuery = 	"var radios=document.getElementsByName('controlCost');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			} else {
				jsQuery = 	"var radios=document.getElementsByName('controlCost');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
				driver.click("dom=" + jsQuery);
			}
		}

		//choose process

		jsQuery = 	"var procRads=document.getElementsByName('tag_sequence');" +
					"for (var i=0; i<procRads.length; i++){" +
					"if (/" + rgP.getProcessName() + "/.test(procRads[i].parentNode.parentNode.getElementsByTagName('span')[0]" +
					".parentNode.firstChild.nodeValue)) {procRads[i];break;}}";
		driver.click("dom=" + jsQuery);

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		//TODO Add changing the date constraints

		//should be on summary page now
		driver.selectFrame("relative=parent");
	}

}
