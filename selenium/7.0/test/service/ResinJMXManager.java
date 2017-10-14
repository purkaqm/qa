package test.service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

import javax.management.*;
import javax.management.remote.*;

import org.apache.log4j.Logger;

import selenium.driver.MySeleniumDriver;

public class ResinJMXManager {
	private MBeanServerConnection server = null;
	private static ResinJMXManager manager = null;

	private Logger logger;

	private ResinJMXManager(Logger logger){
		this.logger = logger;
	}

	public static synchronized ResinJMXManager getConnection(String jmxConnectionString, Logger logger) throws IOException{
		manager = new ResinJMXManager(logger);
        JMXServiceURL url = new JMXServiceURL(jmxConnectionString);
		logger.info("Connecting to the JMX services of the server");
		manager.server = JMXConnectorFactory.connect(url).getMBeanServerConnection();
		return manager;
	}

	public synchronized ResinJMXManager restart() throws InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, IOException{
		logger.info("Restarting the server");
		try{
			manager.server.invoke(new ObjectName("resin:type=Server"), "restart", null, null);
		} catch (java.rmi.UnmarshalException ue) {
			 System.out.println("Expected connection error caused by server restart: " + ue);
		}
		return manager;
	}

	public void waitUntilServerIsReady(MySeleniumDriver driver, String contextUrlString) throws InterruptedException, MalformedURLException, IOException{
		logger.info("Waiting for context initialization");
		Long timeToStop = System.currentTimeMillis() + 300000;
		Boolean isConnectionException;
		do {
			Thread.sleep(2000);
			isConnectionException = false;
			try {
				new URL(contextUrlString).openConnection().getInputStream().close();
			} catch (java.net.ConnectException ce) {
				isConnectionException = true;
			}
		} while((timeToStop < System.currentTimeMillis()) & (isConnectionException));

		logger.info("Refresh 1");
		driver.refresh();
//		driver.waitForPageToLoad("120000");

		Thread.sleep(60000);

		logger.info("Refresh 2");
		driver.refresh();
		driver.waitForPageToLoad("120000");
	}

}
