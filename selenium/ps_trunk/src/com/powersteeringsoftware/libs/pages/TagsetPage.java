package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.enums.page_locators.TagsEditEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.TagsetPageLocators.*;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.11
 * Time: 17:16
 */
public class TagsetPage extends PSPage {

    @Override
    public void open() {
        //TODO
        throw new IllegalMonitorStateException("TODO");
    }

    public void open(String id) {
        if (id == null) throw new NullPointerException("Tag id should be specified");
        super.open(makeUrl(URL, id));
    }

    public static final int TIMEOUT = 500;
    public TagEditPage update() {
        TimerWaiter.waitTime(TIMEOUT);
        Link link = null;
        for (Element e : getElements(true, LINK)) {
            Link l = new Link(e);
            if (l.getDEText().contains(UPDATE_LINK_NAME.getLocator()) && l.exists() && l.getHref().contains(TagsEditEPageLocators.URL.getLocator()) && l.isVisible()) {
                link = l;
                break;
            }
            PSLogger.debug("Link: " + l.asXML());
        }
        Assert.assertNotNull(link, "Can't find update link");
        link.click(false);
        return TagEditPage.getPage();
    }

    public TagSetsPage.TagSetDialog edit() {
        new Link(EDIT_LINK).click(false);
        TagSetsPage.TagSetDialog dialog = new TagSetsPage.TagSetDialog(EDIT_TAG_SET_DIALOG, true);
        dialog.waitForVisible();
        return dialog;
    }

    public TagSetsPage backToTagsList() {
        new Link(BACK_LINK).clickAndWaitNextPage();
        return new TagSetsPage();
    }

}
