package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.pages.CustomColumnEditPage;
import com.powersteeringsoftware.tests.reports.TestData;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class ColumnsRWPage extends ReportWizardPage {
    private static String ARIFMETIC_CALC_COLUMN_TYPE = "Arithmetic calculation";

    protected ColumnsRWPage() {
        super(ReportWizardPageLocators.RWTab.Columns);
    }

    public void selectColumn(String group, String columnName) {
        getColumnCheckBox(group, columnName).check();
    }

    public void addProjectColumnsGroup(String groupType) {
        SelectInput columnGroupSelector = new SelectInput(COL_TAB_COLUMNGROUP_SELECTOR);
        columnGroupSelector.select(groupType);

        Button addBtn = new Button(searchElement(COL_TAB_ADD_BUTTON));
        addBtn.click(true);
        setDocument();
    }


    public void createCustomColumn(String columnType, String columnName, String expression) {
        SelectInput columnTypeSelector = new SelectInput(COL_TAB_CUSTOM_COLUMN_TYPE_SELECTOR);
        columnTypeSelector.select(columnType);
        Button addBtn = new Button(searchElement(COL_TAB_ADD_CUSTOM_BUTTON));
        addBtn.click(true);
        setCustomColumnValues(columnType, columnName, expression);
        setDocument();
    }


    public void removeCustomColumn(String columnName) {
        CheckBox chBox = getCustomColumnCheckBox(columnName);
        chBox.check();
        getCustomColumnRemoveButton(columnName).click(true);
        setDocument();
    }

    public boolean isCustomColumnExist(String columnName) {
        Element custColElm = searchElement(COL_TAB_CUSTOM_COLUMN_ELEMENT.replace(columnName));
        return custColElm != null;
    }

    public void editCustomColumn(String columnType, String columnName, String expression) {
        getCustomColumnEditButton(columnName).click(true);
        setCustomColumnValues(columnType, columnName, expression);
        setDocument();
    }


    public void openColumnsGroup(String groupName) {
        getOpenGroupLink(groupName).click(false);
    }

    public boolean isColumnGroupPresent(String groupName) {
        Link groupLink = getOpenGroupLink(groupName);
        return (groupLink != null) && groupLink.exists();
    }


    public boolean isCustomColumnGroupVisible() {
        return isColumnGroupVisible(TestData.CUSTOM_GROUP_ID);
    }


    public boolean isColumnGroupVisible(String groupName) {
        Element groupElm = searchElement(COL_TAB_GROUP_ELEMENT.replace(groupName));
        return (groupElm != null) && (groupElm.isVisible());
    }


    private void setCustomColumnValues(String columnType, String columnName, String expression) {
        if (ARIFMETIC_CALC_COLUMN_TYPE.equals(columnType)) {
            ArifmeticCalculationCustomColumn arifmCalcCustomColumn = new ArifmeticCalculationCustomColumn();
            arifmCalcCustomColumn.setValues(columnName, expression);
        }
    }

    private CheckBox getCustomColumnCheckBox(String columnName) {
        Element custColElm = searchElement(COL_TAB_CUSTOMCOLUMN_CHBOX.replace(columnName));
        return new CheckBox(custColElm);
    }

    private Button getCustomColumnEditButton(String columnName) {
        Element custColElm = searchElement(COL_TAB_CUSTOMCOLUMN_EDIT_BUTTON.replace(columnName));
        return new Button(custColElm);
    }

    private Button getCustomColumnRemoveButton(String columnName) {
        Element custColElm = searchElement(COL_TAB_CUSTOMCOLUMN_REMOVE_BUTTON.replace(columnName));
        return new Button(custColElm);
    }

    private Link getOpenGroupLink(String groupName) {
        Element linkElm = searchElement(COL_TAB_GROUP_LINK.replace(groupName));
        return (linkElm != null ? new Link(linkElm) : null);
    }


    private CheckBox getColumnCheckBox(String groupName, String colName) {
        Element columnsGroup = new Element("id=" + groupName);
        Element labelChBox = Element.searchElementByXpath(columnsGroup, COL_TAB_COLUMN_CHBOX_BY_LABEL.replace(colName));
        if (labelChBox == null) {
            labelChBox = Element.searchElementByXpath(columnsGroup, COL_TAB_COLUMN_CHBOX_BY_ACRONYM2.replace(colName));
        }
        String chBxId = null;
        if (labelChBox != null) {
            chBxId = labelChBox.getAttribute("for");
        } else {
            chBxId = getChBoxIdByHover(groupName, colName);
        }
        Assert.assertTrue(chBxId != null, "Column '" + colName + "' was not found");
        return new CheckBox("id=" + chBxId);
    }

    private String getChBoxIdByHover(String groupName, String colName) {
        Element columnsGroup = new Element("id=" + groupName);
        Element hoverText = Element.searchElementByXpath(columnsGroup, COL_TAB_COLUMN_CHBOX_BY_HOVER.replace(colName));
        Element hoverElm = hoverText.getParent("div", "hoverPopup");
        String hoverId = hoverElm != null ? hoverElm.getAttribute("id") : null;
        return hoverId != null ? hoverId.substring(0, hoverId.length() - HOVER.length()) : null;
    }

    private class ArifmeticCalculationCustomColumn {
        private void setValues(String columnName, String expression) {
            CustomColumnEditPage customColPage = new CustomColumnEditPage();
            customColPage.setName(columnName);
            customColPage.setExpression(expression);
            customColPage.clickSave();
        }
    } // class ArifmeticCalculationCustomColumn


}// class ColumnsRWPage
