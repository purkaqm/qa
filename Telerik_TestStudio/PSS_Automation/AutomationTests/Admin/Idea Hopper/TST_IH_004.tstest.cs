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

    public class TST_IH_004 : BaseWebAiiTest
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
        public void TST_IH_004_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Addnew Button")]
        public void TST_IH_004_CS02()
        {
            Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_AddIdeaHopperPage.NameText.Wait.ForExists();
        }
    
        [CodedStep(@"Verify User is directed to Add Idea Hopper page")]
        public void TST_IH_004_CS03()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
                                    
        }
        
        [CodedStep(@"Click on empty Location field")]
        public void TST_IH_004_CS04()
        {    
            Pages.PS_AddIdeaHopperPage.SpecificLocSpan.Click();
            Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            Pages.PS_AddIdeaHopperPage.LocationSearchTab.Wait.ForExists();               
            /*
            Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
            if(Data["LocationType"].ToString().Contains("Search")){
                Pages.PS_AddIdeaHopperPage.LocationSearchTab.Click();
                Pages.PS_AddIdeaHopperPage.LocationPopUpFindText.Wait.ForVisible();
                Actions.SetText(Pages.PS_AddIdeaHopperPage.LocationPopUpFindText,Data["SemiLocationPath"].ToString());
                Pages.PS_AddIdeaHopperPage.LocationPopUpGoButton.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("create_idea_location_search_result_div"),Data["LocationPath"].ToString()));
                resDiv.Wait.ForVisible();
                //resDiv.Click(true);               
                //Actions.Click(resDiv.BaseElement);
                resDiv.MouseClick(MouseClickType.LeftClick);
            
            }*/
        }
    }
}
