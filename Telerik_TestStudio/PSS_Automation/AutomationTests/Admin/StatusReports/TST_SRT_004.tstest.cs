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

    public class TST_SRT_004 : BaseWebAiiTest
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
        public void CS_SRT_004_01()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
            Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
        
        [CodedStep(@"Verify Add new Status Report Template is displayed with all fields")]
        public void CS_SRT_004_02()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
            
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.IsVisible(), "Name field should be present in pop up");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.IsVisible(), "Description text area should be present in pop up");
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewTemplateActiveCheckbox.IsVisible(), "Active checkbox should be present in checkbox");
           
        }
    }
}
