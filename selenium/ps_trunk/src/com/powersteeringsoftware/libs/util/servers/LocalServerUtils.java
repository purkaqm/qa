package com.powersteeringsoftware.libs.util.servers;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import org.dom4j.Document;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.htmlcleaner.HtmlCleaner;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.List;

/**
 * Class for operations with local server
 * User: szuev
 * Date: 26.04.2010
 * Time: 17:32:46
 * To change this template use File | Settings | File Templates.
 */
public class LocalServerUtils {
    private static Robot robot;

    private static final int DELAY = 300;
    private static final long F11_SLEEP = 30000;
    private static final long F11_SLEEP_STEP = 2000;
    //private static Map<Boolean, int[]> screenClientDifference = new HashMap<Boolean, int[]>();
    private static final Integer ALLOWED_COLORS_DIFFERENCE = 8;
    public static final Integer DIFFERENCE_FOR_SCROOLING = 8;
    private static Boolean isFullScreen = false;

    private static final int DEFAULT_WIDTH = 1440; //hardcode in rdesktop*bat
    private static final int DEFAULT_HEIGHT = 2000; //hardcode in rdesktop*bat
    private static final Rectangle DEFAULT_RECTANGLE = new Rectangle(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT);

    public static final int PING_TIMEOUT = 3000;
    private static PingThread pingThread;

    private static WindowsConsoleCommands localServer;

    static {
        initRobot();
    }

    private static void initRobot() {
        if (robot != null) return;
        try {
            robot = new Robot();
            robot.setAutoDelay(DELAY);
        } catch (Exception e) {
            PSLogger.warn("initRobot: " + e.getMessage());
        }
    }

    public static void copy(File src, File dst) {
        copy(src, dst, false);
    }

    public static void copy(File src, String dst) {
        copy(src, new File(dst), false);
    }

