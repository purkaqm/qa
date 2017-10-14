package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.elements_locators.DatePickerLocators.*;

/**
 * Class for DatePicker elements (for choosing date, period and inline-elements)
 * User: szuev
 * Date: 01.06.2010
 * Time: 12:39:07
 */
public class DatePicker extends Element {
    protected boolean useDropDownOrArrow = true;
    private boolean usePopup = true;
    private Element popupOpenIcon;
    protected Element popup;
    private static final long TIMEOUT_FOR_POPUP = 30000; //ms

    public DatePicker(ILocatorable locator) {
        super(locator);
    }

    public DatePicker(String locator) {
        super(locator);
    }

    public DatePicker(Element e) {
        super(e);
    }

    public DatePicker(Document document, String locator) {
        super(document, locator);
    }

    public void useDropDownOrArrows(boolean flag) {
        useDropDownOrArrow = flag;
    }

    public void useDatePopup(boolean flag) {
        usePopup = flag;
    }

    public boolean getUseDatePopup() {
        if (!usePopup) return usePopup;
        if (isInline()) { //inline
            usePopup = true;
        }
        return usePopup;
    }

    public void set(long time) {
        if (getUseDatePopup()) {
            setUsingPopup(time);
            return;
        }
        set(time, getFormat());
    }

    public void set(long time, SimpleDateFormat format) {
        String sTime = format.format(time);
        PSLogger.info("Set time " + sTime + " to input " + locator);
        setUsingInput(sTime);
    }

    public void type(String txt) {
        setUsingInput(txt);
    }

    public void type(long time) {
        type(getFormat().format(time));
    }

    public void set(String time) {
        PSLogger.info("Set time " + time);
        focus();
        if (getUseDatePopup()) {
            PSLogger.debug("use popup to set");
            setUsingPopup(time);
        } else {
            PSLogger.debug("use input to set");
            setUsingInput(time);
        }
        focus();
    }

    /**
     * @return true if there is error input
     */
    public boolean isWrongInput() {
        if (isInline()) return false;
        Element parent = getParent();
        parent.focus();
        parent.mouseDownAndUp(); // <-- use mouse down and up instead click because of web-driver
        //parent.click(false);
        focus();
        click(false);
        return getAttribute(CLASS_ATTR).contains(CLASS_ERROR_VALUE.getLocator());
    }

    public Element getInput() {
        return getChildByXpath(INPUT);
    }

    public String get() {
        if (isInline()) { //inline
            return getText();
        }
        return getInput().getValue();
    }

    private void setUsingPopup(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        setUsingPopup(c);
    }

