package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.06.2010
 * Time: 11:59:45
 */
public class ActionsSimpleElements {
    private UIPage ui;
    private TestData data;

    public ActionsSimpleElements(UIPage ui, TestData d) {
        this.ui = ui;
        data = d;
    }

    public void testFirstCheckBox() {
        CheckBox ch = ui.getFirstCheckBox();
        PSLogger.info("Test first checkbox");
        try {
            ch.click();
        } catch (SeleniumException e) {
            if (CoreProperties.getBrowser().isWebDriverIE()) {
                PSLogger.warn(e);
                PSSkipException.skip("Skipp this because web-driver works incorrect with this element");// it is unvisible
            } else {
                throw e;
            }
        }
        List<String> messages = ui.getMessagesFromTopAndClose();
        PSLogger.info("Messages after check " + messages);
        Assert.assertFalse(messages.size() == 0, "Empty list, no messages after checking on checkbox 1");
        String res = ui.getResult(ch.getId());
        PSLogger.info("Result from page " + res);
        Assert.assertEquals(res, data.getCheckboxValue(true), "Incorrect result for first checkbox");
        Assert.assertTrue(ch.getChecked(), "Chekbox is not checked");

        ch.click();
        messages = ui.getMessagesFromTopAndClose();
        PSLogger.info("Messages after uncheck " + messages);
        Assert.assertFalse(messages.size() == 0, "Empty list, no messages after unchecking checkbox 1");
        res = ui.getResult(ch.getId());
        PSLogger.info("Result from page " + res);
        Assert.assertEquals(res, data.getCheckboxValue(false), "Incorrect result, should be false");
        Assert.assertFalse(ch.getChecked(), "Chekbox is not unchecked");
    }


    public void testSecondCheckBox() {
        CheckBox ch2 = ui.getSecondCheckBox();
        PSLogger.info("Test second checkbox (with 'Set middle state' button)");
        ch2.click();
        Assert.assertTrue(ch2.getChecked(), "Second chekbox is not checked");
        ch2.setMiddleState();
        Assert.assertTrue(ch2.isMiddleStateSetted(), "Second chekbox is not in middle state");
        ch2.click();
        Assert.assertFalse(ch2.getChecked(), "Second chekbox is not unchecked");
        ch2.setMiddleState();
        Assert.assertTrue(ch2.isMiddleStateSetted(), "Second chekbox is not in middle state after uncheck");
    }

    public void testRadioButtons() {
        RadioButton rb1 = ui.getRadioButton(1);
        RadioButton rb2 = ui.getRadioButton(2);
        rb1.click();
        radioButtonsChecks(rb1, "1", rb2, "2");
        rb2.click();
        radioButtonsChecks(rb2, "2", rb1, "1");
        rb1.click();
        radioButtonsChecks(rb1, "1", rb2, "2");
    }

    private void radioButtonsChecks(RadioButton shouldbeChecked,
                                    String shouldbeCheckedId,
                                    RadioButton shouldbeUnchecked,
                                    String shouldbeUncheckedId) {
        Assert.assertTrue(shouldbeChecked.getChecked(), "Radiobuton " + shouldbeCheckedId + " is not checked");
        Assert.assertFalse(shouldbeUnchecked.getChecked(), "Radiobuton " + shouldbeUncheckedId + " is checked");
        Assert.assertEquals(ui.getResult(shouldbeChecked.getId()), data.getRadioButtonValue(shouldbeCheckedId, true),
                "Radiobuton " + shouldbeCheckedId + " is not checked in results");
        Assert.assertEquals(ui.getResult(shouldbeUnchecked.getId()), data.getRadioButtonValue(shouldbeUncheckedId, false),
                "Radiobuton " + shouldbeUncheckedId + " is checked in results");
    }

    public void testTooltips() {
        ToolTip tt1 = ui.getToolTipFirst();
        tt1.open();
        Assert.assertEquals(tt1.getLabel(), data.getTooltip("1"), "Incorrect first tooltip text");
        tt1.close();
        ToolTip tt2 = ui.getToolTipSecond();
        tt2.open();
        Assert.assertEquals(tt2.getLabel(), data.getTooltip("2"), "Incorrect second tooltip text");
        tt2.close();
    }

