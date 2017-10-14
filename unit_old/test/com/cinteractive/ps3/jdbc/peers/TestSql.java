package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.PeerRegistry;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.agents.signup.TestSignupAgentSql;
import com.cinteractive.ps3.agents.system.*;
import com.cinteractive.ps3.metrics.TestMetricReminderAgentSql;
import junit.framework.*;

import java.util.*;

public class TestSql extends TestCase {

	public TestSql(String name)	{
		super(name);
	}

	private static Hashtable g_tests = new Hashtable();

	/**
	 * Contains table: Peer class name --> test case class name
	 * It is supposed that exists one and only one test case for one peer class
	 */
	private static Hashtable m_testingClasses = new Hashtable();

	private static void addTest(TestSuite suite, Class cl) throws Exception {		if (g_tests.containsKey(cl.getName())) throw new IllegalArgumentException("Such test already exists: " + cl.getName());
		suite.addTest((Test) (cl.getDeclaredMethod("bareSuite", new Class[]{})).invoke(null, new Object[]{}));
		g_tests.put(cl.getName(), cl);
	}


	public static Test suite() throws Exception
	{
		final TestSuite suite = new TestSuite();
		addTest(suite, TestVersionPeer.class);
		addTest(suite, TestPSObjectBasePeer.class);
		addTest(suite, TestObjectAttributePeer.class);
		addTest(suite, TestEntityPeer.class);
		addTest(suite, TestUserPeer.class);
		addTest(suite, TestWorkPeer.class);
		addTest(suite, TestMetricTemplatePeer.class);
		addTest(suite, TestMetricReminderAgentSql.class);
		addTest(suite, TestMetricInstancePeer.class);
		addTest(suite, TestMetricTemplateItemPeer.class);
		addTest(suite, TestMetricTemplateItemAttributePeer.class);
		addTest(suite, TestMeasurementsPeer.class);
		addTest(suite, TestDocumentPeer.class);
		addTest(suite, TestDocumentVersionPeer.class);
		addTest(suite, TestDocumentFolderPeer.class);
		addTest(suite, TestDocumentRelationshipPeer.class);
		addTest(suite, TestDiscussionItemPeer.class);
		addTest(suite, TestObjectEventPeer.class);
		addTest(suite, TestPersonAlertPeer.class);
		addTest(suite, TestQuestionPeer.class);
		addTest(suite, TestScheduleProblemPeer.class);
		addTest(suite, TestPSTransactionPeer.class);
		addTest(suite, TestRelationshipPeer.class);
		addTest(suite, TestAgentPeer.class);
		addTest(suite, TestAgentTaskPeer.class);
		addTest(suite, TestArchivalAgentSql.class);
		addTest(suite, TestDueReminderAgentSql.class);
		addTest(suite, TestQuestionReminderAgentSql.class);
		addTest(suite, TestSignupAgentSql.class);
		addTest(suite, TestTagPeer.class);
		addTest(suite, TestTagAttributePeer.class);
		addTest(suite, TestTagSetAttributePeer.class);
		addTest(suite, TestTagLinkPeer.class);
		addTest(suite, TestTagLinkableTypePeer.class);
		addTest(suite, TestTimeProjectPeer.class);
		addTest(suite, TestUsageReportAgentSql.class);
		addTest(suite, TestReportSql.class);
		//addTest(suite, TestInternalResourcePeer.class);
		addTest(suite, TestDiscussionItemLinkPeer.class);
		addTest(suite, TestUserFeaturesPeer.class);
		addTest(suite, TestCasesPresence.class);
		addTest(suite, TestSearchSqlPSObject.class);
		addTest(suite, TestSearchSqlDiscussionItem.class);

		return TestJdbcPeer.setUpDb(suite);
	}

	/**
	 * Method registers test case existing for the specified peer class
	 * @param peerClassName Fully-qualified class name of peer, NOT NULL
	 * @param testCaseClassName Fully-qualified class name of peer, NOT NULL
	 */
	public static synchronized void registerTestCase(String peerClassName, String testCaseClassName) {
		if (peerClassName == null || testCaseClassName == null)
			return;
		if (!m_testingClasses.containsKey(peerClassName))
			m_testingClasses.put(peerClassName, testCaseClassName);
	}

	/**
	 * Returns map of all registered unit tests.
	 * @return map of all registered unit tests.
	 */
	public static Map getRegisteredTests() {
		return m_testingClasses;
	}

	/**
	 * Returns list of registered peers without registered test cases
	 * @see #getPeersWithNoTest(String)
	 */
	public static List getPeersWithNoTest()	{
		return getPeersWithNoTest(null);
	}
	/**
	 * Returns list of registered peers without registered test cases in the specified package.
	 * Package must be specified with the help of fully-qualified name beginning.
	 * @param packagePrefix
	 * @see #getPeersWithNoTest()
	 */
	public static List getPeersWithNoTest(String packagePrefix)	{
		List res = new java.util.ArrayList();
		String pref = packagePrefix;
		if (pref == null)
			pref = "";
		for (Iterator i =
				PeerRegistry.get(StringDataSourceId.get(StringDataSourceId.MSSQL7)).getPeersNames(pref).iterator();
			 i.hasNext();)
		{
			String peerName = (String) i.next();
			if (!m_testingClasses.containsKey(peerName))
			{
				res.add(peerName);
			}
		}
		return res;
	}

}

