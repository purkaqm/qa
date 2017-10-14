package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class DiscussionsPage{

	private MySeleniumDriver mydriver;

	public DiscussionsPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisDiscussionsPage() {
		return mydriver.getTitle().contains("Discussions");
	}

	public void doClickNewDiscussion(){
		mydriver.click("dom=document.getElementById('AddDiscussionLink')");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickDiscussionLink(DiscussionBlockHolder dBH){
		int discussionsLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('PSTable').getElementsByTagName('a').length"));
		int i = 0;
		String thisLinksText = null;
		do {
			thisLinksText = mydriver.getText("dom=document.getElementById('PSTable').getElementsByTagName('a')[" + String.valueOf(i) + "]");
			i++;
			} while ((i < discussionsLinksCount) & (!(thisLinksText.trim().equals(dBH.getSubject()))));
		mydriver.click("dom=document.getElementById('PSTable').getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");
	}

}
