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

    public class TST_NAV_UI_047 : BaseWebAiiTest
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
        public void TST_NAV_UI_047_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.InnerText.Contains("Work Tree"),"User should be navigated to Work Tree page");
        }
    
        [CodedStep(@"Verify work tree page is displayed")]
        public void TST_NAV_UI_047_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.DetailsEditLink.IsVisible(),"User should be navigated to Project Summary Page");
        }
    
        [CodedStep(@"Verify Project Summary page is displayed")]
        public void TST_NAV_UI_047_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_EditProjectDetailsPage.EditWorkNameInput.IsVisible(),"User should be navigated to Project Edit Details Page");
        }
    
        [CodedStep(@"Verify Project Edit Details page is displayed")]
        public void TST_NAV_UI_047_CodedStep3()
        {
            Assert.IsTrue(Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.IsVisible(),"User should be navigated to Project Planning Page");
            Assert.IsTrue(Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.BaseElement.InnerText.Contains("Project planning"),"User should be navigated to Project Planning Page");
        }
    
        [CodedStep(@"Verify Project Planning page is displayed")]
        public void TST_NAV_UI_047_CodedStep4()
        {
            Assert.IsTrue(Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.IsVisible(),"User should be navigated to Resource Planning Page");
            Assert.IsTrue(Pages.PS_ProjectPlanningLayoutPage.CurrentLayoutAcronymTag.BaseElement.InnerText.Contains("Resource planning"),"User should be navigated to Resource Planning Page");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_047_CodedStep5()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Documents"),"User should be navigated to Documents Page");
        }
    
        [CodedStep(@"Verify Documents page is displayed")]
        public void TST_NAV_UI_047_CodedStep6()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Discussions"),"User should be navigated to Discussions Page");
        }
    
        [CodedStep(@"Verify Discussions page is displayed")]
        public void TST_NAV_UI_047_CodedStep7()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Issues"),"User should be navigated to Issues Page");
            Assert.IsTrue(Pages.PS_IssuesPage.NewIssueBtn.IsVisible(), "User should be navigated to Issues Page");
        }
    
        [CodedStep(@"Verify Issues page is displayed")]
        public void TST_NAV_UI_047_CodedStep8()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Metric"),"User should be navigated to Metric Templates Page");
            
        }
    
        [CodedStep(@"Verify Metric Templates page is displayed")]
        public void TST_NAV_UI_047_CodedStep9()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Measures"),"User should be navigated to Measures Page");
            
        }
    
        [CodedStep(@"Verify Measures page is displayed")]
        public void TST_NAV_UI_047_CodedStep10()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("References"),"User should be navigated to References Page");
            
        }
    
        [CodedStep(@"Verify References page is displayed")]
        public void TST_NAV_UI_047_CodedStep11()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Related Work"),"User should be navigated to Related Work Page");
            
        }
    
        [CodedStep(@"Verify Related Work page is displayed")]
        public void TST_NAV_UI_047_CodedStep12()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("History"),"User should be navigated to History Page");
            
        }
    
        [CodedStep(@"Verify History page is displayed")]
        public void TST_NAV_UI_047_CodedStep13()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Alert Subscriptions"),"User should be navigated to Alert Subscriptions Page");
            
        }
    
        [CodedStep(@"Verify Alert Subscription page is displayed")]
        public void TST_NAV_UI_047_CodedStep14()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Edit Permissions"),"User should be navigated to Edit Permissions Page");
            
        }
    
        [CodedStep(@"Verify Edit Permissions page is displayed")]
        public void TST_NAV_UI_047_CodedStep15()
        {
            Assert.IsTrue(Pages.PS_ProjectExportMSPPage.MSPMicrosoftVersionHeader.IsVisible(),"User should be navigated to MSP Export Page");
            
        }
    
        [CodedStep(@"Verify Edit Permissions page is displayed")]
        public void TST_NAV_UI_047_CodedStep16()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Edit Tags of Descendants"),"User should be navigated to Edit Tags of Descendants Page");
            
        }
    
        [CodedStep(@"Verify Edit Tags page is displayed")]
        public void TST_NAV_UI_047_CodedStep17()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Move"),"User should be navigated to Move Page");
            
        }
    }
}
