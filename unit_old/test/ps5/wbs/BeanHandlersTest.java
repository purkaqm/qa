package ps5.wbs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import ps5.wbs.logic.handlers.BeanHandler;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.Milestone;
import com.cinteractive.ps3.work.Work;

@SuppressWarnings("javadoc")
public class BeanHandlersTest extends WBSBaseTest {

    @Test
    public void checkpoint() {
    }

    @Test
    public void documentRelationship() {
    }

    @Test
    public void getId() {
        PersistentKey expected = Uuid.create();
        PersistentKey actual;
        {
            Checkpoint object = WBSTestUtils.createCheckpoint("test", getVisitProxy());
            // object.setPersistentKey(expected);
            // object.save();
            expected = object.getId();
            actual = BeanHandler.getId(object, getVisitProxy());
            assertEquals(expected, actual);
        }
        {
            // DocumentRelationship object = null;
            // object.setPersistentKey(expected);
            // object.save();
            // actual = BeanHandler.getId(object, getVisitProxy());
            // assertEquals(expected, actual);
        }
        {
            Milestone object = WBSTestUtils.createMilestone("test", getUser());
            object.setPersistentKey(expected);
            object.save();
            actual = BeanHandler.getId(object, getVisitProxy());
            assertEquals(expected, actual);
        }
        {
            // Need object = null;
            // object.setPersistentKey(expected);
            // object.save();
            // actual = BeanHandler.getId(object, getVisitProxy());
            // assertEquals(expected, actual);
        }
        {
            // TextMessage object = null;
            // object.setPersistentKey(expected);
            // object.save();
            // actual = BeanHandler.getId(object, getVisitProxy());
            // assertEquals(expected, actual);
        }
        {
            Tollgate object = WBSTestUtils.createTollgate("test", getVisitProxy());
            object.setPersistentKey(expected);
            object.save();
            actual = BeanHandler.getId(object, getVisitProxy());
            assertEquals(expected, actual);
        }
        {
            Work object = WBSTestUtils.createWork("test", getUser(), null);
            object.setPersistentKey(expected);
            object.save();
            actual = BeanHandler.getId(object, getVisitProxy());
            assertEquals(expected, actual);
        }
    }

    @After
    public void deleteObjects() {
        WBSTestUtils.deletePSO("test", getUser(), Work.TYPE);
        WBSTestUtils.deletePSO("test", getUser(), Tollgate.TYPE);
        WBSTestUtils.deletePSO("test", getUser(), Milestone.TYPE);
        WBSTestUtils.deletePSO("test", getUser(), Checkpoint.TYPE);
    }

    @Test
    public void milestone() {
    }

    @Test
    public void resource() {
    }

    @Test
    public void textMessage() {
    }

    @Test
    public void tollgate() {
    }

    @Test
    public void work() {
    }
}