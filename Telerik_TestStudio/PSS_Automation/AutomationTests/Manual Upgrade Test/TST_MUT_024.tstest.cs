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

    public class TST_MUT_024 : BaseWebAiiTest
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
    
        [CodedStep(@"Set project name")]
        public void TST_MUT_024_SetName()
        {
            /// \bug Move to a central place
            CustomUtils.locationValue = "Project01";
        }
    
        [CodedStep(@"Verify Menu bar and Quick feature icon is displayed")]
        public void TST_MUT_024_VerifyElements()
        {
            Pages.PS_ProjectPlanningProjectCentralPage.ToolBarAreaDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.LayoutsDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.DisplayDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.ColumnsDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.FiltersDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.OptionsDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.CurrentLayoutAcronymTag.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.ActionsComboBoxDiv.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.ApplyButton.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.JumpToTodayImage.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.RunSchedulerImage.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.PrintImage.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.RunResizingImage.Wait.ForExists();
            Pages.PS_ProjectPlanningProjectCentralPage.ExpandCollapseImage.Wait.ForExists();
            
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.ToolBarAreaDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.LayoutsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.DisplayDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.ColumnsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.FiltersDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.OptionsDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.CurrentLayoutAcronymTag.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.ActionsComboBoxDiv.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.ApplyButton.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.JumpToTodayImage.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.RunSchedulerImage.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.PrintImage.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.RunResizingImage.IsVisible());
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.ExpandCollapseImage.IsVisible());
            
            /// \bug Should not be locale specific
            Assert.IsTrue(Pages.PS_ProjectPlanningProjectCentralPage.CurrentLayoutAcronymTag.BaseElement.InnerText.Contains("Project planning"));
            
        }
    }
}
