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

    public class TST_MUT_016 : BaseWebAiiTest
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
        public void TST_MUT_016_ClickAddIcon()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
            
        }
    
        [CodedStep(@"Select category of report")]
        public void TST_MUT_016_SelectCategory()
        {
            Pages.PS_AddReportWizardPage.CategorySelect.Wait.ForExists();
            Pages.PS_AddReportWizardPage.CategorySelect.SelectByText("Projects");
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select Type portion of report")]
        public void TST_MUT_016_SelectType()
        {
            Pages.PS_AddReportWizardPage.TypeSelect.Wait.ForExists();
            Pages.PS_AddReportWizardPage.TypeSelect.SelectByText("Projects");
            ActiveBrowser.WaitUntilReady();
                                                                        
        }
    
        [CodedStep(@"Verify decription and example field are present")]
        public void TST_MUT_016_VerifyDescriptionField()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionH4Tag.IsVisible(),"Decription tag should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionTableCell.IsVisible(),"Description field should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.AddRepWizExampleH4Tag.IsVisible(),"Example tag should be present");
                                                                        
        }
    
        [CodedStep(@"Click on continue button")]
        public void TST_MUT_016_ClickContineu()
        {
            Pages.PS_AddReportWizardPage.AddRepWizContinueBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is directed to 'Defination' part of report wizard")]
        public void TST_MUT_016_VerifyDefinationPart()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DefinationPortfolioSelect.IsVisible(),"Portfolio dropdown list should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.WorkCheckbox.IsVisible(),"Work chechbox should be present");
        }
    
        [CodedStep(@"Select Work type in 'Defination' part of report")]
        public void TST_MUT_016_SelectEorkType()
        {
            HtmlInputCheckBox customProjectChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("report_wizard_custom_project_checkbox"));
            customProjectChkbox.Wait.ForExists();
            customProjectChkbox.Check(true);
        }
    
        [CodedStep(@"Verify user is directed to 'Columns' part of report")]
        public void TST_MUT_016_VerifyColumn()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ProjectLink.IsVisible(),"Project link should be present");
        }
        
        [CodedStep(@"Select column options to display in report")]
        public void TST_MUT_016_SelectColumn()
        {
            HtmlInputCheckBox workTemplateChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("report_wizard_work_template_checkbox"));
            workTemplateChkbox.Wait.ForExists();
            workTemplateChkbox.Check(true);
            
            HtmlInputCheckBox createdByChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("report_wizard_created_by_checkbox"));
            createdByChkbox.Wait.ForExists();
            createdByChkbox.Check(true);
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_MUT_016_ClickSaveButton()
        {
            Pages.PS_AddReportWizardPage.SaveBtn.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify user is directed to 'Details & Schedule' part of report")]
        public void TST_MUT_016_VerifyDetailsPage()
        {
            Pages.PS_AddReportWizardPage.ReportNameText.Wait.ForExists();
            Pages.PS_AddReportWizardPage.SaveAndH3Tag.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ReportNameText.IsVisible(),"Report name text area should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.SaveAndH3Tag.IsVisible(),"Header name should be correct");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ScheduleLocationSelect.IsVisible(),"Location folder select should be present");            
        }
    
        [CodedStep(@"Set Report name and Loction in 'Details & Schedule' part of report")]
        public void TST_MUT_016_SetReportNameAndLocation()
        {
            reportName = Data["ReportName"].ToString()+Randomizers.generateRandomInt(100,999);
            string locSelcet = Data["LocationSelect"].ToString();
            
            Actions.SetText(Pages.PS_AddReportWizardPage.ReportNameText,reportName);
            Pages.PS_AddReportWizardPage.ScheduleLocationSelect.SelectByText(locSelcet);
                                                            
        }
    
    
        [CodedStep(@"Wait for Public Report Page to be Loaded")]
        public void TST_MUT_016_WaitForPublicReportPage()
        {
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicRepOptionsLink.IsVisible(),"Option link should be present");
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportAddNewButton.IsVisible(),"Add new button should be present");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                                        
        }
    
        [CodedStep(@"Verify Report is saved and Click on generated report link")]
        public void TST_MUT_016_VerifyReportSavedAndClick()
        {
            HtmlDiv reportLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("generated_report_div"),reportName));
            reportLoc.Wait.ForExists();
            Assert.IsTrue(reportLoc.IsVisible(),"new report should be present"); 
            reportLoc.Click();
            reportLoc.InvokeEvent(ScriptEventType.OnMouseUp);
            ActiveBrowser.RefreshDomTree();
                                                            
        }
    
        [CodedStep(@"Run the report and view in 'HTML")]
        public void TST_MUT_016_RunReport()
        {
            Pages.PS_PublicReportsPopUp.RunMenuDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_PublicReportsPopUp.RunMenuDiv.IsVisible(),"Run Menu div should be present");
            Pages.PS_PublicReportsPopUp.HTMLLink.Click();
        }
    
        [CodedStep(@"Connect to Report pop-up window")]
        public void TST_MUT_016_ConnectReportPopup()
        {
            /// \bug The relative path should probably be moved to a central place
            Manager.WaitForNewBrowserConnect("/reports/ProgressScreen.epage", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
                                                            
        }
    
        [CodedStep(@"Close the report html page")]
        public void TST_MUT_016_CloseReport()
        {
                                               
            Manager.ActiveBrowser.Close();
                                                
        }
    
        [CodedStep(@"Delete created report")]
        public void TST_MUT_016_DeleteCreatedReport()
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
                                                
        }
    }
}
