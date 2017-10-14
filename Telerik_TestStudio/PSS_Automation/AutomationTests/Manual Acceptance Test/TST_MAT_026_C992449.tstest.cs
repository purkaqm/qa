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

    public class TST_MAT_026_C992449 : BaseWebAiiTest
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
        
        string descendentProjectName;
    
        [CodedStep(@"Wait for user to be navigated to work summary page")]
        public void TST_MAT_026_C992449_WaitSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Summary"),"page tiltle should contain 'Summary' text");
                                                                                                               
        }
        
        [CodedStep(@"Store Url of Project")]
        public void TST_MAT_026_C992449_SetProjectUrl()
        {
            SetExtractedValue("ProjectUrl",ActiveBrowser.Url);
        }
    
        [CodedStep(@"Click on 'Add New' link to add Descendent work")]
        public void TST_MAT_026_C992449_ClickAddNewLink()
        {
            Pages.PS_AddToFavoritesPopup.DescendantsAddNewLink.Click();
            
        }
        
        [CodedStep(@"Add Descendant Work")]
        public void TST_MAT_026_C992449_CreateDescendantWork()
        {
            //Select work type
            string workType = Data["DescendantProjectWorkType"].ToString();
                     
            
            IList<HtmlOption> options = Pages.PS_CreateNewProjectPage.AddProjWorkTypeSelect.Options;
            bool foundflag = false;
            foreach(HtmlOption option in options){
                if(option.BaseElement.InnerText.Trim().Equals(workType)){
                    foundflag = true;
                    Pages.PS_CreateNewProjectPage.AddProjWorkTypeSelect.SelectByValue(option.Value.ToString());
                    break;
                }
            }
            Assert.IsTrue(foundflag, workType + " is not present in dropdown");
            
            //Enter Work Type Description
            string workTypeDesc = Data["DescendantProjectDescription"].ToString();
            if(workTypeDesc.Length > 0) {
                Pages.PS_CreateNewProjectPage.AddProjWorkDescriptionDiv.SetValue("value",workTypeDesc);
            }
            
            //Click Continue button
            Pages.PS_CreateNewProjectPage.AddProjContinueBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_CreateNewProjectPage.ProjectNameInput.Wait.ForVisible();
            
            //Enter Work Name
            descendentProjectName = Data["DescendantProjectName"].ToString()+Randomizers.generateRandomInt(100,999);
            Actions.SetText(Pages.PS_CreateNewProjectPage.ProjectNameInput,descendentProjectName);
            
            //Click Finish and Create! button
            Pages.PS_CreateNewProjectPage.FinishAndCreateBtn.Focus();
            Pages.PS_CreateNewProjectPage.FinishAndCreateBtn.ScrollToVisible();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.MouseHover();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Descendent work is added")]
        public void TST_MAT_026_C992449_VerifyDescendentWork()
        {
            System.Threading.Thread.Sleep(3*1000);
            ActiveBrowser.RefreshDomTree();
            Log.WriteLine(string.Format("//div[@id='DescendantsGridId']//a[contains(.,'{0}')]",descendentProjectName));
            HtmlAnchor workLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//div[@id='DescendantsGridId']//a[contains(.,'{0}')]",descendentProjectName));
            workLink.Wait.ForExists();
            Assert.IsTrue(workLink.IsVisible(),"Created descendant work link should be visible");
            workLink.Click();
        }
    
    
        [CodedStep(@"Get project URL and open project summary page")]
        public void TST_MAT_026_C992449_GetProjectUrlAndOpenSummaryPage()
        {
            ActiveBrowser.NavigateTo(GetExtractedValue("ProjectUrl").ToString());
            ActiveBrowser.WaitUntilReady();
        }
    }
}
