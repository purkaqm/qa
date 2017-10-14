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

    public class TS_Project_Open_Left_Panel : BaseWebAiiTest
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
    
        [CodedStep(@"Desktop command: LeftClick on ReviewLink")]
        public void TS_Review_Open_Left_Panel_CodedStep()
        {
            // Desktop command: LeftClick on ProjectLeftNavLink
            Pages.PS_HomePage.ProjectLeftNavLink.Wait.ForExists();
            Pages.PS_HomePage.ProjectLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
            
            
        }
    
        [CodedStep(@"Wait for element 'NavPanelTitleDiv' 'is' visible.")]
        public void TS_Review_Open_Left_Panel_CodedStep1()
        {
            // Wait for element 'NavPanelTitleDiv' 'is' visible.
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"Verify 'TextContent' 'Contains' 'REVIEW' on 'NavPanelTitleDiv'")]
        public void TS_Review_Open_Left_Panel_CodedStep2()
        {
            /// \bug Cleanup and also should not be locale specific
            // Verify 'TextContent' 'Contains' 'REVIEW' on 'NavPanelTitleDiv'
            ActiveBrowser.RefreshDomTree();
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForContent(FindContentType.InnerText,"PROJECT");
        }
    }
}
