package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.issues.IssueManager;
import com.powersteeringsoftware.core.managers.issues.IssuesListManager;
import com.powersteeringsoftware.core.managers.thread.ThreadBlockManager;
import com.powersteeringsoftware.core.managers.thread.ThreadManager;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.Issue;
import com.powersteeringsoftware.core.objects.ThreadBlock;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Test issues can attach files</br>
 * Test can be executed only for 7.1 and higher
 *
 * @author selyaev_ag
 *
 */
public class TestAttachments extends PSTest{

	private ThreadManager tM;
	private ThreadBlockManager tBM;
	private IssuesListManager iLM;
	private IssueManager iM;

	public TestAttachments(){
		name = "Issue Attachements";
	}

	public void run() {

		WorkItem wi = WorkItem.createWorkItemUsingCoreProperties();

		new WISummaryAdapter().navigatePageSummary(CoreProperties.getDefaultWorkItemIdWithPrefix());

		ThreadBlock tI = new ThreadBlock("TestIssue", "This is just a test issue created by an automated script.", wi);
		tI.addAttachment(new FileAttachment("itest001.txt",	"IssueTextFile001"));
		tI.addAttachment(new FileAttachment("itest002.txt",	"IssueTextFile002"));
		tI.setIssue(new Issue(3));

		ThreadBlock rB = new ThreadBlock("TestReply", "This is just a test reply created by an automated script.",	wi);
		rB.addAttachment(new FileAttachment("itest003.txt",	"IssueTextFile003"));
		rB.addAttachment(new FileAttachment("itest004.txt",	"IssueTextFile004"));
		rB.setIssue(new Issue(3));

		tM = new ThreadManager(CoreProperties.getLocalFolder());
		tBM = new ThreadBlockManager(CoreProperties.getServerFolder());
		iLM = new IssuesListManager();
		iM = new IssueManager(CoreProperties.getServerFolder());

		iLM.openEditNewIssuePage();
		iM.createNewBlockWithIssue(tI);
		tM.testAttachments(tI);
		tM.reply(tI);
		tBM.createNewBlock(rB);
		tM.testAttachments(rB);
		tM.escalate(rB);
		iM.addIssueToBlock(rB);
		tM.testAttachments(rB);
		tM.close(rB);
		tM.testAttachments(rB);
	}

}
