package com.powersteeringsoftware.libs.objects.metrics;

import com.powersteeringsoftware.libs.objects.ConfigObject;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.Calculator;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.05.12
 * Time: 13:56
 * To change this template use File | Settings | File Templates.
 */
public class MetricTemplate extends ConfigObject {

    public static final String NAME = "metric";

    protected FrequencyType columnType;

    public enum FrequencyType {
        MONTHLY,
        QUARTERLY,
        MONTHLY_13,
        QUARTERLY_13,
        YEARLY,
        NO,
    }

    public enum DisplayTotal {
        NEVER("Never"),
        QUARTERLY("Quarterly"),
        YEARLY("Yearly"),
        INFINITELY("Grand Total", "Project Total"),
        DISPLAY_RANGE("DisplayRange"),;
        private String value;
        private String pageTitle;

        DisplayTotal(String v) {
            value = v;
        }

        DisplayTotal(String v, String t) {
            this(v);
            pageTitle = t;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public String getValue() {
            return value == null ? name() : value;
        }
    }

    public enum WorkTypes {
        WORK_ITEM,
        MSP_PROJECT,
        GATED_PROJECT,
        GATED_PROJECT_NOT_ESG;
    }

    public enum ItemTypes {
        MONETARY("Monetary"),
        NUMERIC("Numeric"),
        COST_MONETARY("Cost (Monetary)", "Cost"),
        SEPARATOR("Separator"),;
        private String name;
        private String shortName;

        ItemTypes(String name) {
            this.name = name;
        }

        ItemTypes(String name, String shortName) {
            this(name);
            this.shortName = shortName;
        }

        public String getName() {
            return name;
        }

        public String getShortName() {
            return shortName != null ? shortName : name;
        }
    }

    public enum CostMapping {
        ESTIMATED("Estimated"),
        ACTUAL("Actual"),
        MANUAL("Manual"),;
        String val;

        CostMapping(String v) {
            val = v;
        }

        public String getValue() {
            return val;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public static CostMapping get(String type) {
            for (CostMapping t : values()) {
                if (t.getName().equals(type)) {
                    return t;
                }
            }
            return null;
        }
    }

    public MetricTemplate(Config conf) {
        super(conf);
        if (conf.hasChild("period-type")) {
            int index = Integer.parseInt(conf.getText("period-type"));
            for (FrequencyType f : FrequencyType.values()) {
                if (f.ordinal() == index) {
                    columnType = f;
                    break;
                }
            }
        } else {
            columnType = FrequencyType.MONTHLY;
            conf.setText("period-type", "0");
        }
    }

    public MetricTemplate(String name, Long start, Long end) {
        this(Config.createConfig(NAME));
        setName(name);
        setStartDateLong(start);
        setEndDateLong(end);
    }

    public MetricTemplate(String name, int months) {
        this(name, PSCalendar.getEmptyCalendar().getTime(), PSCalendar.getEmptyCalendar().set(months * 30).getTime());
    }

    public FrequencyType getPeriodType() {
        return columnType;
    }


    public String getMetricCalendar() {  // this is for 9.4
        return getString("metric-calendar");
    }

    private Long getDateLong(String node) {
        if (conf.getChByName(node) == null) return null;
        return Long.parseLong(conf.getText(node));
    }

    public Long getStartDateLong() {
        return getDateLong("start");
    }

    public Long getEndDateLong() {
        return getDateLong("end");
    }

    public void setStartDateLong(Long start) {
        if (start != null)
            conf.setText("start", String.valueOf(start));
    }

    public void setEndDateLong(Long end) {
        if (end != null)
            conf.setText("end", String.valueOf(end));
    }

    public void setPeriod(Long s, Long e) {
        setStartDateLong(s);
        setEndDateLong(e);
    }


    public void setPeriod() {
        int month = Integer.parseInt(conf.getText("period"));
        Calendar start = GregorianCalendar.getInstance();
        Calendar end = GregorianCalendar.getInstance();
        start.set(Calendar.DAY_OF_MONTH, 1);
        start.set(Calendar.MONTH, Calendar.JANUARY);
        end.set(Calendar.MONTH, Calendar.JANUARY + month);
        end.set(Calendar.DAY_OF_MONTH, 1);
        setPeriod(start.getTimeInMillis(), end.getTimeInMillis());
    }

    public void setBreakdownTag(PSTag tag) {
        Config t = (Config) tag.getConfig().clone();
        t.setAttributeValue("type", "breakdown");
        conf.addChild(t);
    }

