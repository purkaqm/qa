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

    public class TST_FAV_006 : BaseWebAiiTest
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
    
        [CodedStep(@"Cancel Add to Favorites")]
        public void TST_Cancel_Add_To_Fav_Action_Blank_Name_CodedStep()
        {
            Pages.PS_HomePage.AddToFavDialogCancelButton.Click();
            Pages.PS_HomePage.AddToFavouritesDialogDiv.Wait.ForVisibleNot();
            Assert.IsFalse(Pages.PS_HomePage.AddToFavDialogNameText.IsVisible(),"Cancel Button on Add to Favorites should work properly");
            
        }
    
        [CodedStep(@"New Coded Step")]
        public void TST_Cancel_Add_To_Fav_Action_Blank_Name_CodedStep1()
        {
            Actions.SetText(Pages.PS_HomePage.AddToFavDialogNameText,"");
            Pages.PS_HomePage.AddToFavDialogAddButton.Click();
            Pages.PS_HomePage.AddToFavDialogNameErrorDiv.Wait.ForVisible();
            Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogNameErrorDiv.InnerText.Contains("Please, fill the name field."),"Blank Name validation message should be displayed correctly");
        }
    }
}
