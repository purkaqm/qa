package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.DefaultPermissionsPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.page_locators.DefaultPermissionsPageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 11.09.12
 * Time: 17:09
 * To change this template use File | Settings | File Templates.
 */
public interface IPermissionsPage {

    IContent getContent();

    interface IContent {

        PSPage getPage();

        void selectCoreSetsPermission(String user, String level, String role);

        void createCustomSet(User user, String role);

        void saveCoreSet();

        void saveCustomSet();

        List<DPRadioButton> getCoreSets();

        List<DPRadioButton> getCustomSets();

        Content.DeleteCustomSetDialog pushCustomSetDeleteImg(String name);

        boolean hasCustomSet(String user);

    }

    abstract class Content extends Element implements IContent {
        public static final TimerWaiter WAITER = new TimerWaiter(500);
        protected PSPage page;
        protected List<DPRadioButton> customSetsRadioButtons;
        protected List<DPRadioButton> coreSetsRadioButtons;

        public PSPage getPage() {
            return page;
        }

        private Content(PSPage p) {
            super(DefaultPermissionsPageLocators.CONTENT);
            page = p;
        }

        void reset() {
            setDefaultElement();
            coreSetsRadioButtons = null;
            customSetsRadioButtons = null;
        }

        protected String getTextFromCell(Element userCell) {
            if (userCell == null) return null;
            Element innerUserCell_1 = Element.searchElementByXpath(userCell, CELL_LINK_1);
            Element innerUserCell_2 = Element.searchElementByXpath(userCell, CELL_LINK_2);
            if (innerUserCell_1 != null) {
                userCell = innerUserCell_1;
            } else if (innerUserCell_2 != null)
                userCell = innerUserCell_2;
            return StrUtil.trim(userCell.getDEText());
        }

        abstract List<DPRadioButton> loadCoreSets(Document doc);

        abstract List<DPRadioButton> loadCustomSets(Document doc);

        private void setAllRadiobuttons(Document doc) {
            customSetsRadioButtons = new ArrayList<DPRadioButton>();
            coreSetsRadioButtons = new ArrayList<DPRadioButton>();
            PSLogger.info("Get all radiobuttons for 'Core Sets'");
            coreSetsRadioButtons.addAll(loadCoreSets(doc));
            PSLogger.info("Get all radiobuttons for 'Custome Sets'");
            customSetsRadioButtons.addAll(loadCustomSets(doc));
        }

        public List<DPRadioButton> getCoreSets() {
            if (coreSetsRadioButtons == null)
                setAllRadiobuttons(page.getDocument(false));
            return coreSetsRadioButtons;
        }

        public List<DPRadioButton> getCustomSets() {
            if (customSetsRadioButtons == null)
                setAllRadiobuttons(page.getDocument(false));
            return customSetsRadioButtons;
        }

        public boolean hasCustomSet(User user) {
            return hasCustomSet(user.getFormatFullName());
        }

        public boolean hasCustomSet(String user) {
            for (DPRadioButton d : getCustomSets()) {
                if (d.user.equals(user)) return true;
            }
            return false;
        }

        private static RadioButton getRadiobutton(String user, String level, String role, List<DPRadioButton> list) {
            if (list == null) {
                PSLogger.warn("open page to determine radio buttons");
                return null;
            }
            for (DPRadioButton drb : list) {
                if (drb.user.equalsIgnoreCase(user) && drb.role.equals(role.toLowerCase())) {
                    if (level == null || level.equalsIgnoreCase(drb.level))
                        return drb;
                }
            }
            Assert.fail("Can't find radio button for user/role " + user + (level != null ? "(" + level + ")" : "") + " and category " + role);
            return null;
        }

        public RadioButton getCoreSetRadiobutton(String user, String role) {
            return getRadiobutton(user, null, role, getCoreSets());
        }

        public RadioButton getCustomSetRadiobutton(String user, String role) {
            return getRadiobutton(user, null, role, getCustomSets());
        }

        public RadioButton getCoreSetRadiobutton(String user, String level, String role) {
            return getRadiobutton(user, level, role, getCoreSets());
        }

        private static void selectPermission(boolean on_off, RadioButton rb) {
            if (on_off && rb.getChecked()) {
                PSLogger.debug("Radiobutton " + rb + " is already checked");
                return;
            }
            if (!on_off && !rb.getChecked()) {
                PSLogger.debug("Radiobutton " + rb + " is already unchecked");
                return;
            }
            rb.click();
        }

