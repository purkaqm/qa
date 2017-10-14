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

    public class TST_Scroll_To_End : BaseWebAiiTest
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
    

    
        //[CodedStep(@"New Coded Step")]
        //public void TS_Scroll_To_End_CodedStep2()
        //{          
            //string  height = Actions.InvokeScript(@"document.getElementsByClassName(""ruling"").scrollHeight;");
            //Log.WriteLine("height : " + height);
            ///*
            //Actions.InvokeScript(string.Format("var objDiv = document.getElementsByClassName('{0}');objDiv.scrollTop = objDiv.scrollHeight;","ruling"));
            ////HtmlDiv scrollableCenterDiv = ActiveBrowser.Find.ByXPath<HtmlDiv>("//div[@class='ruling']");
            ////scrollableCenterDiv.ScrollTop = 1000;
            //this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            //ActiveBrowser.RefreshDomTree();
            //*/
           
        //}
    
         
        [CodedStep(@"New Coded Step")]
        public void TS_Scroll_To_End_CodedStep()
        {
            SetExtractedValue("Portfolio",Data["Portfolio"].ToString());
            SetExtractedValue("Layout",Data["Layout"].ToString());
            
        }
    
        [CodedStep(@"Click 'Button'")]
        public void TS_Scroll_To_End_CodedStep1()
        {
            // Click 'Go' Buton
            
            Pages.PS_ReviewDashboardPage.GoButton.Click(false);                     
           this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
            
                
            
        }
    
        [CodedStep(@"Scroll Div to bottom of window.")]
        public void TST_Scroll_To_End_CodedStep()
        {
            HtmlDiv elementToScroll = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("rev_dash_scrollable_div"));
            elementToScroll.ScrollToVisible(ArtOfTest.WebAii.Core.ScrollToVisibleType.ElementBottomAtWindowBottom);
            
            this.ExecuteTest("ApplicationLibrary\\Wait_For_App_Ajax_To_Load.tstest");
        }
    }
}
