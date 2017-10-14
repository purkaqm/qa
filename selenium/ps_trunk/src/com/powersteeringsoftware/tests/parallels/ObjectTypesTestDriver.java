package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.EditObjectTypePage;
import com.powersteeringsoftware.libs.pages.ObjectTypesJspPage;
import com.powersteeringsoftware.libs.pages.ObjectTypesPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.core.PSTestDriver;
import com.powersteeringsoftware.libs.tests.core.TestSettings;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.session.TestSession;
import com.thoughtworks.selenium.SeleniumException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 28.03.13
 * Time: 1:29
 * To change this template use File | Settings | File Templates.
 */
public class ObjectTypesTestDriver extends PSTestDriver {

    static int i;
    private static boolean testJsp = true;


    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
    }

    private static final int COUNT = 10;
    private static final int COUNT_2 = 10;

    public PSTestData getTestData() {
        return new PSTestData() {
        };
    }

    @Test(
            threadPoolSize = COUNT,
            invocationCount = COUNT, groups = TestSettings.FIREFOX_ONLY_GROUP)
    public void test() {
        BasicCommons.logIn(!TestSession.isVersionPresent());
        int num = i++;
        ObjectTypesPage page = new ObjectTypesPage();
        ObjectTypesJspPage jsp = new ObjectTypesJspPage();
        boolean testJsp = PSTestData.getRandom().nextBoolean();
        if (testJsp)
            jsp.open();
        else
            page.open();
        for (int j = 0; j < COUNT_2; j++) {
            String name = "test " + CoreProperties.getTestTemplate() + "(" + num + ")(" + j + ")";
            if (testJsp)
                jsp = testJsp(jsp, name);
            else
                page = testPage(page, name);
        }
    }

    private ObjectTypesPage testPage(ObjectTypesPage objects, String name) {
        EditObjectTypePage edit = objects.edit(Work.Type.WORK.getName());
        edit.copy();
        edit.setName(name);
        edit.setPluralName(name);
        edit.update();
        edit.validate();
        return edit.back();
    }

    private ObjectTypesJspPage testJsp(ObjectTypesJspPage objects, String name) {
        objects.open();
        ObjectTypesJspPage.View view = objects.edit(Work.Type.WORK.getName());
        ObjectTypesJspPage.Edit edit = view.copy();
        edit.setName(name);
        edit.setPluralName(name);
        view = edit.submit();
        try {
            return view.back();
        } catch (SeleniumException se) {
            PSLogger.fatal(se);
            PSLogger.save();
            ObjectTypesJspPage res = new ObjectTypesJspPage();
            res.open();
            return res;
        }
    }
}
