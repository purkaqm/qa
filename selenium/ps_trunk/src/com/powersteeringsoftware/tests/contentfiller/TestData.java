package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.libs.objects.PSProcess;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.tests_data.PSTestData;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 25.07.12
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class TestData extends PSTestData {

    public PSTag getTagLocation() {
        return getTag("location");
    }

    public PSTag getTagPlanet() {
        return getTag("planet");
    }

    public PSProcess getDMAICTestProcess() {
        return getProcess("dmaic-test");
    }

    public PSProcess getCDDDRCTestProcess() {
        return getProcess("cdddrc-test");
    }
}
