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

    public class TST_NAV_UI_044 : BaseWebAiiTest
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
        public void TST_NAV_UI_044_CodedStep()
        {
            string addNewClassAttr = Pages.PS_ManageImportantLinksPage.AddNewBtn.BaseElement.GetAttribute("class").Value;
            string deleteClassAttr = Pages.PS_ManageImportantLinksPage.DeleteBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(addNewClassAttr.Equals("btn-white"),"Add New button should be primary");
            Assert.IsTrue(deleteClassAttr.Equals("btn-white"),"Delete button should be secondary");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_044_CodedStep1()
        {
            Pages.PS_ManageImportantLinksPage.UpdateLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ManageImportantLinksUpdatePage.UpdateBtn.Wait.ForVisible();
            Pages.PS_ManageImportantLinksUpdatePage.CancelBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"Verify that Add New button is primary and Delete button is secondary")]
        public void TST_NAV_UI_044_CodedStep2()
        {
            string upadateClassAttr = Pages.PS_ManageImportantLinksUpdatePage.UpdateBtn.BaseElement.GetAttribute("class").Value;
            string cancelClassAttr = Pages.PS_ManageImportantLinksUpdatePage.CancelBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(upadateClassAttr.Equals("btn-white"),"Update button should be primary");
            Assert.IsTrue(cancelClassAttr.Equals("btn-white"),"Cancel button should be secondary");
        }
    }
}
