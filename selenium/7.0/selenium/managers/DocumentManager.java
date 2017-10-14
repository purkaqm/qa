package selenium.managers;

import java.io.IOException;
import java.net.URLDecoder;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.*;
import test.service.*;

public class DocumentManager {
	private MySeleniumDriver driver;
	private Logger logger;
	private String localFilesFolder;
	private String serverFilesFolder;

	public DocumentManager(MySeleniumDriver driver, Logger logger, String localFilesFolder, String serverFilesFolder){
		this.driver = driver;
		this.logger = logger;
		this.localFilesFolder = localFilesFolder;
		this.serverFilesFolder = serverFilesFolder;
	}

	public void update(FileAttachment fA){
		driver.type("dom=window.dojo.byId('upload')", serverFilesFolder + fA.getPath());
		driver.click("dom=window.dojo.byId('LinkSubmit_0')");
		driver.waitForPageToLoad("30000");

		logger.info("Attachment was updated sucessfully");
	}

	public void checkVersion(int versionNumber, FileAttachment fA) throws IOException{
		logger.info("Testing the #" + versionNumber + " attachment version");

		String javascriptQuery = 	"var verLinks=window.dojo.query('.versionColumnValue > a');" +
									"for (var i=0; i< verLinks.length; i++){if (verLinks[i].innerHTML=='#" + versionNumber + "'){verLinks[i].href;break;}}";

		String verHref = URLDecoder.decode(driver.getEval(javascriptQuery).split("'")[1], "UTF-8");

		AttachmentComparator ac = new AttachmentComparator();
		if (!(ac.compareURLWithLocalFile(driver.getCookie(), verHref, localFilesFolder + fA.getPath())))
			throw new IllegalStateException("Version #" + versionNumber + " content and the local copy " + fA.getPath() + " do not match");

		logger.info("#" + versionNumber + " matches the given file");
	}
}
