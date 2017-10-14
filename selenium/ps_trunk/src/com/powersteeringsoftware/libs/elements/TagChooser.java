package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.dom4j.tree.DefaultElement;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.elements_locators.TagChooserLocators.*;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._9_2;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._9_3;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 04.06.2010
 * Time: 19:57:39
 */
public class TagChooser extends Element implements ITagChooser {
    protected Element popup;
    private Element main;
    protected Input in;
    protected List<Item> items = new ArrayList<Item>();
    private Element openElement;
    private boolean isSingleChooser;
    private static final TimerWaiter TIMEOUT_FOR_LADING = new TimerWaiter(5000); //ms, it for ie RC
    private static final long TIMEOUT_FOR_FIRST_OPEN = 30000; //ms
    private static final long TIMEOUT_FOR_SECOND_OPEN = 50000; //ms
    private static final TimerWaiter TIMEOUT_FOR_CLOSE = new TimerWaiter(5000); //ms
    private static final int ITERATIONS_FOR_POPUP = 10; //iterations
    private Button done;
    protected Link none;
    private Button cancel;
    protected Link all;
    protected String name;
    private PSPage page;

    public TagChooser(ILocatorable locator) {
        super(locator);
        setOpenElement(getChildByXpath(OPEN_POPUP));
    }

    public TagChooser(String locator) {
        super(locator);
        setOpenElement(getChildByXpath(OPEN_POPUP));
    }

    public TagChooser(Element e) {
        super(e);
        setOpenElement(e);
    }

    public TagChooser(Document document, ILocatorable locator) {
        super(document, locator);
        setOpenElement(this);
    }

    private void setOpenElement(Element e) {
        openElement = e;
    }

    public Element getPopup() {
        if (popup == null) return popup = new DijitPopup();
        return popup;
    }

    public void openPopup() {
        try {
            _openPopup();
        } catch (Wait.WaitTimedOutException ww) {
            // TODO: investigate!
            if (getDriver().getType().isWebDriver()) {
                PSLogger.save("TagChooser.openPopup");
                PSSkipException.skip("TagChooser.openPopup: " + ww.getMessage());
            }
            throw ww;
        }
    }

    private void _openPopup() {
        PSLogger.debug("Open tag chooser popup");
        openElement.click(false);
        try {
            waitForPopup(TIMEOUT_FOR_FIRST_OPEN);
        } catch (Wait.WaitTimedOutException w) {
            // it is hotfix for ie web-driver (ie8, ie9):
            PSLogger.warn("first iteration : " + w.getMessage());
            Element table = openElement.getParent("table");
            Element tParent = table != null ? table.getParent() : null;
            if (tParent != null)
                tParent.click(false); // todo!!
            for (int i = 0; i < ITERATIONS_FOR_POPUP; i++) {
                PSLogger.debug("try to open again, iteration #" + (i + 1));
                if (page != null) page.clickBody();
                // debug for web-driver
                (i % 2 != 0 ? getParent() : openElement).focus();
                (i % 2 == 0 ? this : openElement).click(false);
                try {
                    waitForPopup(TIMEOUT_FOR_SECOND_OPEN / ITERATIONS_FOR_POPUP);
                    return;
                } catch (Wait.WaitTimedOutException w2) {
                    PSLogger.warn("iteration #" + (i + 1) + " : " + w2.getMessage());
                }
            }
            throw w;
        }
    }

    /**
     * todo: debug method.
     * its for ie9, chrome (web-driver)
     */
    public void focusOut() {
        Element toClick = openElement.getParent("div");
        if (toClick != null)
            toClick = toClick.getParent("table");
        if (toClick != null) {
            PSLogger.debug("focus out");
            toClick.getParent().mouseOut();
            toClick.getParent().focus();
            new TimerWaiter(200).waitTime();
        }
        PSPage.getEmptyInstance().getTopElement().mouseDownAndUp(); // hotfix
    }

    public void setDefaultElement(Document doc) {
        super.setDefaultElement(doc);
        if (openElement != null)
            openElement.setDefaultElement(doc);
    }

    public void waitForPopup() {
        waitForPopup(TIMEOUT_FOR_SECOND_OPEN);
    }

