package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.06.2010
 * Time: 14:03:33
 */
public class ActionsInputs {
    private static final int MAX_ALLOWED_DEC_OR_INC = 5;
    private UIPage ui;
    private TestData data;

    public ActionsInputs(UIPage ui, TestData data) {
        this.ui = ui;
        this.data = data;
    }

    public void testTextArea() {
        List<String> tests = data.getTextAreaData();
        PSLogger.info("Test data: " + tests);
        CounterTextArea cta = ui.getTextArea();
        cta.waitForVisible(30000);
        int counter = cta.getCounter();
        for (String test : tests) {
            PSLogger.debug("test '" + test + "'");
            new TimerWaiter(500).waitTime();
            cta.scrollTo();
            String expectedText = test;
            if (expectedText.length() > counter) {
                expectedText = expectedText.substring(0, counter);
            }
            String expectedCounter = expectedText.length() + "/" + counter;
            cta.setText(test);
            String fromPageCounter = cta.getCounterFromSpan();
            String fromPageText = cta.getText();
            PSLogger.info("Textarea: '" + fromPageText + "', '" + fromPageCounter + "'");
            String _expectedText = getStringToCompare(expectedText);
            Assert.assertEquals(fromPageText, _expectedText, "Incorrect text in textarea, should be '" + _expectedText + "'");
            Assert.assertEquals(fromPageCounter, expectedCounter, "Incorrect counter in textarea, should be " + expectedCounter);
            // for ie6 hotfix to debug:
            new TimerWaiter(500).waitTime();
            ui.scrollToEnd();
            String res = ui.getResult(cta.getId());
            // add trim for ie
            Assert.assertEquals(res.trim(), expectedText.trim(), "Incorrect text after get value, should be " + expectedText);
        }
    }

    public void testTextbox() {
        PSLogger.info("Test textbox");
        List<String> tests = data.getTextAreaData();
        PSLogger.info("Test data: " + tests);
        Input in = ui.getTextbox();
        for (String test : tests) {
            PSLogger.debug("test '" + test + "'");
            in.type(test);
            String _expectedText = getStringToCompare(test);
            Assert.assertEquals(in.getValue(), _expectedText, "Incorrect text in textbox, should be '" + _expectedText + "'");
            Assert.assertFalse(in.isWrongInput(), "Wrong input");
            Assert.assertEquals(ui.getResult(in.getId()).trim(), test.trim(), "Incorrect text after get value, should be " + test);
        }
    }

    /**
     * todo: its workaround for web-driver
     *
     * @param init initial string
     * @return - get string to compare with string from page
     */
    private static String getStringToCompare(String init) {
        return CoreProperties.getBrowser().isWebDriver() ? init : init.replaceAll("^\\s*", "");
    }

    public void testValidationTextbox() {
        PSLogger.info("Test Validation textbox");
        List<String> tests1 = data.getTextAreaData();
        PSLogger.info("Test data: " + tests1);
        Input in = ui.getValidationTextbox();
        for (String test : tests1) {
            PSLogger.info("test '" + test + "'");
            in.type(test);
            String _expectedText = getStringToCompare(test);
            Assert.assertEquals(in.getValue(), _expectedText, "Incorrect text in textbox, should be '" + _expectedText + "'");
            Assert.assertTrue(in.isWrongInput(), "Input is not wrong");
        }
        List<String> tests2 = data.getTextboxData();
        for (String test : tests2) {
            PSLogger.info("test '" + test + "'");
            in.type(test);
            String _expectedText = getStringToCompare(test);
            Assert.assertEquals(in.getValue(), _expectedText, "Incorrect text in textbox, should be '" + _expectedText + "'");
            String getValueText = _expectedText.replaceAll("^\\s*", "");
            if (getValueText.matches(in.getRegExp())) {
                Assert.assertFalse(in.isWrongInput(), "Input is wrong for " + test);
                Assert.assertEquals(ui.getResult(in.getId()), getValueText, "Incorrect text after get value, should be " + test);
            } else {
                Assert.assertTrue(in.isWrongInput(), "Input is not wrong for " + test);
            }
        }
    }

    public void testFirstNumberTextbox(Input in) {
        PSLogger.info("TextBox " + in.getId());
        List<String> tests = data.getTextAreaData();
        tests.addAll(data.getTextboxData());
        Long maxLimit = Long.parseLong(in.getMax());
        Long minLimit = Long.parseLong(in.getMin());
        PSLogger.info("Test Data : " + tests);
        for (String test : tests) {
            PSLogger.debug("test '" + test + "'");
            in.type(test);
            if (test.matches(in.getRegExp()) &&
                    Double.parseDouble(test) <= maxLimit &&
                    Double.parseDouble(test) >= minLimit) {
                testNumberTextbox(Double.parseDouble(test), in);
            } else {
                Assert.assertTrue(in.isWrongInput(), "Input is not wrong");
                //Assert.assertEquals(in.getValue(), test.replaceAll("^\\s*", ""), "Incorrect text in textbox, should be " + test);
            }
        }
    }

