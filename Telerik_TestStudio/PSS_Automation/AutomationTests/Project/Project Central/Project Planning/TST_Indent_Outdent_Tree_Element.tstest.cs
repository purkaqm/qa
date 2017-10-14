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

    public class TST_Indent_Outdent_Tree_Element : BaseWebAiiTest
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
        public void TS_Indent_Outdent_Tree_Element_CodedStep()
        {
            SetExtractedValue("Name",Data["Name"].ToString());
        }
    
        [CodedStep(@"Click on Down Arrow to Indent Tree Element")]
        public void TS_Indent_Outdent_Tree_Element_CodedStep1()
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
            
            //Click Indent Icon in Context Menu
            Pages.PS_ProjectPlanningLayoutPage.IndentElementIconImg.Wait.ForVisible();
            Pages.PS_ProjectPlanningLayoutPage.IndentElementIconImg.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
            // expand previous node further indent verification 
            string prevProjNodeLoc = string.Format(AppLocators.get("proj_plan_prev_node_div"), containerID,level,(projectIndex-1));
            HtmlDiv prevProjNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(prevProjNodeLoc);
            string prevClassAttr = prevProjNode.Attributes.Single(x => x.Name == "class").Value;
            if(prevClassAttr.Contains("dijitTreeExpandoClosed")){
             prevProjNode.Click();          
             this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");   
            }
         
            ActiveBrowser.RefreshDomTree();            
            string nextTreeLoc = string.Format(AppLocators.get("proj_plan_prev_node_div")+"/following-sibling::div[@class='treeChildrenContent']",containerID,level,(projectIndex-1));
            Log.WriteLine("next tree : " + nextTreeLoc);
            HtmlDiv nextTreeNode = ActiveBrowser.Find.ByXPath<HtmlDiv>(nextTreeLoc);
            string newContainerID = nextTreeNode.Attributes.Single(x => x.Name == "containerid").Value;
            Log.WriteLine("New Container : " + newContainerID);
            SetExtractedValue("newContainerID",newContainerID);
            SetExtractedValue("newLevel",(level+1));
            SetExtractedValue("lastChild",lastChild);
            
            
     
        }
     
    
        [CodedStep(@"New Coded Step")]
        public void TS_Indent_Outdent_Tree_Element_CodedStep2()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string newContainerID = GetExtractedValue("newContainerID").ToString();
             string newLevel = GetExtractedValue("newLevel").ToString();
             string lastChild = GetExtractedValue("lastChild").ToString(); 
             string expectedNodeLoc = string.Format(AppLocators.get("proj_plan_intended_node_div"),newContainerID, newLevel,lastChild); 
             Assert.AreEqual(true,ActiveBrowser.Find.AllByXPath(expectedNodeLoc).Count>0, "Node not Indented Successfully");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Indent_Outdent_Tree_Element_CodedStep3()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string newContainerID = GetExtractedValue("newContainerID").ToString();
             string newLevel = GetExtractedValue("newLevel").ToString();
             string lastChild = GetExtractedValue("lastChild").ToString();
             int projectIndex = 1;
            
             IList<Element> projects = ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("proj_plan_node_list_div"),newContainerID,newLevel));
                for(int i=1 ; i <= projects.Count; i++){
                    if(projects[i-1].InnerText.Contains(lastChild)){
                        projectIndex = i;
                        break;   
                    }
              }
            
            
            
            
            //find and click down arrow for given tree node
            string downArrowLoc = string.Format(AppLocators.get("proj_plan_down_arrow_icon_img"), newContainerID,newLevel,projectIndex);
            HtmlImage downArrowImg = ActiveBrowser.Find.ByXPath<HtmlImage>(downArrowLoc);
            downArrowImg.Wait.ForVisible();
            downArrowImg.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            
            //Click Outdent Icon in Context Menu
            Pages.PS_ProjectPlanningLayoutPage.OutdentElementIconImg.Wait.ForVisible();
            Pages.PS_ProjectPlanningLayoutPage.OutdentElementIconImg.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                
            if(ActiveBrowser.Find.AllByXPath<HtmlInputButton>(AppLocators.get("proj_plan_save_and_cont_popup_btn")).Count > 0 ){
                
                HtmlInputButton save_cont_btn = ActiveBrowser.Find.ByXPath<HtmlInputButton>(AppLocators.get("proj_plan_save_and_cont_popup_btn"));
                save_cont_btn.Click();
                this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
             }   
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Indent_Outdent_Tree_Element_CodedStep4()
        {
             ActiveBrowser.RefreshDomTree();
             ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
             string containerID = Data["containerID"].ToString();
             string workTree = Data["Name"].ToString();
             string[] workTreeArray = System.Text.RegularExpressions.Regex.Split(workTree, "---");
             int level = workTreeArray.Length ;
             string lastChild = workTreeArray[level-1];
             string expectedNodeLoc = string.Format(AppLocators.get("proj_plan_intended_node_div"),containerID, level,lastChild); 
             Assert.AreEqual(true,ActiveBrowser.Find.AllByXPath(expectedNodeLoc).Count>0, "Node not Outdented Successfully");
        }
    
       
    }
}
