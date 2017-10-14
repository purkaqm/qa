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
import com.powersteeringsoftware.libs.util.TimerWaiter;
import com.powersteeringsoftware.libs.util.session.TestSession;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 05.06.13
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class Issue85789Test2 extends PSParallelTestDriver {

    private static final int COUNT_1 = 9;
    private static final int COUNT_2 = 1;

    private static final int COUNT_3 = 20;
    private static int ref_count = 100;
    public static final String FILE = "acceptance.xml";

    private List<Work> works;

    @BeforeTest
    public void prepare() {
        for (Work w : getWorks()) {
            if (!WorkManager.exists(w))
                WorkManager.createProject(w);
        }
        SeleniumDriverFactory.stopAllSeleniumDrivers();
        SeleniumDriverFactory.setParallel(true);
        PSPage.setCheckBlankPage(true);
    }

    public List<Work> getWorks() {
        if (works != null) return works;
        works = new ArrayList<Work>();
        for (int i = 0; i < COUNT_1; i++) {
            Work p = new Work("parent" + i);
            Work c = new Work("child" + i);
            c.setParent(p);
            works.add(p);
            works.add(c);
        }
        return works;
    }

    @DataProvider(parallel = true)
    public Object[][] data() {
        Object[][] res = new Object[COUNT_1 + COUNT_2][0];
        List<Work> works = new ArrayList<Work>(getWorks());
        for (int i = 0; i < COUNT_1; i++) {
            Data attach = new Data();
            attach.user = (User) CoreProperties.getDefaultUser().clone();
            attach.isAttach = true;
            attach.parent = works.remove(0);
            attach.child = works.remove(0);
            res[i] = new Object[]{attach};
            list.add(attach);
        }

        for (int i = COUNT_1; i < COUNT_1 + COUNT_2; i++) {
            Data delete = new Data();
            delete.user = (User) CoreProperties.getDefaultUser().clone();
            delete.isAttach = false;
            res[i] = new Object[]{delete};
        }

        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) throws Exception {
        Thread.currentThread().setName(data.toString());
        if (data.isAttach == null) {
            //todo
        } else if (data.isAttach) {
            attachDocument(data);
        } else {
            deleteDocs(data.user);
        }
    }

    private void attachDocument(Data data) {
        try {
            data.isRunning = true;
            BasicCommons.logIn(data.user, !TestSession.isVersionPresent());

            for (int i = 0; i < COUNT_3; i++) {
                if (DeadlockParallelListener.hasFailure()) return;
                FileAttachment file = FileAttachment.getFile(FILE, "doc_" + CoreProperties.getTestTemplate() + ":" + Thread.currentThread().getId() + "_" + i);
                DocumentsManager.addDocument(data.child, file);
                DocumentsManager.removeDocument(data.parent, file);
            }
        } finally {
            data.isRunning = false;
        }
    }

    private void deleteDocs(User user) {
        BasicCommons.logIn(user, TestSession.isObjectNull(TestSession.Keys.APPLICATION_VERSION));
        DeletedJspPage jsp = new DeletedJspPage();
        jsp.open();

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
        private Work child;

        public String toString() {
            return user + ", " + (isAttach ? "Attach document thread" : "Delete thread");
        }
    }
}

