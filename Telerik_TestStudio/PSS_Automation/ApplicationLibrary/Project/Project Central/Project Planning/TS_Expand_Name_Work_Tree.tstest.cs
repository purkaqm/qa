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

    public class TS_Expand_Name_Work_Tree : BaseWebAiiTest
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
    
        [CodedStep(@"Click 'Change_Dates'")]
        public void Expand_Name_Work_Tree_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            //string workTree = Data["Name"].ToString();
            string workTree = GetExtractedValue("Name").ToString();
            string containerID;
            string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
            int level = workTreeArray.Length ;
            if(level == 1){
                    HtmlDiv nextTreeNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("proj_plan_first_level_tree_div"));
                    containerID = nextTreeNode.Attributes.Single(x => x.Name == "containerid").Value;
                    Log.WriteLine("Container : " + containerID);
                    SetExtractedValue("containerID",containerID);
            }
            for(int i = 0 ; i < level - 1 ; i++){
                string parentNodeLoc = string.Format(AppLocators.get("proj_plan_tree_parent_node_div"),(i+1), workTreeArray[i]);
                Assert.AreEqual(true, ActiveBrowser.Find.AllByXPath<HtmlDiv>(parentNodeLoc).Count > 0, workTreeArray[i] + "Is not Present in Work Tree");
                HtmlDiv parentNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(parentNodeLoc);
                if(parentNode.Attributes.Single(x => x.Name == "class").Value.Contains("dijitTreeExpandoClosed")){
                    parentNode.Click();
                    this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                    ActiveBrowser.RefreshDomTree();
                }
                if(i == level - 2){
                    string nextTreeLoc = string.Format(AppLocators.get("proj_plan_next_tree_div"),(i+1), workTreeArray[i]);
                    HtmlDiv nextTreeNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(nextTreeLoc);
                    containerID = nextTreeNode.Attributes.Single(x => x.Name == "containerid").Value;
                    Log.WriteLine("Container : " + containerID);
                    SetExtractedValue("containerID",containerID);
                }
              
            }
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
            
            
     
        }
    }
}
