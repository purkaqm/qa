package com.powersteeringsoftware.tests.measures.instances;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.measures.list.AttachMeasureTemplatePage;
import com.powersteeringsoftware.core.adapters.measures.list.ManageMeasuresPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.MeasureManager;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;

/**
 * Test attaching measure template.
 *
 * @author selyaev_ag
 *
 */
public class Test_AttachMeasureInstance extends PSTest {

    Logger log = Logger.getLogger(Test_AttachMeasureInstance.class);

    public Test_AttachMeasureInstance(){
    	name = "Attach measure";
    }

    public void run() {
    	String mtName =  MeasureManager.createMeasureTemplateManualGoal();

    	MeasureManager.attachMeasureTemplateByName(CoreProperties.getDefaultWorkItemIdWithPrefix(), mtName);

        new WISummaryAdapter().navigatePageManageMeasure(CoreProperties.getDefaultWorkItemIdWithPrefix());

        AttachMeasureTemplatePage attachAdapter = new AttachMeasureTemplatePage(SeleniumDriverSingleton.getDriver());

        String id = attachAdapter.getByNameIdForM(mtName);

        Assert.assertEquals(id.equals("null"),false,"ID is null:"+id);

        Assert.assertNotSame(StringUtils.isEmpty(new ManageMeasuresPageAdapter(SeleniumDriverSingleton.getDriver()).getByNameIdForM(mtName)),
                true,
                "Measure template " + mtName + " hasn't been attached");
    }
}
