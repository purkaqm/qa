package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.*;
import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.page_locators.UIPageLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 31.05.2010
 * Time: 13:30:44
 */
public class UIPage extends PSPage {
    private static final TimerWaiter WAITER = new TimerWaiter(1500);
    private static final int GET_RESULT_NUM = 2;
    private static final TimerWaiter GET_RESULT_TIMEOUT = new TimerWaiter(5000);
    private static final long LOADING_TIMEOUT = 30000;

    public void open() {
        open(URL.getLocator());
        waitForLoading();
    }

    public boolean reopen() {
        if (!super.reopen()) {
            waitForLoading();
            return false;
        }
        return true;
    }

    public void refresh() {
        if (getDriver().getType().isWebDriver())
            WAITER.waitTime(); // hotfix.
        super.refresh(false);
        waitForLoading();
        PSLogger.save("after refreshing UI page");
    }

    private void waitForLoading() {
        Element values = getValueElement();
        for (int i = 0; i < GET_RESULT_NUM; i++) {
            WAITER.waitTime();
            try {
                values.waitForVisible(i == 0 ? GET_RESULT_TIMEOUT.getIntervalInMilliseconds() : CoreProperties.getWaitForElementToLoad());
                break;
            } catch (Wait.WaitTimedOutException ww) {
                PSLogger.warn("waitForLoading (" + i + "):" + ww.getMessage());
                scrollToEnd();
            }
        }
        getDocument();
    }

    public void clickBody() {
        super.clickBody();
        waitForLoading();
    }

    public ToolTip getToolTipFirst() {
        return new ToolTip(TOOLTIP_1);
    }

    public ToolTip getToolTipSecond() {
        return new ToolTip(TOOLTIP_2);
    }


    public DatePicker getDatePickerFirst() {
        return new DatePicker(DATE_PICKER_1);
    }

    public DatePicker getDatePickerSecond() {
        return new DatePicker(DATE_PICKER_2);
    }

    public DatePicker getInlineDatePicker() {
        // this is a hotfix for ie!
        Element res = getElement(false, DATE_PICKER_INLINE);
        res.setSimpleLocator();
        return new DatePicker(res);
    }

    public DatePicker getSecondInlineDatePicker() {
        Element res = getElement(false, DATE_PICKER_INLINE_SECOND);
        res.setSimpleLocator();
        return new DatePicker(res);
    }

    public MultiDateSelector getMultiDatePicker() {
        // this is a hotfix for ie!
        Element res = getElement(false, DATE_PICKER_MULTI);
        res.setSimpleLocator();
        return new MultiDateSelector(res);
    }

    public DateRangeTypeSelection getDateRangeTypeSelection() {
        return new DateRangeTypeSelection(document, DATE_RANGE_TYPE_SELECTOR);
    }


    public ColorPaletteSelector getColorPaletteSelector() {
        ColorPaletteSelector cps = new ColorPaletteSelector(document, COLOR_PALETTE_SELECTOR);
        cps.setId();
        return cps;
    }

    public Element getTagChoosersSectionHeader() {
        //for debugging ie8 rc
        return new Element(TAG_CHOOSERS_SECTION_HEADER);
    }

    public Element getTagChoosersSectionBody() {
        //for debugging ie8 rc
        return getSection(TAG_CHOOSERS_SECTION);
    }

    public Element getSection(ILocatorable loc) {
        Element b = null;
        for (Element _b : getElements(SECTION_B)) {
            if (_b.getDEText().equals(loc.getLocator())) {
                b = _b;
                break;
            }
        }
        if (b == null) return null;
        Element row = b.getParent("tr");
        Element tbody = row.getParent("tbody");
        List<Element> rows = Element.searchElementsByXpath(tbody, "./tr");
        int i = rows.indexOf(row);
        if (i == -1 || i >= rows.size() - 1) return null;
        return rows.get(i + 1);

    }

    private Element getTagChooser(String name) {
        for (int i = 0; i < 2; i++) {
            try {
                scroll(getTagChoosersSectionHeader());
                break;
            } catch (SeleniumException se) {
                PSLogger.warn(se);
                WAITER.waitTime();
            }
        }
        Element body = getTagChoosersSectionBody();
        long s = System.currentTimeMillis();
        while ((body == null || !body.isDEPresent()) && (System.currentTimeMillis() - s) < LOADING_TIMEOUT) {
            body = getTagChoosersSectionBody();
            new TimerWaiter(1000).waitTime();
        }
        Assert.assertFalse(body == null || !body.isDEPresent(), "Can't find any tags"); // for ie
        for (Element header : Element.searchElementsByXpath(body, TAG_CHOOSER_HEADER)) {
            if (header.getDEText().trim().contains(name)) {
                return header.getChildByXpath(TAG_CHOOSER);
            }
        }
        PSLogger.error("Can't find tag-chooser " + name);
        return null;
    }

    public TagChooser getFirstTagChooser(String name) {
        return new TagChooser(getTagChooser(name));
    }

    public FlatTagChooser getFlatTagChooser(String name) {
        return new FlatTagChooser(getTagChooser(name));
    }

