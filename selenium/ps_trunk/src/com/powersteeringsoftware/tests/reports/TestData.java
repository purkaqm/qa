package com.powersteeringsoftware.tests.reports;

import com.powersteeringsoftware.libs.objects.rw.*;
import com.powersteeringsoftware.libs.objects.rw.RWChart.ChartGrouping;
import com.powersteeringsoftware.libs.objects.rw.RWChart.ChartOrientation;
import com.powersteeringsoftware.libs.objects.rw.RWChart.ChartType;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.Config;
import org.testng.Assert;

import java.util.*;

public class TestData {
    private static final String CONFIG_NAME = "/report_folder.xml";
    private static Config conf = Config.getTestConfig(CONFIG_NAME);

    public static final String CUSTOM_GROUP_NAME = "User defined";
    public static final String CUSTOM_GROUP_ID = "extras";

    private static boolean useTimestamp = true;

    public static Folder getFolder() {
        return new Folder(conf.getChByName("report-folder"));
    }

    public static void setUseTimestamp(boolean w) {
        useTimestamp = w;
    }

    public static Tabs getTabs(String id) {
        for (Config c : conf.getChByName("report-wizard").getChsByName("tabs")) {
            if (c.getAttribute("id").equals(id)) return new Tabs(c);
        }
        return null;
    }

    public static List<Tabs> getTabsList(String prefix) {
        List<Tabs> res = new ArrayList<Tabs>();
        for (Config c : conf.getChByName("report-wizard").getChsByName("tabs")) {
            if (c.getAttribute("id").startsWith(prefix)) {
                res.add(new Tabs(c));
            }
        }
        return res;
    }

    static class Folder {
        private Config dir;

        private Folder(Config conf) {
            if (conf.hasXpathAttr()) {
                this.dir = conf.findChildByXpath();
            } else {
                this.dir = conf;
            }
        }

        public boolean isMy() {
            if (dir.getAttributeValue("isMy") == null) {
                dir.setAttributeValue("isMy", String.valueOf(Boolean.TRUE));
            }
            return Boolean.parseBoolean(dir.getAttribute("isMy"));
        }

        public String getName() {
            String name = dir.getText();
            if (name.isEmpty()) return null;
            if (useTimestamp && !name.contains(String.valueOf(CoreProperties.getTestTemplate()))) {
                name += "_" + CoreProperties.getTestTemplate();
                dir.setText(name);
            }
            return name;
        }
    }

    static class Tabs implements Cloneable {
        private Config tabs;

        public String getId() {
            return tabs.getAttributeValue("id");
        }

        public void setId(String id) {
            tabs.setAttributeValue("id", id);
        }

        public Object clone() {
            Config clone = (Config) tabs.clone();
            return new Tabs(clone);
        }

        private Tabs(Config c) {
            tabs = c;
        }

        public String getName() {
            return getSave().getReportName();
        }

        public Folder getLocation() {
            return getSave().getReportLocation();
        }

        public String toString() {
            return getId();
        }

        public void set(Tabs tabs) {
            this.tabs = tabs.tabs;
        }

        protected abstract class Tab {
            protected Config tab;

            protected Tab(String name) {
                for (Config tab : tabs.getChsByName("tab")) {
                    if (tab.getAttribute("name").equals(name)) {
                        this.tab = tab;
                        return;
                    }
                }
                Assert.fail("Incorrect config");
            }

            protected abstract class SubData {
                protected Config subData;

                SubData(String name) {
                    subData = tab.getChByName(name);
                }
            }
        }

        public Type getType() {
            return new Type();
        }

        public boolean hasTab(String name) {
            return tabs.getElementByTagAndAttribute("tab", "name", name) != null;
        }


        //=================  TypeTabTestData ========================
        class Type extends Tab {
            private Type() {
                super("Type");
            }

            public String getDefaultCategory() {
                return tab.getChByName("category").getAttributeValue("name");
            }


            public String getDefaultType() {
                return tab.getChByName("type").getAttributeValue("name");
            }
        }// class TypeTabTestData

        public Definition getDefinition() {
            return new Definition();
        }

        //=================  DefTabTestData ========================
        class Definition extends Tab {
            private Definition() {
                super("Definition");
            }

            public String getDefaultPortfolio() {
                return tab.getChByName("portfolio").getAttributeValue("name");
            }

            public String getDefaultParentFolder() {
                return tab.getChByName("parent-folder").getAttributeValue("name");
            }

