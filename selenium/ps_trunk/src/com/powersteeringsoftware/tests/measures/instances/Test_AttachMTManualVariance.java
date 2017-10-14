package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.pages.AddEditMeasureInstancePage;
import com.powersteeringsoftware.libs.pages.AddEditMeasureTemplatePage;
import com.powersteeringsoftware.libs.pages.MeasureInstancesPage;
import com.powersteeringsoftware.libs.pages.MeasureTemplatesPage;
import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.measures.TestData;
import org.testng.Assert;

/**
 * Attach measure template (manual, variance) with all locked fields.
 *
 * @author selyaev_ag
 */
@Deprecated
public class Test_AttachMTManualVariance extends PSTest {
    private TestData data;

    public Test_AttachMTManualVariance(TestData data) {
        name = "Attach measure:manual and variance";
        this.data = data;
    }

    public void run() {
        String mtName = data.getMeasureName(7);
        String mtDesc = data.getMeasureDescription(7);

        MeasureTemplatesPage list = MeasureManager.createMeasureTemplateManualVariance(mtName, mtDesc);
        AddEditMeasureTemplatePage edit = list.openMenu(mtName).edit();

        edit.getContent().lockAllFields();
        edit.getContent().submit();

        MeasureInstancesPage list2 = MeasureManager.attachMeasureTemplateByName(data.getRootWork(), mtName);

        AddEditMeasureInstancePage edit2 = (AddEditMeasureInstancePage) list2.openMenu(mtName).edit();
        Assert.assertTrue(edit2.getContent().isDisabledAllLockedFields(), "Problem with validation");
    }

}
