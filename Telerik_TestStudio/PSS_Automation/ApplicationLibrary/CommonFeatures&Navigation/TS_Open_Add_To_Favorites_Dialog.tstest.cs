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

    public class TS_Open_Add_To_Favorites_Dialog : BaseWebAiiTest
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
    
        [CodedStep(@"New Coded Step")]
        public void TS_Open_Add_To_Favorites_Dialog_CodedStep()
        {
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
            Pages.PS_HomePage.HeaderFavoriteIconSpan.Wait.ForVisible();
            Pages.PS_HomePage.CenterContainerDiv.Wait.ForVisible();
            Pages.PS_HomePage.HeaderFavoriteIconSpan.Click();
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Open_Add_To_Favorites_Dialog_CodedStep1()
        {
            Pages.PS_HomePage.AddToFavouritesDialogDiv.Wait.ForVisible();
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TS_Open_Add_To_Favorites_Dialog_CodedStep2()
        {
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogTitleDiv.InnerText.Contains("Add to Favorites"),"Add to Favorites dialog should be displayed");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogNameText.IsVisible(),"Name field should be present in Add to Favorites dialog");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogDescTextArea.IsVisible(),"Description textarea should be present in Add to Favorites dialog");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogPinnedCheckBox.IsVisible(),"Pinned check box should be displayed in Add to Favorites dialog");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogGoToFavCheckBox.IsVisible(),"Go to Favorites page chec box should be present in Add to Favorites dialog");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogAddButton.IsVisible(),"Add button should be displayed in Add to Favorites dialog");
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogCancelButton.IsVisible(),"Cancel button should be present in Add to Favorites dialog");
            
        }
    }
}
