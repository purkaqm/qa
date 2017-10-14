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

    public class TST_NAV_UI_038 : BaseWebAiiTest
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
        public void TST_NAV_UI_038_CodedStep()
        {
            Pages.PS_ReviewMeasureLibrary.MassAttachLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerMarkup,"Measures: Mass Attach");
            Pages.PS_MeasuresMassAttachPage.PreviewBtn.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_038_CodedStep1()
        {
            string attachClassAttr = Pages.PS_MeasuresMassAttachPage.AttachBtn.BaseElement.GetAttribute("class").Value;
            string previewClassAttr = Pages.PS_MeasuresMassAttachPage.PreviewBtn.BaseElement.GetAttribute("class").Value;
            
            Assert.IsTrue(attachClassAttr.Equals("btn"),"Attach button should be primary");
            Assert.IsTrue(previewClassAttr.Equals("btn-white"),"Preview button should be secondary");
        }
    }
}
