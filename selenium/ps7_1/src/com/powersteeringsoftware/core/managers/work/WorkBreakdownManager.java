package com.powersteeringsoftware.core.managers.work;

import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;

public class WorkBreakdownManager {

	private Logger logger = Logger.getLogger(WorkBreakdownManager.class);

	public WorkBreakdownManager() {
	}

	public void addChildren(WorkItem project, WorkItem[] children)
			throws InterruptedException {
		this.navigate();

		logger.debug("Adding children to '" + project.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ project.getName()
				+ "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isElementPresent(
					"dom=" + jsQuery);
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ project.getName()
				+ "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);

		// timeToStop = System.currentTimeMillis() + 30000;
		// do {
		// isReady = driver.isElementPresent("dom=window.dojo.byId('table0')");
		// if (!(isReady)) Thread.sleep(200);
		// } while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		// driver.click("dom=window.dojo.query('#table0 span')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isVisible(
					"dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver().getEval(
					"dom=window.dojo.query('div#dlgRowMenu form').length")
					.equals("0")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('#dlgRowMenuAddM a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isVisible("dom=window.dojo.byId('dlgAddMult')");
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver()
					.isElementPresent("dom=window.dojo.byId('addMultForm')");
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		for (int i = 0; (i < children.length) & (i < 10); i++) {
			SeleniumDriverSingleton.getDriver().type("dom=document.getElementById('addMultName" + i + "')",
					children[i].getName());
			// TODO add type selection
		}
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('Button_8')");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	public void indent(WorkItem project) throws InterruptedException {
		this.navigate();

		logger.debug("Indenting '" + project.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ project.getName()
				+ "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isElementPresent("dom=" + jsQuery);
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ project.getName()
				+ "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isVisible("dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.getEval("dom=window.dojo.query('div#dlgRowMenu form').length")
					.equals("0")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('#dlgRowMenuInd a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	public void setPredecessor(WorkItem p1, WorkItem p2)
			throws InterruptedException {
		this.navigate();

		logger.debug("Setting the predecessor for '" + p1.getName() + "' as '"
				+ p2.getName() + "'");

		Boolean isReady;
		long timeToStop;
		String jsQuery;

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ p1.getName() + "/.test(spans[i].lastChild.data)) spans[i];}";
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isElementPresent("dom=" + jsQuery);
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ p2.getName() + "/.test(spans[i].lastChild.data)) "
				+ "spans[i].parentNode.parentNode.firstChild.firstChild.data;}";
		String predIndex = SeleniumDriverSingleton.getDriver().getEval(jsQuery);

		jsQuery = "var spans=window.dojo.query('#tablebody0 span');"
				+ "for (var i=0; i<spans.length; i++)" + "{if (/\\s*"
				+ p1.getName()
				+ "/.test(spans[i].lastChild.data)) spans[i].parentNode;}";
		SeleniumDriverSingleton.getDriver().click("dom=" + jsQuery);

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = SeleniumDriverSingleton.getDriver().isVisible("dom=window.dojo.byId('dlgRowMenu')");
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.getEval("dom=window.dojo.query('div#dlgRowMenu form').length")
					.equals("0")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		SeleniumDriverSingleton.getDriver()
				.click("dom=window.dojo.query('div#dlgRowMenu #dlgRowMenuPred a')[0]");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.getEval("dom=window.dojo.query('div#dlgPred form').length")
					.equals("0")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.byId('dlgPredIdx0')", predIndex);
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('Button_4')");

		timeToStop = System.currentTimeMillis() + 30000;
		do {
			isReady = (!(SeleniumDriverSingleton.getDriver()
					.isVisible("dom=window.dojo.byId('dlgProgress')")));
			if (!(isReady))
				Thread.sleep(200);
		} while ((!(isReady)) & (System.currentTimeMillis() < timeToStop));

	}

	private void navigate() {
		String pageTitle = SeleniumDriverSingleton.getDriver().getTitle();
		String pageName = pageTitle.split("\\|")[0];
		pageName = pageName.trim();
		if (!(pageName.equals("Work Breakdown"))) {
			logger.debug("Navigating to the Work Breakdown page");

			String javascriptQuery = "var tbLinks=window.dojo.query('div#sub ul.toolbar a');"
					+ "for (var i=0;i<tbLinks.length;i++){if (/WBS/.test(tbLinks[i].href)) {tbLinks[i];break;}}";

			if (!(SeleniumDriverSingleton.getDriver().isElementPresent("dom=" + javascriptQuery)))
				throw new IllegalStateException(
						"Work Breakdown page link not found");
			SeleniumDriverSingleton.getDriver().click("dom=" + javascriptQuery);
			SeleniumDriverSingleton.getDriver().waitForPageToLoad("30000");
		}

	}

}
