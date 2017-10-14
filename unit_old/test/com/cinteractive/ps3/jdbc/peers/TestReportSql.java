package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.directedgraph.TopologyException;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.ElectronicSignatureException;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.entities.LoginException;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.CommentEvent;
import com.cinteractive.ps3.events.CreationEvent;
import com.cinteractive.ps3.events.EventType;
import com.cinteractive.ps3.events.ObjectEvent;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.metrics.MetricTemplateItem;
import com.cinteractive.ps3.metrics.StaticItem;
import com.cinteractive.ps3.mimehandler.HandlerRegistry;
import com.cinteractive.ps3.mimehandler.PSMimeHandler;
import com.cinteractive.ps3.relationships.Relationship;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.reports.Report;
import com.cinteractive.ps3.reports.filters.AllocationFilter;
import com.cinteractive.ps3.reports.filters.AlphabetFilter;
import com.cinteractive.ps3.reports.filters.AopReportFilter;
import com.cinteractive.ps3.reports.filters.ByLoginFilter;
import com.cinteractive.ps3.reports.filters.CreateDatePeriodFilter;
import com.cinteractive.ps3.reports.filters.EventPeriodFilter;
import com.cinteractive.ps3.reports.filters.MetricTemplateFilter;
import com.cinteractive.ps3.reports.filters.MilestoneStatusFilter;
import com.cinteractive.ps3.reports.filters.MyIssuesFilter;
import com.cinteractive.ps3.reports.filters.MyWorkFilter;
import com.cinteractive.ps3.reports.filters.ObjectTypeFilter;
import com.cinteractive.ps3.reports.filters.OwnerRootWorkFilter;
import com.cinteractive.ps3.reports.filters.ProcessFilter;
import com.cinteractive.ps3.reports.filters.RootWorkFilter;
import com.cinteractive.ps3.reports.filters.ShowArchivedFilter;
import com.cinteractive.ps3.reports.filters.StatusFilter;
import com.cinteractive.ps3.reports.filters.TagsFilter;
import com.cinteractive.ps3.reports.filters.TagsFilterForIssues;
import com.cinteractive.ps3.reports.filters.TollPhaseFilter;
import com.cinteractive.ps3.reports.filters.TollgateWaitingForApprovalFilter;
import com.cinteractive.ps3.reports.filters.TrackResourceFilter;
import com.cinteractive.ps3.reports.impl.DiscussionItemReport;
import com.cinteractive.ps3.reports.impl.EventReport;
import com.cinteractive.ps3.reports.impl.MetricAdhocReport;
import com.cinteractive.ps3.reports.impl.MetricFinancialReport;
import com.cinteractive.ps3.reports.impl.MetricReport;
import com.cinteractive.ps3.reports.impl.PSObjectReport;
import com.cinteractive.ps3.reports.impl.SqlSearchResult;
import com.cinteractive.ps3.reports.impl.SummaryReport;
import com.cinteractive.ps3.reports.impl.UserAllocationReport;
import com.cinteractive.ps3.reports.impl.UserByLoginReport;
import com.cinteractive.ps3.reports.impl.UserWorksReport;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tagext.uix.Uix;
import com.cinteractive.ps3.tags.InvalidTagEventException;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagEventCode;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.test.MockPSSession;
import com.cinteractive.ps3.test.MockServletRequest;
import com.cinteractive.ps3.tollgate.Checkpoint;
import com.cinteractive.ps3.tollgate.TollPhase;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.types.ObjectType;
import com.cinteractive.ps3.work.FileFolder;
import com.cinteractive.ps3.work.IllegalDependencyException;
import com.cinteractive.ps3.work.Milestone;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil;

