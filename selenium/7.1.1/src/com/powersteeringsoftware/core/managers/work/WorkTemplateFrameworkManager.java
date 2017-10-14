package com.powersteeringsoftware.core.managers.work;

import org.apache.commons.lang.StringUtils;

import com.powersteeringsoftware.core.objects.works.WorkTemplateContainer;
import com.powersteeringsoftware.core.objects.works.WorkTemplateFramework;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.waiters.ElementBecomePresentWaiter;


public class WorkTemplateFrameworkManager {

	public static final String NAME_DATE_CONSTRAINT_TYPE = "ps3.work.schedule.Constraint.type";
	public static final String NAME_DATE_CONSTRAINT_END = "ps3.work.schedule.Constraint.end";
	public static final String NAME_DATE_CONSTRAINT_START = "ps3.work.schedule.Constraint.start";

	public static final String DOM_LOCATOR_CHILD_FRAME_NAME = "dom=window.frames['jspFrame']";
	public static final String LOCATOR_PARENT_FRAME_NAME = "relative=top";


	public WorkTemplateFrameworkManager() {
		super();
	}

	private WorkTemplateFramework rootGatedProject;
	private WorkTemplateContainer workTemplate;

	public WorkTemplateFramework getRootGatedProject() {
		return (null!=this.rootGatedProject)?rootGatedProject:new WorkTemplateFramework("No name", "No name");
	}

	/**
	 * Create new work template container and root work template.
	 *
	 * @param wTemplate
	 * @param rgProject
	 */
	public void create(WorkTemplateContainer wTemplate, WorkTemplateFramework rgProject) {
		rootGatedProject = rgProject;
		workTemplate = wTemplate;

		new WorkTemplateContainerManager().createWorkTemplateContainer(workTemplate);

		setHTMLFrameChild();

		pushCreateNew();

		setWorkType();

		setName();

		setUseStatusReporting();

		setInheritPermissions();

		setInheritControls();

		setManualScheduled();

		setWebFolder();

		setControlCost();

		setScheduleConstraint();

		setDateConstraints();

		setProcess();

		submitChanges();

		setHTMLFrameParent();
	}

	/**
	 * Select process by default (this method select first process in the
	 * process list)
	 */
	private void setProcess() {
		String pattern = (StringUtils.isBlank(this.rootGatedProject.getProcessName())) ? ".*":this.rootGatedProject.getProcessName();

		String jsQuery = "var procRads=document.getElementsByName('tag_sequence');"
				+ "for (var i=0; i<procRads.length; i++){"
				+ "if (/"
				+ pattern
				+ "/.test(procRads[i].parentNode.parentNode.getElementsByTagName('span')[0]"
				+ ".parentNode.firstChild.nodeValue)) {procRads[i];break;}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
	}

	private void submitChanges() {
		String jsQuery = "var links=document.getElementsByTagName('a');" +
		"for (var i=0; i<links.length; i++){if (/javascript:postEvent\\('submit'\\)/.test(links[i].href)){links[i];break}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		//SeleniumDriverSingleton.getDriver().waitForPageToLoad("30000");
		SeleniumDriverSingleton.getDriver().waitForFrameToLoad(DOM_LOCATOR_CHILD_FRAME_NAME,CoreProperties.getWaitForElementToLoadAsString());
	}

	private void setHTMLFrameParent() {
		//ElementBecomePresentWaiter.waitElementBecomePresent(LOCATOR_PARENT_FRAME_NAME);
		SeleniumDriverSingleton.getDriver().selectFrame(LOCATOR_PARENT_FRAME_NAME);
	}

	private void setHTMLFrameChild() {
		ElementBecomePresentWaiter.waitElementBecomePresent(DOM_LOCATOR_CHILD_FRAME_NAME);
		SeleniumDriverSingleton.getDriver().selectFrame(DOM_LOCATOR_CHILD_FRAME_NAME);
	}

	/**
	 * Check if date constrain already has been selected
	 *
	 * @return true if schedule constraint type selected and needed are equals,
	 *         false - in other case
	 */
	private boolean isScheduleConstraintsOptionEquals() {
		String label = SeleniumDriverSingleton.getDriver().getSelectedLabel(NAME_DATE_CONSTRAINT_TYPE);
		return label.equals(rootGatedProject.getScheduleConstraint().getValue());
	}

	private void setScheduleConstraint() {
		if(!isScheduleConstraintsOptionEquals()){
			SeleniumDriverSingleton.getDriver().select(NAME_DATE_CONSTRAINT_TYPE,
					rootGatedProject.getScheduleConstraint().getPattern());

			SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		}
	}

