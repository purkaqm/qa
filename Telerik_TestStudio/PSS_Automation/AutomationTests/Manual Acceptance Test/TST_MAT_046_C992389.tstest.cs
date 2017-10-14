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

    public class TST_MAT_046_C992389 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Help icon")]
        public void TST_MAT_046_C992389_ClickHelpIcon()
        {
            Assert.IsTrue(Pages.PS_HomePage.HelpIconSpan.IsVisible(),"help icon should be present");
            Pages.PS_HomePage.HelpIconSpan.MouseClick(MouseClickType.LeftClick);
                                     
        }
    
        [CodedStep(@"Verify 'View All Online Documentation' link is displayed properly")]
        public void TST_MAT_046_C992389_VerifyAllOnlineHelpDocLink()
        {
            Assert.IsTrue(Pages.PS_HomePage.ViewOnlineHelpDocLink.IsVisible(),"View all online documentation help should be present");                        
        }
    
        [CodedStep(@"Click on 'View All Online Documentation' link")]
        public void TST_MAT_046_C992389_ClickAllOnlineHelpDocLink()
        {
            Pages.PS_HomePage.ViewOnlineHelpDocLink.Click();
                 
        }
    
        [CodedStep(@"Connect to pop-up window : 'https://upland.screenstepslive.com/s/PowerSteering' And Verify page")]
        public void TST_MAT_046_C992389_ConnectHelpPageAndVerify()
        {
            // Connect to pop-up window : 'https://upland.screenstepslive.com/s/PowerSteering'
            Manager.WaitForNewBrowserConnect("https://upland.screenstepslive.com/s/PowerSteering", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            Manager.ActiveBrowser.Window.Maximize();

        }
    
        [CodedStep(@"Verify Online Documentation page")]
        public void TST_MAT_046_C992389_VerifyDocPage()
        {
            Pages.PS_PowerSteeringHelpDocumetPage.SearchButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.SearchButton.IsVisible(),"Search button should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.AdministratorLink.IsVisible(),"Administration link should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.EndUserGuideLink.IsVisible(),"End User link should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.PowerSteeringUsersGuidesH2Tag.IsVisible(),"Powersteering header tag should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.SearchTextInput.IsVisible(),"Search input area should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.UplandCommunityLink.IsVisible(),"Upland community should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.UplandCommunityUserGuideLink.IsVisible(),"Upland community user guide link should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.UplandSoftwareImage.IsVisible(),"Upland software image should be visible");
            Assert.IsTrue(Pages.PS_PowerSteeringHelpDocumetPage.VideoLibraryLink.IsVisible(),"Videa library link should be visible");
            ActiveBrowser.Close();
        }
    }
}
