package com.powersteeringsoftware.tests.measures;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.objects.measures.MeasureInstance;
import com.powersteeringsoftware.libs.objects.measures.MeasureTemplate;
import com.powersteeringsoftware.libs.pages.MeasureInstancePage;
import com.powersteeringsoftware.libs.pages.SummaryWorkPage;
import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.powersteeringsoftware.tests.measures.instances.*;
import com.powersteeringsoftware.tests.measures.templates.Test_Issue57491;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

@Test(groups = {"measures.test", TestSettings.NOT_IE_RC_GROUP})
// Used: 31, 33, 34, 36, 96, 97, 98, 103, 125, 133, 210
public class TestDriver extends PSTestDriver {

    private static final String CREATE_TEMPLATE_GROUP = "MT.create";
    private static final String ATTACH_TEMPLATE_GROUP = "MT.attach";
    private static final String DELETE_GROUP = "delete";
    private static final String INSTANCE_GROUP = "instance";

    private TestData data;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

    class MeasureTemplateData {
        private MeasureTemplate measure;

        private MeasureTemplateData(String name) {
            measure = getTestData().getMeasureTemplate(name);
        }

        public String toString() {
            return measure.getDescription();
        }
    }

    @DataProvider
    public Object[][] measures() {
        Object[][] res = new Object[4][0];
        res[0] = new Object[]{new MeasureTemplateData(TestData.MT_FORMULA_GOAL)};
        res[1] = new Object[]{new MeasureTemplateData(TestData.MT_FORMULA_VARIANCE)};
        res[2] = new Object[]{new MeasureTemplateData(TestData.MT_MANUAL_GOAL)};
        res[3] = new Object[]{new MeasureTemplateData(TestData.MT_MANUAL_VARIANCE)};
        return res;
    }

    @Test(dataProvider = "measures", description = "Create measure template", groups = CREATE_TEMPLATE_GROUP)
    public void createMeasureTemplate(MeasureTemplateData data) {
        MeasureTemplate template = data.measure;
        MeasureManager.createMeasureTemplate(template);
        MeasureManager.validateMeasureTemplate(template);
    }

    @Test(dataProvider = "measures", description = "Attach measure template", alwaysRun = true, dependsOnGroups = CREATE_TEMPLATE_GROUP, groups = ATTACH_TEMPLATE_GROUP)
    public void attachMeasureTemplate(MeasureTemplateData data) {
        MeasureTemplate template = data.measure;
        checkNeedSkip(template);
        MeasureInstance res = MeasureManager.attachMeasureTemplate(template, getTestData().getRootWork());
        MeasureManager.validateMeasureInstance(res);
    }


    @Test(description = "Delete measure template", groups = DELETE_GROUP, dependsOnGroups = {CREATE_TEMPLATE_GROUP, ATTACH_TEMPLATE_GROUP}, alwaysRun = true)
    public void deleteMeasureTemplate() {
        MeasureTemplate template = getTestData().getMeasureTemplate(TestData.MT_FORMULA_GOAL);
        checkNeedSkip(template);
        for (MeasureInstance i : template.getInstances()) {
            checkNeedSkip(i);
        }
        MeasureManager.deleteMeasureTemplate(template, true);
    }

    @Test(description = "Detach measure template", groups = DELETE_GROUP, dependsOnGroups = {CREATE_TEMPLATE_GROUP, ATTACH_TEMPLATE_GROUP}, alwaysRun = true)
    public void detachMeasureTemplate() {
        MeasureTemplate template = getTestData().getMeasureTemplate(TestData.MT_MANUAL_VARIANCE);
        checkNeedSkip(template);
        MeasureManager.deleteMeasureTemplate(template, false);
    }

    @Test(description = "Copy measure template", dependsOnGroups = {CREATE_TEMPLATE_GROUP, ATTACH_TEMPLATE_GROUP}, alwaysRun = true)
    public void copyMeasureTemplate() {
        MeasureTemplate src = getTestData().getMeasureTemplate(TestData.MT_MANUAL_GOAL);
        checkNeedSkip(src);
        MeasureTemplate dst = getTestData().getMeasureTemplate(TestData.MT_TO_COPY);
        MeasureManager.copyMeasureTemplate(src, dst);
        MeasureManager.validateMeasureTemplate(dst);
    }

