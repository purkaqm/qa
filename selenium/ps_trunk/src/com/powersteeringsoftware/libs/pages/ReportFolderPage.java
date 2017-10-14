package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.ReportWizardPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.rw.ReportWizardPage;
import com.powersteeringsoftware.libs.pages.rw.SaveRWPage;
import com.powersteeringsoftware.libs.pages.rw.TypeRWPage;
import com.powersteeringsoftware.libs.util.StrUtil;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportFolderPageLocators.*;

public class ReportFolderPage extends PSPage {
    private boolean isMy;

    public ReportFolderPage(boolean w) {
        isMy = w;
    }

    public ReportFolderPage() {
        this(false);
    }

    public void open() {
        if (isMy)
            clickBrowseMyReports();
        else
            clickBrowsePublicReports();
        //super.open(isMy ? MainMenuLocators.BROWSE_MY_REPORTS : MainMenuLocators.BROWSE_PUBLIC_REPORTS_LINK);
    }

    public void setIsMy(boolean x) {
        isMy = x;
    }

    public void clickAddSubfolderMenu() {
        Menu menu = new Menu(new Link(ADD_FOLDER_LINK));
        menu.open();
        menu.call(ADD_FOLDER_MENU_ADD);
    }

    public void clickRemoveSubfolderMenu() {
        Menu menu = new Menu(new Link(ADD_FOLDER_LINK));
        menu.open();
        menu.call(ADD_FOLDER_MENU_DELETE);
    }


    public boolean isFolderExist(String fName) {
        if (!isAnyFolderExist()) return false;
        ListSubfolderDialog listSfrdDlg = openFolderList();
        boolean res = listSfrdDlg.isFolderLinkExist(fName);
        listSfrdDlg.close();
        return res;
    }

    public void comeInFolder(String fName) {
        ListSubfolderDialog listSfrdDlg = openFolderList();
        Link fLink = listSfrdDlg.getFolderLink(fName);
        fLink.click(true);
        listSfrdDlg.waitForUnvisible();
    }

    public int getReportsAmount() {
        List<Element> rows = getReports();
        return rows == null ? 0 : rows.size();
    }


    /**
     * Creates new folder
     *
     * @param fName - new folder name
     */
    public void createNewFolder(String fName) {
        PSLogger.info("Create folder " + fName);
        clickAddSubfolderMenu();
        AddSubfolderDialog dialog = new AddSubfolderDialog();
        dialog.waitForVisible();
        dialog.setFolderName(fName);
        dialog.pressAddButton();

    }

    public void removeFolder(String fName, boolean forcibly) {
        comeInFolder(fName);
        if (getReportsAmount() > 0) {
            Assert.assertTrue(forcibly, "Report folder is not empty and cannot be removed");
            removeAllReports();
        }
        removeFolder();
    }

    private void removeFolder() {
        PSLogger.info("Remove current folder");
        clickRemoveSubfolderMenu();
        RemoveSubfolderDialog dialog = new RemoveSubfolderDialog();
        dialog.pressDeleteButton();
    }

    public void removeAllReports() {
        Element chImg = Element.searchElementByXpath(new Element(DELETE_COLUMN_HEADER), DELETE_IMG);
        chImg.click(false);
        getRemoveReportButton().click(true);
    }

    private Button getRemoveReportButton() {
        Element btnElm = Element.searchElementByXpath(new Element(FORM_LOCATOR), TB_FOLDER_REMOVE_BUTTON);
        return new Button(btnElm);
    }

    /**
     * Checks and returns if the report exists in the provided folder
     *
     * @param fName - folder name
     * @param rName - report name
     * @return
     */
    public boolean isReportExist(String fName, String rName) {
        comeInFolder(fName);
        return isReportExist(rName);
    }

    public boolean isReportExist(String rName) {
        return getReportsNames().contains(rName);
    }

    private ListSubfolderDialog openFolderList() {
        Link listFolderLink = new Link(LIST_FOLDER_LINK);
        listFolderLink.click(false);
        ListSubfolderDialog dialog = new ListSubfolderDialog();
        dialog.waitForVisible();
        return dialog;
    }

    public boolean isAnyFolderExist() {
        Link listFolderLink = new Link(LIST_FOLDER_LINK);
        return listFolderLink.exists();
    }


