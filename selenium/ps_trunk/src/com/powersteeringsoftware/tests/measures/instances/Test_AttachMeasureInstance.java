package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.tests.measures.TestData;

/**
 * Test attaching measure template.
 *
 * @author selyaev_ag
 */
@Deprecated
public class Test_AttachMeasureInstance extends PSTest {
    private TestData data;

    public Test_AttachMeasureInstance(TestData d) {
        name = "Attach measure";
        data = d;
    }

    public void run() {
        String name = data.getMeasureName(3);
        MeasureManager.createMeasureTemplateManualGoal(name, data.getMeasureDescription(3));
        MeasureManager.attachMeasureTemplateByName(data.getRootWork(), name);
    }
}
