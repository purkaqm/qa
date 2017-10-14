package test.testng;

import java.util.LinkedList;

import org.testng.annotations.*;

import selenium.formholders.*;
import selenium.models.*;
import test.service.*;

public class InternalAcceptanceTest {
	BrowserModel browser;
	String workName;

	@BeforeClass
	public void setUp() throws Exception {
		browser = new BrowserModel("*chrome");
		workName = "IAC_Work_" + System.currentTimeMillis();
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
	public void createNewWork() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		CreateNewWorkPage createNewWorkPage = new CreateNewWorkPage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickCreateNewWork();

		assert createNewWorkPage.isThisCreateNewWorkPage();

 		NewWorkHolder nWH = new NewWorkHolder(workName, "Work");
		createNewWorkPage.createNewWork(nWH);

	}

	@Test(dependsOnMethods = { "createNewWork" })
	public void testDiscussion() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		WorkTreePage workTreePage = new WorkTreePage(browser);
		SummaryPage summaryPage = new SummaryPage(browser);
		Toolbar toolbar = new Toolbar(browser);
		DiscussionsPage discussionsPage = new DiscussionsPage(browser);
		AddEditDiscussionPage addEditDiscussionPage = new AddEditDiscussionPage(browser);
		ViewThreadPage viewThreadPage = new ViewThreadPage(browser);
		AddEditIssuePage addEditIssuePage = new AddEditIssuePage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickWorkTree();
		assert workTreePage.isThisWorkTreePage();
		workTreePage.waitForWorkTreeLoad();
		workTreePage.clickWorkLink(workName);
		assert summaryPage.isThisSummaryPage();
		assert summaryPage.isThisSummaryPageWorkName(workName);
		assert toolbar.isToolbarPresent();
		toolbar.clickDiscussionsLink();
		assert discussionsPage.isThisDiscussionsPage();
		discussionsPage.doClickNewDiscussion();
		assert addEditDiscussionPage.isThisAddEditDiscussionPage();

		AttachmentLocalHolder aLH1 = new AttachmentLocalHolder("dtest001.txt","DiscussionTextFile001");
		AttachmentLocalHolder aLH2 = new AttachmentLocalHolder("dtest002.txt","DiscussionTextFile002");
		DiscussionBlockHolder dBH = new DiscussionBlockHolder("TestDiscussion","This is just a test discussion created by an automated script.");
		dBH.addAttachment(aLH1);
		dBH.addAttachment(aLH2);
		IssueBlockHolder iBH = null;