    @Test(description = "Check recalculate measures", dependsOnGroups = {CREATE_TEMPLATE_GROUP, ATTACH_TEMPLATE_GROUP, INSTANCE_GROUP}, alwaysRun = true)
    public void checkRecalculate() {
        List<MeasureInstance> test = MeasureManager.getMeasures(getTestData().getRootWork());
        if (test.isEmpty())
            PSSkipException.skip("No measures for work " + getTestData().getRootWork());
        List<Measure> all = TestSession.getMeasuresList();
        PSLogger.info("All measures " + all);
        SummaryWorkPage sum = WorkManager.open(getTestData().getRootWork());
        Assert.assertTrue(sum.hasMeasureModule(), "Can't find measure module");
        SummaryWorkPage.Measures mod = sum.getMeasureModule();

        Measure m1 = null;
        Measure m2 = null;
        Measure m3 = null;
        for (Measure m : all) {
            if (test.contains(m)) {
                Assert.assertTrue(mod.hasLink(m.getName()), "Can't find measure " + m);
            } else {
                if (!m.isLibrary())
                    Assert.assertFalse(mod.hasLink(m.getName()), "There is measure " + m + " on summary");
                continue;
            }
            if (((MeasureInstance) m).canRecalculate()) {
                Assert.assertTrue(mod.getRecalculate(m.getName()) != null, "Can't find recalculate checkbox for measure " + m);
                if (m1 == null) {
                    mod.select(m.getName(), true);
                    m1 = m;
                } else if (m2 == null) {
                    m2 = m;
                }
            } else {
                Assert.assertTrue(mod.getRecalculate(m.getName()) == null, "There is recalculate checkbox for measure " + m);
                if (m3 == null) m3 = m;
            }
        }
        Assert.assertEquals(mod.hasSubmit(), m1 != null, m1 != null ? "Should be submit button" : "There is submit button");
        Assert.assertEquals(mod.getRecalculateAll() != null, m1 != null, m1 != null ? "Should be recalculate all checkbox" : "There is recalculate all checkbox");
        mod.submit();
        if (m1 != null) {
            MeasureInstancePage instance = mod.open(m1.getName());
            Assert.assertTrue(instance.hasTable(), "Measure " + m1 + " should be recalculated");
            sum = instance.openSummaryPage();
            mod = sum.getMeasureModule();
        }
        if (m2 != null) {
            MeasureInstancePage instance = mod.open(m2.getName());
            Assert.assertFalse(instance.hasTable(), "Measure " + m2 + " should be not recalculated");
            instance.recalculateMeasures();
            Assert.assertTrue(instance.hasTable(), "Measure " + m2 + " should be recalculated");
            if (m3 != null) {
                mod = instance.openSummaryPage().getMeasureModule();
                instance = mod.open(m3.getName());
                Assert.assertFalse(instance.hasTable(), "Measure " + m3 + " should be not recalculated");
            }
        }
    }

    @Test(description = "Create Measure Instance (Define new)", groups = INSTANCE_GROUP)
    public void addMeasureInstance() {
        MeasureInstance instance = getTestData().getMeasureInstance(TestData.INSTANCE);
        MeasureManager.createMeasureInstance(instance);
        MeasureManager.validateMeasureInstance(instance);
        Assert.assertFalse(MeasureManager.isMeasureTemplateExist(instance), "There is a template " + instance);
    }

    @Test(description = "Edit Measure", dependsOnGroups = {INSTANCE_GROUP})
    public void editMeasureInstance() {
        MeasureInstance instance = getTestData().getMeasureInstance(TestData.INSTANCE);
        checkNeedSkip(instance);
        MeasureInstance dest = getTestData().getMeasureInstance(TestData.INSTANCE_TO_EDIT);
        MeasureManager.editMeasureInstance(instance, dest);
        MeasureManager.validateMeasureInstance(instance);
    }

    private void checkNeedSkip(Measure m) {
        if (!m.exist())
            PSSkipException.skip("Measure" + (m.isLibrary() ? " template " : " instance ") + m + " is not created");
    }


    @Test(groups = TestSettings.BROKEN_ALL_GROUP, description = "t_mf_103.issue#57491")
    public void Test_Issue57491() {
        new Test_Issue57491().execute();
    }

    @Test(groups = {TestSettings.DEVELOPMENT_GROUP}, description = "t_mf_114.issue#57270, disabled")
    public void Test_Issue57270() {
        new Test_Issue57270(getTestData()).execute();
    }

    @Test(groups = {TestSettings.DEVELOPMENT_GROUP}, description = "t_mf_189.Attach MT, disabled")
    public void Test_AttachMeasureInstance() {
        new Test_AttachMeasureInstance(getTestData()).execute();
    }

    @Test(groups = {TestSettings.BROKEN_ALL_GROUP}, description = "t_mf_210.Attach MT(manual,variance")
    public void Test_AttachMTManualVariance() {
        new Test_AttachMTManualVariance(getTestData()).execute();
    }

    @Test(groups = {TestSettings.DEVELOPMENT_GROUP}, description = "t_mf_212.Attach MT(manual,goal), disabled")
    public void Test_AttachMTManualGoal() {
        new Test_AttachMTManualGoal(getTestData()).execute();
    }

    @Test(groups = {TestSettings.DEVELOPMENT_GROUP}, description = "t_mf_213.Attach MT(formula,variance), disabled")
    public void Test_AttachMTFormulaVariance() {
        new Test_AttachMTFormulaVariance(getTestData()).execute();
    }

    @Test(groups = {TestSettings.DEVELOPMENT_GROUP}, description = "t_mf_214.Attach MT(formula,goal), disabled")
    public void Test_AttachMTFormulaGoal() {
        new Test_AttachMTFormulaGoal(getTestData()).execute();
    }

}