    public static void copy(File src, File dst, boolean doAppend) {
        InputStream in = null;
        OutputStream out = null;
        PSLogger.debug((doAppend ? "Append " : "Copy ") + src + " to " + dst + "!");
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst, doAppend);
            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            PSLogger.fatal(e);
        } catch (IOException e) {
            PSLogger.fatal(e);
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
                PSLogger.fatal(e);
            }
            if (out != null) try {
                out.close();
            } catch (IOException e) {
                PSLogger.fatal(e);
            }
        }
    }

    public static void copyAllFilesFromDirToDir(String src, String dst) {
        copyAllFilesFromDirToDir(src, dst, new String[]{});
    }

    public static void copyAllFilesFromDirToDir(String src, String dst, String... exceptions) {
        File srcDir = new File(src);
        File dstDir = new File(dst);
        PSLogger.debug("Copy files form " + src + " to " + dst + "; exceptions: " + Arrays.toString(exceptions));
        try {
            FileUtils.copyDirectory(srcDir, dstDir, getNotOrRegexFileFilter(exceptions));
        } catch (IOException e) {
            PSLogger.fatal(e);
        }
    }

    private static FileFilter getNotOrRegexFileFilter(String... exceptions) {
        if (exceptions.length == 0) return EmptyFileFilter.NOT_EMPTY;
        List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
        for (String regexp : exceptions) {
            filters.add(new RegexFileFilter(regexp));
        }

        return new NotFileFilter(new OrFileFilter(filters));
    }

    public static void deleteAllFilesFromDir(String src) {
        deleteAllFilesFromDir(src, new String[]{});
    }

    public static void deleteAllFilesFromDir(String src, String... exceptions) {
        File srcDir = new File(src);
        PSLogger.debug("Delete files form " + src + "; exceptions: " + Arrays.toString(exceptions));
        for (File thisFile : srcDir.listFiles(getNotOrRegexFileFilter(exceptions))) {
            if (thisFile.isFile()) {
                thisFile.delete();
            } else {
                PSLogger.debug("delete dir: " + thisFile.getAbsolutePath());
                deleteAllFilesFromDir(thisFile.getAbsolutePath(), exceptions);
                if (thisFile.listFiles().length == 0)
                    thisFile.delete();
            }
        }
    }

    public static List<File> listFiles(File dir, String... prefixes) {
        List<IOFileFilter> filters = new ArrayList<IOFileFilter>();
        for (String prefix : prefixes) {
            filters.add(new PrefixFileFilter(prefix));
        }
        return (List<File>) FileUtils.listFiles(dir, prefixes.length == 0 ? FileFileFilter.FILE : new OrFileFilter(filters), DirectoryFileFilter.INSTANCE);
    }

    public static void renameFiles(String prefix, List<File> files) {
        for (File file : files) {
            rename(file, prefix + file.getName());
        }
    }

    public static void renameFile(String prefix, File file) {
        rename(file, prefix + file.getName());
    }

    public static void renameFiles(String prefix, String... files) {
        List<File> list = new ArrayList<File>();
        for (String file : files) list.add(new File(file));
        renameFiles(prefix, list);
    }

    public static File rename(File file, String name) {
        String newName = file.getParentFile().getAbsolutePath() + File.separator + name;
        PSLogger.debug("Rename file " + file + " to " + newName);
        File res = new File(newName);
        if (file.exists()) {
            file.renameTo(res);
            return res;
        }
        PSLogger.debug2("Can't find file " + file + " to rename");
        return null;
    }

    public static File createFile(String name, String content) throws IOException {
        File file = new File(name);
        if (file.exists()) {
            if (!file.delete())
                throw new IOException("Can't delete file " + file);
        }
        if (!file.createNewFile()) {
            throw new IOException("Can't create file " + file);
        }
        if (content == null) return file;
        PSLogger.debug("Content: " + content);
        FileWriter fw = new FileWriter(file);
        BufferedWriter out = new BufferedWriter(fw);
        out.write(content);
        out.close();
        return file;
    }

    public static StringBuffer readFile(String name, int allowedLength) throws IOException {
        File file = new File(name);
        if (!file.exists()) {
            throw new IOException("Can't find file " + file);
        }
        StringBuffer res = new StringBuffer();
        FileReader fr = new FileReader(file);
        BufferedReader in = new BufferedReader(fr);
        String line;
        while ((line = in.readLine()) != null) {
            res.append(line.trim()).append("\n");
            if (allowedLength > 0 && res.length() > allowedLength) break;
        }
        in.close();
        return res;
    }

    public static StringBuffer readFile(String name) throws IOException {
        return readFile(name, -1);
    }


    private static boolean containsWithAsteriskPattern(List<String> list, String name) {
        for (String exception : list) {
            if (!exception.contains(".*")) continue;
            String firstPart = exception.replaceFirst("\\.\\*.*", "");
            String lastPart = exception.replaceFirst(".*\\.\\*", "");
            if (name.startsWith(firstPart) && name.endsWith(lastPart)) {
                return true;
            }
        }
        return false;
    }

    public static void saveDocument(Document doc, String file) {
        try {
            HTMLWriter writer = getHTMLWriter(new FileWriter(file));
            writer.write(doc);
            writer.flush();
        } catch (IOException e) {
            PSLogger.fatal(e);
        }
    }

    public static HTMLWriter getHTMLWriter(Writer w) {
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("utf-8");
        format.setNewlines(false);
        format.setTrimText(true);
        format.setIndent("");
        format.setXHTML(true);
        format.setExpandEmptyElements(false);
        format.setNewLineAfterNTags(20);
        return new HTMLWriter(w, format);
    }

    public static Document loadDocument(String file) {
        Document result = null;
        try {
            HtmlCleaner cleaner = new HtmlCleaner(new File(file));
            cleaner.setUseCdataForScriptAndStyle(false);
            cleaner.clean();
            SAXReader reader = new SAXReader(DOMDocumentFactory.getInstance());
            result = reader.read(new StringReader(cleaner.getCompactXmlAsString()));
        } catch (Exception e) {
            PSLogger.fatal(e);
        }
        return result;
    }

    public static void type(String txt) {
        try {
            type(robot, txt);
        } catch (IllegalArgumentException e) {
            PSLogger.fatal(e);
        }
    }

    public static void type(Robot robot, String txt) {
        PSLogger.debug("type text '" + txt + "'");
        for (char c : txt.toUpperCase().toCharArray()) {
            //robot.delay(delay);
            PSLogger.debug("type '" + c + "'");
            if (c == ':') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_SEMICOLON);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '/') {
                robot.keyPress(KeyEvent.VK_BACK_SLASH);
                robot.keyRelease(KeyEvent.VK_BACK_SLASH);
            } else if (c == '_') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_MINUS);
                //robot.keyPress(KeyEvent.VK_UNDERSCORE);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == ')') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_0);
                //robot.keyPress(KeyEvent.VK_RIGHT_PARENTHESIS);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else if (c == '(') {
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_9);
                //robot.keyPress(KeyEvent.VK_LEFT_PARENTHESIS);
                robot.keyRelease(KeyEvent.VK_SHIFT);
            } else {
                int code = KeyStroke.getKeyStroke(Character.toUpperCase(c), 0).getKeyCode();
                robot.keyPress(code);
                robot.keyRelease(code);
            }
        }
    }

    public static void enter() {
        PSLogger.info("Press 'Enter'");
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
    }

    public static void click() {
        mouseDown();
        mouseUp();
    }

    public static void mouseDown() {
        PSLogger.debug("robot.mouseDown");
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(DELAY);
    }

    public static void mouseUp() {
        PSLogger.debug("robot.mouseUp");
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(DELAY);
    }

    public static void mouseMoveAt(int x, int y) {
        PSLogger.debug("robot.mouseMoveAt " + x + " " + y);
        robot.mouseMove(x, y);
        robot.delay(DELAY);
    }

    public static int[] getHorizontalAndVerticalDifference(Color c1) {
        BufferedImage region = robot.createScreenCapture(DEFAULT_RECTANGLE).getSubimage(0, 0, 100, 200);
        Integer Y = null;
        Integer X = null;
        Color foundColor = null;
        int V = 100;

        for (int y = 1; y < region.getHeight(); y++) {
            Color c2 = new Color(region.getRGB(20, y));
            double r = c1.getRed() - c2.getRed();
            double g = c1.getGreen() - c2.getGreen();
            double b = c1.getBlue() - c2.getBlue();
            int v = (int) Math.sqrt(r * r + g * g + b * b);
            if (v < V) {
                V = v;
                Y = y;
                foundColor = c2;
            }
        }
        PSLogger.debug("Vertical difference is " + Y);
        if (foundColor == null) {
            PSLogger.warn("Can't calculate vertical difference");
            return null;
        }
        for (int x = 0; x < region.getWidth(); x++) {
            Color c2 = new Color(region.getRGB(x, Y));
            if (c2.equals(foundColor)) {
                X = x;
                PSLogger.debug("Horizontal difference is " + X);
                break;
            }
        }
        if (X == null) {
            PSLogger.warn("Can't calculate horizontal difference");
            return null;
        }
        return new int[]{X, Y};
    }

    public static void makeScreenCapture(String fileName) {
        try {
            makeSingleScreenCapture(fileName);
        } catch (Exception e) {
            PSLogger.fatal(e);
        }
    }

    public static void makeSingleScreenCapture(String fileName) throws IOException {
        PSLogger.debug("Make robot screen capture");
        BufferedImage im = robot.createScreenCapture(DEFAULT_RECTANGLE);
        ImageIO.write(im, CoreProperties.getImageFormat(), new File(fileName));
    }

    public static synchronized void convertFromDefaultImage(String fileName) {
        try {
            String format = CoreProperties.getImageFormat();
            if (CoreProperties.DEFAULT_IMAGE_FORMAT.equals(format)) {
                return;
            }

            File file = new File(fileName);

            BufferedImage srcImage = ImageIO.read(file);
            int width = srcImage.getWidth();
            int height = srcImage.getHeight();
            BufferedImage dstImage;
            dstImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
            Graphics2D g2 = null;
            try {
                g2 = dstImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.drawImage(srcImage, 0, 0, width, height, null);
            } finally {
                if (g2 != null) {
                    g2.dispose();
                }
            }

            ImageIO.write(dstImage, format, file);
        } catch (Exception e) {
            PSLogger.error("convertFromDefaultImage(" + fileName + "):" + e.getMessage());
            //PSLogger.fatal(e);
        }

    }

    public static void ctrlEnd() {
        PSLogger.debug("Push ctrl+end");
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_END);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void sendYes() {
        PSLogger.debug("Push Y");
        robot.keyPress(KeyEvent.VK_Y);
        robot.keyRelease(KeyEvent.VK_Y);
    }

    public static void ctrlHome() {
        PSLogger.debug("Push ctrl+home");
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_HOME);
        robot.keyRelease(KeyEvent.VK_HOME);
        robot.keyRelease(KeyEvent.VK_CONTROL);
    }

    public static void esc() {
        PSLogger.debug("Push ESCAPE");
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }

    public static void pageDown() {
        PSLogger.debug("Push Page Down");
        robot.keyPress(KeyEvent.VK_PAGE_DOWN);
        robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
    }


    public static void pageUp() {
        PSLogger.debug("Push Page Up");
        robot.keyPress(KeyEvent.VK_PAGE_UP);
        robot.keyRelease(KeyEvent.VK_PAGE_UP);
    }

    private static void f11() {
        class Region {
            private BufferedImage region;

            Region() {
                region = robot.createScreenCapture(DEFAULT_RECTANGLE).getSubimage(0, 0, 10, 15);
                robot.delay(DELAY);
            }

            private DataBuffer getDB() {
                return region.getRaster().getDataBuffer();
            }

            @Override
            public boolean equals(Object o) {
                if (o == null) return false;
                if (!(o instanceof Region)) return false;
                try {
                    DataBuffer db1 = getDB();
                    DataBuffer db2 = ((Region) o).getDB();
                    int size;
                    if ((size = db1.getSize()) != db2.getSize()) {
                        return false;
                    }
                    for (int i = 0; i < size; i++) {
                        if (db1.getElem(i) != db2.getElem(i)) {
                            return false;
                        }
                    }
                } catch (Exception e) {
                    PSLogger.debug("first image : " + region);
                    PSLogger.debug("second image: " + ((Region) o).region);
                    PSLogger.warn("equals: " + e.getMessage());
                }
                return true;
            }
        }
        Region was = new Region();
        PSLogger.saveFull();
        mouseMoveAt(0, 500);
        robot.keyPress(KeyEvent.VK_F11);
        robot.delay(DELAY);
        robot.keyRelease(KeyEvent.VK_F11);
        robot.delay(DELAY);
        long s = System.currentTimeMillis();
        boolean intermediate = false;
        while (new Region().equals(was)) {
            if (!intermediate && System.currentTimeMillis() - s > F11_SLEEP / 2) {
                PSLogger.warn("See screen");
                PSLogger.saveFull();
                intermediate = true;
            }
            if (System.currentTimeMillis() - s > F11_SLEEP) {
                PSLogger.saveFull();
                PSSkipException.skip("Can't make f11 using robot");
            }
            try {
                Thread.sleep(F11_SLEEP_STEP);
            } catch (InterruptedException e) {
                //ignore
            }
        }
        try {
            Thread.sleep(F11_SLEEP_STEP);
        } catch (InterruptedException e) {
            //ignore
        }
        PSLogger.saveFull();
    }

    public static void pushF11() {
        if (isFullScreen) {
            PSLogger.debug("F11 is pushed");
            return;
        }
        PSLogger.debug("Push F11 to full screen");
        f11();
        isFullScreen = true;
    }

    public static void resumeF11() {
        if (!isFullScreen) return;
        PSLogger.debug("Push F11 to standard screen");
        f11();
        isFullScreen = false;
    }

    public static void resetFullScreen() {
        isFullScreen = false;
    }

    public static WindowsConsoleCommands getLocalServer() {
        if (robot == null) return null;
        if (localServer == null) {
            if (CoreProperties.isLocalSeleniumServer()) {
                localServer = new WindowsConsoleCommands();
                PSLogger.debug(localServer);
                if (!localServer.isReallyWin()) localServer = null;
            }
        }
        return localServer;
    }

    /**
     * @return true if display enabled. if it is not server, then assume that there is not display
     */
    public static boolean isDisplayEnabled() {
        return isDisplayEnabled(true);
    }

    /**
     * @param checkIsServer if true then check server. in case it is server then allow display, otherwise prohibit.
     * @return true if display enabled.
     */
    public static boolean isDisplayEnabled(boolean checkIsServer) {
        return getLocalServer() != null && !getLocalServer().isSystemSession() && (!checkIsServer || getLocalServer().isWinServer());
    }

    public static String getHostInfo() {
        return getHostName() + "," + getHostUser();
    }

    public static String getHostName() {
        if (getLocalServer() != null) {
            return getLocalServer().host;
        }
        return null;
    }

    public static String getHostUser() {
        if (getLocalServer() != null) {
            return getLocalServer().user;
        }
        return null;
    }


    /**
     * kill browsers only if display is enabled
     */
    public static void killBrowsers() {
        isFullScreen = false;
        if (isDisplayEnabled()) {
            getLocalServer().killBrowser();
        }
    }

    public static void killProcess(String name) {
        if (!isDisplayEnabled()) return;
        getLocalServer().killProcess(name);
    }

    public static PingThread pingDataBaseDaemon() {
        if (pingThread == null) {
            String host = CoreProperties.getContextDBHost();
            if (host == null) return null;
            pingThread = new PingThread(host);
            Thread th = new Thread(pingThread);
            th.setName("Ping db(" + host + ")");
            th.setDaemon(true);
            th.start();
        }
        return pingThread;
    }

    public static boolean pingDB() {
        String host = CoreProperties.getContextDBHost();
        return host == null || ping(host);
    }

    public static boolean ping(String host) {
        return ping(host, 8080) || ping(host, 80);
    }

    public static boolean ping5(String host) {
        for (int i = 0; i < 5; i++) {
            if (ping(host)) return true;
        }
        return false;
    }

    public static boolean ping(String host, Integer port) {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(host);
            PSLogger.debug2("ping host: " + address.getHostName() + ", address: " + address.getHostAddress() + " : " + port);
        } catch (UnknownHostException e) {
            PSLogger.debug("Unable to lookup " + host);
            return false;
        }
        try {
            if (port == null || port == 7)
                return address.isReachable(PING_TIMEOUT);
        } catch (IOException e) {
            PSLogger.debug("Unable to reach " + host);
            return false;
        }
        SocketChannel sc = null;
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(address, port);
            sc = SocketChannel.open();
            sc.configureBlocking(true);
            return sc.connect(socketAddress);
        } catch (IOException e) {
            PSLogger.debug("Unable to reach " + host + ":" + port);
        } finally {
            try {
                if (sc != null)
                    sc.close();
            } catch (IOException e) {
                PSLogger.error(e);
            }
        }
        return false;
    }

    /**
     * method for selenium server
     * only for localhost!
     *
     * @param port
     * @return true if port ready
     */
    public static boolean isServerPortBusy(int port) {
        try {
            Socket socket = new Socket("localhost", port);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String readFileAsString(String filePath) throws IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
        BufferedInputStream f = new BufferedInputStream(new FileInputStream(filePath));
        f.read(buffer);
        return new String(buffer);
    }

    public static List<String> readTextFromJar(String s) {
        InputStream is = null;
        BufferedReader br = null;
        String line;
        ArrayList<String> list = new ArrayList<String>();

        try {
            is = FileUtils.class.getResourceAsStream(s);
            br = new BufferedReader(new InputStreamReader(is));
            while (null != (line = br.readLine())) {
                list.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    /**
     * Ping Thred
     */
    public static class PingThread implements Runnable {
        private String host;
        private boolean stop;
        private boolean isAlive;
        private static final long WAIT_TIMEOUT = PING_TIMEOUT;

        PingThread(String host) {
            this.host = host;
        }

        public void run() {
            while (true) {
                if (!stop) {
                    isAlive = true;
                    if (!ping(host))
                        PSLogger.warn("Ping " + host + " failed");
                    isAlive = false;
                }
                try {
                    Thread.sleep(WAIT_TIMEOUT);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }

        public void doStart() {
            stop = false;
        }

        public void doStop() {
            stop = true;
            while (isAlive) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Class for local server commands
     */
    public static class WindowsConsoleCommands {
        private int sessionId = -1;
        private String windowsVersion;
        private String windowsName;
        private String iexplore;
        private Map firefox;
        private Map googlechrome;
        private String host;
        private String user;
        private String drive;
        private static final String DRIVE = "%systemdrive%";
        private static final String USERPROFILE = "%userprofile%";
        private static final String HKLM = "HKEY_LOCAL_MACHINE";
        private static final String HKCU = "HKEY_CURRENT_USER";
        private static final String HKU = "HKEY_USERS";
        private static final String HK_64 = "Wow6432Node";
        private static final String WIN_XP = "5.1";
        private static final String WIN_2003 = "5.2";
        private static final String WIN_2008 = "6.1";
        private static final String WIN_7 = "6.1";
        private static final String WIN_8 = "6.2";

        WindowsConsoleCommands() {
            windowsVersion = getWindowsVersion();
            windowsName = getWindowsName();
            sessionId = getCurrentSessionId();
            host = getHostName();
            user = getSessionName();
            drive = getSystemDrive();
            toString();
        }

        public String toString() {
            return host + "(" + user + ")" + " : windows version: " + windowsVersion + ", session: " + sessionId + ", ie version: " + iexplore;
        }

        public Process exec(String cmd) {
            if (!cmd.toLowerCase().startsWith("cmd /c ")) cmd = "cmd /c " + cmd;
            PSLogger.debug2("exec : " + cmd);
            try {
                return Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Process execWmic(String cmd) {
            Process p = null;
            try {
                PSLogger.debug2("Wmic : " + cmd);
                p = Runtime.getRuntime().exec("wmic.exe");
                OutputStreamWriter oStream = new OutputStreamWriter(p.getOutputStream());
                oStream.write(cmd);
                oStream.flush();
                oStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return p;
        }

        public Process exec(String cmd, String stringToSearch) {
            Process p = exec(cmd);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            try {
                while ((line = in.readLine()) != null) {
                    PSLogger.debug(line);
                    if (line.contains(stringToSearch)) {
                        return p;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        PSLogger.fatal(e);
                    }
                }
            }
            return null;
        }

        public boolean killBrowser() {
            String br = CoreProperties.getBrowser().getProcess();
            PSLogger.info("kill browsers : " + br);
            return killProcess(br);
        }

        public boolean killProcess(String name) {
            try {
                String proc = searchProcess(name);
                PSLogger.debug("kill process : " + name);
                if (proc == null) {
                    PSLogger.debug("No process found for " + name);
                    return true;
                }
                String cmd = "cmd /c taskkill /FI \"SESSION eq " + sessionId + "\" /F /im " + proc +
                        " /T 2>NUL 1>NUL";
                Process p = exec(cmd);
                p.waitFor();
                if (p.exitValue() == 0) {
                    PSLogger.debug("Process " + proc + " has been killed on session " + sessionId);
                }
                Thread.sleep(10000);
                if (searchProcess(name) == null) return true;
                PSLogger.warn("But still exists in tasklist");
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
            return false;
        }

        public boolean isProcessPresent(String name) {
            try {
                return searchProcess(name) != null;
            } catch (Exception e) {
                return false;
            }
        }

        public String searchProcess(String processName) throws Exception {
            List<String> procs = getAllProcesses();
            for (String pc : procs) {
                if (pc.toLowerCase().startsWith(processName.toLowerCase())) return pc;
            }
            PSLogger.debug("Can't find any processes by name " + processName + " in list " + procs);
            return null;
        }

        public List<String> getAllProcesses() {
            return getAllProcesses(false);
        }

        public List<MyProcess> getProcesses() {
            return getProcesses(false);
        }

        public List<MyProcess> getProcessesForName(String name) {
            int pid = getCurrentPid();
            List<MyProcess> res = new ArrayList<MyProcess>();
            for (LocalServerUtils.MyProcess _p : getProcesses()) {
                if (_p.getId() == pid) continue; // ignore local
                if (!_p.getName().equals(name)) continue;
                PSLogger.debug(_p);
                res.add(_p);
            }
            return res;
        }

        private List<MyProcess> getProcesses(boolean doPrint) {
            try {
                Process p = exec("cmd /c tasklist /fi \"session eq " + sessionId + "\" 2>NUL");
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                List<MyProcess> res = new ArrayList<MyProcess>();
                while ((line = stdInput.readLine()) != null) {
                    if (doPrint)
                        PSLogger.debug(line);
                    int resid = -1;
                    int pid = -1;
                    String name = null;
                    String[] sLine = line.split("\\s+");
                    if (sLine.length < 3) continue;
                    if (sLine[1].trim().matches("^\\d+$")) {
                        pid = Integer.parseInt(sLine[1].trim());
                    }
                    if (sLine[3].trim().matches("^\\d+$"))
                        resid = Integer.parseInt(sLine[3].trim());
                    else if (sLine[2].trim().matches("^\\d+$"))
                        resid = Integer.parseInt(sLine[2].trim());
                    if (resid == sessionId) {
                        name = sLine[0].trim();
                    }
                    if (name != null) {
                        MyProcess pc = new MyProcess(pid, name, resid);
                        res.add(pc);
                    }
                }
                p.waitFor();
                if (res.size() > 1) {
                    res.remove(0);
                    res.remove(0);
                }
                return res;
            } catch (Exception e) {
                PSLogger.fatal(e.getMessage());
            }
            return null;
        }


        private List<String> getAllProcesses(boolean doPrint) {
            if (sessionId == -1) throw new NullPointerException("Can't find session id");
            List<MyProcess> procs = getProcesses(doPrint);
            if (procs == null) return null;
            List<String> res = new ArrayList<String>();
            for (MyProcess pc : procs) {
                if (pc.session == sessionId) {
                    res.add(pc.name);
                }
            }
            return res;
        }

        public boolean isUserExists(String user) {
            return getSingleLineCmdResult("net user " + user) != null;
        }

        public Map<String, String> getUsers() {
            Map<String, String> res = new HashMap<String, String>();
            try {
                Process p = execWmic("useraccount where LocalAccount='TRUE' get name,sid");
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = input.readLine()) != null) {
                    if (line.isEmpty()) continue;
                    PSLogger.debug(line);
                    String[] sLine = line.trim().split("\\s+");
                    if (sLine.length != 2) continue;
                    res.put(sLine[0], sLine[1]);
                }
                p.waitFor();
                res.remove("Name");
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
            return res;
        }

        public String getUserProfile(String user) {
            Map map = getUsers();
            if (!map.containsKey(user)) {
                throw new NullPointerException("Can't find user " + user);
            }
            String sid = (String) map.get(user);
            PSLogger.debug("Sid for " + user + " is " + sid);
            String res = getRegistryValue("HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion\\ProfileList\\" + sid, "ProfileImagePath", false);
            PSLogger.debug("User profile for " + user + " is " + res);
            return new File(res.toLowerCase().replace(DRIVE, drive)).getAbsolutePath();
        }

        public String getStartupLocationDir(String user) {
            String profile = getUserProfile(user);
            String res = getRegistryValue("HKU\\.DEFAULT\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders", "Startup", false);
            PSLogger.debug("startup folder for " + user + " is " + res);
            return new File(res.toLowerCase().replace(USERPROFILE, profile)).getAbsolutePath();
        }

        public boolean isSystemSession() {
            return !isWinXP() && sessionId == 0;
        }

        private String getSessionName() {
            String username = "%username%";
            String res = getSingleLineCmdResult("echo " + username);
            if (res.equalsIgnoreCase(username)) return "system";
            return res;
        }

        private String getHostName() {
            return getSingleLineCmdResult("hostname");
        }

        private String getSystemDrive() {
            return getSingleLineCmdResult("echo " + DRIVE);
        }

        private String getSingleLineCmdResult(String cmd) {
            List<String> res = getLineCmdResults(cmd, true);
            if (res != null && !res.isEmpty()) return res.get(res.size() - 1);
            return null;
        }

        private List<String> getLineCmdResults(String cmd) {
            return getLineCmdResults(cmd, true);
        }

        private List<String> getLineCmdResults(String cmd, boolean debug) {
            List<String> res = new ArrayList<String>();
            BufferedReader stdInput = null;
            BufferedReader stdError = null;
            try {
                Process p = exec("cmd /c " + cmd);
                stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = stdInput.readLine()) != null) {
                    if (debug)
                        PSLogger.debug(">" + line);
                    if (!line.isEmpty()) {
                        res.add(line.trim());
                    }
                }
                stdInput.close();
                stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                while ((line = stdError.readLine()) != null) {
                    PSLogger.error(line);
                }
                stdError.close();
                p.waitFor();
                if (p.exitValue() == 0) return res;
            } catch (Exception e1) {
                PSLogger.warn(e1.getMessage());
            } finally {
                if (stdError != null) try {
                    stdError.close();
                } catch (IOException e) {
                    //
                }
                if (stdInput != null) try {
                    stdInput.close();
                } catch (IOException e) {
                    //
                }
            }
            return null;
        }

        public int getSessionId(String user) {
            int res = -1;
            try {
                Process p = exec("cmd /c qwinsta 2>NUL");
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = stdInput.readLine()) != null) {
                    PSLogger.debug(line);
                    if (!line.matches(".*\\s+\\d+\\s+.*")) continue;
                    int id = Integer.parseInt(line.replaceFirst(".*\\s+(\\d+)\\s+.*", "$1"));
                    String name = line.replaceAll(".*\\s+([^\\s]+)\\s+" + id + "\\s+.*", "$1");
                    PSLogger.debug(name + "," + id);
                    if (name.equals(user)) {
                        res = id;
                    }

                }
                p.waitFor();
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
            return res;
        }

        public void killSession(int id) {
            try {
                Process p = exec("cmd /c rwinsta " + id);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    PSLogger.debug(line);
                }
                p.waitFor();
            } catch (Exception e) {
                PSLogger.fatal(e);
            }
        }

        public void killSession(String user) {
            int id = getSessionId(user);
            if (id == -1) {
                PSLogger.debug("Can't find user " + user);
                return;
            }
            if (id == sessionId) {
                PSLogger.warn("User is current " + user);
                return;
            }
            PSLogger.debug("Kill session for " + user);
            killSession(id);
        }

        private int getCurrentSessionId() {
            try {
                if (isWinXP()) {
                    PSLogger.debug("it's win XP");
                    return 0;
                }
                if (isWin8()) {
                    PSLogger.debug("it's win 8");
                    return 0;
                }
                Process p = exec("cmd /c qwinsta 2>NUL");
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line;
                String firstLine = null;
                while ((line = stdInput.readLine()) != null) {
                    PSLogger.debug(line);
                    if (line.startsWith(">")) {
                        firstLine = line;
                        break;
                    }
                }
                p.waitFor();
                if (firstLine != null && firstLine.matches(".*\\s+\\d+\\s+.*")) {
                    int res = Integer.parseInt(firstLine.replaceFirst(".*\\s+(\\d+)\\s+.*", "$1"));
                    PSLogger.debug("Session id is " + res);
                    return res;
                }
            } catch (IOException e1) {
                PSLogger.warn("Can't get session id: " + e1);
            } catch (InterruptedException e2) {
                PSLogger.warn("Can't get session id: " + e2);
            }
            return -1;
        }


        private String getWindowsVersion() {
            return getRegistryValue("HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "CurrentVersion");
        }

        private String getWindowsName() {
            return getRegistryValue("HKLM\\SOFTWARE\\Microsoft\\Windows NT\\CurrentVersion", "ProductName");
        }

        /**
         * getIExploreVersion
         *
         * @return version for ie
         */
        public String getIExploreVersion() {
            if (iexplore != null) return iexplore;
            String ver = getRegistryValue(getHKLMSoftwareRegistryDir() + "\\Microsoft\\Internet Explorer", "svcVersion");
            return iexplore = ver == null ? getRegistryValue(getHKLMSoftwareRegistryDir() + "\\Microsoft\\Internet Explorer", "Version") : ver;
        }

        public String getIExplorePath() {
            String files = null;
            for (String s : new String[]{"ProgramFiles(x86)", "ProgramFiles"}) { // first x86
                String _files = getSingleLineCmdResult("echo %" + s + "%");
                if (_files != null && !_files.isEmpty() && !_files.equals(s)) {
                    files = _files;
                    break;
                }
            }
            if (files == null) {
                PSLogger.error("Can't determinate iexplore path");
                return null;
            }
            return files + "\\Internet Explorer\\iexplore.exe";
        }

        private String getHKLMSoftwareRegistryDir() {
            String dir = "HKLM\\SOFTWARE";
            if (containsRegistryKey(dir + "\\" + HK_64)) {
                dir += "\\" + HK_64;
            }
            return dir;
        }

        public Map getGoogleChrome() {
            if (googlechrome != null) return googlechrome;
            String key = "LastInstallerSuccessLaunchCmdLine";
            String branch = "\\Google\\Update";
            String path = getRegistryValue("HKCU\\SOFTWARE" + branch, key);
            if (path == null || !new File(path).exists()) { // its for not server win:
                path = getRegistryValue(getHKLMSoftwareRegistryDir() + branch, key);
            }
            if (path == null || !new File(path).exists()) {
                for (String s : getRegistryKeys(getHKLMSoftwareRegistryDir() + branch + "\\ClientState")) {
                    path = getRegistryValue(s, key);
                    if (path != null) break;
                }
            }
            if (path == null || !new File(path).exists()) {
                path = getGoogleChromePath();
            }
            List<String> versions = new ArrayList<String>();
            for (String f : new File(path).getParentFile().list()) {
                if (f.matches("^[\\d\\.]+$")) {
                    versions.add(f);
                }
            }
            PSLogger.debug("Google Chrome versions : " + versions);
            Collections.sort(versions);
            googlechrome = new HashMap<String, String>();
            googlechrome.put(versions.get(versions.size() - 1), path);
            return googlechrome;
        }

        private String getGoogleChromePath() {
            String profile = getSingleLineCmdResult("echo %userprofile%");
            if (profile == null) {
                PSLogger.error("Can't find profile");
                return null;
            }
            File file = new File(profile + File.separator + getAppDataDir() + File.separator +
                    "Google" + File.separator +
                    "Chrome" + File.separator +
                    "Application" + File.separator +
                    "chrome.exe");
            if (file.exists()) return file.getAbsolutePath();
            PSLogger.error("Can't find file " + file.getAbsolutePath());
            return null;
        }

        public String getAppDataDir() {
            if (isWinXP() || isWin2k3()) {
                return "Local Settings" + File.separator + "Application Data";
            }
            if (isWin2k8() || isWin7()) {
                return "AppData" + File.separator + "Local";
            }
            return null;
        }

        @Deprecated
        public String getLastFirefox() {
            Map<String, String> map = getFirefoxInfo();
            Object[] keys = map.keySet().toArray();
            return map.get(keys.length - 1);
        }

        public Map<String, String> getFirefoxInfo() {
            if (firefox != null) return firefox;
            List<String> keys = getRegistryKeys(getHKLMSoftwareRegistryDir() + "\\Mozilla\\Mozilla Firefox");
            keys.remove(0);
            firefox = new LinkedHashMap<String, String>();
            for (String key : keys) {
                String path = getRegistryValue(key + "\\Main", "PathToExe");
                String version = key.replaceAll(".*\\\\", "");
                firefox.put(version, path);
            }
            return firefox;
        }

        /**
         * seaarch and close crash report dialog.
         * for firefox it should named 'crashreporter.exe'
         *
         * @return true if crash found
         */
        public boolean hasAnyCrash() {
            List<String> processes = getAllProcesses();
            PSLogger.debug("All processes : " + processes);
            for (String proc : processes) {
                if (proc.toLowerCase().contains("crash")) {
                    PSLogger.warn("Found crash process :  " + proc);
                    killProcess(proc);
                    return true;
                }
            }
            return false;
        }

        private boolean containsRegistryKey(String path) {
            List<String> res = getLineCmdResults("reg query \"" + path + "\"", false);
            return res != null && !res.isEmpty();
        }

        private List<String> getRegistryKeys(String path) {
            List<String> lines = getLineCmdResults("reg query \"" + path + "\"");
            if (lines == null) return null;
            List<String> res = new ArrayList<String>();
            for (String line : lines) {
                    PSLogger.debug(line);
                    if (line.trim().startsWith(HKLM) || line.trim().startsWith(HKCU))
                        res.add(line);
                }
            return res;
        }

        private String getRegistryValue(String path, String key) {
            return getRegistryValue(path, key, true);
        }

        private String getRegistryValue(String path, String key, boolean doPrint) {
            List<String> list = getLineCmdResults("reg query \"" + path + "\" /v " + key, doPrint);
            if (list == null || list.isEmpty()) return null;
            String res = null;
            for (String line : list) {
                if (line.startsWith(key)) {
                    res = line.trim().replaceAll("^[^\\s]+\\s+[^\\s]+\\s+", "").
                            replaceAll("^\"", "").replaceAll("\"$", "");
                }
            }
            if (doPrint) {
                PSLogger.debug(path + "\\" + key + "=" + res);
            }
            return res;
        }

        public boolean isWinXP() {
            return windowsVersion.equals(WIN_XP);
        }

        public boolean isWin8() {
            return windowsVersion.equals(WIN_8);
        }

        public boolean isWin2k3() {
            return windowsVersion.equals(WIN_2003);
        }

        public boolean isWin2k8() {
            return windowsVersion.equals(WIN_2008) && windowsName.contains("2008");
        }

        public boolean isWin7() {
            return windowsVersion.equals(WIN_7) && !windowsName.contains("2008");
        }

        public boolean isReallyWin() {
            return !windowsVersion.isEmpty();
        }

        public boolean isWinServer() {
            return !isWinXP() && !isWin7();
        }

        private int currentPid = -1;

        public int getCurrentPid() {
            if (currentPid != -1) return currentPid;
            String name = ManagementFactory.getRuntimeMXBean().getName();
            return currentPid = Integer.parseInt(name.replaceAll("@.*", ""));
        }

        private List<String> getThreadDump(int pid) {
            return getLineCmdResults("jstack " + pid);
        }

        public Map<Integer, List<String>> getThreadDumps() {
            Map<Integer, List<String>> res = new HashMap<Integer, List<String>>();
            for (LocalServerUtils.MyProcess _p : getProcessesForName("java.exe")) {
                List<String> dmp = getThreadDump(_p.pid);
                if (dmp == null) continue;
                res.put(_p.pid, dmp);
            }
            return res;
        }

        public Map<Integer, String> findJavaDeadlocks() {
            Map<Integer, List<String>> dumps = getThreadDumps();
            Map<Integer, String> res = new HashMap<Integer, String>();
            for (Integer pid : dumps.keySet()) {
                List<String> dump = dumps.get(pid);
                boolean hasDeadLock = false;
                for (String line : dump) {
                    if (line.contains("deadlock")) {
                        hasDeadLock = true;
                        break;
                    }
                }
                if (!hasDeadLock) continue;
                StringBuilder sb = new StringBuilder();
                for (String line : dump) {
                    sb.append(line).append("\n");
                }
                res.put(pid, sb.toString());
            }
            return res;
        }

        /**
         * Warning! application should be running locally, jstack should be in path
         *
         * @return String (First deadlock) or null
         */
        public String getDeadlock() {
            Map<Integer, String> res = findJavaDeadlocks();
            if (res.isEmpty()) return null;
            if (res.size() == 1) {
                Object id = res.keySet().toArray()[0];
                PSLogger.info("There is a deadlock for process " + id);
                return res.get(id);
            }
            PSLogger.info("There are deadlocks for processes " + res.keySet());
            return res.toString();
        }
    }

    public static class MyProcess {
        int pid, session;
        String name;

        private MyProcess(int pid, String name, int sessionId) {
            this.pid = pid;
            this.name = name;
            this.session = sessionId;
        }

        public int getId() {
            return pid;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name + ":" + pid;
        }
    }

}
