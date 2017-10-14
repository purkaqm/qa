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

    public class TST_BRD_125 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on PowerPoint branding tab")]
        public void TST_BRD_125_01()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Mouse hover to table row")]
        public void TST_BRD_125_02()
        {
            HtmlTableRow firstRowLoc = ActiveBrowser.Find.ByXPath<HtmlTableRow>(AppLocators.get("branding_powerpoint_table_first_row"));
            firstRowLoc.Wait.ForExists();
            string prevBackgdColor = firstRowLoc.GetComputedStyle("background-color").Value.ToString();
            SetExtractedValue("PrevBackgdColor",prevBackgdColor);
            firstRowLoc.MouseHover();
            ActiveBrowser.RefreshDomTree();
            string changedBckgdColor = firstRowLoc.GetComputedStyle("background-color").Value.ToString();
            SetExtractedValue("ChangedBckgdColor",changedBckgdColor);
        }
    
        
    
        [CodedStep(@"Verify focus string is hihglighted")]
        public void TST_BRD_125_03()
        {
            Assert.IsFalse(GetExtractedValue("PrevBackgdColor").ToString().Equals(GetExtractedValue("ChangedBckgdColor")),"color should be different");
        }
    }
}
