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

    public class TS_Open_Edit_Preferences_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Click user's name in top right corner")]
        public void TS_CS_01()
        {
                        Pages.PS_HomePage.FirstNameLastNameDiv.Click();
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        Pages.PS_HomePage.PopupUserSettingsWindowDiv.Wait.ForVisible();
                        
        }
    
        [CodedStep(@"Click on Edit Preferences link")]
        public void TS_CS_02()
        {
            Pages.PS_HomePage.EditPreferences.Wait.ForExists();
            Pages.PS_HomePage.EditPreferences.Wait.ForVisible();
            Pages.PS_HomePage.EditPreferences.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();           
            Pages.PS_SettingsPreferencesPage.HighContViewCheckBox.Wait.ForExists();
                        
        }
    }
}
