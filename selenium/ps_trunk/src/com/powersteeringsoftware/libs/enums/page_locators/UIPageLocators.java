package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.enums.VersionLocator;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.session.TestSession;

import static com.powersteeringsoftware.libs.enums.VersionLocator.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 31.05.2010
 * Time: 13:29:39
 */
public enum UIPageLocators implements ILocatorable {
    URL("test/UI.page"),
    TOOLTIP_1("id=tooltipTest1"),
    TOOLTIP_2("id=tooltipTest2"),
    DATE_PICKER_1("id=widget_test14"),
    DATE_PICKER_2("id=widget_test15"),
    DATE_PICKER_INLINE("//div[@widgetid='test16']"),
    DATE_PICKER_INLINE_SECOND(_8_0("//div[@id='widget_PSDatePicker']"), _14("//div[@id='widget_time_PSDatePicker']")),
    DATE_PICKER_MULTI("//div[@widgetid='test21']"),
    DATE_RANGE_TYPE_SELECTOR("//select[@id='dateRangeTypeSelection']"),

    COLOR_PALETTE_SELECTOR("//div[@id='test18']"),
    DIALOG("//div[@id='testShowLink1']"),
    MENU_1("//div[@id='menu1show']"),
    MENU_2("//div[@id='menu2show']"),
    MENU_3("//div[@id='menu3show']"),
    MENU_4("//div[@id='menu4show']"),
    // this tags now in db (qaauto1_trunk_test_100604.bak)
    SECTION_B("//b"),

    TAG_CHOOSERS_SECTION("Tag Chooser:"),
    TAG_CHOOSERS_SECTION_HEADER(SECTION_B.locator + "[contains(text(), '" + TAG_CHOOSERS_SECTION.locator + "')]"),
    TAG_CHOOSER_HEADER("//th"),
    TAG_CHOOSER("/parent::tr//div[contains(@class, 'Selector')]"),
    TAGS_DEPENDENCIES("//tr[contains(@id,'TreeDisptest')]/parent::*"),

    SLIDER_ID("test26"),
    SLIDER("//table[@id='" + SLIDER_ID.locator + "']//div[@class='dijitReset dijitSliderBarContainerH']"),
    TEXTAREA_ID("test17"),
    TEXTAREA("//textarea[@id='" + TEXTAREA_ID.locator + "']"),

    TEXTBOX_ID("test4"),
    TEXTBOX("//input[@id='" + TEXTBOX_ID.locator + "']"),
    VALIDATION_TEXTBOX_ID("test5"),
    VALIDATION_TEXTBOX_REGEXP("\\d{5}"),
    VALIDATION_TEXTBOX("//input[@id='" + VALIDATION_TEXTBOX_ID.locator + "']"),

    NUMBER_TEXTBOX_DEFAULT_REGEXP("[+-]?\\d*\\.?\\d{1,}([eE][-+]?\\d+)?"),
    NUMBER_TEXTBOX_DEFAULT_MAX("8999999999999999"),
    NUMBER_TEXTBOX_DEFAULT_MIN("-8999999999999999"),

    NUMBER_TEXTBOX_1_ID("test6"),
    NUMBER_TEXTBOX_1("//input[@id='" + NUMBER_TEXTBOX_1_ID.locator + "']"),

    NUMBER_TEXTBOX_2_ID("test7"),
    NUMBER_TEXTBOX_2("//input[@id='" + NUMBER_TEXTBOX_2_ID.locator + "']"),
    NUMBER_TEXTBOX_2_MAX("100"),
    NUMBER_TEXTBOX_2_MIN("3"),

    NUMBER_TEXTBOX_3_ID("test8"),
    NUMBER_TEXTBOX_3("//input[@id='" + NUMBER_TEXTBOX_3_ID.locator + "']"),
    NUMBER_TEXTBOX_4_ID("test9"),
    NUMBER_TEXTBOX_4_REGEXP("[+-]?\\d{1,}([eE][-+]?\\d+)?"),
    NUMBER_TEXTBOX_4("//input[@id='" + NUMBER_TEXTBOX_4_ID.locator + "']"),

