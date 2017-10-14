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

    public class TS_Open_Invite_Create_User_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Work Tree tab")]
        public void TS_Open_Add_Project_Page_CodedStep()
        {
            // Click on Dashboard
            //Pages.PS_HomePage.AddProjectTab.Click(false);
            Pages.PS_HomePage.AddUserTab.Click(true);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Work Tree Page to be Loaded")]
        public void TS_Open_Add_Project_Page_CodedStep1()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_InviteCreateNewUserPage.AddUserFirstNameText.Wait.ForExists();
            Pages.PS_InviteCreateNewUserPage.AddUserInviteBtn.Wait.ForExists();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            
        }
    }
}
