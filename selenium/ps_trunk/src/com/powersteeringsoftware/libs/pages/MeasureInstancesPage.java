package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.Link;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.MeasureInstancesEPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 25.05.2010
 * Time: 13:17:46
 * To change this template use File | Settings | File Templates.
 */
public class MeasureInstancesPage extends AbstractWorkPage implements MeasuresPage {

    public boolean isThisPage(String workId) {
        return super.checkUrl(URL) && (workId == null || getUrl().contains(workId));
    }


    public AbstractMeasureMenu openMenu(String name) {
        MeasureInstanceMenu res = new MeasureInstanceManageMenu(name);
        res.open();
        return res;
    }

    public AbstractMeasureMenu openMenu(String name, String id) {
        MeasureInstanceMenu res = new MeasureInstanceManageMenu(name, id);
        res.open();
        return res;
    }


    /**
     * You can invoke this method only on the page Manage Measure
     */
    public AttachMeasureInstancesPage attach() {
        Button bt = new Button(ATTACH_NEW_BUTTON);
        bt.waitForPresent(5000);
        bt.submit();
        TimerWaiter.waitTime(1000);
        PSLogger.save("after pushing 'Attach New'");
        return new AttachMeasureInstancesPage();
    }


    /**
     * Click button "Add new"<br>
     * Before using this method you should navigate page 'Manage Measures'.<br>
     * You mustn't use selenium method waitForPageToLoad() after this method.
     */
    public AddEditMeasureInstancePage addNew() {
        Button bt = new Button(DEFINE_NEW_BUTTON);
        bt.waitForPresent(5000);
        bt.click(true);
        TimerWaiter.waitTime(1000);
        return new AddEditMeasureInstancePage();
    }

    public Link getMeasureLink(String name) {
        return AbstractMeasureMenu.getLink(name, this);
    }

    public class MeasureInstanceManageMenu extends MeasureInstanceMenu {
        private MeasureInstanceManageMenu(String name) {
            super(name);
        }

        private MeasureInstanceManageMenu(String name, String id) {
            super(name, id);
        }


        public void viewMeasure() {
            call(MENU_VIEW_MEASURE);
            waitForPageToLoad();
            // todo:
        }

        public RemoveDeleteDialog remove() {
            RemoveDeleteDialog dialog = callDetach() ? new DetachDialog() : new RemoveDialog();
            dialog.waitForVisible();
            return dialog;
        }
    }


    public class AttachMeasureInstancesPage extends MeasureInstancesPage {

        class MeasureInstanceAttachMenu extends AbstractMeasureMenu {
            private MeasureInstanceAttachMenu(String name) {
                super(name, AttachMeasureInstancesPage.this);
            }

            public void offline() {
                call(MENU_OFFLINE);
                // todo:
            }

            public AddEditMeasureTemplatePage copy() {
                callCopy();
                return new AddEditMeasureTemplatePage();
            }

            public AddEditMeasureTemplatePage edit() {
                callEdit();
                AddEditMeasureTemplatePage res = new AddEditMeasureTemplatePage();
                res.getContent().setEditPage(true);
                return res;
            }
        }

        @Override
        public MeasureInstanceAttachMenu openMenu(String name) {
            MeasureInstanceAttachMenu res = new MeasureInstanceAttachMenu(name);
            res.open();
            return res;
        }

        public MeasureInstancesPage attach(String name) {
            Element row = AbstractMeasureMenu.getRow(name, this);
            Assert.assertNotNull(row, "Can't find measure " + name);
            row.getChildByXpath(ATTACH_CELL_IMG).click(true);
            return new MeasureInstancesPage();
        }

        public AttachMeasureInstancesPage attach() {
            PSLogger.skip("method attach is not supported for this page");
            return this;
        }


    } // end class  AttachMeasureInstancesPage

    public class MeasureInstanceMenu extends AbstractMeasureMenu {

        private MeasureInstanceMenu(String name) {
            super(name, MeasureInstancesPage.this);
        }

        private MeasureInstanceMenu(String name, String id) {
            super(name, id, MeasureInstancesPage.this);
        }

        public AddEditMeasureInstancePage copy() {
            callCopy();
            return new AddEditMeasureInstancePage();
        }

        public AddEditMeasureInstancePage edit() {
            callEdit();
            return new AddEditMeasureInstancePage();
        }

    }


    public int getCount() {
        Element count = new Element(TABLE_COUNT);
        if (count.exists()) {
            return Integer.parseInt(count.getText().trim().replaceAll("[^\\d]+", ""));
        }
        return getElements(TITLE_CELLS).size();
    }

}
