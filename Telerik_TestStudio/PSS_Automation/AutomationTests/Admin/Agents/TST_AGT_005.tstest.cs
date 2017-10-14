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

    public class TST_AGT_005 : BaseWebAiiTest
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
        public void TST_AGT_005_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Delete Old Users Agent' link")]
        public void TST_AGT_005_CS02()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.DeleteOldUsersAgtLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select Agent title link")]
        public void TST_AGT_005_CS03()
        {
            string agentName = "Delete Old Users Agent";
            HtmlAnchor agentTitleLink = Pages.PS_AgentsPage.AgentsContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("admin_agent_name_link"),agentName));
            Log.WriteLine(agentTitleLink.ToString());
            agentTitleLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on edit button")]
        public void TST_AGT_005_CS04()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.EditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Select the 'Is Active' radio button to N0")]
        public void TST_AGT_005_CS05()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.IsActiveRadioNoInput.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Click on Submit button")]
        public void TST_AGT_004_CS06()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify 'Is Active' changes to No")]
        public void TST_AGT_005_CS07()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.IsActiveDetailsTableCell.InnerText.Contains("NO"));
        }
    
        
    }
}
