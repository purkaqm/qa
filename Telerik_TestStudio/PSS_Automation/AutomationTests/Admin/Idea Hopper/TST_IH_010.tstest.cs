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

    public class TST_IH_010 : BaseWebAiiTest
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
        
        string ideaHopperName;
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_010_CS01()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Addnew Button")]
        public void TST_IH_010_CS02()
        {
                        Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_AddIdeaHopperPage.NameText.Wait.ForExists();
        }
    
        [CodedStep(@"Verify User is directed to Add Idea Hopper page")]
        public void TST_IH_010_CS03()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
                                                                                                            
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_IH_010_CS04()
        {
                        ideaHopperName = "TestIdea" + Randomizers.generateRandomInt(1000,9999);
                        Actions.SetText(Pages.PS_AddIdeaHopperPage.NameText,ideaHopperName);            
        }
    
        [CodedStep(@"Select Type")]
        public void TST_IH_010_CS05()
        {
                        Pages.PS_AddIdeaHopperPage.TypeSelect.SelectByValue("0");       
        }
    
        [CodedStep(@"Select Location")]
        public void TST_IH_010_CS06()
        {
                                    
                        Pages.PS_AddIdeaHopperPage.LoctionTypeSelect.SelectByText("Specific location...");  
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify after choosing 'Specific location' the field specified for location is displayed")]
        public void TST_IH_010_CS07()
        {
                                    
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.SpecificLocSpan.IsVisible(),"Additional location field should be displayed");       
        }
    
        [CodedStep(@"Click on empty Location field and Set the Location")]
        public void TST_IH_010_CS08()
        {
                        Pages.PS_AddIdeaHopperPage.SpecificLocSpan.Click();
                        Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        System.Threading.Thread.Sleep(5000);
                        Pages.PS_AddIdeaHopperPage.LocationSearchTab.Wait.ForExists();               
                        Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
                        if(Data["LocationType"].ToString().Contains("Search"))
                        {
                            Pages.PS_AddIdeaHopperPage.LocationSearchTab.Click();
                            Pages.PS_AddIdeaHopperPage.LocationPopUpFindText.Wait.ForVisible();
                            Actions.SetText(Pages.PS_AddIdeaHopperPage.LocationPopUpFindText,Data["SemiLocationPath"].ToString());
                            Pages.PS_AddIdeaHopperPage.LocationPopUpGoButton.Click();
                            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                            System.Threading.Thread.Sleep(5000);
                            ActiveBrowser.RefreshDomTree();                
                            HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("create_idea_location_search_result_div"),Data["LocationPath"].ToString()));
                            Log.WriteLine(string.Format(AppLocators.get("create_idea_location_search_result_div"),Data["LocationPath"].ToString()));
                            resDiv.Wait.ForVisible();
                            Assert.IsTrue(resDiv.IsVisible());
                            resDiv.ScrollToVisible();
                            resDiv.MouseClick(MouseClickType.LeftClick);
                            ActiveBrowser.RefreshDomTree();                       
                        }
        }
    
        [CodedStep(@"Click on Owner Field")]
        public void TST_IH_010_CS09()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadSpan.Click();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Ownner selection form should popup")]
        public void TST_IH_010_CS10()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupTitleSpan.Wait.ForExists();
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.Wait.ForExists();
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.IsVisible());
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.IsVisible());
        }
    
        [CodedStep(@"Enter the name of Owner")]
        public void TST_IH_010_CS11()
        {
                        Actions.SetText(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText,CustomUtils.adminName1);
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_IH_010_CS12()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Click();
                        Manager.ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        System.Threading.Thread.Sleep(5000);
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify user should found and user name is listed as link")]
        public void TST_IH_010_CS13()
        {
                        HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
                        adminNameLocator.Wait.ForExists();
                        Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
                        adminNameLocator.Click();
        }
    
        [CodedStep(@"Select 'Automatically request approval upon Idea submission' checkbox and press Add")]
        public void TST_IH_010_CS14()
        {
                        Pages.PS_AddIdeaHopperPage.BehaviorCheckBox.Click();
                        Pages.PS_AddIdeaHopperPage.AddButton.Click();
                        ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is redirected to 'Admin: Idea Hopper Configuration' page")]
        public void TST_IH_010_CS15()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Idea Hopper Configuration");
                        Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.IsVisible(),"Add button should be displayed"); 
                        ActiveBrowser.RefreshDomTree();
                        
        }
    
        [CodedStep(@"Verify that created Idea Hopper name is present in the list")]
        public void TST_IH_010_CS16()
        {
                        string ideaHopperNameLocStr = string.Format(AppLocators.get("idea_hopper_name_link_row"),ideaHopperName);
                        Log.WriteLine(ideaHopperNameLocStr);
                        HtmlTableRow ideaNameRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(ideaHopperNameLocStr);
                        ideaNameRow.Wait.ForExists();
                        Assert.IsTrue(ideaNameRow.IsVisible());
        }
    
        [CodedStep(@"Navigate to : '/project/IdeaHopper.page'")]
        public void TST_IH_010_CS17()
        {
            // Navigate to : '/project/IdeaHopper.page'
            ActiveBrowser.NavigateTo("/project/IdeaHopper.page", true);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify 'Submit an Idea' page is displayed")]
        public void TST_IH_010_CS18()
        {
            //Assert.IsTrue(Pages.SubmitAnIdeaPage.ManageIdeaHoppersLink.IsVisible(),"Manage idea hopper link should be present");
            Assert.IsTrue(Pages.SubmitAnIdeaPage.ChooseCategorySelect.IsVisible(),"choose a category dropdown select should be present");
            Assert.IsTrue(Pages.SubmitAnIdeaPage.OkButton.IsVisible(),"Ok button should be present");
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_010_CS19()
        {
                        
            Log.WriteLine(string.Format(AppLocators.get("idea_hopper_del_checkbox"),ideaHopperName));
            HtmlInputCheckBox ideaDeleteCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("idea_hopper_del_checkbox"),ideaHopperName));
            ideaDeleteCheckbox.Wait.ForExists();
            ideaDeleteCheckbox.Click();
            Pages.PS_IdeaHopperConfigurationPage.DeleteBtn.Click();
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Wait.ForExists();
            Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
