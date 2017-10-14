package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.actions.WorkTemplateManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

@Test(groups = {TestSettings.GATED_PROJECT_GROUP}
        //,dependsOnGroups = {TestSettings.CONTENT_FILTER_GROUP}
)
public class TestDriver extends PSTestDriver {

    private TestData data;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

    private class Data {
        private String id;

        private Data(String id) {
            this.id = id;
        }

        public Template getTemplate() {
            return getTestData().getTemplate(id);
        }

        public GatedProject getProject() {
            return getTestData().getGatedProject(id);
        }

        public String toString() {
            return getTemplate().getStructure().getName0();
        }

        private boolean isTemplateOk() {
            return getTestData().getTemplate(id).getBooleanFalse("ok");
        }

        private void setTemplateOk() {
            getTestData().getTemplate(id).set("ok", Boolean.TRUE);
        }
    }


    @DataProvider
    public Object[][] data() {
        boolean sgp = TestSession.getAppVersion().verGreaterOrEqual(PowerSteeringVersions._9_3);
        Object[][] res = new Object[sgp ? 5 : 4][0];
        res[0] = new Object[]{new Data(TestData.ASAP)};
        res[1] = new Object[]{new Data(TestData.SNET)};
        res[2] = new Object[]{new Data(TestData.FNLT)};
        res[3] = new Object[]{new Data(TestData.FD)};
        if (sgp)
            res[4] = new Object[]{new Data(TestData.SGP)};
        return res;
    }

    @Test(dataProvider = "data", description = "Create gated project template")
    public void createGatedTemplate(Data data) {
        Template template = data.getTemplate();
        WorkTemplateManager.create(template);
        data.setTemplateOk();
    }

    @Test(dataProvider = "data", description = "Create gated project", alwaysRun = true, dependsOnMethods = "createGatedTemplate")
    public void createGatedProject(Data data) {
        if (!data.getTemplate().exist()) PSSkipException.skip("Skip due to dependencies, template does not exist");
        if (!data.isTemplateOk())
            PSSkipException.skip("Skip due to dependencies, there have been some error during template creation");
        WorkManager.createProject(data.getProject());
    }


    @Test(dependsOnMethods = "createGatedProject", alwaysRun = true)
    public void addDeliverables() {
        GatedProject gp = getTestData().getGatedProject(TestData.ASAP);
        if (!gp.exist()) PSSkipException.skip("Skip due to dependencies, work does not exist");
        new TestAddDeliverables(gp.getFirstGate(), getTestData().getDeliverable()).execute();
    }

    @Test(dependsOnMethods = "addDeliverables")
    public void removeDeliverables() {
        GatedProject gp = getTestData().getGatedProject(TestData.ASAP);
        new TestRemoveDeliverables(gp.getFirstGate(), getTestData().getDeliverable()).execute();
    }

}
