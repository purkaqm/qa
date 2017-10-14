package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.page_locators.AddEditMeasurePageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import com.powersteeringsoftware.libs.tests.core.PSKnownIssueException;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.AddEditMeasurePageLocators.*;
import static com.powersteeringsoftware.libs.enums.page_locators.MeasureTemplatesPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 23.10.11
 * Time: 18:08
 * To change this template use File | Settings | File Templates.
 */
public interface AddEditMeasurePage {
    public AbstractAddEditMeasureBlock getContent();

    /**
     * Created by IntelliJ IDEA.
     * User: szuev
     * Date: 19.10.11
     * Time: 15:56
     * todo: it was AbstractMeasureEditPageAdapter.
     */
    public static abstract class AbstractAddEditMeasureBlock extends Element {

        private PSPage page;

        AbstractAddEditMeasureBlock(PSPage page) {
            super(BLOCK);
            this.page = page;
        }

        public void setDefaultElement() {
            super.setDefaultElement(page.getDocument());
        }

        /**
         * Set data collection to Manual<br>
         * <b>Warning<b>. Before Using this method you must navigate to the page
         * Edit\Add MEasure using methods: addNewMEasureTemplate() or
         * editMEasureTemplate().
         */
        public void setDataCollectionManual() {
            PSLogger.info("Set data collection to manual");
            new ManualFormulaButton(true, DATA_COLLECTION_MANUAL).select();
        }

        /**
         * Set data collection to Formula<br>
         * <b>Warning<b>. Before Using this method you must navigate to the page
         * Edit\Add MEasure using methods: addNewMEasureTemplate() or
         * editMEasureTemplate().
         */
        public void setDataCollectionFormula() {
            PSLogger.info("Set data collection to formula");
            new ManualFormulaButton(true, DATA_COLLECTION_FORMULA).select();
        }

        private class ManualFormulaButton extends RadioButton {
            private Element body = new Element(FORMULA_BODY);
            private boolean is;

            public ManualFormulaButton(boolean isFormula, ILocatorable locator) {
                super(locator);
                is = isFormula;
            }

            public void click() {
                super.click();
                body.waitForClass(is ? SHOW_CLASS : HIDE_CLASS);
                AbstractAddEditMeasureBlock.this.setDefaultElement();
            }
        }

        /**
         * Set measure name <br>
         * <b>Warning<b>. Before Using this method you must navigate to the page
         * Edit\Add MEasure using methods: addNewMEasureTemplate() or
         * editMEasureTemplate().
         *
         * @param name -
         *             name of measure (template)
         */
        public void setName(String name) {
            if (StringUtils.isEmpty(name))
                throw new PSApplicationException("Measure name can't be null or empty");
            new Input(NAME).type(name);
        }

        /**
         * Set measure description <b>Warning<b>. Before Using this method you must
         * navigate to the page Edit\Add MEasure using methods:
         * addNewMEasureTemplate() or editMEasureTemplate().
         *
         * @param description -
         *                    description of measure (template)
         */
        public void setDescription(String description) {
            getDescriptionElement().setText(description);
        }

        private TextArea getDescriptionElement() {
            TextArea ta = new TextArea(DESCRIPTION);
            ta.setDefaultElement(page.getDocument());
            return ta;
        }

        /**
         * Set measure units <br>
         * <b>Warning<b>. Before Using this method you must navigate to the page
         * Edit\Add MEasure using methods: addNewMEasureTemplate() or
         * editMEasureTemplate().
         *
         * @param unit -
         *             measure units
         */
        public void setUnits(String units) {
            Assert.assertFalse(StringUtils.isEmpty(units), "Measure name can't be null or empty");
            new Input(UNITS).type(units);
        }

        /**
         * Set Display format<br>
         *
         * @param displayFormat -
         *                      the display format:<br>
         *                      0 - Integer<br>
         *                      1 - Float<br>
         *                      2 - Monetary<br>
         *                      3 - Percent<br>
         */
        public void setDisplayFormat(int displayFormat) {
            new SelectInput(DISPLAY_FORMAT_SELECT).selectValue(String.valueOf(displayFormat));
            switch (displayFormat) {
                case 1: // float
                case 3: // percent
                    setDisplayFormatPrecision(2);
                    break;
                case 2: // monetary
                    setDisplayFormatPrecision(2);
                    setDisplayFormatScale(2);
                    break;
                default: // integer
                    break;
            }
        }

        /**
         * Set Precision for Float/Monetary format<br>
         *
         * @param precision
         */
        public void setDisplayFormatPrecision(int precision) {
            new SelectInput(DISPLAY_FORMAT_PRECISION_SELECT).selectValue(String.valueOf(precision));
        }

