package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.issues.IssueManager;
import com.powersteeringsoftware.core.managers.thread.ThreadBlockManager;
import com.powersteeringsoftware.core.managers.thread.ThreadManager;
import com.powersteeringsoftware.core.managers.thread.ThreadsListManager;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.objects.Issue;
import com.powersteeringsoftware.core.objects.ThreadBlock;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;


/**
 * Class works only for 7.1
 *
 * @author selyaev_ag
 *
 */
public class TestCreateIssue extends PSTest{

	private ThreadManager tM;
	private ThreadBlockManager tBM;
	private IssueManager iM;
	private ThreadsListManager tLM ;
	private ThreadBlock tD;

	public TestCreateIssue(){
		name = "Create Issue";
	}

	public void run() {
		TestSession.assertIsApplicationVerison71();

		new WISummaryAdapter().navigatePageSummary(CoreProperties.getDefaultWorkItemIdWithPrefix());

		tD = new ThreadBlock("TestDiscussion",
				"This is just a test discussion.",
				WorkItem.createWorkItemUsingCoreProperties());
		tD.addAttachment(new FileAttachment("dtest001.txt",	"DiscussionTextFile001"));
		tD.addAttachment(new FileAttachment("dtest002.txt",	"DiscussionTextFile002"));
		tD.setIssue(new Issue(3));

		tM = new ThreadManager(CoreProperties.getLocalFolder());
		tBM = new ThreadBlockManager(CoreProperties.getServerFolder());
		iM = new IssueManager(CoreProperties.getServerFolder());
		tLM = new ThreadsListManager();

		tLM.goToNewThreadPage();
		tBM.createNewBlock(tD);
		tM.testAttachments(tD);
		tM.escalate(tD);
		iM.addIssueToBlock(tD);
		tM.testAttachments(tD);
		tM.deEscalate(tD);
		tM.testAttachments(tD);
	}



}
