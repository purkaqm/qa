package com.powersteeringsoftware.core.managers.issues;

import java.util.Iterator;
import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.adapters.issues.IssueEditPageAdapter;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.ThreadBlock;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class IssueManager {

	private Logger log = Logger.getLogger(IssueManager.class);
	private String serverFilesFolder;

	public IssueManager(String serverFilesFolder){
		this.serverFilesFolder = serverFilesFolder;
	}

	public void setNewSubjectNoSubmit(){
		throw new RuntimeException("still on construction");
	}

	public void addIssueToBlock(ThreadBlock tB){
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		log.debug("Adding issue to thread block '" + tB.getSubject() + "'");

		int priorityIndex = tB.getIssue().getPriority() - 1;
		driver.select("dom=window.dojo.byId('prioritySelect')", "value=" + priorityIndex);
		//TODO add recommended actions handling
		driver.click("dom=window.dojo.byId('Submit_3')");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		log.debug("Thread block '" + tB.getSubject() + "' now has an issue");
	}

	public void createNewBlockWithIssue(ThreadBlock tB){
		log.debug("Creating a new thread block '" + tB.getSubject() + "' with issue");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		//driver.type("dom=window.dojo.byId('subjectInput')", tB.getSubject());
		IssueEditPageAdapter.typeName(tB.getSubject());
		//driver.runScript("dijit.byId('messageInput_editor').setValue('"+tB.getMessage()+"')");
		IssueEditPageAdapter.typeDescription(tB.getMessage());


		int priorityIndex = tB.getIssue().getPriority() - 1;
		driver.select("dom=window.dojo.byId('prioritySelect')", "value=" + priorityIndex);

	    Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
	    while (fAI.hasNext()) this.addAttachment(fAI.next());

		//TODO add recommended actions handling
		driver.click("dom=window.dojo.byId('Submit_3')");
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		log.debug("New thread block '" + tB.getSubject() + "' with issue was created successfully");
	}

	private void addAttachment(FileAttachment fA){
		log.debug("Adding the attachment '" + fA.getTitle() + "'");
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG_SHOW')");

		//wait up to 1 minute for the visible pop-up
		Boolean isNotReady = true;
		long timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = !(driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')"));
			try{
				if (isNotReady) Thread.sleep(2000);
			} catch (InterruptedException ie){
				log.warn("");
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			try{
				if (isNotReady) Thread.sleep(2000);
			} catch (InterruptedException ie){
				log.warn("");
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
		driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
		driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_TITLE')", fA.getTitle());
		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_ADD_DOC')");

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			try{
				if (isNotReady) Thread.sleep(2000);
			} catch (InterruptedException ie){
				log.warn("");
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addDocuments_ADD_DOCS_DLG')[0])[0]");

		//wait up to 1 minute for the invisible pop-up
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad();
		do {
			isNotReady = driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')");
			try{
				if (isNotReady) Thread.sleep(2000);
			} catch (InterruptedException ie){
				log.warn("");
			}
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));
	}

}
