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

    public class TST_SRT_012 : BaseWebAiiTest
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
        public void CS_SRT_012_01()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
       
    
        [CodedStep(@"Verify that newly added statur report template is added to the table")]
        public void CS_SRT_012_03()
        {
                        string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                        string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record"),srtName);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedSRTRecordLocator).Count > 0, "Newly Created SRT should be displayed in table");
        }
    
        [CodedStep(@"Deactiate the SRT")]
        public void TST_SRT_012_CodedStep()
        {
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
        }
    
        [CodedStep(@"Open Delete Pop up and perform Cancel operation")]
        public void TST_SRT_012_CodedStep1()
        {
                     //Delete given status report template
            string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                        string srtEditImgLocator = string.Format(AppLocators.get("statur_report_table_record_edit_img"),srtName);
                        HtmlImage srtEditImg = ActiveBrowser.Find.ByXPath<HtmlImage>(srtEditImgLocator);
                        srtEditImg.MouseClick(MouseClickType.LeftClick);
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.Wait.ForVisible();
                        Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.MouseClick(MouseClickType.LeftClick);
                        
                        Pages.PS_StatusReportTemplatesPage.DeleteCancelBtn.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.DeleteCancelBtn.Click();
                         ActiveBrowser.WaitUntilReady();
                         ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify delete pop up disappears and status report not deleted")]
        public void TST_SRT_012_CodedStep2()
        {
            
            Pages.PS_StatusReportTemplatesPage.SRTRecordDeleteTab.Wait.ForVisibleNot();
            string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
            string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record"),srtName);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedSRTRecordLocator).Count > 0, "Newly Created SRT should be displayed in table");
        }
    }
}
