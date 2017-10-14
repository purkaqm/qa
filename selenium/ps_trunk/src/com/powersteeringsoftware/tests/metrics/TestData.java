package com.powersteeringsoftware.tests.metrics;

import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.05.12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class TestData extends PSTestData {
    private static final Double MAX = 1000000d;
    private static final int ROUND_DIGITS = 5;
    private static final String TEMPLATE_ID = "temp-1";
    private static final String INSTANCE_ID = "inst-1";
    static final String COST_METRIC_1 = "simple-cost";
    static final String COST_METRIC_2 = "simple-cost-2";


    private List<Data> metrics = new ArrayList<Data>();

    public TestData() {
        super();
        for (Config c : conf.getChsByName("data")) {
            metrics.add(new Data(c));
        }
    }


    public List<Data> getDataList() {
        return metrics;
    }

    public List<Data> getDataList(String... ids) {
        List<Data> res = new ArrayList<Data>();
        for (Data d : metrics) {
            for (String id : ids) {
                if (id.equals(d.id)) {
                    res.add(d);
                    break;
                }
            }

        }
        return res;
    }

    public Data getData(String id) {
        for (Data d : metrics) {
            if (id.equals(d.id)) return d;
        }
        return null;
    }


    public class Data {
        private Config data;
        private String id;
        private PowerSteeringVersions ver;
        private String group;

        private Data(Config data) {
            this.data = data;
            id = data.getAttribute("id");
            for (Config c : data.getChsByName("include")) {
                if (ver == null)
                    ver = PowerSteeringVersions.valueOf(c);
                if (group == null)
                    group = c.getAttributeValue("group");
            }
        }

        public PowerSteeringVersions getVersion() {
            return ver;
        }

        public String getGroup() {
            return group;
        }

        public String toString() {
            return id;
        }

        public boolean isSkip() {
            return data.hasChild("skip") && Boolean.parseBoolean(data.getText("skip"));
        }

        private Config getSkip() {
            Config s = data.getChByName("skip");
            if (s == null) {
                s = data.addElement("skip");
            }
            return s;
        }

        public void setSkip() {
            getSkip().setText(String.valueOf(Boolean.TRUE));
        }

        public void setNotSkip() {
            getSkip().setText(String.valueOf(Boolean.FALSE));
        }


        public MetricTemplate getMetric() {
            Config tmp = data.getElementById(TEMPLATE_ID);
            if (tmp != null) return new MetricTemplate(tmp);
            MetricTemplate template = new MetricTemplate(tmp = (Config) data.getChByName("metric").clone());
            data.addChild(tmp);
            template.setName(template.getName() + "_" + CoreProperties.getTestTemplate());
            template.setPeriod();
            return template;
        }

        public void setInstance(MetricInstance instance) {
            instance.getConfig().setAttributeValue("id", INSTANCE_ID);
            if (data.getElementById(INSTANCE_ID) != null)
                data.removeChild(data.getElementById(INSTANCE_ID));
            data.addChild(instance.getConfig());
        }

        public MetricInstance getInstance() {
            if (data.getElementById(INSTANCE_ID) == null)
                PSSkipException.skip("No instance specified");
            return new MetricInstance(data.getElementById(INSTANCE_ID));
        }

        public Work getWork() {
            return new MetricInstance(data.getChByName("metric")).getWork();
        }

        public MetricInstance getFirstData() {
            return getInstanceData("data1");
        }

        public MetricInstance getSecondData() {
            return getRandomInstanceData("data1");
        }

        public MetricInstance getThirdData() {
            return getInstanceData("data2");
        }

        private MetricInstance getInstanceData(String id) {
            Config c = data.getElementById(id);
            if (c == null) return null;
            if (!c.hasChild("work")) {
                Config w = (Config) data.getChByName("metric").getChByName("work").clone();
                c.addChild(w);
            }
            MetricInstance i = getInstance();
            MetricInstance res = new MetricInstance(c);
            res.setDisplayTotal(i.getDisplayTotal());
            return res;
        }

        private MetricInstance getRandomInstanceData(String id) {
            MetricInstance init = getInstanceData(id);
            MetricInstance metric = (MetricInstance) init.clone();

            List<MetricInstance.DataItemsList> data = metric.getData();
            while (data.size() != 1) {
                data.remove(1);
            }
            MetricInstance.DataItemsList rows = data.get(0);

            MetricInstance.ItemRow row = rows.getRows().get(0);

            rows.clear();
            rows.add(row);
            rows.addAll(row.getSubItems());
            setRandomInstanceData(row, metric.getColumns());
            return metric;
        }
    }


    private static void setRandomInstanceData(MetricInstance.ItemRow row, List<MetricInstance.Column> columns) {
        if (row.isEditable()) {
            for (MetricInstance.Column m : columns) {
                MetricInstance.ItemCell cell = row.getCell(m);
                if (cell == null) {
                    cell = row.newCell(m);
                }
                if (cell.getDValue() != null) {
                    cell.setDValue(null); // clear value to test empty
                } else {
                    cell.setDValue(round((0.5 - getRandom().nextDouble()) * MAX, ROUND_DIGITS));
                }
            }
        }
        for (MetricInstance.ItemRow sub : row.getSubItems()) {
            setRandomInstanceData(sub, columns);
        }
    }

    private static double round(double d, int precise) {
        double p = Math.pow(10, precise);
        return (double) Math.round(d * p) / p;
    }

    public List<Cost> getCosts() {
        List<Cost> res = new ArrayList<Cost>();
        for (Config c : conf.getChsByName(Cost.NAME)) {
            res.add(Cost.getCost(c));
        }
        return res;
    }

}
