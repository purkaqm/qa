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

    public class TST_NAV_UI_010 : BaseWebAiiTest
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
        public void TST_NAV_UI_011_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.HelpIconSpan.IsVisible(),"help icon should be present");
             
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_010_CodedStep()
        {
            ActiveBrowser.Window.SetFocus();
            Pages.PS_HomePage.HelpIconSpan.MouseClick(MouseClickType.LeftClick);
            Assert.IsTrue(Pages.PS_HomePage.HelpIconTable.IsVisible());
            
            
            ActiveBrowser.WaitUntilReady();
            
            string[] arr = {"View Online Help","View All Online Documentation","Submit Help Desk Request","PowerSteering Community","PowerSteering Version","RESTful API Help","Replaced Terms","Manage..."};
            int len = arr.Count();
            
            for(int i=0;i<len;i++)
            {
                HtmlTableCell elementLocator = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format("//tbody[@class='dijitReset']//td[contains(.,'{0}')]",arr[i])); 
                Assert.IsTrue(elementLocator.IsVisible(),"element should be visible ");
            }
            
        }
    }
}
