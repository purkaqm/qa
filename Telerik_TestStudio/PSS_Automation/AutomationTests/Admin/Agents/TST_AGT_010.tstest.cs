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

    public class TST_AGT_010 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on left navigation Admin link")]
        public void TST_AGT_010_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Status change' agent")]
        public void TST_AGT_010_CS02()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_AgentsPage.AgentsContainerFrame.StatusChangeLink.Wait.ForExists();
            Pages.PS_AgentsPage.AgentsContainerFrame.StatusChangeLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Work Status Change' link")]
        public void TST_AGT_010_CS03()
        {
            ActiveBrowser.RefreshDomTree();
            string agentName = "Work status change";
            HtmlAnchor agentLink = Pages.PS_AgentsPage.AgentsContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("work_status_change_link"),agentName));
            agentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Run the agent")]
        public void TST_AGT_010_CS04()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.RunAgentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Get current Date and Time")]
        public void TST_AGT_010_CS05()
        {
            var timeUtc = DateTime.UtcNow;
            TimeZoneInfo easternZone = TimeZoneInfo.FindSystemTimeZoneById("Eastern Standard Time");
            DateTime currentTime = TimeZoneInfo.ConvertTimeFromUtc(timeUtc, easternZone);
            DateTime minuteAgoTime = currentTime.AddMinutes(-1.00);
            DateTime twoMinuteAgoTime = minuteAgoTime.AddMinutes(-1.00);
            lastLoginStr = currentTime.ToString("MM/dd/yyyy HH:mm tt");
            lastLoginDiffStr = minuteAgoTime.ToString("MM/dd/yyyy HH:mm tt");
            lastLoginDiffStr2 =  twoMinuteAgoTime.ToString("MM/dd/yyyy HH:mm tt");
            Log.WriteLine(lastLoginStr);
            Log.WriteLine(lastLoginDiffStr);
            Log.WriteLine(lastLoginDiffStr2); 
        }
        
        [CodedStep(@"")]
        public void TST_AGT_010_CS06()
        {
            
        }
        
        
    
    }
}
