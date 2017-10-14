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

    public class TS_Check_IS_Organization_Checkbox : BaseWebAiiTest
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
        public void TS_Check_IS_Organization_Checkbox_VerifyObjectTable()
        {
            Pages.PS_ObjectTypesPage.ObjectTable.Wait.ForExists();
            Assert.IsTrue(Pages.PS_ObjectTypesPage.ObjectTable.IsVisible(),"Object table should be visible");
        }
    
        [CodedStep(@"Verify object type is present")]
        public void TS_Check_IS_Organization_Checkbox_VeriftObjectType()
        {
            ActiveBrowser.RefreshDomTree();
            string objectType = Data["ObjectType"].ToString();
            ActiveBrowser.RefreshDomTree();
            HtmlAnchor objectTypeLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//a[contains(.,'{0}')]",objectType));
            objectTypeLink.Wait.ForExists();
            objectTypeLink.Click();
                        
        }
    
        [CodedStep(@"Verify required elements are presents")]
        public void TS_Check_IS_Organization_Checkbox_VerifyElements()
        {
            Pages.PS_ObjectTypesPage.CopyTypeLink.Wait.ForExists(30000);
            Pages.PS_ObjectTypesPage.EditLink.Wait.ForExists(30000);
            Pages.PS_ObjectTypesPage.ReturnToTypesImage.Wait.ForExists(30000);
            Assert.IsTrue(Pages.PS_ObjectTypesPage.CopyTypeLink.IsVisible(),"Copy link should be visible");
            Assert.IsTrue(Pages.PS_ObjectTypesPage.EditLink.IsVisible(),"Edit link should be visible");
            Assert.IsTrue(Pages.PS_ObjectTypesPage.ReturnToTypesImage.IsVisible()," 'Return to type' image should be visible");
        }
    
        [CodedStep(@"Click on Edit Link")]
        public void TS_Check_IS_Organization_Checkbox_ClickEditLink()
        {
            Pages.PS_ObjectTypesPage.EditLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Verify user redirect to edit page of object type")]
        public void TS_Check_IS_Organization_Checkbox_VerifyEdit()
        {
            Assert.IsTrue(ActiveBrowser.Url.Contains("object_type_edit.jsp"));
        }
    
        [CodedStep(@"Check 'Is Organization' checkbox")]
        public void TS_Check_IS_Organization_Checkbox_CheckIsOrganization()
        {
            Pages.PS_ObjectTypesPage.IsOrganizationCheckBox.Check(true);
            Pages.PS_ObjectTypesPage.SubmitLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
        
        [CodedStep(@"Verify Is Organization status is Yes")]
        public void TS_Check_IS_Organization_Checkbox_VerifyCheck()
        {
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            HtmlSpan statusNO = ActiveBrowser.Find.ByXPath<HtmlSpan>("//form[@name='main']//td[contains(.,'Is Organization:')]/following-sibling::td/span");
            statusNO.Wait.ForExists();
            Log.WriteLine(statusNO.InnerText);
            Assert.AreEqual(statusNO.InnerText,"Yes");
            
            
        }
    }
}
