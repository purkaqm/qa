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

    public class TST_NAV_UI_037 : BaseWebAiiTest
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
    
        [CodedStep(@"Open any Project/Work type link")]
        public void TST_NAV_UI_037_CodedStep()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.Wait.ForVisible();
        }
    
        //[CodedStep(@"Click Edit Group Title Image")]
        //public void TST_NAV_UI_037_CodedStep1()
        //{
            //Pages.PS_FieldManagementEditPage.AddGroupingDiv.Click();
            //ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            //Pages.PS_FieldManagementEditPage.EditGroupImage.Wait.ForVisible();
            
            //Pages.PS_FieldManagementEditPage.EditGroupImage.Click();
            //Pages.PS_FieldManagementEditPage.EditGroupDialogDiv.Wait.ForVisible();
            //Pages.PS_FieldManagementEditPage.EditGroupPopupSaveBtn.Wait.ForVisible();
        //}
    
        [CodedStep(@"Verify that Save button is primary and Cancel button is secondary")]
        public void TST_NAV_UI_037_CodedStep2()
        {
            string saveClassAttr = Pages.PS_StatusReportTemplatesPage.AddNewTemplateSaveBtn.BaseElement.GetAttribute("class").Value;
            string cancelClassAttr = Pages.PS_StatusReportTemplatesPage.AddNewTemplateCancelBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(saveClassAttr.Equals("btn"),"Save button should be primary");
            Assert.IsTrue(cancelClassAttr.Equals("btn-white"),"Cancel button should be secondary");
        }
    
        [CodedStep(@"Click Cancel and go to Home page")]
        public void TST_NAV_UI_037_CodedStep3()
        {
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateCancelBtn.Click();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
