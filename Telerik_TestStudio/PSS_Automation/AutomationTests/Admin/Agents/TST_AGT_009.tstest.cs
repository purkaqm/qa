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

    public class TST_AGT_009 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on left navigation Admin link")]
        public void TST_AGT_009_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Status change' agent")]
        public void TST_AGT_009_CS02()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.StatusReportAgentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'StatusReport' link")]
        public void TST_AGT_009_CS03()
        {
            ActiveBrowser.RefreshDomTree();
            string agentName = "StatusReport";
            HtmlAnchor agentLink = Pages.PS_AgentsPage.AgentsContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("status_report_link"),agentName));
            agentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit button")]
        public void TST_AGT_009_CS04()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.EditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify edit page is displayed")]
        public void TST_AGT_009_CS05()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.AgentTaskNameInput.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NextRunTimeTextInput.IsVisible());
        }
        
        [CodedStep(@"Select the 'Is Active' toggle to No")]
        public void TST_AGT_009_CS06()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.IsActiveRadioNoInput.Click();
        }
    
        [CodedStep(@"Click on Submit button")]
        public void TST_AGT_009_CS07()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify Agents page displays the status Is Active to 'No'")]
        public void TST_AGT_009_CS08()
        {
            
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.IsActiveDetailsTableCell.InnerText.Contains("NO"));
            
        }
    }
}
