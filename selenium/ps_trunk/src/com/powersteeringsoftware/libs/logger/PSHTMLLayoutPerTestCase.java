package com.powersteeringsoftware.libs.logger;

import org.apache.log4j.Level;
import org.apache.log4j.helpers.Transform;
import org.apache.log4j.spi.LoggingEvent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: sergey.zuev
 * Date: 05.10.2009
 * Time: 15:13:06
 * To change this template use File | Settings | File Templates.
 */
public class PSHTMLLayoutPerTestCase extends AbstractPSHTMLLayout {

    private static final String TIMESTAMP = "dd-MM-yyyy HH:mm:ss-SSS";
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat(TIMESTAMP);
    private static final String TIME_COLUMN_CLASS = "t";
    private static final String LEVEL_COLUMN_CLASS = "l";
    private static final String THREAD_COLUMN_CLASS = "th";
    private static final int TIME_COLUMN_WIDTH = 170;
    private static final int LEVEL_COLUMN_WITH = 70;
    private static final int THREAD_COLUMN_WITH = 30;

    private boolean isCssAppended;
    private boolean printThread;
    private Thread thread;

    public PSHTMLLayoutPerTestCase() {
        this(false);
    }

    public PSHTMLLayoutPerTestCase(boolean printThread) {
        this(printThread, null);
    }

    public PSHTMLLayoutPerTestCase(boolean printThread, Thread thread) {
        super();
        this.printThread = printThread;
        this.thread = thread;
    }

    public void setPrintThread(boolean b) {
        printThread = b;
    }

    /**
     * Formats a {@link org.apache.log4j.spi.LoggingEvent} in conformance with the log4j.dtd.
     */
    protected String _format(LoggingEvent event) {
        appendCss();
        if (thread != null && !Thread.currentThread().equals(thread)) return buf.toString();
        Level level = event.getLevel();
        if (level.equals(PSLevel.UPPER_INFO)) return buf.toString();
        long timestamp = event.timeStamp;
        String formattedTime = FORMAT.format(new Date(timestamp));
        HTMLColorLevel color = HTMLColorLevel.getLogColor(level);
        String message = Transform.escapeTags(event.getRenderedMessage());
        String mes = message != null ? message : "";
        mes = mes.replace("\\", "/").replaceAll("/+", "/").replace("http:/", "http://").replace("https:/", "https://");
        message = putLink(mes);
        // link to url:
        message = putUrls(message);
        buf.append("<table>");
        buf.append("<tr>");
        buf.append("<tr" + (color.getFontSize() != null ? " class='" + color.getClazz() + "'" : "") + ">");
        buf.append("<td class='" + TIME_COLUMN_CLASS + "'>").append(formattedTime).append("</td>");
        buf.append("<td class='" + LEVEL_COLUMN_CLASS + " " + color.getClazz() + "'>[").append(level).append("]</td>");
        if (printThread) {
            buf.append("<td class='" + THREAD_COLUMN_CLASS + "'>").append(Thread.currentThread().getId()).append("</td>");
        }
        buf.append("<td>").append(message).append("</td>");
        buf.append("</tr>");
        buf.append("</table>").append("\n");
        return buf.toString();
    }

    protected void appendCss() {
        if (isCssAppended) return;
        buf.append("<style type='text/css'>").append("\n");
        buf.append("tr {background: " + HTMLColorLevel.BACKGROUND + ";}").append("\n");
        buf.append("td." + TIME_COLUMN_CLASS + " {width: " + TIME_COLUMN_WIDTH + ";}").append("\n");
        buf.append("td." + LEVEL_COLUMN_CLASS + " {width: " + LEVEL_COLUMN_WITH + ";}").append("\n");
        buf.append("td." + THREAD_COLUMN_CLASS + " {width: " + THREAD_COLUMN_WITH + ";}").append("\n");
        for (HTMLColorLevel c : HTMLColorLevel.values()) {
            if (c.getClazz() == null) continue;
            if (c.getFontSize() == null) continue;
            buf.append("tr." + c.getClazz() + " {font-size: " + c.getFontSize() + ";}").append("\n");
        }
        for (HTMLColorLevel c : HTMLColorLevel.values()) {
            if (c.getClazz() == null) continue;
            buf.append("td." + c.getClazz() + " {color: " + c.getName() + ";}").append("\n");
        }
        buf.append("</style>").append("\n");
        isCssAppended = true;
    }

    private String putLink(String mes) {
        if (!mes.contains("file=[")) return mes;
        String path = mes.replaceFirst(".*file=\\[([^\\]]+)\\].*", "$1");
        File file = new File(path);
        if (!file.exists()) {
            return mes;
        }
        if (!file.isFile()) {
            return mes;
        }
        String webPath;
        if ((webPath = getRelativeWebPath(path)) != null) {
            mes = mes.replace(PSLogger.pathToMessage(path), "<a href='" + webPath + "'>" + file.getName() + "</a>");
        }
        return mes;
    }
}
