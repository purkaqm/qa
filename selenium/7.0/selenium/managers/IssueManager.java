package selenium.managers;

import java.util.Iterator;

import org.apache.log4j.Logger;
import selenium.driver.MySeleniumDriver;
import selenium.objects.*;

public class IssueManager {

	private MySeleniumDriver driver;
	private Logger logger;
	private String serverFilesFolder;

	public IssueManager(MySeleniumDriver driver, Logger logger, String serverFilesFolder){
		this.driver = driver;
		this.logger = logger;
		this.serverFilesFolder = serverFilesFolder;
	}

	public void setNewSubjectNoSubmit(){
		//TODO all
	}

	public void addIssueToBlock(ThreadBlock tB){
		logger.info("Adding issue to thread block '" + tB.getSubject() + "'");

		int priorityIndex = tB.getIssue().getPriority() - 1;
		driver.select("dom=window.dojo.byId('prioritySelect')", "value=" + priorityIndex);
		//TODO add recommended actions handling
		driver.click("dom=window.dojo.byId('Submit_3')");
		driver.waitForPageToLoad("30000");

		logger.info("Thread block '" + tB.getSubject() + "' now has an issue");
	}

	public void createNewBlockWithIssue(ThreadBlock tB) throws InterruptedException{
		logger.info("Creating a new thread block '" + tB.getSubject() + "' with issue");

		driver.type("dom=window.dojo.byId('subjectInput')", tB.getSubject());

		//wait up to 1 minute for the tinymce activation
		Boolean isNotReady = true;
		long timeToStop = System.currentTimeMillis() + 60000;
		do {
			isNotReady = driver.getEval("window.tinyMCE.getInstanceById('mce_editor_0') == undefined").equals("true");
			isNotReady = isNotReady | driver.getEval("window.tinyMCE.getInstanceById('mce_editor_0') == undefined").equals("true");
			if (isNotReady) Thread.sleep(2000);
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		driver.runScript("window.tinyMCE.getInstanceById('mce_editor_0').setHTML('" + tB.getMessage() + "')");

		int priorityIndex = tB.getIssue().getPriority() - 1;
		driver.select("dom=window.dojo.byId('prioritySelect')", "value=" + priorityIndex);

	    Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
	    while (fAI.hasNext()) this.addAttachment(fAI.next());

		//TODO add recommended actions handling
		driver.click("dom=window.dojo.byId('Submit_3')");
		driver.waitForPageToLoad("30000");

		logger.info("New thread block '" + tB.getSubject() + "' with issue was created successfully");
	}

	private void addAttachment(FileAttachment fA) throws InterruptedException{
		logger.info("Adding the attachment '" + fA.getTitle() + "'");

		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG_SHOW')");

		//wait up to 1 minute for the visible pop-up
		Boolean isNotReady = true;
		long timeToStop = System.currentTimeMillis() + 60000;
		do {
			isNotReady = !(driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')"));
			if (isNotReady) Thread.sleep(2000);
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + 60000;
		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			if (isNotReady) Thread.sleep(2000);
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
		driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_FILE')", serverFilesFolder + fA.getPath());
		driver.type("dom=window.dojo.byId('addDocuments_ADD_DOC_FORM_TITLE')", fA.getTitle());
		driver.click("dom=window.dojo.byId('addDocuments_ADD_DOCS_ADD_DOC')");

		//wait up to 1 minute for loading.gif on the pop-up to go away
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + 60000;
		do {
			isNotReady = driver.getEval("window.dojo.byId('addDocuments_ADD_DOCS_DLG').innerHTML").contains("loading.gif");
			if (isNotReady) Thread.sleep(2000);
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));

		driver.click("dom=window.dojo.query('input[dojoattachpoint=doneBtn]', window.dojo.query('div#addDocuments_ADD_DOCS_DLG')[0])[0]");

		//wait up to 1 minute for the invisible pop-up
		isNotReady = true;
		timeToStop = System.currentTimeMillis() + 60000;
		do {
			isNotReady = driver.isVisible("dom=window.dojo.byId('addDocuments_ADD_DOCS_DLG')");
			if (isNotReady) Thread.sleep(2000);
		} while ((isNotReady) & (System.currentTimeMillis() < timeToStop));
	}

}
