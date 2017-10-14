package ps5.wbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.AssertionFailedError;

import org.apache.log4j.PropertyConfigurator;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ps5.services.state.Visit;
import ps5.wbs.beans.Property;
import ps5.wbs.beans.WBSBean;
import ps5.wbs.logic.handlers.VisitProxy;

import com.cinteractive.ps3.PSHome;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;

public class WBSBaseTest {

    private static final String DB_PASSWORD = "dbPassword";

    private static final String DB_URL = "dbUrl";

    private static final String DB_USER = "dbUser";

    private static final Logger LOGGER = LoggerFactory.getLogger(WBSBaseTest.class);

    private static final String OWNER_MAIL = "UNIT_TEST_OWNER@test.te";

    private static PSHome psHome;

    private static final String TZ = "tz";

    private static VisitProxy visitProxy;

    private static final String WEB_ROOT_DIRECTORY = "web";

    @BeforeClass
    public static void initContext() {
        if (!org.apache.log4j.Logger.getRootLogger().getAllAppenders().hasMoreElements()) {
            PropertyConfigurator.configure(createLoggerProperties());
        }
        psHome = WBSTestUtils.createPSHome(WEB_ROOT_DIRECTORY);
        assert psHome != null;
        InstallationContext context = InstallationContext.get("default", createContextConfig());
        assert context != null;
        User owner = WBSTestUtils.createUserIfAbsence(OWNER_MAIL, context, true);
        assert owner != null;
        visitProxy = WBSTestUtils.createVisitProxy(owner);
        assert visitProxy != null;
        assert visitProxy.getContext() != null;
        assert visitProxy.getPrincipal() != null;
        assert visitProxy.getVisit() != null;
        WBSTestHelper.init(visitProxy);
    }

    protected static void assertBean(WBSBean bean, String name) {
        assertEquals(bean.getName(), name);
    }

    protected static void assertBeans(List<WBSBean> beans, WBSBean... expected) {
        assertEquals(beans.size(), expected.length);
        for (int i = 0; i < beans.size(); ++i) {
            assertEquals(expected[i], beans.get(i));
        }
    }

    protected static void assertChildHierarchy(WBSBean root, String testHierarchy) {
        String levels[] = testHierarchy.split(",");
        try {
            int idx = assertChildHierarchy(root, levels, 0, 0);
            if (idx != levels.length) {
                throw new AssertionFailedError("Incorrect hierarchy");
            }
        } catch (AssertionFailedError e) {
            LOGGER.info("Expected:");
            for (String level : levels) {
                LOGGER.info(level);
            }
            LOGGER.info("");
            LOGGER.info("Was:");
            WBSTestHelper.printChildren(root, 0);
            throw e;
        }
    }

    protected static int assertChildHierarchy(WBSBean root, String[] levels, int curLev, int currentIdx) {
        int idx = currentIdx;
        Iterator<? extends WBSBean> it = root.getChildrenIterator();
        while (it.hasNext()) {
            if (idx > levels.length) {
                throw new AssertionFailedError("Incorrect hierarchy");
            }
            WBSBean bean = it.next();
            String level = levels[idx];
            int l = level.lastIndexOf(" ") + 1;
            String name = level.substring(l);
            if (!name.equals(bean.getName())) {
                throw new AssertionFailedError(String.format("Levels are different: exp: %s(%d), was %s(%d)", name,
                        Integer.valueOf(l), bean.getName(), Integer.valueOf(curLev)));
            }
            if (l != curLev) {
                throw new AssertionFailedError(String.format("Levels are different: exp: %s(%d), was %s(%d)", name,
                        Integer.valueOf(l), bean.getName(), Integer.valueOf(curLev)));
            }
            idx = assertChildHierarchy(bean, levels, curLev + 1, idx + 1);
        }
        return idx;
    }

    protected static void assertSequence(List<WBSBean> beans, int... expected) {
        assertEquals(beans.size(), expected.length);
        for (int i = 0; i < beans.size(); ++i) {
            assertEquals(expected[i], beans.get(i).getSequence());
            assertTrue(beans.get(i).isSaved(Property.SEQUENCE));
        }
    }

    protected static InstallationContext getContext() {
        return visitProxy.getContext();
    }

    protected static PSHome getPSHome() {
        return psHome;
    }

    protected static User getUser() {
        return visitProxy.getPrincipal();
    }

    protected static Visit getVisit() {
        return visitProxy.getVisit();
    }