            public Set<String> getDefaultWorkTypes() {
                Set<String> types = new HashSet<String>();
                List<Config> wTypeConfigs = tab.getChByName("work-types").getChilds();
                for (Config typeConf : wTypeConfigs) {
                    types.add(typeConf.getAttributeValue("name"));
                }
                return types;
            }

            public StatusFilter getStatusFilter() {
                return new StatusFilter();
            }

            public HasMetricFilter getHasMetricFilter() {
                return new HasMetricFilter();
            }

            public RelatedWorkFilter getRelatedWorkFilter() {
                return new RelatedWorkFilter();
            }

            //=================  StatusFilterTestData ========================
            class StatusFilter extends SubData {
                private StatusFilter() {
                    super("status-filter");
                }

                public String getFilterName() {
                    return subData.getAttributeValue("name");
                }

                public String getStatus() {
                    return subData.getChByName("status").getAttributeValue("name");
                }

                public String getRangeType() {
                    return subData.getChByName("range-type").getAttributeValue("name");
                }

            } // class StatusFilterTestData

            //=================  HasMetricFilterTestData ========================
            class HasMetricFilter extends SubData {
                private HasMetricFilter() {
                    super("metric-filter");
                }

                public String getFilterName() {
                    return subData.getAttributeValue("name");
                }

                public String getMetricName() {
                    return subData.getChByName("metric").getAttributeValue("name");
                }

            }// class HasMetricFilterTestData

            //=================  RelatedWorkFilterTestData ========================
            class RelatedWorkFilter extends SubData {
                RelatedWorkFilter() {
                    super("related-work-filter");
                }

                public String getFilterName() {
                    return subData.getAttributeValue("name");
                }

                public String getRelatedWorkName() {
                    return subData.getChByName("work").getAttributeValue("name");
                }

            }// class HasMetricFilterTestData

        } // class DefTabTestData

        public Columns getColumns() {
            return new Columns();
        }

        //=================  ColTabTestData ========================
        class Columns extends Tab {
            private static final String PROJECT_GROUP_NAME = "column-group";

            Columns() {
                super("Columns");
            }

            public Set<String> getFullNamedProjectColumns() {
                Set<String> colNames = new HashSet<String>();
                Set<RWColumnGroup> groups = getAllGroups();
                for (RWColumnGroup group : groups) {
                    Set<RWColumn> columns = group.getColumns();
                    for (RWColumn column : columns) {
                        colNames.add(column.getFullColumnName());
                    }
                }
                return colNames;
            }

            public Set<RWColumnGroup> getProjectGroups(Set<RWColumnGroup> groups) {
                Set<RWColumnGroup> projGroups = new LinkedHashSet<RWColumnGroup>();
                for (RWColumnGroup group : groups) {
                    if (!group.isCustomColumnGroup()) {
                        projGroups.add(group);
                    }
                }
                return projGroups;
            }

            public RWColumnGroup getCustomGroup(Set<RWColumnGroup> groups) {
                for (RWColumnGroup group : groups) {
                    if (group.isCustomColumnGroup()) {
                        return group;
                    }
                }
                return null;
            }


            public Set<RWColumnGroup> getAllGroups() {
                Set<RWColumnGroup> groups = new LinkedHashSet<RWColumnGroup>();
                List<Config> groupConfigs = tab.getChsByName("column-group");
                for (Config groupConfig : groupConfigs) {
                    groups.add(readGroupData(groupConfig));
                }
                return groups;
            }


            private RWColumnGroup readGroupData(Config grConf) {
                String groupName = grConf.getAttributeValue("name");
                String selAll = grConf.getAttributeValue("selectAll");
                String option = grConf.getAttributeValue("option");
                RWColumnGroup group = new RWColumnGroup(groupName, (selAll != null && selAll.equalsIgnoreCase("true")));
                group.setGroupAsCustom(CUSTOM_GROUP_NAME.equals(groupName));
                if (option != null) group.setOption(option);
                if (group.isCustomColumnGroup()) group.setId(CUSTOM_GROUP_ID);
                List<Config> columnConfigs = grConf.getChsByName("column");
                for (Config colConf : columnConfigs) {
                    group.addColumn(readColumnData(colConf, group));
                }
                return group;
            }

            private RWColumn readColumnData(Config colConf, RWColumnGroup group) {
                String colName = colConf.getAttributeValue("name");
                String groupName = group.isCustomColumnGroup() ? null : group.getName();
                RWColumn column = new RWColumn(colName, groupName);
                column.setParameters(colConf.getAttributes());
                return column;
            }

        } // class ColTabTestData

        public Filter getFilter() {
            return new Filter();
        }

        //=================  FilterTabTestData ========================
        class Filter extends Tab {

