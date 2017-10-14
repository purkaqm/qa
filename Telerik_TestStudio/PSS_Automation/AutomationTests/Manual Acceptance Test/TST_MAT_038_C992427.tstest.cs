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

    public class TST_MAT_038_C992427 : BaseWebAiiTest
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
    
        [CodedStep(@"Set Username for search")]
        public void TST_MAT_038_C992427_SetSearchItem()
        {
            CustomUtils.userName = "Pankaj Malviya";
        }
    
        [CodedStep(@"Click on Search icon on header bar")]
        public void TST_MAT_038_C992427_ClickSearchIcon()
        {
            Pages.PS_HomePage.SearchLink.Click();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(Pages.PS_HomePage.SearchTypeImgIconSpan.IsVisible());
            Assert.IsTrue(Pages.PS_HomePage.SearchDownTraingleSpan.IsVisible());
        }
    
        [CodedStep(@"Type Username value")]
        public void TST_MAT_038_C992427_TypeSearchValue()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.SearchInputText.Click();
            Manager.Desktop.KeyBoard.TypeText(CustomUtils.userName,1);
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            System.Threading.Thread.Sleep(5000);
                                    
        }
    
        [CodedStep(@"Verify Username is displayed at list of results")]
        public void TST_MAT_038_C992427_VerfyProject()
        {
            ActiveBrowser.RefreshDomTree();
            IList<HtmlListItem> searchResultList = ActiveBrowser.Find.AllByXPath<HtmlListItem>("//ul[contains(@id,'peopleSearchResults')]/li");
            Log.WriteLine(searchResultList.Count.ToString());
            bool flag = false;
            foreach(HtmlListItem listItem in searchResultList)
            {
                
                string userFirstName = listItem.BaseElement.InnerText.Split(',')[1];
                string userLastName = listItem.BaseElement.InnerText.Split(',')[0];
                Log.WriteLine(userFirstName);
                Log.WriteLine(userLastName);
                if(CustomUtils.userName.Contains(userFirstName) && CustomUtils.userName.Contains(userLastName))
                {
                    flag= true;
                    break;
                }
            }
            Assert.IsTrue(flag,"User is not present in list");
        }
    }
}
