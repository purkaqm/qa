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

    public class TST_SRT_010 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Add New button")]
        public void CS_SRT_010_01()
        {
                                                Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                                Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
      
        [CodedStep(@"Verify that newly added statur report template is added to the table")]
        public void CS_SRT_010_03()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record"),srtName);
                                    Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedSRTRecordLocator).Count > 0, "Newly Created SRT should be displayed in table");
        }
    
        [CodedStep(@"Click on Newly Craeted Template Name Link")]
        public void CS_SRT_010_04()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record_name_link"),srtName);
                                    HtmlDiv srtNameLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(expectedSRTRecordLocator);
                                    srtNameLink.Wait.ForExists();
                                    srtNameLink.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Verify Edit Status Report Template is displayed")]
        public void CS_SRT_010_05()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
                                    
                                    string name = Actions.InvokeScript("document.getElementById('addNewTemplateDialogName').value;");
                                    Assert.IsTrue(name.Equals(srtName), srtName + " should be present in name field for edit pop up");
                                    
                                   
                                   
        }
    
        [CodedStep(@"Edit Template Name and Save")]
        public void CS_SRT_010_06()
        {
            string editedTemplateName = "Edited"+Randomizers.generateRandomInt(10000,99999);
            ActiveBrowser.Window.SetFocus();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Click();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Focus();
            Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput, editedTemplateName);
            
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            
            SetExtractedValue("GeneratedStatusReportTemplateName",editedTemplateName);
        }
        
        [CodedStep(@"Verify that edited template is reflected to the table")]
        public void CS_SRT_010_07()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record"),srtName);
                                    Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedSRTRecordLocator).Count > 0, "Newly Created SRT should be displayed in table");
        }
    }
}
