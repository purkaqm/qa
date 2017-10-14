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

    public class TST_MAT_033_C992497 : BaseWebAiiTest
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
        
        string tableName, currency, actualDate; string[] actualRate;
        // Add your test methods here...
    
        [CodedStep(@"Click the admin icon on icon bar")]
        public void TST_MAT_033_C992497_ClickAdminIcon()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click(true);
        }
    
        [CodedStep(@"Click the exchange rates link")]
        public void TST_MAT_033_C992497_ClickExchangeRate()
        {
            Pages.PS_HomePage.ExchangeRatesDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify required field and elements are displayed")]
        public void TST_MAT_033_C992497_VerifyExchangeRatesElements()
        {
            Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ExchangeRateTable.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ToCurrencyVisibleSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.AddNewExchangeRateButton.IsVisible());
        }
    
        [CodedStep(@"Click on AddTable Button")]
        public void TST_MAT_033_C992497_ClickAddTableButton()
        {
            Pages.PS_ExchangeRatesPage.AddTableButton.Click();
                        
        }
    
        [CodedStep(@"Fill the detailes and save")]
        public void TST_MAT_033_C992497_FillDetailsAndSave()
        {
            tableName = "PM_ERTable" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_ExchangeRatesPage.AddNameERTableText,tableName);
            
            Pages.PS_ExchangeRatesPage.SaveButtonAddERTablePopup.Click();
            ActiveBrowser.WaitUntilReady();
                        
                        
        }
    
        [CodedStep(@"Click on 'Add Exchange Rate' button")]
        public void TST_MAT_033_C992497_ClickAddERButton()
        {
            Pages.PS_ExchangeRatesPage.AddNewExchangeRateButton.Click();
        }
        
        [CodedStep(@"Fill the details and Click 'Add Exchange Rate' button")]
        public void TST_MAT_033_C992497_FillDetailsAndAddER()
        {
            currency = "Argentina, Pesos (ARS)";
            string xDate = Pages.PS_ExchangeRatesPage.StartPeriodInputText.BaseElement.GetAttribute("value").Value;
            DateTime dt = Convert.ToDateTime(xDate);
            actualDate = dt.ToString("M/dd/yy");
            Log.WriteLine("date = "+ actualDate);
            string rate = Pages.PS_ExchangeRatesPage.RateText.BaseElement.GetAttribute("value").Value.ToString();
            actualRate = rate.Split('.');
            Pages.PS_ExchangeRatesPage.CurrencySelect.Wait.ForExists();
            Pages.PS_ExchangeRatesPage.CurrencySelect.SelectByText(currency);
            Pages.PS_ExchangeRatesPage.AddNewExchangeRatePopupsubmit.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify exchange rate is successfully added in table")]
        public void TST_MAT_033_C992497_VerifyExchangeRate()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.Wait.ForExists();
            string addERMessageBoxText = string.Format("Exchange rate {0} has been added with date, {1} and rate, {2}",currency,actualDate,actualRate[0]);
            Log.WriteLine(addERMessageBoxText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.IsVisible());
            Log.WriteLine(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText.Contains(addERMessageBoxText));
        }
    
        [CodedStep(@"Delete the added exhange rate ")]
        public void TST_MAT_033_C992497_DeleteER()
        {
            HtmlInputCheckBox delChkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(normalize-space(.),'{0}')]/td[@class='deleteColumnValue last']/input",currency));
            delChkbox.Check(true);
            Pages.PS_ExchangeRatesPage.DeleteExchangeRateButton.Click();
            Pages.PS_CustomFieldsPage.DeleteYesBtn.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    }
}
