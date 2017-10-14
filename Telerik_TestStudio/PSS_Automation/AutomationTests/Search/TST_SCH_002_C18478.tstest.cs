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

    public class TST_SCH_002_C18478 : BaseWebAiiTest
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
        public void TST_SCH_002_C18478_CreateUser()
        {
            //Create user
            ActiveBrowser.Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserFirstNameText, Data["FirstName"].ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserLastNameText, Data["LastName"].ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserEmailText, Data["Email"].ToString());
            ActiveBrowser.Actions.SetText(Pages.PS_InviteCreateNewUserPage.AddUserUserNameText, Data["UserName"].ToString());
            Pages.PS_InviteCreateNewUserPage.AddUserInviteBtn.Click();
            ActiveBrowser.WaitUntilReady();            
        }
    
    
        [CodedStep(@"Verify search result list")]
        public void TST_SCH_002_C18478_VerifyResult()
        {
            //Search for all types of created objects with same name
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Manager.Desktop.KeyBoard.TypeText(Data["ProjectName"].ToString());
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000); 
            
            // Verify project is searched
            ActiveBrowser.RefreshDomTree();            
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("quick_search_project_result_list"), Data["ProjectName"].ToString())).Count > 0);
            
            // Verify user is searched    
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("quick_search_people_result_list"), Data["ProjectName"].ToString())).Count > 0);
            
            // Verify organization is not searched     
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("quick_search_organization_result_list"), Data["ProjectName"].ToString())).Count == 0);          
        }
    }
}
