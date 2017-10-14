package com.powersteeringsoftware.core.managers.thread;

import java.util.Iterator;
import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.ThreadBlock;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.thoughtworks.selenium.DefaultSelenium;

/**
 * Class works only for 7.1
 *
 * @author selyaev_ag
 *
 */
public class ThreadBlockManager {

	private Logger logger = Logger.getLogger(ThreadBlockManager.class);
	private String serverFilesFolder;
	private ThreadBlock tB;

	public ThreadBlockManager(String serverFilesFolder){
		this.serverFilesFolder = serverFilesFolder;
	}

	public void createNewBlock(ThreadBlock _tB){
		tB=_tB;
		typeName();
		typeMessage();
		addAttachments();
	    pushButtonUpperSubmit();
	}

	private void addAttachments() {
		Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
	    while (fAI.hasNext()) this.addAttachment(fAI.next());
	}

	private void typeMessage() {
		SeleniumDriverSingleton.getDriver().runScript("window.tinyMCE.setContent('" + tB.getMessage() + "');");
		//driver.runScript("dijit.byId('messageInput_editor').setValue('"+tB.getMessage()+"')");
	}

	private void typeName() {
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.byId('subjectInput')", tB.getSubject()); //type name
	}

	private void pushButtonUpperSubmit() {
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('[value=Submit]>>[type=submit]')[0]");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void addAttachment(FileAttachment fA){
		logger.debug("Adding attachment " + fA.getTitle());
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG_SHOW')");

		//wait up to 1 minute for the visible pop-up
		Boolean isNotReady = true;
		long timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();

//		do {
//			isNotReady = !(driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')"));
//			if (isNotReady) Thread.sleep(2000);
//		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		ElementBecomeVisibleWaiter.waitElementBecomeVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')");

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();

		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			try{
				if (isNotReady)
					Thread.sleep(2000);
			} catch(InterruptedException ie){
				throw new IllegalStateException(ie);
			}

		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
		//driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());

		// works only in FF not in ie!!
		driver.type("addDocuments_ADD_DOC_FORM_FILE", serverFilesFolder + fA.getPath());
		//driver.attachFile("addDocuments_ADD_DOC_FORM_FILE", "file:///"+serverFilesFolder + fA.getPath());
		driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_TITLE')", fA.getTitle());
		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_ADD_DOC')");

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();

		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			try{
				if (isNotReady)
					Thread.sleep(2000);
			} catch(InterruptedException ie){
				throw new IllegalStateException(ie);
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addDocuments_ADD_DOCS_DLG')[0])[0]");

		//wait up to 1 minute for the invisible pop-up
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')");
			try{
				if (isNotReady)
					Thread.sleep(2000);
			} catch(InterruptedException ie){
				throw new IllegalStateException(ie);
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));
	}

}
