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

    public class TST_IH_052 : BaseWebAiiTest
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
        public void TST_IH_052_CS01()
        {
                        CustomUtils.locationValue = "Custom...";
        }
    
        [CodedStep(@"Set user for login")]
        public void TST_IH_052_CS02()
        {
                        SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Click on 'Add' tab on left navigation bar")]
        public void TST_IH_052_CS03()
        {
                        Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Select Idea Hopper and click Ok button")]
        public void TST_IH_052_CS04()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.SubmitAnIdeaPage.ChooseCategorySelect.SelectByText(GetExtractedValue("IdeaHopperName").ToString());
                        Pages.SubmitAnIdeaPage.OkButton.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user directed to New Idea page")]
        public void TST_IH_052_CS05()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Submit New Idea"));
                        Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.IdeaNameText.IsVisible());   
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_052_CS06()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_052_CS07()
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
