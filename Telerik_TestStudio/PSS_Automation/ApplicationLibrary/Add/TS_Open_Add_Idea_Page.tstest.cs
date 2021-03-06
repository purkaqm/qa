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

    public class TS_Open_Add_Idea_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Idea tab")]
        public void TS_Open_Add_Idea_Page_CodedStep()
        {
            Pages.PS_HomePage.AddIdeaTab.Click(true);
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Wait for Idea Page to be Loaded")]
        public void TS_Open_Add_Idea_Page_CodedStep1()
        {
            ActiveBrowser.RefreshDomTree();
            if(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Add: Submit New Idea"))
            {
                Pages.PS_SubmitAnIdeaPage.AddIdeaNameInputText.Wait.ForExists();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        
            }
            else
            {
                Pages.SubmitAnIdeaPage.ChooseCategorySelect.Wait.ForExists();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            }
            
        }
    }
}
