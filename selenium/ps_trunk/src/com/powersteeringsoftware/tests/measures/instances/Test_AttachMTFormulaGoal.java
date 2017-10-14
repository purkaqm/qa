package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.measures.TestData;

/**
 * Attach MT with all locked fields (formula, goal).
 */
@Deprecated
public class Test_AttachMTFormulaGoal extends PSTest {
    private TestData data;


    public Test_AttachMTFormulaGoal(TestData d) {
        name = "Attach measure:formula, goal";
        data = d;
    }

    public void run() {
        String mtName = data.getMeasureName(4);
        MeasureManager.createMeasureTemplateFormulaGoal(mtName, data.getMeasureDescription(4));
        MeasureManager.attachMeasureTemplateByName(data.getRootWork(), mtName);
    }

}
