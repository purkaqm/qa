package com.powersteeringsoftware.libs.tests.actions;


import com.powersteeringsoftware.libs.elements.HierarchicalTagChooser;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.pages.TagEditPage;
import com.powersteeringsoftware.libs.pages.TagSetsPage;
import com.powersteeringsoftware.libs.pages.TagsetPage;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TagManager {

    public static TagsetPage openTag(PSTag tag) {
        TagsetPage res;
        if (tag.getId() != null) {
            res = new TagsetPage();
            res.open(tag.getId());
            tag.setCreated();
            return res;
        }
        TagSetsPage tagSetsPage = new TagSetsPage();
        tagSetsPage.open();
        res = tagSetsPage.openTag(tag.getName());
        tag.setId(res.getUrlId());
        tag.setCreated();
        return res;
    }

    public static List<PSTag> getAllTags() {
        List<PSTag> tags = TestSession.getTagList();
        TagSetsPage tagSetsPage = new TagSetsPage();
        tagSetsPage.open();
        List<PSTag> res = new ArrayList<PSTag>();
        for (String name : tagSetsPage.getTags()) {
            PSTag tg = new PSTag(name);
            int i = tags.indexOf(tg);
            if (i != -1) {
                tg = tags.get(i);
            }
            tg.setCreated();
            res.add(tg);
            for (String type : tagSetsPage.getTagTypes(name)) {
                for (PSTag.Associate t : PSTag.Associate.values()) {
                    if (t.getSingle().equals(type)) {
                        tg.setAssociation(t);
                        break;
                    }
                }
            }
        }
        return res;
    }

    public static List<PSTag> getTags(PSTag.Associate type, String prefix) {
        List<PSTag> res = new ArrayList<PSTag>();
        for (PSTag tg : getAllTags()) {
            if (tg.getAssociations().contains(type) && (prefix == null || tg.getName().startsWith(prefix))) {
                res.add(tg);
            }
        }
        return res;
    }


    public static List<PSTag> getUsersTags() {
        return getTags(PSTag.Associate.PEOPLES_USERS, null);
    }

    public static TagsetPage addTag(PSTag tag) {
        PSLogger.info("Add tag " + tag.getName());
        TagSetsPage tagSetsPage = new TagSetsPage();
        tagSetsPage.open();
        TagSetsPage.TagSetDialog dialog = tagSetsPage.pushAddNewTag();
        dialog.setName(tag.getName());
        if (tag.getDescription() != null) {
            dialog.setDescription(tag.getDescription());
        }
        dialog.setMessageTypes(tag.getAssociations(PSTag.Associate.MESSAGES));
        dialog.setDocumentTypes(tag.getAssociations(PSTag.Associate.DOCUMENTS));
        dialog.setPeopleTypes(tag.getAssociations(PSTag.Associate.PEOPLES));
        dialog.setWorkTypes(tag.getAssociations(PSTag.Associate.WORKS));

        dialog.setHierarchical(tag.isHierarchical());
        dialog.setApplyUsersPermissions(tag.isApplyUsersPermissions());
        dialog.setAlertsAndEventLogging(tag.isAlertsAndEventLogging());
        dialog.setLocked(tag.isLocked());
        dialog.setMultiple(tag.isMultiple());
        dialog.setRequired(tag.isRequired());

        TagsetPage tagSetPage = dialog.submit();
        tag.setId(tagSetPage.getUrlId());
        TagEditPage editPage = tagSetPage.update();

        List<PSTag> tags = tag.getAllChildren();
        PSLogger.info("Add values: " + tags);
        List<Input> inputs = editPage.getInputs();

        if (tags.size() > inputs.size()) {
            editPage = editPage.addMore();
            inputs = editPage.getInputs();
        }
        PSLogger.info("Fill inputs");
        for (int i = 0; i < tags.size(); i++) {
            inputs.get(i).type(tags.get(i).getName());
        }
        TagsetPage set = editPage.updateValues();
        if (!tag.isHierarchical()) {
            return set;
        }
        editPage = set.update();
        PSLogger.info("Set parents");
        for (int i = 0; i < tags.size(); i++) {
            PSTag parent = tags.get(i).getParent();
            String name = tags.get(i).getName();
            if (parent == null || parent.equals(tag)) continue;
            PSLogger.info("Set parent " + parent + " for " + name);
            HierarchicalTagChooser tc = editPage.getParentByName(name);
            tc.setLabel(parent.getName());
            Assert.assertEquals(tc.getContent().get(0), parent.getName(),
                    "Incorrect parent tag for " + name + " after setting : " + tc.getContent());

        }
        set = editPage.updateValues();
        PSLogger.info("Tag '" + tag + "' is created");
        tag.setCreated();
        return set;
    }

    public static void deleteTags(String... prefixes) {
        List<PSTag> tags = TestSession.getTagList();
        PSLogger.info("Delete all tags with prefixes " + Arrays.toString(prefixes));
        TagSetsPage page = new TagSetsPage();
        page.open();
        for (String prefix : prefixes) {
            List<String> rows;
            while ((rows = page.getTagsByPrefix(prefix)).size() != 0) {
                PSLogger.info("Found tags: " + rows);
                String name = rows.get(0);
                page.deleteTag(name);
                int index = tags.indexOf(new PSTag(name));
                if (index != -1) tags.get(index).setDeleted();
            }
        }
    }

    public static void deleteTag(PSTag tg) {
        TagSetsPage page = new TagSetsPage();
        page.open();
        page.deleteTag(tg.getName());
        tg.setDeleted();
    }
}