        /**
         * Set Scale for Monetary Format<br>
         *
         * @param scale
         */
        public void setDisplayFormatScale(int scale) {
            new SelectInput(DISPLAY_FORMAT_SCALE_SELECT).selectValue(String.valueOf(scale));
        }

        /**
         * Set Effective Dates<br>
         *
         * @param effectiveDate <br>
         *                      0 - absolute (from startDate to endDate)<br>
         *                      1 - active project<br>
         *                      2 - Project Lifetime<br>
         *                      3 - Baseline\Target Dates<br>
         *                      4 - Always<br>
         * @param startDate     -
         *                      Start date of effective period, format: 2009/01/20 (dataPicker
         *                      format)<br>
         * @param endDate       -
         *                      End date, format: 2009/01/20 (dataPicker format)<br>
         */
        public void setEffectiveDates(int effectiveDate, String startDate,
                                      String endDate) {
            new SelectInput(EFFECTIVE_DATES_SELECT).selectValue(String.valueOf(effectiveDate));
            if (effectiveDate == 0) {
                DatePicker start = new DatePicker(EFFECTIVE_DATES_ABSOLUTE_START);
                DatePicker end = new DatePicker(EFFECTIVE_DATES_ABSOLUTE_END);
                start.useDatePopup(false);
                end.useDatePopup(false);
                start.set(startDate);
                end.set(endDate);
            }
        }

        public List<ScheduleElement> getSchedules() {
            List<ScheduleElement> res = new ArrayList<ScheduleElement>();
            for (Element e : Element.searchElementsByXpath(this, SCHEDULES_TR)) {
                res.add(new ScheduleElement(e));
            }
            return res;
        }

        private ScheduleElement getSchedule(ILocatorable loc) {
            for (ScheduleElement s : getSchedules()) {
                if (s.getTitle().startsWith(loc.getLocator())) {
                    return s;
                }
            }
            return null;
        }

        public ScheduleElement getTestSchedule() {
            return getSchedule(TEST_SCHEDULE);
        }

        public ScheduleElement getHistorySchedule() {
            return getSchedule(HISTORY_SCHEDULE);
        }

        public ScheduleElement getReminderSchedule() {
            return getSchedule(REMINDER_SCHEDULE);
        }

        /**
         * Set Indicator Type on the page 'Measure Template\Measure Edit\Add new'.<br>
         *
         * @param pageFieldId -
         *                    field id for selecting Indicator Type
         * @see com.powersteeringsoftware.tests.core.pages.field.id.PageFieldIdsForMeasureTemplate *
         */

        public void setIndicatorTypeGoal() {
            new RadioButton(INDICATOR_TYPE_GOAL).click();
        }

        public void setIndicatorTypeVariance() {
            new RadioButton(INDICATOR_TYPE_VARIANCE).click();
        }

        public void setIndicatorTypeNone() {
            new RadioButton(INDICATOR_TYPE_NONE).click();
        }


        public double getThresholdVariance1() {
            String value = new Input(THRESHOLD_VARIANCE_1).getValue();
            return Double.parseDouble(value);
        }

        /**
         * Set Threshold 1 for Variance Indicator type<br>
         */
        public void setThresholdVariance1(double value) {
            new Input(THRESHOLD_VARIANCE_1).type(String.valueOf(value));
        }

        /**
         * Set Threshold 2 for Variance Indicator type<br>
         */
        public void setThresholdVariance2(double value) {
            new Input(THRESHOLD_VARIANCE_2).type(String.valueOf(value));
        }

        public Double getThresholdVariance2() {
            String value = new Input(THRESHOLD_VARIANCE_2).getValue();
            return Double.parseDouble(value);
        }

        /**
         * Set Threshold 1 for Variance Indicator type<br>
         */
        public void setThresholdVariance3(double value) {
            new Input(THRESHOLD_VARIANCE_3).type(String.valueOf(value));
        }

        public double getThresholdVariance3() {
            String value = new Input(THRESHOLD_VARIANCE_3).getValue();
            return Double.parseDouble(value);
        }

        /**
         * Set Threshold 1 for Variance Indicator type<br>
         */
        public void setThresholdVariance4(double value) {
            new Input(THRESHOLD_VARIANCE_4).type(String.valueOf(value));
        }

        public double getThresholdVariance4() {
            String value = new Input(THRESHOLD_VARIANCE_4).getValue();
            return Double.parseDouble(value);
        }


        /**
         * Set High Threshold for Goal Indicator type<br>
         */
        public void setThresholdGoal1(double value) {
            new Input(THRESHOLD_GOAL_1).type(String.valueOf(value));
        }

