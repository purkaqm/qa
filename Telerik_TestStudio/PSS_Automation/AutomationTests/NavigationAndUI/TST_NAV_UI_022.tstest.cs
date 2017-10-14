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

    public class TST_NAV_UI_022 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify user is navigated to My Inbox page")]
        public void TST_NAV_UI_022_CodedStep()
        {
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Inbox"));
            Pages.PS_HomePage.PageContentDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"Verify notification count for each tab matches with left panel labels")]
        public void TST_NAV_UI_022_CodedStep3()
        {
            string alertLeftCounter = Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1];
            Log.WriteLine(Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[0]);
            Log.WriteLine(Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1]);
            string alertCenterCounter = Pages.PS_MyInboxPage.AlertsTabLink.InnerText.Split('(')[1];
            Assert.IsTrue(alertLeftCounter.Equals(alertCenterCounter),"Alerts notification count should match in center tab with left nav tab");
            
            
            string quesNotifLeftCounter = Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1];
            string quesNotifCenterCounter = Pages.PS_MyInboxPage.QuestionsNotifTabLink.InnerText.Split('(')[1];
            Assert.IsTrue(quesNotifLeftCounter.Equals(quesNotifCenterCounter),"Questions/Notifications notification count should match in center tab with left nav tab");
            
            
            string statusDueLeftCounter = Pages.PS_HomePage.InboxStatusReportLeftNavDiv.InnerText.Split('(')[1];
            string statusDueCenterCounter = Pages.PS_MyInboxPage.StatusReportDueTabLink.InnerText.Split('(')[1];
            Assert.IsTrue(statusDueLeftCounter.Equals(statusDueCenterCounter),"Status Reports Due notification count should match in center tab with left nav tab");
            
        }
    
        [CodedStep(@"Verify Inbox icon also displays notification counter")]
        public void TST_NAV_UI_022_CodedStep4()
        {
            Pages.PS_HomePage.InboxLeftNavNotifCounterDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.InboxLeftNavNotifCounterDiv.IsVisible(),"Counter should be displayed on Inbox in Icon bar");
            
            int alertsCount = Int32.Parse(Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1].Split(')')[0]);
            int questNotifCount = Int32.Parse(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            int statusDueCount = Int32.Parse(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            
            int subMenustotal = alertsCount+questNotifCount+statusDueCount;
            Log.WriteLine("total count : " + subMenustotal);
            
            if(subMenustotal > 0 ){
                int inboxIconCounter = Int32.Parse(Pages.PS_HomePage.InboxLeftNavNotifCounterDiv.InnerText.Split('+')[0]);
                if(subMenustotal < 99){
                   Assert.IsTrue(inboxIconCounter == subMenustotal, "Inbox Icon bar notification counter should be sum of all sub menu notifications");
                }else{
                   Assert.IsTrue(inboxIconCounter == 99, "Inbox Icon bar notification counter should be 99 as sum of all sub menu notifications is more than 99");
                }
            }
              
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_022_CodedStep1()
        {
            Pages.PS_MyInboxPage.AlertsTabLink.Click(true);
            ActiveBrowser.WaitUntilReady();
            int alertsCount = Int32.Parse(Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1].Split(')')[0]);
            if(alertsCount == 0){
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("myinbox_records_rows")).Count == 0, "0 alerts should not have any record");
            }
            else if(alertsCount <= 10){
                int records = ActiveBrowser.Find.AllByXPath("//table[@id='PSTable']/tbody/tr").Count - 1;
                Assert.IsTrue(alertsCount ==  records, "alerts count should exactly match with number of alerts records");
            }
            else{
                HtmlSpan alertSpan = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("pagination_span"));
                Assert.IsTrue(alertSpan.InnerText.Contains(alertsCount.ToString()), "alerts count should exactly match with number of alerts records");
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_022_CodedStep2()
        {
            Pages.PS_MyInboxPage.QuestionsNotifTabLink.Click(true);
            ActiveBrowser.WaitUntilReady();
            int quesNotifCount = Int32.Parse(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            if(quesNotifCount == 0){
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("myinbox_records_rows")).Count == 0, "0 questions/notifications should not have any record");
            }
             /*else if(quesNotifCount <= 1000){
                int records = ActiveBrowser.Find.AllByXPath("//form[@id='Main']//div[@class='clearfix step question']").Count - 1;
                 Log.WriteLine("r=" + records.ToString());
                Assert.IsTrue(quesNotifCount ==  records, "questions/notifications count should exactly match with number of questions/notifications records");
            }
            else{
                HtmlSpan quesNotifSpan = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("pagination_span"));
                Assert.IsTrue(quesNotifSpan.InnerText.Contains(quesNotifCount.ToString()), "questions/notifications count should exactly match with number of questions/notifications records");
            }*/
            else{
                int records = ActiveBrowser.Find.AllByXPath("//form[@id='Main']//div[@class='clearfix step question']").Count;
                Assert.IsTrue(quesNotifCount ==  records, "questions/notifications count should exactly match with number of questions/notifications records");
                
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_022_CodedStep5()
        {
            Pages.PS_MyInboxPage.StatusReportDueTabLink.Click(true);
            ActiveBrowser.WaitUntilReady();
            int statusDueCount = Int32.Parse(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            if(statusDueCount == 0){
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("myinbox_records_rows")).Count == 0, "0 status reports due should not have any record");
            }
             else if(statusDueCount <= 10){
                int records = ActiveBrowser.Find.AllByXPath("//table[@id='PSTable']/tbody/tr").Count - 1;
                Assert.IsTrue(statusDueCount ==  records, "status reports due count should exactly match with number of status reports due records");
            }
            else{
                HtmlSpan statusDueSpan = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("pagination_span"));
                Assert.IsTrue(statusDueSpan.InnerText.Contains(statusDueCount.ToString()), "status reports due count should exactly match with number of status reports due records");
            }
        }
    }
}
