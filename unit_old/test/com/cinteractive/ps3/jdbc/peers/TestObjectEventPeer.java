package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.ArchivalEvent;
import com.cinteractive.ps3.events.BudgetChangeEvent;
import com.cinteractive.ps3.events.CreationEvent;
import com.cinteractive.ps3.events.EventQuery;
import com.cinteractive.ps3.events.EventType;
import com.cinteractive.ps3.events.LoginEvent;
import com.cinteractive.ps3.events.ObjectEvent;
import com.cinteractive.ps3.events.ObjectEventId;
import com.cinteractive.ps3.test.MockEventType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestObjectEventPeer extends TestJdbcPeer {
    private static final EventType FAKE_EVENT_TYPE = new MockEventType("__FAKE_TYPE__");

    private ObjectEventPeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(ObjectEventPeer.class.getName(), TestObjectEventPeer.class.getName());
    }

    private static final boolean DEBUG = true; //show debug info

    public TestObjectEventPeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestObjectEventPeer("testFindTopAlertEvents"));
        suite.addTest(new TestObjectEventPeer("testAddAlertEvent"));
        suite.addTest(new TestObjectEventPeer("testCountAlertsByUser"));
        suite.addTest(new TestObjectEventPeer("testDelete"));
        suite.addTest(new TestObjectEventPeer("testDeleteAlertEvents"));
        suite.addTest(new TestObjectEventPeer("testDeleteAlertEventsByUserId"));
        suite.addTest(new TestObjectEventPeer("testFindByQuery"));
        suite.addTest(new TestObjectEventPeer("testGet"));
        suite.addTest(new TestObjectEventPeer("testHasAlert"));
        suite.addTest(new TestObjectEventPeer("testInsert"));
        suite.addTest(new TestObjectEventPeer("testSaveParameters"));
        suite.addTest(new TestObjectEventPeer("testUpdate"));

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (ObjectEventPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return ObjectEventPeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null ObjectEventPeer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
                //if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

    public void testAddAlertEvent() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        Work work = WorkUtil.getNoProject(context);
        User user = Nobody.get(context);
        Timestamp today = new Timestamp(new java.util.Date().getTime());

        conn.setAutoCommit(false);

        try {
            try {
                _peer.addAlertEvent(null, FAKE_ID, today, conn);
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.addAlertEvent(FAKE_ID, null, today, conn);
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.addAlertEvent(FAKE_ID, FAKE_ID, null, conn);
                fail("Null Timestamp parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Timestamp parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.addAlertEvent(FAKE_ID, FAKE_ID, today, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            Map params = new HashMap();
            params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
            ObjectEvent event = ObjectEvent.create(ArchivalEvent.TYPE, work, params, user.getId());
            event.save(conn);

            _peer.addAlertEvent(user.getId(), work.getId(), event.getEventDate(), conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select user_id from alert_event where user_id='" + user.getId() + "'" +
                    " and object_id='" + work.getId() + "' and event_date={ts'" + event.getEventDate().toString() + "'}");

            assertTrue("Expecting record in resultset", rset.next());

        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testCountAlertsByUser() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                try {
                    _peer.countAlertsByUser(null, conn);
                    fail("Null user id in countAlertsByUser method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null user id in countAlertsByUser method should throw IllegalArgumentException.");
                }
                try {
                    _peer.countAlertsByUser(FAKE_ID, null);
                    fail("Null connection in countAlertsByUser method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection in countAlertsByUser method should throw IllegalArgumentException.");
                }
                Integer res = _peer.countAlertsByUser(FAKE_ID, conn);
                assertTrue("Expecting result == 0", res.intValue() == 0);

                User user = User.createNewUser("_TestObjectEvent_coutnAlertsByser_", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEvent_countAlertsByUser1_", user);

                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEvent_countAlertsByUser2_", user);

                Map params = new HashMap();
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    res = _peer.countAlertsByUser(user.getId(), conn);
                    assertTrue("Expecting result == 0", res.intValue() == 0);

                    work1.save(conn);
                    conn.commit();

                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();

                    event2.save(conn);
                    conn.commit();

                    res = _peer.countAlertsByUser(user.getId(), conn);
                    assertTrue("Expecting result == 0", res.intValue() == 0);

                    _peer.addAlertEvent(user.getId(), work1.getId(), event1.getEventDate(), conn);
                    res = _peer.countAlertsByUser(user.getId(), conn);
                    assertTrue("Expecting result == 1", res.intValue() == 1);

                    _peer.addAlertEvent(user.getId(), work2.getId(), event2.getEventDate(), conn);
                    res = _peer.countAlertsByUser(user.getId(), conn);
                    assertTrue("Expecting result == 2", res.intValue() == 2);
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testDelete() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        Work work = WorkUtil.getNoProject(context);
        User user = Nobody.get(context);

        conn.setAutoCommit(false);

        try {

            Map params = new HashMap();
            params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
            ObjectEvent event = null;

            try {
                _peer.delete(event, conn);
                fail("Null Event Object parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Event Object parameter should throw IllegalArgumentException.");
            }

            event = ObjectEvent.create(ArchivalEvent.TYPE, work, params, user.getId());
            event.save(conn);

            try {
                _peer.delete(event, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }
            _peer.delete(event, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select object_id from object_event where object_id='" + work.getId() + "'" +
                    " and event_date={ts'" + event.getEventDate().toString() + "'}");

            assertTrue("Not expecting record in resultset", !rset.next());

        } finally {
            conn.rollback();
            conn.close();
        }
    }

    private final static int DAE_CaseCount = 3;

    public void testDeleteAlertEvents() throws Exception {
        for (int uc = 0; uc < DAE_CaseCount; uc++) {
            testDeleteAlertEvents(uc);
        }
    }

    private void testDeleteAlertEvents(int useCase) throws Exception {
        final InstallationContext context = getContext();
        final int fUseCase = useCase;
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = User.createNewUser("_TestObjectEvent_DeleteAlertEvents_", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEvent_testDeleteAlertEvents1_", user);

                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEvent_testDeleteAlertEvents1_", user);

                Map params = new HashMap();
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                ObjectEventId[] ids = {new ObjectEventId(event1.getObjectId(), event1.getEventDate()),
                                       new ObjectEventId(event2.getObjectId(), event2.getEventDate())};
                ObjectEventId[] ids1 = {new ObjectEventId(event1.getObjectId(), event1.getEventDate())};
                ObjectEventId[] ids2 = {new ObjectEventId(event2.getObjectId(), event2.getEventDate())};
                try {
                    user.save(conn);
                    conn.commit();

                    work1.save(conn);
                    conn.commit();

                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();

                    event2.save(conn);
                    conn.commit();

                    if (fUseCase > 0) {
                        _peer.addAlertEvent(user.getId(), work1.getId(), event1.getEventDate(), conn);
                        _peer.addAlertEvent(user.getId(), work2.getId(), event2.getEventDate(), conn);
                    }

                    switch (fUseCase) {
                        case 0:
                            { // empty cases
                                try {
                                    _peer.deleteAlertEvents(null, ids, conn);
                                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                                } catch (IllegalArgumentException ok) {
                                } catch (Exception e) {
                                    fail("Null Event Object parameter should throw IllegalArgumentException.");
                                }
                                try {
                                    _peer.deleteAlertEvents(user.getId(), (ObjectEventId[]) null, conn);
                                    fail("Null ObjectEventId[] parameter should throw IllegalArgumentException.");
                                } catch (IllegalArgumentException ok) {
                                } catch (Exception e) {
                                    fail("Null ObjectEventId[] parameter should throw IllegalArgumentException.");
                                }
                                try {
                                    _peer.deleteAlertEvents(user.getId(), ids, null);
                                    fail("Null Connection parameter should throw IllegalArgumentException.");
                                } catch (IllegalArgumentException ok) {
                                } catch (Exception e) {
                                    fail("Null Connection parameter should throw IllegalArgumentException.");
                                }
                                try {
                                    _peer.deleteAlertEvents(FAKE_ID, ids, conn);
                                } catch (Exception e) {
                                    fail("Delete with fake user id must rise no exceptions.");
                                }
                            }
                        case 1:
                            { // all events deleting
                                _peer.deleteAlertEvents(user.getId(), ids, conn);
                                List res = _peer.findAlertEvents(user.getId(), null, conn);
                                assertTrue("Expecting created events delete.", (res == null) ||
                                        (res != null && !res.contains(event1) && !res.contains(event2)));
                                try {
                                    _peer.deleteAlertEvents(user.getId(), ids, conn);
                                } catch (Exception e) {
                                    fail("Repeated delete must rise no excaption.");
                                }
                            }
                            break;
                        case 2:
                            { // events deleting on one
                                _peer.deleteAlertEvents(user.getId(), ids1, conn);
                                List res = _peer.findAlertEvents(user.getId(), null, conn);
                                assertTrue("Expecting created events delete.",
                                        (res != null && !res.contains(event1) && res.contains(event2)));
                                _peer.deleteAlertEvents(user.getId(), ids2, conn);
                                res = _peer.findAlertEvents(user.getId(), null, conn);
                                assertTrue("Expecting created events delete.",
                                        (res != null && !res.contains(event1) && !res.contains(event2)));
                                try {
                                    _peer.deleteAlertEvents(user.getId(), ids, conn);
                                } catch (Exception e) {
                                    fail("Repeated delete must rise no excaption.");
                                }
                            }
                            break;
                    }
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testDeleteAlertEventsByUserId() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Timestamp today = new Timestamp(System.currentTimeMillis());

                try {
                    _peer.deleteAlertEvents(null, today, conn);
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                }
                try {
                    _peer.deleteAlertEvents(FAKE_ID, today, null);
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }

                Integer res = _peer.deleteAlertEvents(FAKE_ID, today, conn);
                assertTrue("Expecting 0 deleted events with fake context id. got: " + res, res.intValue() == 0);

                User user = User.createNewUser("_TestObjectEventPeer_deleteAlEvByUserId_", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_deleteAlertEventsByUserId1_", user);
                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_deleteAlertEventsByUserId2_", user);

                Map params = new HashMap();
                pause(1200);

                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    work1.save(conn);
                    conn.commit();

                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();

                    event2.save(conn);
                    conn.commit();

                    _peer.addAlertEvent(user.getId(), work1.getId(), event1.getEventDate(), conn);
                    _peer.addAlertEvent(user.getId(), work2.getId(), event2.getEventDate(), conn);

                    res = _peer.deleteAlertEvents(user.getId(), today, conn);
                    assertTrue("Expecting 0 deleted events. got: " + res, res.intValue() == 0);

                    GregorianCalendar cl1 = new GregorianCalendar();
                    cl1.add(GregorianCalendar.DATE, 1);
                    Timestamp tomorrow = new Timestamp(cl1.getTime().getTime());

                    res = _peer.deleteAlertEvents(user.getId(), tomorrow, conn);
                    assertTrue("Expecting 2 deleted events. got: " + res, res.intValue() == 2);

                    res = _peer.deleteAlertEvents(user.getId(), tomorrow, conn);
                    assertTrue("Expecting 0 deleted events. got: " + res, res.intValue() == 0);
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindByQuery() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                List res = null;
                final String param_name = ArchivalEvent.OLD_ARCHIVE_STATE_KEY;
                final String param_value = (new Boolean(false)).toString();
                EventQuery query = new EventQuery(context, ArchivalEvent.TYPE);
                query.addParameter(param_name, param_value);
                EventQuery query1 = new EventQuery(context, CreationEvent.TYPE);
                EventQuery queryFake = new EventQuery(context, ArchivalEvent.TYPE);
                queryFake.addParameter(param_name + "_fake", param_value + "_fake");
                EventQuery queryFakeType = new EventQuery(context, FAKE_EVENT_TYPE);
                queryFakeType.addParameter(param_name, param_value);

                try {
                    res = _peer.findByQuery(null, conn);
                    fail("Null EventQuery parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null EventQuery parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findByQuery(query, null);
                    fail("Null connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection parameter should throw IllegalArgumentException.");
                }

                User user = User.createNewUser("_TestObjectEventPeer_findByQuery_", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_findBuQuery1_", user);
                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_findBuQuery1_", user);

                Map params = new HashMap();
                ObjectEvent event3 = ObjectEvent.create(CreationEvent.TYPE, work2, params, user.getId());
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    work1.save(conn);
                    conn.commit();
                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();
                    event2.save(conn);
                    conn.commit();
                    event3.save(conn);
                    conn.commit();

                    res = _peer.findByQuery(queryFakeType, conn);
                    assertNotNull("Expecting empty result set, got null.", res);
                    assertTrue("Expecting empty with fake event type. got: " + res.size(), res.isEmpty());
                    res = _peer.findByQuery(queryFake, conn);
                    assertNotNull("Expecting empty result set, got null.", res);
                    assertTrue("Expecting empty with fake query. got: " + res.size(), res.isEmpty());
                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting empty result set, got null.", res);
                    assertTrue("Expecting another results.", res.contains(event1) && res.contains(event2) && !res.contains(event3));
                    query.setObjectId(work1.getId());
                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting empty result set, got null.", res);
                    assertTrue("Expecting another results.", res.contains(event1) && !res.contains(event2) && !res.contains(event3));

                    final String objEvent_name1 = "objEvent_name1";
                    final String objEvent_value1 = "objEvent_value1";
                    event1.setParameter(objEvent_name1, objEvent_value1);
                    event1.save(conn);
                    query.addParameter(objEvent_name1, objEvent_value1);

                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting empty result set, got null.", res);
                    assertTrue("Expecting another results.", res.contains(event1) && !res.contains(event2) && !res.contains(event3));

                    query1.setObjectId(work2.getId());
                    try {
                        res = _peer.findByQuery(query1, conn);
                        fail("Empty query parameters in EventQury should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    } catch (Exception e) {
                        fail("Empty query parameters in EventQury should throw IllegalArgumentException.");
                    }
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(event3, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    private void pause(long millisec) {
        long curMillisec = System.currentTimeMillis();
        while (System.currentTimeMillis() - curMillisec < millisec) ;
    }

    public void testFindTopAlertEvents()
            throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                List res = null;

                res = _peer.findTopAlertEvents(FAKE_ID, 3, context.getName(), conn);
                assertNotNull("Expecting empty list for no results; got null", res);
                assertTrue("Expecting empty list for fake user id.", res.isEmpty());

                User user = User.createNewUser("_TestObjectEventPeer_finTopAlerts_1", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_findTopAlertEvents1_1", user);
                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_findTopAlertEvents2_1", user);

                Map params = new HashMap();
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                pause(1200);
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                pause(1200);
                ObjectEvent event3 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    work1.save(conn);
                    conn.commit();
                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();
                    event2.save(conn);
                    conn.commit();
                    event3.save(conn);
                    conn.commit();

                    _peer.addAlertEvent(user.getId(), work2.getId(), event1.getEventDate(), conn);
                    _peer.addAlertEvent(user.getId(), work1.getId(), event2.getEventDate(), conn);
                    _peer.addAlertEvent(user.getId(), work2.getId(), event3.getEventDate(), conn);

                    res = _peer.findTopAlertEvents(user.getId(), 3, context.getName(), conn);
                    assertNotNull("Expecting not empty list; got null", res);
                    assertTrue("Expecting items count in result set. got: " + res.size(), res.size() == 3);
                    assertTrue("Expecting another items in result list.",
                            (res.contains(event1)) && (res.contains(event2)) && (res.contains(event3)));
                    res = _peer.findTopAlertEvents(user.getId(), 6, context.getName(), conn);
                    assertNotNull("Expecting not empty list; got null", res);
                    assertTrue("Expecting items count in result set. got: " + res.size(), res.size() == 3);
                    assertTrue("Expecting another items in result list.",
                            (res.contains(event1)) && (res.contains(event2)) && (res.contains(event3)));
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(event3, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testGet()
            throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        Work work = WorkUtil.getNoProject(context);
        User user = Nobody.get(context);
        ObjectEvent res = null;

        conn.setAutoCommit(false);

        try {

            Map params = new HashMap();
            params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
            ObjectEvent event = ObjectEvent.create(ArchivalEvent.TYPE, work, params, user.getId());
            event.save(conn);


            res = _peer.get(work.getId(), event.getEventDate(), context.getName(), conn);
            assertNotNull("Expecting not  null result", res);
            assertTrue("Expecting another result", res.equals(event));

        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testHasAlert()
            throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Boolean res;

                User user = User.createNewUser("_TestObjectEventPeer_hasAlerts_1", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_hasAlerts1_1", user);
                Work work2 = Work.createNew(Work.TYPE, "_TestObjectEventPeer_hasAlerts2_1", user);

                Map params = new HashMap();
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work2, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    res = _peer.hasAlert(event1.getEventId(), user.getId(), conn);
                    assertTrue("Expecting false result for fake (not saved) object event id", !res.booleanValue());

                    work1.save(conn);
                    conn.commit();
                    work2.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();
                    event2.save(conn);
                    conn.commit();

                    try {
                        res = _peer.hasAlert(null, user.getId(), conn);
                        fail("Null ObjectEventId parameter should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    } catch (Exception e) {
                        fail("Null ObjectEventId parameter should throw IllegalArgumentException.");
                    }
                    try {
                        res = _peer.hasAlert(event1.getEventId(), null, conn);
                        fail("Null PeristentKey parameter should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    } catch (Exception e) {
                        fail("Null PeristentKey parameter should throw IllegalArgumentException.");
                    }
                    try {
                        res = _peer.hasAlert(event1.getEventId(), user.getId(), null);
                        fail("Null connection parameter should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    } catch (Exception e) {
                        fail("Null connection parameter should throw IllegalArgumentException.");
                    }

                    res = _peer.hasAlert(event1.getEventId(), FAKE_ID, conn);
                    assertTrue("Expecting false result for fake user id", !res.booleanValue());
                    res = _peer.hasAlert(event1.getEventId(), user.getId(), conn);
                    assertTrue("Expecting false result for object event id", !res.booleanValue());

                    _peer.addAlertEvent(user.getId(), work1.getId(), event1.getEventDate(), conn);

                    res = _peer.hasAlert(event1.getEventId(), user.getId(), conn);
                    assertTrue("Expecting true result for object event id", res.booleanValue());
                    res = _peer.hasAlert(event2.getEventId(), user.getId(), conn);
                    assertTrue("Expecting false result for object event id", !res.booleanValue());

                    _peer.addAlertEvent(user.getId(), work2.getId(), event2.getEventDate(), conn);

                    res = _peer.hasAlert(event1.getEventId(), user.getId(), conn);
                    assertTrue("Expecting true result for object event id", res.booleanValue());
                    res = _peer.hasAlert(event2.getEventId(), user.getId(), conn);
                    assertTrue("Expecting true result for object event id", res.booleanValue());
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testInsert() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        Work work = WorkUtil.getNoProject(context);
        User user = Nobody.get(context);

        conn.setAutoCommit(false);

        try {

            Map params = new HashMap();
            params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
            ObjectEvent event = null;
            event = ObjectEvent.create(ArchivalEvent.TYPE, work, params, user.getId());

            try {
                _peer.insert(null, conn);
                fail("Null Event Object parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Event Object parameter should throw IllegalArgumentException.");
            }


            try {
                _peer.insert(event, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            _peer.insert(event, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select object_id from object_event where object_id='" + work.getId() + "'" +
                    " and event_date={ts'" + event.getEventDate().toString() + "'}");

            assertTrue("Expecting record in resultset", rset.next());

        } finally {
            conn.rollback();
            conn.close();
        }

    }

    public void testSaveParameters()
            throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                List res = null;
                final String objEvent_name = "objEvent_name";
                final String objEvent_value = "objEvent_value";

                User user = User.createNewUser("_TestObjectEvent_saveParameters_", context);
                user.setFirstName("objEvent_user");

                Work work1 = Work.createNew(Work.TYPE, "_TestObjectEvent_saveParameters1_1", user);

                Map params = new HashMap();
                params.put(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, new Boolean(false));
                ObjectEvent event1 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                ObjectEvent event2 = ObjectEvent.create(ArchivalEvent.TYPE, work1, params, user.getId());
                try {
                    user.save(conn);
                    conn.commit();

                    work1.save(conn);
                    conn.commit();

                    event1.save(conn);
                    conn.commit();
                    event2.save(conn);
                    conn.commit();

                    EventQuery query = new EventQuery(context, ArchivalEvent.TYPE);
                    query.addParameter(ArchivalEvent.OLD_ARCHIVE_STATE_KEY, (new Boolean(false)).toString());
                    query.setObjectId(work1.getId());

                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting not empty result set. got null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("Expecting another items in result set.", res.contains(event1) && res.contains(event2));

                    query.addParameter(objEvent_name, objEvent_value);

                    event1.setParameter(objEvent_name, objEvent_value);
                    event1.save(conn);

                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting not empty result set. got null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.", res.contains(event1));

                    event2.setParameter(objEvent_name, objEvent_value);
                    event2.save(conn);

                    res = _peer.findByQuery(query, conn);
                    assertNotNull("Expecting not empty result set. got null.", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("Expecting another items in result set.", res.contains(event1) && res.contains(event2));
                } finally {
                    delete(event1, conn);
                    delete(event2, conn);
                    delete(work1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

	public void testUpdate()
	throws Exception
	{
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				List res = null;
				try {
					_peer.update(null, conn);
					fail("Null Event Object parameter should throw IllegalArgumentException.");
				}
				catch (IllegalArgumentException ok) {}
				catch (Exception e) {
					fail("Null Event Object parameter should throw IllegalArgumentException.");
				}

				User user = User.createNewUser("_TestObjectEvent_update_1", context);
				user.setFirstName("objEvent_user");
				
				Work work1 = Work.createNew(Work.TYPE, "_TestObjectEvent_update1_1", user);
				
				Map params = new HashMap();
				ObjectEvent event1 = ObjectEvent.create(LoginEvent.TYPE, work1, params, user.getId());
				params.put(BudgetChangeEvent.WORK_NAME, "blah_blah");
				params.put(BudgetChangeEvent.NEW_VALUE, "10");
				
				ObjectEvent event2 = ObjectEvent.create(BudgetChangeEvent.TYPE, work1, params, user.getId());
				
				try {
					user.save(conn);
					conn.commit();

					work1.save(conn);
					conn.commit();

					event1.save(conn);
					conn.commit();
					event2.save(conn);
					conn.commit();

					try {
						_peer.update(event1, null);
						fail("Null Connection parameter should throw IllegalArgumentException.");
					}
					catch (IllegalArgumentException ok) {}
					catch (Exception e) {
						fail("Null Connection parameter should throw IllegalArgumentException.");
					}

					_peer.update(event1, conn);
					_peer.update(event2, conn);

					EventQuery query = new EventQuery(context.getName());
					query.setObjectId(work1.getId());
					
					res = _peer.findByQuery(query, conn);
					assertTrue("Expecting another items in result set.", res != null
						&& res.contains(event1) && res.contains(event2));

					event1.setIsVisible(new Boolean(!event1.isVisible()));
					event2.setIsVisible(new Boolean(!event2.isVisible()));
					event1.setWasAlertProcessed(new Boolean(!event1.getWasAlertProcessed().booleanValue()));
					event2.setWasAlertProcessed(new Boolean(!event2.getWasAlertProcessed().booleanValue()));

					_peer.update(event1, conn);
					_peer.update(event2, conn);

					res = _peer.findByQuery(query, conn);
					assertTrue("Expecting another items in result set.", (res != null)
						&& res.contains(event1) && res.contains(event2));
				}
				finally {
					delete(event1, conn);
					delete(event2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}
}
