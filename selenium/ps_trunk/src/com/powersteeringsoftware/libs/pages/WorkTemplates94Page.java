package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.WorkTemplates94PageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 07.10.12
 * Time: 15:24
 * To change this template use File | Settings | File Templates.
 */
public class WorkTemplates94Page extends WorkTemplatesPage {

    WorkTemplates94Page() {
        if (TestSession.isVersionPresent() && TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_4)) {
            PSSkipException.skip(getClass().getSimpleName() + ": is only for >=9.4");
        }
    }

    @Override
    public SummaryWorkPage openSummaryForTemplate(String name, boolean doCheck) {
        Link link = getTemplateLink(name);
        Element next;
        while (link == null && (next = getElement(false, NEXT)).isDEPresent()) {
            next.click(true);
            link = getTemplateLink(name);
        }
        Assert.assertNotNull(link, "Can't find template " + name);
        SummaryWorkPage res = SummaryWorkPage.getInstance(doCheck);
        link.setResultPage(res);
        link.clickAndWaitNextPage();
        return res;
    }


    private Link getTemplateLink(String name) {
        for (Element e : getElements(NAME_COLUMN_LINKS)) {
            if (e.getDEText().equals(name)) {
                return new Link(e);
            }
        }
        return null;
    }

    void doCreateNew() {
        new Button(CREATE_NEW).submit();
        initJsErrorChecker();
    }

    public CreateWorkTemplatePage createNew() {
        doCreateNew();
        return new CreateWorkTemplatePage();
    }
}
