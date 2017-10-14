package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.DefinePermissionsPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.DefinePermissionsPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.07.2010
 * Time: 13:10:56
 */
public class DefinePermissionsPage extends PSPage implements IGeneralPermissionsPage {
    private static final TimerWaiter CLICK_TIMEOUT = new TimerWaiter(500);

    private static final int SCROLL_DIFF = 50;
    private static Integer containerTop;

    private List<PermCheckBox> checkboxes;

    public class PermCheckBox extends CheckBox {
        private String name;
        private String category;

        private PermCheckBox(int i, int j, String name, String category) {
            super(CHECKBOX.replace(i, j));
            for (IRow r : DefinePermissionsPageLocators.getPermissions()) {
                if (r.getLocator().equalsIgnoreCase(name)) {
                    this.name = r.name();
                    break;
                }
            }
            if (this.name == null)
                this.name = name;
            this.category = category;
            setName(name + "," + category);
        }

        public void click() {
            super.click();
            CLICK_TIMEOUT.waitTime(); // todo: is it needed?
        }
/*
        public boolean isChecked() {
            String attr = getDEAttribute(CHECKBOX_CHECKED_ATTR);
            return attr != null && !attr.isEmpty() && attr.equalsIgnoreCase(CHECKBOX_CHECKED_VAL.getLocator());
        }
*/

        public String getName() {
            return name;
        }

        public String getCategory() {
            return category;
        }
    }

    private PermCheckBox getPermission(String permission, String role) {
        if (checkboxes == null) {
            PSLogger.warn("open page or select some category");
            return null;
        }
        for (PermCheckBox pch : checkboxes) {
            if (pch.name.equalsIgnoreCase(permission) && pch.category.equalsIgnoreCase(role)) {
                return pch;
            }
        }
        return null;
    }

    public void open() {
        clickAdminDefineCategories();
        checkboxes = null;
    }

    public void selectCategory(PSPermissions.BasicTarget option) {
        CommonActions.selectCategory(option, this);
        checkboxes = null;
    }

    public PSPermissions.BasicTarget getCategory() {
        return CommonActions.getCategory();
    }

    private void setAllCheckBoxes(Document doc) {
        checkboxes = new ArrayList<PermCheckBox>();
        List<String> props = new ArrayList<String>();
        for (Element e : Element.searchElementsByXpath(doc, PROPERTIES)) {
            String prop = StrUtil.replaceSpaces(e.getDEText()).trim();
            if (prop.isEmpty()) continue;
            props.add(prop);
        }
        PSLogger.debug("All properties: " + props);
        List<String> roles = new ArrayList<String>();
        for (Element e : Element.searchElementsByXpath(doc, ROLES)) {
            Element link = e.getChildByXpath(ROLE_LINK);
            String role = StrUtil.replaceSpaces((link.isDEPresent() ? link : e).getDEText()).trim();
            if (role.isEmpty()) continue;
            roles.add(role);
        }
        PSLogger.debug("All roles: " + roles);
        for (int i = 0; i < props.size(); i++) {
            for (int j = 0; j < roles.size(); j++) {
                PermCheckBox ch = new PermCheckBox(i + 2, j + 1, props.get(i).toLowerCase(), roles.get(j).toLowerCase());
                ch.setDefaultElement(doc);
                checkboxes.add(ch);
            }
        }
    }

    public List<PermCheckBox> getAll() {
        if (checkboxes == null)
            setAllCheckBoxes(getDocument());
        return checkboxes;
    }

    private List<CheckBox> getPermissions(String name, boolean perm) {
        List<CheckBox> res = new ArrayList<CheckBox>();
        for (CheckBox ch : getAll()) {
            PermCheckBox pch = (PermCheckBox) ch;
            if (perm) {
                if (pch.name.equalsIgnoreCase(name)) res.add(pch);
            } else {
                if (pch.category.equalsIgnoreCase(name)) res.add(pch);
            }
        }
        return res;
    }

