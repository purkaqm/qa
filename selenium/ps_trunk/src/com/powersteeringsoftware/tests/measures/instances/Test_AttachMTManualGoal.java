package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.pages.AddEditMeasureInstancePage;
import com.powersteeringsoftware.libs.pages.AddEditMeasureTemplatePage;
import com.powersteeringsoftware.libs.pages.MeasureTemplatesPage;
import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.measures.TestData;
import org.testng.Assert;

/**
 * Attach MT with all locked fields (manual, goal)
 */
@Deprecated
public class Test_AttachMTManualGoal extends PSTest {
    private TestData data;

    public Test_AttachMTManualGoal(TestData data) {
        name = "Attach Measure:manual and goal";
        this.data = data;
    }


    public void run() {
        String mtName = data.getMeasureName(6);
        String mtDesc = data.getMeasureDescription(6);
        MeasureTemplatesPage list = MeasureManager.createMeasureTemplateManualGoal(mtName, mtDesc);
        AddEditMeasureTemplatePage edit = list.openMenu(mtName).edit();

        edit.getContent().lockAllFields();
        edit.getContent().submit();

        MeasureManager.attachMeasureTemplateByName(data.getRootWork(), mtName);

        AddEditMeasureInstancePage edit2 = MeasureManager.openInstanceEditPage(mtName, data.getRootWork());
        Assert.assertTrue(edit2.getContent().isDisabledAllLockedFields(), "Problem with validation");
    }

}
