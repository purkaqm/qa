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

    public class TST_Add_Under : BaseWebAiiTest
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
        public void TS_Add_Under_CodedStep()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Add_Under_CodedStep1()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string workTree = Data["Name"].ToString();
             string containerID = Data["containerID"].ToString();
             string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
             int level = workTreeArray.Length ;
             string lastChild = workTreeArray[level-1];
             int projectIndex = 1;
            
            IList<Element> projects = ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("proj_plan_node_list_div"),containerID,level));
            for(int i=1 ; i <= projects.Count; i++){
                 if(projects[i-1].InnerText.Contains(lastChild)){
                       projectIndex = i;
                       break;   
                 }
            }
            
            
            
            
            //find and click down arrow for given tree node
            string downArrowLoc = string.Format(AppLocators.get("proj_plan_down_arrow_icon_img"), containerID,level,projectIndex);
            HtmlImage downArrowImg = ActiveBrowser.Find.ByXPath<HtmlImage>(downArrowLoc);
            downArrowImg.Wait.ForVisible();
            downArrowImg.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            
            //Click Add Under Icon in Context Menu
            Pages.PS_ProjectPlanningLayoutPage.AddUnderElementIconImg.Wait.ForVisible();
            Pages.PS_ProjectPlanningLayoutPage.AddUnderElementIconImg.Click(false);
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Add_Under_CodedStep2()
        {
            Pages.PS_ProjectPlanningLayoutPage.AddUnderSubmitBtn.Wait.ForExists(30000);
            Assert.AreEqual(true, Pages.PS_ProjectPlanningLayoutPage.AddUnderSubmitBtn.IsVisible());
            
        }
    
       
        [CodedStep(@"New Coded Step")]
        public void TS_Add_Under_CodedStep3()
        {
            Random gen = new Random();
            int addUnderPostfix = 10000; 
            string addUnderPostfixStr = gen.Next(addUnderPostfix).ToString();
            string addUnderNameStr = Data["SubNamePrefix"].ToString() + addUnderPostfixStr;
            string addUnderTypeStr = Data["SubNameType"].ToString();
            
            ActiveBrowser.Actions.SetText(Pages.PS_ProjectPlanningLayoutPage.AddUnderTypeInputBox.BaseElement, addUnderTypeStr);
            ActiveBrowser.Actions.SetText(Pages.PS_ProjectPlanningLayoutPage.AddUnderNameInputBox.BaseElement, addUnderNameStr);
            
            Pages.PS_ProjectPlanningLayoutPage.AddUnderSubmitBtn.Click();
            Log.WriteLine("Newly Added Node : " + addUnderNameStr);
            SetExtractedValue("Name",Data["Name"].ToString() + "---" + addUnderNameStr);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
        }
    
      
    
        [CodedStep(@"New Coded Step")]
        public void TS_Add_Under_CodedStep4()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string workTree = GetExtractedValue("Name").ToString();
             string containerID = Data["containerID"].ToString();
             string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
             int level = workTreeArray.Length ;
             string lastChild = workTreeArray[level-1];
             int flag = ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("proj_plan_intended_node_div"),containerID,level,lastChild)).Count;
             Assert.AreEqual(true , flag > 0,"Add Under Operation Failed");
            
            
        }
    }
}
