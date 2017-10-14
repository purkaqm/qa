package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;

import java.util.Arrays;
import java.util.List;

import static com.powersteeringsoftware.libs.enums.page_locators.BasicCommonsLocators.ERROR_BOX;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 15.09.11
 * Time: 15:16
 */
public abstract class Window extends Page {
    protected int index;
    protected boolean doMaximize = true;
    protected String name;
    private static final long TIMEOUT_FOR_POPUP = CoreProperties.getWaitForElementToLoad();
    private static final long TIMEOUT_TO_OPEN = CoreProperties.getWaitForElementToLoad() / 4;
    public static final TimerWaiter STEP = new TimerWaiter(1000);

    public Window() {
        this(1);
    }

    public Window(int i) {
        index = i;
    }

    public static List<String> getTitles() {
        String[] list1 = SeleniumDriverFactory.getDriver().getAllWindowTitles();
        String[] list2 = SeleniumDriverFactory.getDriver().getAllWindowNames();
        String[] list3 = SeleniumDriverFactory.getDriver().getAllWindowIds();
        for (int i = 0; i < list1.length; i++) {
            if (list1[i] == null || list1[i].isEmpty()) {
                String toSet = list2 != null ? list2[i] : null;
                if (toSet == null || toSet.isEmpty()) {
                    toSet = list3 != null ? list3[i] : null;
                }
                if (toSet != null && !toSet.isEmpty())
                    list1[i] = toSet;
            }
        }
        List<String> res = Arrays.asList(list1);
        PSLogger.debug("All window titles : " + res);
        return res;
    }

    public String getName() {
        return name;
    }

    public void waitForOpen() {
        if (name == null) {
            long s = System.currentTimeMillis();
            while (System.currentTimeMillis() - s < TIMEOUT_TO_OPEN) {
                if (getTitles().size() > index) {
                    name = getTitles().get(index);
                    break;
                }
                STEP.waitTime();
            }
            if (name == null) {
                PSLogger.warn("Can't find new window");
                throw new SeleniumException("Can't find new window");
                //return;
            }
        }
        PSLogger.debug("Wait for window " + getName());
        try {
            getDriver().waitForPopUp(getName(), String.valueOf(TIMEOUT_FOR_POPUP));
        } catch (SeleniumException se) {
            // todo: throw if rc-driver?
            PSLogger.warn("waitForPopUp: " + se.getMessage()); // this happens when use web-driver (ff6.0 at least). unsupported method.
            new TimerWaiter(5000).waitTime();
        }
        PSLogger.save("before select");
        select();
        if (doMaximize)
            getDriver().windowMaximize();
        if (!getDriver().getType().isRCDriverIE())
            waitForPageToLoad(TIMEOUT_FOR_POPUP);
    }

    public void select() {
        select(getName());
    }

    private void select(String name) {
        if (name == null) {
            PSLogger.warn("Nothing to select");
            return;
        }
        PSLogger.debug("Select '" + name + "'");
        getDriver().selectWindow(name);
        getDriver().windowFocus();
    }

    public void deselect() {
        /*
        getDriver().deselectPopUp();
        getDriver().windowFocus();
        */
        int index = this.index == 0 ? 0 : this.index - 1;
        PSLogger.info("Deselect");
        String name = getTitles().get(index);
        select(name);
    }

    public void close() {
        PSLogger.debug("Close window " + getName());
        deselect();
        index = 0;
    }

    public String getErrorBoxMessage() {
        Element error = new Element(ERROR_BOX);
        if (!error.exists()) return null;
        StringBuilder sb = new StringBuilder();
        error.setDefaultElement(getDocument());
        sb.append(error.getDEText());
        List<Element> res = Element.searchElementsByXpath(error, "//div");
        for (Element e : res) {
            Element txt = e;
            Element ch = e.getChildByXpath("//strong");
            if (ch.isDEPresent()) txt = ch;
            sb.append(StrUtil.trim(txt.getDEText())).append("\n");
        }
        PSLogger.debug(sb);
        return res.size() == 0 ? sb.toString() : sb.substring(0, sb.length() - 1);
    }

    protected void openNew(String url) {
        if (name == null) throw new NullPointerException("openNew: specify name");
        String u = getDriver().getBaseUrl() + url;
        PSLogger.info("Open new window by url: " + u);
        getDriver().openWindow(u, name);
    }
}
