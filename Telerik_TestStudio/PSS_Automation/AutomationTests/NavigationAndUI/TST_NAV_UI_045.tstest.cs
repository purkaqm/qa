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

    public class TST_NAV_UI_045 : BaseWebAiiTest
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
        public void TST_NAV_UI_045_CodedStep()
        {
            SetExtractedValue("UserSuffix","1");
        }
    
        //[CodedStep(@"Click on Project link present in icon bar")]
        //public void TST_NAV_UI_045_CodedStep1()
        //{
            //Pages.PS_HomePage.ProjectLeftNavLink.Click();
        //}
    
        [CodedStep(@"Verify Project menu is opened")]
        public void TST_NAV_UI_045_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_WorkTreePage.WorkTreeContainerDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Work Tree");
            Assert.IsTrue(Pages.PS_WorkTreePage.WorkTreeContainerDiv.IsVisible(),"Work Tree Page should be displayed when no project visited previously");
            
        }
    
        [CodedStep(@"Click Project Link Left Panel")]
        public void TST_NAV_UI_045_CodedStep1()
        {
            // Desktop command: LeftClick on ProjectLeftNavLink
            Pages.PS_HomePage.ProjectLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.ProjectLeftNavLink.Focus();
            Pages.PS_HomePage.ProjectLeftNavLink.MouseHover();
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait for Left Navigation Panel to Visible.")]
        public void TST_NAV_UI_045_CodedStep3()
        {
            // Wait for element 'NavPanelTitleDiv' 'is' visible.
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            
        }
    }
}