		addEditDiscussionPage.addDiscussion(dBH);
		viewThreadPage.testAttachment(aLH1);
		viewThreadPage.testAttachment(aLH2);
		viewThreadPage.escalateDiscussion(dBH);
		assert addEditIssuePage.isThisAddEditIssuePage();
		iBH = dBH.getEscalated("3");
		addEditIssuePage.setPriorityAndSubmit(iBH);
		viewThreadPage.testAttachment(aLH1);
		viewThreadPage.testAttachment(aLH2);
		viewThreadPage.deEscalateIssue(iBH);
		viewThreadPage.testAttachment(aLH1);
		viewThreadPage.testAttachment(aLH2);
	}

	@Test(dependsOnMethods = { "testDiscussion" })
	public void testIssue() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		WorkTreePage workTreePage = new WorkTreePage(browser);
		SummaryPage summaryPage = new SummaryPage(browser);
		Toolbar toolbar = new Toolbar(browser);
		IssuesPage issuesPage = new IssuesPage(browser);
		AddEditDiscussionPage addEditDiscussionPage = new AddEditDiscussionPage(browser);
		ViewThreadPage viewThreadPage = new ViewThreadPage(browser);
		AddEditIssuePage addEditIssuePage = new AddEditIssuePage(browser);
		AddEditActionItemPage addEditActionItemPage = new AddEditActionItemPage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickWorkTree();
		assert workTreePage.isThisWorkTreePage();
		workTreePage.waitForWorkTreeLoad();
		workTreePage.clickWorkLink(workName);
		assert summaryPage.isThisSummaryPage();
		assert summaryPage.isThisSummaryPageWorkName(workName);
		assert toolbar.isToolbarPresent();
		toolbar.clickIssuesLink();
		assert issuesPage.isThisIssuesPage();
		issuesPage.clickNewIssue();
		assert addEditIssuePage.isThisAddEditIssuePage();

		AttachmentLocalHolder aLH1 = new AttachmentLocalHolder("itest001.txt","IssueTextFile001");
		AttachmentLocalHolder aLH2 = new AttachmentLocalHolder("itest002.txt","IssueTextFile002");
		IssueBlockHolder iBH = new IssueBlockHolder("TestIssue","This is just a test discussion created by an automated script.", "3");
		iBH.addAttachment(aLH1);
		iBH.addAttachment(aLH2);

		AttachmentLocalHolder aLH3 = new AttachmentLocalHolder("itest003.txt","IssueTextFile003");
		AttachmentLocalHolder aLH4 = new AttachmentLocalHolder("itest004.txt","IssueTextFile004");
		DiscussionBlockHolder dBH = new DiscussionBlockHolder("TestReply","This is just a test reply created by an automated script.");
		dBH.addAttachment(aLH3);
		dBH.addAttachment(aLH4);

		ActionItemHolder aIH = new ActionItemHolder("TestTask");

		addEditIssuePage.addIssue(iBH);
		assert viewThreadPage.isThisViewThreadPage();
		viewThreadPage.testAttachment(aLH1);
		viewThreadPage.testAttachment(aLH2);
		viewThreadPage.clickReply(iBH);
		assert addEditDiscussionPage.isThisAddEditDiscussionPage();
		addEditDiscussionPage.addDiscussion(dBH);
		assert viewThreadPage.isThisViewThreadPage();
		viewThreadPage.testAttachment(aLH3);
		viewThreadPage.testAttachment(aLH4);
		viewThreadPage.escalateDiscussion(dBH);
		assert addEditIssuePage.isThisAddEditIssuePage();
		IssueBlockHolder iBH2 = dBH.getEscalated("3");
		addEditIssuePage.setPriorityAndSubmitForTask(iBH2);
		addEditActionItemPage.addTask(aIH);
		assert viewThreadPage.isThisViewThreadPage();
		viewThreadPage.clickCloseIssue(iBH2);
	}

	@Test(dependsOnMethods = { "testIssue" })
	public void testDocument() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		WorkTreePage workTreePage = new WorkTreePage(browser);
		SummaryPage summaryPage = new SummaryPage(browser);
		Toolbar toolbar = new Toolbar(browser);
		DocumentsPage documentsPage = new DocumentsPage(browser);
		DocumentDetailsPage documentDetailsPage = new DocumentDetailsPage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickWorkTree();
		assert workTreePage.isThisWorkTreePage();
		workTreePage.waitForWorkTreeLoad();
		workTreePage.clickWorkLink(workName);
		assert summaryPage.isThisSummaryPage();
		assert summaryPage.isThisSummaryPageWorkName(workName);
		assert toolbar.isToolbarPresent();
		toolbar.clickDocumentsLink();
		assert documentsPage.isThisDocumentsPage();

		LocalFileHolder lFH1 = new LocalFileHolder("dtest001.txt","Document001");
		LocalFileHolder lFH2 = new LocalFileHolder("dtest002.txt","Document002");

		documentsPage.addDocument(lFH1);
		documentsPage.clickUpdate(lFH1);
		assert documentDetailsPage.isThisDocumentDetailsPage();
		documentDetailsPage.updateWith(lFH2, "aaa");
		documentDetailsPage.testVersion(1, lFH1);
		documentDetailsPage.testVersion(2, lFH2);
	}

	@Test(dependsOnMethods = { "testDocument" })
	public void testWBS() throws Exception{
		NavigationBar navigationBar = new NavigationBar(browser);
		BrowsePopup browsePopup = new BrowsePopup(browser);
		WorkTreePage workTreePage = new WorkTreePage(browser);
		SummaryPage summaryPage = new SummaryPage(browser);
		Toolbar toolbar = new Toolbar(browser);
		WorkBreakdownPage workBreakdownPage = new WorkBreakdownPage(browser);

		assert navigationBar.isNavigationBarVisible();
		navigationBar.clickBrowse();
		assert browsePopup.isBrowsePopupVisible();
		browsePopup.clickWorkTree();
		assert workTreePage.isThisWorkTreePage();
		workTreePage.waitForWorkTreeLoad();
		workTreePage.clickWorkLink(workName);
		assert summaryPage.isThisSummaryPage();
		assert summaryPage.isThisSummaryPageWorkName(workName);
		assert toolbar.isToolbarPresent();
		toolbar.clickWorkBreakdownLink();
		workBreakdownPage.isThisWorkTreePage();
		LinkedList<NewWorkHolder> children = new LinkedList<NewWorkHolder>();
		children.add(new NewWorkHolder("Child001" ,"Work"));
		children.add(new NewWorkHolder("Child002" ,"Work"));
		workBreakdownPage.add(children);
		workBreakdownPage.indent(children.get(1));
	}

	@AfterClass
	public void tearDown() throws Exception {
		Thread.sleep(10000);
		browser.stop();
		//MailReportManager.INSTANCE.sendReport();
	}

}