    public void waitForPopup(long timeout) {
        getPopup().waitForVisible(timeout);
        waitWhileLoading(this);
        if (getDriver().getType().isIE())
            TIMEOUT_FOR_LADING.waitTime();
        searchForLabels();
        if (items.size() != 0) return;
        if (!getDriver().getType().isIE()) return;
        PSLogger.warn("Can't find any items under ie, try again");
        TIMEOUT_FOR_LADING.waitTime();
        searchForLabels();
    }

    static void waitWhileLoading(ITagChooser tagChooser) {
        try {
            tagChooser.getPopup().getChildByXpath(POPUP_LOADING).waitForUnvisible();
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(tagChooser.getClass().getSimpleName() + " : " + we.getMessage());
            if (getAppVersion().verGreaterOrEqual(_9_2) && getAppVersion().verLessOrEqual(_9_3))
                throw new PSKnownIssueException(77980, we);
            throw we;
        }
        try {
            tagChooser.getPopup().getChildByXpath(POPUP_LABELS_PARENT).waitForPresent(TIMEOUT_FOR_LADING);
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(we.getMessage());
        }
    }


    public void closePopup() {
        PSLogger.debug("Close tag chooser popup");
        getParent().mouseDownAndUp();
        waitForPopupUnVisible();
    }

    public List<String> getAllLabels() {
        List<String> res = new ArrayList<String>();
        for (Item e : items) {
            res.add(e.getName());
        }
        return res;
    }

    @Override
    public void setName(String label) {
        name = label;
    }

    @Override
    public String getName() {
        return name;
    }

    public String toString() {
        return name == null ? super.toString() : (name + "[" + TagChooser.class.getSimpleName() + "]");
    }

    public boolean isSingleSelector() {
        return isSingleChooser;
    }

    public Element getLabel(String name) {
        for (Item label : items) {
            String txt = label.getName();
            if (txt.equals(name)) return label.innerElement;
        }
        PSLogger.warn("Can't find item " + name + " in popup");
        PSLogger.debug("All labels: " + getAllLabels());
        return null;
    }

    public boolean isChecked(String name) {
        for (Item item : items) {
            String txt = item.getName();
            if (txt.equals(name)) {
                return ((CheckBox) item.innerElement).getChecked();
            }
        }
        PSLogger.warn("Can't find item " + name + " in popup");
        return false;
    }

    public boolean isEditable(String name) {
        for (Item item : items) {
            String txt = item.getName();
            if (name.equals(txt)) {
                return ((CheckBox) item.innerElement).isEditable();
            }
        }
        PSLogger.warn("Can't find item " + name + " in popup");
        return false;
    }


    protected void searchForLabels() {
        items.clear();
        getPopup().setDefaultElement();
        main = popup.getChildByXpath(POPUP_MAIN);
        List<Element> list = Element.searchElementsByXpath(getPopup(), POPUP_LABELS);
        Element hiddenContainer = Element.searchElementByXpath(getPopup(), POPUP_HIDDEN_LABELS_PARENT);

        for (Element element : list) {
            DefaultElement e = element.getDefaultElement();
            if (hiddenContainer != null && e.getUniquePath().startsWith(hiddenContainer.getDefaultElement().getUniquePath())) {
                //then hidden, skip
                continue;
            }
            String attr = element.getDEAttribute(POPUP_LABEL_TYPE);
            String txt = StrUtil.replaceSpaces(element.getParent().getDEText()).trim();
            Assert.assertNotNull(attr, "Can't find any type for label '" + txt + "' (" + element.getLocator() + ")");
            CheckBox ch = null;
            if (attr.equals(POPUP_LABEL_TYPE_CHECKBOX.getLocator())) {
                isSingleChooser = false;
                ch = new CheckBox(element);
            } else if (attr.equals(POPUP_LABEL_TYPE_RADIO.getLocator())) {
                isSingleChooser = true;
                ch = new RadioButton(element);
            } else {
                Assert.fail("Unknown type for label '" + txt + "' (" + element.getLocator() + "): " + attr);
            }
            if (ch != null) {
                Item item = new Item(txt);
                ch.setName(txt);
                item.innerElement = ch;
                items.add(item);
            }
        }
        setElements();
        if (items.size() == 0) {
            PSLogger.warn("Can't find any items");
            PSLogger.save();
            PSLogger.debug(getPopup().asXML());
        }
        getPopup().setDynamic();
    }

