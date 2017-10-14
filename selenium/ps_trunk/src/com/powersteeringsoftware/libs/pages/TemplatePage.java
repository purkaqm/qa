package com.powersteeringsoftware.libs.pages;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 26.09.13
 * Time: 15:14
 * To change this template use File | Settings | File Templates.
 */

import com.powersteeringsoftware.libs.elements.Element;
import com.powersteeringsoftware.libs.logger.PSLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.powersteeringsoftware.libs.enums.page_locators.TemplatePageLocators.*;

/**
 * Metrics>Template page
 */
public class TemplatePage extends PSPage {
    private String id;

    public TemplatePage(String id) {
        this.id = id;
    }

    protected void setId() {
        id = getUrlId();
    }

    @Override
    public void open() {
        if (id == null) throw new NullPointerException("Metric id is null");
        PSLogger.info("Open instance with id " + id);
        super.open(URL_ID.replace(id));
        PSLogger.save("After open page");
    }

    private List<GeneralInfo> info;

    public class GeneralInfo {
        private GeneralInfo() {
        }

        private String key;
        private String value;

        public String toString() {
            return key + "=" + value;
        }

        public String getName() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }

    public String getInfo(String name) {
        for (GeneralInfo i : getInfo()) {
            if (i.key.equalsIgnoreCase(name)) return i.value;
        }
        return "";
    }

    public List<GeneralInfo> getInfo() {
        if (info == null) {
            info = new ArrayList<GeneralInfo>();
            for (Element tr : getElements(false, TR)) {
                Element th = tr.getChildByXpath("//th");
                Element td = tr.getChildByXpath("//td");
                if (th == null || !th.isDEPresent()) continue;
                if (td == null || !td.isDEPresent()) continue;
                GeneralInfo i = new GeneralInfo();
                i.key = th.getDEText().replaceAll("\\s*:$", "");
                info.add(i);
                String txt = td.getDEText();
                if (!txt.isEmpty()) {
                    i.value = txt;
                } else {
                    for (Element e : Element.searchElementsByXpath(td, "//*")) {
                        txt = e.getDEText();
                        if (!txt.isEmpty()) {
                            i.value = txt;
                            break;
                        }
                    }
                }
            }
        }
        return info;
    }

    private List<Item> items;

    public class Item {
        private Map<String, String> map = new HashMap<String, String>();

        public String toString() {
            return map.toString();
        }

        public String get(String name) {
            for (String k : map.keySet()) {
                if (k.equalsIgnoreCase(name)) return map.get(k);
            }
            return "";
        }
    }

    public List<Item> getItems() {
        if (items == null) {
            items = new ArrayList<Item>();
            List<String> ths = new ArrayList<String>();
            Element table = getElement(false, ITEM_TABLE);
            for (Element th : Element.searchElementsByXpath(table, ITEM_TH)) {
                ths.add(th.getDEText());
            }
            List<String> all = new ArrayList<String>();
            for (Element td : Element.searchElementsByXpath(table, ITEM_TD)) {
                all.add(td.getDEText());
            }
            while (all.size() > 0) {
                Item i = new Item();
                items.add(i);
                for (String th : ths) {
                    i.map.put(th, all.remove(0));
                }
            }
        }
        return items;
    }
}
