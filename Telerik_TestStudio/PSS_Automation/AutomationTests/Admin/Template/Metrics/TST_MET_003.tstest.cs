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

    public class TST_MET_003 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Metric Template Page is opened")]
        public void TST_MET_003_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        }
    
        [CodedStep(@"Click Create New link")]
        public void TST_MET_003_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForVisible();
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_003_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_003_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_003_CodedStep4()
        {
            int previousViewLineItems = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath("//table[@class='bgEdit']//table[2]//tr").Count;
            SetExtractedValue("PreviousViewLineItems",previousViewLineItems);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_003_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewAddLineItemsLink.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_003_CodedStep6()
        {
            int newViewLineItems = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath("//table[@class='bgEdit']//table[2]//tr").Count;
            int previousViewLineItems = Int32.Parse(GetExtractedValue("PreviousViewLineItems").ToString());
            Assert.IsTrue(newViewLineItems == (previousViewLineItems + 2),"Two view line items should be added to the list");
        }
    }
}