    public MultipleTagChooser getMultipleTagChooser(String name) {
        return new MultipleTagChooser(getTagChooser(name));
    }

    public HierarchicalTagChooser getHierarchicalTagChooser(String name) {
        return new HierarchicalTagChooser(getTagChooser(name));
    }


    public TagsDependencies getTagsDependenciesElement() {
        scroll(getTagChoosersSectionHeader());
        TagsDependencies tc = new TagsDependencies(TAGS_DEPENDENCIES);
        tc.waitForVisible();
        tc.setDefaultElement(getDocument());
        return tc;

    }

    public Menu getMenu(int index) {
        Menu menu = null;
        if (index == 1) {
            menu = new Menu(MENU_1);
        }
        if (index == 2) {
            menu = new Menu(MENU_2);
        }
        if (index == 3) {
            menu = new Menu(MENU_3);
        }
        if (index == 4) {
            menu = new Menu(MENU_4);
        }
        if (menu != null) menu.scrollTo();
        menu.setHowToOpen(CoreProperties.getBrowser().isWebDriverIE());
        return menu;
    }

    public MultiDialog getDialog() {
        return new MultiDialog(DIALOG);
    }

    public Slider getSlider() {
        Slider sl = new Slider(document, SLIDER);
        sl.setId(SLIDER_ID);
        return sl;
    }

    public CounterTextArea getTextArea() {
        CounterTextArea cta = new CounterTextArea(TEXTAREA);
        cta.setId(TEXTAREA_ID);
        return cta;
    }

    public Input getTextbox() {
        Input in = new Input(TEXTBOX);
        in.setId(TEXTBOX_ID);
        return in;
    }

    public Input getValidationTextbox() {
        Input in = new Input(VALIDATION_TEXTBOX);
        in.setId(VALIDATION_TEXTBOX_ID);
        in.setRegExp(VALIDATION_TEXTBOX_REGEXP);
        return in;
    }

    public Input getNumberTextbox(int index) {
        Input in;
        switch (index) {
            case 1:
                in = new Input(NUMBER_TEXTBOX_1);
                in.setId(NUMBER_TEXTBOX_1_ID);
                in.setRegExp(NUMBER_TEXTBOX_DEFAULT_REGEXP);
                in.setMax(NUMBER_TEXTBOX_DEFAULT_MAX);
                in.setMin(NUMBER_TEXTBOX_DEFAULT_MIN);
                break;
            case 2:
                in = new Input(NUMBER_TEXTBOX_2);
                in.setId(NUMBER_TEXTBOX_2_ID);
                in.setRegExp(NUMBER_TEXTBOX_DEFAULT_REGEXP);
                in.setMax(NUMBER_TEXTBOX_2_MAX);
                in.setMin(NUMBER_TEXTBOX_2_MIN);
                break;
            case 3:
                in = new Input(NUMBER_TEXTBOX_3);
                in.setId(NUMBER_TEXTBOX_3_ID);
                break;
            case 4:
                in = new Input(NUMBER_TEXTBOX_4);
                in.setId(NUMBER_TEXTBOX_4_ID);
                in.setRegExp(NUMBER_TEXTBOX_4_REGEXP);
                in.setMax(NUMBER_TEXTBOX_DEFAULT_MAX);
                in.setMin(NUMBER_TEXTBOX_DEFAULT_MIN);
                break;
            case 5:
                in = new Input(NUMBER_TEXTBOX_PERCENTAGE);
                in.setId(NUMBER_TEXTBOX_PERCENTAGE_ID);
                in.setRegExp(NUMBER_TEXTBOX_PERCENTAGE_REGEXP);
                in.setMax(NUMBER_TEXTBOX_PERCENTAGE_MAX);
                in.setMin(NUMBER_TEXTBOX_PERCENTAGE_MIN);
                break;
            case 6:
                in = new DisplayTextBox(DISPLAY_TEXTBOX);
                in.setId(DISPLAY_TEXTBOX_ID);
                in.setRegExp(DISPLAY_TEXTBOX_REGEXP);
                in.setMin(DISPLAY_TEXTBOX_MIN);
                break;
            default:
                in = null;
        }
        return in;
    }

    public NumberSpinner getNumberSpinner() {
        return new NumberSpinner(document, NUMBER_SPINNER);
    }

    public ComboBox getCombBox(int index) {
        ComboBox cb;
        switch (index) {
            case 1:
                cb = new ComboBox(COMBOBOX_1);
                if (!CoreProperties.getBrowser().isSafari())
                    cb.setAlert(COMBOBOX_1_ALERT);
                cb.setId(COMBOBOX_1_ID);
                break;
            case 2:
                if (TestSession.getAppVersion().verLessThan(PowerSteeringVersions._9_0))
                    return null; // no such combobox on 8.2
                cb = new ComboBox(COMBOBOX_2);
                if (!CoreProperties.getBrowser().isSafari())
                    cb.setAlert(COMBOBOX_2_ALERT);
                cb.setId(COMBOBOX_2_ID);
                break;
            case 3:
                cb = new ComboBox(COMBOBOX_3);
                cb.setId(COMBOBOX_3_ID);
                break;
            case 4:
                cb = new ComboBox(COMBOBOX_4);
                cb.setId(COMBOBOX_4_ID);
                break;
            default:
                cb = null;
        }
        return cb;
    }

