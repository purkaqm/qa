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

    public class TST_NAV_UI_024 : BaseWebAiiTest
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
        public void TST_NAV_UI_023_CodedStep()
        {
            ActiveBrowser.RefreshDomTree();
            if(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Add: Submit New Idea"))
            {
                Assert.IsTrue(Pages.PS_SubmitAnIdeaPage.AddIdeaNameInputText.IsVisible());
                //Assert.IsTrue(Pages.PS_SubmitAnIdeaPage.AddIdeaYourNameInputText.IsVisible()); 
            }
            else
            {
                Assert.IsTrue(Pages.SubmitAnIdeaPage.ChooseCategorySelect.IsVisible(),"choose a category dropdown select should be present");
                Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Submit an Idea");
            }
        }
    }
}
