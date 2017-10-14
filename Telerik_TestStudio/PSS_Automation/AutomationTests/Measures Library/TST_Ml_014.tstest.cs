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

    public class TST_Ml_014 : BaseWebAiiTest
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
        
        string measureName;
    
        [CodedStep(@"Verify Review left navigation tab is present")]
        public void TST_ML_014_CS01()
        {
            Pages.PS_HomePage.ReviewLeftNavLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_HomePage.ReviewLeftNavLink.IsVisible(),"Admin left navigation should be present");
        }
    
        [CodedStep(@"Click on Review left navigation link")]
        public void TST_ML_014_CS02()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify 'X_measure_template01' Measure library template was created")]
        public void TST_ML_014_CS03()
        {
            Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Click();
            System.Threading.Thread.Sleep(3000);
            Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Click(true);
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
            measureName = "X_measure_template01";
            if(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0)
            {
                Log.WriteLine("Library is present");
                Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
            }
            else{
                
                // Create 'X_measure_template' measure library
                
                    //Click on add button
                    Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.Click(true);
                    ActiveBrowser.WaitUntilReady();
                
                    //Enter name of library                
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.NameTextInput,measureName);
                    SetExtractedValue("MeasureName",measureName);
                
                    //Set Threashold1
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalIndTextInput,"10");
                
                    //Set Threashold2
                    Actions.SetText(Pages.PS_MeasureTemplatesNewPage.ThresholdGoalInd0Text,"20");
                
                    //Click on submit button
                    Pages.PS_MeasureTemplatesNewPage.SubmitButton.Click();
                    ActiveBrowser.WaitUntilReady();
                
                    //Verify library is created
                    Pages.PS_ReviewMeasureLibrary.ShowMoreLessImage.Click();
                    System.Threading.Thread.Sleep(3000);
                    Pages.PS_ReviewMeasureLibrary.TitleColumnLink.Click(true);
                    System.Threading.Thread.Sleep(3000);
                    ActiveBrowser.WaitUntilReady();
                    ActiveBrowser.RefreshDomTree();
                    Assert.IsTrue(ActiveBrowser.Find.AllByXPath(string.Format(AppLocators.get("measure_library_name_link"),measureName)).Count>0);
                    
            }
                                                                                    
                                                                                        
        }
    
        [CodedStep(@"Select 'Offline' on drop down menu near 'X_measure_template01'")]
        public void TST_ML_014_CS04()
        {
            try{
            HtmlImage measureNameTraingleImg = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            Log.WriteLine(string.Format(AppLocators.get("measure_library_traingle_img"),measureName));
            measureNameTraingleImg.MouseClick();
            Pages.PS_ReviewMeasureLibrary.OfflineDiv.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.OfflineDiv.IsVisible());
            Pages.PS_ReviewMeasureLibrary.OfflineDiv.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);
            }
            
            catch(Exception ex)
            {
                Log.WriteLine("Already 'Online' field is blank" + ex.Message);
               
            }
        }
    
        [CodedStep(@"Verify Measure library page is opened")]
        public void TST_ML_014_CS05()
        {
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue( Pages.PS_HomePage.PageTitleDiv.BaseElement.InnerText.Contains("Measures Library"));
            Assert.IsTrue(Pages.PS_ReviewMeasureLibrary.MeasureLibraryAddNewButton.IsVisible());            
                                    
        }
        
        [CodedStep(@"Verify 'Online' field is blank")]
        public void TST_ML_014_CS06()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlTableCell offlineCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>(string.Format(AppLocators.get("measure_library_offline_table_cell"),measureName));
            offlineCell.Wait.ForExists();
            Log.WriteLine(offlineCell.TextContent);
            Assert.AreNotEqual("Yes", offlineCell.BaseElement.InnerText);
            
        }
        
    }
}
