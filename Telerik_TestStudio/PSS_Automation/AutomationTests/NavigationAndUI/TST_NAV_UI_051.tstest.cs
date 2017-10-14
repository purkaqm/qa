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

    public class TST_NAV_UI_051 : BaseWebAiiTest
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
        public void TST_NAV_UI_051_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.HelpIconSpan.MouseClick(MouseClickType.LeftClick);
            Assert.IsTrue(Pages.PS_HomePage.HelpManageDiv.IsVisible(),"Manage link should be present in help menu");
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_051_CodedStep1()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_051_CodedStep1()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Set user for login")]
        public void TST_NAV_UI_051_CodedStep2()
        {
            SetExtractedValue("UserSuffix","2");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_051_CodedStep3()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.HelpIconSpan.MouseClick(MouseClickType.LeftClick);
            Assert.IsFalse(ActiveBrowser.Find.AllByXPath(AppLocators.get("Help_Manage_Link_Div")).Count>0);
            
        }
    }
}