        public void selectCustomSetsPermission(String user, String role, boolean on_off) {
            selectPermission(on_off, getCustomSetRadiobutton(user, role));
        }


        public void selectCoreSetsPermission(String user, String role) {
            selectPermission(true, getCoreSetRadiobutton(user, role));
        }

        public void selectCoreSetsPermission(String user, String level, String role) {
            selectPermission(true, getCoreSetRadiobutton(user, level, role));
        }

        private void save(String name, ILocatorable save, ILocatorable table) {
            PSLogger.info("Save '" + name + "'");
            PSLogger.save("before saving " + name);
            Button bt = new Button(Element.searchElementByXpath(this, save));
            bt.click(true);
            new Element(table).waitForPresent();
            reset();
            PSLogger.save("after saving " + name);
        }

        public void saveCustomSet() {
            save("Custom Sets", CUSTOM_SET_SAVE_BUTTON, CUSTOM_SET_TABLE);
        }

        public void saveCoreSet() {
            save("Core Sets", CORE_SET_SAVE_BUTTON, CORE_SET_TABLE);
        }

        public void setDefaultElement() {
            setDefaultElement(page.getDocument());
        }

        public Element getCustomSetRow(String name) {
            for (DPRadioButton d : getCustomSets()) {
                if (d.user.equals(name)) {
                    return d.userRow;
                }
            }
            return null;
        }

        public Element getCoreSetRow(String name) {
            for (DPRadioButton d : getCoreSets()) {
                if (d.user.equals(name)) {
                    return d.userRow;
                }
            }
            return null;
        }

        private Img getDeleteImg(Element row) {
            Element delete = row.getChildByXpath(CUSTOM_SET_DELETE_IMG);
            if (!delete.isDEPresent()) {
                delete.waitForVisible(30000);
                WAITER.waitTime(); // <- chrome12 ff36 2.0b3 debug.
                setDefaultElement();
                delete.setDefaultElement(getPage().getDocument(false));
                if (!delete.isDEPresent()) return null;
            }
            return new Img(delete);
        }

        public DeleteCustomSetDialog pushCustomSetDeleteImg(String name) {
            PSLogger.info("Delete custom set '" + name + "'");
            Element row = getCustomSetRow(name);
            Assert.assertNotNull(row, "Can't find custom set for " + name);
            Img img = getDeleteImg(row);
            Assert.assertNotNull(img, "Can't find delete img for " + name + " in custom set table");
            String id = img.getParent().getDEId().replace(CUSTOM_SET_DELETE_IMG_LINK_ID.getLocator(), "");
            img.click(false);
            DeleteCustomSetDialog dialog;
            if (id.isEmpty())
                dialog = new DeleteCustomSetDialog(this);
            else
                dialog = new DeleteCustomSetDialog(this, id); // deleting after save
            dialog.waitForVisible();
            return dialog;
        }

        public void addCustomSetUser(String fullFormattedName) {
            PSLogger.info("Add User " + fullFormattedName + " to Custom Set");
            addCustomSetUser(fullFormattedName, fullFormattedName);
        }

        abstract Button getCustomSetAddNew();

        abstract Button getCoreSetAddNew();

        public void addCustomSetUser(String nameToSearch, String nameToSet) {
            getCustomSetAddNew().click(false);
            UsersDialog dialog = new UsersDialog();
            dialog.setPopup(CUSTOM_SET_USERS_POPUP);
            dialog.getPopup().waitForVisible();
            dialog.search(nameToSearch);
            dialog.set(nameToSet);
            dialog.submit();
            reset();
        }

        public void createCustomSet(String user, String role) {
            if (hasCustomSet(user)) {
                PSLogger.info("User " + user + " already has custom sets");
            } else {
                addCustomSetUser(user);
            }
            selectCustomSetsPermission(user, role, true);
        }

        public void createCustomSet(User user, String role) {
            PSLogger.info("Create Custom Set for user " + user + " and role " + role);
            createCustomSet(user.getFormatFullName(), role);
        }

        protected AddNewDialog pushAddNew() {
            PSLogger.info("Push Add New");
            Button b = getCoreSetAddNew();

            b.waitForVisible(); //wait?
            b.click(false);
            AddNewDialog dialog = new AddNewDialog(this);
            dialog.waitForVisible();
            return dialog;
        }

