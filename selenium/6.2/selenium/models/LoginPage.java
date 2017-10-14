package selenium.models;

import selenium.driver.MySeleniumDriver;

public class LoginPage{

	private MySeleniumDriver mydriver;

	public LoginPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public void loginAs(String userName, String passWord) {
		mydriver.type("dom=document.getElementById('user')",userName);
		mydriver.type("dom=document.getElementById('pass')",passWord);
		mydriver.click("dom=document.getElementById('Submit_0')");
		mydriver.waitForPageToLoad("30000");
	}

	public boolean isThisLoginPage() {
		return mydriver.getTitle().contains("Login");
	}

	public boolean isLoginFormPresent() {
		return mydriver.isElementPresent("dom=document.getElementById('form')");
	}
}
