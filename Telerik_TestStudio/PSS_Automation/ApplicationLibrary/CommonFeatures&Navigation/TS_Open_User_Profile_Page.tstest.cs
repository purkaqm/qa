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

    public class TS_Open_User_Profile_Page : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Open_User_Profile_Page_CodedStep()
        {
            Pages.PS_HomePage.FirstNameLastNameDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_HomePage.PopupUserSettingsWindowDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Open_User_Profile_Page_CodedStep1()
        {
            HtmlAnchor profileLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(AppLocators.get("header_user_profile_link"));
            profileLink.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
            Pages.PS_UserProfilePage.EmailTableHeader.Wait.ForVisible();
            
        }
    }
}
