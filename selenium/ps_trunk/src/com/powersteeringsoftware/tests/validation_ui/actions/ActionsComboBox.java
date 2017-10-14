package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.ComboBox;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.06.2010
 * Time: 17:43:52
 */
public class ActionsComboBox {
    private UIPage ui;

    private TestData data;

    public ActionsComboBox(UIPage ui, TestData data) {
        this.ui = ui;
        this.data = data;
    }

    public void validateCombobox(int index) {
        ComboBox cb = ui.getCombBox(index);
        if (cb == null) {
            // this combobox is not supported for this version
            return;
        }
        PSLogger.info("Validate combobox " + cb.getId());
        List<String> testData = new ArrayList<String>(data.getTextAreaData());
        testData.remove(0); // this is hotfix for ie.
        //testData.addAll();

        for (String test : testData) {
            cb.type(test);
            String fromPage = cb.getValue();
            PSLogger.debug("From page " + fromPage.trim() + ":" + StrUtil.stringToUnicode(fromPage.trim()));
            PSLogger.debug("Expected " + test.trim() + ":" + StrUtil.stringToUnicode(test.trim()));
            Assert.assertEquals(fromPage.trim(), test.trim(), "Incorrect data in combobox after setting: " + fromPage);
            Assert.assertTrue(cb.isWrongInput(), "Input is not wrong as expected for value " + test);
        }
    }


    public void testCombobox(int index) {
        ComboBox cb = ui.getCombBox(index);
        if (cb == null) {
            // this combobox is not supported for this version
            return;
        }
        PSLogger.info("Test combobox " + cb.getId());
        String expectedDefault = data.getComboBoxDefaultValue(index);
        if (expectedDefault != null) {
            String actualDefault = cb.getSelectedLabel();
            PSLogger.debug(cb.getId() + "'s default value is '" + actualDefault + "'");
            Assert.assertEquals(actualDefault, expectedDefault, "Incorrect default value for combobox " + cb.getId());
        }
        cb.open();
        List<String> items = cb.getOptions();
        PSLogger.info("items: " + items);
        Assert.assertEquals(items, data.getComboBoxData(index),
                "Incorrect options in combobox " + cb.getId() +
                        ", should be " + data.getComboBoxData(index));
        cb.close();
        for (String item : items) {
            cb.select(item);
            String res = cb.getSelectedLabel();
            PSLogger.info("Selected label is '" + res + "'");
            Assert.assertEquals(res, item, "Incorrect item after selecting " + item + "for combobox " + cb.getId());
            if (!ui.hasCombBoxResult(index)) continue;
            Assert.assertEquals(ui.getResult(cb.getId()),
                    cb.getHiddenInput().getValue(),
                    "Incorrect value after getValues for combobox " + cb.getId() + ", should be " + cb.getHiddenInput().getValue());
        }
    }


}
