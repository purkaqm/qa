package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.WorkTreeElementLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 20.08.2010
 * Time: 15:33:23
 */
public class WorkTreeElement extends Element {
    private Element popup;
    private long loadingTimeout = CoreProperties.getWaitForElementToLoad();
    private static final TimerWaiter SEARCHING_LINK_TIMEOUT = new TimerWaiter(3000);
    private static final TimerWaiter SCROLL_TIMEOUT = new TimerWaiter(1000);

    private static final String SPACE = "\t\t";
    private static final String LINE = "\n";
    private List<TreeNode> rootNodes = new ArrayList<TreeNode>();
    private boolean doScroll;
    public static final int ALLOWED_NON_SCROLLABLE_NODES = 16;
    protected int allowedNonScrollableNodes;

    protected WorkTreeElement(int n) {
        this(TREE_ROOT, n);
    }

    public WorkTreeElement() {
        this(ALLOWED_NON_SCROLLABLE_NODES);
    }

    public WorkTreeElement(ILocatorable loc) {
        this(loc, ALLOWED_NON_SCROLLABLE_NODES);
    }

    protected WorkTreeElement(ILocatorable loc, int n) {
        super(loc);
        allowedNonScrollableNodes = n;
    }

    public void setTimeout(long time) {
        loadingTimeout = time;
    }

    public void waitWhileLoading() {
        new Element(LOADING_ELEMENT_LOCATOR).waitForUnvisible(loadingTimeout);
        SEARCHING_LINK_TIMEOUT.waitTime();
        setDefaultElement();
        init();
    }

    private void init() {
        rootNodes.clear();
        for (Element link : Element.searchElementsByXpath(this, WORK_ITEM_ROOTS)) {
            rootNodes.add(new TreeNode(link));
        }
    }

    private void initTree() {
        if (rootNodes.size() != 0) return;
        waitWhileLoading();
    }

    @Deprecated
    public void openWorkNode(String name) {
        Element nodePlus = Element.searchElementByXpath(getWorkLink(name).getParent(), TREE_ITEM_PLUS);
        if (nodePlus == null) return;
        nodePlus.setSimpleLocator();
        nodePlus.click(false);
        nodePlus.waitForClass(TREE_ITEM_OPENNED_CLASS);
        setDefaultElement();
    }

    public Link getWorkLink(String linkName) {
        return getWorkLink(linkName, true);
    }

    public Link getWorkLink(String linkName, boolean doCheck) {
        List<TreeNode> links = new ArrayList<TreeNode>();
        for (TreeNode link : getTreeNodes()) {
            if (linkName.equals(link.name)) {
                links.add(link);
            }
        }
        if (doCheck)
            Assert.assertFalse(links.size() == 0, "Cannot find link '" + linkName + "'");
        if (links.size() != 1) {
            PSLogger.warn("There is more then one nodes for '" + linkName + "':" + links);
        }
        return links.size() > 0 ? links.get(links.size() - 1).getLink() : null;
    }

    protected List<TreeNode> getTreeNodes(Boolean isVisible) {
        List<TreeNode> res = new ArrayList<TreeNode>();
        for (TreeNode n : rootNodes) {
            putToList(res, n, isVisible);
        }
        return res;
    }

    public List<TreeNode> getTreeNodes() {
        return getTreeNodes(null);
    }

    private void putToList(List<TreeNode> res, TreeNode node, Boolean isVisible) {
        if (isVisible == null || isVisible == !node.isHidden) {
            res.add(node);
        }
        for (TreeNode n : node.nodes) {
            if (isVisible == null || isVisible == !n.isHidden) {
                putToList(res, n, isVisible);
            }
        }
    }

    public String print() {
        StringBuilder sb = new StringBuilder(LINE);
        for (TreeNode node : rootNodes) {
            sb.append(node.print()).append(LINE);
        }
        return sb.substring(0, sb.length() - LINE.length());
    }


    public void openTree() {
        PSLogger.info("Open all tree");
        initTree();
        openTree(rootNodes);
        PSLogger.save("After opening tree");
    }

    private void openTree(List<TreeNode> nodes) {
        for (int i = 0; i < nodes.size(); i++) { // do not use foreach: ConcurrentModificationException
            nodes.get(i).open();
        }
        for (int i = 0; i < nodes.size(); i++) {
            openTree(nodes.get(i).nodes);
        }
    }

