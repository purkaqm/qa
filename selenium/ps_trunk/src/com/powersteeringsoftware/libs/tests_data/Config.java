package com.powersteeringsoftware.libs.tests_data;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 12.05.2010
 * Time: 13:43:55
 * To change this template use File | Settings | File Templates.
 */
public class Config implements Cloneable {

    private static Config conf;
    private Element element;
    private static final String XPATH_ATTR = "xpath";


    private File confFile;

    private Config(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        confFile = file;
        element = builder.parse(file).getDocumentElement();
    }

    private Config(Element element) {
        this.element = element;
        this.element.normalize();
        try {
            if (element.getBaseURI() != null)
                confFile = new File(new URI(element.getBaseURI()));
        } catch (URISyntaxException e) {
            // exc
        }
    }

    /**
     * Create new config with specified parent name
     *
     * @param rootNodeName
     */
    private Config(String rootNodeName) {
        DOMImplementation domImpl = new DOMImplementationImpl();
        Document document = domImpl.createDocument(null, rootNodeName, null);
        this.element = document.getDocumentElement();
        this.element.normalize();
    }


    /**
     * Return org.w3c.dom.Element document.
     *
     * @return org.w3c.dom.Element document.
     */
    public final Element getElement() {
        return element;
    }

    public Config getParent() {
        if (element.getParentNode() == null || !(element.getParentNode() instanceof Element)) return null;
        return new Config((Element) element.getParentNode());
    }

    /**
     * @param file - full path to config
     * @return - Config Object
     */
    public static Config getTestConfig(String file, boolean doExit) {
        try {
            File f = getTestFile(file);
            if (f == null) {
                if (doExit) throw new FileNotFoundException("Can't find file " + file);
            }
            return new Config(f);
        } catch (Exception e) {
            if (doExit) {
                PSLogger.fatal(e);
                System.exit(-1);
            } else {
                PSLogger.warn(e);
            }
        }
        return null;
    }

    public static Config getTestConfig(String file) {
        return getTestConfig(file, true);
    }

    public static File getTestFile(String file) {
        CoreProperties.loadProperties();
        if (file.contains("\\") || file.contains("/")) { // if path specified.
            file = (file.matches("^[A-Za-z]:.*$") ? "" : CoreProperties.getServerFolder() + "/") + file;
        }
        File f = new File(file);
        return f.exists() ? f : null;
    }

    /**
     * @param name
     * @return
     */
    public synchronized List<Config> getChsByName(String name) {
        for (int i = 0; i < 10; i++) {
            try {
                return _getChsByName(name);
            } catch (Exception e) {
                // todo: has npe when concurrent working. investigate and fix.
                // todo: see http://netbeans.org/bugzilla/show_bug.cgi?id=156076
                // todo: see https://issues.apache.org/jira/browse/AXIS2-4517
                PSLogger.fatal("Config.getChsByName: " + e.getMessage());
                try {
                    Thread.sleep(330);
                } catch (InterruptedException e1) {
                    // ignore
                }
            }
        }
        return null;
    }

