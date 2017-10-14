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

    public class TST_SRT_003 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Default Status Report Template record link")]
        public void CS_SRT_003_01()
        {
            Pages.PS_StatusReportTemplatesPage.StatusReportDiv.Wait.ForExists();
            Pages.PS_StatusReportTemplatesPage.StatusReportDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
        
        [CodedStep(@"Verify Edit Status Report Template is displayed with all default values")]
        public void CS_SRT_003_02()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
            
            string name = Actions.InvokeScript("document.getElementById('addNewTemplateDialogName').value;");
            Assert.IsTrue(name.Equals("Status Report"), "'Status Report' should be present in name field");
            
            string description = Actions.InvokeScript("document.getElementById('addNewTemplateDialogDsc').value;");
            Assert.IsTrue(description.Equals("Default Status Report"), "'Default Status Report' should be present in description text area");
            
            string active =Pages.PS_StatusReportTemplatesPage.AddNewTemplateActiveCheckbox.BaseElement.GetAttribute("checked").Value.ToString();
            Assert.IsTrue(active.Equals("true"), "Active checkbox should be checked by default");
           
        }
        
        [CodedStep(@"Click on Cancel button")]
        public void CS_SRT_003_03()
        {
            
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateCancelBtn.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    }
}