public class TestReportSql extends TestJdbcPeer {
	private ReportSql _peer;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(ReportSql.class.getName(), TestReportSql.class.getName());
	}

	private PSSession FAKE_SESSION;
	private static final boolean DEBUG = true; // show debug information

	private EventType FAKE_EVENT_TYPE = new EventType("FAKE_EVENT_TYPE")
	{
		protected ObjectEvent instantiate()
		{
			throw new UnsupportedOperationException("Not implemented.");
		}
	};

	public TestReportSql(String name) {
		super(name);
	}

	private static void addTest(TestSuite suite, String testName) {
		suite.addTest(new TestReportSql(testName));
	}

	public static Test bareSuite() {
		final TestSuite suite = new TestSuite("TestReportSql");
		addTest(suite, "testFindDiscussionItems");
		addTest(suite, "testFindEvents");

		addTest(suite, "testFindUserAlphabet");
		addTest(suite, "testFindUserWorkIds");
		addTest(suite, "testFindWorkIds");
		addTest(suite, "testFindFinancialMetricIds");
		addTest(suite, "testFindWorkByFinancialMetricIds");

		//filters tests
		addTest(suite, "testAllocationFilter");
		addTest(suite, "testTollPhaseFilter");
		addTest(suite, "testAlphabetFilter");
		addTest(suite, "testCreateDatePeriodFilter");
		addTest(suite, "testEventPeriodFilter");
		addTest(suite, "testObjectTypeFilter");

		addTest(suite, "testMilestoneStatusFilter");
		addTest(suite, "testMyWorkFilter");
		addTest(suite, "testMyIssuesFilter");

		addTest(suite, "testShowArchivedFilter");

		addTest(suite, "testStatusFilter");
		addTest(suite, "testTagsFilter");
		addTest(suite, "testTagsFilterForIssues");
		addTest(suite, "testTollgateWaitingForApprovalFilter");
		addTest(suite, "testTrackResourceFilter");
		addTest(suite, "testOwnerRootWorkFilter");

		addTest(suite, "testMetricTemplateFilter");
		addTest(suite, "testByLoginFilter");
		addTest(suite, "testAopReportFilter");
		addTest(suite, "testFindUserAlphabetLogin");
		addTest(suite, "testFindInstancesForAop");
		addTest(suite, "testGetMetricTypes");
		addTest(suite, "testFindTollgateWithEvents");

		return suite;
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}

	public void setUp() throws Exception {
		super.setUp();

		_peer = (ReportSql) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				return ReportSql.getInstance(conn);
			}
		}.execute();

		if (_peer == null)
			throw new NullPointerException("Null ReportSql peer instance.");

		FAKE_SESSION = new MockPSSession(getContext(), Nobody.get(getContext()));
	}

	public void tearDown() throws Exception {
		super.tearDown();
		_peer = null;
	}

	public void testFindDiscussionItems() throws Exception {
		final InstallationContext context = getContext();
		final User user = Nobody.get(context);
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final DiscussionItemReport rep = new DiscussionItemReport(new MockPSSession(context, user));
				SqlSearchResult result = null;
                    result = _peer.findDiscussionItems(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				DiscussionItem item = null;
				DiscussionItem item1 = null;
				Work work = null;
				try {
					final Work noproject = WorkUtil.getNoProject(context);
					final User nobody = Nobody.get(context);
					work = Work.createNew(Work.TYPE, "_TestReportSQL_findDiscussions_", nobody);
					item = DiscussionItem.createNew(work, null, nobody, "_dicussion title_", "text");
					work.save(conn);
					conn.commit();
					item.save(conn);
					conn.commit();

					// create second discussion item
					item1 = DiscussionItem.createNew(noproject, null, nobody, "no title", "text");
					item1.save(conn);
					conn.commit();

					DiscussionItemReport rep = new DiscussionItemReport(new MockPSSession(context, nobody));
					SqlSearchResult res = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting discussion items in resulting set", res.getResults().contains(item) && res.getResults().contains(item1));

					// testing with filter
					RootWorkFilter f = new RootWorkFilter(true);
					f.setRootWork(work);
					rep.addFilter(f);
					res = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting discussion item in resulting set", res.getResults().contains(item));
					assertTrue("Unexpected discussion item found", !res.getResults().contains(item1));

					f.setRootWork(noproject);
					rep = new DiscussionItemReport(new MockPSSession(context, nobody));
					rep.addFilter(f);
					res = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting discussion item in resulting set", res.getResults().contains(item1));
					assertTrue("Unexpected discussion item found", !res.getResults().contains(item));
                } finally {
					delete(item, conn);
					delete(work, conn);
					delete(item1, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindEvents() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final EventReport rep = new EventReport(FAKE_SESSION, FAKE_EVENT_TYPE);
                SqlSearchResult result = null;
                    result = _peer.findEvents(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
		assertTrue("Expecting empty result set for fake event type", sr.getResults().isEmpty());

		// real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				CreationEvent ev = null;
				CommentEvent cev = null;
				Work work = null;
				try {
					final User nobody = Nobody.get(context);
					work = Work.createNew(Work.TYPE, "_TestReportSQL_Events_", nobody);
					work.save(conn);
					conn.commit();
					ev = (CreationEvent) CreationEvent.create(CreationEvent.TYPE, work, null);
					ev.save(conn);
					conn.commit();
					final EventReport rep = new EventReport(FAKE_SESSION, CreationEvent.TYPE);
					SqlSearchResult res = _peer.findEvents(rep, conn);
					assertTrue("Expecting at least 1 event id; got " + res.getResults().size(), res.getResults().size() > 0);
					boolean yep = false;
					for (Iterator i = res.getResults().iterator(); !yep && i.hasNext(); yep = ((ObjectEvent) i.next()).getId().equals(ev.getId())) ;
					assertTrue("Expecting event id", yep);

					Map params = new TreeMap();
					params.put(CommentEvent.COMMENT_CODE, "no comments");
					cev = (CommentEvent) CommentEvent.create(CommentEvent.TYPE, work, params);
					cev.save(conn);
					conn.commit();
					res = _peer.findEvents(rep, conn);
					assertTrue("Expecting at least 1 event id; got " + res.getResults().size(), res.getResults().size() > 0);
					yep = false;
					for (Iterator i = res.getResults().iterator(); !yep && i.hasNext(); yep = ((ObjectEvent) i.next()).getId().equals(ev.getId())) ;
					assertTrue("Expecting event id", yep);
					yep = false;
					for (Iterator i = res.getResults().iterator(); !yep && i.hasNext(); yep = ((ObjectEvent) i.next()).getId().equals(cev.getId())) ;
					assertTrue("Unexpected event id found", !yep);
                } finally {
					delete(ev, conn);
					delete(cev, conn);
					delete(work, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindUserAlphabet() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				return _peer.findUserAlphabet(rep, conn, true);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, false);
				return _peer.findUserAlphabet(rep, conn, false);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		// real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user1 = null;
				User user2 = null;
				User user3 = null;
				Work work1 = null;
				Work work2 = null;
				try {
					user1 = User.createNewUser("_TestReportSQL_findUserA12_", context);
					user1.setLastName("Petrov");
					user1.save(conn);
					conn.commit();

					work1 = Work.createNew(Work.TYPE, "_TestReportSql_findUserAlphabet_work1_", user1);
					work1.save(conn);
					conn.commit();

					user2 = User.createNewUser("_TestReportSQL_findUserA3_", context);
					user2.setLastName("Ivanov");
					user2.save(conn);
					conn.commit();

					work1.addTeamMember(user2, user1);
					work1.getRelationships().getRelationship(work1.getId(), user2.getId(), RelationshipType.TEAM_MEMBER).setIsConfirmed(Boolean.TRUE);
					work1.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
					RootWorkFilter f = new RootWorkFilter(true);
					f.setRootWork(work1);
					rep.addFilter(f);

					SqlSearchResult res = _peer.findUserAlphabet(rep, conn, true);
					Collection ab = res.getResults();
					assertTrue("Expecting 1 letter in resulting set; got " + ab.size(), ab.size() == 1);
					assertTrue("Expecting 'P' in resulting set", ab.contains("P"));

					res = _peer.findUserAlphabet(rep, conn, false);
					ab = res.getResults();
					assertTrue("Expecting 2 letters in resulting set; got " + ab.size(), ab.size() == 2);
					assertTrue("Expecting 'P' and 'I' in resulting set", ab.contains("P") && ab.contains("I"));

					work2 = Work.createNew(Work.TYPE, "_TestReportSql_finUserAlphabet_work2_", user2);
					work2.setParentWork(work1, user2);
					work2.save(conn);
					conn.commit();

					res = _peer.findUserAlphabet(rep, conn, true);
					ab = res.getResults();
					assertTrue("Expecting 2 letters in resulting set; got " + ab.size(), ab.size() == 2);
					assertTrue("Expecting 'P' and 'I' in resulting set", ab.contains("P") && ab.contains("I"));

					f.setRootWork(work2);
					res = _peer.findUserAlphabet(rep, conn, true);
					ab = res.getResults();
					assertTrue("Expecting 1 letter in resulting set; got " + ab.size(), ab.size() == 1);
					assertTrue("Expecting 'I' in resulting set", ab.contains("I"));

					user3 = User.createNewUser("_TestReportSql_findAlphabet2_", context);
					user3.setLastName("Sidorov");
					user3.save(conn);
					conn.commit();

					work2.addTeamMember(user3, user2);
					work2.getRelationships().getRelationship(work2.getId(), user3.getId(), RelationshipType.TEAM_MEMBER).setIsConfirmed(Boolean.TRUE);
					work2.save(conn);
					conn.commit();

					res = _peer.findUserAlphabet(rep, conn, true);
					ab = res.getResults();
					assertTrue("Expecting 1 letter in resulting set; got " + ab.size(), ab.size() == 1);
					assertTrue("Expecting 'I' in resulting set", ab.contains("I"));

					f.setRootWork(work1);
					res = _peer.findUserAlphabet(rep, conn, false);
					ab = res.getResults();
					assertTrue("Expecting 3 letters in resulting set; got " + ab.size(), ab.size() == 3);
					assertTrue("Expecting 'P' and 'I' and 'S' in resulting set", ab.contains("P") && ab.contains("I") && ab.contains("S"));
				} finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(user3, conn);
					delete(user2, conn);
					delete(user1, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindUserWorkIds() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				SqlSearchResult result = null;
                    result = _peer.findUserWorkIds(rep, conn, true);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, false);
                SqlSearchResult result = null;
                    result = _peer.findUserWorkIds(rep, conn, false);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		// real data testing
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, false);
				User user = null;
				Work work1 = null;
				Work work2 = null;
				try {
					final User nobody = Nobody.get(context);
					user = User.createNewUser("_TestReportSQL_UserWorks_", context);
					user.save(conn);
					conn.commit();
//					SqlSearchResult res = _peer.findUserWorkIds(rep, conn, true);
//					Map map = (Map) res.getResults().toArray()[0];
//					assertTrue("Unexpected work list for user who doesn't has any work.", !map.containsKey(user.getId()));
//					res = _peer.findUserWorkIds(rep, conn, false);
//					map = (Map) res.getResults().toArray()[0];
//					assertTrue("Unexpected work list for user who doesn't has any work.", !map.containsKey(user.getId()));
					work1 = Work.createNew(Work.TYPE, "_TestReportSql_UserWorks_work1_", user);
					work1.save(conn);
					conn.commit();
					SqlSearchResult res = _peer.findUserWorkIds(rep, conn, true);
					Map map = (Map) res.getResults().toArray()[0];
					List works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting one work in resulting list; got " + works.size(), works.size() == 2);
					res = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) res.getResults().toArray()[0];
					works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting one work in resulting list; got " + works.size(), works.size() == 2);

					// two works. user is team member of second work
					work2 = Work.createNew(Work.TYPE, "_TestReportSql_UserWorks_work2_", nobody);
					work2.save(conn);
					conn.commit();
					work2.addTeamMember(user, nobody);
					work2.save(conn);
					conn.commit();

					res = _peer.findUserWorkIds(rep, conn, true);
					map = (Map) res.getResults().toArray()[0];
					works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting 2 works in resulting list; got " + works.size(), works.size() == 2);
					assertTrue("Expecting valid data", works.contains(work1.getId()) && works.contains(user.getFolder().getId()));
					res = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) res.getResults().toArray()[0];
					works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting 2 works in resulting list; got " + works.size(), works.size() == 2);
					assertTrue("Expecting valid data", works.contains(work1.getId()) && works.contains(user.getFolder().getId()));

					Relationship r = work2.getRelationships().getRelationship(work2.getId(), user.getId(), RelationshipType.TEAM_MEMBER);
					assertNotNull("Expecting not null relation ship; got null", r);
					r.setIsConfirmed(Boolean.TRUE);
					work2.save(conn);
					conn.commit();
					res = _peer.findUserWorkIds(rep, conn, true);
					map = (Map) res.getResults().toArray()[0];
					works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting 1 work in resulting list; got " + works.size(), works.size() == 2);
					assertTrue("Expecting valid data", works.contains(work1.getId()) && works.contains(user.getFolder().getId()));
					res = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) res.getResults().toArray()[0];
					works = (List) map.get(user.getId());
					assertNotNull("Expecting work list; got null", works);
					assertTrue("Expecting 2 works in resulting list; got " + works.size(), works.size() == 3);
					assertTrue("Expecting valid data", works.contains(work1.getId()) && works.contains(work2.getId())
                        && works.contains(user.getFolder().getId()));
                } finally {
					delete(work1, conn);
					delete(work2, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	private void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
               // if (o instanceof PSObject)
               //     ((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}


	public void testFindWorkIds() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final PSObjectReport rep = new PSObjectReport(FAKE_SESSION, true);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();

		assertNotNull("Expecting search result; got null", sr);
		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final PSObjectReport rep = new PSObjectReport(FAKE_SESSION, false);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				try {
					final PSObjectReport rep = new PSObjectReport(FAKE_SESSION, false);
					final User nobody = Nobody.get(context);
					work1 = Work.createNew(Work.TYPE, "_TestReportSQL_findWorkIds_work1_", nobody);
					work1.save(conn);
					conn.commit();

					work2 = Work.createNew(FileFolder.TYPE, "_TestReportSql_findWorkIds_folder_", nobody);
					work2.save(conn);
					conn.commit();
					SqlSearchResult res = _peer.findWorkIds(rep, conn);
					Collection ids = res.getResults();
					assertTrue("Expecting at least 2 work ids; got " + ids.size(), ids.size() > 1);
					assertTrue("Expecting work ids", ids.contains(work1.getId()) && ids.contains(work1.getId()));

					ObjectTypeFilter f = new ObjectTypeFilter(true, Work.TYPE);
					rep.addFilter(f);
					res = _peer.findWorkIds(rep, conn);
					ids = res.getResults();
					assertTrue("Expecting at least 1 work id; got " + ids.size(), ids.size() > 0);
					assertTrue("Expecting work ids", ids.contains(work1.getId()));
					assertTrue("Unexpected work id found", !ids.contains(work2.getId()));
                } finally {
					delete(work1, conn);
					delete(work2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testAllocationFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				rep.addFilter(new AllocationFilter(true));
				SqlSearchResult result = null;
                    _peer.findUserWorkIds(rep, conn, false);
                    result = _peer.findUserWorkIds(rep, conn, true);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
	}

	public void testAlphabetFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				rep.addFilter(new AlphabetFilter());
				return _peer.findUserAlphabet(rep, conn, true);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				AlphabetFilter f = new AlphabetFilter();
				f.setLetter("A");
				rep.addFilter(f);
				return _peer.findUserAlphabet(rep, conn, true);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User userA = null;
				User userB = null;
				Work work1 = null;
				Work work2 = null;
				Work work3 = null;
				try {
					userA = User.createNewUser("A_TestReportSQL_alphabetFilter_", context);
					userA.setLastName("Aabbcc");
					userA.save(conn);
					conn.commit();

					userB = User.createNewUser("B_TestReportSQL_alphabetFilter_", context);
					userB.setLastName("Bccdd");
					userB.save(conn);
					conn.commit();

					work1 = createWork("_testReportSQL_alphabetFilter1_", userA, conn);
					conn.commit();

					work2 = createWork("_testReportSQL_alphabetFilter2_", userA);
					work2.setParentWork(work1, userA);
					work2.save(conn);
					conn.commit();

					work3 = createWork("_testReportSQL_alphabetFilter3_", userB);
					work3.setParentWork(work1, userB);
					work3.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, userA), true);
					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work1);
					rep.addFilter(rf);

					SqlSearchResult sr = _peer.findUserWorkIds(rep, conn, false);
					Map map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 2 user ids in result set; got " + map.size(), map.size() == 2);
					assertTrue("Expecting valid data",
							((List) map.get(userA.getId())).contains(work1.getId()) &&
							((List) map.get(userA.getId())).contains(work2.getId()) &&
							((List) map.get(userB.getId())).contains(work3.getId()) &&
							((List) map.get(userA.getId())).size() == 2 &&
							((List) map.get(userB.getId())).size() == 1
					);

					AlphabetFilter f = new AlphabetFilter();
					f.setLetter("B");
					rep.addFilter(f);
					sr = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 1 id in result set; got " + map.size(), map.size() == 1);
					assertTrue("Expecting valid data",
							((List) map.get(userB.getId())).contains(work3.getId()) &&
							((List) map.get(userB.getId())).size() == 1
					);

					f.setLetter("A");
					rep.addFilter(f);
					sr = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 1 id in result set; got " + map.size(), map.size() == 1);
					assertTrue("Expecting valid data",
							((List) map.get(userA.getId())).contains(work1.getId()) &&
							((List) map.get(userA.getId())).contains(work2.getId()) &&
							((List) map.get(userA.getId())).size() == 2
					);
                } finally {
					delete(work3, conn);
					delete(work2, conn);
					delete(work1, conn);
					delete(userA, conn);
					delete(userB, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testTollPhaseFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final TollPhaseFilter f = new TollPhaseFilter(true,false);
				f.addPhaseId( FAKE_ID, FAKE_ID);
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
		assertTrue("Expecting empty result st for fake phase id", sr.getResults().isEmpty());

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Tollgate tg = null;
				Tollgate tg1 = null;
				User user = null;
				User champ = null;
				PSTag process = null;
				Work root = null;
				PSTag p1 = null;
				PSTag p2 = null;
				try {
					user = User.createNewUser("_TestReportSql_TollphaseFilter_1", context);
					user.save(conn);
					conn.commit();

					champ = User.createNewUser("_TestReportSql_TollphaseFilter_champ_1", context);
					champ.save(conn);
					conn.commit();

					// create own process definition
					final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
					process = tp.addTag("_TestReportSql_TollphaseFilter_");
					tp.save(conn);
					conn.commit();

					p1 = process.getTagSet().addTag("phase1");
					p1.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					p2 = process.getTagSet().addTag("phase2");
					p2.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					root = createWork("_TestReeportSql_TollphaseFilter_", user, conn);
					conn.commit();
					root.save(conn);
					conn.commit();

					final PSSession sess = new MockPSSession(context, user);
					final MockServletRequest req = new MockServletRequest();
					req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
					req.setParameter(Work.NAME, "_TestReportSql_ToolphaseFilter1_");
					req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
					req.setAttribute(PSSession.class.getName(), sess);
					req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

					final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
					try {
						tg = (Tollgate) handler.create(req);
					} catch (Exception e) {
						if (DEBUG) e.printStackTrace();
						fail("Unexpected exception occured\n" + e.getMessage());
					}
					tg.save(conn);
					conn.commit();

					req.setParameter(Work.NAME, "_TestReportSql_ToolphaseFilter2_");

					try {
						tg1 = (Tollgate) handler.create(req);
					} catch (Exception e) {
						if (DEBUG) e.printStackTrace();
						fail("Unexpected exception occured\n" + e.getMessage());
					}
					tg1.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), true);
					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(root);
					rep.addFilter(rf);
					ObjectTypeFilter of = new ObjectTypeFilter(true, Tollgate.TYPE);
					rep.addFilter(of);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Unexpected id in result set", !sr.getResults().contains(tg.getId()) && !sr.getResults().contains(tg1.getId()));

					tg.setParentWork(root, user);
					tg.save(conn);
					conn.commit();
					tg1.setParentWork(root, user);
					tg1.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(),
							sr.getResults().size() == 2 &&
							sr.getResults().contains(tg.getId()) &&
							sr.getResults().contains(tg1.getId())
					);

					TollPhaseFilter f = new TollPhaseFilter(true,false);
					rep.addFilter(f);

					f.addPhase(p2);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().size() == 0);

					f.clear();
					f.addPhase(p1);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().size() == 0);

					tg.setActivePhase((Checkpoint) tg.getCheckpoints().get(0), user);
					tg.save(conn);
					conn.commit();

					tg.addChampion(champ);
					tg.save(conn);
					conn.commit();

					tg.requestApproval(null, "no comments", null);
					tg.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1 && sr.getResults().contains(tg.getId()));

					tg1.setActivePhase((Checkpoint) tg.getCheckpoints().get(1), user);
					tg1.save(conn);
					conn.commit();

					tg1.addChampion(champ);
					tg1.save(conn);
					conn.commit();
					tg1.requestApproval(null, "no comments", null);
					tg1.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1 && sr.getResults().contains(tg.getId()));

					f.addPhase(p2);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(),
							sr.getResults().size() == 2 &&
							sr.getResults().contains(tg.getId()) &&
							sr.getResults().contains(tg1.getId())
					);
                } finally {
					try {
						delete(tg, conn);
						delete(tg1, conn);
						delete(root, conn);
						delete(user, conn);
						delete(champ, conn);

						// remove the process definition
						final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
						if (p1 != null) tp.removeTag(p1.getId());
						if (p2 != null) tp.removeTag(p2.getId());
						if (process != null) tp.removeTag(process.getId());
						tp.save(conn);
						conn.commit();
					} catch (SQLException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testCreateDatePeriodFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final CreateDatePeriodFilter f = new CreateDatePeriodFilter();
				f.setEndDate(Calendar.getInstance().getTime());
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				f.setStartDate(c.getTime());
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				User user = null;
				final long TIMEOUT = 2002L;
				try {
					user = User.createNewUser("_TestReportSql_CreateDateFilter_", context);
					user.save(conn);
					conn.commit();
					CreateDatePeriodFilter f = new CreateDatePeriodFilter();
					f.setStartDate(Calendar.getInstance().getTime());
					pause(TIMEOUT);
					work1 = createWork("_TestReportSql_CreatedateFilter1_", user, conn);
					conn.commit();
					pause(TIMEOUT);
					f.setEndDate(Calendar.getInstance().getTime());
					pause(TIMEOUT);
					work2 = createWork("_TestReportSql_CreatedateFilter2_", user, conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), true);
					work2.setParentWork(work1, user);
					work2.save(conn);
					conn.commit();

					final RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work1);
					rep.addFilter(rf);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 work ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					rep.addFilter(f);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 work id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1);
					assertTrue("Expecting work id", sr.getResults().contains(work1.getId()));

					f.setEndDate(Calendar.getInstance().getTime());
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 work ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);
					assertTrue("Expecting list of work ids", sr.getResults().contains(work1.getId()) && sr.getResults().contains(work2.getId()));
                } finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testEventPeriodFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final EventReport rep = new EventReport(FAKE_SESSION, FAKE_EVENT_TYPE);
				final EventPeriodFilter f = new EventPeriodFilter();
				f.setEndDate(Calendar.getInstance().getTime());
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				f.setStartDate(c.getTime());
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findEvents(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
		assertTrue("Expecting empty result set for fake event type", sr.getResults().isEmpty());

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
            boolean findInArray(Object[] objs, PersistentKey obj) {
                for (int i = 0; i < objs.length ; i++)
                {
                    CreationEvent ev = (CreationEvent) objs[i];
                    if (ev.getObjectId().equals(obj))
                        return true;
                }
                return false;
            }

			protected Object query(Connection conn) throws SQLException {
				User user = null;
				Work work = null;
				Work work1 = null;
				try {
					final long TIMEOUT = 2002L;
					Date startDate = Calendar.getInstance().getTime();
					pause(TIMEOUT);
					user = User.createNewUser("_TestReportSql_EventPeriod_", context);
					user.save(conn);
					conn.commit();

					work = createWork("_TestReportSql_EventPeriod_", user, conn);
					conn.commit();
					work1 = createWork("_TestReportSql_EventPeriod1_", user, conn);
					conn.commit();
					pause(TIMEOUT);
					Date endDate = Calendar.getInstance().getTime();

					final EventReport rep = new EventReport(new MockPSSession(context, user), CreationEvent.TYPE);
					EventPeriodFilter f = new EventPeriodFilter();
					f.setStartDate(endDate);
					pause(TIMEOUT);
					f.setEndDate(Calendar.getInstance().getTime());
					rep.addFilter(f);

					SqlSearchResult sr = _peer.findEvents(rep, conn);
					assertNotNull("Expecting search results; got null", sr);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					f.setStartDate(startDate);
					f.setEndDate(endDate);
					sr = _peer.findEvents(rep, conn);
					assertTrue("Expecting 3 event ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 3);
					assertTrue("Expecting events for created objects",
                            findInArray(sr.getResults().toArray(), work.getId()) &&
                            findInArray(sr.getResults().toArray(), work1.getId()) &&
                            findInArray(sr.getResults().toArray(), user.getFolder().getId())
					);
                } finally {
					delete(work, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testObjectTypeFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserAllocationReport rep = new UserAllocationReport(FAKE_SESSION);
				final ObjectTypeFilter f = new ObjectTypeFilter(true, User.TYPE);
				rep.addFilter(f);
				return _peer.findUserAlphabet(rep, conn, true);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();

		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				Work work3 = null;
				try {
					final ObjectType type = Work.TYPE;

					final User nobody = Nobody.get(context);
					work1 = createWork("_TestReportSql_ObjectTypeFilter_work1_", nobody, conn);
					conn.commit();
					work3 = createWork("_TestReportSql_ObjectTypeFilter_work3_", nobody, conn);
					conn.commit();
					work2 = Work.createNew(FileFolder.TYPE, "_TestReportSQL::testObjectTypeFilter_FileFolder_", nobody);
					work2.save(conn);
					conn.commit();
					work2.setParentWork(work1, nobody);
					work2.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
					RootWorkFilter rwf = new RootWorkFilter(true);
					rwf.setRootWork(work1);
					rep.addFilter(rwf);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 work ids", sr.getResults().size() == 2);

					final ObjectTypeFilter f = new ObjectTypeFilter(true, type);
					rep.addFilter(f);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set", sr.getResults().size() == 1);

					work3.setParentWork(work1, nobody);
					work3.save(conn);
					conn.commit();
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set", sr.getResults().size() == 2);
                } finally {
					delete(work3, conn);
					delete(work2, conn);
					delete(work1, conn);
				}

				return null;
			}
		}.execute();
	}

	public void testMilestoneStatusFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final MilestoneStatusFilter f = new MilestoneStatusFilter(true);
				f.setEnd(Calendar.getInstance().getTime());
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				f.setStart(c.getTime());
				f.setStatus(MilestoneStatusFilter.Status.ALL);
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final MilestoneStatusFilter f = new MilestoneStatusFilter(true);
				f.setEnd(Calendar.getInstance().getTime());
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				f.setStart(c.getTime());
				f.setStatus(MilestoneStatusFilter.Status.DUE);
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final MilestoneStatusFilter f = new MilestoneStatusFilter(true);
				f.setEnd(Calendar.getInstance().getTime());
				final Calendar c = Calendar.getInstance();
				c.add(Calendar.DATE, -1);
				f.setStart(c.getTime());
				f.setStatus(MilestoneStatusFilter.Status.COMPLETED);
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work root = null;
				Work c1 = null;
				Work c2 = null;
				Work c3 = null;
				Milestone m = null;
				Milestone m1 = null;
				User user = null;
				try {
					final Calendar cc = Calendar.getInstance();
					cc.set(Calendar.MILLISECOND, 0);
					cc.add(Calendar.DAY_OF_YEAR, -2);
					final Date start = cc.getTime();

					user = User.createNewUser("_TestReportSql_MilestoneFilter_", context);
					user.save(conn);
					conn.commit();

					root = createWork("_TestReportSql_MilestoneFilter_Root_", user);
					root.save(conn);
					conn.commit();

					Calendar cc1 = Calendar.getInstance();
					cc1.set(Calendar.MILLISECOND, 0);
					cc1.add(Calendar.DAY_OF_YEAR, 1);
					Timestamp end = new Timestamp(cc1.getTime().getTime());
					end.setNanos(0);

					c1 = createWork("_TestReportSql_MilestoneFilter1_", user);
					c1.setParentWork(root, user);
					c1.getSchedules().setSystemEndDate(end);
					//c1.setUpdateSystem(false);
					c1.save(conn);
					conn.commit();

					c2 = createWork("_TestReportSql_MilestoneFilter2_", user);
					c2.setParentWork(root, user);
					c2.getSchedules().setSystemEndDate(end);
					//c2.setUpdateSystem(false);
					c2.save(conn);
					conn.commit();

					c3 = createWork("_TestReportSql_MilestoneFilter3_", user);
					c3.setParentWork(root, user);
					c3.getSchedules().setSystemEndDate(end);
					//c3.setUpdateSystem(false);
					c3.save(conn);
					conn.commit();

					m = (Milestone) Milestone.createNew(Milestone.TYPE, "_TestreportSql_Milestone1_", user);
					m.setParentWork(root, user);
					m.getSchedules().setSystemEndDate(end);
					//m.setUpdateSystem(false);
					m.save(conn);
					conn.commit();

					// start--r-c1-c2-c3-m-------

					try {
						m.getDependencies().addDependency(c1, RelationshipType.SF_DEPENDENCY, user).save(conn);
					} catch (IllegalDependencyException ignored) {
						if (DEBUG) ignored.printStackTrace();
						fail("Unexpected exception");
					}
					m.save(conn);
					conn.commit();

/*
					final SchedulerImpl sImpl = new SchedulerImpl();
					try {
						List problems = sImpl.run(new WSWorkbreakdown(root, user));
						assertTrue("No problems expecting while scheduling", problems.isEmpty());
					} catch (TopologyException ignored) {
						if (DEBUG) ignored.printStackTrace();
						fail("Unexpected exception");
					}
*/
					final PSObjectReport rep = new PSObjectReport(new MockPSSession(context, user), true);
					ObjectTypeFilter tf = new ObjectTypeFilter(true, Work.TYPE);

					rep.addFilter(tf);

					// root work filter
					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(root);
					rep.addFilter(rf);

					final MilestoneStatusFilter f = new MilestoneStatusFilter(true);
					f.setStatus(MilestoneStatusFilter.Status.ALL);
					f.setStart(start);
					Calendar c = Calendar.getInstance();
					c.set(Calendar.MILLISECOND, 0);
					c.add(Calendar.DAY_OF_YEAR, 2);
					f.setEnd(c.getTime());
					rep.addFilter(f);

					// |--r-c1-c2-c3-m--|

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setEnd(null); // |--r-c1-c2-c3-m------
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(null); // -----r-c1-c2-c3-m--|
					f.setEnd(c.getTime());
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(null);
					f.setEnd(null); // ----r-c1-c2-c3-m----
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(c.getTime()); // (--r-c1-c2-c3-m--) |-----------
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());
					f.setStart(null);

					f.setStatus(MilestoneStatusFilter.Status.DUE); // DUE ----r-c1-c2-c3-m----
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(c.getTime()); // DUE |----------------
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					f.setStart(start); // DUE |--r-c1-c2-c3-m------
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setEnd(c.getTime()); // DUE |--r-c1-c2-c3-m--|
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(null); // DUE -----r-c1-c2-c3-m--|
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					//missed
					c = Calendar.getInstance();
					c.set(Calendar.MILLISECOND, 0);
					c.add(Calendar.DAY_OF_YEAR, -5);
					root.getSchedules().setSystemStartDate(new Timestamp(c.getTime().getTime()));
					// -----r---start--c1-c2-c3-m--|
					c.add(Calendar.DAY_OF_YEAR, 2);
					root.getSchedules().setSystemEndDate(new Timestamp(c.getTime().getTime()));
					//root.setUpdateSystem(false);
					root.save(conn);
					// -----rs--re-start--c1-c2-c3-m--|
					conn.commit();
					f.setStatus(MilestoneStatusFilter.Status.MISSED);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 work id; got " + sr.getResults().size(), sr.getResults().size() == 1);
					System.out.println(PSObject.get(((PersistentKey)sr.getResults().toArray()[0]), context).getName());

					// completed
					c = Calendar.getInstance();
					c.set(Calendar.MILLISECOND, 0);
					c.add(Calendar.DAY_OF_YEAR, 1);
					root.setStatus(WorkStatus.ON_TRACK);
					//root.setUpdateSystem(false);
					root.save(conn);
					conn.commit();

					f.setStatus(MilestoneStatusFilter.Status.COMPLETED);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					f.setStart(null);
					f.setEnd(null);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 4 work ids; got " + sr.getResults().size(), sr.getResults().size() == 4);

					f.setStart(start);
					f.setEnd(c.getTime());
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					root.setStatus(WorkStatus.COMPLETED);
					root.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 work id; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setEnd(null);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 work id; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setStart(null);
					f.setEnd(c.getTime());
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 work id; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setStart(c.getTime());
					f.setEnd(null);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());
                } finally {
					delete(m, conn);
					delete(m1, conn);
					delete(c1, conn);
					delete(c2, conn);
					delete(c3, conn);
					delete(root, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	class FakeMyWorkFilter extends MyWorkFilter {

		public FakeMyWorkFilter(boolean isVisible) {
			super(isVisible);
		}

		public void init(Report rep) {
			final ServletRequest req = new MockServletRequest();
			init(rep, req);
		}
	};

	public void testMyWorkFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {

			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				Work work3 = null;
				Work work4 = null;
				User user = null;
				try {
					final User nobody = Nobody.get(context);
					user = User.createNewUser("_TestReportSQL_MyWorkFilter_", context);
					user.save(conn);
					conn.commit();
					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), false);
					final FakeMyWorkFilter f = new FakeMyWorkFilter(true);
					f.init(rep);
					f.setPortfolio(null);
					rep.addFilter(f);
					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr);
                    /**todo report query selects an user folder in case of old permissions and ignore it in case of new
                     * (database) permissions system*/
					if ( context.getPermissions().useClassicPermissions() )
                    {
                        assertTrue("Expecting user folder in result set.", sr.getResults().iterator().hasNext());
					    final PSObject uf = user.getFolder();
					    for (Iterator i = sr.getResults().iterator(); i.hasNext(); ) {
						    final PersistentKey id = (PersistentKey) i.next();
						    assertTrue("Expecting only UserFolder object in result set, got also: " + PSObject.get(id, context).getName(), id.equals(uf.getId()));
					    }
                    } else {
                        assertTrue("Expecting empty result set. got: " + sr.getResults().size(), !sr.getResults().iterator().hasNext());
                    }

					work1 = createWork("_TestReportSQL_MyWorkFilter_work1_", user, conn);
					conn.commit();
					work2 = createWork("_TestReportSQL_MyWorkFilter_work2_", user, conn);
					conn.commit();
					work3 = createWork("_TestReportSQL_MyWorkFilter_work3_", nobody, conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					if ( context.getPermissions().useClassicPermissions() )
                    {
					    assertTrue("Expecting 2 + 1 ids in resulting set", sr.getResults().size() == 2 + 1);
                    } else {
                        assertTrue("Expecting 2 ids in resulting set", sr.getResults().size() == 2 );
                    }

					work3.addTeamMember(user, nobody).setIsConfirmed(Boolean.TRUE);
					work3.save(conn);
					conn.commit();
					sr = _peer.findWorkIds(rep, conn);
                    if ( context.getPermissions().useClassicPermissions() )
                    {
                        assertTrue("Expecting 3 + 1 ids in resulting set", sr.getResults().size() == 3 + 1);
                    } else {
                        assertTrue("Expecting 3 ids in resulting set", sr.getResults().size() == 3 );
                    }
                } finally {
					delete(work4, conn);
					delete(work3, conn);
					delete(work2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testMyIssuesFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			class FakeMyIssuesFilter extends MyIssuesFilter {
				FakeMyIssuesFilter(boolean visible) {
					super(visible);
				}

				void init(Report rep) {
					MockServletRequest req = new MockServletRequest();
					init(rep, req);
				}
			}
			protected Object query(Connection conn) throws SQLException {
				DiscussionItem item1 = null;
				DiscussionItem item2 = null;
				DiscussionItem item3 = null;
				User user = null;
				User user1 = null;
				Work work = null;
				Work work1 = null;
				try {
					user = User.createNewUser("_TestReportSQL_MyIssues_", context);
					user.save(conn);
					conn.commit();
					user1 = User.createNewUser("_TestReportSQL_MyIssues1_", context);
					user1.save(conn);
					conn.commit();

					work = createWork("_TestReportSQL_testMyIssuesFilter_", user, conn);
					conn.commit();
					work1 = createWork("_TestReportSQL_testMyIssuesFilter1_", user1, conn);
					conn.commit();

					item1 = DiscussionItem.createNew(work, null, user, "title", "text");
					item1.save(conn);
					conn.commit();
					item2 = DiscussionItem.createNew(work, null, user, "title", "text");
					item2.save(conn);
					conn.commit();
					item3 = DiscussionItem.createNew(work1, null, user1, "title", "text");
					item3.save(conn);
					conn.commit();

					final DiscussionItemReport rep = new DiscussionItemReport(new MockPSSession(context, user));
					SqlSearchResult sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting more than 2 items in result set; got " + sr.getResults().size(), sr.getResults().size() > 2);

					FakeMyIssuesFilter f = new FakeMyIssuesFilter(true);
					f.init(rep);
					rep.addFilter(f);
					sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting 2 items in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);
					assertTrue("Expecting correct data",
							sr.getResults().contains(item1) &&
							sr.getResults().contains(item2)
					);
                } finally {
					delete(item3, conn);
					delete(item2, conn);
					delete(item1, conn);
					delete(work, conn);
					delete(work1, conn);
					delete(user, conn);
					delete(user1, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testShowArchivedFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				User user1 = null;
				User user2 = null;
				try {
					user1 = User.createNewUser("_TestReportSql_showArchived1_", context);
					user1.save(conn);
					conn.commit();
					user2 = User.createNewUser("_TestReportSql_showArchived2_", context);
					user2.save(conn);
					conn.commit();

					work1 = createWork("_TestReportSql_showArhcived1_", user1, conn);
					conn.commit();
					work2 = createWork("_TestReportSql_showArhcived2_", user1);
					work2.setParentWork(work1, user1);
					work2.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user1), false);
					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work1);
					rep.addFilter(rf);
					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					//work2.archive(user1);
					work2.save(conn);
					conn.commit();

					ShowArchivedFilter f = new ShowArchivedFilter();
					user1.setFeaturePreference(context.getUixFeatures().getFeature(Uix.FEATURE_ARCHIVE_VIEW), true);
					//archive.view
					user1.save(conn);
					conn.commit();
					rep.addFilter(f);
					MockServletRequest mockRequest = new MockServletRequest();
					rep.init(mockRequest);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					user1.setFeaturePreference(context.getUixFeatures().getFeature(Uix.FEATURE_ARCHIVE_VIEW), false);
					rep.init(mockRequest);
					f.entityScope(true);
					sr = _peer.findUserWorkIds(rep, conn, false);
					Map map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 1 user in result set; got " + map.size(), map.size() == 1);
					assertTrue("Expecting 1 work id in result set; got " + ((List) map.get(user1.getId())).size(), ((List) map.get(user1.getId())).size() == 1);
					assertTrue("Expecting work id", ((List) map.get(user1.getId())).contains(work1.getId()));

					work2.addTeamMember(user2, user1).setIsConfirmed(Boolean.TRUE);
					work2.save(conn);
					conn.commit();
					f.entityScope(false);
					sr = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 2 users in result set; got " + map.size(), map.size() == 2);
					assertTrue("Expecting 2 work ids in result set; got " + ((List) map.get(user1.getId())).size(), ((List) map.get(user1.getId())).size() == 2);
					assertTrue("Expecting 1 work id in result set; got " + ((List) map.get(user2.getId())).size(), ((List) map.get(user2.getId())).size() == 1);
					assertTrue("Expecting list of work ids",
							((List) map.get(user1.getId())).contains(work1.getId()) &&
							((List) map.get(user1.getId())).contains(work2.getId()) &&
							((List) map.get(user2.getId())).contains(work2.getId())
					);

					//user2.archive(Nobody.get(context));
					user2.save(conn);
					conn.commit();
					sr = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 1 user in result set; got " + map.size(), map.size() == 1);
					assertTrue("Expecting 2 work ids in result set; got " + ((List) map.get(user1.getId())).size(), ((List) map.get(user1.getId())).size() == 2);

					user1.setFeaturePreference(context.getUixFeatures().getFeature(Uix.FEATURE_ARCHIVE_VIEW), true);
					rep.init(mockRequest);
					sr = _peer.findUserWorkIds(rep, conn, false);
					map = (Map) sr.getResults().toArray()[0];
					assertTrue("Expecting 2 users in result set; got " + map.size(), map.size() == 2);
					assertTrue("Expecting 2 work ids in result set; got " + ((List) map.get(user1.getId())).size(), ((List) map.get(user1.getId())).size() == 2);
					assertTrue("Expecting 1 work id in result set; got " + ((List) map.get(user2.getId())).size(), ((List) map.get(user2.getId())).size() == 1);
					assertTrue("Expecting list of work ids",
							((List) map.get(user1.getId())).contains(work1.getId()) &&
							((List) map.get(user1.getId())).contains(work2.getId()) &&
							((List) map.get(user2.getId())).contains(work2.getId())
					);
                } finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(user1, conn);
					delete(user2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testStatusFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final StatusFilter f = new StatusFilter(true);
				f.add(WorkStatus.NOT_STARTED);
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				User user = null;
				try {
					user = User.createNewUser("_TestReportSql_StatusFilter_", context);
					user.save(conn);
					conn.commit();

					work1 = createWork("_TestReportSql_StatusFilter1_", user, conn);
					conn.commit();
					work2 = createWork("_TestReportSql_StatusFilter2_", user, conn);
					conn.commit();
					work1.setStatus(WorkStatus.PROPOSED);
					work1.save(conn);
					conn.commit();

					work2.setStatus(WorkStatus.PROPOSED);
					work2.setParentWork(work1, user);
					work2.save(conn);
					conn.commit();

					StatusFilter f = new StatusFilter(true);
					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), true);
					final RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work1);
					rep.addFilter(rf);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					rep.addFilter(f);
					f.add(WorkStatus.ON_TRACK);
					f.add(WorkStatus.COMPLETED);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					f.getStatuses().clear();
					f.add(WorkStatus.PROPOSED);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					work2.setStatus(WorkStatus.OFF_TRACK);
					work2.save(conn);
					conn.commit();
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.add(WorkStatus.OFF_TRACK);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);
                } finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testTagsFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				class FakeTagsFilter extends TagsFilter {

					public FakeTagsFilter(boolean isVisible) {
						super(isVisible);
					}

					public void initFilter(Report rep, ServletRequest req) {
						init(rep, req);
					}
				}
				;

				Work work1 = null;
				Work work2 = null;
				TagSet ts = null;
				try {
					ts = TagSet.createNew("_TestReportSQL_TagsFilter_tagset1_", context);
					ts.addLinkableType(Work.TYPE);
					final PSTag tag = ts.addTag("tag1");
					ts.addTag("tag2");
					ts.save(conn);
					conn.commit();

					final User nobody = Nobody.get(context);
					work1 = createWork("_TestReportSql_TagsFilter_work1_", nobody, conn);
					conn.commit();
					work2 = createWork("_TestReportSql_TagsFilter_work2_", nobody, conn);
					conn.commit();
					work2.setParentWork(work1, nobody);
					work2.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
					RootWorkFilter rwf = new RootWorkFilter(true);
					rwf.setRootWork(work1);
					rep.addFilter(rwf);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 work ids", sr.getResults().size() == 2);

					final MockServletRequest req = new MockServletRequest();
					final FakeTagsFilter f = new FakeTagsFilter(true);
					req.setParameter(TagsFilter.NAME + "." + TagSet.TAGSET_PARAM_PREFIX + ts.getId(), tag.getId().toString());
					f.initFilter(rep, req);
					rep.addFilter(f);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set got: " + sr.getResults().size(), sr.getResults().isEmpty());

					try {
						work1.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
					work1.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set", sr.getResults().size() == 1);

					try {
						work2.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
					work2.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set", sr.getResults().size() == 2);
                } finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(ts, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testTagsFilterForIssues() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {

				class FakeTagsFilterForIssues extends TagsFilterForIssues {

					public FakeTagsFilterForIssues(boolean isVisible) {
						super(isVisible);
					}

					public void initFilter(Report rep, ServletRequest req) {
						init(rep, req);
					}
				}

				Work work1 = null;
				DiscussionItem di1 = null;
				DiscussionItem di2 = null;
				TagSet ts = null;
				try {
					ts = TagSet.createNew("_TestReportSQL_TagsFilterForIssues_tagset1_", context);
					ts.addLinkableType(DiscussionItem.TYPE);
					final PSTag tag = ts.addTag("tag1");
					ts.addTag("tag2");
					ts.save(conn);
					conn.commit();

					final User nobody = Nobody.get(context);
					work1 = createWork("_TestReportSql_TagsFilterForIssues_work1_", nobody, conn);
					conn.commit();

					di1 = DiscussionItem.createNew(work1, null, nobody, "title1", "txt1");
					di1.setPriority(new Integer(2));
					di2 = DiscussionItem.createNew(work1, null, nobody, "title2", "txt2");
					di2.setPriority(new Integer(3));
					di1.save(conn);
					di2.save(conn);
					conn.commit();

					final DiscussionItemReport rep = new DiscussionItemReport(FAKE_SESSION);
					RootWorkFilter rwf = new RootWorkFilter(true);
					rwf.setRootWork(work1);
					rep.addFilter(rwf);

					SqlSearchResult sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting 2 discussion items ids", sr.getResults().size() == 2);

					final MockServletRequest req = new MockServletRequest();
					final FakeTagsFilterForIssues f = new FakeTagsFilterForIssues(true);
					req.setParameter(TagsFilter.NAME + "." + TagSet.TAGSET_PARAM_PREFIX + ts.getId(), tag.getId().toString());
					f.initFilter(rep, req);
					rep.addFilter(f);

					sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting empty result set. got: " + sr.getResults().size(),
							sr.getResults().isEmpty());

					try {
						di1.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
					di1.save(conn);
					conn.commit();

					sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting 1 id in result set", sr.getResults().size() == 1);

					try {
						di2.changeTag(TagEventCode.ADD_TAG_CODE, tag, null);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
					di2.save(conn);
					conn.commit();

					sr = _peer.findDiscussionItems(rep, conn);
					assertTrue("Expecting 2 ids in result set", sr.getResults().size() == 2);
                } finally {
					delete(di1, conn);
					delete(di2, conn);
					delete(work1, conn);
					delete(ts, conn);
				}
				return null;
			}
		}.execute();
	}

	private Work createWork(String name, User user) {
		final Work work = Work.createNew(Work.TYPE, name, user);
		return work;
	}

	private Work createWork(String name, User user, Connection conn) throws SQLException {
		Work work = createWork(name, user);
		work.save(conn);
		return work;
	}

	public void testTollgateWaitingForApprovalFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final TollgateWaitingForApprovalFilter f = new TollgateWaitingForApprovalFilter();
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);


		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Tollgate tg = null;
				User user = null;
				User champ = null;
				PSTag process = null;
				Work root = null;
				PSTag p1 = null;
				PSTag p2 = null;
				try {
					user = User.createNewUser("_TestReportSql_TollgateFilter_", context);
					user.save(conn);
					conn.commit();

					champ = User.createNewUser("_TestReportSql_TollgateFilter_champ_", context);
					champ.save(conn);
					conn.commit();

					// create own process definition
					final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
					process = tp.addTag("_TestReportSql_tollgateFilter_");
					tp.save(conn);
					conn.commit();

					p1 = process.getTagSet().addTag("phase1");
					p1.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					p2 = process.getTagSet().addTag("phase2");
					p2.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					root = createWork("_TestReeportSql_TollgateFilter_", user, conn);
					conn.commit();
					root.save(conn);
					conn.commit();

					final PSSession sess = new MockPSSession(context, user);
					final MockServletRequest req = new MockServletRequest();
					req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
					req.setParameter(Work.NAME, "_testReportSql_tollgateFilter1_");
					req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
					req.setAttribute(PSSession.class.getName(), sess);
					req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

					final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
					try {
						tg = (Tollgate) handler.create(req);
					} catch (Exception e) {
						if (DEBUG) e.printStackTrace();
						fail("Unexpected exception occured\n" + e.getMessage());
					}
					tg.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), true);
					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(root);
					rep.addFilter(rf);
					ObjectTypeFilter of = new ObjectTypeFilter(true, Tollgate.TYPE);
					rep.addFilter(of);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Unexpected id in result set", !sr.getResults().contains(tg.getId()));

					tg.setParentWork(root, user);
					tg.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1 && sr.getResults().contains(tg.getId()));

					TollgateWaitingForApprovalFilter f = new TollgateWaitingForApprovalFilter();
					rep.addFilter(f);

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting empty result set", sr.getResults().size() == 0);

					//tg.setActivePhase((Checkpoint) tg.getCheckpoints().get(0));
					tg.addChampion(champ);
					tg.save(conn);
					conn.commit();
                    tg.setActivePhase((Checkpoint) tg.getCheckpoints().get(0), user);
                    tg.save(conn);
                    conn.commit();

					tg.requestApproval(null, "no comments", null);
					tg.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1 && sr.getResults().contains(tg.getId()));
                } finally {
					try {
						delete(tg, conn);
						delete(root, conn);
						delete(user, conn);
						delete(champ, conn);

						// remove the process definition
						final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
						if (p1 != null) tp.removeTag(p1.getId());
						if (p2 != null) tp.removeTag(p2.getId());
						if (process != null) tp.removeTag(process.getId());
						tp.save(conn);
						conn.commit();
					} catch (SQLException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testTrackResourceFilter() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final UserWorksReport rep = new UserWorksReport(FAKE_SESSION, true);
				final TrackResourceFilter f = new TrackResourceFilter();
				rep.addFilter(f);
				SqlSearchResult result = null;
                    result = _peer.findWorkIds(rep, conn);
                return result;
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work1 = null;
				Work work2 = null;
				User user = null;
				try {
					user = User.createNewUser("_TestReportSql_TrackRes_", context);
					user.save(conn);
					conn.commit();

					work1 = createWork("_TestReportSql_TrackRes_1_", user, conn);
					conn.commit();
					work2 = createWork("_TestReportSql_TrackRes_2_", user);
					work2.setParentWork(work1, user);
					work2.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), true);
					final TrackResourceFilter f = new TrackResourceFilter();
					rep.addFilter(f);

					RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work1);
					rep.addFilter(rf);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in result set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					//work1.archive(user);
					work1.save(conn);
					conn.commit();

					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1);
					assertTrue("Expecting work id", sr.getResults().contains(work2.getId()));
                } finally {
					delete(work2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindFinancialMetricIds() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				MetricTemplate tmpl = null;
				MetricTemplate tmpl1 = null;
				User user = null;
				Work work = null;
				MetricInstance inst = null;

				try {
					user = User.createNewUser("_TestReportSql_finFin_", context);
					user.save(conn);
					conn.commit();

					tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_findFinancial_", user);
					tmpl.save(conn);
					conn.commit();
					tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_findFinancial1_", user);
					tmpl1.save(conn);
					conn.commit();

					work = createWork("_TestReportSql_finFinancial1_", user, conn);
					conn.commit();

					inst = MetricInstance.createNew(tmpl, work, false);
					inst.save(conn);
					conn.commit();

					final MetricFinancialReport rep = new MetricFinancialReport(new MockPSSession(context, user));
					rep.addMetricTemplate(tmpl);

					final RootWorkFilter rf = new RootWorkFilter(true);
					rf.setRootWork(work);

					rep.addFilter(rf);

					SqlSearchResult sr = _peer.findFinancialMetricIds(rep, conn);
					assertNotNull("Expecting search results; got null", sr);
					assertTrue("Expecting 1 id in result set; got " + sr.getResults().size(), sr.getResults().size() == 1 && sr.getResults().contains(inst.getId()));


				} finally {
					delete(inst, conn);
					delete(tmpl, conn);
					delete(tmpl1, conn);
					delete(work, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindWorkByFinancialMetricIds() throws Exception {
		SqlSearchResult sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final MetricReport rep = new MetricReport(FAKE_SESSION, false);
				return _peer.findWorkByFinancialMetricIds(rep, conn);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);
		sr = (SqlSearchResult) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final MetricReport rep = new MetricReport(FAKE_SESSION, true);
				return _peer.findWorkByFinancialMetricIds(rep, conn);
			}
		}.execute();
		assertNotNull("Expecting search result; got null", sr);

		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				MetricTemplate tmpl = null;
				MetricInstance inst = null;
				MetricInstance inst1 = null;
				Work work = null;
				Work work1 = null;
				try {
					User user = Nobody.get(context);

					final MetricReport rep = new MetricReport(new MockPSSession(context, user), true);

					int baseCount = _peer.findWorkByFinancialMetricIds(rep, conn).getResults().size();

					work = createWork("_TestReportSql_finFinancial1_", user, conn);
					conn.commit();
					work1 = createWork("_TestReportSql_finFinancial2_", user, conn);
					conn.commit();

					// try to get Project Metric
					tmpl = MetricTemplate.getByCode(MetricReport.FINANCIAL_METRIC_NAME, context);
					if (tmpl != null) { // create instance of the metric template
						inst = MetricInstance.createNew(tmpl, work, false);
						/*inst.save(conn);
						conn.commit();*/
						inst1 = MetricInstance.createNew(tmpl, work1, false);
						/*inst1.save(conn);
						conn.commit();*/
						tmpl = null;
					} else { // if Financial Metric not found create Mock version of the metric
						tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_findFinancial_", user);
						tmpl.setCode(MetricReport.FINANCIAL_METRIC_NAME);
						tmpl.save(conn);
						conn.commit();

						inst = MetricInstance.createNew(tmpl, work, false);
						/*inst.save(conn);
						conn.commit();*/
						inst1 = MetricInstance.createNew(tmpl, work1, false);
						/*inst1.save(conn);
						conn.commit();*/
					}
					inst.save(conn);
					conn.commit();

					SqlSearchResult sr = _peer.findWorkByFinancialMetricIds(rep, conn);
					assertNotNull("Expecting search results; got null", sr);
					assertTrue("Expecting " + (baseCount + 1) + " id in result set; got " + sr.getResults().size(), sr.getResults().size() == baseCount + 1);
					assertTrue("Expecting id in result set", sr.getResults().contains(work.getId()));
					inst1.save(conn);
					conn.commit();
					sr = _peer.findWorkByFinancialMetricIds(rep, conn);
					assertTrue("Expecting " + (baseCount + 2) + " ids in result set; got " + sr.getResults().size(), sr.getResults().size() == baseCount + 2);
					assertTrue("Expecting list of ids in result set", sr.getResults().contains(work.getId()) && sr.getResults().contains(work1.getId()));
				} finally {
					delete(inst, conn);
					delete(inst1, conn);
					delete(tmpl, conn);
					delete(work, conn);
					delete(work1, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testOwnerRootWorkFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			class FakeOwnerRootWorkFilter extends OwnerRootWorkFilter {
				FakeOwnerRootWorkFilter(boolean visible) {
					super(visible);
				}

				void init(PSObject pso, Report rep) {
					MockServletRequest req = new MockServletRequest();
					req.setParameter(OwnerRootWorkFilter.SEARCH_PARAM, pso.getId().toString());
					req.setAttribute(PSSession.class.getName(), new MockPSSession(context, rep.getUser()));
					init(rep, req);
				}
			}
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				Work work1 = null;
				Work work2 = null;
				Work work3 = null;
				MetricTemplate tmpl = null;
				MetricInstance inst = null;
				try {
					user = User.createNewUser("_TestReportSQL_RootOwner_", context);
					user.save(conn);
					conn.commit();

					work1 = createWork("_ReportSQL_RootWork1_", user, conn);
					conn.commit();
					work2 = createWork("_ReportSQL_RootWork2_", user, conn);
					conn.commit();
					work3 = createWork("_ReportSQL_RootWork3_", user, conn);
					conn.commit();
					work2.setParentWork(work1, user);
					work2.save(conn);
					conn.commit();
					work3.setParentWork(work2, user);
					work3.save(conn);
					conn.commit();

					final UserWorksReport rep = new UserWorksReport(new MockPSSession(context, user), false);
					FakeOwnerRootWorkFilter f = new FakeOwnerRootWorkFilter(true);
					f.init(work2, rep);
					rep.addFilter(f);

					SqlSearchResult sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 2 ids in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 2);

					f.init(work1, rep);
					sr = _peer.findWorkIds(rep, conn);
					assertTrue("Expecting 3 ids in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 3);

					// project owner part
					final MetricAdhocReport rep1 = new MetricAdhocReport(new MockPSSession(context, user));
					rep1.addFilter(f);
					f.init(work2, rep1);

					tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSQL_rootOwnerWorkFilter_", user);
					tmpl.save(conn);
					conn.commit();

					inst = MetricInstance.createNew(tmpl, work2, false);
					inst.save(conn);
					conn.commit();
					sr = _peer.findMetricAdhocIds(rep1, conn, null, null);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);
					assertTrue("Expecting id in result set", inst.getId().equals(((Object[]) sr.getResults().toArray()[0])[0]));

					f.init(work3, rep1);
					sr = _peer.findMetricAdhocIds(rep1, conn, null, null);
					assertTrue("Expecting empty result set", sr.getResults().size() == 0);

					f.init(user, rep1);
					sr = _peer.findMetricAdhocIds(rep1, conn, null, null);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);
					assertTrue("Expecting id in result set", inst.getId().equals(((Object[]) sr.getResults().toArray()[0])[0]));
                } finally {
					delete(inst, conn);
					delete(tmpl, conn);
					delete(work3, conn);
					delete(work2, conn);
					delete(work1, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	void pause(long millisec) {
		final String p = "";
		synchronized (p) {
			try {
				p.wait(millisec);
			} catch (InterruptedException ignored) {
			}
		}
	}

	private class MyMetricFilter extends MetricTemplateFilter {
		private Boolean _rollupReady = null;
		private PersistentKey _phaseId = null;

		MyMetricFilter(boolean v, boolean si) {
			super(v, si);
		}

		public void setRollupReady(Boolean value) {
			_rollupReady = value;
		}

		public Boolean getRollupReady() {
			return _rollupReady;
		}

		public PersistentKey getPhaseId() {
			return _phaseId;
		}

		public void setPhaseId(PersistentKey phaseId) {
			_phaseId = phaseId;
		}

	}

	public void testMetricTemplateFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				PSTag process = null;
				PSTag p1 = null;
				PSTag p2 = null;
				MetricTemplate mt = null;
				MetricTemplateItem item = null;
				MetricTemplateItem item1 = null;
				Work work = null;
				MetricInstance inst = null;
				try {
					user = User.createNewUser("_TestReportSql_MetrictFilter_", context);
					user.save(conn);
					conn.commit();

					final MetricAdhocReport rep = new MetricAdhocReport(new MockPSSession(context, user));
					MyMetricFilter f = new MyMetricFilter(true, true);
					rep.addFilter(f);

					// create own process definition
					final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
					process = tp.addTag("_TestReportSql_MetricFilter_");
					tp.save(conn);
					conn.commit();

					p1 = process.getTagSet().addTag("phase1");
					p1.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					p2 = process.getTagSet().addTag("phase2");
					p2.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					//create metric template
					mt = MetricTemplate.createNew(MetricTemplate.TYPE, "__TestReportSQL_MetricFilter_Metric_Template_", user);
					mt.setHasPhases(true);
					try {
						mt.changePhase(TagEventCode.ADD_TAG_CODE, process);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
						fail("Unexpected exception");
					}
					mt.save(conn);

					conn.commit();
					item = MetricTemplateItem.createNew("item name", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
					item.save(conn);
					conn.commit();
					mt.save(conn);
					conn.commit();

					item1 = MetricTemplateItem.createNew("item1 name", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
					item1.save(conn);
					conn.commit();
					mt.save(conn);
					conn.commit();

					f.setTemplate(mt);

					SqlSearchResult sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting empty result set; got null", sr);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());


					/*inst.save(conn);
					conn.commit();*/

					work = createWork("_TestReportSql_MetricFilter_Work1_", user);
					inst = MetricInstance.createNew(mt, work, false);
					work.addMetricToSaveCollection(inst, false);
					work.save(conn);
					conn.commit();

					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting result set; got null", sr);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setRollupReady(Boolean.TRUE);
					inst.setIsReadyForRollup(false,user);
					inst.save(conn);
					conn.commit();

					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting empty result set; got null", sr);
					assertTrue("Expecting empty result set; got " + sr.getResults().size() + " values", sr.getResults().isEmpty());

					inst.setIsReadyForRollup(true,user);
					inst.save(conn);
					conn.commit();

					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting result set; got null", sr);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setPhaseId(p1.getId());

					try {
						PSTag old = inst.getInstancePhaseTag();
						if (old != null) inst.changeTag(TagEventCode.REMOVE_TAG_CODE, old, null);
						inst.changeTag(TagEventCode.ADD_TAG_CODE, p1, null);
					} catch (InvalidTagEventException ignored) {
						if (DEBUG) ignored.printStackTrace();
						fail("Unexpected exception");
					}
					inst.save(conn);
					conn.commit();

					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting result set; got null", sr);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setRollupReady(Boolean.FALSE);
					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting empty result set; got null", sr);
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					f.setRollupReady(null);
					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting result set; got null", sr);
					assertTrue("Expecting 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() == 1);

					f.setTemplate(null);
					sr = _peer.findMetricAdhocIds(rep, conn, null, null);
					assertNotNull("Expecting result set; got null", sr);
					assertTrue("Expecting at least 1 id in resulting set; got " + sr.getResults().size(), sr.getResults().size() >= 1);
                } finally {
					try {
						delete(item, conn);
						delete(item1, conn);
						delete(inst, conn);
						delete(mt, conn);
						delete(work, conn);
						delete(user, conn);

						// remove the process definition
						final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
						if (p1 != null) tp.removeTag(p1.getId());
						if (p2 != null) tp.removeTag(p2.getId());
						if (process != null) tp.removeTag(process.getId());
						tp.save(conn);
						conn.commit();
					} catch (SQLException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testByLoginFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				try {
					user = User.createNewUser("_TestReportSql_ByLoginFilter_", context);
					user.save(conn);
					conn.commit();

					UserByLoginReport rep = new UserByLoginReport(new MockPSSession(context, user));

					ByLoginFilter f = new ByLoginFilter(true);
					f.setIsLogin(null);
					rep.addFilter(f);

					SqlSearchResult sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 user id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting user id", sr.getResults().contains(user.getId()));

					f.setIsLogin(Boolean.TRUE);
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Unexpected user id", !sr.getResults().contains(user.getId()));

					f.setIsLogin(Boolean.FALSE);
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Unexpected user id", !sr.getResults().contains(user.getId()));

					user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
					user.save(conn);
					conn.commit();

					f.setIsLogin(Boolean.FALSE);
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 user id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting user id", sr.getResults().contains(user.getId()));

					user.setLogin("_TestReportSql_Login_");
					user.setPassword("1");
					user.save(conn);
					conn.commit();
					try
					{
						User.loginByUsername( "_TestReportSql_Login_", "1", context );
					}
					catch (LoginException ex) 
					{
						// Ignore.
					}
					user.save(conn);
					conn.commit();

					f.setIsLogin(Boolean.TRUE);
					pause(2002);
					Calendar c = Calendar.getInstance();
					c.set(Calendar.MILLISECOND, 0);
					c.add(Calendar.YEAR, 10);
					f.setStart(c.getTime());
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting empty result set", sr.getResults().isEmpty());

					c = Calendar.getInstance();
					c.set(Calendar.MILLISECOND, 0);
					c.add(Calendar.DAY_OF_YEAR, -1);
					f.setEnd(c.getTime());
					f.setStart(null);
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Unexpected user id", !sr.getResults().contains(user.getId()));

					f.setStart(c.getTime());
					f.setEnd(null);
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 user id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting user id", sr.getResults().contains(user.getId()));

					f.setStart(null);
					f.setEnd(Calendar.getInstance().getTime());
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 user id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting user id", sr.getResults().contains(user.getId()));

					f.setStart(c.getTime());
					f.setEnd(Calendar.getInstance().getTime());
					sr = _peer.findByLoginUserIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 user id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting user id", sr.getResults().contains(user.getId()));
				} finally {
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	class MyAopReportFilter extends AopReportFilter {
		Boolean _rollupReady = null;

		public MyAopReportFilter(boolean isVisible) {
			super(AopReportFilter.NAME, isVisible);
		}

		public Boolean getRollupReady() {
			return _rollupReady;
		}

		public void setRollupReady(Boolean value) {
			_rollupReady = value;
		}
	}

	public void testAopReportFilter() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				MetricTemplate tmpl = null;
				MetricInstance inst = null;
				Work work = null;
				try {
					user = User.createNewUser("_TestReportSql_AopReportFilter_", context);
					user.save(conn);
					conn.commit();

					work = createWork("_TestReportSql_AopFilter_work1_", user, conn);
					conn.commit();

					tmpl = MetricTemplate.getByCode(MetricFinancialReport.FINANCIAL_METRIC_NAME, context);
					if (tmpl == null) {
						tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_AopFilter_MetricTempl_", user);
						tmpl.setCode(MetricFinancialReport.FINANCIAL_METRIC_NAME);
						tmpl.save(conn);
						conn.commit();
						inst = MetricInstance.createNew(tmpl, work, false);
						/*inst.save(conn);
						conn.commit();*/
					} else {
						inst = MetricInstance.createNew(tmpl, work, false);
						/*inst.save(conn);
						conn.commit();*/
						tmpl = null;
					}
					work.addMetricToSaveCollection(inst, false);
					work.save(conn);
					conn.commit();

					inst.setIsReadyForRollup(false,user);
					inst.save(conn);
					conn.commit();

					final MetricFinancialReport rep = new MetricFinancialReport(new MockPSSession(context, user));
					final MyAopReportFilter f = new MyAopReportFilter(true);

					rep.addFilter(f);

					SqlSearchResult sr = _peer.findFinancialMetricIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting at least 1 metric instance id; got " + sr.getResults().size(), sr.getResults().size() >= 1);
					assertTrue("Expecting instance id", sr.getResults().contains(inst.getId()));

					f.setRollupReady(Boolean.TRUE);
					sr = _peer.findFinancialMetricIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Unexpected metric id", !sr.getResults().contains(inst.getId()));

					inst.setIsReadyForRollup(true,user);
					inst.save(conn);
					conn.commit();

					sr = _peer.findFinancialMetricIds(rep, conn);
					assertNotNull("Expecting result set; got null", sr.getResults());
					assertTrue("Expecting metric instance id", sr.getResults().size() >= 1 && sr.getResults().contains(inst.getId()));
				} finally {
					delete(inst, conn);
					delete(work, conn);
					delete(tmpl, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindUserAlphabetLogin() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				User user1 = null;
				try {
					user = User.createNewUser("_TestReportSql_findUserAlphabetLogin_", context);
					user.setLastName("A User");
					user.save(conn);
					conn.commit();

					user1 = User.createNewUser("_TestReportSql_findUserAlphabetLogin1_", context);
					user1.setLastName("B User");
					user1.save(conn);
					conn.commit();

					final UserByLoginReport rep = new UserByLoginReport(new MockPSSession(context, user));

					SqlSearchResult res = _peer.findUserAlphabetLogin(rep, conn);
					assertNotNull("Expecting search results", res);

					final Collection ch = res.getResults();
					assertTrue("Expecting at least two chars in result set; got " + ch.size(), ch.size() > 1);
					assertTrue("Expecting valid letters in result set",
							ch.contains(user.getLastName().substring(0, 1)) &&
							ch.contains(user1.getLastName().substring(0, 1))); // at least first letters of name of our new users
				} finally {
					delete(user, conn);
					delete(user1, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindInstancesForAop() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				MetricTemplate tmpl = null;
				MetricInstance inst1 = null;
				MetricInstance inst2 = null;
				MetricInstance inst3 = null;
				User user = null;
				Work work = null;
				Work work1 = null;
				Work work2 = null;
				PSTag process = null;
				PSTag phase1 = null;
				PSTag phase2 = null;

				final int pause = 5000; // 5 sec
				try {
					Timestamp start = new Timestamp(System.currentTimeMillis());
					pause(pause);
					Timestamp sstart = start;

					user = User.createNewUser("_TestReportSql_findInstancesForAop_", context);
					user.save(conn);
					conn.commit();

					final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
					process = tp.addTag("_TestReportSql_findInstancesForAop_");
					tp.save(conn);
					conn.commit();

					phase1 = tp.addTag("phase1");
					phase1.setParent(process);
					tp.save(conn);
					conn.commit();

					phase2 = tp.addTag("phase2");
					phase2.setParent(process);
					tp.save(conn);
					conn.commit();

					tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_testFindInstancesForAop_", user);
					tmpl.setPermitsRollup(false);
					tmpl.setHasPhases(false);
					tmpl.changePhase(TagEventCode.ADD_TAG_CODE, process);
					tmpl.save(conn);
					conn.commit();

					MetricTemplateItem.createNew("item1", tmpl.getItems(), StaticItem.DEFAULT_TYPE);
					tmpl.save(conn);
					conn.commit();

					work = Work.createNew(Work.TYPE, "_TestReportSql_findInstancesForAop_", user);
					work.save(conn);
					conn.commit();

					inst1 = MetricInstance.createNew(tmpl, work, false);
					work.addMetricToSaveCollection(inst1, false);
					work.save(conn);
					conn.commit();

					inst1.changeTag(TagEventCode.ADD_TAG_CODE, phase2, null);
					inst1.save(conn);
					conn.commit();

					inst1.changeTag(TagEventCode.CHANGE_TAG_CODE, phase1, null);
					inst1.save(conn);
					conn.commit();

					// create the second work
					work1 = Work.createNew(Work.TYPE, "_TestReportSql_findInstancesForAop1_", user);
					work1.save(conn);
					conn.commit();

					inst2 = MetricInstance.createNew(tmpl, work1, false);
					work1.addMetricToSaveCollection(inst2, false);
					work1.save(conn);
					conn.commit();

					inst2.changeTag(TagEventCode.ADD_TAG_CODE, phase1, null);
					inst2.save(conn);
					conn.commit();

					// create the third work
					work2 = Work.createNew(Work.TYPE, "_TestReportSql_findInstancesForAop2_", user);
					work2.save(conn);
					conn.commit();

					inst3 = MetricInstance.createNew(tmpl, work2, false);
					work2.addMetricToSaveCollection(inst3, false);
					work2.save(conn);
					conn.commit();

					inst3.changeTag(TagEventCode.ADD_TAG_CODE, phase1, null);
					inst3.save(conn);
					conn.commit();

					pause(pause);
					Timestamp end = new Timestamp(System.currentTimeMillis());

					final MetricFinancialReport rep = new MetricFinancialReport(new MockPSSession(context, user));
					SqlSearchResult res = _peer.findInstancesForAop(rep, conn, tmpl, null, start, end, phase1.getName());
					Collection ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);

					start = end;
					pause(pause);
					end = new Timestamp(System.currentTimeMillis());

					res = _peer.findInstancesForAop(rep, conn, tmpl, null, start, end, phase1.getName());
					ids = res.getResults();
					assertTrue("Expecting 0 ids in result set; got " + ids.size(), ids.isEmpty());

					inst1.changeTag(TagEventCode.CHANGE_TAG_CODE, phase2, null);
					inst1.save(conn);
					conn.commit();

					inst2.changeTag(TagEventCode.CHANGE_TAG_CODE, phase1, null);
					inst2.save(conn);
					conn.commit();

					pause(pause);
					end = new Timestamp(System.currentTimeMillis());

					res = _peer.findInstancesForAop(rep, conn, tmpl, null, start, end, phase1.getName());
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					res = _peer.findInstancesForAop(rep, conn, tmpl, null, start, end, phase2.getName());
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);

					res = _peer.findInstancesForAop(rep, conn, tmpl, phase2.getId(), start, end, phase2.getName());
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Expecting valid instance id in result set", ids.contains(inst1.getId()));

					res = _peer.findInstancesForAop(rep, conn, tmpl, phase2.getId(), sstart, end, phase2.getName());
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Expecting valid instance id in result set", ids.contains(inst1.getId()));

					res = _peer.findInstancesForAop(rep, conn, tmpl, phase1.getId(), sstart, end, phase1.getName());
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Expecting valid instance id in result set", ids.contains(inst2.getId()));

					res = _peer.findInstancesForAop(rep, conn, tmpl, null, sstart, end, phase1.getName());
					ids = res.getResults();
					assertTrue("Expecting 2 ids in result set; got " + ids.size(), ids.size() == 2);

					res = _peer.findInstancesForAop(rep, conn, tmpl, phase2.getId(), sstart, end, null);
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Expecting valid instance id in result set", ids.contains(inst1.getId()));

					res = _peer.findInstancesForAop(rep, conn, tmpl, phase2.getId(), end, end, null);
					ids = res.getResults();
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Expecting valid instance id in result set", ids.contains(inst1.getId()));

				} catch (InvalidTagEventException ignored) {
					if (DEBUG) ignored.printStackTrace();
					fail("Unexpected InvalidTagEventException occured");
				} finally {
					delete(inst1, conn);
					delete(inst2, conn);
					delete(inst3, conn);
					delete(work, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(tmpl, conn);
					delete(user, conn);
					delete(phase1, conn);
					delete(phase2, conn);
					delete(process, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testGetMetricTypes() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				MetricTemplate tmpl = null;
				User user = null;
				Work work = null;
				MetricInstance inst = null;
				try {
					user = User.createNewUser("_TestReportSql_getMetricTypes_", context);
					user.save(conn);
					conn.commit();
					tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestReportSql_getMetricTypes_", user);
					tmpl.save(conn);
					conn.commit();

					work = createWork("_TestReportSql_getMetricTypesWork_", user, conn);
					conn.commit();

					inst = MetricInstance.createNew(tmpl, work, false);
					inst.save(conn);
					conn.commit();
					List list = _peer.getMetricTypes(tmpl.getId(), conn);
					assertNotNull("Expecting search results; got null", list);
					assertTrue("Expecting 1 id in result set; got " + list.size(), list.size() == 1 && list.contains(Work.TYPE.getCode()));
				} finally {
					delete(inst, conn);
					delete(tmpl, conn);
					delete(work, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindTollgateWithEvents() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Tollgate tg = null;
				Tollgate tg1 = null;
				User user = null;
				User champ = null;
				PSTag process = null;
				PSTag p1 = null;
				PSTag p2 = null;
				final int pause = 3000; // 3 sec
				try {
					pause(pause);

					user = User.createNewUser("_TestReportSql_TollgateEventsUser_", context);
					user.save(conn);
					conn.commit();

					champ = User.createNewUser("_TestReportSql_TollgateEventsUser_champ_", context);
					champ.save(conn);
					conn.commit();

					// create own process definition
					final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
					process = tp.addTag("_TestReportSql_TollgateEventsProcess_");
					tp.save(conn);
					conn.commit();

					p1 = process.getTagSet().addTag("phase1");
					p1.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					p2 = process.getTagSet().addTag("phase2");
					p2.setParent(process);
					process.getTagSet().save(conn);
					conn.commit();

					final PSSession sess = new MockPSSession(context, user);
					final MockServletRequest req = new MockServletRequest();
					req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
					req.setParameter(Work.NAME, "_TestReportSql_ToolphaseFilter1_");
					req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
					req.setAttribute(PSSession.class.getName(), sess);
					req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

					final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
					try {
						tg = (Tollgate) handler.create(req);
					} catch (Exception e) {
						if (DEBUG) e.printStackTrace();
						fail("Unexpected exception occured\n" + e.getMessage());
					}
					tg.save(conn);
					conn.commit();

					tg.setActivePhase((Checkpoint) tg.getCheckpoints().get(0), user);
					tg.save(conn);
					conn.commit();

					tg.addChampion(champ);
					tg.save(conn);
					conn.commit();

					try {
						tg.championApprove("no comments", champ,null,null);
					} catch (ElectronicSignatureException e1) {
						if (DEBUG) e1.printStackTrace();
						fail("Unexpected exception occured\n" + e1.getMessage());
					}
					tg.save(conn);
					conn.commit();

					tg.championCancel("no comments", champ);
					tg.save(conn);
					conn.commit();

					try {
						tg1 = (Tollgate) handler.create(req);
					} catch (Exception e) {
						if (DEBUG) e.printStackTrace();
						fail("Unexpected exception occured\n" + e.getMessage());
					}

					tg1.save(conn);
					conn.commit();

					tg1.setActivePhase((Checkpoint) tg1.getCheckpoints().get(0), user);
					tg1.addChampion(champ);
					tg1.save(conn);
					conn.commit();

					pause(pause);

					final SummaryReport rep = new SummaryReport(new MockPSSession(context, user));
					ProcessFilter filter = new ProcessFilter(true);
					rep.addFilter(filter);
					MockServletRequest reqest = new MockServletRequest();
					reqest.setParameter(ProcessFilter.PROCESS_PARAM, process.getId().toString());
					rep.init(reqest);
					SqlSearchResult res = _peer.findTollgateWithEvents(rep, conn);
					Collection ids = res.getResults();
					assertTrue("Expecting 3 id in result set; got " + ids.size(), ids.size() == 3);
                } finally {
					try {
						delete(tg, conn);
						delete(tg1, conn);
						delete(user, conn);
						delete(champ, conn);

						// remove the process definition
						final TollPhase tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
						if (p1 != null) tp.removeTag(p1.getId());
						if (p2 != null) tp.removeTag(p2.getId());
						if (process != null) tp.removeTag(process.getId());
						tp.save(conn);
						conn.commit();
					} catch (SQLException ignored) {
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	// gags
	public void testGetInstance() throws Exception {
	}

	public void testFindByLoginUserIds() throws Exception { /** see test testByLoginFilter */
	}

	public void testFindMetricAdhocIds() throws Exception { /** see testOwnerRootWorkFilter */
	}
}
