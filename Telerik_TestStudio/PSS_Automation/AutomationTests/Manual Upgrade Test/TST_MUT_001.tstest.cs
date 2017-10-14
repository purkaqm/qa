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

    public class TST_MUT_001 : BaseWebAiiTest
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
    
        
       [CodedStep(@"Navigate to login page")]
        public void TST_MUT_001_CS01()
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
    
        [CodedStep(@"Verify all required login components are displayed")]
        public void TST_MUT_001_CS02()
        {
            string url = ActiveBrowser.Url;
            
            
            if(ActiveBrowser.Find.AllByXPath<HtmlImage>("//div[@class='image']/img").Count > 0 ){
                Log.WriteLine("Lohin Logo is present");
                Assert.IsTrue(Pages.PS_LoginPage.LoginLogoImage.IsVisible(),"Login logo should be displayed");
            }
            else{
                    HtmlSpan defaultLoginLogo = ActiveBrowser.Find.ByXPath<HtmlSpan>("//div[@class='image']/span");
                    defaultLoginLogo.Wait.ForExists();
                    Assert.IsTrue(defaultLoginLogo.IsVisible(),"Default logo should be displayed");
            }
            /// \bug Domain check is missing
            Assert.IsTrue(Pages.PS_LoginPage.UsernameTextField.IsVisible(),"Username textfield should be present");
            Assert.IsTrue(Pages.PS_LoginPage.PasswordTextField.IsVisible(),"Password textfield should be present");
            Assert.IsTrue(Pages.PS_LoginPage.HelpAndForgotDiv.IsVisible(),"Help link should be present");
            Assert.IsTrue(Pages.PS_LoginPage.SignInSubmitButton.IsVisible(),"SignIn button should be present");
            if(url.Contains("quicktest")) /// \bug Should not be context specific
            {
                Assert.IsTrue(Pages.PS_LoginPage.LocaleSelectDiv.IsVisible(),"Local selected option be displayed");
            }
            Assert.IsTrue(Pages.PS_LoginPage.SupportLink.IsVisible(),"Support link should be present");
            Assert.IsTrue(Pages.PS_LoginPage.BuildNunberDiv.IsVisible(),"Build number should be present");
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
