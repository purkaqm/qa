package com.powersteeringsoftware.core.server;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openqa.selenium.server.RemoteControlConfiguration;
import org.openqa.selenium.server.SeleniumServer;

/**
 * Start and stop Selenium Server
 *
 * @since implemented in 1-b14 2009 June 22
 *
 * @deprecated
 *
 * This class is not deprecated, but it's works too unstable. Selenium started with
 * this class method startSeleniumServer() not always stop to work after
 * invoking method stopSeleniumServer()
 */
public class SeleniumServerManager {

	private static final SeleniumServerManager _instance = new SeleniumServerManager();
	private Logger log = Logger.getLogger(SeleniumServerManager.class);
	protected SeleniumServerManager() {
	}

	public static SeleniumServerManager getInstance() {
		return _instance;
	}

	private Map<Integer, SeleniumServer> _servers = new HashMap<Integer, SeleniumServer>();;

	/*
	 * Return true if a new server was created and started on the given port,
	 * false if this controller thinks that the server is already running and
	 * did nothing
	 */
	public synchronized boolean startSeleniumServer(Integer port) {
		if (port == null)
			port = RemoteControlConfiguration.DEFAULT_PORT;
		SeleniumServer server = _servers.get(port);

		if (server == null) {
			try {
				RemoteControlConfiguration configuration = new RemoteControlConfiguration();
				configuration.setPort(port);

				server = new SeleniumServer(configuration);
				log.info("Selenium Server on port " + port + " was created");
			} catch (Exception e) {
				log.error("Could not create Selenium Server");
			}

			try {
				server.start();
				log.info("Selenium Server on port " + port + " was started ");
			} catch (Exception e) {
				log.error("Could not create Selenium Server");
			}

			_servers.put(port, server);
			return true;
		} else {
			return false;
		}
	}

	public boolean startSeleniumServer() {
		return startSeleniumServer(null);
	}

	/*
	 * Return true if server on that port was found running and stopped, and
	 * false if this controller could not find a server running on that port,
	 * and did not do anything
	 */
	public synchronized boolean stopSeleniumServer(Integer port) {
		if (port == null)
			port = RemoteControlConfiguration.DEFAULT_PORT;

		SeleniumServer server = _servers.get(port);

		if(server == null) return false;

		try {
			server.stop();
			_servers.remove(port);
			log.info("Selenium Server on port " + port + " was stopped");
		} catch (Exception e) {
			log.error("Could not stop Selenium Server");
		}
		return true;
	}



	public synchronized boolean stopSeleniumServer() {
		return stopSeleniumServer(null);
	}

}