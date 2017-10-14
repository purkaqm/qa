package selenium.managers;

import org.apache.log4j.Logger;
import selenium.driver.*;
import selenium.objects.*;

public class WorkTreeManager{

	private MySeleniumDriver driver;
	private Logger logger;

	public WorkTreeManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void navigateToProject(IProject p) throws InterruptedException{
		logger.info("Navigating to '" + p.getName() + "'");

		driver.click("dom=window.dojo.query('div#BrowseMenu a[href*=/WorkTree]')[0]");
		driver.waitForPageToLoad("30000");

		Thread.sleep(5000);

		Boolean isTreeNotReady = true;
		Long timeToStop = System.currentTimeMillis() + 60000;

		do {
			if (driver.isElementPresent("dom=window.dojo.query('#ps_widget_psTree_0 > .dijitTreeNode')[0]"))
				isTreeNotReady = driver.isVisible("dom=window.dojo.query('#ps_widget_psTree_0 > .dijitTreeNode')[0]");
			if (isTreeNotReady) Thread.sleep(2000);
		} while ((timeToStop < System.currentTimeMillis()) & (isTreeNotReady));

		String javascriptQuery =	"var nodes = window.dojo.query('#ps_widget_psTree_0 > .dijitTreeContainer > .dijitTreeNode');" +
									"for (var i=0;i<nodes.length;i++){" +
									"var nodeLink = window.dojo.query('#' + nodes[i].id + ' > .dijitTreeContent > span.dijitTreeLabel > a')[0];" +
									"if (nodeLink.innerHTML == '" + p.getName() + "'){nodeLink;break;}}";

		if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("No link for '" + p.getName() + "' found in Work Tree");

		driver.click("dom=" + javascriptQuery);
		driver.waitForPageToLoad("30000");
		//TODO Add support for nodes with parents
	}

}
