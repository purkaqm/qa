package com.powersteeringsoftware.tests.measures;

import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.objects.measures.MeasureInstance;
import com.powersteeringsoftware.libs.objects.measures.MeasureTemplate;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.TimeStampName;

/**
 * Created by IntelliJ IDEA.
 * User: zss
 * Date: 23.10.11
 * Time: 16:23
 * To change this template use File | Settings | File Templates.
 */
public class TestData extends PSTestData {

    public TestData() {
        super(true);
    }

    public static final String MT_FORMULA_GOAL = "CreateMTFormulaGoal";
    public static final String MT_FORMULA_VARIANCE = "CreateMTFormulaVariance";
    public static final String MT_MANUAL_GOAL = "Test_CreateMTManualGoal";
    public static final String MT_MANUAL_VARIANCE = "Test_CreateMTManualVariance";
    public static final String MT_TO_COPY = "copy";

    public static final String INSTANCE = "instance";
    public static final String INSTANCE_TO_EDIT = "edit";


    public MeasureTemplate getMeasureTemplate(String id) {
        Measure res = getMeasure(id);
        if (res.isLibrary())
            return (MeasureTemplate) res;
        return null;
    }

    public MeasureInstance getMeasureInstance(String id) {
        Measure res = getMeasure(id);
        if (!res.isLibrary())
            return (MeasureInstance) res;
        return null;
    }

    public String getMeasureName(Object id) {
        String namePrefix = getMeasure(id).getText("name");
        return useTimestamp ? new TimeStampName(namePrefix).getTimeStampedName() : namePrefix;
    }

    private Config getMeasure(Object id) {
        for (Config c : conf.getChsByName("measure")) {
            if (c.getAttribute("id").equals(String.valueOf(id))) return c;
        }
        return null;
    }

    public String getMeasureDescription(Object id) {
        String descriptionPrefix = getMeasure(id).getText("description");
        return useTimestamp ? new TimeStampName(descriptionPrefix).getTimeStampedName() : descriptionPrefix;
    }


}
