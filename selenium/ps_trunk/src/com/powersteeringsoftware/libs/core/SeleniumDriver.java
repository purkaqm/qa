package com.powersteeringsoftware.libs.core;

import com.powersteeringsoftware.libs.settings.BrowserTypes;
import com.thoughtworks.selenium.Selenium;
import org.dom4j.Document;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 22.12.10
 * Time: 18:21
 */
public interface SeleniumDriver extends Selenium {

    String getCurrentFrame();

    BrowserTypes getType();

    String getBaseUrl();

    void captureScreenshotAsHtml(String file);

    Document getDocument(String frameLocator);

    Document getDocument();

    String getAttribute(String locator, String attributeName);

    Integer[] getCoordinates(String locator);

    void dragAndDropToElement(String locSrc, String locDst);

    void waitForPageToLoad();

    void waitForFrameToLoad(String frameAddress, String timeout);

    void waitForFrameToLoad(String loc);

    long getTimeout();

    String superGetValue(String locator);

    String superGetText(String locator);

    void open(String url, boolean skipIfOpen);

}
