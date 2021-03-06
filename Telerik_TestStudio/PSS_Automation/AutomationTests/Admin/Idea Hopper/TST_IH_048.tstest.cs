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

    public class TST_IH_048 : BaseWebAiiTest
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
    
        [CodedStep(@"Set location value")]
        public void TST_IH_048_CS01()
        {
            CustomUtils.locationValue = "Submitter's default location";
        }
        
        [CodedStep(@"Set user for login")]
        public void TST_IH_048_CS02()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_048_CS03()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();
            Pages.PS_HomePage.EditPreferences.Wait.ForExists();
                                                
        }
    
        [CodedStep(@"Click Edit Preference link")]
        public void TST_IH_048_CS04()
        {
            Pages.PS_HomePage.EditPreferences.Click();     
        }
    
        [CodedStep(@"Verify Prefernce page is open")]
        public void TST_IH_048_CS05()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Settings: Preferences"));
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.HighContViewCheckBox.IsVisible(),"'High Contrast view' checkbok should be present");
                                                
        }
    
        [CodedStep(@"Set the empty value for the default location")]
        public void TST_IH_048_CS06()
        {
            Pages.PS_SettingsPreferencesPage.DefaultLocWorkTreeDiv.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_SettingsPreferencesPage.PopUpSearchSpan.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.PopUpClearLink.Wait.ForExists();
            Pages.PS_SettingsPreferencesPage.PopUpClearLink.Click();
                        
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_IH_048_CS07()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
                        
        }
    
        [CodedStep(@"Verify new setting is saved")]
        public void TST_IH_048_CS08()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_SettingsPreferencesPage.DefaultLocWorkTreeDiv.BaseElement.InnerText.Contains(""));
        }
    
        [CodedStep(@"Click on 'Add' tab on left navigation bar")]
        public void TST_IH_048_CS09()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Verify created idea not be able to select")]
        public void TST_IH_048_CS10()
        {
            ActiveBrowser.RefreshDomTree();
            //Pages.SubmitAnIdeaPage.ChooseCategorySelect.SelectByText(GetExtractedValue("IdeaHopperName").ToString());
            Log.WriteLine(Pages.SubmitAnIdeaPage.ChooseCategorySelect.BaseElement.InnerText);
            Assert.IsFalse(Pages.SubmitAnIdeaPage.ChooseCategorySelect.BaseElement.InnerText.Contains(GetExtractedValue("IdeaHopperName").ToString()));
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_048_CS11()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_048_CS12()
        {
                                                                                    
            Log.WriteLine(string.Format(AppLocators.get("idea_hopper_del_checkbox"),GetExtractedValue("IdeaHopperName").ToString()));
            HtmlInputCheckBox ideaDeleteCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("idea_hopper_del_checkbox"),GetExtractedValue("IdeaHopperName").ToString()));
            ideaDeleteCheckbox.Wait.ForExists();
            ideaDeleteCheckbox.Click();
            Pages.PS_IdeaHopperConfigurationPage.DeleteBtn.Click();
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Wait.ForExists();
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
