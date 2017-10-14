package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.util.StrUtil;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.TagsComponentLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 14.10.13
 * Time: 13:47
 * To change this template use File | Settings | File Templates.
 */
public class TagsComponent extends Element {
    private List<ITagChooser> selectors;
    private PSPage page;

    public TagsComponent(PSPage p) {
        page = p;
    }

    public List<ITagChooser> getAllSelectors() {
        if (selectors != null) return selectors;
        selectors = new ArrayList<ITagChooser>();
        for (Element e : page.getElements(SELECTOR)) {
            Element parent = e.getParent(SELECTOR_TR);
            Element label = parent.getChildByXpath(SELECTOR_LABEL);
            String sLabel = label.getDEText().replace(":", "");
            if (sLabel.isEmpty()) {
                label = label.getChildByXpath(SELECTOR_LABEL_2);
                sLabel = label.getDEText();
            }
            ITagChooser tg;
            if (e.getDEClass().contains(SELECTOR_COMBO.getLocator())) {
                //flat
                tg = new FlatTagChooser(e);
            } else {
                String[] config = e.getDEAttribute(SELECTOR_CONFIG).split(",");
                boolean isHierar = false;
                for (String conf : config) {
                    if (conf.contains(SELECTOR_CONFIG_HIERARCHICAL.getLocator())) {
                        isHierar = conf.contains(String.valueOf(Boolean.TRUE));
                        break;
                    }
                }
                if (isHierar) {
                    tg = new HierarchicalTagChooser(e);
                } else {
                    tg = new TagChooser(e);
                }
            }
            tg.setName(sLabel);
            selectors.add(tg);
        }
        return selectors;
    }

    public ITagChooser getSelector(PSTag tg) {
        tg = tg.getRoot();
        for (ITagChooser selector : getAllSelectors()) {
            if (tg.getName().equals(selector.getName())) return selector;
        }
        return null;
    }

    public void setTag(PSTag tg) {
        ITagChooser selector = getSelector(tg);
        Assert.assertNotNull(selector, "Can't find tag " + tg);
        if (tg.hasParent()) {
            setTag(tg, selector);
        } else {
            for (PSTag t : tg.getAllChildren()) {
                setTag(t, selector);
            }
        }
    }

    private static void setTag(PSTag tg, ITagChooser selector) {
        PSLogger.info("Set tag " + tg + "'");
        selector.setLabel(tg.getName());
    }

    public List<String> getSelectorsContent() {
        List<String> res = new ArrayList<String>();
        for (ITagChooser s : getAllSelectors()) {
            res.add(s.getContentString());
        }
        return res;
    }

    public String getSelectorContent(PSTag tg) {
        ITagChooser selector = getSelector(tg);
        if (selector == null) return null;
        return selector.getContentString();
    }

    public boolean isSelectorEmpty(PSTag tg) {
        ITagChooser selector = getSelector(tg);
        if (selector == null) return true;
        if (selector instanceof FlatTagChooser) {
            return StrUtil.trim(((FlatTagChooser) selector).getContent()).isEmpty();
        }
        return ((TagChooser) selector).getContent().isEmpty();
    }

}

