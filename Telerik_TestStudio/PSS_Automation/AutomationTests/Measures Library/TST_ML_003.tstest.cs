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

    public class TST_ML_003 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Review left navigation tab is present")]
        public void TST_ML_003_CS01()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Review left navigation link")]
        public void TST_ML_003_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Add New button")]
        public void TST_ML_003_CS03()
        {
            Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.Click(true);
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify 'Metric Template' page is opened")]
        public void TST_ML_003_CS04()
        {
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.NameTextInput.IsVisible());
            Assert.IsTrue(Pages.PS_MeasureTemplatesNewPage.SubmitButton.IsVisible());
        }
        
        [CodedStep(@"Verify Measure library template  is created")]
        public void TST_ML_003_CS05()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),GetExtractedValue("MeasureName"))).Count>0);
    
        }
        
        [CodedStep(@"Verify Measures Library page is opened")]
        public void TST_ML_003_CS06()
        {
            Assert.IsTrue( Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Measures Library"));
        }
    
        [CodedStep(@"Delete generated Measure library")]
        public void TST_ML_003_CS07()
        {
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),GetExtractedValue("MeasureName")));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),GetExtractedValue("MeasureName")));
            measureNameTraingleImg.MouseClick();
            Pages.PS_ReviewMeasureLibrary.DeleteTableCell.Click();
            Pages.PS_ReviewMeasureLibrary.DeletePopupDeleteAllRadio.Click();
            Pages.PS_ReviewMeasureLibrary.DeletePopupCommitButton.Click();  
            ActiveBrowser.WaitUntilReady();
        }
    
    }
}
