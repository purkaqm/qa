package test.service;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class Config {
	private Boolean isConfigNotReady = true;

	private String contextServer;
	private String contextServerJMXString;
	private String contextName;
	private String contextPath;
	private URL contextURL;

	private String dbConnectionString;
	private String dbName;
	private String dbBackupPath;

	private String seleniumHost;
	private int seleniumPort;

	private String attachmentsDir;

	// Protected constructor is sufficient to suppress unauthorized calls to the constructor
	protected Config() {
	}

	/**
	 * MyConfigHolder is loaded on the first execution of MyConfig.getInstance()
	 * or the first access to MyConfigHolder.instance , not before.
	 */
	private static class MyConfigHolder{
		private final static Config INSTANCE = new Config();
	}

	public static Config getInstance()  throws Exception {
		return MyConfigHolder.INSTANCE;
	}

	private synchronized  void readConfig() throws IOException{
		if (isConfigNotReady) {
			Properties config = new Properties();
			config.load(ClassLoader.getSystemResourceAsStream("test/service/general.properties"));

			URL contextURL = new URL(config.getProperty("context.url"));
			this.contextURL = contextURL;

			if (contextURL.getPort() > 0) {
				contextServer = contextURL.getProtocol() + "://" + contextURL.getHost() + ":" + contextURL.getPort() + "/";
			} else {
				contextServer = contextURL.getProtocol() + "://" + contextURL.getHost() + "/";
			}

			String[] contextPathSegments = contextURL.getPath().split("/");
			contextPath = new String("");
			for (int i = 0; i < contextPathSegments.length; i++){
				if ((contextPathSegments[i].length() > 0) & (contextPathSegments[i].indexOf(".page") < 0) & (contextPathSegments[i].indexOf(".epage") < 0)){
					contextPath = contextPath + "/" + contextPathSegments[i];
					contextName = contextPathSegments[i];
				}
			}

			contextServerJMXString = config.getProperty("context.jmx");

			dbConnectionString = config.getProperty("db.cstring");
			dbName = config.getProperty("db.name");
			dbBackupPath = config.getProperty("db.bupath");

			seleniumHost = config.getProperty("selenium.host");
			seleniumPort = Integer.valueOf(config.getProperty("selenium.port"));

			this.attachmentsDir = config.getProperty("att.folder");

			this.isConfigNotReady = false;
		}
	}

	public String getContextServer() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return contextServer;
	}

	public String getContextName() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return contextName;
	}

	public String getContextPath() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return contextPath;
	}

	public String getDbConnectionString() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return dbConnectionString;
	}

	public String getDbName() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return dbName;
	}

	public String getDbBackupPath() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return dbBackupPath;
	}

	public String getSeleniumHost() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return seleniumHost;
	}

	public int getSeleniumPort() throws IOException {
		if (this.isConfigNotReady) readConfig();
		return seleniumPort;
	}

	public String getContextServerJMXString() throws Exception {
		if (this.isConfigNotReady) readConfig();
		return contextServerJMXString;
	}

	public URL getContextURL() throws Exception {
		if (this.isConfigNotReady) readConfig();
		return contextURL;
	}

	public String getAttachmentsDir() {
		return attachmentsDir;
	}

}
