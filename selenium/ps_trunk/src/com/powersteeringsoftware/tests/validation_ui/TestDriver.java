package com.powersteeringsoftware.tests.validation_ui;

import com.powersteeringsoftware.libs.elements.DisplayTextBox;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.tests.validation_ui.actions.*;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.06.2010
 * Time: 17:48:35
 */
@Test(groups = {TestSettings.PS_82_GROUP})
public class TestDriver extends PSTestDriver {
    private static final String TAGS_GROUP = "tags";
    private static final String TEXBOXES_GROUP = "text-box";

    private static boolean doRefresh = true;
    private static int testCount;

    private UIPage ui = new UIPage();
    private ActionsWithDates dateActions = new ActionsWithDates(ui, getTestData());
    private ActionsColor colorActions = new ActionsColor(ui);
    private ActionsTagChooser tagSelectorActions = new ActionsTagChooser(ui, getTestData());
    private ActionsMenu menuActions = new ActionsMenu(ui, getTestData());
    private ActionsInputs inputsActions = new ActionsInputs(ui, getTestData());
    private ActionsComboBox comboboxActions = new ActionsComboBox(ui, getTestData());
    private ActionsSimpleElements simpleElementsActions = new ActionsSimpleElements(ui, getTestData());

    private TestData data;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

