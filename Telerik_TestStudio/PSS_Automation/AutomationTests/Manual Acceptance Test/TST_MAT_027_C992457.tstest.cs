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

    public class TST_MAT_027_C992457 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to work summary page")]
        public void TST_MAT_027_C992457_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Summary"),"page tiltle should contain 'Summary' text");
                                                                                                                           
        }
    
        [CodedStep(@"Click on Edit Details tab")]
        public void TST_MAT_027_C992457_ClickEditDetailsTab()
        {
            Pages.PS_ProjectSummaryPage.ProjectEditDetailsTab.Click(true);
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Assign Role")]
        public void TST_MAT_027_C992457_AssignRole()
        {
            nameOfUser = Data["FirstNamefUser"].ToString();
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
            userLink.DragTo(Pages.PS_CreateNewProjectPage.DropTeamMemberH3Tag);
            Pages.PS_EditProjectDetailsPage.EditWorkSaveChangesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify Role is assigned")]
        public void TST_MAT_027_C992457_CodedStep()
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
    }
}
