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

    public class TST_Change_Date : BaseWebAiiTest
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
        public void TS_Change_Date_CodedStep()
        {
            SetExtractedValue("Portfolio",Data["Portfolio"].ToString());
            SetExtractedValue("Layout",Data["Layout"].ToString());
            
            
            
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Date_CodedStep1()
        {
            // Click 'Go' Buton
            Pages.PS_ReviewDashboardPage.GoButton.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
                
            
        }
    
        [CodedStep(@"Click 'ProposedNobrTag'")]
        public void TS_Change_Date_CodedStep2()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            int projectIndex = 1;
            IList<Element> projects = ActiveBrowser.Find.AllByXPath(AppLocators.get("rev_dash_project_list_div"));
            for(int i=1 ; i <= projects.Count; i++){
                if(projects[i-1].InnerText.Contains(Data["Project"].ToString())){
                 projectIndex = i;
                 break;   
                }
            }
            string dateFieldLoc = string.Format(AppLocators.get("rev_dash_projected_end_date_field_div"), projectIndex);
            
            HtmlDiv dateField = ActiveBrowser.Find.ByXPath<HtmlDiv>(dateFieldLoc);
            dateField.Wait.ForVisible();
            
            string currDate = dateField.InnerText;
            DateTime currDateVal = DateTime.Today;
            if(currDate.Length > 0){
                currDateVal = Convert.ToDateTime(currDate);
            }
            
            dateField.Click();
            
            int month = Int32.Parse(Data["Month"].ToString());
            DateTime randomDate = Randomizers.generateRandomDate(DateTime.Today,month*30,currDateVal); 
            string dateToEnter = randomDate.ToString("MM/dd/yyyy");
            ActiveBrowser.RefreshDomTree();
            HtmlInputText firstEditableDateField = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("rev_dash_editable_date_field_input"));
            firstEditableDateField.Wait.ForVisible();
            Actions.SetText(firstEditableDateField, dateToEnter);
            firstEditableDateField.InvokeEvent(ScriptEventType.OnBlur);
            Pages.PS_HomePage.PageTitleDiv.Click();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            SetExtractedValue("dateFieldLoc",dateFieldLoc);
            SetExtractedValue("dateToEnter",dateToEnter);
            Log.WriteLine("Entered Date : " + dateToEnter);
            
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
    

    
        [CodedStep(@"Click 'Button'")]
        public void TS_Change_Date_CodedStep4()
        {
            // Click 'Button0'
            Pages.PS_ReviewDashboardPage.SaveButton.Click(false);
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
                      
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Change_Date_CodedStep5()
        {
            ActiveBrowser.RefreshDomTree();
            ActiveBrowser.WaitForAjax(Manager.Settings.ClientReadyTimeout);
            
            HtmlDiv datFieldToVerify = ActiveBrowser.Find.ByXPath<HtmlDiv>(GetExtractedValue("dateFieldLoc").ToString());
            datFieldToVerify.Wait.ForVisible();
            Assert.AreEqual(true , datFieldToVerify.InnerText.Contains(GetExtractedValue("dateToEnter").ToString()),"Date Update Failed");
            
                       
        }
    
 
    }
}
