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

    public class TST_HIST_011 : BaseWebAiiTest
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
    
        [CodedStep(@"Store New Project Page URL")]
        public void CSHIST01101()
        {
                        string projUrl = ActiveBrowser.Url;
                        SetExtractedValue("Page1URL", projUrl);
        }
        
          
        [CodedStep(@"Copy URL and verify")]
        public void CSHIST01102()
        {
            string projUrl = GetExtractedValue("Page1URL").ToString();
            IList<HtmlControl> urlElements = ActiveBrowser.Find.AllByXPath<HtmlControl>(AppLocators.get("manage_history_url_records"));
            urlElements[0].DragToWindowLocation(ArtOfTest.Common.OffsetReference.TopLeftCorner, 2, 14, true, ArtOfTest.Common.OffsetReference.TopLeftCorner, 1089, 267, true);
            ActiveBrowser.Manager.Desktop.KeyBoard.KeyPress(ArtOfTest.WebAii.Win32.KeyBoard.KeysFromString("Ctrl+C"), 150, 1);
            //ActiveBrowser.WaitUntilReady();
            string copiedText = System.Windows.Forms.Clipboard.GetText();
            Log.WriteLine(copiedText);
            Assert.IsTrue(projUrl.Contains(copiedText),"User should be able to copy URL");
        }
    }
}
