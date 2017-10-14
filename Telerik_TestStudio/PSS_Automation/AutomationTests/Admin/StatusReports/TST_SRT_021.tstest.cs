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

    public class TST_SRT_021 : BaseWebAiiTest
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
        public void CS_SRT_021_01()
        {
                                    Pages.PS_CustomFieldsPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_CustomFieldsPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Verify that newly added statur report template is added to the table")]
        public void CS_SRT_021_02()
        {
            
                        System.Threading.Thread.Sleep(5000);
                        string cFieldName = GetExtractedValue("GeneratedCustomFieldName").ToString();
                        string expectedCFieldRecordLocator = string.Format(AppLocators.get("custom_fields_table_record"),cFieldName);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(expectedCFieldRecordLocator).Count > 0, "Newly Created Custom Field should be displayed in table");
        }
        
        [CodedStep(@"Verify that other association column displays status report icon")]
        public void CS_SRT_021_03()
        {
            
                        System.Threading.Thread.Sleep(5000);
                        string cFieldName = GetExtractedValue("GeneratedCustomFieldName").ToString();
                        string statusReportIcon = string.Format(AppLocators.get("custom_field_record_other_association"),cFieldName);
                        Assert.IsTrue(ActiveBrowser.Find.AllByXPath(statusReportIcon).Count > 0, "other association column displays status report icon");
                        HtmlImage icon = ActiveBrowser.Find.ByXPath<HtmlImage>(statusReportIcon);
                        Assert.IsTrue(icon.BaseElement.GetAttribute("title").Value.Contains("Status Report"), "other association column displays status report icon");
        }
        
        [CodedStep(@"Delete created custom field")]
        public void CS_SRT_021_04()
        {
            
                        System.Threading.Thread.Sleep(5000);
                        string cFieldName = GetExtractedValue("GeneratedCustomFieldName").ToString();
                        string deleteFieldIcon = string.Format(AppLocators.get("custom_field_record_delete_icon"),cFieldName);
                        HtmlImage deleteIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(deleteFieldIcon);
                        deleteIcon.Click();
                        
                        Pages.PS_CustomFieldsPage.DeleteYesBtn.Wait.ForExists();
                        Pages.PS_CustomFieldsPage.DeleteYesBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
        
        
    }
}
