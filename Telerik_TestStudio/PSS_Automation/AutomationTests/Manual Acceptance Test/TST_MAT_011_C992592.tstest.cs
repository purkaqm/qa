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

    public class TST_MAT_011_C992592 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_MAT_011_C992592_ClickAdminLink()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Verify Grid with tags is displayed")]
        public void TST_MAT_011_C992592_VerifyGrid()
        {
            Pages.PS_TagsListPage.AddNewTagBtn.Wait.ForExists();
            Assert.IsTrue(Pages.PS_CustomFieldsPage.GridDiv.IsVisible(),"Grid should be visible in tag page"); 
        }
    
        [CodedStep(@"Click on Dependencies tab")]
        public void TST_MAT_011_C992592_ClickDepTab()
        {
            Pages.PS_TagsListPage.DepedenciesLink.Wait.ForExists();
            Pages.PS_TagsListPage.DepedenciesLink.Click();
        }
        
        [CodedStep(@"Verify Grid with Dependencies is displayed")]
        public void TST_MAT_011_C992592_VerifyDepGrid()
        {
            Pages.PS_TagDependenciesPage.AddNewButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_TagDependenciesPage.AddNewButton.IsVisible(),"Add New button should be vissible");
            Assert.IsTrue(Pages.PS_CustomFieldsPage.GridDiv.IsVisible(),"Grid should be visible in dependencies page"); 
            Assert.IsTrue(Pages.PS_TagDependenciesPage.AllFieldsTable.IsVisible(),"Table with all dependencies should be displayed");
        }
    }
}
