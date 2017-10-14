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

    public class TST_MUT_021 : BaseWebAiiTest
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
        
        /// \bug This probably needs an overhaul in the light of switching to MadCap.
        /// https://uplandsoftware.atlassian.net/browse/PS-11518
        /// Should probably generalize enough to handle context sensitiveness.
    
        [CodedStep(@"Verify 'help icon' is displayed properly")]
        public void TST_MUT_021_VerifyHelpIcon()
        {
            Assert.IsTrue(Pages.PS_HomePage.HelpIconSpan.IsVisible(),"help icon should be present");
                         
        }
    
        [CodedStep(@"Verify 'view online link' is displayed properly")]
        public void TST_MUT_021_VerifyOnlineLink()
        {
            Pages.PS_HomePage.HelpIconSpan.Click();
            Pages.PS_HomePage.HelpIconSpan.InvokeEvent(ScriptEventType.OnMouseUp);            
            Assert.IsTrue(Pages.PS_HomePage.ViewOnlineHelpCell.IsVisible(),"view online help should be present");
                        
        }
    
        [CodedStep(@"Click on 'view online link' and verify help window is opened")]
        public void TST_MUT_021_ClickOnlineLink()
        {
            /// \bug Move relative page to a central place
            /// Might fail for v18.0 (MadCap)
            Pages.PS_HomePage.ViewOnlineHelpCell.Click();
            Manager.WaitForNewBrowserConnect("/help/help.jsp", true,Manager.Settings.ClientReadyTimeout);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();
            /// \bug Fix this so that it works for both old and new help
            //Pages.PS_PowerSteeringHelp.FrameHelpFrame0.SearchButton.Wait.ForExists();
            Assert.IsTrue(ActiveBrowser.Url.Contains("/help/help.jsp"),"Help window sould be open");            
            //Assert.IsTrue(Pages.PS_PowerSteeringHelp.FrameHelpFrame0.LogOutLink.IsVisible(),"LogOut link should be visible");
            //Assert.IsTrue(Pages.PS_PowerSteeringHelp.FrameHelpFrame0.SearchButton.IsVisible(),"Search Button should be visible");
            //Assert.IsTrue(Pages.PS_PowerSteeringHelp.FrameHelpFrame0.SearchTextAreaInput.IsVisible(),"Search area should be visible");
            //Assert.IsTrue(Pages.PS_PowerSteeringHelp.FrameHelpFrame0.TopicsH3Tag.IsVisible(),"Topics header tag should be visible");
            //Assert.IsTrue(Pages.PS_PowerSteeringHelp.FrameHelpFrame0.UplandCommunityLink.IsVisible(),"Upland community should be visible");
            ActiveBrowser.Close();            
        }
    }
}
