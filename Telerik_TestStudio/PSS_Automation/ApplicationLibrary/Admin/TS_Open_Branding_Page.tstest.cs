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

    public class TS_Open_Branding_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Expand Configuration tab")]
        public void TS_Open_Branding_Page_CodedStep()
        {
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("configuration_leftnav_plus_span")).Count > 0){
                HtmlSpan plusConfiguration = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("configuration_leftnav_plus_span"));
                plusConfiguration.Click();
            }
            Pages.PS_HomePage.AdminBrandingTabDiv.Wait.ForExists();           
        }
    
        [CodedStep(@"Click Status Report Templates tab")]
        public void TS_Open_Branding_Page_CodedStep1()
        {
            Pages.PS_HomePage.AdminBrandingTabDiv.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForExists(); 
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, "Look & Feel", ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
            Pages.PS_BrandingLookAndFeelPage.ContentBGInputText.Wait.ForVisible();
        }
    }
}
