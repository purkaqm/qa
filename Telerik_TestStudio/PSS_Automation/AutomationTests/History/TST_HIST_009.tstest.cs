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

    public class TST_HIST_009 : BaseWebAiiTest
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
    
        [CodedStep(@"Verify user is navigated to Manage History page")]
        public void CSHIST00901()
        {
                        Pages.PS_ManageHistoryPage.GoButton.Wait.ForExists();
                        Assert.IsTrue(Pages.PS_ManageHistoryPage.SearchInputBox.IsVisible(),"User should be navigated to History page");
                        Assert.IsTrue(Pages.PS_ManageHistoryPage.FromDateInputBox.IsVisible(),"User should be navigated to History page");
                        Assert.IsTrue(Pages.PS_ManageHistoryPage.GoButton.IsVisible(),"User should be navigated to History page");
                       
        }
    
        [CodedStep(@"verify history record wiht add to favorite icon is present")]
        public void CSHIST00902()
        {
                       
                        
                        IList<Element> nameElements = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_name_records"));
                        Assert.IsTrue(nameElements[0].InnerText.Contains("Dashboard"),"Dashboard should be displayed in history table name coumn");
                        IList<Element> addToFaveList = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_add_to_fav_icon_records"));
                        Assert.IsTrue(nameElements.Count == addToFaveList.Count ,"Add to favorite icon should be present for each record");
                                   
                                    
        }
        
        [CodedStep(@"verify add to favorit pop up is displayed when user clicks on icon")]
        public void CSHIST00903()
        {
                       
                        
                        IList<Element> addToFaveList = ActiveBrowser.Find.AllByXPath(AppLocators.get("manage_history_add_to_fav_icon_records"));
                        Actions.Click(addToFaveList[0]);
                        Pages.PS_HomePage.AddToFavouritesDialogDiv.Wait.ForVisible();
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogTitleDiv.InnerText.Contains("Add to Favorites"),"Add to Favorites dialog should be displayed");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogNameText.IsVisible(),"Name field should be present in Add to Favorites dialog");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogDescTextArea.IsVisible(),"Description textarea should be present in Add to Favorites dialog");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogPinnedCheckBox.IsVisible(),"Pinned check box should be displayed in Add to Favorites dialog");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogGoToFavCheckBox.IsVisible(),"Go to Favorites page chec box should be present in Add to Favorites dialog");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogAddButton.IsVisible(),"Add button should be displayed in Add to Favorites dialog");
                        Assert.IsTrue(Pages.PS_HomePage.AddToFavDialogCancelButton.IsVisible(),"Cancel button should be present in Add to Favorites dialog");
                        string attr = ActiveBrowser.Actions.InvokeScript("document.getElementById(\"addToFavouritesName\").value;").ToString();
                        Assert.IsTrue(attr.Equals("Dashboard"), "Name field should be pre populated");           
        }
        
        
    }
}
