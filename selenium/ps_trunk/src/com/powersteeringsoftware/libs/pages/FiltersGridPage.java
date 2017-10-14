package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.servers.RemoteLinServerUtils;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.powersteeringsoftware.libs.enums.page_locators.WBSEPageLocators.*;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._11_0;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._9_0;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 25.10.11
 * Time: 14:14
 */
public interface FiltersGridPage {

    TimerWaiter JS_WAITER = new TimerWaiter(2000); // for js

    void waitWhileLoading();

    SimpleGrid getGrid();

    Element getContainerHeaderElement();

    AbstractOptionsBlock getOptions();

    void refresh();

    void goBack();

    GeneralActions getGeneralActions();

    static abstract class AbstractOptionsBlock extends Element {
        private PSPage page;

        AbstractOptionsBlock(PSPage page) {
            super(OPTIONS_BLOCK_DIV);
            this.page = page;
        }

        public boolean isOpened() {
            return isVisible();
        }

        public void waitForVisible() {
            super.waitForVisible();
            setDefaultElement();
        }

        public void setDefaultElement() {
            setDefaultElement(page.getDocument());
        }

        @Deprecated // deprecated since 9.0
        public void apply() {
            PSLogger.info("Apply changes in option block");
            PSLogger.save("Before applying block");
            submit(OPTIONS_BLOCK_APPLY);
        }

        @Deprecated // deprecated since 9.0
        public void reset() {
            PSLogger.info("Reset changes in option block");
            submit(OPTIONS_BLOCK_RESET);
        }

        private class SaveDialog extends SimpleDialog {
            public SaveDialog() {
                super(OPTIONS_BLOCK_APPLY_DIALOG_YES, OPTIONS_BLOCK_APPLY_DIALOG_NO, page);
            }
        }

        protected void submit(ILocatorable loc) {
            setDefaultElement(page.getDocument(false));
            getChildByXpath(loc).click(false);
            try {
                waitForUnvisible(30000);
            } catch (Wait.WaitTimedOutException ww) { // its for debug:
                PSLogger.warn("AbstractOptionsBlock.submit:" + ww.getMessage());
                PSLogger.save();
            }
            ((FiltersGridPage) page).waitWhileLoading(); // don't wait arrow
            page.checkJSError();
        }

        protected void submit(ILocatorable loc, boolean save) {
            getChildByXpath(loc).click(false);
            SaveDialog dialog = new SaveDialog();
            dialog.waitForVisible();
            if (save)
                dialog.yes();
            else
                dialog.no();
        }

        public abstract Menu getLayoutMenu();

        /**
         * todo: its for debug, hotfix method
         *
         * @param document
         * @param name
         * @return
         */
        static Element searchTopLink(Document document, ILocatorable name) {
            Element link = Element.searchElementByXpath(document, OPTIONS_BLOCK_GENERAL_LINK_2.replace(name));
            if (link == null) {
                PSLogger.warn("Can't find '" + name.getLocator() + "' link");
                for (Element ee : Element.searchElementsByXpath(document, OPTIONS_BLOCK_GENERAL_LINK_1)) {
                    if (ee.getDefaultElement().getText().equals(name.getLocator())) {
                        link = ee;
                        break;
                    }
                }
            }
            Assert.assertNotNull(link, "Can't find '" + name.getLocator() + "' link");
            return link;
        }

        protected AbstractOptionsBlock open(ILocatorable name) {
            Element link = searchTopLink(page.getDocument(false), name);
            if (isOpened()) {
                PSLogger.debug("'" + name.getLocator() + "' block is already opened");
                setDefaultElement();
                return this;
            }
            PSLogger.info("Open " + name.getLocator() + " block");
            link.click(false);
            waitForVisible();
            waitForAttribute(OPTIONS_BLOCK_DIV_STYLE, OPTIONS_BLOCK_DIV_STYLE_AUTO);
            PSLogger.debug(getAttribute(OPTIONS_BLOCK_DIV_STYLE));
            PSLogger.save("After open block '" + name.getLocator() + "'");
            return this;
        }

        protected abstract class AbstractFilter extends Element {

            protected AbstractFilter(ILocatorable locator) {
                super(locator);
            }