            private Filter() {
                super("Filter");
            }

            public Set<RWColumn> getFilteredColumns(RWColumnGroup group) {
                Set<RWColumn> columns = new HashSet<RWColumn>();
                Set<RWColumn> grpColumns = group.getColumns();
                for (RWColumn col : grpColumns) {
                    if (col.isFiltered()) columns.add(col);
                }
                return columns;
            }

            public Set<RWColumnGroup> getGroupsOfStep(int stepNum) {
                Set<RWColumnGroup> groups = new LinkedHashSet<RWColumnGroup>();
                List<Config> configs = tab.getChsByName("columnFilters");
                for (Config conf : configs) {
                    if (conf.getAttributeValue("step").equals(String.valueOf(stepNum))) {
                        List<Config> groupConfigs = conf.getChsByName("column-group");
                        for (Config grConf : groupConfigs) {
                            groups.add(readGroupData(grConf));
                        }
                    }
                }
                return groups;
            }


            private RWColumnGroup readGroupData(Config grConf) {
                String groupName = grConf.getAttributeValue("name");
                String selAll = grConf.getAttributeValue("selectAll");
                RWColumnGroup group = new RWColumnGroup(groupName, (selAll != null && selAll.equalsIgnoreCase("true")));
                group.setGroupAsCustom(CUSTOM_GROUP_NAME.equals(groupName));
                if (group.isCustomColumnGroup()) group.setId(CUSTOM_GROUP_ID);
                List<Config> columnConfigs = grConf.getChsByName("column");
                for (Config colConf : columnConfigs) {
                    group.addColumn(readColumnData(colConf, group));
                }
                return group;
            }


            private RWColumn readColumnData(Config colConf, RWColumnGroup group) {
                String colName = colConf.getAttributeValue("name");
                String groupName = group.isCustomColumnGroup() ? null : group.getName();

                Config filterConf = colConf.getChByName("filter");
                RWFilterValue filterValue = filterConf != null ? readFilterValue(filterConf) : null;
                return new RWColumn(colName, groupName, filterValue);
            }


            private RWFilterValue readFilterValue(Config filterConf) {
                RWFilterValue fValue = new RWFilterValue(filterConf.getAttributeValue("type"));
                List<Config> configValues = filterConf.getChsByName("value");
                for (Config config : configValues) {
                    fValue.addValue(config.getText());
                }
                fValue.setParameters(filterConf.getAttributes());
                return fValue;
            }

        } //class FilterTabTestData

        public Sort getSort() {
            return new Sort();
        }

        //=================  SortTabTestData ========================
        class Sort extends Tab {

            Sort() {
                super("Sort");
            }

            public Set<RWColumn> getGroupByColumns() {
                Set<RWColumn> columns = new LinkedHashSet<RWColumn>();
                List<Config> columnConfigs = tab.getChByName("groupby").getChsByName("column");
                for (Config columnConfig : columnConfigs) {
                    columns.add(readColumnData(columnConfig));
                }
                return columns;
            }

            public Set<RWColumn> getAdditionalColumnSort() {
                Set<RWColumn> columns = new LinkedHashSet<RWColumn>();
                List<Config> columnConfigs = tab.getChByName("additional-sort").getChsByName("column");
                for (Config columnConfig : columnConfigs) {
                    columns.add(readColumnData(columnConfig));
                }
                return columns;
            }

            private RWColumn readColumnData(Config columnConf) {
                String colName = columnConf.getAttributeValue("name");
                RWSortValue sortValue = readSortValue(columnConf.getChByName("sort"));
                RWColumn column = new RWColumn(colName, sortValue);
                return column;
            }

            private RWSortValue readSortValue(Config sortConf) {
                String strOrder = sortConf.getAttributeValue("order");
                String custom = sortConf.getAttributeValue("custom");
                RWSortValue sortValue = new RWSortValue(strOrder,
                        (custom != null) &&
                                (custom.equalsIgnoreCase("true")));
                return sortValue;
            }

        } // class SortTabTestData

        public Summary getSummary() {
            return new Summary();
        }

        //=================  SummaryTabTestData ========================
        class Summary extends Tab {

            private Summary() {
                super("Summary");
            }

            public Set<RWColumn> getColumnToSetSummaryValues() {
                Set<RWColumn> columns = new HashSet<RWColumn>();
                List<Config> columnConfigs = tab.getChsByName("column");
                for (Config columnConfig : columnConfigs) {
                    columns.add(readColumnData(columnConfig));
                }
                return columns;
            }


