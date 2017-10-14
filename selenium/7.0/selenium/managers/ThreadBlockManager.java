package selenium.managers;

import java.util.Iterator;
import org.apache.log4j.Logger;
import selenium.driver.MySeleniumDriver;
import selenium.objects.*;

public class ThreadBlockManager {

	private MySeleniumDriver driver;
	private Logger logger;
	private String serverFilesFolder;

	public ThreadBlockManager(MySeleniumDriver driver, Logger logger, String serverFilesFolder){
		this.driver = driver;
		this.logger = logger;
		this.serverFilesFolder = serverFilesFolder;
	}

	public void createNewBlock(ThreadBlock tB) throws InterruptedException{
		logger.info("Creating new thread block '" + tB.getSubject() + "'");

		driver.type("dom=window.dojo.byId('subjectInput')", tB.getSubject());
		Thread.sleep(10000);
		driver.runScript("window.tinyMCE.setContent('" + tB.getMessage() + "');");

	    Iterator<FileAttachment> fAI = tB.getFileAttachmentsList().iterator();
	    while (fAI.hasNext()) this.addAttachment(fAI.next());

	    driver.click("dom=window.dojo.byId('Submit_3')");
	    driver.waitForPageToLoad("30000");

		logger.info("Thread block '" + tB.getSubject() + "' was created sucessfully");
	}


	public void updateThreadBlock(){
		//TODO code this method
	}

	private void addAttachment(FileAttachment fA) throws InterruptedException{
		logger.info("Adding attachment " + fA.getTitle());

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
