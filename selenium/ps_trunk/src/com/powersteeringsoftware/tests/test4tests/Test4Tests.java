package com.powersteeringsoftware.tests.test4tests;

import com.powersteeringsoftware.libs.pages.WBSPage;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.tests.project_central.TestData;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: rew24
 * Date: 24.04.2010
 * Time: 14:29:06
 * To change this template use File | Settings | File Templates.
 */
public class Test4Tests extends PSTestDriver {

    @Test
    public void test() throws Exception {
        WBSPage.openWBSPage(getTestData().getRootWork());
    }

    private TestData data;

    @Override
    public PSTestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }
}
