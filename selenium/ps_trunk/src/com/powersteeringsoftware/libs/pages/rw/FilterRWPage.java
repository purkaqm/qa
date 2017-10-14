package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.elements.rw.RWColumnFilterControl;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.objects.rw.RWColumn;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;


public class FilterRWPage extends ReportWizardPage {
    private Map<String, Map<String, String>> columnChBoxes = new HashMap<String, Map<String, String>>();

    protected FilterRWPage() {
        super(ReportWizardPageLocators.RWTab.Filter);
    }

    public Collection<String> getColumnsPresented() {
        return getColumnsData(null).values();
    }


    public void checkAllColumns(String groupName) {
        Set<String> columnChBoxIds = getColumnsData(groupName).keySet();
        for (String id : columnChBoxIds) {
            new CheckBox("id=" + id).check();
        }
    }


    public void checkColumn(RWColumn column) {
        CheckBox chBox = getColumnCheckBox(column);
        Assert.assertTrue(chBox != null, "Chechbox for column '" + column.getFullColumnName() + "' was not found");
        chBox.check();
    }

    public boolean isAdditionalFiltersVisible() {
        Element additionalFiltersElm = searchElement(FILTER_TAB_ADDITIONAL_FILTERS_ELEMENT);
        return (additionalFiltersElm != null) && additionalFiltersElm.isVisible();
    }

    public void openAdditionalFilters() {
        getAddtitionalFiltersLink().click(false);
    }

    public boolean isColumnGroupVisible(String columnGroup) {
        Element groupElm = searchElement(FILTER_TAB_COLUMNGROUP_ELEMENT.replace(columnGroup));
        return (groupElm != null) && groupElm.isVisible();
    }

    public void openColumnGroup(String groupName) {
        getColumnGroupLink(groupName).click(false);
    }

    public RWColumnFilterControl getColumnFilterControl(RWColumn column) {
        if (!column.isFiltered()) return null;
        Element filterElm = Element.getElementByXpath(FILTER_TAB_COLUMNFILTER_CONTROL_ELEMENT.replace(column.getFullColumnName()));
        return RWColumnFilterControl.getRWColumnFilter(filterElm, column.getFilterValue().getFilterType());
    }

    public void clickAddMoreFilters() {
        new Link(Element.getElementByXpath(FILTER_TAB_ADDMORE_FILTER_LINK.getLocator())).click(true);
        setDocument();
    }

    private Link getColumnGroupLink(String groupName) {
        Element groupLinkElm = Element.getElementByXpath(FILTER_TAB_COLUMNGROUP_LINK.replace(groupName));
        return new Link(groupLinkElm);
    }

    private Link getAddtitionalFiltersLink() {
        Element linkElm = searchElement(FILTER_TAB_ADDITIONALFILTERS_LINK);
        return new Link(linkElm);
    }

    private CheckBox getColumnCheckBox(RWColumn column) {
        Map<String, String> colData = getColumnsData(column.getGroup());
        for (Map.Entry<String, String> item : colData.entrySet()) {
            if (item.getValue().equals(column.getName())) {
                String chBxId = item.getKey();
                return new CheckBox("id=" + chBxId);
            }
        }
        return null;
    }


    private Map<String, String> getColumnsData(String groupName) {
        Map<String, String> columns = this.columnChBoxes.get(groupName);
        if (columns == null) {
            columns = new HashMap<String, String>();
            this.columnChBoxes.put(groupName, columns);
            Element parentContainerElm = groupName == null ?
                    searchElement(FILTER_TAB_PREVIOUSLY_CHOSEN_COLUMNS_ELEMENT) :
                    searchElement(FILTER_TAB_AF_COLUMNGROUP_ELEMENT.replace(groupName));
            List<Element> labelElms = Element.searchElementsByXpath(parentContainerElm, FILTER_TAB_COLUMN_LABEL);
            for (Element labelElm : labelElms) {
                String chBoxId = labelElm.getAttribute("for");
                String columnName = getColumnName(labelElm);
                if (columnName == null) {
                    columnName = labelElm.getText();
                }
                columns.put(chBoxId, columnName);
            }
        }
        return columns;
    }

    private String getColumnName(Element labelElm) {
        String labelFor = labelElm.getAttribute("for");
        Element hoverElm = new Element("id=" + labelFor + HOVER);
        return ((hoverElm != null) && hoverElm.exists()) ? hoverElm.getInnerText() : null;
    }

} // class FilterRWPage
