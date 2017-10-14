package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.page_locators.MeasureInstancesEPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.MeasureInstancesEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 21.10.11
 * Time: 17:52
 */
public interface MeasuresPage {

    public void testUrl();

    public AbstractMeasureMenu openMenu(String name);

    public PSPage addNew();

    public Link getMeasureLink(String name);

    static abstract class AbstractMeasureMenu extends Menu {
        private PSPage page;

        AbstractMeasureMenu(String name, PSPage page) {
            super(getImg(name, null, page));
            this.page = page;
        }

        AbstractMeasureMenu(String name, String id, PSPage page) {
            super(getImg(name, id, page));
            this.page = page;
        }

        private static List<Element> getTitleCells(MeasureInstancesEPageLocators loc, PSPage page) {
            return page.getElements(loc);
        }

        static Element getRow(String name, PSPage page) {
            return getRow(name, null, page);
        }

        static Element getRow(String name, String id, PSPage page) {
            Element row = _getRow(name, id, page);
            Element next = new Element(NEXT);
            while (row == null && next.exists()) {
                PSLogger.info("Go to next table page");
                next.click(true);
                row = _getRow(name, id, page);
            }
            return row;
        }

        private static Element _getRow(String name, String id, PSPage page) {
            List<Element> rows = new ArrayList<Element>();
            for (Element cell : getTitleCells(TITLE_CELLS, page)) {
                Link link = new Link(cell.getChildByXpath(TITLE_CELL_LINK));
                if (id != null && !link.getHref().contains(id)) continue;
                String txt = link.getDEText();
                PSLogger.debug2("Row: " + txt);
                if (txt.equals(name)) {
                    rows.add(cell.getParent(ROW));
                }
            }
            return rows.isEmpty() ? null : rows.get(rows.size() - 1);
        }

        static Link getLink(String name, PSPage page) {
            Element row = getRow(name, page);
            if (row == null) return null;
            return new Link(row.getChildByXpath(TITLE_CELL_LINK));
        }

        private static Img getImg(String name, String id, PSPage page) {
            Element row = getRow(name, id, page);
            Assert.assertNotNull(row, "Can't find measure " + name);
            return new Img(row.getChildByXpath(TITLE_CELL_IMG));
        }

        protected void callViewProperties() {
            call(MENU_VIEW_PROPERTIES);
            page.waitForPageToLoad();
        }

        protected void callEdit() {
            call(MENU_EDIT);
            page.waitForPageToLoad();
        }

        protected void callCopy() {
            call(MENU_COPY);
            page.waitForPageToLoad();
        }

        public abstract AddEditMeasurePage edit();

        public abstract AddEditMeasurePage copy();

        public DeleteDialog delete() {
            call(MENU_DELETE);
            DeleteDialog dialog = new DeleteDialog();
            dialog.waitForVisible();
            PSLogger.save("delete dialog");
            return dialog;
        }

        protected boolean callDetach() {
            if (containsItem(MENU_DETACH)) {
                call(MENU_DETACH);
                return true;
            } else if (containsItem(MENU_DELETE)) {
                call(MENU_DELETE);
            } else {
                Assert.fail("Can't find neither delete or remove");
            }
            return false;
        }

        private abstract class AbstractDetachDeleteDialog extends Dialog {

            private AbstractDetachDeleteDialog() {
                super(DELETE_DETACH_DIALOG);
                setPopup(DELETE_DETACH_DIALOG);
            }
        }

        public abstract class RemoveDeleteDialog extends AbstractDetachDeleteDialog {

            public abstract void yes();
        }

        public class RemoveDialog extends RemoveDeleteDialog {

            public void yes() {
                String title = getTitle();
                PSLogger.info("Title: " + title);
                Button bt = new Button(REMOVE_YES_BUTTON);
                bt.submit();
            }
        }

        public class DetachDialog extends RemoveDeleteDialog {

            public void yes() {
                String title = getTitle();
                PSLogger.info("Title: " + title);
                Button bt = new Button(DETACH_YES_BUTTON);
                bt.submit();
            }
        }

        public class DeleteDialog extends AbstractDetachDeleteDialog {

            public void detach() {
                new RadioButton(DELETE_DIALOG_DETACH_RADIOBUTTON).click();
            }

            public void delete() {
                new RadioButton(DELETE_DIALOG_DELETE_RADIOBUTTON).click();
            }

            public void commit() {
                new Button(DELETE_DIALOG_COMMIT_BUTTON).submit();
            }
        }
    }

}