    public void openTree(String parentName) {
        if (parentName == null) {
            PSLogger.debug("Parent not specified");
            return;
        }
        PSLogger.info("Open tree for '" + parentName + "'");
        initTree();
        TreeNode node = null;
        for (TreeNode n : rootNodes) {
            if (parentName.equals(n.name)) {
                node = n;
                break;
            }
        }
        Assert.assertNotNull(node, "Can not find node '" + parentName + "'");
        node.open();
        openTree(node.nodes);
        PSLogger.save("After open tree for node '" + parentName + "'");
    }

    public Link openTree(Work work) {
        PSLogger.debug("Open tree for '" + work.getFullName() + "'");
        initTree();
        List<String> path = getPath(work);
        List<TreeNode> res = openNodes(rootNodes, path);
        if (res == null) return null;
        if (res.size() != 1) {
            PSLogger.warn("There are " + res.size() + " nodes with path " + work.getFullName());
        }
        return res.get(0).getLink();
    }

    private List<String> getPath(Work work) {
        List<String> full = work.getPath();
        if (full.size() < 2) return full;
        List<String> res = getPath(BasicCommons.getCurrentUser(), work.getParent());
        res.add(res.size(), work.getName());
        if (res.size() != full.size()) {
            PSLogger.info("Visible path for work " + work + " is " + res);
        }
        return res;
    }

    private static List<String> getPath(User user, Work work) {
        List<String> res = new ArrayList<String>();
        if (!work.canView(user)) return res;
        if (work.hasParent()) {
            res.addAll(getPath(user, work.getParent()));
        }
        res.add(work.getName());
        return res;
    }

    private List<TreeNode> openNodes(List<TreeNode> nodes, List<String> path) {
        String p = path.remove(0);
        List<String> _path = new ArrayList<String>(path);
        List<TreeNode> _nodes = searchNodes(nodes, p);
        if (_nodes.size() == 0) return null;
        if (path.size() == 0) {
            return _nodes;
        }
        for (TreeNode n : _nodes) {
            n.open();
            List<TreeNode> res = openNodes(n.nodes, _path);
            if (res != null) return res;
        }
        return null;
    }

    private static List<TreeNode> searchNodes(List<TreeNode> nodes, String name) {
        List<TreeNode> res = new ArrayList<TreeNode>();
        for (TreeNode n : nodes) {
            if (name.equals(n.name)) {
                res.add(n);
            }
        }
        return res;
    }

    public void setPopup(Element d) {
        popup = d;
        popup.setDefaultElement();
    }

    public Element getPane() {
        if (popup == null) return null;
        return popup.getChildByXpath(POPUP_BROWSE_PANE);
    }

    public boolean isPopup() {
        return popup != null;
    }

    public List<String> getAllWorks() {
        List<String> res = new ArrayList<String>();
        for (TreeNode e : getTreeNodes()) {
            res.add(e.name);
        }
        return res;
    }


    public class TreeNode extends Element {
        private String name;
        private Link link;
        private PlusElement plus;
        private Img img;
        private List<TreeNode> nodes = new ArrayList<TreeNode>();
        private boolean isHidden;

        private TreeNode(Element container) {
            super(container);
            if (!isSimpleLocator())
                setSimpleLocator();
            init();
        }

        public boolean equals(Object o) {
            return o != null && o instanceof TreeNode && getId().equals(((TreeNode) o).getId());
        }

        private void init() {
            Element item = getChildByXpath(WORK_ITEM);
            Assert.assertTrue(item.isDEPresent(), "Can not find item in container " + asXML());
            Element link = item.getChildByXpath(isPopup() ? WORK_ITEM_LINK_POPUP : WORK_ITEM_LINK);
            if (link.isDEPresent()) {
                name = link.getDEText();
                if (name.isEmpty()) { // 10.0, timesheets page
                    Element ch = link.getChildByXpath(WORK_ITEM_LINK_POPUP_TIMESHEETS_100);
                    if (ch.isDEPresent()) {
                        link = ch;
                        name = link.getDEText();
                    }
                }
                this.link = new Link(link);
                this.link.setName(name);
            } else { // no link.
                String n = item.getDEText();
                if (!n.isEmpty())
                    name = n;
                else {
                    Element noLink = item.getChildByXpath(WORK_ITEM_NO_LINK);
                    if (noLink.isDEPresent()) {
                        name = noLink.getDEText();
                    }
                }
            }
            Element plus = item.getChildByXpath(TREE_ITEM_EXPAND_COLLAPSE);
            if (plus.isDEPresent() && !plus.getDEClass().contains(TRRR_ITEM_LEAF_CLASS.getLocator())) {
                this.plus = new PlusElement(plus);
            }
            Element img = item.getChildByXpath(TREE_ITEM_IMG);
            if (img.isDEPresent()) {
                this.img = new Img(img);
            }
            nodes.clear();
            for (Element _container : Element.searchElementsByXpath(this, WORK_ITEM_PARENTS)) {
                nodes.add(new TreeNode(_container));
            }
        }

