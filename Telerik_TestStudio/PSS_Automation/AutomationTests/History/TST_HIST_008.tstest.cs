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

    public class TST_HIST_008 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Name Column Header for Sort")]
        public void CSHIST00801()
        {
            Manager.Desktop.Mouse.Click(MouseClickType.LeftClick, Pages.PS_ManageHistoryPage.NameColHeaderDiv.BaseElement.GetRectangle());
           // Actions.Click(Pages.PS_ManageHistoryPage.NameColHeaderDiv.BaseElement);
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            
        }
        
        [CodedStep(@"Verify that records are sorted by Name in ascending order")]
        public void CSHIST00802()
        {
            ActiveBrowser.RefreshDomTree();
            string ascArrowImgLocator = string.Format(AppLocators.get("manage_history_sort_asc_img"),"Name");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(ascArrowImgLocator).Count > 0 , "Ascending sort arrow should be displayed in Name column header");
            List<string> names = new List<string>();
            List<string> orderedByAsc = new List<string>();
            IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
            foreach (Element e in nameElements){
                names.Add(e.InnerText);
                
            }
           orderedByAsc = names;
           names.Sort();
           Assert.IsTrue(names.SequenceEqual(orderedByAsc),"Records should be ordered by ascending order of Names");
            
        }
        
        [CodedStep(@"Verify that records are sorted by Name in descending order")]
        public void CSHIST00803()
        {
            ActiveBrowser.RefreshDomTree();
            string ascArrowImgLocator = string.Format(AppLocators.get("manage_history_sort_dsc_img"),"Name");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(ascArrowImgLocator).Count > 0 , "Descending sort arrow should be displayed in Name column header");
            List<string> names = new List<string>();
            List<string> orderedByDesc = new List<string>();
            IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
            foreach (Element e in nameElements){
                names.Add(e.InnerText);
                Log.WriteLine(e.InnerText);
            }
           names.Sort();
           orderedByDesc = names;
           Assert.IsTrue(names.SequenceEqual(orderedByDesc),"Records should be ordered by descending order of Names");
            
        }
    }
}
