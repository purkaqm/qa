package com.powersteeringsoftware.libs.util;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import com.powersteeringsoftware.libs.util.servers.RemoteLinServerUtils;
import org.testng.TestException;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

public class ResinJMXManager {
    private MBeanServerConnection server;
    private static ResinJMXManager manager;

    private static long waitTimeToStop = System.currentTimeMillis();

    private static final int BROWSER_START_COUNT = 2;
    private static final int REFRESH_COUNT = 3;
    private static final int CONNECTION_ATTEMPT_NUMBER = 5;
    private static final TimerWaiter REFRESH_TIMEOUT = new TimerWaiter(5000);


    public static synchronized ResinJMXManager getConnection(String jmxConnectionString) throws IOException {
        manager = new ResinJMXManager();
        JMXServiceURL url = new JMXServiceURL(jmxConnectionString);
        PSLogger.info("Connecting to the JMX services of the server " + url.getURLPath());
        JMXConnector connector = null;
        IOException exception = null;
        for (int i = 0; i < CONNECTION_ATTEMPT_NUMBER; i++) {
            try {
                PSLogger.info("Try to connect to " + jmxConnectionString + " #" + (i + 1));
                connector = JMXConnectorFactory.connect(url);
                break;
            } catch (IOException io) {
                PSLogger.warn(io);
                exception = io;
                REFRESH_TIMEOUT.waitTime();
            }
        }
        if (connector == null) throw exception;
        manager.server = connector.getMBeanServerConnection();
        return manager;
    }

    /**
     * Restart Resin Server. It is better before invoking this method invoke
     * method killActiveConnectionAndRestoreDB
     *
     * @throws Exception
     */
    public static void restartResin() throws Exception {
        if (CoreProperties.isRestartResin()) {
            PSLogger.info("Restart resin");
            String cmd = CoreProperties.getApplicationServerRestartCmd();
            if (cmd == null) {
                doStopResin();
                doStartResin();
            } else {
                RemoteLinServerUtils.getServer().exec(CoreProperties.getApplicationServerRestartCmd());
            }
            getConnection(CoreProperties.getContextJMX()).
                    waitUntilServerIsReady(CoreProperties.getURLServerWithContext());
        }
    }

    private static void doStartResin() throws IOException {
        PSLogger.info("Start resin");
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec(CoreProperties.getApplicationServerStartCmd());
        PSLogger.debug(res.getResult());
        if (res.getExitCode() != 0) throw new IOException("Can't start resin : " + res.getExitCode());
    }

    private static void doStopResin() throws IOException {
        PSLogger.info("Stop resin");
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec(CoreProperties.getApplicationServerStopCmd());
        PSLogger.debug(res.getResult());
        if (res.getExitCode() != 0) throw new IOException("Can't stop resin : " + res.getExitCode());
    }

    private static void doClearLogs() {
        PSLogger.info("Clear logs");
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec(CoreProperties.getApplicationServerClearLogsCmd());
        PSLogger.debug(res.getResult());
        if (res.getExitCode() != 0) PSLogger.error("Error while clearing logs : " + res.getExitCode());
    }

    public static void startResin() throws Exception {
        doStartResin();
        try {
            String contextJMX = CoreProperties.getContextJMX();
            if (contextJMX == null) {
                PSLogger.warn("Can't find context jmx in config");
            }
            getConnection(contextJMX).waitUntilServerIsReady(CoreProperties.getURLServerWithContext());

        } catch (IOException e) {
            PSLogger.fatal(e.getClass().getName());
            PSLogger.fatal(e);
            throw e;
        }
    }

    public static void stopResin(boolean doKill) throws Exception {
        PSLogger.info("Stop resin");
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec(CoreProperties.getApplicationServerStopCmd());
        PSLogger.debug(res.getResult());
        if (doKill) {
            List<Integer> list = RemoteLinServerUtils.getServer().getProcessList("Resin");
            if (list != null)
                for (Integer i : list)
                    RemoteLinServerUtils.getServer().killProcess(i);
        } else if (res.getExitCode() != 0) {
            throw new IOException("Can't stop resin");
        }
        doClearLogs();
    }

