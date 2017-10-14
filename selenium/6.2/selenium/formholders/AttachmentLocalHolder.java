package selenium.formholders;

public class AttachmentLocalHolder {

	private String attachmentLocation = null;
	private String attachmentTitle = null;
	private String attachmentDescription = null;
	private String attachmentStatus = null;
	private Boolean attachmentPpt = null;

	public AttachmentLocalHolder(String attachmentLocation, String attachmentTitle){
		this.attachmentLocation = attachmentLocation;
		this.attachmentTitle = attachmentTitle;
	}

	public String getAttachmentDescription() {
		return attachmentDescription;
	}

	public void setAttachmentDescription(String attachmentDescription) {
		this.attachmentDescription = attachmentDescription;
	}

	public String getAttachmentStatus() {
		return attachmentStatus;
	}

	public void setAttachmentStatus(String attachmentStatus) {
		this.attachmentStatus = attachmentStatus;
	}

	public Boolean getAttachmentPpt() {
		return attachmentPpt;
	}

	public void setAttachmentPpt(Boolean attachmentPpt) {
		this.attachmentPpt = attachmentPpt;
	}

	public String getAttachmentLocation() {
		return attachmentLocation;
	}

	public String getAttachmentTitle() {
		return attachmentTitle;
	}

}
