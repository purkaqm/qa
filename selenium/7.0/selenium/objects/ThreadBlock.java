package selenium.objects;

import java.util.LinkedList;

public class ThreadBlock {
	private String subject;
	private String message;

	private LinkedList<FileAttachment> FileAttachmentsList;

	private Project parentProject;

	private String threadName;
	private ThreadBlock parentThreadBlock;

	private Issue issue;

	public ThreadBlock(String subject, String message, Project parentProject){
		this.subject = subject;
		this.message = message;
		this.FileAttachmentsList = null;
		this.parentProject = parentProject;
		this.parentThreadBlock = null;
		this.threadName = subject;
		this.issue = null;
	}

	public ThreadBlock(String subject, String message, Project parentProject, ThreadBlock parentThreadBlock){
		this.subject = subject;
		this.message = message;
		this.FileAttachmentsList = null;
		this.parentProject = parentProject;
		this.parentThreadBlock = parentThreadBlock;
		this.threadName = parentThreadBlock.getThreadName();
		this.issue = null;
	}

	public String getSubject() {
		return this.subject;
	}

	public String getMessage() {
		return this.message;
	}

	public LinkedList<FileAttachment> getFileAttachmentsList() {
		return this.FileAttachmentsList;
	}

	public Boolean isReply(){
		return (null == parentThreadBlock);
	}

	public ThreadBlock getParentThreadBlock() {
		return parentThreadBlock;
	}


	public Boolean hasIssue() {
		return (null == issue);
	}

	public Issue getIssue(){
		return issue;
	}

	public void setIssue(Issue issue){
		this.issue = issue;
	}

	public String getThreadName(){
		return this.threadName;
	}

	public void addAttachment(FileAttachment attachment){
		if (null == FileAttachmentsList) FileAttachmentsList = new LinkedList<FileAttachment>();
		FileAttachmentsList.add(attachment);
	}

	public Project getParentProject() {
		return parentProject;
	}

	public void setParentProject(Project parentProject) {
		this.parentProject = parentProject;
	}

}
