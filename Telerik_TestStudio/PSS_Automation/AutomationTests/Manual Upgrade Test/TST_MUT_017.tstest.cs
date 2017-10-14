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

    public class TST_MUT_017 : BaseWebAiiTest
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
        
        string reportName;
    
        [CodedStep(@"Click on Add icon on Left Navigation menu")]
        public void TST_MUT_017_ClickAddIcon()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
                        
        }
    
        [CodedStep(@"Click on See List link on Advanced section")]
        public void TST_MUT_017_ClickSeeList()
        {
            Pages.PS_CreateReportsPage.CreateRepSeeListLink.Click();
        }
    
        [CodedStep(@"Select required template")]
        public void TST_MUT_017_SelectTemplate()
        {
            ActiveBrowser.RefreshDomTree();
            
            string templateName = Data["AdvancedTemplate"].ToString();
            Log.WriteLine(string.Format(AppLocators.get("advanced_template_link"),templateName).ToString());
            HtmlAnchor templateLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("advanced_template_link"),templateName));
            templateLink.Wait.ForExists();
            templateLink.Click();
        }
    
        [CodedStep(@"Verify user is directed to Edit Report details page ")]
        public void TST_MUT_017_VerifyDetailsPage()
        {
            Pages.PS_AddReportWizardPage.ReportNameText.Wait.ForExists();
            Pages.PS_EditReportDetailsPage.SaveButton.Wait.ForExists();
            Pages.PS_EditReportDetailsPage.CancelButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ReportNameText.IsVisible(),"Report name text area should be present");
            Assert.IsTrue(Pages.PS_EditReportDetailsPage.SaveButton.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_EditReportDetailsPage.CancelButton.IsVisible(),"Cancel Button should be visible");
        }
    
        [CodedStep(@"Set Report name and Loction in 'Details & Schedule' part of report")]
        public void TST_MUT_017_SetReportNameAndLocation()
        {
            reportName = Data["ReportName"].ToString()+Randomizers.generateRandomInt(100,999);
            string locSelcet = Data["LocationSelect"].ToString();
            
            Actions.SetText(Pages.PS_AddReportWizardPage.ReportNameText,reportName);
            Pages.PS_AddReportWizardPage.ScheduleLocationSelect.SelectByText(locSelcet);
                                                                        
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_MUT_017_ClickSaveButton()
        {
            Pages.PS_EditReportDetailsPage.SaveButton.Wait.ForExists();
            Pages.PS_EditReportDetailsPage.SaveButton.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Public Report Page to be Loaded")]
        public void TST_MUT_017_WaitForPublicReportPage()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicRepOptionsLink.IsVisible(),"Option link should be present");
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportAddNewButton.IsVisible(),"Add new button should be present");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                                                    
        }
    
        [CodedStep(@"Verify Report is saved and Click on generated report link")]
        public void TST_MUT_017_VerifyReportSavedAndClick()
        {
            HtmlDiv reportLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("generated_report_div"),reportName));
            reportLoc.Wait.ForExists();
            Assert.IsTrue(reportLoc.IsVisible(),"new report should be present"); 
            reportLoc.Click();
            reportLoc.InvokeEvent(ScriptEventType.OnMouseUp);
            ActiveBrowser.RefreshDomTree();
                                                                        
        }
    
        [CodedStep(@"Run the report and view in 'HTML")]
        public void TST_MUT_017_RunReport()
        {
            Pages.PS_PublicReportsPopUp.RunMenuDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_PublicReportsPopUp.RunMenuDiv.IsVisible(),"Run Menu div should be present");
            Pages.PS_PublicReportsPopUp.HTMLLink.Click();
        }
    
        [CodedStep(@"Connect to Report pop-up window")]
        public void TST_MUT_017_ConnectReportPopup()
        {
            /// \bug Move to central place
            Manager.WaitForNewBrowserConnect("/reports/ProgressScreen.epage", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
                                                                        
        }
    
        [CodedStep(@"Close the report html page")]
        public void TST_MUT_017_CloseReport()
        {
                                                           
            Manager.ActiveBrowser.Close();
                                                            
        }
    
        [CodedStep(@"Delete created report")]
        public void TST_MUT_017_DeleteCreatedReport()
        {
            try
            {
                ActiveBrowser.Refresh();
                HtmlInputCheckBox repChxbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("report_chekbox"),reportName));
                repChxbox.Click();
                Pages.PS_ReviewPublicReports.PublicReportDeleteButton.Click();
                ActiveBrowser.WaitUntilReady();
            }
            
            catch(Exception ex)
            {
                Log.WriteLine("Delete" + reportName + " manaualy " + ex.Message);
                
            }
            System.Threading.Thread.Sleep(3000);
        }
    }
}
