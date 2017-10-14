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

    public class TS_Create_Dashboard_Layout : BaseWebAiiTest
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
    
        [CodedStep(@"Wait till Create Dashboard layout page is displayed")]
        public void TS_CS_00()
        {
            Pages.PS_CreateDashboardLayoutPage.NameText.Wait.ForExists();
            Pages.PS_CreateDashboardLayoutPage.DefaultCheckBox.Wait.ForExists();
            Pages.PS_CreateDashboardLayoutPage.DescriptionTextArea.Wait.ForExists();
        }
        
        //Name and Description details
        
        [CodedStep(@"Enter Name")]
        public void TS_CS_01()
        {
            string layoutName = Data["LayoutName"].ToString() + Randomizers.generateRandomInt(1000,9999);
            Pages.PS_CreateDashboardLayoutPage.NameText.Focus();
            Actions.SetText(Pages.PS_CreateDashboardLayoutPage.NameText,layoutName);
            SetExtractedValue("GeneratedLayoutName",layoutName);
        }
        
        [CodedStep(@"Check Defualt Layout checkbox")]
        public void TS_CS_02()
        {
            if(!Data["DefaultLayout"].ToString().ToLower().Contains("no"))
            {
                Pages.PS_CreateDashboardLayoutPage.DefaultCheckBox.Click();
            }
        }
        
        [CodedStep(@"Enter Description")]
        public void TS_CS_03()
        {
            if(Data["LayoutDescription"].ToString().Length >0)
            {
                ActiveBrowser.Window.SetFocus();
                Pages.PS_CreateDashboardLayoutPage.DescriptionTextArea.Focus();
                Actions.SetText(Pages.PS_CreateDashboardLayoutPage.DescriptionTextArea,Data["LayoutDescription"].ToString());
            }
        }
        
        [CodedStep(@"Click on 'Finish And Save Changes' button")]
        public void TS_CS_04()
        {
            Pages.PS_CreateDashboardLayoutPage.FinishSaveChangesButton.Focus();
            Pages.PS_CreateDashboardLayoutPage.FinishSaveChangesButton.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
        
        /*[CodedStep(@"Click on Continue button")]
        public void TS_CS_04()
        {
            Pages.PS_CreateDashboardLayoutPage.ContinueButton.Click();
            ActiveBrowser.WaitUntilReady();
            ActiveBrowser.RefreshDomTree();
        }
        
        //Special Columns: Tags, Custom Fields, Financials and Measures details
        
        [CodedStep(@"Add the custom field column")]
        public void TS_CS_05()
        {
            if(Data["AddCustomFields"].ToString().ToLower().Contains("no"))
            {
                Pages.PS_CreateDashboardLayoutPage.AddCustomFieldsImage.Focus();
                Pages.PS_CreateDashboardLayoutPage.AddCustomFieldsImage.Click();
                ActiveBrowser.Window.SetFocus();
                Pages.PS_CreateDashboardLayoutPage.AddCustomFeildPopUpCustomFieldSelect.SelectByText(GetExtractedValue("GeneratedCustomField").ToString());
                Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength.Focus();
                Actions.SetText(Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength,"");
                Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpDisplayLength.Click();
                Manager.Desktop.KeyBoard.TypeText("10000",2);
                
                Pages.PS_CreateDashboardLayoutPage.AddCustomFieldPopUpAddCFButton.Click();
                ActiveBrowser.RefreshDomTree();
                ActiveBrowser.Window.SetFocus();  
            }
        }
        
        */
        
        
    }
}
