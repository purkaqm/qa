package com.powersteeringsoftware.mail;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.logger.PSResults;
import com.powersteeringsoftware.libs.util.StrUtil;
import com.powersteeringsoftware.libs.util.servers.LocalServerUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.HTMLWriter;
import org.dom4j.tree.DefaultElement;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static com.powersteeringsoftware.mail.PSResultProperties.*;


/**
 * Return formatted string for TestResultReporter object. You can get single line string
 * or string with several lines.
 */
public class PSResultFormatter {

    private static final String LINE_SEPARATOR = "\n";
    private static final String STRING_SEPARATOR = ";";
    private static final String HTML_SEPARATOR = "\n<br/>";
    private static final int TABLE_COLUMN_WIDTH = 20;

    private static String simpleFormatMailBody(String title) {
        StringBuilder res = appendGeneralInfo(title);

        List<List> knises = TEST_KNOWN_ISSUE.getListListValues();
        if (!knises.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < knises.size(); i++) {
                sb.append(knises.get(i).get(1));//knis id
                if (i != knises.size() - 1)
                    sb.append(", ");
            }
            append(res, "Known bugs", sb.toString());
        }

        append(res, "All Tests", TEST_ALL_TESTS_INFO.getListValues().toString());
        append(res, "Failed Test-cases", TEST_FAILED_TEST_CASES_INFO);
        append(res, "Full Logs", getUrl(TEST_LOGS_FILE));
        append(res, "TestNG Logs", getUrl(TEST_LOGS_TEST_NG_FILE));

