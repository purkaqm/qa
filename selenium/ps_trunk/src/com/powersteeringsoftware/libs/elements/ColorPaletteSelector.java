package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;
import org.dom4j.Document;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.elements_locators.ColorPaletteSelectorLocators.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 03.06.2010
 * Time: 13:20:48
 */
public class ColorPaletteSelector extends Element {
    protected Element popup;
    private Map<Color, Element> colors = new HashMap<Color, Element>();
    public static final Color NULL_COLOR = new Color(255, 255, 255);
    private static final long POPUP_TIMEOUT = 30000; //ms
    private static final int POPUP_ATTEMPTS = 3; //ms

    public ColorPaletteSelector(ILocatorable locator) {
        super(locator);
    }

    public ColorPaletteSelector(String locator) {
        super(locator);
    }

    public ColorPaletteSelector(Element e) {
        super(e);
    }

    public ColorPaletteSelector(Document document, ILocatorable locator) {
        super(document, locator);
    }

    public Element getPopup() {
        if (popup == null) return popup = new DijitPopup();
        return popup;
    }

    public void openPopup() {
        PSLogger.debug("Open color popup");
        Element op = getOpenPopupIcon();
        op.focus();
        op.click(false);
        op.mouseDownAndUp();
        try {
            getPopup().waitForVisible(POPUP_TIMEOUT);
        } catch (Wait.WaitTimedOutException w) {
            op.click(false);
            getPopup().waitForVisible(POPUP_TIMEOUT);
        }
        colors.clear();
        setAllColorsFromPopup();
    }

    public void closePopup() {
        PSLogger.debug("Close color popup");
        Element parent = getParent();
        parent.mouseDownAndUp();
        try {
            getPopup().waitForUnvisible(POPUP_TIMEOUT);
        } catch (Wait.WaitTimedOutException we) {
            PSLogger.warn(getClass().getSimpleName() + ".closePopup:" + we.getMessage());
            parent.click(false);
            getPopup().waitForUnvisible(POPUP_TIMEOUT); // try again
        }
    }

    public void setUsingMoreColorsPopupInput(Color col) {
        PSLogger.info("Set color " + col + " using input in more colors popup");
        openPopup();
        Element link = getPopup().getChildByXpath(POPUP_MORE_COLORS);
        PSLogger.debug("Click on link: " + link.getText());
        link.click(false);
        getPopup().setDynamic();
        getPopup().getChildByXpath(POPUP_CONTENT).waitForClass(POPUP_CONTENT_CLASS_MORE_COLLORS);
        getPopup().setDefaultElement();
        getPopup().getChildByXpath(POPUP_MORE_COLLORS_INPUT).type(colorToString(col));
        PSLogger.save("save after type color in more colors popup");
        getPopup().getChildByXpath(POPUP_MORE_COLLORS_OK).click(false);
        getPopup().waitForUnvisible();
    }

    @Deprecated
    // this method doesn't work correct
    public Color setUsingMoreColorsPopupPickerBox(float x, float y, float z) {
        PSLogger.info("Set color using picker box in more colors popup, coords to click: " + x + "," + y + "," + z);
        openPopup();
        Element link = getPopup().getChildByXpath(POPUP_MORE_COLORS);
        PSLogger.debug("Click on link: " + link.getText());
        link.click(false);
        getPopup().getChildByXpath(POPUP_CONTENT).waitForClass(POPUP_CONTENT_CLASS_MORE_COLLORS);

        Element underlay = getPopup().getChildByXpath(POPUP_MORE_COLLORS_PICKER_UNDERLAY);
        Element underlayCursor = getPopup().getChildByXpath(POPUP_MORE_COLLORS_PICKER_UNDERLAY_CURSOR);
        Element box = getPopup().getChildByXpath(POPUP_MORE_COLLORS_PICKER_BOX);
        Element boxCursor = getPopup().getChildByXpath(POPUP_MORE_COLLORS_PICKER_BOX_CURSOR);
        Element preview = getPopup().getChildByXpath(POPUP_MORE_COLLORS_PICKER_PREVIEW);
        String attr = preview.getAttribute(POPUP_COLOR_CELLS_STYLE.getLocator());
        Color colorWas = getColorFromAttribute(attr);

        Integer[] underlayCoords = underlay.getCoordinates();
        Integer[] boxCoords = box.getCoordinates();
        Integer[] underlayCursorCoords = underlayCursor.getCoordinates();
        Integer[] boxCursorCoords = boxCursor.getCoordinates();

        int uX = underlayCoords[2] / 2;
        int uY = (int) (z * underlayCoords[3]);
        int bX = (int) (x * boxCoords[2]);
        int bY = (int) (y * boxCoords[3]);
        //underlay.clickAt(uX, uY);
        //box.clickAt(bX, bY);
        //underlay.mouseDownAndUp(uX, uY);
        //box.mouseDownAndUp(bX, bY);
        //underlayCursor.dragAndDrop(uX, uY);
        //boxCursor.dragAndDrop(bX, bY);
        //underlay.mouseMoveAt(10, 50);
        //getPopup().focus();
        //underlay.click(false);
        //underlayCursor.mouseDown();
        //underlay.mouseUp();
        //underlay.mouseDownAndUp(10,150);
        //LocalServerUtils.click();
        //underlayCursor.mouseDown();
        //underlayCursor.mouseMoveAt(0, 93);
        //underlayCursor.mouseUp();
        //underlay.mouseDownAndUp();
        //new TimerWaiter(3000).waitTime();
        //boxCursor.dragAndDrop(10,100);
        //box.click(false);
        //underlayCursor.dragAndDrop(0, 40);
        //underlayCursor.mouseDown();
        //underlay.mouseMoveAt(uX, uY);
        //underlay.mouseUp();

        Color colorNow = getColorFromAttribute(attr);
        if (colorWas.equals(colorNow)) {
            PSLogger.warn("Color is not changed: " + colorWas + "!");
        } else {
            PSLogger.debug("Color changed to " + colorNow);
        }
        PSLogger.save("save after type color to more colors popup");
        getPopup().getChildByXpath(POPUP_MORE_COLLORS_OK).click(false);
        getPopup().waitForUnvisible();
        return colorNow;
    }


