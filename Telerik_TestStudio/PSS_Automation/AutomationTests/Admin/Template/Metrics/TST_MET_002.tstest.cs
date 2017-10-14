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

    public class TST_MET_002 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify the financial page ")]
        public void TST_MET_002_CodedStep()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.IsVisible(),"User should be navigated to metric template page");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep1()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.CreateNewLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateNameImput.Wait.ForExists();
        }
    
        //[CodedStep(@"Enter text 'test' in 'ObjectNameText'")]
        //public void TST_MET_002_CodedStep2()
        //{
            
            
        //}
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep2()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep3()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep4()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep5()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_MET_002_CodedStep6()
        //{
            //System.Threading.Thread.Sleep(5000);
        //}
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep6()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"Click Next button")]
        public void TST_MET_002_CodedStep7()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.BasicInfoNextLink.Click();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep8()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabDisplayTable.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.FinishTabPropertiesTable.Wait.ForVisible();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep9()
        {
            HtmlTableCell basicInfoTabDetails = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//table[@class='bgWhite']/tbody/tr[1]/td[1]");
            string basicInfoDetailsStr = basicInfoTabDetails.InnerText;
            
            Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["MetricName"].ToString().ToLower()), "Metric Name should be displayed in Preview under Finish Tab");
            Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["Description"].ToString().ToLower()), "Metric Description should be displayed in Preview under Finish Tab");
            
            int viewCount = Int32.Parse(Data["TotalViews"].ToString());
            for(int i = 1; i <=viewCount;i++){
                Assert.IsTrue(basicInfoDetailsStr.ToLower().Contains(Data["ViewName"+i].ToString().ToLower()), "View names should be displayed in Preview under Finish Tab");
            }
            
            HtmlTableCell rollUpCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Allow Rollup')]]");
            Assert.IsTrue(rollUpCell.InnerText.ToLower().Contains(Data["AllowRollUp"].ToString().ToLower()), "Allow Rollup info should be displayed in Preview under Finish Tab");
            
            HtmlTableCell startEndDates = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Start Date')]]");
            Log.WriteLine(startEndDates.InnerText.ToLower());
            Log.WriteLine(Data["PeriodStartDate"].ToString().ToLower());
            Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodStartDate"].ToString().ToLower()), "Start Date info should be displayed in Preview under Finish Tab");
            Assert.IsTrue(startEndDates.InnerText.ToLower().Contains(Data["PeriodEndDate"].ToString().ToLower()), "End Date info should be displayed in Preview under Finish Tab");
            
            HtmlTableCell frequencyCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Frequency Is')]]");
            if(!Data["Frequency"].ToString().ToLower().Contains("infinitely")){
                Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains(Data["Frequency"].ToString().ToLower()), "Frequency info should be displayed in Preview under Finish Tab");
            }else{
              Assert.IsTrue(frequencyCell.InnerText.ToLower().Contains("no frequency"), "Frequency info should be displayed in Preview under Finish Tab");
             } 
            
            HtmlTableCell sendReminderCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Send Reminders')]]");
            Assert.IsTrue(sendReminderCell.InnerText.ToLower().Contains(Data["SendReminder"].ToString().ToLower()), "Send Reminder info should be displayed in Preview under Finish Tab");
            
              HtmlTableCell versionControlCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Version Control')]]");
                        if(Data["VersionControl"].ToString().ToLower().Contains("change")){
                            Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("version on change"), "Version Control info should be displayed in Preview under Finish Tab");
                        }else{
                               Assert.IsTrue(versionControlCell.InnerText.ToLower().Contains("none"), "Version Control info should be displayed in Preview under Finish Tab");
                        }
                        
            HtmlTableCell periodsBackForwardCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Periods')]]");
            Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsBack"].ToString().ToLower()), "Periods Back info should be displayed in Preview under Finish Tab");
            Assert.IsTrue(periodsBackForwardCell.InnerText.ToLower().Contains(Data["PeriodsForward"].ToString().ToLower()), "Periods Forward info should be displayed in Preview under Finish Tab");
            
            HtmlTableCell totalsCell = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.ByXPath<HtmlTableCell>("//td[@class='bgWhite'][span[contains(.,'Totals:')]]");
            Assert.IsTrue(totalsCell.InnerText.ToLower().Contains(Data["TotalsDisplayed"].ToString().ToLower()), "Totals info should be displayed in Preview under Finish Tab");
            
            
            
            IList<HtmlTableRow> lineItemRowsInPreview = Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.Find.AllByXPath<HtmlTableRow>("//table[@class='bgWhite']/tbody/tr[2]//table[@class='bgDark']/tbody/tr");
            int enteredLineItemCount = Int32.Parse(Data["TotalLineItems"].ToString());
            Assert.IsTrue(lineItemRowsInPreview.Count == enteredLineItemCount + 1, "Totals line item count displayed should match with line items entered");
            for(int j = 1;j<lineItemRowsInPreview.Count;j++){
                string currentRowStr = lineItemRowsInPreview[j].InnerText.ToLower();
                Log.WriteLine(currentRowStr);
                Log.WriteLine(Data["LineItemName"+j].ToString().ToLower());
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemName"+j].ToString().ToLower()), "Line Item Name should be displayed as Entered");
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemDesc"+j].ToString().ToLower()), "Line Item Description should be displayed as Entered");
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemSequence"+j].ToString().ToLower()), "Line Item Sequence should be displayed as Entered");
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemDataType"+j].ToString().ToLower()), "Line Item Type should be displayed as Entered");
                if(Data["LineItemDataType"+j].ToString().ToLower().Contains("cost") || Data["LineItemDataType"+j].ToString().ToLower().Contains("separator")){
                    continue;
                }
                if(Data["LineItemRollUp"+j].ToString().ToLower().Contains("yes")){
                    Assert.IsTrue(currentRowStr.Contains(Data["LineItemRollUp"+j].ToString().ToLower()), "Line Item RollUp should be displayed as Entered");
                }
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemBehavior"+j].ToString().ToLower()), "Line Item Behavior should be displayed as Entered");
                if(Data["LineItemBehavior"+j].ToString().ToLower().Contains("static") || Data["LineItemConstraint"+j].ToString().ToLower().Contains("none")){
                    continue;
                }
                Assert.IsTrue(currentRowStr.Contains(Data["LineItemConstraint"+j].ToString().ToLower()), "Line Item Constraint should be displayed as Entered");
                              
            }
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep10()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.TemplateSubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep11()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryEditLink.IsVisible(), "User should be navigated to template summary page");
            string title = Pages.PS_HomePage.PageTitleDiv.InnerText;
            Assert.IsTrue(title.Contains(GetExtractedValue("GeneratedMetricName").ToString()),"Template title should be displayed");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_MET_002_CodedStep12()
        {
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.SummaryDeleteSpan.Click();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateTableCell.Wait.ForVisible();
            Pages.PS_MetricTemplatesPage.TemplatesContainerFrame.ConfirmDeleteTemplateOkBtn.Click();
            ActiveBrowser.WaitUntilReady();
        }
    }
}
