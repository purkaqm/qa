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

    public class TST_FAV_005 : BaseWebAiiTest
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
    
        //[CodedStep(@"Get Page URL for reference")]
        //public void TST_Verify_Max_Char_Limits_Name_Desc_Fields_CodedStep()
        //{
            //SetExtractedValue("PageURL", ActiveBrowser.Url);
        //}
    
        //[CodedStep(@"Verify Page title is pre populated in Name field ")]
        //public void TST_Verify_Max_Char_Limits_Name_Desc_Fields_CodedStep1()
        //{
            //Element nameInput = ActiveBrowser.Find.ById("addToFavouritesURL");
            //string attr = nameInput.GetAttribute("value").Value;
            //string currPageUrl = GetExtractedValue("PageURL").ToString();
            //Assert.IsTrue(attr.Equals(currPageUrl), "Name field should be pre populated with current page title");
        //}
    
        [CodedStep(@"Enter all details and add current page to favorites")]
        public void TST_Verify_Max_Char_Limits_Name_Desc_Fields_CodedStep2()
        {
            ActiveBrowser.Window.SetFocus();
            string projName = "Hello psteering users, this project name is having more than 75 characters.";
            string projNameExtra = "This is extra";
            ActiveBrowser.Actions.SetText(Pages.PS_HomePage.AddToFavDialogNameText,"");
            
            Pages.PS_HomePage.AddToFavDialogNameText.MouseClick(MouseClickType.LeftClick);
            Pages.PS_HomePage.AddToFavDialogNameText.Focus();
            Manager.Desktop.KeyBoard.TypeText(projName+projNameExtra,2);
                       
            ActiveBrowser.RefreshDomTree();
            string acceptedName = Actions.InvokeScript<string>("document.getElementById(\"addToFavouritesName\").value");
            Assert.IsTrue(acceptedName.Equals(projName) && !acceptedName.Contains(projNameExtra),"Name should accept only 75 characters");
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Verify_Max_Char_Limits_Name_Desc_Fields_CodedStep()
        {
            string projDesc = "This life, which had been the tomb of his virtue and of his honour, is but a walking shadow; a poor player, that struts and frets his hour upon the stage, and then is heard no more: it is a tale magic";
            string projDescExtra = "This is extra";
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogDescTextArea,"");
            Pages.PS_HomePage.AddToFavDialogDescTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_HomePage.AddToFavDialogDescTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(projDesc+projDescExtra,1);
                             
            ActiveBrowser.RefreshDomTree();
            string acceptedDesc = Actions.InvokeScript<string>("document.getElementById(\"addToFavouritesDescription\").value");
            Assert.IsTrue(acceptedDesc.Equals(projDesc) && !acceptedDesc.Contains(projDescExtra),"Description should accept only 200 characters");
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_Verify_Max_Char_Limits_Name_Desc_Fields_CodedStep1()
        //{
            //Pages.PS_HomePage.AddToFavDialogCancelButton.Click();
            //Pages.PS_HomePage.AddToFavDialogTitleDiv.Wait.ForVisibleNot();
            //Assert.IsFalse(Pages.PS_HomePage.AddToFavDialogNameText.IsVisible(),"Cancel Button on Add to Favorites should work properly");
            
        //}
    }
}
