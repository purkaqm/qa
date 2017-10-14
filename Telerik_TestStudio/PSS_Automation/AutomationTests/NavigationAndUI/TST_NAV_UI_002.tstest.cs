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

    public class TST_NAV_UI_002 : BaseWebAiiTest
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
        public void TST_Verify_AllExpandable_SubMenuItems_CodedStep2()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("report_leftnav_plus_span")).Count>0,"Report should have +icon in navigation bar");
            
            HtmlSpan plusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("report_leftnav_plus_span"));
            plusIcon1.Click();
            plusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("report_leftnav_minus_span")).Count>0,"Report central should have - icon in navigation bar");
            HtmlSpan minusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("report_leftnav_minus_span"));
            minusIcon1.Click();
            minusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("report_leftnav_plus_span")).Count>0,"Report central should have +icon in navigation bar");
            
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_AllExpandable_SubMenuItems_CodedStep1()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("reports_leftnav_plus_span")).Count>0," Reports should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("usermanagement_leftnav_plus_span")).Count>0," Usermanagement should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("importexport_leftnav_plus_span")).Count>0," Importexport should have +icon in navigation bar");
            
            HtmlSpan plusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("reports_leftnav_plus_span"));
            plusIcon1.Click();
            plusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("reports_leftnav_minus_span")).Count>0,"Reports should have - icon in navigation bar");
            HtmlSpan minusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("reports_leftnav_minus_span"));
            minusIcon1.Click();
            minusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("reports_leftnav_plus_span")).Count>0,"Reports should have +icon in navigation bar");
            
            HtmlSpan plusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("usermanagement_leftnav_plus_span"));
            plusIcon2.Click();
            plusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("usermanagement_leftnav_minus_span")).Count>0,"Usermanagement should have - icon in navigation bar");
            HtmlSpan minusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("usermanagement_leftnav_minus_span"));
            minusIcon2.Click();
            minusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("usermanagement_leftnav_plus_span")).Count>0,"Usermanagement should have +icon in navigation bar");
            
            HtmlSpan plusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("importexport_leftnav_plus_span"));
            plusIcon3.Click();
            plusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("importexport_leftnav_minus_span")).Count>0,"Importexport should have - icon in navigation bar");
            HtmlSpan minusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("importexport_leftnav_minus_span"));
            minusIcon3.Click();
            minusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("importexport_leftnav_plus_span")).Count>0,"Importexport should have +icon in navigation bar");
            
            
        }
    
                  
            [CodedStep(@"New Coded Step")]
        public void TST_Verify_AllExpandable_SubMenuItems_CodedStep3()
        {
            
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("templates_leftnav_plus_span")).Count>0," Templates should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("layouts_leftnav_plus_span")).Count>0," Layouts should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("permissions_leftnav_plus_span")).Count>0," Permission should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("localization_leftnav_plus_span")).Count>0," Localization should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("configuration_leftnav_plus_span")).Count>0," configuration should have +icon in navigation bar");
            
            HtmlSpan plusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("templates_leftnav_plus_span"));
            plusIcon1.Click();
            plusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("templates_leftnav_minus_span")).Count>0,"Templates should have - icon in navigation bar");
            HtmlSpan minusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("templates_leftnav_minus_span"));
            minusIcon1.Click();
            minusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("templates_leftnav_plus_span")).Count>0,"Templates should have +icon in navigation bar");
            
            HtmlSpan plusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("layouts_leftnav_plus_span"));
            plusIcon2.Click();
            plusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("layouts_leftnav_minus_span")).Count>0,"Layouts should have - icon in navigation bar");
            HtmlSpan minusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("layouts_leftnav_minus_span"));
            minusIcon2.Click();
            minusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("layouts_leftnav_plus_span")).Count>0,"Layouts should have +icon in navigation bar");
            
            HtmlSpan plusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("permissions_leftnav_plus_span"));
            plusIcon3.Click();
            plusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("permissions_leftnav_minus_span")).Count>0,"Permission should have - icon in navigation bar");
            HtmlSpan minusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("permissions_leftnav_minus_span"));
            minusIcon3.Click();
            minusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("permissions_leftnav_plus_span")).Count>0,"Permission should have +icon in navigation bar");
            
            HtmlSpan plusIcon4 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("localization_leftnav_plus_span"));
            plusIcon4.Click();
            plusIcon4.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("localization_leftnav_minus_span")).Count>0,"Localization should have - icon in navigation bar");
            HtmlSpan minusIcon4 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("localization_leftnav_minus_span"));
            minusIcon4.Click();
            minusIcon4.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("localization_leftnav_plus_span")).Count>0,"Localization should have +icon in navigation bar");
            
           
            HtmlSpan plusIcon5 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("configuration_leftnav_plus_span"));
            plusIcon5.Click();
            plusIcon5.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("configuration_leftnav_minus_span")).Count>0,"Configuration should have - icon in navigation bar");
            HtmlSpan minusIcon5 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("configuration_leftnav_minus_span"));
            minusIcon5.Click();
            minusIcon5.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("configuration_leftnav_plus_span")).Count>0,"Configuration should have +icon in navigation bar");
            
            
        }
        
        
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_AllExpandable_SubMenuItems_CodedStep()
        {
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("projcentral_leftnav_plus_span")).Count>0,"Project central should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("measures_leftnav_plus_span")).Count>0,"Measures should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("references_leftnav_plus_span")).Count>0," References should have +icon in navigation bar");
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("more_leftnav_plus_span")).Count>0," More should have +icon in navigation bar");
            
            HtmlSpan plusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("projcentral_leftnav_plus_span"));
            plusIcon1.Click();
            plusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("projcentral_leftnav_minus_span")).Count>0,"Project central should have - icon in navigation bar");
            HtmlSpan minusIcon1 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("projcentral_leftnav_minus_span"));
            minusIcon1.Click();
            minusIcon1.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("projcentral_leftnav_plus_span")).Count>0,"Project central should have +icon in navigation bar");
            
            
            HtmlSpan plusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("measures_leftnav_plus_span"));
            plusIcon2.Click();
            plusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("measures_leftnav_minus_span")).Count>0,"Measures should have - icon in navigation bar");
            HtmlSpan minusIcon2 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("measures_leftnav_minus_span"));
            minusIcon2.Click();
            minusIcon2.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("measures_leftnav_plus_span")).Count>0,"Measures should have +icon in navigation bar");
            
            HtmlSpan plusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("references_leftnav_plus_span"));
            plusIcon3.Click();
            plusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("references_leftnav_minus_span")).Count>0,"References should have - icon in navigation bar");
            HtmlSpan minusIcon3 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("references_leftnav_minus_span"));
            minusIcon3.Click();
            minusIcon3.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("references_leftnav_plus_span")).Count>0,"References should have +icon in navigation bar");
            
           
            HtmlSpan plusIcon4 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("more_leftnav_plus_span"));
            plusIcon4.Click();
            plusIcon4.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("more_leftnav_minus_span")).Count>0,"More should have - icon in navigation bar");
            HtmlSpan minusIcon4 = ActiveBrowser.Find.ByXPath<HtmlSpan>(AppLocators.get("more_leftnav_minus_span"));
            minusIcon4.Click();
            minusIcon4.Wait.ForExistsNot();
            ActiveBrowser.RefreshDomTree();
            Assert.IsTrue(ActiveBrowser.Find.AllByXPath(AppLocators.get("more_leftnav_plus_span")).Count>0,"More should have +icon in navigation bar");
            
           
                       
        }
    }
}
