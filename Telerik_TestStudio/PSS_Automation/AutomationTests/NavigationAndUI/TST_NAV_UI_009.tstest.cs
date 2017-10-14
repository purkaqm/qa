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

    public class TST_NAV_UI_009 : BaseWebAiiTest
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
        public void TST_NAV_UI_009_CodedStep()
        {
            Pages.PS_HomePage.SearchLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible()," search link should be visible");
            Pages.PS_HomePage.SearchLink.Click(true);
            Pages.PS_HomePage.SearchInputText.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.SearchInputText.IsVisible(),"input text area should be visible ");
            
            Assert.IsTrue(Pages.PS_HomePage.SearchInputTextBackgroundSpan.IsVisible(),"background span should be present");
            string elementText = Pages.PS_HomePage.SearchInputTextBackgroundSpan.InnerText;
            Assert.IsTrue(elementText=="Search...","element text should be 'search...'");
            
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_HomePage.SearchInputText.MouseClick(MouseClickType.LeftClick);
            Pages.PS_HomePage.SearchInputText.Focus();
            Manager.Desktop.KeyBoard.TypeText("project",2);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_HomePage.ClearInputTextSearchImage.Wait.ForExists();
            Pages.PS_HomePage.ClearInputTextSearchImage.MouseClick(MouseClickType.LeftClick);
            Pages.PS_HomePage.ClearInputTextSearchImage.Wait.ForVisibleNot();
 
        }
    
    
    }
}
