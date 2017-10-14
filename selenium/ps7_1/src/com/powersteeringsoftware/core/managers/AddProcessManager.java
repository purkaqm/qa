package com.powersteeringsoftware.core.managers;

import java.util.Iterator;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.objects.PSProcess;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.thoughtworks.selenium.DefaultSelenium;

public class AddProcessManager{

	private PSProcess process;

	public AddProcessManager(){
	}

	public AddProcessManager(PSProcess _process){
		process = _process;
	}

	public void manage(PSProcess process){
		//logger.info("Creating new process '" + process.getName() + "'");

//		driver.click();
//		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
		MainMenuAdapter.clickAdminProcesses();

		selectChildFrame();

		DefaultSelenium driver = SeleniumDriverSingleton.getDriver();
		
		String jsQuery = 	"var links=document.getElementsByTagName('a');" +
					"for (var i=0; i<links.length; i++){if (/.*phase_tag_add.jsp/.test(links[i].href)){links[i];break}}";
		driver.click("dom=" + jsQuery);
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

		if ((process.getPhasesList().size() < 3) && (process.getPhasesList().size() > 13))
			throw new IllegalArgumentException("Less than 3 or more than 13 phases are not supported yet");
		driver.select("dom=document.getElementById('windowedControl')", "value=" + process.getPhasesList().size());
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());

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
		driver.waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());


		selectParentFrame();
	}

	/**
	 * Go back to the main frame
	 */
	private void selectParentFrame() {
		SeleniumDriverSingleton.getDriver().selectFrame("relative=parent");
	}

	/**
	 * Work in the frame on jsp page
	 */
	private void selectChildFrame() {
		SeleniumDriverSingleton.getDriver().selectFrame("dom=window.frames['jspFrame']");
	}

	public PSProcess getProcess() {
		return process;
	}

	public void setProcess(PSProcess process) {
		this.process = process;
	}

}
