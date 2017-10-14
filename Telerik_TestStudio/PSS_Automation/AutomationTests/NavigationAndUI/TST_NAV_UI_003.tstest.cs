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

    public class TST_NAV_UI_003 : BaseWebAiiTest
    {
        #region [ Dynamic Pages Reference ]

        private Pages _pages;

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
    
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Header_Items_CodedStep()
        {
            Pages.PS_HomePage.PowerSteeringImage.Wait.ForExists();
            Pages.PS_HomePage.SearchLink.Wait.ForExists();
            Pages.PS_HomePage.HelpIconSpan.Wait.ForExists();
            Pages.PS_HomePage.EditDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.TextContent,"Home");
            
            
            
            
            
            
        }
    
        //[CodedStep(@"Wait_For_Ajax_To_Load")]
        //public void TST_Verify_Header_Items_CodedStep1()
        //{
           
            //System.Threading.Thread.Sleep(2000);
            //Wait.For<bool>(c => ActiveAjaxConnections() == true, false,Manager.Settings.ElementWaitTimeout);
        //}
    }
}
