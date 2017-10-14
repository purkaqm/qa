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

    public class TST_FAV_009 : BaseWebAiiTest
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
        public void TST_FAV_009_CodedStep()
        {
            string totalMenubarBeforeDrag = Pages.PS_HomePage.NavPanelMenuBarDiv.GetComputedStyleValue("height").Split('p')[0];
            string upperMenubarBeforeDrag = Pages.PS_HomePage.MenuContentWrapper1Div.GetComputedStyleValue("height").Split('p')[0];
            string lowerMenubarBeforeDrag = Pages.PS_HomePage.MenuContentWrapper2Div.GetComputedStyleValue("height").Split('p')[0];
            
            int totalHeight = Int32.Parse(totalMenubarBeforeDrag);
            int upperHeight = Int32.Parse(upperMenubarBeforeDrag);
            int lowerHeight = Int32.Parse(lowerMenubarBeforeDrag);
            int half = totalHeight/2;
            
            Assert.IsTrue((upperHeight > half) && (lowerHeight < half), "By default Upper menu bar height should be greater than lower pinned area");
            
            HtmlDiv draggableElement = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@class='vresizer']");
            draggableElement.DragTo(0,-(totalHeight/2 + 100));
            ActiveBrowser.RefreshDomTree();
            
            string upperMenubarAfterDrag = Pages.PS_HomePage.MenuContentWrapper1Div.GetComputedStyleValue("height").Split('p')[0];
            string lowerMenubarAfterDrag = Pages.PS_HomePage.MenuContentWrapper2Div.GetComputedStyleValue("height").Split('p')[0];
            
            int upperHeightLater = Int32.Parse(upperMenubarAfterDrag);
            int lowerHeightLater = Int32.Parse(lowerMenubarAfterDrag);
            int diff = upperHeightLater - lowerHeightLater;
            
            Assert.IsTrue((diff > -50) && (diff < 50), "pinned area should be resized at max half of the left navigation bar");
            
        }
    }
}
