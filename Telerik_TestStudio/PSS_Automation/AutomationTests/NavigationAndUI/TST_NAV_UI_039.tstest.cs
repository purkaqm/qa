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

    public class TST_NAV_UI_039 : BaseWebAiiTest
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
      
        [CodedStep(@"Verify that Save button is primary and Cancel button is secondary")]
        public void TST_NAV_UI_039_CodedStep1()
        {
            string updateClassAttr = Pages.PS_EditObjectTypePage.EditObjTypesUpdateBtn.BaseElement.GetAttribute("class").Value;
            string copyClassAttr = Pages.PS_EditObjectTypePage.EditObjTypesCopyBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(updateClassAttr.Equals("btn"),"Update button should be primary");
            Assert.IsTrue(copyClassAttr.Equals("btn-white"),"Copy button should be secondary");
        }
    
        [CodedStep(@"Open any Project/Work type link")]
        public void TST_NAV_UI_039_CodedStep()
        {
            HtmlAnchor projWorkTypeLink = ActiveBrowser.Find.ById<HtmlAnchor>("editWorkType");
            projWorkTypeLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_EditObjectTypePage.EditObjTypesConfigurationTabLink.Wait.ForVisible();
            Pages.PS_EditObjectTypePage.EditObjTypesRolesTabLink.Wait.ForVisible();
            Pages.PS_EditObjectTypePage.EditObjTypesRolesTabLink.Wait.ForVisible();
        }
    }
}