    @BeforeTest(alwaysRun = true, timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void before() {
        ui.open();
    }

    @BeforeMethod(alwaysRun = true, timeOut = 600000) // 10 min for refreshing?
    public void refreshPage() throws Exception {
        PSLogger.save("before test-case");
        try {
            if (!ui.reopen()) {
                // refresh page if url is correct (current ui):
                if (doRefresh && testCount++ != 0) {
                    ui.refresh();
                }
            }
        } catch (RuntimeException e) {
            if (!(e instanceof Wait.WaitTimedOutException) || !(e instanceof SeleniumException)) throw e;
            PSLogger.save("Exception in validation-ui, before method");
            PSLogger.fatal(e);
            BasicCommons.reLogIn();
            ui.open();
        }
    }

    @Test(description = "Check First Date Picker")
    public void checkDatePicker() {
        dateActions.testDatePickerFirst(ui.getDatePickerFirst());
    }

    @Test(description = "Check Second Date Picker (Period)")
    public void checkDatePickerPeriod() {
        dateActions.testDatePickerSecond();
    }

    @Test(description = "Check Inline Date Picker")
    public void checkDatePickerInline() {
        dateActions.testDatePickerInline();
    }

    @Test(description = "Check Second inline Date Picker")
    public void checkSecondDatePickerInline() {
        dateActions.testDatePickerFirst(ui.getSecondInlineDatePicker());
    }

    @Test(description = "Check DateRangeTypeSelection element")
    public void checkDateRangeTypeSelection() {
        dateActions.testDateRangeTypeSelection();
    }

    @Test(description = "Check Multi date selector")
    public void checkMultiDateSelector() {
        dateActions.testMultiDateSelector();
    }

    @Test(description = "Validate color selector")
    public void checkColorPaletteSelector() {
        colorActions.testColorPaletteSelector();
    }

    @Test(description = "Validate simple general tag chooser", groups = TAGS_GROUP)
    public void checkSimpleGeneralTagChooser() {
        tagSelectorActions.testSimpleTagChooser("1");
    }

    @Test(description = "Validate simple flat tag chooser", groups = TAGS_GROUP)
    public void checkSimpleFlatTagChooser() {
        tagSelectorActions.testFlatTagChooser("2");
    }

    @Test(description = "Validate multiple flat tag chooser", groups = TAGS_GROUP)
    public void checkMultipleFlatTagChooser() {
        tagSelectorActions.testFlatTagChooser("3");
    }

    @Test(description = "Validate multiple tag chooser with searching", groups = TAGS_GROUP)
    public void checkMultipleGeneralTagChooser() {
        tagSelectorActions.testMultipleTagChooser("4");
    }


    @Test(description = "Validate hierarchical tag chooser", groups = TAGS_GROUP)
    public void checkFirstHierarchicalTagChoosers() {
        tagSelectorActions.testHierarchicalTagChooser("5");
    }

    @Test(description = "Validate hierarchical tag chooser", groups = TAGS_GROUP)
    public void checkSecondHierarchicalTagChoosers() {
        tagSelectorActions.testHierarchicalTagChooser("6");
    }

    //@Test(description = "Validate tag dependencies element", groups = TAGS_GROUP)
    //todo: temporary disable this tc.
    //public void checkTagDependencies() {
    //    tagSelectorActions.testTagsWithDependencies(ui.getTagsDependenciesElement(), "TD");
    //}

    @Test(description = "Validate tooltips")
    public void checkTooltips() {
        simpleElementsActions.testTooltips();
    }

    @Test(description = "Validate menus")
    public void checkMenus() {
        for (int i = 1; i < 5; i++) {
            menuActions.testMenu(i);
        }
        menuActions.testSubmenu();
    }

    @Test(description = "Validate dialog")
    public void checkDialog() {
        simpleElementsActions.testDialog();
    }

    @Test(description = "Check Slider")
    public void checkSlider() {
        simpleElementsActions.testSlider(TestData.ATTEMPTS_NUMBER);
    }

    @Test(description = "Check Text Area")
    public void checkCounterTextArea() {
        inputsActions.testTextArea();
    }

    @Test(description = "Check inputs")
    public void checkTextboxes() {
        inputsActions.testTextbox();
        inputsActions.testValidationTextbox();
    }

    @Test(description = "Check number inputs", timeOut = 2000000, groups = TEXBOXES_GROUP)
    public void checkNumberTextboxes() {
        inputsActions.testFirstNumberTextbox(ui.getNumberTextbox(1));
        inputsActions.testFirstNumberTextbox(ui.getNumberTextbox(2));
        inputsActions.testFirstNumberTextbox(ui.getNumberTextbox(4));
    }

    @Test(description = "Check percentage textbox", groups = TEXBOXES_GROUP)
    public void checkPercentageTextbox() {
        inputsActions.testFirstNumberTextbox(ui.getNumberTextbox(5));
    }

    @Test(description = "Check display textbox", groups = TEXBOXES_GROUP)
    public void checkDisplayTextbox() {
        inputsActions.testDisplayTextbox((DisplayTextBox) ui.getNumberTextbox(6));
    }

    @Test(description = "Check comboboxes")
    public void checkComboBoxes() {
        comboboxActions.testCombobox(4);
        comboboxActions.testCombobox(3);
        comboboxActions.testCombobox(2);
        comboboxActions.testCombobox(1);
        comboboxActions.validateCombobox(3);
        comboboxActions.validateCombobox(2);
        comboboxActions.validateCombobox(1);
    }

    /**
     * check number spinner element.
     * this testcase is disabled for ie, because sometimes it has failed.
     */
    @Test(description = "Check number spinner", groups = {TestSettings.NOT_IE_RC_GROUP})
    public void checkNumberSpinner() {
        inputsActions.testNumberSpinner();
    }

    @Test(description = "Check checkboxes and radiobuttons")
    public void checkCheckboxesRadiobuttons() {
        simpleElementsActions.testFirstCheckBox();
        simpleElementsActions.testSecondCheckBox();
        simpleElementsActions.testRadioButtons();
    }

    @Test(description = "Check buttons")
    public void checkButtons() {
        simpleElementsActions.testButtons();
    }

    @Test(description = "Check holiday selector")
    public void checkHolidaySelector() {
        dateActions.testHolidaySelector();
    }

    @Test(description = "Check non work days selector")
    public void checkNonWorkDaysSelector() {
        dateActions.testNonWorkDaysSelector();
    }

    @Test(description = "Check drag and drop", groups = {TestSettings.PS_90_GROUP})
    public void checkDragAndDrop() {
        simpleElementsActions.testDragAndDrop();
    }

    @Test(description = "Check underlay node")
    public void checkUnderlayNode() {
        simpleElementsActions.testUnderlayNode();
    }

    /* // now this testcase is outdated
    @Test(description = "Check lightbox dialog")
    public void checkLightboxDialog() {
        simpleElementsActions.testLightboxDialog();
    }
    */

    @Test(description = "Check image box element")
    public void checkImageBox() {
        simpleElementsActions.testImageBox();
    }


}
