package test.testng;

import org.testng.annotations.*;

import selenium.formholders.*;
import selenium.models.*;
import test.service.*;

import selenium.models.WorkTemplatesPage.DATE_CONSTRAINTS;

public class EmptyContentFiller {
	BrowserModel browser;

	@BeforeClass
	public void setUp() throws Exception {
		PSHomeServletManager psmanager = new PSHomeServletManager();
		//ResinJMXManager resinJMXManager = new ResinJMXManager();
		ContextDBManager dbmanager = new ContextDBManager();
		SilentHTTPManager silentHTTPManager = new SilentHTTPManager();

		//psmanager.clearContextCache();
		//dbmanager.killActiveConnectionAndRestoreDB();
		//resinJMXManager.serverRestart();
		//silentHTTPManager.waitUntilResponseCodeIs200("60000");
		//psmanager.clearContextCache();

		browser = new BrowserModel("*iehta");
	}

	@Test
	public void login() throws Exception {
		LoginPage loginPage = new LoginPage(browser);
		HomePage homePage = new HomePage(browser);

		assert loginPage.isThisLoginPage();
		assert loginPage.isLoginFormPresent();
		loginPage.loginAs("admino", "qwer");
		assert homePage.isThisHomePage();
	}

	@Test(dependsOnMethods = { "login" })
//	public void createCustomTags() throws Exception{
//		NavigationBar navigationBar = new NavigationBar(browser);
//		AdminPopup adminPopup = new AdminPopup(browser);
//		TagsListPage tagsListPage = new TagsListPage(browser);
//		TagPage tagPage = new TagPage(browser);
//		UpdateTagValuesPage updateTagValuesPage = new UpdateTagValuesPage(browser);
//
//		TagHolder simpleTag = new TagHolder("Planet","This should be a non-hierarchical tag.");
//		simpleTag.addTypeOfPeople("Users");
//		simpleTag.addTypeOfWorks("Work Items");
//		simpleTag.addTypeOfWorks("Gated Projects");
//		simpleTag.addTagValue("Earth");
//		simpleTag.addTagValue("Mercury");
//		simpleTag.addTagValue("Venus");
//		simpleTag.addTagValue("Jupiter");
//		simpleTag.addTagValue("Mars");
//		simpleTag.addTagValue("Saturn");
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickTags();
//
//		assert tagsListPage.isThisTagsPage();
//		tagsListPage.addNewTag(simpleTag);
//		assert tagPage.isThisTagPage();
//		tagPage.clickAddUpdateValuesLink();
//		assert updateTagValuesPage.isThisUpdateTagValuesPagePage();
//		updateTagValuesPage.addTagValuesNames(simpleTag);
//		assert tagPage.isThisTagPage();
//
//		TagHolder hierarchicalTag = new TagHolder("Location","This should be a hierarchical, multi-select tag.");
//		hierarchicalTag.addTypeOfPeople("Users");
//		hierarchicalTag.addTypeOfWorks("Work Items");
//		hierarchicalTag.addTypeOfWorks("Gated Projects");
//		hierarchicalTag.setPropMultiple(true);
//		hierarchicalTag.setPropHierarchical(true);
//		hierarchicalTag.addTagValue("North America");
//		hierarchicalTag.addTagValue("United States", "North America");
//		hierarchicalTag.addTagValue("Massachusetts", "United States");
//		hierarchicalTag.addTagValue("Boston", "Massachusetts");
//		hierarchicalTag.addTagValue("Asia");
//		hierarchicalTag.addTagValue("Russia","Asia");
//		hierarchicalTag.addTagValue("Moscow","Russia");
//		hierarchicalTag.addTagValue("Australia");
//		hierarchicalTag.addTagValue("Sydney","Australia");
//		hierarchicalTag.addTagValue("Melbourne","Australia");
//		hierarchicalTag.addTagValue("Perth","Australia");
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickTags();
//
//		assert tagsListPage.isThisTagsPage();
//		tagsListPage.addNewTag(hierarchicalTag);
//		assert tagPage.isThisTagPage();
//		tagPage.clickAddUpdateValuesLink();
//		assert updateTagValuesPage.isThisUpdateTagValuesPagePage();
//		updateTagValuesPage.addTagValuesNames(hierarchicalTag);
//		assert tagPage.isThisTagPage();
//		tagPage.clickAddUpdateValuesLink();
//		assert updateTagValuesPage.isThisUpdateTagValuesPagePage();
//		updateTagValuesPage.setTagValuesParents(hierarchicalTag);
//		assert tagPage.isThisTagPage();
//	}
//
//	@Test(dependsOnMethods = { "createCustomTags" })
	public void createCustomFields() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		AdminPopup adminPopup = new AdminPopup(browser);
		CustomFieldsPage customFieldsPage = new CustomFieldsPage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickAdmin();
		assert adminPopup.isAdminPopupVisible();
		adminPopup.clickCustomFields();
		assert customFieldsPage.isThisCustomFieldsPage();

