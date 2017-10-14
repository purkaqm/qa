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

    public class TST_HMC_004 : BaseWebAiiTest
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
    
        [CodedStep(@"Click Change home pages link")]
        public void CS_HMC_004_01()
        {
              Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.Click();
              ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                                    
        }
        
        [CodedStep(@"Verify change home page popup is displayed with home page and user options")]
        public void CS_HMC_004_02()
        {
              Pages.PS_HomePageConfigurationsPage.HomePageToUseDropdown.Wait.ForExists();
              Pages.PS_HomePageConfigurationsPage.UsersDropdown.Wait.ForExists(); 
              
                                    
        }
    }
}
