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

    public class TS_Create_User : BaseWebAiiTest
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
    
        [CodedStep(@"Enter FirstName")]
        public void TS_CS_01()
        {
            /// \bug Might be affected by PS-10461
            string firstName ="AutoFirst" + Randomizers.generateRandomInt(1000,9999);
            Log.WriteLine(firstName);
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserFirstNameText,firstName);
            SetExtractedValue("FirstName",firstName);
            
        }
        
        [CodedStep(@"Enter LastName")]
        public void TS_CS_02()
        {
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserLastNameText,"AutoLast");
        }
        
        [CodedStep(@"Enter EmailID")]
        public void TS_CS_03()
        {
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserEmailText,"Auto" + Randomizers.generateRandomInt(1000,9999) + "@gmail.com");
        }
        
        [CodedStep(@"Enter Username")]
        public void TS_CS_04()
        {
            string userName = "AutoName" + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserUserNameText,userName);
            SetExtractedValue("Username",userName);
        }
    
        [CodedStep(@"Click on Invite user button")]
        public void TS_CS_05()
        {
            Pages.PS_InviteCreateNewUserPage.AddUserInviteBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        
    }
}
