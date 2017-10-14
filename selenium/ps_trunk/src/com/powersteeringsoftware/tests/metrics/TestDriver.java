package com.powersteeringsoftware.tests.metrics;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.Cost;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.MetricInstancePage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.MetricManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 03.05.12
 * Time: 15:23
 * To change this template use File | Settings | File Templates.
 */
public class TestDriver extends PSTestDriver {

    public static final String BEFORE = "before";
    public static final String GENERAL = "general";

    private TestData data;
    private Map<MetricInstance, MetricInstance> costMetrics = new LinkedHashMap<MetricInstance, MetricInstance>();
    private boolean skipCostsChanging;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }


    @DataProvider(name = "metricsData")
    public Object[][] testData(Method m) {
        List<TestData.Data> data;
        if (CoreProperties.getBrowser().isIE()) { // simplify for ie
            data = getTestData().getDataList("simple", "simple-cost");
        } else {
            data = getTestData().getDataList();
        }
        List groups = Arrays.asList(((Test) m.getDeclaredAnnotations()[0]).groups());
        List<Object[]> res = new ArrayList<Object[]>();
        for (TestData.Data d : data) {
            if (d.getVersion() != null && TestSession.getAppVersion().verLessThan(d.getVersion())) {
                continue; // exclude not compatible ver;
            }
            if (d.getGroup() != null && !groups.contains(d.getGroup())) {
                continue; // exclude groups
            }
            res.add(new Object[]{d});
        }
        return res.toArray(new Object[][]{});
    }

    private void startTestCase(TestData.Data data) {
        if (data.isSkip()) PSSkipException.skip("Skip due to dependencies");
        data.setSkip();
    }

    private void endTestCase(TestData.Data data) {
        data.setNotSkip();
    }

    @Test(description = "Add Estimated/Manual Costs")
    public void addManualCosts() {
        Work work = getTestData().getData(TestData.COST_METRIC_1).getWork();
        if (skipCostsChanging) {
            WorkManager.collectCosts(work);
            PSLogger.info(work.getCosts());
            PSSkipException.skip("Skip this tc");
        }
        if (!work.getControlCost()) {
            WorkManager.enableCosts(work, true);
        }
        List<Cost> costs = getTestData().getCosts();
        WorkManager.addCosts(work, costs);
        PSLogger.info("Costs: " + work.getCosts());
    }

    @Test(description = "Create template",
            dataProvider = "metricsData",
            groups = {BEFORE, GENERAL})
    public void createTemplate(TestData.Data data) {
        MetricTemplate template = data.getMetric();
        PSLogger.info("Create metric " + template);
        MetricManager.createTemplate(template);
    }

    @Test(description = "Attach and prepare metric",
            dependsOnMethods = "createTemplate",
            dataProvider = "metricsData",
            groups = {BEFORE, GENERAL})
    public void attach(TestData.Data data) {
        startTestCase(data);
        MetricTemplate template = data.getMetric();
        Work work = data.getWork();
        MetricInstance instance = MetricManager.attachTemplate(template, work);
        MetricInstancePage page = MetricManager.open(instance);
        if (template.isBreakdown()) {
            MetricInstancePage.EditProperties edit = page.edit();
            edit.openTagBreakdownTab().setTag(template.getBreakdownTag());
            edit.update();
        }
        data.setInstance(instance);
        MetricManager.simpleValidateTemplate(page, instance);
        endTestCase(data);
    }

    @Test(
            groups = GENERAL,
            dependsOnGroups = BEFORE,
            alwaysRun = true,
            dataProvider = "metricsData",
            description = "Set data and validate")
    public void setAndValidate(TestData.Data testData) {
        startTestCase(testData);
        MetricInstance instance = testData.getInstance();
        MetricInstance dataMetric = testData.getFirstData();
        setAndValidate(instance, dataMetric, true);
        endTestCase(testData);
        String id = testData.toString();
        if (id.equals(TestData.COST_METRIC_1) || id.equals(TestData.COST_METRIC_2)) {
            costMetrics.put(instance, testData.getThirdData());
        }
    }

    private void setAndValidate(MetricInstance instance, MetricInstance dataMetric, boolean checkAll) {
        MetricInstancePage page = MetricManager.open(instance);

        for (MetricInstance.DataItemsList data : dataMetric.getData()) {
            page.openView(data.getViewName());
            MetricManager.setSaveMetricInstanceData(page, data, checkAll);
            instance.setData(data);
        }

        for (MetricInstance.DataItemsList data : instance.getData()) {
            page.openView(data.getViewName());
            MetricManager.validateMetricInstanceData(page, data);
        }
    }

    @Test(
            dependsOnGroups = BEFORE,
            dependsOnMethods = "setAndValidate",
            alwaysRun = true,
            dataProvider = "metricsData",
            description = "Set random data, clear some cells")
    public void setAndClear(TestData.Data testData) {
        startTestCase(testData);
        MetricInstance instance = testData.getInstance();

        MetricInstance dataMetric = testData.getSecondData();

        MetricInstance.DataItemsList data = dataMetric.getData().get(0);

        MetricInstancePage page = MetricManager.open(instance);
        page.openView(data.getViewName());
        MetricManager.setSaveMetricInstanceData(page, data, false);
        instance.setData(data);
        MetricManager.validateMetricInstanceData(page, instance.getData(data.getViewName()));
        endTestCase(testData);
    }

    @Test(
            dependsOnGroups = BEFORE,
            dependsOnMethods = "setAndValidate",
            alwaysRun = true,
            dataProvider = "metricsData",
            description = "Set replications to metric")
    public void setReplications(TestData.Data testData) {
        startTestCase(testData);
        MetricInstance instance = testData.getInstance();
        String view = instance.getViews().get(0).getName();
        MetricInstance dataMetric = testData.getThirdData();

        MetricInstancePage page = MetricManager.open(instance);

        MetricInstance.DataItemsList data = dataMetric.getData(view);
        page.openView(data.getViewName());
        MetricManager.setMetricInstanceData(page, data);
        MetricManager.validateMetricInstanceData(page, data, false);
        page.getGrid().reset();
        MetricManager.validateMetricInstanceData(page, instance.getData(view));
        endTestCase(testData);
    }


    @Test(dependsOnMethods = {"setAndValidate", "addManualCosts"},
            description = "Disable control cost, check metric(s) again")
    public void disableControlCost() {
        if (skipCostsChanging) PSSkipException.skip("Skip this tc");
        if (costMetrics.isEmpty())
            PSSkipException.skip("Depends on method setAndValidate for " + TestData.COST_METRIC_1 + " metric");
        TestData.Data data = getTestData().getData(TestData.COST_METRIC_1);
        Work work = data.getWork();
        WorkManager.enableCosts(work, false);
        for (MetricInstance m : costMetrics.keySet()) {
            if (costMetrics.get(m) == null) continue;
            PSLogger.info("Validate metric '" + m + "'");
            setAndValidate(m, costMetrics.get(m), false);
        }
    }

}
