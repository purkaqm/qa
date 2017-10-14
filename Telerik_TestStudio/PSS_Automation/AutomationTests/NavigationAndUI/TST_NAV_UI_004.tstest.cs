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

    public class TST_NAV_UI_004 : BaseWebAiiTest
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
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep()
        {
            Pages.PS_HomePage.HomeLeftNavLink.MouseHover();
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HOME");
            float homeHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Home']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (homeHeight+20) && notchHeight > (homeHeight-20), "Icon bar notch pointer should point to 'Home'");
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep1()
        {
            Pages.PS_HomePage.InboxLeftNavLink.MouseHover();
            Pages.PS_HomePage.InboxLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"INBOX");
            float inboxHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Inbox']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (inboxHeight+20) && notchHeight > (inboxHeight-20), "Icon bar notch pointer should point to 'Inbox'");
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep2()
        {
            Pages.PS_HomePage.AddLeftNavLink.MouseHover();
            Pages.PS_HomePage.AddLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADD");
            float addHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Add']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (addHeight+20) && notchHeight > (addHeight-20), "Icon bar notch pointer should point to 'Add'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep3()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.MouseHover();
            Pages.PS_HomePage.ReviewLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"REVIEW");
            float reviewHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Review']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (reviewHeight+20) && notchHeight > (reviewHeight-20), "Icon bar notch pointer should point to 'Review'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep4()
        {
            Pages.PS_HomePage.AdminLeftNavLink.MouseHover();
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"ADMIN");
            float adminHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Admin']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (adminHeight+20) && notchHeight > (adminHeight-20), "Icon bar notch pointer should point to 'Admin'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep5()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"PROJECT");
            float projectHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Project']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (projectHeight+20) && notchHeight > (projectHeight-20), "Icon bar notch pointer should point to 'Project'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep6()
        {
            Pages.PS_HomePage.FavoritesLeftNavLink.MouseHover();
            Pages.PS_HomePage.FavoritesLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"FAVORITES");
            float favoritesHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Favorites']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (favoritesHeight+20) && notchHeight > (favoritesHeight-20), "Icon bar notch pointer should point to 'Favorites'");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep7()
        {
            Pages.PS_HomePage.HistoryLeftNavLink.MouseHover();
            Pages.PS_HomePage.HistoryLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"HISTORY");
            float historyHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='History']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (historyHeight+20) && notchHeight > (historyHeight-20), "Icon bar notch pointer should point to 'History'");
            
        }
    
        [CodedStep(@"Click on icon bar element and verify sub menu notch pointer position for 'History'")]
        public void TST_Verify_Icon_Bar_Notch_Pointer_Position_CodedStep8()
        {
            Pages.PS_HomePage.ImportantLinksLeftNavLink.MouseHover();
            Pages.PS_HomePage.ImportantLinksLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(2000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"IMPORTANT LINKS");
            float impLinksHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Important Links']\").getBoundingClientRect().top");
            float notchHeight =  Actions.InvokeScript<float>("document.querySelector(\"div#notch\").getBoundingClientRect().top");
            Assert.IsTrue( notchHeight < (impLinksHeight+20) && notchHeight > (impLinksHeight-20), "Icon bar notch pointer should point to 'Important Links'");
            
        }
    }
}
