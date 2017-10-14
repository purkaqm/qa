package selenium.models;

import java.io.IOException;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;
import test.service.*;

public class ViewThreadPage{

	private MySeleniumDriver mydriver;

	public ViewThreadPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisViewThreadPage() {
		return mydriver.getTitle().contains("View Thread");
	}

	public void testAttachment(AttachmentLocalHolder aLH) throws IOException, Exception{
		//TODO redo to test all attachments
		int i;
		//find the link which spawn the pop-up for the given attachment
		int uidIndex = -1;
		String thisUIDsText = null;
		String thisUIDsString = null;
		i = 1;
		do {
			thisUIDsText = "";
			thisUIDsString = mydriver.getEval("window.document.getElementById('uid_" + i + "')");
			if (!(thisUIDsString.equals("null"))){
				thisUIDsText = mydriver.getText("dom=document.getElementById('uid_" + i + "')").trim();
				if (thisUIDsText.equals(aLH.getAttachmentTitle())) uidIndex = i;
				i++;
			}
		} while ((!(thisUIDsString.equals("null"))) & (!(thisUIDsText.equals(aLH.getAttachmentTitle()))));
		if (thisUIDsString.equals("null")) throw new IllegalStateException("No attachment link found for: " + aLH.getAttachmentTitle());
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
		// find the GetLinkServlet link on the visible popup
		int linkIndex = -1;
		String thisLinksHref = null;
		i = 0;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('dojoPopupMenu2')[" + popUpIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("com.cinteractive.ps3.documents.GetLinkServlet") < 0) linkIndex = i;
			i++;
		}while ((i < popupLinksCount) & (thisLinksHref.indexOf("com.cinteractive.ps3.documents.GetLinkServlet") < 0));
		//Compare and throw the exception if no match
		AttachmentComparator ac = new AttachmentComparator();
		if (!(ac.compareURLWithLocalFile(mydriver.getCookie(), thisLinksHref, Config.getInstance().getAttachmentsDir() + aLH.getAttachmentLocation())))
			throw new IllegalStateException("Attachment " + thisUIDsText
					+ " and its local copy " + aLH.getAttachmentLocation() + " do not match");
	}

	public void escalateDiscussion(DiscussionBlockHolder dbH){
		int i;
		//find the block for our discussion
		int blockIndex = -1;
		int blocksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block').length"));
		i = 0;
		String thisBlocksTitle = null;
		do {
			thisBlocksTitle = mydriver.getText("dom=window.dojo.html.getElementsByClass('block')[" + i + "].getElementsByTagName('span')[0]");
			if (thisBlocksTitle.equals(dbH.getSubject())) blockIndex = i;
			i++;
		}while ((i < blocksCount) & (!(thisBlocksTitle.equals(dbH.getSubject()))));
		//find the Escalate link
		int blockLinkIndex = -1;
		int blockLinksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a').length"));
		i = 0;
		String thisLinksHref = null;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("IssueAdd.epage") > -1) blockLinkIndex = i;
			i++;
		}while ((i < blockLinksCount) & (!(thisLinksHref.indexOf("IssueAdd.epage") > -1)));
		//click Escalate link
		mydriver.click("dom=window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + blockLinkIndex + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void deEscalateIssue(IssueBlockHolder iBH) throws InterruptedException{
		int i;
		//find the block
		int blockIndex = -1;
		int blocksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block').length"));
		i = 0;
		String thisBlocksTitle = null;
		do {
			thisBlocksTitle = mydriver.getText("dom=window.dojo.html.getElementsByClass('block')[" + i + "].getElementsByTagName('span')[0]");
			if (thisBlocksTitle.equals(iBH.getSubject())) blockIndex = i;
			i++;
		}while ((i < blocksCount) & (!(thisBlocksTitle.equals(iBH.getSubject()))));
		//find the de-escalate link
		int blockLinkIndex = -1;
		int blockLinksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a').length"));
		i = 0;
		String thisLinksHref = null;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("#") > -1) blockLinkIndex = i;
			i++;
		}while ((i < blockLinksCount) & (!(thisLinksHref.indexOf("#") > -1)));
		//click de-escalate link
		mydriver.click("dom=window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + blockLinkIndex + "]");
		mydriver.waitForElementToBecomeVisible("dom=document.getElementById('uid_1')", "30000");
		mydriver.click("dom=document.getElementById('uid_1').getElementsByTagName('input')[0]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickReply(ThreadBlockHolder tBH) throws InterruptedException{
		int i;
		//find the block
		int blockIndex = -1;
		int blocksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block').length"));
		i = 0;
		String thisBlocksTitle = null;
		do {
			thisBlocksTitle = mydriver.getText("dom=window.dojo.html.getElementsByClass('block')[" + i + "].getElementsByTagName('span')[0]");
			if (thisBlocksTitle.equals(tBH.getSubject())) blockIndex = i;
			i++;
		}while ((i < blocksCount) & (!(thisBlocksTitle.equals(tBH.getSubject()))));
		//find the reply link
		int blockLinkIndex = -1;
		int blockLinksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a').length"));
		i = 0;
		String thisLinksHref = null;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("DiscussionAdd") > -1) blockLinkIndex = i;
			i++;
		}while ((i < blockLinksCount) & (!(thisLinksHref.indexOf("DiscussionAdd") > -1)));
		//click reply link
		mydriver.click("dom=window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + blockLinkIndex + "]");
		mydriver.waitForPageToLoad("30000");
	}

	public void clickCloseIssue(IssueBlockHolder iBH){
		int i;
		//find the block
		int blockIndex = -1;
		int blocksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block').length"));
		i = 0;
		String thisBlocksTitle = null;
		do {
			thisBlocksTitle = mydriver.getText("dom=window.dojo.html.getElementsByClass('block')[" + i + "].getElementsByTagName('span')[0]");
			if (thisBlocksTitle.equals(iBH.getSubject())) blockIndex = i;
			i++;
		}while ((i < blocksCount) & (!(thisBlocksTitle.equals(iBH.getSubject()))));
		//find the close issue link
		int blockLinkIndex = -1;
		int blockLinksCount = Integer.valueOf(mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a').length"));
		i = 0;
		String thisLinksHref = null;
		do {
			thisLinksHref = mydriver.getEval("window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + i + "].href");
			if (thisLinksHref.indexOf("DiscussionIssueView,$DirectLink_0.sdirect") > -1) blockLinkIndex = i;
			i++;
		}while ((i < blockLinksCount) & (!(thisLinksHref.indexOf("DiscussionIssueView,$DirectLink_0.sdirect") > -1)));
		//click close issue link
		mydriver.click("dom=window.dojo.html.getElementsByClass('block')[" + blockIndex + "].getElementsByTagName('a')[" + blockLinkIndex + "]");
		mydriver.waitForPageToLoad("30000");

	}
}
