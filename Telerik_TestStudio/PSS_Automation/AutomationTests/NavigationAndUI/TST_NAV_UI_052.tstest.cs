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

    public class TST_NAV_UI_052 : BaseWebAiiTest
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
        public void TST_NAV_UI_052_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_WorkTemplatesPage.AddNewButton.IsVisible(),"Addnew buttton should be vissible");
            Assert.IsTrue(Pages.PS_WorkTemplatesPage.DeleteButton.IsVisible(),"Delete button should be vissible");
            Assert.IsTrue(Pages.PS_WorkTemplatesPage.ContentDiv.IsVisible(),"Content div should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_WorkGenerationTemplatesPage.AddNewButton.IsVisible(),"Addnew button should be present");
            Assert.IsTrue(Pages.PS_WorkGenerationTemplatesPage.DeleteButton.IsVisible(),"Addnew button should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"create new link should be present");
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_052_CodedStep3()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep3()
        {
            Assert.IsTrue(Pages.PS_PowerPointTemplates.TemplatesContainerFrame.NewTemplateLink.IsVisible(),"New template link should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_AdminDashboardLayoutsPage.DashboardLayoutsContainerForm.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep5()
        {
            
            Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.DeleteButton.IsVisible(),"Delete button should be present");
            Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.AddNewButton.IsVisible(),"Add new button should be present");
            Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.ContainerDiv.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep6()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Financial Review Layouts"));
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_052_CodedStep7()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep7()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Default Permissions"));
            Assert.IsTrue(Pages.PS_DefaultPermissionsPage.HelpLink.IsVisible(),"Help link should be present");
            Assert.IsTrue(Pages.PS_DefaultPermissionsPage.PermissionTsDropDownList.IsVisible(),"Select drop down list should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep8()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Define Permissions"));
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.CancelBtn.IsVisible());
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.PermissionsDropDownList.IsVisible());
            Assert.IsTrue(Pages.PS_DefinePermissionsPage.UpdateBtn.IsVisible());
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep9()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Group Profile"));
            Assert.IsTrue(Pages.PS_GroupProfilePage.AddMembersLink.IsVisible(),"Add member link should be present");
            Assert.IsTrue(Pages.PS_GroupProfilePage.DeleteBtn.IsVisible(),"Delete should be present");
            Assert.IsTrue(Pages.PS_GroupProfilePage.EmailMembersShowLink.IsVisible(),"Email member link should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep10()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Edit Work Status Names"));
            Assert.IsTrue(Pages.PS_EditWorkStatusNamesPage.SaveBtn.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_EditWorkStatusNamesPage.ResetBtn.IsVisible(),"Save button should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep11()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Replaceable Terms"));
            Assert.IsTrue(Pages.PS_ReplaceableTermsPage.SaveBtn.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_ReplaceableTermsPage.ResetBtn.IsVisible(),"Reset button should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep12()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Alerts -- Set subscriptions by role"));
            Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.SaveBtn.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.ResetBtn.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.CopyFromSelectDropDownList.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.RoleSelectDropDownList.IsVisible(),"Save button should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep13()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Agents"));
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep14()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Look & Feel"));
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.SaveBtn.IsVisible(),"Save button should be present");
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ContentBGInputText.IsVisible());
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep15()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Charts"));
            Assert.IsTrue(Pages.PS_ChartsPage.ContainerDiv.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep16()
        {
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep17()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Custom Fields"));
            Assert.IsTrue(Pages.PS_CustomFieldsPage.AddNewBtn.IsVisible(),"Add New button should be present");
            Assert.IsTrue(Pages.PS_CustomFieldsPage.ContainerDiv.IsVisible(),"Container div should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep18()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Field Management"));
            Assert.IsTrue(Pages.PS_FieldManagementPage.OtherH1Tag.IsVisible());
            Assert.IsTrue(Pages.PS_FieldManagementPage.ProjectWorkH1Tag.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep19()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Help Links"));
            Assert.IsTrue(Pages.PS_HelpLinksPage.AddNewBtn.IsVisible(),"AddNew button should be present");
            Assert.IsTrue(Pages.PS_HelpLinksPage.UpdateBtn.IsVisible(),"Update button should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep20()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Help Desk Requests"));
            Assert.IsTrue(Pages.PS_HelpDeskRequestsPage.AddNewBtn.IsVisible(),"AddNew button should be present");
            Assert.IsTrue(Pages.PS_HelpDeskRequestsPage.DeleteBtn.IsVisible(),"Delete button should be present");
          
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep21()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Home Page Configurations"));
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.IsVisible(),"Add new configuration link should be present");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.IsVisible(),"Change home pages div should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep22()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Idea Hopper Configuration"));
            Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.IsVisible(),"AddNew button should be present");
            Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.DeleteBtn.IsVisible(),"AddNew button should be present");
            Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.SubmitAnIdeaLink.IsVisible(),"AddNew button should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep23()
        {
            Assert.IsTrue(Pages.PS_ManageImportantLinksPage.AddNewBtn.IsVisible(),"AddNew button should be displayed");
            Assert.IsTrue(Pages.PS_ManageImportantLinksPage.DeleteBtn.IsVisible(),"Delete button should be displayed");
            Assert.IsTrue(Pages.PS_ManageImportantLinksPage.UpdateLink.IsVisible(),"Update link should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep24()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Mass change % complete method"));
            Assert.IsTrue(Pages.PS_MassChangeCompletePage.CMDropDownList.IsVisible(),"Complete method dropdown list should be present");
            Assert.IsTrue(Pages.PS_MassChangeCompletePage.GoBtn.IsVisible(),"Complete method dropdown list should be present");
            Assert.IsTrue(Pages.PS_MassChangeCompletePage.PortfolioDropDownList.IsVisible(),"Complete method dropdown list should be present");
            
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_052_CodedStep25()
        //{
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep25()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Object Types"));
            Assert.IsTrue(Pages.PS_ObjectTypesPage.ObjectTypesTableDiv.IsVisible(),"Object type table should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep26()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Phase Advance Configuration"));
            Assert.IsTrue(Pages.PS_PhaseAdvanceConfigurationPage.SaveBtn.IsVisible(),"save button should be present");
            Assert.IsTrue(Pages.PS_PhaseAdvanceConfigurationPage.ResetLink.IsVisible(),"Reset button should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep27()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Processes"));
            Assert.IsTrue(Pages.PS_ProcessesPage.AddNewProcessBtn.IsVisible(),"AddNewProcess button should be displayed");
            Assert.IsTrue(Pages.PS_ProcessesPage.DeleteBtn.IsVisible(),"Delete button should be displayed");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep28()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Calendar"));
            Assert.IsTrue(Pages.PS_ResourceCalendarPage.AddNewBtn.IsVisible(),"AddNew button should be displayed");
            Assert.IsTrue(Pages.PS_ResourceCalendarPage.EditLink.IsVisible(),"Edit link should be displayed");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep29()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Planning"));
            Assert.IsTrue(Pages.PS_ResourcePlanningPage.CancelBtn.IsVisible(),"Cancel button should be dispaled");
            Assert.IsTrue(Pages.PS_ResourcePlanningPage.SaveBtn.IsVisible(),"Save button should be dispaled");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep30()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Search Synonyms"));
            Assert.IsTrue(Pages.PS_SearchSynonymsPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
            Assert.IsTrue(Pages.PS_SearchSynonymsPage.InstructionTextarea.IsVisible(),"Instruction test area should be present");
            Assert.IsTrue(Pages.PS_SearchSynonymsPage.SaveBtn.IsVisible(),"Save button should be displayed");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep31()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Status Report Templates"));
            Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewBtn.IsVisible(),"Addnew button should be displayed");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep32()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Tags List"));
            Assert.IsTrue(Pages.PS_TagsListPage.AddNewTagBtn.IsVisible(),"AddNew button should be displayed");
            Assert.IsTrue(Pages.PS_TagsListPage.AllTagGroupsLink.IsVisible(),"All Groups link should be displayed");
            Assert.IsTrue(Pages.PS_TagsListPage.DepedenciesLink.IsVisible(),"Dependencies link should be displayed");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep33()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("User Creation Options"));
            Assert.IsTrue(Pages.PS_UserCreationOptionsPage.SaveBtn.IsVisible(),"Save button should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep34()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Work Template Visibility Configuration"));
            Assert.IsTrue(Pages.PS_WorkTemplateVisibilityPage.SaveBtn.IsVisible(),"Save button should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep35()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Logs"));
            Assert.IsTrue(Pages.PS_LogsPage.GoBtn.IsVisible(),"Go buttun should be visible");
            Assert.IsTrue(Pages.PS_LogsPage.ViewDropDownList.IsVisible(),"View drop down list should be visible");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep36()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Announcement"));
            Assert.IsTrue(Pages.PS_AnnouncementPage.AnnounceContainerFrame.SubmitLink.IsVisible(),"Submit button should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_052_CodedStep37()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Related Work"));
            
        }
    }
}