        public double getThresholdGoal1() {
            String value = new Input(THRESHOLD_GOAL_1).getValue();
            return Double.parseDouble(value);
        }

        /**
         * Set Low Threshold for Goal Indicator type<br>
         */
        public void setThresholdGoal2(double value) {
            new Input(THRESHOLD_GOAL_2).type(String.valueOf(value));
        }

        public double getThresholdGoal2() {
            String value = new Input(THRESHOLD_GOAL_2).getValue();
            return Double.parseDouble(value);
        }

        /**
         * Return data collection is Manual<br>
         */
        public boolean isDataCollectionManual() {
            return new RadioButton(DATA_COLLECTION_MANUAL).getChecked();
        }

        /**
         * Return data collection is Formula<br>
         */
        public boolean isDataCollectionFormula() {
            return new RadioButton(DATA_COLLECTION_FORMULA).getChecked();
        }

        /**
         * Get current data collection value
         *
         * @return true DataCollection.Manual - if data collection=manual,
         *         false DataCollection.Formula - if data collection=formula
         */
        public boolean getDataCollectionType() {
            boolean isManual = isDataCollectionManual();
            boolean isFormula = isDataCollectionFormula();
            Assert.assertNotSame(isManual, isFormula, "We can't have both manual and formula data collection types");
            return isManual;
        }

        /**
         * Check if indicator type is goal
         *
         * @return true if indicator type is goal, false - otherwise
         */
        public boolean isIndicatorTypeGoal() {
            return new RadioButton(INDICATOR_TYPE_GOAL).getChecked();
        }

        /**
         * Check if indicator type is goal
         *
         * @return true if indicator type is goal, false - otherwise
         */
        public boolean isIndicatorTypeVariance() {
            return new RadioButton(INDICATOR_TYPE_VARIANCE).getChecked();
        }

        public Measure.IndicatorType getIndicatorTypeType() {
            boolean isGoal = isIndicatorTypeGoal();
            boolean isVariance = isIndicatorTypeVariance();
            // measure can't be both manual and formula
            Assert.assertNotSame(isGoal, isVariance, "We can't have both goal and variance indicator types");
            if (isGoal) {
                return Measure.IndicatorType.GOAL;
            }
            if (isVariance) {
                return Measure.IndicatorType.VARIANCE;
            }
            return Measure.IndicatorType.NONE; // none
        }


        public String getName() {
            return new Input(NAME).getValue();
        }

        /**
         * Get description value
         *
         * @return description value
         */
        public String getDescription() {
            return getDescriptionElement().getText();
        }


        /**
         * Cancel changes on the page with upper button "Cancel"<br>
         * Page will reloaded but you mustn't use Selenium method
         * waitForPageToLoad(...) after invoking current method
         */
        public void cancelChangesWithUpperButton() {
            PSLogger.info("click upper button Cancel");
            new Button(CANCEL_TOP).click(true);
        }

        /**
         * Cancel changes on the page with bottom button "Cancel"<br>
         * Page will reloaded but you mustn't use Selenium method
         * waitForPageToLoad(...) after invoking current method
         */
        public void cancelChangesWithBottomButton() {
            PSLogger.debug("click upper bottom Cancel");
            new Button(CANCEL_BOTTOM).click(true);
        }

        /**
         * Submit changes on the page with upper button "Submit"<br>
         * Page will reloaded.<br>
         * You mustn't use Selenium method waitForPageToLoad(...) after invoking
         * current method
         */
        protected void submitWithoutDialog() {
            PSLogger.save("before submitting");
            Button bt = new Button(SUBMIT_BUTTON_ELEMENT);
            bt.submit();
            String error = page.getErrorBoxMessage();
            if (getDriver().getType().isIE(9) && VALIDATION_ERROR_1.getLocator().equals(error)) {
                throw new PSKnownIssueException(75335);
            }
            Assert.assertNull(error, "Have errors : " + error);
        }

        public abstract MeasuresPage submit();


        /**
         * Get value for field Units
         *
         * @return
         */
        public String getUnits() {
            return new Input(UNITS).getValue();
        }

        /**
         * Get value for select box Display Format: 0-value<br>
         * 1-float<br>
         * 2-monetary<br>
         * 3-percent<br>
         *
         * @return index of the selected display format
         */
        public int getDisplayFormat() {
            String res = new SelectInput(DISPLAY_FORMAT_SELECT).getSelectedValue();
            return Integer.parseInt(res);
        }

        /**
         * Get value for select box Effective Dates
         * <p/>
         * 0 - Absolute<br>
         * 1 - Active Project<br>
         * 2 - Project Lifetime<br>
         * 3 - Baseline/Target Dates<br>
         * 4 - Always<br>
         *
         * @return index of the selected effective date
         */
        public int getEffectiveDates() {
            String value = new SelectInput(EFFECTIVE_DATES_SELECT).getSelectedValue();
            return Integer.parseInt(value);
        }

