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

    public class TST_MAT_084_C992802 : BaseWebAiiTest
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
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_084_C992802_WaitProjectSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                       
        }
    
    
        [CodedStep(@"Delete the user permanently through deleted_user.jsp page")]
        public void TST_MAT_084_C992802_DeleteWorkPermanently()
        {
            HtmlInputCheckBox workCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//div[@class='deleted'][contains(.,'{0}')]/input",GetExtractedValue("CreatedProjectName").ToString()));
            workCheckbox.Wait.ForExists();
            workCheckbox.ScrollToVisible();
            workCheckbox.Check(true);
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Click();
            Pages.PS_Deleted_Users_Page.PermanentlyDeleteUserProgressContainer.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForVisible();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Click();
        }
    }
}
