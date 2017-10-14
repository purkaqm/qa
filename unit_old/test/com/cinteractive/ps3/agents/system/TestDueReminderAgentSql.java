package com.cinteractive.ps3.agents.system;

/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.questions.Question;
import com.cinteractive.ps3.questions.handlers.DueDateHandler;
import com.cinteractive.ps3.work.Work;

public class TestDueReminderAgentSql extends TestJdbcPeer {

    private DueReminderAgentSql _peer;

    static {
        registerCase();
    }

    private static void registerCase() {
        TestSql.registerTestCase(DueReminderAgentSql.class.getName(), TestDueReminderAgentSql.class.getName());
    }

    public TestDueReminderAgentSql(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestDueReminderAgentSql("testDeleteOldQuestions"));
        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }


    public void setUp() throws Exception {
        super.setUp();

        _peer = (DueReminderAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return DueReminderAgentSql.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null DueReminderAgentSql peer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    private static final boolean DEBUG = true; //show debug info

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
                //if (o instanceof PSObject)
                  //  ((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

    public void testDeleteOldQuestions() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                final Set workIds = new java.util.HashSet(); //empty set
                workIds.add(FAKE_ID);

                try {
                    _peer.deleteOldQuestions(null, conn);
                    fail("Null work Ids set should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null work Ids set should throw IllegalArgumentException.");
                }
                try {
                    _peer.deleteOldQuestions(workIds, null);
                    fail("Null connection should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection should throw IllegalArgumentException.");
                }

                int res = _peer.deleteOldQuestions(workIds, conn);
                assertTrue("Expecting 0 deleted questions. got: " + res, res == 0);

                User user1 = User.createNewUser("testDueRmndrAg_user1", context);
                user1.setLastName("testDueRmndrAg_user1");
                Work work1 = Work.createNew(Work.TYPE, "testDueRmndrAg_work1", user1);
                Work work2 = Work.createNew(Work.TYPE, "testDueRmndrAg_work2", user1);
                Question q1 = Question.createNew(user1, new DueDateHandler(), new Object[]{work1});
                Question q2 = Question.createNew(user1, new DueDateHandler(), new Object[]{work2});
                Question q3 = Question.createNew(user1, new DueDateHandler(), new Object[]{work1});
                Question q4 = Question.createNew(user1, new DueDateHandler(), new Object[]{work2});
                Question q5 = Question.createNew(user1, new DueDateHandler(), new Object[]{work1});
                try {
                    user1.save(conn);
                    conn.commit();
                    work1.save(conn);
                    work2.save(conn);
                    conn.commit();
                    q1.save(conn);
                    conn.commit();

                    workIds.add(work1.getId());
                    workIds.add(work2.getId());

                    res = _peer.deleteOldQuestions(workIds, conn);
                    assertTrue("Expecting 1 deleted questions. got: " + res, res == 1);

                    q2.save(conn);
                    q3.save(conn);
                    conn.commit();
                    q3.putParameter("par_name", "par_value");

                    res = _peer.deleteOldQuestions(workIds, conn);
                    assertTrue("Expecting 2 deleted questions. got: " + res, res == 2);

                    q4.setResponseDate(new Timestamp(System.currentTimeMillis()));
                    q4.save(conn);
                    q5.save(conn);
                    conn.commit();

                    res = _peer.deleteOldQuestions(workIds, conn);
                    assertTrue("Expecting 1 deleted questions. got: " + res, res == 1);
                } finally {
                    try {
                        delete(q3, conn);
                        delete(q2, conn);
                        delete(q1, conn);
                        conn.commit();
                        delete(work1, conn);
                        delete(work2, conn);
                        conn.commit();
                        delete(user1, conn);
                        conn.commit();
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

}