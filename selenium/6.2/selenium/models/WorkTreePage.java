package selenium.models;

import selenium.driver.MySeleniumDriver;

public class WorkTreePage{

	private MySeleniumDriver mydriver;

	public WorkTreePage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisWorkTreePage() {
		return mydriver.getTitle().contains("Work Tree");
	}

	public boolean isWorkLinkPresent(String workName) throws InterruptedException {
		return mydriver.isElementPresent("dom=document.getElementById('content').getElementsByTagName('a')["
				+ this.getWorkLinkIndex(workName).toString() + "]");
	}

	public void waitForWorkTreeLoad() throws InterruptedException{
		Long whenToStop = System.currentTimeMillis() + 60000;
		Boolean isAnyLinkPresentInContent;
		//Just wait until there is at least one link in 'content' area
		do {
			isAnyLinkPresentInContent = mydriver.isElementPresent("dom=document.getElementById('content').getElementsByTagName('a')[0]");
			Thread.sleep(500);
		} while ((!(isAnyLinkPresentInContent)) && (System.currentTimeMillis() < whenToStop));
		if (!(isAnyLinkPresentInContent))
			throw new IllegalStateException("Timed out on waiting for Work Tree load");
	}

	public void clickWorkLink(String workName){
		mydriver.click("dom=document.getElementById('content').getElementsByTagName('a')["
				+ this.getWorkLinkIndex(workName).toString() + "]");
		mydriver.waitForPageToLoad("30000");
	}

	private Integer getWorkLinkIndex(String workName){
		int workTreeLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('content').getElementsByTagName('a').length"));
		int i = 0;
		String thisLinksText;
		do{
			thisLinksText = mydriver.getText("dom=document.getElementById('content').getElementsByTagName('a')["
					+ String.valueOf(i) + "]");
			i++;
		} while ((!(thisLinksText.equals(workName))) && (i < workTreeLinksCount));
		return (i - 1);
	}
}
