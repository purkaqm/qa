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

    public class TST_MUT_025 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Project icon on left navigation menu")]
        public void TST_MUT_025_ProjectLink()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            Pages.PS_HomePage.ProjectWorkTreeTab.Wait.ForExists();
        }
        
        [CodedStep(@"Wait for Tree structure to be displayed")]
        public void TST_MUT_025_WaitTreeStructure()
        {
            /// \bug Move to a central place
            HtmlDiv loadingDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@id='ps_ui_Tree_0']/div/div[1]");
            loadingDiv.Wait.ForExists();
            loadingDiv.Wait.ForVisibleNot();
            System.Threading.Thread.Sleep(2000);
        }
        
        [CodedStep(@"Verify Expand/Collapse work properly")]
        public void TST_MUT_025_VerifyExpand()
        {   
            ActiveBrowser.RefreshDomTree();
            /// \bug Move to a central place
            IList<HtmlSpan> plusIconList = ActiveBrowser.Find.AllByXPath<HtmlSpan>("//span[contains(@class,'dijitTreeExpandoClosed')]");
            Log.WriteLine(plusIconList.Count.ToString());
            plusIconList[0].Wait.ForExists();
            plusIconList[0].Click();
            System.Threading.Thread.Sleep(2000);
            ActiveBrowser.RefreshDomTree();
            /// \bug Move to a central place
            IList<HtmlSpan> minusIconList = ActiveBrowser.Find.AllByXPath<HtmlSpan>("//span[contains(@class,'dijitTreeExpandoOpened')]");
            minusIconList[0].Wait.ForExists();
            Assert.IsTrue(minusIconList[0].IsVisible());
            minusIconList[0].Click();
            
        }
        
        [CodedStep(@"Verify summary page is opened")]
        public void TST_MUT_025_VerifySummaryPage()
        {
            ActiveBrowser.RefreshDomTree();
            string firstLinkValue;
            /// \bug Move to a central place
            IList<HtmlAnchor> firstLink = ActiveBrowser.Find.AllByXPath<HtmlAnchor>("//span[contains(@class,'dijitTreeExpandoClosed')]/following-sibling::a");
            firstLink[0].Wait.ForExists();
            firstLinkValue = firstLink[0].BaseElement.InnerText;
            
            
            Log.WriteLine("one=" + firstLinkValue);
            firstLink[0].Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains(firstLinkValue));
        }
    
       
    }
}
