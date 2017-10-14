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
    
    public class Utils : BaseWebAiiTest
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
        public void Utils_CodedStep()
        {
               
        }
        
        
    }
    public static class CustomUtils {
        
        #region Variables
        public static string userName1 = "pankaj";
        public static string adminName1 = "Pratik";
        public static string locationValue;
        public static string defaultLocationWorkTree = "Idea Pipeline";
        public static string userName;
                
        #endregion
        
        #region Functions
        public static string getProjectBaseURL(string projectPath){
                
            string projectSettingsFile = projectPath+"\\Settings.aiis";
            string fileContent = System.IO.File.ReadAllText(projectSettingsFile);
            string baseURLPart = System.Text.RegularExpressions.Regex.Split(fileContent,"\"RecorderBaseUrl\": ")[1];
            System.Text.RegularExpressions.Regex regex = new System.Text.RegularExpressions.Regex("\"(.*)\"");   
            var v = regex.Match(baseURLPart);
            string baseURL = v.Groups[1].ToString();
            return baseURL;
        }
        
        public static HtmlControl waitForElementToExist(Browser browser, string elementLocator,  int timeOutSeconds){
                
          DateTime currentTime = DateTime.Now;
          DateTime future = currentTime.AddSeconds(timeOutSeconds);
          while (future > currentTime)
          {
               
                if(browser.Find.AllByXPath(elementLocator).Count > 0){
                   HtmlControl element = browser.Find.ByXPath<HtmlControl>(elementLocator);
                   return element;
                }
               
                browser.RefreshDomTree();
                currentTime = DateTime.Now;
                
          }
          return null;
        }
        #endregion
       
    }
}
