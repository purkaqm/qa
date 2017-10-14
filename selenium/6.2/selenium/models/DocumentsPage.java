package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;
import test.service.AttachmentComparator;
import test.service.Config;

public class DocumentsPage {

	private MySeleniumDriver mydriver;

	public DocumentsPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisDocumentsPage() {
		return mydriver.getTitle().contains("Documents");
	}

	public void addDocument(DocumentHolder dH) throws Exception{
		//two different cases - if we have a button or a link
		if (mydriver.isElementPresent("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')")){
			mydriver.click("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_DLG')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('PSForm')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsButton_ADD_DOC_FORM_FILE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsButton_ADD_DOC_FORM_TITLE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_ADD_DOC')", "30000");
			//fill out the Add Document Pop-Up fields
			mydriver.type("dom=document.getElementById('addNewDocumentsButton_ADD_DOC_FORM_FILE')",
					Config.getInstance().getAttachmentsDir() + dH.getLocation());
			mydriver.type("dom=document.getElementById('addNewDocumentsButton_ADD_DOC_FORM_TITLE')",
					dH.getTitle());
			mydriver.click("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_ADD_DOC')");
			//Wait 1 minute for attachment to load (and 'loading.gif' to disappear)
			long timeToStop = System.currentTimeMillis() + 60000;
			String submittedDocumentsElementsHtml = "";
			do {
				submittedDocumentsElementsHtml = mydriver.getEval("window.document.getElementById('scroll_text').innerHTML");
				} while ((submittedDocumentsElementsHtml.indexOf("loading.gif") > -1) & (System.currentTimeMillis() < timeToStop));
			//close with Done
			mydriver.click("dom=document.getElementById('scroll_clipper').parentNode.getElementsByTagName('input')[0]");
			mydriver.waitForPageToLoad("30000");
		}else{
			mydriver.click("dom=document.getElementById('addNewDocumentsLink_ADD_DOCS_DLG_SHOW')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('addNewDocumentsLink_ADD_DOCS_ADD_DOC')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('PSForm')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsLink_ADD_DOC_FORM_FILE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsLink_ADD_DOC_FORM_TITLE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addNewDocumentsLink_ADD_DOCS_ADD_DOC')", "30000");
			//fill out the Add Document Pop-Up fields
			mydriver.type("dom=document.getElementById('addNewDocumentsLink_ADD_DOC_FORM_FILE')",
					Config.getInstance().getAttachmentsDir() + dH.getLocation());
			mydriver.type("dom=document.getElementById('addNewDocumentsLink_ADD_DOC_FORM_TITLE')",
					dH.getTitle());
			mydriver.click("dom=document.getElementById('addNewDocumentsLink_ADD_DOCS_ADD_DOC')");
			//Wait 1 minute for attachment to load (and 'loading.gif' to disappear)
			long timeToStop = System.currentTimeMillis() + 60000;
			String submittedDocumentsElementsHtml = "";
			do {
				submittedDocumentsElementsHtml = mydriver.getEval("window.document.getElementById('scroll_text').innerHTML");
				} while ((submittedDocumentsElementsHtml.indexOf("loading.gif") > -1) & (System.currentTimeMillis() < timeToStop));
			//close with Done
			mydriver.click("dom=document.getElementById('scroll_clipper').parentNode.getElementsByTagName('input')[0]");
			mydriver.waitForPageToLoad("30000");
		}
		//TODO add handling of URL documents
	}

	public void clickUpdate(DocumentHolder dH) throws InterruptedException{
		int i;
		//find the link which spawns the pop-up for the given document
		int uidIndex = -1;
		String thisUIDsText = null;
		String thisUIDsString = null;
		i = 1;
		do {
			thisUIDsText = "";
			thisUIDsString = mydriver.getEval("window.document.getElementById('uid_" + i + "')");
			if (!(thisUIDsString.equals("null"))){
				thisUIDsText = mydriver.getText("dom=document.getElementById('uid_" + i + "')").trim();
				if (thisUIDsText.equals(dH.getTitle())) uidIndex = i;
				i++;
			}
		} while ((!(thisUIDsString.equals("null"))) & (!(thisUIDsText.equals(dH.getTitle()))));
		if (thisUIDsString.equals("null")) throw new IllegalStateException("No attachment link found for: " + dH.getTitle());
		//click the attachment link, find the spawned pop-up and extract the download link
		mydriver.click("dom=document.getElementById('uid_" + uidIndex + "')");
		// wait 1 second to let dojo spawn the pop-up properly
		Thread.sleep(1000);
		//find the visible pop-up
		int popUpIndex = -1;
		int popupsCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('dojoPopupMenu2').length"));
		Boolean thisPopUpVisibility = null;
		i = 0;
		do {
			thisPopUpVisibility = mydriver.isVisible("dom=window.dojo.html.getElementsByClass('dojoPopupMenu2')[" + i + "]");
			if (thisPopUpVisibility) popUpIndex = i;
			i++;
		}while ((i < popupsCount) & (!(thisPopUpVisibility)));
		int popupLinksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('dojoPopupMenu2')[" + popUpIndex + "].getElementsByTagName('a').length"));
		// find the DocumentListing,$PSObjectLink_0.documentLink.$DirectLink.sdirect link on the visible popup
		int linkIndex = -1;
		String thisLinksHref = null;
		i = 0;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('dojoPopupMenu2')[" + popUpIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("DocumentListing,$PSObjectLink_0.documentLink.$DirectLink.sdirect") > -1) linkIndex = i;
			i++;
		}while ((i < popupLinksCount) & (thisLinksHref.indexOf("DocumentListing,$PSObjectLink_0.documentLink.$DirectLink.sdirect") < 0));
		//Click the update link
		mydriver.click("dom=window.dojo.html.getElementsByClass('dojoPopupMenu2')[" + popUpIndex + "].getElementsByTagName('a')[" + linkIndex + "]");
		mydriver.waitForPageToLoad("30000");

	}

}
