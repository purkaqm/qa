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

    public class TST_CF_004 : BaseWebAiiTest
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
        public void TST_CF_004_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }

        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_CF_004_CS02()
        {
            Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
    
        
        
        [CodedStep(@"Wait till Add/Edit Custom Field pop up is displayed")]
        public void TST_CF_004_CS03()
        {
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();                       
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();                                    
                                                                                                          
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_CF_004_CS04()
        {
                        string custFieldName = Data["CustomFieldName"].ToString() + Randomizers.generateRandomInt(1000,9999);
                        ActiveBrowser.Window.SetFocus();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
                        Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
                        SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TST_CF_004_CS05()
        {
                        if(Data["CustomFieldDesc"].ToString().Length > 0){
                        ActiveBrowser.Window.SetFocus();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Click();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Focus();
                        Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea, Data["CustomFieldDesc"].ToString());
                        }
        }
    
        [CodedStep(@"Choose Field Type")]
        public void TST_CF_004_CS06()
        {
                       if(Data["CustomFieldType"].ToString().Length > 0){
                           Pages.PS_CustomFieldsPage.AddCustFieldTypeSelector.SelectByText(Data["CustomFieldType"].ToString(),true);
                                    
                        }
                                                
        }
    
        [CodedStep(@"Verify tooltip is displayed - Maximum Text Length")]
        public void TST_CF_004_CS07()
        {
                          ActiveBrowser.RefreshDomTree();
                          Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthColumn.IsVisible(),"Maximum test length tooltip should be displayed");
                          Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthInput.IsVisible());
        }
    
        [CodedStep(@"Set 'Maximum Text Length' to 10000")]
        public void TST_CF_004_CS08()
        {
                        ActiveBrowser.Window.SetFocus();
                        //Pages.PS_CustomFieldsPage.MaximumTextLengthInput.Click();
                        Pages.PS_CustomFieldsPage.MaximumTextLengthInput.MouseClick(MouseClickType.LeftClick);
                        Manager.Desktop.KeyBoard.TypeText("10000",2);
                                    
        }
    
        [CodedStep(@"Choose Work Item")]
        public void TST_CF_004_CS09()
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
        public void TST_CF_004_CS10()
        {
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Focus();
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.MouseHover();
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Click(true);
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
                                                  
        }
    
        [CodedStep(@"Verify new 'Custom field' is created")]
        public void TST_CF_004_CS11()
        {
                        HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
                        customFieldRow.Wait.ForExists();
                        Assert.IsTrue(customFieldRow.IsVisible());
        }
        
        [CodedStep("Click on 'Add New' button on dashboard layout page")]
        public void TST_CF_004_CS12()
        {
            Pages.PS_ReviewDashboardPage.AddNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Dashboard Layout is created")]
        public void TST_CF_004_CS13()
        {
            HtmlImage showMoreLess = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("dashboard_layout_show_more_less"));
            showMoreLess.Wait.ForExists();
            showMoreLess.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            
            
            HtmlTableRow layoutRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("generated_dashboard_layout_row"),GetExtractedValue("GeneratedLayoutName").ToString()));
            layoutRow.Wait.ForExists();
            Assert.IsTrue(layoutRow.IsVisible());
        }
        
        [CodedStep(@"open generated Dashboard layout")]
        public void TST_CF_004_CS14()
        {
            HtmlAnchor layoutLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("generated_dashboard_layout_link"),GetExtractedValue("GeneratedLayoutName").ToString()));
            layoutLink.Wait.ForExists();
            layoutLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Continue button")]
        public void TST_CF_004_CS15()
        {
            Pages.PS_CreateDashboardLayoutPage.ContinueButton.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Add the custom field column")]
        public void TST_CF_004_CS16()
        {
            Pages.PS_CreateDashboardLayoutPage.AddCustomFieldsImage.Click();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CreateDashboardLayoutPage.AddCustomFeildPopUpCustomFieldSelect.SelectByText(GetExtractedValue("GeneratedCustomFieldName").ToString());
            Actions.SetText(Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpColumnName,GetExtractedValue("GeneratedCustomFieldName").ToString());
            Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength.Focus();
            Actions.SetText(Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength,"");
            Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength.MouseClick(MouseClickType.LeftClick);
            Manager.Desktop.KeyBoard.TypeText("10000",2);
            
            Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpAddCFButton.Click();
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CreateDashboardLayoutPage.FinishSaveChangesButton.Focus();
            Pages.PS_CreateDashboardLayoutPage.FinishSaveChangesButton.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }

    
        [CodedStep(@"Click on left navigation Review tab")]
        public void TST_CF_004_CS17()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Click();
        }
    
        [CodedStep(@"Select Portfolio and Layout on dashboard page")]
        public void TST_CF_004_CS18()
        {
            Pages.PS_ReviewDashboardPage.DashboardLeftPortfolioSelectorSelect.SelectByText("My Projects");
            Pages.PS_ReviewDashboardPage.DashboardLayoutSelectorSelect.SelectByText(GetExtractedValue("GeneratedLayoutName").ToString());
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_CF_004_CS19()
        {
            Pages.PS_ReviewDashboardPage.GoButton.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Verify Custom field is displayed in the Dashboard page")]
        public void TST_CF_004_CS20()
        {
            if(Pages.PS_ReviewDashboardPage.EmptyFilteredOutClearDiv.IsVisible()){
                Pages.PS_ReviewDashboardPage.EmptyFilteredOutClearDiv.Click();
                ActiveBrowser.RefreshDomTree();
            }
            
            System.Threading.Thread.Sleep(15000);
            if(Pages.PS_ReviewDashboardPage.EmptyFilteredOutClearDiv.IsVisible()){
                Pages.PS_ReviewDashboardPage.EmptyFilteredOutClearDiv.Click();
                ActiveBrowser.RefreshDomTree();
            }
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell customFieldColumn = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("dashboard_custom_field_column"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldColumn.Wait.ForExists();
            Assert.IsTrue(customFieldColumn.IsVisible(),"Custom field column should be present");
        }
    
        [CodedStep(@"Delete generated Dashboard layout")]
        public void TST_CF_004_CS21()
        {
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format(AppLocators.get("generated_dashboard_layout_checkbox"),GetExtractedValue("GeneratedLayoutName").ToString()));
            HtmlInputCheckBox delLayoutCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("generated_dashboard_layout_checkbox"),GetExtractedValue("GeneratedLayoutName").ToString()));
            delLayoutCheckbox.Wait.ForExists();
            delLayoutCheckbox.Click();
            
            //click on delete button
            Pages.PS_ReviewDashboardPage.DeleteButton.Click();
            ActiveBrowser.WaitUntilReady();
            
            Pages.PS_AdminDashboardLayoutsPage.DeleteDashbaordLayoutOkButton.Click();
            ActiveBrowser.WaitUntilReady();
            
        }

        [CodedStep(@"Delete created custom tag")]
        public void TST_CF_004_CS22()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            
            Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
            ActiveBrowser.WaitUntilReady();          
        }
    }
}
