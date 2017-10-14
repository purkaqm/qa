package com.powersteeringsoftware.tests.permissions;

import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import org.testng.ITest;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 10.09.12
 * Time: 17:17
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestDriver extends PSTestDriver implements ITest  {
    public static final String MAIN_GROUP = "permissions.test";
    private TestData data;
    public TestData getTestData() {
        if (data == null) data = new TestData();
        return data;
    }

}
