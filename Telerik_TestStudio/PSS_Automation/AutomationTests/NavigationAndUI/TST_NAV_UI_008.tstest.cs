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

    public class TST_NAV_UI_008 : BaseWebAiiTest
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
        public void TST_NAV_UI_008_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.SearchLink.IsVisible(),"search icon should present");
            Pages.PS_HomePage.SearchLink.Click();
            Pages.PS_HomePage.SearchTypeImgIconSpan.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchInputText.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchAdvancedDiv.IsVisible());
            Pages.PS_HomePage.SearchAdvancedDiv.Click();
            
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(5000);
            string[] arr = {"All","Projects","Organizations","Works","People","Ideas","Discussions","Issues","Documents"};
            int len = arr.Count();
            
            for(int i=0;i<len;i++)
            {
                string locator = string.Format("//ul[@class='clearfix']//a[contains(.,'{0}')]",arr[i]);
                ActiveBrowser.WaitForElement(new HtmlFindExpression("xpath="+locator), 3000,false);
                
            }
            
            
        }
    }
}
