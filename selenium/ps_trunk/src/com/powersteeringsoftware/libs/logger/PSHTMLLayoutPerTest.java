package com.powersteeringsoftware.libs.logger;

import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import org.apache.log4j.spi.LoggingEvent;
import org.dom4j.dom.DOMDocument;
import org.dom4j.dom.DOMElement;
import org.w3c.dom.NodeList;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.powersteeringsoftware.libs.logger.AbstractPSHTMLLayout.HTMLColorLevel.BLUE;
import static com.powersteeringsoftware.libs.logger.AbstractPSHTMLLayout.HTMLColorLevel.GREEN;
import static com.powersteeringsoftware.libs.logger.PSResults.TITLE;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 27.04.2010
 * Time: 13:31:11
 * To change this template use File | Settings | File Templates.
 */
public class PSHTMLLayoutPerTest extends AbstractPSHTMLLayout {
    private static final int FIRST_COLUMN_LENGTH = 31;
    private static final String FIRST_COLUMN_FONT_SIZE = "17ps";
    public static final String SPACE = StrUtil.fromUTF8("\u00A0");
    public static final String WORD_SEPARATOR = "</br>";

    private static final String FIRST_COLUMN_CLASS = "c1";
    private static final String SECOND_COLUMN_CLASS = "c2";
    private static final String THIRD_COLUMN_CLASS = "c3";
    private static final String FOURTH_COLUMN_CLASS = "c4";
    private static final int FIRST_COLUMN_WIDTH = 250;
    private static final int THIRD_COLUMN_WIDTH = 50;
    private static final int FOURTH_COLUMN_WIDTH = 70;
    private static final String TITLE_CLASS = "title";
    private static final String PER_TEST_TITLE_CLASS = "title-per-test";
    private static final String DATE_FORMAT = "HH:mm";

    public static final String LOGS_LINK = "(logs)";

    private static DOMDocument doc;
    private boolean isCssAppended;


    protected String _format(LoggingEvent event) {
        appendCss();
        String testName = null;
        String linkName = "";
        String fileName = "";
        Long start = null;
        Long end = null;
        PSResults result;
        if (event.getMessage() instanceof Object[]) {
            Object[] objects = (Object[]) event.getMessage();
            if (objects.length < 2) return null;
            result = (PSResults) objects[0];
            testName = (String) objects[1];
            if (objects.length > 3) {
                linkName = ((String) objects[2]).replace("\n", WORD_SEPARATOR);
                fileName = (String) objects[3];
            }
            if (objects.length > 5) {
                start = (Long) objects[4];
                end = (Long) objects[5];
            }
        } else {
            return null;
        }
        if (result.equals(TITLE)) {
            boolean perTest = !fileName.isEmpty() && !linkName.isEmpty();
            buf.append("<table>\n");
            buf.append("\t<tr style=\"background: ").append(result.color()).append(";\">\n");
            buf.append("\t\t<td alight='left'>");
            buf.append("<strong class='" + (perTest ? PER_TEST_TITLE_CLASS : TITLE_CLASS) + "'>").append(testName).append("</strong>");
            buf.append("</td>");
            if (perTest) {
                buf.append("\t\t<td ").append("width='" + FOURTH_COLUMN_WIDTH + "'>");
                String file = getRelativeWebPath(fileName);
                buf.append("\t\t\t<a href='").append(file).append("'>").append(linkName).append("</a>");
                buf.append("\t\t</td>\n");
            }
            buf.append("\t</tr>\n");
            buf.append("</table>\n");
        } else {
            testName = formatCellText(testName, FIRST_COLUMN_LENGTH);
            buf.append("<table>\n");
            buf.append("\t<tr>\n");
            buf.append("\t\t<td class='").append(FIRST_COLUMN_CLASS).append("'>").append(testName).append("</td>");
            buf.append("\t\t<td class='").append(SECOND_COLUMN_CLASS).append("'>");
            buf.append("\t\t\t<a href='").append(getRelativeWebPath(fileName)).append("'>").append(linkName).append("</a>");
            buf.append("\t\t</td>\n");

            buf.append("\t\t<td class='").append(THIRD_COLUMN_CLASS).append("'>");
            if (start != null)
                buf.append(new SimpleDateFormat(DATE_FORMAT).format(end));
            buf.append("</td>");

            buf.append("\t\t<td style='color: ").append(result.color()).append("' class='" + FOURTH_COLUMN_CLASS + "'>").append(result).append("</td>");

            buf.append("\t</tr>\n");
            buf.append("</table>\n");
        }
        return buf.toString();
    }

