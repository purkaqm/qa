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

    public class TST_IH_012 : BaseWebAiiTest
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
        public void TST_IH_012_CS01()
        {
            CustomUtils.locationValue = "Specific location...";
        }
    
        [CodedStep(@"Navigate to : '/project/IdeaHopper.page'")]
        public void TST_IH_012_CS02()
        {
            // Navigate to : '/project/IdeaHopper.page'
            ActiveBrowser.NavigateTo("/project/IdeaHopper.page", true);
            ActiveBrowser.WaitUntilReady();
                                    
        }
    
        [CodedStep(@"Verify 'Submit an Idea' page is displayed")]
        public void TST_IH_012_CS03()
        {
            Assert.IsTrue(Pages.SubmitAnIdeaPage.ChooseCategorySelect.IsVisible(),"choose a category dropdown select should be present");
            Assert.IsTrue(Pages.SubmitAnIdeaPage.OkButton.IsVisible(),"Ok button should be present");
        }
    
        [CodedStep(@"Select Idea Hopper and click Ok button")]
        public void TST_IH_012_CS04()
        {
            Pages.SubmitAnIdeaPage.ChooseCategorySelect.SelectByText(GetExtractedValue("IdeaHopperName").ToString());
            Pages.SubmitAnIdeaPage.OkButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user directed to New Idea page")]
        public void TST_IH_012_CS05()
        {
            HtmlSpan newIdeaNameSpan = ActiveBrowser.Find.ByXPath<HtmlSpan>(string.Format(AppLocators.get("submit_new_idea_page_span"),GetExtractedValue("IdeaHopperName").ToString()));
            newIdeaNameSpan.Wait.ForExists();
            Assert.IsTrue(newIdeaNameSpan.IsVisible());
        }
    
        [CodedStep(@"Verify Idea is submited")]
        public void TST_IH_012_CS06()
        {
            Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.DisclaimerTagDiv.IsVisible());
            Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.YouIdeaSubmitedDiv.IsVisible());
            Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.SubmitAnotherIdeaLink.IsVisible());
        }
    
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_012_CS07()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_012_CS08()
        {
            try
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
            
            catch(Exception ex)
            {
                Log.WriteLine("Delete the" + GetExtractedValue("IdeaHopperName").ToString() + " manualy " + ex.Message);
            }
        }
    }
}
