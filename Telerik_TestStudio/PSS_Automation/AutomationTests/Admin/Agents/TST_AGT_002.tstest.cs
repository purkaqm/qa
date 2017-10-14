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

    public class TST_AGT_002 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on left navigation Admin link")]
        public void TST_AGT_002_CS01()
        {
            Pages.PS_HomePage.AdminLeftNavLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Click on 'Delete Old Users Agent' link")]
        public void TST_AGT_002_CS02()
        {
            Pages.PS_AgentsPage.AgentsContainerFrame.DeleteOldUsersAgtLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify Agent page all elements is present")]
        public void TST_AGT_002_CS03()
        {
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.ActivateImage.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.DescriptionTableCell.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.IntervalTableCell.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.IsActiveTableCell.IsVisible());
            //Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.LastRuntimeTableCell.IsVisible());
            //Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.NextRuntimeTableCell.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.SystemSpan.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.TaskTableCell.IsVisible());
            Assert.IsTrue(Pages.PS_AgentsPage.AgentsContainerFrame.TypeTableCell.IsVisible());
        }
    
        
    
        
    }
}