    protected void appendCss() {
        if (isCssAppended) return;
        buf.append("<style type='text/css'>").append("\n");
        buf.append("table {width: 100%;}").append("\n");
        buf.append("tr {background: " + HTMLColorLevel.BACKGROUND + ";}").append("\n");
        buf.append("td." + FIRST_COLUMN_CLASS + " {width: " + FIRST_COLUMN_WIDTH + "px; color: " + GREEN.getName() + "; font-family: monospace; font-size:" + FIRST_COLUMN_FONT_SIZE + ";}").append("\n");
        buf.append("td." + SECOND_COLUMN_CLASS + " {color: " + BLUE.getName() + ";}").append("\n");
        buf.append("td." + THIRD_COLUMN_CLASS + " {width: " + THIRD_COLUMN_WIDTH + "px;color:" + GREEN.getName() + ";}").append("\n");
        buf.append("td." + FOURTH_COLUMN_CLASS + " {width: " + FOURTH_COLUMN_WIDTH + "px;}").append("\n");
        buf.append("</style>").append("\n");
        isCssAppended = true;
    }

    private static String formatCellText(String str, int len) {
        StringBuffer res = new StringBuffer();
        //int count = 1;
        for (char c : str.toCharArray()) {
            res.append(c);
            if (res.length() % len == 0) {
                //if (res.length() != str.length())
                //    ++count;
                res.append(WORD_SEPARATOR);
            }
        }
        /*for (int i = res.length(); i < len * count; i++) {
            res.append(SPACE);
        }*/
        return res.toString();
    }

    public String getRelativeWebPath(String file) {
        if (file == null) return null;
        if (baseDir == null) {
            baseDir = PSLogger.getLogsDir().getParentFile().getAbsolutePath();
            PSLogger.debug("BASE_DIR for main logger is" + baseDir);
        }
        return getRelativeWebPath(baseDir, file);
    }


    private static void appendLink(DOMElement top, File file, String name, String id) {
        if (top == null) return;
        DOMElement link = createLink((DOMDocument) top.getDocument(), file, name, id);
        if (link.getParent() == null) {
            DOMElement text = (DOMElement) top.addText("," + SPACE);
            text.add(link);
        }
        PSLogger.debug(top.asXML());
    }

    private static DOMElement getHTMLHeader() {
        DOMElement head = (DOMElement) getDocument().selectSingleNode("//head");
        if (head == null) {
            head = (DOMElement) doc.getRootElement().addElement("head");
        }
        return head;
    }

    public static void setTitle(String set) {
        DOMElement head = getHTMLHeader();
        DOMElement title = (DOMElement) head.selectSingleNode("//title");
        if (title == null) {
            title = (DOMElement) head.addElement("title");
        }
        title.setText(set);
    }

    public static void setEncoding() {
        setEncoding("UTF-8");
    }

    private static void setEncoding(String encoding) {
        DOMElement head = getHTMLHeader();
        DOMElement meta = (DOMElement) head.selectSingleNode("//meta");
        if (meta != null) return;
        meta = (DOMElement) head.addElement("meta");
        meta.setAttribute("content", "text/html; charset=" + encoding);
        meta.setAttribute("http-equiv", "content-type");
    }

    public static void appendLinkToTop(File file, String name, String id) {
        appendLink(getTopElement(true), file, name, id);
    }

    public static void appendLinkToTop(File file, String name) {
        appendLink(getTopElement(false), file, name, null);
    }

    public static DOMElement getTopElement(boolean getFirst) {
        List nodes = getDocument().selectNodes("//strong[@class='" + TITLE_CLASS + "']");
        if (nodes.size() == 0) return null;
        return (DOMElement) (getFirst ? nodes.get(0) : nodes.get(nodes.size() - 1));
    }

    public static void saveDoc() {
        LocalServerUtils.saveDocument(doc, PSLogger.getMainLogsFile().getAbsolutePath());
    }

    private static DOMDocument getDocument() {
        if (doc != null) return doc;
        String file = PSLogger.getMainLogsFile().getAbsolutePath();
        doc = (DOMDocument) LocalServerUtils.loadDocument(file);
        return doc;
    }

    private static DOMElement createLink(DOMDocument doc, File file, String name, String id) {
        DOMElement link = null;
        if (id != null) {
            NodeList list = doc.getElementsByTagName("a");
            for (int i = 0; i < list.getLength(); i++) {
                DOMElement _link = (DOMElement) list.item(i);
                String _id = _link.getAttribute("id");
                if (id.equals(_id)) {
                    link = _link;
                    break;
                }
            }
        }
        if (link == null)
            link = (DOMElement) doc.createElement("a");
        String href = getRelativeWebPath(PSLogger.getLogsDir().getParentFile().getAbsolutePath(), file.getAbsolutePath());
        if (id != null) {
            link.setAttribute("id", id);
        }
        link.setAttribute("href", href);
        link.setText(name);
        return link;
    }

}
