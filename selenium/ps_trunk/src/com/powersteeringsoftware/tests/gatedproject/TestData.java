package com.powersteeringsoftware.tests.gatedproject;

import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 10.06.12
 * Time: 18:36
 * To change this template use File | Settings | File Templates.
 */
public class TestData extends PSTestData {
    public static final String ASAP = "asap";
    public static final String SNET = "snet";
    public static final String FNLT = "fnlt";
    public static final String FD = "fd";
    public static final String SGP = "sgp-test";

    private String getWorkName(String id) {
        Config c = conf.getElementByIdAndName("work-name", id);
        return c == null ? null : c.getText();
    }

    public GatedProject getGatedProject(String id) {
        GatedProject res = (GatedProject) getWork(id);
        if (res != null) return res;
        Config w = (Config) getTemplate(id).getStructure().getConfig().clone();
        w.setAttributeValue("id", id);
        res = new GatedProject(w);
        String _name = getWorkName(id);
        if (_name != null)
            res.setName(_name);
        res.setDeleted();
        res.setTemplateRoot(false);
        if (!res.getEnforceSequential() && res.isUseAdvancedDatesComponent()) { // hotfix for bug for nsgp (fd)
            long d = res.getConstraintEndDateAsLong() - res.getConstraintStartDateAsLong();
            long start = System.currentTimeMillis(); //CoreProperties.getTestTemplate();
            res.setConstraintStartDate(start);
            res.setConstraintEndDate(start + d);
        }
        if (useTimestamp) {
            res.setNameSuffix(CoreProperties.getTestTemplate());
        }
        customObjects.add(res);
        return res;
    }

    public Work getDeliverable() {
        return Work.createTimeStamped("TetDeliverable");
    }

}