    private List<Config> _getChsByName(String name) {
        NodeList nodes = element.getChildNodes();
        ArrayList<Config> result = new ArrayList<Config>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equalsIgnoreCase(name)) {
                    result.add(new Config((Element) node));
                }
            }
        }
        return result;
    }

    public List<Config> findChsByName(String name) {
        List<Config> res = new ArrayList<Config>();
        for (Config conf : getChsByName(name)) {
            res.add(conf);
        }
        for (Config conf : getChilds()) {
            res.addAll(conf.findChsByName(name));
        }
        return res;
    }


    /**
     * @return
     */
    public List<Config> getChilds() {
        NodeList nodes = element.getChildNodes();
        ArrayList<Config> result = new ArrayList<Config>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result.add(new Config((Element) node));
            }
        }
        return result;
    }


    /**
     * Child with correct name.
     *
     * @param name child name
     * @return child
     */
    public synchronized Config getChByName(String name) {
        List<Config> chs = getChsByName(name);
        if (chs.size() > 0) {
            return chs.get(0);
        } else {
            return null;
        }
    }

    public boolean hasChild(String name) {
        return getChsByName(name).size() > 0;
    }

    /**
     * Get text value for element.
     *
     * @return text value
     */
    public String getText() {
        if (hasXpathAttr()) {
            Config c = getChildByXPath(getXpathAttr());
            return c != null ? c.getText() : "";
        }
        return getTrimText();
    }

    public String getTrimText() {
        return getNotTrimText().trim().replace("\n", "");
    }

    public boolean hasXpathAttr() {
        return getAttributeValue(XPATH_ATTR) != null;
    }

    public void removeXpathAttr() {
        removeAttribute(XPATH_ATTR);
    }

    public String getXpathAttr() {
        return getAttributeValue(XPATH_ATTR);
    }

    public String getNotTrimText() {
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.TEXT_NODE) {
                return ((Text) node).getData().replace("\\n", "\n");
            }
        }
        return "";
    }

    public void replace(Map<String, String> replacement) {
        if (replacement.isEmpty()) return;
        String txt = getText();
        if (txt != null && !txt.isEmpty() && txt.matches(".*\\{[^\\}]*\\}.*")) {
            for (String key : replacement.keySet()) {
                txt = txt.replace("{" + key + "}", replacement.get(key));
                setText(txt);
            }
        }
        for (Config conf : getChilds()) {
            conf.replace(replacement);
        }
    }

    public void replace(String... rep) {
        Map<String, String> toRep = new HashMap<String, String>();
        for (String r : rep) {
            toRep.put(r.replaceAll("=\\>.*", ""), r.replaceAll(".*=\\>", ""));
        }
        replace(toRep);
    }

    public String getText(String node) {
        Config c = getChByName(node);
        if (c == null)
            return null;
        return c.getText();
    }

    /**
     * getChildByXPath
     *
     * @param locator
     * @return
     */
    public Config getChildByXPath(String locator) {
        try {
            XPathFactory factory = XPathFactory.newInstance();
            XPath xpath = factory.newXPath();
            XPathExpression expr = xpath.compile(locator);
            Object result = expr.evaluate(element, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            if (nodes.getLength() == 0) return null;
            else return new Config((Element) nodes.item(0));
        } catch (Exception e) {
            PSLogger.error("Can't find child by xpath " + locator + " in config " + (confFile != null ? confFile.getAbsolutePath() : "null"));
            return null;
        }
    }

    public Config findChildByXpath() {
        if (!hasXpathAttr()) return null;
        return getChildByXPath(getXpathAttr());
    }

    /**
     * @param attr
     * @return empty string if there is not such attribute
     */
    public String getAttribute(String attr) {
        String res = getAttributeValue(attr);
        return res == null ? "" : res;
    }

    public String getID() {
        return getAttribute("id");
    }

    public void setID(String id) {
        setAttributeValue("id", id);
    }

    public boolean hasAttribute(String attr) {
        return getAttributeValue(attr) != null;
    }

    public Map getAttributes() {
        Map<String, String> res = new TreeMap<String, String>();
        NamedNodeMap atts = element.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            res.put(atts.item(i).getNodeName(), atts.item(i).getNodeValue());
        }
        return res;
    }


    public String getName() {
        return element.getTagName();
    }


    /**
     * @param name
     * @param attrValue
     * @param attrText
     * @return
     */
    public Config getElementByTagAndAttribute(String name, String attrValue, String attrText) {
        for (Config conf : getChsByName(name)) {
            String atv = conf.getAttribute(attrValue);
            if (atv != null && atv.equals(attrText)) return conf;
        }
        return null;
    }

    public Config getElementById(String id) {
        for (Config conf : getChilds()) {
            String atv = conf.getAttribute("id");
            if (atv != null && atv.equals(id)) return conf;
        }
        return null;
    }

    public Config getElementByIdAndName(String name, String id) {
        return getElementByTagAndAttribute(name, "id", id);
    }

    public Config findElementById(String id) {
        for (Config conf : getChilds()) {
            String atv = conf.getAttribute("id");
            if (atv != null && atv.equals(id)) return conf;
        }
        for (Config conf : getChilds()) {
            Config res = conf.findElementById(id);
            if (res != null) return res;
        }
        return null;
    }


    public Config getElementByInnerText(String text) {
        for (Config conf : getChilds()) {
            String atv = conf.getText();
            if (atv != null && atv.equals(text)) return conf;
        }
        return null;
    }


    /**
     * Add one element node to config. Doesn't check if there is element with the same name.
     *
     * @param name element name
     */
    public Config addElement(String name) {
        Document document = getElement().getOwnerDocument();
        Element newel = document.createElement(name);
        element.appendChild(newel);
        return new Config(newel);
    }

    /**
     * Add specified config
     *
     * @param child child config
     */
    public Config addChild(Config child) {
        Node node = element.getOwnerDocument().importNode(child.getElement(), true);
        element.appendChild(node);
        return new Config((Element) node);
    }

    public void addConfig(Config config) {
        for (Config c : config.getChilds()) {
            addChild(c);
        }
    }

    public File getLocation() {
        return confFile;
    }

    /**
     * Add one element node to config. If there is element with the same name it will be replaced.
     *
     * @param name element name
     */
    public void setElement(String name) {
        if (getChByName(name) != null) {
            Element oldel = getChByName(name).getElement();
            element.removeChild(oldel);
        }
        addElement(name);
    }

    /**
     * Save config to xml file
     *
     * @param file
     * @throws javax.xml.transform.TransformerException
     *
     */
    private void _save(String file) {
        File tmpfile = new File(file);
        if (tmpfile.exists()) {
            tmpfile.delete();
        }
        try {
            OutputStream out = new FileOutputStream(file);
            Document doc = getElement().getOwnerDocument();
            DOMSource source = new DOMSource(doc);
            TransformerFactory transFactory = TransformerFactory.newInstance();
            Transformer transformer = transFactory.newTransformer();
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
            out.close();
        } catch (Exception e) {
            PSLogger.fatal(e);
        }
    }

    public void save(String file) {
        PSLogger.info("Save config " + this.confFile + " to file " + file);
        _save(file);
    }

    public void save() {
        PSLogger.info("Save config " + this.confFile);
        _save(confFile.getAbsolutePath());
    }

    /**
     * Add text node to config, if there is text it will be replaced.
     *
     * @param text value
     */
    public void setText(String text) {
        if (text == null) {
            PSLogger.error("Trying to insert null into config");
            return;
        }
        Document document = getElement().getOwnerDocument();
        NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            element.removeChild(node);
        }
        element.appendChild(document.createTextNode(text));
    }

    /**
     * Add text node to Config or change text for node in case of node already exists
     *
     * @param node node, for which text will be set
     * @param text text which will be set in text node
     */
    public void setText(String node, String text) {
        if (text == null) {
            PSLogger.error("Trying to insert null into config");
            return;
        }
        if (getChByName(node) == null) {
            Document document = getElement().getOwnerDocument();
            Element el = document.createElement(node);
            getElement().appendChild(el);
        }
        getChByName(node).setText(text);
    }

    /**
     * @param name
     * @return attridute value if such attribute presents; otherwise null
     */
    public final String getAttributeValue(final String name) {
        if (getElement().getNodeType() == Node.ELEMENT_NODE) {
            if (getElement().getAttributeNode(name) != null) {
                return getElement().getAttribute(name);
            }
        }
        return null;
    }

    /**
     * set attribute value
     *
     * @param name
     * @param value
     */
    public final void setAttributeValue(final String name, final String value) {
        if (getElement().getNodeType() == Node.ELEMENT_NODE) {
            getElement().setAttribute(name, value);
        }
    }

    public void removeAttribute(String name) {
        if (getElement().getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        getElement().removeAttribute(name);
    }

    public static Config createConfig(String name) {
        return new Config(name);
    }

    public synchronized Object clone() {
        Element el = (Element) element.cloneNode(true);
        try {
            element.getParentNode().appendChild(el);
        } catch (Exception e) {
            //ignore exception ...
        }
        return new Config(el);
    }

    public synchronized Config copy(boolean newConfig) throws ParserConfigurationException {
        Element e = (Element) element.cloneNode(true);
        if (newConfig) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.adoptNode(e);
        }
        return new Config(e);
    }

    public void replaceConfig(Config conf) {
        Node node = element.getParentNode();
        Node e;
        if (node != null) {
            e = node.appendChild(conf.getElement());
            node.removeChild(element);
        } else { // no parent node
            e = conf.getElement();
        }
        element = (Element) e;
    }

    public String toString() {
        StringBuffer res = new StringBuffer("<" + getName());
        Map<String, String> attrs = getAttributes();
        if (attrs != null && attrs.size() != 0) {
            for (String k : attrs.keySet()) {
                String txt = attrs.get(k);
                char quote = txt.contains("'") ? '\"' : '\'';
                res.append(" ").append(k).append("=").append(quote).append(txt).append(quote);
            }
        }
        res.append(">");
        for (Config c : getChilds()) {
            res.append("\n\t" + c.toString() + "\n");
        }
        res.append(getTrimText() + "</" + getName() + ">");
        return res.toString().replace("\n\n", "\n");
    }

    public void removeChild(String name) {
        Config toRemove = getChByName(name);
        if (toRemove != null)
            removeChild(toRemove);
    }

    public void removeChildren(String name) {
        while (hasChild(name)) {
            removeChild(name);
        }
    }

    public void removeChild(Config conf) {
        try {
            element.removeChild(conf.element);
        } catch (Exception ex) {
            // ignore
            PSLogger.error(ex.getMessage());
        }
    }


}
