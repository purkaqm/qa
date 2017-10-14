package com.powersteeringsoftware.core.enums;

public enum BrowserTypes {

	BROWSER_FIREFOX_CHROME("*chrome"),
	BROWSER_FIREFOX("*firefox"),
	BROWSER_IE_IEHTA("*iehta"),
	BROWSER_IE("*iexplorer");

	String browser;

	BrowserTypes(String _browser){
		browser = _browser;
	}

	public String getBrowser() {
		return browser;
	}

}
