package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.DatePicker;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.elements.SelectInput;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.thoughtworks.selenium.Wait;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.HistoryWorkPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 13.05.11
 * Time: 18:42
 */
public class HistoryWorkPage extends AbstractWorkPage {
    private static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat(EVENT_DAY_DATE_FORMAT.getLocator());
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(EVENT_DATE_FORMAT.getLocator());

    @Override
    public void open() {
        //TODO:
        throw new IllegalMonitorStateException("TODO");
    }


    public DatePicker getDatePickerTo() {
        return new DatePicker(DATE_TO_SELECTOR);
    }

    public DatePicker getDatePickerStart() {
        return new DatePicker(DATE_START_SELECTOR);
    }

    public void clearEndDateFilter() {
        getDatePickerTo().type("");
    }

    public void selectAll() {
        waitForLoading();
        new SelectInput(VIEW_SELECTOR).select(VIEW_SELECTOR_ALL);
        new Button(GO).click(true);
        waitForLoading();
        getDocument();
    }

    private void waitForLoading() {
        try {
            new Button(GO).waitForVisible();
        } catch (Wait.WaitTimedOutException we) {
            Exception cause;
            if ((cause = getProblemFromPage()) != null) {
                throw new PSKnownIssueException(75079, cause);
            } else {
                throw we;
            }
        }
    }

    public List<Event> getEvents() {
        List<Event> res = new ArrayList<Event>();
        for (Element tr : Element.searchElementsByXpath(getTable(), ROW)) {
            if (tr.getChildByXpath(TH_EVENT).isDEPresent()) continue;
            res.add(new Event(tr));
        }
        return res;
    }

    private Element getTable() {
        return getElement(false, TABLE);
    }

    public class Event extends Element {
        String name;
        String author;
        String item;
        String date;
        String description;

        protected Event(Element e) {
            super(e);
            name = getChildByXpath(TD_EVENT).getDEText();
            author = getChildByXpath(TD_AUTHOR_TXT).getDEText();
            item = getChildByXpath(TD_ITEM_LINK).getDEText();
            date = getChildByXpath(TD_DATE).getDEText();
            Element desc = getChildByXpath(TD_DESC_SPAN);
            description = StrUtil.trim(desc.getDEInnerText(TD_DESC_SPAN_MENU).replaceAll("\\s+", " "));
        }

        public String toString() {
            return "{" + name + "," + author + "," + item + "," + date + "," + description + "}";
        }

        public Date getDate() {
            try {
                return DATE_FORMAT.parse(date);
            } catch (ParseException e) {
                PSLogger.fatal(e.getMessage());
                return null;
            }
        }

        public String getSDate() {
            return DAY_DATE_FORMAT.format(getDate());
        }

        public String getAuthor() {
            return author;
        }

        public String getName() {
            return name;
        }

        public String getItem() {
            return item;
        }


        public String getDescription() {
            return description;
        }

        public boolean isDelegation(Work work, PSCalendar expected) {
            PSLogger.debug("Check event " + this);
            if (!name.equals(EVENT_WORK_DELEGATION_NAME.getLocator())) {
                PSLogger.warn("Incorrect name " + name + ". expected " + EVENT_WORK_DELEGATION_NAME.getLocator());
                return false;
            }
            if (!item.equals(work.getName())) {
                PSLogger.warn("Incorrect item, expected : " + work.getName());
                return false;
            }
            String actual = getSDate();
            if (!actual.equals(expected.toString())) {
                PSLogger.warn("Incorrect date, expected : " + expected + "(" + expected.getDate() + ")" + ", really " + actual + "(" + actual + ")");
                return false;
            }
            return true;
        }

        public boolean isDelegationAccept(User from, User to, Work work, PSCalendar date) {
            boolean res = isDelegation(work, date);
            if (!author.equals(to.getFullName())) {
                PSLogger.warn("Incorrect author, expected : " + to.getFullName());
                return false;
            }
            String desc = EVENT_WORK_DELEGATION_ACCEPTED.replace(to.getFullName(), work.getType(), work.getName(), from.getFullName());
            if (!description.startsWith(desc)) {
                PSLogger.warn("Incorrect description, expected : " + desc + ", really " + description);
                return false;
            }
            return res;
        }

        public boolean isDelegationAsked(User from, User to, Work work, PSCalendar date) {
            boolean res = isDelegation(work, date);
            if (!author.equals(from.getFullName())) {
                PSLogger.warn("Incorrect author, expected : " + from.getFullName());
                return false;
            }
            String desc = EVENT_WORK_DELEGATION_ASKED.replace(from.getFullName(), to.getFullName(), work.getType(), work.getName());
            if (!description.startsWith(desc)) {
                PSLogger.warn("Incorrect description, expected : " + desc);
                return false;
            }
            return res;
        }

    }
}
