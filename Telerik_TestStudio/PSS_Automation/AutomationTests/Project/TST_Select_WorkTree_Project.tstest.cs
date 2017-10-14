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

    public class TST_Select_WorkTree_Project : BaseWebAiiTest
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
        public void TST_Select_WorkTree_Project_CodedStep()
        {
                       
            string workTree = Data["Name"].ToString();
            string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
            int level = workTreeArray.Length ;
            
            for(int i = 0 ; i < level - 1 ; i++){
                this.ExecuteTest("ApplicationLibrary\\Wait_For_Small_Loader_Image_To_Disappear.tstest");
                ActiveBrowser.RefreshDomTree();
                string parentNodeLoc = string.Format(AppLocators.get("proj_work_tree_node_div"), workTreeArray[i]);
                Assert.AreEqual(true, ActiveBrowser.Find.AllByXPath<HtmlDiv>(parentNodeLoc).Count > 0, workTreeArray[i] + " Is not Present in Work Tree");
                HtmlDiv parentNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(parentNodeLoc);
                if(parentNode.Find.AllByXPath<HtmlSpan>(AppLocators.get("proj_work_tree_node_plus_icon")).Count > 0){
                    HtmlSpan plusIcon = parentNode.Find.ByXPath<HtmlSpan>(AppLocators.get("proj_work_tree_node_plus_icon"));
                    plusIcon.Click();
                    this.ExecuteTest("ApplicationLibrary\\Wait_For_Small_Loader_Image_To_Disappear.tstest");
                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                    ActiveBrowser.RefreshDomTree();              
                }
                if(i == level - 2){
                   break;
                }
             
             }
            
             string workNodeLoc = string.Format(AppLocators.get("proj_work_tree_node_div")+"//a", workTreeArray[level-1]);
             HtmlAnchor workNode = ActiveBrowser.Find.ByXPath<HtmlAnchor>(workNodeLoc);
             workNode.Click();
             ActiveBrowser.WaitUntilReady();
             SetExtractedValue("WorkNodeName",workTreeArray[level-1]);
         }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Select_WorkTree_Project_CodedStep1()
        {
            Pages.PS_HomePage.PageTitleDiv.BaseElement.Wait.ForCondition((a_0, a_1) => ArtOfTest.Common.CompareUtils.StringCompare(a_0.TextContent, GetExtractedValue("WorkNodeName").ToString(), ArtOfTest.Common.StringCompareType.Contains), false, null, Manager.Settings.ElementWaitTimeout);
        }
    }
}
