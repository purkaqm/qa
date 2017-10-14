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

    public class TST_SRT_015 : BaseWebAiiTest
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
        public void CS_SRT_015_01()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Verify that newly added statur report template is added to the table as inactive")]
        public void CS_SRT_015_02()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record_name_icon"),srtName);
                                    HtmlImage iconImg = ActiveBrowser.Find.ByXPath<HtmlImage>(expectedSRTRecordLocator);
                                    Assert.IsTrue(iconImg.BaseElement.GetAttribute("title").Value.ToString().Equals("Inactive"), "Newly Created SRT should be displayed as inactive in table");
        }
    
       
        [CodedStep(@"Click on Newly Craeted Template Name Link")]
        public void CS_SRT_015_04()
        {
                                                            string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                                            string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record_name_link"),srtName);
                                                            HtmlDiv srtNameLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(expectedSRTRecordLocator);
                                                            srtNameLink.Wait.ForExists();
                                                            srtNameLink.Click();
                                                            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Edit Template Activity from inactive to active and save")]
        public void CS_SRT_015_05()
        {
                                    //change name
                                    string editedTemplateName = "Edited"+Randomizers.generateRandomInt(10000,99999);
                                    ActiveBrowser.Window.SetFocus();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Click();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Focus();
                                    Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput, editedTemplateName);
                                                        
                                    //make the template active
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateActiveCheckbox.Click();
            
                                    //save the template
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.Click();
                                    ActiveBrowser.WaitUntilReady();
                                    ActiveBrowser.RefreshDomTree();
                                    
                                    SetExtractedValue("GeneratedStatusReportTemplateName",editedTemplateName);
        }
    
        [CodedStep(@"Verify that the template is displayed as active in the table")]
        public void CS_SRT_015_06()
        {
                                    string srtName = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
                                    string expectedSRTRecordLocator = string.Format(AppLocators.get("statur_report_table_record_name_icon"),srtName);
                                    HtmlImage iconImg = ActiveBrowser.Find.ByXPath<HtmlImage>(expectedSRTRecordLocator);
                                    Assert.IsTrue(iconImg.BaseElement.GetAttribute("title").Value.ToString().Equals("Active"), "Newly Created SRT should be displayed as active in table");
        }
    }
}
