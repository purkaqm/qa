/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.agents.signup;

import com.cinteractive.ps3.agents.AgentTask;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.Collection;
import java.util.Iterator;

import java.sql.Timestamp;
import java.sql.SQLException;
import java.sql.Connection;

public class TestSignupAgentSql extends TestJdbcPeer {
    private SignupAgentSql _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(SignupAgentSql.class.getName(), TestSignupAgentSql.class.getName());
    }

    private final static boolean DEBUG = true; // Debug mode

    public TestSignupAgentSql(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestSignupAgentSql("testFindContextOwnerId"));
        suite.addTest(new TestSignupAgentSql("testSearch"));

        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (SignupAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return SignupAgentSql.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null SignupAgentSql instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindContextOwnerId() throws Exception {
        // empty case
        {
            final PersistentKey id = (PersistentKey) new JdbcQuery(this) {
                protected Object query(Connection conn) throws SQLException {
                    return _peer.findContextOwnerId(FAKE_ID, conn);
                }
            }.execute();
            assertNull("Expecting null context owenr id for fake context id; got '" + id + "'.", id);
        }

        // maybe some data
        final PersistentKey contextId = getContext().getId();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.findContextOwnerId(contextId, conn);
            }
        }.execute();

        final InstallationContext context = getContext();
        // real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                try {
                    user = User.createNewUser("_TestSignupAgentSql_findContextOwner_", context);
                    user.save(conn);
                    conn.commit();

                    PersistentKey key = _peer.findContextOwnerId(contextId, conn);
                    assertNotNull("Expecting not null persistent key", key);
                } finally {
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    private final void delete(PSObject o, Connection conn) {
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

    public void testSearch() throws Exception {
        // empty case
        {
            final Collection info = (Collection) new JdbcQuery(this) {
                protected Object query(Connection conn) throws SQLException {
                    return _peer.search(FAKE_ID, FAKE_ID, conn);
                }
            }.execute();
            assertNotNull("Expecting empty collection for no results; got null.", info);
            assertTrue("Expecting empty collection for fake task and context ids.", info.isEmpty());
        }

        // real data
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                User user = null;
                try {
                    user = User.createNewUser("_TestSignupAgentSql_search_", context);
                    user.save(conn);
                    conn.commit();

                    Collection users = _peer.search(context.getId(), AgentTask.get(SignupAgent.SIGNUP, context).getId(), conn);
                    assertTrue("Expecting at least 1 user in result set", users.size() > 0);
                    assertNotNull("Expecting user id in collection", findRemiderInfo(users, user.getId()));

                    user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
                    user.save(conn);
                    conn.commit();

                    users = _peer.search(context.getId(), AgentTask.get(SignupAgent.SIGNUP, context).getId(), conn);
                    assertNull("Unexpected user id found in collection", findRemiderInfo(users, user.getId()));
                } finally {
                    delete(user, conn);
                }

                return null;
            }
        }.execute();
    }

    private final ReminderInfo findRemiderInfo(Collection infoList, PersistentKey userId) {
        for (Iterator i = infoList.iterator(); i.hasNext();) {
            final ReminderInfo info = (ReminderInfo) i.next();
            if (info.getUserId().equals(userId)) return info;
        }
        return null;
    }
}
