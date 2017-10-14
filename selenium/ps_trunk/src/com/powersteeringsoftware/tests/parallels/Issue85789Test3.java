package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.elements.TagChooser;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.FileAttachment;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.pages.AttachPage;
import com.powersteeringsoftware.libs.pages.DeletedJspPage;
import com.powersteeringsoftware.libs.pages.PSPage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.DocumentsManager;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 07.06.13
 * Time: 19:30
 * To change this template use File | Settings | File Templates.
 */
public class Issue85789Test3 extends PSParallelTestDriver {

    private static final int COUNT_1 = 8;


    private static final int COUNT_3 = 20;
    private static int ref_count = 100;
    public static final String FILE = "acceptance.xml";

    public static final String CHILD_NAME = "child_test";

    public static final List<String> contexts = Arrays.asList("ppx_94/test", "ppx_94/test2");

    @BeforeTest
    public void prepare() {
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
    }


    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_1 + contexts.size()][0];
        for (int i = 0; i < COUNT_1; i++) {
            Data attach = new Data();
            attach.context = contexts.get(PSTestData.getRandom().nextInt(contexts.size()));
            attach.user = (User) CoreProperties.getDefaultUser().clone();
            attach.isAttach = true;
            Work _p = getTestData().getSGPWork().copy();
            attach.parent = _p.getChild(PSTestData.getRandom().nextInt(_p.getChildren().size()));
            res[i] = new Object[]{attach};
            list.add(attach);
        }

        for (int i = COUNT_1; i < COUNT_1 + contexts.size(); i++) {
            Data delete = new Data();
            delete.user = (User) CoreProperties.getDefaultUser().clone();
            delete.isAttach = false;
            delete.context = contexts.get(i - COUNT_1);
            res[i] = new Object[]{delete};
        }

        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        BasicCommons.logIn(data.user, !TestSession.isVersionPresent(), data.context);
        if (data.isAttach == null) {
            //todo
        } else if (data.isAttach) {
            attachDocument(data);
        } else {
            deleteDocs();
        }
    }

    private void attachDocument(Data data) {
        try {
            data.isRunning = true;
            for (int i = 0; i < COUNT_3; i++) {
                if (DeadlockParallelListener.hasFailure()) return;
                FileAttachment file = FileAttachment.getFile(FILE, "doc_" + CoreProperties.getTestTemplate() + ":" + Thread.currentThread().getId() + "_" + i);
                Work child = new Work(CHILD_NAME + "_" + CoreProperties.getTestTemplate() + "_" + Thread.currentThread().getId() + "_" + i);
                child.setParent(data.parent);
                WorkManager.createProject(child, true);
                DocumentsManager.addDocument(child, file);
                WorkManager.deleteProject(child);
                WorkManager.open(data.parent.getParent());
            }
        } finally {
            data.isRunning = false;
        }
    }

    private void deleteDocs() {
        DeletedJspPage jsp = new DeletedJspPage();
        jsp.open(DeletedJspPage.URL.WORKS);

        while (isAnyAlive()) {
            List<String> items = null;
            for (int i = 0; i < ref_count; i++) {
                items = jsp.getItems();
                if (items.size() != 0) break;
                jsp.refresh();
                TimerWaiter.waitTime(1000);
            }
            PSLogger.info(items);
            jsp.deleteAll();
            jsp.refresh();
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
        private Boolean isAttach;
        private Work parent;
        private String context;

        public String toString() {
            return user + ", " + (isAttach ? "Document/Work thread(" + context + ")" : "Delete thread (" + context + ")");
        }
    }
}

