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

    public class TST_SRT_017 : BaseWebAiiTest
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
        public void CS_SRT_017_01()
        {
                                                Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                                Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Click Down Arrow")]
        public void CS_SRT_017_02()
        {
                                     string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string srtEditImgLocator = string.Format(AppLocators.get("statur_report_table_record_edit_img"),srtName);
                                    HtmlImage srtEditImg = ActiveBrowser.Find.ByXPath<HtmlImage>(srtEditImgLocator);
                                    srtEditImg.MouseClick(MouseClickType.LeftClick);
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                   
        }
    
        [CodedStep(@"Click Statistics option")]
        public void CS_SRT_017_03()
        {
                         Pages.PS_StatusReportTemplatesPage.SRTRecordStatisticsTab.Wait.ForExists();
                         Pages.PS_StatusReportTemplatesPage.SRTRecordStatisticsTab.Wait.ForVisible();
                         Pages.PS_StatusReportTemplatesPage.SRTRecordStatisticsTab.MouseClick(MouseClickType.LeftClick);
        }
        
        [CodedStep(@"Verify template statistics pop  up is displayed with active work items count and total status report count")]
        public void CS_SRT_017_04()
        {
                       
                        Pages.PS_StatusReportTemplatesPage.SRTStatisticsActiveData.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.SRTStatisticsTotalData.Wait.ForVisible();
            
                        string activeStr =  Pages.PS_StatusReportTemplatesPage.SRTStatisticsActiveData.InnerText;
                        string totalStr =  Pages.PS_StatusReportTemplatesPage.SRTStatisticsTotalData.InnerText;
                        
                        Pages.PS_StatusReportTemplatesPage.SRTStatisticsOKBtn.MouseClick(MouseClickType.LeftClick);
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
        
         [CodedStep(@"Delete given status report template")]
        public void CS_SRT_017_05()
        {
                                  
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
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
                                    ActiveBrowser.WaitUntilReady();
                                    ActiveBrowser.RefreshDomTree();
        }
        
        
    }
}
