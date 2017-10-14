package com.powersteeringsoftware.libs.pages;


import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import static com.powersteeringsoftware.libs.enums.page_locators.TimesheetsPageLocators.*;

public class TimesheetsPage extends AbstractTimesheetsPage {


    public void open() {
        super.open();
        assertIsEditablePage();
    }


    public TimesheetsPageSubmitted pushSaveSubmit() {
        PSLogger.debug("Save and Submit");
        Button save = new Button(BUTTON_SAVE_SUBMIT);
        save.waitForVisible();
        save.click(true);
        PSLogger.save();
        TimesheetsPageSubmitted res = new TimesheetsPageSubmitted();
        res.waitWhileLoad();
        PSLogger.save("After saving and submitting on Timesheets page");
        if (SeleniumDriverFactory.getDriver().getType().isGoogleChrome()) {
            // to investigate for chrome. sometimes after reopen timesheets page there is a submitted page, may be this waiting will be fix
            WAITER.waitTime();
        }
        return res;
    }

    public void pushSaveChanges() {
        PSLogger.debug("Save Changes");
        Button save = new Button(BUTTON_SAVE);
        save.waitForVisible();
        save.click(true);
        PSLogger.save();
        waitWhileLoad();
        PSLogger.save("After saving on Timesheets page");
        if (SeleniumDriverFactory.getDriver().getType().isGoogleChrome()) {
            WAITER.waitTime();
        }
    }

    public void pushCancel() {
        PSLogger.debug("Save Cancel");
        Button save = new Button(BUTTON_CANCEL);
        save.waitForVisible();
        save.click(true);
        waitWhileLoad();
        PSLogger.save("After cancel on Timesheets page");
        if (SeleniumDriverFactory.getDriver().getType().isGoogleChrome()) {
            WAITER.waitTime();
        }
    }

    public String getWorkItemName(int lineNumber) {
        Element w = new Element(WORK_ITEM_EDITABLE.replace(lineNumber));
        return w.exists() ? w.getText().trim() : null;
    }

    public void pushAdd() {
        Element add = new Element(BUTTON_ADD);
        add.click(true);
        waitWhileLoad();
    }

    public void fillTimeSheetCell(int row, int column, String value) {
        new Input(TIME_SHEET_TABLE_CELL_EDITABLE.replace(row, column)).type(value);
    }

    public void selectWorkItemInFavorites(String workitemLocator, int lineNumber) {
        clickWorkItem(lineNumber);
        clickFavoritesWorkItemInPopup(workitemLocator);
        waitWhileLoad();
        PSLogger.save("after selecting work");
    }

    public void clearWorkItem(int row) {
        String item = getWorkItemName(row);
        if (!item.isEmpty()) {
            PSLogger.info("Clear item '" + item + "'");
            clickWorkItem(row);
            new Link(DIALOG_CLEAR_LINK).click(false);
            new Element(WORK_ITEM_DIV).waitForUnvisible();
            new Element(LOADING_PAGE).waitForDisapeared();
        }
        for (int i = 0; i < 7; i++) {
            Input cell = new Input(TIME_SHEET_TABLE_CELL_EDITABLE.replace(row, i));
            if (!cell.getValue().isEmpty()) {
                PSLogger.debug("Clear cell " + row + "," + i + " for item " + item);
                cell.type("");
            }
        }
        waitWhileLoad();
        PSLogger.save("after clearing row " + row);
    }

    /**
     * call selected work item dialog for specified row
     *
     * @param lineNumber (0,1,...)
     */
    public void clickWorkItem(int lineNumber) {
        new Element(WORK_ITEM_EDITABLE.replace(lineNumber)).click(false);
        new Element(WORK_ITEM_DIV).waitForVisible();
    }


    public String getTimeSheetCellValue(int row, int column) {
        return new Input(TIME_SHEET_TABLE_CELL_EDITABLE.replace(row, column)).getValue();
    }


