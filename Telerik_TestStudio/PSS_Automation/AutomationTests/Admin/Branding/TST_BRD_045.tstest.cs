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

    public class TST_BRD_045 : BaseWebAiiTest
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
        
       
        string selectedColor;
   
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep()
        {
           
            Pages.PS_BrandingLookAndFeelPage.PalleteDiv.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.PalleteDiv.MouseClick(MouseClickType.LeftClick);           
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible());
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep2()
        {    
            selectedColor = Data["Select_Color"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_email_logo_background_color_select"),selectedColor));
            colorImgLoc.Click();
            System.Threading.Thread.Sleep(3000);
            ActiveBrowser.RefreshDomTree();           
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep3()
        {            
            HtmlDiv selColorLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_email_logo_background_color_view"));
            Log.WriteLine( selColorLoc.BaseElement.GetAttribute("style").Value.ToString());
            Assert.IsTrue( selColorLoc.BaseElement.GetAttribute("style").Value.ToString().Contains(selectedColor),"selected color should be visible");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(4000);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_045_CodedStep5()
        {
            string hexValue = Data["Selected_Hex_Dec_Color"].ToString();
            HtmlInputText hexColorLoc = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("branding_email_logo_hex_dec_val_input"));
            hexColorLoc.Wait.ForExists();
            hexColorLoc.Wait.ForVisible();
            Log.WriteLine(hexColorLoc.BaseElement.GetAttribute("value").Value.ToString());
            Assert.IsTrue(hexColorLoc.BaseElement.GetAttribute("value").Value.ToString().Contains(hexValue),"Correct hexadecimal value should be displayed");
        }
    }
}
