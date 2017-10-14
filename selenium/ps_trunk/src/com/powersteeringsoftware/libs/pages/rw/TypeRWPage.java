package com.powersteeringsoftware.libs.pages.rw;

import com.powersteeringsoftware.libs.elements.InfoBox;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWizardTabLocator.*;

public class TypeRWPage extends ReportWizardPage {
    private InfoBox descriptionBox;
    private InfoBox exampleBox;

    public TypeRWPage() {
        super(ReportWizardPageLocators.RWTab.Type);
    }


    public void saveState() {
        getDescriptionBox().saveState();
        getExampleBox().saveState();
    }


    public boolean isExampleChanged() {
        return getExampleBox().isStateChanged();
    }

    public boolean isDescriptionChanged() {
        return getDescriptionBox().isStateChanged();
    }

    public int getCategoryAmount() {
        return getCategorySelector().getOptionAmount();
    }

    public int getTypeAmount() {
        return getTypeSelector().getOptionAmount();
    }

    public void setCategoryByIndex(int index) {
        getCategorySelector().select(index);
        waitForPageToLoad();
    }

    public void setTypeByIndex(int index) {
        getTypeSelector().select(index);
        getExampleBox().waitForVisible();
    }

    public void setCategoryByName(String name) {
        if (name.equals(getCategorySelector().getSelectedLabel())) return;
        getCategorySelector().select(name);
        waitForPageToLoad();
    }

    public void setTypeByName(String name) {
        if (name.equals(getTypeSelector().getSelectedLabel())) return;
        getTypeSelector().select(name);
        getExampleBox().waitForVisible();
    }


    private InfoBox getExampleBox() {
        if (exampleBox == null) {
            exampleBox = new InfoBox(TYPE_TAB_EXAMPLE);
        }
        return exampleBox;
    }

    private InfoBox getDescriptionBox() {
        if (descriptionBox == null) {
            descriptionBox = new InfoBox(TYPE_TAB_DESCRIPTION);
        }
        return descriptionBox;
    }

    private SelectInput getTypeSelector() {
        return new SelectInput(TYPE_TAB_TYPE_SELECTOR);
    }

    private SelectInput getCategorySelector() {
        return new SelectInput(TYPE_TAB_CATEGORY_SELECTOR);
    }

} // class TypeRWPage 