        public class ScheduleElement extends Element {

            private ScheduleElement(Element e) {
                super(e);
            }

            public String getTitle() {
                Element e = getChildByXpath(SCHEDULE_TH);
                String res = e.getDEText();
                if (!res.isEmpty()) return res.replace(":", "");
                return getChildByXpath(SCHEDULE_TH_DIV).getDEText().replace(":", "");
            }

            public List<RadioButton> getRadioButtons() {
                List<RadioButton> res = new ArrayList<RadioButton>();
                for (Element e : Element.searchElementsByXpath(this, SCHEDULE_RADIO)) {
                    res.add(new ScheduleRadioButton(e));
                }
                return res;
            }

            public RadioButton getRadioButton(Schedule type) {
                for (RadioButton rb : getRadioButtons()) {
                    if (((ScheduleRadioButton) rb).getType().equals(type)) return rb;
                }
                return null;
            }

            public void setDaily() {
                getRadioButton(Schedule.DAILY).select();
            }

            public void setNever() {
                getRadioButton(Schedule.NEVER).select();
            }


            public ScheduleRadioButton getSelectedRadioButton() {
                for (RadioButton rb : getRadioButtons()) {
                    if (rb.getChecked()) return (ScheduleRadioButton) rb;
                }
                return null;
            }

            public Schedule getSelectedType() {
                ScheduleRadioButton rb = getSelectedRadioButton();
                Assert.assertNotNull(rb, "Nothing selected");
                return rb.type;
            }


            public void setDefaultElement() {
                AbstractAddEditMeasureBlock.this.setDefaultElement();
                setDefaultElement(page.getDocument(false));
            }

            protected Input getDailyDayInput() {
                Element e = getChildByXpath(SCHEDULE_DAILY_INPUT);
                if (e.isDEPresent() && e.exists() && e.isVisible()) return new Input(e) {
                    public void type(String txt) {
                        super.type(txt);
                        mouseDownAndUp(); // this is for ie9:!
                        AbstractAddEditMeasureBlock.this.mouseDownAndUp();
                    }
                };
                return null;
            }

            public void setDailyEvery(Integer every) {
                if (every == null) return;
                Input in = getDailyDayInput();
                Assert.assertNotNull(in, "Can't find daily every input");
                PSLogger.info("Set day to " + every + " for " + getTitle());
                in.type(String.valueOf(every));
            }

            public Integer getDailyEvery() {
                Input in = getDailyDayInput();
                if (in == null) return null;
                String v = in.getValue();
                return Integer.parseInt(v);
            }

            public void setTimeOfDay(Integer min) {
                if (min == null) return;
                String label = AddEditMeasurePageLocators.getTimeOfDay(min);
                ComboBox cb = getTimeOfDayComboBox();
                Assert.assertNotNull(cb, "Can't find time-of-day combobox");
                cb.select(label);
            }

            public Integer getTimeOfDay() {
                ComboBox cb = getTimeOfDayComboBox();
                Assert.assertNotNull(cb, "Can't find time-of-day combobox");
                String val = cb.getValue();
                return AddEditMeasurePageLocators.getMinOfDay(val);
            }

            public Element getBlock() {
                return getSelectedRadioButton().getBlock();
            }

            public ComboBox getTimeOfDayComboBox() {
                Element e = getBlock().getChildByXpath(SCHEDULE_COMBOBOX);
                if (!e.isDEPresent()) return null;
                return new ComboBox(e);
            }

            private class ScheduleRadioButton extends RadioButton {
                private Schedule type;
                private Element block;

                private ScheduleRadioButton(Element e) {
                    super(e);
                    String label = e.getParent().getChildByXpath(SCHEDULE_RADIO_LABEL).getDEText();
                    setName(label);
                    int value = Integer.parseInt(getDEAttribute(SCHEDULE_RADIO_VALUE));
                    for (Schedule s : Schedule.values()) {
                        if (s.getIndex() == value) {
                            type = s;
                            break;
                        }
                    }
                    block = new Element(type.getBlockLoc());
                    block.setDefaultElement(page.getDocument(false));
                }

                public void click() {
                    super.click();
                    new TimerWaiter(500).waitTime();
                    block.waitForVisible();
                    ScheduleElement.this.setDefaultElement();
                    block.setDefaultElement(page.getDocument());
                }

                public Element getBlock() {
                    return block;
                }

                public Schedule getType() {
                    return type;
                }
            }
        }

    }
}
