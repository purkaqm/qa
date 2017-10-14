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

    public class TST_UCO_031 : BaseWebAiiTest
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
        
        string lastLoginStr,lastLoginDiffStr;
    
        [CodedStep(@"Set user for login")]
        public void TST_UCO_031_CS01()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        [CodedStep(@"Verify Admin left navigation tab is present")]
        public void TST_UCO_031_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_031_CS03()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that in left navigation title 'admin' is displayed")]
        public void TST_UCO_031_CS04()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.BaseElement.InnerText.Contains("ADMIN"));
        }
    
        [CodedStep(@"Make any changes on 'user creation option' page")]
        public void TST_UCO_031_CS05()
        {
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
            var timeUtc = DateTime.UtcNow;
            Log.WriteLine(timeUtc.ToString());
            //Log.WriteLine(DateTime.Now.ToString("HH:mm tt"));
            TimeZoneInfo easternTime = TimeZoneInfo.FindSystemTimeZoneById("Eastern Standard Time");
            Log.WriteLine(easternTime.ToString());
            DateTime currentTime = TimeZoneInfo.ConvertTimeFromUtc(timeUtc,easternTime);
            DateTime minuteAgoTime = currentTime.AddMinutes(-1.00);
            lastLoginStr = currentTime.ToString("MM/dd/yyyy HH:mm tt");
            lastLoginDiffStr = minuteAgoTime.ToString("MM/dd/yyyy HH:mm tt");
            Log.WriteLine(lastLoginStr);
            Log.WriteLine(lastLoginDiffStr);
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
        }
    
        [CodedStep(@"Select 'Admin-Other' category")]
        public void TST_UCO_031_CS06()
        {
                                
            Pages.PS_LogsPage.ViewDropDownList.SelectByText("Admin - Other");
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_UCO_031_CS07()
        {
                                
            Pages.PS_LogsPage.GoBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify new event with message is displayed")]
        public void TST_UCO_009_CS08()
        {
            string userName = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value;
            Log.WriteLine(userName);
            string otherMsg = "User";
            string remOtherMsg = "changed user creation options";
            string finalStr1 = string.Format(AppLocators.get("logs_event_change_message_row"),lastLoginStr,otherMsg,userName,remOtherMsg);
            string finalStr2 = string.Format(AppLocators.get("logs_event_change_message_row"),lastLoginDiffStr,otherMsg,userName,remOtherMsg);
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(finalStr1).Count>0 || ActiveBrowser.Find.AllByXPath(finalStr2).Count>0 ,"Log entry should be correct");
        }
    }
}