        return res.toString();
    }

    private static String singleStringFormatMailBodyIn(String title) {
        return simpleFormatMailBody(title).replaceAll(LINE_SEPARATOR, STRING_SEPARATOR);
    }

    private static String htmlFormatMailBody(String title) {
        StringBuilder res = new StringBuilder("<html><body>");
        res.append(generateHtmlTop(title));
        res.append(generateHtmlBody());
        res.append("</body></html>");
        return res.toString();
    }

    private static String shortHtmlFormatMailBody(String title) {
        StringBuilder res = new StringBuilder("<html><body>");
        res.append(generateHtmlTop(title));
        res.append(generateHtmlBody(PSResults.SKIP.toString(), PSResults.OK.toString()));
        res.append("</body></html>");
        return res.toString();
    }

    private static String generateHtmlTop(String title) {
        StringBuilder res = appendGeneralInfo(title);

        appendKnises(res);

        append(res, "Hudson job", getLink(getBseUrl(),
                StrUtil.urlToString(ENVIRONMENT_TEST_URL_JOB_NAME.getValue() + ", " + ENVIRONMENT_TEST_URL_JOB_ID.getValue())).toLowerCase());
        append(res, null, getLink(ENVIRONMENT_TESTS_LIST.getValue(), "All tests list"));
        append(res, null, getLink(getUrl(TEST_LOGS_FILE), "Full Logs: "));
        res.append(HTML_SEPARATOR);
        return StrUtil.toUTF8(res.toString());
    }

    private static String loadingHtmlFormatMailBody(String title) {
        StringBuilder res = new StringBuilder("<html><body>");
        res.append(appendGeneralInfo(title));

        appendKnises(res);

        append(res, "Hudson job", getLink(getBseUrl(),
                StrUtil.urlToString(ENVIRONMENT_TEST_URL_JOB_NAME.getValue() + ", " + ENVIRONMENT_TEST_URL_JOB_ID.getValue())).toLowerCase());
        append(res, null, getLink(ENVIRONMENT_TESTS_LIST.getValue(), "All tests list"));
        append(res, null, getLink(getUrl(TEST_LOGS_FILE), "Full Logs: "));
        res.append(HTML_SEPARATOR);

        if (TEST_LOADING_REFRESH_TIMEOUT.getValue() != null && !TEST_LOADING_REFRESH_TIMEOUT.getValue().isEmpty())
            append(res, "Timeout for refreshing", TEST_LOADING_REFRESH_TIMEOUT);
        appendTable(res, null, BODY_TEST_LOADING_HEADER.getListValues(), BODY_TEST_LOADING_TABLE.getListListValues());

        res.append("</body></html>");
        return StrUtil.toUTF8(res.toString());
    }


    private static void appendKnises(StringBuilder res) {
        appendTable(res, "Known bugs:", null, TEST_KNOWN_ISSUE.getListListValues());
    }

    /**
     * @param res    builder
     * @param title  String
     * @param header List
     * @param table  List of List with Strings or Arrays
     */
    private static void appendTable(StringBuilder res, String title, List header, List<List> table) {
        if (table.isEmpty()) return;
        if (title != null && !title.isEmpty()) {
            res.append("<strong>" + title + "</strong>");
            res.append(HTML_SEPARATOR);
        }

        res.append("<table><tbody>");
        if (header != null) {
            res.append("<tr>");
            for (Object cell : header) {
                StringBuilder txt = new StringBuilder(cell.toString());
                while (txt.length() < TABLE_COLUMN_WIDTH) {
                    txt.append(" ");
                }
                res.append("<th align='left' style='font-family: \"Courier New\", Courier, monospace;'>").
                        append(StrUtil.stringToHtml(txt.toString())).append("</th>");
            }
            res.append("</tr>");
        }
        for (List row : table) {
            res.append("<tr>");
            for (Object cell : row) {
                if (cell instanceof Object[] && ((Object[]) cell).length != 0) {
                    Object[] content = (Object[]) cell;
                    if (content.length > 1) {
                        String url = String.valueOf(content[0]);
                        if (url.startsWith("http")) {
                            res.append("<td style='font-family: \"Courier New\", Courier, monospace;'>");
                            res.append(getLink(url, String.valueOf(content[1])));
                            res.append("</td>");
                        } else {
                            StringBuilder sb = new StringBuilder(url);
                            for (int i = 1; i < content.length; i++) {
                                sb.append(" ").append(content[i]);
                            }
                            res.append("<td>").append(sb.toString().trim()).append("</td>");
                        }
                    } else if (content.length > 0) {
                        res.append("<td>").append(content[0]).append("</td>");
                    }
                } else {
                    res.append("<td>").append(cell).append("</td>");
                }
            }
            res.append("</tr>");
        }
        res.append("</tbody></table>");
    }

    private static String generateHtmlBody(String... statusesToSkip) {
        File html = TEST_LOGS_FILE.getFile();
        if (!html.exists()) return "<br>Can't find main log file: " + html + "</br>";
        PSLogger.debug("File to parse mail body : " + html.getAbsolutePath());
        String urlPrefix = getUrl(html.getParentFile()) + "/";
        StringBuilder res = new StringBuilder();
        Document result = LocalServerUtils.loadDocument(html.getAbsolutePath());
        Node style = result.selectSingleNode("//style");
        if (style != null) {
            res.append(style.asXML());
        }
        StringBuilder toAdd = null;
        for (Object oTr : result.selectNodes("//table//tr")) {
            DefaultElement tr = (DefaultElement) oTr;
            for (Object oTd : tr.selectNodes("./td/a")) {
                DefaultElement td = (DefaultElement) oTd;
                Attribute href;
                if ((href = td.attribute("href")) != null && href.getValue().startsWith("./")) {
                    href.setValue(urlPrefix + href.getValue().replace("./", ""));
                }
            }
            if (tr.selectSingleNode("./td[2]") == null) continue;
            Node third;
            if ((third = tr.selectSingleNode("./td[3]")) == null) {
                if (toAdd != null) {
                    res.append(toAdd).append("</tbody>\n</table>\n");
                    toAdd = null;
                }
                String tmp = tr.getParent().getParent().asXML();
                PSLogger.debug(tmp);
                res.append(tmp).append("\n");
                continue;
            }
            if (toAdd == null) {
                toAdd = new StringBuilder("<table width='100%'>\n<tbody>\n");
            }
            String status = third.getText();
            if (!Arrays.asList(statusesToSkip).contains(status)) {
                StringWriter sw = new StringWriter();
                HTMLWriter hw = LocalServerUtils.getHTMLWriter(sw);
                try {
                    hw.write(tr);
                    hw.flush();
                    toAdd.append(sw.getBuffer()).append("\n");
                    sw.close();
                    hw.close();
                } catch (IOException e) {
                    PSLogger.fatal(e);
                }
                //toAdd.append(tr.asXML().replace("\n", PSHTMLLayoutPerTest.WORD_SEPARATOR)).append("\n");
            }
        }
        if (toAdd != null) {
            res.append(toAdd).append("</tbody>\n</table>\n");
        }
        return StrUtil.toUTF8(res.toString());
    }

    private static StringBuilder appendGeneralInfo(String prefix) {
        StringBuilder res = new StringBuilder();
        if (!getType().isLoading())
            append(res, null, (prefix != null ? prefix : "") + BODY_TEST_NAME.getValue() + " (PS ver." + ENVIRONMENT_PS_VERSION_SHORT.getValue() + ", " + ENVIRONMENT_CLIENT_BROWSER_BRAND.getValue() + "):");
        if (!getType().isHtmlType())
            append(res, "Status", Boolean.parseBoolean(TEST_RESULT.getValue()) ? BODY_TEST_RESULT_OK : BODY_TEST_RESULT_FAILED);
        append(res, "Version", ENVIRONMENT_PS_VERSION);
        if (!getType().isLoading())
            append(res, "Browser", ENVIRONMENT_CLIENT_BROWSER);
        append(res, "Start Time", toDate(TEST_START_LONG));
        append(res, "Duration", getDuration());
        if (!getType().isHtmlType())
            append(res, "Server", getServerInfo());
        else
            append(res, "Server", getLink(getServerInfo(), null));
        append(res, "Client", getClientInfo());
        append(res, "Selenium", ENVIRONMENT_TEST_CORE_VERSION);
        append(res, null, TEST_SHORT_DESCRIPTION);
        return res;
    }

    private static void append(StringBuilder builder, String key, String value) {
        if (PSResultProperties.getType().isHtmlType()) {
            builder.append("<strong>").append(key != null ? key + ": " : "").append(value).append("</strong>").append(HTML_SEPARATOR);
        } else {
            if (key != null)
                builder.append(key + ": ");
            builder.append(value).append(LINE_SEPARATOR);
        }
    }

    private static void append(StringBuilder builder, String key, PSResultProperties value) {
        append(builder, key, value.getValue());
    }

    private static String toDate(PSResultProperties prop) {
        return new SimpleDateFormat(PSResultProperties.BODY_DATE_FORMAT.getValue()).format(Long.parseLong(prop.getValue()));
    }

    private static String getServerInfo() {
        return ENVIRONMENT_SERVER_URL.getValue() + ENVIRONMENT_SERVER_CONTEXT.getValue();
    }

    private static String getClientInfo() {
        return ENVIRONMENT_CLIENT_HOST.getValue() + ", " + ENVIRONMENT_CLIENT_USER.getValue();
    }

    private static String getDuration() {
        Long e = Long.parseLong(TEST_END_LONG.getValue());
        Long s = Long.parseLong(TEST_START_LONG.getValue());
        float d = (float) (e - s) / 1000 / 60 / 60;
        NumberFormat nf = NumberFormat.getInstance(Locale.ROOT);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(d) + "h";
    }

    private static String getUrl(PSResultProperties prop) {
        return getUrl(prop.getFile());
    }

    private static String getUrl(File path) {
        try {
            return StrUtil.replaceUrlSlashes(getBseUrl() + "/" +
                    path.getCanonicalPath().replace(TEST_ROOT_LOGS_DIR.getFile().getParentFile().getCanonicalPath(), ""));
        } catch (IOException e) {
            PSLogger.fatal(e);
            return null;
        }
    }

    private static String getLink(String href, String name) {
        if (name == null || name.isEmpty()) name = href;
        return "<a href='" + href + "'>" + StrUtil.stringToHtml(name) + "</a>";
    }

    private static String getBseUrl() {
        return StrUtil.replaceUrlSlashes(ENVIRONMENT_TEST_URL_BASE.getValue() + "/" +
                ENVIRONMENT_TEST_URL_JOB_NAME.getValue() + "/" +
                ENVIRONMENT_TEST_URL_JOB_ID.getValue() + "/" +
                ENVIRONMENT_TEST_URL.getValue());
    }


    /**
     * Create formated string using test properties file
     *
     * @param str - first Line
     * @return
     */
    public static String formatMailBody(String str) {
        switch (PSResultProperties.getType()) {
            case SIMPLE:
                return simpleFormatMailBody(str);
            case SINGLE_STRING:
                return singleStringFormatMailBodyIn(str);
            case HTML:
                return htmlFormatMailBody(str);
            case SHORT_HTML:
                return shortHtmlFormatMailBody(str);
            case LOADING:
                return loadingHtmlFormatMailBody(str);
            default:
                return simpleFormatMailBody(str);
        }
    }

    public static String formatMailBody() {
        return formatMailBody("");
    }
}
