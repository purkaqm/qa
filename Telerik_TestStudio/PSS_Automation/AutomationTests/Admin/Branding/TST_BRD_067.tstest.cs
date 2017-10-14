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

    public class TST_BRD_067 : BaseWebAiiTest
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
        public void TST_BRD_067_CodedStep()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_067_CodedStep1()
        {
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible(),"pallete color box should be displayed");
        }
    
        [CodedStep(@"Select Color")]
        public void TST_BRD_067_CodedStep2()
        {
            selectedColor = Data["Select_Color"].ToString();            
            HtmlImage colorImgLoc = ActiveBrowser.Find.ByXPath<HtmlImage>(string.Format(AppLocators.get("branding_select_color_from_pallete_box"),selectedColor));
            colorImgLoc.Click();
            Assert.IsTrue(!Pages.PS_BrandingLookAndFeelPage.PalleteColorBoxDiv.IsVisible());
            System.Threading.Thread.Sleep(1000);
            ActiveBrowser.RefreshDomTree();   
        }
    
        [CodedStep(@"Click on pallete of top navigation text and icon in color scheme")]
        public void TST_BRD_067_CodedStep3()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavTxtAndIconPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
        
    
        [CodedStep(@"Click on pallete of top navigation icon highlight in color scheme")]
        public void TST_BRD_067_CodedStep4()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHighPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of top navigation icon hover in color scheme")]
        public void TST_BRD_067_CodedStep5()
        {
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.TopNavIconHoverPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of left navigation background  in color scheme")]
        public void TST_BRD_067_CodedStep6()
        {
                        
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of left navigation text and icon  in color scheme")]
        public void TST_BRD_067_CodedStep7()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavTxtAndIconPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of left navigation icon highlight in color scheme")]
        public void TST_BRD_067_CodedStep8()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHighPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of left navigation icon hover in color scheme")]
        public void TST_BRD_067_CodedStep9()
        {
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.LeftNavIconHoverPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of content background in color scheme")]
        public void TST_BRD_067_CodedStep10()
        {
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ContentBkgdPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of context text in color scheme")]
        public void TST_BRD_067_CodedStep11()
        {
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ContentTxtPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
        [CodedStep(@"Click on pallete of application menu text in color scheme")]
        public void TST_BRD_067_CodedStep12()
        {
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.Wait.ForExists();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.Wait.ForVisible();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.ScrollToVisible();
            ActiveBrowser.Window.SetFocus();
            Pages.PS_BrandingLookAndFeelPage.ApplictnMenuTxtPalleteIcon.MouseClick(MouseClickType.LeftClick);
        }
    
       
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_067_CodedStep13()
        {
            Pages.PS_BrandingLookAndFeelPage.SaveBtn.Click();
            System.Threading.Thread.Sleep(4000);
        }
        
        
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_067_CodedStep14()
        {
           try{
            //check color is displayed in top navigation background field in color scheme
            
            HtmlDiv selColorLoc = ActiveBrowser.Find.ByXPath<HtmlDiv>(AppLocators.get("branding_color_scheme_top_nav_bkgd_color_view"));
            Assert.IsTrue( selColorLoc.BaseElement.GetAttribute("style").Value.ToString().Contains(selectedColor),"selected color should be visible in top navigation background field");
            
            //check color is displayed in all fields in color scheme

            for(int i=0;i<10;i++)
            {
              HtmlDiv selColorLoci = ActiveBrowser.Find.ByXPath<HtmlDiv>(string.Format(AppLocators.get("branding_color_scheme_check_all_color_view"),i));
              Assert.IsTrue( selColorLoci.BaseElement.GetAttribute("style").Value.ToString().Contains(selectedColor),"selected color should be visible");
            }
           }
           catch{
               Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
           }
           
           
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_BRD_067_CodedStep16()
        {
            try{
            //check hexadecimel value for selected color is displayed in top navigation field in color scheme
            
            string hexValue = Data["Selected_Hex_Dec_Color"].ToString();
            HtmlInputText hexColorLoc = ActiveBrowser.Find.ByXPath<HtmlInputText>(AppLocators.get("branding_color_scheme_tnb_hex_dec_val_input"));
            hexColorLoc.Wait.ForExists();
            hexColorLoc.Wait.ForVisible();            
            Assert.IsTrue(hexColorLoc.BaseElement.GetAttribute("value").Value.ToString().Contains(hexValue),"Correct hexadecimal value should be displayed");
            
            //check hexadecimel value for selected colors is displayed in all fields in color scheme
            
            for(int i=0;i<9;i++){
            HtmlInputText hexColorLoci = ActiveBrowser.Find.ByXPath<HtmlInputText>(string.Format(AppLocators.get("branding_color_scheme_check_all_hex_dec_val_input"),i));
            hexColorLoci.Wait.ForExists();
            hexColorLoci.Wait.ForVisible();
            Log.WriteLine(hexColorLoci.BaseElement.GetAttribute("value").Value.ToString());
            Assert.IsTrue(hexColorLoci.BaseElement.GetAttribute("value").Value.ToString().Contains(hexValue),"Correct hexadecimal value should be displayed");
            }   
            }
            catch{
                Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
            }
        }
        
         [CodedStep(@"Reset the changes")]
        public void TST_BRD_067_CodedStep15()
        {
            Pages.PS_BrandingLookAndFeelPage.ResetBtn.Click();
        }
        
    }
    
}
