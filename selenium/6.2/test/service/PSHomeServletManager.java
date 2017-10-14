package test.service;

import java.io.*;
import java.net.*;

public class PSHomeServletManager {

	String servletConnectionString;
	URL contextURL;

	public PSHomeServletManager() throws Exception{
		servletConnectionString = Config.getInstance().getContextServer() + "servlet/PSHome?event=reload&current_context="
			+ Config.getInstance().getContextName() + "&hard-reset=false";
	}

	public void clearContextCache() throws Exception{
		HttpURLConnection conn = (HttpURLConnection) new URL(servletConnectionString).openConnection();
		InputStream rd = conn.getInputStream();
		while (rd.read() > -1) {}
		rd.close();
	}

}
