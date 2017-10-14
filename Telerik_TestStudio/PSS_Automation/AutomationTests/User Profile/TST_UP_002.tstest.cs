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

    public class TST_UP_002 : BaseWebAiiTest
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
    
        [CodedStep(@"Enter user name in Name Field")]
        public void TST_UP_002_CS01()
        {
            string userName = Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString();            
            Actions.SetText(Pages.PS_ReviewFindAPerson.NameSearchInputTxt,userName);            
        }
    
        [CodedStep(@"Click on Search button")]
        public void TST_UP_002_CS02()
        {
            Pages.PS_ReviewFindAPerson.SearchBtn.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on User name link")]
        public void TST_UP_002_CS03()
        {
            HtmlDiv userNameLoc =  ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("user_name_link"),"Pratik"));         
            userNameLoc.Wait.ForExists();
            ActiveBrowser.Window.SetFocus();
            userNameLoc.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();          
        }
    
        [CodedStep(@"Verify User Details popup is shown")]
        public void TST_UP_002_CS04()
        {
            Pages.PS_UserDetailsPopUp.UserDetailsPopupDiv.Wait.ForExists();
            Pages.PS_UserDetailsPopUp.UserDetailsMoreLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserDetailsPopUp.UserDetailsPopupDiv.IsVisible(),"User details popup shoud be present");
            Assert.IsTrue(Pages.PS_UserDetailsPopUp.UserDetailsMoreLink.IsVisible(),"More link should be present in popup");
        }
    
        [CodedStep(@"Click on More... link ")]
        public void TST_UP_002_CS05()
        {
            Pages.PS_UserDetailsPopUp.UserDetailsMoreLink.Click();
            ActiveBrowser.WaitUntilReady();
            System .Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify User Profile page is displayed")]
        public void TST_UP_002_CodedStep()
        {
            string pageTitleName = string.Format("{0}: Profile: User Profile",Pages.PS_HomePage.FirstNameLastNameDiv.BaseElement.GetAttribute("title").Value.ToString());
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,pageTitleName);
            Pages.PS_UserProfilePage.DocumentsLink.Wait.ForExists();
            Pages.PS_UserProfilePage.EditUserLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserProfilePage.DocumentsLink.IsVisible(),"Document link should be present");
            Assert.IsTrue(Pages.PS_UserProfilePage.EditUserLink.IsVisible(),"EditUser link should be present");
        }
    }
}
