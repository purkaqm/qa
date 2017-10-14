package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.DiscussionBlockHolder;
import selenium.formholders.IssueBlockHolder;
import test.service.Config;

public class AddEditIssuePage {
	private MySeleniumDriver mydriver;

	public AddEditIssuePage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisAddEditIssuePage() {
		return mydriver.getTitle().contains("Add/Edit Issue");
	}


	public void setPriorityAndSubmit(IssueBlockHolder iBH) {
		mydriver.select("dom=document.getElementById('prioritySelect')", "value=" + iBH.getIssuePriority());
		mydriver.click("dom=document.getElementById('Submit_3')");
		mydriver.waitForPageToLoad("30000");
	}

	public void setPriorityAndSubmitForTask(IssueBlockHolder iBH) {
		mydriver.select("dom=document.getElementById('prioritySelect')", "value=" + iBH.getIssuePriority());
		mydriver.click("dom=document.getElementById('Submit_4')");
		mydriver.waitForPageToLoad("30000");
	}

	public void addIssue(IssueBlockHolder iBH) throws Exception{
		//add attachments if we have some
		if (!(null == iBH.getLocalAttachmentsList())) {
			//call pop-up and wait till it is ready
			mydriver.click("dom=document.getElementById('addDocuments_ADD_DOCS_DLG_SHOW')");
			mydriver.waitForElementToBecomeVisible("dom=document.getElementById('addDocuments_ADD_DOCS_DLG_SHOW')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('PSForm')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addDocuments_ADD_DOC_FORM_FILE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addDocuments_ADD_DOC_FORM_TITLE')", "30000");
			mydriver.waitForElementToAppear("dom=document.getElementById('addDocuments_ADD_DOCS_ADD_DOC')", "30000");
			//add attachments
			for (int i=0; i < iBH.getLocalAttachmentsList().size(); i++){
				//add a check if the path is a full path or relative path (now assumed as always relative to test files dir)
				mydriver.type("dom=document.getElementById('addDocuments_ADD_DOC_FORM_FILE')",
						Config.getInstance().getAttachmentsDir() + iBH.getLocalAttachmentsList().get(i).getAttachmentLocation());
				mydriver.type("dom=document.getElementById('addDocuments_ADD_DOC_FORM_TITLE')",
						iBH.getLocalAttachmentsList().get(i).getAttachmentTitle());
				mydriver.click("dom=document.getElementById('addDocuments_ADD_DOCS_ADD_DOC')");
				//Wait 1 minute for attachment to load (and 'loading.gif' to disappear)
				long timeToStop = System.currentTimeMillis() + 60000;
				String submittedDocumentsElementsHtml = "";
				do {
					submittedDocumentsElementsHtml = mydriver.getEval("window.document.getElementById('scroll_text').innerHTML");
					} while ((submittedDocumentsElementsHtml.indexOf("loading.gif") > -1) & (System.currentTimeMillis() < timeToStop));
			}
			//close pop-up with done
			mydriver.click("dom=document.getElementById('scroll_clipper').parentNode.getElementsByTagName('input')[0]");
			mydriver.waitForElementToBecomeInvisible("dom=document.getElementById('addDocuments_ADD_DOCS_DLG')", "30000");
		}
		//TODO Add support for URL attachments
		//Fill in the discussion fields and submit
		mydriver.type("//body/div[@id='content']//input[@id='subjectInput']", iBH.getSubject());
		mydriver.runScript("tinyMCE.execInstanceCommand('mce_editor_0', 'mceFocus');tinyMCE.setContent('" + iBH.getMessage() + "')");
		mydriver.select("dom=document.getElementById('prioritySelect')", "value=" + iBH.getIssuePriority());
		mydriver.click("dom=document.getElementById('Submit_3')");
		mydriver.waitForPageToLoad("30000");
	}

}
