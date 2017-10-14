package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate;
import com.powersteeringsoftware.libs.pages.CustomFieldsPage;
import com.powersteeringsoftware.libs.pages.CustomFieldsPage81;
import com.powersteeringsoftware.libs.pages.CustomFieldsPage82;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.libs.util.session.TestSession;

/**
 * Test creating custom fields
 */
@Deprecated // todo: move test data to config, create manager, remove class
public class CreateCustomField extends PSTest {
    public CreateCustomField() {
        name = "Create Custom Field";
    }

    public void run() {
        CustomFieldsTemplate cFT = new CustomFieldsTemplate("General custom fields");
        cFT.setAssocWithTemplates(true);
        cFT.setAssocWithWorkItems(true);
        cFT.setAssocWithUnexpWorkItems(true);
        cFT.setAssocWithGatedProjects(true);
        cFT.addYesNoButtonField("Sample Yes-No button", "Just a sample yes-no radio button created by an automated script");
        String[] checkboxes = {"Red", "Green", "Blue"};
        cFT.addCheckboxesField("Sample Checkboxes", "Just sample checkboxes created by an automated script", checkboxes);

        CustomFieldsPage page = getPage();
        page.open();
        page.deleteAll();
        page.create(cFT);
    }

    private CustomFieldsPage getPage() {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
            return new CustomFieldsPage82();
        }
        if (TestSession.getAppVersion().verEqualsTo(PowerSteeringVersions._8_1)) {
            return new CustomFieldsPage81();
        }
        return null;
    }

}
