package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.HierarchicalTagChooserLocators.*;
import static com.powersteeringsoftware.libs.enums.elements_locators.TagChooserLocators.POPUP_LABELS;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.07.2010
 * Time: 16:46:41
 */
public class HierarchicalTagChooser extends TagChooser {
    private boolean isReallyHierarchical = true;

    public HierarchicalTagChooser(ILocatorable locator) {
        super(locator);
    }

    public HierarchicalTagChooser(String locator) {
        super(locator);
    }

    public HierarchicalTagChooser(Element e) {
        super(e);
    }

    public HierarchicalTagChooser(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void openAll() {
        PSLogger.info("Expand TagChooser");
        waitForTree();
        Element plus;
        while ((plus = getPopup().getChildByXpath(POPUP_LABEL_OPEN_BRANCH_ELEMENT)).isDEPresent()) {
            scrollTo(plus);
            plus.click(false);
            waitForTree();
            getPopup().setDefaultElement();
        }
        searchForLabels();
        PSLogger.save();
    }

    public void closeAll() {
        PSLogger.info("Collapse TagChooser");
        waitForTree();
        getPopup().setDefaultElement();
        for (Element minus : Element.searchElementsByXpath(getPopup(), POPUP_LABEL_CLOSE_BRANCH_ELEMENT)) {
            if (minus.isVisible()) {
                minus.click(false);
                waitForTree();
            }
        }
        getPopup().setDynamic();
        searchForLabels();
        PSLogger.save();
    }

    public void openBranch(String item) {
        PSLogger.info("Open branch for " + item);
        Element openOrClose = getOpenOrCloseBranchElement(item);
        String clazz = openOrClose.getElementClass();
        if (clazz == null) {
            PSLogger.warn("Can't find class for element to open " + item);
            return;
        }
        if (clazz.contains(POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_OPENED.getLocator())) {
            PSLogger.info("Branch for item " + item + " already opened");
            return;
        }
        if (clazz.contains(POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT_CLOSED.getLocator())) {
            openOrClose.click(false);
            waitForTree();
            getPopup().defaultElement = null;
            searchForLabels();
            return;
        }
        PSLogger.warn("Can't find open or close element for item " + item);
        PSLogger.save();
    }

    public void waitForTree() {
        getPopup().getChildByXpath(POPUP_LABEL_OPEN_CLOSE_BRANCH_LOADING).
                waitForUnvisible();
    }

    public Element getOpenOrCloseBranchElement(String forItem) {
        Element eItem = getLabel(forItem);
        Element res = null;
        if (eItem != null) {
            Element parent = eItem.getParent(POPUP_LABEL_PARENT_NAME, POPUP_LABEL_PARENT_CLASS);
            res = parent.getChildByXpath(POPUP_LABEL_OPEN_CLOSE_BRANCH_ELEMENT);
            res.setSimpleLocator();
        }
        return res;
    }

    protected void searchForLabels() {
        super.searchForLabels();
        for (int i = 0; i < items.size(); i++) {
            Item _this = items.get(i);
            String value = _this.get().getDEAttribute(POPUP_LABEL_VALUE);
            Element container = Element.searchElementByXpath(getPopup(), POPUP_LABEL_CONTAINER.replace(value));
            if (container == null) {
                continue;
            }
            List<String> chs = new ArrayList<String>();
            for (Element e : Element.searchElementsByXpath(container, POPUP_LABELS)) {
                chs.add(e.getParent().getDEText());
            }
            boolean hidden = !container.isVisible();
            for (int j = i + 1; j < items.size(); j++) {
                if (chs.contains(items.get(j).getName())) {
                    items.get(j).setParent(_this);
                    items.get(j).isHidden = hidden;
                }
            }
        }
    }

    public List<String> getTree() {
        List<String> set2 = new ArrayList<String>();
        for (Item i : items) {
            if (!i.isHidden)
                set2.add(i.toString());
        }
        return set2;
    }

    public List<String> collectFullTree() {
        if (items.size() != 0) {
            DijitPopup popup = DijitPopup.getParent(items.get(0).get());
            List<Element> list = Element.searchElementsByXpath(popup, POPUP_LABEL_OPEN_BRANCH_ELEMENT);
            list.addAll(Element.searchElementsByXpath(popup, POPUP_LABEL_CLOSE_BRANCH_ELEMENT));
            PSLogger.debug("open elements : " + list.size());
            isReallyHierarchical = list.size() != 0;
        }
        openAll();
        searchForLabels();
        return getTree();
    }

    public void setNoneLabels() {
        if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1) && isSingleSelector()) {
            PSLogger.info("Push 'Clear'");
            openPopup();
            Link link = new Link(getPopup().getChildByXpath(POPUP_CLEAR));
            if (!link.isVisible()) {
                PSLogger.warn("Link for clearing none is unvisible");
                return;
            }
            link.click(false);
            waitForPopupUnVisible();
        } else {
            super.setNoneLabels();
        }
    }

    public String toString() {
        return name == null ? super.toString() : (name + "[" + HierarchicalTagChooser.class.getSimpleName() + "]");
    }

    public void select(String... labels) {
        openAll();
        super.select(labels);
    }


}