        protected AddNewDialog pushLinkToEdit(String name) {
            Element row = getCoreSetRow(name);
            Assert.assertNotNull(row, "Can't find core set '" + name + "'");
            Element eLink = null;
            try {
                row.getChildByXpath(LINK).waitForDEPresent(5000, getPage());
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.warn(row.asXML());
            }
            for (Element e : Element.searchElementsByXpath(row, LINK)) {
                String txt = e.getDEText();
                if (name.equals(txt)) {
                    eLink = e;
                    break;
                }
            }
            Assert.assertNotNull(eLink, "Can't find link for '" + name + "'");
            Link link = new Link(eLink);
            link.click(false);
            AddNewDialog dialog = new AddNewDialog(this);
            dialog.waitForVisible();
            return dialog;
        }

        protected DeleteCoreSetDialog pushCoreSetDeleteImg(String name) {
            PSLogger.info("Delete core set '" + name + "'");
            Element row = getCoreSetRow(name);
            Assert.assertNotNull(row, "Can't find core set '" + name + "'");
            Img img = getDeleteImg(row);
            Assert.assertNotNull(img, "Can't find delete img for " + name + " in core set table");
            img.click(false);
            DeleteCoreSetDialog dialog = new DeleteCoreSetDialog();
            dialog.waitForVisible();
            return dialog;
        }

        public class AddNewDialog extends Dialog {

            private AddNewDialog(Content con) {
                super(ADD_ROLE_POPUP);
            }

            public void save() {
                PSLogger.save("Save in Add New Dialog");
                WAITER.waitTime();
                Button b = new Button(getChildByXpath(ADD_ROLE_POPUP_SAVE_BUTTON));
                b.waitForVisible(); // why?
                b.click(true);
                WAITER.waitTime();
                reset();
            }

            public void setSequence(String txt) {
                new Input(ADD_ROLE_POPUP_SEQUENCE).type(txt);
            }

            public String getSequence() {
                return new Input(ADD_ROLE_POPUP_SEQUENCE).getValue();
            }

            public void setName(String txt) {
                new Input(ADD_ROLE_POPUP_NAME).type(txt);
            }

            public String getName() {
                return new Input(ADD_ROLE_POPUP_NAME).getValue();
            }

            public void setPluralName(String txt) {
                new Input(ADD_ROLE_POPUP_PLURAL_NAME).type(txt);
            }

            public String getPluralName() {
                return new Input(ADD_ROLE_POPUP_PLURAL_NAME).getValue();
            }

            public void setDescription(String txt) {
                getTextArea().setText(txt);
            }

            private TextArea getTextArea() {
                TextArea res = new TextArea(getChildByXpath(ADD_ROLE_POPUP_DESCRIPTION));
                res.setDefaultElement(page.getDocument());
                return res;
            }

            public String getDescription() {
                return new TextArea(ADD_ROLE_POPUP_DESCRIPTION).getText();
            }

            public void cancel() {
                PSLogger.save("Cancel in dialog");
                Button b = new Button(ADD_ROLE_POPUP_CALCEL_BUTTON);
                b.waitForVisible();
                b.click(false);
                waitForUnvisible();
                WAITER.waitTime();
                reset();
            }

            public TagChooser getWorksSelector() {
                return new DialogTagChooser(ADD_ROLE_POPUP_WORKS);
            }
        }

        public class DeleteCoreSetDialog extends Dialog {
            private DeleteCoreSetDialog() {
                super(DELETE_CORE_SET_ROLE_POPUP);
            }

            public void ok() {
                new Button(getChildByXpath(DELETE_CORE_SET_ROLE_POPUP_OK_BUTTON)).click(true);
                WAITER.waitTime();
                reset();
            }
        }

        public class DeleteCustomSetDialog extends Dialog {
            private boolean afterSave;

            private DeleteCustomSetDialog(Content con) {
                super(DELETE_CUSTOM_SET_ROLE_POPUP);
            }

            private DeleteCustomSetDialog(Content con, String id) {
                super(DELETE_CUSTOM_SET_ROLE_POPUP_AFTER_SAVE.replace(id));
                afterSave = true;
            }

            public void ok() {
                Button bt = new Button(getChildByXpath(afterSave ? DELETE_CUSTOM_SET_ROLE_POPUP_AFTER_SAVE_OK_BUTTON : DELETE_CUSTOM_SET_ROLE_POPUP_OK_BUTTON));
                bt.click(afterSave);
                if (!afterSave)
                    waitForUnvisible();
                reset();
            }

            public void waitForVisible() {
                super.waitForVisible();
                Content.this.setDefaultElement();
                setDefaultElement(page.getDocument(false));
            }
        }
    }

