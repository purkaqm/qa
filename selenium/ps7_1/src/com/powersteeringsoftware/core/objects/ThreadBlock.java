package com.powersteeringsoftware.core.objects;

import java.util.LinkedList;

import com.powersteeringsoftware.core.objects.works.WorkItem;

public class ThreadBlock {
	private String subject;
	private String message;
	private LinkedList<FileAttachment> FileAttachmentsList;
	private WorkItem parentProject;
	private String threadName;
	private ThreadBlock parentThreadBlock;
	private Issue issue;

	public ThreadBlock(String subject, String message, WorkItem _parentProject){
		this.subject = subject;
		this.message = message;
		this.FileAttachmentsList = new LinkedList<FileAttachment>();
		this.parentProject = _parentProject;
		this.parentThreadBlock = null;
		this.threadName = subject;
		this.issue = new Issue();
	}

	public ThreadBlock(String subject, String message, WorkItem parentProject, ThreadBlock parentThreadBlock){
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

	public WorkItem getParentProject() {
		return parentProject;
	}

	public void setParentProject(WorkItem parentProject) {
		this.parentProject = parentProject;
	}

}
