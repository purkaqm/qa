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

    public class TST_UCO_008 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Admin left navigation tab is present")]
        public void TST_UCO_008_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_008_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that in left navigation title 'admin' is displayed")]
        public void TST_UCO_008_CS03()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.BaseElement.InnerText.Contains("ADMIN"));
        }
    
        [CodedStep(@"Select 'Admin-Other' category")]
        public void TST_UCO_008_CS04()
        {
        
            Pages.PS_LogsPage.ViewDropDownList.SelectByText("Admin - Other");
        }
        
        [CodedStep(@"Set days on calendar when there were no events")]
        public void TST_UCO_008_CS05()
        {
            string startDate = Data["StartDate"].ToString();
            string endDate = Data["EndDate"].ToString();
            Pages.PS_LogsPage.AdminLogsFromDate.SetValue("value","");
            Pages.PS_LogsPage.AdminLogsToDate.SetValue("value","");
            Pages.PS_LogsPage.AdminLogsFromDate.MouseClick(MouseClickType.LeftClick);
            Manager.Desktop.KeyBoard.TypeText(startDate,01);
            Pages.PS_LogsPage.AdminLogsToDate.MouseClick(MouseClickType.LeftClick);
            Manager.Desktop.KeyBoard.TypeText(endDate,01);
        }
        
        [CodedStep(@"Click on Go button")]
        public void TST_UCO_008_CS06()
        {
        
            Pages.PS_LogsPage.GoBtn.Click();
        }
        
        [CodedStep(@"'No history to show' message is displayed")]
        public void TST_UCO_008_CS07()
        {
            string noHistoryLocator = Pages.PS_LogsPage.NoHistoryToShowDiv.TextContent;
            Assert.IsTrue(noHistoryLocator.Contains("No history to show"));
        }
    }
}