    /**
     * click on item in favorites tab in popup
     *
     * @param workName - name for item
     */
    public void clickFavoritesWorkItemInPopup(String workName) {
        PSLogger.info("Open favorites tab in popup and click on " + workName + " item");
        Element tab = new Element(WORK_ITEM_DIALOG_TAB_FAVORITES);
        tab.setDefaultElement(getDocument());
        tab.click(false);
        PSLogger.save("On 'Favorites' tab");
        Element link = new Element(WORK_ITEM_DIALOG_LINK.replace(workName));
        link.click(false);
        new Element(WORK_ITEM_DIV).waitForUnvisible();
        new Element(LOADING_PAGE).waitForDisapeared();
    }

    public void selectActivityOption(String optionLocator, int lineNumber) {
        if (optionLocator == null || optionLocator.isEmpty()) return;
        PSLogger.debug("Select activity " + optionLocator + " for row " + lineNumber);
        SelectInput input = getActivitySelector(lineNumber);
        input.select(ACTIVITY_EDITABLE_LABEL.replace(optionLocator));
    }

    SelectInput getActivitySelector(int num) {
        SelectInput selector = new SelectInput(ACTIVITY_EDITABLE.replace(num));
        selector.waitForPresent(WAITER.getIntervalInMilliseconds());
        if (SeleniumDriverFactory.getDriver().getType().isGoogleChrome())
            selector.setDefaultElement();
        return selector;
    }


    public void selectBillingCategoryOption(String optionLocator, int lineNumber) {
        if (getBillingUsed()) {
            new SelectInput(BILLING_CATEGORY.replace(lineNumber)).select(BILLING_CATEGORY_LABEL.replace(optionLocator));
        }
    }

    public void clickCopyLast() {
        Link link = new Link(LINK_COPY_LAST);
        link.waitForPresent();
        link.clickAndWaitNextPage();
    }

    public String getActivity(int lineNumber) {
        return getActivitySelector(lineNumber).getSelectedLabel();
    }

    public String getBillingCategory(int lineNumber) {
        if (getBillingUsed()) {
            return new SelectInput(BILLING_CATEGORY.replace(lineNumber)).getSelectedLabel().trim();
        } else {
            return "";
        }
    }

    public WorkItemDialog openItemDialog(int i) {
        WorkItemDialog d = new WorkItemDialog(i);
        d.open();
        return d;
    }

    public boolean isSubmitted() {
        return false;
    }

    public class WorkItemDialog extends WorkChooserDialog {

        private WorkItemDialog(int i) {
            super(WORK_ITEM_EDITABLE.replace(i), WORK_ITEM_DIV);
        }

        public void setPopup(String loc) {
            popup = new Element(popupLocator = loc) {
                public void waitForUnvisible() {
                    super.waitForUnvisible();
                    new Element(LOADING_PAGE).waitForDisapeared();
                }
            };
        }

        public void openFavorites() {
            openTab(WORK_ITEM_DIALOG_FAVORITES);
        }

        public void chooseWorkOnFavoritesTab(Work work) {
            Link link = new Link(WORK_ITEM_DIALOG_LINK.replace(work.getName()));
            link.waitForVisible(); // web-driver
            link.click(false);
            waitAfterMakingChoice();
        }

        public void clear() {
            new Link(DIALOG_CLEAR_LINK).click(false);
            waitAfterMakingChoice();
        }

        protected void initSearchTab() {
            if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_4)) {
                super.initSearchTab();
                return;
            }
            searchTab = new WorkSearchTab() {
                public Link getLink(String name) {
                    WorkTreeElement tree = new WorkTreeElement(SEARCH_WORK_TREE_100);
                    tree.setPopup(getPopup());
                    tree.waitWhileLoading();
                    for (WorkTreeElement.TreeNode node : tree.getTreeNodes()) {
                        Link link = node.getLink();
                        if (link != null && link.getName().equals(name)) return link;
                    }
                    PSLogger.warn("Can't find de for " + name);
                    PSLogger.debug(asXML());
                    return null;
                }
            };
        }

    }

}
