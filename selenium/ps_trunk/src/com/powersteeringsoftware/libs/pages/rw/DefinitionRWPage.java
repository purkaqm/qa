package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class DefinitionRWPage extends ReportWizardPage {

    protected DefinitionRWPage() {
        super(ReportWizardPageLocators.RWTab.Definition);
    }

    public void setPortfolio(String name) {
        getPortfolioSelector().select(name);
    }


    public void setParentFolder(String fName) {
        WorkMultiChooserDialog wchDlg = getWorkChooser();
        wchDlg.open();
        wchDlg.openBrowseTab();
        wchDlg.selectWorkOnBrowseTab(fName);
        wchDlg.clickSave();
    }

    public void setWorkTypeSelected(String workType) {
        CheckBox chBox = getCheckBoxForWorkType(workType);
        chBox.check();
    }

    public void addNewFilter(String flterName) {
        getMoreFiltersSelector().select(flterName);
        clickAddModeFilterButton();
    }

    public void setStatusFilter(String statusName) {
        SelectInput statusSelector = getWorkStatusSelector();
        statusSelector.select(statusName);
    }

    public void setRangeType(String rangeName) {
        SelectInput rangeTypeSelector = getRangeTypeSelector();
        rangeTypeSelector.select(rangeName);
    }

    public void setMetricFilter(String metricName) {
        SelectInput metricSelector = getMetricFilterSelector();
        metricSelector.select(metricName);
    }

    public void setRelatedWork(String workName) {
        WorkChooserDialog workChooserDlg = getRelatedWorkChooser();
        workChooserDlg.open();
    }


    public boolean isFilterBlockPresent(String filterName) {
        Element filterBlock = searchElement(DEF_TAB_FILTER_ELEMENT.replace(filterName));
        return filterBlock != null;
    }


    private CheckBox getCheckBoxForWorkType(String wType) {
        Element labelChBox = searchElement(DEF_TAB_WORK_TYPE_LABEL_BY_LABEL.replace(wType));
        if (labelChBox == null) {
            labelChBox = searchElement(DEF_TAB_WORK_TYPE_LABEL_BY_ACRONYM.replace(wType));
        }
        String chBxId = null;
        if (labelChBox != null) {
            chBxId = labelChBox.getAttribute("for");
        } else {
            chBxId = getChBoxIdByHover(wType);
        }
        Assert.assertTrue(chBxId != null, "Work type '" + wType + "' was not found");
        return new CheckBox("id=" + chBxId);
    }


    private String getChBoxIdByHover(String wType) {
        Element hoverChBox = searchElement(DEF_TAB_WORK_TYPE_LABEL_BY_HOVER.replace(wType));
        String hoverId = hoverChBox != null ? hoverChBox.getAttribute("id") : null;
        return hoverId != null ? hoverId.substring(0, hoverId.length() - HOVER.length()) : null;
    }

    private void clickAddModeFilterButton() {
        new Button(DEF_TAB_ADD_MORE_FILTER_BUTTON).click(true);
        setDocument();
    }


    private SelectInput getWorkStatusSelector() {
        Element selElm = searchElement(DEF_TAB_WORKSTATUS_SELECTOR);
        return selElm != null ? new SelectInput(selElm) : null;
    }

    private SelectInput getMetricFilterSelector() {
        Element selElm = searchElement(DEF_TAB_METRICFILTER_SELECTOR);
        return selElm != null ? new SelectInput(selElm) : null;
    }


    private WorkMultiChooserDialog getWorkChooser() {
        final Element elm = searchElement(DEF_TAB_WORKCHOOSER_ELEMENT);
        final String id = elm.getAttribute("id");
        final String popupId = elm.getAttribute("linksboxid");
        return new WorkMultiChooserDialog(id, "//div[@id='" + popupId + "']");
    }


    private WorkChooserDialog getRelatedWorkChooser() {
        final Element linkElm = searchElement(DEF_TAB_RELATED_WORKCHOOSER_ELEMENT);
        final String id = linkElm.getAttribute("id");
        Element popupElm = searchElement(linkElm.getXpathLocator() + "/following-sibling::input");
        final String popupId = popupElm.getAttribute("id");
        return new WorkChooserDialog("id=" + id, "id=" + popupId);
    }

    private SelectInput getPortfolioSelector() {
        return new SelectInput(DEF_TAB_PORTFOLIO_SELECTOR);
    }

    private SelectInput getMoreFiltersSelector() {
        return new SelectInput(DEF_TAB_MORE_FILTER_SELECTOR);
    }

    private SelectInput getRangeTypeSelector() {
        return new SelectInput(DEF_TAB_RANGE_TYPE_SELECTOR);
    }


}//  class DefinitionRWPage 
