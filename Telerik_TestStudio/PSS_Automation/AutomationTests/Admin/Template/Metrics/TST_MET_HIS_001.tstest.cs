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

    public class TST_MET_HIS_001 : BaseWebAiiTest
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
    
        //[CodedStep(@"Verify Metric Template Page is opened")]
        //public void TST_MET_HIS_000_CodedStep()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        //}
    
        //[CodedStep(@"Click Create New link")]
        //public void TST_MET_HIS_000_CodedStep1()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForVisible();
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep2()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep3()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep4()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep5()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep6()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Click Next button")]
        //public void TST_MET_HIS_000_CodedStep7()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        //}
    
        //[CodedStep(@"Wait for Finish Tab ")]
        //public void TST_MET_HIS_000_CodedStep8()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        //}
    
        //[CodedStep(@"Verify Finish tab information displayed as a Preview")]
        //public void TST_MET_HIS_000_CodedStep9()
        //{
            //HtmlTableCell basicInfoTabDetails = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//table[@class='bgWhite']/tbody/tr[1]/td[1]");
            //string basicInfoDetailsStr = basicInfoTabDetails.InnerText;
            
            //Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["MetricName"].ToString().ToLower()), "Metric Name should be displayed in Preview under Finish Tab");
            //Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["Description"].ToString().ToLower()), "Metric Description should be displayed in Preview under Finish Tab");
            
            //int viewCount = Int32.Parse(Data["TotalViews"].ToString());
            //for(int i = 1; i <=viewCount;i++){
                //Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["ViewName"+i].ToString().ToLower()), "View names should be displayed in Preview under Finish Tab");
            //}
            
            //HtmlTableCell rollUpCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Allow Rollup')]]");
            //Assert.IsTrue(rollUpCell.InnerText.ToLower().Contains(Data["AllowRollUp"].ToString().ToLower()), "Allow Rollup info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell startEndDates = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Start Date')]]");
            //Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodStartDate"].ToString().ToLower()), "Start Date info should be displayed in Preview under Finish Tab");
            //Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodEndDate"].ToString().ToLower()), "End Date info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell frequencyCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Frequency Is')]]");
            //Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains(Data["Frequency"].ToString().ToLower()), "Frequency info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell sendReminderCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Send Reminders')]]");
            //Assert.IsTrue(sendReminderCell.InnerText.ToLower().Contains(Data["SendReminder"].ToString().ToLower()), "Send Reminder info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell versionControlCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Version Control')]]");
            //Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains(Data["VersionControl"].ToString().ToLower()), "Version Control info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell periodsBackForwardCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Periods')]]");
            //Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsBack"].ToString().ToLower()), "Periods Back info should be displayed in Preview under Finish Tab");
            //Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsForward"].ToString().ToLower()), "Periods Forward info should be displayed in Preview under Finish Tab");
            
            //HtmlTableCell totalsCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Totals:')]]");
            //Assert.IsTrue(totalsCell.InnerText.ToLower().Contains(Data["TotalsDisplayed"].ToString().ToLower()), "Totals info should be displayed in Preview under Finish Tab");
            
            
            
            //IList<HtmlTableRow> lineItemRowsInPreview = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlTableRow>("//table[@class='bgWhite']/tbody/tr[2]//table[@class='bgDark']/tbody/tr");
            //int enteredLineItemCount = Int32.Parse(Data["TotalLineItems"].ToString());
            //Assert.IsTrue(lineItemRowsInPreview.Count == enteredLineItemCount + 1, "Totals line item count displayed should match with line items entered");
            //for(int j = 1;j<lineItemRowsInPreview.Count;j++){
                //string currentRowStr = lineItemRowsInPreview[j].InnerText.ToLower();
                //Log.WriteLine(currentRowStr);
                //Log.WriteLine(Data["LineItemName"+j].ToString().ToLower());
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemName"+j].ToString().ToLower()), "Line Item Name should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemDesc"+j].ToString().ToLower()), "Line Item Description should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemSequence"+j].ToString().ToLower()), "Line Item Sequence should be displayed as Entered");
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemDataType"+j].ToString().ToLower()), "Line Item Type should be displayed as Entered");
                //if(Data["LineItemDataType"+j].ToString().ToLower().Contains("cost") || Data["LineItemDataType"+j].ToString().ToLower().Contains("separator")){
                    //continue;
                //}
                //if(Data["LineItemRollUp"+j].ToString().ToLower().Contains("yes")){
                    //Assert.IsTrue(currentRowStr.Contains(Data["LineItemRollUp"+j].ToString().ToLower()), "Line Item RollUp should be displayed as Entered");
                //}
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemBehavior"+j].ToString().ToLower()), "Line Item Behavior should be displayed as Entered");
                //if(Data["LineItemBehavior"+j].ToString().ToLower().Contains("static") || Data["LineItemConstraint"+j].ToString().ToLower().Contains("none")){
                    //continue;
                //}
                //Assert.IsTrue(currentRowStr.Contains(Data["LineItemConstraint"+j].ToString().ToLower()), "Line Item Constraint should be displayed as Entered");
                              
            //}
            
        //}
    
        //[CodedStep(@"Click Submit button")]
        //public void TST_MET_HIS_000_CodedStep10()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            //ActiveBrowser.WaitUntilReady();
            
            
            
        //}
    
        //[CodedStep(@"Verify Summary Page is displayed")]
        //public void TST_MET_HIS_000_CodedStep11()
        //{
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            //Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            //string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
            //Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        //}
    
        [CodedStep(@"Delete newly created metric template")]
        public void TST_MET_HIS_000_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on Edit link")]
        public void TST_MET_HIS_000_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Set name in basic info")]
        public void TST_MET_HIS_000_CodedStep1()
        {
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Focus();
            string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Value.ToString();
            Log.WriteLine(oldName);
            string newName = "newName"+Randomizers.generateRandomInt(10000,99999);
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.SetValue("value",newName);
            SetExtractedValue("OldName",oldName);
            SetExtractedValue("NewName",newName);
            
            
        }
    
        [CodedStep(@"Click on history tab")]
        public void TST_MET_HIS_000_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.HistoryLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.HistoryLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Click on submit")]
        public void TST_MET_HIS_000_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Wait.ForVisible();
            
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.HistorySubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
        }
    
        [CodedStep(@"Verify name is changed")]
        public void TST_MET_HIS_000_CodedStep4()
        {   
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"objectName\" field in the  Metric Template";
            string updateStr1 = "from: "+GetExtractedValue("OldName").ToString();
            string updateStr2 = "to: "+GetExtractedValue("NewName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "template name change action should be logged in history");
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.SummaryLink.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
        }
    
        //[CodedStep(@"Edit Template Name")]
        //public void TST_MET_HIS_000_CodedStep6()
        //{
            //ActiveBrowser.Window.SetFocus();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Click();
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Focus();
            //string oldName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Value.ToString();
            //Log.WriteLine(oldName);
            //string newName = "newName"+Randomizers.generateRandomInt(10000,99999);
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.SetValue("value",newName);
            //SetExtractedValue("OldName",oldName);
            //SetExtractedValue("NewName",newName);
            //Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoSubmitLink.Click();
            
        //}
    
        [CodedStep(@"Select send reminders")]
        public void TST_MET_HIS_000_CodedStep7()
        {
            string oldReminder = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SendRemindersSelect.SelectedOption.BaseElement.InnerText;
            string s1 = "Before Period Begins";
            string s2 = "After Period Begins";
            Log.WriteLine(oldReminder);
            if(oldReminder.Equals(s1))
            {
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SendRemindersSelect.SelectByText(s2, true);
            }
            else
            {
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SendRemindersSelect.SelectByText(s1, true);
            }
            string newReminder = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SendRemindersSelect.SelectedOption.BaseElement.InnerText;
            Log.WriteLine(newReminder);
            SetExtractedValue("OldSendReminderVal",oldReminder);
            SetExtractedValue("NewSendReminderVal",newReminder);
        
        }
    
        [CodedStep(@"Verify name is changed is reflected in History")]
        public void TST_MET_HIS_000_CodedStep6()
        {   
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"Remind\" field in the  Metric Template";
            string updateStr1 = "from: "+GetExtractedValue("OldSendReminderVal").ToString();;
            string updateStr2 = "to: "+GetExtractedValue("NewSendReminderVal").ToString();;
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "send reminder setting change action should be logged in history");
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
        }
    
        [CodedStep(@"Select Frequency")]
        public void TST_MET_HIS_000_CodedStep9()
        {
            string oldFrequencyVal = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FrequencySelect.SelectedOption.BaseElement.InnerText;
            string frequency = Data["EditFrequency"].ToString();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FrequencySelect.SelectByValue(frequency,true);
            string newFrequencyVal = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FrequencySelect.SelectedOption.BaseElement.InnerText;
            SetExtractedValue("OldFrequencyVal",oldFrequencyVal);
            SetExtractedValue("NewFrequencyVal",newFrequencyVal);
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep10()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"Period\" field in the  Metric Template";
            string updateStr1 = "from: "+GetExtractedValue("OldFrequencyVal").ToString();;
            string updateStr2 = "to: "+GetExtractedValue("NewFrequencyVal").ToString();;
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Frequency setting change action should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep11()
        {
            if(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.Checked){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.Click();
            string allowRollupOldInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.BaseElement.InnerText;
            string allowRollupNewInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.BaseElement.InnerText;    
            SetExtractedValue("AllowRollupOldInput",allowRollupOldInput);
            SetExtractedValue("AllowRollupNewInput",allowRollupNewInput);
            }
            else{
             Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.Click();
             string allowRollupOldInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpNoInput.BaseElement.InnerText;
             string allowRollupNewInput = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.AllowRollUpYesInput.BaseElement.InnerText;  
             SetExtractedValue("AllowRollupOldInput",allowRollupOldInput);
             SetExtractedValue("AllowRollupNewInput",allowRollupNewInput);
            
            }
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep13()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr = "changed the \"Permits Rollups\" field in the  Metric Template";
            string updateStr1 = "from: "+GetExtractedValue("AllowRollupOldInput").ToString();;
            string updateStr2 = "to: "+GetExtractedValue("AllowRollupNewInput").ToString();;
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr,updateStr1,updateStr2);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "Frequency setting change action should be logged in history");
            
        }
    
       
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_000_CodedStep17()
        {
            
        }
        
         [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_001_CodedStep()
        {
            string oldStartDate = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.Value;
            if(Data["EditFrequency"].ToString().Length > 0 && !Data["EditFrequency"].ToString().ToLower().Contains("infinitely")){
            
            if(Data["EditPeriodStartDate"].ToString().Length > 0 ){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.SetValue("value","");
            ActiveBrowser.Window.SetFocus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.Focus();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.PeriodStartsInput.SetValue("value",Data["EditPeriodStartDate"].ToString());
            }
            }
            SetExtractedValue("OldStartDate",oldStartDate);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_001_CodedStep1()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Start Date\" field in the ";
            string actionStr2 = "Metric Template";
            string metricName = GetExtractedValue("NewName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0,"Period change event in Basic Info should be logged in history");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_001_CodedStep2()
        {
            if(Data["EditViews"].ToString().ToLower().Contains("yes")){
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewsYesInput.Click();
            }
            else{
                Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ViewsNoInput.Click();
            }
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_HIS_001_CodedStep3()
        {
            string userName = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.UserNameLink.InnerText;
            string actionStr1 = "changed the \"Has Views\" field in the";
            string actionStr2 = "Metric Template";
            string metricName = GetExtractedValue("NewName").ToString();
            string nameVerificationLocator = string.Format("//form[@name='main']//tr[contains(.,'{0}') and contains(.,'{1}') and contains(.,'{2}') and contains(.,'{3}')]",userName,actionStr1,actionStr2,metricName);
            Log.WriteLine(nameVerificationLocator);
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath(nameVerificationLocator).Count > 0, "send reminder setting change action should be logged in history");                        
        }
    }
}
