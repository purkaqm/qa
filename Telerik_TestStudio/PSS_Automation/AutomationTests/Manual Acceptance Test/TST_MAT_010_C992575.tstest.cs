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

    public class TST_MAT_010_C992575 : BaseWebAiiTest
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
        public void TST_MAT_008_C992575_ClickAdminLink()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Verify Grid with tags is displayed")]
        public void TST_MAT_008_C992575_VerifyGrid()
        {
            Pages.PS_TagsListPage.AddNewTagBtn.Wait.ForExists();
            Assert.IsTrue(Pages.PS_CustomFieldsPage.GridDiv.IsVisible(),"Grid should be visible in tag page"); 
        }
    
        [CodedStep(@"Click on Add new tag button")]
        public void TST_MAT_008_C992575_ClickAddNewTag()
        {
            Pages.PS_TagsListPage.AddNewTagBtn.Click();
        }
    
        [CodedStep(@"Verify new tag is created")]
        public void TST_MAT_008_C992575_VerifyNewTag()
        {
            HtmlTableRow tagRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_tag_row"),GetExtractedValue("CustTagName").ToString()));
            tagRow.Wait.ForExists();
            tagRow.IsVisible();
        }
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_008_C992575_SummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
                                                                           
        }
    
        [CodedStep(@"Verify Tag is associate with project")]
        public void TST_MAT_008_C992575_VerifyTagWithProject()
        {
            if(!Pages.PS_ProjectSummaryPage.ProjectSummaryDetailsDiv.IsVisible())
            {
                Pages.PS_ProjectSummaryPage.DetailsLink.Click();
            }
            HtmlTableCell tagCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format("//th[contains(.,'{0}')]",GetExtractedValue("CustTagName").ToString()));
            tagCell.Wait.ForExists();
            Assert.IsTrue(tagCell.IsVisible(),"Tag field shold be present");
        }
    
        [CodedStep(@"Delete created tag")]
        public void TST_MAT_008_C992575_DeleteTag()
        {
            HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_tag_image"),GetExtractedValue("CustTagName").ToString()));
            delImageIcon.Wait.ForVisible();
            delImageIcon.Click();
            System.Threading.Thread.Sleep(2000);
            
            Pages.PS_TagsListPage.DeleteTagYesButton.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_TagsListPage.AddNewTagBtn.Wait.ForVisible();
        }
    }
}
