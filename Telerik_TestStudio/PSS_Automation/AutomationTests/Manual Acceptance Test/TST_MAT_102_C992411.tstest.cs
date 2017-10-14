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

    public class TST_MAT_102_C992411 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify existing measure is present")]
        public void TST_MAT_102_C992411_VerifyExistingMeasure()
        {
            HtmlDiv noMeasureMsg = ActiveBrowser.Find.ByXPath<HtmlDiv>("//form[@id='valuesForm']/div[2]");
            noMeasureMsg.Wait.ForExists();
            if(noMeasureMsg.BaseElement.InnerText.Contains("No library measures have been defined"))
                Log.WriteLine("No library measures have been defined");
                else
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath<HtmlTableRow>("//table[@id='PSTable']/tbody/tr").Count > 1);
            
        }
    }
}