        private void init(TreeNode node) {
            name = node.name;
            link = node.link;
            img = node.img;
            nodes = node.nodes;
        }

        public Link getLink() {
            return link;
        }

        private class PlusElement extends ScrollableElement {
            private boolean expanded;
            private boolean collapsed;

            private PlusElement(Element e) {
                super(e);
                init();
            }

            private void init() {
                String clazz = getDEClass();
                expanded = clazz.contains(TREE_ITEM_OPENNED_CLASS.getLocator());
                collapsed = clazz.contains(TREE_ITEM_CLOSED_CLASS.getLocator());
            }

            public void scrollTo() {
                super.scrollTo();
                SCROLL_TIMEOUT.waitTime();
            }

            public void waitWhileLoading() {
                waitForClassChanged(TREE_ITEM_LOADING_CLASS);
            }

            public boolean isExpanded() {
                return expanded;
            }

            public boolean isCollapsed() {
                return collapsed;
            }

            public void click() {
                super.click(false);
                ILocatorable clazz = null;
                if (expanded && !collapsed)
                    clazz = TREE_ITEM_CLOSED_CLASS;
                if (!expanded && collapsed)
                    clazz = TREE_ITEM_OPENNED_CLASS;
                if (clazz != null)
                    waitForClass(clazz);
            }
        }

        private void expandCollapse(boolean doOpen) {
            if (plus == null) {
                PSLogger.debug2("Node for '" + name + "' has not any children");
                return;
            }
            if ((doOpen && plus.isExpanded()) || (!doOpen && plus.isCollapsed())) {
                // already opened
                PSLogger.debug("Node for '" + name + "' is already " + (doOpen ? "expanded" : "collapsed"));
                return;
            }
            PSLogger.debug((doOpen ? "Open" : "Close") + " node for '" + name + "'");
            if (doScroll)
                plus.scrollTo();
            plus.click();
            WorkTreeElement.this.setDefaultElement();
            for (TreeNode n : WorkTreeElement.this.rootNodes) {
                n.setDefaultElement(WorkTreeElement.this.getParentDoc());
            }
            List<TreeNode> all = getTreeNodes();
            init(all.get(all.indexOf(this)));
            for (TreeNode n : nodes) {
                n.isHidden = !doOpen;
            }
            int n = getTreeNodes(true).size();
            PSLogger.debug("Number of visible nodes is " + n);
            doScroll = n > allowedNonScrollableNodes;
        }

        @Override
        public void setDefaultElement(Document doc) {
            super.setDefaultElement(doc);
            init();
        }

        public void open() {
            expandCollapse(true);
        }

        public void close() {
            expandCollapse(false);
        }

        @Override
        public String toString() {
            return name;
        }

        public String print() {
            if (nodes.size() == 0) {
                return name;
            }
            StringBuilder sb = new StringBuilder(name).append(LINE);
            for (TreeNode n : nodes) {
                for (String s : n.print().split(LINE)) {
                    sb.append(SPACE).append(s).append(LINE);
                }
            }
            return sb.substring(0, sb.length() - LINE.length());
        }
    }

    private class ScrollableElement extends Element {

        private ScrollableElement(Element e) {
            super(e);
        }

        public void scrollTo() {
            if (!isPopup()) {
                super.scrollTo();
                return;
            }
            Element pane = getPane();
            Integer[] c1 = pane.getCoordinates();
            Integer[] c2 = getCoordinates();
            Integer diff = 0;
            String script = pane.getDomLocator() + ".scrollTop";
            if (getDriver().getType().isWebDriver()) { // mb only for ie-web-driver
                String pos = getDriver().getEval(script);
                diff = Integer.parseInt(pos);
            }
            int position = c2[1] - c1[1] + diff;
            script += "=" + position;
            getDriver().getEval(script);
        }
    }

}
