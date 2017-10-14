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

    public class TST_UP_012 : BaseWebAiiTest
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
    
        string cityName,prevCityName;
        
        [CodedStep(@"Click on User Name link")]
        public void TST_UP_012_CS01()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_012_CS02()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit User tab")]
        public void TST_UP_012_CS03()
        {
            Pages.PS_UserProfilePage.EditUserLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Upadate Personal Info")]
        public void TST_UP_012_CS04()
        {
             prevCityName = Pages.PS_SettingsProfilePage.CityNameInput.Value;
             cityName = Data["CityName"].ToString();
            
            //update city name
            
            Actions.SetText(Pages.PS_SettingsProfilePage.CityNameInput,"");
            Actions.SetText(Pages.PS_SettingsProfilePage.CityNameInput,cityName);
             
        }
    
        [CodedStep(@"Click on Cancel button")]
        public void TST_UP_012_CS05()
        {
            Pages.PS_SettingsProfilePage.CancelBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
        

        [CodedStep(@"Verify User Info is not updated")]
        public void TST_UP_012_CS06()
        {
            Assert.IsTrue(Pages.PS_UserProfilePage.CityNameRow.BaseElement.InnerText.Contains(prevCityName),"City name should should be matched");
        }
    }
}
