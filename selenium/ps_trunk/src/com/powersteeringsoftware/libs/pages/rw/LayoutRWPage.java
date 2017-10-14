package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class LayoutRWPage extends ReportWizardPage {

    protected LayoutRWPage() {
        super(ReportWizardPageLocators.RWTab.Layout);
    }

    public void setPaperSize(String pSize) {
        getPaperSizeSelector().select(pSize);
    }

    public void setTemplate(String template) {
        getTemplateSelector().select(template);
    }

    private SelectInput getPaperSizeSelector() {
        return new SelectInput(LAYOUT_TAB_PAPERSIZE_SELECTOR);
    }

    private Input getColumnTitleInput(String columnName) {
        Element inputElm = Element.searchElementByXpath(getTRElement(columnName), LAYOUT_TAB_COLUMN_TITLE_INPUT);
        return new Input(inputElm);
    }

    private SelectInput getTemplateSelector() {
        return new SelectInput(LAYOUT_TAB_TEMPLATE_SELECTOR);
    }


    private Element getTRElement(String columnName) {
        Element tr = searchElement(LAYOUT_TAB_COLUMN_ROW_ELEMENT.replace(columnName));
        return tr;
    }

} // class LayoutRWPage
