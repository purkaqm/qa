package selenium.formholders;

import java.util.*;

public abstract class ThreadBlockHolder {

	private LinkedList<AttachmentURLHolder> uRLAttachmentsList = null;
	private LinkedList<AttachmentLocalHolder> localAttachmentsList = null;
	private String subject = null;
	private String message = null;

	protected ThreadBlockHolder(String subject, String message){
		this.subject = subject;
		this.message = message;
	}

	public void addAttachment(AttachmentURLHolder attachment){
		if (null == uRLAttachmentsList) uRLAttachmentsList = new LinkedList<AttachmentURLHolder>();
		uRLAttachmentsList.add(attachment);
	}

	public void addAttachment(AttachmentLocalHolder attachment){
		if (null == localAttachmentsList) localAttachmentsList = new LinkedList<AttachmentLocalHolder>();
		localAttachmentsList.add(attachment);
	}

	public LinkedList<AttachmentURLHolder> getURLAttachmentsList() {
		return uRLAttachmentsList;
	}

	public LinkedList<AttachmentLocalHolder> getLocalAttachmentsList() {
		return localAttachmentsList;
	}

	public String getSubject() {
		return subject;
	}

	public String getMessage() {
		return message;
	}

	protected void setURLAttachmentsList(
			LinkedList<AttachmentURLHolder> attachmentsList) {
		uRLAttachmentsList = attachmentsList;
	}

	protected void setLocalAttachmentsList(
			LinkedList<AttachmentLocalHolder> localAttachmentsList) {
		this.localAttachmentsList = localAttachmentsList;
	}

}