    public void testDisplayTextbox(DisplayTextBox in) {
        PSLogger.info("Test Display TextBox " + in.getId());
        List<String> tests = data.getTextAreaData();
        tests.addAll(data.getTextboxData());
        Long minLimit = Long.parseLong(in.getMin());
        for (String test : tests) {
            PSLogger.debug("test '" + test + "'");
            in.type(test);
            String fromInput;
            if (test.matches(in.getRegExp()) &&
                    Double.parseDouble(test) >= minLimit) {
                PSLogger.debug("Expected value " + test);
                Assert.assertFalse(in.isWrongInput(), "Input is wrong");
                fromInput = in.getValue();
                PSLogger.debug("From input " + fromInput);
                Assert.assertEquals(fromInput, test,
                        "Incorrect text in textbox, should be " + test);
                Assert.assertEquals(ui.getResult(in.getId()), test,
                        "Incorrect text after get value, should be " + test);
                PSLogger.debug("Clear right value");
            } else {
                Assert.assertTrue(in.isWrongInput(), "Input is not wrong");
                PSLogger.debug("Clear incorrect value");
            }
            Element icon = in.getIcon();
            icon.click(false);
            PSLogger.debug("Input is '" + (fromInput = in.getValue()) + "'");
            Assert.assertTrue(fromInput.isEmpty(), "Input is not empty after clearing");
            Assert.assertTrue(ui.getResult(in.getId()).isEmpty(),
                    "Input is not empty in result after clearing");
        }
    }

    public void testNumberSpinner() {
        List<String> allData = data.getTextAreaData();
        allData.addAll(data.getTextboxData());
        List<String> rightData = new ArrayList<String>();
        NumberSpinner ns = ui.getNumberSpinner();
        Input in = ns.getInput();
        Long maxLimit = Long.parseLong(in.getMax());
        Long minLimit = Long.parseLong(in.getMin());
        PSLogger.info("Test wrong inputs");
        for (String test : allData) {
            PSLogger.debug("test '" + test + "'");
            in.type(test);
            if (test.matches(in.getRegExp()) &&
                    Double.parseDouble(test) <= maxLimit &&
                    Double.parseDouble(test) >= minLimit) {
                rightData.add(test);
            } else {
                Assert.assertTrue(in.isWrongInput(), "Input is not wrong");
            }
        }

        PSLogger.info("Test right data: " + rightData);
        for (String test : rightData) {
            Error error = null;
            for (int i = 0; i < TestData.ATTEMPTS_NUMBER; i++) {
                try {
                    testNumberSpinner(test, ns, in);
                    error = null;
                    break;
                } catch (AssertionError e) {
                    PSLogger.warn("Seems too quickly, error : '" + e.getMessage() + "'; try again");
                    error = e;
                }
            }
            if (error != null) throw error;
        }
    }

    private void testNumberSpinner(String test, NumberSpinner ns, Input in) {
        PSLogger.debug("test '" + test + "'");
        in.type(test);
        Double d = Double.parseDouble(test);
        PSLogger.debug("test increase");
        for (int i = 0; i < TestData.getRandom().nextInt(MAX_ALLOWED_DEC_OR_INC); i++) {
            d++;
            PSLogger.debug("Increase #" + (i + 1));
            ns.increase();
        }
        PSLogger.debug("test decrease");
        for (int i = 0; i < TestData.getRandom().nextInt(MAX_ALLOWED_DEC_OR_INC); i++) {
            d--;
            PSLogger.debug("Decrease #" + (i + 1));
            ns.decrease();
        }
        testNumberTextbox(d, in);
    }

    private void testNumberTextbox(Double d, Input in) {
        NumberFormat nf = NumberFormat.getInstance(TestData.DEFAULT_LOCALE);
        String expectedNumber1 = nf.format(d);
        nf.setGroupingUsed(false);
        String expectedNumber2 = nf.format(d);
        if (expectedNumber2.equals("-0")) expectedNumber2 = "0";

        PSLogger.debug("Expected value " + expectedNumber1);
        if (expectedNumber1.equals("-0")) expectedNumber1 = "0";

        Assert.assertFalse(in.isWrongInput(), "Input is wrong");
        String fromInput = in.getValue();
        PSLogger.debug("From input " + fromInput);
        if (fromInput.equals("-0")) fromInput = "0";

        Assert.assertEquals(fromInput, expectedNumber1,
                "Incorrect text in textbox, should be " + expectedNumber1);
        Assert.assertEquals(ui.getResult(in.getId()), expectedNumber2,
                "Incorrect text after get value, should be " + expectedNumber2);

    }

}