    public void testDialog() {
        MultiDialog d = ui.getDialog();
        d.open();
        Assert.assertTrue(d.isPresent(), "Can't find dialog after clicking");
        PSLogger.info("Title '" + d.getTitle() + "'");
        PSLogger.info("Body '" + d.getBody() + "'");
        MultiDialog dd = d.getNext();
        Assert.assertEquals(d.getTitle(), data.getDialogTitle("1"), "Incorrect dialog title");
        Assert.assertEquals(d.getBody(), data.getDialogBody("1"), "Incorrect dialog body");
        PSLogger.info("Title '" + dd.getTitle() + "'");
        PSLogger.info("Body '" + dd.getBody() + "'");
        Assert.assertEquals(dd.getTitle(), data.getDialogTitle("2"), "Incorrect second dialog title");
        Assert.assertEquals(dd.getBody(), data.getDialogBody("2"), "Incorrect second dialog body");
        dd.close(false);
        Assert.assertFalse(dd.isPresent(), "Second popup still present");
        d.close();
        Assert.assertFalse(d.isPresent(), "First popup still present");
    }

    public void testSlider(int attempts) {
        int num = 5;
        List<Integer> randomPercentage = new ArrayList<Integer>();
        for (int i = 0; i < num; i++) {
            randomPercentage.add(TestData.getRandom().nextInt(100));
        }
        randomPercentage.add(TestData.getRandom().nextInt(randomPercentage.size()), 0);
        randomPercentage.add(TestData.getRandom().nextInt(randomPercentage.size()), 100);
        PSLogger.info("Test data: " + randomPercentage);
        Slider sl = ui.getSlider();
        for (int i : randomPercentage) {
            Error error = null;
            for (int j = 0; j < attempts; j++) {
                try {
                    testSlider(sl, i);
                    error = null;
                    break;
                } catch (AssertionError e) {
                    PSLogger.warn("Seems too quickly for " + sl.getDriver().getType().getBrowser() + ": " + e.getMessage());
                    PSLogger.warn("Iteration #" + j);
                    error = e;
                }
            }
            if (error != null) {
                if (sl.getDriver().getType().isGoogleChrome()) {
                    // seems still doesn't work correct for chrome:
                    PSSkipException.skip(error.getMessage());
                }
                throw error;
            }
        }
    }

    private void testSlider(Slider sl, int percentage) {
        sl.scrollTo();
        sl.clickAt(percentage);
        Assert.assertTrue(sl.compare(percentage), "Incorrect value set:  " + sl.getValue() + ", should be " + percentage);
        float fromRes = Float.parseFloat(ui.getResult(sl.getId()));
        Assert.assertTrue(Slider.compare(percentage, fromRes), "Incorrect value in result:  " + fromRes +
                ", should be " + percentage);
    }

    public void testButtons() {
        PSLogger.info("Test button");
        Button bt1 = ui.getButton();
        bt1.click(false);
        List<String> messages = ui.getMessagesFromTopAndClose();
        PSLogger.info("Messages after click button " + messages);
        Assert.assertTrue(messages.size() == 1, "Incorrect messages list");
        Assert.assertTrue(messages.get(0).matches(data.getMessage("click")), "Incorrect message, should match " +
                data.getMessage("click"));

        PSLogger.info("Test toggle button");
        ToggleButton bt3 = ui.getToggleButton();
        bt3.click();
        Assert.assertTrue(bt3.isChecked(), "Toggle button is not checked");
        bt3.click();
        Assert.assertFalse(bt3.isChecked(), "Toggle button is checked");
        bt3.click();
        Assert.assertTrue(bt3.isChecked(), "Toggle button is not checked");

        PSLogger.info("Test submit form");
        Button bt2 = ui.getSubmitForm();
        String urlWas = ui.getUrl();
        bt2.click(true);
        String urlNow = ui.getCurrentUrl();
        PSLogger.info("Url now " + urlNow);
        Assert.assertNotSame(urlNow, urlWas, "Url is not changes after submit form");
        ui.getDocument();
    }

