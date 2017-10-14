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

    public class TST_ML_001 : BaseWebAiiTest
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
        public void TST_ML_001_CS01()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Review left navigation link")]
        public void TST_ML_001_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify required elements are displayed")]
        public void TST_ML_001_CS03()
        {
            Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.InUseColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.OwnerColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.LastEditedColLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.OnlineColLink.Wait.ForExists();
            //Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.MassAttachLink.Wait.ForExists();
            Pages.PS_ReviewMeasureLibrary.MassEvaluateLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.TitleColumnLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.DescriptionColTableHeader.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.InUseColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.OwnerColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.LastEditedColLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.OnlineColLink.IsVisible());
            //Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MassAttachLink.IsVisible());
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MassEvaluateLink.IsVisible());
        }
    
    }
}
