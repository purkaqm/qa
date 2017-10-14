package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.ps3.mimehandler.HandlerRegistry;
import com.cinteractive.ps3.mimehandler.PSMimeHandler;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.test.MockPSSession;
import com.cinteractive.ps3.test.MockServletRequest;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tollgate.TollPhase;
import com.cinteractive.jdbc.*;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.database.*;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.ArrayList;


import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.util.SortedList;

public class TestTagPeer extends TestJdbcPeer {
    private TagPeer _peer;
    private static final boolean DEBUG = false;


    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(TagPeer.class.getName(), TestTagPeer.class.getName());
    }

    public TestTagPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest(new TestTagPeer(testName));
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        addTest(suite, "testFindTags");
        addTest(suite, "testFindTagSetIdByTag");
        addTest(suite, "testInsertUpdateDelete");
        addTest(suite, "testIsInUse");
        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void testInsert() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testDelete() throws Exception { /** see testInsertUpdateDelete */
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }


    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (TagPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return TagPeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null TagPeer peer instance.");
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindTags()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
            	final CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                CIResultSet rs = _peer.findTags(FAKE_ID, pstmt);
                TestTagPeer.assertEquals("Result set is not empty", false, rs.next());
                return null;
            }
        }.execute();

        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                TagSet ts = null;
                final CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                conn.setAutoCommit(false);
                try {
                    ts = TagSet.createNew("_TestTagPeer_findTags_", context);
                    ts.addTag("tag1");
                    ts.addTag("tag2");
                    ts.save(conn);

                    CIResultSet rs = _peer.findTags(ts.getId(), pstmt);
                    assertTrue("Expecting 2 records in result set", rs.next() && rs.next());

                    ts.addTag("tag3");
                    ts.save(conn);
                    rs = _peer.findTags(ts.getId(), pstmt);
                    assertTrue("Expecting 3 records in result set", rs.next() && rs.next() && rs.next());
                    return null;
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    pstmt.close();
                }
            }
        }.execute();

    }

    public void testFindTagSetIdByTag()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                try {
                    Object oRes = _peer.findTagSetIdByTag(FAKE_ID, conn);
                    assertTrue("Result is not empty", false);
                } catch (EmptyResultSetException ok) {
                }
                return null;
            }
        }.execute();

        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);
                try {
                    TagSet ts = TagSet.createNew("Fake_tagset2", context);
                    ts.addTag("Fake_tag2");
                    ts.save(conn);
                    PSTag tag = ts.getTag("Fake_tag2");
                    PersistentKey key = _peer.findTagSetIdByTag(tag.getId(), conn);
                    assertNotNull("Result is null", key);
                    assertTrue("Expecting tagset persistent key", ts.getId().equals(key));
                    return null;
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            }
        }.execute();

    }

