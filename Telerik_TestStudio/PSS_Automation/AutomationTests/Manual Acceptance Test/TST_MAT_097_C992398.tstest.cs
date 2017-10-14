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

    public class TST_MAT_097_C992398 : BaseWebAiiTest
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
        
        string titleLink;
    
        [CodedStep(@"Click on Add new button")]
        public void TST_MAT_097_C992398_ClickAddNewButton()
        {
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Fill the details")]
        public void TST_MAT_097_C992398_FillDetails()
        {
            //Enter url
            Actions.SetText(Pages.PS_ManageImportantLinksPage.UrlInputText,Data["URL"].ToString());
            
            //Enter title
            titleLink = Data["TITLE"].ToString() + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_ManageImportantLinksPage.TitleInputText,titleLink);
                        
        }
    
        [CodedStep(@"Click on Add Document button")]
        public void TST_MAT_097_C992398_ClickAddDocBtn()
        {
            Pages.PS_ManageImportantLinksPage.AddDocumentButton.Click();
        }
    
        [CodedStep(@"Click on Done button")]
        public void TST_MAT_097_C992398_ClickDoneBtn()
        {
            Pages.PS_ManageImportantLinksPage.DoneButton.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.DoneButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify link is created and Click on that link")]
        public void TST_MAT_097_C992398_VerifyLink()
        {
            ActiveBrowser.RefreshDomTree();
            //System.Threading.Thread.Sleep(3000);
            HtmlAnchor impLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='titleColumnValue']/a[contains(.,'{0}')]",titleLink));   
            impLink.Wait.ForExists();
            Assert.IsTrue(impLink.IsVisible(),"Link should be visible");
            impLink.MouseClick(MouseClickType.LeftClick);
                        
        }
        
        [CodedStep(@" Click to Open link")]
        public void TST_MAT_097_C992398_ClickOPenLink()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ManageImportantLinksPage.OpenLink.Wait.ForExists();
            Pages.PS_ManageImportantLinksPage.OpenLink.Click();
                        
        }
    
        [CodedStep(@"Connect to pop-up window")]
        public void TST_MAT_097_C992398_ConnectNewWindow()
        {
            // Connect to pop-up window
            Manager.WaitForNewBrowserConnect(Data["URL"].ToString(), true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Connect to pop-up window : 'https://uplandsoftware.com/'")]
        public void TST_MAT_097_C992398_CodedStep1()
        {
            // Connect to pop-up window : 'https://uplandsoftware.com/'
            Manager.WaitForNewBrowserConnect("https://uplandsoftware.com/", true, 15000);
            Manager.ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Verify navigate to correct link")]
        public void TST_MAT_097_C992398_VerifyNavigationLink()
        {
            Assert.IsTrue(ActiveBrowser.Url.Contains(Data["URL"].ToString()));
            ActiveBrowser.Close();
        }
    
        [CodedStep(@"Delete the generated link")]
        public void TST_MAT_097_C992398_DeleteLink()
        {
        ActiveBrowser.RefreshDomTree();
        HtmlInputCheckBox impLinkCkhbox = ActiveBrowser.Find.ByXPath<HtmlInputCheckBox>(string.Format("//tr[contains(.,'{0}')]/td[@class='updateColumnValue last']//input",titleLink));
        impLinkCkhbox.Check(true);
        Pages.PS_ManageImportantLinksPage.DeleteBtn.Click();
        ActiveBrowser.RefreshDomTree();
        Pages.PS_ManageImportantLinksPage.DeleteYesButton.Wait.ForExists(30000);
        Pages.PS_ManageImportantLinksPage.DeleteYesButton.Click();
        ActiveBrowser.WaitUntilReady();
                        
        }
    }
}
