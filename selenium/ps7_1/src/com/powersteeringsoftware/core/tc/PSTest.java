package com.powersteeringsoftware.core.tc;

import org.apache.log4j.Logger;
import com.powersteeringsoftware.core.adapters.BasicCommons;
import com.powersteeringsoftware.core.util.waiters.TimerWaiter;
import com.thoughtworks.selenium.SeleniumException;



public abstract class PSTest implements Testable {
	protected String name = "Test name has not been written yet.";

	private Logger log = Logger.getLogger(PSTest.class);

	public final void execute() {
		log.info("Test: "+this.name+" - has started");
		setupEnvironment();

		try{
			run();
			log.info("Test: "+this.name+" - has passed");
		} catch (Exception e){
			log.error("Test: "+this.name+" - has FAILED");
			throw new PSTestCaseException(e);
		}

		new TimerWaiter(1000).waitTime();
	}

	protected void setupEnvironment(){
		try{
			BasicCommons.selectPageTopFrame();
		} catch (SeleniumException se){
			log.warn("Error while setuping environment for test",se);
		}
			BasicCommons.loadHomePage();

	}

	protected abstract void run();
}

