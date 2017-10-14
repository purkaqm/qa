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

    public class TST_NAV_UI_007 : BaseWebAiiTest
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
        public void TST_NAV_UI_007_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible(),"search icon should present");
            Pages.PS_HomePage.SearchLink.Click();
            Pages.PS_HomePage.SearchTypeImgIconSpan.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchInputText.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchAdvancedDiv.IsVisible());
            
            Assert.IsTrue(Pages.PS_HomePage.SearchInputText.BaseElement.GetAttribute("maxlength").Value.Equals("250"),"should have 250px witdh"); 
            
            ActiveBrowser.Window.SetFocus();
            Pages.PS_HomePage.SearchDownTraingleSpan.Focus();
            Pages.PS_HomePage.SearchDownTraingleSpan.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();
            string[] arr = {"All","Projects","Organizations","Works","People","Ideas","Discussions","Issues","Documents"};
            int len=arr.Count();
            for(int i=0;i<len;i++)
            {
                Log.WriteLine(i.ToString());
                string elementLocator = string.Format("//span[@class='qs_item_name' and contains(.,'{0}')]",arr[i]);  
                HtmlSpan myelement = ActiveBrowser.Find.ByXPath<HtmlSpan>(elementLocator);
                Assert.IsTrue(myelement.IsVisible()," element should be visible");
            }
             
         }
    }
}
