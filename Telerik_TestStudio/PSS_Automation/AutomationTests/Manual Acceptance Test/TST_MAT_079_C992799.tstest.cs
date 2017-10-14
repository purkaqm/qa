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

    public class TST_MAT_079_C992799 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify page is displayed correctly")]
        public void TST_MAT_079_C99279_VerifyElements()
        {
            Assert.IsTrue(Pages.PS_AdminModulesPage.ModulesListContainerTable.IsVisible());
            Assert.IsTrue(Pages.PS_AdminModulesPage.ModuleTypeTableCell.IsVisible());
            Assert.IsTrue(ActiveBrowser.Url.Contains("modules.jsp"));
        }
    
        [CodedStep(@"Verify object type is present and click that object type ")]
        public void TST_MAT_079_C99279_VeriftObjectType()
        {
            ActiveBrowser.RefreshDomTree();
            string objectType = Data["ObjectType"].ToString();
            HtmlAnchor customProjectLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//a[contains(.,'{0}')]",objectType));
            customProjectLink.Wait.ForExists();
            customProjectLink.Click();
                                                
        }
    
        [CodedStep(@"Verify page navigate to module_wizard.jsp page")]
        public void TST_MAT_079_C99279_VerifyPageUrl()
        {
            Assert.IsTrue(ActiveBrowser.Url.Contains("module_wizard.jsp"));
        }
    
        [CodedStep(@"Check the 'Document' checkbox")]
        public void TST_MAT_079_C99279_CheckDocument()
        {
            Pages.PS_ModuleWizardPage.DocumentsViewCheckBox.Check(true);
            Pages.PS_ModuleWizardPage.OptionsSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Navigate to Home page")]
        public void TST_MAT_079_C99279_NavigateToHomePage()
        {
            ActiveBrowser.NavigateTo("~/Home.page");
        }
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_079_C99279_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                       
        }
    
        [CodedStep(@"Store Url of Project")]
        public void TST_MAT_079_C99279_StorePageUrl()
        {
            SetExtractedValue("ProjectUrl",ActiveBrowser.Url);
        }

    
        [CodedStep(@"Verify 'Documents' link is displayed")]
        public void TST_MAT_075_C992796_VerifyDocumentsLink()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ProjectSummaryPage.DocumentsLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.DocumentsLink.IsVisible(),"Documents link should be visible");
        }
    
        [CodedStep(@"Unckeck the Documents checkbox")]
        public void TST_MAT_079_C992799_UncheckDocumentsCheckbox()
        {
            Pages.PS_ModuleWizardPage.DocumentsViewCheckBox.Wait.ForExists();
            Pages.PS_ModuleWizardPage.DocumentsViewCheckBox.Wait.ForVisible();
            if(Pages.PS_ModuleWizardPage.DocumentsViewCheckBox.Checked)
            {
                Pages.PS_ModuleWizardPage.DocumentsViewCheckBox.Click();
                Pages.PS_ModuleWizardPage.OptionsSubmitLink.Click();
                ActiveBrowser.WaitUntilReady();   
            }
        }
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MAT_079_C992799_OpenSummaryPage()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectUrl").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    }
}
