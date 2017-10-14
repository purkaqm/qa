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

    public class TST_CF_001 : BaseWebAiiTest
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
    
        [CodedStep(@"Click on Admin left navigation link")]
        public void TST_CF_001_CS01()
        {
                Pages.PS_HomePage.AdminLeftNavLink.Click();
                ActiveBrowser.WaitUntilReady();
                ActiveBrowser.RefreshDomTree();
        }
    
        [CodedStep(@"Click on 'AddNew' button on custom field page")]
        public void TST_CF_001_CS02()
        {
                Pages.PS_CustomFieldsPage.AddNewBtn.Click();
        }
    
        [CodedStep(@"Wait till Add/Edit Custom Field pop up is displayed")]
        public void TST_CF_001_CS03()
        {
                Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Wait.ForExists();                       
                Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForExists();
                Pages.PS_CustomFieldsPage.AddCustFieldSaveBtn.Wait.ForVisible();                                    
                                                                      
        }
    
        [CodedStep(@"Enter Name")]
        public void TST_CF_001_CS04()
        {
                string custFieldName = Data["CustomFieldName"].ToString() + Randomizers.generateRandomInt(1000,9999);
                ActiveBrowser.Window.SetFocus();
                Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Click();
                Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText.Focus();
                Actions.SetText(Pages.PS_CustomFieldsPage.AddNewFieldDialogNameText, custFieldName);
                SetExtractedValue("GeneratedCustomFieldName",custFieldName);
        }
    
        [CodedStep(@"Choose Field Type")]
        public void TST_CF_001_CS05()
        {
               if(Data["CustomFieldType"].ToString().Length > 0){
                   Pages.PS_CustomFieldsPage.AddCustFieldTypeSelector.SelectByText(Data["CustomFieldType"].ToString(),true);
                            
                }
            
        }
        
        [CodedStep(@"Verify tooltip is displayed - Maximum Text Length")]
        public void TST_CF_001_CS06()
        {
              ActiveBrowser.RefreshDomTree();
              Assert.IsTrue(Pages.PS_CustomFieldsPage.MaximumTextLengthColumn.IsVisible(),"Maximum test length tooltip should be displayed");
        }
    }
}