    NUMBER_TEXTBOX_PERCENTAGE_ID("test12"),
    NUMBER_TEXTBOX_PERCENTAGE("//input[@id='" + NUMBER_TEXTBOX_PERCENTAGE_ID.locator + "']"),
    NUMBER_TEXTBOX_PERCENTAGE_REGEXP("\\d{1,}"),
    NUMBER_TEXTBOX_PERCENTAGE_MAX("100"),
    NUMBER_TEXTBOX_PERCENTAGE_MIN("0"),

    DISPLAY_TEXTBOX_ID("test13"),
    DISPLAY_TEXTBOX("//input[@id='" + DISPLAY_TEXTBOX_ID.locator + "']"),
    DISPLAY_TEXTBOX_REGEXP("\\d{1,}"),
    DISPLAY_TEXTBOX_MIN("0"),

    NUMBER_SPINNER("//div[@id='widget_test25']"),

    COMBOBOX_1("//div[@id='widget_test22']"),
    COMBOBOX_1_ID("test22"),
    COMBOBOX_1_ALERT("false", "true", "false"),
    COMBOBOX_2("//div[@id='widget_test22_b']"),
    COMBOBOX_2_ID("test22_b"),
    COMBOBOX_2_ALERT("false", "true", "false"),
    COMBOBOX_3("//div[@id='widget_test23']"),
    COMBOBOX_3_ID("test23"),
    COMBOBOX_4("//div[@id='widget_test24']"),
    COMBOBOX_4_ID("test24"),

    CHECKBOX_1("//input[@id='test1']"),
    CHECKBOX_1_LABEL(CHECKBOX_1.locator + "/parent::div/following-sibling::label"),
    CHECKBOX_2("//input[@id='testCheckBox2']"),
    CHECKBOX_2_LABEL(CHECKBOX_1.locator + "/parent::div/following-sibling::label"),
    CHECKBOX_2_BUTTON("//span[text()='Set middle state']/parent::button"),

    RADIOBUTTON_1("//input[@id='test2']"),
    RADIOBUTTON_1_LABEL(RADIOBUTTON_1.locator + "/parent::div/following-sibling::label"),
    RADIOBUTTON_2("//input[@id='test3']"),
    RADIOBUTTON_2_LABEL(RADIOBUTTON_2.locator + "/parent::div/following-sibling::label"),

    BUTTON("//button[@id='ps_form_Button_0']"),
    BUTTON_SUBMIT("//button[@id='ps_form_Button_1']"),
    BUTTON_TOGGLE("//button[@id='test0']"),

    NON_WORK_DAYS_SELECTOR("//div[@widgetid='test19']"),
    HOLIDAY_SELECTOR("//div[@widgetid='test20']"),

    DRAG_AND_DROP_AREA("//div[@id='dndArea']"),
    DRAG_AND_DROP_ROW("//div[@class='grippy']"),

    UNDRERLAY_NODE("//div[@id='underlayNode']"),
    UNDERLAY_NODE_OPEN("//div[text()='Underlay']"),
    UNDERLAY_NODE_CLOSE("//div[text()='CloseUnderlay']"),

    LIGHTBOX_DIALOG("//div[text()='Open LightboxDialog']"),
    IMAGE_BOX("//div[@class='imagebox']"),

    GET_VALUE("//input[@value='GetValues']"),
    RESULT("//td[@id='results']"),;
    String locator;

    UIPageLocators(String loc) {
        locator = loc;
    }

    UIPageLocators(String loc82, String loc90, String loc91) {
        if (!TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_0)) {
            locator = loc82;
        } else if (TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_1)) {
            locator = loc91;
        } else {
            locator = loc90;
        }
    }

    UIPageLocators(VersionLocator... locs) {
        locator = String.valueOf(chooseLocator(locs).get());
    }

    public String getLocator() {
        return locator;
    }
}
