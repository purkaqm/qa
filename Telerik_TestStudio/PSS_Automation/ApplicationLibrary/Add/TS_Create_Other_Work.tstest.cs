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

    public class TS_Create_Other_Work : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep()
        {
            string workType = Data["WorkType"].ToString();
                     
            
            IList<HtmlOption> options = Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.Options;
            bool foundflag = false;
            foreach(HtmlOption option in options){
                if(option.BaseElement.InnerText.Trim().Equals(workType)){
                    foundflag = true;
                    Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.SelectByValue(option.Value.ToString());
                    break;
                }
            }
            Assert.IsTrue(foundflag, workType + " is not present in dropdown");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep1()
        {
            string workTypeDesc = Data["WorkDescription"].ToString();
            if(workTypeDesc.Length > 0) {
                Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkDescriptionDiv.SetValue("value",workTypeDesc);
                //Actions.SetText(Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkDescriptionDiv,workTypeDesc);
            }
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep2()
        {
            Pages.PS_CreateOtherWorkPage.AddOtherWorkContinueBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_CreateOtherWorkPage.WorkNameInput.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep3()
        {
            string workName = Data["WorkName"].ToString()+Randomizers.generateRandomInt(100,999);
            Actions.SetText(Pages.PS_CreateOtherWorkPage.WorkNameInput,workName);
            SetExtractedValue("CreatedWorkName", workName);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep4()
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
                Actions.Click(resDiv.BaseElement);                
                //resDiv.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep5()
        {
            //the code for selecting people will be here....
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Create_Other_Work_CodedStep6()
        {
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.Focus();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.MouseHover();
            Pages.PS_CreateOtherWorkPage.FinishAndCreateBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
