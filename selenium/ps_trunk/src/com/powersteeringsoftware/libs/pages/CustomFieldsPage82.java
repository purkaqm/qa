package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate;
import org.testng.Assert;

import java.util.Arrays;

import static com.powersteeringsoftware.libs.enums.page_locators.CustomFields82PageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.07.2010
 * Time: 13:13:13
 */
public class CustomFieldsPage82 extends CustomFieldsPage {

    public void create(CustomFieldsTemplate template) {
        setTemplate(template);
        //set checkboxes:
        for (CustomFieldsTemplate.PSFieldCheckboxes ch : template.getFieldsCheckboxesList()) {
            pushAddNew();
            typeName(ch.getName());
            typeDescription(ch.getDescription());
            new SelectInput(DIALOG_TYPE_SELECT).
                    select(DIALOG_TYPE_SELECT_CHECKBOXES_LABEL);
            TextArea ta = new TextArea(DAILOG_CHECKBOXES_DESCRIPTION);
            ta.waitForVisible();
            String chs = Arrays.toString(ch.getCheckboxesNames()).replace("[", "").replace("]", "").replaceAll(",\\s*", "\n");
            ta.setText(chs);
            checkObjects();
            submitCreation();
            Assert.assertTrue(isCFExists(ch.getName()), "Can't find CF '" + ch.getName() + "' after creation");
        }
        // set yes no buttons
        for (CustomFieldsTemplate.PSFieldYesNoButton b : template.getFieldsYesNoButtonsList()) {
            pushAddNew();
            typeName(b.getName());
            typeDescription(b.getDescription());
            new SelectInput(DIALOG_TYPE_SELECT).
                    select(DIALOG_TYPE_SELECT_BUTTONS_YES_NO_LABEL);
            checkObjects();
            submitCreation();
            Assert.assertTrue(isCFExists(b.getName()), "Can't find CF '" + b.getName() + "' after creation");
        }
    }

    public void checkObjects() {
        String[] toSet1 = template.associateStupidTemplateSettingsWithWorkAssocsTypes();
        String[] toSet2 = template.associateStupidTemplateSettingsWithOtherAssocsTypes();
        if (toSet1.length != 0) {
            PSLogger.info("Set work assocs: " + Arrays.toString(toSet1));
            TagChooser tc = new TagChooser(DIALOG_WORK_ACCOS_TAG_CHOOSER);
            tc.setLabel(toSet1);
        }
        if (toSet2.length != 0) {
            PSLogger.info("Set other assocs: " + Arrays.toString(toSet2));
            TagChooser tc2 = new TagChooser(DIALOG_OTHER_ACCOS_TAG_CHOOSER);
            tc2.setLabel(toSet2);
        }
    }

    public void deleteAll() {
        Element img = getElement(DELETE_IMG);
        if (img == null) return;
        PSLogger.info("Delete all templates");
        do {
            Element row = img.getParent("td").getParent("tr");
            String customFieldName = row.getChildByXpath(CUSTOM_FIELD_NAME_CELL).getDEText();
            PSLogger.info("Delete custom field '" + customFieldName + "'.");
            img.click(false);
            Element popup = new Element(DELETE_DIALOG);
            popup.waitForVisible();
            popup.getChildByXpath(DELETE_DIALOG_YES).click(false);
            waitForPageToLoad();
            img = getElement(DELETE_IMG);
        } while (img != null);
    }

    public void pushAddNew() {
        new Button(ADD_NEW_BUTTON).clickAndWaitForDialog(DIALOG);
    }

    public void typeDescription(String desc) {
        new TextArea(DESCRIPTION_TEXT_AREA).setText(desc);
    }

    public void typeName(String name) {
        new Input(NAME_INPUT).type(name);
    }

    public void submitCreation() {
        PSLogger.info("Submit");
        PSLogger.save("before submitting");
        Button submit = new Button(DIALOG_SUBMIT);
        submit.waitForVisible(5000);
        submit.click(false);
        waitForPageToLoad();
        getDocument();
    }

    public boolean isCFExists(String name) {
        return getElement(false, CUSTOM_FIELD_NAME.replace(name)) != null;
    }
}
