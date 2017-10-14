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

    public class TST_NAV_UI_055 : BaseWebAiiTest
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
        public void TST_NAV_UI_055_CodedStep1()
        {
            
            Pages.PS_HomePage.ProjectNominateAsBestPracTab.Wait.ForVisible();
            Pages.PS_HomePage.ProjectNominateAsBestPracTab.Click();
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep2()
        {
            Pages.PS_NominateAsPopUpWindow.NominateAsPopUpDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.NominateAsPopUpDiv.IsVisible(),"Pop up window should be open");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.NominateButton.IsVisible(),"In pop up window nomainate button should be present");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.CancelButton.IsVisible(),"In pop up window cancel button should be present");
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.CommentsArea.IsVisible(),"In pop up window comment text area should be present");
                
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep3()
        {
            Actions.SetText(Pages.PS_NominateAsPopUpWindow.CommentsArea,"test");
            Pages.PS_NominateAsPopUpWindow.NominateButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep4()
        {
            Pages.PS_NominateAsPopUpWindow.BestPracticeLink.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_NominateAsPopUpWindow.BestPracticeLink.IsVisible(),"Best Practice link should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep5()
        {
            Pages.PS_NominateAsPopUpWindow.BestPracticeLink.Focus();
            Pages.PS_NominateAsPopUpWindow.BestPracticeLink.Click();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_NominateAsPopUpWindow.OptionsLink.Wait.ForExists();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep6()
        {
            Pages.PS_NominateAsPopUpWindow.OptionsLink.Click();
            Pages.PS_NominateAsPopUpWindow.ApproveButton.Wait.ForVisible();
            Pages.PS_NominateAsPopUpWindow.RejectButton.Wait.ForVisible();
            Pages.PS_NominateAsPopUpWindow.CancelNominationButton.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_055_CodedStep7()
        {
            Pages.PS_NominateAsPopUpWindow.CancelNominationButton.Click();
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"Wait for user to be navigated to newly created work summary page")]
        public void TST_NAV_UI_055_CodedStep()
        {
            Pages.PS_ProjectSummaryPage.ProjectSummaryContentDiv.Wait.ForVisible();
            Pages.PS_ProjectSummaryPage.ProjectSummaryDescendantsDiv.Wait.ForVisible();
               
        }
    }
}
