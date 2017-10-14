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

    public class TST_UP_009 : BaseWebAiiTest
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
    
        string lastLoginStr;
        string lastLoginDiffStr;
        string lastLoginDiffStr2;
        
        [CodedStep(@"Get current Date and Time")]
        public void TST_UP_009_CS01()
        {
            var timeUtc = DateTime.UtcNow;
            TimeZoneInfo easternZone = TimeZoneInfo.FindSystemTimeZoneById("Eastern Standard Time");
            DateTime currentTime = TimeZoneInfo.ConvertTimeFromUtc(timeUtc, easternZone);
            DateTime minuteAgoTime = currentTime.AddMinutes(-1.00);
            DateTime twoMinuteAgoTime = minuteAgoTime.AddMinutes(-1.00);
            lastLoginStr = currentTime.ToString("MM/dd/yyyy HH:mm tt");
            lastLoginDiffStr = minuteAgoTime.ToString("MM/dd/yyyy HH:mm tt");
            lastLoginDiffStr2 =  twoMinuteAgoTime.ToString("MM/dd/yyyy HH:mm tt");
            
        }
    
        [CodedStep(@"Click on User Name link")]
        public void TST_UP_009_CS02()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();   
        }
    
        [CodedStep(@"Click on Profile link")]
        public void TST_UP_009_CS03()
        {
            Pages.PS_HomePage.ProfileDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Last Login field displays correct date and time")]
        public void TST_UP_009_CS04()
        {
            string lastLoginValue = Pages.PS_UserProfilePage.LastLoginRow.InnerText.ToString();
            Log.WriteLine(lastLoginValue);
            Assert.IsTrue(lastLoginValue.Contains(lastLoginStr) || lastLoginValue.Contains(lastLoginDiffStr)|| lastLoginValue.Contains(lastLoginDiffStr2),"Date and Time should be matched");
        }
    }
}
