package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.pages.AddEditMeasureInstancePage;
import com.powersteeringsoftware.libs.pages.MeasureInstancesPage;
import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.measures.TestData;
import org.testng.Assert;

/**
 * Attach MT with all locked fields (formula, variance).</br>
 * <p/>
 * TODO: please check this test becouse of refactoring!!!
 */
@Deprecated
public class Test_AttachMTFormulaVariance extends PSTest {
    private TestData data;

    public Test_AttachMTFormulaVariance(TestData data) {
        name = "Attach measure:formula, variance";
        this.data = data;
    }

    public void run() {
        String mtName = data.getMeasureName(5);
        MeasureManager.createMeasureTemplateFormulaVariance(mtName, data.getMeasureDescription(5));
        MeasureInstancesPage page = MeasureManager.attachMeasureTemplateByName(data.getRootWork(), mtName);
        AddEditMeasureInstancePage edit = (AddEditMeasureInstancePage) page.openMenu(mtName).edit();
        Assert.assertTrue(edit.getContent().isDisabledAllLockedFields(), "Problem with validation");
    }
}
