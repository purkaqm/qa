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

    public class TST_CF_003 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_CF_003_CS01()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
                                                   
        }
        
        [CodedStep(@"Store Url of Project")]
        public void TST_CF_003_CS02()
        {
            SetExtractedValue("ProjectUrl",ActiveBrowser.Url);
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_CF_003_CS03()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_CF_003_CS04()
        {
            Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
        
        [CodedStep(@"Wait till Add/Edit Custom Field pop up is displayed")]
        public void TST_CF_003_CS05()
        {
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();                       
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();                                    
                                                                                              
        }
        
        [CodedStep(@"Enter Name")]
        public void TST_CF_003_CS06()
        {
            string custFieldName = Data["CustomFieldName"].ToString() + Randomizers.generateRandomInt(1000,9999);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
            Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
            SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TST_CF_003_CS07()
        {
            if(Data["CustomFieldDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Click();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Focus();
            Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea, Data["CustomFieldDesc"].ToString());
            }
        }
    
        [CodedStep(@"Choose Field Type")]
        public void TST_CF_003_CS08()
        {
           if(Data["CustomFieldType"].ToString().Length > 0){
               Pages.PS_CustomFieldsPage.AddCustFieldTypeSelector.SelectByText(Data["CustomFieldType"].ToString(),true);
                        
            }
                                    
        }
        
        [CodedStep(@"Verify tooltip is displayed - Maximum Text Length")]
        public void TST_CF_003_CS09()
        {
              ActiveBrowser.RefreshDomTree();
              Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthColumn.IsVisible(),"Maximum test length tooltip should be displayed");
              Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthInput.IsVisible());
        }
        
        [CodedStep(@"Set 'Maximum Text Length' to 10000")]
        public void TST_CF_003_CS10()
        {
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CustomFieldsPage.MaximumTextLengthInput.Click();
            Manager.Desktop.KeyBoard.TypeText("10000",2);
                        
        }
        
        [CodedStep(@"Choose Work Item")]
        public void TST_CF_003_CS11()
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
        public void TST_CF_003_CS12()
        {
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Focus();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.MouseHover();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Click(true);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
                                      
        }
    
        [CodedStep(@"Verify new 'Custom field' is created")]
        public void TST_CF_003_CS13()
        {
            HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldRow.Wait.ForExists();
            Assert.IsTrue(customFieldRow.IsVisible());
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_CF_003_CS14()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectUrl").ToString());
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify 'Custom Field' is displayed in Project details")]
        public void TST_CF_003_CS15()
        {
            HtmlTableCell customFieldColumn = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("created_custom_field_div"),GetExtractedValue("GeneratedCustomFieldName").ToString())); 
        }

        
    
        [CodedStep(@"Delete created custom tag")]
        public void TST_CF_003_CS16()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            
            Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
            ActiveBrowser.WaitUntilReady();          
        }
    }
}
