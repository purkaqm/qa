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

    public class TST_HIST_006 : BaseWebAiiTest
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
    
        [CodedStep(@"Search history by from and to dates")]
        public void CSHIST00601()
        {
            string todayToEnter = DateTime.Now.ToString("yyyy-MM-dd");
            
            //Enter start date
            string startDateScriptStr = string.Format("document.getElementsByName(\"startDateOne\")[0].value='{0}';",todayToEnter);
            Log.WriteLine(startDateScriptStr);
            Actions.InvokeScript(startDateScriptStr);
           
               
            //Enter finish date date
            string finishDateScriptStr = string.Format("document.getElementsByName(\"finishDateOne\")[0].value='{0}';",todayToEnter);
            Log.WriteLine(finishDateScriptStr);
            Actions.InvokeScript(finishDateScriptStr);
            
            //click go button
            Actions.InvokeScript("document.getElementsByClassName(\"btn apply\")[0].click();");
            ActiveBrowser.WaitUntilReady();
            Pages.PS_ManageHistoryPage.HistorySearchFormDiv.Wait.ForVisible();
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            System.Threading.Thread.Sleep(5000);
        }
        
         [CodedStep(@"Verify all serach results are displayed between given dates")]
        public void CSHIST00602()
        {
            string today = DateTime.Now.ToString("MM/dd/yyyy");
            IList<Element> dateTimeElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_datetime_records"));
            
            for(int i=0; i<dateTimeElements.Count;i++){
                Log.WriteLine(dateTimeElements[i].InnerText);
                 Log.WriteLine(today);
                Assert.IsTrue(dateTimeElements[i].InnerText.Contains(today),"Only history records belonging to time stamp searched should be displayed");
            }
        }
    }
}
