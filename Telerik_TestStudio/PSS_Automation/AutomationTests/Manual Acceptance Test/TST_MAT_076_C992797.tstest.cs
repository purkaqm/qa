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

    public class TST_MAT_076_C992797 : BaseWebAiiTest
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
    
        [CodedStep(@"Navigate to Home page")]
        public void TST_MAT_076_C992797_NavigateToHomePage()
        {
            ActiveBrowser.NavigateTo("~/Home.page");
        }
    
        [CodedStep(@"Verify 'Object Type' is present in select work item list to create organazation ")]
        public void TST_MAT_076_C992797_verifyWorkType()
        {
            string workType = Data["WorkType"].ToString();

            IList<HtmlOption> options = Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.Options;
            bool foundflag = false;
            foreach(HtmlOption option in options){
                if(option.BaseElement.InnerText.Trim().Equals(workType)){
                    foundflag = true;
                    break;
                }
            }
            Assert.IsTrue(foundflag, workType + " is not present in dropdown");
                        
        }
        
        [CodedStep(@"Verify 'Object Type' is not present in select work item list to create organazation ")]
        public void TST_MAT_076_C992797_verifyWorkTypeNotPresent()
        {
            string workType = Data["WorkType"].ToString();

            IList<HtmlOption> options = Pages.PS_CreateOtherWorkPage.AddOtherWorkWorkTypeSelect.Options;
            bool foundflag = false;
            foreach(HtmlOption option in options){
                if(option.BaseElement.InnerText.Trim().Equals(workType)){
                    foundflag = true;
                    break;
                }
            }
            Assert.IsFalse(foundflag, workType + " is present in dropdown");
                        
        }
    }
}
