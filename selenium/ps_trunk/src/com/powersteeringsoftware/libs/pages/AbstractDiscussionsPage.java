package com.powersteeringsoftware.libs.pages;


import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DiscussionsPageLocators.SUBJECT_COLUMN_CELL_LINK;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.11
 * Time: 16:23
 */
public abstract class AbstractDiscussionsPage extends AbstractWorkPage {

    public List<String> getSubjects() {
        List<String> res = new ArrayList<String>();
        for (Element e : getIssueColumn(getDocument())) {
            res.add(e.getDEText());
        }
        return res;
    }

    public DiscussionIssueViewPage openDiscussion(String name) {
        Assert.assertTrue(getSubjects().contains(name), "Can't find issue/discussion " + name + " to open");
        for (Element e : getIssueColumn(document)) {
            if (name.equals(e.getDEText())) {
                Link link = new Link(e);
                link.clickAndWaitNextPage();
                return new DiscussionIssueViewPage();
            }
        }
        return null;
    }

    private List<Element> getIssueColumn(Document doc) {
        return Element.searchElementsByXpath(doc, SUBJECT_COLUMN_CELL_LINK);
    }

    public abstract DiscussionAddPage pushAddNew();
}