    public static String colorToString(Color col) {
        return Integer.toHexString(col.getRGB()).replaceAll("^ff", "");
    }

    public void setUsingInput(Color col) {
        PSLogger.info("Set color " + col + " using input");
        setUsingInput(colorToString(col));
    }


    public void setUsingInput(String col) {
        getChildByXpath(INPUT_COLOR).type(col);
    }

    public void setUsingPopup(Color col) {
        SeleniumException th = null;
        for (int i = 0; i < POPUP_ATTEMPTS; i++) {
            try {
                _setUsingPopup(col);
                return;
            } catch (SeleniumException se) { // this is for web-driver
                th = se;
                PSLogger.fatal(se.getMessage());
            }
        }
        if (th != null) throw th;
    }

    private void _setUsingPopup(Color col) {
        PSLogger.info("Set color " + col + " using popup");
        openPopup();
        Element e = colors.get(col);
        if (e == null) {
            PSLogger.warn("Can't find color " + col + " in popup");
            return;
        }
        PSLogger.save("save before choosing color in popup");
        e.click(false);
        getPopup().waitForUnvisible();
    }

    public void clearColorUsingPopup() {
        setUsingPopup(null);
    }

    public Color getColorFromInput() {
        String txt = getChildByXpath(INPUT_COLOR).getValue();
        if (txt != null && !txt.isEmpty()) {
            return Color.decode("0x" + txt);
        }
        return null;
    }

    public Color getColorFromDiv() {
        String attr = getChildByXpath(DIV_COLOR).
                getAttribute(POPUP_COLOR_CELLS_STYLE);
        Color res = getColorFromAttribute(attr);
        return res;
    }

    public List<Color> getAllColorsFromPopup() {
        return new ArrayList<Color>(colors.keySet());
    }

    public void setAllColorsFromPopup() {
        for (Element o : Element.searchElementsByXpath(getPopup(), POPUP_COLOR_CELLS)) {
            String attr = o.getDEAttribute(POPUP_COLOR_CELLS_STYLE);
            Color col = getColorFromAttribute(attr);
            Element eCol = o.getParent(POPUP_COLOR_CELL);
            colors.put(col, eCol);
        }
    }

    private static Color getColorFromAttribute(String attr) {
        if (attr == null || attr.isEmpty()) return null;
        String sCol = attr.toLowerCase().replaceAll(".*" +
                POPUP_COLOR_CELLS_STYLE_COLOR.getLocator() +
                "\\s+", "").replaceAll(";+.*", "");
        Color col = null;
        if (sCol.startsWith("#")) { //ie: COLOR: #ffffe0; BACKGROUND-COLOR: #ffffe0
            col = Color.decode(sCol.replace("#", "0x"));
        } else if (sCol.matches("rgb\\(.*\\)")) { //ff,safari:background-color: rgb(139, 0, 0); color: rgb(139, 0, 0);
            String[] aCol = sCol.replaceAll("rgb\\(", "").replaceAll("\\)", "").split(",\\s*");
            col = new Color(Integer.parseInt(aCol[0]), Integer.parseInt(aCol[1]), Integer.parseInt(aCol[2]));
        }
        return col;
    }

    private Element getOpenPopupIcon() {
        return getChildByXpath(POPUP_OPEN_ICON);
    }

}
