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

    public class TST_NAV_UI_034 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify User is navigated to Create Other Work Page")]
        public void TST_NAV_UI_034_CodedStep()
        {
            Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.Wait.ForExists();
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkDescriptionDiv.IsVisible());
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkContinueBtn.IsVisible());
            Assert.IsTrue(Pages.PS_CreateOtherWorkPage.AddOtherWorkCancelBtn.IsVisible());

        }

        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_034_CodedStep1()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
           
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_034_CodedStep2()
        {
            string workName = Data["WorkName"].ToString();
            if(workName.Length >= 90){
                string textOverflowProperty = Pages.PS_HomePage.PageTitleDiv.GetComputedStyleValue("text-overflow");    
                Assert.IsTrue(textOverflowProperty.Equals("ellipsis"),"Long page title should be truncated...");
            }
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_034_CodedStep3()
        {
            string workName = Data["WorkName"].ToString();
            Pages.PS_HomePage.PageTitleDiv.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.Attributes.Single(x => x.Name == "title").Value.Contains(workName),"On hovering the page title, it should display full page title");
        }
    
    }
}
