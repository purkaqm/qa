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

    public class TST_MET_014 : BaseWebAiiTest
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
    
      
    
        [CodedStep(@"Disable Metric Loader feature")]
        public void TST_MET_014_CodedStep1()
        {
            Pages.PS_UIXEditPage.MetricLoaderSelect.SelectByValue("off",true);
            Pages.PS_UIXEditPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Navigate to context home page")]
        public void TST_MET_012_CodedStep1()
        {
                         ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString()), true);
                         Manager.ActiveBrowser.WaitUntilReady();            
        }
    
 
    
        [CodedStep(@"Verify that Metric Loader link not available")]
        public void TST_MET_014_CodedStep2()
        {
            
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("review_imp_exp_metric_looader_tab")).Count == 0, "Financial/Metric Loader link should not be available");
        }
    
        [CodedStep(@"Enable Metric Loader feature")]
        public void TST_MET_014_CodedStep()
        {
             Pages.PS_UIXEditPage.MetricLoaderSelect.SelectByValue("on",true);
            Pages.PS_UIXEditPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify that Metric Loader link is availabe in left panel")]
        public void TST_MET_014_CodedStep3()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("review_imp_exp_metric_looader_tab")).Count > 0, "Financial/Metric Loader link should be available");
        }
    
        [CodedStep(@"Verify all elements on metric loader page(check boxes and file uploader) are present on the page")]
        public void TST_MET_014_CodedStep4()
        {
             Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.ZeroEmptyCheckbox.IsVisible(),"Treat zero values as empty checkbox should be present");
            Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.RemoveEmptyValsCheckbox.IsVisible(),"Remove empty values checkbox should be present");
            Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.AutoAddBenefCheckbox.IsVisible(),"Automatically add beneficiaries to Financials checkbox should be present");
            Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.CancelUploadOnErrCheckbox.IsVisible(),"Cancel upload if any errors checkbox should be present");
           // Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.FileInputBox.IsVisible(),"File uploader control should be present");
            Assert.IsTrue(Pages.PS_ReviewLoadFinancialsPage.FileLoadButton.IsVisible(),"Load button should be present");
        }
    }
}
