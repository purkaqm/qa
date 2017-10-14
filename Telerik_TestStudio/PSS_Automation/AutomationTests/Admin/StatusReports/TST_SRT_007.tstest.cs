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

    public class TST_SRT_007 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Add New button")]
        public void CS_SRT_007_01()
        {
                        Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                        Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                        ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Wait till Add new Status Report Template is displayed")]
        public void CS_SRT_007_02()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
                                   
                                   
        }
    
        [CodedStep(@"Verify system accepts maximum 50 characters for Name field")]
        public void CS_SRT_007_03()
        {
            ActiveBrowser.Window.SetFocus();
            string srtName = "thesrtname1234567890123456789012345678901234567890";
            string srtNameExtra = "This is extra";
            ActiveBrowser.Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput,"");
            
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.MouseClick(MouseClickType.LeftClick);
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateNameInput.Focus();
            Manager.Desktop.KeyBoard.TypeText(srtName+srtNameExtra,2);
                       
            ActiveBrowser.RefreshDomTree();
            string acceptedName = Actions.InvokeScript<string>("document.getElementById(\"addNewTemplateDialogName\").value");
            Assert.IsTrue(acceptedName.Equals(srtName) && !acceptedName.Contains(srtNameExtra),"Name should accept only 50 characters");
        }
    }
}
