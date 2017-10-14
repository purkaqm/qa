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

    public class TST_NAV_UI_018 : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_018_CodedStep()
        {
            ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize(); 
            
            if(isUserLoggedIn())
            {
                 this.ExecuteTest("ApplicationLibrary\\CommonFeatures&Navigation\\TS_Logout.tstest");
                  Manager.ActiveBrowser.WaitUntilReady(); 
                  Manager.ActiveBrowser.ClearCache(BrowserCacheType.Cookies); 
                
            }
            HtmlDiv  elementLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("bulid_number_div"));
            Assert.IsTrue(elementLocator.IsVisible(),"Bulid no. should properly displayed");
            Assert.IsTrue(elementLocator.InnerText.Contains("v. trunk -"));
            
            
            
            
            
        }
        
        public bool isUserLoggedIn(){
        if(ActiveBrowser.Find.AllByXPath(AppLocators.get("username_top_div")).Count > 0){
             Log.WriteLine("USER LOGGED IN");
            return true;
        }
        Log.WriteLine("USER NOT LOGGED IN");
        return false;
       } 
    }
}