    // Save banner methods
    public ReportWizardPage clickEditLastReport() {
        Link openLastEdited = new Link(SAVE_BANNER_EDIT_REPORT);
        openLastEdited.click(true);
        ReportWizardPage res = ReportWizardPage.getPageInstance();
        res.setDocument();
        res.setIsCompleted(true);
        return res;
    }

    public void clickRunItNow() {
        Link runItNow = new Link(SAVE_BANNER_RUN_IT_NOW);
        runItNow.click(true);
    }

    public ReportWizardPage clickCreateAnotherReport() {
        Link runItNow = new Link(SAVE_BANNER_CREATE_ANOTHER_REPORT);
        runItNow.click(true);
        ReportWizardPage res = ReportWizardPage.getPageInstance();
        res.setIsCompleted(false);
        res.setDocument();
        return res;
    }

    public boolean checkSaveBannerValues() {
        Element banner = new Element(SAVE_BANNER);
        Link openLastEdited = new Link(SAVE_BANNER_RUN_IT_NOW);
        Link runItNow = new Link(SAVE_BANNER_LAST_EDITED);
        Link editReport = new Link(SAVE_BANNER_EDIT_REPORT);
        Link createAnotherReport = new Link(SAVE_BANNER_CREATE_ANOTHER_REPORT);
        return banner.exists() && openLastEdited.exists() && runItNow.exists() && editReport.exists() && createAnotherReport.exists();
    }

    public String getReportNameFromBanner() {
        Link runItNow = new Link(SAVE_BANNER_LAST_EDITED);
        if (!runItNow.exists()) return null;
        return StrUtil.trim(runItNow.getText());
    }

    public List<Element> getReports() {
        showMore();
        return getElements(REPORT_NAME_LOCATOR);
    }

    public void showMore() {
        Link img = new Link(LAST_LINK);
        if (img.exists()) {
            PSLogger.debug("Show more");
            new Link(MORE_LESS_IMG).click(false);
            waitForPageToLoad();
        }
    }

    public List<String> getReportsNames() {
        List<String> res = new ArrayList<String>();
        for (Element r : getReports()) {
            String txt = StrUtil.trim(r.getDEText());
            if (!txt.isEmpty())
                res.add(txt);
        }
        return res;
    }


    public LinksMenu callReportMenu(String name) {
        List<Element> names = getReports();
        Assert.assertTrue(names.size() != 0, "Cannot find any reports on page");
        Element link = null;
        if (name == null) {
            name = StrUtil.trim((link = names.get(0)).getDEText());
        }
        PSLogger.info("Call menu for report '" + name + "'");
        if (link == null)
            for (Element e : names) {
                if (name.equals(StrUtil.trim(e.getDEText()))) {
                    link = e;
                    break;
                }
            }
        Assert.assertNotNull(link, "Can't find report '" + name + "'");
        LinksMenu menu = new LinksMenu(link);
        menu.open();
        return menu;
    }

    public LinksMenu callReportMenu() {
        return callReportMenu(null);
    }

    public ReportWindow callRunAs(String name, ILocatorable loc) {
        LinksMenu menu = callReportMenu(name);
        Link link = menu.getLink(loc);
        Assert.assertNotNull(link, "Can't find link " + loc.getLocator());
        ReportWindow res = new ReportWindow();
        PSLogger.debug(link.asXML());
        link.click(false);
        res.waitForOpen();
        return res;
    }

    public ReportWindow clickReportNameForRun(String name) {
        return callRunAs(name, MENU_RUN_HTML);
    }


    public SaveRWPage clickReportNameToCopy(String name) {
        LinksMenu menu = callReportMenu(name);
        Link link = menu.getLink(MENU_COPY);
        link.clickAndWaitNextPage();
        SaveRWPage res = (SaveRWPage) ReportWizardPage.getPageInstance(ReportWizardPageLocators.RWTab.Save);
        res.setDocument();
        res.setIsCopy(true);
        return res;
    }

    public SaveRWPage clickReportNameToEdit(String name) {
        LinksMenu menu = callReportMenu(name);
        Link link = menu.getLink(MENU_EDIT);
        link.clickAndWaitNextPage();
        SaveRWPage res = (SaveRWPage) ReportWizardPage.getPageInstance(ReportWizardPageLocators.RWTab.Save);
        res.setDocument();
        return res;
    }

