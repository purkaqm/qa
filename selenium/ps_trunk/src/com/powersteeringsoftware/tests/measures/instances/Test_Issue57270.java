package com.powersteeringsoftware.tests.measures.instances;

import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.tests.actions.MeasureManager;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.libs.util.TimeStampName;
import com.powersteeringsoftware.tests.measures.TestData;
import org.testng.Assert;

/**
 * Test issue 57270
 */
@Deprecated
public class Test_Issue57270 extends PSTest {
    private TestData data;

    public Test_Issue57270(TestData data) {
        name = "Issue 57270";
        this.data = data;
    }

    public void run() {
        //step-1: create measure template
        String mtName = data.getMeasureName(8);
        String mtDesc = data.getMeasureDescription(8);
        String copiedMName = new TimeStampName(mtName).getTimeStampedName();

        MeasureTemplatesPage list = MeasureManager.createMeasureTemplateFormulaGoal(mtName, mtDesc);


        //step-2: lock all fields in the measure template
        AddEditMeasureTemplatePage edit = list.openMenu(mtName).edit();
        edit.getContent().lockAllFields();

        //step-3: attach measure template
        MeasureInstancesPage list2 = MeasureManager.attachMeasureTemplateByName(data.getRootWork(), mtName);

        //step-4: copy attached measure template
        AddEditMeasurePage edit2 = list2.openMenu(mtName).copy();
        edit2.getContent().setName(copiedMName);
        list2 = (MeasureInstancesPage) edit2.getContent().submit();

        //step-5: assert all fields are enabled for copied measure
        AddEditMeasureInstancePage edit3 = (AddEditMeasureInstancePage) list2.openMenu(copiedMName).edit();
        edit3.getContent().isEnabledAllLockedFields();

        //step-6: change measure template
        MeasureTemplatesPage list3 = new MeasureTemplatesPage();
        list3.open();
        AddEditMeasureTemplatePage edit4 = list3.openMenu(mtName).edit();

        //step-7: assert all fields in the copied measure hasn't changed
        //TODO: continue creating bugs
        Assert.assertEquals(true, false, "Special assertion");
    }
}
