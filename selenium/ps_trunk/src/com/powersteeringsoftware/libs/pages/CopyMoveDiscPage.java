package com.powersteeringsoftware.libs.pages;


import org.testng.Assert;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.WorkChooserDialog;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.CopyMoveDiscPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.11
 * Time: 17:02
 */
public class CopyMoveDiscPage extends AbstractWorkPage {
    @Override
    public void open() {
        // todo do nothing
    }

    /**
     * todo: its copy-paste from CreateWorkPage
     *
     * @param parent
     * @param howToSet - if true use browse, otherwise use search
     */
    public void setParent(String parent, boolean howToSet) {
        WorkChooserDialog dialog = new WorkChooserDialog(LOCATION_FIELD, LOCATION_POPUP);
        dialog.open();
        if (howToSet) {
            // set using browse
            dialog.openBrowseTab();
            dialog.chooseWorkOnBrowseTab(parent);
        } else {
            //set using searching
            dialog.openSearchTab();
            dialog.getSearchTab().choose(parent);
        }
        Assert.assertFalse(dialog.getPopup().isVisible(),
                "popup still visible after searching parent location");
    }

    public DiscussionsPage move() {
        PSLogger.info("Submit moving");
        PSLogger.save("before submit");
        new Button(MOVE_BUTTON).submit();
        PSLogger.save("after submit");
        return new DiscussionsPage();
    }

}
