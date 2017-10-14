package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.elements_locators.GridLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.*;

import static com.powersteeringsoftware.libs.enums.elements_locators.GridLocators.*;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions.*;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;

/**
 * User: szuev
 * Date: 05.09.11
 * Time: 15:45
 */
public class SimpleGrid extends Element {

    protected static final TimerWaiter JS_WAITER = new TimerWaiter(2000); // for js
    protected static final TimerWaiter CYCLE_GRID_WAITER = new TimerWaiter(2000);
    public static final TimerWaiter LOADING_GRID_WAITER_IE = new TimerWaiter(500);
    protected static final TimerWaiter LOADING_GRID_WAITER = new TimerWaiter(5000);
    private static final long GRID_CELL_LOADING_TIMEOUT = 10000;// ms
    private static final long GRID_CELL_LOADING_TIMEOUT_IE = 50000;// ms
    public static final String PROBLEM_MESSAGE_PREFIX = "There is a problem";
    private boolean saveAfterLoading;


    public SimpleGrid() {
        this(false);
    }

    public SimpleGrid(boolean catchLoading) {
        this(GRID, catchLoading);
    }

    protected SimpleGrid(ILocatorable grid, boolean catchLoading) {
        super(grid);
        saveAfterLoading = catchLoading;
    }

    public Element getBody() {
        return getChildByXpath(GRID_BODY);
    }

    protected Element getTreeSection() {
        return getBody().getChildByXpath(TREE);
    }

    public HorizontalScrollBar getScroll() {
        return new HorizontalScrollBar(GRID_SCROLL_BAR);
    }

    protected int getRowIndex(List<String> tree, Work work) {
        return indexOf(tree, work);
    }

    public int getRowIndex(Work work) {
        return getRowIndex(getListTree(), work);
    }

    private static int indexOf(List list, Work w) {
        List<Integer> indexes = getIndexes(list, w.getName());
        if (indexes.size() == 0) return -1;
        if (indexes.size() == 1) return indexes.get(0) + 1;
        List<String> chain = getWorkChain(list, w);
        List<Integer> start = getIndexes(list, chain.get(0));
        List<Integer> end = getIndexes(list, chain.get(chain.size() - 1));
        for (int i : indexes) {
            if (i >= start.get(0) && i <= end.get(end.size() - 1)) return i + 1;
        }
        return -1;
    }

