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

    public class TST_SRT_020 : BaseWebAiiTest
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
        
        [CodedStep(@"Get All Active Status Report Template Names")]
        public void CS_SRT_020_00()
        {
            string activeTemplates = "";
            IList<Element> statusRepList = ActiveBrowser.Find.AllByXPath("//td[@class='nameColumnValue']/div");
            foreach (Element e in statusRepList){
                 string onclick = e.GetAttribute("onclick").Value;
                 if(onclick.Contains("\"active\":true")){
                     activeTemplates += e.InnerText+"---";
                 }
                 
               
             }
             
             SetExtractedValue("ActiveStatusReports", activeTemplates);
        }
    
        [CodedStep(@"Click on Add New button")]
        public void CS_SRT_020_01()
        {
                                    Pages.PS_CustomFieldsPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_CustomFieldsPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
        
        [CodedStep(@"Click Other Assoc selector")]
        public void CS_SRT_020_02()
        {
                                    Pages.PS_CustomFieldsPage.AddCustFieldOtherAssocDiv.Wait.ForExists();
                                    Pages.PS_CustomFieldsPage.AddCustFieldOtherAssocDiv.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                    System.Threading.Thread.Sleep(5000);
        }
    
        [CodedStep(@"Verify All active status reports are displayed")]
        public void CS_SRT_020_03()
        {
             string activeStatusReports = GetExtractedValue("ActiveStatusReports").ToString();
             string statusListstr = "";
             IList<Element> statusRepList = ActiveBrowser.Find.AllByXPath(AppLocators.get("cust_fields_add_asso_status_repo_list"));
             foreach (Element e in statusRepList){
                statusListstr += e.InnerText+"---";
             }
             Assert.IsTrue(statusListstr.Equals(activeStatusReports),"All active Status Reports should be displayed as Other Assoc options");
        }
        
    
       
    }
}
