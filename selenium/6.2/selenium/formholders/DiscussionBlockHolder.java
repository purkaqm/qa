package selenium.formholders;

public class DiscussionBlockHolder extends ThreadBlockHolder{

	public DiscussionBlockHolder(String issueSubject, String issueMessage){
		super(issueSubject, issueMessage);
	}

	public IssueBlockHolder getEscalated(String priority){
		IssueBlockHolder iBH = new IssueBlockHolder(this.getSubject(), this.getMessage(), priority);
		iBH.setLocalAttachmentsList(this.getLocalAttachmentsList());
		iBH.setURLAttachmentsList(this.getURLAttachmentsList());
		return iBH;
	}

}