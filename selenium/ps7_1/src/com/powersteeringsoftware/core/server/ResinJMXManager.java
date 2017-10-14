package com.powersteeringsoftware.core.server;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.management.*;
import javax.management.remote.*;
import org.apache.log4j.Logger;

import com.powersteeringsoftware.core.util.CoreProperties;

public class ResinJMXManager {
	private MBeanServerConnection server;
	private static ResinJMXManager manager;

	private static Logger logger = Logger.getLogger(ResinJMXManager.class);

	//public static final long WAIT_TIME_PERIOD_TO_STOP = 500000;
	//public static final long SLEEP_TIME_PERIOD = 10000;
	private static long waitTimeToStop = System.currentTimeMillis();

	public static final int REFRESH_COUNT = 2;

	private ResinJMXManager(){
	}

	public static synchronized ResinJMXManager getConnection(String jmxConnectionString)
		throws IOException{
		manager = new ResinJMXManager();
        JMXServiceURL url = new JMXServiceURL(jmxConnectionString);
		logger.info("Connecting to the JMX services of the server");
		manager.server = JMXConnectorFactory.connect(url).getMBeanServerConnection();
		return manager;
	}

	public synchronized ResinJMXManager restart() throws InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, IOException{
		logger.info("Restarting Resin the server");
		try{
			manager.server.invoke(new ObjectName("resin:type=Server"), "restart", null, null);
		} catch (java.rmi.UnmarshalException ue) {
			 System.out.println("Expected connection error caused by server restart: " + ue);
		}
		return manager;
	}

	public static void setNextTimeThreshold(){
		waitTimeToStop = System.currentTimeMillis()+Long.parseLong(CoreProperties.getWaitForPageToStop());
	}

	public static long getNextTimeThreshold(){
		return  waitTimeToStop;
	}

	public void waitUntilServerIsReady(String contextUrlString)
			throws InterruptedException, MalformedURLException, IOException {

		logger.info("Waiting for context initialization");

		setNextTimeThreshold();

		Boolean isConnectionException;
		do {
			Thread.sleep(Long.parseLong(CoreProperties.getWaitForElementToLoadAsString()));
			isConnectionException = false;
			try {
				new URL(contextUrlString).openConnection().getInputStream().close();
			} catch (java.net.ConnectException ce) {
				isConnectionException = true;
			}
		} while((getNextTimeThreshold() < System.currentTimeMillis()) & (isConnectionException));

		for(int i=0;i<REFRESH_COUNT;i++){
			logger.info("Refresh "+(i+1));
			refresh();
		}

		SeleniumDriverSingleton.getDriver().waitForPageToLoad(
				CoreProperties.getWaitForElementToLoadAsString());
	}

	private void refresh() throws InterruptedException{
		Thread.sleep(Long.parseLong(CoreProperties.getWaitForElementToLoadAsString()));
		SeleniumDriverSingleton.getDriver().refresh();
	}

}
