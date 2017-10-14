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

    public class TST_MUT_002 : BaseWebAiiTest
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
        
        [CodedStep(@"Navigate to landing page")]
        public void TST_MUT_002_CS01()
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
        }
        
        [CodedStep(@"Select the required Locale and verify locale is switched")]
        public void TST_MUT_002_CS02()
        {   
            if(ActiveBrowser.Url.Contains("quicktest")) /// \bug Should not be context specific
            {
               string[] localeListValue = {"English", "English (United States)","español","español (España)","français","italiano (Italia)","italiano","日本語"}; 
               string[] localeListChangeValue = {"Sign in","Sign in","Iniciar sesión","Iniciar sesión","Connexion","Accedi","Accedi","ログイン"};
               for (int i=0;i<localeListValue.Count();i++)
               {    
                   Pages.PS_LoginPage.LocaleSelectDiv.Click(true);  
                   HtmlDiv localeLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("login_page_locale_list"),localeListValue[i]));
                   localeLocator.Wait.ForExists();
                   localeLocator.Click();
                   ActiveBrowser.WaitUntilReady();
                   Assert.IsTrue(Pages.PS_LoginPage.SignInSubmitButton.BaseElement.GetAttribute("value").Value.Equals(localeListChangeValue[i]));
               }
            }
        }
        
        [CodedStep(@"Select default locale(English US)")]
        public void TST_MUT_002_CS03()
        {
            if(ActiveBrowser.Url.Contains("quicktest")) /// \bug Should not be context specific
            {
               string localeValue =  "English (United States)";     /// \bug Default locale is not EN US but EN
               Pages.PS_LoginPage.LocaleSelectDiv.Click(true);
               HtmlDiv localeLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("login_page_locale_list"),localeValue));
               localeLocator.Wait.ForExists();
               localeLocator.Click();
               ActiveBrowser.WaitUntilReady();
               Assert.IsTrue(Pages.PS_LoginPage.SignInSubmitButton.BaseElement.GetAttribute("value").Value.Equals("Sign in"));   
            }
        }
        
         public bool isUserLoggedIn()
        {
            if(ActiveBrowser.Find.AllByXPath(AppLocators.get("username_top_div")).Count > 0)
            {
                Log.WriteLine("USER LOGGED IN");
                return true;
            }
                Log.WriteLine("USER NOT LOGGED IN");
                return false;
        }
    
        
    }
}
