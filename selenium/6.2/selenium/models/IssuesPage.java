package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class IssuesPage {

	private MySeleniumDriver mydriver;

	public IssuesPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisIssuesPage() {
		return mydriver.getTitle().contains("Issues");
	}

	public void clickNewIssue(){
		mydriver.click("dom=document.getElementById('AddIssueLink')");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickIssueLink(IssueBlockHolder iBH){
		int discussionsLinksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('PSTable').getElementsByTagName('a').length"));
		int i = 0;
		String thisLinksText = null;
		do {
			thisLinksText = mydriver.getText("dom=document.getElementById('PSTable').getElementsByTagName('a')[" + String.valueOf(i) + "]");
			i++;
			} while ((i < discussionsLinksCount) & (!(thisLinksText.trim().equals(iBH.getSubject()))));
		mydriver.click("dom=document.getElementById('PSTable').getElementsByTagName('a')[" + String.valueOf(i - 1) + "]");
		mydriver.waitForPageToLoad("30000");
	}

}
