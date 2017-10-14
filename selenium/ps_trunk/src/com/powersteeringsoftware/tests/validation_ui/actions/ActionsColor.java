package com.powersteeringsoftware.tests.validation_ui.actions;

import com.powersteeringsoftware.libs.elements.ColorPaletteSelector;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.UIPage;
import com.powersteeringsoftware.tests.validation_ui.TestData;
import org.testng.Assert;

import java.awt.*;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.2010
 * Time: 13:40:39
 */
public class ActionsColor {
    private UIPage ui;


    public ActionsColor(UIPage ui) {
        this.ui = ui;
    }

    public void testColorPaletteSelector() {
        // its work correct for ie web-driver 2.0b3
        ColorPaletteSelector cps = ui.getColorPaletteSelector();
        cps.scrollTo();

        cps.openPopup();
        List<Color> cols = cps.getAllColorsFromPopup();
        cps.closePopup();
        PSLogger.info(cols);
        Assert.assertTrue(cols.size() > 1, "Can't get colors from popup");

        Color nullColor = cols.remove(0);
        Assert.assertNull(nullColor, "First element in popup should be " + ColorPaletteSelector.NULL_COLOR);

        Color toSet1 = cols.get(TestData.getRandom().nextInt(cols.size()));
        cps.setUsingInput(toSet1);
        Color fromInput = cps.getColorFromInput();
        Color fromDiv = cps.getColorFromDiv();
        PSLogger.info("Color from input: " + fromInput + ", " + fromDiv);
        Assert.assertEquals(fromInput, toSet1, "Incorrect color after setting using input, should be: " + toSet1);
        Assert.assertEquals(fromDiv, toSet1, "Incorrect color after setting using input in div, should be: " + toSet1);
        Assert.assertEquals(ui.getResult(cps.getId()), ColorPaletteSelector.colorToString(toSet1),
                "Incorrect color in getResult after setting " + toSet1);

        Color toSet2 = cols.get(TestData.getRandom().nextInt(cols.size()));
        cps.setUsingPopup(toSet2);
        fromInput = cps.getColorFromInput();
        fromDiv = cps.getColorFromDiv();
        PSLogger.info("Color from input: " + fromInput + ", " + fromDiv);
        Assert.assertEquals(fromInput, toSet2, "Incorrect color after setting using popup, should be: " + toSet2);
        Assert.assertEquals(fromDiv, toSet2, "Incorrect color after setting using popup in div, should be: " + toSet2);
        Assert.assertEquals(ui.getResult(cps.getId()), ColorPaletteSelector.colorToString(toSet2),
                "Incorrect color in getResult after setting " + toSet2);
        cps.scrollTo();

        Color toSet3 = cols.get(TestData.getRandom().nextInt(cols.size()));
        cps.setUsingMoreColorsPopupInput(toSet3);
        fromInput = cps.getColorFromInput();
        fromDiv = cps.getColorFromDiv();
        PSLogger.info("Color from input: " + fromInput + ", " + fromDiv);
        Assert.assertEquals(fromInput, toSet3, "Incorrect color after setting using more color popup, should be: " + toSet3);
        Assert.assertEquals(fromDiv, toSet3, "Incorrect color after setting using more color popup in div, should be: " + toSet3);
        Assert.assertEquals(ui.getResult(cps.getId()), ColorPaletteSelector.colorToString(toSet3),
                "Incorrect color in getResult after setting " + toSet3);
        cps.scrollTo();

        PSLogger.info("Test clear color");
        cps.clearColorUsingPopup();
        fromInput = cps.getColorFromInput();
        fromDiv = cps.getColorFromDiv();
        PSLogger.info("Color from input: " + fromInput + ", " + fromDiv);
        Assert.assertEquals(fromInput, nullColor, "Incorrect color after clearing using popup, should be: " + toSet2);
        Assert.assertEquals(fromDiv, ColorPaletteSelector.NULL_COLOR, "Incorrect color after clearing using popup in div, should be: " + toSet2);
        Assert.assertTrue(ui.getResult(cps.getId()).isEmpty(),
                "Incorrect color in getResult after clearing");

    }

}