	private void setDateConstraints() {
		if(rootGatedProject.isDateConstraintEndNeeded() && isDateConstraintEndVisible()){
			SeleniumDriverSingleton.getDriver().type(NAME_DATE_CONSTRAINT_END,
					rootGatedProject.getDateConstraintEndValue());
		}

		if(rootGatedProject.isDateConstraintStartNeeded() && isDateConstraintStartVisible()){
			SeleniumDriverSingleton.getDriver().type(NAME_DATE_CONSTRAINT_START,
					rootGatedProject.getDateConstraintStartValue());
		}
	}

	private boolean isDateConstraintEndVisible() {
		return SeleniumDriverSingleton.getDriver().isVisible(NAME_DATE_CONSTRAINT_END);
	}

	private boolean isDateConstraintStartVisible() {
		return SeleniumDriverSingleton.getDriver().isVisible(NAME_DATE_CONSTRAINT_START);

	}

	private void pushCreateNew() {
		SeleniumDriverSingleton.getDriver().click("dom=window.document.getElementById('RelatedProjects').getElementsByTagName('table')[0].getElementsByTagName('a')[0]");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());//create template in the container
	}

	private void setWorkType() {
		String jsQuery = 	"var links=document.getElementsByTagName('a');" +
		"for (var i=0; i<links.length; i++){if (/javascript:selectType\\('basic','Tollgate'\\)/.test(links[i].href)){links[i];break}}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());//select "Gated Project"

	}

	private void setName() {
		SeleniumDriverSingleton.getDriver().type("dom=document.getElementsByName('objectName')[0]", this.rootGatedProject.getName());
	}

	private void setUseStatusReporting() {
		if (this.rootGatedProject.isUseStatusReporting()) {
			SeleniumDriverSingleton.getDriver().click("dom=document.getElementsByName('com.cinteractive.ps3.work.UpdateFrequency.isAvailable')[0]");
			if (this.rootGatedProject.getStatusReportingFrequency().equals(WorkTemplateFramework.StatusReportingFrequency.NO_FREQUENCY))
				SeleniumDriverSingleton.getDriver().select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=No Frequency");
			if (this.rootGatedProject.getStatusReportingFrequency().equals(WorkTemplateFramework.StatusReportingFrequency.WEEKLY))
				SeleniumDriverSingleton.getDriver().select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Weekly");
			if (this.rootGatedProject.getStatusReportingFrequency().equals(WorkTemplateFramework.StatusReportingFrequency.BIWEEKLY))
				SeleniumDriverSingleton.getDriver().select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Bi-Weekly");
			if (this.rootGatedProject.getStatusReportingFrequency().equals(WorkTemplateFramework.StatusReportingFrequency.MONTHLY))
				SeleniumDriverSingleton.getDriver().select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Monthly");
			if (this.rootGatedProject.getStatusReportingFrequency().equals(WorkTemplateFramework.StatusReportingFrequency.QUARTERLY))
				SeleniumDriverSingleton.getDriver().select("dom=document.getElementsByName('updateFrequency_options')[0]", "value=Quarterly");
		}
	}

	private void setInheritPermissions() {
		String jsQuery;

		if (this.rootGatedProject.isInheritPermissions()) {
			jsQuery = "var radios=document.getElementsByName('inheritPermissions');"
					+ "for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
		} else {
			jsQuery = "var radios=document.getElementsByName('inheritPermissions');"
					+ "for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
		}

		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
	}

	private void setInheritControls() {
		String jsQuery;
		if (this.rootGatedProject.isInheritControls()) {
			jsQuery = "var radios=document.getElementsByName('inheritControls');"
					+ "for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
		} else {
			jsQuery = "var radios=document.getElementsByName('inheritControls');"
					+ "for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
		}
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
	}

	private void setWebFolder() {
		String jsQuery;

			if (this.rootGatedProject.isWebFolder()){
				jsQuery = 	"var radios=document.getElementsByName('webdav.is_resource');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
			} else {
				jsQuery = 	"var radios=document.getElementsByName('webdav.is_resource');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
			}
			SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
	}

	private void setControlCost() {
		String jsQuery;

		if (!(false == this.rootGatedProject.isControlCost())) {
			if (this.rootGatedProject.isControlCost()){
				jsQuery = 	"var radios=document.getElementsByName('controlCost');" +
							"for (var i=0; i<radios.length; i++){if (radios[i].checked) {radios[i];break;}}";
			} else {
				jsQuery = 	"var radios=document.getElementsByName('controlCost');" +
							"for (var i=0; i<radios.length; i++){if (!radios[i].checked) {radios[i];break;}}";
			}
			SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);
		}
	}

	private void setManualScheduled(){
		if (this.rootGatedProject.isManualScheduled()) {
			SeleniumDriverSingleton.getDriver().click("manualScheduling");
		} else {
			SeleniumDriverSingleton.getDriver().click("//input[@name='manualScheduling' and @value='false']");
		};
	}
}