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

    public class TST_MAT_077_C992421 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify Object type table is displayed")]
        public void TST_MAT_077_C992421_VerifyObjectTable()
        {
            Pages.PS_ObjectTypesPage.ObjectTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ObjectTypesPage.ObjectTable.IsVisible(),"Object table should be visible");
        }
    
        [CodedStep(@"Verify object type is present")]
        public void TST_MAT_077_C992421_VeriftObjectType()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlAnchor customProjrctLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>("//a[contains(.,'Custom Project')]");
            customProjrctLink.Wait.ForExists();
            customProjrctLink.Click();
                        
        }
    
        [CodedStep(@"Vreify required elements are presents")]
        public void TST_MAT_077_C992421_VerifyElements()
        {
            Pages.PS_ObjectTypesPage.CopyTypeLink.Wait.ForExists(30000);
            Pages.PS_ObjectTypesPage.EditLink.Wait.ForExists(30000);
            Pages.PS_ObjectTypesPage.ReturnToTypesImage.Wait.ForExists(30000);
            Assert.IsTrue(Pages.PS_ObjectTypesPage.CopyTypeLink.IsVisible(),"Copy link should be visible");
            Assert.IsTrue(Pages.PS_ObjectTypesPage.EditLink.IsVisible(),"Edit link should be visible");
            Assert.IsTrue(Pages.PS_ObjectTypesPage.ReturnToTypesImage.IsVisible()," 'Return to type' image should be visible");
        }
    
        [CodedStep(@"Click on Edit Link")]
        public void TST_MAT_077_C992421_ClickEditLink()
        {
                Pages.PS_ObjectTypesPage.EditLink.Click();
                ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user redirect to edit page of object type")]
        public void TST_MAT_077_C992421_VerifyEdit()
        {
            Assert.IsTrue(ActiveBrowser.Url.Contains("object_type_edit.jsp"));
        }
    }
}
