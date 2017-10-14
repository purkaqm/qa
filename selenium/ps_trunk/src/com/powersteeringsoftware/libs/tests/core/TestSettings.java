package com.powersteeringsoftware.libs.tests.core;

import com.powersteeringsoftware.libs.core.SeleniumDriver;
import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.settings.BrowserTypes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 23.12.11
 * Time: 15:40
 */
public class TestSettings {
    // common groups:

    // use this groups to exclude test-method in case current app version is less than specified in @Test
    static final String _LESS = "_less";
    public static final String PS_82_GROUP = "_8_2" + _LESS;
    public static final String PS_90_GROUP = "_9_0" + _LESS;
    public static final String PS_91_GROUP = "_9_1" + _LESS;
    public static final String PS_92_GROUP = "_9_2" + _LESS;
    public static final String PS_93_GROUP = "_9_3" + _LESS;
    public static final String PS_100_GROUP = "_10_0" + _LESS;

    // use this groups to exclude test-method in case current app version is greater than specified in @Test
    static final String _GREATER = "_great";
    public static final String PS_92_GREATER_GROUP = "_9_2" + _GREATER;
    public static final String PS_93_GREATER_GROUP = "_9_3" + _GREATER;
    public static final String PS_100_GREATER_GROUP = "_10_0" + _GREATER;

    public static final String FIREFOX_ONLY_GROUP = "firefox.only";
    public static final String IE_ONLY_GROUP = "ie.only";
    public static final String SAFARI_ONLY_GROUP = "safari.only";
    public static final String CHROME_ONLY_GROUP = "googlechrome.only";
    public static final String DEVELOPMENT_GROUP = "development";
    public static final String BROKEN_GROUP_PREFIX = "broken.";
    public static final String BROKEN_ALL_GROUP = BROKEN_GROUP_PREFIX + "all";
    public static final String NOT_IE_RC_GROUP = BROKEN_GROUP_PREFIX + "iexplore.false";

    // specified for test groups:
    public static final String ACCEPTANCE_GROUP = "acceptance.test";
    public static final String GATED_PROJECT_GROUP = "gatedproject.test";
    public static final String CONTENT_FILTER_GROUP = "contentfiller.test";

    public static final String USE_RC_SUITE_PARAMETER = "use_rc"; // to set way to run in xml


    private static Boolean attachingWorkaroundForFF80(SeleniumDriver driver, List<String> groups) {
        if (!groups.contains(ACCEPTANCE_GROUP)) return null;
        BrowserTypes browser = driver.getType();
        if (!browser.isFF(8)) return null;
        if (!SeleniumDriverFactory.getVersion().isAttachingBroken(driver)) return null;
        return !browser.isRCDriver();
    }

    private static Boolean dNdWorkaroundForWebDriver(SeleniumDriver driver, List<String> groups) {
        if (!groups.contains(GATED_PROJECT_GROUP)) return null;
        BrowserTypes browser = driver.getType();
        if (!SeleniumDriverFactory.getVersion().isDragAndDropBroken(driver)) return null;
        return !browser.isWebDriver();
    }

    static Map _workaround(SeleniumDriver driver, List<String> groups) {
        Map<String, String> res = new HashMap<String, String>();
        Boolean type = attachingWorkaroundForFF80(driver, groups);
        //if (type == null) type = dNdWorkaroundForWebDriver(driver, groups);
        if (type != null) res.put(USE_RC_SUITE_PARAMETER, String.valueOf(type));
        return res;
    }
}