    public void testDragAndDrop() {
        List<Element> rows = ui.getDragAndDropRows();
        Assert.assertTrue(rows.get(0).getY() < rows.get(1).getY(),
                "Before drag'n'drop second row above the first");

        PSLogger.info("Move " + rows.get(0).getParent().getText() + " to " + rows.get(1).getParent().getText());
        rows.get(0).dragAndDrop(rows.get(1));
        Assert.assertTrue(rows.get(1).getY() < rows.get(0).getY(), "First drag'n'drop was unsuccessful");
        Assert.assertEquals(rows.get(1).getX(), rows.get(0).getX(), "Incorrect x coordinate for rows after d'n'd");

        PSLogger.info("Move " + rows.get(1).getParent().getText() + " to " + rows.get(0).getParent().getText());
        rows.get(1).dragAndDrop(rows.get(0));
        Assert.assertTrue(rows.get(0).getY() < rows.get(1).getY(), "Second drag'n'drop was unsuccessful");
        Assert.assertEquals(rows.get(1).getX(), rows.get(0).getX(), "Incorrect x coordinate for rows after d'n'd");
    }

    public void testUnderlayNode() {
        UnderlayNode node = ui.getUnderlayNode();
        node.scrollTo();
        ui.getUnderlayNodeOpen().click(false);
        node.waiting(true);
        ui.getUnderlayNodeClose().click(false);
        node.waiting(false);
    }

    private void testImageBox(ImageBox imageBox) {
        String title = imageBox.getTitle();
        Assert.assertEquals(title, data.getLightboxDialogTitle("1"),
                "Incorrect title on first image box dialog: " + title);
        String description = imageBox.getDescription();
        Assert.assertEquals(description.replaceAll("\n\\s*", " "),
                data.getLightboxDialogDescription("1").replaceAll("\n\\s*", " "),
                "Incorrect description on first image box dialog: " + description);
        String img = imageBox.getImage();
        Assert.assertTrue(img.endsWith(data.getLightboxDialogImage("1")),
                "Incorrect image on first image box dialog: " + img);
        String group1 = imageBox.getGroup();

        imageBox.next();
        title = imageBox.getTitle();
        Assert.assertEquals(title, data.getLightboxDialogTitle("2"),
                "Incorrect title on second image box dialog: " + title);
        description = imageBox.getDescription();
        Assert.assertEquals(description.replaceAll("\n\\s*", " "),
                data.getLightboxDialogDescription("2").
                        replaceAll("\n\\s*", " "),
                "Incorrect description on second image box dialog: " + description);
        img = imageBox.getImage();
        Assert.assertTrue(img.endsWith(data.getLightboxDialogImage("2")),
                "Incorrect image on second image box dialog: " + img);
        String group2 = imageBox.getGroup();
        Assert.assertFalse(group1.equals(group2), "Incorrect group after first next " + group2);

        imageBox.next();
        Assert.assertTrue(group1.equals(imageBox.getGroup()), "Incorrect group after second next " + imageBox.getGroup());

        imageBox.prev();
        Assert.assertTrue(group2.equals(imageBox.getGroup()), "Incorrect group after prev " + imageBox.getGroup());

        imageBox.prev();
        Assert.assertTrue(group1.equals(imageBox.getGroup()), "Incorrect group after second prev " + imageBox.getGroup());
    }

    public void testImageBox() {
        ImageBox ib = ui.getImageBox();
        testImageBox(ib);
        LightboxDialog ld = ib.getLightBoxDialog();
        openDialog(ld);
        testImageBox(ld.getImageBox());
        ld.close();
        openDialog(ld);
        ld.close();
    }

    private void openDialog(IDialog ld) {
        try {
            ld.open();
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.error("openLightboxDialog: " + we);
            ui.refresh(); // why?
            ld.open();
        }
    }

    @Deprecated
    public void testLightboxDialog() {
        LightboxDialog ld = ui.getLightboxDialog();
        ld.open();
        testImageBox(ld.getImageBox());
        ld.close();

        ld.open();
        ld.close();
    }
}
