package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.WorkMultiChooserDialog;
import com.powersteeringsoftware.libs.objects.works.Work;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.EditPortfolioPageLocators.*;

/**
 * Created by admin on 09.05.2014.
 */
public class EditPortfolioPage extends PSPage {
    @Override
    public void open() {
        //do not nothing
    }

    public void setName(String name) {
        new Input(NAME).type(name);
    }

    public void setDescendedFrom(Work dscFrom) {
        setDescendedFrom(dscFrom, false);
    }

    public void setDescendedFrom(Work dscFrom, boolean howToSet) {
        WorkMultiChooserDialog dialog = new WorkMultiChooserDialog(DESCENDED_FROM, DESCENDED_FROM_POPUP);
        dialog.open();
        if (howToSet) {
            // set using browse
            dialog.openBrowseTab();
            dialog.chooseWorkOnBrowseTab(dscFrom);
        } else {
            //set using searching
            dialog.openSearchTab();
            dialog.setWithWaitingForReindexSearching(dscFrom.getName());
        }
        dialog.clickSave();
        Assert.assertFalse(dialog.getPopup().isVisible(),
                "popup still visible after searching 'descended from' location");
    }

    public void clearDescendedFrom() {
        WorkMultiChooserDialog dialog = new WorkMultiChooserDialog(DESCENDED_FROM, DESCENDED_FROM_POPUP);
        dialog.open();
        dialog.clear();
    }

    public PortfolioDetailsPage submit() {
        new Button(SUBMIT).submit();
        return new PortfolioDetailsPage();
    }
}
