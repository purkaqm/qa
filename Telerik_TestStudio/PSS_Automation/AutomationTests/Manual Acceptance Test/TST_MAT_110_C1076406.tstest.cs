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

    public class TST_MAT_110_C1076406 : BaseWebAiiTest
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
        
        string portalName;
    
        [CodedStep(@"Click on 'Add Visual Portal' button")]
        public void TST_MAT_110_C1076406_ClickAddVisualButton()
        {
            Pages.PS_ReviewManageLayouts.ManageLayoutsAddVisualPortanButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Enter name and Click submit button")]
        public void TST_MAT_110_C1076406_EnterNameAndSubmit()
        {
            portalName = "NewPortal" + Randomizers.generateRandomInt(1000,9999);
            Pages.PS_ReviewAddVisualPortal.PortalSettingNameText.Wait.ForExists();
            Pages.PS_ReviewAddVisualPortal.EditVisualPortalSubmitButton.Wait.ForExists();
            Actions.SetText(Pages.PS_ReviewAddVisualPortal.PortalSettingNameText,portalName);
            
            Pages.PS_ReviewAddVisualPortal.EditVisualPortalSubmitButton.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for ManageLayouts page to be loaded")]
        public void TST_MAT_110_C1076406_WaitForManageLayoutsPage()
        {
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Manage Visual Portals");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
        }
    
        [CodedStep(@"Select newly created layout")]
        public void TST_MAT_110_C1076406_SelectNewLayout()
        {
            ActiveBrowser.RefreshDomTree();
            HtmlAnchor portalLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='nameColumnValue']//a[contains(.,'{0}')]",portalName));
            portalLink.Wait.ForExists();
            portalLink.Click();
            ActiveBrowser.WaitUntilReady();
        }
    
        [CodedStep(@"Wait for VisualPortal page to be loaded")]
        public void TST_MAT_110_C1076406_WaitForPortalPage()
        {
            ActiveBrowser.RefreshDomTree();
            Pages.PS_ReviewVisualPortal.VisualPortalLeftGoButtun.Wait.ForExists();
            Pages.PS_HomePage.PageTitleDiv.Wait.ForContent(FindContentType.InnerText,"Visual Portal");
            ActiveBrowser.WaitForAjax(Manager.Settings.ElementWaitTimeout);
                        
        }
    
        [CodedStep(@"Verify new layout has been choosen")]
        public void TST_MAT_110_C1076406_VerifyLayout()
        {
            string selectedValue = Pages.PS_ReviewVisualPortal.VisualPortalPropertySelectionSelect.SelectedOption.BaseElement.InnerText;
            Log.WriteLine(selectedValue);
            Assert.AreEqual(portalName,selectedValue);
        }
    
        [CodedStep(@"Delete created layout")]
        public void TST_MAT_109_C1076394_DeleteLayout()
        {
            HtmlAnchor downArrowLink = ActiveBrowser.Find.ByXPath<HtmlAnchor>(string.Format("//td[@class='nameColumnValue'][contains(.,'{0}')]/a[2]",portalName));
            downArrowLink.Wait.ForExists();
            downArrowLink.MouseClick(MouseClickType.LeftClick);
            ActiveBrowser.RefreshDomTree();
            System.Threading.Thread.Sleep(3000);
            HtmlTableCell deleteCell = ActiveBrowser.Find.ByXPath<HtmlTableCell>("//td[contains(@id,'ps_ui_MenuItem')][contains(.,'Delete')]"); 
            deleteCell.Wait.ForExists();
            deleteCell.Click();
            HtmlInputSubmit deleteYesButton = ActiveBrowser.Find.ByXPath<HtmlInputSubmit>("//input[@id='Submit_0']");
            deleteYesButton.Click(true);
            ActiveBrowser.WaitUntilReady();
            System.Threading.Thread.Sleep(3000);            
        }
    }
}
