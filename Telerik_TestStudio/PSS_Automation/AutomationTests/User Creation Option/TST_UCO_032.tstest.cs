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

    public class TST_UCO_032 : BaseWebAiiTest
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
    
        [CodedStep(@"Set user for login")]
        public void TST_UCO_032_CS01()
        {
            SetExtractedValue("UserSuffix","1");
        }
        
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_030_CS02()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Turn on 'Enforce required tags and required custom fields' at Invite New Users (spreadsheet) column")]
        public void TST_UCO_030_CS03()
        {
                                    
                        Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Check(true);
        }
    
        [CodedStep(@"Click on Save button")]
        public void TST_UCO_030_CS04()
        {
                        if(Pages.PS_UserCreationOptionsPage.SaveBtn.IsEnabled)
                        {
                            Pages.PS_UserCreationOptionsPage.SaveBtn.Click();
                            ActiveBrowser.RefreshDomTree();
                        }
        }

        [CodedStep(@"Click on Add tab on left navigation panel")]
        public void TST_UCO_032_CS05()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
        }
    
        [CodedStep(@"Click on User tab")]
        public void TST_UCO_032_CS06()
        {
            Pages.PS_HomePage.AddUserTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify warning is displayed")]
        public void TST_UCO_032_CS07()
        {
            Assert.IsTrue(Pages.PS_InviteCreateNewUserPage.WarningMsgRedBoxDiv.IsVisible());
        }
    
        
    }
}
