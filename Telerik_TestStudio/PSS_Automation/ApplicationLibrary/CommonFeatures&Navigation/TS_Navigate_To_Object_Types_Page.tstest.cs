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

    public class TS_Navigate_To_Object_Types_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Open /admin/Object_types.jsp page")]
        public void TS_Navigate_To_Object_Types_Page_NavigateToObjectTypesPage()
        {
            ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString())+"/admin/object_types.jsp", true);
            Manager.ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait until Object types page is loaded")]
        public void TS_Navigate_To_Object_Types_Page_CodedStep1()
        {
           Pages.PS_ObjectTypesPage.OrderAllTypesLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ObjectTypesPage.OrderAllTypesLink.IsVisible(),"Order all types link should be visible");
        }
    }
}
