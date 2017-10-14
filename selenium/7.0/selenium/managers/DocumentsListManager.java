package selenium.managers;

import org.apache.log4j.Logger;
import selenium.driver.MySeleniumDriver;
import selenium.objects.*;

public class DocumentsListManager {

	private MySeleniumDriver driver;
	private Logger logger;
	private String serverFilesFolder;

	public DocumentsListManager(MySeleniumDriver driver, Logger logger, String serverFilesFolder){
		this.driver = driver;
		this.logger = logger;
		this.serverFilesFolder = serverFilesFolder;
	}

	public void addDocument(FileAttachment fA) throws InterruptedException{
		logger.info("Adding the new attachment '" + fA.getTitle() + "'");

		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/DocumentListing/.test(tbLinks[i].href)) {tbLinks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Issues page link not found");
		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");

		this.addAttachment(fA);
	}

	private void addAttachment(FileAttachment fA) throws InterruptedException{

		if (driver.isElementPresent("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')")){
			driver.click("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')");

			//wait up to 1 minute for the visible pop-up
			Boolean isNotReady = true;
			long timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = !(driver.isVisible("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG')"));
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
			driver.type("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
			driver.type("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOC_FORM_TITLE')", fA.getTitle());
			driver.click("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_ADD_DOC')");

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addNewDocumentsButton_ADD_DOCS_DLG')[0])[0]");
			driver.waitForPageToLoad("30000");
		}else{
			driver.click("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG_SHOW')");

			//wait up to 1 minute for the visible pop-up
			Boolean isNotReady = true;
			long timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = !(driver.isVisible("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG')"));
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
			driver.type("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
			driver.type("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOC_FORM_TITLE')", fA.getTitle());
			driver.click("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_ADD_DOC')");

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + 60000;
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				if (isNotReady) Thread.sleep(2000);
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addNewDocumentsLink_ADD_DOCS_DLG')[0])[0]");
			driver.waitForPageToLoad("30000");
		}

		logger.info("Attachment '" + fA.getTitle() + "' was added sucessfully");
	}

	public void seeDetails(FileAttachment fA){
		logger.info("Navigating to details page for attachment " + fA.getTitle());

		String javascriptQuery = 	"var links = window.dojo.query('div#content td.titleColumnValue a');" +
									"for (var i=0; i<links.length; i++){" +
									"if (links[i].innerHTML.match(/\\W*(.*)\\W*/)[1]=='" + fA.getTitle() + "'){ links[i].id;break;}}";

		String specialId = driver.getEval(javascriptQuery);
		driver.click("dom=window.dojo.byId('" + specialId + "')");

		javascriptQuery = 	"var menuItems = window.dojo.query('#" + specialId + "_pspop tr.dijitMenuItem');" +
							"for (var i=0; i<menuItems.length; i++){" +
							"var itemLink=window.dijit.byId(menuItems[i].id).href;" +
							"if (/DirectLink_1\\.sdirect/.test(itemLink)) menuItems[i];}";

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");
	}

	public void update(FileAttachment fA) {
		logger.info("Starting update for attachment " + fA.getTitle());

		String javascriptQuery = 	"var links = window.dojo.query('div#content td.titleColumnValue a');" +
									"for (var i=0; i<links.length; i++){" +
									"if (links[i].innerHTML.match(/\\W*(.*)\\W*/)[1]=='" + fA.getTitle() + "'){ links[i].id;break;}}";

		String specialId = driver.getEval(javascriptQuery);
		driver.mouseUp("dom=window.dojo.byId('" + specialId + "')");

		javascriptQuery = 	"var menuItems = window.dojo.query('#" + specialId + "_pspop tr.dijitMenuItem');" +
							"for (var i=0; i<menuItems.length; i++){" +
							"var itemLink=window.dijit.byId(menuItems[i].id).href;" +
							"if (/DirectLink\\.sdirect/.test(itemLink)) menuItems[i];}";

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");
	}

}
