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

    public class TST_MAT_100_C992399 : BaseWebAiiTest
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
        public void TST_MAT_100_C992399_ClickAddNewButton()
        {
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Fill the details")]
        public void TST_MAT_100_C992399_FillDetails()
        {
            //Enter url
            Actions.SetText(Pages.PS_ManageImportantLinksPage.UrlInputText,Data["URL"].ToString());
            
            //Enter title
            titleLink = Data["TITLE"].ToString() + Randomizers.generateRandomInt(1000,9999);
            Actions.SetText(Pages.PS_ManageImportantLinksPage.TitleInputText,titleLink);
                                    
        }
    
        [CodedStep(@"Click on Add Document button")]
        public void TST_MAT_100_C992399_ClickAddDocBtn()
        {
            Pages.PS_ManageImportantLinksPage.AddDocumentButton.Click();
        }
    
        [CodedStep(@"Click on Done button")]
        public void TST_MAT_100_C992399_ClickDoneBtn()
        {
            Pages.PS_ManageImportantLinksPage.DoneButton.Wait.ForExists(30000);
            Pages.PS_ManageImportantLinksPage.DoneButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify link is created on important link page")]
        public void TST_MAT_100_C992399_VerifyLink()
        {
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            HtmlAnchor impLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='titleColumnValue']/a[contains(.,'{0}')]",titleLink));   
            impLink.Wait.ForExists();
            Assert.IsTrue(impLink.IsVisible(),"Link should be visible");
                                    
        }
    
        [CodedStep(@"Verify added link is displayed in the left menu")]
        public void TST_MAT_100_C992399_VerifyLinkLeftMenu()
        {
            Pages.PS_HomePage.ImportantLinksLeftNavLink.MouseHover();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.ManageImpLinksLeftNavDiv.Wait.ForExists();
            Pages.PS_HomePage.ManageImpLinksLeftNavDiv.Wait.ForVisible();
            ActiveBrowser.RefreshDomTree();
            HtmlAnchor impLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//div[@id='MenuContent']//a[@title='{0}']",titleLink));
            impLink.Wait.ForExists();
            Assert.IsTrue(impLink.IsVisible(),"Added link should be visible");
        }
    
        [CodedStep(@"Delete the generated link")]
        public void TST_MAT_100_C992399_DeleteLink()
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
