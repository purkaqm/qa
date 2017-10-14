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

    public class Wait_For_Small_Loader_Image_To_Disappear : BaseWebAiiTest
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
        public void Wait_For_Small_Loader_Image_To_Disappear_CodedStep()
        {
               
            System.Threading.Thread.Sleep(5000);
            Wait.For<bool>(c => waitForLoaderToDisappear() == true, false,Manager.Settings.ElementWaitTimeout);
        }
        
        public bool waitForLoaderToDisappear(){
        {
            try{
                System.Threading.Thread.Sleep(2000);
                HtmlImage smallLoader = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("small_loader_image"));
                return !smallLoader.IsVisible();
            }
            catch(Exception ex){
                return false;
            }
             
        }
        }
}
}
