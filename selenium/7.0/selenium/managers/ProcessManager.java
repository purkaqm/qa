package selenium.managers;

import java.util.Iterator;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;
import selenium.objects.*;

public class ProcessManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public ProcessManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void addProcess(PSProcess process){
		logger.info("Creating new process '" + process.getName() + "'");

		driver.click("dom=window.dojo.query('div#AdminMenu a[href*=Processes]')[0]");
		driver.waitForPageToLoad("30000");

		//Work in the frame on jsp page
		driver.selectFrame("dom=window.frames['jspFrame']");

		String jsQuery;

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*phase_tag_add.jsp/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		if ((process.getPhasesList().size() < 3) && (process.getPhasesList().size() > 13))
			throw new IllegalArgumentException("Less than 3 or more than 13 phases are not supported yet");
		driver.select("dom=document.getElementById('windowedControl')", "value=" + process.getPhasesList().size());
		driver.waitForPageToLoad("30000");

		driver.type("dom=document.getElementsByName('tagname')[0]", process.getName());
		driver.type("dom=document.getElementsByName('tagdesc')[0]", process.getDescription());

		int i = 0;
		for (Iterator<PSProcess.Phase> it = process.getPhasesList().iterator(); it.hasNext();) {
			PSProcess.Phase phase = it.next();i++;
			driver.type("dom=document.getElementsByName('textfield" + i + "')[0]", phase.getName());
			if (null != phase.getPercent())
				driver.type("dom=document.getElementsByName('pcntcompleted" + i + "')[0]", phase.getPercent());
		}

		jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++)" +
					"{if (/javascript:doSubmit()/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad("30000");

		//Go back to the main frame
		driver.selectFrame("relative=parent");
	}

}
