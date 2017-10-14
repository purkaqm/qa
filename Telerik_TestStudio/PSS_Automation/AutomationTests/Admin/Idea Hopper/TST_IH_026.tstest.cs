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

    public class TST_IH_026 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_IH_026_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Addnew Button")]
        public void TST_IH_026_CS02()
        {
            Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_AddIdeaHopperPage.NameText.Wait.ForExists();
        }
    
        [CodedStep(@"Verify User is directed to Add Idea Hopper page")]
        public void TST_IH_026_CS03()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
                                                                                    
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_IH_026_CS04()
        {
            string ideaHopperName = "TestIdea" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_AddIdeaHopperPage.NameText,ideaHopperName);            
        }
    
        [CodedStep(@"Select Type")]
        public void TST_IH_026_CS05()
        {
            string ideaHopperName = "TestIdea" + Randomizers.generateRandomInt(1000,9999);
            Pages.PS_AddIdeaHopperPage.TypeSelect.SelectByValue("0");       
        }
    
        [CodedStep(@"Select Location")]
        public void TST_IH_026_CS06()
        {
            
            Pages.PS_AddIdeaHopperPage.LoctionTypeSelect.SelectByValue("1", true);
            ActiveBrowser.RefreshDomTree();
            
        }
    
        [CodedStep(@"Click on Owner Field")]
        public void TST_IH_026_CS07()
        {
            Pages.PS_AddIdeaHopperPage.ProjectLeadSpan.Click();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Ownner selection form should popup")]
        public void TST_IH_026_CS08()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupTitleSpan.Wait.ForExists();
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.Wait.ForExists();
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.IsVisible());
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.IsVisible());
        }
    
        [CodedStep(@"Enter the name of Owner")]
        public void TST_IH_026_CS09()
        {
            Actions.SetText(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText,CustomUtils.adminName1);
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_IH_026_CS10()
        {
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Click();
            Manager.ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify user should found and user name is listed as link")]
        public void TST_IH_026_CS11()
        {
            HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
            adminNameLocator.Wait.ForExists();
            Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
        }
    
        [CodedStep(@"Click on User Name link")]
        public void TST_IH_026_CS12()
        {
            HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
            adminNameLocator.Wait.ForExists();
            Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
            adminNameLocator.Click();
        }
    
        [CodedStep(@"Verify user is redirected to 'Admin: Add Idea Hopper' page")]
        public void TST_IH_026_CS13()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
        }
    
        [CodedStep(@"Verify selected user's name should be in the Owner field")]
        public void TST_IH_026_CS14()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadDiv.BaseElement.InnerText.Contains(CustomUtils.adminName1),"Selected user name should be present");
            
        }
    }
}
