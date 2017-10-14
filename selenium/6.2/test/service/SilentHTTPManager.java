package test.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class SilentHTTPManager {

	public SilentHTTPManager() {
	}

	public void waitUntilResponseCodeIs200(String millisToWait) throws Exception {
		URL monitorURL = new URL(Config.getInstance().getContextURL(), "monitor.jsp");
		Long timeToStop = System.currentTimeMillis() + Long.valueOf(millisToWait);

		HttpURLConnection conn = (HttpURLConnection) monitorURL.openConnection();
		do {
			try {
				conn.getInputStream().close();
			} catch (IOException e) {
				if (conn.getResponseCode() == 500) {
					conn = (HttpURLConnection) monitorURL.openConnection();
				} else {
					throw e;
				}
			}
		} while ((System.currentTimeMillis() < timeToStop) & (conn.getResponseCode() != 200));
//		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		String inputLine;
//		while ((inputLine = in.readLine()) != null)
//			System.out.println(inputLine);
//		in.close();
//		//conn.getInputStream().close();
//		Long timeToStop = System.currentTimeMillis() + Long.valueOf(millisToWait);
//		while ((System.currentTimeMillis() < timeToStop) & (conn.getResponseCode() != 200))
//			if (conn.getResponseCode() != 200) {
//				conn = (HttpURLConnection) new URL(Config.getInstance().getContextURL(), "").openConnection();
//				conn.getInputStream().close();
//			}
	}
}
