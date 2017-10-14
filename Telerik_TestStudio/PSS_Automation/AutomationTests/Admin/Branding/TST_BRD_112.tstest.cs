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

    public class TST_BRD_112 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Custom Messages tab")]
        public void TST_BRD_112_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.CustomMessagesLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Report Footer Text link")]
        public void TST_BRD_112_CodedStep1()
        {
            Pages.PS_BrandingCustomMessages.ReportFooterTxtLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.ReportFooterTxtLink.Click();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify section is expand")]
        public void TST_BRD_112_CodedStep2()
        {
            Pages.PS_BrandingCustomMessages.ReportFooterBlockDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_BrandingCustomMessages.ReportFooterBlockDiv.IsVisible(),"Report Footer Text section should be expanded"); 
        }
    
        [CodedStep(@"Verify section is collapse back")]
        public void TST_BRD_112_CodedStep3()
        {
            Assert.IsFalse(Pages.PS_BrandingCustomMessages.SelfRegistrationBlockDiv.IsVisible(),"Report Footer Text should be collapse back"); 
        }
    }
}
