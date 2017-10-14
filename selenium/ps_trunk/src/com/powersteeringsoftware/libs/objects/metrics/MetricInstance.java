package com.powersteeringsoftware.libs.objects.metrics;

import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.Calculator;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.Assert;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.05.12
 * Time: 15:05
 * To change this template use File | Settings | File Templates.
 */
public class MetricInstance extends MetricTemplate implements Cloneable {
    public static final String SINGLE_VIEW = "Default View";

    public static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);

    private List<Column> columns = null;

    private static NumberFormat getTmpFormat() {
        return getFormat(null, false, 9, 0);
    }

    public static NumberFormat getCellMonetaryEditFormat(Currency c) {
        return getFormat(c.getNumberFormat(), false, CoreProperties.getMetricItemMonetaryFormatMax(), 0);
    }

    public static NumberFormat getCellMonetaryFormat(Currency c) {
        return getFormat(c.getCurrencyFormat(), true, CoreProperties.getMetricItemMonetaryFormatMax(), 0);
    }

    public static NumberFormat getCellNumericEditFormat(Currency c) {
        return getFormat(c.getNumberFormat(), false, CoreProperties.getMetricItemNumericFormatMax(), 0);
    }

    public static NumberFormat getCellNumericFormat(Currency c) {
        return getFormat(c.getNumberFormat(), true, CoreProperties.getMetricItemNumericFormatMax(), 0);
    }

    private static NumberFormat getFormat(NumberFormat numberFormat, boolean group, int max, int min) {
        NumberFormat res = numberFormat == null ? NumberFormat.getInstance(Locale.ROOT) : numberFormat;
        res.setGroupingUsed(group);
        res.setMaximumFractionDigits(max);
        res.setMinimumFractionDigits(0);
        return res;
    }


    private Work work;

    public MetricInstance(Work w, MetricTemplate t) {
        this(w, (Config) t.getConfig().clone());
    }

    public MetricInstance(Work w, Config t) {
        super(t);
        work = w;
    }

    public MetricInstance(Config t) {
        super(t);
        if (conf.getChByName("work") != null) {
            Config workConfig = conf.getChByName("work");
            if (workConfig.getChilds().size() == 0 && workConfig.hasXpathAttr()) {
                workConfig = workConfig.findChildByXpath();
            }
            work = new Work(workConfig);
        }
    }

    public Object clone() {
        return new MetricInstance((Config) conf.clone());
    }


    public Work getWork() {
        return work;
    }

    @Override
    public Currency getCurrency() {
        if (work != null) return work.getCurrency();
        return Currency.getDefaultCurrency();
    }

    @Override
    public void setCurrency(Currency c) {
    }

    public String toString() {
        return "[Instance " + getName() + "," + getId() + "]";
    }

    public List<View> getViews() {
        List<View> res = super.getViews();
        if (res.size() == 0) {
            Config views = conf.getChByName("views");
            if (views == null) views = conf.addElement("views");
            for (DataItemsList d : getData()) {
                String txt = d.getViewName();
                Config c = Config.createConfig("view");
                c.addElement("name");
                c.setText("name", txt);
                if (d.getCost() != null) {
                    c.setText("type", d.getCost().getName());
                }
                views.addChild(c);
            }
        }
        return super.getViews();
    }

    private List<DataItemsList> data;

    public List<DataItemsList> getData() {
        if (data != null && data.size() != 0) return data;
        // view as key
        List<DataItemsList> res = new ArrayList<DataItemsList>();

        // load views:
        List<Config> views = conf.getChsByName("data");
        for (Config view : views) {
            res.add(new DataItemsList(view));
        }
        return this.data = res;
    }

    public DataItemsList getData(String view) {
        for (DataItemsList res : getData()) {
            if (res.getViewName().equals(view)) return res;
        }
        return null;
    }

    public void setData(DataItemsList toAdd) {
        List<DataItemsList> data = getData();
        int index = data.indexOf(toAdd);
        if (index != -1) {
            data.get(index).addAll(toAdd);
        } else {
            data.add(toAdd);
            conf.addChild(toAdd.getConfig());
        }
    }

    private Integer getRightCellIndex() {
        if (FrequencyType.MONTHLY.equals(columnType))
            return Period.Month.Dec.ordinal();
        if (FrequencyType.QUARTERLY.equals(columnType))
            return Period.Quarter.Q4.ordinal();
        if (FrequencyType.MONTHLY_13.equals(columnType))
            return 12;
        if (FrequencyType.QUARTERLY_13.equals(columnType))
            return 4;
        return null;
    }

    public class DataItemsList extends ArrayList<ItemRow> implements Cloneable {
        private String view;
        private CostMapping cost;
        private Config config;

        private DataItemsList(Config config) {
            super();
            if (config == null) {
                config = Config.createConfig("data");
                config.setAttributeValue("view", SINGLE_VIEW);
            }
            this.config = config;
            if (config.hasAttribute("view")) {
                view = config.getAttributeValue("view");
            } else if (config.hasXpathAttr()) {
                Config _c = config.findChildByXpath();
                view = _c.getText("name");
                cost = CostMapping.get(_c.getText("type"));
            }
            if (view == null) view = SINGLE_VIEW;
            // load items:
            for (Config item : config.getChsByName("item")) {
                ItemRow row = new ItemRow(item);
                add(row);
                addAll(row.getSubItems());
            }
        }

        public Work getWork() {
            return MetricInstance.this.getWork();
        }

        public Config getConfig() {
            return config;
        }

        public String getViewName() {
            return view;
        }

        public CostMapping getCost() {
            return cost;
        }

        public View getView() {
            for (View v : getViews()) {
                if (v.getName().equals(view)) return v;
            }
            return null;
        }

        public boolean equals(Object o) {
            return o != null && o instanceof DataItemsList && view.equals(((DataItemsList) o).view);
        }

        public List<Column> getHeader() {
            return getColumns();
        }

        public void addAll(DataItemsList list) { // merge list
            for (ItemRow toAdd : list) {
                int index = indexOf(toAdd);
                if (index == -1) {
                    add(toAdd);
                    config.addChild(toAdd.getConfig());
                    continue;
                }
                ItemRow was = get(index);
                for (ItemCell cell : toAdd.getCells()) {
                    was.setCell(cell);
                }

            }
        }

        @Override
        public Object clone() {
            return new DataItemsList((Config) config.clone());
        }

        /**
         * ignore subrows.
         *
         * @return
         */
        public List<ItemRow> getRows() {
            List<ItemRow> res = new ArrayList<ItemRow>();
            for (ItemRow row : this) {
                if (row.hasSubItems() || row.isEditable()) {
                    res.add(row);
                }
            }
            return res;
        }

        public boolean isNotEmpty() {
            for (Config c : config.getChsByName("item")) {
                if (c.hasChild("cell")) return true;
            }
            /*for (ItemRow row : this) {
                for (ItemCell c : row.getCells()) {
                    if (c.getValue() != null) return true;
                }
            }*/
            return false; //empty
        }

        public void removeRow(ItemRow row) {
            List<ItemRow> res = new ArrayList<ItemRow>();
            res.add(row);
            res.addAll(row.getSubItems());
            removeAll(res);
        }

        public boolean hasRow(String parent, String child) {
            ItemRow res = _getRow(parent, child);
            return res != null && !res.isEmpty();
        }

        private ItemRow _getRow(String parent, String child) {
            for (ItemRow row : this) {
                if (child == null) {
                    if (!row.hasParent() && row.getName().equals(parent)) {
                        return row;
                    }
                } else {
                    if (row.hasParent() && row.getParent().getName().equals(parent) && row.getName().equals(child)) {
                        return row;
                    }
                }
            }
            return null;
        }

        public boolean isCostItem(Item item) {
            if (!getWork().getControlCost()) return false;
            if (!item.getType().equals(ItemTypes.COST_MONETARY)) return false;
            CostMapping cm = getCostMapping(item);
            return cm != null && !cm.equals(CostMapping.MANUAL);
        }

        public CostMapping getCostMapping(Item item) {
            CostMapping cm = item.getCostMapping();
            if (cm != null) return cm;
            View view = getView();
            return cm = view != null ? view.getType() : null;
        }

        public ItemRow getRow(String parent, String child) {
            ItemRow res = null;
            if ((res = _getRow(parent, child)) != null) return res;
            res = _getRow(parent, null);
            Item item = getItem(parent);
            Assert.assertNotNull(item, "Incorrect config. Can't find item " + parent);
            boolean parentEditable = !isBreakdown() || isPercentageAllocation();
            boolean isCostItem = isCostItem(item);
            if (res == null) {
                Config c = Config.createConfig("item");
                c.setText("name", parent);
                res = new ItemRow(c, item, parentEditable && !item.hasFormula() && !isCostItem);
                res.setEmpty();
                add(res);
            }
            if (getBreakdownTag() != null) {
                for (PSTag tag : getBreakdownTag().getAllChildren()) {
                    Assert.assertNotNull(item, "Incorrect config. Can't find tag for item " + child);
                    Config c = res.getConfig().addElement("item");
                    c.setText("name", tag.getName());
                    ItemRow ch = new ItemRow(c, item, !parentEditable && !item.hasFormula());
                    ch.setEmpty();
                    ch.parent = res;
                    res.subRows.add(ch);
                    add(ch);
                }
            }
            return _getRow(parent, child);
        }

        public ItemCell getCell(String parent, String child, int column) {
            return getCell(parent, child, getColumns().get(column));
        }

        public ItemCell getCell(String parent, String child, Column column) {
            ItemRow row = getRow(parent, child);
            if (isBreakdown()) {
                if (isPercentageAllocation()) {
                    return getBreakdownPercentageCell(row, column);
                }
                return getBreakdownCell(row, column);
            }
            return getSimpleCell(row, column);
        }

        private ItemCell getSimpleCell(ItemRow row, Column column) {
            return getCell(column, row);
        }

        private ItemCell getBreakdownPercentageCell(ItemRow row, Column column) {
            ItemCell parentCell = getCell(column, row.hasParent() ? row.getParent() : row);
            Item it = row.item;
            if (!row.hasParent()) {
                return parentCell;
            }

            ItemCell cell = getCell(column, row);
            MetricTag tag = getBreakdownTag(row.getName());
            if (tag == null)
                Assert.fail("Incorrect config. Can't find tag for item " + row);
            if (tag.getPercent() == null)
                Assert.fail("Incorrect config. Can't find percents for tag for item " + row);
            if (cell != null && parentCell.getDValue() != null) {
                cell.setDValue(tag.getPercentDouble() * parentCell.getDValue()); // hack. convert to float.
                return cell;
            }
            return new ItemCell(column, it, null, null);
        }

        /**
         * Percent Allocation by Beneficiaries is off
         *
         * @param view
         * @param parent
         * @param child
         * @param column
         * @return ItemCell for validation purpose
         */
        private ItemCell getBreakdownCell(ItemRow row, Column column) {
            Item it = row.item;
            if (!row.hasParent()) {
                double res = 0;
                for (ItemRow r : this) {
                    if (r.hasParent() && r.getParent().equals(row)) {
                        ItemCell cell = getCell(column, r);
                        if (cell != null && cell.getDValue() != null) {
                            res += cell.getDValue();
                        }
                    }
                }
                return new ItemCell(column, it, res == 0 ? null : res, row.isEditable());
            }
            return getCell(column, row);
        }

        private ItemCell getCell(Column column, ItemRow row) {
            ItemCell res = row.getCell(column);
            if (res != null) return res;
            res = new ItemCell(column, row.item, null, row.isEditable());
            //todo:
            if (isCostItem(row.item)) { // process cost items
                CostMapping cm = getCostMapping(row.item);
                List<Cost> costs = getWork().getCosts(cm.equals(CostMapping.ESTIMATED) ? Cost.Type.ESTIMATED : Cost.Type.ACTUAL);
                Double val = null;
                /*if (!costs.isEmpty()) {
                    PSLogger.debug("Metric costs: " + costs);
                }*/
                for (Cost c : costs) {
                    Double _v = c.getValue(column.getStart(), column.getEnd());
                    if (val == null) {
                        val = _v;
                    } else if (_v != null) {
                        val += _v;
                    }
                }
                if (val != null)
                    res.setDValue(val);
            }
            if (row.item.hasFormula()) {
                Map<Integer, ItemCell> cells = getRelatedCells(res, row);
                String formula = row.item.getFormula();
                for (Integer seq : cells.keySet()) {
                    Double val = cells.get(seq).getDValue();
                    formula = formula.replace("[" + seq + "]", val == null ? "0" : String.valueOf(val));
                }
                Double d = Calculator.calculate(formula);
                if (Double.isInfinite(d) || Double.isNaN(d)) d = 0d;
                res.setDValue(d);
                return res;
            }
            if (column.isTotal()) {
                Double d = 0d; //todo: only for year now
                for (Column _c : getColumns()) {
                    if (_c.equals(column)) continue;
                    ItemCell c = getCell(_c, row);
                    if (c.getDValue() != null) {
                        d += c.getDValue();
                    }
                }
                if (d != 0 || isNotEmpty()) {
                    res.setDValue(d);
                }
            }
            ItemCell prev = null;
            for (ItemCell c : row.getCells()) {
                if (c.getColumn().hashCode() >= res.getColumn().hashCode()) break;
                prev = c;
            }
            if (prev == null || !prev.isRep()) return res;
            if (res.getColumn().hashCode() <= prev.getRep().getIndex()) {
                res.setDValue(prev.getDValue());
            }
            return res;
        }

        private Map<Integer, ItemCell> getRelatedCells(ItemCell cell, ItemRow row) {
            Map<Integer, ItemCell> res = new HashMap<Integer, ItemCell>();
            Column col = cell.getColumn();
            Item item = row.item;

            if (!isBreakdown() || isPercentageAllocation()) {
                for (ItemRow r : this) {
                    if (r.hasParent()) continue;
                    if (r.item.equals(item)) continue;
                    ItemCell c = r.getCell(col);
                    if (c == null) c = r.newCell(col);
                    res.put(r.item.getSequence(), c);
                }
            } else if (row.hasParent()) {
                int index = 0;
                for (PSTag t : getBreakdownTag().getAllChildren()) {
                    if (t.getName().equals(row.getName())) {
                        break;
                    }
                    index++;
                }
                for (ItemRow r : this) {
                    if (r.hasParent()) continue;
                    if (r.item.equals(item)) continue;
                    ItemRow sub = r.getSubItems().get(index);
                    ItemCell c = sub.getCell(col);
                    if (c == null) c = sub.newCell(col);
                    res.put(r.item.getSequence(), c);
                }
            }
            return res;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (ItemRow r : this) {
                String parent = r.hasParent() ? r.getParent().getName() : r.getName();
                String child = r.hasParent() ? r.getName() : null;

                for (Column c : getColumns()) {
                    ItemCell cell = getCell(parent, child, c);
                    sb.append("[").append(r).append("]").append(" => ").
                            append(cell).append("(").append(cell.getResultValue()).append(";").append(cell.getEditValue()).append(")").append(",").append("\n");
                }
            }
            return sb.toString();
        }

    }

    private Column toColumn(Period p) {
        Column c = new Column(p.ordinal());
        c.setName(p.getName(YEAR));
        c.setStart(p.getStart(YEAR));
        c.setEnd(p.getEnd(YEAR));
        return c;
    }

    public List<Column> getColumns() {
        if (columns != null) return columns;
        columns = new ArrayList<Column>();
        if (FrequencyType.MONTHLY.equals(columnType)) {
            for (Period.Month m : Period.Month.values()) {
                columns.add(toColumn(m));
            }
        } else if (FrequencyType.QUARTERLY.equals(columnType)) {
            for (Period.Quarter q : Period.Quarter.values()) {
                columns.add(toColumn(q));
            }
        } else if (FrequencyType.MONTHLY_13.equals(columnType) || FrequencyType.QUARTERLY_13.equals(columnType)) {
            if (FrequencyType.MONTHLY_13.equals(columnType)) {
                for (Period.Month13 m : Period.Month13.values()) {
                    columns.add(toColumn(m));
                }
            }
            if (FrequencyType.QUARTERLY_13.equals(columnType)) {
                for (Period.Quarter13 m : Period.Quarter13.values()) {
                    columns.add(toColumn(m));
                }
            }
        }
        DisplayTotal dt = getDisplayTotal();
        if (dt != null) {
            if (DisplayTotal.INFINITELY.equals(dt)) {
                Column c = new Column(columns.size(), dt);
                c.setEnd(columns.get(columns.size() - 1).getEnd());
                c.setStart(columns.get(0).getStart());
                columns.add(c);
            } else {
                //todo
                //todo: for other totals
                //todo
            }
        }
        return columns;
    }


    public class Column {
        private Integer index;
        private String name;
        private String start;
        private String end;
        private DisplayTotal d;

        private Column(int i) {
            index = i;
        }

        private Column(int i, DisplayTotal d) {
            this(i);
            setName((this.d = d).getPageTitle());
        }

        public boolean isTotal() {
            return d != null;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            else if (!(o instanceof Column))
                return false;
            return index == ((Column) o).index;
        }

        @Override
        public int hashCode() {
            return index;
        }

        @Override
        public String toString() {
            return name == null ? String.valueOf(index) : name;
        }

        public String getStart() {
            return start;
        }

        public void setStart(String start) {
            this.start = start;
        }

        public String getEnd() {
            return end;
        }

        public void setEnd(String end) {
            this.end = end;
        }
    }

    public class ItemCell {
        private Column column; // now supported only for monthly metrics
        private String value;  // what entered
        private Double dValue;
        private Item item;
        private Replication replication;
        private boolean editable = true;
        private Config cellConfig;

        private class Replication {
            private Replication(String s, int i) {
                this.val = s;
                this.order = i;
            }

            private String val;
            private Integer order;

            public String getValue() {
                return val;
            }

            public int getIndex() {
                return order;
            }
        }

        private ItemCell(Config cell, Item it) {
            this(cell);
            setValue(cell.getText());
            this.item = it;
        }

        private ItemCell(Config cell) {
            if (cell == null) {
                cellConfig = Config.createConfig("cell");
            } else {
                cellConfig = cell;
            }

            if (cell.hasAttribute("column")) {
                String columnName = cell.getAttribute("column");
                for (int i = 0; i < getColumns().size(); i++) {
                    Column c = getColumns().get(i);
                    if (columnName.equals(c.getName())) {
                        column = c;
                        if (!cell.hasAttribute("index")) {
                            cell.setAttributeValue("index", String.valueOf(i));
                        }
                        break;
                    }
                }
            }
            if (column == null && cell.hasAttribute("index")) {
                int index = Integer.parseInt(cell.getAttribute("index"));
                if (index < getColumns().size()) {
                    column = getColumns().get(index);
                    if (column.getName() != null)
                        cell.setAttributeValue("column", column.getName());
                }
            }
        }

        private ItemCell(Column column, Item it) {
            cellConfig = Config.createConfig("cell");
            this.column = column;
            cellConfig.setAttributeValue("index", String.valueOf(column.hashCode()));
            if (column.getName() != null)
                cellConfig.setAttributeValue("column", column.getName());
            this.item = it;
        }

        private ItemCell(Column index, Item it, Double dValue, Boolean editable) {
            this(index, it);
            this.dValue = dValue;
            if (editable != null)
                this.editable = editable;
        }

        public Config getConfig() {
            return cellConfig;
        }

        @Override
        public boolean equals(Object o) {
            return o != null && o instanceof ItemCell && column.equals(((ItemCell) o).column);
        }

        public boolean isEditable() {
            return editable;
        }

        @Override
        public int hashCode() {
            return column.hashCode();
        }

        public Column getColumn() {
            return column;
        }

        public String toString() {
            return column + "->" + value;
        }

        private void setValue(String val) {
            if (val == null) {
                value = "";
                return;
            }
            value = val;
            cellConfig.setText(val);
            if (value.contains("r")) {
                // todo:
                value = val.replace("####", String.valueOf(YEAR));
                String rep = value.replaceAll("^.*r(.*)$", "$1");
                val = value.replaceAll("^(.*)r.*$", "$1");
                Integer replication = null;
                if (rep.matches("\\d+")) {
                    replication = Integer.parseInt(rep) - 1 + column.hashCode();
                } else if (rep.matches("\\d{4}.\\d{2}")) {
                    String rep1 = rep.replaceAll(".*(\\d{4}).\\d{2}$", "$1");
                    rep = rep.replaceAll(".*(\\d{2})$", "$1");
                    if (rep1.matches("\\d+") && rep.matches("\\d+")) {
                        int r = Integer.parseInt(rep);
                        replication = r - 1;
                        if (replication < 0 || replication > getRightCellIndex()) {
                            replication = column.hashCode();
                        }

                        if (Integer.parseInt(rep1) > YEAR) {
                            replication = getRightCellIndex();
                        }
                        if (Integer.parseInt(rep1) < YEAR) {
                            replication = column.hashCode();
                        }
                    }
                }
                this.replication = new Replication(val, replication);
            }
            try {
                if (!val.isEmpty())
                    dValue = Double.parseDouble(val);
            } catch (NumberFormatException e) {
                // ignore
            }
        }


        public boolean isRep() {
            return replication != null;
        }

        public Replication getRep() {
            return replication;
        }

        public String getValue() {
            return value;
        }

        public Double getDValue() {
            return dValue;
        }

        public void setDValue(Double d) {
            dValue = d;
            setValue(d == null ? "" : String.valueOf(d));
        }

        public void setFValue(Double d) {
            setDValue((double) d.floatValue());
        }

        public String getEditValue() {
            if (!editable) return null;
            if (dValue == null) return "";
            ItemTypes type = item.getType();
            if (type.equals(ItemTypes.MONETARY) || type.equals(ItemTypes.COST_MONETARY)) {
                return getCellMonetaryEditFormat(getCurrency()).format(getTempDValue());
            } else if (type.equals(ItemTypes.NUMERIC)) {
                return getCellNumericEditFormat(getCurrency()).format(getTempDValue());
            }
            // unsupported now
            return null;
        }

        public String getResultValue() {
            if (dValue == null) return "";
            ItemTypes type = item.getType();
            if (type.equals(ItemTypes.MONETARY) || type.equals(ItemTypes.COST_MONETARY)) {
                String res = getCellMonetaryFormat(getCurrency()).format(Math.abs(getTempDValue()));
                if (dValue < 0) res = "(" + res + ")";
                return res;
            } else if (type.equals(ItemTypes.NUMERIC)) {
                return getCellNumericFormat(getCurrency()).format(getTempDValue());
            }
            // unsupported now
            return null;
        }

        /**
         * due to #81023
         * 139201.84500000003 -> 139201.84 (see java.math.RoundingMode.HALF_EVEN)
         *
         * @return
         */
        private Double getTempDValue() {
            return Double.parseDouble(getTmpFormat().format(dValue));
        }
    }

    public class ItemRow {
        private String name;
        private ItemRow parent;
        private Set<ItemCell> cells;
        private List<ItemRow> subRows = new ArrayList<ItemRow>();
        private Item item;
        private Boolean editable;
        private Config rowConfig;
        private Boolean empty;

        private ItemRow(Config conf) {
            this(conf, null, !isBreakdown() || isPercentageAllocation());
            if (isPercentageAllocation()) {
                for (PSTag t : getBreakdownTag().getAllChildren()) {
                    Config c = rowConfig.addElement("item");
                    c.setText("name", t.getName());
                    ItemRow toAdd = new ItemRow(c, this.item, false);
                    subRows.add(toAdd);
                    toAdd.parent = this;
                }
            } else { // breakdown:
                for (Config row : conf.getChsByName("item")) {
                    ItemRow toAdd = new ItemRow(row, this.item, true);
                    subRows.add(toAdd);
                    toAdd.parent = this;
                }
            }
        }

        private ItemRow(Config conf, Item item, Boolean editable) {
            rowConfig = conf;
            this.editable = editable;
            name = conf.getText("name");
            if (item == null) {
                item = getItem(name);
            }
            if (item == null) {
                Assert.fail("incorrect config: can't find item " + name);
            }
            this.item = item;
            cells = new HashSet<ItemCell>();
            for (Config cell : conf.getChsByName("cell")) {
                cells.add(new ItemCell(cell, this.item));
            }
        }

        public String getTitle() {
            if (item == null) return null;
            String formula = getFormula(item);
            String desc = hasParent() ? null : item.getDescription();
            if (formula == null) {
                return desc;
            } else if (desc == null) {
                return formula;
            }
            return formula + " " + desc;
        }

        public boolean hasFormula() {
            return item != null && item.getFormula() != null;
        }

        public boolean isEmpty() {
            if (empty == null) empty = conf.hasAttribute("empty");
            return empty;
        }

        private void setEmpty() {
            empty = true;
            conf.setAttributeValue("empty", "true");
        }

        public Config getConfig() {
            return rowConfig;
        }

        public boolean isEditable() {
            return editable;
        }

        public String toString() {
            return parent != null ? parent.getName() + ">" + getName() : getName();
        }

        public boolean equals(Object o) {
            if (o == null || !(o instanceof ItemRow)) return false;
            ItemRow r = (ItemRow) o;
            return name.equals(r.name) && (parent == null && r.parent == null || parent.equals(r.parent));
        }

        public String getName() {
            return name;
        }

        public List<ItemRow> getSubItems() {
            return subRows;
        }

        public ItemRow getParent() {
            return parent;
        }

        public boolean hasParent() {
            return parent != null;
        }

        public boolean hasSubItems() {
            return subRows.size() > 0;
        }

        public ItemCell getCell(Column column) {
            for (ItemCell cell : cells) {
                if (column.equals(cell.column)) return cell;
            }
            return null;
        }

        public ItemCell newCell(Column column) {
            ItemCell cell = new ItemCell(column, item);
            cells.remove(cell);
            cells.add(cell);
            cell.cellConfig = rowConfig.addChild(cell.cellConfig);
            return cell;
        }

        public void removeCell(ItemCell cell) {
            cells.remove(cell);
            rowConfig.removeChild(cell.cellConfig);
        }

        public void setCell(ItemCell cell) {
            ItemCell was = getCell(cell.getColumn());
            cells.remove(cell);
            cells.add(cell);
            if (was != null) {
                rowConfig.removeChild(was.cellConfig);
            }
            rowConfig.addChild(cell.cellConfig);
        }

        public Set<ItemCell> getCells() {
            return cells;
        }

    }


    @Override
    public void setCreated() {
        super._setCreated();
        TestSession.putMetric(this);
    }

    @Override
    public void setDeleted() {
        super.setDeleted();
        TestSession.removeMetric(this);
    }

    public void setLocked(boolean y) {
        set("locked", y); // todo
    }

    public boolean getLocked() {
        return getBooleanFalse("locked");
    }

    public void setReadyForRollup(boolean y) {
        set("ready-for-rollup", y); // todo
    }

    public boolean getReadyForRollup() {
        return getBooleanFalse("ready-for-rollup");
    }

    public MetricInstance copy() {
        MetricInstance mi = (MetricInstance) super.copy();
        mi.work = work;
        return mi;
    }
}
