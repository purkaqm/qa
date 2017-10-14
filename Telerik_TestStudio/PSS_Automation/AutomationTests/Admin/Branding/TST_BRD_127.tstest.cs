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

    public class TST_BRD_127 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on PowerPoint branding tab")]
        public void TST_BRD_127_CS01()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
       
    
        [CodedStep(@"Click on User name link")]
        public void TST_BRD_127_CS02()
        {
            HtmlDiv userNameLoc =  ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("user_name_link"),"Pratik"));
            Log.WriteLine(userNameLoc.ToString());
            userNameLoc.Wait.ForExists();
            ActiveBrowser.Window.SetFocus();
            userNameLoc.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();          
        }
    
        [CodedStep(@"Verify User Details popup is shown")]
        public void TST_BRD_127_CS03()
        {
            Pages.PS_UserDetailsPopUp.UserDetailsPopupDiv.Wait.ForExists();
            Pages.PS_UserDetailsPopUp.UserDetailsMoreLink.Wait.ForExists();
            Assert.IsTrue(Pages.PS_UserDetailsPopUp.UserDetailsPopupDiv.IsVisible(),"User details popup shoud be present");
            Assert.IsTrue(Pages.PS_UserDetailsPopUp.UserDetailsMoreLink.IsVisible(),"More... link should be present in popup");
        }
    }
}