		CustomFieldsTemplateHolder cftBean = new CustomFieldsTemplateHolder();
		cftBean.setAssocWithTemplates(true);
		cftBean.setAssocWithWorkItems(true);
		cftBean.setAssocWithUnexpWorkItems(true);
		cftBean.setAssocWithGatedProjects(true);

		customFieldsPage.createCustomFieldsTemplate("General custom fields", "This set of custom fields is for projects and gated projects", cftBean);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickAdmin();
		assert adminPopup.isAdminPopupVisible();
		adminPopup.clickCustomFields();
		assert customFieldsPage.isThisCustomFieldsPage();


		customFieldsPage.addCustomYesNoButton("General custom fields", "Sample Yes-No button");
		//customFieldsPage.addCustomCheckbox("General custom fields", "Sample Checkboxes", "Red\nGreen\nBlue");
	}

//	@Test(dependsOnMethods = { "createCustomFields" })
//	public void createProcesses() throws Exception{
//		ProcessesPage processesPage = new ProcessesPage(browser);
//
//		ProcessHolder DMAICBean = new ProcessHolder("DMAIC", "This is a sample DMAIC process created by an automated script");
//		DMAICBean.addPhase("Define",null);
//		DMAICBean.addPhase("Measure",null);
//		DMAICBean.addPhase("Analyze",null);
//		DMAICBean.addPhase("Improve",null);
//		DMAICBean.addPhase("Control",null);
//		processesPage.addProcess(DMAICBean);
//
//		ProcessHolder CDDDRCBean = new ProcessHolder("CDDDRC", "This is a sample CDDDRC process created by an automated script");
//		CDDDRCBean.addPhase("Concept","10");
//		CDDDRCBean.addPhase("Define","20");
//		CDDDRCBean.addPhase("Design","45");
//		CDDDRCBean.addPhase("Development","75");
//		CDDDRCBean.addPhase("Release","95");
//		CDDDRCBean.addPhase("Close","100");
//		processesPage.addProcess(CDDDRCBean);
//	}
//
//	@Test(dependsOnMethods = { "createProcesses" })
//	public void createWorkTemplates() throws Exception{
//		NavigationBar navigationBar = new NavigationBar(browser);
//		AdminPopup adminPopup = new AdminPopup(browser);
//		WorkTemplatesPage workTemplatesPage = new WorkTemplatesPage(browser);
//
//		browser.openContextPath();
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickWorkTemplates();
//
//		WorkTemplateHolder wTDMAIC = new WorkTemplateHolder("DMAIC Work Template", "This is a DMAIC Work Template created by an automated script.");
//		workTemplatesPage.createNewWorkTemplate(wTDMAIC);
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickWorkTemplates();
//
//		WorkTemplateRootGatedProjectHolder wTRGPDMAIC = new WorkTemplateRootGatedProjectHolder("DMAIC Root Gated Project","DMAIC objectives","DMAIC");
//		wTRGPDMAIC.setStatusReporting("No Frequency");
//		//FIGNJA S KONTROLSAMI!!!!!!
//		wTRGPDMAIC.setInheritPermissions(true);
//		wTRGPDMAIC.setInheritControls(true);
//		wTRGPDMAIC.setWebFolder(false);
//		wTRGPDMAIC.setControlCost(true);
//		wTRGPDMAIC.setManualSchedule(true);
//		workTemplatesPage.createRootGatedProject(wTDMAIC, wTRGPDMAIC);
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickWorkTemplates();
//
//		WorkTemplateHolder wTCDDDRC = new WorkTemplateHolder("CDDDRC Work Template", "This is a CDDDRC Work Template created by an automated script.");
//		workTemplatesPage.createNewWorkTemplate(wTCDDDRC);
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickAdmin();
//		assert adminPopup.isAdminPopupVisible();
//		adminPopup.clickWorkTemplates();
//
//		WorkTemplateRootGatedProjectHolder wTRGPCDDDRC = new WorkTemplateRootGatedProjectHolder("CDDDRC Root Gated Project","CDDDRC objectives","CDDDRC");
//		wTRGPCDDDRC.setStatusReporting("No Frequency");
//		//FIGNJA S KONTROLSAMI!!!!!!
//		wTRGPCDDDRC.setInheritPermissions(true);
//		wTRGPCDDDRC.setInheritControls(true);
//		wTRGPCDDDRC.setWebFolder(false);
//		wTRGPCDDDRC.setControlCost(false);
//		wTRGPCDDDRC.setManualSchedule(false);
//		wTRGPCDDDRC.setDateConstraint(DATE_CONSTRAINTS.START_NO_EARLIER_THEN, "", "", ""); //is not implemented yet
//		workTemplatesPage.createRootGatedProject(wTCDDDRC, wTRGPCDDDRC);
//
//		//Since that last submit will redirect from /admin/ to /, killing the selenium javascript in process,
//		//we need to RESTART the selenium-driven browser
////		browser.restart();
//		//browser.getDriver().selectWindow("mainwindow");
//
//	}
//
////	@Test(dependsOnMethods = { "createWorkTemplates" })
////	public void loginAgain() throws Exception {
////		browser.restart();
////		LoginPage loginPage = new LoginPage(browser);
////		HomePage homePage = new HomePage(browser);
////
////		assert loginPage.isThisLoginPage();
////		assert loginPage.isLoginFormPresent();
////		loginPage.loginAs("admino", "qwer");
////		assert homePage.isThisHomePage();
////	}
////
////
//	@Test(dependsOnMethods = { "createWorkTemplates" })
//	public void createNewWorks() throws Exception{
//		NavigationBar navigationBar = new NavigationBar(browser);
//		BrowsePopup browsePopup = new BrowsePopup(browser);
//		CreateNewWorkPage createNewWorkPage = new CreateNewWorkPage(browser);
//
//		assert navigationBar.isNavigationBarVisible();
//		navigationBar.clickBrowse();
//		assert browsePopup.isBrowsePopupVisible();
//		browsePopup.clickCreateNewWork();
//
//		assert createNewWorkPage.isThisCreateNewWorkPage();
//
//		NewGatedProjectHolder nGPHDMAIC = new NewGatedProjectHolder("DMAIC Test Work 111", "DMAIC Work Template");
//		nGPHDMAIC.addProjectChampion("Admino");
//		nGPHDMAIC.addProjectDateField("Define", "10/10/2010");
//		nGPHDMAIC.addProjectDateField("Measure", "10/10/2010");
//		nGPHDMAIC.addProjectDateField("Analyze", "10/10/2010");
//		nGPHDMAIC.addProjectDateField("Improve", "10/10/2010");
//		nGPHDMAIC.addProjectDateField("Control", "10/10/2010");
//
//		createNewWorkPage.createNewWork(nGPHDMAIC);
//
//	}

	@AfterClass
	public void tearDown() throws Exception {
		Thread.sleep(10000);
		browser.stop();
		//MailReportManager.INSTANCE.sendReport();
	}

}
