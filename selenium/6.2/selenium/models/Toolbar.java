package selenium.models;

import selenium.driver.MySeleniumDriver;

public class Toolbar{

	private MySeleniumDriver mydriver;

	public Toolbar(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isToolbarPresent(){
		return mydriver.isElementPresent("dom=document.getElementById('sub').getElementsByTagName('ul')[0]");
	}

	private Integer getToolbarLinkIndex(String linksHrefPart){
		int toolbarLinkCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a').length"));
		int i = 0;
		String thisLinksHref;
		do{
			thisLinksHref = mydriver.getEval("window.document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a')["
					+ String.valueOf(i) + "].href");
			i++;
		} while ((thisLinksHref.indexOf(linksHrefPart) < 0) && (i < toolbarLinkCount));
		return (i - 1);
	}

	public void clickDiscussionsLink(){
		mydriver.click("dom=document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a')["
				+ getToolbarLinkIndex("Discussions").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickIssuesLink(){
		mydriver.click("dom=document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a')["
				+ getToolbarLinkIndex("Issues").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickDocumentsLink(){
		mydriver.click("dom=document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a')["
				+ getToolbarLinkIndex("DocumentListing").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickWorkBreakdownLink(){
		mydriver.click("dom=document.getElementById('sub').getElementsByTagName('ul')[1].getElementsByTagName('a')["
				+ getToolbarLinkIndex("WBS").toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}
}
