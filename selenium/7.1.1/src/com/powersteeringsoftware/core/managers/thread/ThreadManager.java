package com.powersteeringsoftware.core.managers.thread;

import java.util.Iterator;
import org.apache.log4j.Logger;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.ThreadBlock;
import com.powersteeringsoftware.core.objects.FileAttachment.AttachmentComparator;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;


/**
 * Class works only for 7.1
 *
 * @author selyaev_ag
 *
 */
public class ThreadManager {
	private Logger log  = Logger.getLogger(ThreadManager.class);
	private String localFilesFolder;

	public ThreadManager(String localFilesFolder){
		this.localFilesFolder = localFilesFolder;
	}

	public void reply(ThreadBlock tB){
		log.debug("Replying to thread block '" + tB.getSubject() + "'");

		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		String javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
									"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
									"if (title=='" + tB.getSubject() + "'){blocks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No thread block '" + tB.getSubject() + "' found");

		javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
		"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
		"if (title=='" + tB.getSubject() + "'){window.dojo.query('ul.menu.left a[href*=DiscussionAdd]', blocks[i])[0];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No Reply link found for block '" + tB.getSubject() + "'");

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void close(ThreadBlock tB){
		log.debug("Closing the issue for thread block '" + tB.getSubject() + "'");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
									"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
									"if (title=='" + tB.getSubject() + "'){blocks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No thread block '" + tB.getSubject() + "' found");

		javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
							"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
							"if (title=='" + tB.getSubject() + "'){window.dojo.query('ul.menu.left a[href*=DiscussionIssueView]', blocks[i])[0];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No Close link found for block '" + tB.getSubject() + "'");

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		log.debug("Thread block '" + tB.getSubject() + "' issue was closed sucessfully");
	}

	public void escalate(ThreadBlock tB){
		log.debug("Escalating the thread block '" + tB.getSubject() + "'");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
									"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
									"if (title=='" + tB.getSubject() + "'){blocks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No thread block '" + tB.getSubject() + "' found");

		javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
							"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
							"if (title=='" + tB.getSubject() + "'){window.dojo.query('ul.menu.left a[href*=IssueAdd]', blocks[i])[0];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No Escalate link found for block '" + tB.getSubject() + "'");

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void deEscalate(ThreadBlock tB) {
		log.debug("De-escalating the thread block '" + tB.getSubject() + "'");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		String javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
									"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
									"if (title=='" + tB.getSubject() + "'){blocks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No thread block '" + tB.getSubject() + "' found");

		javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
							"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
							"if (title=='" + tB.getSubject() + "'){window.dojo.query('ul.menu.left a[href*=#]', blocks[i])[0];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No Escalate link found for block '" + tB.getSubject() + "'");
		driver.click("dom=" + javascriptQuery);

		javascriptQuery = 	"var blocks = window.dojo.query('div#content div.block');" +
							"for (var i=0;i<blocks.length;i++){var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
							"if (title=='" + tB.getSubject() +
							"'){window.dojo.query('ul.menu.left a[href*=#]', blocks[i])[0].id.match(/(\\w*)Show/)[1];break;}}";
		String popupId = driver.getEval(javascriptQuery);

		Boolean isNotReady = true;
		long timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = !(driver.isVisible("dom=window.dojo.byId('" + popupId + "')"));
			try {
			if (isNotReady) Thread.sleep(2000);
			} catch (InterruptedException ie){
				throw new IllegalStateException(ie);
			}

		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		driver.click("dom=window.dojo.query('div#" + popupId + " input[onclick*=confirmDeEscalateDialog]')[0]");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		log.debug("Thread block '" + tB.getSubject() + "' was de-escalated sucessfully");
	}


	public void testAttachments(ThreadBlock tB) {
		log.debug("Testing the attachments for thread block '" + tB.getSubject() + "'");

		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		Iterator<FileAttachment> tBFAI = tB.getFileAttachmentsList().iterator();

	    while (tBFAI.hasNext()){
	    	FileAttachment fA = tBFAI.next();
	    	log.debug("Testing the attachment '" + fA.getTitle() + "'");

	    	String javascriptQuery =	"var blocks = window.dojo.query('div#content div.block');" +
										"for (var i=0;i<blocks.length;i++){" +
										"var title=window.dojo.query('div.box > span', blocks[i])[0].innerHTML;" +
										"if (title=='" + tB.getSubject() + "'){" +
										"var links=window.dojo.query('a[id*=uid]', blocks[i]);" +
										"for (var j=0; j<links.length; j++){" +
										"if (/\\W*" + fA.getTitle() + "\\W*/.test(links[j].innerHTML))" +
										"{links[j].id;break;}}break;}}";

	    	String specialId = driver.getEval(javascriptQuery);

	    	javascriptQuery ="dom=window.dojo.byId('" + specialId + "')";
	    	//driver.click(javascriptQuery);
	    	driver.mouseUp(javascriptQuery);

	    	javascriptQuery = 	"var menuItems = window.dojo.query('#" + specialId + "_pspop tr.dijitMenuItem');" +
	    						"for (var i=0; i<menuItems.length; i++){" +
	    						"var itemLink=window.dijit.byId(menuItems[i].id).href;" +
	    						"if (/.*GetLinkServlet.*/.test(itemLink)) {itemLink;break;}}";

	    	String attHref = driver.getEval(javascriptQuery);

	    	AttachmentComparator ac = new AttachmentComparator();
			if (!(ac.compareURLWithLocalFile(driver.getCookie(), attHref, localFilesFolder + fA.getPath())))
				throw new IllegalStateException("Original content and the local copy differ for '" + fA.getTitle() + "'");
	    }
	}


}
