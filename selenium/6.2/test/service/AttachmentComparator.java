package test.service;

import java.io.*;
import java.net.*;

public class AttachmentComparator {

	public AttachmentComparator(){

	}

	public boolean compareURLWithLocalFile(String sessionCookie, String fileUrl, String fullLocalFilePath) throws IOException{
		URL myUrl = new URL(fileUrl);
		URLConnection urlConn = myUrl.openConnection();
		urlConn.setRequestProperty("Cookie", sessionCookie);
		urlConn.connect();
		BufferedReader in = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
		String netContent = in.readLine();
		RandomAccessFile f = new RandomAccessFile(fullLocalFilePath,"r");
		String localContent = f.readLine();
		return netContent.equals(localContent);
	}
}
