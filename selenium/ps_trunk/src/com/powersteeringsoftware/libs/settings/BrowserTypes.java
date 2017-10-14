package com.powersteeringsoftware.libs.settings;

import java.io.File;
import java.io.FileNotFoundException;

public enum BrowserTypes {
    NO_BROWSER("null"),
    ZERO("null") {
        public boolean isWebDriver() {
            return true;
        }
    },
    FF("firefox"),
    FF30("firefox3", "30", null),
    FF36("firefox3", "36", null),
    FF40("firefox", "40", null),
    FF50("firefox", "50", null),
    FF60("firefox", "60", null),
    FF70("firefox", "70", null),
    FF80("firefox", "80", null),
    FF90("firefox", "90", null),
    FF100("firefox", "100", null),
    FF110("firefox", "110", null),
    FF120("firefox", "120", null),
    FF130("firefox", "130", null),
    FF160("firefox", "160", null),
    FF170("firefox", "170", null),
    FF210("firefox", "210", null),
    FF260("firefox", "260", null),
    FF270("firefox", "270", null),
    FF280("firefox", "280", null),
    FF290("firefox", "290", null),
    FF300("firefox", "300", null),
    FF310("firefox", "310", null),
    FF320("firefox", "320", null),
    FF330("firefox", "330", null),
    FF340("firefox", "340", null),
    FF370("firefox", "370", null),
    FF380("firefox", "380", null),
    FF410("firefox", "410", null),

    IE("iexplore"),
    IE6("iexplore", "6", null, "iexplore.exe"),
    IE7("iexplore", "7", null),
    IE8("iexplore", "8", null),
    IE9("iexplore", "9", null),
    IE10("iexplore", "10", null),
    IE11("iexplore", "11", null),
    SF("safati"),
    SF3("safati", "3", null),
    GCH("googlechrome", "10", null, "chrome.exe"),;

    private static final String FULL_NAME_IEXPLORE = "Internet Explorer";
    private static final String FULL_NAME_FIREFOX = "Firefox";
    private static final String FULL_NAME_GOOGLE_CHROME = "Google Chrome";
    private static final String FULL_NAME_SAFARI = "Safari";

    String browser;
    String name;
    String process;
    String path;
    Driver driver;
    String version;
    String cmd;
    String rightVersion;
    String profile;
    boolean webDriver;

    BrowserTypes(String browser) {
        this.browser = browser;
        this.process = browser;
    }

    BrowserTypes(String browser, String version, String path) {
        this(browser, version, path, browser.replaceAll("\\d+", ""));
    }

    BrowserTypes(String browser, String version, String path, String process) {
        this.browser = browser;
        this.version = version;
        this.path = path;
        this.process = process;
    }

    public void setCommand(String cmd) {
        this.cmd = cmd;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setDriver(String path, String name) {
        driver = new Driver(path, name);
    }

    public void setVersion(String version) {
        this.rightVersion = version;
    }

    public String getPath() {
        return path;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public String getVersion() {
        return rightVersion == null ? version : rightVersion;
    }

    public int getMainVersion() {
        String ver = getVersion();
        if (ver == null) return 0;
        String res = ver.replaceFirst("\\..*", "");
        if (res.matches("\\d+"))
            return Integer.valueOf(res);
        return -1;
    }


    public String getBrowser() {
        return browser;
    }

    public String getName() {
        if (name != null) return name;
        if (isIE()) name = IE.browser;
        if (isFF()) name = FF.browser;
        if (isGoogleChrome()) name = GCH.browser;
        if (isSafari()) name = SF.browser;
        return name;
    }


    public String getProcess() {
        return process;
    }

    public String getCommand() {
        if (cmd != null) return cmd;
        return cmd = browser + (path != null ? " " + path : "");
    }

    public boolean isFF() {
        return name().startsWith(FF.name());
    }

    public boolean isFF(int num) {
        return isFF() && getMainVersion() == num;
    }


    public boolean isWebDriverFF() {
        return webDriver && isFF();
    }

    public boolean isWebDriverFF(int num) {
        return webDriver && isFF(num);
    }

    public boolean isIE() {
        return name().startsWith(IE.name());
    }

    public boolean isIE(int num) {
        return isIE() && getMainVersion() == num;
    }

    public boolean isWebDriverIE() {
        return webDriver && isIE();
    }

    public boolean isWebDriverIE(int ver) {
        return webDriver && isIE(ver);
    }

    public boolean isRCDriverIE() {
        return !webDriver && isIE();
    }

    public boolean isRCDriverFF() {
        return !webDriver && isFF();
    }

    public boolean isRCDriverIE(int num) {
        return !webDriver && isIE(num);
    }

    public boolean isIEGreaterThan(int num) {
        return isIE() && isGreaterThan(num);
    }

    public boolean isFFGreaterThan(int num) {
        return isFF() && isGreaterThan(num);
    }

    public boolean isGreaterThan(int num) {
        return getMainVersion() > num;
    }

    public boolean isLessThan(int num) {
        return getMainVersion() < num;
    }

    public boolean isSafari() {
        return name().startsWith(SF.name());
    }

    public boolean isGoogleChrome() {
        return name().startsWith(GCH.name());
    }

    public boolean isGoogleChrome(int num) {
        return isGoogleChrome() && getMainVersion() == num;
    }

    public boolean isGoogleChromeGreaterThan(int num) {
        return isGoogleChrome() && getMainVersion() > num;
    }


    public boolean isChromeOrSafari() {
        return isSafari() || isGoogleChrome();
    }

    public void setWebDriver() {
        setDriverType(true);
    }

    public void setRCDriver() {
        setDriverType(false);
    }

    public void setDriverType(boolean isWeb) {
        webDriver = isWeb;
    }

    public boolean isWebDriver() {
        return webDriver;
    }

    public boolean isRCDriver() {
        return !isWebDriver();
    }

    public boolean isZero() {
        return equals(ZERO);
    }

    public boolean isNull() {
        return equals(ZERO) || equals(NO_BROWSER);
    }


    public static BrowserTypes getBrowser(String value) {
        return valueOf(value.toUpperCase());
    }

    public String toString() {
        return "brand=" + name() + ",name=" + getName() +
                ",version=" + getVersion() + ",path=" + getPath() + ",start=" + getCommand() +
                ",web-driver=" + isWebDriver() + (getDriver() != null ? ",driver-path=" + getDriver().getPath() : "");
    }

    public String getFullName() {
        String br;
        if (isIE()) {
            br = FULL_NAME_IEXPLORE;
        } else if (isFF()) {
            br = FULL_NAME_FIREFOX;
        } else if (isSafari()) {
            br = FULL_NAME_SAFARI;
        } else if (isGoogleChrome()) {
            br = FULL_NAME_GOOGLE_CHROME;
        } else {
            br = browser;
        }
        return br + " " + getVersion();
    }

    public void parsePath() {
        if (path != null) return;
        if (cmd == null) throw new NullPointerException("Can't find cmd to parse");
        String path = cmd.replaceAll("^[^\\s]+\\s*", "");
        File file;
        if ((file = new File(path)).exists()) {
            this.path = file.getAbsolutePath();
        } else {
            try {
                throw new FileNotFoundException("Can't find browser path");
            } catch (FileNotFoundException e) {
                // ignore
            }
        }
    }

    public class Driver {
        private Driver(String p, String n) {
            path = p;
            if (n != null) {
                name = n;
            } else {
                name = new File(path).getName().replace(".[\\.]+$", "");
            }
        }

        private String path;
        private String name;

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }

        public String toString() {
            return name + "(" + path + ")";
        }
    }

}
