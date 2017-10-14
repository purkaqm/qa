package com.powersteeringsoftware.libs.util.bugtrackers;


import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 02.11.11
 * Time: 13:25
 * TODO: This class will be used for collecting JIRAs knises
 */
public class Jira extends BugTracker {

    protected Jira(List<KnownIssue> knises) {
        super(knises);
    }

    public void doLogin(String login, String pwd) {
        PSLogger.info("Do log in to jira");
        Frame frame = new Frame("gadget-0");
        frame.select();
        new Input("username").type(login);
        new Input("password").type(pwd);
        new Button("login").submit();
    }

    public void doLogin() {
        doLogin(getUrl(), getContext());
    }

    public void login() {
        init();
        doLogin();
    }

    @Override
    public boolean isJira() {
        return true;
    }

    @Override
    public String getUrl() {
        return CoreProperties.getJiraUrl();
    }

    @Override
    public String getContext() {
        return CoreProperties.getJiraContext();
    }

    @Override
    public String getLogin() {
        return CoreProperties.getJiraUser();
    }

    @Override
    public String getPassword() {
        return CoreProperties.getJiraPassword();
    }


    public void administration() {
        PSLogger.info("Go to Administration");
        new Link("id=admin_link").clickAndWaitNextPage();
        confirmAdministration();
    }

    public void confirmAdministration() {
        Input in = new Input("id=login-form-authenticatePassword");
        if (in.exists() && in.isVisible()) {
            in.type(CoreProperties.getJiraPassword());
            new Button("id=authenticateButton").submit();
        }
    }

    public void fields() {
        PSLogger.info("Go to Custom Fields");
        new Img("id=admin_issues_menu_drop").click(false);
        Link link = new Link("//a[@id='fields_section']");
        link.waitForVisible();
        link.clickAndWaitNextPage();
        confirmAdministration();
    }

    public enum CustomFieldType {
        MULTI_CHECKBOXES("com.atlassian.jira.plugin.system.customfieldtypes:multicheckboxes_id"),
        MULTI_SELECT("com.atlassian.jira.plugin.system.customfieldtypes:multiselect_id"),
        MULTI_CASCADING_SELECT("com.sourcesense.jira.plugin.cascadingselect:multi-level-cascading-select_id"),
        SINGLE_SELECT("com.atlassian.jira.plugin.system.customfieldtypes:select_id");
        private String value;

        CustomFieldType(String s) {
            value = s;
        }
    }

    public void addCustomField(CustomFieldType type, String name, String description) {
        PSLogger.info("Add custom field " + name);
        new Link("id=add_custom_fields").clickAndWaitNextPage();
        new RadioButton(type.value).click();
        new Button("//input[@id='nextButton']").submit();
        new Input("//input[@name='fieldName']").type(name);
        new TextArea("//textarea[@name='description']").setText(description);
        new Button("//input[@id='nextButton']").submit();
        selectAllScreens();
    }

    public void configureCustomField(String name) {
        Element row = null;
        for (Element e : Element.searchElementsByXpath("//tr/td/strong")) {
            if (e.getDEText().equals(name)) {
                row = e.getParent().getParent();
            }
        }
        new Link(row.getChildByXpath("//a[text()='Configure']")).clickAndWaitNextPage();
    }

    public void addSimpleOptions(List<String> values) {
        new Link("//a[text()='Edit Options']").clickAndWaitNextPage();
        for (String val : values) {
            new Input("//input[@name='addValue']").type(val);
            new Button("//input[@id='add_submit']").submit();
        }
        new Button("//input[@value='Done']").submit();
    }

    public void selectAllScreens() {
        for (Element e : Element.searchElementsByXpath("//input[@name='associatedScreens']")) {
            new CheckBox(e).click();
        }
        new Button("//input[@id='update_submit']").submit();
    }

    @Override
    public void openIssue(KnownIssue kn) {
        String _url = kn.getUrl();
        PSLogger.info("Open issue " + _url);
        SeleniumDriverFactory.getDriver().open(_url);
        Element title = new Element("id=summary-val");
        Element status = new Element("//span[@id='status-val']/span");
        kn.setSubject(title.getText());
        if (status.exists() && status.getText().trim().equalsIgnoreCase("Closed")) {
            kn.setIsClosed(true);
        }
    }

}
