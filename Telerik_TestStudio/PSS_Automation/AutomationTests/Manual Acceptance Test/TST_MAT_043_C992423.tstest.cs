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

    public class TST_MAT_043_C992423 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin icon in left navigation menu")]
        public void TST_MAT_043_C992423_ClickAdminIcon()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
        }
    
        [CodedStep(@"Verify Permission page is displayed correctly")]
        public void TST_MAT_043_C992423_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Define Permissions"),"Page title should be Define Permissions");
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.UpdateBtn.IsVisible(),"Update button should be visible");
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.CancelBtn.IsVisible(),"Cancel button should be visible");
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.PermissionsDropDownList.IsVisible(),"One dropdown select should be visible");
        }
    }
}
