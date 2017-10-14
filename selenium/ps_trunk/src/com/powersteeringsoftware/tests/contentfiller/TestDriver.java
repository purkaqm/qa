package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.libs.tests.actions.ProcessesManager;
import com.powersteeringsoftware.libs.tests.actions.TagManager;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import org.testng.annotations.Test;

/**
 * Content Filler Tests
 *
 * @author selyaev_ag
 */

public class TestDriver extends PSTestDriver {

    private TestData data;


    @Test(groups = {"addtag", TestSettings.CONTENT_FILTER_GROUP},
            description = "Empty Content Filler: Add tag")
    public void addTag() {
        TagManager.addTag(getTestData().getTagPlanet());
        TagManager.addTag(getTestData().getTagLocation());
    }

    @Test(groups = TestSettings.CONTENT_FILTER_GROUP,
            description = "Empty Content Filler: Create custom field")
    public void createCustomField() {
        new CreateCustomField().execute();
    }

    @Test(groups = {TestSettings.CONTENT_FILTER_GROUP, "create.process"},
            description = "Empty Content Filler: Create Process")
    public void createProcess() {
        ProcessesManager.create(getTestData().getDMAICTestProcess());
        ProcessesManager.create(getTestData().getCDDDRCTestProcess());
    }

    @Override
    public TestData getTestData() {
        return data != null ? data : (data = new TestData());
    }
}
