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

    public class TST_NAV_UI_021 : BaseWebAiiTest
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
        public void TST_NAV_UI_021_CodedStep()
        {
            Pages.PS_HomePage.PageContentDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Inbox"));
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_021_CodedStep1()
        {
            Assert.IsTrue(Pages.PS_HomePage.InboxAlertsDiv.IsVisible(),"Alerts tab in left menu should be present");
            Assert.IsTrue(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.IsVisible(),"Questions/Notifications tab in left menu should be present");
            Assert.IsTrue(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.IsVisible(),"Status Reports Due tab in left menu should be present");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_NAV_UI_021_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_MyInboxPage.AlertsTabLink.IsVisible(),"Alerts tab in center navigation should be present");
            Assert.IsTrue(Pages.PS_MyInboxPage.QuestionsNotifTabLink.IsVisible(),"Questions/Notifications tab in center navigation should be present");
            Assert.IsTrue(Pages.PS_MyInboxPage.StatusReportDueTabLink.IsVisible(),"Status Reports Due tab in center navigation should be present");
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_021_CodedStep3()
        //{
            //string alertLeftCounter = Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1];
            //string alertCenterCounter = Pages.PS_MyInboxPage.AlertsTabLink.InnerText.Split('(')[1];
            //Assert.IsTrue(alertLeftCounter.Equals(alertCenterCounter),"Alerts notification count should match in center tab with left nav tab");
            
            //string quesNotifLeftCounter = Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1];
            //string quesNotifCenterCounter = Pages.PS_MyInboxPage.QuestionsNotifTabLink.InnerText.Split('(')[1];
            //Assert.IsTrue(quesNotifLeftCounter.Equals(quesNotifCenterCounter),"Questions/Notifications notification count should match in center tab with left nav tab");
            
            
            //string statusDueLeftCounter = Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1];
            //string statusDueCenterCounter = Pages.PS_MyInboxPage.QuestionsNotifTabLink.InnerText.Split('(')[1];
            //Assert.IsTrue(statusDueLeftCounter.Equals(statusDueCenterCounter),"Status Reports Due notification count should match in center tab with left nav tab");
            
        //}
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_021_CodedStep4()
        //{
            //Assert.IsTrue(Pages.PS_HomePage.InboxLeftNavNotifCounterDiv.IsVisible(),"Counter should be displayed on Inbox in Icon bar");
            
            //int alertsCount = Int32.Parse(Pages.PS_HomePage.InboxAlertsDiv.InnerText.Split('(')[1].Split(')')[0]);
            //int questNotifCount = Int32.Parse(Pages.PS_HomePage.InboxQuestNotifLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            //int statusDueCount = Int32.Parse(Pages.PS_HomePage.InboxStatusReportLeftNavDiv.InnerText.Split('(')[1].Split(')')[0]);
            
            //int subMenustotal = alertsCount+questNotifCount+statusDueCount;
            //Log.WriteLine("total count : " + subMenustotal);
            
            //int inboxIconCounter = Int32.Parse(Pages.PS_HomePage.InboxLeftNavNotifCounterDiv.InnerText);
            //if(subMenustotal < 99){
                //Assert.IsTrue(inboxIconCounter == subMenustotal, "Inbox Icon bar notification counter should be sum of all sub menu notifications");
            //}else{
                //Assert.IsTrue(inboxIconCounter == 99, "Inbox Icon bar notification counter should be 99 as sum of all sub menu notifications is more than 99");
            //}
            
            
            
        //}
    }
}
