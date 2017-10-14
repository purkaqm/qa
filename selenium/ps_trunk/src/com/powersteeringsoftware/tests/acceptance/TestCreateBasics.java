package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AbstractWorkPage;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.Assert;
import org.testng.ITest;
import org.testng.annotations.Test;

/**
 * Tests: Create Basic WorkItems
 *
 * @author vsumenkov
 */

@Test(groups = TestSettings.ACCEPTANCE_GROUP)
public class TestCreateBasics extends PSTestDriver implements ITest {
    private static final String BASIC = "basic";

    private TestData data;

    @Override
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }
    /*
     * ------   Public methods - for TestNG execution
     */

    @Test(groups = BASIC, description = "Create work item")
    public void createWorkItem() {
        createBasicWorkItem(TestData.WORK_ITEM_ID);
    }

    @Test(groups = BASIC, description = "Create folder")
    public void createFolder() {
        createBasicWorkItem(TestData.FOLDER_ID);
    }

    @Test(groups = BASIC, description = "Create ms-project")
    public void createMSPProject() {
        createBasicWorkItem(TestData.MSP_PROJECT_ID);
    }

    @Test(description = "Create work item advanced", dependsOnGroups = BASIC)
    public void createWorkItemAdvanced() {
        createBasicWorkItem(TestData.WORK_ITEM_2_ID);
        //todo: validate
    }

    @Test(dependsOnMethods = "createWorkItemAdvanced", description = "Edit just created work item")
    public void editWorkItemAdvanced() {
        Work w1 = getTestData().getWork(TestData.WORK_ITEM_2_ID);
        Work w2 = getTestData().getWork(TestData.WORK_ITEM_3_ID);
        WorkManager.edit(w1, w2);
        //todo: validate
    }
    //-------------------------------------------------

    private void createBasicWorkItem(String id) {
        Work project = getTestData().getWork(id);
        AbstractWorkPage page = WorkManager.createProject(project);
        // Ensure the WorkItem is created
        Assert.assertEquals(page.getContainerHeader(), project.getName(), "Incorrect header for container");
    }

    @Override
    public String getTestName() {
        return "Create Basic WorkItems";
    }

}
