package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import com.powersteeringsoftware.libs.util.session.TestSession;

public class TestPredessors extends PSTest {

    //private WorkBreakdownManager wBM;

    public TestPredessors() {
        name = "WBS:Predessesors";
    }

    public void run() {

        Work _project = (Work) TestSession.getObject(TestDriver.SESSION_KEY_WORK_ITEM_WORK);
        Work child00001 = Work.create("Child00001", _project.getType());
        Work child00002 = Work.create("Child00002", _project.getType());
        Work[] children = {child00001, child00002};
        //TODO: WorkBreakdownManager is deleted in revision #75628
/*
        wBM = new WorkBreakdownManager();
        try {
            wBM.addChildren(_project, children);
            wBM.indent(child00002);
            wBM.setPredecessor(child00001, child00002);
        } catch (Exception e) {
            throw new SeleniumException("Error on testing predessesors", e);
        }
*/
    }


}
