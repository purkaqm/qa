using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using ArtOfTest.WebAii.Win32.Dialogs;
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

    public class TST_NAV_UI_036 : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_036_CodedStep()
        {
            string projWorkTypeLinkLocator = "//tr/td[1]/a[2]";
            Manager.ActiveBrowser.WaitForElement(new HtmlFindExpression("xpath="+projWorkTypeLinkLocator), Manager.Settings.ElementWaitTimeout,false);
            HtmlAnchor projWorkTypeLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(projWorkTypeLinkLocator);
            projWorkTypeLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_FieldManagementEditPage.AddGroupingDiv.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_036_CodedStep1()
        {
            Pages.PS_FieldManagementEditPage.AddGroupingDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_FieldManagementEditPage.EditGroupImage.Wait.ForVisible();
            
            Pages.PS_FieldManagementEditPage.EditGroupImage.Click();
            Pages.PS_FieldManagementEditPage.EditGroupDialogDiv.Wait.ForVisible();
            Pages.PS_FieldManagementEditPage.EditGroupPopupSaveBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_036_CodedStep2()
        {
            string saveClassAttr = Pages.PS_FieldManagementEditPage.EditGroupPopupSaveBtn.BaseElement.GetAttribute("class").Value;
            string cancelClassAttr = Pages.PS_FieldManagementEditPage.EditGroupPopupCancelBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(saveClassAttr.Equals("btn"),"Save button should be primary");
            Assert.IsTrue(cancelClassAttr.Equals("btn-white"),"Cancel button should be secondary");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_036_CodedStep3()
        {
            
           
                Pages.PS_FieldManagementEditPage.EditGroupPopupCancelBtn.Click();
               
                OnBeforeUnloadDialog dialog = OnBeforeUnloadDialog.CreateOnBeforeUnloadDialog(ActiveBrowser, DialogButton.OK);
                Manager.DialogMonitor.AddDialog(dialog);
                
                Pages.PS_HomePage.HomeLeftNavLink.Click();
                if(!ActiveBrowser.BrowserType.ToString().Contains("Internet")){
                dialog.WaitUntilHandled(5000);
                ActiveBrowser.WaitUntilReady();    
                }
           
            
            
        }
    }
}
