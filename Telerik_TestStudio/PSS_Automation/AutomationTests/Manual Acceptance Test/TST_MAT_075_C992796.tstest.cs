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

    public class TST_MAT_075_C992796 : BaseWebAiiTest
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
        
        [CodedStep(@"Check 'Home show dashboard' setting on uix_edit page")]
        public void TST_MAT_075_C992796_CheckSetting()
        {
            HtmlSelect showDasdboardSelect =  ActiveBrowser.Find.ByXPath<HtmlSelect>("//table[@class='bgEdit']//table//tr[contains(.,'Home: Show Dashboard')]//select");
            showDasdboardSelect.Wait.ForExists();
            showDasdboardSelect.SelectByValue("On");
            Pages.PS_UIXEditPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Navigate to Home page")]
        public void TST_MAT_075_C992796_NavigateToHomePage()
        {
            ActiveBrowser.NavigateTo("~/Home.page");
        }
        
        [CodedStep(@"Select Dashboard from Home Show list")]
        public void TST_MAT_001_C992487_SelectDashboard()
        {
            Pages.PS_SettingsPreferencesPage.MyDashboardCheckbox.Check(true);
            Pages.PS_SettingsPreferencesPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify Dashboard header is displayed")]
        public void TST_MAT_075_C992796_VerifyDashboardHeader()
        {
            Pages.PS_HomePage.DashboardH2Tag.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.DashboardH2Tag.IsVisible(),"Dashboard H2 tag should be visible");
        }

        [CodedStep(@"Change home dashboard setting on Uix_edit.jsp page: set to off ")]
        public void TST_MAT_075_C992796_ChangeSetting()
        {
            HtmlSelect showDasdboardSelect =  ActiveBrowser.Find.ByXPath<HtmlSelect>("//table[@class='bgEdit']//table//tr[contains(.,'Home: Show Dashboard')]//select");
            showDasdboardSelect.Wait.ForExists();
            
            showDasdboardSelect.SelectByValue("off");
            Pages.PS_UIXEditPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user redirected to uix_key.jsp page")]
        public void TST_MAT_075_C992796_VerifyUixKeyPage()
        {
            string url = ActiveBrowser.Url;
            Pages.PS_UixKeyPage.EditFeatureLink.Wait.ForExists();
            Assert.IsTrue(url.Contains("uix_key.jsp"),"Uix_key page is displayed");
            Assert.IsTrue(Pages.PS_UixKeyPage.EditFeatureLink.IsVisible());
            
        }
        
           
        [CodedStep(@"Verify change has been saved sussesfully")]
        public void TST_MAT_075_C992796_VerifyChange()
        {
            HtmlImage shoeDashboardImg = ActiveBrowser.Find.ByXPath<HtmlImage>("//td[@id='contentCell']/table[2]//tr[contains(.,'Home: Show Dashboard')]/td[2]//img");
            shoeDashboardImg.Wait.ForExists();
            Assert.AreEqual(shoeDashboardImg.BaseElement.GetAttribute("title").Value, "Off");
        }
    
    }
}