    class Content93 extends Content {

        Content93(PSPage p) {
            super(p);
        }

        private List<DPRadioButton> loadRadioButtons(Document doc, ILocatorable tableLoc) {
            Element table = new Element(tableLoc);
            if (!table.exists()) {
                PSLogger.warn("Can't find table " + tableLoc);
                PSLogger.warn(doc.asXML());
            }
            table.setDefaultElement(doc);
            List<DPRadioButton> res = new ArrayList<DPRadioButton>();
            final List<String> header = new ArrayList<String>();
            for (Element e : Element.searchElementsByXpath(table, TABLE_HEADER)) {
                String prop = StrUtil.trim(e.getDEText());
                if (prop.isEmpty()) {
                    Element center = Element.searchElementByXpath(e, TABLE_HEADER_CENTER);
                    prop = StrUtil.replaceSpaces(center.getDEText()).trim();
                    if (prop.isEmpty()) continue;
                }
                header.add(prop);
            }
            PSLogger.info("header (categories): " + header);
            final int nameColumnIndex = header.indexOf(TABLE_HEADER_NAME.getLocator());
            final int levelColumnIndex = header.indexOf(TABLE_HEADER_LEVEL.getLocator());
            final boolean hasLevel = levelColumnIndex != -1;

            class Row {
                private Map<String, Element> radios;
                private String user;
                private String level;
                private Element userRow;

                private Row(List<Element>... columns) {
                    List<Element> cells = new ArrayList<Element>();
                    radios = new HashMap<String, Element>();
                    for (List<Element> c : columns) {
                        if (userRow == null) {
                            for (Element e : c) {
                                userRow = e.getParent(SET_TABLE_ROW);
                                break;
                            }
                        }
                        cells.addAll(c);
                    }
                    while (cells.size() < header.size()) {
                        cells.add(0, null);
                    }
                    Element userCell = cells.get(nameColumnIndex);

                    user = getTextFromCell(userCell);
                    level = hasLevel ? getTextFromCell(cells.get(levelColumnIndex)) : null;
                    for (int i = Math.max(nameColumnIndex, levelColumnIndex) + 1; i < cells.size(); i++) {
                        Element radio = Element.searchElementByXpath(cells.get(i), CELL_INPUT);
                        radios.put(header.get(i).toLowerCase(), radio);
                    }
                }

                public String toString() {
                    return (hasLevel ? (user + "," + level) : user) + "[" + radios.size() + "]";
                }

                public List<DPRadioButton> getRadios() {
                    List<DPRadioButton> res = new ArrayList<DPRadioButton>();
                    for (String key : radios.keySet()) {
                        res.add(new DPRadioButton(userRow, radios.get(key), level, user, key));
                    }
                    return res;
                }

            }
            List<Row> rows = new ArrayList<Row>();

            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3)) { // has two tables instead one
                List<Element> innerTables = Element.searchElementsByXpath(table, TABLE_INNER_TABLE_93);
                List<Element> nameRows = Element.searchElementsByXpath(innerTables.get(0), TABLE_ROWS);
                List<Element> radioRows = Element.searchElementsByXpath(innerTables.get(1), TABLE_ROWS);
                Assert.assertEquals(nameRows.size(), radioRows.size(), "Different number of rows in two tables");
                for (int j = 1; j < nameRows.size(); j++) { // skipp header
                    List<Element> cols1 = Element.searchElementsByXpath(nameRows.get(j), TABLE_COLUMNS);
                    List<Element> cols2 = Element.searchElementsByXpath(radioRows.get(j), TABLE_COLUMNS);
                    rows.add(new Row(cols1, cols2));
                }
            } else {
                List<Element> _rows = Element.searchElementsByXpath(table, TABLE_ROWS);
                for (int j = 1; j < _rows.size(); j++) { // skipp header
                    rows.add(new Row(Element.searchElementsByXpath(_rows.get(j), TABLE_COLUMNS)));
                }
            }

