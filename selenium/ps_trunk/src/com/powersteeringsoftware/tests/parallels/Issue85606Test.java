package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.TagChooser;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AttachPage;
import com.powersteeringsoftware.libs.pages.MeasureTemplatesPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 21.05.13
 * Time: 16:01
 * To change this template use File | Settings | File Templates.
 */
public class Issue85606Test extends PSParallelTestDriver {

    private static final int COUNT_1 = 10;
    private static final int COUNT_CREATE = 6;
    private static final int COUNT_ATTACH = 3;


    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
    }

    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_CREATE + COUNT_ATTACH + 0][0];

        for (int i = 0; i < COUNT_CREATE; i++) {
            Data attach = new Data();
            attach.user = (User) CoreProperties.getDefaultUser().clone();
            attach.isCreate = true;
            res[i] = new Object[]{attach};
            list.add(attach);
        }

        for (int i = COUNT_CREATE; i < COUNT_CREATE + COUNT_ATTACH; i++) {
            Data template = new Data();
            template.user = (User) CoreProperties.getDefaultUser().clone();
            template.isCreate = false;
            res[i] = new Object[]{template};
        }

        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isCreate == null) {
            //todo
        } else if (data.isCreate) {
            createTemplateTest(data);
        } else {
            attachTest(data.user);
        }
    }

    private void createTemplateTest(Data data) {
        try {
            data.isRunning = true;
            BasicCommons.logIn(data.user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));

            for (int i = 0; i < COUNT_1; i++) {
                if (DeadlockParallelListener.hasFailure()) return;
                Work w = new Work("test-" + CoreProperties.getTestTemplate() + "_" + i);
                WorkManager.createProject(w);
                /* MeasureTemplate template = new MeasureTemplate("test-" + System.currentTimeMillis() + "_" + i);
               MeasureManager.createMeasureTemplate(template);
               MeasureManager.deleteMeasureTemplate(template, true);*/
            }
        } finally {
            data.isRunning = false;
        }
    }

    private void attachTest(User user) {
        BasicCommons.logIn(user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
        MeasureTemplatesPage page = new MeasureTemplatesPage();
        page.open();
        AttachPage att = page.massAttach();
        att.selectMyProjects();
        setAllTemplates(att);
        while (isAnyAlive()) {
            //setAllTemplates(att);
            att.doAttach();
            att.validate();
        }
    }

    private void setAllTemplates(AttachPage att) {
        TagChooser tg = att.getTemplatesTagChooser();
        tg.openPopup();
        PSLogger.debug(tg.getAllLabels());
        tg.selectAll();
        tg.done();
    }

    private class Data extends AbstractData {
        private Boolean isCreate;

        public String toString() {
            return user + ", " + (isCreate ? "Template creation thread" : "Attach thread");
        }
    }
}
