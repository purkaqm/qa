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

    public class TST_MUT_011 : BaseWebAiiTest
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
        
        [CodedStep(@"Click on Add icon on Left Navigation menu")]
        public void TST_MUT_016_ClickAddIcon()
        {
            Pages.PS_HomePage.AddLeftNavLink.Click();
                                    
        }
    
        /// \bug This is far too complicated, can we simplify the logic somehow?
        
        /// \bug The method name does not make sense, copy paste error?
        [CodedStep(@"Verify User is navigated to DashBoard Page")]
        public void TST_NAV_UI_017_CodedStep()
        {
                        Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardDiv.IsVisible(),"div should be present in dashboard page");
                        Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardLeftPortfolioSelectorSelect.IsVisible(),"Portfolio selector list should be present in dashboard page");
                        Assert.IsTrue(Pages.PS_ReviewDashboardPage.DashboardLayoutSelectorSelect.IsVisible(),"Layout selector list should be present in dashboard page");
        }
    
        [CodedStep(@"Verify User is navigated to Visual Portal page")]
        public void TST_NAV_UI_017_CodedStep1()
        {
                        Assert.IsTrue(Pages.PS_ReviewVisualPortal.VisualPortalPropertySelectionSelect.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewVisualPortal.VisualPortalLeftGoButtun.IsVisible());
                        
        }
    
        [CodedStep(@"Verify User is navigated to Manage Layouts Page ")]
        public void TST_NAV_UI_017_CodedStep2()
        {
                        Assert.IsTrue(Pages.PS_ReviewManageLayouts.ManageLayoutHelpLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewManageLayouts.ManageLayoutsAddVisualPortanButton.IsVisible());
        }
    
        [CodedStep(@"Verify User is navigated to Add Visual Portal")]
        public void TST_NAV_UI_017_CodedStep3()
        {
                        Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.PortalSettingsTag.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.DefaultChartTag.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.PortalSettingNameText.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.EditVisualPortalCancelButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewAddVisualPortal.EditVisualPortalSubmitButton.IsVisible());
        }
    
        [CodedStep(@"Verify User is navigated to Resource Review  page")]
        public void TST_NAV_UI_017_CodedStep4()
        {
                        Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewLayoutDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridFiltersDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridOptionsDisplayFilterDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridWhatCanDoDiv.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewResourceReview.ResourceReviewGridExportDiv.IsVisible());
        }
    
        [CodedStep(@"Verify User is navigated to Executive Review page ")]
        public void TST_NAV_UI_017_CodedStep5()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Executive Review"));
                        Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewPortfolioSelectSelect.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewLayoutSelectSelect.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewExecutiveReview.ExecutiveReviewGoButton.IsVisible());
                        
        }
    
        [CodedStep(@"Verify User is navigated to Financial Review page")]
        public void TST_NAV_UI_017_CodedStep6()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Financial Review"));
                        Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewPortfolioSelectSelect.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewGoButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFinancialReview.FinancialReviewLayoutSelectSelect.IsVisible());
        }
    
        [CodedStep(@"Verify User is navigated to Portfolio page ")]
        public void TST_NAV_UI_017_CodedStep7()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Portfolios"));
                        Assert.IsTrue(Pages.PS_ReviewPortfolios.PortfoliosAddPortfolioButton.IsVisible());
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Measures Library page")]
        public void TST_NAV_UI_017_CodedStep8()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Measures Library"));
                        Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.IsVisible());
                        
        }
    
        [CodedStep(@"Verify User is navigated to MY Report page")]
        public void TST_NAV_UI_017_CodedStep9()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("My Reports | Reports"));
                        Assert.IsTrue(Pages.PS_MyReportsPage.AddNewReportInputButton.IsVisible());
        }
    
        [CodedStep(@"Verify User is navigated to Public Report page")]
        public void TST_NAV_UI_017_CodedStep10()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Public Reports | Reports"));
                        Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportAddNewButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewPublicReports.PublicReportDeleteButton.IsVisible());
                        
        }
    
        [CodedStep(@"Verify  User is navigated to Manage Report page")]
        public void TST_NAV_UI_017_CodedStep11()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Manage Reports"));
                        Assert.IsTrue(Pages.PS_ReviewManageReports.ManageLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewManageReports.ManageReportSearchButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewManageReports.ManageReportResetButton.IsVisible());
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Find A Paerson page")]
        public void TST_NAV_UI_017_CodedStep12()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Find a person: Active"));
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.ActiveLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.DeletedLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.LockedLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.OnlineLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.NonGroupedLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.NonRespondentsLink.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewFindAPerson.NoAccessUsersLink.IsVisible());
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Groups page")]
        public void TST_NAV_UI_017_CodedStep13()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Groups"));
                        Assert.IsTrue(Pages.PS_ReviewGroupsPage.GroupsAddNewButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewGroupsPage.GroupsDeleteButton.IsVisible());
                        
                        
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Resource Pool page")]
        public void TST_NAV_UI_017_CodedStep14()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Pools"));
                        
        }
    
        [CodedStep(@"Verify User is navigated to Certifiction page")]
        public void TST_NAV_UI_017_CodedStep15()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Certification"));
                        /*if(Pages.PS_ReviewCertificationPage.FrameJspFrame.HideSearchLink.IsVisible())
                        {
                            Assert.IsTrue(Pages.PS_ReviewCertificationPage.FrameJspFrame.CertificateSearchLink.IsVisible());
                        }*/
                        
        }
    
        [CodedStep(@"Verify User is navigated to Resources Rates page")]
        public void TST_NAV_UI_017_CodedStep16()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Rates"));
                        Assert.IsTrue(Pages.PS_ReviewResourceRatesPage.RateTablesLink.IsVisible());
                        
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Financials page")]
        public void TST_NAV_UI_017_CodedStep17()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("From a File"));
                        Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.IsVisible());
                        
        }
    
        [CodedStep(@"Verify User is navigated to Users page")]
        public void TST_NAV_UI_017_CodedStep18()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Import New Users"));
                        Assert.IsTrue(Pages.PS_ReviewImportNewUsersPage.UploadFileLoadButton.IsVisible());
                        
        }
    
        [CodedStep(@"Verify User is navigated to Tags page")]
        public void TST_NAV_UI_017_CodedStep19()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Load Tag From File"));
                        Assert.IsTrue(Pages.PS_ReviewLoadTagsPage.TagsUploadAndContinueButton.IsVisible());
                        Assert.IsTrue(Pages.PS_ReviewLoadTagsPage.TagsCancelButton.IsVisible());
                        
                        
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Financial Bugs page")]
        public void TST_NAV_UI_017_CodedStep20()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Metric Bulk Actions"));
                        Assert.IsTrue(Pages.PS_ReviewFinancialBulkActionsPage.FinancialBulkPortfolioSelect.IsVisible());
                        
        }
    
        
    
        [CodedStep(@"Verify user is navigated to Work template page")]
        public void TST_NAV_UI_052_CodedStep()
        {
                        ActiveBrowser.RefreshDomTree();
                        Assert.IsTrue(Pages.PS_WorkTemplatesPage.AddNewButton.IsVisible(),"Addnew buttton should be vissible");
                        Assert.IsTrue(Pages.PS_WorkTemplatesPage.DeleteButton.IsVisible(),"Delete button should be vissible");
                        Assert.IsTrue(Pages.PS_WorkTemplatesPage.ContentDiv.IsVisible(),"Content div should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Work Generation template page")]
        public void TST_NAV_UI_052_CodedStep1()
        {
                        Assert.IsTrue(Pages.PS_WorkGenerationTemplatesPage.AddNewButton.IsVisible(),"Add New button should be present");
                        Assert.IsTrue(Pages.PS_WorkGenerationTemplatesPage.DeleteButton.IsVisible(),"Delete button should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Metric template page ")]
        public void TST_NAV_UI_052_CodedStep2()
        {
                        Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"create new link should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Power Point page")]
        public void TST_NAV_UI_052_CodedStep3()
        {
                        Assert.IsTrue(Pages.PS_PowerPointTemplates.TemplatesContainerFrame.NewTemplateLink.IsVisible(),"New template link should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Dashboard page")]
        public void TST_NAV_UI_052_CodedStep4()
        {
                        Assert.IsTrue(Pages.PS_AdminDashboardLayoutsPage.DashboardLayoutsContainerForm.IsVisible());
                        
        }
    
        [CodedStep(@"Verify user is navigated to Executive Review page")]
        public void TST_NAV_UI_052_CodedStep5()
        {
                        
                        Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.DeleteButton.IsVisible(),"Delete button should be present");
                        Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.AddNewButton.IsVisible(),"Add new button should be present");
                        Assert.IsTrue(Pages.PS_ExecutiveReviewLayoutsPage.ContainerDiv.IsVisible());
                        
        }
    
        [CodedStep(@"Verify user is navigated to Financial Review page")]
        public void TST_NAV_UI_052_CodedStep6()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Financial Review Layouts"));
                        
        }
    
        [CodedStep(@"Verify user is navigated to Default Permission page")]
        public void TST_NAV_UI_052_CodedStep7()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Default Permissions"));
                        Assert.IsTrue(Pages.PS_DefaultPermissionsPage.HelpLink.IsVisible(),"Help link should be present");
                        Assert.IsTrue(Pages.PS_DefaultPermissionsPage.PermissionTsDropDownList.IsVisible(),"Select drop down list should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Define Permission page")]
        public void TST_NAV_UI_052_CodedStep8()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Define Permissions"));
                        Assert.IsTrue(Pages.PS_DefinePermissionsPage.CancelBtn.IsVisible());
                        Assert.IsTrue(Pages.PS_DefinePermissionsPage.PermissionsDropDownList.IsVisible());
                        Assert.IsTrue(Pages.PS_DefinePermissionsPage.UpdateBtn.IsVisible());
                        
                        
        }
    
        [CodedStep(@"Verify user is navigated to Group Profile page")]
        public void TST_NAV_UI_052_CodedStep9()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Group Profile"));
                        Assert.IsTrue(Pages.PS_GroupProfilePage.AddMembersLink.IsVisible(),"Add member link should be present");
                        Assert.IsTrue(Pages.PS_GroupProfilePage.DeleteBtn.IsVisible(),"Delete should be present");
                        Assert.IsTrue(Pages.PS_GroupProfilePage.EmailMembersShowLink.IsVisible(),"Email member link should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Work Status Name page")]
        public void TST_NAV_UI_052_CodedStep10()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Edit Work Status Names"));
                        Assert.IsTrue(Pages.PS_EditWorkStatusNamesPage.SaveBtn.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_EditWorkStatusNamesPage.ResetBtn.IsVisible(),"Save button should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Replaceable Terms page")]
        public void TST_NAV_UI_052_CodedStep11()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Replaceable Terms"));
                        Assert.IsTrue(Pages.PS_ReplaceableTermsPage.SaveBtn.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_ReplaceableTermsPage.ResetBtn.IsVisible(),"Reset button should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Alert Subscription page")]
        public void TST_NAV_UI_052_CodedStep12()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Alerts -- Set subscriptions by role"));
                        Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.SaveBtn.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.ResetBtn.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.CopyFromSelectDropDownList.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_AlertsSetSubscriptionsPage.RoleSelectDropDownList.IsVisible(),"Save button should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Agents page")]
        public void TST_NAV_UI_052_CodedStep13()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Agents"));
                        
        }
    
        [CodedStep(@"Verify user is navigated to Branding page")]
        public void TST_NAV_UI_052_CodedStep14()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Look & Feel"));
                        Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.SaveBtn.IsVisible(),"Save button should be present");
                        Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.ContentBGInputText.IsVisible());
                        
        }
    
        [CodedStep(@"Verify user is navigated to Charts page")]
        public void TST_NAV_UI_052_CodedStep15()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Charts"));
                        Assert.IsTrue(Pages.PS_ChartsPage.ContainerDiv.IsVisible());
        }
    
        [CodedStep(@"Verify user is navigated to Conditions page")]
        public void TST_NAV_UI_052_CodedStep16()
        {
                        
        }
    
        [CodedStep(@"Verify user is navigated to Custom Fields page")]
        public void TST_NAV_UI_052_CodedStep17()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Custom Fields"));
                        Assert.IsTrue(Pages.PS_CustomFieldsPage.AddNewBtn.IsVisible(),"Add New button should be present");
                        Assert.IsTrue(Pages.PS_CustomFieldsPage.ContainerDiv.IsVisible(),"Container div should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Field Management page")]
        public void TST_NAV_UI_052_CodedStep18()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Field Management"));
                        Assert.IsTrue(Pages.PS_FieldManagementPage.OtherH1Tag.IsVisible());
                        Assert.IsTrue(Pages.PS_FieldManagementPage.ProjectWorkH1Tag.IsVisible());
        }
    
        [CodedStep(@"Verify user is navigated to Help link page")]
        public void TST_NAV_UI_052_CodedStep19()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Help Links"));
                        Assert.IsTrue(Pages.PS_HelpLinksPage.AddNewBtn.IsVisible(),"AddNew button should be present");
                        Assert.IsTrue(Pages.PS_HelpLinksPage.UpdateBtn.IsVisible(),"Update button should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Help Desk Requested link page")]
        public void TST_NAV_UI_052_CodedStep20()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Help Desk Requests"));
                        Assert.IsTrue(Pages.PS_HelpDeskRequestsPage.AddNewBtn.IsVisible(),"AddNew button should be present");
                        Assert.IsTrue(Pages.PS_HelpDeskRequestsPage.DeleteBtn.IsVisible(),"Delete button should be present");
                      
                        
        }
    
        [CodedStep(@"Verify user is navigated to Home Page link page")]
        public void TST_NAV_UI_052_CodedStep21()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Home Page Configurations"));
                        Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.IsVisible(),"Add new configuration link should be present");
                        Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.IsVisible(),"Change home pages div should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Idea Hoppers page")]
        public void TST_NAV_UI_052_CodedStep22()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Idea Hopper Configuration"));
                        Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.AddNewBtn.IsVisible(),"AddNew button should be present");
                        Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.DeleteBtn.IsVisible(),"AddNew button should be present");
                        Assert.IsTrue(Pages.PS_IdeaHopperConfigurationPage.SubmitAnIdeaLink.IsVisible(),"AddNew button should be present");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Important Links page")]
        public void TST_NAV_UI_052_CodedStep23()
        {
                        Assert.IsTrue(Pages.PS_ManageImportantLinksPage.AddNewBtn.IsVisible(),"AddNew button should be displayed");
                        Assert.IsTrue(Pages.PS_ManageImportantLinksPage.DeleteBtn.IsVisible(),"Delete button should be displayed");
                        Assert.IsTrue(Pages.PS_ManageImportantLinksPage.UpdateLink.IsVisible(),"Update link should be displayed");
        }
    
        [CodedStep(@"Verify user is navigated to Mass Change % Complte Method page")]
        public void TST_NAV_UI_052_CodedStep24()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Mass change % complete method"));
                        Assert.IsTrue(Pages.PS_MassChangeCompletePage.CMDropDownList.IsVisible(),"Complete method dropdown list should be present");
                        Assert.IsTrue(Pages.PS_MassChangeCompletePage.GoBtn.IsVisible(),"Complete method dropdown list should be present");
                        Assert.IsTrue(Pages.PS_MassChangeCompletePage.PortfolioDropDownList.IsVisible(),"Complete method dropdown list should be present");
                        
                        
        }
    
        [CodedStep(@"Verify user is navigated to Object Type page")]
        public void TST_NAV_UI_052_CodedStep25()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Object Types"));
                        Assert.IsTrue(Pages.PS_ObjectTypesPage.ObjectTypesTableDiv.IsVisible(),"Object type table should be displayed");
        }
    
        [CodedStep(@"Verify user is navigated to Phase Advance page")]
        public void TST_NAV_UI_052_CodedStep26()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Phase Advance Configuration"));
                        Assert.IsTrue(Pages.PS_PhaseAdvanceConfigurationPage.SaveBtn.IsVisible(),"save button should be present");
                        Assert.IsTrue(Pages.PS_PhaseAdvanceConfigurationPage.ResetLink.IsVisible(),"Reset button should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Processes page")]
        public void TST_NAV_UI_052_CodedStep27()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Processes"));
                        Assert.IsTrue(Pages.PS_ProcessesPage.AddNewProcessBtn.IsVisible(),"AddNewProcess button should be displayed");
                        Assert.IsTrue(Pages.PS_ProcessesPage.DeleteBtn.IsVisible(),"Delete button should be displayed");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Resource Calender page")]
        public void TST_NAV_UI_052_CodedStep28()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Calendar"));
                        Assert.IsTrue(Pages.PS_ResourceCalendarPage.AddNewBtn.IsVisible(),"AddNew button should be displayed");
                        Assert.IsTrue(Pages.PS_ResourceCalendarPage.EditLink.IsVisible(),"Edit link should be displayed");
                        
                        
        }
    
        [CodedStep(@"Verify user is navigated Resource Planning page")]
        public void TST_NAV_UI_052_CodedStep29()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Resource Planning"));
                        Assert.IsTrue(Pages.PS_ResourcePlanningPage.CancelBtn.IsVisible(),"Cancel button should be dispaled");
                        Assert.IsTrue(Pages.PS_ResourcePlanningPage.SaveBtn.IsVisible(),"Save button should be dispaled");
                        
                        
        }
    
        [CodedStep(@"Verify User is navigated to Search Synonyms page")]
        public void TST_NAV_UI_052_CodedStep30()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Search Synonyms"));
                        Assert.IsTrue(Pages.PS_SearchSynonymsPage.CancelBtn.IsVisible(),"Cancel button should be displayed");
                        Assert.IsTrue(Pages.PS_SearchSynonymsPage.InstructionTextarea.IsVisible(),"Instruction test area should be present");
                        Assert.IsTrue(Pages.PS_SearchSynonymsPage.SaveBtn.IsVisible(),"Save button should be displayed");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Staus Report Template page")]
        public void TST_NAV_UI_052_CodedStep31()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Status Report Templates"));
                        Assert.IsTrue(Pages.PS_StatusReportTemplatesPage.AddNewBtn.IsVisible(),"Addnew button should be displayed");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Tags List page")]
        public void TST_NAV_UI_052_CodedStep32()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Tags List"));
                        Assert.IsTrue(Pages.PS_TagsListPage.AddNewTagBtn.IsVisible(),"AddNew button should be displayed");
                        Assert.IsTrue(Pages.PS_TagsListPage.AllTagGroupsLink.IsVisible(),"All Groups link should be displayed");
                        Assert.IsTrue(Pages.PS_TagsListPage.DepedenciesLink.IsVisible(),"Dependencies link should be displayed");
                        
        }
    
        [CodedStep(@"Verify user is navigated to User Creation Options page")]
        public void TST_NAV_UI_052_CodedStep33()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("User Creation Options"));
                        Assert.IsTrue(Pages.PS_UserCreationOptionsPage.SaveBtn.IsVisible(),"Save button should be displayed");
        }
    
        [CodedStep(@"Verify user is navigated to Work Templates Visibility page")]
        public void TST_NAV_UI_052_CodedStep34()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Work Template Visibility Configuration"));
                        Assert.IsTrue(Pages.PS_WorkTemplateVisibilityPage.SaveBtn.IsVisible(),"Save button should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Logs page")]
        public void TST_NAV_UI_052_CodedStep35()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Logs"));
                        Assert.IsTrue(Pages.PS_LogsPage.GoBtn.IsVisible(),"Go buttun should be visible");
                        Assert.IsTrue(Pages.PS_LogsPage.ViewDropDownList.IsVisible(),"View drop down list should be visible");
                        
        }
    
        [CodedStep(@"Verify user is navigated to Announcements page ")]
        public void TST_NAV_UI_052_CodedStep36()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Announcement"));
                        Assert.IsTrue(Pages.PS_AnnouncementPage.AnnounceContainerFrame.SubmitLink.IsVisible(),"Submit button should be present");
        }
    
        [CodedStep(@"Verify user is navigated to Related Work page")]
        public void TST_NAV_UI_052_CodedStep37()
        {
                        Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Related Work"));
                        
        }
    }
}
