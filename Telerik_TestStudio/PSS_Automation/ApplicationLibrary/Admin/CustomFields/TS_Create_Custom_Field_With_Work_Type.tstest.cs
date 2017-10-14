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

    public class TS_Create_Custom_Field_With_Work_Type : BaseWebAiiTest
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
    
        [CodedStep(@"Enter Name")]
        public void TS_CF_01()
        {
                        string custFieldName = Data["CustomFieldName"].ToString() + Randomizers.generateRandomInt(1000,9999);
                        ActiveBrowser.Window.SetFocus();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
                        Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
                        SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CF_02()
        {
                        if(Data["CustomFieldDesc"].ToString().Length > 0){
                        ActiveBrowser.Window.SetFocus();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Click();
                        Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea.Focus();
                        Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogDscTextArea, Data["CustomFieldDesc"].ToString());
                        }
        }
    
        [CodedStep(@"Choose Work Item")]
        public void TS_CF_03()
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
        public void TS_CF_04()
        {
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Focus();
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.MouseHover();
                        Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Click(true);
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
                                                  
        }
    }
}