//Insert/Update/Delete
    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();

                try {
                    TagSet ts = TagSet.createNew("Fake_tagset1", context);
                    ts.setIsHierarchical(Boolean.TRUE);
                    ts.save(conn);
                    ts.addTag("Fake_tag1");
                    ts.addTag("Fake_tag2");
                    PSTag tag = ts.getTag("Fake_tag1");
                    PSTag tag1 = ts.getTag("Fake_tag2");
                    tag.setDisplaySequence(new Integer(10));
                    _peer.insert(tag, conn);
                    _peer.insert(tag1, conn);
                    CIResultSet rs = _peer.findTags(ts.getId(), pstmt);
                    assertTrue("Expecting tag in tagset", (rs.next() && compare(tag, rs)) || (rs.next() && compare(tag, rs)));

                    tag.setName("name1");
                    tag.setDisplaySequence(new Integer(20));
                    tag.setParent(tag1);

                    _peer.update(tag, conn);
                    rs = _peer.findTags(ts.getId(), pstmt);
                    assertTrue("Expecting tag in tagset", (rs.next() && compare(tag, rs)) || (rs.next() && compare(tag, rs)));

                    _peer.delete(tag, conn);
                    rs = _peer.findTags(ts.getId(), pstmt);
                    assertTrue("Unexpected  tag in tagset", !(rs.next() && compare(tag, rs)) && !(rs.next() && compare(tag, rs)));
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    pstmt.close();
                }
                return null;
            }
        }.execute();
    }

    private void err(String attrName) {
        System.err.println(">> TestTagPeer: wrong attribvute value. Attribute '" + attrName + "'");
    }

    private boolean equals(Object o1, Object o2) {
        return o1 == null && o2 == null ||
                o1 != null && o1.equals(o2);
    }

    private boolean compare(PSTag tag, CIResultSet rs) throws SQLException {
        boolean res = true;
        if (res && !tag.getId().toString().equals(rs.getString("map_item_id")) && !(res = false)) {
            return false;
        }
        if (res && !tag.getName().equals(rs.getString("display_name")) && !(res = false)) {
            err("Name");
        }
        if (res && !tag.getDisplaySequence().toString().equals(rs.getString("display_sequence")) && !(res = false)) {
            err("Sequence");
        }
        if (res && !equals(tag.getParentId() == null ? null : tag.getParentId().toString(), rs.getString("parent_id")) && !(res = false)) {
            err("Parent id");
        }
        return res;
    }

    public void testIsInUse() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                Tollgate tg = null;
                PSTag process = null;
                final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
                try {
                    user = User.createNewUser("_TestTagPeer_isInUse_", context);
                    user.save(conn);
                    conn.commit();

                    // create own process definition

                    process = tp.addTag("_TestTagPeer_IsInUse_");
                    tp.save(conn);
                    conn.commit();

                    PSTag p1 = process.getTagSet().addTag("phase1");
                    p1.setParent(process);
                    process.getTagSet().save(conn);
                    conn.commit();

                    PSTag p2 = process.getTagSet().addTag("phase2");
                    p2.setParent(process);
                    process.getTagSet().save(conn);
                    conn.commit();

                    boolean is = _peer.isInUse(process.getId(), Tollgate.TYPE, conn);
                    assertTrue("Expecting false", !is);
                    is = _peer.isInUse(process.getId(), null, conn);
                    assertTrue("Expecting false", !is);

                    final PSSession sess = new MockPSSession(context, user);
                    final MockServletRequest req = new MockServletRequest();
                    req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
                    req.setParameter(Work.NAME, "_TestTagPeer_IsInUse_");
                    req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
                    req.setAttribute(PSSession.class.getName(), sess);
                    req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

                    final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
                    try {
                        tg = (Tollgate) handler.create(req);
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                        fail("Unexpected exception occured\n" + e.getMessage());
                    }
                    tg.save(conn);
                    conn.commit();

                    is = _peer.isInUse(process.getId(), null, conn);
                    assertTrue("Expecting true", is);
                    is = _peer.isInUse(process.getId(), Tollgate.TYPE, conn);
                    assertTrue("Expecting true", is);
                    is = _peer.isInUse(process.getId(), MetricInstance.TYPE, conn);
                    assertTrue("Expecting false", !is);
                } finally {
                    delete(tg, conn);

                    // remove the process definition
                    if (process != null) {
//		    process.getTagSet().getTags().delete(conn);
//			process.delete(conn);
                        SortedList sl = process.getChildren();
                        ArrayList l = new ArrayList();
                        for (Iterator tgIter = sl.iterator(); tgIter.hasNext();) {
                            PSTag tg1 = ((PSTag) tgIter.next());
                            l.add(tg1.getId());
                        }

                        for (Iterator tgIter = l.iterator(); tgIter.hasNext();)
                            tp.removeTag((PersistentKey) tgIter.next());


                        tp.removeTag(process.getId());
                        tp.save(conn);
                        conn.commit();
                    }
                    delete(user, conn);
                }

                return null;
            }
        }.execute();
    }

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
                //if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

}