    public MetricTag getBreakdownTag() {
        for (Config c : conf.getChsByName(PSTag.NAME)) {
            String val = c.getAttributeValue("type");
            if (val == null || !val.equals("breakdown")) continue;
            if (c.getChilds().size() == 0) {
                c = c.findChildByXpath();
                if (c == null) return null;
            }
            return new MetricTag(c);
        }
        return null;
    }

    public MetricTag getBreakdownTag(String tag) {
        MetricTag parent = getBreakdownTag();
        if (parent == null) return null;
        for (PSTag t : parent.getAllChildren()) {
            if (t.getName().equals(tag)) return (MetricTag) t;
        }
        return null;
    }

    public boolean isBreakdown() {
        return getBreakdownTag() != null;
    }

    /**
     * This is only for 9.3 or later.
     *
     * @return
     */
    public boolean isPercentageAllocation() {
        return getBreakdownTag() != null && getBreakdownTag().isPercentageAllocation();
    }

    public void setWorkTypes(WorkTypes... types) {
        Config cTypes;
        if ((cTypes = conf.getChByName("types")) == null) cTypes = conf.addElement("types");
        for (WorkTypes type : types) {
            cTypes.addElement("type").setText(type.name());
        }
    }

    public boolean hasWorkType(WorkTypes type) {
        Config cTypes = conf.getChByName("types");
        if (cTypes == null) return false;
        for (Config c : cTypes.getChilds()) {
            if (c.getText().equalsIgnoreCase(type.name())) return true;
        }
        return false;
    }

    public void setFiscalYear(boolean current) {
        conf.setText("fiscal-year", String.valueOf(current));
    }

    /**
     * @return true if current
     */
    public boolean getFiscalYear() {
        return Boolean.parseBoolean(conf.getText("fiscal-year"));
    }

    public DisplayTotal getDisplayTotal() {
        if (!conf.hasChild("display-total")) return null;
        String txt = conf.getText("display-total");
        for (DisplayTotal d : DisplayTotal.values()) {
            if (d.getValue().equalsIgnoreCase(txt)) {
                return d;
            }
        }
        return null;
    }

    public void setDisplayTotal(DisplayTotal dt) {
        if (dt != null) {
            conf.setText("display-total", dt.getValue());
        }
    }


    public List<Item> getItems() {
        List<Item> res = new ArrayList<Item>();
        Config cTypes = conf.getChByName("items");
        if (cTypes == null) return res;
        if (cTypes.getChilds().size() == 0)
            cTypes = cTypes.findChildByXpath();
        if (cTypes == null) return res;
        for (Config c : cTypes.getChilds()) {
            res.add(new Item(c));
        }
        return res;
    }

    public List<Item> getActualItems() {
        return getActualItems(false);
    }

    public List<Item> getActualItems(boolean excludeSeparators) {
        List<Item> res = new ArrayList<Item>();
        for (Item i : getItems()) {
            if (excludeSeparators && i.getType().equals(ItemTypes.SEPARATOR)) continue;
            res.add(i);
        }
        Collections.sort(res);
        return res;
    }

    public boolean hasFormula() {
        for (Item i : getItems()) {
            if (i.getFormula() != null) return true;
        }
        return false;
    }

    public Item getItem(String name) {
        for (Item item : getItems()) {
            if (item.getName().equals(name)) return item;
        }
        return null;
    }

    public void addItem(String name) {
        addItem(name, null, null, null, null);
    }

    public void addItem(String name, ItemTypes type, CostMapping mapping, Integer seq, String desc) {
        Config items = conf.getChByName("items");
        if (items == null) items = conf.addElement("items");
        if (type == null) type = ItemTypes.NUMERIC;
        if (seq == null) seq = getItems().size() + 1;
        Config item = items.addElement("item");
        item.setText("name", name);
        item.setText("seq", String.valueOf(seq));
        item.setText("type", type.name().toLowerCase());
        if (desc != null)
            item.setText("description", desc);
        if (mapping != null) {
            item.setText("cost_mapping", mapping.name().toLowerCase());
        }
    }

    public boolean hasViews() {
        return conf.hasChild("views");
    }

    public List<View> getViews() {
        List<View> res = new ArrayList<View>();
        Config cView = conf.getChByName("views");
        if (cView == null) return res;
        for (Config c : cView.getChilds()) {
            String name = c.getText("name");
            Integer seq = c.hasChild("seq") ? Integer.parseInt(c.getText("seq")) : null;
            String type = c.hasChild("type") ? c.getText("type").toLowerCase() : null;
            CostMapping iType = CostMapping.get(type);
            res.add(new View(name, seq, iType));
        }
        return res;
    }

