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

    public class TST_NAV_UI_011 : BaseWebAiiTest
    {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

      
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
        
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_011_CodedStep()
        {
        
            string[] arr = {"Projects","Organizations","Works","People","Ideas","Discussions","Issues","Documents"};
            int len=arr.Count();
            for(int i=0;i<len;i++)
            { 
                Pages.PS_HomePage.SearchLink.Wait.ForExists();
                Pages.PS_HomePage.SearchLink.Click();
                Pages.PS_HomePage.SearchDownTraingleSpan.Wait.ForVisible();
                ActiveBrowser.Window.SetFocus();
                Pages.PS_HomePage.SearchDownTraingleSpan.Focus();
                Pages.PS_HomePage.SearchDownTraingleSpan.MouseClick(MouseClickType.LeftClick);
                ActiveBrowser.RefreshDomTree();
                Pages.PS_HomePage.SearchDownTraingleSpan.MouseClick(MouseClickType.LeftClick);
                System.Threading.Thread.Sleep(4000);
                string elementLocator = string.Format("//span[@class='qs_item_name' and contains(.,'{0}')]",arr[i]);  
                HtmlSpan myElement = ActiveBrowser.Find.ByXPath<HtmlSpan>(elementLocator);
                Assert.IsTrue(myElement.IsVisible()," element should be visible");
                myElement.Click();
                Pages.PS_HomePage.SearchInputTextBackgroundSpan.Wait.ForContent(FindContentType.InnerText,arr[i]);
                Pages.PS_HomePage.SearchInputText.Focus();
                Manager.Desktop.KeyBoard.TypeText("test",2);
                Pages.PS_HomePage.SearchResultsTable.Wait.ForVisible();
                
                System.Threading.Thread.Sleep(4000);
                string elementCell =  string.Format("//th[contains(.,'{0}')]",arr[i]);
                HtmlTableCell elementCellLoc =  ActiveBrowser.Find.ByXPath<HtmlTableCell>(elementCell);
                if(!elementCellLoc.IsVisible()){
                     Assert.IsTrue(Pages.PS_HomePage.SearchResultsTable.InnerText.Contains("No results found."),string.Format(" {0} search item should be displayed",arr[i]));
                }else{
                    Assert.IsTrue(elementCellLoc.IsVisible(), string.Format(" {0} search item should be displayed",arr[i]));
                }
                
                ActiveBrowser.Refresh();
                ActiveBrowser.WaitUntilReady();
                   
            }
             
        }
    
    }
}
