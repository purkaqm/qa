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

    public class Ts_Open_User_Creation_Options_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for Resource Planning Menu Item in Left Navigation Panel")]
        public void Ts_Open_User_Creation_Options_Page_CodedStep()
        {
            Pages.PS_HomePage.AdminUserCreationOptTabDiv.Wait.ForExists();
        }
    
        [CodedStep(@"Click on Resource Planning tab")]
        public void Ts_Open_User_Creation_Options_Page_CodedStep1()
        {
            Pages.PS_HomePage.AdminUserCreationOptTabDiv.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for Resource Planning page to be displayed")]
        public void Ts_Open_User_Creation_Options_Page_CodedStep2()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"User Creation Options");
            Pages.PS_UserCreationOptionsPage.SaveBtn.Wait.ForVisible();
        }
    }
}