            public void apply() {
                PSLogger.info("Submit changes in Filters/Display");
                PSLogger.save("Before apply");
                submit(OPTIONS_BLOCK_APPLY);
            }

            public void reset() {
                PSLogger.info("Reset changes in Filters/Display");
                PSLogger.save("Before reset");
                submit(OPTIONS_BLOCK_RESET);
            }

            public void apply(boolean save) {
                PSLogger.info("Submit changes and " + (save ? "Save" : "Cancel"));
                submit(OPTIONS_BLOCK_APPLY, save);
            }

        }

        public void fullScreen() {
            if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) return; // unsupported now
            if (isFullScreen()) {
                PSLogger.debug("Screen is maximize");
                return;
            }
            getFullScreenImg().click(false);
            page.checkJSError();
            ((FiltersGridPage) page).getContainerHeaderElement().waitForUnvisible();
            getStandardScreenImg().waitForPresent();
            JS_WAITER.waitTime(); // its for ff and ps91
            PSLogger.save("After push full screen img");
            page.getDocument();
        }

        public void standardScreen() {
            if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) return; // unsupported now
            if (!isFullScreen()) {
                PSLogger.debug("Screen is standard");
                return;
            }
            getStandardScreenImg().click(false);
            ((FiltersGridPage) page).getContainerHeaderElement().waitForVisible();
            getFullScreenImg().waitForPresent();
            JS_WAITER.waitTime(); // its for ff and ps91
            PSLogger.save("After push standard screen img");
            page.getDocument();
        }

        public boolean isFullScreen() {
            if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) return true; // unsupported now
            return !((FiltersGridPage) page).getContainerHeaderElement().isVisible();
        }

        private Element getStandardScreenImg() {
            if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) return null; // unsupported now
            return new Element(STANDARD_SCREEN_IMG);
        }

        private Element getFullScreenImg() {
            if (TestSession.getAppVersion().verGreaterOrEqual(_11_0)) return null; // unsupported now
            return new Element(FULL_SCREEN_IMG);
        }

        public void showLevel(int num) {
            PSLogger.info("Show " + num + " level(s)");
            Element e = page.getElement(false, SHOW_LEVELS_IMG);
            if (e == null) {
                new Element(SHOW_LEVELS_IMG_2).waitForVisible();
                e = page.getElement(true, SHOW_LEVELS_IMG);
            }
            Menu menu = new Menu(e, true);
            menu.open();
            String label = menu.getMenuItems().get(PowerSteeringVersions._12.verLessOrEqual(getAppVersion()) ? num : num - 1);
            menu.call(label);
            if (page instanceof WBSPage && !((WBSPage) page).waitGrid) return;
            ((FiltersGridPage) page).getGrid().waitForVisible();
        }


        public void saveLayout(String name) {
            PSLogger.info("Save layout '" + name + "'");
            SaveLayoutDialog dialog = new SaveLayoutDialog(getLayoutMenu());
            dialog.open();
            dialog.set(name);
            dialog.submit();
        }

        public void overwriteLayout(String name) {
            PSLogger.info("Overwrite layout '" + name + "'");
            SaveLayoutDialog dialog = new SaveLayoutDialog(getLayoutMenu());
            dialog.open();
            dialog.overwrite(name);
            dialog.submit();
        }

        public void deleteLayout(String name) {
            PSLogger.info("Delete layout '" + name + "'");
            DeleteLayoutDialog dialog;
            Menu menu = getLayoutMenu();
            if (getAppVersion().verGreaterOrEqual(_9_0)) {
                dialog = new DeleteLayoutDialog90(menu);
            } else {
                dialog = new DeleteLayoutDialog82(menu);
            }
            dialog.open();
            dialog.select(name);
            dialog.submit();
        }

        public void deleteLayouts() {
            PSLogger.info("Delete all layouts");
            if (getAppVersion().verGreaterOrEqual(_9_0)) {
                DeleteLayoutDialog90 dialog;
                do {
                    dialog = new DeleteLayoutDialog90(getLayoutMenu());
                    dialog.open();
                    dialog.select();
                    dialog.submit();
                } while (dialog.getOptionsSize() > 0);
                return;
            }
            DeleteLayoutDialog82 dialog = new DeleteLayoutDialog82(getLayoutMenu());
            dialog.open();
            dialog.select();
            dialog.submit();
        }

        class SaveLayoutDialog extends Dialog {
            SaveLayoutDialog(Menu menu) {
                super(menu.getMenuItem(LAYOUTS_SAVE_LAYOUT));
                setPopup(SAVE_LAYOUT_DIALOG);
            }

            public void open() {
                super.open();
                PSLogger.save("After opening save lauout dialog");
            }

            void set(String name) {
                new Input(SAVE_LAYOUT_DIALOG_INPUT_NEW).type(name);
            }

            void overwrite(String name) {
                ComboBox cb = new ComboBox(SAVE_LAYOUT_DIALOG_COMBOBOX_EXISTING);
                if (cb.getElementClass().contains(SAVE_LAYOUT_DIALOG_COMBOBOX_EXISTING_DIABLED.getLocator())) {
                    Assert.fail("Can't find any layouts");
                }
                cb.select(name);
            }

            void submit() {
                PSLogger.save("Before submitting layout");
                new Button(SAVE_LAYOUT_DIALOG_SUBMIT).click(false);
                getPopup().waitForUnvisible();
                ((FiltersGridPage) page).getGrid().checkForPopupError();
            }
        }


        abstract class DeleteLayoutDialog extends Dialog {

            DeleteLayoutDialog(Menu menu) {
                super(menu.getMenuItem(LAYOUTS_DELETE_LAYOUT));
                setPopup(DELETE_LAYOUT_DIALOG);
            }

            public void open() {
                super.open();
                getPopup().setDefaultElement();
            }

            void submit() {
                PSLogger.save("Before submitting deletion");
                new Button(DELETE_LAYOUT_DIALOG_SUBMIT).click(false);
                getPopup().waitForUnvisible();
                ((FiltersGridPage) page).getGrid().checkForPopupError();
            }

            abstract void select(String name);
        }

        class DeleteLayoutDialog82 extends DeleteLayoutDialog {
            private List<CheckBox> checkboxes = new ArrayList<CheckBox>();

            DeleteLayoutDialog82(Menu menu) {
                super(menu);
            }

            public void open() {
                super.open();
                checkboxes.clear();
                for (Element e : Element.searchElementsByXpath(getPopup(), DELETE_LAYOUT_DIALOG_CHECKBOX)) {
                    CheckBox ch = new CheckBox(e);
                    ch.setName(e.getParent().getDefaultElement().getTextTrim());
                    checkboxes.add(ch);
                }
            }

            void select() {
                checkboxes.get(0).click();
            }

            void select(String name) {
                for (CheckBox ch : checkboxes) {
                    if (ch.getName().equals(name)) {
                        ch.click();
                        return;
                    }
                }
                PSLogger.warn("Can't find layout '" + name + "'");
            }
        }

        class DeleteLayoutDialog90 extends DeleteLayoutDialog {
            private ComboBox list;
            private List<String> options;

            DeleteLayoutDialog90(Menu menu) {
                super(menu);
            }

            public void open() {
                super.open();
                list = new ComboBox(getPopup().getChildByXpath(DELETE_LAYOUT_DIALOG_COMBOBOX));
            }

            void select() {
                list.open();
                options = list.getOptions();
                list.set(options.remove(0));
            }

            void select(String name) {
                list.open();
                options = list.getOptions();
                int index = options.indexOf(name);
                if (index == -1) {
                    PSLogger.warn("Can't find layout '" + name + "'");
                    return;
                }
                list.set(options.remove(index));
            }

            int getOptionsSize() {
                if (options == null) return -1;
                return options.size();
            }
        }


    }

    static class SimpleDialog extends Dialog {
        private ILocatorable yes;
        private ILocatorable no;
        private PSPage page;

        protected SimpleDialog(ILocatorable dialog, ILocatorable yes, ILocatorable no, PSPage page) {
            super(dialog);
            setPopup(dialog);
            this.yes = yes;
            this.no = no;
            this.page = page;
        }

        protected SimpleDialog(ILocatorable yes, ILocatorable no, PSPage page) {
            this(WBSEPageLocators.DIALOG, yes, no, page);
        }

        public void yes() {
            getChildByXpath(yes).click(false);
            waitForUnvisible();
            ((FiltersGridPage) page).waitWhileLoading();
            PSLogger.save("after submitting simple dialog");
        }

        public void no() {
            getChildByXpath(no).click(false);
            waitForUnvisible();
        }

    }


    static class GeneralActions {
        private PSPage page;
        private SimpleGrid grid;

        GeneralActions(PSPage page) {
            this.page = page;
            grid = ((FiltersGridPage) page).getGrid();
        }

        public void waitWhileLoading(boolean setDoc) {
            try {
                page.initJsErrorChecker();
                grid.waitForVisible(setDoc);
                page.setIsDocumentFresh();
            } catch (Wait.WaitTimedOutException e) {
                checkReload(e);
            }
            if (!LocalServerUtils.isDisplayEnabled() && SeleniumDriverFactory.getDriver().getType().isIE())
                return; // skip for local speedup
            page.checkJSError();
            page.checkForErrorMessagesFromTop(); // do not get dynamic messages
            page.initJsErrorChecker();
        }


        protected void checkReload(Wait.WaitTimedOutException e) {
            if (!WBSPage.isCheckReload()) throw e;
            PSLogger.warn(e.getMessage());
            try {
                PSLogger.debug("Server time: " + RemoteLinServerUtils.getServer().getDate());
            } catch (Exception ee) {
                PSLogger.warn("checkReload:" + ee);
            }

            if (LocalServerUtils.pingDB()) {
                throw new PSKnownIssueException(67691, e, true); // do not rerun testcase
            } else {
                throw new PSSkipException("Database is unavailable", e);
            }
        }

    } // end for GeneralActions


    static abstract class AbstractGanttSection extends Element {
        private static final int GANTT_SECTION_LEFT_INDENT = 10; //pxs
        protected PSPage page;

        protected AbstractGanttSection(ILocatorable locator, PSPage page) {
            super(locator);
            this.page = page;
        }

        protected AbstractGanttSection(Element e) {
            super(e);
        }

        public Element checkHeader() {
            Element res = getHeader();
            if (res.exists() && res.isVisible())
                return res;
            return null;
        }

        public Element getHeader() {
            return new Element(GRID_GANTT_HEADER);
        }

        public boolean isVisible() {
            return getDynamicElement().isVisible();
        }

        public void waitForVisible() {
            getDynamicElement().waitForVisible();
        }

        public void waitForUnvisible() {
            getDynamicElement().waitForUnvisible();
        }

        public abstract Element getDynamicElement();


        protected class CellElement extends Element {

            protected CellElement(Element e) {
                super(e);
            }

            public Integer[] getCoordinates() {
                Integer[] coords = getSuperCoordinates();
                if (getAppVersion().verGreaterOrEqual(_9_0)) {
                    coords[0] -= getScroll().getScroll();
                    PSLogger.debug(getLocator() + " : " + Arrays.toString(coords));
                }
                return coords;
            }

            protected Integer[] getSuperCoordinates() {
                return super.getCoordinates();
            }

            protected int getAbsoluteX() {
                int diff = super.getCoordinates()[0] - AbstractGanttSection.super.getCoordinates()[0];
                if (getAppVersion().verGreaterOrEqual(_9_0)) {
                    PSLogger.debug(getLocator() + " : " + diff);
                    return diff;
                }
                return diff + getScroll().getScroll();
            }
        }

        public HorizontalScrollBar getScroll() {
            return new HorizontalScrollBar(GRID_GANTT_SCROLL_BAR);
        }

        public void scrollTo(CellElement cell) {
            int to = cell.getAbsoluteX() - GANTT_SECTION_LEFT_INDENT;
            getScroll().doScroll(to);
            JS_WAITER.waitTime(); // debug waiting
            PSLogger.save("After scrolling to center");
            setDefaultElement(page.getDocument());
        }
    }

    static class TeamPaneCell {
        private Content allocation;
        private Content availability;
        private PSCalendar calendar;

        private final static String PERCENT = "%";

        public enum Color {
            ZERO,
            WHITE,
            YELLOW,
            RED,;

            public static Color get(String clazz) {
                if (clazz == null || clazz.isEmpty()) {
                    return ZERO;
                } else if (clazz.contains(TEAM_PANE_CELL_COLOR_RED.getLocator())) {
                    return RED;
                } else if (clazz.contains(TEAM_PANE_CELL_COLOR_YELLOW.getLocator())) {
                    return YELLOW;
                } else {
                    return WHITE;
                }
            }
        }

        private class Content {
            private Color color;
            private Object value;
            private boolean percentage;

            /**
             * constructor for actual cell
             *
             * @param txt
             * @param col
             */
            Content(String txt, Color col) {
                this.value = txt.replace(PERCENT, "");
                color = col != null ? col : Color.WHITE;
                percentage = txt.endsWith(PERCENT);
            }

            /**
             * constructor for expected cell
             *
             * @param value
             * @param col
             * @param percentage
             */
            Content(Float value, Color col, boolean percentage) {
                this.percentage = percentage;
                this.value = value;
                color = col != null ? col : Color.WHITE;
            }

            public String getValue() {
                if (value instanceof String) // for cell from page
                    return (String) value;
                // from our cells
                NumberFormat nf = percentage ? getPercentageFormatter() : getNotPercentageFormatter();
                return nf.format(value);
            }

            public String toString() {
                return getValue() + (percentage ? PERCENT : "") + "," + color;
            }

            public boolean equals(Content c) {
                return toString().equals(c.toString());
            }
        }

        public TeamPaneCell(float al, float av, boolean percentage) {
            allocation = new Content(al, null, percentage);
            availability = new Content(av, null, percentage);
        }

        private TeamPaneCell(Element al, Element av) {
            allocation = new Content(al.getDEText(), Color.get(al.getParent().getDefaultElement().attributeValue("class")));
            availability = new Content(av.getDEText(), Color.get(av.getParent().getDefaultElement().attributeValue("class")));
        }

        public Float getAllocation() {
            return Float.parseFloat(allocation.getValue());
        }

        public void setColor(Color c) {
            if (c == null) return;
            allocation.color = c;
            availability.color = c;
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            if (!(o instanceof TeamPaneCell)) return false;
            return allocation.equals(((TeamPaneCell) o).allocation) &&
                    availability.equals(((TeamPaneCell) o).availability);
        }

        public String toString() {
            return calendar + "=>[" + allocation.toString() + "," + availability.toString() + "]";
        }

        private static NumberFormat getNotPercentageFormatter() {
            return getNumberFormatter(TEAM_PANE_NUMBER_FORMAT_GROUPING_USED,
                    TEAM_PANE_NUMBER_FORMAT_MAX_FRACTION_DIGITS_HOURS,
                    TEAM_PANE_NUMBER_FORMAT_MIN_FRACTION_DIGITS);
        }

        private static NumberFormat getPercentageFormatter() {
            return getNumberFormatter(TEAM_PANE_NUMBER_FORMAT_GROUPING_USED,
                    TEAM_PANE_NUMBER_FORMAT_MAX_FRACTION_DIGITS_PERCENTS,
                    TEAM_PANE_NUMBER_FORMAT_MIN_FRACTION_DIGITS);
        }

        protected static NumberFormat getNumberFormatter(WBSEPageLocators groupingUsed,
                                                         WBSEPageLocators maxFractionDigits,
                                                         WBSEPageLocators minFractionDigits) {
            return getNumberFormatter(groupingUsed != null ? Boolean.parseBoolean(groupingUsed.getLocator()) : null,
                    maxFractionDigits != null ? maxFractionDigits.getInt() : null,
                    minFractionDigits != null ? minFractionDigits.getInt() : null);
        }

        protected static NumberFormat getNumberFormatter(Boolean groupingUsed,
                                                         Integer maxFractionDigits,
                                                         Integer minFractionDigits) {
            NumberFormat nf = NumberFormat.getInstance(Locale.ROOT);
            if (groupingUsed != null)
                nf.setGroupingUsed(groupingUsed);
            if (maxFractionDigits != null)
                nf.setMaximumFractionDigits(maxFractionDigits);
            if (minFractionDigits != null)
                nf.setMinimumFractionDigits(minFractionDigits);
            return nf;
        }


    } // end for TeamPaneCell

    static class TeamPaneTable extends AbstractPeriodGrid {
        TeamPaneTable(Element e) {
            super(e);
        }

        public TeamPaneTable(ILocatorable loc, PSPage page) {
            super(loc, page);
        }

        @Override
        public Element getDynamicElement() {
            return new Element(TEAM_PANE_COLUMN_FIRST);
        }

        @Override
        protected Column[] initColumns(PSCalendar week) {
            Column av = initColumnElement(week, TEAM_PANE_COLUMN_DATE_FORMAT_WEEK_AV);
            Column al = initColumnElement(week, TEAM_PANE_COLUMN_DATE_FORMAT_WEEK_AL);
            if (av == null || al == null) return null;
            return new Column[]{al, av};
        }

        public List<TeamPaneCell> getColumnContent(PSCalendar calendar) {
            Column[] columns = searchWeekColumns(calendar);
            List<CellElement> cells1 = columns[0].getContent();
            List<CellElement> cells2 = columns[1].getContent();
            Assert.assertTrue(cells1.size() == cells2.size(), "Incorrect column for '" + calendar + "'");
            List<TeamPaneCell> res = new ArrayList<TeamPaneCell>();
            for (int i = 0; i < cells1.size(); i++) {
                TeamPaneCell cell = new TeamPaneCell(cells1.get(i), cells2.get(i));
                cell.calendar = calendar;
                res.add(cell);
            }
            return res;
        }
    }


    static abstract class AbstractPeriodGrid extends AbstractGanttSection {
        private int index;

        protected AbstractPeriodGrid(ILocatorable loc, PSPage page) {
            super(loc, page);
        }

        protected AbstractPeriodGrid(Element e) {
            super(e);
        }

        public void setIndex(int index) {
            this.index = index;
        }

        class Column extends CellElement {
            protected Column(Element e) {
                super(e);
            }

            public List<CellElement> getContent() {
                List<CellElement> cells = new ArrayList<CellElement>();
                for (Element cell : Element.searchElementsByXpath(this, TEAM_PANE_CELL_CONTENT)) {
                    cells.add(new CellElement(cell));
                }
                return cells;
            }
        }

        private Column[] getWeekColumns(PSCalendar week) {
            setDefaultElement(page.getDocument(false));
            Column[] columns = initColumns(week);
            if (columns == null) {
                PSLogger.warn("Can't find column for period " + week + "; try again");
                setDefaultElement(page.getDocument());
                columns = initColumns(week);
            }
            return columns;
        }

        protected Column[] searchWeekColumns(PSCalendar week) {
            Column[] columns = getWeekColumns(week);
            if (columns[index].getContent().size() == 0) {
                scrollTo(columns[index]);
                columns = initColumns(week);
            }
            return columns;
        }


        protected Column initColumnElement(PSCalendar week, ILocatorable format) {
            Element e = Element.searchElementByXpath(this, TEAM_PANE_COLUMN.replace(week.get(format)));
            if (e == null) return null;
            return new Column(e);
        }

        protected abstract Column[] initColumns(PSCalendar week);

        public Element getCell(PSCalendar period, Work work) {
            int index = work.getRowIndex() - 1;
            if (work.getResourceRowIndex() != -1) {
                index += work.getResourceRowIndex();
            }
            return getCell(period, index);
        }

        protected Element getCell(PSCalendar period, int index) {
            Column[] columns = searchWeekColumns(period);
            return columns[this.index].getContent().get(index);
        }

        public void scrollTo(PSCalendar position) {
            PSLogger.debug("Scroll to week " + position);
            scrollTo(getWeekColumns(position)[index]);
        }

        public String getValueForPeriod(PSCalendar period, Work work) {
            return getCell(period, work).getText();
        }

        public void setValueForPeriod(PSCalendar period, Work work, String toSet) {
            SimpleGrid.EditableCellInput in = ((FiltersGridPage) page).getGrid().new EditableCellInput(getCell(period, work));
            in.set(toSet);
        }

        public void setValueForPeriod(PSCalendar period, Work work, int toSet) {
            setValueForPeriod(period, work, String.valueOf(toSet));
        }
    }


}
