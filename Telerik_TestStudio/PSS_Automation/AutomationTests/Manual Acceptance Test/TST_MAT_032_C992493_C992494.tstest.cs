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

    public class TST_MAT_032_C992493 : BaseWebAiiTest
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
        
        string tableName;
        
        // Add your test methods here...
    
        [CodedStep(@"Click the admin icon on icon bar")]
        public void TST_MAT_032_C992493_ClickAdminIcon()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click(true);
        }
    
        [CodedStep(@"Click the exchange rates link")]
        public void TST_MAT_032_C992493_ClickExchangeRate()
        {
            Pages.PS_HomePage.ExchangeRatesDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify required field and elements are displayed")]
        public void TST_MAT_032_C992493_VerifyExchangeRatesElements()
        {
            Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.DefaultCurrencyPropertySelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ExchangeRateTable.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.ToCurrencyVisibleSelect.IsVisible());
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.AddNewExchangeRateButton.IsVisible());
        }
    
        [CodedStep(@"Click on AddTable Button")]
        public void TST_MAT_032_C992493_ClickAddTableButton()
        {
            Pages.PS_ExchangeRatesPage.AddTableButton.Click();
            
        }
    
        [CodedStep(@"Fill the detailes and save")]
        public void TST_MAT_032_C992493_FillDetailsAndSave()
        {
            tableName = "PM_ERTable" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_ExchangeRatesPage.AddNameERTableText,tableName);
            
            Pages.PS_ExchangeRatesPage.SaveButtonAddERTablePopup.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Verify Table is added")]
        public void TST_MAT_032_C992493_VerifyTable()
        {
            string addMessageBoxText = string.Format("New Exchange Rate Table \"{0}\" was added",tableName);
            Log.WriteLine(addMessageBoxText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.IsVisible());
            Log.WriteLine(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText.Contains(addMessageBoxText));
        }
    
        [CodedStep(@"Delete the ER table")]
        public void TST_MAT_032_C992493_DeleteERTable()
        {
            Pages.PS_ExchangeRatesPage.DeleteTableButton.Click();
            Pages.PS_ExchangeRatesPage.DeleteTableSubmit.Wait.ForExists();
            Pages.PS_ExchangeRatesPage.DeleteTableSubmit.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify delete messsage is displayed")]
        public void TST_MAT_032_C992493_VerifyDeleteMessage()
        {
            string delMessageBoxText = string.Format("Exchange Rate Table \"{0}\" was deleted",tableName);
            Log.WriteLine(delMessageBoxText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.IsVisible());
            Log.WriteLine(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText);
            Assert.IsTrue(Pages.PS_ExchangeRatesPage.MessageDivNewERTableAdded.BaseElement.InnerText.Contains(delMessageBoxText));
        }
    }
}
