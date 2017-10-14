package selenium.models;

import selenium.driver.MySeleniumDriver;

public class HomePage{

	private MySeleniumDriver mydriver;

	public HomePage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisHomePage() {
		return mydriver.getTitle().contains("Home");
	}

}
