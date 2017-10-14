package com.powersteeringsoftware.libs.pages;

import java.util.Iterator;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.CheckBox;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Frame;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.RadioButton;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.elements.TextArea;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate;
import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate.PSFieldCheckboxes;
import com.powersteeringsoftware.libs.objects.CustomFieldsTemplate.PSFieldYesNoButton;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import static com.powersteeringsoftware.libs.enums.page_locators.CustomFields81PageLocators.*;

public class CustomFieldsPage81 extends CustomFieldsPage {

    public void create(CustomFieldsTemplate _template) {

        setTemplate(_template);

        selectFrameChild();

        pushAddNew();

        typeName(template.getName());

        typeDescription(template.getDescription());

        checkObjects();

        submitCreation();

        addYesNoList();

        doSMTH();

        selectFrameParent();
    }

    private void doSMTH() {
        if (template.getFieldsCheckboxesList().size() > 0) {
            for (Iterator<PSFieldCheckboxes> it = template.getFieldsCheckboxesList().iterator(); it.hasNext(); ) {
                PSFieldCheckboxes checkboxes = it.next();

                PSLogger.info("Push Add Button on edit page screen");
                new Button(ADD_CUSTOM_DATA_FIELD_BUTTON_LINK).click(true);

                PSLogger.info("Adding checkboxes '" + checkboxes.getName() + "' to the template '" + template.getName() + "'");

                new Input(CUSTOM_DATA_FIELD_NAME_INPUT_FIELD.getLocator()).type(checkboxes.getName());

                new RadioButton(CUSTOM_DATA_FIELD_REQUERED_RADIOBUTTON_NO).click();
                new RadioButton(CUSTOM_DATA_FIELD_ON_REPORT_RADIOBUTTON_NO).click();
                new TextArea(CUSTOM_DATA_FIELD_DESCRIPTION_TEXTAREA).
                        setText(checkboxes.getDescription());

                new SelectInput(CUSTOM_DATA_FIELD_DATA_TYPE_SELECT).
                        select(CUSTOM_DATA_FIELD_DATA_TYPE_SELECT_VALUE_PREFIX.getLocator() + "check");
                waitForPageToLoad();

                String[] checkboxesNames = checkboxes.getCheckboxesNames();
                String checkboxesInputString = "";
                for (int i = 0; i < checkboxesNames.length; i++) {
                    checkboxesInputString += checkboxesNames[i];
                    if (i < checkboxesNames.length - 1) checkboxesInputString += "\n";
                }

                new TextArea(CUSTOM_DATA_FIELD_CHECKBOXES_DESCRIPTION_TEXTAREA).
                        setText(checkboxesInputString);
                new Button(CUSTOM_DATA_FIELD_SUBMIT_LINK_BUTTON).click(true);
            }
        }
    }

    private void addYesNoList() {
        if (template.getFieldsYesNoButtonsList().size() > 0) {
            for (Iterator<PSFieldYesNoButton> it = template.getFieldsYesNoButtonsList().iterator(); it.hasNext(); ) {
                PSFieldYesNoButton yesNoButton = it.next();

                PSLogger.info("Push Add Button on edit page screen");
                new Button(ADD_CUSTOM_DATA_FIELD_BUTTON_LINK).click(true);

                new Input(CUSTOM_DATA_FIELD_NAME_INPUT_FIELD).type(yesNoButton.getName());
                new RadioButton(CUSTOM_DATA_FIELD_REQUERED_RADIOBUTTON_NO).click();
                new RadioButton(CUSTOM_DATA_FIELD_ON_REPORT_RADIOBUTTON_NO).click();
                new TextArea(CUSTOM_DATA_FIELD_DESCRIPTION_TEXTAREA).
                        setText(yesNoButton.getDescription());

                new SelectInput(CUSTOM_DATA_FIELD_DATA_TYPE_SELECT).
                        select(CUSTOM_DATA_FIELD_DATA_TYPE_SELECT_VALUE_PREFIX.getLocator() + "radio");
                waitForPageToLoad();

                new Button(CUSTOM_DATA_FIELD_SUBMIT_LINK_BUTTON).click(true);
            }
        }
    }

    public void deleteAll() {
        int allowedIterations = 10;
        for (int i = 0; i < allowedIterations; i++) {
            Element delete;
            if ((delete = new Element(DELETE_BUTTON_LINK_LOCATOR)).exists()) {
                PSLogger.info("Delete some template");
                delete.click(false);
                PSLogger.info("confirmation=" + SeleniumDriverFactory.getDriver().getConfirmation());
                PSLogger.info("next confirmation=" + SeleniumDriverFactory.getDriver().getConfirmation());
                SeleniumDriverFactory.getDriver().waitForFrameToLoad(
                        CUSTOM_FIELDS_PAGE_ADMIN_AREA_FRAME_LOCATOR.getLocator(),
                        CoreProperties.getWaitForElementToLoadAsString());
            } else {
                break;
            }
        }
    }

    public void submitCreation() {
        PSLogger.info("Submit");
        new Button(SUBMIT_BUTTON_LINK).click(true);
    }

    public void checkObjects() {
        PSLogger.info("Checkboxes");
        if (template.getAssocWithEvents())
            new CheckBox(EVENTS_CHECKBOX).check();
        if (template.getAssocWithMSPProjects())
            new CheckBox(MSPCONTAINER_CHECKBOX).check();
        if (template.getAssocWithMilestones())
            new CheckBox(MILESTONE_CHECKBOX).check();
        if (template.getAssocWithTemplates())
            new CheckBox(TEMPLATE_CHECKBOX).check();
        if (template.getAssocWithFolders())
            new CheckBox(FILE_FOLDER_CHECKBOX).check();
        if (template.getAssocWithWorkItems())
            new CheckBox(WORK_CHECKBOX).check();
        if (template.getAssocWithUnexpWorkItems())
            new CheckBox(UNEXPANDED_WORK_CHECKBOX).check();
        if (template.getAssocWithGatedProjects())
            new CheckBox(TOLLGATE_CHECKBOX).check();
        if (template.getAssocWithGates())
            new CheckBox(CHECKPOINT_CHECKBOX).check();
        if (template.getAssocWithUsers())
            new CheckBox(USER_CHECKBOX).check();
        if (template.getAssocWithGroups())
            new CheckBox(GROUP_CHECKBOX).check();
        if (template.getAssocWithStatusReport())
            new CheckBox(STATUS_REPORT_HANDLER_CHECKBOX).check();
    }

    public void typeDescription(String desc) {
        if (null != desc) {
            PSLogger.info("Set description " + desc);
            new TextArea(DESCRIPTION_TEXTAREA).setText(desc);
        }
    }

    public void typeName(String name) {
        PSLogger.info("Set name " + name);
        new Input(NAME_INPUT_FIELD).type(name);
    }

    public void pushAddNew() {
        PSLogger.info("Push Add button");
        new Button(ADD_BUTTON_LINK).click(false);
        SeleniumDriverFactory.getDriver().
                waitForFrameToLoad(CUSTOM_FIELDS_PAGE_ADMIN_AREA_FRAME_LOCATOR.getLocator(),
                        CoreProperties.getWaitForElementToLoadAsString());
    }

    private void selectFrameChild() {
        new Frame(CUSTOM_FIELDS_PAGE_ADMIN_AREA_FRAME_LOCATOR).select();
    }


    private void selectFrameParent() {
        new Frame(CUSTOM_FIELDS_PAGE_ADMIN_AREA_FRAME_LOCATOR).selectUp();
    }
}