    protected void setElements() {
        done = cancel = null;
        all = none = null;
        getDoneButton();
        getCancelButton();
        getSelectAllLink();
        getSelectNoneLink();
    }

    public void done() {
        PSLogger.save("before done");
        Button done = getDoneButton();
        PSLogger.debug("Push '" + done.getValue() + "'(done)");
        done.click(false);
        waitForPopupUnVisible();
        PSLogger.save("after done");
    }

    public void cancel() {
        getCancelButton().click(false);
        waitForPopupUnVisible();
    }

    protected void waitForPopupUnVisible() {
        try {
            getPopup().waitForUnvisible();
            focusOut();
        } catch (Wait.WaitTimedOutException we) { // seems like #87071
            Assert.fail("popup is still visible", we);
        }
        if (getDriver().getType().isIE()) {
            TIMEOUT_FOR_CLOSE.waitTime();
        }
        popup = null;
    }

    public void selectAll() {
        Link link = getSelectAllLink();
        if (!link.isVisible()) {
            PSLogger.warn("Link for selecting all is unvisible");
            return;
        }
        link.click(false);
    }

    public Link getSelectAllLink() {
        return all != null ? all : (all = new Link(getPopup().getChildByXpath(POPUP_ALL)));
    }

    public Link getSelectNoneLink() {
        return none != null ? none : (none = new Link(getPopup().getChildByXpath(POPUP_NONE)));
    }

    public Button getDoneButton() {
        return done != null ? done : (done = new Button(getPopup().getChildByXpath(POPUP_DONE)));
    }

    public Button getCancelButton() {
        return cancel != null ? cancel :
                (cancel = new Button(getPopup().getChildByXpath(POPUP_CANCEL)));
    }

    public void selectNone() {
        PSLogger.debug("none");
        Link link = getSelectNoneLink();
        if (!link.isVisible()) {
            PSLogger.warn("Link for selecting none is unvisible");
            return;
        }
        link.click(false);
    }

    public void select(String... labels) {
        select(true, labels);
    }

    public void unSelect(String... labels) {
        select(false, labels);
    }

    private void select(boolean check, String... labels) {
        PSLogger.debug("TagChooser.select: " + (labels != null ? Arrays.asList(labels) : "null"));
        for (String label : labels) {
            Element e = getLabel(label);
            Assert.assertTrue(isEditable(label), "label '" + label + "' is not editable");
            if (!isChecked(label) ^ check) {
                PSLogger.warn("label " + label + " is already " + (check ? "checked" : "unchecked"));
                continue;
            }
            PSLogger.debug("Select " + e);
            if (CoreProperties.getBrowser().isIE(9)) {
                scrollTo(e);
            }
            e.click(false);
        }
    }

    public void scrollTo(String label) {
        scrollTo(getLabel(label));
    }

    protected void scrollTo(Element e) {
        if (main == null || !main.isDEPresent()) {
            // no main
            return;
        }
        Integer[] coords = main.getCoordinates();
        int pos = e.getCoordinates()[1] - main.getCoordinates()[1];
        if (pos <= coords[3]) {
            // nothing to scroll
            return;
        }
        String script = main.getDomScrollTopScript() + "=" + pos;
        getDriver().getEval(script);
        PSLogger.save("After scrolling");
    }

    public void selectUnselect(boolean checkOn) {
        for (Item label : items) {
            if (label.innerElement != null) {
                CheckBox ch = ((CheckBox) label.innerElement);
                if (ch.getChecked() ^ checkOn) {
                    PSLogger.info("Check " + (checkOn ? "on" : "off") + " " + ch);
                    if (CoreProperties.getBrowser().isIE(9)) {
                        scrollTo(ch);
                    }
                    ch.click();
                }
            } else {
                PSLogger.debug("Can't select element " + label + "; it is not checkbox");
            }
        }
    }

    public void setLabel(String... labels) {
        PSLogger.info("Select " + Arrays.toString(labels) + " in tag chooser");
        openPopup();
        select(labels);
        searchForLabels();
        done();
    }

