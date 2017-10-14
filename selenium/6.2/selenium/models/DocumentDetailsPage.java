package selenium.models;

import java.io.IOException;
import java.net.URLDecoder;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;
import test.service.*;

public class DocumentDetailsPage {

	private MySeleniumDriver mydriver;

	public DocumentDetailsPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisDocumentDetailsPage() {
		return mydriver.getTitle().contains("Document Details");
	}

	public void updateWith(DocumentHolder dH, String status) throws Exception{
		//TODO redo this, introduce a special holder for update document form objects
		//check if the update form is in place
		if (!(mydriver.isElementPresent("dom=document.getElementById('PSForm_0_0')")))
			throw new IllegalStateException("Update Document form is not present");
		//Assume that the document is locked and ready for update, fill the form and click submit
		mydriver.type("dom=document.getElementById('upload')", Config.getInstance().getAttachmentsDir() + dH.getLocation());
		//TODO add status handling
		mydriver.click("dom=document.getElementById('LinkSubmit_0')");
		mydriver.waitForPageToLoad("30000");
	}

	public void testVersion(Integer versionNumber, DocumentHolder dH) throws IOException, Exception{

		//TODO redo to test all attachments
		int i;
		//find the index of the link which corresponds to the versionNumber
		int linkIndex = -1;
		int linksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('versionColumnValue', window.dojo.byId('PSTable')).length"));
		String thisLinksText = null;
		i = 0;
		do {
			thisLinksText = mydriver.getText("dom=window.dojo.html.getElementsByClass('versionColumnValue', window.dojo.byId('PSTable'))[" + i + "].getElementsByTagName('a')[0]");
			if (thisLinksText.equals("#" + versionNumber.toString())) linkIndex = i;
			i++;
		} while ((i < linksCount) & (!(thisLinksText.equals("#" + versionNumber.toString()))));
		if (!(thisLinksText.equals("#" + versionNumber.toString()))) throw new IllegalStateException("No '" + "#" + versionNumber.toString() + "' version link found for");
		//get the found javascript link, extract the http part and decode it
		String versionDownloadLink = mydriver.getEval("window.dojo.html.getElementsByClass('versionColumnValue', window.dojo.byId('PSTable'))[" + linkIndex + "].getElementsByTagName('a')[0].href");
		versionDownloadLink = URLDecoder.decode(versionDownloadLink.split("'")[1], "UTF-8");
		//Compare and throw the exception if no match
		AttachmentComparator ac = new AttachmentComparator();
		if (!(ac.compareURLWithLocalFile(mydriver.getCookie(), versionDownloadLink, Config.getInstance().getAttachmentsDir() + dH.getLocation())))
			throw new IllegalStateException("Version " + "#" + versionNumber.toString() + " content and the local copy " + dH.getLocation() + " do not match");
	}

}
