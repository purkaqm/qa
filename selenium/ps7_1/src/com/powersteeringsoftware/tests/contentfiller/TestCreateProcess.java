package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.core.managers.AddProcessManager;
import com.powersteeringsoftware.core.objects.PSProcess;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.TimeStampName;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;

public class TestCreateProcess extends PSTest{

	public TestCreateProcess(){
		name = "Process Creating";
	}

	public void run(){
		addDMAICProcess();
		addCDDDRCProcess();
	}

	private void addProcess(PSProcess process){
		AddProcessManager pM = new AddProcessManager();
		pM.manage(process);
	}


	private PSProcess addDMAICProcess(){
		TimeStampName timeStamp = new TimeStampName("DMAIC");
		PSProcess DMAIC = new PSProcess(timeStamp.getTimeStampedName(), "This is a sample DMAIC process created by an automated script");
		DMAIC.addPhase("Define", null);
		DMAIC.addPhase("Measure", null);
		DMAIC.addPhase("Analyze", null);
		DMAIC.addPhase("Improve", null);
		DMAIC.addPhase("Control", null);

		this.addProcess(DMAIC);

		TestSession.putObject(TestSessionObjectNames.PROCESS_DMAIC.getObjectKey(), DMAIC);

		return  DMAIC;
	}

	private PSProcess addCDDDRCProcess(){
		TimeStampName timeStamp = new TimeStampName("CDDDRC");
		PSProcess CDDDRC = new PSProcess(timeStamp.getTimeStampedName(), "This is a sample CDDDRC process created by an automated script");
		CDDDRC.addPhase("Concept", "10");
		CDDDRC.addPhase("Define", "20");
		CDDDRC.addPhase("Design", "45");
		CDDDRC.addPhase("Development", "75");
		CDDDRC.addPhase("Release", "95");
		CDDDRC.addPhase("Close", "100");

		this.addProcess(CDDDRC);

		TestSession.putObject(TestSessionObjectNames.PROCESS_CDDDRC.getObjectKey(), CDDDRC);

		return CDDDRC;
	}
}
