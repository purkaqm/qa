package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.logger.PSLogger;

import static com.powersteeringsoftware.libs.enums.page_locators.DiscussionsPageLocators.NEW_DISCUSSION_BUTTON;

/**
 * Class for DiscussionsPage
 * User: szuev
 * Date: 21.05.2010
 * Time: 11:21:25
 * To change this template use File | Settings | File Templates.
 */
public class DiscussionsPage extends AbstractDiscussionsPage {

    @Override
    public void open() {
        //todo:
    }

    public DiscussionAddPage pushAddNew() {
        Button bt = new Button(NEW_DISCUSSION_BUTTON);
        bt.submit();
        DiscussionAddPage res = new DiscussionAddPage();
        if (isCheckBlankPage() && isBlankPage()) {
            PSLogger.error("Blank page!");
            res.refresh();
        }
        return res;
    }

}