    public static void cleanupLuceneIndexes() {
        String dir = CoreProperties.getApplicationServerLuceneDir();
        if (dir == null) return;
        PSLogger.info("Remove content of dir " + dir);
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec("rm -rf " + dir + "/*");
        if (res == null) {
            PSLogger.error("Can't clean lucene indexes!");
            return;
        }
        PSLogger.debug(res.getResult());
        if (res.getExitCode() != 0) {
            PSLogger.error("Can't remove lucene directory content");
        }
    }

    public static int getResinPid() {
        RemoteLinServerUtils.ExecResult res = RemoteLinServerUtils.getServer().exec("ps afx | grep resin | grep conf  | awk '{print $1}'");
        if (res == null) {
            PSLogger.fatal("failed to get current pid");
            return -1;
        }
        return Integer.parseInt(res.getResult().trim());
    }

    public static String getResinThreadDump() {
        int pid = getResinPid();
        return RemoteLinServerUtils.getServer().getThreadDump(pid);
    }

    public static synchronized String getDeadlock() {
        String res = getResinThreadDump();
        return res.contains("deadlock") ? res : null;
    }

    public synchronized ResinJMXManager restart() throws InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, IOException {
        PSLogger.info("Restarting Resin the server");
        try {
            manager.server.invoke(new ObjectName("resin:type=Server"), "restart", null, null);
        } catch (java.rmi.UnmarshalException ue) {
            System.out.println("Expected connection error caused by server restart: " + ue);
        }
        return manager;
    }

    public static void setNextTimeThreshold() {
        waitTimeToStop = System.currentTimeMillis() + CoreProperties.getWaitForElementToLoad() * 2;
    }

    public static long getNextTimeThreshold() {
        return waitTimeToStop;
    }

    public void waitUntilServerIsReady(String contextUrlString) throws InterruptedException, IOException {
        PSLogger.info("Waiting for context " + contextUrlString + " initialization");
        setNextTimeThreshold();
        // Create a trust manager that does not validate certificate chains
        Thread th = new Thread() {
            public void run() {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }

                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                                // do nothing
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                                // do nothing
                            }
                        }
                };

                try {
                    PSLogger.debug("Install the all-trusting trust manager");
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                } catch (Throwable e) {
                    PSLogger.fatal(e);
                }
            }
        };
        th.setDaemon(true);
        th.start();
        while (getNextTimeThreshold() > System.currentTimeMillis()) {
            Thread.sleep(1000);
            try {
                if (!th.isAlive())
                    new URL(contextUrlString).openConnection().getInputStream().close();
                PSLogger.debug("Context " + contextUrlString + " is up");
                return;
            } catch (java.net.ConnectException ce) {
                PSLogger.info("Wait: ConnectException : " + ce.getMessage());
            } catch (java.io.IOException ce) {
                PSLogger.info("Wait: IOException : " + ce.getMessage());
            }
        }
        throw new IOException("Timeout " + CoreProperties.getWaitForElementToLoadAsString() + " was reached");
    }

    /**
     * why this function is needed?
     *
     * @throws InterruptedException
     */
    public static void refresh() throws InterruptedException {
        Throwable e = null;
        for (int i = 0; i < BROWSER_START_COUNT; i++) {
            try {
                for (int j = 0; j < REFRESH_COUNT; j++) {
                    PSLogger.info("Refresh #" + (j + 1));
                    //SeleniumDriverFactory.getDriver().deleteAllVisibleCookies();
                    PSPage page = PSPage.getEmptyInstance();
                    page.refresh();
                    Boolean res = BasicCommons.isServerAvailable();
                    if (res == null || !res || !BasicCommons.isContextNotReady()) {
                        REFRESH_TIMEOUT.waitTime();
                    } else {
                        return;
                    }
                }
            } catch (Throwable ex) {
                e = ex;
                PSLogger.warn("Can't start browser, iter #" + (i + 1) + " : " + e.getMessage());
                PSLogger.saveFull();
                LocalServerUtils.killBrowsers();
            }
        }
        if (e != null)
            throw new TestException(e);
    }


}
