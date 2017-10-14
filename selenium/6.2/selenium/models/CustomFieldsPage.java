package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.CustomFieldsTemplateHolder;
import test.service.Config;

public class CustomFieldsPage{

	private MySeleniumDriver mydriver;
	private String contextPath;

	public CustomFieldsPage(BrowserModel browserModel) throws Exception {
		this.contextPath = Config.getInstance().getContextPath();
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisCustomFieldsPage() {
		return mydriver.getTitle().contains("Custom Fields");
	}

	public boolean isJSPFrameWorkingOk() {
		return mydriver.isElementPresent("//body//iframe//form");
	}

	public void createCustomFieldsTemplate(String templateName, String description, CustomFieldsTemplateHolder cftBean) {
		//mydriver.open(contextPath + "/admin/data_template_edit.jsp");
		mydriver.selectFrame("dom=window.frames['jspFrame']");
		int contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementsByTagName('a').length"));
		int i = 0;
		String linkHref;
		do {
			linkHref = mydriver.getEval("window.document.getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
			} while((linkHref.indexOf("data_template_edit.jsp") < 0) && (i < contentLinksCount));
		mydriver.click("dom=document.getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");


		//mydriver.waitForPageToLoad("30000");


		mydriver.type("dom=document.getElementsByName('mockup.name')[0]", templateName);
		mydriver.type("dom=document.getElementsByName('mockup.description')[0]", description);
		if (cftBean.getAssocWithEvents()) mydriver.check("dom=document.getElementsByName('object.type.ProjectEvent')[0]");
		if (cftBean.getAssocWithMSPProjects()) mydriver.check("dom=document.getElementsByName('object.type.MSPContainer')[0]");
		if (cftBean.getAssocWithMilestones()) mydriver.check("dom=document.getElementsByName('object.type.Milestone')[0]");
		if (cftBean.getAssocWithTemplates()) mydriver.check("dom=document.getElementsByName('object.type.Template')[0]");
		if (cftBean.getAssocWithFolders()) mydriver.check("dom=document.getElementsByName('object.type.FileFolder')[0]");
		if (cftBean.getAssocWithWorkItems()) mydriver.check("dom=document.getElementsByName('object.type.Work')[0]");
		if (cftBean.getAssocWithUnexpWorkItems()) mydriver.check("dom=document.getElementsByName('object.type.UnexpandedWork')[0]");
		if (cftBean.getAssocWithGatedProjects()) mydriver.check("dom=document.getElementsByName('object.type.Tollgate')[0]");
		if (cftBean.getAssocWithGates()) mydriver.check("dom=document.getElementsByName('object.type.Checkpoint')[0]");
		if (cftBean.getAssocWithUsers()) mydriver.check("dom=document.getElementsByName('object.type.User')[0]");
		if (cftBean.getAssocWithGroups()) mydriver.check("dom=document.getElementsByName('object.type.Group')[0]");
		if (cftBean.getStatusReport()) mydriver.check("dom=document.getElementsByName('object.type.StatusReportHandler')[0]");

		contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementsByTagName('a').length"));
		i = 0;
		do {
			linkHref = mydriver.getEval("window.document.getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
			} while((linkHref.indexOf("javascript:postEvent('submit')") < 0) & (i < contentLinksCount));
		mydriver.click("dom=document.getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");
		mydriver.selectFrame("relative=parent");
	}

	public void addCustomYesNoButton(String customFieldsTemplateName, String customFieldName){
		//mydriver.open(contextPath + "/admin/data_templates.jsp");
		mydriver.selectFrame("dom=window.frames['jspFrame']");
		int contentLinksCount;
		int i;
		String linkHref;

		contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementsByTagName('a').length"));
		i = 0;
		do {
			linkHref = mydriver.getEval("window.document.getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
			} while((linkHref.indexOf(customFieldsTemplateName) < 0) & (i < contentLinksCount));
		mydriver.click("dom=document.getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");

		contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementsByTagName('a').length"));
		i = 0;
		do {
			linkHref = mydriver.getEval("window.document.getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
			} while((linkHref.indexOf("data_field_edit.jsp") < 0) & (i < contentLinksCount));
		mydriver.click("dom=document.getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");

		mydriver.type("dom=document.getElementsByName('fieldName')[0]", customFieldName);
		mydriver.click("dom=document.getElementsByName('fieldRequired')[1]");
		mydriver.click("dom=document.getElementsByName('fieldOnReport')[1]");

		mydriver.select("dom=document.getElementById('windowedControl')", "value=radio");
		mydriver.waitForPageToLoad("30000");

		contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementsByTagName('a').length"));
		i = 0;
		do {
			linkHref = mydriver.getEval("window.document.getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
			} while((linkHref.indexOf("javascript:postEvent('submit')") < 0) & (i < contentLinksCount));
		mydriver.click("dom=document.getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");
		mydriver.selectFrame("relative=parent");
	}

	public void addCustomCheckbox(String customFieldsTemplateName, String customFieldName, String checkboxValues){
		mydriver.open(contextPath + "/admin/data_templates.jsp");
		mydriver.click("//a[text()='" + customFieldsTemplateName + "']");
		mydriver.waitForPageToLoad("30000");
		mydriver.click("//a[contains(@href,'data_field_edit.jsp')]");
		mydriver.waitForPageToLoad("30000");
		mydriver.type("//input[@name='fieldName']", customFieldName);
		mydriver.click("//input[@value='false' and @name='fieldRequired']");
		mydriver.click("//input[@value='false' and @name='fieldOnReport']");
		mydriver.select("//select[@id='windowedControl']", "value=check");
		mydriver.waitForPageToLoad("30000");
		mydriver.type("//textarea[@name='fieldCheck']", checkboxValues);
		mydriver.click("//a[@href=\"javascript:postEvent(\'submit\')\"]");
		mydriver.waitForPageToLoad("30000");
	}

}
