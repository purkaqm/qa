package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.2010
 * Time: 13:21:04
 */
public enum ColorPaletteSelectorLocators implements ILocatorable {
    POPUP_OPEN_ICON("//div[contains(@class, 'psColorPickerIcon')]"),
    POPUP_COLOR_CELL("span"),
    POPUP_COLOR_CELLS("//div[@class='psColorPaletteInner']/" + POPUP_COLOR_CELL.locator + "/img"),
    POPUP_COLOR_CELLS_STYLE("style"),
    POPUP_COLOR_CELLS_STYLE_COLOR("background-color:"),

    POPUP_MORE_COLORS("//a"),
    INPUT_COLOR("//input"),
    DIV_COLOR("//div[contains(@class, 'psColorPickerRect')]"),
    POPUP_CONTENT("/div"),

    POPUP_CONTENT_CLASS_MORE_COLLORS("psxColorPickerPopup"),
    POPUP_MORE_COLLORS_PICKER_BOX("//div[@class='psxColorPickerBox']"),
    POPUP_MORE_COLLORS_PICKER_BOX_CURSOR("//div[@class='psxColorPickerPoint']"),
    POPUP_MORE_COLLORS_PICKER_UNDERLAY("//div[@class='psxHuePickerUnderlay']"),
    POPUP_MORE_COLLORS_PICKER_UNDERLAY_CURSOR("//div[@class='psxHuePickerPoint']"),

    POPUP_MORE_COLLORS_PICKER_PREVIEW("//div[@class='psxColorPickerPreview']"),

    POPUP_MORE_COLLORS_INPUT("//div[@class='psxColorPickerHex']/input"),
    POPUP_MORE_COLLORS_OK("//div[@class='psxColorPickerOk']/input"),
    POPUP_MORE_COLLORS_CANCEL("//div[@class='psxColorPickerCancel']/a[1]"),
    POPUP_MORE_COLLORS_CLEAR("//div[@class='psxColorPickerCancel']/a[2]"),;
    String locator;

    ColorPaletteSelectorLocators(String loc) {
        locator = loc;
    }

    public String getLocator() {
        return locator;
    }
}
