package com.powersteeringsoftware.core.managers;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.log4j.Logger;
import org.testng.Assert;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.FileAttachment.AttachmentComparator;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.thoughtworks.selenium.DefaultSelenium;


public class DocumentManager {
	private Logger logger = Logger.getLogger(DocumentManager.class);
	private String localFilesFolder;
	private String serverFilesFolder;

	public DocumentManager(String _localFilesFolder, String _serverFilesFolder){
		localFilesFolder = _localFilesFolder;
		serverFilesFolder = _serverFilesFolder;
	}

	public void update(FileAttachment fA){
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		driver.type("dom=window.dojo.byId('upload')", serverFilesFolder + fA.getPath());
		driver.click("dom=window.dojo.byId('LinkSubmit_0')");
		driver.waitForPageToLoad("30000");

		logger.info("Attachment was updated sucessfully");
	}

	public void checkVersion(int versionNumber, FileAttachment fA){
		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();

		logger.info("Testing the #" + versionNumber + " attachment version");

		String javascriptQuery = 	"var verLinks=window.dojo.query('.versionColumnValue > a');" +
									"for (var i=0; i< verLinks.length; i++){if (verLinks[i].innerHTML=='#" + versionNumber + "'){verLinks[i].href;break;}}";
		String verHref = "";
		try{
			verHref = URLDecoder.decode(driver.getEval(javascriptQuery).split("'")[1], "UTF-8");
		}catch (UnsupportedEncodingException uee){
			Assert.fail("Exception while check version", uee);
		}

		AttachmentComparator ac = new AttachmentComparator();
		if (!(ac.compareURLWithLocalFile(driver.getCookie(), verHref, localFilesFolder + fA.getPath())))
			throw new IllegalStateException("Version #" + versionNumber + " content and the local copy " + fA.getPath() + " do not match");

		logger.info("#" + versionNumber + " matches the given file");
	}
}
