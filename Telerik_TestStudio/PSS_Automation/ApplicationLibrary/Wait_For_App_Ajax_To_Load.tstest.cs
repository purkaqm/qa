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

    public class Wait_For_App_Ajax_To_Load : BaseWebAiiTest
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
        public void Wait_For_App_Ajax_To_Load_CodedStep()
        {
           
            System.Threading.Thread.Sleep(2000);
            Wait.For<bool>(c => ActiveAjaxConnections() == true, false,Manager.Settings.ElementWaitTimeout);
        }
        
        public bool ActiveAjaxConnections()
        {
            try{
            System.Threading.Thread.Sleep(2000);
            return Pages.PS_ProjectPlanningLayoutPage.LoadingDiv.Attributes.Single(x => x.Name == "class").Value.Contains("hidden");
            }
            catch(Exception ex){
                return false;
            }
             
        }
    
        
    }
    
   
}
