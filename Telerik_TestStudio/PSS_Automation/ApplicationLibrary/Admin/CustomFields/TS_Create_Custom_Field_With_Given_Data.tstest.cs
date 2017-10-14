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

    public class TS_Create_Custom_Field_With_Given_Data : BaseWebAiiTest
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
    
        [CodedStep(@"Wait till Add/Edit Status Report Template is displayed")]
        public void TS_CS_00()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
                                                          
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CS_01()
        {
            string templateName = Data["StatusRepTemplateName"].ToString()+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Click();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Focus();
            Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput, templateName);
            //Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.SetValue("value",metricName);
            SetExtractedValue("GeneratedStatusReportTemplateName",templateName);
        }
    
        [CodedStep(@"Enter Description")]
        public void TS_CS_02()
        {
            if(Data["StatusRepTemplateDesc"].ToString().Length > 0){
            ActiveBrowser.Window.SetFocus();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.Click();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.Focus();
            Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea, Data["StatusRepTemplateDesc"].ToString());
            //Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.SetValue("value",Data["StatusRepTemplateDesc"].ToString());
            
            }
        }
    
        [CodedStep(@"Select active checkbox")]
        public void TS_CS_03()
        {
            if(Data["StatusRepTemplateActive"].ToString().ToLower().Contains("yes")){
                Pages.PS_StatusReportTemplatesPage.AddNewTemplateActiveCheckbox.Click();
            }
                       
        }
    
        [CodedStep(@"Choose Work Item")]
        public void TS_CS_04()
        {
           if(!Data["StatusRepTemplateWorkItem"].ToString().ToLower().Contains("no")){
               Pages.PS_StatusReportTemplatesPage.AddNewTmplWorkItemsSelDiv.Click();
               ActiveBrowser.RefreshDomTree();
               Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForExists();
               Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForVisible();
               
               
               
               string objectTypeStr = Data["StatusRepTemplateWorkItem"].ToString();
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
           }
                        
        }
    
        [CodedStep(@"Click Save button")]
        public void TS_CS_05()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
                                    
        }
    }
}
