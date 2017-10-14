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

    public class TST_MAT_094_C992812 : BaseWebAiiTest
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
        
        string measureName;
        
        // Add your test methods here...
        
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_MAT_094_C992812_WaitProjectSummaryPage()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForExists();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Summary"),"Page title should contains 'Summary' text");
                                                                                                                           
        }
    
        [CodedStep(@"Click on 'Manage Measures' on Nav Menu")]
        public void TST_MAT_094_C992812_ClickManageMeasure()
        {
            Pages.PS_ProjectSummaryPage.ProjectManageMeasuresTab.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
    
        [CodedStep(@"Create measure library template")]
        public void TST_MAT_094_C992812_CreateMeasureTemplate()
        {
            measureName = "X_measure_define" + Randomizers.generateRandomInt(1000,9999);
                            
            //Click on define new button
            Pages.PS_ProjectMeasuresPage.DefineNewButton.Click();
            ActiveBrowser.WaitUntilReady();
        
            //Enter name of library                
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.NameTextInput,measureName);
            SetExtractedValue("MeasureName",measureName);
        
            //Set Threashold1
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalIndTextInput,"10");
        
            //Set Threashold2
            Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalInd0Text,"20");
        
            //Click on submit button
            Pages.PS_MeasureTemplatesNewPage.SubmitButton.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
                            
                        
        }
    
        [CodedStep(@"Verify created measure library is appeared in Currently Attached measures table")]
        public void TST_MAT_094_C992812_VerifyMeasueTemplate()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
                            
        }
    
        [CodedStep(@"Verify 'On Summary page' column status is Yes")]
        public void TST_MAT_094_C992812_VerifySummaryPageStatus()
        {
            HtmlTableCell onSummaryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_on_summary_page_column_status"),measureName));
            Assert.AreEqual("Yes", onSummaryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Verify 'From Library' column status is empty")]
        public void TST_MAT_094_C992812_VerifyFromLibraryStatus()
        {
            HtmlTableCell fromLibraryCol = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("manage_measures_from_library_column_status"),measureName));
            Assert.AreNotEqual("Yes", fromLibraryCol.BaseElement.InnerText);
        }
    
        [CodedStep(@"Delete the Measure instance permanently through deleted_measure_instances.jsp page")]
        public void TST_MAT_094_C992812_DeleteMetricTemplatePermanently()
        {
            
            HtmlInputCheckBox deleteMeasureInstanceCheckbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//div[@class='deleted'][contains(.,'{0}')]/input",measureName));
            deleteMeasureInstanceCheckbox.Wait.ForExists();
            deleteMeasureInstanceCheckbox.ScrollToVisible();
            deleteMeasureInstanceCheckbox.Check(true);
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Click();
            Pages.PS_Deleted_Users_Page.PermanentlyDeleteUserProgressContainer.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Wait.ForVisible();
            Pages.PS_Deleted_Users_Page.PermanentlyDelUserOkButton.Click();
        }
    }
}
