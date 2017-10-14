package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.BuiltInRole;
import com.powersteeringsoftware.libs.objects.Role;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.ResourceReviewPageLocators.*;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 24.02.11
 * Time: 19:22
 */
public class ResourceReviewPage extends PSPage implements FiltersGridPage {

    private Grid table;

    public void open() {
        clickResourceReview();
        waitWhileLoading();
        setUrl();
        table = new Grid();
    }

    public GeneralActions getGeneralActions() {
        return new GeneralActions(this);
    }

    public void waitWhileLoading() {
        getGeneralActions().waitWhileLoading(false);
        try {
            new Element(GRID_TREE_ALLOCATION).waitForVisible(JS_WAITER);
            new Element(GRID_TREE_DEMAND).waitForVisible(JS_WAITER);
            getGrid().waitForVisible();
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.debug(w.getMessage());
        }
        getDocument();
    }

    public SimpleGrid getGrid() {
        return new SimpleGrid();
    }

    public void refresh() {
        super.refresh();
        waitWhileLoading();
    }

    public void goBack() {
        super.goBack();
        waitWhileLoading();
    }

    @Override
    public Element getContainerHeaderElement() {
        return new Element(CONTAINER_HEADER);
    }

    public OptionsBlock getOptions() {
        return new OptionsBlock();
    }

    public Work makeTreeByShowLevel() {
        getOptions().showLevel(8);
        Work res = loadTree(getDocument());
        table.setDefaultElement(document);
        return res;
    }

    public Work getTree() {
        return loadTree(document);
    }

    public Work getWorkRow(Work work, Role role, User user) {
        Work w0 = getQuasiRootWork();
        Work w1;
        Work w2 = (Work) work.clone();
        Work w3 = (Work) work.clone();
        if (BuiltInRole.OWNER.equals(role)) {
            w1 = new Work(role.getName());
        } else {
            w1 = new Work(user.getFullName());
            w3.setName(w3.getName() + " - " + role.getName());
        }
        w0.addChild(w1);
        w1.addChild(w2);
        w2.addChild(w3);
        for (Work w : getTree().getAllChildren()) {
            if (w.getFullName().equals(w3.getFullName())) {
                return w;
            }
        }
        PSLogger.warn("Can't find work " + w3.getFullName());
        return null;
    }

    public static Work getQuasiRootWork() {
        return new Work(GRID.name());
    }

    public static Work getQuasiWorkTree(Work work) {
        Work res = getQuasiRootWork();
        List<Work> chs = new ArrayList<Work>();
        for (Work resource : work.getChildren()) {
            String role = resource.getResource().getRole().getName();
            String person = null;
            if (resource.getResource().getPerson() != null) {
                person = resource.getResource().getPerson().getFullName();
            }
            Work firstLevelWork = new Work(person == null ? role : person);
            if (person != null) {
                firstLevelWork.getResource().setPerson(resource.getResource().getPerson());
            }
            Work secondLevelWork = (Work) work.clone();
            Work thirdLevelWork = new Work(secondLevelWork.getName() + (person == null ? "" : " - " + role));
            firstLevelWork.addChild(secondLevelWork);
            secondLevelWork.addChild(thirdLevelWork);
            chs.add(firstLevelWork);
        }
        Collections.sort(chs, new Comparator<Work>() {
            @Override
            public int compare(Work o1, Work o2) {
                if (o1.getResource().getPerson() == null) {
                    if (o2.getResource().getPerson() == null) return 0;
                    return 1;
                }
                if (o2.getResource().getPerson() == null) return -1;
                return o1.getResource().getPerson().getFullName().compareTo(o2.getResource().getPerson().getFullName());
            }
        });
        res.addChildren(chs);
        return res;
    }

    private Work loadTree(Document doc) {
        Work parent = getQuasiRootWork();
        for (Element e : Element.searchElementsByXpath(doc, TREE_ITEM_PARENT)) {
            parent.addChild(loadTree(e));
        }
        int index = 0;
        for (Work ch : parent.getAllChildren()) {
            ch.setRowIndex(++index);
        }
        return parent;
    }

