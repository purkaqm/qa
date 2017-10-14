package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.enums.page_locators.MeasureTemplatesPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.tests.core.PSApplicationException;
import org.testng.Assert;

import static com.powersteeringsoftware.libs.enums.page_locators.MeasureTemplatesPageLocators.*;

public class AddEditMeasureTemplatePage extends PSPage implements AddEditMeasurePage {

    private Content content;

    public Content getContent() {
        if (content == null) content = new Content();
        return content;
    }

    @Override
    public void open() {
        //TODO
        throw new IllegalMonitorStateException("TODO");
    }


    @Deprecated // rewrite it
    public class Content extends AddEditMeasureInstancePage.AbstractAddEditMeasureBlock {

        private boolean isEditPage;

        public void setEditPage(boolean editPage) {
            isEditPage = editPage;
        }

        private Content() {
            super(AddEditMeasureTemplatePage.this);
        }

        /**
         * Assert loading page Measure Add\edit.
         * <p/>
         * <b>Warning<b>. Before Using this method you must navigate to the page
         * Edit\Add MEasure using methods: addNewMEasureTemplate() or
         * editMEasureTemplate().
         */
        public void assertIsLoadedPageMTEdit() {
            Assert.assertEquals(true,
                    isLoadedPageEditMT(),
                    "You must navigate Measure Template:Add\\Edit page.");
        }

        /**
         * Get owner index
         *
         * @return 0- if template owner, 1 - in other cases
         */
        public int getEvaluateAs() {
            assertIsLoadedPageMTEdit();

            Assert
                    .assertNotSame(isTemplateOwner(), isProjectOwner(),
                            "We can't have both checked or unchecked owners: project and template.");
            return (isTemplateOwner()) ? 0 : 1;
        }

        /**
         * Check if locking checkbox is checked. This method can be run only on the
         * page Measure Template: Edit/Add<br>
         *
         * @param checkboxNumber -
         *                       the checkbox number, can be one of the follows:<br>
         * @return true - if checkbox is checked, false - in other cases.
         */
        public boolean isCheckedLockedCheckbox(
                MeasureTemplatesPageLocators.MeasureLockedFieldsEnum lockedField) {
            assertIsLoadedPageMTEdit();
            return SeleniumDriverFactory.getDriver().isChecked(
                    lockedField.getFieldId());
        }

