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
using ArtOfTest.WebAii.Win32.Dialogs;

namespace PSS_Automation
{

    public class TST_UCO_005 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Admin left navigation tab is present")]
        public void TST_UCO_005_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.AdminLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_UCO_005_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that in left navigation title 'admin' is displayed")]
        public void TST_UCO_005_CS03()
        {
            Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.BaseElement.InnerText.Contains("ADMIN"));
        }
    
        [CodedStep(@"Make any changes on 'user creation option' page")]
        public void TST_UCO_005_CS04()
        {
            Pages.PS_UserCreationOptionsPage.EnforceTagsAndCustomFieldsInvtiteUserChkbox.Click();
            ActiveBrowser.RefreshDomTree();
        }
        
        [CodedStep(@"Close the active page without saving changes")]
        public void TST_UCO_005_CS05()
        {
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            
        }
    }
}