            private RWColumn readColumnData(Config columnConfig) {
                String colName = columnConfig.getAttributeValue("name");
                RWSummaryValue summValue = readSummaryValue(columnConfig.getChByName("summary"));
                return new RWColumn(colName, summValue);
            }

            private RWSummaryValue readSummaryValue(Config conf) {
                String sumValue = conf.getAttributeValue("value");
                String showDispLbl = conf.getAttributeValue("displayLabel");
                RWSummaryValue val = RWSummaryValue.valueOf(sumValue);
                val.setDisplayLabel((showDispLbl != null) && (showDispLbl.equalsIgnoreCase("true")));
                return val;
            }
        } // class SummaryTabTestData

        public Layout getLayout() {
            return new Layout();
        }

        //=================  LayoutTabTestData ========================
        class Layout extends Tab {

            Layout() {
                super("Layout");
            }

            public String getPaperSize() {
                return tab.getChByName("paper-size").getAttributeValue("value");
            }
        } // class LayoutTabTestData

        public Chart getChart() {
            return new Chart();
        }

        //=================  ChartTabTestData ========================
        class Chart extends Tab {

            private Chart() {
                super("Chart");
            }

            public RWChart getChartData() {
                RWChart chart = new RWChart();

                Config chartConfig = tab.getChByName("chart");
                String chartType = chartConfig.getAttributeValue("type");
                String strChartOrientation = chartConfig.getAttributeValue("orientation");
                chart.setType(ChartType.valueOf(chartType));
                ChartOrientation chartOrientation = strChartOrientation != null ? ChartOrientation.valueOf(strChartOrientation) : null;
                chart.setOrientation(chartOrientation);
                chart.setGrouping(readGrouping(chartConfig.getChByName("grouping")));
                readDataDefinition(chart, chartConfig.getChByName("define-data"));
                readStylingOptions(chart, chartConfig.getChByName("styling"));
                return chart;
            }

            private ChartGrouping readGrouping(Config groupingConf) {
                if (groupingConf == null) return null;
                String groupingValue = groupingConf.getAttributeValue("value");
                ChartGrouping grouping = ChartGrouping.valueOf(groupingValue);
                grouping.setGroupBy(groupingConf.getAttributeValue("groupby"));
                return grouping;
            }

            private void readDataDefinition(RWChart chart, Config defDataConf) {
                chart.setAxis(defDataConf.getAttributeValue("axis"));
                List<Config> seriesConfigs = defDataConf.getChsByName("data-series");
                for (Config seriesConf : seriesConfigs) {
                    String series = seriesConf.getAttributeValue("series");
                    String drawAslLine = seriesConf.getAttributeValue("drawAsLine");
                    String newScale = seriesConf.getAttributeValue("newScale");
                    chart.addDataSeries(series,
                            (drawAslLine != null) && (drawAslLine.equalsIgnoreCase("true")),
                            (newScale != null) && (newScale.equalsIgnoreCase("true")));
                }
            }

            private void readStylingOptions(RWChart chart, Config styleConf) {
                chart.setPosition(readConfigText(styleConf.getChByName("position")));
                chart.setSize(readConfigText(styleConf.getChByName("size")));
                chart.setTextFont(readConfigText(styleConf.getChByName("font")));
                chart.setLegendPosition(readConfigText(styleConf.getChByName("legendPos")));
                chart.setLabelPosition(readConfigText(styleConf.getChByName("labelPos")));
                Config backgroundConf = styleConf.getChByName("background");
                chart.setBagroundFade(readConfigText(backgroundConf));
                chart.setFromColor(backgroundConf.getAttributeValue("fromColor"));
                chart.setToColor(backgroundConf.getAttributeValue("toColor"));
            }

            private String readConfigText(Config paramConf) {
                return paramConf == null ? null : paramConf.getText();
            }
        } // class ChartTabTestData


        public Save getSave() {
            return new Save();
        }


        //=================  SaveTabTestData ========================
        class Save extends Tab {
            private Save() {
                super("Save");
            }

            public String getReportName() {
                String name = tab.getChByName("name").getText();
                if (useTimestamp && !name.contains(String.valueOf(CoreProperties.getTestTemplate()))) {
                    name += "(" + CoreProperties.getTestTemplate() + ")";
                    setReportName(name);
                }
                return name;
            }

            public void setReportName(String name) {
                tab.setText("name", name);
            }

            public String getReportDescription() {
                return tab.getText("description");
            }

            public Folder getReportLocation() {
                if (!tab.hasChild("location")) {
                    tab.addElement("location");
                }
                return new Folder(tab.getChByName("location"));
            }

        } // class SaveTabTestData


    }
} // class TestData 
