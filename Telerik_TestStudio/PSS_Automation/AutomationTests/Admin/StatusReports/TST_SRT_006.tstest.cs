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

    public class TST_SRT_006 : BaseWebAiiTest
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
        public void CS_SRT_006_01()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
     
    
        [CodedStep(@"Verify that newly added statur report template is added to the table")]
        public void CS_SRT_006_03()
        {
                        string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                        string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record"),srtName);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedSRTRecordLocator).Count > 0, "Newly Created SRT should be displayed in table");
        }
    
        [CodedStep(@"[TST_SRT_005_CodedStep] : Inactivate SRT and Delete")]
        public void CS_SRT_006_04()
        {
                        //Open Edit Statut Report Template Pop Up
                        string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                        string srtLinkLocator = string.Format(AppLocators.get("statur_report_table_record_name_link"),srtName);
                        HtmlDiv srtNameLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(srtLinkLocator);
                        srtNameLink.Click();
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
                        
                        //Deactivate selected Status Report Template
                         Pages.PS_StatusReportTemplatesPage.AddNewTemplateActiveCheckbox.Click();
                         Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.Click();
                         ActiveBrowser.WaitUntilReady();
                         ActiveBrowser.RefreshDomTree();
                        
                        //Delete given status report template
                        string srtEditImgLocator = string.Format(AppLocators.get("statur_report_table_record_edit_img"),srtName);
                        HtmlImage srtEditImg = ActiveBrowser.Find.ByXPath<HtmlImage>(srtEditImgLocator);
                        srtEditImg.MouseClick(MouseClickType.LeftClick);
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.Wait.ForVisible();
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.MouseClick(MouseClickType.LeftClick);
                        
                        Pages.PS_StatusReportTemplatesPage.DeleteConfirmBtn.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.DeleteConfirmBtn.Click();
        }
    
        [CodedStep(@"Choose 'Configuration - Status Report Templates' from logs category and click Go button")]
        public void CS_SRT_006_05()
        {
            Pages.PS_LogsPage.ViewDropDownList.SelectByText("Configuration - Status Report Templates", true);
            Pages.PS_LogsPage.GoBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
         
        }
        
        [CodedStep(@"Verify Configuration - Status Reports event (about adding new SRT) is added.")]
        public void CS_SRT_006_06()
        {
            string username = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value;
            string logEvent = "created status report template";
            string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();              
            string logRecordLocatorStr = string.Format(AppLocators.get("admin_log_rec_desc"),username, logEvent, srtName);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(logRecordLocatorStr).Count > 0, "Configuration - Status Reports event (about adding new SRT) should be added to log");
        }
    }
}
