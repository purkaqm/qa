package selenium.models;

import java.util.LinkedList;
import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class WorkBreakdownPage {

	private MySeleniumDriver mydriver;

	public WorkBreakdownPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisWorkTreePage() {
		return mydriver.getTitle().contains("Work Breakdown");
	}

	public void add(LinkedList<NewWorkHolder> works) throws InterruptedException {
		mydriver.waitForElementToAppear("dom=document.getElementById('table0')", "30000");
		mydriver.click("dom=document.getElementById('table0').getElementsByTagName('span')[0]");
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('dlgRowMenu')", "30000");
		mydriver.waitForElementToAppear("dom=document.getElementById('PSForm_1')", "30000");
		mydriver.click("dom=document.getElementById('dlgRowMenuAddM').getElementsByTagName('a')[0]");
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('dlgAddMult')", "30000");
		mydriver.waitForElementToAppear("dom=document.getElementById('addMultForm')", "30000");
		int i;
		for (i=0; (i < works.size()) & (i < 10); i++){
			mydriver.type("dom=document.getElementById('addMultName" + i + "')", works.get(i).getProjectName());
			//TODO add type selection
		}
		mydriver.click("dom=document.getElementById('Button_8')");
		mydriver.waitForElementToBecomeInvisible("dom=window.dojo.html.getElementsByClass('dialogUnderlay')[0]", "30000");
		Thread.sleep(2000);
	}

	public void indent(NewWorkHolder nWH) throws InterruptedException{
		mydriver.waitForElementToAppear("dom=document.getElementById('table0')", "30000");
		String wbsNodeToClick = "dom=document.getElementById('table0').getElementsByTagName('span')["
			+ this.getWorkIndex(nWH) + "].parentNode";
		mydriver.click(wbsNodeToClick);
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('dlgRowMenu')", "30000");
		mydriver.waitForElementToAppear("dom=document.getElementById('PSForm_1')", "30000");
		mydriver.click("dom=document.getElementById('dlgRowMenuInd').getElementsByTagName('a')[0]");
		mydriver.waitForElementToBecomeInvisible("dom=window.dojo.html.getElementsByClass('dialogUnderlay')[0]", "30000");
	}

	public int getWorkIndex(NewWorkHolder nWH){
		int workIndex = -1;
		int worksCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('table0').getElementsByTagName('span').length"));
		String thisWorksText = null;
		int i = 0;
		do {
			thisWorksText = mydriver.getText("dom=document.getElementById('table0').getElementsByTagName('span')[" + i + "]");
			if (thisWorksText.trim().equals(nWH.getProjectName())) workIndex = i;
			i++;
			} while ((i < worksCount) & (!(thisWorksText.trim().equals(nWH.getProjectName()))));
		return workIndex;
	}
}
