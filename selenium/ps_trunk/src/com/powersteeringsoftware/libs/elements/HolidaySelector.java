package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.elements.waiters.AlertWaiter;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.NonWorkDaysSelectorLocators.*;


/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 16:32:23
 */
public class HolidaySelector extends ASelector {
    private static final long ALERT_TIMEOUT = 30000;
    public HolidaySelector(ILocatorable locator) {
        super(locator);
    }


    public HolidaySelector(String locator) {
        super(locator);
    }

    public HolidaySelector(Element e) {
        super(e);
    }


    public void set(String name, String date) {
        set(name, date, null);
    }

    public void set(String name, String date, boolean[] howtoset) {
        PSLogger.info("Set " + name + "," + date + " to holiday selector");
        DatePicker dp = new DatePicker(getFirstRow().getChildByXpath(DATE_PICKER));
        Input in = new Input(getFirstRow().getChildByXpath(INPUT_NAME));
        if (howtoset != null && howtoset.length == 2) {
            dp.useDatePopup(howtoset[0]);
            dp.useDropDownOrArrows(howtoset[1]);
        }
        dp.set(date);
        in.type(name);
        getFirstRow().setSimpleLocator();
    }

    protected void set(ARowItem item) {
        RowItem i = ((RowItem) item);
        set(i.name, i.date, i.howtoset);
    }

    protected ARowItem get(Element row) {
        RowItem res = new RowItem();
        res.name = row.getChildByXpath(INPUT_CELL).getDEText();
        res.date = row.getChildByXpath(DATE_PICKER_CELL).getDefaultElement().getText();
        return res;
    }

    public void setList(List<ARowItem> items) {
        click(false); //<- ist for web-driver (ie)
        List<ARowItem> toSet = new ArrayList<ARowItem>(items);
        Collections.reverse(toSet);
        List<String> dates = new ArrayList<String>();
        List<ARowItem> toRemove = new ArrayList<ARowItem>();
        for (ARowItem item : toSet) {
            String d = ((RowItem) item).date;
            set(item);
            pushAdd();
            if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_4) && dates.contains(d)) {
                AlertWaiter.waitForAlert(ALERT_TIMEOUT);
                String alert = AlertWaiter.getAlert();
                //Assert.assertTrue(alert != null && !alert.isEmpty(), "Should be alert on duplicate date");
                PSLogger.info("Alert: '" + alert + "', item=" + item);
                toRemove.add(item);
            }
            dates.add(d);
        }
        toSet.removeAll(toRemove);
        Collections.reverse(toSet);
        items.clear();
        items.addAll(toSet);
    }

    public static class RowItem extends ARowItem {
        public String name;
        public String date;
        public boolean[] howtoset;

        public String toString() {
            return "{" + name + "," + date + "}";
        }

        public boolean equals(Object o) {
            if (!super.equals(o)) return false;

            String oName = ((RowItem) o).name;
            if (!name.trim().equals(oName.trim())) return false;
            if (!date.equals(((RowItem) o).date)) return false;
            return true;
        }
    }
}