    private Work loadTree(Element parent) {
        Work res = getChildWorkByElement(parent);
        Element childrenBox = parent.getChildByXpath(TREE_ITEM_CHILDREN_BOX);
        if (childrenBox.isDEPresent()) {
            for (Element e : Element.searchElementsByXpath(childrenBox, TREE_ITEM)) {
                Work ch = loadTree(e);
                res.addChild(ch);
            }
        }
        return res;
    }

    private Work getChildWorkByElement(Element parent) {
        return new Work(SimpleGrid.getWorkNameByElement(parent.getChildByXpath(TREE_ITEM_CONTENT)));
    }


    public class OptionsBlock extends AbstractOptionsBlock {
        private Display display;
        private Filter filter;

        OptionsBlock() {
            super(ResourceReviewPage.this);
        }

        protected void submit(ILocatorable loc) {
            super.submit(loc);
            table.setDefaultElement(document);
        }

        protected void submit(ILocatorable loc, boolean save) {
            super.submit(loc, save);
            table.setDefaultElement(document);
        }

        public Display getRRDisplay() {
            display = new Display();
            super.open(DISPLAY_NAME);
            return display;
        }

        public Filter getRRFilter() {
            filter = new Filter();
            super.open(FILTER_NAME);
            return filter;
        }

        /**
         * @return Menu
         */
        public Menu getLayoutMenu() {
            Menu menu = new Menu(AbstractOptionsBlock.searchTopLink(document, LAYOUTS_NAME), true);
            menu.open();
            return menu;
        }


        public class Filter extends AbstractFilter {

            private Filter() {
                super(FILTER);
            }

            public void setAllocationRoles(String... roles) {
                setRole(2, FILTER_ALLOCATION_ROLE, FILTER_ALLOCATION_ROLE_TAG_CHOOSER, roles);
            }

            public void setDemandRoles(String... roles) {
                setRole(2, FILTER_DEMAND_ROLE, FILTER_DEMAND_ROLE_TAG_CHOOSER, roles);
            }

            private void setRole(int type,
                                 ILocatorable elementLocator,
                                 ILocatorable tagChooserLocator,
                                 String... roles) {
                ILocatorable optionValue = null;
                switch (type) {
                    case 0:
                        optionValue = FILTER_ROLE_EMPTY_VALUE;
                        break;
                    case 1:
                        optionValue = FILTER_ROLE_ANY_VALUE;
                        break;
                    case 2:
                        optionValue = FILTER_ROLE_SPECIFIED_VALUE;
                        break;
                    case 3:
                        break;
                    default:
                        PSLogger.warn("Incorrect type specified for selector in filter options");
                        return;
                }
                SelectInput select = new SelectInput(elementLocator);
                if (!optionValue.equals(FILTER_ROLE_SPECIFIED_VALUE)) {
                    select.select(optionValue);
                    return;
                }
                select.select(optionValue);
                StatusChooser tc = new StatusChooser(tagChooserLocator);
                tc.openPopup();
                tc.selectUnselect(false);
                for (String role : roles) {
                    tc.select(role);
                }
                tc.done();
                PSLogger.info("Selected: " + tc.getContent());
            }

        }

        public class Display extends AbstractFilter {

            public Display() {
                super(DISPLAY);
            }

            public void selectWeekly() {
                selectPeriod(DISPLAY_PERIOD_WEEKLY);
            }

            public void selectPeriod(ILocatorable loc) {
                ComboBox cb = new ComboBox(DISPLAY_PERIOD);
                cb.select(loc);
            }

            public void checkDateRange() {
                new RadioButton(DISPLAY_RANGE_DATE).select(true);
            }

            public void checkRollingRange() {
                new RadioButton(DISPLAY_RANGE_ROLLING).select(true);
            }

            public void setStartDate(String date) {
                processDatePicker(DISPLAY_RANGE_BEGIN, date, true, true);
            }

