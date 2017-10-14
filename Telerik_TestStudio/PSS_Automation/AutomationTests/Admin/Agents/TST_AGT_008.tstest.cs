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

    public class TST_AGT_008 : BaseWebAiiTest
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
        
        int daysFirst,daysSecond;
        
    
        [CodedStep(@"Click on left navigation Admin link")]
        public void TST_AGT_008_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Click on 'Status change' agent")]
        public void TST_AGT_008_CS02()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.StatusChangeLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Work Status Change' link")]
        public void TST_AGT_008_CS03()
        {
            string agentName = "Work status change";
            HtmlAnchor agentLink = Pages.PS_AgentsPage.AgentsContainerFrame.Find.ByXPath<HtmlAnchor>(string.Format(AppLocators.get("work_status_change_link"),agentName));
            agentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        
    
        [CodedStep(@"Click on edit button")]
        public void TST_AGT_008_CS04()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.EditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify edit page is displayed")]
        public void TST_AGT_008_CS05()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.AgentTaskNameInput.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NextRunTimeTextInput.IsVisible());
        }
    
        [CodedStep(@"Select 'Work' checkbok and exclude other items")]
        public void TST_AGT_008_CS06()
        {
            if(Pages.PS_AgentsPage.AgentsContainerFrame.MilestonesCheckBox.Checked)
            {                
                   Pages.PS_AgentsPage.AgentsContainerFrame.MilestonesCheckBox.Click();
            }
            if(!Pages.PS_AgentsPage.AgentsContainerFrame.WorkChkbox.Checked)
            {                
                   Pages.PS_AgentsPage.AgentsContainerFrame.WorkChkbox.Click();
            }
            if(Pages.PS_AgentsPage.AgentsContainerFrame.ActionItemsCheckBox.Checked)
            {                
                   Pages.PS_AgentsPage.AgentsContainerFrame.ActionItemsCheckBox.Click();
            }
        }
    
        [CodedStep(@"Set Number of days where status is set to Needs Attention before it turns to Delayed")]
        public void TST_AGT_008_CS07()
        {
            daysFirst = Randomizers.generateRandomInt(10,60);
            Pages.PS_AgentsPage.AgentsContainerFrame.NeedAttentionInput.BaseElement.SetValue("value",daysFirst);
        }
        
        [CodedStep(@"Set Number of days where status is set to Needs Attention (Delayed) before it turns to Off Track")]
        public void TST_AGT_008_CS08()
        {
            daysSecond = Randomizers.generateRandomInt(1,10);
            Pages.PS_AgentsPage.AgentsContainerFrame.NeedAttentionDelayedInput.BaseElement.SetValue("value",daysSecond);
        }
    
        [CodedStep(@"Click on Submit button")]
        public void TST_AGT_008_CS09()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Run the agent")]
        public void TST_AGT_008_CS10()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.RunAgentLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify upadate is applied")]
        public void TST_AGT_008_CS11()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NeedAttentionDaysAgentPageSpan.BaseElement.InnerText.Contains(daysFirst.ToString()));
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NeedAttentionDelayedDaysAgentPageSpan.BaseElement.InnerText.Contains(daysSecond.ToString()));
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.ParametersWorkImage.BaseElement.GetAttribute("alt").Value.Equals("yes"));
        }
    }
}
