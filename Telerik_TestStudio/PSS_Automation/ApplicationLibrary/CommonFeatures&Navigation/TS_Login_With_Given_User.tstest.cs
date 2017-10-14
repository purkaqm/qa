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

    public class TS_Login_With_Given_User : BaseWebAiiTest
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
    
      
    
        [CodedStep(@"Navigate to Landing Page and Login")]
        public void TS_Login_with_given_user_CodedStep()
        {
             string suffix = GetExtractedValue("UserSuffix").ToString();
             ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
             Manager.ActiveBrowser.WaitUntilReady();
             Manager.ActiveBrowser.Window.Maximize(); 
             string firstname = string.Format("Firstname{0}",suffix);
            string lastname = string.Format("Lastname{0}",suffix);
            string username = string.Format("Username{0}",suffix);
            string password = string.Format("Password{0}",suffix);
           
            if(isUserLoggedIn()){
                string firstLast = Pages.PS_HomePage.FirstNameLastNameDiv.TextContent;
                Log.WriteLine(firstLast);
                string expectedFirst = Data[firstname].ToString();
                string expectedLast = Data[lastname].ToString();
                
                if(firstLast.Contains(expectedFirst) && firstLast.Contains(expectedLast)){
                   return; 
                }
                else{
                    
                  // Execute test 'TS_Logout'
                  this.ExecuteTest("ApplicationLibrary\\CommonFeatures&Navigation\\TS_Logout.tstest");
                  Manager.ActiveBrowser.WaitUntilReady(); 
                  Manager.ActiveBrowser.ClearCache(BrowserCacheType.Cookies); 
                  
                    
                }
                
            }
            
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.UsernameTextField,Data[username].ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.PasswordTextField,Data[password].ToString());
            Pages.PS_LoginPage.SignInSubmitButton.Click();
            
            if(Data["IsLicencePopUp"].ToString().Contains("Yes")){
                Pages.PS_LicencePopup.ContinueButton.Wait.ForVisible();
                Pages.PS_LicencePopup.ContinueButton.Click();
            }
            
            ActiveBrowser.WaitUntilReady();
            
            //below code is due to cookie error on firefox and chrome... need to remove...
            System.Threading.Thread.Sleep(3000);
            if(ActiveBrowser.PageTitle.Contains("Session Expired")){
                ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
                ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.UsernameTextField,Data[username].ToString());
                ActiveBrowser.Actions.SetText(Pages.PS_LoginPage.PasswordTextField,Data[password].ToString());
                Pages.PS_LoginPage.SignInSubmitButton.Click();
            }
            
           SetExtractedValue("CurrentUsername",Data[username].ToString());
           SetExtractedValue("CurrentUserFirstname",Data[firstname].ToString());
           SetExtractedValue("CurrentUserLastname",Data[lastname].ToString()); 
           SetExtractedValue("CurrentUserPassword",Data[password].ToString());
         
        }
    
        [CodedStep(@"Verify User Logged in")]
        public void TS_Login_with_given_user_CodedStep1()
        {
            // Wait for Exists 'LogOutLink'
            Pages.PS_HomePage.LogOutLink.Wait.ForExists();
            // Wait for element 'CenterContainerDiv' 'is' visible.
            Pages.PS_HomePage.CenterContainerDiv.Wait.ForVisible();
            // Verify element 'LeftNavigationBarDiv' 'is' visible.
            Assert.AreEqual(true, Pages.PS_HomePage.LeftNavigationBarDiv.IsVisible(),"User should be on Home Page after Login");
        }
         public bool isUserLoggedIn(){
        Manager.ActiveBrowser.WaitUntilReady();
        System.Threading.Thread.Sleep(3000);
        if(ActiveBrowser.Find.AllByXPath(AppLocators.get("username_top_div")).Count > 0){
             Log.WriteLine("USER LOGGED IN");
            return true;
        }
        Log.WriteLine("USER NOT LOGGED IN");
        return false;
       } 
    }
}
