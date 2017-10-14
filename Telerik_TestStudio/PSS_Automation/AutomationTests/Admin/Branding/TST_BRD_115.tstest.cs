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

    public class TST_BRD_115 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on PowerPoint Branding tab")]
        public void TST_BRD_115_CodedStep()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify all elements of PowerPoint branding page ")]
        public void TST_BRD_115_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.PowerPointH2Tag.IsVisible(),"Title should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.DescriptionPTag.IsVisible(),"Description should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.AddNewTempBtn.IsVisible(),"Add new template button should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.PowerPointTable.IsVisible(),"Table should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.NameColumnHeader.IsVisible(),"Name column should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.DateColumnHeader.IsVisible(),"Last Changed column should be present");
            Assert.IsTrue(Pages.PS_BrandingPowerPointPage.ModifierColumnHeader.IsVisible(),"Changed By column should be present");           
        }
            
    }
}
