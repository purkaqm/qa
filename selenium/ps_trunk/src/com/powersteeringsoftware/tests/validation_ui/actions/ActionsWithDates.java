package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.powersteeringsoftware.libs.core.SeleniumDriverFactory.getDriver;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.06.2010
 * Time: 17:55:25
 */
public class ActionsWithDates {
    private static final int LOWER_DATE_DAYS = -1 * 364;
    private static final int UPPER_DATE_DAYS = 1 * 364;
    private static final String WRONG_INPUT = "XXX";
    private static final int NUMBER_DATES_IN_MULTI_SELECTOR = 6;

    private TestData data;
    private UIPage ui;

    public ActionsWithDates(UIPage ui, TestData data) {
        this.ui = ui;
        this.data = data;
    }

    public void testDatePickerFirst(DatePicker dp) {
        String date1 = getRandStringDate();
        String date2 = getRandStringDate();
        String date3 = getRandStringDate();
        //DatePicker dp = ui.getDatePickerFirst();
        checkFirstTypeOfPopup(dp, date1, date2);
        checkInput(dp, date3, WRONG_INPUT);
    }

    public void testDatePickerSecond() {
        Calendar date = getRandDate();
        DatePicker dp = ui.getDatePickerSecond();
        PSLogger.info("Set date " + getStringDate(date) + " to date period picker");
        dp.set(date.getTimeInMillis());
        Assert.assertFalse(dp.isWrongInput(), "Wrong input");
        String sRes = dp.get();
        Calendar res = getCalendarDate(sRes);
        Assert.assertEquals(res.get(Calendar.YEAR), date.get(Calendar.YEAR), "Incorrect result after setting: " + sRes);
        Assert.assertEquals(res.get(Calendar.MONTH), date.get(Calendar.MONTH), "Incorrect result after setting: " + sRes);

        String date3 = getRandStringDate();
        checkInput(dp, date3, WRONG_INPUT);
    }

    public void testDatePickerInline() {
        String date1 = getRandStringDate();
        String date2 = getRandStringDate();
        DatePicker dp = ui.getInlineDatePicker();
        checkFirstTypeOfPopup(dp, date1, date2);
    }

    public void testDateRangeTypeSelection() {
        DateRangeTypeSelection select = ui.getDateRangeTypeSelection();
        select.scrollTo();
        List<String> options = select.getSelectOptions();
        PSLogger.info("All options " + options);
        Assert.assertTrue(options.size() != 0, "No options found for DateRangeTypeSelection element");
        for (int i = options.size() - 1; i > 0; i--) {
            select.select(options.get(i));
            Assert.assertEquals(select.getFinishDatePicker(), null, "There is finish date picker for option " + options.get(i));
            Assert.assertEquals(select.getStartDatePicker(), null, "There is start date picker for option " + options.get(i));
        }
        select.select(options.get(0));
        Assert.assertNotNull(select.getStartDatePicker(), "Can't find start date picker for option " + options.get(0));
        Assert.assertNotNull(select.getFinishDatePicker(), "Can't find finish date picker for option " + options.get(0));
        testDatePickerFirst(select.getStartDatePicker());
        testDatePickerFirst(select.getFinishDatePicker());
    }

    public void testMultiDateSelector() {
        MultiDateSelector mds = ui.getMultiDatePicker();
        List<String> dates = new ArrayList<String>();
        Boolean[][] howtoset = new Boolean[NUMBER_DATES_IN_MULTI_SELECTOR][2];
        for (int i = 0; i < NUMBER_DATES_IN_MULTI_SELECTOR; i++) {
            dates.add(getRandStringDate());
            howtoset[i][0] = TestData.getRandom().nextBoolean();
            howtoset[i][1] = TestData.getRandom().nextBoolean();
        }
        PSLogger.info("Dates to set: " + dates);
        mds.set(dates, howtoset);
        List<String> res = mds.get();
        PSLogger.info("List from page: " + res);
        PSLogger.info("Expected: " + dates);
        Assert.assertEquals(res, dates, "Incorrect dates after setting");
        //chek delete
        int index1 = NUMBER_DATES_IN_MULTI_SELECTOR / 2;
        int index2 = 0; // last element
        mds.remove(index2);
        mds.remove(index1);
        dates.remove(index2);
        dates.remove(index1);
        List<String> res2 = mds.get();
        PSLogger.info("List from page: " + res2);
        PSLogger.info("Expected: " + dates);
        Assert.assertEquals(res2, dates, "Incorrect dates after deleting");
    }

    private void checkFirstTypeOfPopup(DatePicker dp, String date1, String date2) {
        PSLogger.info("Set date " + date1 + " to date picker using popup and drop-down subpopup");
        dp.useDatePopup(true);
        dp.useDropDownOrArrows(true);
        dp.set(date1);
        Assert.assertFalse(dp.isWrongInput(), "Wrong input");
        Assert.assertEquals(dp.get(), date1, "Incorrect date after setting with popup and subpopup");

        PSLogger.info("Set date " + date2 + " to date picker using popup and arrows in it");
        dp.useDatePopup(true);
        dp.useDropDownOrArrows(false);
        dp.set(date2);
        Assert.assertFalse(dp.isWrongInput(), "Wrong input");
        Assert.assertEquals(dp.get(), date2, "Incorrect date after setting with popup and arrows");
    }

