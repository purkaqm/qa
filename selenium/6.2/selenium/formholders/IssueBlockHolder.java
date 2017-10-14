package selenium.formholders;

public class IssueBlockHolder extends ThreadBlockHolder{
	private String issuePriority;

	public IssueBlockHolder(String issueSubject, String issueMessage, String issuePriority){
		super(issueSubject, issueMessage);
		this.issuePriority = issuePriority;
	}

	public String getIssuePriority() {
		return issuePriority;
	}

	public DiscussionBlockHolder getDeEscalated(){
		DiscussionBlockHolder dBH = new DiscussionBlockHolder(this.getSubject(), this.getMessage());
		dBH.setLocalAttachmentsList(this.getLocalAttachmentsList());
		dBH.setURLAttachmentsList(this.getURLAttachmentsList());
		return dBH;
	}
}
