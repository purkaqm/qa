package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.page_locators.TagsSetEPageLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.TagsSetEPageLocators.AddTagSetDialogLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 19.07.2010
 * Time: 17:00:28
 */
public class TagSetsPage extends PSPage {
    private Map<String, Element> rows;

    public void open() {
        if (!checkUrl()) {
            PSLogger.info("Open Tag Sets Page");
            clickAdminConfigurationsTags();
            Assert.assertTrue(checkUrl(), "Can't open " + getClass().getSimpleName() + " page");
        } else {
            PSLogger.info("Tag Sets page already open");
        }
        setTagRows();
    }

    public boolean checkUrl() {
        return checkUrl(URL) || checkUrl(URL2);
    }

    private Map<String, Element> getTagRows() {
        if (rows == null) setTagRows();
        return rows;
    }

    private void setTagRows() {
        Element table = new Element(getDocument(), TABLE);
        List<Element> elements = Element.searchElementsByXpath(table, TABLE_ROW);
        rows = new HashMap<String, Element>();
        for (Element row : elements) {
            Element link = row.getChildByXpath(TABLE_ROW_NAME_LINK);
            if (link.isDEPresent()) {
                String text = link.getDEText();
                if (!CoreProperties.getBrowser().isWebDriver() && !row.isSimpleLocator())
                    row.setSimpleLocator();
                rows.put(text, row);
            }
        }
        PSLogger.info("All tags: " + rows.keySet());
    }

    public TagsetPage openTag(String name) {
        getTagRows().get(name).getChildByXpath(TABLE_ROW_NAME_LINK).click(true);
        return new TagsetPage();
    }

    private Map<String, Element> getTagRowsByPrefix(String... prefixes) {
        Map<String, Element> rows = getTagRows();
        Map<String, Element> res = new HashMap<String, Element>();
        for (String name : rows.keySet()) {
            for (String prefix : prefixes) {
                if (name.startsWith(prefix)) {
                    res.put(name, rows.get(name));
                }
            }
        }
        return res;
    }

    public List<String> getTagsByPrefix(String... prefixes) {
        List<String> res = new ArrayList<String>();
        for (String name : getTags()) {
            for (String prefix : prefixes) {
                if (name.startsWith(prefix)) {
                    res.add(name);
                }
            }
        }
        return res;
    }

    public List<String> getTags() {
        return getTags(null);
    }

    public List<String> getTags(String type) {
        List<String> res = new ArrayList<String>();
        if (type == null) {
            res.addAll(getTagRows().keySet());
        } else {
            for (String tag : getTagRows().keySet()) {
                Element row = getTagRows().get(tag);
                for (Element img : Element.searchElementsByXpath(row, TABLE_ROW_IMG)) {
                    if (img.getDEAttribute(TABLE_ROW_IMG_TITLE).equals(type)) {
                        res.add(tag);
                        break;
                    }
                }
            }
        }
        return res;
    }

    public List<String> getTagTypes(String tag) {
        List<String> tags = getTags(null);
        Assert.assertTrue(tags.contains(tag), "Can't find tag " + tag);
        Element row = getTagRows().get(tag);
        List<String> res = new ArrayList<String>();
        for (Element img : Element.searchElementsByXpath(row, TABLE_ROW_IMG)) {
            res.add(img.getDEAttribute(TABLE_ROW_IMG_TITLE));
        }
        return res;
    }


    public void deleteTag(String name) {
        PSLogger.info("Delete tag " + name);
        Element row = getTagRows().get(name);
        Assert.assertNotNull("Can not find tag " + name);
        deleteTag(row);
        waitForPageToLoad();
    }

    public void waitForPageToLoad(boolean doCheckPage, long timeout) {
        super.waitForPageToLoad(doCheckPage, timeout);
        setTagRows();
    }

    private static void deleteTag(Element row) {
        Element img = row.getChildByXpath(TABLE_ROW_DELETE_IMG);
        if (!img.isDEPresent()) {
            PSLogger.warn("Can't find delete image icon");
            return;
        }
        img.click(false);
        Element popup = new Element(DELETE_POPUP_DIALOG);
        popup.waitForVisible();
        Element delete = popup.getChildByXpath(DELETE_POPUP_DIALOG_YES);
        delete.waitForVisible();
        delete.click(false);
        //popup.waitForUnvisible();
    }

    public TagSetDialog pushAddNewTag() {
        new Button(BUTTON_ADD_NEW_TAG).clickAndWaitForDialog(ADD_TAG_SET_DIALOG);
        return new TagSetDialog(ADD_TAG_SET_DIALOG, false, this);
    }

