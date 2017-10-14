package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.FileDocument;
import com.cinteractive.ps3.entities.Admins;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.LoginEvent;
import com.cinteractive.ps3.events.ObjectEvent;
import com.cinteractive.ps3.mimehandler.HandlerRegistry;
import com.cinteractive.ps3.mimehandler.PSMimeHandler;
import com.cinteractive.ps3.reports.filters.ByLoginFilter;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.test.MockPSSession;
import com.cinteractive.ps3.test.MockServletRequest;
import com.cinteractive.ps3.tollgate.TollPhase;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.FileFolder;
import com.cinteractive.ps3.work.ProjectEvent;
import com.cinteractive.ps3.work.ScheduleSettingsException;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.entityfolder.HomeFolder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.Cookie;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestUserPeer extends TestJdbcPeer {
    private static final boolean DEBUG = false;


    private UserPeer _peer;


    static {
        registerCase();
    }

    private static void registerCase() {
        TestSql.registerTestCase(UserPeer.class.getName(), TestUserPeer.class.getName());
    }

    public TestUserPeer(String name) {
        super(name);
    }


    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestUserPeer");

        suite.addTest(new TestUserPeer("testGetNoGroupsUserIds"));
        suite.addTest(new TestUserPeer("testCountLogins"));
        suite.addTest(new TestUserPeer("testInsertDelete"));
        suite.addTest(new TestUserPeer("testFindDeleteNotificationIds"));
        suite.addTest(new TestUserPeer("testFindUserEmails"));
        suite.addTest(new TestUserPeer("testFindUserIdByLogin"));
        suite.addTest(new TestUserPeer("testFindIdByActiveLoginCookie"));
        suite.addTest(new TestUserPeer("testFindUserIdByPublicKey"));
        suite.addTest(new TestUserPeer("testGetOwnedGroupsContract"));
        suite.addTest(new TestUserPeer("testGetOwnedGroups"));
        suite.addTest(new TestUserPeer("testGetOwnedProjectIds"));
        suite.addTest(new TestUserPeer("testGetUserIdList"));
        suite.addTest(new TestUserPeer("testRoleDeleted"));
        suite.addTest(new TestUserPeer("testUpdateRestore"));
        suite.addTest(new TestUserPeer("testGetUsersLastLogin"));
        suite.addTest(new TestUserPeer("testFindUserIdsByLoginDate"));
        suite.addTest(new TestUserPeer("testFindIdsByTypeDatesAndUser"));
        suite.addTest(new TestUserPeer("testFindIdsByDueDatesAndUser"));
        suite.addTest(new TestUserPeer("testFindMyWorkIds"));
        suite.addTest(new TestUserPeer("testFindWorkloadIds"));

        return suite;
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (UserPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return UserPeer.getUserPeer(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null UserPeer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testCountLogins() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String login = "fakeLogin";

        Integer res;

        try {
            try {
                res = _peer.countLogins(null, context.getId(), conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.countLogins(login, null, conn);
                fail("Null ContextId parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ContextId parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.countLogins(login, context.getId(), null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }


            res = _peer.countLogins(login, context.getId(), conn);

            assertTrue("Expecting result = 0 due to fake login", res.intValue() == 0);
        } finally {
            conn.close();
        }

        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                User user1 = null;
                try {
                    DEBUG_DELETE("__TestUserPeer_countLogins_email1__", context);
                    user = User.createNewUser("__TestUserPeer_countLogins_email1__", context);
                    user.setLogin("testlogin1");
                    DEBUG_DELETE("__TestUserPeer_countLogins_email2__", context);
                    user1 = User.createNewUser("__TestUserPeer_countLogins_email2__", context);
                    user1.setLogin("testlogin1");
                    user.save(conn);
                    conn.commit();
                    user1.save(conn);
                    conn.commit();
                    Integer c = _peer.countLogins("testlogin1", FAKE_ID, conn);
                    assertTrue("Expecting 0 for fake context id; got " + c, c.intValue() == 0);
                    c = _peer.countLogins("testlogin1", context.getId(), conn);
                    assertTrue("Expecting 2 for real context id; got " + c, c.intValue() == 2);
                    user1.setLogin("testlogin2");
                    user1.save(conn);
                    conn.commit();
                    c = _peer.countLogins("testlogin1", context.getId(), conn);
                    assertTrue("Expecting 1 for real context id; got " + c, c.intValue() == 1);
                    //user.setModifiedById(Nobody.get(getContext()).getId());
                    user.deleteHard(conn);
                    conn.commit();
                    user = null;
                    c = _peer.countLogins("testlogin1", context.getId(), conn);
                    assertTrue("Expecting 0 for real context id; got " + c, c.intValue() == 0);
                } finally {
                    delete(user, conn);
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();
    }

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
              //  if (o instanceof PSObject)
               //     ((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }


    public void testFindDeleteNotificationIds() throws Exception {
        final Connection conn = getConnection();
        Set res = null;

        try {
            try {
                res = _peer.findDeleteNotificationIds(null, conn);
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findDeleteNotificationIds(FAKE_ID, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findDeleteNotificationIds(FAKE_ID, conn);
            assertNotNull("Null result.", res);
            assertTrue("Expecting empty result for fake User id '" + FAKE_ID + "'.", res.isEmpty());
        } finally {
            conn.close();
        }

        final InstallationContext context = getContext();
        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                Work work = null;
                FileDocument doc = null;
                try {
                    DEBUG_DELETE("_TestUserPeer_findDeleteNotifications_", context);
                    user = User.createNewUser("_TestUserPeer_findDeleteNotifications_", context);
                    user.save(conn);
                    conn.commit();

                    work = Work.createNew(Work.TYPE, "_TestUserPeer_FindDeleteNotifications_", user);
                    work.save(conn);
                    conn.commit();

                    Set __ids = _peer.findDeleteNotificationIds(user.getId(), conn);
                    assertTrue("Expecting 2 notification ids; got " + __ids.size(), __ids.size() == 2);
                    assertTrue("Expecting another notification ids;", __ids.contains(work.getId()));
                    assertTrue("Expecting another notification ids;", __ids.contains(user.getFolder().getId()));

                    doc = FileDocument.createNew("_TestUserPeer::findDeleteNotificationsIds_", user);
                    doc.save(conn);
                    conn.commit();

                    __ids = _peer.findDeleteNotificationIds(user.getId(), conn);
                    assertTrue("Expecting 3 notification ids; got " + __ids.size(), __ids.size() == 3);
                    assertTrue("Expecting another notification ids;", __ids.contains(work.getId()));
                    assertTrue("Expecting another notification ids;", __ids.contains(user.getFolder().getId()));
                    assertTrue("Expecting another notification ids;", __ids.contains(doc.getId()));

                } finally {
                    delete(doc, conn);
                    delete(work, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();

    }

    public void testFindUserEmails() throws Exception {
        final Connection conn = getConnection();
        Set res = null;
        final InstallationContext context = InstallationContext.get(getContextName());

        try {
            try {
                res = _peer.findUserEmails(null, conn);
                fail("Null InstallationContext parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null InstallationContext parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findUserEmails(context.getId(), null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findUserEmails(context.getId(), conn);
            assertNotNull("Null result.", res);
        } finally {
            conn.close();
        }
        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                try {
                    final String email = "_TestUserPeer_findUserEmails_";
                    int oldCount = _peer.findUserEmails(context.getId(), conn).size();

                    DEBUG_DELETE(email, context);
                    user = User.createNewUser(email, context);
                    user.save(conn);
                    conn.commit();

                    Set set = _peer.findUserEmails(context.getId(), conn);
                    int newCount = set.size();
                    assertTrue("Expecting new email in resulting set", newCount == ++oldCount && set.contains(email));
                } finally {
                    delete(user, conn);
                }
                return null;
            }
        }.execute();

    }

    public void testFindUserIdByLogin() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "NewFakeEmailAddress";
        final String login = "NewFakeLogin";

        conn.setAutoCommit(false);

        try {
            User user = User.createNewUser(email, context);
            user.setLogin(login);
            _peer.insert(user, conn);
            PersistentKey res = null;


            try {
                res = _peer.findUserIdByLogin(null, context.getId(), conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findUserIdByLogin(login, null, conn);
                fail("Null ContextId parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ContextId parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findUserIdByLogin(login, context.getId(), null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findUserIdByLogin(login, context.getId(), conn);

            assertNotNull("Expecting not null PersistentKey result object", res);
            assertTrue("Expecting user id in resulting set", res.equals(user.getId()));
        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testFindIdByActiveLoginCookie() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "NewFakeEmailAddress";
        final String login = "NewFakeLogin";
        Calendar cal = Calendar.getInstance();
        Timestamp today = new Timestamp(new java.util.Date().getTime());
        cal.setTime(today);
        cal.add(Calendar.DATE, 1);
        Timestamp tomorrow = new Timestamp(cal.getTime().getTime());


        conn.setAutoCommit(false);

        try {
            User user = User.createNewUser(email, context);
            _peer.insert(user, conn);
            final Statement stmt = conn.createStatement();
            stmt.execute("UPDATE PSUser set login_cookie_id = '" + login + "', cookie_xprtn_date={ts '" + tomorrow.toString() + "'} where user_id='" + user.getId().toString() + "'");


            PersistentKey res = null;


            try {
                res = _peer.findIdByActiveLoginCookie(null, conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findIdByActiveLoginCookie(login, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findIdByActiveLoginCookie(login, conn);

            assertNotNull("Expecting not null PersistentKey result object", res);
            assertTrue("Expecting user id in resulting set", res.equals(user.getId()));
        } finally {
            conn.rollback();
            conn.close();
        }

    }

    public void testFindUserIdByPublicKey() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "NewFakeEmailAddress";
        final String keyName = "NewFakeKey";
        final String keyValue = "NewFakeKeyValue";

        conn.setAutoCommit(false);

        try {
            User user = User.createNewUser(email, context);
            _peer.insert(user, conn);
            final Statement stmt = conn.createStatement();
            stmt.execute("INSERT INTO Object_attribute (object_id, attribute_name, string_value) " +
                    "values ('" + user.getId().toString() + "','" + keyName + "', '" + keyValue + "')");

            PersistentKey res = null;


            try {
                res = _peer.findUserIdByPublicKey(null, keyValue, conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findUserIdByPublicKey(keyName, null, conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findUserIdByPublicKey(keyName, keyValue, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findUserIdByPublicKey(keyName, keyValue, conn);

            assertNotNull("Expecting not null PersistentKey result object", res);
            assertTrue("Expecting user id in resulting set", res.equals(user.getId()));
        } finally {
            conn.rollback();
            conn.close();
        }
    }

    public void testGetNoGroupsUserIds()
            throws SQLException {
        // empty case
        final List ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return _peer.getNoGroupsUserIds(FAKE_ID, conn);
            }
        }.execute();
        assertNotNull("Expecting empty user ids for not results; got null.", ids);
        assertTrue("Expecting empty ids for fake context id '" + FAKE_ID + "'.", ids.isEmpty());

        final InstallationContext context = getContext();

        // some data
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return _peer.getNoGroupsUserIds(context.getId(), conn);
            }
        }.execute();

        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                User user1 = null;
                try {
                    int count = _peer.getNoGroupsUserIds(context.getId(), conn).size();
                    DEBUG_DELETE("_TestUserPeer_NoGroupsUsers_", context);
                    user = User.createNewUser("_TestUserPeer_NoGroupsUsers_", context);
                    user.save(conn);
                    conn.commit();

                    DEBUG_DELETE("_TestUserPeer_NoGroupsUsers1_", context);
                    user1 = User.createNewUser("_TestUserPeer_NoGroupsUsers1_", context);
                    user1.save(conn);
                    conn.commit();

                    List ids = _peer.getNoGroupsUserIds(context.getId(), conn);
                    int newCount = ids.size();
                    assertTrue("Expecting " + (count + 2) + " ids; got " + newCount, count + 2 == newCount);
                    assertTrue("Expecting ids in resulting set", ids.contains(user.getId()) && ids.contains(user1.getId()));
                    final Group admins = Admins.get(context);
                    admins.addUser(user);
                    admins.save(conn);
                    conn.commit();

                    ids = _peer.getNoGroupsUserIds(context.getId(), conn);
                    newCount = ids.size();
                    assertTrue("Expecting " + (count + 1) + " ids; got " + newCount, count + 1 == newCount);
                    assertTrue("Expecting ids in resulting set", !ids.contains(user.getId()) && ids.contains(user1.getId()));

                    admins.removeMember(user, user1);
                    admins.save(conn);
                    conn.commit();
                } finally {
                    delete(user, conn);
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testGetOwnedGroups()
            throws Exception {
        // real data
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                final String sql =
                        "SELECT e.owner_id, COUNT(*) " +
                        "FROM Entity e, Object o " +
                        "WHERE e.entity_id = o.object_id " +
                        "AND o.object_type = 'Group' " +
                        "AND o.context_id = '" + context.getId() + "' " +
                        "GROUP BY e.owner_id " +
                        "ORDER BY COUNT(*) DESC";

                PersistentKey id = null;
                int count = 0;

                final Statement stmt = conn.createStatement();
                final ResultSet rset = stmt.executeQuery(sql);
                if (rset.next()) {
                    id = Uuid.get(rset.getString(1));
                    count = rset.getInt(2);
                }
                rset.close();
                stmt.close();

                if (id == null)
                    return null;

                final List ids = _peer.getOwnedGroups(id, conn);
                assertTrue("Expecting " + count + " got " + ids.size() + " ids for user id ''.", ids.size() == count);
                return ids;
            }
        }.execute();
    }

    public void testGetOwnedGroupsContract()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                try {
                    _peer.getOwnedGroups(null, conn);
                    fail("Null PersistentKey should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.getOwnedGroups(FAKE_ID, null);
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }

                final List ids = _peer.getOwnedGroups(FAKE_ID, conn);
                assertNotNull("Null result.", ids);
                assertTrue("Expecting empty ids for fake user id.", ids.isEmpty());
                return null;
            }
        }.execute();
    }

    public void testGetOwnedProjectIds() throws Exception {
        final Connection conn = getConnection();
        Set res = null;

        try {
            try {
                res = _peer.getOwnedProjectIds(null, conn);
                fail("Null PersistentKey should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.getOwnedProjectIds(FAKE_ID, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(null, statuses, conn);
                fail("Null user id parameter should throw IllegalArgumentException exception");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null user id parameter should throw IllegalArgumentException exception");
            }
            try {
                res = _peer.getOwnedProjectIds(FAKE_ID, null, conn);
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null statuses set parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(FAKE_ID, statuses, null);
                fail("Null connection parameter should throw IllegalArgumentException exception");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null connection parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(null, statuses, statuses, true, conn);
                fail("Null user id parameter should throw IllegalArgumentException exception");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null user id parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(null, statuses, statuses, false, conn);
                fail("Null user id parameter should throw IllegalArgumentException exception");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null user id parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(FAKE_ID, null, statuses, true, conn);
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null statuses set parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(FAKE_ID, statuses, statuses, true, conn);
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null types set parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(FAKE_ID, statuses, statuses, false, conn);
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null types set parameter should throw IllegalArgumentException exception");
            }
            try {
                Set statuses = new TreeSet();
                res = _peer.getOwnedProjectIds(FAKE_ID, statuses, statuses, true, null);
                fail("Null connection parameter should throw IllegalArgumentException exception");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null connection parameter should throw IllegalArgumentException exception");
            }

            res = _peer.getOwnedProjectIds(FAKE_ID, conn);
            assertNotNull("Null result.", res);
        } finally {
            conn.close();
        }

        // real data
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                Work work1 = null;
                Work work2 = null;
                try {
                    DEBUG_DELETE("_TestUserPeer_OwnedProjects_", context);
                    user = User.createNewUser("_TestUserPeer_OwnedProjects_", context);
                    user.save(conn);
                    conn.commit();

                    Set ids = _peer.getOwnedProjectIds(user.getId(), conn);
                    assertTrue("Expecting result set only with entity folder", ids.size() == 1);
                    assertTrue("Expecting result set only with entity folder", ids.contains(user.getFolder().getId()));

                    Set statuses = new TreeSet();
                    statuses.add(WorkStatus.COMPLETED);
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, conn);
                    assertTrue("Expecting result set only with entity folder", ids.size() == 1);
                    assertTrue("Expecting result set only with entity folder", ids.contains(user.getFolder().getId()));
                    Set types = new TreeSet();
                    types.add(Work.TYPE);
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, true, conn);
                    assertTrue("Expecting empty result set", ids.isEmpty());
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, false, conn);
                    assertTrue("Expecting result set only with entity folder", ids.size() == 1);
                    assertTrue("Expecting result set only with entity folder", ids.contains(user.getFolder().getId()));

                    work1 = Work.createNew(Work.TYPE, "_TestUserPeer_OwnedProjects1_", user);
                    work1.setStatus(WorkStatus.NOT_STARTED);
                    work1.save(conn);
                    conn.commit();

                    ids = _peer.getOwnedProjectIds(user.getId(), conn);
                    assertTrue("Expecting 1 id in result set", ids.size() == 2);
                    assertTrue("Expecting valid id", ids.contains(work1.getId()) &&
                            ids.contains(user.getFolder().getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, conn);
                    assertTrue("Expecting 1 id in result set", ids.size() == 2);
                    assertTrue("Expecting valid id", ids.contains(work1.getId()) &&
                            ids.contains(user.getFolder().getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, true, conn);
                    assertTrue("Expecting 1 id in result set", ids.size() == 1);
                    assertTrue("Expecting valid id in resulting set", ids.contains(work1.getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, false, conn);
                    assertTrue("Expecting result set only with entity folder", ids.size() == 1);
                    assertTrue("Expecting result set only with entity folder", ids.contains(user.getFolder().getId()));

                    work2 = Work.createNew(FileFolder.TYPE, "_TestUserPeer::getOwnedProjects_folder_", user);
                    work2.save(conn);
                    conn.commit();

                    ids = _peer.getOwnedProjectIds(user.getId(), conn);
                    assertTrue("Expecting 2 ids in resulting set", ids.size() == 3);
                    assertTrue("Expecting valid ids", ids.contains(work1.getId()) &&
                            ids.contains(work2.getId()) &&
                            ids.contains(user.getFolder().getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, conn);
                    assertTrue("Expecting 2 ids in resulting set", ids.size() == 3);
                    assertTrue("Expecting valid ids", ids.contains(work1.getId()) &&
                            ids.contains(work2.getId()) &&
                            ids.contains(user.getFolder().getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, true, conn);
                    assertTrue("Expecting 1 id in resulting set", ids.size() == 1);
                    assertTrue("Expecting valid id in resulting set", ids.contains(work1.getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, false, conn);
                    assertTrue("Expecting 2 ids in resulting set", ids.size() == 2);
                    assertTrue("Expecting valid id in resulting set", ids.contains(work2.getId()) &&
                            ids.contains(user.getFolder().getId()));

                    types.add(FileFolder.TYPE);
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, true, conn);
                    assertTrue("Expecting 2 id in resulting set", ids.size() == 2);
                    assertTrue("Expecting valid ids", ids.contains(work1.getId()) && ids.contains(work2.getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, false, conn);
                    assertTrue("Expecting result set only with entity folder", ids.size() == 1);
                    assertTrue("Expecting result set only with entity folder", ids.contains(user.getFolder().getId()));

                    statuses.add(WorkStatus.NOT_STARTED);
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, conn);
                    assertTrue("Expecting 1 ids in resulting set", ids.size() == 2);
                    assertTrue("Expecting valid id", ids.contains(work2.getId()) &&
                            ids.contains(user.getFolder().getId()));
                    ids = _peer.getOwnedProjectIds(user.getId(), statuses, types, true, conn);
                    assertTrue("Expecting 1 id in resulting set", ids.size() == 1);
                    assertTrue("Expecting valid id in resulting set", ids.contains(work2.getId()));
                } finally {
                    delete(work2, conn);
                    delete(work1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();


    }

    public void testGetUserIdList()
		throws Exception
		{
        final Connection conn = getConnection();
        List res = null;
        
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();

        try {
            try {
                res = _peer.getUserIdList(null, conn);
                fail("Null InstallationContext parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null InstallationContext parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.getUserIdList(contextId, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.getUserIdList(contextId, conn);
            assertNotNull("Null result.", res);
        } finally {
            conn.close();
        }

        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                User user1 = null;
                try {
                    DEBUG_DELETE("_TestUserPeer_getUserList1_", context);
                    user = User.createNewUser("_TestUserPeer_getUserList1_", context);
                    user.save(conn);
                    conn.commit();

                    List ids = _peer.getUserIdList(contextId, conn);
                    assertTrue("Expecting at least 1 user in resulting set; got " + ids.size(), ids.size() >= 1);
                    assertTrue("Expecting user id in resulting set", ids.contains(user.getId()));

                    DEBUG_DELETE("_TestUserPeer_getUserList2_", context);
                    user1 = User.createNewUser("_TestUserPeer_getUserList2_", context);
                    user1.save(conn);
                    conn.commit();

                    ids = _peer.getUserIdList(contextId, conn);
                    assertTrue("Expecting at least 2 users in resulting set; got " + ids.size(), ids.size() >= 2);
                    assertTrue("Expecting user ids in resulting set", ids.contains(user.getId()) && ids.contains(user1.getId()));
                } finally {
                    delete(user1, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();

    }

    public void testInsertDelete() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "NewFakeEmailAddress";

        conn.setAutoCommit(false);

        try {
            try {
                _peer.insert(null, conn);
                fail("Null Entity Object in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Entity Object in insert method should throw IllegalArgumentException.");
            }

            User user = User.createNewUser(email, context);
            user.setPassword("psw");
            user.setPasswordHint("pass hint");

            try {
                _peer.insert(user, null);
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            }

            _peer.insert(user, conn);
            PersistentKey userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);

            assertNotNull("Expecting not null PersistentKey result object", userId);

            RestoreMap data = _peer.getRestoreData(user.getId(), conn);
            assertTrue("Expecting data for new inserted user", compare(user, data));

            try {
                _peer.delete(null, conn);
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            }

            try {
                _peer.delete(user, null);
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            }

            _peer.delete(user, conn);
            userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);
            assertNull("Expecting null PersistentKey result object", userId);
        } finally {
            conn.rollback();
            conn.close();
        }

    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null || o2 == null) return false;
        return (o1.equals(o2));
    }

    private boolean equals(Timestamp data1, Object data2) {
        if (data1 == null && data2 == null) return true;
        if (data1 == null || data2 == null) return false;
        if (!(data2 instanceof Timestamp)) return false;
        data1.setNanos(0);
        ((Timestamp) data2).setNanos(0);
        return data1.equals(data2);
    }

    private void err(String attrName) {
        System.out.println("Wrong value of '" + attrName + "' attribute");
    }

    private final boolean compare(User user, RestoreMap data) {
        boolean equal = true;
        if (equal && !equals(user.getLogin(), data.get(User.LOGIN)) && !(equal = false)) {
            err("Login");
        }
        if (equal && !equals(user.getPassword(), data.get(User.PASSWORD)) && !(equal = false)) {
            err("Password");
        }
        if (equal && !equals(user.getPasswordHint(), data.get(User.PASSWORD_HINT)) && !(equal = false)) {
            err("Password Hint");
        }
        if (equal && !equals(user.getLoginCookieId(), data.get(User.LOGIN_COOKIE_ID)) && !(equal = false)) {
            err("Login Cookie Id");
        }
        if (equal && !equals(user.getLoginCookieExpirationDate(), data.get(User.LOGIN_COOKIE_EXPIRATION_DATE)) && !(equal = false)) {
            err("Login Cookie Expiration Date");
        }
        if (equal && !equals(user.getLastLoginDate(), data.get(User.LAST_LOGIN_DATE)) && !(equal = false)) {
            err("Last Login Date");
        }
        if (equal && !equals(user.getInviterId(), data.get(User.INVITER_ID)) && !(equal = false)) {
            err("Inviter Id");
        }
        return equal;
    }

    public void testRoleDeleted() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());

        try {

            try {
                _peer.roleDeleted(null, conn);
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.roleDeleted(FAKE_ID, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            _peer.roleDeleted(FAKE_ID, conn);
        } finally {
            conn.close();
        }

        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Work work = null;
                User user = null;
                TagSet roles = null;
                try {
                    roles = TagSet.createNew("_TestUserPeer_TagSet", context);
                    final PSTag role = roles.addTag("_test_tag_");
                    roles.save(conn);
                    conn.commit();

                    DEBUG_DELETE("_TestUserPeer_RoleDeleted_", context);
                    user = User.createNewUser("_TestUserPeer_RoleDeleted_", context);
                    final User nobody = Nobody.get(context);
                    work = Work.createNew(Work.TYPE, "_TestUserPeer::testRoleDeleted_", nobody);
                    work.generateAlerts(false);
                    user.generateAlerts(false);
                    user.save(conn);
                    conn.commit();

                    work.save(conn);
                    conn.commit();

                    work.addTeamMember(user, nobody);
                    work.save(conn);
                    conn.commit();

                    work.getTeamMember(user).setRoleId(role.getId().toString());
                    work.save(conn);
                    conn.commit();

                    final WorkPeer peer = WorkPeer.getWorkPeer(conn);
                    peer.restore(work, conn);
                    assertTrue("Expecting role", role.getId().toString().equals(work.getTeamMember(user).getRoleId()));

                    work.setOwnerRole(role.getId().toString());
                    work.save(conn);
                    conn.commit();

                    peer.restore(work, conn);
                    assertTrue("Expecting role", role.getId().toString().equals(work.getOwnerRoleId()));
                    _peer.roleDeleted(role.getId(), conn);
                    conn.commit();

                    peer.restore(work, conn);
                    assertTrue("Unexpected role found", work.getOwnerRoleId() == null && work.getTeamMember(user).getRoleId() == null);
                } finally {
                    delete(work, conn);
                    delete(user, conn);
                    delete(roles, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testUpdateRestore()
            throws Exception {
        final String email = "NewFakeEmailAddress";
        final InstallationContext context = getContext();

        new Update(this) {
            protected Object doUpdate(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);
                User user = User.createNewUser(email, context);
                user.generateAlerts(false);

                _peer.insert(user, conn);

                try {
                    PersistentKey userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);
                    assertNotNull("Expecting not null PersistentKey result object", userId);

                    try {
                        _peer.update(null, conn);
                        fail("Null User should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.update(user, null);
                        fail("Null Connection should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }

                    user.setLastName("LastName");
                    _peer.update(user, conn);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }

                return null;
            }
        }.execute();
        // test getRestoreData. empty case
        new Update(this) {
            protected Object doUpdate(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);
                User user = User.createNewUser(email, context);
                _peer.insert(user, conn);

                try {
                    _peer.getRestoreData(null, conn);
                    fail("Null user id should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.getRestoreData(FAKE_ID, null);
                    fail("Null Connection should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }

                try {
                    RestoreMap res = _peer.getRestoreData(user.getId(), conn);
                    assertNotNull("Expecting not null Hashtable from UserPeer.getRestoreData", res);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
        // additional test
        new Update(this) {
            protected Object doUpdate(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);

                Calendar c = Calendar.getInstance();
                c.set(Calendar.MILLISECOND, 0);

                DEBUG_DELETE("_TestUserPeer_RoleDeleted_", context);
                User user = User.createNewUser("_TestUserPeer_Update_", context);
                user.generateAlerts(false);
                user.setLogin("fake_login");
                user.setPassword("pswd");
                user.setPasswordHint("pswd hint");
                user.setLastLoginDate(new Timestamp(c.getTime().getTime()));
                Cookie cookie = new Cookie("cname", "cvalue");
                cookie.setMaxAge(10);
                user.setLoginCookie(cookie);
                _peer.insert(user, conn);

                try {
                    RestoreMap data = _peer.getRestoreData(user.getId(), conn);
                    assertNotNull("Expecting data for user id", data);
                    assertTrue("Expecting valid user attributes", compare(user, data));

                    /* triggers a query (=deadlock in mssqlserver if transaction isolation not READ_UNCOMMITTED)
                    user.setLogin("newfakelogin");
                    */
                    user.setPassword("newpswd");
                    user.setPasswordHint("newpswdhint");
                    cookie.setValue("newcookievalue");
                    cookie.setMaxAge(0);
                    user.setLoginCookie(cookie);
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    user.setLastLoginDate(new Timestamp(c.getTime().getTime()));
                    _peer.update(user, conn);

                    data = _peer.getRestoreData(user.getId(), conn);
                    assertNotNull("Expecting data for user id", data);
                    assertTrue("Expecting valid user attributes", compare(user, data));
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }

                return null;
            }
        }.execute();
    }

    public void testGetUsersLastLogin()
            throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();
        
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return _peer.getUsersLastLogin(contextId, 512, conn);
            }
        }.execute();

        new Update(this) {
            protected Object doUpdate(Connection conn)
                    throws SQLException {
                User user = null;
                User user1 = null;

                try {
                    final Calendar c = Calendar.getInstance();
                    c.add(Calendar.DAY_OF_YEAR, -10);

                    DEBUG_DELETE("_TestUserPeer_LastLogin_", context);
                    user = User.createNewUser("_TestUserPeer_LastLogin_", context);

                    user.setLastLoginDate(new Timestamp(c.getTime().getTime()));
                    user.save(conn);
                    conn.commit();

                    DEBUG_DELETE("_TestUserPeer_LastLogin1_", context);
                    user1 = User.createNewUser("_TestUserPeer_LastLogin1_", context);
                    c.add(Calendar.DAY_OF_YEAR, -10);
                    user1.setLastLoginDate(new Timestamp(c.getTime().getTime()));
                    user1.save(conn);
                    conn.commit();

                    List ids = _peer.getUsersLastLogin(contextId, 30, conn);
                    assertNotNull("Expecting ids list; got null", ids);
                    assertTrue("Expecting no user id == " + user.getId().toString() + " or " + user1.getId().toString(), !ids.contains(user.getId()) && !ids.contains(user1.getId()));

                    ids = _peer.getUsersLastLogin(contextId, 2, conn);
                    assertNotNull("Expecting ids list; got null", ids);
                    assertTrue("Expecting 2 user ids", ids.contains(user.getId()) && ids.contains(user1.getId()));
                    ids = _peer.getUsersLastLogin(contextId, 15, conn);
                    assertNotNull("Expecting ids list; got null", ids);
                    assertTrue("Expecting 1 user id", ids.contains(user1.getId()));

                    ids = _peer.getUsersLastLogin(contextId, 19, conn);
                    assertNotNull("Expecting ids list; got null", ids);
                    assertTrue("Expecting 1 user id", ids.contains(user1.getId()));

                    ids = _peer.getUsersLastLogin(contextId, 20, conn);
                    assertNotNull("Expecting ids list; got null", ids);
                    assertTrue("Unexpected user id", !ids.contains(user1.getId()));
                } finally {
                    delete(user1, conn);
                    delete(user, conn);
                }

                return null;
            }
        }.execute();
    }

    public void testFindUserIdsByLoginDate() throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                ByLoginFilter blf = new ByLoginFilter(true);
                blf.setAllUsers(true);
                ByLoginFilter blfLog = new ByLoginFilter(true);
                blfLog.setAllUsers(false);
                blfLog.setIsLogin(new Boolean(true));

                try {
                    _peer.findUserIdsByLoginDate(null, FAKE_ID, conn);
                    fail("Null User should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.findUserIdsByLoginDate(blf, null, conn);
                    fail("Null User should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }
                try {
                    _peer.findUserIdsByLoginDate(blf, FAKE_ID, null);
                    fail("Null User should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                }


                int res = _peer.findUserIdsByLoginDate(blf, FAKE_ID, conn);
                assertTrue("Expecting result = 0 for fake context.", res == 0);

                DEBUG_DELETE("_TestUserPeer_ByLogin_1", context);
                DEBUG_DELETE("_TestUserPeer_ByLogin_2", context);
                DEBUG_DELETE("_TestUserPeer_ByLogin_3", context);
                DEBUG_DELETE("_TestUserPeer_ByLogin_4", context);
                User user1 = User.createNewUser("_TestUserPeer_ByLogin_1", context);
                User user2 = User.createNewUser("_TestUserPeer_ByLogin_2", context);
                User user3 = User.createNewUser("_TestUserPeer_ByLogin_3", context);
                User user4 = User.createNewUser("_TestUserPeer_ByLogin_4", context);
                ObjectEvent le2 = null;

                long today = System.currentTimeMillis();
                try {
                    int baseAll = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    blf.setScope(ByLoginFilter.LOGED_IN);
                    int baseNLogged = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    int baseLogged = _peer.findUserIdsByLoginDate(blfLog, contextId, conn);
                    blfLog.setStart(new Date(today - 24L * 60L * 60L * 1000L));
                    blfLog.setEnd(new Date(today + 48L * 60L * 60L * 1000L));
                    blfLog.setIsLogin(new Boolean(true));
                    int baseLog1 = _peer.findUserIdsByLoginDate(blfLog, contextId, conn);
                    blfLog.setIsLogin(new Boolean(false));
                    int baseLog2 = _peer.findUserIdsByLoginDate(blfLog, contextId, conn);

                    user1.save(conn);
                    user2.save(conn);
                    user3.save(conn);
                    user4.save(conn);
                    conn.commit();

                    blf.setAllUsers(true);
                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 4 new users in result.", res == (baseAll + 4));
                    blf.setScope(ByLoginFilter.LOGED_IN);
                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 4 new users in result.", res == (baseNLogged + 4));

                    Map params = new java.util.HashMap();

                    user2.setLastLoginDate(new Timestamp(today));
                    le2 = LoginEvent.create(LoginEvent.TYPE, user2, params);
                    user3.setLastLoginDate(new Timestamp(today + 24L * 60L * 60L * 1000L));
                    user4.setLastLoginDate(new Timestamp(today + 96L * 60L * 60L * 1000L));

                    user2.save(conn);
                    user3.save(conn);
                    user4.save(conn);
                    conn.commit();

                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 1 new users in result. got: " + (res - baseNLogged), res == (baseNLogged + 1));
                    blf.setIsLogin(new Boolean(true));
                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 3 new users in result. got: " + (res - baseLogged), res == (baseLogged + 3));

                    le2.save(conn);
                    conn.commit();

                    blf.setStart(new Date(today - 24L * 60L * 60L * 1000L));
                    blf.setEnd(new Date(today + 48L * 60L * 60L * 1000L));

                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 1 new users in result. got: " + (res - baseLog1), res == (baseLog1 + 1));
                    blf.setIsLogin(new Boolean(false));
                    res = _peer.findUserIdsByLoginDate(blf, contextId, conn);
                    assertTrue("Expecting 3 new users in result. got: " + (res - baseLog2), res == (baseLog2 + 3));
                } finally {
                    delete(le2, conn);
                    conn.commit();
                    delete(user1, conn);
                    delete(user2, conn);
                    delete(user3, conn);
                    delete(user4, conn);
                    conn.commit();
                }
                return null;
            }
        }.execute();
    }

/*
    public void testFindIdsByTypeDatesAndUser()
            throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    try {
                        _peer.findIdsByTypeDatesAndUser(null, Work.TYPE, new Date(), new Date(), conn);
                        fail("Null User id should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByTypeDatesAndUser(FAKE_ID, null, new Date(), new Date(), conn);
                        fail("Null PSType should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByTypeDatesAndUser(FAKE_ID, Work.TYPE, null, new Date(), conn);
                        fail("Null start date should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByTypeDatesAndUser(FAKE_ID, Work.TYPE, new Date(), null, conn);
                        fail("Null end date should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }


                    final User user = Nobody.get(context);
                    final Work work1 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByTypeDatesAndUser_work1_", user);
                    final Work work2 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByTypeDatesAndUser_work2_", user);
                    final Work work3 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByTypeDatesAndUser_work3_", user);
                    try {
                        work1.setControlSchedule(Boolean.TRUE);
                        work2.setControlSchedule(Boolean.TRUE);
                        work3.setControlSchedule(Boolean.FALSE);
                    } catch (ScheduleSettingsException e) {
                        if (DEBUG) e.printStackTrace();
                    }

                    long base = System.currentTimeMillis();
                    work1.getConstraints().setStartDate(new Timestamp(base + 1000));
                    work1.getConstraints().setEndDate(new Timestamp(base + 24 * 60 * 60 * 1000 - 1000));
                    work2.getConstraints().setStartDate(new Timestamp(base - 1000));
                    work2.getConstraints().setEndDate(new Timestamp(base + 24 * 60 * 60 * 1000 + 1000));
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);

                    Set __ids = _peer.findIdsByTypeDatesAndUser(user.getId(), ProjectEvent.TYPE, new Date(base), new Date(base + 24 * 60 * 60 * 1000), conn);
                    assertTrue("Expecting empty ids set", __ids.isEmpty());
                    __ids = _peer.findIdsByTypeDatesAndUser(user.getId(), Work.TYPE, new Date(base), new Date(base + 24 * 60 * 60 * 1000), conn);
                    assertTrue("Expecting one id in resulting set; got " + __ids.size(), __ids.size() == 1);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }
*/
    public void testFindIdsByDueDatesAndUser()
            throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    try {
                        _peer.findIdsByDueDatesAndUser(null, Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                                Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                                new Date(), new Date(), conn);
                        fail("Null User id should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByDueDatesAndUser(FAKE_ID, null,
                                Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                                new Date(), new Date(), conn);
                        fail("Null excluded types should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByDueDatesAndUser(FAKE_ID, Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                                null, new Date(), new Date(), conn);
                        fail("Null excluded statuses should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByDueDatesAndUser(FAKE_ID, Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                                Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                                null, new Date(), conn);
                        fail("Null start date should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }
                    try {
                        _peer.findIdsByDueDatesAndUser(FAKE_ID, Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                                Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                                new Date(), null, conn);
                        fail("Null end date should throw IllegalArgumentException.");
                    } catch (IllegalArgumentException ok) {
                    }


                    Set res = _peer.findIdsByDueDatesAndUser(FAKE_ID, Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                            Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                            new Date(), new Date(), conn);
                    assertTrue("Expecting empty result for fake context.", res.isEmpty());

                    final User user = Nobody.get(context);
                    final Work work1 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByDueDatesAndUser_work1_", user);
                    final Work work2 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByDueDatesAndUser_work2_", user);
                    final Work work3 = Work.createNew(ProjectEvent.TYPE, "_TestUserPeer_findIdsByDueDatesAndUser_work3_", user);
                    final Work work4 = Work.createNew(Work.TYPE, "_TestUserPeer_findIdsByDueDatesAndUser_work4_", user);
                    work4.setStatus(WorkStatus.COMPLETED);

                    long base = System.currentTimeMillis();
                    work1.getSchedules().setPlannedEndDate(new Timestamp(base));
                    work2.getSchedules().setPlannedEndDate(new Timestamp(base));
                    work3.getSchedules().setPlannedEndDate(new Timestamp(base));
                    work4.getSchedules().setPlannedEndDate(new Timestamp(base));
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    work4.save(conn);

                    Set __ids = _peer.findIdsByDueDatesAndUser(user.getId(), Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                            Arrays.asList(new Object[]{WorkStatus.COMPLETED, WorkStatus.CANCELED}),
                            new Date(base - 1000), new Date(base + 1000), conn);
                    assertTrue("Expecting two id in resulting set; got " + __ids.size(), __ids.size() == 2);

                    __ids = _peer.findIdsByDueDatesAndUser(user.getId(), Arrays.asList(new Object[]{Work.TYPE}),
                            Arrays.asList(new Object[]{WorkStatus.CANCELED}),
                            new Date(base - 1000), new Date(base + 1000), conn);
                    assertTrue("Expecting one id in resulting set; got " + __ids.size(), __ids.size() == 1);

                    __ids = _peer.findIdsByDueDatesAndUser(user.getId(), Arrays.asList(new Object[]{ProjectEvent.TYPE}),
                            Arrays.asList(new Object[]{WorkStatus.CANCELED}),
                            new Date(base + 1000), new Date(base + 2000), conn);
                    assertTrue("Expecting empty ids set", __ids.isEmpty());

                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

    public void testFindMyWorkIds() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user1 = null;
                User user2 = null;
                Work root1 = null;
                Work root2 = null;
                Work child1_1 = null;
                Work child2_1_1 = null;
                Work child2_2_1_1 = null;
                Work child1_2_2_1_1 = null;
                Work child_r2 = null;
                Tollgate t1 = null;
                Tollgate t2 = null;
                PSTag process = null;
                TollPhase tp = null;
                try {
                    DEBUG_DELETE("_TestUserPeer_findMyWorkIds1_", context);
                    user1 = User.createNewUser("_TestUserPeer_findMyWorkIds1_", context);
                    user1.save(conn);
                    conn.commit();

                    Set ids = _peer.findMyWorkIds(user1.getId(), true, conn);
                    assertNotNull("Expecting empty ids set; got null", ids);
                    assertTrue("Expecting ids set only with entity folder id. got: " + ids.size(), ids.size() == 2);
                    assertTrue("Expecting ids set only with entity folder id", ids.contains(user1.getFolder().getId()) &&
                            ids.contains(HomeFolder.getHomeFolder(context).getId()));

                    DEBUG_DELETE("_TestUserPeer_findMyWorkIds2_", context);
                    user2 = User.createNewUser("_TestUserPeer_findMyWorkIds2_", context);
                    user2.save(conn);
                    conn.commit();

                    root1 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_root1_", user1);
                    root1.save(conn);
                    conn.commit();

                    root2 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_root2_", user1);
                    root2.save(conn);
                    conn.commit();

                    child1_1 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_child1_1_", user1);
                    child1_1.setParentWork(root1, user1);
                    child1_1.save(conn);
                    conn.commit();

                    child2_1_1 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_child2_1_1_", user2);
                    child2_1_1.setParentWork(child1_1, user2);
                    child2_1_1.save(conn);
                    conn.commit();

                    child2_2_1_1 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_child2_2_1_1_", user2);
                    child2_2_1_1.setParentWork(child2_1_1, user2);
                    child2_2_1_1.save(conn);
                    conn.commit();

                    child1_2_2_1_1 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_child1_2_2_1_1_", user1);
                    child1_2_2_1_1.setParentWork(child2_2_1_1, user1);
                    child1_2_2_1_1.save(conn);
                    conn.commit();

                    child_r2 = Work.createNew(Work.TYPE, "_TestUserPeer_findMyWorkIds_child_r2_", user1);
                    child_r2.setParentWork(root2, user1);
                    child_r2.save(conn);
                    conn.commit();

                    child_r2.addTeamMember(user2, user1);
                    child_r2.save(conn);
                    conn.commit();

                    // create own process definition
                    tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
                    process = tp.addTag("_TestUserPeer_findMyWorkIds_");
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

                    final PSSession sess = new MockPSSession(context, user1);
                    final MockServletRequest req = new MockServletRequest();
                    req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
                    req.setParameter(Work.NAME, "_TestUserPeer_findMyWorkIds1_");
                    req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
                    req.setAttribute(PSSession.class.getName(), sess);
                    req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

                    final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
                    try {
                        t1 = (Tollgate) handler.create(req);
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                        fail("Unexpected exception occured\n" + e.getMessage());
                    }
                    t1.addChampion(user2);
                    t1.save(conn);
                    conn.commit();

                    req.setParameter(Work.NAME, "_TestUserPeer_findMyWorkIds1_");
                    try {
                        t2 = (Tollgate) handler.create(req);
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                        fail("Unexpected exception occured\n" + e.getMessage());
                    }
                    t2.save(conn);
                    conn.commit();


                    List user1_works = new LinkedList();
                    user1_works.add(root1.getId());
                    user1_works.add(root2.getId());
                    user1_works.add(child1_1.getId());
                    user1_works.add(child2_1_1.getId());
                    user1_works.add(child2_2_1_1.getId());
                    user1_works.add(child1_2_2_1_1.getId());
                    user1_works.add(child_r2.getId());
                    user1_works.add(t1.getId());
                    user1_works.add(t2.getId());
                    user1_works.add(((Work) t1.getCheckpoints().get(0)).getId());
                    user1_works.add(((Work) t1.getCheckpoints().get(1)).getId());
                    user1_works.add(((Work) t2.getCheckpoints().get(0)).getId());
                    user1_works.add(((Work) t2.getCheckpoints().get(1)).getId());
                    user1_works.add(user1.getFolder().getId());
                    user1_works.add(HomeFolder.getHomeFolder(context).getId());

                    List user2_works = new LinkedList();
                    user2_works.add(root1.getId());
                    user2_works.add(root2.getId());
                    user2_works.add(child_r2.getId());
                    user2_works.add(child1_1.getId());
                    user2_works.add(child2_1_1.getId());
                    user2_works.add(child2_2_1_1.getId());
                    user2_works.add(t1.getId());
                    user2_works.add(user2.getFolder().getId());
                    user2_works.add(HomeFolder.getHomeFolder(context).getId());

                    ids = _peer.findMyWorkIds(user1.getId(), true, conn);
                    assertTrue("Expecting " + user1_works.size() + " ids in result set; got " + ids.size(), ids.size() == user1_works.size());
                    assertTrue("Expecting valid data for user1", ids.containsAll(user1_works));

                    ids = _peer.findMyWorkIds(user2.getId(), true, conn);
                    assertTrue("Expecting " + user2_works.size() + " ids in result set for user2; got " + ids.size(), ids.size() == user2_works.size());
                    assertTrue("Expecting valid data for user2", ids.containsAll(user2_works));
                } finally {
                    delete(child1_2_2_1_1, conn);
                    delete(child2_2_1_1, conn);
                    delete(child2_1_1, conn);
                    delete(child1_1, conn);
                    delete(child_r2, conn);
                    delete(t1, conn);
                    delete(t2, conn);

                    // remove the process definition
                    if (process != null) {
                        ArrayList al = new ArrayList(10);
                        for (Iterator i = process.getChildren().iterator(); i.hasNext();) {
                            com.cinteractive.ps3.tags.PSTag tag1 = (com.cinteractive.ps3.tags.PSTag) i.next();
                            al.add(tag1);
                        }
                        for (Iterator i = al.iterator(); i.hasNext();) {
                            com.cinteractive.ps3.tags.PSTag tag1 = (com.cinteractive.ps3.tags.PSTag) i.next();
                            tp.removeTag(tag1.getId());
                        }

                        tp.removeTag(process.getId());
                        tp.save(conn);
                        conn.commit();
                    }

                    /*if (process != null) {
                        process.getTagSet().getTags().delete(conn);
                        process.delete(conn);
                        conn.commit();
                    }
                    */
                    delete(root1, conn);
                    delete(root2, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindWorkloadIds() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {

                try {
                    _peer.findWorkloadIds(null, conn);
                    fail("Null user id id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null user id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.findWorkloadIds(FAKE_ID, null);
                    fail("Null connection should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }


                Set res = _peer.findWorkloadIds(FAKE_ID, conn);

                assertNotNull("expecting not null result set", res);
                assertTrue("expecting empty result set", res.isEmpty());

                DEBUG_DELETE("_findWorkloadIds_user1", context);
                DEBUG_DELETE("_findWorkloadIds_user2", context);
                User user1 = User.createNewUser("_findWorkloadIds_user1", context);
                User user2 = User.createNewUser("_findWorkloadIds_user2", context);

                Work work1 = Work.createNew(Work.TYPE, "_findWorkloadIds_wrk1", user1);
                Work work2 = Work.createNew(Work.TYPE, "_findWorkloadIds_wrk1", user2);
                work1.setUseResourcePlanning(Boolean.FALSE, user1);
                work2.setUseResourcePlanning(Boolean.FALSE, user2);

                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();

                    res = _peer.findWorkloadIds(user1.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting empty result set", res.isEmpty());

                    res = _peer.findWorkloadIds(user2.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting empty result set", res.isEmpty());

                    work1.save(conn);
                    work2.save(conn);
                    conn.commit();
                    work2.addTeamMember(user1, user2);
                    work2.save(conn);
                    conn.commit();

                    res = _peer.findWorkloadIds(user1.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting empty result set", res.isEmpty());

                    res = _peer.findWorkloadIds(user2.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting empty result set", res.isEmpty());

                    work1.setUseResourcePlanning(Boolean.TRUE, user1);
                    work2.setUseResourcePlanning(Boolean.TRUE, user2);
                    work1.save(conn);
                    work2.save(conn);
                    conn.commit();

                    res = _peer.findWorkloadIds(user1.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("expecting another items in result set",
                            res.contains(work1.getId()) &&
                            res.contains(work2.getId()));

                    res = _peer.findWorkloadIds(user2.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("expecting another items in result set", res.contains(work2.getId()));

                } finally {
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }
                return null;
            }
        }.execute();
    }

    // gags
    public void testInsert() {
    }

    public void testUpdate() {
    }

    public void testDelete() {
    }

    public void testGetUserPeer() {
    }

    public void testGetCreatedProjectIds() {/*equals getUserWorkIds*/
    }

    private void DEBUG_DELETE(String sEmailAddress, InstallationContext context) {
        if (!DEBUG)
            return;
        User userForDelete = User.getByEmailAddress(sEmailAddress, context);
        if (userForDelete != null) {
     //       userForDelete.setModifiedById(Nobody.get(getContext()).getId());
            userForDelete.deleteHard();
        }

    }
}
