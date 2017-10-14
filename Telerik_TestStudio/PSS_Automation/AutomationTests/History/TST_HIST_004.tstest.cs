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

    public class TST_HIST_004 : BaseWebAiiTest
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
    
        [CodedStep(@"Store New Project Page URL")]
        public void CSHIST00401()
        {
            string projUrl = ActiveBrowser.Url;
            SetExtractedValue("Page1URL", projUrl);
        }
        
        [CodedStep(@"Store Dashboard Page URL")]
        public void CSHIST00402()
        {
            string dashUrl = ActiveBrowser.Url;
            SetExtractedValue("Page2URL", dashUrl);
        }
        
        [CodedStep(@"Verify user is navigated to Manage History page")]
        public void CSHIST00403()
        {
            Pages.PS_ManageHistoryPage.GoButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ManageHistoryPage.SearchInputBox.IsVisible(),"User should be navigated to History page");
            Assert.IsTrue(Pages.PS_ManageHistoryPage.FromDateInputBox.IsVisible(),"User should be navigated to History page");
            Assert.IsTrue(Pages.PS_ManageHistoryPage.GoButton.IsVisible(),"User should be navigated to History page");
           
        }
        
    
        
        [CodedStep(@"verify name of history pages...")]
        public void CSHIST00404()
        {
           
            
            IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
            Assert.IsTrue(nameElements[0].InnerText.Contains("Dashboard"),"Dashboard should be displayed in history table name coumn");
            Assert.IsTrue(nameElements[1].InnerText.Contains("Create New Project"),"Create New Project should be displayed in history table name column");
            
                        
        }
        
        [CodedStep(@"verify description of history pages...")]
        public void CSHIST00405()
        {
           
            IList<Element> descElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_desc_records"));
            Log.WriteLine(descElements[0].InnerText);
            Assert.IsTrue(descElements[0].InnerText.Contains("Dashboard") && descElements[0].InnerText.Contains("Portfolio") && descElements[0].InnerText.Contains("Layout"),"Dashboard should be displayed in history table description coumn");
            
           
                        
        }
        
        [CodedStep(@"verify URLs of history pages...")]
        public void CSHIST00406()
        {
           
            string dashUrl = GetExtractedValue("Page2URL").ToString();
            string projUrl = GetExtractedValue("Page1URL").ToString();
                        
            IList<Element> urlElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_url_records"));
            Assert.IsTrue(urlElements[0].InnerText.Contains(dashUrl),"Dashboard page URL should be displayed in history table url coumn");
            Assert.IsTrue(urlElements[1].InnerText.Contains(projUrl),"Create new project page URL should be displayed in history table url coumn");
            
                                
        }
        
        [CodedStep(@"verify data time stamp of history pages...")]
        public void CSHIST00407()
        {
           
            IList<Element> dateTimeElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_datetime_records"));
            string dashboardDatTimeStr = dateTimeElements[0].InnerText;
            string newprojectDatTimeStr = dateTimeElements[1].InnerText;
            
            DateTime dashboardDatTime = DateTime.ParseExact(dashboardDatTimeStr, "MM/dd/yyyy HH:mm tt",null);
            DateTime newprojectDatTime = DateTime.ParseExact(newprojectDatTimeStr, "MM/dd/yyyy HH:mm tt",null);
            
            Assert.IsTrue(DateTime.Compare(dashboardDatTime,newprojectDatTime) >= 0,"time stamp column should be displayed");
                        
        }
    }
}
