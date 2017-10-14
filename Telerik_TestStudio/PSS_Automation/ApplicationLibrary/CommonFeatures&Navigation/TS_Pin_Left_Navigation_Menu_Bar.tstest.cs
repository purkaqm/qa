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

    public class TS_Pin_Left_Navigation_Menu_Bar : BaseWebAiiTest
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
    
        [CodedStep(@"Hide_Left_Nav_Menu_Bar")]
        public void TS_Pin_Left_Navigation_Menu_Bar_CodedStep()
        {            
            /// \bug Is Mouse over or hover is used at all?
            Pages.PS_HomePage.HomeLeftNavLink.InvokeEvent(ScriptEventType.OnMouseOver);
            System.Threading.Thread.Sleep(3000);
            Pages.PS_HomePage.NavPanelTitleDiv.Wait.ForVisible();
            if(!Pages.PS_HomePage.NavMenuPinDiv.BaseElement.GetAttribute("class").Value.Contains("active")){
                Pages.PS_HomePage.NavMenuPinDiv.Click();
                ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                Assert.IsTrue(Pages.PS_HomePage.NavPanelTitleDiv.IsVisible(),"Left navigation menu should always be visible when pinned");
            }   
            
        }
    }
}
