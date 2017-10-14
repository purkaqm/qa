package com.cinteractive.ps3.agents.system;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.agents.Agent;
import com.cinteractive.ps3.agents.AgentTask;
import com.cinteractive.ps3.agents.InstallationException;
import com.cinteractive.ps3.agents.TaskInfo;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.questions.Question;
import com.cinteractive.ps3.questions.handlers.WorkDelegationHandler;
import com.cinteractive.ps3.work.Work;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestQuestionReminderAgentSql extends TestJdbcPeer {
    private QuestionReminderAgentSql _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(QuestionReminderAgentSql.class.getName(), TestQuestionReminderAgentSql.class.getName());
    }

    protected TestQuestionReminderAgentSql(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestQuestionReminderAgentSql("testSearch"));

        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (QuestionReminderAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return QuestionReminderAgentSql.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null QuestionReminderAgentSql peer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    private static final boolean DEBUG = true; //show debug info

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
  //              if (o instanceof PSObject)
//                    ((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

	public void testSearch()
	throws Exception
	{
		final InstallationContext context = getContext();
		final String contextName = context.getName();
		new JdbcQuery(this)
		{
			private QuestionReminderAgent.UserInfo findUserInfo(List res, PersistentKey userId)
			{
				for (Iterator i = res.iterator(); i.hasNext();) {
					QuestionReminderAgent.UserInfo tmpUI = (QuestionReminderAgent.UserInfo) i.next();
					if (tmpUI.getUserId().equals(userId))
						return tmpUI;
				}
				return null;
			}

			private boolean myContains(List res, Question q1)
			{
				for (Iterator i = res.iterator(); i.hasNext();) {
					if (q1.getId().equals(((Question) i.next()).getId()))
						return true;
				}
				return false;
			}

			protected Object query(Connection conn)
			throws SQLException
			{
				try {
					_peer.search(null, FAKE_CONTEXT_NAME, conn);
					fail("Null task id parameter should throw IllegalArgumentException.");
				}
				catch (IllegalArgumentException ok) {}
				catch (Exception e) {
					fail("Null task id parameter should throw IllegalArgumentException.");
				}
				try {
					_peer.search(FAKE_ID, null, conn);
					fail("Null context id parameter should throw IllegalArgumentException.");
				}
				catch (IllegalArgumentException ok) {}
				catch (Exception e) {
					fail("Null context id parameter should throw IllegalArgumentException.");
				}
				try {
					_peer.search(FAKE_ID, FAKE_CONTEXT_NAME, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				}
				catch (IllegalArgumentException ok) {}
				catch (Exception e) {
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				List res = _peer.search(FAKE_ID, FAKE_CONTEXT_NAME, conn);
				assertNotNull("Expecting empty result list, not null.", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("testQuestRemindAgtSql_usr1", context);
				user1.setLastName("testQuestRemindAgtSql_usr1");
				user1.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
				User user2 = User.createNewUser("testQuestRemindAgtSql_usr2", context);
				user2.setLastName("testQuestRemindAgtSql_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testQuestRemindAgtSql_wrk1", user1);
				Group group1 = Group.createNew("testQuestRemindAgtSql_gr1", user2, context);

				Object[] args = new Object[] {work1, user1, group1, "no", "no", "no"};
				Question q1 = Question.createNew(user1, new WorkDelegationHandler(), args);
				String comments = "testQuestRemindAgtSql_comment";
				q1.setComments(comments);

				QuestionReminderAgent qra = null;
				AgentTask at = null;
				try {
					user1.save(conn);
					user2.save(conn);
					conn.commit();
					work1.save(conn);
					group1.save(conn);
					conn.commit();
					q1.save(conn);
					conn.commit();

					qra = new QuestionReminderAgent();
					try {
						qra.install(context);
					}
					catch (InstallationException ie) {
						System.out.println(ie.toString());
					}

					int freq1 = 80;
					TaskInfo ti = new TaskInfo();
					ti.setMagicCode(QuestionReminderAgent.REMIND);
					ti.setFrequencyInMinutes(new Integer(freq1));
					ti.setName("testQuestRemindAgtSql_ag1");
					Agent agent = Agent.getAgent(Agent.findIdByPSAgent(qra, context), context);
					at = agent.addTask(ti);
					at.setComments(comments);
					at.save(conn);
					conn.commit();

					res = _peer.search(at.getId(), contextName, conn);
					assertTrue("Expecting not empty result list.", !res.isEmpty());
					int resCount = res.size();
					QuestionReminderAgent.UserInfo tmpUI = findUserInfo(res, user1.getId());
					assertNotNull("Expection another items in result list.", tmpUI);
					assertTrue("Expection another test item content in result list.", myContains(tmpUI.listQuestions(), q1));

					at.addLogEntry(QuestionReminderAgent.REMIND_ACTION, comments, q1.getId());
					at.addLogEntry(QuestionReminderAgent.REMIND_USER_ACTION, comments, user1.getId());
					at.save(conn);
					conn.commit();

					res = _peer.search(at.getId(), contextName, conn);
					assertTrue("Expecting " + resCount + " items in result list. got: " + res.size(), res.size() == resCount);
					tmpUI = findUserInfo(res, user1.getId());
					assertNotNull("Expection another items in result list.", tmpUI);
					assertTrue("Expection another test item content in result list.", myContains(tmpUI.listQuestions(), q1));
				}
				finally {
					try {
						delete(at, conn);
						conn.commit();
						delete(q1, conn);
						conn.commit();
						delete(work1, conn);
						delete(group1, conn);
						conn.commit();
						delete(user1, conn);
						delete(user2, conn);
						conn.commit();
					}
					catch (SQLException ignored) {
						if (DEBUG)
							ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}
}