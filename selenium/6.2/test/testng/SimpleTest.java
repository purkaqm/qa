package test.testng;

import org.testng.annotations.*;

import selenium.models.*;

public class SimpleTest {
	String testWorkName = "work_" + String.valueOf(System.currentTimeMillis());
	BrowserModel browser;

	@BeforeClass
	public void setUp() throws Exception {
		browser = new BrowserModel("*iexplore");
		//BrowserModel.INSTANCE.start("localhost", 4545, "*iexplore", "http://dev-load.cinteractive.com/","", "test");
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
	public void createANewWork() throws InterruptedException{
//		NavigationBar navigationBar = new NavigationBar(browser);
//		BrowsePopup browsePopup = new BrowsePopup(browser);
//		CreateNewWorkPage createNewWorkPage = new CreateNewWorkPage(browser);
//		SummaryPage summaryPage = new SummaryPage(browser);
//
//		navigationBar.clickBrowse();
//		assert browsePopup.isBrowsePopupVisible();
//		browsePopup.clickCreateNewWork();
//		assert createNewWorkPage.isThisCreateNewWorkPage();
//		assert createNewWorkPage.isThisStepOnePage();
//		createNewWorkPage.doStepOneWorkTypeSubmit("Work");
//		assert createNewWorkPage.isThisCreateNewWorkPage();
//		assert createNewWorkPage.isThisStepTwoPage();
//		createNewWorkPage.doStepTwoWorkSubmitForFinishAndCreate(testWorkName);
//		assert summaryPage.isThisSummaryPage();
//		assert summaryPage.isThisSummaryPageWorkName(testWorkName);
	}

	@Test(dependsOnMethods = { "createANewWork" })
	public void checkThatCreatedWorkIsInWorkTree() throws InterruptedException{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		WorkTreePage workTreePage = new WorkTreePage(browser);

		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickWorkTree();
		assert workTreePage.isThisWorkTreePage();
		workTreePage.waitForWorkTreeLoad();
		assert workTreePage.isWorkLinkPresent(testWorkName);
	}

	@AfterClass
	public void tearDown() throws Exception {
		Thread.sleep(5000);
		browser.stop();
		//MailReportManager.INSTANCE.sendReport();
	}

}
