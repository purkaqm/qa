package selenium.models;

import selenium.driver.MySeleniumDriver;

public class TagPage {


	private MySeleniumDriver mydriver;

	public TagPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisTagPage() {
		return mydriver.getTitle().contains("Tag");
	}

	public void clickAddUpdateValuesLink() {
		int contentLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('content').getElementsByTagName('a').length"));
		for (int i=0; i < contentLinksCount; i++){
			String thisLinksHref = mydriver.getEval("window.document.getElementById('content').getElementsByTagName('a')[" + String.valueOf(i) + "].href");
			if (thisLinksHref.indexOf("TagsEdit.epage") > -1){
				mydriver.click("dom=document.getElementById('content').getElementsByTagName('a')[" + String.valueOf(i) + "]");
				mydriver.waitForPageToLoad("30000");
			}
		}
		mydriver.waitForPageToLoad("30000");
	}

}