            PSLogger.debug("Rows: " + rows);
            for (int i = 0; i < rows.size(); i++) {
                Row row = rows.get(i);
                if (row.user == null && hasLevel && i != 0) {
                    row.user = rows.get(i - 1).user;
                }
                res.addAll(row.getRadios());
            }
            return res;
        }

        @Override
        List<DPRadioButton> loadCoreSets(Document doc) {
            return loadRadioButtons(doc, CORE_SET_TABLE);
        }

        @Override
        List<DPRadioButton> loadCustomSets(Document doc) {
            return loadRadioButtons(doc, CUSTOM_SET_TABLE);
        }

        Button getCustomSetAddNew() {
            return new Button(ADD_NEW_CUSTOM_SET);
        }

        protected Button getCoreSetAddNew() {
            return new Button(ADD_NEW_BUTTON);
        }

    }

    class Content94 extends Content {

        Content94(PSPage p) {
            super(p);
        }

        List<DPRadioButton> loadCoreSets(Document doc) {
            return loadRadioButtons(doc, CORE_SET_TABLE_94);
        }

        List<DPRadioButton> loadCustomSets(Document doc) {
            return loadRadioButtons(doc, CUSTOM_SET_TABLE_94);
        }

        protected List<DPRadioButton> loadRadioButtons(Document doc, DefaultPermissionsPageLocators tableLoc) {
            Element leftTable = Element.searchElementByXpath(doc, tableLoc.replace(SET_TABLE_LEFT_94));
            Element rightTable = Element.searchElementByXpath(doc, tableLoc.replace(SET_TABLE_RIGHT_94));
            class User {
                String user;
                String level;
                Element userRow;
            }
            List<User> users = new ArrayList<User>();
            // get users:
            boolean hasLevel = false;
            for (Element tr : Element.searchElementsByXpath(leftTable, TABLE_ROWS)) {
                List<Element> cells = Element.searchElementsByXpath(tr, TABLE_COLUMNS);
                if (cells.isEmpty()) continue;
                if (cells.get(0).getChildByXpath(IMG).isDEPresent()) {
                    cells.remove(0);
                }
                if (cells.isEmpty()) continue;
                String lastTxt = getTextFromCell(cells.get(cells.size() - 1));
                if (lastTxt.isEmpty()) continue;
                User u = new User();

                if (cells.size() == 1) {
                    if (hasLevel) {
                        u.user = users.get(users.size() - 1).user;
                        u.userRow = users.get(users.size() - 1).userRow;
                        u.level = lastTxt;
                    } else {
                        u.user = lastTxt;
                        u.userRow = tr;
                    }
                } else {
                    String firstTxt = getTextFromCell(cells.get(cells.size() - 2));
                    if (!firstTxt.isEmpty()) {
                        u.user = firstTxt;
                        u.level = lastTxt;
                        u.userRow = tr;
                        hasLevel = true;
                    } else {
                        continue;
                    }
                }
                users.add(u);
            }
            //load header:
            List<String> header = new ArrayList<String>();
            for (Element e : Element.searchElementsByXpath(rightTable, HEADER_94)) {
                String txt = e.getDEText();
                if (txt.isEmpty()) continue;
                header.add(txt);
            }
            List<Element> rows = Element.searchElementsByXpath(rightTable, TABLE_ROWS);
            //load radios:
            List<DPRadioButton> res = new ArrayList<DPRadioButton>();
            for (int i = 1; i <= users.size(); i++) {
                Element row = rows.get(rows.size() - i);
                User u = users.get(users.size() - i);
                List<Element> inputs = Element.searchElementsByXpath(row, INPUT_94);
                for (int j = 0; j < header.size(); j++) {
                    String h = header.get(j);
                    Element in = inputs.get(j);
                    res.add(new DPRadioButton(u.userRow, in, u.level, u.user, h.toLowerCase()));
                }
            }
            return res;
        }

        protected Button getCustomSetAddNew() {
            return new Button(ADD_NEW_CUSTOM_SET_94);
        }

        protected Button getCoreSetAddNew() {
            // its for chrome web-driver
            VerticalScrollBar scroll = getCoreSetScroll();
            scroll.doScroll(0);

            return new Button(ADD_NEW_BUTTON_94);
        }

        public VerticalScrollBar getCoreSetScroll() {
            return new VerticalScrollBar(getPage().getElement(false, CORE_SET_RIGHT_DIV_94));
        }

    }


    public static class DPRadioButton extends RadioButton {
        private String level;
        private String user;
        private String role;
        private Element userRow;

        private DPRadioButton(Element userRow, Element e, String level, String user, String header) {
            super(e);
            this.level = level;
            this.user = user;
            this.role = header;
            Assert.assertNotNull(user);
            Assert.assertNotNull(header);
            setName(user + (level != null ? "(" + level + ")" : "") + "," + header);
            this.userRow = userRow;
        }

        public String getCategory() {
            return role;
        }

        public String getUser() {
            return user;
        }

        public String getLevel() {
            return level;
        }
    }

}
