package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.objects.rw.RWColumn;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;


public class GroupSortRWPage extends ReportWizardPage {

    protected GroupSortRWPage() {
        super(ReportWizardPageLocators.RWTab.GroupSort);
    }

    public void setGroupBy(RWColumn column, int level) {
        getGroupBySelector(level).select(column.getFullColumnName());
        waitForPageToLoad();
        getSortBySelector(level).select(column.getSortValue().getSortOrder().name());
        if (column.getSortValue().isCustomSort()) {
            getCustomSortChBox(level).check();
        }
    }


    public void appendAdditionalSort(RWColumn column) {
        getAdditionalSortColumnSelector().select(column.getName());
        getAdditionalSortAddButton().click(true);
        setDocument();
    }

    public void setAdditionalSortValue(RWColumn column) {
        SelectInput selector = getAdditionalColumnOrderSelector(column.getFullColumnName());
        selector.select(column.getSortValue().getSortOrder().name());
    }

    public boolean isAdditionalSortExist(RWColumn column) {
        Element selElm = Element.searchElementByXpath(getAddtionalSortContainer(), SORT_TAB_COLUMN_ELEMENT.replace(column.getFullColumnName()));
        return selElm != null;
    }

    private SelectInput getAdditionalColumnOrderSelector(String columnName) {
        Element selElm = searchElement(SORT_TAB_ADDSORT_ORDER_SELECTOR.replace(columnName));
        return new SelectInput(selElm);
    }

    public SelectInput getAdditionalSortColumnSelector() {
        Element selectorElm = Element.searchElementByXpath(getAddtionalSortContainer(), SORT_TAB_ADDSORT_COLUMN_SELECTOR);
        return new SelectInput(selectorElm);
    }

    private Button getAdditionalSortAddButton() {
        Element inputElm = Element.searchElementByXpath(getAddtionalSortContainer(), SORT_TAB_ADDSORT_ADD_BUTTON);
        return new Button(inputElm);
    }

    private SelectInput getGroupBySelector(int level) {
        Element selectorElm = Element.searchElementByXpath(getGroupContainer(),
                SORT_TAB_GROUPBY_COLUMN_SELECTOR.replace(String.valueOf(level)));
        return new SelectInput(selectorElm);
    }

    private SelectInput getSortBySelector(int level) {
        Element selectorElm = Element.searchElementByXpath(getGroupContainer(),
                SORT_TAB_GROUPBY_ORDER_SELECTOR.replace(String.valueOf(level)));
        return new SelectInput(selectorElm);
    }

    private CheckBox getCustomSortChBox(int level) {
        Element chBoxElm = Element.searchElementByXpath(getGroupContainer(),
                SORT_TAB_GROUPBY_CUSTOM_CHECKBOX.replace(String.valueOf(level)));
        return new CheckBox(chBoxElm);
    }

    private Element getGroupContainer() {
        Element groupByContainer = searchElement(SORT_TAB_GRPUPBY_ELEMENT);
        return groupByContainer;
    }

    private Element getAddtionalSortContainer() {
        Element additionalSortElm = searchElement(SORT_TAB_ADDITIONALSORT_ELEMENT);
        return additionalSortElm;
    }

} //  class GroupSortRWPage 
