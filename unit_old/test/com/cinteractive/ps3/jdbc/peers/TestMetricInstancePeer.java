package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;

import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.metrics.MetricTemplateItem;
import com.cinteractive.ps3.metrics.MetricInstanceItem;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.NoProject;
import com.cinteractive.ps3.work.WorkUtil;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.PSObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.Map;
import java.util.HashSet;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestMetricInstancePeer extends TestJdbcPeer {
    private MetricInstancePeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(MetricInstancePeer.class.getName(), TestMetricInstancePeer.class.getName());
    }

    private static final boolean DEBUG = true; //show debug info

    public TestMetricInstancePeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest(new TestMetricInstancePeer(testName));
    }

    private void delete(JdbcPersistableBase o, Connection conn) {
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

    private Work createWork(String name, User user) {
        final Work work = Work.createNew(Work.TYPE, name, user);
        return work;
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        addTest(suite, "testFindAssociatedObjectIdsByTemplateId");
        addTest(suite, "testFindIdsByTemplateId");
        addTest(suite, "testFindIdsByWorkId");
        addTest(suite, "testFindIdsByTemplateIdsAndObjectIds");
        addTest(suite, "testFinds");
        addTest(suite, "testDeleteSubitemsByItemId");
        addTest(suite, "testInsertUpdateDelete");

        return suite;
    }

    public void testInsert() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testDelete() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testGetMetricInstancePeer() throws Exception {
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (MetricInstancePeer) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return MetricInstancePeer.getMetricInstancePeer(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null MetricInstancePeer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindAssociatedObjectIdsByTemplateId() throws Exception {
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                Set res = null;

                try {
                    res = _peer.findAssociatedObjectIdsByTemplateId(null, conn);
                    fail("Null template id parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null template id parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findAssociatedObjectIdsByTemplateId(FAKE_ID, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                res = _peer.findAssociatedObjectIdsByTemplateId(FAKE_ID, conn);
                assertNotNull("Expecting empty result set, not null.", res);
                assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

                return null;
            }
        }.execute();
    }

    public void testFindIdsByTemplateId() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                Set res = null;

                try {
                    res = _peer.findIdsByTemplateId(null, conn);
                    fail("Null template id parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null template id parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findIdsByTemplateId(FAKE_ID, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                res = _peer.findIdsByTemplateId(FAKE_ID, conn);
                assertNotNull("Expecting empty result set, not null.", res);
                assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

                User user1 = User.createNewUser("fake1@email.add___", context);
                user1.generateAlerts(false);
                user1.setLastName("testMI_user_1");
                User user2 = User.createNewUser("fake2@email.add___", context);
                user2.generateAlerts(false);
                user2.setLastName("testMI_user_2");
                MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_1_3", user1);
                MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_2_4", user1);
                Work work1 = createWork("_testMI_work1_", user1);
                Work work2 = createWork("_testMI_work2_", user2);
                Work work3 = createWork("_testMI_work3_", user2);
                MetricInstance ins1 = MetricInstance.createNew(tmpl1, work1, false);
                MetricInstance ins2 = MetricInstance.createNew(tmpl1, work2, false);
                MetricInstance ins3 = MetricInstance.createNew(tmpl2, work3, false);

                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();
                    tmpl1.save(conn);
                    tmpl2.save(conn);
                    conn.commit();
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();
                    res = _peer.findIdsByTemplateId(tmpl1.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("expecting empty result set. got: " + res.size(), res.isEmpty());
                    res = _peer.findIdsByTemplateId(tmpl2.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("expecting empty result set. got: " + res.size(), res.isEmpty());

                    ins1.save(conn);
                    ins2.save(conn);
                    ins3.save(conn);
                    conn.commit();

                    res = _peer.findIdsByTemplateId(tmpl1.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", res);
                    assertTrue("Expecting anotehr items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("Expecting anotehr items in result set.",
                            res.contains(ins1.getId()) && res.contains(ins2.getId()));
                    res = _peer.findIdsByTemplateId(tmpl2.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", res);
                    assertTrue("Expecting anotehr items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting anotehr items in result set.",
                            res.contains(ins3.getId()));
                } finally {
                    delete(ins1, conn);
                    delete(ins2, conn);
                    delete(ins3, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(work3, conn);
                    delete(tmpl1, conn);
                    delete(tmpl2, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindIdsByWorkId() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                Set res = null;

                try {
                    res = _peer.findIdsByWorkId(null, conn);
                    fail("Null work id parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null template id parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findIdsByWorkId(FAKE_ID, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                res = _peer.findIdsByWorkId(FAKE_ID, conn);
                assertNotNull("Expecting empty result set, not null.", res);
                assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

                User user1 = User.createNewUser("fake1@email.add___", context);
                user1.generateAlerts(false);
                user1.setLastName("testMI_user_1");
                User user2 = User.createNewUser("fake2@email.add___", context);
                user2.generateAlerts(false);
                user2.setLastName("testMI_user_2");
                MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_1_2", user1);
                MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_2_2", user1);
                Work work1 = createWork("_testMI_work1_", user1);
                Work work2 = createWork("_testMI_work2_", user2);
                Work work3 = createWork("_testMI_work3_", user2);
                MetricInstance ins1 = MetricInstance.createNew(tmpl1, work1, false);
                MetricInstance ins2 = MetricInstance.createNew(tmpl1, work2, false);
                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();
                    tmpl1.save(conn);
                    tmpl2.save(conn);
                    conn.commit();
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();
                    res = _peer.findIdsByWorkId(work1.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("expecting empty result set. got: " + res.size(), res.isEmpty());
                    res = _peer.findIdsByWorkId(work2.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("expecting empty result set. got: " + res.size(), res.isEmpty());

                    ins1.save(conn);
                    ins2.save(conn);
                    conn.commit();

                    res = _peer.findIdsByWorkId(work1.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", res);
                    assertTrue("Expecting anotehr items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting anotehr items in result set.",
                            res.contains(ins1.getId()));
                    res = _peer.findIdsByWorkId(work2.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", res);
                    assertTrue("Expecting anotehr items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting anotehr items in result set.",
                            res.contains(ins2.getId()));
                } finally {
                    delete(ins1, conn);
                    delete(ins2, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(work3, conn);
                    delete(tmpl1, conn);
                    delete(tmpl2, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFinds() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                Map res = null;
                Set assRes = null;

                Set fakeObjSet = new HashSet();
                fakeObjSet.add(FAKE_ID);
                Set workSet = new HashSet();

                User user1 = User.createNewUser("fake1@email.add__", context);
                user1.generateAlerts(false);
                user1.setLastName("testMI_user_1");
                User user2 = User.createNewUser("fake2@email.add__", context);
                user2.generateAlerts(false);
                user2.setLastName("testMI_user_2");
                Work work1 = Work.createNew(Work.TYPE, "testMI_work_1", user1);
                work1.generateAlerts(false);
                Work work2 = Work.createNew(Work.TYPE, "testMI_work_2", user1);
                work2.generateAlerts(false);
                Work work3 = Work.createNew(Work.TYPE, "testMI_work_3", user1);
                work3.generateAlerts(false);

                MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_1_", user1);
                MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMI_templ_2_", user1);
                MetricInstance ins1 = MetricInstance.createNew(tmpl1, work1, false);
                MetricInstance ins2 = MetricInstance.createNew(tmpl2, work2, false);
                MetricInstance ins3 = MetricInstance.createNew(tmpl2, work3, false);
                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();
                    tmpl1.save(conn);
                    tmpl2.save(conn);
                    conn.commit();

                    workSet.add(work1.getId());
                    workSet.add(work2.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl1.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), fakeObjSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(FAKE_ID, workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

                    assRes = _peer.findAssociatedObjectIdsByTemplateId(tmpl1.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", assRes);
                    assertTrue("Expecting empty result set. got: " + assRes.size(), assRes.isEmpty());
                    assRes = _peer.findAssociatedObjectIdsByTemplateId(tmpl2.getId(), conn);
                    assertNotNull("Expecting empty result set, not null.", assRes);
                    assertTrue("Expecting empty result set. got: " + assRes.size(), assRes.isEmpty());

                    ins1.save(conn);
                    ins2.save(conn);
                    ins3.save(conn);
                    conn.commit();

                    workSet.clear();
                    workSet.add(work1.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl1.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.",
                            res.get(work1.getId()) == ins1.getId());
                    workSet.clear();
                    workSet.add(work2.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.",
                            res.get(work2.getId()) == ins2.getId());
                    workSet.add(work3.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    workSet.clear();
                    workSet.add(work1.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.isEmpty());
                    workSet.add(work2.getId());
                    workSet.add(work3.getId());
                    res = _peer.findIdsByTemplateIdsAndObjectIds(tmpl2.getId(), workSet, conn);
                    assertNotNull("Expecting empty result set, not null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("Expecting another items in result set.",
                            res.get(work2.getId()) == ins2.getId() && res.get(work3.getId()) == ins3.getId());

                    assRes = _peer.findAssociatedObjectIdsByTemplateId(tmpl1.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", assRes);
                    assertTrue("Expecting another items count in result set. got: " + assRes.size(), assRes.size() == 1);
                    assertTrue("Expecting another items in result set.",
                            assRes.contains(work1.getId()));
                    assRes = _peer.findAssociatedObjectIdsByTemplateId(tmpl2.getId(), conn);
                    assertNotNull("Expecting not empty result set, not null.", assRes);
                    assertTrue("Expecting another items count in result set. got: " + assRes.size(), assRes.size() == 2);
                    assertTrue("Expecting another items in result set.",
                            assRes.contains(work2.getId()) && assRes.contains(work3.getId()));
                } finally {
                    delete(ins1, conn);
                    delete(ins2, conn);
                    delete(ins3, conn);
                    delete(tmpl1, conn);
                    delete(tmpl2, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(work3, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindIdsByTemplateIdsAndObjectIds() throws Exception {
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                Map res = null;

                Set emptyObjSet = new HashSet();
                Set fakeObjSet = new HashSet();
                fakeObjSet.add(FAKE_ID);

                try {
                    res = _peer.findIdsByTemplateIdsAndObjectIds(null, fakeObjSet, conn);
                    fail("Null template id parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null template id parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findIdsByTemplateIdsAndObjectIds(FAKE_ID, null, conn);
                    fail("Null object Ids set parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null object Ids set parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findIdsByTemplateIdsAndObjectIds(FAKE_ID, fakeObjSet, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findIdsByTemplateIdsAndObjectIds(FAKE_ID, emptyObjSet, conn);
                    fail("Empty object Ids set parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Empty object Ids set parameter should throw IllegalArgumentException.");
                }

                res = _peer.findIdsByTemplateIdsAndObjectIds(FAKE_ID, fakeObjSet, conn);
                assertNotNull("Expecting empty result set, not null.", res);
                assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

                return null;
            }
        }.execute();
    }

    public void testInsertUpdateDelete() throws Exception {
        final MetricTemplate mt = MetricTemplate.createNew(MetricTemplate.TYPE, "TestMetricInstancePeer.testInsertUpdateDelete", User.getNobody(getContext()));
        final MetricInstance mi = MetricInstance.createNew(mt, WorkUtil.getNoProject(getContext()), false);
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                //conn.setAutoCommit(false);
                try {
                    _peer.insert(null, conn);
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.update(null, conn);
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.delete(null, conn);
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null metric instance parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.insert(mi, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.update(mi, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.delete(mi, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                try {
                    mt.save(conn);
                    conn.commit();

                    conn.setAutoCommit(false);

                    mi.setIsReadyForRollup(false,null);
                    _peer.insert(mi, conn);
                    _peer.restore(mi, conn);

                    assertTrue("Expecting another instance content.",
                            !mi.isReadyForRollup());
                    mi.setIsReadyForRollup(true,null);
                    _peer.update(mi, conn);
                    _peer.restore(mi, conn);
                    assertTrue("Expecting another instance content.",
                            mi.isReadyForRollup());
                    _peer.delete(mi, conn);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    delete(mt, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testDeleteSubitemsByItemId() throws Exception {
        final InstallationContext context = getContext();
        //final PersistentKey contextId = context.getId();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {

                try {
                    _peer.deleteSubitemsByItemId(null, conn);
                    fail("Null template id parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null template id parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.deleteSubitemsByItemId(FAKE_ID, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                Set res = _peer.deleteSubitemsByItemId(FAKE_ID, conn);
                assertNotNull("Expecting not null result set.", res);
                assertTrue("Expecting not null result set.", res.isEmpty());

                User user1 = User.createNewUser("_test__MetrInstPeer_u1", context);
                Work work1 = Work.createNew(Work.TYPE, "_test__MetrInstPeer_wrk1", user1);

                MetricTemplate mt = MetricTemplate.createNew(MetricTemplate.TYPE, "_test__MetrInstPeer_mt", user1);

                MetricInstance mi = MetricInstance.createNew(mt, work1, true);

                MetricTemplateItem mti1 = MetricTemplateItem.createNew("_test__MetrInstPeer_mti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                mti1.setSequence(new Integer(1));
                MetricTemplateItem mti2 = MetricTemplateItem.createNew("_test__MetrInstPeer_mti2", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                mti2.setSequence(new Integer(2));

                MetricInstanceItem mii1 = MetricInstanceItem.createNew("_test__MetrInstPeer_mii1", mi.getItems(), mti1);
                mii1.setSequence(new Integer(1));
                MetricInstanceItem mii2 = MetricInstanceItem.createNew("_test__MetrInstPeer_mii2", mi.getItems(), mti2);
                mii2.setSequence(new Integer(2));

                try {
                    user1.save(conn);
                    work1.save(conn);
                    mt.save(conn);
                    conn.commit();
                    mti1.save(conn);
                    mti2.save(conn);
                    mi.save(conn);
                    conn.commit();

                    mt.save(conn);
                    conn.commit();

                    mii1.save(conn);
                    mii2.save(conn);
                    conn.commit();

                    res = _peer.deleteSubitemsByItemId(mti1.getId(), conn);
                    assertNotNull("Expecting not null result set.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.", res.contains(mi.getId()));

                    res = _peer.deleteSubitemsByItemId(mti2.getId(), conn);
                    assertNotNull("Expecting not null result set.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.", res.contains(mi.getId()));

                    res = _peer.deleteSubitemsByItemId(mti1.getId(), conn);
                    assertNotNull("Expecting not null result set.", res);
                    assertTrue("Expecting empty result set.", res.isEmpty());

                    res = _peer.deleteSubitemsByItemId(mti2.getId(), conn);
                    assertNotNull("Expecting not null result set.", res);
                    assertTrue("Expecting empty result set.", res.isEmpty());

                } finally {
                    delete(mii1, conn);
                    delete(mii2, conn);
                    delete(mi, conn);
                    delete(mti1, conn);
                    delete(mti2, conn);
                    delete(mt, conn);
                    delete(work1, conn);
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();
    }
}