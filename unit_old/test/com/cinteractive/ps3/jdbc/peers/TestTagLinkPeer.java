package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.*;

import java.util.*;

import com.cinteractive.ps3.tags.*;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.jdbc.*;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.PSObject;

import java.sql.Connection;
import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TestTagLinkPeer extends TestJdbcPeer {

//////////////// Common part //////////////
    private TagLinkPeer _peer;

    private final static boolean DEBUG = true;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(TagLinkPeer.class.getName(), TestTagLinkPeer.class.getName());
    }

    public TestTagLinkPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest(new TestTagLinkPeer(testName));
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestTagLinkPeer");
        addTest(suite, "testFindTags");
        addTest(suite, "testDeleteByTagId");
        addTest(suite, "testInsertUpdateDelete");

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

        _peer = (TagLinkPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return TagLinkPeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null TagLinkPeer peer instance.");
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }
//////////////// end of Common part //////////////

    public void testFindTags()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                CIParameterizedStatement pstmt = null;
                CIResultSet rs = null;
                try {
                    pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                    rs = _peer.findTags(FAKE_ID, pstmt);
                    TestTagLinkPeer.assertTrue("Method findTags(FAKE_ID, pstmt) returned not empty ResultSet.", !rs.next());
                } finally {
                    if (rs != null) rs.close();
                    if (pstmt != null) pstmt.close();
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
                    TagSet ts = TagSet.createNew("_TestTagLinkPeer_findTags_", context);
                    ts.addLinkableType(User.TYPE);
                    PSTag tag = ts.addTag("tag1");
                    ts.save(conn);
                    final User user = User.createNewUser("_TestTagLinkPeer_findTags_", context);
                    try {
                        user.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
                    } catch (com.cinteractive.ps3.tags.InvalidTagEventException e) {
                        assertNotNull("Cant add tag to user object." + e.getMessage(), null);
                    }

                    user.save(conn);

                    CIParameterizedStatement pstmt = null;
                    CIResultSet rs = null;
                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        rs = _peer.findTags(user.getId(), pstmt);
                        assertTrue("Nothing were founded during TagLinkPeer.findTags(...).", rs.next());
                        assertTrue("Expecting valid tag",
                                tag.getId().toString().equals(rs.getString("map_item_id"))
                                && ts.getId().toString().equals(rs.getString("map_id"))
                                && context.getId().toString().equals(rs.getString("context_id"))
                        );
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                    }

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
                try {
                    TagSet ts = TagSet.createNew("_TestTagLinkPeer_insertUpdateDelete_", context);
                    ts.addLinkableType(User.TYPE);
                    ts.save(conn);
                    PSTag tag = ts.addTag("tag1");
                    tag.save(conn);
                    final User user = User.createNewUser("_TestTagLinkPeer_insertUpdateDelete_", context);

                    try {
                        user.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
                    } catch (com.cinteractive.ps3.tags.InvalidTagEventException e) {
                        fail("Cant add tag to user object." + e.getMessage());
                    }

                    TagLink tagLink = user.getTag(tag);
                    //user.save(conn);
                    assertNotNull("Cant add tag to user object", tagLink);
                    //insert
                    _peer.insert(tagLink, conn);
                    CIResultSet rs = null;
                    CIParameterizedStatement pstmt = null;
                    boolean fnd = false;
                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        rs = _peer.findTags(user.getId(), pstmt);

                        assertTrue("Expecting tag link in result set", rs.next());

                        do {
                            fnd =
                                    tag.getId().toString().equals(rs.getString("map_item_id"))
                                    && ts.getId().toString().equals(rs.getString("map_id"))
                                    && context.getId().toString().equals(rs.getString("context_id"));
                        } while (!fnd && rs.next());

                        assertTrue("Expecting tag link in result set", fnd);
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        rs = null;
                        pstmt = null;
                    }

                    _peer.update(tagLink, conn);

                    // delete
                    _peer.delete(tagLink, conn);
                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        rs = _peer.findTags(user.getId(), pstmt);
                        fnd = false;
                        while (!fnd && rs.next()) {
                            fnd =
                                    tag.getId().toString().equals(rs.getString("map_item_id"))
                                    && ts.getId().toString().equals(rs.getString("map_id"))
                                    && context.getId().toString().equals(rs.getString("context_id"));
                        }
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        rs = null;
                        pstmt = null;
                    }
                    assertTrue("Unexpected tag link in result set", !fnd);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

    public void testDeleteByTagId()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                Set vRes = _peer.deleteByTagId(FAKE_ID, conn);
                TestTagLinkPeer.assertTrue("Method deleteByTagId(FAKE_ID, pstmt) returned not empty result.", vRes.isEmpty());
                return null;
            }
        }.execute();

        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);
                try {
                    TagSet ts = TagSet.createNew("_TestTagLinkPeer_deleteByTagId_", context);
                    ts.addLinkableType(User.TYPE);
                    ts.save(conn);
                    PSTag tag = ts.addTag("tag1");
                    ts.save(conn);
                    final User user = User.createNewUser("_TestTagLinkPeer_deletebyTagId_", context);
                    try {
                        user.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
                    } catch (com.cinteractive.ps3.tags.InvalidTagEventException e) {
                        assertNotNull("Cant add tag to user object", null);
                    }

                    user.save(conn);

                    CIResultSet rs = null;
                    CIParameterizedStatement pstmt = null;
                    boolean fnd = false;
                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        rs = _peer.findTags(user.getId(), pstmt);

                        assertTrue("Expecting tag link in result set", rs.next());

                        do {
                            fnd =
                                    tag.getId().toString().equals(rs.getString("map_item_id"))
                                    && ts.getId().toString().equals(rs.getString("map_id"))
                                    && context.getId().toString().equals(rs.getString("context_id"));
                        } while (!fnd && rs.next());

                        assertTrue("Expecting tag link in result set", fnd);
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        rs = null;
                        pstmt = null;
                    }

                    Set vRes = _peer.deleteByTagId(tag.getId(), conn);
                    assertTrue("Nothing were deleted during TagLinkPeer.deleteByTagId(...).", !vRes.isEmpty());

                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        rs = _peer.findTags(user.getId(), pstmt);
                        fnd = false;
                        while (!fnd && rs.next()) {
                            fnd =
                                    tag.getId().toString().equals(rs.getString("map_item_id"))
                                    && ts.getId().toString().equals(rs.getString("map_id"))
                                    && context.getId().toString().equals(rs.getString("context_id"));
                        }
                    } finally {
                        if (rs != null) rs.close();
                        if (pstmt != null) pstmt.close();
                        rs = null;
                        pstmt = null;
                    }
                    assertTrue("Unexpected tag link in result set", !fnd);

                    return null;
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }

            }
        }.execute();
    }

    public void testClearMultipleLinks() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                TagSet ts = null;
                PSTag tag1 = null;
                PSTag tag2 = null;
                PSTag tag3 = null;
                Work work = null;
                DiscussionItem item = null;
                User user = null;
                try {
                    ts = TagSet.createNew("_TestTagLinkPeer_testClearMultipleLinks_", context);
                    ts.setAllowMultipleLinks(Boolean.TRUE);
                    ts.addLinkableType(Work.TYPE);
                    //ts.addLinkableType(DiscussionItem.LESSONS_LEARNED_TYPE);
                    ts.save(conn);
                    conn.commit();
                    tag1 = ts.addTag("tag1");
                    tag2 = ts.addTag("tag2");
                    tag3 = ts.addTag("tag3");
                    ts.save(conn);
                    conn.commit();

                    user = User.createNewUser("_TEstTagLinkPeer_clearNultiple_", context);
                    user.save(conn);
                    conn.commit();

                    work = Work.createNew(Work.TYPE, "_TestTagLinkPeer_testClearMultipleLinks_", user);
                    work.save(conn);
                    conn.commit();

                    //item = DiscussionItem.createNew(DiscussionItem.LESSONS_LEARNED_TYPE, work, null, user, "lesson title", "disc text");
                    item.save(conn);
                    conn.commit();

                    work.addItem(item);
                    work.save(conn);
                    conn.commit();

                    work.changeTag(TagEventCode.ADD_TAG_CODE, tag1, null);
                    work.changeTag(TagEventCode.ADD_TAG_CODE, tag2, null);
                    work.changeTag(TagEventCode.ADD_TAG_CODE, tag3, null);
                    work.save(conn);
                    conn.commit();

                    item.changeTag(TagEventCode.ADD_TAG_CODE, tag2, null);
                    item.changeTag(TagEventCode.ADD_TAG_CODE, tag3, null);
                    item.save(conn);
                    conn.commit();

                    final CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                    try {
                        CIResultSet res = _peer.findTags(work.getId(), pstmt);
                        List ids = getIds(res);
                        assertTrue("Expectign three tags in result set", ids.size() == 3);
                        assertTrue("Expecting valid tag keys", ids.contains(tag1.getId()) && ids.contains(tag2.getId()) && ids.contains(tag3.getId()));

                        res = _peer.findTags(item.getId(), pstmt);
                        ids = getIds(res);
                        assertTrue("Expectign two tags in result set", ids.size() == 2);
                        assertTrue("Expecting valid tag keys", ids.contains(tag3.getId()) && ids.contains(tag2.getId()));
                    } finally {
                        pstmt.close();
                    }

                    // remove the multiple links
                    ts.setAllowMultipleLinks(Boolean.FALSE);
                    ts.save(conn);
                    conn.commit();

                    // check the links removed
                    final CIParameterizedStatement pstmt1 = ((JdbcConnection) conn).createParameterizedStatement();
                    try {
                        CIResultSet res = _peer.findTags(work.getId(), pstmt1);
                        List ids = getIds(res);
                        assertTrue("Expectign only one tag in result set", ids.size() == 1);
                        assertTrue("Expecting valid tag key", ids.contains(tag1.getId()));

                        res = _peer.findTags(item.getId(), pstmt1);
                        ids = getIds(res);
                        assertTrue("Expectign only one tag in result set", ids.size() == 1);
                        assertTrue("Expecting valid tag key", ids.contains(tag2.getId()));
                    } finally {
                        pstmt1.close();
                    }


                } catch (InvalidTagEventException ignored) {
                    if (DEBUG) ignored.printStackTrace();
                    fail("Unexpected InvalidTagEventException occured");
                } finally {
                    delete(item, conn);
                    delete(work, conn);
                    delete(ts, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    private final List getIds(CIResultSet res) throws SQLException {
        final List ids = new ArrayList();
        while (res.next()) {
            final PersistentKey key = Uuid.get(res.getString(1));
            if (key != null) ids.add(key);
        }
        return ids;
    }

    private void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
               // if (o instanceof PSObject)
               //     ((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }

    }
}
