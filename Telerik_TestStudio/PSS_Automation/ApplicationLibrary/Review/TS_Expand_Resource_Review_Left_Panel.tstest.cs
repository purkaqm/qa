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

    public class TS_Expand_Resource_Review_Left_Panel : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Plus icon")]
        public void TS_ClickPlusIcon()
        {
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("resource_review_leftnav_plus_span")).Count > 0){
                HtmlSpan plusIcon = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("resource_review_leftnav_plus_span"));
                plusIcon.Click();
                ActiveBrowser.RefreshDomTree();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                Pages.PS_HomePage.DefaultResourceReviewPage.Wait.ForExists();
                
            }
        }
    
    }
}