    public boolean hasCombBoxResult(int index) {
        return index != 2;
    }

    public CheckBox getFirstCheckBox() {
        CheckBox ch = new CheckBox(CHECKBOX_1);
        ch.waitForPresent(5000); // present (not visible) for ie9-web-driver
        ch.setDefaultElement(getDocument());
        ch.setId();
        ch.setName(CHECKBOX_1_LABEL);
        return ch;
    }

    public CheckBox getSecondCheckBox() {
        CheckBox ch = new CheckBox(CHECKBOX_2);
        ch.waitForPresent(5000); // present (not visible) for ie9-web-driver
        ch.setDefaultElement(getDocument());
        ch.setId();
        ch.setName(CHECKBOX_2_LABEL);
        ch.setMiddleStateButton();
        return ch;
    }

    public RadioButton getRadioButton(int index) {
        RadioButton rb = new RadioButton(document, index == 1 ? RADIOBUTTON_1 : RADIOBUTTON_2);
        rb.setId();
        rb.setName(index == 1 ? RADIOBUTTON_1_LABEL : RADIOBUTTON_2_LABEL);
        return rb;
    }

    public Button getButton() {
        return new Button(BUTTON);
    }

    public Button getSubmitForm() {
        return new Button(BUTTON_SUBMIT);
    }

    public ToggleButton getToggleButton() {
        return new ToggleButton(BUTTON_TOGGLE);
    }

    public HolidaySelector getHolidaySelector() {
        HolidaySelector hs = new HolidaySelector(getElement(false, HOLIDAY_SELECTOR));
        hs.setSimpleLocator();
        return hs;
    }

    public NonWorkDaysSelector getNonWorkDaysSelector() {
        NonWorkDaysSelector nwds = new NonWorkDaysSelector(document,
                NON_WORK_DAYS_SELECTOR);
        nwds.setSimpleLocator();
        nwds.scrollTo();
        return nwds;
    }

    public List<Element> getDragAndDropRows() {
        Element area = new Element(DRAG_AND_DROP_AREA);
        area.setDefaultElement(document);
        area.scrollTo();
        List<Element> rows = new ArrayList<Element>();
        for (Element row : Element.searchElementsByXpath(area, DRAG_AND_DROP_ROW)) {
            row.setSimpleLocator();
            rows.add(row);
        }
        return rows;
    }

    public UnderlayNode getUnderlayNode() {
        return new UnderlayNode(UNDRERLAY_NODE);
    }

    public Element getUnderlayNodeOpen() {
        return new Element(UNDERLAY_NODE_OPEN);
    }

    public Element getUnderlayNodeClose() {
        return new Element(UNDERLAY_NODE_CLOSE);
    }

    public LightboxDialog getLightboxDialog() {
        return new LightboxDialog(LIGHTBOX_DIALOG);
    }

    public ImageBox getImageBox() {
        ImageBox res = new ImageBox(IMAGE_BOX);
        res.scrollTo();
        return res;
    }


    private Element getValueElement() {
        return new Element(GET_VALUE);
    }

    public String getResult(String id) {
        Map<String, String> res = getResult(GET_RESULT_NUM);
        if (res == null || !res.containsKey(id))
            return null;
        return res.get(id);
    }

    private Map<String, String> getResult(int num) {
        for (int i = 0; i < num; i++) {
            int was = getVerticalScrollPosition();
            WAITER.waitTime();
            Map<String, String> res = new HashMap<String, String>();
            Element values = getValueElement();
            try {
                values.waitForVisible(GET_RESULT_TIMEOUT);
            } catch (Wait.WaitTimedOutException ww) {
                if (i == 0) scrollToEnd();
                if (i == num - 1)
                    PSLogger.warn("getResult(" + i + "): " + ww.getMessage());
                else throw ww;
            }
            values.focus();
            values.click(false);
            Element result = new Element(RESULT);
            result.waitForVisible(GET_RESULT_TIMEOUT); // for ie9. had failure here
            String txt = getDriver().getType().isIE() ? result.getText() : result.getInnerText();
            PSLogger.debug("Get value text : '" + txt + "'");
            if (txt.isEmpty() || !txt.contains("{")) continue;
            for (String s : txt.replaceAll("^\\{\"*", "").replaceAll("\\}$", "").split(",\"")) {
                String[] s1 = s.split("\":");
                if (s1.length == 2)
                    res.put(s1[0], s1[1].replaceAll("^\"(.*)\"$", "$1"));
            }
            int now = getVerticalScrollPosition();
            if (was != now) {
                PSLogger.debug("Scroll back");
                verticalScroll(was);
                GET_RESULT_TIMEOUT.waitTime();
            }
            return res;
        }
        return null;
    }

}