    protected static VisitProxy getVisitProxy() {
        return visitProxy;
    }

    private static Document createContextConfig() {
        String dbUrl = System.getProperty(DB_URL) != null ? System.getProperty(DB_URL) : "2k8std.simbirsoft/vfedoskin_temp";
        String dbUser = System.getProperty(DB_USER) != null ? System.getProperty(DB_USER) : "ps";
        String dbPassword = System.getProperty(DB_PASSWORD) != null ? System.getProperty(DB_PASSWORD) : "smaster";
        String tz = System.getProperty(TZ) != null ? System.getProperty(TZ) : "Europe/Moscow";
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document document = impl.createDocument(null, null, null);
            Element contextElement = document.createElement("context");
            contextElement.setAttribute("disable-report-check", "true");
            contextElement.setAttribute("proxy", "default");
            document.appendChild(contextElement);
            Element nameElement = document.createElement("name");
            nameElement.setTextContent("default");
            contextElement.appendChild(nameElement);
            Element jdbcElement = document.createElement("jdbc");
            Element jdbcDriverElement = document.createElement("jdbc.driver");
            jdbcDriverElement.setTextContent("net.sourceforge.jtds.jdbc.Driver");
            jdbcElement.appendChild(jdbcDriverElement);
            Element jdbcUrlElement = document.createElement("jdbc.url");
            jdbcUrlElement.setTextContent("jdbc:jtds:sqlserver://" + dbUrl);
            jdbcElement.appendChild(jdbcUrlElement);
            Element prop1Element = document.createElement("jdbc.prop");
            Element prop1NameElement = document.createElement("jdbc.prop.name");
            prop1NameElement.setTextContent("pool.name");
            prop1Element.appendChild(prop1NameElement);
            Element prop1ValueElement = document.createElement("jdbc.prop.value");
            prop1ValueElement.setTextContent("main");
            prop1Element.appendChild(prop1ValueElement);
            jdbcElement.appendChild(prop1Element);
            Element prop2Element = document.createElement("jdbc.prop");
            Element prop2NameElement = document.createElement("jdbc.prop.name");
            prop2NameElement.setTextContent("user");
            prop2Element.appendChild(prop2NameElement);
            Element prop2ValueElement = document.createElement("jdbc.prop.value");
            prop2ValueElement.setTextContent(dbUser);
            prop2Element.appendChild(prop2ValueElement);
            jdbcElement.appendChild(prop2Element);
            Element prop3Element = document.createElement("jdbc.prop");
            Element prop3NameElement = document.createElement("jdbc.prop.name");
            prop3NameElement.setTextContent("password");
            prop3Element.appendChild(prop3NameElement);
            Element prop3ValueElement = document.createElement("jdbc.prop.value");
            prop3ValueElement.setTextContent(dbPassword);
            prop3Element.appendChild(prop3ValueElement);
            jdbcElement.appendChild(prop3Element);
            Element prop4Element = document.createElement("jdbc.prop");
            Element prop4NameElement = document.createElement("jdbc.prop.name");
            prop4NameElement.setTextContent("lock.millis");
            prop4Element.appendChild(prop4NameElement);
            Element prop4ValueElement = document.createElement("jdbc.prop.value");
            prop4ValueElement.setTextContent("300000");
            prop4Element.appendChild(prop4ValueElement);
            jdbcElement.appendChild(prop4Element);
            Element prop5Element = document.createElement("jdbc.prop");
            Element prop5NameElement = document.createElement("jdbc.prop.name");
            prop5NameElement.setTextContent("expire.millis");
            prop5Element.appendChild(prop5NameElement);
            Element prop5ValueElement = document.createElement("jdbc.prop.value");
            prop5ValueElement.setTextContent("10000000");
            prop5Element.appendChild(prop5ValueElement);
            jdbcElement.appendChild(prop5Element);
            Element prop6Element = document.createElement("jdbc.prop");
            Element prop6NameElement = document.createElement("jdbc.prop.name");
            prop6NameElement.setTextContent("wait.millis");
            prop6Element.appendChild(prop6NameElement);
            Element prop6ValueElement = document.createElement("jdbc.prop.value");
            prop6ValueElement.setTextContent("4000");
            prop6Element.appendChild(prop6ValueElement);
            jdbcElement.appendChild(prop6Element);
            Element prop7Element = document.createElement("jdbc.prop");
            Element prop7NameElement = document.createElement("jdbc.prop.name");
            prop7NameElement.setTextContent("connections.max");
            prop7Element.appendChild(prop7NameElement);
            Element prop7ValueElement = document.createElement("jdbc.prop.value");
            prop7ValueElement.setTextContent("20");
            prop7Element.appendChild(prop7ValueElement);
            jdbcElement.appendChild(prop7Element);
            Element prop8Element = document.createElement("jdbc.prop");
            Element prop8NameElement = document.createElement("jdbc.prop.name");
            prop8NameElement.setTextContent("transaction.isolation");
            prop8Element.appendChild(prop8NameElement);
            Element prop8ValueElement = document.createElement("jdbc.prop.value");
            prop8ValueElement.setTextContent("1");
            prop8Element.appendChild(prop8ValueElement);
            jdbcElement.appendChild(prop8Element);
            Element prop9Element = document.createElement("jdbc.prop");
            Element prop9NameElement = document.createElement("jdbc.prop.name");
            prop9NameElement.setTextContent("ping.query");
            prop9Element.appendChild(prop9NameElement);
            Element prop9ValueElement = document.createElement("jdbc.prop.value");
            prop9ValueElement.setTextContent("SELECT NULL FROM Version");
            prop9Element.appendChild(prop9ValueElement);
            jdbcElement.appendChild(prop9Element);
            Element prop10Element = document.createElement("jdbc.prop");
            Element prop10NameElement = document.createElement("jdbc.prop.name");
            prop10NameElement.setTextContent("ping.interval");
            prop10Element.appendChild(prop10NameElement);
            Element prop10ValueElement = document.createElement("jdbc.prop.value");
            prop10ValueElement.setTextContent("10000");
            prop10Element.appendChild(prop10ValueElement);
            jdbcElement.appendChild(prop10Element);
            Element prop11Element = document.createElement("jdbc.prop");
            Element prop11NameElement = document.createElement("jdbc.prop.name");
            prop11NameElement.setTextContent("socketKeepAlive");
            prop11Element.appendChild(prop11NameElement);
            Element prop11ValueElement = document.createElement("jdbc.prop.value");
            prop11ValueElement.setTextContent("true");
            prop11Element.appendChild(prop11ValueElement);
            jdbcElement.appendChild(prop11Element);
            Element jdbcBackupElement = document.createElement("jdbc.backup");
            jdbcBackupElement.setAttribute("backup", "false");
            jdbcBackupElement.setAttribute("upgrade", "true");
            jdbcBackupElement.setAttribute("rebuild-startup-schema-objects", "false");
            jdbcElement.appendChild(jdbcBackupElement);
            contextElement.appendChild(jdbcElement);
            Element urlElement = document.createElement("url");
            contextElement.appendChild(urlElement);
            Element smtpElement = document.createElement("smtp");
            smtpElement.setAttribute("relay", "false");
            smtpElement.setAttribute("spoofing", "false");
            smtpElement.setAttribute("dumpMode", "false");
            Element emailElement = document.createElement("email");
            Element addressElement = document.createElement("address");
            addressElement.setTextContent("spam@gmail.com");
            emailElement.appendChild(addressElement);
            Element titleElement = document.createElement("title");
            titleElement.setTextContent("ps");
            emailElement.appendChild(titleElement);
            smtpElement.appendChild(emailElement);
            contextElement.appendChild(smtpElement);
            Element companyElement = document.createElement("company");
            Element cNameElement = document.createElement("name");
            cNameElement.setTextContent("ps");
            companyElement.appendChild(cNameElement);
            contextElement.appendChild(companyElement);
            Element productElement = document.createElement("product");
            Element pNameElement = document.createElement("name");
            pNameElement.setTextContent("ps");
            productElement.appendChild(pNameElement);
            contextElement.appendChild(productElement);
            Element alertsElement = document.createElement("alerts");
            alertsElement.setAttribute("synchronous", "true");
            contextElement.appendChild(alertsElement);
            Element contactElement = document.createElement("contact");
            Element helpdeskElement = document.createElement("helpdesk");
            helpdeskElement.setAttribute("suppress_popup", "false");
            helpdeskElement.appendChild(emailElement.cloneNode(true));
            Element phoneElement = document.createElement("phone");
            phoneElement.setTextContent("0");
            helpdeskElement.appendChild(phoneElement);
            contactElement.appendChild(helpdeskElement);
            Element accessElement = document.createElement("access");
            accessElement.appendChild(emailElement.cloneNode(true));
            accessElement.appendChild(phoneElement.cloneNode(true));
            contactElement.appendChild(accessElement);
            Element suggestionsElement = document.createElement("suggestions");
            suggestionsElement.appendChild(emailElement.cloneNode(true));
            contactElement.appendChild(suggestionsElement);
            Element demonstrationElement = document.createElement("demonstration");
            demonstrationElement.appendChild(emailElement.cloneNode(true));
            contactElement.appendChild(demonstrationElement);
            Element bugreportElement = document.createElement("bugreport");
            bugreportElement.appendChild(emailElement.cloneNode(true));
            contactElement.appendChild(bugreportElement);
            contextElement.appendChild(contactElement);
            Element uixElement = document.createElement("uix");
            Element categoriesElement = document.createElement("categories");
            uixElement.appendChild(categoriesElement);
            contextElement.appendChild(uixElement);
            Element permissionsElement = document.createElement("permissions");
            permissionsElement.setAttribute("classic", "true");
            permissionsElement.setAttribute("cache-on-login", "false");
            contextElement.appendChild(permissionsElement);
            Element timezoneElement = document.createElement("timezone");
            timezoneElement.setTextContent(tz);
            contextElement.appendChild(timezoneElement);
            Element documentsElement = document.createElement("documents_storage");
            documentsElement.setAttribute("max_bytes", "30485760");
            documentsElement.setAttribute("max_bytes_db", "30485760");
            documentsElement.setAttribute("buffer_bytes", "1024");
            contextElement.appendChild(documentsElement);
            Element itemsElement = document.createElement("item_per_page");
            itemsElement.setTextContent("30");
            contextElement.appendChild(itemsElement);
            Element threadsElement = document.createElement("threads_per_page");
            threadsElement.setTextContent("25");
            contextElement.appendChild(threadsElement);
            Element agentsElement = document.createElement("agents");
            agentsElement.setAttribute("install", "false");
            contextElement.appendChild(agentsElement);
            Element alertsPPElement = document.createElement("alert_per_page");
            alertsPPElement.setTextContent("40");
            contextElement.appendChild(alertsPPElement);
            Element alertsPWElement = document.createElement("alert_per_workspace");
            alertsPWElement.setTextContent("5");
            contextElement.appendChild(alertsPWElement);
            Element configurables = document.createElement("configurables");
            contextElement.appendChild(configurables);
            Element mime = document.createElement("mime_mappings");
            contextElement.appendChild(mime);
            Element dashboard = document.createElement("dashboard");
            Element tagword = document.createElement("tagword");
            dashboard.appendChild(tagword);
            contextElement.appendChild(dashboard);
            Element header = document.createElement("header");
            contextElement.appendChild(header);
            Element gantt = document.createElement("gantt");
            Element ganttversion = document.createElement("ganttversion");
            ganttversion.setTextContent("2");
            gantt.appendChild(ganttversion);
            Element initload = document.createElement("initload");
            initload.setTextContent("30");
            gantt.appendChild(initload);
            contextElement.appendChild(gantt);
            Element number = document.createElement("numberscales");
            contextElement.appendChild(number);
            Element session = document.createElement("session_timeout_options");
            contextElement.appendChild(session);
            Element signon = document.createElement("single_sign_on");
            signon.setAttribute("enabled", "false");
            contextElement.appendChild(signon);
            Element ldap = document.createElement("ldap");
            ldap.setAttribute("enabled", "false");
            contextElement.appendChild(ldap);
            Element ping = document.createElement("ping_federate");
            ping.setAttribute("enabled", "false");
            contextElement.appendChild(ping);
            Element expand = document.createElement("expand");
            contextElement.appendChild(expand);
            Element metrics = document.createElement("metrics");
            Element detailLines = document.createElement("detail_lines");
            detailLines.setAttribute("enabled", "false");
            metrics.appendChild(detailLines);
            contextElement.appendChild(metrics);
            Element calendar = document.createElement("master_calendar");
            contextElement.appendChild(calendar);
            Element security = document.createElement("security");
            Element access = document.createElement("multiple_access");
            access.setAttribute("control", "none");
            access.setAttribute("deny_default_user", "false");
            security.appendChild(access);
            Element username = document.createElement("username");
            username.setAttribute("min_length", "1");
            security.appendChild(username);
            contextElement.appendChild(security);
            Element emailCharset = document.createElement("emailcharset");
            contextElement.appendChild(emailCharset);
            Element viewHierarchy = document.createElement("view_hierarchy");
            contextElement.appendChild(viewHierarchy);
            Element searchWizard = document.createElement("search_wizard");
            contextElement.appendChild(searchWizard);
            Element locking = document.createElement("locking");
            locking.setAttribute("enabled", "true");
            contextElement.appendChild(locking);
            Element cacheConfig = document.createElement("cache_config");
            cacheConfig.setAttribute("factory", "com.cinteractive.ps3.cache.VanillaObjectCache$Factory");
            Element storeConfig = document.createElement("store_config");
            storeConfig.setAttribute("factory", "com.cinteractive.ps3.cache.MapStore$Factory");
            cacheConfig.appendChild(storeConfig);
            contextElement.appendChild(cacheConfig);
            Element errorCapture = document.createElement("error_capture");
            errorCapture.setAttribute("default_retirement_age", "45");
            contextElement.appendChild(errorCapture);
            Element refererCheck = document.createElement("referer_check");
            contextElement.appendChild(refererCheck);
            Element lucene = document.createElement("lucene_basic");
            Element luceneProperties = document.createElement("lucene_properties");
            luceneProperties.setAttribute("enabled", "false");
            luceneProperties.setAttribute("max_results", "1000");
            luceneProperties.setAttribute("update_interval_unit", "SECONDS");
            luceneProperties.setAttribute("regular_update_interval", "30");
            luceneProperties.setAttribute("start_update_interval", "30");
            luceneProperties.setAttribute("max_writer_lock_wait_time", "2000");
            luceneProperties.setAttribute("batch_size", "100");
            luceneProperties.setAttribute("merge_factor", "10");
            luceneProperties.setAttribute("lucene_version", "LUCENE_30");
            luceneProperties.setAttribute("index_large_objects", "false");
            Element include = document.createElement("include");
            include.setTextContent("*");
            luceneProperties.appendChild(include);
            Element exclude = document.createElement("exclude");
            exclude.setTextContent("*");
            luceneProperties.appendChild(exclude);
            lucene.appendChild(luceneProperties);
            contextElement.appendChild(lucene);
            Element luceneDocs = document.createElement("lucene_document_content");
            luceneDocs.appendChild(luceneProperties.cloneNode(true));
            contextElement.appendChild(luceneDocs);
            return document;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Properties createLoggerProperties() {
        Properties p = new Properties();
        p.setProperty("log4j.logger.com.cinteractive.ps3.session.PasswordSecurity", "ERROR");
        p.setProperty("log4j.logger.com.cinteractive.ps3.servlets.RedirectFilter", "WARN");
        p.setProperty("log4j.logger.com.cinteractive.concurrent.SlowClock", "WARN");
        p.setProperty("log4j.logger.org.apache.tapestry.ApplicationServlet", "WARN");
        p.setProperty("log4j.logger.tapestry.page.ComponentTemplateLoader", "ERROR");
        p.setProperty("log4j.logger.com.cinteractive.ps3.jdbc.peers.InstallationContextPeer", "WARN");
        p.setProperty("log4j.logger.ResCache", "ERROR");
        p.setProperty("log4j.logger.com.cinteractive.ps3.search.lucene.index.LuceneIndexServiceImpl", "WARN");
        p.setProperty("log4j.logger.com.cinteractive.ps3.reports.scheduler.ReportHelper", "OFF");
        p.setProperty("log4j.logger.com.cinteractive.javamail.CIMimeMailDaemon", "OFF");
        p.setProperty("log4j.logger.com.cinteractive.ps3.agents.AgentScheduler", "WARN");
        p.setProperty("log4j.logger.com.cinteractive.ps3.alerts.AlertGenerator", "WARN");
        p.setProperty("log4j.logger.com.cinteractive.ps3.entities.User", "ERROR");
        p.setProperty("log4j.logger.ps5.pages.admin.WorkTemplates", "DEBUG");
        p.setProperty("log4j.logger.ps5.pages.admin.CreateWorkTemplate", "DEBUG");
        p.setProperty("log4j.logger.org.apache.tapestry.engine.AbstractEngine", "DEBUG");
        p.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        p.setProperty("log4j.appender.stdout.Target", "System.out");
        p.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        p.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{ABSOLUTE}] {%t} %-5p (%F:%L) - %m%n");
        p.setProperty("log4j.rootLogger", "info, stdout");
        return p;
    }
}