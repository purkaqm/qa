/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.StatusReportEvent;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.test.MockObjectType;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.MasterTask;
import com.cinteractive.ps3.work.UpdateFrequency;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestWorkPeer extends TestJdbcPeer {
    private static final int failNum = 0; //fail the assertion! for debug purposes only!
    private static int count = 0; //79 counted
    private static final boolean DEBUG = false; //show debug info


    private WorkPeer _peer;

    static {
        registerCase();
    }

    private static void registerCase() {
        TestSql.registerTestCase(WorkPeer.class.getName(), TestWorkPeer.class.getName());
    }

    public TestWorkPeer(String name) {
        super(name);
    }


    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestWorkPeer");


        suite.addTest(new TestWorkPeer("testGetReportedWorkIds"));
        suite.addTest(new TestWorkPeer("testIsProcessedByReportAgent"));
        suite.addTest(new TestWorkPeer("testSearchByName"));
        suite.addTest(new TestWorkPeer("testSearchTasksByName"));
        suite.addTest(new TestWorkPeer("testIsInvited"));
        suite.addTest(new TestWorkPeer("testGetRootWorkIdsContract"));
        suite.addTest(new TestWorkPeer("testGetRootWorkIds"));
        suite.addTest(new TestWorkPeer("testInsertUpdateDelete"));
        suite.addTest(new TestWorkPeer("testGetDependedids"));
        suite.addTest(new TestWorkPeer("testGetVisibleRootWorkIds"));
        suite.addTest(new TestWorkPeer("testGetVisibleProjectHierarchyIds"));
        suite.addTest(new TestWorkPeer("testIsReportManuallyUpdated"));

        return suite;
    }

    public void testInsert() { /** see testInsertUpdateDelete */
    }

    public void testUpdate() { /** see testInsertUpdateDelete */
    }

    public void testDelete() { /** see testInsertUpdateDelete */
    }

    public void testGetWorkPeer() {
    }

    public static void assertTrue(String text, boolean b) {
        count++;
        TestCase.assertTrue(text, b && failNum != count);
        log();
    }

    public static void assertNotNull(String text, Object o) {
        count++;
        TestCase.assertNotNull(text, failNum != count ? o : null);
        log();
    }

    private static void log() {
        if (DEBUG) System.out.println(count);
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (WorkPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return WorkPeer.getWorkPeer(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null WorkPeer instance.");
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testGetReportedWorkIds() throws Exception {
        final Collection types = new java.util.TreeSet();
        types.add(new MockObjectType("Work"));

        // empty case
        final List ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return _peer.getReportedWorkIds(FAKE_ID, types, conn);
            }
        }.execute();
        assertNotNull("Expecting empty work ids for not results; got null.", ids);
        assertTrue("Expecting empty work ids for fake context id '" + FAKE_ID + "'.", ids.isEmpty());

        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                // test null values
                try {
                    _peer.getReportedWorkIds(null, types, conn);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null context id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getReportedWorkIds(FAKE_ID, null, conn);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null types should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getReportedWorkIds(FAKE_ID, types, null);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection throw IllegalArgumentException exception");
                }

                // real data
                Collection typesList = new Vector();
                typesList.add(Work.TYPE);

                final User user = User.getNobody(context);
                int was = _peer.getReportedWorkIds(context.getId(), typesList, conn).size();

                Work work = null;

                try {
                    work = Work.createNew(Work.TYPE, "_TestWorkPeer_getReportedWorkIds_", user);
                    work.setUpdateFrequency(UpdateFrequency.WEEKLY);
                    work.setStatus(WorkStatus.ON_TRACK);
                    work.save(conn);
                    conn.commit();

                    List __ids = _peer.getReportedWorkIds(context.getId(), typesList, conn);
                    int now = __ids.size();
                    assertTrue("Expecting " + ++was + " ids; got " + now, was == now);
                    assertTrue("Expecting valid data", __ids.contains(work.getId()));
                } finally {
                    delete(work, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testIsProcessedByReportAgent() throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                final Timestamp today = new Timestamp(System.currentTimeMillis());
                // empty case
                boolean yep = _peer.isProcessedByReportAgent(FAKE_ID, FAKE_ID, today, conn);
                assertTrue("Expecting false for fake ids; got true", !yep);
                try {
                    _peer.isProcessedByReportAgent(null, FAKE_ID, today, conn);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null taskId should throw IllegalArgumentException exception");
                }
                try {
                    _peer.isProcessedByReportAgent(FAKE_ID, null, today, conn);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null workId should throw IllegalArgumentException exception");
                }
                try {
                    _peer.isProcessedByReportAgent(FAKE_ID, FAKE_ID, null, conn);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null time should throw IllegalArgumentException exception");
                }
                try {
                    _peer.isProcessedByReportAgent(FAKE_ID, FAKE_ID, today, null);
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }
                return null;
            }
        }.execute();
    }


    public void testSearchTasksByName() throws Exception {
        final String workName = ":>>" + System.currentTimeMillis() + "___TEST_IMPOSSIBLE_PROJECT_NAME___" + System.currentTimeMillis();
        final String name = ":>>" + System.currentTimeMillis() + "___TEST_IMPOSSIBLE_TASK_NAME___" + System.currentTimeMillis();
        final InstallationContext context = getContext();

        final Collection types = new java.util.TreeSet();
        types.add(new MockObjectType("Work"));

        // empty case
        List ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.searchTasksByName(conn, FAKE_ID, name, FAKE_ID, true);
            }
        }.execute();
        assertNotNull("Expecting empty work ids for no results; got null.", ids);
        assertTrue("Expecting empty work ids for fake context id and work id '" + FAKE_ID + "'.", ids.isEmpty());
        ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.searchTasksByName(conn, context.getId(), name, FAKE_ID, true);
            }
        }.execute();
        assertNotNull("Expecting empty work ids for no results; got null.", ids);
        assertTrue("Expecting empty work ids for fake task name and parent id '" + FAKE_ID + "'.", ids.isEmpty());


        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Work work = null;
                MasterTask task = null;
                MasterTask task1 = null;
                try {
                    final User user = User.getNobody(context);

                    work = Work.createNew(Work.TYPE, workName, user);
                    work.save(conn);
                    conn.commit();

                    List __ids = _peer.searchTasksByName(conn, context.getId(), name, work.getId(), false);
                    assertTrue("Expecting empty list", __ids.isEmpty());
                    __ids = _peer.searchTasksByName(conn, context.getId(), name, work.getId(), true);
                    assertTrue("Expecting empty list", __ids.isEmpty());

                    task = (MasterTask) MasterTask.createNew(MasterTask.TYPE, name, user);
                    task.setParentWork(work, user);
                    task.save(conn);
                    conn.commit();

                    __ids = _peer.searchTasksByName(conn, context.getId(), name, work.getId(), true);
                    assertTrue("Expecting 1 task id in resulting list; got " + __ids.size(), __ids.size() == 1);

                    task1 = (MasterTask) MasterTask.createNew(MasterTask.TYPE, "__TASK__", user);
                    task1.setParentWork(work, user);
                    task1.save(conn);
                    conn.commit();

                    __ids = _peer.searchTasksByName(conn, context.getId(), name, work.getId(), true);
                    assertTrue("Expecting 1 task id in resulting list; got " + __ids.size(), __ids.size() == 1);
                    __ids = _peer.searchTasksByName(conn, context.getId(), "Task", work.getId(), true);
                    assertTrue("Expecting 2 task ids in resulting list; got " + __ids.size(), __ids.size() == 2);
                } finally {
                    delete(work, conn);
                }
                return null;
            }
        }.execute();
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

    public void testIsInvited() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                // empty case
                boolean invited = _peer.isInvited(FAKE_ID, FAKE_ID, conn);
                assertTrue("Expecting false for fake user and work id " + FAKE_ID.toString(), !invited);
                invited = _peer.isInvited(User.getNobody(context).getId(), FAKE_ID, conn);
                assertTrue("Expecting false for fake work id " + FAKE_ID.toString(), !invited);

                // real data
                Work work = null;
                User user = null;
                try {
                    final String name = ":>>" + System.currentTimeMillis() + "___TEST_IMPOSSIBLE_PROJECT_NAME___" + System.currentTimeMillis();
                    final User nobody = User.getNobody(context);
                    work = Work.createNew(Work.TYPE, name, nobody);
                    work.save(conn);
                    conn.commit();

                    invited = _peer.isInvited(nobody.getId(), work.getId(), conn);
                    assertTrue("Expecting false", !invited);

                    user = DEBUG_CREATE_NEW_USER("_TestWorkPeer_testIsInvited_", context);
                    user.save(conn);
                    conn.commit();

                    invited = _peer.isInvited(user.getId(), work.getId(), conn);
                    assertTrue("Expecting false", !invited);

                    work.addTeamMember(user, nobody);
                    work.save(conn);
                    conn.commit();

                    invited = _peer.isInvited(user.getId(), work.getId(), conn);
                    assertTrue("Expecting true", invited);
                } finally {
                    delete(work, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }


    public void testGetRootWorkIdsContract()
            throws Exception {
        final Collection types = new java.util.TreeSet();
        types.add(new MockObjectType("Work"));

        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                try {
                    _peer.getRootWorkIds(null, types, conn);
                    fail("Expecting IllegalArgumentException for null context id.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.getRootWorkIds(FAKE_ID, null, conn);
                    fail("Expecting IllegalArgumentException for null types.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.getRootWorkIds(FAKE_ID, Collections.EMPTY_SET, conn);
                    fail("Expecting IllegalArgumentException for empty types.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.getRootWorkIds(FAKE_ID, types, null);
                    fail("Expecting IllegalArgumentException for null Connection.");
                } catch (IllegalArgumentException ok) {
                }

                final List ids = _peer.getRootWorkIds(FAKE_ID, types, conn);
                assertNotNull("No results should give empty list, not null.", ids);
                assertTrue("Expecting empty ids for fake context id.", ids.isEmpty());
                return ids;
            }
        }.execute();
    }


    public void testSearchByName() throws Exception {
        final String name = ":>>" + System.currentTimeMillis() + "___TEST_IMPOSSIBLE_PROJECT_NAME___" + System.currentTimeMillis();
        final InstallationContext context = getContext();
        List ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.searchByName(conn, context, name);
            }
        }.execute();
        assertNotNull("Expecting empty list for impossible name '" + name + "'; got null", ids);
        assertTrue("Expecting empty ids list for impossible name '" + name + "'", ids.isEmpty());

        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Work work = null;
                try {
                    List __ids = _peer.searchByName(conn, context, "Project");
                    assertTrue("Expecting at least 1 id in resuling list; got " + __ids.size(), __ids.size() >= 1);
                    final User user = User.getNobody(context);

                    work = Work.createNew(Work.TYPE, name, user);
                    work.save(conn);
                    conn.commit();

                    __ids = _peer.searchByName(conn, context, "Project");
                    assertTrue("Expecting at least 2 ids in resuling list; got " + __ids.size(), __ids.size() >= 2);
                    __ids = _peer.searchByName(conn, context, name);
                    assertTrue("Expecting exactly 1 id in resuling list; got " + __ids.size(), __ids.size() == 1);
                } finally {
                    delete(work, conn);
                }
                return null;
            }
        }.execute();

    }

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Work work = null;
                User newUser = null;
                try {
                    final User user = User.getNobody(context);
                    work = Work.createNew(Work.TYPE, "__TestWorkPeer::testInsertUpdateDelete__", user);
                    _peer.insert(work, conn);
                    conn.commit();

                    RestoreMap data = _peer.getRestoreData(work.getId(), conn);
                    assertTrue("Expecting valid data", compare(work, data));

                    newUser = DEBUG_CREATE_NEW_USER("_testWorkPeer_testInsertUDelete_", context);
                    newUser.save(conn);
                    conn.commit();

                    work.setName("_TestWorkPeer_testInsertUpdate_delete1_");
                    work.setStatus(WorkStatus.COMPLETED);
                    work.setOwnerId(newUser.getId());
                    work.setAssignedPriority(new Integer(2));
                   // work.delegate(user, newUser, false, false, false, false);
                    work.setPercentComplete(new Integer("51"));
                    work.setUpdateFrequency(UpdateFrequency.QUARTERLY);
                    work.setBudgetedCostAmount(new Double(1.5));
                    work.setControlCost(turnBoolean(work.getControlCost()));
                    work.setInheritControls(turnBoolean(work.getInheritControls()));
                    work.setInheritPermissions(turnBoolean(work.getInheritPermissions()));
                    work.setIsScheduleCommitted(Boolean.FALSE);
                    work.setUseResourcePlanning(Boolean.TRUE, newUser);
                   // work.setIsDavResource(Boolean.TRUE);
                    _peer.update(work, conn);
                    conn.commit();

                    data = _peer.getRestoreData(work.getId(), conn);
                    assertTrue("Expecting valid data", compare(work, data));
                    _peer.restore(work, conn);
                    _peer.delete(work, conn);
                    conn.commit();

                    Set ids = _peer.findIdsByPeerIdAndType(work.getId().toString(), Work.TYPE, context.getId(), conn);
                    work = null;
                    assertTrue("Expecting empty list", ids.isEmpty());
                } finally {
                    try {
                        if (work != null) _peer.delete(work, conn);
                        conn.commit();
                        delete(newUser, conn);
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    private Boolean turnBoolean(Boolean b) {
        return (b == null || !b.booleanValue()) ? Boolean.TRUE : Boolean.FALSE;
    }

    private void err(String attrName) {
        System.out.println(">> Wrong '" + attrName + "' attribute value");
    }

    private boolean equals(PSObject o, Object key) {
        return (o == null && key == null) ||
                (key != null && o.getId().equals(key));
    }

    private boolean equals(PSObject o, String key) {
        return (o == null && key == null) ||
                (key != null && o.getId().toString().equals(key));
    }

    private boolean equals(Object o, Object o1) {
        return (o == null && o1 == null) ||
                (o != null && o.equals(o1));
    }

    private boolean compare(Work work, RestoreMap data) {
        boolean equal = true;
        if (equal && !work.getName().equals(data.get(Work.NAME)) && !(equal = false)) {
            err("Name");
        }
        if (equal && !equals(work.getOwner(), data.get(WorkUtil.OWNER_ID).toString()) && !(equal = false)) {
            err("Owner");
        }
        if (equal && !work.getStatus().equals(data.get(WorkUtil.STATUS)) && !(equal = false)) {
            err("Project Status");
        }
        if (equal && !equals(work.getAssignedPriority(), data.get(WorkUtil.ASSIGNED_PRIORITY)) && !(equal = false)) {
            err("Assigned Priority");
        }
        if (equal && !equals(work.getDelegatedOwner(), data.get(WorkUtil.DELEGATED_OWNER_ID)) && !(equal = false)) {
            err("Delegated owner");
        }
        if (equal && !equals(work.getPercentComplete(), data.get(WorkUtil.PERCENT_COMPLETE)) && !(equal = false)) {
            err("Percent Complete");
        }
        if (equal && !equals(work.getUpdateFrequency(), data.get(WorkUtil.UPDATE_FREQUENCY)) && !(equal = false)) {
            err("Update frequency");
        }
        if (equal && !equals(work.getBudgetedCostAmount(), data.get(WorkUtil.BUDGETED_COST)) && !(equal = false)) {
            err("Budgeted Cost");
        }
        if (equal && !equals(work.getControlCost(), data.get(WorkUtil.CONTROL_COST)) && !(equal = false)) {
            err("Control Cost");
        }
        if (equal && !work.getInheritControls().equals(data.get(WorkUtil.INHERIT_CONTROLS)) && !(equal = false)) {
            err("Inherit Controls");
        }
        if (equal && !work.getInheritPermissions().equals(data.get(WorkUtil.INHERIT_PERMISSIONS)) && !(equal = false)) {
            err("Inherit Permissions");
        }
        return equal;
    }


    public void testGetDependedids() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {

                try {
                    _peer.getDependedids(null, conn);
                    fail("Null work id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null work id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getDependedids(FAKE_ID, null);
                    fail("Null connection should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }

                List res = _peer.getDependedids(FAKE_ID, conn);

                assertNotNull("expecting not null result list.", res);
                assertTrue("expecting empty result list.", res.isEmpty());

                User user1 = DEBUG_CREATE_NEW_USER("_getDependentIds_user1", context);
                Work ansWork = Work.createNew(Work.TYPE, "_getDependentIds_ansWork", user1);
                Work chldWork1 = Work.createNew(Work.TYPE, "_getDependentIds_chldWork1", user1);
                chldWork1.setParentWork(ansWork, user1);
                Work chldWork2 = Work.createNew(Work.TYPE, "_getDependentIds_chldWork2", user1);
                chldWork2.setParentWork(ansWork, user1);

                MetricTemplate mt = MetricTemplate.createNew(MetricTemplate.TYPE, "_getDependentIds_metrTmpl", user1);

                MetricInstance mi1 = MetricInstance.createNew(mt, chldWork1, true);
                MetricInstance mi2 = MetricInstance.createNew(mt, chldWork2, true);
                MetricInstance mi3 = MetricInstance.createNew(mt, ansWork, true);

                try {
                    user1.save(conn);
                    conn.commit();
                    ansWork.save(conn);
                    mt.save(conn);
                    conn.commit();

                    res = _peer.getDependedids(ansWork.getId(), conn);

                    assertNotNull("expecting not null result list.", res);
                    assertTrue("expecting empty result list.", res.isEmpty());

                    chldWork1.save(conn);
                    chldWork2.save(conn);
                    conn.commit();
                    mi1.save(conn);
                    mi2.save(conn);
                    mi3.save(conn);
                    conn.commit();

                    res = _peer.getDependedids(ansWork.getId(), conn);

                    assertNotNull("expecting not null result list.", res);
                    assertTrue("expecting another result list size. got: " + res.size(), res.size() == 5);
                    assertTrue("expecting another items in result list.", res.contains(mi1.getId()));
                    assertTrue("expecting another items in result list.", res.contains(mi2.getId()));
                    assertTrue("expecting another items in result list.", res.contains(mi3.getId()));
                    assertTrue("expecting another items in result list.", res.contains(chldWork1.getId()));
                    assertTrue("expecting another items in result list.", res.contains(chldWork2.getId()));

                    res = _peer.getDependedids(chldWork2.getId(), conn);

                    assertNotNull("expecting not null result list.", res);
                    assertTrue("expecting another result list size. got: " + res.size(), res.size() == 1);
                    assertTrue("expecting another items in result list.", res.contains(mi2.getId()));

                } finally {
                    delete(mi1, conn);
                    delete(mi2, conn);
                    delete(mi3, conn);
                    delete(chldWork1, conn);
                    delete(chldWork2, conn);
                    delete(ansWork, conn);
                    delete(mt, conn);
                    delete(user1, conn);
                }

                return null;
            }
        }.execute();
    }


    public void testGetVisibleRootWorkIds() throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                try {
                    _peer.getVisibleRootWorkIds(null, FAKE_ID, true, conn);
                    fail("Null work id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null work id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getVisibleRootWorkIds(contextId, null, true, conn);
                    fail("Null user id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null user id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getVisibleRootWorkIds(contextId, FAKE_ID, true, null);
                    fail("Null connection should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }

                List res = null;

                User user1 = DEBUG_CREATE_NEW_USER("_testGetVisibleRootWorkIds__user1", context);
                User user2 = DEBUG_CREATE_NEW_USER("_testGetVisibleRootWorkIds__user2", context);

                Work work1 = null;
                Work work11 = null;
                Work work111 = null;
                Work work2 = null;
                Tollgate work3 = null;

                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();

                    work1 = Work.createNew(Work.TYPE, "_testGetVisibleRootWorkIds__w1", user1);
                    work11 = Work.createNew(Work.TYPE, "_testGetVisibleRootWorkIds__w11", user1);
                    work111 = Work.createNew(Work.TYPE, "_testGetVisibleRootWorkIds__w111", user1);
                    work2 = Work.createNew(Work.TYPE, "_testGetVisibleRootWorkIds__w2", user1);
                    work3 = (Tollgate) Work.createNew(Tollgate.TYPE, "_testGetVisibleRootWorkIds__w3", user1);

                    work1.save(conn);
                    work11.save(conn);
                    work111.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();

                    work11.setParentWork(work1, user1);
                    work111.setParentWork(work11, user1);
                    work11.save(conn);
                    work111.save(conn);
                    conn.commit();

                    work11.addTeamMember(user2, user1);
                    work2.addTeamMember(user2, user1);
                    work3.addChampion(user2);
                    work11.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();

                    //work2.archive(user1);
                    work2.save(conn);
                    conn.commit();

                    res = _peer.getVisibleRootWorkIds(contextId, user1.getId(), true, conn);
                    assertTrue("Expecting another items in result list.", res.contains(work1.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work2.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work3.getId()));

                    res = _peer.getVisibleRootWorkIds(contextId, user1.getId(), false, conn);
                    assertTrue("Expecting another items in result list.", res.contains(work1.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work3.getId()));

                    res = _peer.getVisibleRootWorkIds(contextId, user2.getId(), true, conn);
                    assertTrue("Expecting another items in result list.", res.contains(work11.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work2.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work3.getId()));

                    res = _peer.getVisibleRootWorkIds(contextId, user2.getId(), false, conn);
                    assertTrue("Expecting another items in result list.", res.contains(work11.getId()));
                    assertTrue("Expecting another items in result list.", res.contains(work3.getId()));
                } finally {
                    delete(work111, conn);
                    delete(work11, conn);
                    delete(work3, conn);
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testGetVisibleProjectHierarchyIds() throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                try {
                    _peer.getVisibleProjectHierarchyIds(null, FAKE_ID, FAKE_ID, true, conn);
                    fail("Null work id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null work id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getVisibleProjectHierarchyIds(contextId, FAKE_ID, null, true, conn);
                    fail("Null user id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null user id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.getVisibleProjectHierarchyIds(contextId, FAKE_ID, FAKE_ID, true, null);
                    fail("Null connection should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }

                Map res = null;
                Collection col = null;

                User user1 = DEBUG_CREATE_NEW_USER("_testGetVisibleProjectHierarchyIds__user1", context);
                User user2 = DEBUG_CREATE_NEW_USER("_testGetVisibleProjectHierarchyIds__user2", context);

                Work work1 = null;
                Work work11 = null;
                Work work111 = null;
                Work work112 = null;
                Work work2 = null;
                Tollgate work3 = null;

                try {

                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();

                    work1 = Work.createNew(Work.TYPE, "_testGetVisibleProjectHierarchyIds__w1", user1);
                    work11 = Work.createNew(Work.TYPE, "_testGetVisibleProjectHierarchyIds__w11", user1);
                    work111 = Work.createNew(Work.TYPE, "_testGetVisibleProjectHierarchyIds__w111", user1);
                    work112 = Work.createNew(Work.TYPE, "_testGetVisibleProjectHierarchyIds__w112", user2);
                    work2 = Work.createNew(Work.TYPE, "_testGetVisibleProjectHierarchyIds__w2", user1);
                    work3 = (Tollgate) Work.createNew(Tollgate.TYPE, "_testGetVisibleProjectHierarchyIds__w3", user1);

                    work1.save(conn);
                    work11.save(conn);
                    work111.save(conn);
                    work112.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();

                    work11.setParentWork(work1, user1);
                    work111.setParentWork(work11, user1);
                    work11.save(conn);
                    work111.save(conn);
                    conn.commit();

                    work11.addTeamMember(user2, user1);
                    work2.addTeamMember(user2, user1);
                    work3.addChampion(user2);
                    work11.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();

                    work112.setParentWork(work11, user2);
                    //work2.archive(user1);
                    work112.save(conn);
                    work2.save(conn);
                    conn.commit();

                    // all visible projects

                    res = _peer.getVisibleProjectHierarchyIds(contextId, null, user1.getId(), true, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 3 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 3));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work1.getId()));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (>=3) in result collection. got: " + col.size(), col.size() >= 3);
                    assertTrue("Expecting another items in result.", col.contains(work1.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work2.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work3.getId()));
                    assertTrue("Expecting another items in result.", !col.contains(work11.getId()));
                    assertTrue("Expecting another items in result.", !col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", !col.contains(work112.getId()));

                    col = (Collection) res.get(work1.getId());
                    assertTrue("Expecting another items count (1) in result collection. got: " + col.size(), col.size() == 1);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                    res = _peer.getVisibleProjectHierarchyIds(contextId, null, user2.getId(), true, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 2 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 2));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (>=2) in result collection. got: " + col.size(), col.size() >= 2);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work2.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                    //filter archived projects (by visibility)

                    res = _peer.getVisibleProjectHierarchyIds(contextId, null, user1.getId(), false, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 3 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 3));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work1.getId()));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (>=2) in result collection. got: " + col.size(), col.size() >= 2);
                    assertTrue("Expecting another items in result.", col.contains(work1.getId()));
                    assertTrue("Expecting another items in result.", !col.contains(work2.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work3.getId()));

                    col = (Collection) res.get(work1.getId());
                    assertTrue("Expecting another items count (1) in result collection. got: " + col.size(), col.size() == 1);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                    res = _peer.getVisibleProjectHierarchyIds(contextId, null, user2.getId(), false, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 2 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 2));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (>=1) in result collection. got: " + col.size(), col.size() >= 1);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));
                    assertTrue("Expecting another items in result.", !col.contains(work2.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                    // visible child projects (root project != null)

                    res = _peer.getVisibleProjectHierarchyIds(contextId, work1.getId(), user1.getId(), true, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 2 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 2));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (1) in result collection. got: " + col.size(), col.size() == 1);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                    res = _peer.getVisibleProjectHierarchyIds(contextId, work1.getId(), user2.getId(), true, conn);
                    assertNotNull("Expecting not null result", res);
                    assertTrue("Expecting 2 items in result keyset. got: " + res.keySet().size(), (res.keySet().size() == 2));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(null));
                    assertTrue("Expecting another items in result keyset.", res.keySet().contains(work11.getId()));

                    col = (Collection) res.get(null);
                    assertTrue("Expecting another items count (1) in result collection. got: " + col.size(), col.size() == 1);
                    assertTrue("Expecting another items in result.", col.contains(work11.getId()));

                    col = (Collection) res.get(work11.getId());
                    assertTrue("Expecting another items count (2) in result collection. got: " + col.size(), col.size() == 2);
                    assertTrue("Expecting another items in result.", col.contains(work111.getId()));
                    assertTrue("Expecting another items in result.", col.contains(work112.getId()));

                } finally {
                    delete(work111, conn);
                    delete(work112, conn);
                    delete(work11, conn);
                    delete(work3, conn);
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testIsReportManuallyUpdated() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date(System.currentTimeMillis()));

                cal.set(Calendar.MILLISECOND, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.HOUR_OF_DAY, 12);

                User user1 = DEBUG_CREATE_NEW_USER("_isReportManuallyUpdated_user1", context);

                Work work1 = null;

                try {
                    user1.save(conn);
                    conn.commit();

                    work1 = Work.createNew(Work.TYPE, "_isReportManuallyUpdated_wrk1", user1);
                    work1.save(conn);
                    conn.commit();

                    final Map map = new java.util.HashMap();

                    StatusReportEvent event = (StatusReportEvent)work1.addSaveEvent(StatusReportEvent.TYPE, map, user1);
                    event.getStatusReport().setComment("comment");
                    event.getStatusReport().setManuallyUpdated(true);
                    event.getStatusReport().setAgentDueDate(cal.getTime());

                    work1.save(conn);
                    conn.commit();

                    boolean res = work1.isReportManuallyUpdated(cal.getTime());
                    assertTrue("Expecting true result", res);

                    cal.set(Calendar.DATE, (cal.get(Calendar.DATE) + 1));
                    res = work1.isReportManuallyUpdated(cal.getTime());
                    assertTrue("Expecting false result", !res);

                } finally {
                    delete(work1, conn);
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();
    }



    //gags
    public void testIsTollgateRejectedByUser() {
    };
    public void testIsTollgateApprovedByUser() {
    };
    public static void DEBUG_DELETE_USER(String sEmailAddress, InstallationContext context) {
        if (!DEBUG)
            return;
        try {
            User userForDelete = User.getByEmailAddress(sEmailAddress, context);
            if (userForDelete != null) {
               // userForDelete.setModifiedById(User.getNobody(context).getId());
                userForDelete.deleteHard();
            }
        } catch (RuntimeSQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    public static User DEBUG_CREATE_NEW_USER(String sEmailAddress, InstallationContext context) {
        DEBUG_DELETE_USER(sEmailAddress, context);
        return User.createNewUser(sEmailAddress, context);
    }

}
