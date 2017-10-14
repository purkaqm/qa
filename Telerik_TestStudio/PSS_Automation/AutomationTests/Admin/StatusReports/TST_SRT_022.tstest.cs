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

    public class TST_SRT_022 : BaseWebAiiTest
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
        public void CS_SRT_022_01()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        
    
        [CodedStep(@"Click on Add New button for custom field creation")]
        public void CS_SRT_022_02()
        {
                                                Pages.PS_CustomFieldsPage.AddNewBtn.Wait.ForExists();
                                                Pages.PS_CustomFieldsPage.AddNewBtn.Click();
                                                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Deactiate the SRT")]
        public void CS_SRT_022_03()
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
    
        [CodedStep(@"Verify that other association column displays inactive status report icon")]
        public void CS_SRT_022_04()
        {
                        
                                    System.Threading.Thread.Sleep(5000);
                                    string cFieldName = GetExtractedValue("GeneratedCustomFieldName").ToString();
                                    string statusReportIcon = string.Format(AppLocators.get("custom_field_record_other_association"),cFieldName);
                                    Assert.IsTrue(ActiveBrowser.Find.AllByXPath(statusReportIcon).Count > 0, "other association column displays status report icon");
                                    HtmlImage icon = ActiveBrowser.Find.ByXPath<HtmlImage>(statusReportIcon);
                                    Assert.IsTrue(icon.BaseElement.GetAttribute("title").Value.Contains("inactive"), "other association column should display inactive status report icon");
        }
    
        [CodedStep(@"Delete created custom field")]
        public void CS_SRT_022_05()
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
    
        [CodedStep(@"Delete created SRT")]
        public void CS_SRT_022_06()
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
                                    
                                    Pages.PS_StatusReportTemplatesPage.DeleteConfirmBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.DeleteConfirmBtn.Click();
                                     ActiveBrowser.WaitUntilReady();
                                     ActiveBrowser.RefreshDomTree();
        }
    
        
    
        [CodedStep(@"Click on created custom field link")]
        public void CS_SRT_022_07()
        {
            string cFieldName = GetExtractedValue("GeneratedCustomFieldName").ToString();
            string cfNameLinkLocator = string.Format(AppLocators.get("custom_fields_record_name_link"),cFieldName);
            HtmlDiv cfNameLink = ActiveBrowser.Find.ByXPath<HtmlDiv>(cfNameLinkLocator);
            cfNameLink.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            
            Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
            Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();
        }
        
        [CodedStep(@"Verify that inactive SRT can not be edited/deleted from other association")]
        public void CS_SRT_022_08()
        {
             Pages.PS_CustomFieldsPage.AddCustFieldOtherAssocDiv.Click();
             ActiveBrowser.RefreshDomTree();
             Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForExists();
             Pages.PS_StatusReportTemplatesPage.DoneButton.Wait.ForVisible();
                           
                           
                           
             string objectTypeStr = GetExtractedValue("GeneratedStatusReportTemplateName").ToString();
             System.Threading.Thread.Sleep(3000);
             string workItemCheckboxLocator = string.Format(AppLocators.get("add_status_report_tmplt_work_item"),objectTypeStr);
             Assert.IsTrue(ActiveBrowser.Find.AllByXPath(workItemCheckboxLocator).Count == 0, "InActivated SRT should not be present in list");
            
             Pages.PS_CustomFieldsPage.AddCustFieldCancelButton.Click();
             ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                           
        }
    }
}
