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

    public class TST_MAT_014_C992595 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify user is navigated to My Inbox page")]
        public void TST_MAT_014_C992595_InboxPage()
        {
            Pages.PS_HomePage.PageContentDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Inbox"));
                        
        }
    
        [CodedStep(@"Verify Inbox sub menu labels are preseent in left panel")]
        public void TST_MAT_014_C992595_VerifySubMenuLabels()
        {
            Assert.IsTrue(Pages.PS_HomePage.InboxAlertsDiv.IsVisible(),"Alerts tab in left menu should be present");
            Assert.IsTrue(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.IsVisible(),"Questions/Notifications tab in left menu should be present");
            Assert.IsTrue(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.IsVisible(),"Status Reports Due tab in left menu should be present");
                        
        }
    
        [CodedStep(@"Verify tabs are present in center similar to left panel sub menus")]
        public void TST_MAT_014_C992595_VerifyTabs()
        {
            Assert.IsTrue(Pages.PS_MyInboxPage.AlertsTabLink.IsVisible(),"Alerts tab in center navigation should be present");
            Assert.IsTrue(Pages.PS_MyInboxPage.QuestionsNotifTabLink.IsVisible(),"Questions/Notifications tab in center navigation should be present");
            Assert.IsTrue(Pages.PS_MyInboxPage.StatusReportDueTabLink.IsVisible(),"Status Reports Due tab in center navigation should be present");
                        
        }
    }
}
