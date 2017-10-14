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

    public class TS_Create_Custom_Field_With_Given_Name : BaseWebAiiTest
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

    
        [CodedStep(@"Wait till Add/Edit Custom Field pop up is displayed")]
        public void TS_CS_00()
        {
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();                       
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();                                    
                                                          
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CS_01()
        {
            string custFieldName = Data["CustomFieldName"].ToString();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
            Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
            SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CS_02()
        {
            if(Data["CustomFieldDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Click();
            Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Focus();
            Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea, Data["CustomFieldDesc"].ToString());
            
            }
        }
    
        [CodedStep(@"Add Administrators")]
        public void TS_CS_03()
        {
            //code for adding administrator
        }
    
        [CodedStep(@"Choose Field Type")]
        public void TS_CS_04()
        {
           if(Data["CustomFieldType"].ToString().Length > 0){
               Pages.PS_CustomFieldsPage.AddCustFieldTypeSelector.SelectByText(Data["CustomFieldType"].ToString(),true);
                        
            }
        }
    
        [CodedStep(@"Chhose option for Apply Permission")]
        public void TS_CS_05()
        {
            if(Data["FieldApplyPermission"].ToString().ToLower().Contains("yes")){
                Pages.PS_CustomFieldsPage.AddCustFieldApplyUserPermCB.Click();
            }
                       
        }
    
        [CodedStep(@"Chhose option for Required")]
        public void TS_CS_06()
        {
            if(Data["FieldIsReqiured"].ToString().ToLower().Contains("yes")){
                Pages.PS_CustomFieldsPage.AddCustFieldRequiredCB.Click();
            }
                       
        }
    
        [CodedStep(@"Chhose option for Locked")]
        public void TS_CS_07()
        {
            if(Data["FieldIsLocked"].ToString().ToLower().Contains("yes")){
                Pages.PS_CustomFieldsPage.AddCustFieldFieldLockedCB.Click();
            }
                       
        }
    
        [CodedStep(@"Choose Work Item")]
        public void TS_CS_08()
        {
           if(!Data["FieldWorkItem"].ToString().ToLower().Contains("no")){
               Pages.PS_CustomFieldsPage.AddCustFieldWorkTypesDiv.Click();
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
    
        [CodedStep(@"Choose Other Association Items")]
        public void TS_CS_09()
        {
           if(!Data["FieldOtherAssociation"].ToString().ToLower().Contains("no")){
               Pages.PS_CustomFieldsPage.AddCustFieldOtherAssocDiv.Click();
               ActiveBrowser.RefreshDomTree();
               Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForExists();
               Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForVisible();
               
               
               
               string objectTypeStr = Data["FieldOtherAssociation"].ToString();
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
        public void TS_CS_10()
        {
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Focus();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.MouseHover();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
              
        }
    }
}
