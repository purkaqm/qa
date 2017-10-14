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

    public class TS_Display_Percentage_Completed_Column : BaseWebAiiTest
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
    
        [CodedStep(@"Verify 'aria-pressed' attribute 'Same' value 'true' on 'PsFormCheckBox36CheckBox'")]
        public void TS_Display_Percentage_Completed_Column_CodedStep()
        {
            Pages.PS_ProjectPlanningLayoutPage.GridColumnsDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectsArrowImg.Wait.ForVisible();
            Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectsArrowImg.Click();
            Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox.Wait.ForVisible();
            string pressedAttr = Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox.Attributes.Single(x => x.Name == "aria-pressed").Value;
            if(pressedAttr.Equals("true")){
                Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox.Click();
                Wait.For<HtmlInputCheckBox>(c => c.AssertAttribute(false).Value("aria-pressed", ArtOfTest.Common.StringCompareType.Same, "false"), Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox, Manager.Settings.ElementWaitTimeout);
                Pages.PS_ProjectPlanningLayoutPage.GridColumnsDiv.Click();
                this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                Pages.PS_ProjectPlanningLayoutPage.GridColumnsDiv.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
                Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectsArrowImg.Wait.ForVisible();
                Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectsArrowImg.Click();
                Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox.Wait.ForVisible();
            } 
            Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox.Click();
            Wait.For<HtmlInputCheckBox>(c => c.AssertAttribute(false).Value("aria-pressed", ArtOfTest.Common.StringCompareType.Same, "true"), Pages.PS_ProjectPlanningLayoutPage.ColumnsProjectPercComplCheckBox, Manager.Settings.ElementWaitTimeout);
            Pages.PS_ProjectPlanningLayoutPage.GridColumnsDiv.Click();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
               
    
        }
    }
}
