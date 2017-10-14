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

    public class TS_Navigate_To_Admin_Deleted_Documents_Jsp_Page : BaseWebAiiTest
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
    
        [CodedStep(@"Open /admin/deleted_documents.jsp page")]
        public void TS_Navigate_To_Admin_Deleted_Documents_Page_NavigateToDeletedWorksPage()
        {
            ActiveBrowser.NavigateTo(CustomUtils.getProjectBaseURL(this.ExecutionContext.DeploymentDirectory.ToString())+"/admin/deleted_documents.jsp", true);
            Manager.ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait until Deleted Documents page is loaded")]
        public void TS_Navigate_To_Admin_Deleted_Documents_Page_WaitDeletedWorksPage()
        {
            Pages.PS_Deleted_Users_Page.PermanentlyDelAllItemsSubmit.Wait.ForExists();
            Pages.PS_Deleted_Users_Page.PermanentlyDelChekedItemsSubmit.Wait.ForExists();
            Assert.IsTrue(Pages.PS_Deleted_Users_Page.PermanentlyDelAllItemsSubmit.IsVisible(),"'Permanently delete all item' input should be displayed");
        }
    }
}
