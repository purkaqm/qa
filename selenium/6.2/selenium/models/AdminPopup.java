package selenium.models;

import selenium.driver.MySeleniumDriver;

public class AdminPopup{

	private MySeleniumDriver mydriver;

	public AdminPopup(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isAdminPopupVisible() throws InterruptedException{
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('AdminMenu')", "5000");
		return true;
	}

	public void clickCustomFields(){
		int adminPopupLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a').length"));
		for (int i=0; i < adminPopupLinksCount; i++){
			String thisLinksHref = mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "].href");
			if (thisLinksHref.indexOf("CustomFields.page") > -1){
				mydriver.click("dom=document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "]");
				mydriver.waitForPageToLoad("30000");
			}
		}
	}

	public void clickTags(){
		int adminPopupLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a').length"));
		for (int i=0; i < adminPopupLinksCount; i++){
			String thisLinksHref = mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "].href");
			if (thisLinksHref.indexOf("TagSets.page") > -1){
				mydriver.click("dom=document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "]");
				mydriver.waitForPageToLoad("30000");
			}
		}
	}

	public void clickWorkTemplates(){
		int adminPopupLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a').length"));
		for (int i=0; i < adminPopupLinksCount; i++){
			String thisLinksHref = mydriver.getEval("window.document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "].href");
			if (thisLinksHref.indexOf("WorkTemplates.page") > -1){
				mydriver.click("dom=document.getElementById('AdminMenu').getElementsByTagName('a')[" + String.valueOf(i) + "]");
				mydriver.waitForPageToLoad("30000");
			}
		}
	}

}
