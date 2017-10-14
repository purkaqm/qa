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

    public class TST_SRT_008 : BaseWebAiiTest
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
        public void CS_SRT_008_01()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewBtn.Click();
                                    ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Wait till Add new Status Report Template is displayed")]
        public void CS_SRT_008_02()
        {
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForExists();
                                    Pages.PS_StatusReportTemplatesPage.AddNewTemplateDialogDiv.Wait.ForVisible();
                                   
                                   
        }
        
         [CodedStep(@"Verify system accepts maximum 255 characters for Description field")]
        public void CS_SRT_008_03()
        {
                     
            ActiveBrowser.Window.SetFocus();
            string srtDesc = "SRTDescription1234567890123456789012345678901234567890 This life, which had been the tomb of his virtue and of his honour, is but a walking shadow; a poor player, that struts and frets his hour upon the stage, and then is heard no more: it is a tale magic";
            string srtDescExtra = "This is extra";
            ActiveBrowser.Actions.SetText(Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea,"");
            
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.MouseClick(MouseClickType.LeftClick);
            Pages.PS_StatusReportTemplatesPage.AddNewTemplateDscTextArea.Focus();
            Manager.Desktop.KeyBoard.TypeText(srtDesc+srtDescExtra,1);
                       
            ActiveBrowser.RefreshDomTree();
            string acceptedName = Actions.InvokeScript<string>("document.getElementById(\"addNewTemplateDialogDsc\").value");
            Assert.IsTrue(acceptedName.Equals(srtDesc) && !acceptedName.Contains(srtDescExtra),"Description should accept only 255 characters");
        }
    }
}
