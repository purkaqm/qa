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

    public class TST_MAT_019_C992547 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Add left navigation link")]
        public void TST_MAT_019_C992547_ClickAdminLink()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Wait for user to be navigated to work summary page")]
        public void TST_MAT_019_C992547_VerifySummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Summary"),"page tiltle should contain 'Summary' text");                                                                                                                              
        }
    
        [CodedStep(@"Click to 'Alert Subscriptions' in the left navigation menu")]
        public void TST_MAT_019_C992547_ClickAlertSubs()
        {
            Pages.PS_ProjectSummaryPage.ProjectAlertSubscriptionsTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"'Notify by email' set to No")]
        public void TST_MAT_019_C992547_NotifyByEmail()
        {
            Pages.PS_AlertSubscriptionsPage.NotifyByEmailNoRadio.Wait.ForExists();
            Pages.PS_AlertSubscriptionsPage.NotifyByEmailNoRadio.Check(true);
        }
        
        [CodedStep(@"Mark all events")]
        public void TST_MAT_019_C992547_MarkAllEvents()
        {
            Pages.PS_AlertSubscriptionsPage.AllCheckCheckBox.Wait.ForExists();
            Pages.PS_AlertSubscriptionsPage.AllCheckCheckBox.Click();
        }
        
        [CodedStep(@"Save the changes")]
        public void TST_MAT_019_C992547_ClickSaveChanges()
        {
            Pages.PS_AlertSubscriptionsPage.SaveChangesButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Click to status of project")]
        public void TST_MAT_019_C992547_ClickStatus()
        {
            Pages.PS_IdeaSubmitSummaryPage.StatusLink.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Change status of project")]
        public void TST_MAT_019_C992547_ChangeStatus()
        {
            Pages.PS_ProjectSummaryPage.NotStartedDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.NotStartedDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Inbox icon in left navigation menu")]
        public void TST_MAT_019_C992547_ClickInboxIcon()
        {
            Pages.PS_HomePage.InboxLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Alerts tab")]
        public void TST_MAT_019_C992547_ClickAlertTab()
        {
            Pages.PS_MyInboxPage.AlertsTabLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify User gets a alert about changing the status of project")]
        public void TST_MAT_019_C992547_VerifyAlert()
        {
            ActiveBrowser.RefreshDomTree();
            string userName = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value;
            string otherData1 = "changed the status of Custom Project";
            string projectName = GetExtractedValue("CreatedProjectName").ToString(); 
            Log.WriteLine(string.Format("//td[contains(.,'{0}')][contains(.,'{1}')][contains(.,'{2}')]",userName,otherData1,projectName));
            HtmlTableCell statusChange = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format("//td[contains(.,'{0}')][contains(.,'{1}')][contains(.,'{2}')]",userName,otherData1,projectName));
            statusChange.Wait.ForExists();
            Assert.IsTrue(statusChange.IsVisible(),"Alert for changing status is displayed");
        }
    
    
        [CodedStep(@"Delete the alert")]
        public void TST_MAT_019_C992547_DeleteAlert()
        {
            HtmlInputCheckBox delAlert = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(.,'{0}')]//input",GetExtractedValue("CreatedProjectName").ToString()));
            delAlert.Check(true);
            HtmlInputSubmit delButton = ActiveBrowser.Find.ByXPath<HtmlInputSubmit>("//input[@id='Clear']");
            delButton.Click(true);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();  
        }
    
        [CodedStep("Verify alert is deleted")]
        public void TST_MAT_019_C992547_VerifyAlertIsDeleted()
        {
            string userName = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value;
            string otherData1 = "changed the status of Custom Project";
            string projectName = GetExtractedValue("CreatedProjectName").ToString(); 
            Assert.IsFalse(ActiveBrowser.Find.AllByXPath<HtmlTableCell>(string.Format("//td[contains(.,'{0}')][contains(.,'{1}')][contains(.,'{2}')]",userName,otherData1,projectName)).Count > 0);
        }
    }
}
