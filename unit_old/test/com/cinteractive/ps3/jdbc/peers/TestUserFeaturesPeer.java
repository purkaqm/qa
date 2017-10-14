package com.cinteractive.ps3.jdbc.peers;

/**
 * Title:        PS3
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      SoftDev
 * @author
 * @version 1.0
 */

import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestUserFeaturesPeer extends TestJdbcPeer {


    private static final boolean DEBUG = true;
    private UserFeaturesPeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(UserFeaturesPeer.class.getName(), TestUserFeaturesPeer.class.getName());
    }

    public TestUserFeaturesPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest(new TestUserFeaturesPeer(testName));
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestUserFeaturesPeer");

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void testClearLog() throws Exception {/** see testInsertDelete */
    }

    public void testLogHits() throws Exception {/** see testInsertDelete */
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (UserFeaturesPeer) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return UserFeaturesPeer.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null UserFeaturesPeer peer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    private void delete(PSObject o, Connection conn) {
        try {
            if (o != null) {
                //o.setModifiedById(User.getNobody(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

    public void testDelete() throws Exception {
    };
    public void testInsert() throws Exception {
    };
    public void testUpdate() throws Exception {
    };


    //gag
    public void testGetUserFeaturesPeer() {
    }

    public static void DEBUG_DELETE_USER(String sEmailAddress, InstallationContext context) {
        if (!DEBUG)
            return;
        try {
            User userForDelete = User.getByEmailAddress(sEmailAddress, context);
            if (userForDelete != null)
                userForDelete.deleteHard();
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