            public void setEndDate(String date) {
                processDatePicker(DISPLAY_RANGE_END, date, true, false);
            }

            private void processDatePicker(ILocatorable loc, String time, boolean popup, boolean arrow) {
                DatePicker dp = new DatePicker(loc);
                dp.useDatePopup(popup);
                dp.useDropDownOrArrows(arrow);
                dp.set(time);
            }

        }
    }


    /**
     * @return Demand Team Pane Table
     */
    public GridTable getDemandTable() {
        GridTable table = new GridTable(GRID_DEMAND);
        table.setDefaultElement(document);
        return table;
    }

    /**
     * @return Allocation Team Pane Table
     */
    public GridTable getAllocationTable() {
        GridTable table = new GridTable(GRID_ALLOCATION);
        table.setDefaultElement(document);
        return table;
    }

    public Grid getTable() {
        return table;
    }

    public class Grid {
        private GridTable allocationTable;
        private GridTable demandTable;

        private Grid() {
            allocationTable = new GridTable(GRID_ALLOCATION);
            demandTable = new GridTable(GRID_DEMAND);
            setDefaultElement(getDocument());
        }

        private void setDefaultElement(Document doc) {
            allocationTable.setDefaultElement(doc);
            demandTable.setDefaultElement(doc);
        }

        public List<TeamPaneCell> getColumnContent(PSCalendar calendar) {
            List<TeamPaneCell> res = allocationTable.getColumnContent(calendar);
            res.addAll(demandTable.getColumnContent(calendar));
            return res;
        }

        /**
         * @param calendar
         * @return Cell for allocation column
         */
        public SimpleGrid.EditableCellInput getAllocationCell(PSCalendar calendar, Work work) {
            return getCell(calendar, work, 0);
        }

        /**
         * @param calendar
         * @return Cell for allocation column
         */
        public SimpleGrid.EditableCellInput getAllocationCell(PSCalendar calendar, Work work, User user, Role role) {
            return getCell(calendar, getWorkRow(work, role, user), 0);
        }

        /**
         * @param calendar
         * @return Cell for allocation column
         */
        private SimpleGrid.EditableCellInput getCell(PSCalendar calendar, Work work, int column) {
            int index = work.getRowIndex() - 1;
            List<AbstractGanttSection.CellElement> cells1 = allocationTable.searchWeekColumns(calendar)[column].getContent();
            if (cells1.size() > index) {
                return getGrid().new EditableCellInput(cells1.get(index));
            }
            List<AbstractGanttSection.CellElement> cells2 = demandTable.searchWeekColumns(calendar)[column].getContent();
            if (cells1.size() + cells2.size() > index) {
                return getGrid().new EditableCellInput(cells2.get(index - cells1.size()));
            }
            return null;
        }

    }

    public class GridTable extends TeamPaneTable {

        public GridTable(ILocatorable loc) {
            super(loc, ResourceReviewPage.this);
        }

        public Column[] searchWeekColumns(PSCalendar week) {
            return super.searchWeekColumns(week.set(6));
        }

        public HorizontalScrollBar getScroll() {
            return new HorizontalScrollBar(GRID_SCROLL_BAR);
        }

        /*
                public Element getCell(PSCalendar period, Work work) {
                    String absoluteParent = getQuasiRootWork().getName();
                    Work parent = work.getParent();
                    int parentIndex = 0;
                    do {
                        parentIndex = parent.getRowIndex();
                        parent = parent.getParent();
                    } while (!parent.getName().equals(absoluteParent));
                    int index = work.getRowIndex() - parentIndex;
                    return getCell(period, index);
                }
        */

    }

    public static String toGridFormat(float a) {
        if (a == 0) return "";
        return TeamPaneCell.getNumberFormatter(Boolean.parseBoolean(GRID_NUMBER_FORMAT_GROUPING_USED.getLocator()),
                GRID_NUMBER_FORMAT_MAX_FRACTION_DIGITS.getInt(),
                GRID_NUMBER_FORMAT_MIN_FRACTION_DIGITS.getInt()).format(a);
    }


}
