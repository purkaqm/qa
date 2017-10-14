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

    public class TST_NAV_UI_030 : BaseWebAiiTest
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
        public void TST_NAV_UI_30_CodedStep()
        {
            float homeHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Home']\").getBoundingClientRect().top");
            float inboxHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Inbox']\").getBoundingClientRect().top");
            float addHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Add']\").getBoundingClientRect().top");
            float reviewHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Review']\").getBoundingClientRect().top");
            float adminHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Admin']\").getBoundingClientRect().top");
            float projectHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Project']\").getBoundingClientRect().top");
            
            float favoritesHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Favorites']\").getBoundingClientRect().top");
            float historyHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='History']\").getBoundingClientRect().top");
            float impLinksHeight = Actions.InvokeScript<float>("document.querySelector(\"li a[title='Important Links']\").getBoundingClientRect().top");
            
            float separatorHeight = Actions.InvokeScript<float>("document.querySelector(\".navBarDivider\").getBoundingClientRect().top");
            
            Assert.IsTrue( separatorHeight > homeHeight && separatorHeight > addHeight && separatorHeight > inboxHeight && separatorHeight > reviewHeight && separatorHeight > projectHeight && separatorHeight > adminHeight, "Separator should be after Home, Add, Inbox, Review, Project and Admin icons in icon bar");
            Assert.IsTrue( separatorHeight < favoritesHeight && separatorHeight < impLinksHeight && separatorHeight < historyHeight, "Separator should be before Favorites, Hisotory and Important Links icons in icon bar");
        }
    }
}
