package ps5.wbs;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ps5.wbs.beans.WBSBean;
import ps5.wbs.logic.ProjectEditorModel;
import ps5.wbs.logic.handlers.BeanHandler;

import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.Milestone;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil.SortOrder;

/**
 * #81403 -- 9.3 Sort Order on Summary Page Not working
 * 
 * @author Valentin Fedoskin <valentin.fedoskin@simbirsoft.com>
 */
public class BeansSortOrderTest extends WBSBaseTest {

    private Milestone milestone;

    private ProjectEditorModel model;

    private Work root;

    private Tollgate tollgate;

    private Work work;

    private Work work2;

    @Before
    public void createBeanModel() {
        root = WBSTestUtils.createWork("root", getUser(), null);
        work = WBSTestUtils.createWork("ch1", getUser(), null);
        tollgate = WBSTestUtils.createTollgate("ch2", getVisitProxy());
        work2 = WBSTestUtils.createWork("ch3", getUser(), null);
        milestone = WBSTestUtils.createMilestone("ch4", getUser());
        tollgate.setParentWork(root, getUser());
        tollgate.save();
        work.setParentWork(root, getUser());
        work.save();
        work2.setParentWork(root, getUser());
        work2.save();
        milestone.setParentWork(root, getUser());
        milestone.save();
        root.save();
        model = new ProjectEditorModel(getVisitProxy());
    }

    @After
    public void deleteBeanModel() {
        WBSTestUtils.deletePSO("root", getUser(), Work.TYPE);
    }

    @Test
    public void orderByDate() {
        List<JdbcPersistableBase> orderedWorks = getOrderedWorks(SortOrder.BY_DATE);
        model.loadTree(root);
        model.loadFullTree();
        Iterator<WBSBean> it = model.getTree().getChildrenIterator(model.getRoot(), false, 0);
        assertOrder(orderedWorks, it);
    }

    @Test
    public void orderByName() {
        List<JdbcPersistableBase> orderedWorks = getOrderedWorks(SortOrder.ALPHABETICALLY);
        model.loadTree(root);
        model.loadFullTree();
        Iterator<WBSBean> it = model.getTree().getChildrenIterator(model.getRoot(), false, 0);
        assertOrder(orderedWorks, it);
    }

    @Test
    public void orderBySequence() {
        List<JdbcPersistableBase> orderedWorks = getOrderedWorks(SortOrder.BY_SEQUENCE);
        model.loadTree(root);
        model.loadFullTree();
        Iterator<WBSBean> it = model.getTree().getChildrenIterator(model.getRoot(), false, 0);
        assertOrder(orderedWorks, it);
    }

    private void assertOrder(List<JdbcPersistableBase> expected, Iterator<WBSBean> actual) {
        int pos = 0;
        while (actual.hasNext()) {
            WBSBean bean = actual.next();
            assertEquals(BeanHandler.getId(expected.get(pos), getVisitProxy()), bean.getId());
            pos++;
        }
    }

    private List<JdbcPersistableBase> getOrderedWorks(SortOrder order) {
        root.setSortOrder(order);
        switch (order) {
        case ALPHABETICALLY:
            work2.setName("1");
            milestone.setName("2");
            tollgate.setName("3");
            work.setName("4");
            work2.save();
            milestone.save();
            tollgate.save();
            work.save();
            return Arrays.asList(new JdbcPersistableBase[] { work2, milestone, tollgate, work });
        case BY_DATE:
            milestone.getSchedules().setActualStartDate(Timestamp.valueOf("2012-01-01 00:00:00"));
            milestone.getSchedules().setActualEndDate(Timestamp.valueOf("2012-01-02 00:00:00"));
            tollgate.getSchedules().setActualStartDate(Timestamp.valueOf("2012-01-02 00:00:00"));
            tollgate.getSchedules().setActualEndDate(Timestamp.valueOf("2012-01-04 00:00:00"));
            work.getSchedules().setActualStartDate(Timestamp.valueOf("2012-01-02 00:00:00"));
            work.getSchedules().setActualEndDate(Timestamp.valueOf("2012-01-03 00:00:00"));
            work2.save();
            milestone.save();
            tollgate.save();
            work.save();
            return Arrays.asList(new JdbcPersistableBase[] { milestone, tollgate, work, work2 });
        case BY_SEQUENCE:
            milestone.setSequenceWithinParent(getUser(), 1);
            tollgate.setSequenceWithinParent(getUser(), 2);
            work.setSequenceWithinParent(getUser(), 2);
            work2.save();
            milestone.save();
            tollgate.save();
            work.save();
            return Arrays.asList(new JdbcPersistableBase[] { milestone, work, tollgate, work2 });
        default:
            return null;
        }
    }
}