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

    public class TST_MUT_012 : BaseWebAiiTest
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
        
       string leftValueBefore,widthValueBefore,heightValueBefore,leftValueAfter,widthValueAfter,heightValueAfter;
    
        [CodedStep(@"Get a style values before pinned")]
        public void TST_MUT_012_GetStyleBefore()
        {
            ActiveBrowser.RefreshDomTree();
            leftValueBefore = Pages.PS_HomePage.PageContentDiv.GetStyleValue("left");
            widthValueBefore = Pages.PS_HomePage.PageContentDiv.GetStyleValue("width");
            heightValueBefore = Pages.PS_HomePage.PageContentDiv.GetStyleValue("height");
            
            Log.WriteLine(leftValueBefore);
            Log.WriteLine(widthValueBefore);
            Log.WriteLine(heightValueBefore);
        }
        
        [CodedStep(@"Get a style values after pinned")]
        public void TST_MUT_012_GetStyleAfter()
        {
            heightValueAfter = Pages.PS_HomePage.PageContentDiv.GetStyleValue("height");
            ActiveBrowser.RefreshDomTree();            
            leftValueAfter = Pages.PS_HomePage.PageContentDiv.GetStyleValue("left").ToString();
            widthValueAfter = Pages.PS_HomePage.PageContentDiv.GetStyleValue("width").ToString();
            heightValueAfter = Pages.PS_HomePage.PageContentDiv.GetStyleValue("height").ToString();
            Log.WriteLine(leftValueAfter);
            Log.WriteLine(widthValueAfter);
            Log.WriteLine(heightValueAfter);
            
        }
    
        [CodedStep(@"Verify content area is shifted")]
        public void TST_MUT_012_VerifyContentArea()
        {
            Assert.IsTrue(leftValueAfter.Contains("51px"),"Left pixel value should be correct");
            Assert.AreEqual(heightValueBefore, heightValueAfter);
        }
    }
}
