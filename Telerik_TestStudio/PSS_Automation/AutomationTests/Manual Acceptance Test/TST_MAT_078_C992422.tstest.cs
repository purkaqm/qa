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

    public class TST_MAT_078_C992422 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify page is displayed correctly")]
        public void TST_MAT_078_C992422_VerifyElements()
        {
            Assert.IsTrue(Pages.PS_AdminModulesPage.ModulesListContainerTable.IsVisible());
            Assert.IsTrue(Pages.PS_AdminModulesPage.ModuleTypeTableCell.IsVisible());
            Assert.IsTrue(ActiveBrowser.Url.Contains("modules.jsp"));
        }
    
        [CodedStep(@"Verify object type is present")]
        public void TST_MAT_078_C992422_VeriftObjectType()
        {
            ActiveBrowser.RefreshDomTree();
            string objectType = Data["ObjectType"].ToString();
            HtmlAnchor customProjrctLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//a[contains(.,'{0}')]",objectType));
            customProjrctLink.Wait.ForExists();
            customProjrctLink.Click();
                                    
        }
    
        [CodedStep(@"Vreify page navigate to module_wizard.jsp page ")]
        public void TST_MAT_078_C992422_VerifyPageUrl()
        {
            Assert.IsTrue(ActiveBrowser.Url.Contains("module_wizard.jsp"));
        }
    }
}
