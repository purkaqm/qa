package ps5.wbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ps5.wbs.beans.WBSBean;
import ps5.wbs.logic.ProjectEditor;
import ps5.wbs.logic.ProjectEditorModel;
import ps5.wbs.model.sections.DataView;
import ps5.wbs.model.sections.ViewItem;
import ps5.wbs.model.sections.WBSActions;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.ps3.work.Work;

@SuppressWarnings({ "javadoc", "unused" })
public class IndentOutdentTest extends WBSBaseTest {

    @SuppressWarnings("synthetic-access")
    private class Actions extends WBSActions {

        public Actions() {
            // stub
        }

        @Override
        protected ProjectEditor getBeanModel() {
            return model;
        }

        @Override
        protected DataView getView() {
            return new DataView(model.getTree()) {

                @Override
                public boolean addItem(ViewItem item) {
                    return false;
                }

                @Override
                public void fill() {
                }

                @Override
                public ViewItem getById(PersistentKey id) {
                    return null;
                }

                @Override
                public void move(ViewItem bean) {
                }
            };
        }
    }

    private ProjectEditorModel model;

    private Work rootWork;

    private Work test1;

    private Work test2;

    private Work test3;

    private Work test4;

    private Work test5;

    private Work test6;

    private Work test7;

    private Work test8;

    private Work test9;

    @Test
    public void bulkOutdent1() {
        WBSBean bean2 = model.getBean(test2.getId());
        WBSBean bean3 = model.getBean(test3.getId());
        WBSBean bean4 = model.getBean(test4.getId());
        WBSBean bean5 = model.getBean(test5.getId());
        WBSBean bean6 = model.getBean(test6.getId());
        WBSBean bean7 = model.getBean(test7.getId());
        WBSBean bean8 = model.getBean(test8.getId());
        List<WBSBean> movedBeans = new Actions().indentOutdent(Arrays.asList(bean4, bean6, bean8), false,
                new ArrayList<RuntimeException>());
        assertTrue(movedBeans.contains(bean4));
        assertTrue(movedBeans.contains(bean6));
        assertTrue(movedBeans.contains(bean8));
        assertTrue(movedBeans.contains(bean5));
        assertTrue(movedBeans.contains(bean7));
        assertEquals(bean2, bean4.getParent());
        assertEquals(bean4, bean5.getParent());
        assertEquals(bean4, bean5.getParent());
        assertEquals(bean4, bean7.getParent());
        assertEquals(bean4, bean6.getParent());
        assertEquals(bean4, bean8.getParent());
        assertEquals(1, bean2.getChildPosition(bean4));
        assertEquals(0, bean4.getChildPosition(bean5));
        assertEquals(1, bean4.getChildPosition(bean6));
        assertEquals(2, bean4.getChildPosition(bean7));
        assertEquals(3, bean4.getChildPosition(bean8));
        assertEquals(5, bean4.getNumber());
        assertEquals(6, bean5.getNumber());
        assertEquals(7, bean6.getNumber());
        assertEquals(8, bean7.getNumber());
        assertEquals(9, bean8.getNumber());
    }

    @Test
    public void bulkOutdent2() {
        test4.setParentWork(test2, getUser());
        test5.setParentWork(test2, getUser());
        test6.setParentWork(test2, getUser());
        test7.setParentWork(test6, getUser());
        test8.setParentWork(test6, getUser());
        model.loadFullTree();
        WBSBean bean2 = model.getBean(test2.getId());
        WBSBean bean4 = model.getBean(test4.getId());
        WBSBean bean5 = model.getBean(test5.getId());
        WBSBean bean6 = model.getBean(test6.getId());
        WBSBean bean7 = model.getBean(test7.getId());
        WBSBean bean8 = model.getBean(test8.getId());
        List<WBSBean> movedBeans = new Actions().indentOutdent(Arrays.asList(bean7, bean8), false,
                new ArrayList<RuntimeException>());
        assertTrue(movedBeans.contains(bean7));
        assertTrue(movedBeans.contains(bean8));
        assertEquals(bean2, bean7.getParent());
        assertEquals(bean2, bean8.getParent());
    }

    @Test
    public void bulkOutdetn3() {
        test1.setParentWork(rootWork, getUser());
        test2.setParentWork(rootWork, getUser());
        test3.setParentWork(test2, getUser());
        test4.setParentWork(test2, getUser());
        test5.setParentWork(test4, getUser());
        test6.setParentWork(test4, getUser());
        test7.setParentWork(test4, getUser());
        test8.setParentWork(test4, getUser());
        test9.setParentWork(rootWork, getUser());
        model.loadFullTree();
        WBSBean bean2 = model.getBean(test2.getId());
        WBSBean bean3 = model.getBean(test3.getId());
        WBSBean bean5 = model.getBean(test5.getId());
        WBSBean bean6 = model.getBean(test6.getId());
        WBSBean bean7 = model.getBean(test7.getId());
        WBSBean bean8 = model.getBean(test8.getId());
        List<WBSBean> movedBeans = new Actions().indentOutdent(Arrays.asList(bean5, bean6, bean7, bean8), false,
                new ArrayList<RuntimeException>());
        assertTrue(movedBeans.contains(bean5));
        assertTrue(movedBeans.contains(bean6));
        assertTrue(movedBeans.contains(bean7));
        assertTrue(movedBeans.contains(bean8));
        assertEquals(bean2, bean5.getParent());
        assertEquals(bean2, bean6.getParent());
        assertEquals(bean2, bean7.getParent());
        assertEquals(bean2, bean8.getParent());
        assertEquals(6, bean5.getNumber());
        assertEquals(7, bean6.getNumber());
        assertEquals(8, bean7.getNumber());
        assertEquals(9, bean8.getNumber());
    }

    @Before
    public void createModel() {
        rootWork = WBSTestUtils.createWork("test", getUser(), null);
        test1 = WBSTestUtils.createWork("test1", getUser(), rootWork);
        test2 = WBSTestUtils.createWork("test2", getUser(), rootWork);
        test3 = WBSTestUtils.createWork("test3", getUser(), test2);
        test4 = WBSTestUtils.createWork("test4", getUser(), test3);
        test5 = WBSTestUtils.createWork("test5", getUser(), test3);
        test6 = WBSTestUtils.createWork("test6", getUser(), test5);
        test7 = WBSTestUtils.createWork("test7", getUser(), test3);
        test8 = WBSTestUtils.createWork("test8", getUser(), test7);
        test9 = WBSTestUtils.createWork("test9", getUser(), rootWork);
        model = new ProjectEditorModel(getVisitProxy());
        model.loadTree(rootWork);
    }

    @After
    public void deleteWorks() {
        WBSTestUtils.deletePSO("test", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test1", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test2", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test3", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test4", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test5", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test6", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test7", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test8", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test9", getUser(), Work.TYPE);
    }
}