    private void checkInput(DatePicker dp, String date1, String date2) {
        PSLogger.info("Set date " + date1 + " to date picker using input");
        dp.useDatePopup(false);
        dp.set(date1);
        Assert.assertFalse(dp.isWrongInput(), "Wrong input");
        Assert.assertEquals(dp.get(), date1, "Incorrect date after setting with input");

        PSLogger.info("Set date " + date2 + " to date picker using input");
        dp.useDatePopup(false);
        dp.set(date2);
        Assert.assertTrue(dp.isWrongInput(), "No errors with incorrect input");
    }


    /**
     * @return Calendar with random date between -50, + 50 years
     */
    private Calendar getRandDate() {
        Integer days = TestData.getRandom().nextInt(UPPER_DATE_DAYS - LOWER_DATE_DAYS) + LOWER_DATE_DAYS;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(c.getTimeInMillis() + days * 24 * 60 * 60 * 1000L);
        return c;
    }

    private String getRandStringDate() {
        return getStringDate(getRandDate());
    }

    private static String getStringDate(Calendar c) {
        return new SimpleDateFormat(CoreProperties.getDateFormat()).format(c.getTimeInMillis());
    }

    private static Calendar getCalendarDate(String txt) {
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(new SimpleDateFormat(CoreProperties.getDateFormat()).parse(txt));
        } catch (ParseException e) {
            Assert.fail("Can't parse input", e);
        }
        return c;
    }


    public void testHolidaySelector() {
        List<String> names = data.getTextAreaData();
        names.add(TestData.getRandom().nextInt(names.size()), "");
        List<ASelector.ARowItem> items = new ArrayList<ASelector.ARowItem>();
        //generate list:
        for (String name : names) {
            HolidaySelector.RowItem item = new HolidaySelector.RowItem();
            item.name = name;
            item.date = getRandStringDate();
            item.howtoset = new boolean[2];
            item.howtoset[0] = TestData.getRandom().nextBoolean();
            item.howtoset[1] = getDriver().getType().isIE() || TestData.getRandom().nextBoolean();
            items.add(item);
        }
        testSelectorLikeHoliday(ui.getHolidaySelector(), items);
    }


    public void testNonWorkDaysSelector() {
        List<String> names = data.getTextAreaData();
        List<ASelector.ARowItem> items = new ArrayList<ASelector.ARowItem>();
        //generate list:
        int randIndex0 = TestData.getRandom().nextInt(names.size());
        names.add(randIndex0, "");
        for (String name : names) {
            NonWorkDaysSelector.RowItem item = new NonWorkDaysSelector.RowItem(name, getRandStringDate(), getRandStringDate());
            boolean[] howtoset1 = new boolean[2];
            howtoset1[0] = TestData.getRandom().nextBoolean();
            howtoset1[1] = getDriver().getType().isIE() || TestData.getRandom().nextBoolean();
            boolean[] howtoset2 = new boolean[2];
            howtoset2[0] = TestData.getRandom().nextBoolean();
            howtoset2[1] = getDriver().getType().isIE() || TestData.getRandom().nextBoolean();
            item.setHowToSet(howtoset1, howtoset2);
            items.add(item);
        }
        int randIndex1 = TestData.getRandom().nextInt(items.size());
        int randIndex2 = TestData.getRandom().nextInt(items.size());
        while (randIndex2 == randIndex1) {
            randIndex2 = TestData.getRandom().nextInt(items.size());
        }
        // set empty date from
        ((NonWorkDaysSelector.RowItem) items.get(randIndex1)).setStartDate("");
        // set empty date to
        ((NonWorkDaysSelector.RowItem) items.get(randIndex2)).setEndDate("");

        // add duplicate date:
        int randIndex3 = TestData.getRandom().nextInt(items.size());
        int randIndex4 = TestData.getRandom().nextInt(items.size());
        NonWorkDaysSelector.RowItem item = (NonWorkDaysSelector.RowItem) items.get(randIndex3);
        NonWorkDaysSelector.RowItem dupItem = new NonWorkDaysSelector.RowItem("DUPLICATE", item.getStartDate(), item.getEndDate());
        boolean[] howtoset1 = new boolean[2];
        howtoset1[0] = TestData.getRandom().nextBoolean();
        howtoset1[1] = getDriver().getType().isIE() || TestData.getRandom().nextBoolean();
        boolean[] howtoset2 = new boolean[2];
        howtoset2[0] = TestData.getRandom().nextBoolean();
        howtoset2[1] = getDriver().getType().isIE() || TestData.getRandom().nextBoolean();
        dupItem.setHowToSet(howtoset1, howtoset2);
        items.add(randIndex4, dupItem);
        PSLogger.info("Test Data : " + items);

        testSelectorLikeHoliday(ui.getNonWorkDaysSelector(), items);
    }

    private void testSelectorLikeHoliday(ASelector selector, List<ASelector.ARowItem> items) {
        PSLogger.info("Test date: " + items);
        selector.setList(items);
        List<ASelector.ARowItem> fromPage = selector.getList();
        PSLogger.info("From page: " + fromPage);
        PSLogger.info("Expected List: " + items);
        Assert.assertEquals(fromPage, items, "Incorrect list of items from page, should be " + items);

        //chek delete
        int index1 = items.size() / 2;
        int index2 = items.size() - 1; // last element
        selector.remove(index2);
        selector.remove(index1);
        items.remove(index2);
        items.remove(index1);
        List<ASelector.ARowItem> fromPage2 = selector.getList();
        PSLogger.info("List from page after deleting: " + fromPage2);
        PSLogger.info("Expected List after deleting: " + items);
        Assert.assertEquals(fromPage2, items, "Incorrect list of items from page after removing, should be " + items);

    }
}
