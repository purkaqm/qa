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

    public class TST_BRD_121 : BaseWebAiiTest
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
        public void TST_BRD_121_CS01()
        {
            Pages.PS_BrandingCustomMessages.PowerPointLink.Wait.ForExists();
            Pages.PS_BrandingCustomMessages.PowerPointLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
              
    
        [CodedStep(@"Click on Name Column Header for sort")]
        public void TST_BRD_121_CS02()
        {
            Pages.PS_BrandingPowerPointPage.NameColumnLink.Wait.ForExists();
            Pages.PS_BrandingPowerPointPage.NameColumnLink.Click();
            //ActiveBrowser.Window.SetFocus();
            //Pages.PS_BrandingPowerPointPage.NameColumnLink.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Verify that records are sorted by Name in ascending order")]
        public void TST_BRD_121_CS03()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlImage ascArrowImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("branding_powerpoint_sort_asc_arrow_img"));
            ascArrowImgLoc.Wait.ForExists(); 
                       
            List<string> names = new List<string>();
            List<string> orderedByAsc = new List<string>();
            IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("powerpoint_template_name_records"));
            foreach (Element e in nameElements){
                names.Add(e.InnerText);
            }
            orderedByAsc = names;
            names.Sort();
            Assert.IsTrue(names.SequenceEqual(orderedByAsc),"Template should be ordered by ascending order of Names");
        }
        
        [CodedStep(@"Verify that records are sorted by Name in descending order")]
        public void TST_BRD_121_CS04()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlImage descArrowImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(AppLocators.get("branding_powerpoint_sort_desc_arrow_img"));
            descArrowImgLoc.Wait.ForExists(); 
            
            List<string> names = new List<string>();
            List<string> orderedByDesc = new List<string>();
            IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
            foreach (Element e in nameElements){
                names.Add(e.InnerText);
                Log.WriteLine(e.InnerText);
            }
           names.Sort();
           orderedByDesc = names;
           Assert.IsTrue(names.SequenceEqual(orderedByDesc),"Template should be ordered by descending order of Names");
            
        }
    }
}
