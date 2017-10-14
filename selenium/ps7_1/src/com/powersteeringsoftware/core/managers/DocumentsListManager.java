package com.powersteeringsoftware.core.managers;

import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class DocumentsListManager {

	private String serverFilesFolder;

	public DocumentsListManager(String serverFilesFolder){
		this.serverFilesFolder = serverFilesFolder;
	}

	public void addDocument(FileAttachment fA) {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
									"for (var i=0;i<tbLinks.length;i++){if (/DocumentListing/.test(tbLinks[i].href)) {tbLinks[i];break;}}";
		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Issues page link not found");
		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		this.addAttachment(fA);
	}

	private void addAttachment(FileAttachment fA) {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		if (driver.isElementPresent("dom=document.getElementById('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')")){
			driver.click("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG_SHOW')");

			//wait up to 1 minute for the visible pop-up
			Boolean isNotReady = true;
			long timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = !(driver.isVisible("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG')"));
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
			driver.type("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
			driver.type("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOC_FORM_TITLE')", fA.getTitle());
			driver.click("dom=window.dojo.byId('addNewDocumentsButton_ADD_DOCS_ADD_DOC')");

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsButton_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addNewDocumentsButton_ADD_DOCS_DLG')[0])[0]");
			driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		}else{
			driver.click("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG_SHOW')");

			//wait up to 1 minute for the visible pop-up
			Boolean isNotReady = true;
			long timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = !(driver.isVisible("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG')"));
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
			driver.type("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
			driver.type("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOC_FORM_TITLE')", fA.getTitle());
			driver.click("dom=window.dojo.byId('addNewDocumentsLink_ADD_DOCS_ADD_DOC')");

			//wait up to 1 minute for loading.gif on the pop-up to go away
			isNotReady = true;
			timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
			do {
				isNotReady = driver.getEval("window.dojo.byId('addNewDocumentsLink_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
				try{
					if (isNotReady) Thread.sleep(2000);
				} catch (InterruptedException ie){

				}
			} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

			driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addNewDocumentsLink_ADD_DOCS_DLG')[0])[0]");
			driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		}

	}

	public void seeDetails(FileAttachment fA){
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

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
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	public void update(FileAttachment fA) {
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
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
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

}
