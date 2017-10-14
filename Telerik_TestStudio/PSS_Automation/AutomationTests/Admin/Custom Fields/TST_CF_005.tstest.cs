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

    public class TST_CF_005 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_CF_005_CS01()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_CF_005_CS02()
        {
                        Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Wait till Add/Edit Custom Field pop up is displayed")]
        public void TST_CF_005_CS03()
        {
                                    Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();                       
                                    Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
                                    Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();                                    
                                                                                                                      
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_CF_005_CS04()
        {
                                    string custFieldName = Data["CustomFieldName"].ToString() + Randomizers.generateRandomInt(1000,9999);
                                    ActiveBrowser.Window.SetFocus();
                                    Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
                                    Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
                                    Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
                                    SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TST_CF_005_CS05()
        {
                                    if(Data["CustomFieldDesc"].ToString().Length > 0){
                                    ActiveBrowser.Window.SetFocus();
                                    Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Click();
                                    Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Focus();
                                    Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea, Data["CustomFieldDesc"].ToString());
                                    }
        }
    
        [CodedStep(@"Choose Field Type")]
        public void TST_CF_005_CS06()
        {
                                   if(Data["CustomFieldType"].ToString().Length > 0){
                                       Pages.PS_CustomFieldsPage.AddCustFieldTypeSelector.SelectByText(Data["CustomFieldType"].ToString(),true);
                                                
                                    }
                                                            
        }
    
        [CodedStep(@"Verify tooltip is displayed - Maximum Text Length")]
        public void TST_CF_005_CS07()
        {
                                      ActiveBrowser.RefreshDomTree();
                                      Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthColumn.IsVisible(),"Maximum test length tooltip should be displayed");
                                      Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthInput.IsVisible());
        }
    
        [CodedStep(@"Set 'Maximum Text Length' to 10000")]
        public void TST_CF_005_CS08()
        {
                                    ActiveBrowser.Window.SetFocus();
                                    //Pages.PS_CustomFieldsPage.MaximumTextLengthInput.Click();
                                    Pages.PS_CustomFieldsPage.MaximumTextLengthInput.MouseClick(MouseClickType.LeftClick);
                                    Manager.Desktop.KeyBoard.TypeText("10000",2);
                                                
        }
    
        [CodedStep(@"Choose Work Item")]
        public void TST_CF_005_CS09()
        {
                                   if(!Data["FieldWorkItem"].ToString().ToLower().Contains("no")){
                                       //Pages.PS_CustomFieldsPage.AddCustFieldWorkTypesDiv.Click();
                                       HtmlDiv worktypeChoose = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("select_work_item_for_custom_fields"));
                                       worktypeChoose.Wait.ForExists();
                                       worktypeChoose.Click();
                                       ActiveBrowser.RefreshDomTree();
                                       Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForExists();
                                       Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForVisible();
                                       
                                       
                                       
                                       string objectTypeStr = Data["FieldWorkItem"].ToString();
                                       Log.WriteLine(objectTypeStr);
                                       string[] objectTypes = System.Text.RegularExpressions.Regex.Split(objectTypeStr, "---");
                                       
                                       for(int i=0; i < objectTypes.Length ; i++){
                                           System.Threading.Thread.Sleep(3000);
                                           string workItemCheckboxLocator = string.Format(AppLocators.get("add_status_report_tmplt_work_item"),objectTypes[i]);
                                           if(ActiveBrowser.Find.AllByXPath(workItemCheckboxLocator).Count > 0){
                                               HtmlInputCheckBox workItemChkbx = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(workItemCheckboxLocator);
                                               workItemChkbx.Click();
                                           }
                                           else{
                                               Assert.IsTrue(false, objectTypes[i] + " Not present in work items list");
                                           }
                                       }
                                       
                                       Pages.PS_StatusReportTemplatesPage.DoneButton.Click();
                                       ActiveBrowser.RefreshDomTree();
                                   }
        }
    
        [CodedStep(@"Click Save button")]
        public void TST_CF_005_CS10()
        {
                                    Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Focus();
                                    Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.MouseHover();
                                    Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Click(true);
                                    ActiveBrowser.WaitUntilReady();
                                    ActiveBrowser.RefreshDomTree();
                                                              
        }
    
        [CodedStep(@"Verify new 'Custom field' is created")]
        public void TST_CF_005_CS11()
        {
                                    HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
                                    customFieldRow.Wait.ForExists();
                                    Assert.IsTrue(customFieldRow.IsVisible());
        }
        
        
        [CodedStep(@"Click on Review tab in left navigation bar")]
        public void TST_CF_005_CS12()
        {
                        Pages.PS_HomePage.ReviewLeftNavLink.Click();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on AddNew button")]
        public void TST_CF_005_CS13()
        {
                        Pages.PS_ReviewPublicReports.PublicReportAddNewButton.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Create Reports Page to be Loaded")]
        public void TST_CF_005_CS14()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add: Report: Create Reports");
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);                       
        }
    
        [CodedStep(@"Click on Startnow link")]
        public void TST_CF_005_CS15()
        {
                        Pages.PS_CreateReportsPage.CreateRepStartNowLink.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Report Wizard: Projects: New report Page to be Loaded")]
        public void TST_CF_005_CS16()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add: Report: Report Wizard: New");
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.AddRepWizTypeLink.IsVisible(),"Type link should be present");
                                                                                                
        }
    
        [CodedStep(@"Select category of report")]
        public void TST_CF_005_CS17()
        {
                        Pages.PS_AddReportWizardPage.CategorySelect.Wait.ForExists();
                        Pages.PS_AddReportWizardPage.CategorySelect.SelectByText("Projects");
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select Type portion of report")]
        public void TST_CF_005_CS18()
        {
                        Pages.PS_AddReportWizardPage.TypeSelect.Wait.ForExists();
                        Pages.PS_AddReportWizardPage.TypeSelect.SelectByText("Projects");
                        ActiveBrowser.WaitUntilReady();
                                                                                    
        }
    
        [CodedStep(@"Verify decription and example field are present")]
        public void TST_CF_005_CS19()
        {
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionH4Tag.IsVisible(),"Decription tag should be present");
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.DescriptionTableCell.IsVisible(),"Description field should be present");
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.AddRepWizExampleH4Tag.IsVisible(),"Example tag should be present");
                                                                                    
        }
    
        [CodedStep(@"Click on continue button")]
        public void TST_CF_005_CS20()
        {
                        Pages.PS_AddReportWizardPage.AddRepWizContinueBtn.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is directed to 'Defination' part of report wizard")]
        public void TST_CF_005_CS21()
        {
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.DefinationPortfolioSelect.IsVisible(),"Portfolio dropdown list should be present");
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.WorkCheckbox.IsVisible(),"Work chechbox should be present");
        }
    
        [CodedStep(@"Select Work type in 'Defination' part of report")]
        public void TST_CF_005_CS22()
        {
                        Pages.PS_AddReportWizardPage.CustomProjChkbox.Click();
        }
    
        [CodedStep(@"Verify user is directed to 'Columns' part of report")]
        public void TST_CF_005_CS23()
        {
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.ProjectLink.IsVisible(),"Project link should be present");
                        Assert.IsTrue(Pages.PS_AddReportWizardPage.ProjectBestPracticeLink.IsVisible(),"Project/Best Practice link should be present");
        }
    
        [CodedStep(@"Click on 'Project/Custom Fields' link and select 'All' named checkbox")]
        public void TST_CF_005_CS24()
        {
                        Pages.PS_AddReportWizardPage.ProjectCustomFieldsLink.Click();
                        System.Threading.Thread.Sleep(1000);
                        ActiveBrowser.RefreshDomTree();
                        HtmlInputCheckBox allCustomFields = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(AppLocators.get("column_custom_fields_all_checkbox"));
                        allCustomFields.Wait.ForExists();
                        allCustomFields.Click();
        }
    
        [CodedStep(@"Run the report and view in 'HTML")]
        public void TST_CF_005_CS25()
        {
            Pages.PS_AddReportWizardPage.RunBtn.MouseClick(MouseClickType.LeftClick);
            Pages.PS_AddReportWizardPage.RunPopUpTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddReportWizardPage.RunPopUpTable.IsVisible(),"Run Menu div should be present");           
            Pages.PS_AddReportWizardPage.RunReportHtmlTd.Click();
        }
    
        [CodedStep(@"Connect to Report pop-up window")]
        public void TST_CF_005_CS26()
        {
            Manager.WaitForNewBrowserConnect("/reports/ProgressScreen.epage", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
                                                
        }

        
        [CodedStep(@"Verify generated Custom Fields column is present")]
        public void TST_CF_005_CS27()
        {
            HtmlSpan customFieldColumn = ActiveBrowser.Find.ByXPath<HtmlSpan>(string.Format(AppLocators.get("report_custom_field_column"),GetExtractedValue("GeneratedCustomFieldName")));
            customFieldColumn.Wait.ForExists();
            customFieldColumn.IsVisible();
        }
        
        [CodedStep(@"Close the report html page")]
        public void TST_CF_005_CS28()
        {
                       
            Manager.ActiveBrowser.Close();
                        
        }
    
        [CodedStep(@"Delete created custom tag")]
        public void TST_CF_005_CS29()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            
            Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
            ActiveBrowser.WaitUntilReady();          
        }
    }
}
