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

    public class TST_SRT_011 : BaseWebAiiTest
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
        public void CS_SRT_011_01()
        {
                                                            Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                                            Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                                            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
      
    
      
    
        [CodedStep(@"Click on Newly Craeted Template Name Link")]
        public void CS_SRT_011_03()
        {
                                                string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                                string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record_name_link"),srtName);
                                                HtmlDiv srtNameLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(expectedSRTRecordLocator);
                                                srtNameLink.Wait.ForExists();
                                                srtNameLink.Click();
                                                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
           
        [CodedStep(@"Edit Template Name and Save")]
        public void CS_SRT_011_04()
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
    
        
    
        [CodedStep(@"Choose 'Configuration - Status Report Templates' from logs category and click Go button")]
        public void CS_SRT_011_05()
        {
                        Pages.PS_LogsPage.ViewDropDownList.SelectByText("Configuration - Status Report Templates", true);
                        Pages.PS_LogsPage.GoBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        System.Threading.Thread.Sleep(3000);
                        
                     
        }
    
        [CodedStep(@"Verify Configuration - Status Reports event (about editing new SRT) is added.")]
        public void CS_SRT_011_06()
        {
                        string username = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value;
                        string logEvent = "edited status report template";
                        string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();     
                        
                        string logRecordLocatorStr = string.Format(AppLocators.get("admin_log_rec_desc"),username, logEvent, srtName);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(logRecordLocatorStr).Count > 0, "Configuration - Status Reports event (about editing new SRT) should be added to log");
        }
    }
}
