package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.MetricInstancePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.MetricInstancePageLocators.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 28.04.12
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class MetricInstancePage extends AbstractWorkPage {
    private static final TimerWaiter EDIT_PROP_WAITER = new TimerWaiter(2000);

    private static final long GRID_WAITER_TIMEOUT = CoreProperties.getWaitForElementToLoad();
    private static final TimerWaiter BENEFICIARY_GRID_INPUT_TIMEOUT = new TimerWaiter(30 * 1000);
    private static final int BENEFICIARY_GRID_INPUT_ITERATIONS = 3;
    private static final TimerWaiter CELL_WAITER = new TimerWaiter(40 * 1000);

    private String id;

    private Grid grid;

    public MetricInstancePage(String id) {
        this();
        this.id = id;
    }

    protected MetricInstancePage() {
        grid = null;
    }

    public void init() {
        getGrid().setDefaultElement(getDocument());
    }

    protected void setId() {
        id = getUrlId();
    }

    public Grid getGrid() {
        if (grid != null) return grid;
        return grid = new Grid();
    }

    public void waitForPageToLoad() {
        super.waitForPageToLoad();
        init();
    }

    public String getId() {
        return id;
    }

    public void open() {
        if (id == null) throw new NullPointerException("Metric id is null");
        PSLogger.info("Open instance with id " + id);
        super.open(URL_ID.replace(id));
        init();
        PSLogger.save("After open page");
    }

    public EditProperties edit() {
        new Link(EDIT_PROPERTIES_LINK).click(false);
        EditProperties dialog = new EditProperties();
        dialog.waitForVisible();
        return dialog;
    }


    public EditComments editComments() {
        new Link(EDIT_COMMENTS_LINK).click(false);
        EditComments dialog = new EditComments();
        dialog.waitForVisible();
        return dialog;
    }

    public String getComments() {
        return StrUtil.trim(new Element(EDIT_COMMENTS_TXT).getText());
    }

    public String getCommentsUser() {
        Element div = Element.searchElementByXpath(getDocument(false), EDIT_COMMENTS_LINK).getParent("div");
        Element link = div.getChildByXpath(EDIT_COMMENTS_USER_LINK);
        if (!link.isDEPresent()) return null;
        return StrUtil.trim(link.getText());
    }


    /**
     * example:
     * String id = "o2k053g0000jdt2h3pm0000000";
     * MetricInstancePage page = new MetricInstancePage(id);
     * page.open();
     * MetricInstancePage.EditProperties dialog = page.edit();
     * dialog.openTagBreakdownTab();
     * TagChooser td = dialog.getTagBreakdownSelector();
     * PSLogger.info(td.getContent());
     * td.openPopup();
     * List<String> labels = td.getAllLabels();
     * td.cancel();
     * dialog.close();
     */
    public class EditProperties extends Dialog {

        private EditProperties() {
            super(EDIT_PROPERTIES_DIALOG);
            setPopup(EDIT_PROPERTIES_DIALOG);
        }

        public TagsBreakdownTab openTagBreakdownTab() {
            TagsBreakdownTab res = new TagsBreakdownTab();
            res.open();
            return res;
        }

        public PropertiesTab openPropertiesTab() {
            PropertiesTab res = new PropertiesTab();
            res.open();
            return res;
        }


        public void update() {
            PSLogger.save("Before update");
            Button bt = new Button(EDIT_PROPERTIES_DIALOG_UPDATE);
            bt.waitForVisible();
            bt.click(false);
            waitForPageToLoad();
        }

        public class PropertiesTab extends Tab {
            private PropertiesTab() {
                super(EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES.getLocator());
                isOpened = true; // by default opened
            }

            public void lock(boolean yes) {
                new CheckBox(EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES_LOCK).select(yes);
            }

            public boolean getLock() {
                return new CheckBox(EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES_LOCK).getChecked();
            }


            public void readyForRollup(boolean yes) {
                new CheckBox(EDIT_PROPERTIES_DIALOG_TAB_PROPERTIES_READY_FOR_ROLLUP).select(yes);
            }

        }


        public class TagsBreakdownTab extends Tab {

            private TagsBreakdownTab() {
                super(EDIT_PROPERTIES_DIALOG_TAB_TAGS_BREAKDOWN.getLocator());
            }

            private TagChooser getTagBreakdownSelector(boolean hierarchical) {
                ILocatorable loc = EDIT_PROPERTIES_DIALOG_TAB_TAGS_BREAKDOWN_SELECTOR;
                return hierarchical ? new HierarchicalTagChooser(loc) : new TagChooser(loc);
            }

            private Grid getGrid() {
                if (TestSession.getAppVersion().verLessOrEqual(PowerSteeringVersions._9_2)) return null;
                return new Grid();
            }

            public void setTag(MetricInstance.MetricTag tag) {
                PSLogger.debug("Set breakdown tag " + tag);
                if (tag.isPercentageAllocation()) {
                    getGrid().setTag(tag);
                    return;
                }
                List<PSTag> tags = tag.getAllChildren();
                String[] labels = new String[tags.size()];
                for (int i = 0; i < tags.size(); i++) {
                    labels[i] = tags.get(i).getName();
                }
                getTagBreakdownSelector(tag.isHierarchical()).setLabel(labels);
            }

            public class Grid extends Element {
                private List<Element> rows = new ArrayList<Element>();

                private Grid() {
                    super(BENEFICIARY_GRID);
                    setDefaultElement();
                }

                public void setTag(MetricInstance.MetricTag tag) {
                    int sum = 0;
                    for (PSTag tg : tag.getAllChildren()) {
                        int p = ((MetricInstance.MetricTag) tg).getPercent();
                        String name = tg.getName();
                        PSLogger.debug("Set tag " + tg + ", percentage " + p);
                        getSelector(tg.isHierarchical()).setLabel(name);
                        getNumberBox().type(String.valueOf(p));

                        sum += p;
                        if (sum != 100)
                            waitNextRow();
                        PSLogger.save("after setting " + tg + ", " + p);
                        setDefaultElement();
                        String sSum = StrUtil.trim(getChildByXpath(BENEFICIARY_GRID_SUM).getDEText()).replace("%", "");
                        Assert.assertEquals(sSum, String.valueOf(sum), "Incorrect sum after setting tag " + name + "," + p);
                    }
                }

                private void waitNextRow() {
                    Wait.WaitTimedOutException we = null;
                    Element newRow = new Element(BENEFICIARY_GRID_ROW_NEW);
                    for (int i = 0; i < BENEFICIARY_GRID_INPUT_ITERATIONS; i++) {
                        try {
                            newRow.waitForVisible(BENEFICIARY_GRID_INPUT_TIMEOUT);
                            return;
                        } catch (Wait.WaitTimedOutException _we) {
                            PSLogger.warn("New Row: " + (we = _we));
                            mouseDownAndUp(); // for ie9
                            if (i != 0) click(false);
                        }
                    }
                    if (we != null) throw we;
                }

                private TagChooser getSelector(boolean hierarchical) {
                    int index = rows.size() - 1;
                    Element cell = Element.searchElementByXpath(rows.get(index), BENEFICIARY_GRID_ROW_SELECTOR);
                    return hierarchical ? new HierarchicalTagChooser(cell) : new TagChooser(cell);
                }

                private Input getNumberBox() {
                    int index = rows.size() - 1;
                    final Element cell = Element.searchElementByXpath(rows.get(index), BENEFICIARY_GRID_ROW_NUMBER_BOX);
                    return new Input(BENEFICIARY_GRID_INPUT) {
                        public void type(String txt) {
                            PSLogger.info("Set " + txt + " to numberbox");
                            cell.click(false);
                            super.type(txt);
                            EDIT_PROP_WAITER.waitTime(); // to debug.
                            Wait.WaitTimedOutException we = null;
                            for (int i = 0; i < BENEFICIARY_GRID_INPUT_ITERATIONS; i++) {
                                try {
                                    Grid.this.mouseDownAndUp();
                                    //Grid.this.click(false);
                                    waitForUnvisible(BENEFICIARY_GRID_INPUT_TIMEOUT);
                                    new TimerWaiter(500).waitTime();
                                    return;
                                } catch (Wait.WaitTimedOutException w) {
                                    PSLogger.warn("NumberBox.type: " + (we = w));
                                }
                            }
                            if (we != null) throw we;
                        }
                    };
                }

                public void setDefaultElement() {
                    super.setDefaultElement(MetricInstancePage.this.getDocument());
                    Assert.assertTrue(isDEPresent(), "Cannot find element " + this);
                    rows.clear();
                    rows.addAll(Element.searchElementsByXpath(this, BENEFICIARY_GRID_ROW));
                }
            }
        }

        private class Tab extends Element {
            private String name;
            protected boolean isOpened;

            public Tab(String txt) {
                super(EDIT_PROPERTIES_DIALOG_TAB.replace(txt));
                name = txt;
            }

            public void open() {
                if (isOpened) return;
                PSLogger.debug("Open tab '" + name + "'");
                click(false); // todo
                new TimerWaiter(500).waitTime();
                isOpened = true;
            }
        }


    }

    public void openView(String name) {
        Element active = new Element(VIEW_ACTIVE);
        if (name == null || name.equals(MetricInstance.SINGLE_VIEW)) {
            Assert.assertFalse(active.exists(), "There are views on page");
            PSLogger.debug("Skipp opening view");
            return;
        }
        PSLogger.info("Open view " + name);
        Link link = new Link(VIEW_INACTIVE.replace(name));
        if (link.exists()) {
            link.click(false);
            waitForPageToLoad();
            return;
        }
        if (active.exists() && StrUtil.trim(active.getText()).equals(name)) {
            PSLogger.warn("View " + name + " is active");
            return;
        }
        Assert.fail("Can't find view '" + name + "'");
    }


    public class Grid extends Element {
        private List<Row> rows = new ArrayList<Row>();
        private List<Header> months = new ArrayList<Header>();
        private List<Row.Cell> inputs = new ArrayList<Row.Cell>();

        public void setDefaultElement(Document doc) {
            setDefaultElement(doc, true);
        }

        public void setDefaultElement(Document doc, boolean clearInputs) {
            super.setDefaultElement(doc);

            initRows();
            initHeader();
            initCells();
            initSubRows();
            if (clearInputs) inputs.clear();
            else initInputs();
        }

        public boolean save() {
            PSLogger.info("Save");
            Button bt = new Button(GRID_SAVE);
            bt.click(false);
            try {
                pleaseWait();
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.save("Error occurred");
                return false;
            }
            waitForPageToLoad();
            PSLogger.save("After saving");
            return true;
        }

        public void reset() {
            PSLogger.info("Reset");
            Button bt = new Button(GRID_RESET);
            bt.click(false);
            waitForPageToLoad();
        }

        public List<Row> getRows() {
            return rows;
        }

        public Row getRow(String name) {
            for (Row row : rows) {
                if (name.equals(row.name)) return row;
            }
            return null;
        }

        public List<Header> getHeader() {
            return months;
        }


        private void initRows() {
            rows.clear();
            Element column = getChildByXpath(GRID_NAME_COLUMN);
            if (column == null || !column.isDEPresent()) {
                PSLogger.error("Can't find row header");
                return;
            }
            for (Element e : Element.searchElementsByXpath(column, GRID_ROWS)) {
                rows.add(new Row(e));
            }
        }

        @Override
        public Element getChildByXpath(ILocatorable loc) {
            Element res = super.getChildByXpath(loc);
            if (res.isDEPresent()) return res;
            Assert.fail("Can't find " + loc.getLocator());
            return null;
        }

        private void initHeader() {
            months.clear();
            Element header = getChildByXpath(GRID_HEADER);
            if (header == null || !header.isDEPresent()) {
                PSLogger.error("Can't find grid header.");
                return;
            }
            List<String> ths = new ArrayList<String>();
            for (Element e : Element.searchElementsByXpath(header, GRID_HEADER_MONTH)) {
                String txt = StrUtil.trim(e.getDEText());
                ths.add(txt);
            }
            String year = ths.remove(0);
            for (int i = 0; i < ths.size(); i++) {
                Header h = new Header(i);
                h.setYear(year);
                h.setMonth(ths.get(i));
                months.add(h);
            }
        }

        private void initCells() {
            for (Row row : rows) {
                row.cells.clear();
                String id = row.id;
                Element eRow = Element.searchElementByXpath(getChildByXpath(GRID_MAIN), GRID_ROW.replace(id));
                for (Element e : Element.searchElementsByXpath(eRow, GRID_CELLS)) {
                    row.cells.add(row.new Cell(e));
                }
                for (int i = 0; i < months.size(); i++) {
                    Header header = months.get(i);
                    if (row.cells.size() > i) {
                        row.cells.get(i).column = header;
                    }
                }
            }
        }

        private void initSubRows() {
            List<Integer> parents = new ArrayList<Integer>();
            for (int i = 0; i < rows.size(); i++) {
                if (rows.get(i).expand != null) {
                    parents.add(i);
                }
            }
            for (int i = 0; i < parents.size(); i++) {
                int fromIndex = parents.get(i);
                int toIndex = i != parents.size() - 1 ? parents.get(i + 1) : rows.size();
                Row parentRow = rows.get(fromIndex);
                parentRow.subRows.clear();
                parentRow.subRows.addAll(rows.subList(fromIndex + 1, toIndex));
                for (Row sub : parentRow.subRows) {
                    sub.parent = parentRow;
                }
            }
        }

        private void initInputs() {
            if (inputs.size() == 0) return;
            for (Row row : rows) {
                for (Row.Cell cell : row.cells) {
                    int i = inputs.indexOf(cell);
                    if (i != -1) {
                        cell.input = inputs.get(i).input;
                    }
                }
            }
        }

        private Grid() {
            super(GRID);
        }

        public void pleaseWait(boolean setDoc) {
            Element div = new Element(MetricInstancePageLocators.GRID_WAITER);
            Throwable th = null;
            try {
                div.waitForUnvisible(GRID_WAITER_TIMEOUT);
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.warn(ww.getMessage() + ":" + div.getText());
                // seems error occurred
                th = ww;
            }
            if (setDoc || th != null) {
                setDefaultElement(getDocument(true));
            }
            if (th != null) {
                PSLogger.save("There is still loading element!");
                clickBody();
                PSLogger.debug(MetricInstancePage.this.getDocument(false).asXML());
            }

        }

        public void pleaseWait() {
            pleaseWait(false);
        }


        public class Header {
            private String year;
            private String month;
            private int index;

            private Header(int index) {
                this.index = index;
            }

            public void setYear(String y) {
                year = y;
            }

            public void setMonth(String m) {
                month = m;
            }

            public String toString() {
                return year != null && month != null ? year + " - " + month : String.valueOf(index);
            }

            public String getMonth() {
                return month;
            }

            public int getIndex() {
                return index;
            }
        }

        public class Row extends Element {
            private String name;
            private Boolean expand;
            private Img img;
            private String id;
            private List<Cell> cells = new ArrayList<Cell>();
            private List<Row> subRows = new ArrayList<Row>();
            private Row parent;
            private String title;

            public List<Cell> getCells() {
                return cells;
            }

            public List<Row> getRows() {
                return subRows;
            }

            public String getName() {
                return name;
            }

            public String getFullName() {
                String suff = title != null ? " (" + title + ")" : "";
                if (parent == null) return name + suff;
                return parent.getName() + " > " + name + suff;
            }

            public Row getRow(String name) {
                for (Row row : subRows) {
                    if (name.equals(row.name)) return row;
                }
                return null;
            }

            public boolean hasParent() {
                return parent != null;
            }

            public Row getParent() {
                return parent;
            }

            public Cell getCell(int month) {
                Cell res = _getCell(month);
                if (res == null) return null;
                if (res.relatedCells != null) return res;
                List<Cell> related = new ArrayList<Cell>();
                Row parentRow = parent == null ? this : parent;
                Cell c = parentRow._getCell(month);
                if (c != null && !res.equals(c)) related.add(c);
                for (Row sub : parentRow.subRows) {
                    Cell c1 = sub._getCell(month);
                    if (c1 != null && !res.equals(c1)) related.add(c1);
                }
                res.relatedCells = related;
                return res;
            }


            private Cell _getCell(int month) {
                if (month >= cells.size()) return null;
                return cells.get(month);
            }

            private Row(Element e) {
                super(e);
                id = e.getDEAttribute(GRID_ROW_ID);
                Element eExpand = e.getChildByXpath(GRID_ROW_EXPAND);
                Element acronym = e.getChildByXpath(GRID_ACRONYM);
                Element eTxt = acronym.isDEPresent() ? acronym : e.getChildByXpath(GRID_TXT);
                title = acronym.isDEPresent() ? acronym.getDEAttribute(GRID_ACRONYM_TITLE) : null;
                expand = eExpand.isDEPresent() ? (img = new Img(eExpand)).getSrc().contains(GRID_ROW_EXPAND_SRC.getLocator()) : null;
                name = StrUtil.trim(eTxt.getDEText());
            }

            private void doExpand(boolean yes) {
                if (expand == null) {
                    PSLogger.warn("Can't expand/collapse row " + name);
                    return;
                }
                if (expand ^ yes) {
                    PSLogger.debug("Row " + name + " is already " + (yes ? "expanded" : "collapsed"));
                    return;
                }
                PSLogger.info((yes ? "Expand" : "Collapse") + " row " + name);
                img.click(false);
                if (expand)
                    img.waitForSrc(GRID_ROW_COLLAPSE_SRC);
                else
                    img.waitForSrc(GRID_ROW_EXPAND_SRC);
                expand = !expand;
                PSLogger.save("After expand\\collapse");
            }

            public void expand() {
                doExpand(true);
            }

            public void collapse() {
                doExpand(false);
            }

            public String getTitle() {
                return title;
            }

            public class Cell extends Element {
                private Boolean editable;
                private boolean edited;
                private Header column;
                private String id;
                private String txt;
                private Input input;
                private List<Cell> relatedCells;

                public Header getColumn() {
                    return column;
                }

                private Cell(Element e) {
                    super(e);
                    id = getDEAttribute(GRID_CELL_ID);
                    txt = StrUtil.trim(e.getChildByXpath(GRID_TXT).getDEText());
                    String clazz = e.getDEClass();
                    editable = clazz != null && !clazz.contains(GRID_CELL_NOT_EDITABLE.getLocator()) && clazz.contains(GRID_CELL_EDITABLE.getLocator());
                }

                public String toString() {
                    return "[" + getFullName() + ", " + column + "]";
                }

                public boolean equals(Object o) {
                    return o != null && toString().equals(o.toString());
                }

                public String getTxt() {
                    if (txt != null) return txt;   // ??
                    return StrUtil.replaceNumberSpaces(getChildByXpath(GRID_TXT).getText());
                }

                public void setTxt(String txt) {
                    this.txt = null; // ??
                    if (relatedCells != null) {
                        for (Cell c : relatedCells) {
                            c.txt = null;
                        }
                    }
                    if (txt == null) return;
                    PSLogger.info("Set text '" + txt + "' for cell " + this);
                    try {
                        _setTxt(txt);
                    } catch (Wait.WaitTimedOutException ww) {
                        PSLogger.error("getValue:" + ww.getMessage());
                        clickBody();
                        _setTxt(txt);
                    }
                }

                private void _setTxt(String txt) {
                    Input in = callInput();
                    in.type(txt);
                    closeInput(true);
                }

                private Input callInput() {
                    Assert.assertNotNull(editable, "Can't set txt " + txt + " to cell " + this + ": not editable");
                    PSLogger.debug("Activate cell " + this);
                    click(false);

                    if (input == null) {
                        Grid.this.setDefaultElement(getDocument(true), false);
                        List<Element> inputs = Element.searchElementsByXpath(Grid.this, GRID_INPUT);
                        if (inputs.size() == 0)
                            input = new Input(GRID_INPUT);
                        else
                            input = new Input(inputs.get(inputs.size() - 1));
                    }

                    PSLogger.debug2(inputs);
                    input.waitForVisible(CELL_WAITER);
                    if (!input.isDEPresent()) {
                        Grid.this.setDefaultElement(getDocument(true), false);
                    }
                    Grid.this.inputs.add(this);
                    return input;
                }

                private void closeInput(boolean doWait) {
                    PSLogger.debug("Deactivate cell " + this);
                    Grid.this.mouseMoveAt(0, 0);
                    Grid.this.mouseDownAndUp();
                    TimerWaiter.waitTime(200);
                    if (input != null)
                        input.waitForUnvisible(CELL_WAITER);
                    if (doWait)
                        pleaseWait();
                }

                private String _getValue() {
                    Input in = callInput();
                    String res = in.getValue();
                    closeInput(false);
                    return res;
                }

                public String getValue() {
                    try {
                        return _getValue();
                    } catch (Wait.WaitTimedOutException ww) {
                        PSLogger.error("getValue:" + ww.getMessage());
                        clickBody();
                        return _getValue();
                    }
                }
            }

            public String toString() {
                return name + "(" + id + ")(" + expand + ")" + (title != null ? "(formula: '" + title + "')" : "");
            }

        }
    }

    public class EditComments extends Dialog {

        private EditComments() {
            super(EDIT_COMMENTS_DIALOG);
            setPopup(EDIT_COMMENTS_DIALOG);
        }

        public void setText(String txt) {
            new TextArea(EDIT_COMMENTS_TEXTAREA).setText(txt);
        }

        public void ok() {
            new Button(EDIT_COMMENTS_OK).click(false);
            waitForUnvisible();
        }

        public void cancel() {
            new Button(EDIT_COMMENTS_CANCEL).click(false);
            waitForUnvisible();
        }
    }

    public TemplatePage info() {
        Link link = new Link(INFO);
        //link.clickAndWaitNextPage();
        link.openByHref();
        TemplatePage res = new TemplatePage(null);
        res.setId(); // template id

        return res;
    }

    public String getUrlId() {
        if (id != null) return id;
        return id = super._getUrlId();
    }

}
