package selenium.models;

import selenium.driver.MySeleniumDriver;

public class BrowsePopup{

	private MySeleniumDriver mydriver;

	public BrowsePopup(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isBrowsePopupVisible() throws InterruptedException{
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('BrowseMenu')", "5000");
		return true;
	}

	private Integer getBrowsePopupLinkIndex(String linksHrefPart){
		int browsePopupLinkCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('BrowseMenu').getElementsByTagName('a').length"));
		int i = 0;
		String thisLinksHref;
		do{
			thisLinksHref = mydriver.getEval("window.document.getElementById('BrowseMenu').getElementsByTagName('a')[" + String.valueOf(i) + "].href");
			i++;
		} while ((thisLinksHref.indexOf(linksHrefPart) < 0) && (i < browsePopupLinkCount));
		return (i - 1);
	}

	public void clickCreateNewWork(){
		mydriver.click("dom=document.getElementById('BrowseMenu').getElementsByTagName('a')["
				+ getBrowsePopupLinkIndex("createNewWorkFormInBrowseMenu").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickWorkTree(){
		mydriver.click("dom=document.getElementById('BrowseMenu').getElementsByTagName('a')["
				+ getBrowsePopupLinkIndex("WorkTree.page").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

}
