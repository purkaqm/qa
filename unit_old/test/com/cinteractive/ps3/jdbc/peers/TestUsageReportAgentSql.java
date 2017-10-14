package com.cinteractive.ps3.jdbc.peers;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;


public class TestUsageReportAgentSql extends TestJdbcPeer {
    private final static boolean DEBUG = true;
    private UsageReportAgentSql _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(UsageReportAgentSql.class.getName(), TestUsageReportAgentSql.class.getName());
    }

    public TestUsageReportAgentSql(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest(new TestUsageReportAgentSql(testName));
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        addTest(suite, "testCountLogins");
        addTest(suite, "testCountUsers");

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (UsageReportAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return UsageReportAgentSql.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null UsageReportAgentSql instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    private void pause(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ignored) {
        }
    }

    public void testCountLogins() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                //epmty case
                int count = _peer.countLogins(FAKE_ID, Calendar.getInstance().getTime(), conn);
                assertTrue("Expecting 0 value for fake context id", count == 0);
                //some data
                _peer.countLogins(context.getId(), Calendar.getInstance().getTime(), conn);

                //real data
                User user = null;
                try {
                    Calendar c = Calendar.getInstance();
                    c.add(Calendar.YEAR, -50);
                    final Date beginDate = c.getTime();
                    final int startCount = _peer.countLogins(context.getId(), beginDate, conn);

                    c = Calendar.getInstance();
                    pause(2);
                    user = DEBUG_CREATE_NEW_USER("_TestUsageReportAgentSql_countLogins_", context);
                    user.save(conn);
                    conn.commit();

                    count = _peer.countLogins(context.getId(), beginDate, conn);
                    assertTrue("Expecting " + startCount + " logins; got " + count, count == startCount);

                    user.setLastLoginDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    user.save(conn);
                    conn.commit();

                    count = _peer.countLogins(context.getId(), beginDate, conn);
                    assertTrue("Expecting " + (startCount + 1) + " logins; got " + count, count == startCount + 1);

//                    user.delete(conn);
//                    conn.commit();
                    delete(user, conn);
                    user = null;

                    count = _peer.countLogins(context.getId(), beginDate, conn);
                    assertTrue("Expecting " + startCount + " logins; got " + count, count == startCount);
                } finally {
                    delete(user, conn);
                }

                return null;
            }
        }.execute();
    }

    public void testCountUsers() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                //epmty case
                int count = _peer.countUsers(FAKE_ID, conn);
                assertTrue("Expecting 0 users for fake context id", count == 0);
                //some data
                _peer.countUsers(context.getId(), conn);

                //real data
                User user = null;
                try {
                    final int startCount = _peer.countUsers(context.getId(), conn);

                    user = DEBUG_CREATE_NEW_USER("_TestUsageReportAgentSql_countLogins_", context);
                    user.save(conn);
                    conn.commit();

                    count = _peer.countUsers(context.getId(), conn);
                    assertTrue("Expecting " + (startCount + 1) + " users; got " + count, count == startCount + 1);

//                    user.delete(conn);
//                    conn.commit();
                    delete(user, conn);
                    user = null;

                    count = _peer.countUsers(context.getId(), conn);
                    assertTrue("Expecting " + startCount + " users; got " + count, count == startCount);
                } finally {
                    delete(user, conn);
                }

                return null;
            }
        }.execute();
    }

    private void delete(JdbcPersistableBase o, Connection conn) {
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

    private void DEBUG_DELETE_USER(String sEmailAddress, InstallationContext context) {
        if (!DEBUG)
            return;
        try {
            User userForDelete = User.getByEmailAddress(sEmailAddress, context);
            if (userForDelete != null) {
                //userForDelete.setModifiedById(Nobody.get(getContext()).getId());
                userForDelete.deleteHard();
            }
        } catch (RuntimeSQLException e) {
            e.printStackTrace();
            throw e;
        }


    }

    private User DEBUG_CREATE_NEW_USER(String sEmailAddress, InstallationContext context) {
        DEBUG_DELETE_USER(sEmailAddress, context);
        return User.createNewUser(sEmailAddress, context);
    }

}