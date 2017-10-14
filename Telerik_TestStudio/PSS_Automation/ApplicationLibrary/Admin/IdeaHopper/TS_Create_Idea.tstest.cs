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

    public class TS_Create_Idea : BaseWebAiiTest
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
        
        string ideaHopperName;
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TS_CS_001()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            SetExtractedValue("LocationValue",Data["LocationPath"].ToString());
        }
    
        [CodedStep(@"Click on Addnew Button")]
        public void TS_CS_002()
        {
            Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_AddIdeaHopperPage.NameText.Wait.ForExists();
        }
    
        [CodedStep(@"Verify User is directed to Add Idea Hopper page")]
        public void TS_CS_003()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
                                                                                                            
        }
    
        [CodedStep(@"Enter Name")]
        public void TS_CS_004()
        {
            ideaHopperName = Data["TestIdea"].ToString() + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_AddIdeaHopperPage.NameText,ideaHopperName);
            SetExtractedValue("IdeaHopperName",ideaHopperName);
        }
    
        [CodedStep(@"Select Type")]
        public void TS_CS_005()
        {
            Pages.PS_AddIdeaHopperPage.TypeSelect.SelectByValue("0");       
        }
    
        [CodedStep(@"Select Location")]
        public void TS_CS_006()
        {
                        
            Pages.PS_AddIdeaHopperPage.LoctionTypeSelect.SelectByText(CustomUtils.locationValue,true);  
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify after choosing 'location' a field specified for location is displayed")]
        public void TS_CS_007()
        {
            Pages.PS_AddIdeaHopperPage.CustomLocTextArea.IsVisible();
             if(Pages.PS_AddIdeaHopperPage.SpecificLocSpan.IsVisible())  
             {
                Assert.IsTrue(Pages.PS_AddIdeaHopperPage.SpecificLocSpan.IsVisible(),"Additional location field should be displayed");       
             }
             
             if(Pages.PS_AddIdeaHopperPage.CustomLocTextArea.IsVisible())
             {                       
                Pages.PS_AddIdeaHopperPage.CustomLocTextArea.Wait.ForVisible();
                Assert.IsTrue(Pages.PS_AddIdeaHopperPage.CustomLocTextArea.IsVisible(),"Blank field for custom location should be displayed");
             }
        }
        
        [CodedStep(@"Verify blank field specified for Custom location is displayed")]
        public void TS_CS_IF_01()
        {
            ActiveBrowser.RefreshDomTree();                        
            Pages.PS_AddIdeaHopperPage.CustomLocTextArea.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.CustomLocTextArea.IsVisible(),"Blank field for custom location should be displayed");
        }
    
        [CodedStep(@"Click on the blank field specified for Custom Location")]
        public void TS_CS_IF_02()
        {
            Pages.PS_AddIdeaHopperPage.CustomLocTextArea.Click();                 
        }
    
        [CodedStep(@"Connect to formula builder pop-up window")]
        public void TS_CS_IF_03()
        {
                                                                                                                        
            Manager.WaitForNewBrowserConnect("/eml/EMLBuilder.epage", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
                                                                                                                        
        }
    
        [CodedStep(@"Click on Objects tab")]
        public void TS_CS_IF_04()
        {
            Pages.PS_FormulaBuilderPage.ObjectsSpanTab.Wait.ForExists();
            Pages.PS_FormulaBuilderPage.ObjectsSpanTab.Click();
                                                                                                                        
        }
    
        [CodedStep(@"Verify Object tab is open")]
        public void TS_CS_IF_05()
        {
            Pages.PS_FormulaBuilderPage.InObjectsProjectSpan.Wait.ForExists();
            Assert.IsTrue(Pages.PS_FormulaBuilderPage.InObjectsProjectSpan.IsVisible());
                                                                                                                        
        }
    
        [CodedStep(@"Click on Project field and select location")]
        public void TS_CS_IF_06()
        {
            Pages.PS_FormulaBuilderPage.InObjectsProjectSpan.Click();
            Pages.PS_FormulaBuilderPage.WorkPopUpDiv.Wait.ForVisible();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            Pages.PS_FormulaBuilderPage.WorkPopUpSearchSpan.Wait.ForExists();               
            Pages.PS_FormulaBuilderPage.WorkPopUpSearchSpan.Wait.ForVisible();
            if(Data["LocationType"].ToString().Contains("Search"))
            {
                Pages.PS_FormulaBuilderPage.WorkPopUpSearchSpan.Click();
                Pages.PS_FormulaBuilderPage.WorkPopUpFindTextInput.Wait.ForVisible();
                Actions.SetText(Pages.PS_FormulaBuilderPage.WorkPopUpFindTextInput,Data["SemiLocationPath"].ToString());
                Pages.PS_FormulaBuilderPage.WorkPopUpGoButton.Click(true);
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("select_work_location_search_result_div"),Data["LocationPath"].ToString()));
                Log.WriteLine(string.Format(AppLocators.get("select_work_location_search_result_div"),Data["LocationPath"].ToString()));
                resDiv.Wait.ForVisible();
                Assert.IsTrue(resDiv.IsVisible());
                resDiv.ScrollToVisible();
                resDiv.MouseClick(MouseClickType.LeftClick);                    
            }
        }
    
        [CodedStep(@"Verify location is selected")]
        public void TS_CS_IF_07()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.IsVisible());
            Assert.IsTrue(Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.BaseElement.InnerText.Contains(Data["LocationPath"].ToString()),"Work Location should be selected");
        }
    
        [CodedStep(@"Click + on Project field")]
        public void TS_CS_IF_08()
        {
            HtmlImage plusSign = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("plus_sign_projects_formula_builder"));
            plusSign.Wait.ForExists();
            plusSign.Click();
        }
    
        [CodedStep(@"Verify location is added as formula")]
        public void TS_CS_IF_09()
        {
            string locValue = Actions.InvokeScript("document.getElementById(\"usrEditId\").value"); 
            Assert.IsTrue(locValue.Contains(Data["LocationPath"].ToString()),"Location should added as formula");
        }
    
        [CodedStep(@"Click on Ok button")]
        public void TS_CS_IF_10()
        {
            Pages.PS_FormulaBuilderPage.FormulaBuilderOkBtn.Click(true);
            ActiveBrowser.Close();
        }
    
        [CodedStep(@"Verify 'formula' is in the location field on 'Admin: Add Idea Hopper' page")]
        public void TS_CS_IF_11()
        {
            string locValue = Actions.InvokeScript("document.getElementById(\"usrDispId_\").value"); 
            Assert.IsTrue(locValue.Contains(Data["LocationPath"].ToString()),"Formula should display in the locatoin field of 'Admin: Add Idea Hopper' page");
        }
    
        [CodedStep(@"Click on empty Location field and Set the Location")]
        public void TS_CS_IF_12()
        {
            
                   
            Pages.PS_AddIdeaHopperPage.SpecificLocSpan.Click();
            Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            Pages.PS_AddIdeaHopperPage.LocationSearchTab.Wait.ForExists();               
            Pages.PS_AddIdeaHopperPage.LocationPopUpTitleSpan.Wait.ForVisible();
            if(Data["LocationType"].ToString().Contains("Search"))
            {
                Pages.PS_AddIdeaHopperPage.LocationSearchTab.Click();
                Pages.PS_AddIdeaHopperPage.LocationPopUpFindText.Wait.ForVisible();
                Actions.SetText(Pages.PS_AddIdeaHopperPage.LocationPopUpFindText,Data["SemiLocationPath"].ToString());
                Pages.PS_AddIdeaHopperPage.LocationPopUpGoButton.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                System.Threading.Thread.Sleep(5000);
                ActiveBrowser.RefreshDomTree();                
                HtmlDiv resDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("create_idea_location_search_result_div"),Data["LocationPath"].ToString()));
                Log.WriteLine(string.Format(AppLocators.get("create_idea_location_search_result_div"),Data["LocationPath"].ToString()));
                resDiv.Wait.ForVisible();
                Assert.IsTrue(resDiv.IsVisible());
                resDiv.ScrollToVisible();
                resDiv.MouseClick(MouseClickType.LeftClick);
                ActiveBrowser.RefreshDomTree();                       
            }
            
            
            
        }
    
        [CodedStep(@"Click on Owner Field")]
        public void TS_CS_008()
        {
            Pages.PS_AddIdeaHopperPage.ProjectLeadSpan.Click();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Ownner selection form should popup")]
        public void TS_CS_009()
        {
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupTitleSpan.Wait.ForExists();
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.Wait.ForExists();
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Wait.ForExists();
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.IsVisible());
            Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.IsVisible());
        }
    
        [CodedStep(@"Enter the name of Owner")]
        public void TS_CS_010()
        {
            Actions.SetText(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText,CustomUtils.adminName1);
        }
    
        [CodedStep(@"Click on Go button")]
        public void TS_CS_011()
        {
            Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Click();
            Manager.ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify user should found and user name is listed as link")]
        public void TS_CS_012()
        {
            HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
            adminNameLocator.Wait.ForExists();
            Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
            adminNameLocator.Click();
        }
    
        [CodedStep(@"Select 'Automatically request approval upon Idea submission' checkbox and press Add")]
        public void TS_CS_013()
        {
            Pages.PS_AddIdeaHopperPage.BehaviorCheckBox.Click();
            Pages.PS_AddIdeaHopperPage.AddButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user is redirected to 'Admin: Idea Hopper Configuration' page")]
        public void TS_CS_014()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Idea Hopper Configuration");
            Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.IsVisible(),"Add button should be displayed"); 
            ActiveBrowser.RefreshDomTree();
                        
        }
    
        [CodedStep(@"Verify that created Idea Hopper name is present in the list")]
        public void TS_CS_016()
        {
            string ideaHopperNameLocStr = string.Format(AppLocators.get("idea_hopper_name_link_row"),ideaHopperName);
            Log.WriteLine(ideaHopperNameLocStr);
            HtmlTableRow ideaNameRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(ideaHopperNameLocStr);
            ideaNameRow.Wait.ForExists();
            Assert.IsTrue(ideaNameRow.IsVisible());
        }
    
        [CodedStep(@"Refresh Dom Tree")]
        public void TS_CS_00()
        {
            ActiveBrowser.RefreshDomTree();
        }
    }
}
