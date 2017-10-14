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

    public class TST_RW_014 : BaseWebAiiTest
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
        
        string reportName,designationCmt,approvalComment;
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_RW_014_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                                               
        }
    
        [CodedStep(@"Store Url of Project with best practice 'Yes'")]
        public void TST_RW_014_CS02()
        {
            SetExtractedValue("ProjectUrlYes",ActiveBrowser.Url);
        }
    
        [CodedStep(@"Click on 'Nominate as best practice' link")]
        public void TST_RW_014_CS03()
        {
            Pages.PS_HomePage.ProjectNominateAsBestPracTab.Wait.ForVisible();
            Pages.PS_HomePage.ProjectNominateAsBestPracTab.Click();
                                                                                        
        }
    
        [CodedStep(@"Verify 'Nominate as best practice' pop up is shown")]
        public void TST_RW_014_CS04()
        {
            Pages.PS_NominateAsPopUpWindow.NominateAsPopUpDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.NominateAsPopUpDiv.IsVisible(),"Pop up window should be open");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.NominateButton.IsVisible(),"In pop up window nomainate button should be present");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.CancelButton.IsVisible(),"In pop up window cancel button should be present");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.CommentsArea.IsVisible(),"In pop up window comment text area should be present");
                                                                            
        }
    
        [CodedStep(@"Set 'Test' comment in comment area for nomination of best practice")]
        public void TST_RW_014_CS05()
        {
            Actions.SetText(Pages.PS_NominateAsPopUpWindow.CommentsArea,"test");
            Pages.PS_NominateAsPopUpWindow.NominateButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Best Practice option is present")]
        public void TST_RW_014_CS06()
        {
            Pages.PS_NominateAsPopUpWindow.BestPracticeLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.BestPracticeLink.IsVisible(),"Best Practice link should be present");
        }
    
        [CodedStep(@"Click on 'Best Practice' link")]
        public void TST_RW_014_CS07()
        {
            Pages.PS_NominateAsPopUpWindow.BestPracticeLink.Click();
            System.Threading.Thread.Sleep(1000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Options link")]
        public void TST_RW_014_CS08()
        {
            Pages.PS_ProjectSummaryPage.BestPracticeOptionsLink.Click();
            System.Threading.Thread.Sleep(1000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Set text in comment area and Approve it")]
        public void TST_RW_014_CS09()
        {
            approvalComment = "Testing purpose comment";
            Actions.SetText(Pages.PS_ProjectSummaryPage.CommentTextArea,approvalComment);
            Pages.PS_ProjectSummaryPage.ApproveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
                                                                        
        }
    
        [CodedStep(@"Click on Review tab in left navigation bar")]
        public void TST_RW_014_CS10()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Click();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on AddNew button")]
        public void TST_RW_014_CS11()
        {
            Pages.PS_ReviewPublicReports.PublicReportAddNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Create Reports Page to be Loaded")]
        public void TST_RW_014_CS12()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add: Report: Create Reports");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);                       
        }
    
        [CodedStep(@"Click on Startnow link")]
        public void TST_RW_014_CS13()
        {
            Pages.PS_CreateReportsPage.CreateRepStartNowLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Report Wizard: Projects: New report Page to be Loaded")]
        public void TST_RW_014_CS14()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add: Report: Report Wizard: New");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Assert.IsTrue(Pages.PS_AddReportWizardPage.AddRepWizTypeLink.IsVisible(),"Type link should be present");
                                                                                                
        }
    
        [CodedStep(@"Select category of report")]
        public void TST_RW_014_CS15()
        {
            Pages.PS_AddReportWizardPage.CategorySelect.Wait.ForExists();
            Pages.PS_AddReportWizardPage.CategorySelect.SelectByText("Projects");
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select Type portion of report")]
        public void TST_RW_014_CS16()
        {
            Pages.PS_AddReportWizardPage.TypeSelect.Wait.ForExists();
            Pages.PS_AddReportWizardPage.TypeSelect.SelectByText("Projects");
            ActiveBrowser.WaitUntilReady();
                                                                                    
        }
    
        [CodedStep(@"Verify decription and example field are present")]
        public void TST_RW_014_CS17()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionH4Tag.IsVisible(),"Decription tag should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionTableCell.IsVisible(),"Description field should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.AddRepWizExampleH4Tag.IsVisible(),"Example tag should be present");
                                                                                    
        }
    
        [CodedStep(@"Click on continue button")]
        public void TST_RW_014_CS18()
        {
            Pages.PS_AddReportWizardPage.AddRepWizContinueBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is directed to 'Defination' part of report wizard")]
        public void TST_RW_014_CS19()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.DefinationPortfolioSelect.IsVisible(),"Portfolio dropdown list should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.WorkCheckbox.IsVisible(),"Work chechbox should be present");
        }
    
        [CodedStep(@"Select Work type in 'Defination' part of report")]
        public void TST_RW_014_CS20()
        {
            HtmlInputCheckBox customProjectChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("report_wizard_custom_project_checkbox"));
            customProjectChkbox.Wait.ForExists();
            customProjectChkbox.Check(true);
        }
    
        [CodedStep(@"Verify user is directed to 'Columns' part of report")]
        public void TST_RW_014_CS21()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ProjectLink.IsVisible(),"Project link should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ProjectBestPracticeLink.IsVisible(),"Project/Best Practice link should be present");
        }
    
        [CodedStep(@"Click on 'Project/Best Practice' link and select 'All' named checkbox")]
        public void TST_RW_014_CS22()
        {
            Pages.PS_AddReportWizardPage.ProjectBestPracticeLink.Click();
            System.Threading.Thread.Sleep(1000);
            ActiveBrowser.RefreshDomTree();
            HtmlInputCheckBox allBestPractice = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("column_best_practice_all_checkbox"));
            allBestPractice.Wait.ForExists();
            allBestPractice.Click();
        }
    
        [CodedStep(@"Verify user is directed to 'Filter' part of report")]
        public void TST_RW_014_CS23()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.FilterSelectSpan.IsVisible(),"Select link should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.FilterAdditionalFiltersLink.IsVisible(),"Additional Filters link should be present");
                        
        }
    
        [CodedStep(@"Select 'Designation removed comment' checkbox")]
        public void TST_RW_014_CS24()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlInputCheckBox desigtnRemCmtChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("html_report_designation_rem_cmt_chkbox"));
            desigtnRemCmtChkbox.Wait.ForExists();
            desigtnRemCmtChkbox.Click();
        }
    
        [CodedStep(@"Select 'Any of' option")]
        public void TST_RW_014_CS25()
        {
            ActiveBrowser.RefreshDomTree();
            //HtmlSelect setAnyOfSelect = ActiveBrowser.Find.ByXPath<HtmlSelect>("//select[@id='criteria']");
            //setAnyOfSelect.Wait.ForExists();
            //setAnyOfSelect.SelectByText("Any of"); 
            HtmlSelect setAnyOff = ActiveBrowser.Find.ByXPath<HtmlSelect>(AppLocators.get("report_wizard_set_option"));
            setAnyOff.Wait.ForExists();
            setAnyOff.SelectByText("Any of");
        }
    
        [CodedStep(@"Set text in Designation Removed Comment field in 'Filter' part of report")]
        public void TST_RW_014_CS26()
        {
            designationCmt = Data["DesignationRemovedComment"].ToString();
            Actions.SetText(Pages.PS_AddReportWizardPage.FilterStartText,designationCmt);
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_RW_014_CS27()
        {
            Pages.PS_AddReportWizardPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is directed to 'Details & Schedule' part of report")]
        public void TST_RW_014_CS28()
        {
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ReportNameText.IsVisible(),"Report name text area should be present");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.SaveAndH3Tag.IsVisible(),"Header name should be correct");
            Assert.IsTrue(Pages.PS_AddReportWizardPage.ScheduleLocationSelect.IsVisible(),"Location folder select should be present");            
        }
    
        [CodedStep(@"Set Report name and Loction in 'Details & Schedule' part of report")]
        public void TST_RW_014_CS29()
        {
            reportName = Data["ReportName"].ToString()+Randomizers.generateRandomInt(100,999);
            string locSelcet = Data["LocationSelect"].ToString();
            
            Actions.SetText(Pages.PS_AddReportWizardPage.ReportNameText,reportName);
            Pages.PS_AddReportWizardPage.ScheduleLocationSelect.SelectByText(locSelcet);
                                    
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_RW_014_CS30()
        {
            Pages.PS_AddReportWizardPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Public Report Page to be Loaded")]
        public void TST_RW_014_CS31()
        {
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicRepOptionsLink.IsVisible(),"Option link should be present");
            Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportAddNewButton.IsVisible(),"Add new button should be present");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                                
        }
    
        [CodedStep(@"Verify Report is saved and Click on generated report link")]
        public void TST_RW_014_CS32()
        {
            HtmlDiv reportLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("generated_report_div"),reportName));
            reportLoc.Wait.ForExists();
            Assert.IsTrue(reportLoc.IsVisible(),"new report should be present"); 
            reportLoc.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();
                                    
        }
    
        [CodedStep(@"Run the report and view in 'HTML")]
        public void TST_RW_014_CS33()
        {
            Pages.PS_PublicReportsPopUp.RunMenuDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_PublicReportsPopUp.RunMenuDiv.IsVisible(),"Run Menu div should be present");
            Pages.PS_PublicReportsPopUp.HTMLLink.Click();
        }
    
        [CodedStep(@"Connect to Report pop-up window")]
        public void TST_RW_014_CS34()
        {
            Manager.WaitForNewBrowserConnect("/reports/ProgressScreen.epage", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
                                    
        }
    
        [CodedStep(@"Close the report html page")]
        public void TST_RW_014_CS35()
        {
                                               
            Manager.ActiveBrowser.Close();
                                                
        }
    
        [CodedStep(@"Delete created report")]
        public void TST_RW_014_CS36()
        {
            try
            {
                ActiveBrowser.Refresh();
                ActiveBrowser.RefreshDomTree();
                HtmlInputCheckBox repChxbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("report_chekbox"),reportName));
                repChxbox.Wait.ForExists();
                repChxbox.ScrollToVisible();
                repChxbox.Check(true);
                Pages.PS_ReviewPublicReports.PublicReportDeleteButton.Click();
                ActiveBrowser.WaitUntilReady();
            }
            
            catch(Exception ex)
            {
                Log.WriteLine("Inside catch : Delete" + reportName + " manaualy");
                Console.WriteLine(ex.Message);
            }
                                                
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_RW_014_CS37()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectUrlYes").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    }
}
