using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;

using ArtOfTest.Common.UnitTesting;
using ArtOfTest.WebAii.Core;
using ArtOfTest.WebAii.Controls.HtmlControls;
using ArtOfTest.WebAii.Controls.HtmlControls.HtmlAsserts;
using ArtOfTest.WebAii.Design;
using ArtOfTest.WebAii.Design.Execution;
using ArtOfTest.WebAii.ObjectModel;
using ArtOfTest.WebAii.Silverlight;
using ArtOfTest.WebAii.Silverlight.UI;

namespace PSS_Automation
{

    public class TST_MAT_017_C992546 : BaseWebAiiTest
    {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

        /// <summary>
        /// Gets the Pages object that has references
        /// to all the elements, frames or regions
        /// in this project.
        /// </summary>
        public Pages Pages
        {
            get
            {
                if (_pages == null)
                {
                    _pages = new Pages(Manager.Current);
                }
                return _pages;
            }
        }

        #endregion
        
        // Add your test methods here...
        
        string nameOfUser;
    
        [CodedStep(@"Click on Add left navigation link")]
        public void TST_MAT_017_C992546_ClickAdminLink()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"[TST_MAT_016_C992596_VerifySummaryPage] : Wait for user to be navigated to work summary page")]
        public void TST_MAT_017_C992546_VerifySummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Summary"),"page tiltle should contain 'Summary' text");
                                                                                                               
        }
    
        [CodedStep(@"Click on Edit details on left naviagtion menu")]
        public void TST_MAT_017_C992546_ClickEDitDetails()
        {
            Pages.PS_ProjectSummaryPage.DetailsEditLink.Click();
            ActiveBrowser.WaitUntilReady();
                                 
        }
    
        [CodedStep(@"Assign Role as a owner")]
        public void TST_MAT_017_C992546_AssignRoleOwner()
        {
            nameOfUser = Data["FirstNameofUser"].ToString();
            Pages.PS_CreateNewProjectPage.GoButtonCreateWork.Wait.ForExists();
            Pages.PS_CreateNewProjectPage.GoButtonCreateWork.ScrollToVisible();
            Actions.SetText(Pages.PS_CreateNewProjectPage.FindUserCreateWorkInputText,nameOfUser);
            Pages.PS_CreateNewProjectPage.GoButtonCreateWork.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("add_project_searched_people_link"),nameOfUser));
            HtmlAnchor userLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("add_project_searched_people_link"),nameOfUser));
            userLink.Wait.ForExists();
            userLink.DragTo(Pages.PS_CreateNewProjectPage.DropProjectLeadH3Tag);
            Pages.PS_EditProjectDetailsPage.EditWorkSaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
                                    
        }
    
        [CodedStep(@"Verify Role is assigned")]
        public void TST_MAT_017_C992546_VerifyRole()
        {
            ActiveBrowser.RefreshDomTree();
            if(!Pages.PS_ProjectSummaryPage.ProjectSummaryDetailsDiv.IsVisible())
            {
                Pages.PS_ProjectSummaryPage.DetailsLink.Click();
            }
            HtmlDiv assignedUserDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("project_summary_team_members_div"),nameOfUser));
            assignedUserDiv.Wait.ForExists();
            Assert.IsTrue(assignedUserDiv.IsVisible());
        }
    
        [CodedStep(@"Set user for login")]
        public void TST_MAT_017_C992546_SetSuffix()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Click on Inbox icon in left navigation menu")]
        public void TST_MAT_017_C992546_ClickInboxIcon()
        {
            Pages.PS_HomePage.InboxLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Question/Notification tab")]
        public void TST_MAT_017_C992546_ClickQuestionNotification()
        {
            Pages.PS_MyInboxPage.QuestionsNotifTabLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify User gets a Question about owmership of the project")]
        public void TST_MAT_017_C992546_VerifyQuestion()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlDiv projectContainsDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("inbox_notification_project_contains_div"),GetExtractedValue("CreatedProjectName").ToString()));
            projectContainsDiv.Wait.ForExists();
            Assert.IsTrue(projectContainsDiv.IsVisible(),"Notification contains created project name link in div");
            Log.WriteLine(projectContainsDiv.InnerText);
            Assert.IsTrue(projectContainsDiv.InnerText.Contains("Accept Ownership?"),"Accept Ownwership? text should be displayed"); 
        }
    
    
        [CodedStep(@"Navigate to Genearted project summary page")]
        public void TST_MAT_017_C992546_CodedStep()
        {
            IList<HtmlAnchor> projectNameLinks = ActiveBrowser.Find.AllByXPath<HtmlAnchor>(string.Format("//a[contains(.,'{0}')]",GetExtractedValue("CreatedProjectName").ToString()));
            projectNameLinks[0].Wait.ForExists();
            projectNameLinks[0].Click();
        }
    }
}