    public ReportFiltersPage clickReportNameToEditFilters(String name) {
        LinksMenu menu = callReportMenu(name);
        Link link = menu.getLink(MENU_EDIT_FILTERS);
        link.clickAndWaitNextPage();
        return new ReportFiltersPage();
    }

    public TypeRWPage clickReportNameToGoToRW(String name) {
        LinksMenu menu = callReportMenu(name);
        Link link = menu.getLink(MENU_REPORT_WIZARD);
        Assert.assertNotNull(link, "Can't find report wizard link in menu.");
        link.clickAndWaitNextPage();
        TypeRWPage res = (TypeRWPage) ReportWizardPage.getPageInstance();
        res.setDocument();
        res.setIsCompleted(true);
        return res;
    }

    public ReportFolderPage openMyReports() {
        if (isMyReports()) return this;
        Link el = new Link(MY_REPORTS);
        el.clickAndWaitNextPage();
        Assert.assertTrue(isMyReports(), "This is not my reports page");
        return new ReportFolderPage(true);
    }

    public ReportFolderPage openPublicReports() {
        if (isPublicReports()) return this;
        Link el = new Link(PUBLIC_REPORTS);
        el.clickAndWaitNextPage();
        Assert.assertTrue(isPublicReports(), "This is not public reports page");
        return new ReportFolderPage(false);
    }

    public String getCurrentTab() {
        return StrUtil.trim(new Element(CURRENT_TAB_LINK).getText());
    }

    public boolean isMyReports() {
        return checkIsMyReports(getCurrentTab());
    }

    public boolean isPublicReports() {
        return checkIsPublicReports(getCurrentTab());
    }

    public static boolean checkIsMyReports(String name) {
        return MY_REPORTS_TAB_NAME.getLocator().equals(name);
    }

    public static boolean checkIsPublicReports(String name) {
        return PUBLIC_REPORTS_TAB_NAME.getLocator().equals(name);
    }

    public String getFolderName() {
        return getContainerShortHeader();
    }

    //=================  AddSubfolderDialog =====================//
    private class AddSubfolderDialog extends Dialog {
        AddSubfolderDialog() {
            super(ADD_FOLDER_DIALOG);
            this.setPopup(this.getLocator());
        }


        void setFolderName(String fName) {
            Input foderNameElm = new Input(FOLDER_NAME_INPUT);
            foderNameElm.type(fName);
            PSLogger.save("After setting new folder report name");
        }

        void pressAddButton() {
            Button btn = new Button(getChildByXpath(FOLDER_ADD_BUTTON));
            btn.waitForVisible(5000);
            btn.click(false);
            waitForPageToLoad();
        }

    }// class AddSubfolderDialog 


    //=================  RemoveSubfolderDialog  =====================//
    private class RemoveSubfolderDialog extends Dialog {
        RemoveSubfolderDialog() {
            super(REMOVE_FOLDER_DIALOG);
            this.setPopup(this.getLocator());
        }

        void pressDeleteButton() {
            Button btn = new Button(FOLDER_REMOVE_BUTTON);
            btn.click(false);
            waitForPageToLoad();
        }
    } // class RemoveSubfolderDialog 


    //=================  ListSubfolderDialog =====================//
    private class ListSubfolderDialog extends Dialog {

        ListSubfolderDialog() {
            super(LIST_FOLDER_DIALOG);
            this.setPopup(this.getLocator());
        }

        boolean isFolderLinkExist(String fName) {
            Link fLink = getFolderLink(fName);
            return fLink != null && fLink.exists();
        }

        Link getFolderLink(String fName) {
            WorkTreeElement wtElm = new WorkTreeElement();
            wtElm.openTree();
            PSLogger.save("After opening report folder tree");
            Link fLink = wtElm.getWorkLink(fName, false);
            return fLink;
        }

        void closeFolderDialog() {
            Button btn = new Button(LIST_FOLDER_CANCEL_BUTTON);
            btn.waitForVisible(5000);
            btn.click(false);
        }

    } // class ListSubfolderDialog 


} // class ReportFolderPage 


