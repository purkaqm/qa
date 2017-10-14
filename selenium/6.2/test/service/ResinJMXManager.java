package test.service;

import javax.management.*;
import javax.management.remote.*;

public class ResinJMXManager {
	MBeanServerConnection server = null;

	public ResinJMXManager() throws Exception{
        JMXServiceURL url = new JMXServiceURL(Config.getInstance().getContextServerJMXString());
        this.server =  JMXConnectorFactory.connect(url).getMBeanServerConnection();

	}

	public synchronized void serverRestart() throws Exception{
        server.invoke(new ObjectName("resin:type=Server"), "restart", null, null);
	}

}
