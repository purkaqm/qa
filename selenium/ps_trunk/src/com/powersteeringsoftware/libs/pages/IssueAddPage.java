package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.page_locators.IssueAddPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 07.06.2010
 * Time: 12:09:54
 */
public class IssueAddPage extends DiscussionAddPage {

    public void selectTags(String tagName, String value) {
        PSLogger.info("Select " + value + " for tag " + tagName);
        String loc = ISSUE_TAG.replace(tagName);
        if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._8_2)) {
            TagChooser tc = new TagChooser(loc);
            tc.setLabel(value);
        } else {
            FlatTagChooser tc = new FlatTagChooser(loc);
            tc.setLabel(value);
        }
    }

    public void setPriority(int num) {
        PSLogger.info("Set priority to " + num);
        new SelectInput(ISSUE_PRIORITY).select(ISSUE_PRIORITY_OPTION.replace(num - 1));
    }

    public List<ITagChooser> getTagChoosers() {
        // todo:
        List<ITagChooser> res = new ArrayList<ITagChooser>();
        for (Element e : getElements(CUSTOM_FIELD_TAG_CHOOSER)) {
            Element row = e.getParent(CUSTOM_FIELD_ROW);
            Element label = row.getChildByXpath(CUSTOM_FIELD_LABEL_TOOLTIP);
            String sLabel = null;
            if (label.isDEPresent()) {
                sLabel = label.getDEText();
            } else {
                sLabel = row.getChildByXpath(CUSTOM_FIELD_LABEL).getDEInnerText().trim();
            }
            if (sLabel.isEmpty()) continue;
            ITagChooser tag;
            if (e.getDEClass().contains(CUSTOM_FIELD_FLAT_TAG_CHOOSER.getLocator())) {
                tag = new FlatTagChooser(e) {
                    public void setDefaultElement() {
                        super.setDefaultElement(getDocument());
                    }
                };
            } else {
                String conf = e.getDEAttribute(CUSTOM_FIELD_CONFIG);
                PSLogger.debug(conf);
                if (conf.contains(CUSTOM_FIELD_HIERAR.getLocator())) {
                    tag = new HierarchicalTagChooser(e) {
                        public void setDefaultElement() {
                            super.setDefaultElement(getDocument());
                        }
                    };
                } else {
                    tag = new TagChooser(e) {
                        public void setDefaultElement() {
                            super.setDefaultElement(getDocument());
                        }
                    };
                }
            }
            tag.setName(sLabel);
            res.add(tag);
        }
        return res;
    }

    public List<ITagChooser> collectCustomTagsInfo(Integer type) {
        Map<String, List<String>> res = new LinkedHashMap<String, List<String>>();
        List<ITagChooser> tags = getTagChoosers();
        PSLogger.info(tags);
        for (ITagChooser tag : tags) {
            if (type != null) {
                if (type == 0 && !(tag instanceof TagChooser)) continue;
                if (type == 1 && !(tag instanceof HierarchicalTagChooser)) continue;
                if (type == 2 && !(tag instanceof FlatTagChooser)) continue;
            }
            tag.openPopup();
            List<String> labels;
            if (tag instanceof HierarchicalTagChooser) {
                labels = ((HierarchicalTagChooser) tag).collectFullTree();
            } else {
                labels = tag.getAllLabels();
            }
            PSLogger.info(tag + ":" + labels);
            tag.closePopup();
            res.put(tag.toString(), labels);
        }
        PSLogger.debug(res);
        return tags;
    }

    public List<ITagChooser> collectCustomTagsInfo() {
        return collectCustomTagsInfo(null);
    }


}
