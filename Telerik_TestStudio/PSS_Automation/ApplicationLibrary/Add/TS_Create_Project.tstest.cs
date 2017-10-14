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

    public class TS_Create_Project : BaseWebAiiTest
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
    
        [CodedStep(@"Select Work Type")]
        public void TS_Create_Project_CodedStep()
        {
            string workType = Data["ProjectWorkType"].ToString();
                     
            
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
            
        }
    
        [CodedStep(@"Enter Work Type Description")]
        public void TS_Create_Project_CodedStep1()
        {
            string workTypeDesc = Data["ProjectDescription"].ToString();
            if(workTypeDesc.Length > 0) {
                Pages.PS_CreateNewProjectPage.AddProjWorkDescriptionDiv.SetValue("value",workTypeDesc);
                //Actions.SetText(Pages.PS_CreateNewProjectPage.AddProjWorkDescriptionDiv,workTypeDesc);
            }
            
        }
    
        [CodedStep(@"Click Continue button")]
        public void TS_Create_Project_CodedStep2()
        {
            Pages.PS_CreateNewProjectPage.AddProjContinueBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_CreateNewProjectPage.ProjectNameInput.Wait.ForVisible();
        }
    
        [CodedStep(@"Enter Work Name")]
        public void TS_Create_Project_CodedStep3()
        {
            string projectName = Data["ProjectName"].ToString()+Randomizers.generateRandomInt(100,999);
            Actions.SetText(Pages.PS_CreateNewProjectPage.ProjectNameInput,projectName);
            SetExtractedValue("CreatedProjectName", projectName);
        }
    
        [CodedStep(@"Select Location in Work Tree")]
        public void TS_Create_Project_CodedStep4()
        {
            Pages.PS_CreateOtherWorkPage.LocationInWorkTreeSpan.Click();
            Pages.PS_CreateOtherWorkPage.LocationPopupTitleSpan.Wait.ForVisible();
            if(Data["LocationType"].ToString().Contains("Search")){
                Pages.PS_CreateOtherWorkPage.LocationPopupSearchTab.Click();
                Pages.PS_CreateOtherWorkPage.LocationPopUpFindInput.Wait.ForVisible();
                Actions.SetText(Pages.PS_CreateOtherWorkPage.LocationPopUpFindInput,Data["LocationPath"].ToString());
                Pages.PS_CreateOtherWorkPage.LocationPopupGoBtn.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("create_other_work_location_search_result_div"),Data["LocationPath"].ToString()));
                resDiv.Wait.ForVisible();
                resDiv.Focus();
                resDiv.Click();
            }
        }
    
        [CodedStep(@"Select People for the work")]
        public void TS_Create_Project_CodedStep5()
        {
            /// \bug Empty code
            //the code for selecting people will be here....
        }
    
        [CodedStep(@"Click Finish and Create! button")]
        public void TS_Create_Project_CodedStep6()
        {
            Pages.PS_CreateNewProjectPage.FinishAndCreateBtn.Focus();
            Pages.PS_CreateNewProjectPage.FinishAndCreateBtn.ScrollToVisible();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.Focus();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        
    }
}