    public static class TagSetDialog extends Dialog {
        private boolean edit;
        private PSPage page;

        private TagSetDialog(ILocatorable locator, boolean isEdit, PSPage p) {
            super(locator);
            edit = isEdit;
            page = p;
        }

        public TagSetDialog(ILocatorable locator, boolean isEdit) {
            this(locator, isEdit, null);
        }

        public TagsetPage submit() {
            PSLogger.save("before submit");
            new TimerWaiter(500).waitTime();
            new Button(getChildByXpath(edit ? BUTTON_EDIT_SUBMIT : BUTTON_ADD_SUBMIT)).click(true);
            return new TagsetPage();
        }

        public void setName(String tagName) {
            new Input(FIELD_NAME).type(tagName);
        }

        public void setDescription(String tagDescription) {
            new TextArea(FIELD_DESCRIPTION).setText(tagDescription);
        }

        public void setHierarchical(boolean isHierarhical) {
            new CheckBox(FIELD_IS_HIERARCHICAL).select(isHierarhical);
        }

        public void setApplyUsersPermissions(boolean isApplyUsersPermissions) {
            new CheckBox(FIELD_IS_APPLY_USERS_PERMISSIONS).select(isApplyUsersPermissions);
        }

        public void setAlertsAndEventLogging(boolean isEnableAlertsAndEventLogging) {
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._8_2)) {
                // not supported for 82
                return;
            }
            new CheckBox(FIELD_IS_ENABLE_ALERTS_AND_EVENT_LOGGING).select(isEnableAlertsAndEventLogging);
        }


        public void setLocked(boolean isLocked) {
            new CheckBox(FIELD_IS_LOCKED).select(isLocked);
        }


        public void setMultiple(boolean isMultiple) {
            new CheckBox(FIELD_IS_MULTIPLE).select(isMultiple);
        }

        public void setRequired(boolean isRequired) {
            new CheckBox(FIELD_IS_MANDANATORY).select(isRequired);
        }

/*        private void focusOnNewTagOptionElement(ILocatorable loc, boolean doThrow) {
            new TimerWaiter(500).waitTime();
            Element input = new Element(loc);
            if (CoreProperties.getBrowser().isWebDriver()) {
                input.click(false);
            } else {
                input.focus();
                input.mouseDownAndUp();
            }
            try {
                new Element(DIJIT_POPUP).waitForVisible(30000);
            } catch (Wait.WaitTimedOutException ex) {
                // todo: investigate
                if (doThrow) throw ex;
                PSLogger.warn(ex);
                focusOnNewTagOptionElement(loc, true);
            }
        }*/

        private void setAssociateWith(AddTagSetDialogLocators loc, String type) {
            List<String> types = new ArrayList<String>();
            types.add(type);
            setAssociateWith(loc, types);
        }

        private void setAssociateWith(AddTagSetDialogLocators loc, List<String> types) {
            if (null == types || types.isEmpty()) return;
            TagChooser tc = new DialogTagChooser(loc) {
                public void done() {
                    super.done();
                    TimerWaiter.waitTime(500); // ?
                    TagSetDialog.this.setDefaultElement();
                }
            };
            tc.setPage(page);
            tc.openPopup();
            PSLogger.debug("All items : " + tc.getAllLabels());
            tc.selectUnselect(false);
            tc.select(types.toArray(new String[types.size()]));
            tc.done();
            List<String> content = tc.getContent();
            Collections.sort(content);
            Collections.sort(types);
            Assert.assertEquals(content, types, "Incorrect types after 'Done'");
        }

        public void setMessageTypes(List<String> messageTypes) {
            PSLogger.info("Set messageTypes=" + messageTypes);
            setAssociateWith(FIELD_MESSAGES, messageTypes);
        }


        public void setDocumentTypes(List<String> documentTypes) {
            PSLogger.info("Set documentTypes=" + documentTypes);
            setAssociateWith(FIELD_DOCUMENTS, documentTypes);
        }

        public void setPeopleTypes(List<String> peopleTypes) {
            PSLogger.info("Set peopleTypes=" + peopleTypes);
            setAssociateWith(FIELD_PEOPLES, peopleTypes);
        }

        public void setPeopleTypes(String type) {
            setAssociateWith(FIELD_PEOPLES, type);
        }

        public void setWorkTypes(List<String> workTypes) {
            PSLogger.info("Set workTypes=" + workTypes);
            setAssociateWith(FIELD_WORKS, workTypes);
        }
    }

}
