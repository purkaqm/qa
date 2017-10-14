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

    public class TST_UCO_009 : BaseWebAiiTest
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
        
        string lastModifiedStr;
    
        [CodedStep(@"Verify Admin left navigation tab is present")]
        public void TST_UCO_009_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_009_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that in left navigation title 'admin' is displayed")]
        public void TST_UCO_009_CS03()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.BaseElement.InnerText.Contains("ADMIN"));
        }
    
        [CodedStep(@"Make any changes on 'user creation option' page")]
        public void TST_UCO_009_CS04()
        {
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
            var timeUtc = DateTime.UtcNow;
            Log.WriteLine(timeUtc.ToString());
            Log.WriteLine(DateTime.Now.ToString("HH:mm tt"));
            TimeZoneInfo easternTime = TimeZoneInfo.FindSystemTimeZoneById("Eastern Standard Time");
            DateTime currentTime = TimeZoneInfo.ConvertTimeFromUtc(timeUtc,easternTime);
            lastModifiedStr = currentTime.ToString("MM/dd/yyyy");
            Log.WriteLine(lastModifiedStr);
            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
        }
    
        [CodedStep(@"Select 'Admin-Other' category")]
        public void TST_UCO_009_CS05()
        {
                    
            Pages.PS_LogsPage.ViewDropDownList.SelectByText("Admin - Other");
        }
    
        [CodedStep(@"Set days on calendar when there were no events")]
        public void TST_UCO_009_CS06()
        {
            string startDate = Data["StartDate"].ToString();
            Pages.PS_LogsPage.AdminLogsFromDate.SetValue("value","");
            Pages.PS_LogsPage.AdminLogsFromDate.MouseClick(MouseClickType.LeftClick);
            Manager.Desktop.KeyBoard.TypeText(startDate,01);
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_UCO_009_CS07()
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
            Log.WriteLine(string.Format(AppLocators.get("logs_event_change_message_row"),lastModifiedStr,otherMsg,userName,remOtherMsg));
            HtmlTableRow msgLocator = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("logs_event_change_message_row"),lastModifiedStr,otherMsg,userName,remOtherMsg));
            msgLocator.Wait.ForExists();
            Assert.IsTrue(msgLocator.IsVisible());
        }
    }
}
