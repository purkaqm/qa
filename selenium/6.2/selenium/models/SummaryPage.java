package selenium.models;

import selenium.driver.MySeleniumDriver;

public class SummaryPage{

	private MySeleniumDriver mydriver;

	public SummaryPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisSummaryPage() {
		return mydriver.getTitle().contains("Summary");
	}

	public boolean isThisSummaryPageWorkName(String workName) {
		return mydriver.getText("document.getElementById('sub').getElementsByTagName('span')[0]").trim().equals(workName);
	}

	public String getWorkPSID() {
		String currentURL = mydriver.getLocation();
		return currentURL.substring(currentURL.indexOf("?sp=") + 4, currentURL.length() - 1);
	}
}
