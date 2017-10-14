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

    public class TST_NAV_UI_041 : BaseWebAiiTest
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
        public void TST_NAV_UI_041_CodedStep()
        {
            SetExtractedValue("UserSuffix", "1");
        }
    
        //[CodedStep(@"New Coded Step")]
        //public void TST_NAV_UI_041_CodedStep1()
        //{
            
        //}
    
        [CodedStep(@"Verify that long page tilte is truncated")]
        public void TST_NAV_UI_041_CodedStep2()
        {
             string firstname = Data["Firstname1"].ToString();
            string lastname = Data["Lastname1"].ToString();
            
            if(firstname.Length >= 10 ||lastname.Length >= 10  ){
                string textOverflowProperty = Pages.PS_HomePage.FirstNameLastNameDiv.GetComputedStyleValue("text-overflow");    
                Assert.IsTrue(textOverflowProperty.Equals("ellipsis"),"Long names should be truncated...");
            }
            
        }
    
        [CodedStep(@"Verify that it displays entire title on hovering the mouse on page title")]
        public void TST_NAV_UI_041_CodedStep3()
        {
            string firstname = Data["Firstname1"].ToString();
            string lastname = Data["Lastname1"].ToString();
            
            Pages.PS_HomePage.FirstNameLastNameDiv.MouseHover();
            System.Threading.Thread.Sleep(3000);
            Assert.IsTrue(Pages.PS_HomePage.FirstNameLastNameDiv.Attributes.Single(x => x.Name == "title").Value.Contains(firstname + " " + lastname),"On hovering the name, it should display full firstname and lastname");
        }
    }
}
