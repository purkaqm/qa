package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.WorkChooserDialog;
import com.powersteeringsoftware.libs.objects.works.Work;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.MoveWorkPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 17.04.13
 * Time: 21:26
 * To change this template use File | Settings | File Templates.
 */
public class MoveWorkPage extends AbstractWorkPage {


    public void setLocation(Work parent, boolean howToSet) {
        WorkChooserDialog dialog = new WorkChooserDialog(LOCATION_FIELD, LOCATION_POPUP);
        dialog.open();
        if (howToSet) {
            // set using browse
            dialog.openBrowseTab();
            dialog.chooseWorkOnBrowseTab(parent);
        } else {
            //set using searching
            dialog.openSearchTab();
            dialog.chooseWorkOnSearchTab(parent);
        }
        Assert.assertFalse(dialog.getPopup().isVisible(),
                "popup still visible after searching parent location");
    }

    public void setLocation(Work parent) {
        setLocation(parent, true);
    }

    public SummaryWorkPage move() {
        SummaryWorkPage res = getSummaryInstance();
        new Button(MOVE, res).submit();
        return res;
    }
}
