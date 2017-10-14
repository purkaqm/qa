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

    public class TST_MAT_031_C992492 : BaseWebAiiTest
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
    
        [CodedStep(@"Click the admin icon on icon bar")]
        public void TST_MAT_031_C992492_ClickAdminIcon()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click(true);
        }
    
        [CodedStep(@"Click the exchange rates link")]
        public void TST_MAT_031_C992492_ClickExchangeRate()
        {
            Pages.PS_HomePage.ExchangeRatesDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify required field and elements are displayed")]
        public void TST_MAT_031_C992492_VerifyExchangeRatesElements()
        {
            Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ExchangeRateTable.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ToCurrencyVisibleSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.AddNewExchangeRateButton.IsVisible());
        }
    }
}
