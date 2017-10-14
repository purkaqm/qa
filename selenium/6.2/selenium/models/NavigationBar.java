package selenium.models;

import selenium.driver.MySeleniumDriver;

public class NavigationBar{

	private MySeleniumDriver mydriver;

	public NavigationBar(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isNavigationBarVisible(){
		return mydriver.isVisible("dom=document.getElementById('nav')");
	}

	public void clickBrowse(){
		mydriver.click("dom=document.getElementById('BrowseShow')");
	}

	public void clickAdmin(){
		mydriver.click("dom=document.getElementById('AdminShow')");
	}

}
