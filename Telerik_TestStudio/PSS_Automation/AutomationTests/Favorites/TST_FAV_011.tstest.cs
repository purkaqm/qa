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

    public class TST_FAV_011 : BaseWebAiiTest
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
    
        //[CodedStep(@"Open any Portfolio page")]
        //public void TST_FAV_011_CodedStep()
        //{
            
            //HtmlAnchor randomPortFolioElement = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//tr[2]//td[@class='nameColumnValue']/a[1]");
            //randomPortFolioElement.Click();
            //ActiveBrowser.WaitUntilReady();
            //Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText, "Portfolios: Portfolio Details");
            
        //}
    
        //[CodedStep(@"Verify Portfolio Name is pre populated in Name field ")]
        //public void TST_FAV_011_CodedStep1()
        //{
           //// Element nameInput = ActiveBrowser.Find.ById("addToFavouritesName");
            //string attr = ActiveBrowser.Actions.InvokeScript("document.getElementById(\"addToFavouritesName\").value;").ToString();
            ////string attr = nameInput.getv("value").Value;
            //string portfolioName = "Portfolios: Portfolio Details";
            //Assert.IsTrue(attr.Equals(portfolioName), "Name field should be pre populated with Selected Portfolio Name");
           //Pages.PS_HomePage.AddToFavDialogCancelButton.Click();
        //}
    
        [CodedStep(@"Open any layout page")]
        public void TST_FAV_011_CodedStep2()
        {
            
            HtmlAnchor randomLayoutElement = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//tr[2]//td[@class='nameColumnValue']/a[2]");
            string layoutName = randomLayoutElement.InnerText.Trim();
            randomLayoutElement.Click();
            ActiveBrowser.WaitUntilReady();
            string pageTitle = string.Format("Edit Dashboard Layout : {0}",layoutName);
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText, pageTitle);
            SetExtractedValue("LayoutPageTitle",pageTitle);
        }
    
        [CodedStep(@"Verify Layout Name is pre populated in Name field ")]
        public void TST_FAV_011_CodedStep3()
        {
           
            string attr = ActiveBrowser.Actions.InvokeScript("document.getElementById(\"addToFavouritesName\").value;").ToString();
            string layoutName = GetExtractedValue("LayoutPageTitle").ToString();
            Assert.IsTrue(attr.Equals(layoutName), "Name field should be pre populated with Selected Layout Name");
           Pages.PS_HomePage.AddToFavDialogCancelButton.Click();
        }
    }
}
