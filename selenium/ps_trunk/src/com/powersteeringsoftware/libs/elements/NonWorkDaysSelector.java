package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;
import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.Version;
import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getVersion;
import static com.powersteeringsoftware.libs.enums.elements_locators.NonWorkDaysSelectorLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 16:46:57
 */
public class NonWorkDaysSelector extends ASelector {
    private static final TimerWaiter TIMEOUT = new TimerWaiter(1000);

    public NonWorkDaysSelector(ILocatorable locator) {
        super(locator);
    }

    public NonWorkDaysSelector(String locator) {
        super(locator);
    }

    public NonWorkDaysSelector(Element e) {
        super(e);
    }

    public NonWorkDaysSelector(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public void set(String name, String date1, boolean[] howtoset1, String date2, boolean[] howtoset2) {
        PSLogger.info("Set " + name + "," + date1 + "," + date2 + " to non-work-days selector");
        DatePicker dp1 = new DatePicker(getFirstRow().getChildByXpath(DATE_PICKER));
        DatePicker dp2 = new DatePicker(getFirstRow().getChildByXpath(DATE_PICKER_2));
        Input in = new Input(getFirstRow().getChildByXpath(INPUT_NAME));

        if (howtoset1 != null && howtoset1.length == 2) {
            dp1.useDatePopup(howtoset1[0]);
            dp1.useDropDownOrArrows(howtoset1[1]);
        }
        if (howtoset2 != null && howtoset2.length == 2) {
            dp2.useDatePopup(howtoset2[0]);
            dp2.useDropDownOrArrows(howtoset2[1]);
        }
        if (!date1.isEmpty())
            dp1.set(date1);
        else
            dp1.getInput().type("");
        if (!date2.isEmpty())
            dp2.set(date2);
        else
            dp2.getInput().type("");

        in.type(name);
        getFirstRow().mouseDownAndUp();

        //if (getDriver().getType().isGoogleChrome()) {
        new TimerWaiter(1300).waitTime();
        //}
        getFirstRow().setSimpleLocator();
    }

    protected void set(ARowItem item) {
        RowItem i = ((RowItem) item);
        set(i.name, i.date1, i.howtoset1, i.date2, i.howtoset2);
        if (i.date2.isEmpty()) {
            i.date2 = i.date1;
            i.date1 = "";
        }
        if (i.date2.equals(i.date1)) {
            i.date1 = ""; // now cleaning first date
        }
        if (i.date1.isEmpty()) {
            return;
        }
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(new SimpleDateFormat(CoreProperties.getDateFormat()).parse(i.date1));
            c2.setTime(new SimpleDateFormat(CoreProperties.getDateFormat()).parse(i.date2));
        } catch (ParseException e) {
            Assert.fail("Can't parse input", e);
        }
        if (c1.compareTo(c2) > 0) {
            PSLogger.debug("Swap " + i.date1 + " and " + i.date2);
            String date = i.date1;
            i.date1 = i.date2;
            i.date2 = date;
        }
    }

    public void setList(List<ARowItem> items) {
        List<ARowItem> toSet = new ArrayList<ARowItem>();
        Collections.reverse(items);
        for (ARowItem item : items) {
            RowItem rItem = (RowItem) item;
            PSLogger.info("Set data: " + rItem);
            set(rItem);
            pushAdd();
            PSLogger.save("After pushing 'add'");
            if (containsInList(toSet, rItem)) {
                waitForWarning();
            } else {
                toSet.add(rItem);
            }
            TIMEOUT.waitTime();
        }
        Collections.reverse(toSet);
        items.clear();
        items.addAll(toSet);
    }

    public void waitForWarning() {
        PSLogger.info("Check popup with warning");
        Element warning = new Element(WARNING_DIALOG);
        warning.waitForVisible();
        String txt = warning.getChildByXpath(WARNING_DIALOG_MESSAGE).getText();
        Assert.assertFalse(txt.isEmpty(), "Can't find warning message");
        PSLogger.info(txt);
        // can't click on this button for ff50 web-driver-2.0.0. can for web-driver-2.0-rc3
        if (getDriver().getType().isWebDriverFF(5) && getVersion().equals(Version._2_0_0)) {
            PSSkipException.skip("Not supported for this configuration.");
        }
        new TimerWaiter(100).waitTime();
        Button bt = new Button(warning.getChildByXpath(WARNING_DIALOG_OK));
        //bt.waitForVisible();
        //bt.setDefaultElement();
        bt.click(false);
        try {
            warning.waitForUnvisible();
        } catch (Wait.WaitTimedOutException we) {
            if (getVersion().isDialogBroken(getDriver())) {
                PSSkipException.skip(getClass().getSimpleName() + ":" + we.getMessage());
            } else {
                throw we;
            }
        }
    }

    private static boolean containsInList(List<ARowItem> items, RowItem item) {
        for (ARowItem i : items) {
            if (((RowItem) i).getDates().equals(item.getDates())) return true;
        }
        return false;
    }


    protected ARowItem get(Element row) {

        String name = row.getChildByXpath(INPUT_CELL).getDEText();
        String date1 = row.getChildByXpath(DATE_PICKER_CELL).getDefaultElement().getText();
        String date2 = row.getChildByXpath(DATE_PICKER_CELL_2).getDefaultElement().getText();
        return new RowItem(name, date1, date2);
    }


    public static class RowItem extends ARowItem {
        private String name;
        private String date1;
        private String date2;
        private boolean[] howtoset1;
        private boolean[] howtoset2;

        public RowItem(String name, String start, String end) {
            this.name = name;
            setStartDate(start);
            setEndDate(end);
        }

        public void setHowToSet(boolean[] start, boolean[] end) {
            howtoset1 = start;
            howtoset2 = end;
        }

        public void setStartDate(String start) {
            date1 = start;
        }

        public void setEndDate(String end) {
            date2 = end;
        }

        public String getStartDate() {
            return date1;
        }

        public String getEndDate() {
            return date2;
        }

        public String getDates() {
            return date1 + "," + date2;
        }

        public String toString() {
            return "{" + name + "," + getDates() + "}";
        }

        public boolean equals(Object o) {
            if (!super.equals(o)) return false;
            String oName = ((RowItem) o).name;
            if (!name.trim().equals(oName.trim())) return false;
            if (!date1.equals(((RowItem) o).date1)) return false;
            if (!date2.equals(((RowItem) o).date2)) return false;
            return true;
        }
    }


}