        /**
         * Uncheck all locked fields
         */
        public void unlockAllFields() {
            assertIsLoadedPageMTEdit();

            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.NAME);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DESCRIPTION);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.UNITS);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DATA_COLLECTION);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DISPLAY_FORMAT);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.EFFECTIVE_DATES);

            Measure.IndicatorType it = getIndicatorTypeType();
            if (it.equals(Measure.IndicatorType.GOAL)) {
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.GOAL_MESSAGES);
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.GOAL_THRESHOLDS);
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.START_DATE);
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TARGET_DATE);
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TARGET_VALUE);
            } else if (it.equals(Measure.IndicatorType.VARIANCE)) {
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.VARIANCE_MESSAGES);
                setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.VARIANCE_THRESHOLDS);
            }


            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.REMINDER_SHEDULE);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.FORMULA);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TEST_SCHEDULE);
            setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.HISTORY_SCHEDULE);

        }


        /**
         * Check locking checkbox by number. This method can be run only on the page
         * Measure Template: Edit/Add<br>
         *
         * @param checkboxNumber -
         *                       the checkbox number, can be one of the follows:<br>
         */
        private void checkLockingCheckbox(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum lockedFields) {
            PSLogger.info("Lock field " + lockedFields.name());
            getDriver().check(lockedFields.getFieldId());
        }

        /**
         * Check locking checkbox
         *
         * @param checkboxNumber -
         *                       the checkbox number
         */
        public void setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum lockedField) {
            checkLockingCheckbox(lockedField);
        }

        /**
         * Lock all visible fields
         */
        public void lockAllFields() {
            PSLogger.info("Lock all fields");

            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.NAME);
            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DESCRIPTION);
            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.UNITS);
            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DATA_COLLECTION);
            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.DISPLAY_FORMAT);
            setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.EFFECTIVE_DATES);

            try {
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.REMINDER_SHEDULE);
            } catch (Exception e) {
                PSLogger.fatal("Can't check locked field", e);
            }
            try {
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.FORMULA);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TEST_SCHEDULE);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.HISTORY_SCHEDULE);
            } catch (Exception e) {
                PSLogger.fatal("Can't check locked field", e);
            }

            Measure.IndicatorType it = getIndicatorTypeType();
            if (it.equals(Measure.IndicatorType.GOAL)) {
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.GOAL_MESSAGES);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.GOAL_THRESHOLDS);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.START_DATE);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TARGET_DATE);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.TARGET_VALUE);
            } else if (it.equals(Measure.IndicatorType.VARIANCE)) {
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.VARIANCE_MESSAGES);
                setLockingCheckboxChecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum.VARIANCE_THRESHOLDS);
            }
        }

        public boolean isTemplateOwner() {

            return SeleniumDriverFactory.getDriver().isChecked(
                    "dom= window.dojo.byId('template')");
        }

        public boolean isProjectOwner() {

            return SeleniumDriverFactory.getDriver().isChecked(
                    "dom= window.dojo.byId('project')");
        }

        /**
         * @return
         */
        public boolean isLoadedPageEditMT() {
            return SeleniumDriverFactory.getDriver().isTextPresent(
                    "Edit Measure Template");
        }

        /**
         * Set Evaluate as<br>
         * We have to set data collection = formula before invoking this method
         *
         * @param owner: 0 - Template Owner, 1 - Project Owner
         */
        public void setEvaluateAs(int owner) {

            Assert.assertEquals(isDataCollectionFormula(), true,
                    "We have to set data collection = formula");
            Assert.assertSame(isDataCollectionFormula(), true,
                    "Data collections must be Formula");
            if (owner == 1) {
                SeleniumDriverFactory.getDriver().click(
                        "dom=window.dojo.byId('template')");
            } else if (owner == 0) {
                SeleniumDriverFactory.getDriver().click(
                        "dom=window.dojo.byId('project')");
            } else {
                throw new PSApplicationException(
                        "You can use only values 1 or 0 for parameter owner");
            }
        }

        /**
         * Uncheck locking checkbox
         *
         * @param checkboxNumber -
         */
        public void setLockingCheckboxUnchecked(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum lockedField) {
            uncheckLockingCheckbox(lockedField);
        }


        /**
         * Uncheck locking checkbox by number. This method can be run only on the
         * page Measure Template: Edit/Add<br>
         *
         * @param checkboxNumber -
         *                       the checkbox number, can be one of the follows:<br>
         */
        private void uncheckLockingCheckbox(MeasureTemplatesPageLocators.MeasureLockedFieldsEnum lockedFields) {
            SeleniumDriverFactory.getDriver().uncheck(lockedFields.getFieldId());
        }

        @Override
        public MeasuresPage submit() {
            doSubmit();
            return new MeasureTemplatesPage();
        }

        private void doSubmit() {
            if (isEditPage) {
                submitWithDialog();
            } else {
                submitWithoutDialog();
            }
        }

        /**
         * Submit changes and push "Yes" in the dialog "Save Measure"\"Save Measure
         * Template" that appears on pushing button "Submit".<br>
         * Use this method only after editing existed measure template<br>
         * Page will reloaded but you mustn't use Selenium method
         * waitForPageToLoad(...) after invoking current method
         */
        private void submitWithDialog() {
            new Button(SUBMIT_BUTTON_ELEMENT).click(false);
            new Element(CONFIRM_DIALOG_LOCATOR).waitForVisible();
            new Button(CONFIRM_DIALOG_YES_LOCATOR).submit();
        }
    }
}

