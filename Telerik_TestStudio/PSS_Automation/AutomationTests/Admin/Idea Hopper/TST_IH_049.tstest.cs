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

    public class TST_IH_049 : BaseWebAiiTest
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
        public void TST_IH_049_CS01()
        {
                        CustomUtils.locationValue = "Submitter's default location";
        }
    
        [CodedStep(@"Set user for login")]
        public void TST_IH_049_CS02()
        {
                        SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_049_CS03()
        {
                        Pages.PS_HomePage.FirstNameLastNameDiv.Click();
                        Pages.PS_HomePage.EditPreferences.Wait.ForExists();
                                                            
        }
    
        [CodedStep(@"Click on User name on header bar")]
        public void TST_IH_049_CS04()
        {
                        Pages.PS_HomePage.EditPreferences.Click();     
        }
    
        [CodedStep(@"Verify Prefernce page is open")]
        public void TST_IH_049_CS05()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Settings: Preferences"));
                        Assert.IsTrue(Pages.PS_SettingsPreferencesPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
                        Assert.IsTrue(Pages.PS_SettingsPreferencesPage.HighContViewCheckBox.IsVisible(),"'High Contrast view' checkbok should be present");
                                                            
        }
    
        [CodedStep(@"Select default location")]
        public void TST_IH_049_CS06()
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
        public void TST_IH_049_CS07()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                                                            
        }
    
        [CodedStep(@"Verify new setting is saved")]
        public void TST_IH_049_CS08()
        {
                        ActiveBrowser.RefreshDomTree();
                        Assert.IsTrue(Pages.PS_SettingsPreferencesPage.DefaultLocWorkTreeDiv.BaseElement.InnerText.Contains(CustomUtils.defaultLocationWorkTree));
                                                            
        }
    
        [CodedStep(@"Click on 'Add' tab on left navigation bar")]
        public void TST_IH_049_CS09()
        {
                        Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Select Idea Hopper and click Ok button")]
        public void TST_IH_049_CS10()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.SubmitAnIdeaPage.ChooseCategorySelect.SelectByText(GetExtractedValue("IdeaHopperName").ToString());
                        Pages.SubmitAnIdeaPage.OkButton.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user directed to New Idea page")]
        public void TST_IH_049_CS11()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Submit New Idea"));
                        Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.IdeaNameText.IsVisible());
                                    
        }
    
        [CodedStep(@"Verify Idea is submited")]
        public void TST_IH_049_CS12()
        {
                        Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.DisclaimerTagDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.ViewInput.IsVisible());
                        Assert.IsTrue(Pages.PS_AddSubmitNewIdeaPage.SubmitAnotherInput.IsVisible());
                        
        }
        
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_IH_049_CS13()
        {
                                    Pages.PS_HomePage.SearchLink.Click();
                                    ActiveBrowser.RefreshDomTree();
                                    Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
                                    Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Set location")]
        public void TST_IH_049_CS14()
        {
                                    ActiveBrowser.RefreshDomTree();;
                                    ActiveBrowser.Window.SetFocus();
                                    Pages.PS_HomePage.SearchInputTextBackgroundSpan.MouseClick(MouseClickType.LeftClick);
                                    Manager.Desktop.KeyBoard.TypeText("Pipeline",2);
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                    System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on Location path")]
        public void TST_IH_049_CS15()
        {
                                    ActiveBrowser.RefreshDomTree();
                                    string loc = "Idea Pipeline";
                                    Pages.PS_HomePage.SearchResultTable.Wait.ForExists();
                                    Assert.IsTrue(Pages.PS_HomePage.SearchResultTable.IsVisible());
                                    HtmlListItem listLoc = ActiveBrowser.Find.ByXPath<HtmlListItem>(string.Format("//li[@title='{0}']",loc));
                                    listLoc.Wait.ForExists();
                                    //listLoc.MouseHover();
                                    listLoc.MouseClick(MouseClickType.LeftClick);
                                    ActiveBrowser.WaitUntilReady();
                                    System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Click on project tab on left navigation bar")]
        public void TST_IH_049_CS16()
        {
                        Pages.PS_HomePage.ProjectLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify submited idea name is present in form of link")]
        public void TST_IH_049_CS17()
        {
                        ActiveBrowser.RefreshDomTree();
                        Log.WriteLine(string.Format(AppLocators.get("submited_idea_link"),GetExtractedValue("GeneratedIdeaName").ToString()));
                        HtmlAnchor submitedIdeaName = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("submited_idea_link"),GetExtractedValue("GeneratedIdeaName").ToString()));
                        submitedIdeaName.Wait.ForExists();
                        submitedIdeaName.ScrollToVisible();
                        Assert.IsTrue(submitedIdeaName.IsVisible(),"Link should be present on summary page");
        }
    
        [CodedStep(@"Click on Created idea link")]
        public void TST_IH_049_CS18()
        {
                        ActiveBrowser.RefreshDomTree();
                        HtmlAnchor ideaLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("project_summary_page_idea_link"),GetExtractedValue("GeneratedIdeaName")));
                        ideaLink.Wait.ForExists();
                        ideaLink.ScrollToVisible();
                        ideaLink.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_049_CS19()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_049_CS20()
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
