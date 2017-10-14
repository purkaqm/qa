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

    public class TST_IH_041 : BaseWebAiiTest
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
    
        [CodedStep(@"Set user for login")]
        public void TST_IH_041_CS01()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_041_CS02()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();
            Pages.PS_HomePage.EditPreferences.Wait.ForExists();
                        
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_041_CS03()
        {
            Pages.PS_HomePage.EditPreferences.Click();     
        }
    
        [CodedStep(@"Verify Prefernce page is open")]
        public void TST_IH_041_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Settings: Preferences"));
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.HighContViewCheckBox.IsVisible(),"'High Contrast view' checkbok should be present");
                        
        }
        
        [CodedStep(@"Select default location")]
        public void TST_IH_041_CS05()
        {       
                Pages.PS_SettingsPreferencesPage.DefaultLocWorkTreeDiv.Click();
                ActiveBrowser.WaitUntilReady();
                Pages.PS_SettingsPreferencesPage.PopUpSearchSpan.Wait.ForExists();
                Pages.PS_SettingsPreferencesPage.PopUpSearchSpan.Click();
                Pages.PS_SettingsPreferencesPage.PopUpFindTextInput.Wait.ForExists();
                Actions.SetText(Pages.PS_SettingsPreferencesPage.PopUpFindTextInput,CustomUtils.defaultLocationWorkTree);
                Pages.PS_SettingsPreferencesPage.PopUpGoButton.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("setting_default_location_div"),CustomUtils.defaultLocationWorkTree));
                Log.WriteLine(string.Format(AppLocators.get("setting_default_location_div"),CustomUtils.defaultLocationWorkTree));
                resDiv.Wait.ForVisible();
                Assert.IsTrue(resDiv.IsVisible());
                resDiv.ScrollToVisible();
                resDiv.MouseClick(MouseClickType.LeftClick);
            
        }
        
        [CodedStep(@"Click on save button")]
        public void TST_IH_041_CS06()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
        [CodedStep(@"Verify new setting is saved")]
        public void TST_IH_041_CS07()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.DefaultLocWorkTreeDiv.BaseElement.InnerText.Contains(CustomUtils.defaultLocationWorkTree));
                        
        }
    }
}
