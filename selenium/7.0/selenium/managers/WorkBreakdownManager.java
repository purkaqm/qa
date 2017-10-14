package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.IProject;

public class WorkBreakdownManager {


	private MySeleniumDriver driver;
	private Logger logger;

	public WorkBreakdownManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void addChildren(IProject project, IProject[] children) throws InterruptedException{
		this.navigate();

		logger.info("Adding children to '" + project.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + project.getName() + "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isElementPresent("dom=" + jsQuery);
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));


		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + project.getName() + "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		driver.click("dom=" + jsQuery);




//		timeToStop = System.currentTimeMillis() + 30000;
//		do {
//			isReady = driver.isElementPresent("dom=window.dojo.byId('table0')");
//			if (!(isReady)) Thread.sleep(200);
//		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
//		driver.click("dom=window.dojo.query('#table0 span')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isVisible("dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.getEval("dom=window.dojo.query('div#dlgRowMenu form').length").equals("0")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		driver.click("dom=window.dojo.query('#dlgRowMenuAddM a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isVisible("dom=window.dojo.byId('dlgAddMult')");
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isElementPresent("dom=window.dojo.byId('addMultForm')");
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		for (int i=0; (i < children.length) & (i < 10); i++){
			driver.type("dom=document.getElementById('addMultName" + i + "')", children[i].getName());
			//TODO add type selection
		}
		driver.click("dom=window.dojo.byId('Button_8')");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	public void indent(IProject project) throws InterruptedException{
		this.navigate();

		logger.info("Indenting '" + project.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + project.getName() + "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isElementPresent("dom=" + jsQuery);
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + project.getName() + "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		driver.click("dom=" + jsQuery);

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isVisible("dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.getEval("dom=window.dojo.query('div#dlgRowMenu form').length").equals("0")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		driver.click("dom=window.dojo.query('#dlgRowMenuInd a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	public void setPredecessor(IProject p1, IProject p2) throws InterruptedException{
		this.navigate();

		logger.info("Setting the predecessor for '" + p1.getName() + "' as '" + p2.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + p1.getName() + "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isElementPresent("dom=" + jsQuery);
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));


		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + p2.getName() + "/.test(spans[i].lastChild.data)) " +
					"spans[i].parentNode.parentNode.firstChild.firstChild.data;}";
		String predIndex = driver.getEval(jsQuery);

		jsQuery = 	"var spans=window.dojo.query('#tablebody0 span');" +
					"for (var i=0; i<spans.length; i++)" +
					"{if (/\\s*" + p1.getName() + "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		driver.click("dom=" + jsQuery);

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = driver.isVisible("dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.getEval("dom=window.dojo.query('div#dlgRowMenu form').length").equals("0")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		driver.click("dom=window.dojo.query('div#dlgRowMenu #dlgRowMenuPred a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.getEval("dom=window.dojo.query('div#dlgPred form').length").equals("0")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		driver.type("dom=window.dojo.byId('dlgPredIdx0')", predIndex);
		driver.click("dom=window.dojo.byId('Button_4')");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(driver.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady)) Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	private void navigate(){
		String pageTitle = driver.getTitle();
		String pageName = pageTitle.split("\\|")[0];
		pageName = pageName.trim();
		if (!(pageName.equals("Work Breakdown"))) {
			logger.info("Navigating to the Work Breakdown page");

			String javascriptQuery = 	"var tbLinks=window.dojo.query('div#sub ul.toolbar a');" +
										"for (var i=0;i<tbLinks.length;i++){if (/WBS/.test(tbLinks[i].href)) {tbLinks[i];break;}}";

			if (!(driver.isElementPresent("dom=" + javascriptQuery))) throw new IllegalStateException("Work Breakdown page link not found");
			driver.click("dom=" + javascriptQuery);
			driver.waitForPageToLoad("30000");
		}

	}

}
