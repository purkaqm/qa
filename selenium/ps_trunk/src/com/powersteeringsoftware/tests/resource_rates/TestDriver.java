package com.powersteeringsoftware.tests.resource_rates;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.ResourceManager;
import com.powersteeringsoftware.libs.tests.actions.UixManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created by admin on 11.04.2014.
 */
public class TestDriver extends PSTestDriver {
    private TestData data;

    @Override
    public PSTestData getTestData() {
        return data == null ? data = new TestData() : data;
    }

    @BeforeTest(description = "Set resource planning to on", timeOut = CoreProperties.BEFORE_TEST_TIMEOUT)
    public void beforeTest() {
        PSLogger.info("Set 'Resource Planning' to 'ON'");
        UixManager.setResourcePlanningOn();
    }

    @Test(description = "Add Rate Codes")
    public void addRateCodes() {
        ResourceManager.addRateCode(getTestData().getRateCode(TestData.RATE_CODE_1), getTestData().getRateCode(TestData.RATE_CODE_2));
    }

    @Test(description = "Create Rate Table #1", dependsOnMethods = "addRateCodes")
    public void createRateTable() {
        ResourceManager.createRateTable(getTestData().getRateTable(TestData.RATE_TABLE_1));
    }

    @Test(description = "Create Rate Table #2 and set it default", dependsOnMethods = "createRateTable")
    public void createRateTableAndSetDefault() {
        RateTable tb2 = getTestData().getRateTable(TestData.RATE_TABLE_2);
        ResourceManager.createRateTable(tb2);
        ResourceManager.setDefaultTable(tb2);
    }

    @Test(description = "Create Resource Pool")
    public void createResourcePool() {
        ResourcePool pool = getTestData().getResourcePool(TestData.POOL_1);
        ResourceManager.createPool(pool);
    }


    @Test(description = "Enable Labor Estimated Costs")
    public void enableCosts() {
        ResourceManager.enableLaborCosts();
    }

    /**
     * Created by admin on 11.04.2014.
     */
    public static class TestData extends PSTestData {
        public static final String RATE_CODE_1 = "code1";
        public static final String RATE_CODE_2 = "code2";
        public static final String RATE_TABLE_1 = "table1";
        public static final String RATE_TABLE_2 = "table2";
        public static final String POOL_1 = "test-pool-1";
    }
}
