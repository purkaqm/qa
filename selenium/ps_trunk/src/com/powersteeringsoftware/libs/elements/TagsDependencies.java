package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.powersteeringsoftware.libs.enums.elements_locators.TagsDependenciesLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.07.2010
 * Time: 13:48:01
 */
public class TagsDependencies extends Element {
    public static final String HELP_SEPARATOR = " > ";
    // this is 'As result of selecting: ' now:
    public static final String HELP_STRING_PATTERN = "^[A-Za-z\\s]+:\\s";

    public TagsDependencies(ILocatorable locator) {
        super(locator);
    }

    public TagsDependencies(String locator) {
        super(locator);
    }

    public TagsDependencies(Element e) {
        super(e);
    }

    public TagsDependencies(Document document, ILocatorable locator) {
        super(document, locator);
    }

    private List<Element> getRows() {
        setDefaultElement();
        List<Element> rows = new ArrayList<Element>();
        for (Element row : Element.searchElementsByXpath(this, ROWS)) {
            //if (row.exists()) {
            // debug:
            if (row.getDEAttribute(ROW_STYLE) != null && row.getDEAttribute(ROW_STYLE).toLowerCase().
                    contains(ROW_STYLE_UNVISIBLE.getLocator())) {
                continue;
            }
            rows.add(row);
            //} else {
            //    break;
            //}
        }
        /*
        for (int i = 1; ; i++) {
            String loc = getLocator() +
                    ROW.getLocator().
                            replace(LOCATOR_REPLACE_PATTERN, String.valueOf(i));
            Element row = new Element(loc);
            if (row.exists()) {
                // debug:
                if (row.getAttribute(ROW_STYLE) != null && row.getAttribute(ROW_STYLE).toLowerCase().
                        contains(ROW_STYLE_UNVISIBLE.getLocator())) {
                    continue;
                }
                rows.add(row);
            } else {
                break;
            }
        }
        */
        return rows;
    }

    public List<TagItemSelector> getItems() {
        Element loading = getChildByXpath(LOADING);
        loading.waitForDisapeared();
        List<TagItemSelector> res = new ArrayList<TagItemSelector>();
        int index = 1;
        for (Element row : getRows()) {
            row.getChildByXpath(LOADING).waitForDisapeared();
            Element select = row.getChildByXpath(SELECT);
            if (!select.exists()) continue;
            //if (!select.getParent().isVisible()) continue; // for ff
            TagItemSelector item = new TagItemSelector(select);
            item.parentRow = row;
            item.index = index++;
            Element help = row.getChildByXpath(HELPER);
            if (help.exists()) {
                item.help = help;
            }
            res.add(item);
        }
        return res;
    }

    public Set<TagChooser.Item> collectTree() {
        List<TagItemSelector> selects = getItems();
        return new LinkedHashSet<TagChooser.Item>(getListTagChooserItem(selects.get(0), null));
    }

    public List<String> getTree() {
        List<String> set2 = new ArrayList<String>();
        for (TagChooser.Item i : collectTree()) {
            set2.add(i.toString());
        }
        return set2;
    }

    private List<TagChooser.Item> getListTagChooserItem(TagItemSelector selector, TagChooser.Item parent) {
        List<TagChooser.Item> res = new ArrayList<TagChooser.Item>();
        List<String> options = selector.getSelectOptions();
        PSLogger.debug("Options: " + options);
        for (String option : options) {
            if (option.trim().isEmpty()) continue;
            TagChooser.Item item = new TagChooser.Item(option);
            if (parent != null)
                item.setParent(parent);
            res.add(item);
            PSLogger.info("Select input: " + selector);
            selector.select(option);
            List<TagItemSelector> items = getItems();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(selector)) {
                    for (int j = i + 1; j < items.size(); j++) {
                        List<TagChooser.Item> childs = getListTagChooserItem(items.get(j), item);
                        res.addAll(childs);
                    }
                    break;
                }
            }
        }
        return res;
    }

    public class TagItemSelector extends SelectInput {
        Element help;
        Element parentRow;
        public int index;

        TagItemSelector(Element e) {
            super(e);
            setId();
        }

        public String getToolTipInfo() {
            if (help == null) return "";
            ToolTip tt = new ToolTip(help.getLocator());
            tt.open();
            String res = tt.getLabel();
            tt.close();
            return res;
        }

        public void select(String option) {
            PSLogger.info("Select option " + option);
            super.select(option);
            new TimerWaiter(500).waitTime(); // for web-driver (2.0.0)
        }

        public String toString() {
            return String.valueOf(index) + " : " + getSelectedLabel() + ", " + getSelectOptions();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof TagItemSelector)) return false;
            if (getId() != null && !getId().isEmpty()) {
                return getId().equals(((TagItemSelector) o).getId());
            }
            return getSelectOptions().equals(((TagItemSelector) o).getSelectOptions());
        }
    }
}
