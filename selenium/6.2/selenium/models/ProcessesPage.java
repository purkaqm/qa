package selenium.models;

import java.util.*;
import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;
import test.service.Config;

public class ProcessesPage{

	private MySeleniumDriver mydriver;
	private String contextPath;

	public ProcessesPage(BrowserModel browserModel) throws Exception {
		this.contextPath = Config.getInstance().getContextPath();
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisProcessesPage() {
		return mydriver.getTitle().contains("Processes");
	}

	public boolean isJSPFrameWorkingOk() {
		return mydriver.isElementPresent("//body//iframe//form");
	}

	public void addProcess(ProcessHolder processBean) {
		mydriver.open(contextPath + "/admin/phase_tag_add.jsp?event=changePhaseCount&phaseCount=" + String.valueOf(processBean.getList().size()));
		mydriver.waitForPageToLoad("30000");
		mydriver.type("//input[@name='tagname']", processBean.getProcessName());
		mydriver.type("//textarea[@name='tagdesc']", processBean.getProcessDescription());
		Integer i = new Integer(0);
		for (Iterator<ProcessPhase> p = processBean.getList().iterator(); p.hasNext();) {
			ProcessPhase processPhase = p.next();i++;
			mydriver.type("//input[@name='textfield" + String.valueOf(i) + "']", processPhase.getPhaseName());
			if (null != processPhase.getPhasePercent())
				mydriver.type("//input[@name='pcntcompleted" + String.valueOf(i) + "']", processPhase.getPhasePercent());
		}
		mydriver.click("//a[@href='javascript:doSubmit()']");
		mydriver.waitForPageToLoad("30000");
	}

}
