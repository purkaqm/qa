package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;

import static com.powersteeringsoftware.libs.enums.elements_locators.WorkChooserDialogLocators.DLG_CANCEL_BUTTON;
import static com.powersteeringsoftware.libs.enums.elements_locators.WorkChooserDialogLocators.DLG_SAVE_BUTTON;

public class WorkMultiChooserDialog extends WorkChooserDialog {


    public WorkMultiChooserDialog(ILocatorable loc, ILocatorable pop) {
        super(loc, pop);
    }

    public WorkMultiChooserDialog(String loc, String pop) {
        super(loc, pop);
    }

    public WorkMultiChooserDialog(DisplayTextBox e) {
        super(e);
    }

    @Override
    public void waitAfterMakingChoice() {
    }

    public void clickSave() {
        new Button(getPopup().getChildByXpath(DLG_SAVE_BUTTON)).click(false);
        getPopup().waitForUnvisible();
    }

    public void clickCancel() {
        new Button(getPopup().getChildByXpath(DLG_CANCEL_BUTTON)).click(false);
        getPopup().waitForUnvisible();
    }

} // class WorkMultiChooserDialog 
