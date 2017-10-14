package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class AddEditActionItemPage {

	private MySeleniumDriver mydriver;

	public AddEditActionItemPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisAddEditActionItemPage() {
		return mydriver.getTitle().contains("Add/Edit Action Item");
	}

	public void addTask(ActionItemHolder aIH) throws Exception{
		mydriver.type("dom=document.getElementById('nameMasterTask')", aIH.getName());
//		mydriver.runScript("tinyMCE.setContent('" + dBH.getMessage() + "')");
		mydriver.click("dom=document.getElementById('Submit_3')");
		mydriver.waitForPageToLoad("30000");
	}


}