    private static List<Integer> getIndexes(List<String> list, String w) {
        List<Integer> res = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(w)) res.add(i);
        }
        return res;
    }

    private static List<String> getWorkChain(List<String> list, Work w) {
        List<String> chain = new ArrayList<String>();
        chain.add(w.getName());
        Work parent = w;
        while ((parent = parent.getParent()) != null) {
            if (list.contains(parent.getName()))
                chain.add(parent.getName());
        }
        Collections.reverse(chain);
        for (Work ch : w.getAllChildren()) {
            if (list.contains(ch.getName()))
                chain.add(ch.getName());
        }
        return chain;
    }


    /**
     * for Status column
     *
     * @param work   ChildWork
     * @param status Status
     */
    public void setStatus(Work work, String status) {
        PSLogger.info("Set status '" + status + "' for " + work.getName());
        SingleStatusSelector sss = getSingleStatusSelector(GRID_TABLE_STATUS, work);
        if (!isCellEditable(sss)) {
            PSLogger.warn("Can't set status for " + work + ": cell is not editable");
            return;
        }
        sss.setLabel(status);
        //waitWhileChangesIsApplied();
    }

    protected Element getLoadingCycle() {
        return new Element(LOADING_PLEASE_WAIT);
    }

    protected Element getLoadingUnderlay() {
        return new Element(LOADING_UNDERLAY);
    }

    protected Element getLoadingUnderlayBox() {
        return new Element(LOADING_BOX);
    }

    protected void waitWhileLoadingCycle() {
        Element cycle = getLoadingCycle();
        if (cycle.exists())
            cycle.waitForClass(LOADING_HIDDEN);
    }

    protected void waitWhileLoading() {
        try {
            Element box = getLoadingUnderlayBox();
            Element underlay = getLoadingUnderlay();
            if (!getDriver().getType().isIE()) {
                box.waitForClass(LOADING_HIDDEN);
                underlay.waitForClass(LOADING_HIDDEN);
            }
            box.waitForClass(LOADING_HIDDEN);

            waitWhileLoadingCycle();
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(getClass().getSimpleName() + ".waitForLoadingUnvisible: " + we.getMessage());
            checkForPopupError();
            throw we;
        }
    }

    private void waitWhileChangesIsApplied(boolean setDoc) {
        PSLogger.info("Wait cycle");
        waitWhileLoadingCycle();
        getBody().waitForPresent();
        if (saveAfterLoading) {
            PSLogger.save("After waiting grid body");
        }
        if (!getDriver().getType().isIE())
            CYCLE_GRID_WAITER.waitTime();
        if (setDoc) {
            setDefaultElement();
            checkForPopupError(false);
        }
    }

    protected void waitWhileChangesIsApplied() {
        setDynamic();
        waitWhileChangesIsApplied(true);
    }

    public void waitForVisible() {
        waitForVisible(true);
    }

    public void waitForVisible(boolean setDoc) {
        setDynamic();
        try {
            PSLogger.info("Wait grid loading");
            waitWhileLoading();
            if (saveAfterLoading) {
                PSLogger.save("After loading has been processed(1)");
            }
            getBody().waitForPresent();
            if (saveAfterLoading) {
                PSLogger.save("After grid has become visible");
            }
            waitWhileLoading();
            if (saveAfterLoading) {
                PSLogger.save("After loading has been processed(2)");
            }
            if (!getDriver().getType().isIE())
                LOADING_GRID_WAITER.waitTime();
            else
                LOADING_GRID_WAITER_IE.waitTime();
            if (setDoc) {
                setDefaultElement();
            }
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(we);
            checkForPopupError(true);
            throw we;
        }
        if (setDoc) {
            checkForPopupError(false);
            PSLogger.save("Saving after SimpleGrid.waitWhileVisible");
        }
    }

    public void setStatus(Work work, Work.Status status) {
        setStatus(work, status.getValue());
    }

    public void setOwner(Work work, User owner) {
        setOwner(work, owner.getFullName());
    }

    public void setOwner(Work work, String owner) {
        PSLogger.debug("Set owner " + owner + " for " + work);
        // hotfix:!!
        Element cell = getCellByName(GRID_TABLE_OWNER, work);
        Element child = cell.getChildByXpath(GRID_TABLE_CELL_FIRST_CHILD);
        if (child.isDEPresent()) cell = child;
        if (!isCellEditable(cell)) {
            PSLogger.warn("Can't set owner for " + work + ": cell is not editable");
            return;
        }
        new UserChooser(cell).set(owner);
        waitWhileChangesIsApplied();
    }


    public boolean isProjectAGate(Work work) {
        int index = getRowIndex(work);
        List<String> images = getListImages();
        Assert.assertFalse(images.size() == 0, "No icons for projects found in grid");
        String image = images.get(index - 1);
        return image.endsWith(TREE_ITEM_IMG_SRC_GATED.getLocator()) ||
                image.endsWith(TREE_ITEM_IMG_SRC_TOLLGATE.getLocator());
    }

    public String getOwner(Work work) {
        return getCellByName(GRID_TABLE_OWNER, work).getText();
    }


    public SingleStatusSelector getSingleStatusSelector(ILocatorable loc, Work work) {
        final Element cell = getCellByName(loc, work);
        return new SingleStatusSelector(cell) {
            public void set(String s) {
                super.set(s);
                waitWhileChangesIsApplied();
            }
        };
    }

    public WBSDatePicker getDatePicker(ILocatorable loc, Work work) {
        return getDatePicker(loc.getLocator(), work);
    }

    public WBSDatePicker getDatePicker(String loc, Work work) {
        Element cell = getCellByName(loc, work);
        return new WBSDatePicker(cell);
    }

    /**
     * for status column
     *
     * @param work ChildWork
     * @return Sting status
     */
    public String getStatus(Work work) {
        return getSingleStatusSelector(GRID_TABLE_STATUS, work).getContent();
    }


    public List<String> getAllStatusesFromPage() {
        List<String> res = new ArrayList<String>();
        List<String> tree = getListTree();
        for (int i = 1; i < tree.size() + 1; i++) {
            Element cell = searchCellByNameAndWork(tree, GRID_TABLE_STATUS.getLocator(), i, true);
            SingleStatusSelector selector = new SingleStatusSelector(cell);
            res.add(selector.getContent());
        }
        return res;
    }

    public boolean isCellEdited(ILocatorable name, Work work) {
        return isCellEdited(name.getLocator(), work);
    }

    protected boolean isCellEdited(String locator, Work work) {
        return isCellEdited(getCellByName(locator, work));
    }

    protected String getCellClass(Element cell) {
        String clazz = null;
        if (getAppVersion().verGreaterOrEqual(_9_0)) {
            Element parent = cell.getParent();
            if (parent == null) {
                PSLogger.warn("Null parent for cell " + cell);
            } else {
                clazz = parent.isDEPresent() ? parent.getDEClass() : parent.getElementClass();
            }
        } else {
            clazz = cell.isDEPresent() ? cell.getDEClass() : cell.getElementClass();
        }
        return clazz == null ? "" : clazz.toLowerCase();
    }

    protected boolean isCellEdited(Element cell) {
        return getCellClass(cell).contains(GRID_TABLE_CELL_EDITED_CLASS_ATTR.getLocator());
    }

    public boolean isCellEditable(ILocatorable name, Work work) {
        return isCellEditable(name.getLocator(), work);
    }

    public boolean isCellEditable(String name, Work work) {
        return isCellEditable(getCellByName(name, work));
    }

    public boolean isCellEditable(Element cell) {
        if (cell == null) {
            PSLogger.warn("isCellEditable: No cell specified");
            return false;
        }
        if (!getDriver().getType().isIE()) { // todo: is it correct?
            cell.waitForClassChanged(GRID_TABLE_CELL_UNEDITABLE_CLASS_ATTR, JS_WAITER);
        }
        return !getCellClass(cell).contains(GRID_TABLE_CELL_UNEDITABLE_CLASS_ATTR.getLocator());
    }

    public Element getCellByName(ILocatorable name, Work work) {
        return getCellByName(name.getLocator(), work);
    }

    public Element getFirstCellByName(ILocatorable name) {
        return getColumnByTableHeader(name.getLocator()).getChildByXpath(GRID_TABLE_CELL_FIRST);
    }

    protected Element searchCellByNameAndWork(String name, Work work, boolean doWait) {
        PSLogger.debug2("Search cell " + name + " for " + work.getFullName());
        List<String> tree = getListTree();
        if (doWait && tree.isEmpty()) {
            PSLogger.warn("Tree is empty, wait.");
            JS_WAITER.waitTime();
            setDefaultElement();
            tree = getListTree();
        }
        int index = getRowIndex(tree, work);
        if (index == -1) {
            PSLogger.debug("Tree : " + tree);
            return null;
        }
        if (!doWait)
            PSLogger.debug2("Work '" + work.getFullName() + "' has index #" + index + ".");
        return searchCellByNameAndWork(tree, name, index, doWait);
    }

    /**
     * this is for acceleration (under ie)
     */
    private class GridCell extends Element {
        private Integer row;
        private String column;

        private GridCell(Element element) {
            super(element);
        }

        private GridCell(Element element, String column, Integer row) {
            this(element);
            this.column = column;
            this.row = row;
        }

        public String getText() {
            if (getDriver().getType().isFF()) return super.getText();
            String res = getTextContent(this, GridCell.class);
            return res == null ? super.getText() : res;
        }

        public String toString() {
            if (row != null && column != null) return "[" + column + "," + row + "]";
            return super.toString();
        }
    }

    private Element searchCellByNameAndWork(List<String> tree, String name, int index, boolean doWait) {
        Element column = getColumnByTableHeader(name);
        if (column == null) {
            if (doWait) {
                PSLogger.warn("Can't find column '" + name + "', try again");
                JS_WAITER.waitTime();
                setDefaultElement();
                column = getColumnByTableHeader(name);
                if (column == null) return null;
            } else {
                return null;
            }
        }
        List<Element> cells = Element.searchElementsByXpath(column, GRID_TABLE_CELL_CONTENT);
        if (cells.size() == tree.size()) {
            return new GridCell(cells.get(index - 1), name, index);
        }
        // for 9.1 or later wait some time while cells loading:
        if (doWait && getAppVersion().verGreaterOrEqual(_9_0)) {
            PSLogger.debug2("Wait for cells (column: " + name + ")");
            long s = System.currentTimeMillis();
            while ((System.currentTimeMillis() - s) < (getDriver().getType().isIE() ? GRID_CELL_LOADING_TIMEOUT_IE : GRID_CELL_LOADING_TIMEOUT)) {
                JS_WAITER.waitTime();
                column.setDefaultElement();
                cells = Element.searchElementsByXpath(column, GRID_TABLE_CELL_CONTENT);
                if (cells.size() == tree.size()) return new GridCell(cells.get(index - 1), name, index);
            }
        }
        // nothing found
        return null;
    }

    protected Element getCellByName(String column, Work work) {
        PSLogger.debug("Get cell for column '" + column + "' and child '" + work.getFullName() + "'");
        Element res = searchCellByNameAndWork(column, work, false);
        if (res != null) return res;
        if (getAppVersion().verGreaterOrEqual(_9_0)) { // lazy loading, need scroll
            int[] position = getColumnPosition(column);
            if (position != null) {
                if (!isColumnVisible(position)) {
                    scrollTo(position[1]);
                }
                // research after scrolling:
                res = searchCellByNameAndWork(column, work, true);
                if (res != null)
                    return res;
            }
        }
        PSLogger.warn("Can't find cell '" + column + "' for work " + work.getName());
        return res;
    }

    /**
     * @param name - name for column
     * @return true if column is visible
     */
    protected boolean isColumnVisible(String name) {
        return isColumnVisible(getColumnPosition(name));
    }

    private boolean isColumnVisible(int[] coords) {
        int x = coords[1] - getScroll().getScroll();
        return x <= coords[0] && x >= 0;
    }

    public boolean isColumnVisible(ILocatorable name) {
        return isColumnVisible(name.getLocator());
    }

    /**
     * @param name
     * @return array with absolute x,w for header and relative x for specified column
     */
    protected int[] getColumnPosition(String name) {
        Element header = getTableHeader();
        Element th = getTableHeaderElement(name);

        if (th == null) {
            PSLogger.warn("Can't find table header " + name);
            return null;
        }
        Integer[] thCoords = th.getCoordinates();
        Integer[] headerCoords = header.getCoordinates();
        return new int[]{headerCoords[2], thCoords[0] - headerCoords[0]};
    }

    private void scrollTo(int x) {
        getScroll().doScroll(x);
        setDefaultElement();
    }

    public void scrollTo(ILocatorable name) {
        int[] pos = getColumnPosition(name.getLocator());
        scrollTo(pos[1]);
    }

    protected List<Element> getTreeCells() {
        return Element.searchElementsByXpath(this, GRID_TABLE_CELL_TREE);
    }

    public Element getCellByName(Work work) {
        int index = getRowIndex(work);
        if (index == -1) return null;
        return getCell(index);
    }

    public Element getCell(int rowIndex) {
        return getTreeCells().get(rowIndex - 1);
    }

    /**
     * get all items from tree (without relations)
     *
     * @return List
     */
    public List<String> getListTree() {
        return getListTree(getTreeItemLoc());
    }

    public Link getItem(String name) {
        for (Element e1 : getElementsListTree(getTreeItemLoc())) {
            Element container = e1.getParent(TREE_ITEM_PARENT_DIV, TREE_ITEM_PARENT_DIV_CLASS);
            if (container == null || !container.getDEClass().contains(TREE_ITEM_PARENT_DIV_CLASS_HIDDEN.getLocator())) {
                Element e2 = e1.getChildByXpath(TREE_ITEM_LINK);
                if (e2 == null || !e2.isDEPresent()) continue;
                if (e2.getDEText().equals(name)) return new Link(e2);
            }
        }
        return null;
    }

    protected List<String> getListTree(ILocatorable loc) {
        return getListTree(getElementsListTree(loc));
    }

    protected List<String> getListTree(List<Element> tree) {
        List<String> res = new ArrayList<String>();
        for (Element e1 : tree) {
            Element container = e1.getParent(TREE_ITEM_PARENT_DIV, TREE_ITEM_PARENT_DIV_CLASS);
            if (container == null || !container.getDEClass().contains(TREE_ITEM_PARENT_DIV_CLASS_HIDDEN.getLocator())) // skip invisible items
                res.add(getWorkNameByElement(e1));
        }
        return res;
    }

    public void expandTree() {
        doExpandCollapse(true);
        PSLogger.save("After expand");
    }

    public void collapseTree() {
        doExpandCollapse(false);
        PSLogger.save("After collapse");
    }

    protected void doExpandCollapse(boolean expand) {
        if (!getDriver().getType().isRCDriverFF()) {
            //TODO
            PSLogger.skip("doExpandCollapse: Unsupported for this driver type");
            return;
        }
        Element div = null;
        for (Element e : Element.searchElementsByXpath(getTreeSection(), expand ? TREE_ITEM_CLOSED : TREE_ITEM_OPENED)) {
            if (e.getDEClass().contains(TREE_ITEM_FIRST_CLASS.getLocator())) continue;
            div = e;
            break;
        }
        if (div == null || !div.exists()) {
            PSLogger.debug("Nothing to " + (expand ? "expand" : "collapse"));
            return;
        }
        String name = getWorkNameByElement(div.getChildByXpath(getTreeItemLoc()));
        PSLogger.debug((expand ? "Expand" : "Collapse") + " item '" + name + "'");
        div.click(false);
        waitWhileLoadingCycle();
        setDefaultElement();
        doExpandCollapse(expand);
    }

    protected ILocatorable getTreeItemLoc() {
        return TREE_ITEM;
    }

    protected List<Element> getElementsListTree(ILocatorable loc) {
        return Element.searchElementsByXpath(getTreeSection(), loc);
    }

    protected List<Element> getTreeChildrenContents() {
        return Element.searchElementsByXpath(getTreeSection(), TREE_ITEM_PARENT_DIV_LOC);
    }
    public List<String> getListImages() {
        List<String> res = new ArrayList<String>();
        for (Element e : Element.searchElementsByXpath(getTreeSection(), TREE_ITEM_IMG)) {
            String attr = e.getDEAttribute(TREE_ITEM_IMG_SRC);
            if (attr != null && !attr.isEmpty())
                res.add(attr);
        }
        return res;
    }

    /**
     * for status column
     *
     * @return - List of all statuses from root cell
     */
    public List<String> getAllStatusesFromFirstCell() {
        SingleStatusSelector selector = new SingleStatusSelector(getFirstCellByName(GRID_TABLE_STATUS));
        selector.openPopup();
        List<String> res = selector.getAllLabels();
        //new Element(GRID_TABLE_HEADER_PARENT).mouseDownAndUp();
        mouseDownAndUp();
        selector.getPopup().waitForUnvisible();
        return res;
    }

    protected Element getTableHeader() {
        return Element.searchElementByXpath(this, GRID_TABLE_HEADER);
    }

    public List<String> getTableHeaderList() {
        return new ArrayList<String>(getTableHeaderMap().keySet());
    }


    /**
     * @param element dom4j Document
     * @return Map. as key header name, as value Element of header cell
     */
    protected Map<String, Element> getTableHeaderMap() {
        Map<String, Element> res = new LinkedHashMap<String, Element>();
        for (Element div : getTableHeaderElements()) {
            String text;
            if (getAppVersion().verGreaterOrEqual(_9_2)) {
                text = div.getDEInnerText().trim();
            } else if (getAppVersion().verGreaterOrEqual(_9_1)) {
                text = div.getChildByXpath(GRID_TABLE_HEADER_CELL_TEXT_91).getDEText();
            } else {
                text = div.getDEText();
            }
            Element th = div.getParent();
            th.setId(th.getDEId());
            //th.setId();
            res.put(text, th);
        }
        return res;
    }


    protected List<Element> getTableHeaderElements() {
        return Element.searchElementsByXpath(this, GRID_TABLE_HEADER_CELL);
    }

    /**
     * @param name - column name
     * @return Element header by column name
     */
    protected Element getTableHeaderElement(String name) {
        return getTableHeaderMap().get(name);
    }

    /**
     * @return Element header for tree section
     */
    public Element getTableHeaderElement() {
        return Element.searchElementByXpath(this, GRID_TABLE_FIRST_COLUMN);
    }

    public void reSizeFirstColumn(float number) {
        reSizeColumn(null, number);
    }

    /**
     * Re-size specified column
     *
     * @param name   - name of column
     * @param number - factor, e.g. 0.5, 1.5
     */
    public void reSizeColumn(String name, float number) {
        if (getDriver().getType().isIE() && getDriver().getType().getMainVersion() > 6) {
            //PSLogger.skipWithThrow("Not supported for ie 7,8");
            LocalServerUtils.mouseMoveAt(0, 0);
        }

        Element header;
        if (name != null) {
            header = getTableHeaderElement(name);
        } else {
            header = getTableHeaderElement();
            name = GRID_TABLE_TREE_NAME.getLocator();
        }
        int widthWas = getColumnWidth(header);
        int widthNew = (int) (widthWas * number);
        PSLogger.debug("Column '" + name + "' width is " + widthWas + "");
        PSLogger.info("Re-size column '" + name + "' width to " + number + " times");
        header.mouseMoveAt(widthWas, 0);
        header.waitForClass(GRID_TABLE_HEADER_RESIZING_CLASS);

        Element divider = new Element(GRID_TABLE_DIVIDER);
        divider.waitForPresent();

        Element underlay = new Element(GRID_TABLE_RESIZING_UNDERLAY);
        PSLogger.save("After clicking on divider");

        int dividerWidth = divider.getCoordinates()[2];
        widthNew -= dividerWidth / 2;

        Integer[] underlayCoordinates = underlay.getCoordinates();
        underlay.mouseDownAt(underlayCoordinates[2], underlayCoordinates[3]);
        header.mouseMoveAt(widthNew, 0);
        underlay.mouseUp();
        divider.waitForDisapeared();
        header.mouseOut();
        header.waitForClassChanged(GRID_TABLE_HEADER_RESIZING_CLASS);
        underlay.waitForUnvisible();
        JS_WAITER.waitTime();
        PSLogger.save("after resizing " + name);
    }

    /**
     * its for resizing testcase
     *
     * @param name
     * @return
     */
    public static int getMinAllowedWidth(String name) {
        if (name == null) // tree section
            return Integer.parseInt(GRID_TABLE_NAME_MIN_WIDTH.getLocator());
        // general header.
        return Integer.parseInt(GRID_TABLE_HEADER_MIN_WIDTH.getLocator());
    }

    public int getColumnWidth(String name) {
        return getColumnWidth(name != null ? getTableHeaderElement(name) : getTableHeaderElement());
    }

    private int getColumnWidth(Element header) {
        return header.getCoordinates()[2];
    }

    /**
     * get element for column by name
     *
     * @param header - String name for column
     * @return - Element for column
     */
    public Element getColumnByTableHeader(String header) {
        String columnId = null;
        Element th = getTableHeaderElement(header);
        if (th == null) {
            PSLogger.warn("Can't find column '" + header + "'");
            return null;
        }
        columnId = th.getDEAttribute(GRID_TABLE_HEADER_COLUMN_ID);
        if (columnId == null) {
            PSLogger.warn("Can't find column id for '" + header + "'");
            return null;
        }
        Element res = Element.searchElementByXpath(this, GRID_TABLE_COLUMN.replace(columnId));
        if (res == null) {
            PSLogger.warn("Can't find column '" + header + "'");
        }
        return res;
    }

    public List<Link> getWorkLinks() {
        List<Link> res = new ArrayList<Link>();
        List<Element> cells = getTreeCells();
        for (Element cell : cells) {
            Element l = cell.getChildByXpath(TREE_ITEM_WORK_LINK);
            if (l == null || !l.isDEPresent()) continue;
            Link link = new Link(l);
            link.setName(l.getDEText());
            res.add(link);
        }
        return res;
    }

    public SummaryWorkPage openSummaryPage(String name) {
        Link link = null;
        for (Link l : getWorkLinks()) {
            if (name.equals(l.getName())) {
                link = l;
                break;
            }
        }
        if (link == null || !link.exists()) {
            PSLogger.warn("Can't find item '" + name + "' in tree on page");
            return null;
        }
        SummaryWorkPage res = SummaryWorkPage.getInstance();
        link.setResultPage(res);
        link.clickAndWaitNextPage();
        return res;
    }

    public static String getWorkNameByElement(Element e) {
        String link = e.getChildByXpath(TREE_ITEM_LINK).getDEText();
        if (link.isEmpty())
            link = e.getChildByXpath(TREE_ITEM_NOBR).getDEText();
        String parent = e.getDEText();
        if (!link.isEmpty() && !parent.isEmpty()) {
            return link + " " + parent;
        } else if (link.isEmpty()) {
            return parent;
        }
        return link;
    }

    public class EditableCellInput extends Element {
        private ILocatorable input;

        protected EditableCellInput(Element cell, ILocatorable input) {
            super(cell);
            this.input = input;
        }

        public EditableCellInput(Element cell) {
            this(cell, GRID_TABLE_NUMBER_INPUT);
        }

        public void set(String toSet) {
            click(false);
            new Element(input).waitForPresent();
            SimpleGrid.this.setDefaultElement();
            List<Element> inputs = Element.searchElementsByXpath(SimpleGrid.this.getParentDoc(), input);
            Input in = new Input(inputs.get(inputs.size() - 1));
            in.waitForVisible();
            in.type(toSet);
            SimpleGrid.this.mouseMoveAt(0, 0);
            SimpleGrid.this.mouseDownAndUp();
            try {
                in.waitForUnvisible();
            } catch (Wait.WaitTimedOutException ww) {
                Assert.fail("The input field is still visible");
            }
            waitWhileChangesIsApplied();
        }
    }

    protected class GridInput extends EditableCellInput {
        private Work work;
        private String name;
        private Element cell;

        public GridInput(Work work, ILocatorable name, ILocatorable input) {
            this(getCellByName(name, work), work, name.getLocator(), input);
        }


        public GridInput(Element cell, Work work, String name, ILocatorable input) {
            super(cell, input);
            this.cell = cell;
            this.work = work;
            this.name = name;
        }

        public GridInput(Element cell, Work work, String name) {
            this(cell, work, name, GRID_TABLE_NUMBER_INPUT);
        }

        public GridInput(Work work, ILocatorable name, ILocatorable input, ILocatorable parentDiv) {
            this(work, name, input);
        }

        public void set(String toSet) {
            PSLogger.info("Set '" + toSet + "'" + (work != null ? " for work " + work.getName() : "") + (cell != null ? " to cell " + cell : ""));
            if (!isCellEditable(this)) {
                PSLogger.warn("Can't set " + name + " for " + work + ": cell is not editable");
                return;
            }
            super.set(toSet);
        }
    }


    public class WBSDatePicker extends DatePicker {
        private Element cell;
        private Work work;

        public WBSDatePicker(Element cell) {
            super(cell);
            this.cell = cell;
        }

        public WBSDatePicker(Element cell, Work work) {
            this(cell);
            this.work = work;
        }


        public void set(String date) {
            PSLogger.info("Set date " + date + (work != null ? " for work " + work.getName() : "") + " to cell " + cell);
            click(false);
            getPopup().waitForVisible();
            if (getUseDatePopup()) {
                PSLogger.debug("use popup to set");
                setUsingPopup(date);
                SimpleGrid.this.setDefaultElement(getParentDoc());
            } else {
                SimpleGrid.this.setDefaultElement();
                PSLogger.debug("use input to set");
                setUsingInput(date);
            }
            SimpleGrid.this.mouseMoveAt(0, 0);
            SimpleGrid.this.mouseDownAndUp();
            waitWhileChangesIsApplied();
            PSLogger.debug("After set date " + date + " in grid");
        }

        public Element getInput() {
            List<Element> inputs = Element.searchElementsByXpath(SimpleGrid.this.getParentDoc(), GRID_TABLE_DATE_PICKER_INPUT);
            return inputs.get(inputs.size() - 1);
        }

        public ADatePickerPopup openPopup() {
            return new DatePickerPopup(getPopup(), useDropDownOrArrow);
        }

        public String get() {
            String res = getTextContent(cell, WBSDatePicker.class);
            if (res != null) {
                return res;
            }
            return super.get();
        }
    }

    public static String getTextContent(Element cell, Class clazz) {
        if (!cell.isDEPresent()) return null;
        PSLogger.debug("Get content for " + clazz.getSimpleName() + "(" + cell.initialLocator + ")");
        return StrUtil.replaceSpaces(cell.getDEInnerText()).trim();
    }

    public class ApplyButton extends Button {
        private ApplyButton(GridLocators loc) {
            super(loc);
        }

        public boolean isEnabled() {
            return exists() && !getElementClass().contains(AREA_SAVE_BUTTON_DISABLED.getLocator());
        }

        public void waitForEnabled() {
            try {
                waitForClassChanged(AREA_SAVE_BUTTON_DISABLED);
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.error(ww); // ignore and wait another fault situation?
            }
        }

        public void submit() {
            click(false);
            SimpleGrid.this.waitForVisible(true);
        }
    }

    public ApplyButton getSaveButton() {
        return new ApplyButton(AREA_SAVE_BUTTON);
    }

    public ApplyButton getResetButton() {
        return new ApplyButton(AREA_RESET_BUTTON);
    }

    public void checkForPopupError() {
        try {
            checkForPopupError(true);
        } catch (Throwable t) {
            PSLogger.save("checkForPopupError");
            PSLogger.fatal(t);
        }
    }

    private void checkForPopupError(boolean setDoc) {
        PSLogger.debug("checkForPopupError " + setDoc);
        if (setDoc) {
            JS_WAITER.waitTime();
            setDefaultElement();
        }
        Assert.assertTrue(isDEPresent(), "Can't find grid");

        Element dialog = null;
        for (Element d : Element.searchElementsByXpath(getParentDoc(), PROBLEM_DETECTED_DIALOG)) {
            Element title = Element.searchElementByXpath(d, PROBLEM_DETECTED_DIALOG_TITLE);
            if (title != null && title.getDEText().equalsIgnoreCase(PROBLEM_DETECTED_DIALOG_TITLE_TXT.getLocator())) {
                dialog = d;
                break;
            }
        }
        if (dialog == null || !dialog.exists() || !dialog.isVisible()) {
            PSLogger.debug("No problem dialog found");
            return;
        }
        Element details = Element.searchElementByXpath(dialog, PROBLEM_DETECTED_DIALOG_DETAIL_LINK);
        if (details != null && details.exists())
            details.click(false);
        StringBuffer error = new StringBuffer();
        PSApplicationException ex = new PSApplicationException();
        for (Element content : Element.searchElementsByXpath(dialog, PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT)) {
            error.append(content.getDEText()).append("\n");
            for (Element ch : Element.searchElementsByXpath(content, PROBLEM_DETECTED_DIALOG_DETAIL_CONTENTS)) {
                String txt = ch.getDEText();
                String[] trace;
                if (PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT_BLOCK.getLocator().equalsIgnoreCase(ch.getDENode()) &&
                        (trace = txt.split(PROBLEM_DETECTED_DIALOG_DETAIL_CONTENT_BLOCK_SEPARATOR.getLocator())).length > 1) {
                    int index = 0;
                    if (!PSApplicationException.isStackTraceMsg(trace[0])) {
                        ex.setMessage(trace[0].trim());
                        index = 1;
                    }
                    for (int i = index; i < trace.length; i++) {
                        String stt = trace[i].trim();
                        error.append(stt).append("\n");
                        ex.addStackTrace(stt);
                    }
                } else {
                    error.append(txt).append("\n");
                    ex.addStackTrace(txt);
                }
            }
        }
        if (!ex.hasOwnStackTrace()) { // js error:
            ex = new PSApplicationException(error.toString());
        }
        PSKnownIssueException ex2 = ex.toKnownIssueException(CoreProperties.getKnownPopupExceptions());
        if (ex2 != null) throw ex2;
        PSLogger.error(error);

        if (!LocalServerUtils.ping5(CoreProperties.getApplicationServerHost())) {
            PSSkipException.skip("Server is not pingable.");
        }
        Assert.fail(PROBLEM_MESSAGE_PREFIX + " (" + (ex.hasOwnStackTrace() ? "exception" : "js-error") + ") on page");
    }

}
