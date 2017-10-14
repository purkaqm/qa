package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.DocumentManager;
import com.powersteeringsoftware.core.managers.DocumentsListManager;
import com.powersteeringsoftware.core.objects.FileAttachment;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;


/**
 * Test issues can update attached– files</br>
 * Test can be executed only for 7.1 and higher
 *
 * @author selyaev_ag
 *
 */
public class TestAttachmentUpdates extends PSTest{

	private DocumentsListManager dLM;
	private DocumentManager dM;

	public TestAttachmentUpdates(){
		name = "Update Attachements";
	}

	public void run() {
		new WISummaryAdapter().navigatePageSummary(CoreProperties.getDefaultWorkItemIdWithPrefix());

		FileAttachment fA1 = new FileAttachment("utest001.txt",	"UpdateTextFile001");
		FileAttachment fA2 = new FileAttachment("utest002.txt",	"UpdateTextFile002");

		dLM = new DocumentsListManager(CoreProperties.getServerFolder());
		dM = new DocumentManager(CoreProperties.getLocalFolder(), CoreProperties.getServerFolder());

		dLM.addDocument(fA1);
		dLM.addDocument(fA2);

		try{
			Thread.sleep(5000);
		} catch (InterruptedException ie){
		}

		dLM.update(fA1);
		dM.update(fA2);
		dM.checkVersion(1, fA1);
		dM.checkVersion(2, fA2);
	}
}