    protected void setUsingPopup(String time) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTimeInMillis(getFormat().parse(time).getTime());
        } catch (ParseException e) {
            PSLogger.warn("incorrect time specified: " + time);
            return;
        }
        setUsingPopup(c);
    }

    private void setUsingPopup(Calendar c) {
        ADatePickerPopup popup = openPopup();
        popup.set(c);
    }

    protected void setUsingInput(String time) {
        getInput().type(time);
        unFocus();
    }

    protected void unFocus() {
        // this is for ie:
        getInput().mouseDownAndUp();
        new Element("//body").mouseDownAndUp();
    }


    protected Element getPopup() {
        if (popup != null) return popup;
        popup = new DijitPopup();
        popup.setFrame(this);
        return popup;
    }

    public ADatePickerPopup openPopup() {
        Element icon = getOpenPopupIcon();
        Element popup = getPopup();
        try {
            //icon.focus();
            icon.click(false);
            popup.waitForVisible(TIMEOUT_FOR_POPUP);
        } catch (Wait.WaitTimedOutException w) {
            PSLogger.warn(w.getMessage());
            click(false);
            icon.click(false);
            popup.waitForVisible();
        }
        if (!popup.getChildByXpath(POPUP_DATE_PICKER_TABLE).exists()) {
            PSLogger.debug("Seems this is a period date popup");
            return new PeriodDatePickerPopup(popup);
        }
        return new DatePickerPopup(popup, useDropDownOrArrow);
    }

    private Element getOpenPopupIcon() {
        if (popupOpenIcon != null) return popupOpenIcon;
        if (!isDEPresent()) {
            setDefaultElement();
        }
        Element icon = getChildByXpath(POPUP_OPEN_ICON);
        if (icon.isDEPresent()) {
            popupOpenIcon = icon;
        } else {
            popupOpenIcon = this; // inline
        }
        return popupOpenIcon;
    }

    public boolean isInline() {
        return equals(getOpenPopupIcon());
    }

    protected static abstract class ADatePickerPopup extends Element {
        private static final int NUMBER_ATTEMPTS_YEAR = 2;

        protected ADatePickerPopup(Element e) {
            super(e);
            setDefaultElement();
        }

        public abstract void set(Calendar c);

        protected abstract void clickYear(Element toClick);

        public void setYear(int year) {
            int currentYear;
            int tmpCurrentYear = 0;
            //hotfix! for ie search another locator:
            Element eCurrentYear = Element.searchElementByXpath(this, POPUP_CURREN_YEAR);
            int i = 0;
            while ((currentYear = Integer.valueOf(eCurrentYear.getText())) != year) {
                PSLogger.debug2("Current year: " + currentYear);
                Element toClick = Element.searchElementByXpath(this, currentYear > year ? POPUP_PREVIOUS_YEAR :
                        POPUP_NEXT_YEAR);
                clickYear(toClick);
                if (tmpCurrentYear == currentYear) {
                    PSLogger.warn("Can't change current year " + getClass().getSimpleName());
                    PSLogger.save();
                    if (i++ == NUMBER_ATTEMPTS_YEAR)
                        break;
                }
                tmpCurrentYear = currentYear;
            }
            PSLogger.debug2("After setting year: " + currentYear);
        }
    }

    /**
     * class for period popup
     */
    public static class PeriodDatePickerPopup extends ADatePickerPopup {

        PeriodDatePickerPopup(Element popup) {
            super(popup);
        }

        @Override
        public void set(Calendar c) {
            setYear(c.get(Calendar.YEAR));
            setMonth(c.get(Calendar.MONTH));
        }

        @Override
        protected void clickYear(Element toClick) {
            toClick.setSimpleLocator();
            toClick.click(false);
            new TimerWaiter(100).waitTime();
        }

        private void setMonth(int month) {
            Element eMonth = new Element(POPUP_TYPE_2_MONTH.replace(month));
            eMonth.click(false);
            PSLogger.info("Current month is " + eMonth.getText() + " in period popup");
        }
    }

    public static class DatePickerPopup extends ADatePickerPopup {
        private boolean useDropDown = true;
        private static List<String> months;

        public DatePickerPopup(Element popup, boolean flag) {
            super(popup);
            useDropDown = flag;
        }

        @Override
        public void set(Calendar c) {
            if (useDropDown)
                setMonthUsingDropDown(c.get(Calendar.MONTH));
            else
                setMonthUsingArrows(c.get(Calendar.MONTH));
            setYear(c.get(Calendar.YEAR));
            setDay(c.get(Calendar.DAY_OF_MONTH));
        }

        @Override
        protected void clickYear(Element toClick) {
            if (!getDriver().getType().isWebDriver()) {
                toClick.setSimpleLocator();
                toClick.mouseDownAndUp();
            } else {
                toClick.click(false);
            }
            new TimerWaiter(100).waitTime();
        }

        private List<String> getAllMonths() {
            if (months == null) {
                months = new ArrayList<String>();
                for (int i = 0; i < 12; i++) {
                    String monthTxt = Element.searchElementByXpath(this,
                            POPUP_TYPE_1_MONTH.replace(i)).getDEText();
                    months.add(monthTxt);
                }
                if (months.size() == 0) {
                    PSLogger.error("Can't collect months");
                    months = null;
                }
            }
            return months;
        }

        private void setMonthUsingArrows(int month) {
            PSLogger.info("Set month " + month + " using arrows");
            Element currentMonth = Element.searchElementByXpath(this, POPUP_TYPE_1_SELECTED_MONTH);
            String currentMonthTxt = currentMonth.getText();
            int currentMonthIndex = getAllMonths().indexOf(currentMonthTxt);
            int numberOfClicking = currentMonthIndex - month;
            PSLogger.info("Number of clicking on '" + (numberOfClicking > 0 ? "<" : ">") + "' is " + numberOfClicking);
            Element increment = Element.searchElementByXpath(this, POPUP_TYPE_1_INCREMENT_MONTH);
            Element decrement = Element.searchElementByXpath(this, POPUP_TYPE_1_DECREMENT_MONTH);
            Element rightChanger = numberOfClicking > 0 ? decrement : increment;
            Element reverseChanger = numberOfClicking > 0 ? increment : decrement;

            if (!getDriver().getType().isWebDriver()) {
                rightChanger.setSimpleLocator();
                reverseChanger.setSimpleLocator();
            }

            int prevMonthIndex = currentMonthIndex;
            while (currentMonthIndex != month) {
                incrementDecrementClicker(rightChanger);
                currentMonthTxt = currentMonth.getText();
                PSLogger.debug2("current month : " + currentMonthTxt);
                currentMonthIndex = getAllMonths().indexOf(currentMonthTxt);
                int diff = Math.abs(prevMonthIndex - currentMonthIndex);
                prevMonthIndex = currentMonthIndex;
                if (diff != 1) {
                    PSLogger.warn("Incorrect month, diff is " + diff + "; try go back");
                }
                while (diff > 1) {
                    incrementDecrementClicker(reverseChanger);
                    currentMonthTxt = currentMonth.getText();
                    PSLogger.debug2("current month : " + currentMonthTxt);
                    currentMonthIndex = getAllMonths().indexOf(currentMonthTxt);
                    diff = Math.abs(prevMonthIndex - currentMonthIndex);
                }
            }
            /*
            int prevMonthIndex = currentMonthIndex;
            int num = Math.abs(numberOfClicking);
            for (int i = 0; i < num; i++) {
                incrementDecrementClicker(rightChanger);
                currentMonthTxt = currentMonth.getText();
                PSLogger.debug("current month : " + currentMonthTxt);
                currentMonthIndex = getAllMonths().indexOf(currentMonthTxt);
                if (currentMonthIndex == month) break;

                int diff = Math.abs(prevMonthIndex - currentMonthIndex);
                prevMonthIndex = currentMonthIndex;
                if (diff == 1) continue;
                PSLogger.warn("Incorrect month, diff is " + diff);
                if (diff > 1) {
                    incrementDecrementClicker(reverseChanger);
                    num++;
                    continue;
                }
                num--;
            }
            */
        }

        private void incrementDecrementClicker(Element changer) {
            if (!getDriver().getType().isWebDriver()) {
                changer.mouseDownAndUp();
                if (getDriver().getType().isIE())
                    new TimerWaiter(500).waitTime(); // wait for ie
            } else {
                changer.click(false);
            }
        }

        private void setMonthUsingDropDown(int month) {
            PSLogger.debug2("Set month " + month + " using drop-down popup");
            Element currentMonth = Element.searchElementByXpath(this, POPUP_TYPE_1_SELECTED_MONTH);
            Element eMonth = Element.searchElementByXpath(this, POPUP_TYPE_1_MONTH.replace(month));
            currentMonth.focus();
            currentMonth.mouseDown();
            Element selector = Element.searchElementByXpath(this, POPUP_TYPE_1_MONTH_DROP_DOWN);
            selector.waitForVisible();
            eMonth.mouseUp();
            PSLogger.info("Current month is " + currentMonth.getText());
        }

        public void setDay(int day) {
            PSLogger.debug2("Set day " + day);
            String loc = POPUP_TYPE_1_DAY.replace(day);
            setDefaultElement();
            Element eDay = Element.searchElementByXpath(this, loc);//new Element(loc);
            if (eDay == null || !eDay.exists()) {
                PSLogger.warn("Can't find day " + day + " for this month");
                return;
            }
            try {
                eDay.click(false);
            } catch (SeleniumException se) {
                if (!getDriver().getType().isGoogleChromeGreaterThan(10)) throw se;
                // hotfix for chrome 12. will it work in general?
                PSLogger.warn(se.getMessage());
                eDay.getParent().click(false);
            }
        }

    }

    private static SimpleDateFormat getFormat() {
        SimpleDateFormat res = new SimpleDateFormat(CoreProperties.getDateFormat());
        res.setTimeZone(CoreProperties.getApplicationServerTimeZone());
        return res;
    }
}