    public String getFormula(Item i) {
        if (i == null || i.getFormula() == null) return null;
        String res = Calculator.formatFormula(i.getFormula());
        List<Item> items = getActualItems(true);
        for (int j = 0; j < items.size(); j++) { // format formula like on page
            res = res.replace("[" + (j + 1) + "]", items.get(j).getName());
        }
        return res;
    }


    public class Item implements Comparable {
        private ItemTypes type;
        private int seq;
        private String name;
        private Config conf;
        private String formula;
        private String description;
        private CostMapping mapping;

        protected Item(Config c) {
            this.conf = c;
            this.name = c.getText("name");
            this.seq = Integer.parseInt(c.getText("seq"));
            description = c.getText("description");
            String sType = c.getText("type");
            formula = c.getText("formula");
            if (sType != null)
                sType = sType.toLowerCase();
            for (ItemTypes t : ItemTypes.values()) {
                if (t.name().toLowerCase().equals(sType)) {
                    this.type = t;
                    break;
                }
            }
            String sMapping = c.getText("cost_mapping");
            if (sMapping != null) {
                for (CostMapping cm : CostMapping.values()) {
                    if (cm.name().toLowerCase().equals(sMapping)) {
                        mapping = cm;
                        break;
                    }
                }
            }
        }

        public Config getConfig() {
            return conf;
        }

        public int getSequence() {
            return seq;
        }

        public ItemTypes getType() {
            if (type == null) return ItemTypes.MONETARY;
            return type;
        }

        public String getTypeString() {
            if (type == null) return null;
            return type.getName();
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getFormula() {
            return formula;
        }

        public boolean hasFormula() {
            return formula != null;
        }

        public CostMapping getCostMapping() {
            return mapping;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Item && name.equals(((Item) o).name) && seq == ((Item) o).seq;
        }

        @Override
        public int compareTo(Object o) {
            if (!(o instanceof Item))
                return 0;
            return getSequence() - ((Item) o).getSequence();
        }
    }

    public class View {
        private CostMapping type;
        private Integer seq;
        private String name;

        public View(String name, Integer seq, CostMapping type) {
            this.name = name;
            this.seq = seq;
            this.type = type;
        }

        public View(String name, int seq) {
            this(name, seq, null);
        }

        public int getSequence() {
            return seq;
        }

        public CostMapping getType() {
            return type;
        }

        public String getTypeString() {
            if (type == null) return null;
            return type.getValue();
        }

        public String getName() {
            return name;
        }
    }

    public String toString() {
        return "[Template " + getName() + "]";
    }

    public class MetricTag extends PSTag {
        private Integer percent;

        public MetricTag(Config conf) {
            super(conf);
            String sp = conf.getText("percent");
            if (sp != null) {
                percent = sp.isEmpty() ? 0 : Integer.parseInt(sp);
            }
        }

        public boolean isPercentageAllocation() {
            return percent != null;
        }

        public Integer getPercent() {
            if (percent == null && !hasParent()) {
                for (PSTag t : getAllChildren()) {
                    Integer p = ((MetricTag) t).getPercent();
                    if (p != null) {
                        if (percent == null) percent = 0;
                        percent += p;
                    }
                }
            }
            return percent;
        }

        public Float getPercentFloat() {
            Integer p = getPercent();
            if (p == null) return null;
            return p.floatValue() / 100f;
        }

        public Double getPercentDouble() {
            Integer p = getPercent();
            if (p == null) return null;
            return p.doubleValue() / 100d;
        }

        @Override
        public List<PSTag> getAllChildren() {
            if (!isPercentageAllocation()) return super.getAllChildren();
            List<PSTag> res = new ArrayList<PSTag>();
            for (PSTag t : super.getAllChildren()) {
                if (((MetricTag) t).percent != null) res.add(t);
            }
            return res;
        }

        @Override
        public List<PSTag> getChilds() {
            List<PSTag> tags = new ArrayList<PSTag>();
            for (Config c : getConfig().getChsByName(NAME)) {
                PSTag tag = new MetricTag(c);
                tag.setParent(this);
                tags.add(tag);
            }
            return tags;
        }
    }


    @Override
    public void setCreated() {
        super.setCreated();
        TestSession.putMetricTemplate(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeMetricTemplate(this);
    }
}