    public void setLabel(String label) {
        setLabel(new String[]{label});
    }

    public void setAllLabels() {
        PSLogger.info("Open Tag Chooser. Push 'All'");
        openPopup();
        selectAll();
        done();
    }

    public void setNoneLabels() {
        PSLogger.info("Open Tag Chooser. Push 'None'");
        openPopup();
        selectNone();
        done();
    }

    private Element getContentElement() {
        return getChildByXpath(INPUT_CONTENT);
    }

    public String getContentString() {
        return getContent().toString();
    }

    public List<String> getContent() {
        List<String> res = new ArrayList<String>();
        Element content = getContentElement();
        String[] sTxt;
        if (CoreProperties.getBrowser().isWebDriverIE(9)) {
            //todo: its workaround for ie9-web-driver
            content.setDefaultElement();
            PSLogger.debug("TagChooser content: " + content.asXML());
            Element empty = content.getChildByXpath(INPUT_CONTENT_EMPTY);
            String txt = content.getInnerText();
            if (empty.isDEPresent()) txt = txt.replace(empty.asXML(), "");
            sTxt = StrUtil.splitHtml(StrUtil.htmlToString(txt));
        } else {
            sTxt = content.getText().split("\n");
        }
        for (String s : sTxt) {
            if (!s.trim().isEmpty())
                res.add(s.trim());
        }
        PSLogger.debug(res);
        return res;
    }

    public void setPage(PSPage page) {
        this.page = page;
    }

    public static class Item {
        private Element innerElement;
        Item parent;
        String txt;
        boolean isHidden;

        public Item(String s) {
            txt = s;
        }

        public void setParent(Item p) {
            parent = p;
        }

        public Element get() {
            return innerElement;
        }

        public void set(Element e) {
            if (e instanceof CheckBox) ((CheckBox) e).setName(txt);
            innerElement = e;
        }

        public String toString() {
            if (parent == null) return txt;
            return parent.toString() + PSTag.SEPARATOR + txt;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (o instanceof String) {
                return txt.equals(o);
            }
            if (o instanceof Item) {
                return txt.equals(((Item) o).txt);
            }
            return false;
        }

        public int hashCode() {
            return txt.hashCode();
        }

        public Item getParent() {
            return parent;
        }

        public String getName() {
            return txt;
        }

        public static Set<Item> getOpenedBranches(Set<Item> set, List<Item> chain) {
            Set<Item> res = new HashSet<Item>();
            for (Item item : chain) {
                res.add(item);
                res.addAll(getChilds(set, item));
            }
            res.addAll(getRoots(set));
            return res;
        }

        public static List<Item> getChilds(Set<Item> set, Item parent) {
            List<Item> res = new ArrayList<Item>();
            for (Item item : set) {
                if (parent.equals(item.getParent())) {
                    res.add(item);
                }
            }
            return res;
        }

        public static List<Item> getChainForChild(Item child) {
            List<Item> res = new ArrayList<Item>();
            res.add(child);
            if (child.getParent() == null) return res;
            res.addAll(getChainForChild(child.getParent()));
            return res;
        }

        public static List<List<Item>> getAllChains(Set<Item> set) {
            List<List<Item>> chains = new ArrayList<List<Item>>();
            for (Item item : set) {
                List<Item> chain = getChainForChild(item);
                Collections.reverse(chain);
                PSLogger.debug("Chain: " + chain);
                chains.add(chain);
            }
            return chains;
        }

        public static List<Item> getRoots(Set<Item> set) {
            List<Item> res = new ArrayList<Item>();
            List<List<Item>> sequences = getAllChains(set);
            for (List list : sequences) {
                if (list.size() == 1) {
                    res.add((Item) list.get(0));
                }
            }
            return res;
        }

        public static List<Item> getFirstLongestChain(Set<Item> set) {
            List<List<Item>> sequences = getAllChains(set);
            Collections.sort(sequences, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((List) o1).size() > ((List) o2).size()) return -1;
                    if (((List) o1).size() < ((List) o2).size()) return 1;
                    return 0;
                }
            });
            return sequences.get(0);
        }
    }
}
