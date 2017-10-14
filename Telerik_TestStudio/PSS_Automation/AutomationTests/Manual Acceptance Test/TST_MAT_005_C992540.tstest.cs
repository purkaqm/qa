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

    public class TST_MAT_005_C992540 : BaseWebAiiTest
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
        public void TST_MAT_005_C992540_ClickAdminLink()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Click on Add New button")]
        public void TST_MAT_005_C992540_ClickAddNewBtn()
        {
           Pages.PS_CustomFieldsPage.AddNewBtn.Wait.ForExists();
           Pages.PS_CustomFieldsPage.AddNewBtn.Click(); 
        }
    
        [CodedStep(@"Verify Custom Fields is added")]
        public void TST_MAT_005_C992540_VerifyCustomFieldRow()
        {
            HtmlTableRow customFieldRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(string.Format(AppLocators.get("created_custom_field_row"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldRow.Wait.ForExists();
            Assert.IsTrue(customFieldRow.IsVisible());
            Log.WriteLine(GetExtractedValue("GeneratedCustomFieldName").ToString());
        }
    
        [CodedStep(@"Click on Project tab")]
        public void TST_MAT_005_C992540_ClickProjectLink()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_005_C992540_SummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
                                                               
        }
    
        [CodedStep(@"Verify Custom field is associate with project")]
        public void TST_MAT_005_C992540_VerifyCustomField()
        {
            if(!Pages.PS_ProjectSummaryPage.ProjectSummaryDetailsDiv.IsVisible())
            {
                Pages.PS_ProjectSummaryPage.DetailsLink.Click();
            }
            HtmlTableCell customFieldCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format("//th[contains(.,'{0}')]",GetExtractedValue("GeneratedCustomFieldName").ToString()));
            customFieldCell.Wait.ForExists();
            Assert.IsTrue(customFieldCell.IsVisible(),"Custom field shold be present");
        }

    
        [CodedStep(@"Delete created custom tag")]
        public void TST_MAT_005_C992540_DeleteCustomField()
        {
                        HtmlImage delImageIcon = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("delete_custom_field_image"),GetExtractedValue("GeneratedCustomFieldName").ToString()));
                        delImageIcon.Wait.ForVisible();
                        delImageIcon.Click();
                        
                        Pages.PS_CustomFieldsPage.DeleteCustomFieldYesButton.Click();
                        ActiveBrowser.WaitUntilReady();          
        }
    }
}
