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

    public class TST_HMC_010 : BaseWebAiiTest
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
        
        List<string> executivelayouts;
        List<string> portfolios;
        // Add your test methods here...
    
        [CodedStep(@"Click Add new configuration link")]
        public void CS_HMC_010_01()
        {
            Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.Click();
            ActiveBrowser.WaitUntilReady();
                    
        }
    
        [CodedStep(@"Wait till user navigates to Add/Edit configuration page")]
        public void CS_HMC_010_02()
        {
            Pages.PS_AddEditHomePage.SaveButton.Wait.ForExists();
            Pages.PS_AddEditHomePage.BackToListLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigNameInput.IsVisible(),"Name input field should be present on  New Configuration page");
            Assert.IsTrue(Pages.PS_AddEditHomePage.NewConfigDescTextArea.IsVisible(),"Description textarea should be present on  New Configuration page");
                                    
        }
    
        [CodedStep(@"Click on My Executive Review Checkbox")]
        public void CS_HMC_010_03()
        {
             string optLocator = string.Format(AppLocators.get("new_hmc_config_record_chkbx"),"My Executive Review");
             HtmlInputCheckBox chkbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(optLocator);
             chkbox.Click();
             ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
        
        [CodedStep(@"Wait till Portfolio and Layout dropdowns are visible for executive revied initialization")]
        public void CS_HMC_010_04()
        {
            Pages.PS_AddEditHomePage.ERPortfolioSelector.Wait.ForExists();
            Pages.PS_AddEditHomePage.ERLayoutSelector.Wait.ForExists();
        }
    
        [CodedStep(@"Get all executive review layouts")]
        public void CS_HMC_010_06()
        {
            executivelayouts = new List<string>();
            IList<Element> exLayouts = ActiveBrowser.Find.AllByXPath(AppLocators.get("exrev_layout_page_records_name"));
            foreach (Element e in exLayouts){
                 executivelayouts.Add(e.InnerText.Trim());
            }
        }
        
        [CodedStep(@"Get all portfolios")]
        public void CS_HMC_010_05()
        {
            portfolios = new List<string>();
            IList<Element> exPortfolios = ActiveBrowser.Find.AllByXPath(AppLocators.get("rev_portfolios_page_records_name"));
            foreach (Element e in exPortfolios){
                 portfolios.Add(e.InnerText.Trim());
            }
        }
    
        [CodedStep(@"Verify portfolio options for executive review")]
        public void CS_HMC_010_07()
        {
            string ERPortfolioOptions = Pages.PS_AddEditHomePage.ERPortfolioSelector.InnerText;
             foreach (string e in portfolios){
                 Assert.IsTrue(ERPortfolioOptions.Contains(e),e + " should be present in executive review portfolio options");
            }
        }
        
        [CodedStep(@"Verify layouts options for executive review")]
        public void CS_HMC_010_08()
        {
            string ERLayoutOptions = Pages.PS_AddEditHomePage.ERLayoutSelector.InnerText;
             foreach (string e in executivelayouts){
                 Assert.IsTrue(ERLayoutOptions.Contains(e),e + " should be present in executive review layout options");
            }
        }
    }
}
