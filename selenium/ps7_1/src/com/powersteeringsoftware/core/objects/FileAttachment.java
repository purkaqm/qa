package com.powersteeringsoftware.core.objects;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

public class FileAttachment implements IAttachment{

	public static class AttachmentComparator {

		public AttachmentComparator(){
		}

		public boolean compareURLWithLocalFile(String sessionCookie,
				String fileUrl, String fullLocalFilePath){
			//URL myUrl = new URL(fileUrl);
			try{
			URLConnection urlConn = new URL(fileUrl).openConnection();
			urlConn.setRequestProperty("Cookie", sessionCookie);
			urlConn.connect();
			//BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			String netContent = new BufferedReader(new InputStreamReader(urlConn.getInputStream())).readLine();
			//RandomAccessFile f = new RandomAccessFile(fullLocalFilePath,"r");
			String localContent = new RandomAccessFile(fullLocalFilePath,"r").readLine();

			return netContent.equals(localContent);

			} catch (IOException ioe){
				return false;
			}

		}
	}

	private String location;
	private String title;
	private String description;
	private Boolean ppt;
	private String status;

	public FileAttachment(String location){
		this.location = location;
		this.title = null;
		this.description = null;
		this.ppt = null;
		this.status = null;
	}

	public FileAttachment(String location, String title){
		this.location = location;
		this.title = title;
		this.description = null;
		this.ppt = null;
		this.status = null;
	}

	public String getPath() {
		return location;
	}

	public void setPath(String path) {
		this.location = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getPpt() {
		return ppt;
	}

	public void setPpt(Boolean ppt) {
		this.ppt = ppt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}


}
