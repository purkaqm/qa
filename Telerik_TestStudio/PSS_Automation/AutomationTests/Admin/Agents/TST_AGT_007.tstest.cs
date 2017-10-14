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

    public class TST_AGT_007 : BaseWebAiiTest
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
        
        string nextRunTime; 
    
        [CodedStep(@"Click on left navigation Admin link")]
        public void TST_AGT_007_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Delete Old Users Agent' link")]
        public void TST_AGT_007_CS02()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.DeleteOldUsersAgtLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select Agent title link")]
        public void TST_AGT_007_CS03()
        {
            string agentName = "Delete Old Users Agent";
            HtmlAnchor agentTitleLink = Pages.PS_AgentsPage.AgentsContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("admin_agent_name_link"),agentName));
            agentTitleLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit button")]
        public void TST_AGT_007_CS04()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.EditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Update the value for 'Next Run Time'")]
        public void TST_AGT_007_CS05()
        {
            DateTime dt = new DateTime(2016,9,2,04,10,00);
            
            nextRunTime = string.Format("{0:MM/dd/yyyy hh:mm tt}",dt);
            Pages.PS_AgentsPage.AgentsContainerFrame.NextRunTimeTextInput.BaseElement.SetValue("value",nextRunTime);
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Submit button")]
        public void TST_AGT_007_CS06()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify updated time Interval is saved")]
        public void TST_AGT_007_CS07()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NextRunTimeTableCell.BaseElement.InnerText.Contains(nextRunTime));
        }
    }
}
