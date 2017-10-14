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

    public class TST_HMC_001 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify All Elements of Home Page Configuration")]
        public void CS_HMC_001_01()
        {
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.AddNewConfigurationLink.IsVisible(),"Add New Configuration Link should be present");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.ChangeHomePageDiv.IsVisible(),"Change Home Pages Link should be present");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.NameColumnHeader.IsVisible(),"Name column  should be present in table");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.DescColumnHeader.IsVisible(),"Description column  should be present in table");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.LastChangeColumnHeader.IsVisible(),"Last change column  should be present in table");
            Assert.IsTrue(Pages.PS_HomePageConfigurationsPage.EditorColumnHeader.IsVisible(),"Last editor column  should be present in table");
        }
    }
}
