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

    public class TST_IH_039 : BaseWebAiiTest
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
        public void TST_IH_039_CS01()
        {
                        Pages.PS_HomePage.AdminLeftNavLink.Click();
                        ActiveBrowser.WaitUntilReady();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on Addnew Button")]
        public void TST_IH_039_CS02()
        {
                        Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.Click();
                        ActiveBrowser.WaitUntilReady();
                        Pages.PS_AddIdeaHopperPage.NameText.Wait.ForExists();
        }
    
        [CodedStep(@"Verify User is directed to Add Idea Hopper page")]
        public void TST_IH_039_CS03()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
                                                                                                                                                                                    
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_IH_039_CS04()
        {
                        ideaHopperName = "TestIdea" + Randomizers.generateRandomInt(1000,9999);
                        Actions.SetText(Pages.PS_AddIdeaHopperPage.NameText,ideaHopperName);            
        }
    
        [CodedStep(@"Select Type")]
        public void TST_IH_039_CS05()
        {
                        //string ideaHopperName = "TestIdea" + Randomizers.generateRandomInt(1000,9999);
                        Pages.PS_AddIdeaHopperPage.TypeSelect.SelectByValue("0");       
        }
    
        [CodedStep(@"Select Location")]
        public void TST_IH_039_CS06()
        {
                                                                                                                                                            
                        Pages.PS_AddIdeaHopperPage.LoctionTypeSelect.SelectByText("Custom...",true);
                        System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify blank field specified for Custom location is displayed")]
        public void TST_IH_039_CS07()
        {
                       ActiveBrowser.RefreshDomTree();                        
                       Pages.PS_AddIdeaHopperPage.CustomLocTextArea.Wait.ForVisible();
                       Assert.IsTrue(Pages.PS_AddIdeaHopperPage.CustomLocTextArea.IsVisible(),"Blank field for custom location should be displayed");
        }
    
        [CodedStep(@"Click on the blank field specified for Custom Location")]
        public void TST_IH_039_CS08()
        {
                        Pages.PS_AddIdeaHopperPage.CustomLocTextArea.Click();                 
        }
    
        [CodedStep(@"Connect to formula builder pop-up window")]
        public void TST_IH_039_CS09()
        {
                                                                                                            
                        Manager.WaitForNewBrowserConnect("/eml/EMLBuilder.epage", true, 15000);
                        Manager.ActiveBrowser.WaitUntilReady();
                                                                                                            
        }
    
        [CodedStep(@"Click on Objects tab")]
        public void TST_IH_039_CS10()
        {
                        Pages.PS_FormulaBuilderPage.ObjectsSpanTab.Wait.ForExists();
                        Pages.PS_FormulaBuilderPage.ObjectsSpanTab.Click();
                                                                                                            
        }
    
        [CodedStep(@"Verify Object tab is open")]
        public void TST_IH_039_CS11()
        {
                        Pages.PS_FormulaBuilderPage.InObjectsProjectSpan.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_FormulaBuilderPage.InObjectsProjectSpan.IsVisible());
                                                                                                            
        }
    
        [CodedStep(@"Click on Project field and select location")]
        public void TST_IH_039_CS12()
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
        public void TST_IH_039_CS13()
        {
                        ActiveBrowser.RefreshDomTree();
                        Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_FormulaBuilderPage.WorkPopupProjectsChooseDiv.BaseElement.InnerText.Contains(Data["LocationPath"].ToString()),"Work Location should be selected");
        }
    
        [CodedStep(@"Click + on Project field")]
        public void TST_IH_039_CS14()
        {
                        HtmlImage plusSign = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("plus_sign_projects_formula_builder"));
                        plusSign.Wait.ForExists();
                        plusSign.Click();
        }
    
        [CodedStep(@"Verify location is added as formula")]
        public void TST_IH_039_CS15()
        {
                        string locValue = Actions.InvokeScript("document.getElementById(\"usrEditId\").value"); 
                        Assert.IsTrue(locValue.Contains(Data["LocationPath"].ToString()),"Location should added as formula");
        }
    
        [CodedStep(@"Click on Ok button")]
        public void TST_IH_039_CS16()
        {
                        Pages.PS_FormulaBuilderPage.FormulaBuilderOkBtn.Click(true);
                        ActiveBrowser.Close();
        }
    
        [CodedStep(@"Verify 'formula' is in the location field on 'Admin: Add Idea Hopper' page")]
        public void TST_IH_039_CS17()
        {
                        string locValue = Actions.InvokeScript("document.getElementById(\"usrDispId_\").value"); 
                        Assert.IsTrue(locValue.Contains(Data["LocationPath"].ToString()),"Formula should display in the locatoin field of 'Admin: Add Idea Hopper' page");
        }
    
        [CodedStep(@"Click on Owner Field")]
        public void TST_IH_039_CS18()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadSpan.Click();
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify Ownner selection form should popup")]
        public void TST_IH_039_CS19()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupTitleSpan.Wait.ForExists();
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.Wait.ForExists();
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.IsVisible());
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText.IsVisible());
        }
    
        [CodedStep(@"Enter the name of Owner")]
        public void TST_IH_039_CS20()
        {
                        Actions.SetText(Pages.PS_AddIdeaHopperPage.ProjectLeadPopupFindText,CustomUtils.adminName1);
        }
    
        [CodedStep(@"Click on Go button")]
        public void TST_IH_039_CS21()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadPopupGoButton.Click();
                        Manager.ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        System.Threading.Thread.Sleep(5000);
                        ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify user should found and user name is listed as link")]
        public void TST_IH_039_CS22()
        {
                        HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
                        adminNameLocator.Wait.ForExists();
                        Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
        }
    
        [CodedStep(@"Click on User name link")]
        public void TST_IH_039_CS23()
        {
                        HtmlDiv adminNameLocator = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("after_find_user_name_div"),CustomUtils.adminName1));
                        adminNameLocator.Wait.ForExists();
                        Assert.IsTrue(adminNameLocator.IsVisible(),"User Link should be present");
                        adminNameLocator.Click();
        }
    
        [CodedStep(@"Verify user should taken to 'Admin: Add Idea Hopper' page")]
        public void TST_IH_039_CS24()
        {
                        Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Add Idea Hopper");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.NameText.IsVisible(),"Name column field should be present on Add Idea Hopper Page");
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.AddButton.IsVisible(),"Add button should be displayed"); 
        }
    
        [CodedStep(@"Verify selected user's name should be in the Owner field")]
        public void TST_IH_039_CS25()
        {
                        Pages.PS_AddIdeaHopperPage.ProjectLeadDiv.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_AddIdeaHopperPage.ProjectLeadDiv.BaseElement.InnerText.Contains(CustomUtils.adminName1));
        }
    
        [CodedStep(@"Uncheck 'Automatically request approval upon Idea submission' checkbox and press Add")]
        public void TST_IH_039_CS26()
        {
                                    Pages.PS_AddIdeaHopperPage.BehaviorCheckBox.Check(false);
                                    Pages.PS_AddIdeaHopperPage.AddButton.Click();
                                    ActiveBrowser.WaitUntilReady();
                                    
        }
    
        [CodedStep(@"Verify user is redirected to 'Admin: Idea Hopper Configuration' page")]
        public void TST_IH_039_CS27()
        {
                                    Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Idea Hopper Configuration");
                                    Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.IsVisible(),"Add button should be displayed"); 
                                    ActiveBrowser.RefreshDomTree();
                                                
        }
    
        [CodedStep(@"Verify that created Idea Hopper name is present in the list")]
        public void TST_IH_039_CS28()
        {
                                    string ideaHopperNameLocStr = string.Format(AppLocators.get("idea_hopper_name_link_row"),ideaHopperName);
                                    Log.WriteLine(ideaHopperNameLocStr);
                                    HtmlTableRow ideaNameRow = ActiveBrowser.Find.ByXPath<HtmlTableRow>(ideaHopperNameLocStr);
                                    ideaNameRow.Wait.ForExists();
                                    Assert.IsTrue(ideaNameRow.IsVisible());
        }
    
        [CodedStep(@"Delete idea")]
        public void TST_IH_027_CS29()
        {
                                                
                                    Log.WriteLine(string.Format(AppLocators.get("idea_hopper_del_checkbox"),ideaHopperName));
                                    HtmlInputCheckBox ideaDeleteCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format(AppLocators.get("idea_hopper_del_checkbox"),ideaHopperName));
                                    ideaDeleteCheckbox.Wait.ForExists();
                                    ideaDeleteCheckbox.Click();
                                    Pages.PS_IdeaHopperConfigurationPage.DeleteBtn.Click();
                                    Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Wait.ForExists();
                                    Pages.PS_IdeaHopperConfigurationPage.DeleteYesButton.Click();
                                    ActiveBrowser.WaitUntilReady();
        }
    }
}
