package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.ReportWindowLocators.*;

/**
 * To test:
 * private void testPublicReport() {
 * ReportFolderPage publicFolder = new ReportFolderPage();
 * publicFolder.open();
 * checkReportWindow(publicFolder.clickReportNameForRun(null));
 * }
 * <p/>
 * private void testWizard() {
 * ReportWizardPage wizard = ReportWizardPage.getPageInstance();
 * wizard.open();
 * wizard.clickContinue();
 * checkReportWindow(wizard.clickRunAsHtmlOption());
 * }
 * private void checkReportWindow(ReportWindow w) {
 * String err = w.getErrorBoxMessage();
 * if (err != null)
 * PSLogger.warn(err);
 * w.deselect();
 * }
 */
public class ReportWindow extends Window {
    private static boolean isAnyInit;

    public ReportWindow(boolean doOpen) {
        this();
        if (doOpen && !isAnyInit && !isBroken()) {
            name = NAME.getLocator();
            openNew(URL.replace("unknown"));
        }
    }

    public ReportWindow() {
        super();
        doMaximize = false;
    }

    /**
     * TODO: its hotfix.
     * investigate
     *
     * @return
     */
    public static boolean isBroken() {
        return SeleniumDriverFactory.getDriver().getType().isWebDriver() || SeleniumDriverFactory.getDriver().getType().isIE();
    }


    public void waitForOpen() {
        if (isBroken()) {
            return;
        }
        PSLogger.save("before new window");
        super.waitForOpen();
        if (isAnyInit) {
            new TimerWaiter(5000).waitTime();
        }
        isAnyInit = true;
        new Element(WAIT).waitForDisapeared();
        getDocument();
        PSLogger.save("See this report");
    }

    public List<List<String>> getContent() {
        Element table = getElement(false, TABLE);
        List<List<String>> res = new ArrayList<List<String>>();
        if (table == null || !table.isDEPresent()) {
            return res;
        }
        for (Element row : Element.searchElementsByXpath(table, ROW)) {
            List<String> cells = new ArrayList<String>();
            for (Element cell : Element.searchElementsByXpath(row, CELL)) {
                String txt = StrUtil.trim(cell.getDEText());
                Element span = cell.getChildByXpath(SPAN);
                if (span.isDEPresent()) {
                    txt += StrUtil.trim(span.getDEText());
                }
                cells.add(txt);
            }
            res.add(cells);
        }
        return res;
    }

    public String getContentAsString() {
        List<List<String>> res = getContent();
        StringBuffer sb = new StringBuffer();
        for (List<String> row : res) {
            if (row.isEmpty()) continue;
            for (String cell : row) {
                sb.append(cell).append("\t");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
