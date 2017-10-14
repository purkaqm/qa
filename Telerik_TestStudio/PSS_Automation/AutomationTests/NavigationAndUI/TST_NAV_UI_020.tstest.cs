using Telerik.TestingFramework.Controls.KendoUI;
using Telerik.WebAii.Controls.Html;
using Telerik.WebAii.Controls.Xaml;
using System;
using System.Collections.Generic;
using System.Text;
using System.Linq;
using ArtOfTest.WebAii.Win32.Dialogs;
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

    public class TST_NAV_UI_020 : BaseWebAiiTest
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
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_020_CodedStep()
        //{
            //Pages.PS_HomePage.ProjectLeftNavLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //string headerText = Pages.PS_HomePage.PageTitleDiv.InnerText.Split(':')[0];
            //HtmlAnchor locationElement = ActiveBrowser.Find.ByXPath<HtmlAnchor>(AppLocators.get("Where_am_i_icon_lastrow"));
            //string locationText = locationElement.InnerText;
            //Pages.PS_ProjectTestSummary.WhereAmILink.Wait.ForVisible();
            //Pages.PS_ProjectTestSummary.WhereAmILink.Click();
            //ActiveBrowser.WaitUntilReady();
            
            //Assert.IsTrue(headerText.Trim().Equals(locationText),"text name should be same");
            
            
            
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep1()
        {
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep2()
        {
            
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.WhereAmISpan.IsVisible(),"link should be present");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep3()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_ProjectSummaryPage.WhereAmISpan.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectSummaryPage.WhereAMIMenuDiv.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep4()
        {
            string elememtColor = Data["Expected_color"].ToString();
            string eColor = Pages.PS_ProjectSummaryPage.WhereAmISpan.GetComputedStyleValue("color");
            Log.WriteLine(eColor);
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.WhereAmISpan.GetComputedStyleValue("color").Equals(elememtColor),"element color should be white");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep()
        {
            
            Pages.PS_ProjectSummaryPage.WhereAmISpan.MouseHover();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep5()
        {
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.WhereAMIMenuDiv.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep6()
        {
            string headerText = Pages.PS_HomePage.PageTitleDiv.InnerText.Split(':')[0];
            HtmlAnchor locationElement = ActiveBrowser.Find.ByXPath<HtmlAnchor>(AppLocators.get("Where_am_i_icon_lastrow"));
            string locationText = locationElement.InnerText;
            Assert.IsTrue(headerText.Trim().Equals(locationText),"text name should be same");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep7()
        {
            Pages.PS_ProjectSummaryPage.DetailsEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_EditProjectDetailsPage.EditWorkSaveChangesBtn.Wait.ForVisible();
            Pages.PS_EditProjectDetailsPage.EditWorkNameInput.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep8()
        {
            Pages.PS_EditProjectDetailsPage.EditWorkNameInput.MouseClick(MouseClickType.LeftClick);
            Pages.PS_EditProjectDetailsPage.EditWorkNameInput.Focus();
            Manager.Desktop.KeyBoard.TypeText("sample test",2);
            ActiveBrowser.RefreshDomTree();
            
        }
    
        //[CodedStep(@"Hover on 'Where am i' icon")]
        //public void TST_NAV_UI_020_CodedStep9()
        //{
            
            //Pages.PS_ProjectTestSummary.WhereAmISpan.MouseHover();
        //}
    
        //[CodedStep(@"Verify 'Where am i' icon highlited  by white color")]
        //public void TST_NAV_UI_020_CodedStep10()
        //{
            //string elememtColor = Data["Expected_color"].ToString();
            //string eColor = Pages.PS_ProjectTestSummary.WhereAmISpan.GetComputedStyleValue("color");
            //Log.WriteLine(eColor);
            //Assert.IsTrue(Pages.PS_ProjectTestSummary.WhereAmISpan.GetComputedStyleValue("color").Equals(elememtColor),"element color should be white");
        //}
    
        //[CodedStep(@"Click on 'Where am i' icon link")]
        //public void TST_NAV_UI_020_CodedStep11()
        //{
            //ActiveBrowser.RefreshDomTree();
            //ActiveBrowser.Window.SetFocus();
            //Pages.PS_ProjectTestSummary.WhereAmISpan.MouseClick(MouseClickType.LeftClick);
            //ActiveBrowser.WaitUntilReady();
            
        //}
    
        [CodedStep(@"Hover on 'Where am i' icon")]
        public void TST_NAV_UI_020_CodedStep9()
        {
            
            Pages.PS_ProjectSummaryPage.WhereAmISpan.MouseHover();
        }
    
        [CodedStep(@"Verify 'Where am i' icon highlited  by white color")]
        public void TST_NAV_UI_020_CodedStep10()
        {
            string elememtColor = Data["Expected_color"].ToString();
            string eColor = Pages.PS_ProjectSummaryPage.WhereAmISpan.GetComputedStyleValue("color");
            Log.WriteLine(eColor);
            Assert.IsTrue(Pages.PS_ProjectSummaryPage.WhereAmISpan.GetComputedStyleValue("color").Equals(elememtColor),"element color should be white");
        }
    
        [CodedStep(@"Click on 'Where am i' icon link")]
        public void TST_NAV_UI_020_CodedStep11()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_ProjectSummaryPage.WhereAmISpan.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ProjectSummaryPage.WhereAMIMenuDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_020_CodedStep12()
        {
            
            
            OnBeforeUnloadDialog dialog = OnBeforeUnloadDialog.CreateOnBeforeUnloadDialog(ActiveBrowser, DialogButton.CANCEL);
            Manager.DialogMonitor.AddDialog(dialog);
            
            HtmlAnchor otherParentProjLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(AppLocators.get("Where_am_i_icon_firstrow"));
            otherParentProjLink.MouseClick();
            
            dialog.WaitUntilHandled(5000);
            ActiveBrowser.WaitUntilReady();
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_020_CodedStep13()
        //{
            
            ////Settings.Current.UnexpectedDialogAction = UnexpectedDialogAction.HandleAndContinue;
        //}
    
        //[CodedStep(@"Enter text 'testsample' in 'NameText'")]
        //public void TST_NAV_UI_020_CodedStep13()
        //{
            //// Enter text 'testsample' in 'NameText'
            //Actions.SetText(Pages.TestEditDetails.NameText, "testsample");
            
        //}
    
        //[CodedStep(@"Enter text 'testsample' in 'NameText'")]
        //public void TST_NAV_UI_020_CodedStep14()
        //{
            //// Enter text 'testsample' in 'NameText'
            //Actions.SetText(Pages.TestEditDetails.NameText, "testsample");
            
        //}
    
        //[CodedStep(@"Enter text 'testsample' in 'NameText'")]
        //public void TST_NAV_UI_020_CodedStep15()
        //{
            //// Enter text 'testsample' in 'NameText'
            //Actions.SetText(Pages.TestEditDetails.NameText, "testsample");
            
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_020_CodedStep13()
        //{
              //System.Threading.Thread.Sleep(3000);
              //Manager.Desktop.KeyBoard.KeyPress(System.Windows.Input., 200);
 
       
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_020_CodedStep14()
        //{
            //Settings.Current.UnexpectedDialogAction = UnexpectedDialogAction.HandleAndContinue;
        //}
    
        [CodedStep(@"Click Cancel and go to Home page")]
        public void TST_NAV_UI_020_CodedStep13()
        {
            
            OnBeforeUnloadDialog dialog = OnBeforeUnloadDialog.CreateOnBeforeUnloadDialog(ActiveBrowser, DialogButton.OK);
            Manager.DialogMonitor.AddDialog(dialog);
            
            Pages.PS_HomePage.HomeLeftNavLink.Click();
            dialog.WaitUntilHandled(5000);
            ActiveBrowser.WaitUntilReady();
        }
    }
}
