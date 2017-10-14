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

    public class TST_UP_010 : BaseWebAiiTest
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
        
        string mobileNum,cityName;
    
        [CodedStep(@"Click on User Name link")]
        public void TST_UP_010_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_010_CS02()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit User tab")]
        public void TST_UP_010_CS03()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Upadate Personal Info")]
        public void TST_UP_010_CS04()
        {
             mobileNum = Data["MobileNumber"].ToString();
             cityName = Data["CityName"].ToString();

            //update mobile number
            
            Actions.SetText(Pages.PS_SettingsProfilePage.MobileInput,"");
            Actions.SetText(Pages.PS_SettingsProfilePage.MobileInput,mobileNum); 
            
            //update city name
            
            Actions.SetText(Pages.PS_SettingsProfilePage.CityNameInput,"");
            Actions.SetText(Pages.PS_SettingsProfilePage.CityNameInput,cityName);
 
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UP_010_CS05()
        {
            Pages.PS_SettingsProfilePage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
         
        [CodedStep(@"[TST_UP_010_CS06] : Verify user is directed to User Profile page")]
        public void TST_UP_010_CS06()
        {
            Pages.PS_UserProfilePage.UserProfileListItemTab.Wait.ForExists();
            Pages.PS_UserProfilePage.DocumentsLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"Edit User link tab should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.UserProfileListItemTab.IsVisible(),"User Profile tab should be present");
        }
        
        [CodedStep(@"Verify Updated personal info is reflected in Profile page")]
        public void TST_UP_010_CS07()
        {
            Assert.IsTrue(Pages.PS_UserProfilePage.MobileNumRow.BaseElement.InnerText.Contains(mobileNum),"Mobile nunber should be matched");
            Assert.IsTrue(Pages.PS_UserProfilePage.CityNameRow.BaseElement.InnerText.Contains(cityName),"City name should should be matched");
        }
    }
}