    public void selectPermission(String permission, String role, boolean yes, boolean disabled) {
        PermCheckBox c = getPermission(permission, role);
        Assert.assertNotNull(c, "Can't find checkbox for " + permission + " and " + role);
        Assert.assertEquals(c.isEditable(), !disabled, "Checkbox " + c.name + " for category role " + c.category + " should be " + (disabled ? "disabled" : "enabled"));
        if (disabled) return;
        PSLogger.info((yes ? "Select" : "Deselect") + " value " + c.name + " for category role " + c.category);
        c.select(yes);
        // scrolling for web-driver: checkbox is not visible. not for 9.3: there is not scrollbar
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) &&
                getDriver().getType().isWebDriver() &&
                (yes ^ c.getChecked())) {
            Integer[] checkbox = c.getCoordinates();
            int y = checkbox[1] - getContainerTop() - SCROLL_DIFF;
            if (y < 0) y = 0;
            PSLogger.info("Try Scroll to " + c.name + "," + c.category);
            getVerticalScrollBar94().doScroll(y);
            c.select(yes);
        }
    }


    private int getContainerTop() {
        if (containerTop != null) return containerTop;
        Element e = new Element(CONTAINER);
        if (!e.exists()) return -1;
        return containerTop = e.getCoordinates()[1];
    }

    public VerticalScrollBar getVerticalScrollBar94() {
        return new VerticalScrollBar(VERTICAL_SCROLL_BAR_94);
    }

    public List<CheckBox> getPermissionsForRole(String name) {
        List<CheckBox> res = getPermissions(name, false);
        if (res.size() == 0) {
            PSLogger.warn("No row column for role '" + name + "'");
        }
        return res;
    }

    public List<CheckBox> getPermissionsForProperty(IRow name) {
        return getPermissionsForProperty(name.getLocator());
    }

    public List<CheckBox> getPermissionsForProperty(String name) {
        List<CheckBox> res = getPermissions(name, true);
        if (res.size() == 0) {
            PSLogger.warn("No row found for property '" + name + "'");
        }
        return res;
    }

    public CheckBox getPermission(IRow row, String column) {
        return getPermission(row.getLocator(), column);
    }

    public void update() {
        PSLogger.save("before updating");
        new Button(UPDATE).submit();
        PSLogger.save("after updating");
        setAllCheckBoxes(getDocument());
    }

    public AddNewCategoryDialog pushAddNew() {
        new Button(ADD_NEW_CATEGORY).click(false);
        AddNewCategoryDialog dialog = new AddNewCategoryDialog();
        dialog.waitForVisible();
        Assert.assertEquals(dialog.getTitle(), ADD_NEW_CATEGORY_TITLE.getLocator(), "Incorrect title for dialog");
        return dialog;
    }

    private Link getNewCategoryLink(String name) {
        return new Link(NEW_CATEGORY_LINK.replace(name));
    }

    public boolean isNewCategoryPresent(String name) {
        return getNewCategoryLink(name).exists();
    }

    public DeleteCategoryDialog pushDeleteCategory(String name) {
        getNewCategoryLink(name).waitForVisible();
        new TimerWaiter(5000).waitTime();
        Link link = null;
        for (Element e : getElements(true, DIV_LINK)) {
            if (e.getDEText().trim().equals(name)) {
                link = new Link(e);
                break;
            }
        }
        Assert.assertNotNull(link, "Can't find link : " + getElements(false, DIV_LINK));
        Element img = link.getParent().getChildByXpath(NEW_CATEGORY_DELETE_IMG);
        Assert.assertTrue(img.isDEPresent(), "Can't find img for " + name);
        String id = img.getParent().getDEAttribute("id").replace(NEW_CATEGORY_LINK_ID.getLocator(), "");
        Assert.assertFalse(id.isEmpty(), "Can't find id for role " + name);
        img.click(false);
        DeleteCategoryDialog dialog = new DeleteCategoryDialog(id);
        dialog.waitForVisible();
        dialog.setDefaultElement(getDocument());
        Assert.assertEquals(dialog.getTitle(), DELETE_CATEGORY_POPUP_TITLE.getLocator(), "Incorrect title for delete dialog");
        return dialog;
    }

    public class DeleteCategoryDialog extends Dialog {

        private DeleteCategoryDialog(String id) {
            super(DELETE_CATEGORY_POPUP.replace(id));
            setPopup(getLocator());
        }

        public void submit() {
            new Button(getChildByXpath(DELETE_CATEGORY_POPUP_OK)).click(false);
            new TimerWaiter(500).waitTime();
            waitForPageToLoad();
            checkboxes = null;
        }

        public void cancel() {
            new Button(getChildByXpath(DELETE_CATEGORY_POPUP_CANCEL)).click(false);
            waitForUnvisible();
            checkboxes = null;
        }
    }

    public class AddNewCategoryDialog extends Dialog {
        public AddNewCategoryDialog() {
            super(ADD_NEW_CATEGORY_POPUP);
            setPopup(ADD_NEW_CATEGORY_POPUP);
        }

        public void submit() {
            PSLogger.save("Before submiting");
            Button submit = new Button(getChildByXpath(ADD_NEW_CATEGORY_POPUP_SUBMIT));
            //submit.waitForVisible();
            submit.click(false);
            new TimerWaiter(500).waitTime();
            waitForPageToLoad();
            Assert.assertFalse(getPopup().isVisible(), "Dialog still present");
            checkboxes = null;
        }

        public void cancel() {
            PSLogger.save("Before cancel");
            Button cancel = new Button(getChildByXpath(ADD_NEW_CATEGORY_POPUP_CANCEL));
            //cancel.waitForVisible();
            cancel.click(false);
            waitForUnvisible();
            checkboxes = null;
        }

        public void setSequence(String txt) {
            new Input(ADD_NEW_CATEGORY_POPUP_SEQUENCE).type(txt);
        }

        public void setName(String txt) {
            new Input(ADD_NEW_CATEGORY_POPUP_NAME).type(txt);
        }


        public void setDescription(String txt) {
            new TextArea(ADD_NEW_CATEGORY_POPUP_DESCRIPTION).setText(txt);
        }

        private void setObjectType(String label) {
            new SelectInput(ADD_NEW_CATEGORY_POPUP_OBJ_TYPE).select(label);
        }

        public void setObjectType(PSPermissions.BasicTarget option) {
            setObjectType(SelectorOptions.get(option).getLocator());
        }

        public PSPermissions.BasicTarget getObjectType() {
            String label = new SelectInput(ADD_NEW_CATEGORY_POPUP_OBJ_TYPE).getSelectedLabel();
            return DefinePermissionsPageLocators.SelectorOptions.getByLabel(label);
        }

    }
}
