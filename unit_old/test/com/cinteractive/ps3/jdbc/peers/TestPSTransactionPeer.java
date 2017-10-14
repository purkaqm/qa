package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.CIResultSet;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.DeletedUser;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.ObjectEvent;
import com.cinteractive.ps3.events.TimeRejectedEvent;
import com.cinteractive.ps3.pstransactions.ActivityCodes;
import com.cinteractive.ps3.pstransactions.PSTransaction;
import com.cinteractive.ps3.pstransactions.TimeRecordHolder;
import com.cinteractive.ps3.pstransactions.TransactionHelp;
import com.cinteractive.ps3.pstransactions.TransactionType;
import com.cinteractive.ps3.tags.InvalidTagEventException;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagEventCode;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class TestPSTransactionPeer extends TestJdbcPeer {
	private PSTransactionPeer _peer;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(PSTransactionPeer.class.getName(), TestPSTransactionPeer.class.getName());
	}

	protected TestPSTransactionPeer(String name) {
		super(name);
	}


	public static Test bareSuite() {
		final TestSuite suite = new TestSuite("TestPSTransactionPeer");

		suite.addTest(new TestPSTransactionPeer("testActivityDeleted"));
		suite.addTest(new TestPSTransactionPeer("testFindActualCostsByProject"));
		suite.addTest(new TestPSTransactionPeer("testFindById"));
		suite.addTest(new TestPSTransactionPeer("testFindCostsByProject"));
		suite.addTest(new TestPSTransactionPeer("testFindEstimatedCostsByProject"));
		suite.addTest(new TestPSTransactionPeer("testFindNotSubmitted"));
		suite.addTest(new TestPSTransactionPeer("testFindOutstanding"));
		suite.addTest(new TestPSTransactionPeer("testFindRejected"));
		suite.addTest(new TestPSTransactionPeer("testFindTimeSheetEntries"));
		suite.addTest(new TestPSTransactionPeer("testFindTimeSheetEntriesByWeek"));
		suite.addTest(new TestPSTransactionPeer("testFindTimeSheetEntriesP"));
		suite.addTest(new TestPSTransactionPeer("testFindTimeSheetEntriesByWork"));
		suite.addTest(new TestPSTransactionPeer("testFindUnprocessedTimeEntries"));
		suite.addTest(new TestPSTransactionPeer("testGetAmountByDate"));
		suite.addTest(new TestPSTransactionPeer("testGetAmountByRange"));
		suite.addTest(new TestPSTransactionPeer("testGetAmountByRangeWork"));
		suite.addTest(new TestPSTransactionPeer("testGetAmountMoneyByRange"));
		suite.addTest(new TestPSTransactionPeer("testGetAmountMoneyByRangeWork"));
		suite.addTest(new TestPSTransactionPeer("testGetTransByRange"));
		suite.addTest(new TestPSTransactionPeer("testGetUsersByRange"));
		suite.addTest(new TestPSTransactionPeer("testGetWeekbyUser"));
		suite.addTest(new TestPSTransactionPeer("testGetWeekbyUser1"));
		suite.addTest(new TestPSTransactionPeer("testIsTimesheetRejected"));
		suite.addTest(new TestPSTransactionPeer("testGetIsTimesheetSubmit"));
		suite.addTest(new TestPSTransactionPeer("testGetWorksByRange"));
		suite.addTest(new TestPSTransactionPeer("testInsertUpdateDelete"));
		suite.addTest(new TestPSTransactionPeer("testUserDeleted"));
		suite.addTest(new TestPSTransactionPeer("testFindWaitingForApproval"));
		suite.addTest(new TestPSTransactionPeer("testFindApproved"));
		suite.addTest(new TestPSTransactionPeer("testGetSubmittedCountMonthbyUser"));

		suite.addTest(new TestPSTransactionPeer("testGetMonthbyUser"));

		suite.addTest(new TestPSTransactionPeer("testGetWeeksInMonth"));

		suite.addTest(new TestPSTransactionPeer("testGetTimesheetRejectedDate"));
		suite.addTest(new TestPSTransactionPeer("testGetTimesheetAprovalDate"));
		suite.addTest(new TestPSTransactionPeer("testIsTimesheetWaitingForApproval"));

		return suite;
	}

	public void testGetInstance() {
	}

	public void testInsert() { /** see testInsertUpdateDelete */
	}

	public void testUpdate() { /** see testInsertUpdateDelete */
	}

	public void testDelete() { /** see testInsertUpdateDelete */
	}

	public void setUp() throws Exception {
		super.setUp();

		_peer = (PSTransactionPeer) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				return PSTransactionPeer.getInstance(conn);
			}
		}.execute();
		if (_peer == null)
			throw new NullPointerException("Null PSTransactionPeer instance.");
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}

	public void tearDown() throws Exception {
		super.tearDown();
		_peer = null;
	}

	private static final boolean DEBUG = true; //show debug info

	private final void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
//                if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}

	public void testActivityDeleted() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			try {
				_peer.activityDeleted(null, conn);
				fail("Null ActivityId should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ActivityId should throw IllegalArgumentException.");
			}
			try {
				_peer.activityDeleted(FAKE_ID, null);
				fail("Null connection should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null connection should throw IllegalArgumentException.");
			}

			int res = _peer.activityDeleted(FAKE_ID, conn);
			assertTrue("Expecting 0 updated items. got: " + res, res == 0);

			ActivityCodes ac = ActivityCodes.get(context);
			PersistentKey fake_tag_id = null;
			ac.addTag("testPSTran_actag1");

			User user = User.getNobody(context);
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);

			PSTransaction tr1 = null;//PSTransaction.createCostEstimate(Double.valueOf("5.5"), user, work1);
			PSTransaction tr2 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);

			ac.save(conn);
			work1.save(conn);
			fake_tag_id = ac.getTag("testPSTran_actag1").getId();
			tr1.setActivityId(fake_tag_id);
			tr2.setActivityId(fake_tag_id);
			tr1.save(conn);
			tr2.save(conn);

			res = _peer.activityDeleted(fake_tag_id, conn);
			assertTrue("Expecting 2 updated items. got: " + res, res == 2);

			ac.removeTag(fake_tag_id);
			ac.save(conn);
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testFindCostsByProject() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			List res = null;
			User user = User.getNobody(context);
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);

			PSTransaction tr1 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);
			PSTransaction tr2 = null;//PSTransaction.createTimeRollup(Double.valueOf("5.5"), user, work1);
			PSTransaction tr3 = null;//PSTransaction.createCostEstimate(Double.valueOf("5.5"), user, work1);
			//try {
			work1.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);

			//res = _peer.findByProject(work1.getId(), false, PSTransaction.ACTUAL_COST_TYPES, null, null, conn);
			assertNotNull("Expecting not null result set.", res);
			assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another items in result set.", res.contains(tr1) && res.contains(tr2));
			//res = _peer.findByProject(work1.getId(), false, PSTransaction.ESTIMATED_COST_TYPES, null, null, conn);
			assertNotNull("Expecting not null result set.", res);
			assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result set.", res.contains(tr3));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testFindActualCostsByProject() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			List res = null;

			try {
				//res = _peer.findByProject(null, false, null, null, null, conn);
				fail("Null ProjectId should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ProjectId should throw IllegalArgumentException.");
			}
			try {
				//res = _peer.findByProject(FAKE_ID, false, null, null, null, null);
				fail("Null Connection should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection should throw IllegalArgumentException.");
			}

			//res = _peer.findByProject(FAKE_ID, false, PSTransaction.ACTUAL_COST_TYPES, null, null, conn);
			assertNotNull("Expecting not null result set.", res);
			assertTrue("Expecting empty result set for work FAKE_ID: " + FAKE_ID, res.size() == 0);

			User user = User.getNobody(context);
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);

			PSTransaction tr1 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);
			PSTransaction tr2 = null;//PSTransaction.createTimeRollup(Double.valueOf("5.5"), user, work1);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work1);
			//try {
			work1.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);

			//res = _peer.findByProject(work1.getId(), false, PSTransaction.ACTUAL_COST_TYPES, null, null, conn);
			assertNotNull("Expecting not null result set.", res);
			assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another items in result set.", res.contains(tr1) && res.contains(tr2));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testFindById() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());

		conn.setAutoCommit(false);

		try {

			PSTransaction tr = null;//PSTransaction.createTimeEntry(new Double(666), User.getNobody(context), WorkUtil.getNoProject(context));
			_peer.insert(tr, conn);

			PSTransaction tr1;
			try {
				_peer.findById(null, conn);
				fail("Null TransactionId in findById method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null TransactionId in findById method should throw IllegalArgumentException.");
			}

			try {
				_peer.findById(tr.getId(), null);
				fail("Null Connection in findById method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findById method should throw IllegalArgumentException.");
			}

			tr1 = _peer.findById(FAKE_ID, conn);
			assertNull("Expecting null PSTransaction object due to fake Transaction ID", tr1);

			tr1 = _peer.findById(tr.getId(), conn);
			assertNotNull("Expecting not null PSTransaction object", tr1);
			assertTrue("Expecting another result.", tr1.equals(tr));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}

	}

	public void testFindNotSubmitted() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek = context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Map tagSets = new java.util.HashMap();
				long base = System.currentTimeMillis();
				/*empty cases*/
				try {
					_peer.findNotSubmitted(null, FAKE_ID, conn, new Timestamp(base), tagSets, firstWeek);
					fail("Null ContextId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null ContextId should throw IllegalArgumentException.");
				}
				try {
					_peer.findNotSubmitted(FAKE_ID, null, conn, new Timestamp(base), tagSets, firstWeek);
					fail("Null NoProjectId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null NoProjectId should throw IllegalArgumentException.");
				}
				try {
					_peer.findNotSubmitted(FAKE_ID, FAKE_ID, null, new Timestamp(base), tagSets, firstWeek);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}
				try {
					_peer.findNotSubmitted(FAKE_ID, FAKE_ID, conn, null, tagSets, firstWeek);
					fail("Null firstDay should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null firstDay should throw IllegalArgumentException.");
				}
				/*fake context*/
				List res = _peer.findNotSubmitted(FAKE_ID, FAKE_ID, conn, new Timestamp(base), tagSets, firstWeek);
				assertNotNull("Expecting empty result list, not null.", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				/*creating test objects*/
				TagSet tagset1 = TagSet.createNew("testPSTran_ts1_34", context);
				tagset1.addLinkableType(User.TYPE);
				tagset1.addTag("testPSTran_tag11");
				tagset1.addTag("testPSTran_tag12");
				TagSet tagset2 = TagSet.createNew("testPSTran_ts234_35", context);
				tagset2.addLinkableType(User.TYPE);
				tagset2.addTag("testPSTran_tag21");
				tagset2.addTag("testPSTran_tag22");

				User user1 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				Work work3 = Work.createNew(Work.TYPE, "testPSTran_wrk3", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user1, work1);
				tr1.setAmount_h(Double.valueOf("10.5"));
				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user1, work2);
				tr2.setAmount_h(Double.valueOf("10"));
				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user2, work1);
				tr3.setAmount_h(Double.valueOf("10.5"));
				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user2, work2);
				tr4.setAmount_h(Double.valueOf("10"));

				try {
					/*saving test objects*/
					user1.save(conn);
					user2.save(conn);
					tagset1.save(conn);
					tagset2.save(conn);
					conn.commit();

					try {
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset2.getTag("testPSTran_tag21"), null);
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
						user2.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
					} catch (InvalidTagEventException itee) {
						fail(itee.getError());
					}
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					work1.save(conn);
					work2.save(conn);
					work3.save(conn);
					conn.commit();

					int baseCount = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek).size();

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					/*tests*/
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 4) + " items in result set. got: " + res.size(), baseCount + 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));

					res = _peer.findNotSubmitted(contextId, work1.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 1) + " items in result set. got: " + res.size(), baseCount + 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(5), new Float(10), 1) && findResArray(res, user2.getId(), new Float(5), new Float(10), 1));

					/*another week*/
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 8L * 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting another items in result set.", !findIdInResArray(res, user1.getId(), 0));
					assertTrue("Expecting another items in result set.", !findIdInResArray(res, user2.getId(), 0));

					/*with tagset map*/
					tagSets.put(tagset1.getId(), null);
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));

					tagSets.put(tagset2.getId(), null);
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					tagSets.put(tagset1.getId(), tagset1.getTag("testPSTran_tag11").getId());
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));
					tagSets.clear();

					tagSets.put(tagset2.getId(), tagset2.getTag("testPSTran_tag21").getId());
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					/*all transactions submitted*/
					tr1.setSubmitDate(new Timestamp(base));
					tr2.setSubmitDate(new Timestamp(base));
					tr1.save(conn);
					tr2.save(conn);
					conn.commit();
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 2) + " items in result set. got: " + res.size(), baseCount + 2 == res.size());
					assertTrue("Expecting another items in result set.", !findIdInResArray(res, user1.getId(), 0) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));

					/*with TimeRejectedEvents*/
					final Calendar calendar = (Calendar) Calendar.getInstance().clone();
					calendar.setTime(new Date());
					calendar.set(Calendar.HOUR, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					calendar.set(Calendar.MILLISECOND, 0);
					if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
						calendar.add(Calendar.DATE, -7);
					}
					calendar.setFirstDayOfWeek(Calendar.MONDAY);
					calendar.add(Calendar.DATE, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
					Map props = new HashMap();
					props.put(TimeRejectedEvent.COMMENT_CODE, "comment");
					props.put(TimeRejectedEvent.WEEK_CODE, calendar.getTime());
					user2.addSaveEvent(TimeRejectedEvent.TYPE, props);
					user2.save(conn);
					conn.commit();
					res = _peer.findNotSubmitted(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount) + " items in result set. got: " + res.size(), baseCount == res.size());
					assertTrue("Expecting another items in result set.", !findIdInResArray(res, user1.getId(), 0) && !findIdInResArray(res, user2.getId(), 0));
				} finally {
					/*deleting test objects*/
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(work3, conn);
					delete(user1, conn);
					delete(user2, conn);
					delete(tagset1, conn);
					delete(tagset2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindOutstanding() throws Exception {
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Map tagSets = new java.util.HashMap();
				long base = System.currentTimeMillis();
				/*empty cases*/
				try {
					_peer.findOutstanding(null, conn, new Timestamp(base), FAKE_ID, tagSets);
					fail("Null ContextId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null ContextId should throw IllegalArgumentException.");
				}
				try {
					_peer.findOutstanding(contextId, conn, new Timestamp(base), null, tagSets);
					fail("Null NoProjectId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null NoProjectId should throw IllegalArgumentException.");
				}
				try {
					_peer.findOutstanding(contextId, null, new Timestamp(base), FAKE_ID, tagSets);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}
				try {
					_peer.findOutstanding(contextId, conn, null, FAKE_ID, tagSets);
					fail("Null firstDay should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null firstDay should throw IllegalArgumentException.");
				}

				/*fake context*/
				List res = _peer.findOutstanding(FAKE_ID, conn, new Timestamp(base), FAKE_ID, tagSets);
				assertNotNull("Expecting empty result list, not null.", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				/*creating test objects*/
				TagSet tagset1 = TagSet.createNew("testPSTran_ts1_34", context);
				tagset1.addLinkableType(User.TYPE);
				tagset1.addTag("testPSTran_tag11");
				tagset1.addTag("testPSTran_tag12");
				TagSet tagset2 = TagSet.createNew("testPSTran_ts2342", context);
				tagset2.addLinkableType(User.TYPE);
				tagset2.addTag("testPSTran_tag21");
				tagset2.addTag("testPSTran_tag22");

				User user1 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				User user3 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user3.setLastName("testPSTran_usr3");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user1, work1);
				tr1.setTransactionDate(new Timestamp(base - 7L * 24L * 60L * 60L * 1000L));
				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user1, work2);
				tr2.setTransactionDate(new Timestamp(base - 7L * 24L * 60L * 60L * 1000L));
				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user2, work1);
				tr3.setTransactionDate(new Timestamp(base - 7L * 24L * 60L * 60L * 1000L));
				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user2, work2);
				tr4.setTransactionDate(new Timestamp(base - 7L * 24L * 60L * 60L * 1000L));

				try {
					/*saving test objects*/
					user1.save(conn);
					user2.save(conn);
					user3.save(conn);
					tagset1.save(conn);
					tagset2.save(conn);
					conn.commit();

					try {
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset2.getTag("testPSTran_tag21"), null);
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
						user2.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
					} catch (InvalidTagEventException itee) {
						fail(itee.getError());
					}
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					work1.save(conn);
					work2.save(conn);
					conn.commit();
					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					/*Tests*/
					/*testing nobody*/
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting anohter items in result set.", findIdInResArray(res, user1.getId(), 0) && findIdInResArray(res, user2.getId(), 0) && !findIdInResArray(res, user3.getId(), 0));

					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user2.getId(), tagSets);
					assertTrue("Expecting anohter items in result set.", findIdInResArray(res, user1.getId(), 0) && !findIdInResArray(res, user2.getId(), 0) && findIdInResArray(res, user3.getId(), 0));

					/*week with transactions*/
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 8L * 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting anohter items in result set.", !findIdInResArray(res, user1.getId(), 0) && !findIdInResArray(res, user2.getId(), 0));

					/*with tagset map*/
					tagSets.put(tagset1.getId(), null);
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findIdInResArray(res, user1.getId(), 0) && findIdInResArray(res, user2.getId(), 0));

					tagSets.put(tagset2.getId(), null);
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting 1 items in result set. got: " + res.size(), 1 == res.size());
					assertTrue("Expecting another items in result set.", findIdInResArray(res, user1.getId(), 0) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					tagSets.put(tagset1.getId(), tagset1.getTag("testPSTran_tag11").getId());
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findIdInResArray(res, user1.getId(), 0) && findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					tagSets.put(tagset2.getId(), tagset2.getTag("testPSTran_tag21").getId());
					res = _peer.findOutstanding(contextId, conn, new Timestamp(base - 24L * 60L * 60L * 1000L), user3.getId(), tagSets);
					assertTrue("Expecting 1 items in result set. got: " + res.size(), 1 == res.size());
					assertTrue("Expecting another items in result set.", findIdInResArray(res, user1.getId(), 0) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();
				} finally {
					/*deleting test objects*/
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
					delete(user2, conn);
					delete(user3, conn);
					delete(tagset1, conn);
					delete(tagset2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindRejected() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek = context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Map tagSets = new java.util.HashMap();
				long base = System.currentTimeMillis();
				Calendar gc = Calendar.getInstance();
				gc.setTime(new Date(base));
				if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
					base += 2L * 24L * 60L * 60L * 1000L;
				/*empty cases*/
				try {
					//_peer.findRejected(null, FAKE_ID, conn, new Timestamp(base), tagSets, firstWeek);
					fail("Null ContextId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null ContextId should throw IllegalArgumentException.");
				}
				try {
					//_peer.findRejected(FAKE_ID, null, conn, new Timestamp(base), tagSets, firstWeek);
					fail("Null NoProjectId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null NoProjectId should throw IllegalArgumentException.");
				}
				try {
					//_peer.findRejected(FAKE_ID, FAKE_ID, null, new Timestamp(base), tagSets, firstWeek);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}
				try {
					//_peer.findRejected(FAKE_ID, FAKE_ID, conn, null, tagSets, firstWeek);
					fail("Null firstDay should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null firstDay should throw IllegalArgumentException.");
				}
				/*fake context*/
				List res = null;//_peer.findRejected(FAKE_ID, FAKE_ID, conn, new Timestamp(base), tagSets, firstWeek);
				assertNotNull("Expecting empty result list, not null.", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				/*creating test objects*/
				TagSet tagset1 = TagSet.createNew("testPSTran_ts1_34", context);
				tagset1.addLinkableType(User.TYPE);
				tagset1.addTag("testPSTran_tag11");
				tagset1.addTag("testPSTran_tag12");
				TagSet tagset2 = TagSet.createNew("testPSTran_ts234", context);
				tagset2.addLinkableType(User.TYPE);
				tagset2.addTag("testPSTran_tag21");
				tagset2.addTag("testPSTran_tag22");

				User user1 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				Work work3 = Work.createNew(Work.TYPE, "testPSTran_wrk3", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user1, work1);
				tr1.setAmount_h(Double.valueOf("10.5"));
				tr1.setTransactionDate(new Timestamp(base));
				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user1, work2);
				tr2.setAmount_h(Double.valueOf("10"));
				tr2.setTransactionDate(new Timestamp(base));
				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user2, work1);
				tr3.setAmount_h(Double.valueOf("10.5"));
				tr3.setTransactionDate(new Timestamp(base));
				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user2, work2);
				tr4.setAmount_h(Double.valueOf("10"));
				tr4.setTransactionDate(new Timestamp(base));

				final Calendar calendar = (Calendar) Calendar.getInstance().clone();
				calendar.setTime(new Date(base));
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					calendar.add(Calendar.DATE, -7);
				}
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				calendar.add(Calendar.DATE, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
				Map props = new HashMap();
				String comment = "2comment";
				props.put(TimeRejectedEvent.COMMENT_CODE, comment);
				props.put(TimeRejectedEvent.WEEK_CODE, calendar.getTime());
				try {
					/*saving test objects*/
					user1.save(conn);
					user2.save(conn);
					tagset1.save(conn);
					tagset2.save(conn);
					conn.commit();

					try {
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset2.getTag("testPSTran_tag21"), null);
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
						user2.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
					} catch (InvalidTagEventException itee) {
						fail(itee.getError());
					}
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					work1.save(conn);
					work2.save(conn);
					work3.save(conn);
					conn.commit();

					int baseCount =0;//= _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek).size();

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					/*Tests*/
					/*without events*/
					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount) + " items in result list. got: " + res.size(), baseCount == res.size());
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user1.getId(), 0));
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user2.getId(), 0));

					/*events saved*/
					user1.addSaveEvent(TimeRejectedEvent.TYPE, props);
					user2.addSaveEvent(TimeRejectedEvent.TYPE, props);
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 4) + " items in result list. got: " + res.size(), baseCount + 4 == res.size());
					assertTrue("Expecting another items in result list.", findResArray(res, user1.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L) && findCommentInResArray(res, user1.getId(), comment));
					assertTrue("Expecting another items in result list.", findResArray(res, user2.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L) && findCommentInResArray(res, user2.getId(), comment));

					//res = _peer.findRejected(contextId, work2.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 2) + " items in result list. got: " + res.size(), baseCount + 2 == res.size());
					assertTrue("Expecting another items in result list.", findResArray(res, user1.getId(), Float.valueOf("5.5"), Float.valueOf("10.5"), 1L) && findCommentInResArray(res, user1.getId(), comment));
					assertTrue("Expecting another items in result list.", findResArray(res, user2.getId(), Float.valueOf("5.5"), Float.valueOf("10.5"), 1L) && findCommentInResArray(res, user2.getId(), comment));

					/*with tagset map*/
					tagSets.put(tagset1.getId(), null);
					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user1.getId(), comment) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user2.getId(), comment));

					tagSets.put(tagset2.getId(), null);
					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user1.getId(), comment) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					tagSets.put(tagset1.getId(), tagset1.getTag("testPSTran_tag11").getId());
					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user1.getId(), comment) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user2.getId(), comment));
					tagSets.clear();

					tagSets.put(tagset2.getId(), tagset2.getTag("testPSTran_tag21").getId());
					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findCommentInResArray(res, user1.getId(), comment) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					/*setting question with another comment*/
					String comment1 = "1comment";
					props.remove(TimeRejectedEvent.COMMENT_CODE);
					props.put(TimeRejectedEvent.COMMENT_CODE, comment1);
					user1.addSaveEvent(TimeRejectedEvent.TYPE, props);
					user1.save(conn);
					conn.commit();

					//res = _peer.findRejected(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets, firstWeek);
					assertTrue("Expecting " + (baseCount + 4) + " items in result list. got: " + res.size(), baseCount + 4 == res.size());
					assertTrue("Expecting another items in result list.", findResArray(res, user1.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L) && findCommentInResArray(res, user1.getId(), comment1));
					assertTrue("Expecting another items in result list.", findResArray(res, user2.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L) && findCommentInResArray(res, user2.getId(), comment));
				} finally {
					/*deleting test objects*/
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(work3, conn);
					delete(user1, conn);
					delete(user2, conn);
					delete(tagset1, conn);
					delete(tagset2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindEstimatedCostsByProject() throws Exception {
		final Connection conn = getConnection();

		conn.setAutoCommit(false);

		try {
			List res = null;
			try {
				//res = _peer.findByProject(null, false, null, null, null, conn);
				fail("Null ProjectId in findEstimatedCostsByProject method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ProjectId in findEstimatedCostsByProject method should throw IllegalArgumentException.");
			}

			try {
				//res = _peer.findByProject(FAKE_ID, false, null, null, null, null);
				fail("Null Connection in findEstimatedCostsByProject method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findEstimatedCostsByProject method should throw IllegalArgumentException.");
			}

			//res = _peer.findByProject(FAKE_ID, false, PSTransaction.ESTIMATED_COST_TYPES, null, null, conn);
			assertNotNull("Expecting not null set", res);
			assertTrue("Expecting empty set. got: " + res.size(), res.isEmpty());
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}

	}

	private boolean findResArray(List res, PersistentKey userId, Float value, Float value_h, long count) {
		boolean out = false;
		Float valueD = new Float(0);
		Float value_hD = new Float(0);
		long countD = 0;
		for (Iterator i = res.iterator(); i.hasNext();) {
			Object[] tmp = (Object[]) i.next();
			if (userId.equals(tmp[0])) {
				valueD = new Float(valueD.floatValue() + (tmp[1] == null ? 0 : ((Float) tmp[1]).floatValue()));
				value_hD = new Float(value_hD.floatValue() + (tmp[2] == null ? 0 : ((Float) tmp[2]).floatValue()));
				countD++;
			}
		}
		out = (value.equals(valueD) && value_h.equals(value_hD) && countD == count);
/*
		if ( userId.equals( tmp[ 0 ] ) &&
				(!value.equals( tmp[ 1 ] ) ||
				!value_h.equals( tmp[ 2 ] ) ||
				((Long) tmp[ 3 ]).longValue() != count
				)
		)
			return false;
*/
		return out;
	}

	private boolean findIdInResArray(List res, PersistentKey Id, int place) {
		for (Iterator i = res.iterator(); i.hasNext();) {
			Object[] tmp = (Object[]) i.next();
			if ((tmp[place]).equals(Id))
				return true;
		}
		return false;
	}

	private boolean findCommentInResArray(List res, PersistentKey keyId, String comment) {
		for (Iterator i = res.iterator(); i.hasNext();) {
			Object[] tmp = (Object[]) i.next();
			if (tmp[4].equals(comment) && keyId.equals(tmp[0]))
				return true;
			if (!tmp[4].equals(comment) && keyId.equals(tmp[0]))
				return false;
		}
		return false;
	}

	public void testFindTimeSheetEntries() throws Exception
	{
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final Connection conn = getConnection();

		try {

			Set res = null;
			Timestamp today = null;
			try {
				//res = _peer.findTimeSheetEntries(FAKE_ID, today, firstWeek, conn);
				fail("Null Timestamp parameter in findTimeSheetEntries method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in findTimeSheetEntries method should throw IllegalArgumentException.");
			}

			today = new Timestamp(new java.util.Date().getTime());

			try {
				//res = _peer.findTimeSheetEntries(null, today, firstWeek, conn);
				fail("Null ContextId in findTimeSheetEntries method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ContextId in findTimeSheetEntries method should throw IllegalArgumentException.");
			}

			try {
	//			res = _peer.findTimeSheetEntries(FAKE_ID, today, firstWeek, null);
				fail("Null Connection in findTimeSheetEntries method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findTimeSheetEntries method should throw IllegalArgumentException.");
			}

	//		res = _peer.findTimeSheetEntries(FAKE_ID, today, firstWeek, conn);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting empty result set. got" + res.size(), res.isEmpty());
		} finally {
			conn.close();
		}

	}

	public void testFindTimeSheetEntriesByWeek() throws Exception {
		final Connection conn = getConnection();

		try {

			Set res = null;
			Timestamp today = null;
			try {
		//		res = _peer.findTimeSheetEntriesByWeek(FAKE_ID, today, conn, TransactionType.TIME);
				fail("Null Timestamp parameter in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			}

			today = new Timestamp(new java.util.Date().getTime());

			try {
		//		res = _peer.findTimeSheetEntriesByWeek(null, today, conn, TransactionType.TIME);
				fail("Null ContextId in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ContextId in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			}

			try {
		//		res = _peer.findTimeSheetEntriesByWeek(FAKE_ID, today, null, TransactionType.TIME);
				fail("Null Connection in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findTimeSheetEntriesByWeek method should throw IllegalArgumentException.");
			}

		//	res = _peer.findTimeSheetEntriesByWeek(FAKE_ID, today, conn, TransactionType.TIME);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting empty result set. got" + res.size(), res.isEmpty());
		} finally {
			conn.close();
		}

	}

	public void testFindTimeSheetEntriesP() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek = context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Set res = null;
			Timestamp today = new Timestamp(System.currentTimeMillis());
			User user = User.createNewUser("testPSTran" + Uuid.create(), context);
			user.setLastName("testPSTran_user");

			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);
			Work work2 = Work.createNew(Work.TYPE, "testPSTran_work2", user);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work1);
			tr1.setTransactionDate(today);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			tr2.setTransactionDate(today);
			PSTransaction tr4 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);
			tr4.setTransactionDate(today);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work1);
			tr3.setTransactionDate(new Timestamp(System.currentTimeMillis() + 24L * 60L * 60L * 1000L));
			//try {
			user.save(conn);
			work1.save(conn);
			work2.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);

		//	res = _peer.findTimeSheetEntriesByWeek(user.getId(), today, conn, TransactionType.TIME);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting another results count. got: " + res.size(), res.size() == 3);
			assertTrue("Expecting another results in set.", res.contains(tr1) && res.contains(tr2) && res.contains(tr3));
		//	res = _peer.findTimeSheetEntries(user.getId(), today, firstWeek, conn);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting another results count. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another results in set.", res.contains(tr1) && res.contains(tr2));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testFindTimeSheetEntriesByWork() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Set res = null;

			try {
			//	res = _peer.findTimeSheetEntriesByWork(null, conn);
				fail("Null PersistentKey parameter in findTimeSheetEntriesByWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in findTimeSheetEntriesByWork method should throw IllegalArgumentException.");
			}
			try {
		//		res = _peer.findTimeSheetEntriesByWork(FAKE_ID, null);
				fail("Null Connection in findTimeSheetEntriesByWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findTimeSheetEntriesByWork method should throw IllegalArgumentException.");
			}

		//	res = _peer.findTimeSheetEntriesByWork(FAKE_ID, conn);
			assertNotNull("Expecting not null set", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

			User user = User.createNewUser("testPSTran" + Uuid.create(), context);
			user.setLastName("testPSTran_user");

			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);
			Work work2 = Work.createNew(Work.TYPE, "testPSTran_work2", user);
			Work work3 = Work.createNew(Work.TYPE, "testPSTran_work3", user);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work1);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			PSTransaction tr4 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);
			// try {
			user.save(conn);
			work1.save(conn);
			work2.save(conn);
			work3.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);

		//	res = _peer.findTimeSheetEntriesByWork(work1.getId(), conn);
			assertNotNull("Expecting not null set", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result set.", res.contains(tr1.getId().toString()));
		//	res = _peer.findTimeSheetEntriesByWork(work2.getId(), conn);
			assertNotNull("Expecting not null set", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another items in result set.", res.contains(tr2.getId().toString()) && res.contains(tr3.getId().toString()));
		//	res = _peer.findTimeSheetEntriesByWork(work3.getId(), conn);
			assertNotNull("Expecting not null set", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testFindUnprocessedTimeEntries() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Set res = null;
			try {
		//		res = _peer.findUnprocessedTimeEntries(null, conn);
				fail("Null ContextId in findUnprocessedTimeEntries method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null ContextId in findUnprocessedTimeEntries method should throw IllegalArgumentException.");
			}
			try {
		//		res = _peer.findUnprocessedTimeEntries(context.getId(), null);
				fail("Null Connection in findUnprocessedTimeEntries method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in findUnprocessedTimeEntries method should throw IllegalArgumentException.");
			}

		//	res = _peer.findUnprocessedTimeEntries(FAKE_ID, conn);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

			User user = User.createNewUser("testPSTran" + Uuid.create(), context);
			user.setLastName("testPSTran_user");

			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user);
			Work work2 = Work.createNew(Work.TYPE, "testPSTran_work2", user);

			Timestamp today = new Timestamp(System.currentTimeMillis());

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work1);
			tr1.setApprovalDate(today);
			tr1.setApprovedById(user.getId());
			tr1.setWasProcessed(new Boolean(false));
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			tr2.setApprovalDate(today);
			tr2.setApprovedById(user.getId());
			tr2.setWasProcessed(new Boolean(false));
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			tr3.setApprovalDate(today);
			tr3.setApprovedById(user.getId());
			tr3.setWasProcessed(new Boolean(true));
			PSTransaction tr4 = null;//PSTransaction.createActualCost(Double.valueOf("5.5"), user, work1);
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			tr5.setApprovalDate(today);
			tr5.setApprovedById(user.getId());
			PSTransaction tr6 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			tr6.setApprovalDate(today);
			PSTransaction tr7 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work2);
			//try {
			user.save(conn);
			work1.save(conn);
			work2.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);
			tr6.save(conn);
			tr7.save(conn);

		//	res = _peer.findUnprocessedTimeEntries(context.getId(), conn);
			assertNotNull("Expecting not null List", res);
			assertTrue("Expecting another results.", res.contains(tr1));
			assertTrue("Expecting another results.", res.contains(tr2));
			assertTrue("Expecting another results.", !res.contains(tr3));
			assertTrue("Expecting another results.", !res.contains(tr4));
			assertTrue("Expecting another results.", res.contains(tr5));
			assertTrue("Expecting another results.", !res.contains(tr6));
			assertTrue("Expecting another results.", !res.contains(tr7));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetAmountByDate() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
		    Double res = null;
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());

			try {
				res = _peer.getAmountByDate(null, today1, conn);
				fail("Null PersistentKey parameter in getAmountByDate method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getAmountByDate method should throw IllegalArgumentException.");
			}
			try {
				res = _peer.getAmountByDate(FAKE_ID, null, conn);
				fail("Null Timestamp parameter in getAmountByDate method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountByDate method should throw IllegalArgumentException.");
			}
			try {
				res = _peer.getAmountByDate(FAKE_ID, today1, null);
				fail("Null Connection parameter in getAmountByDate method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getAmountByDate method should throw IllegalArgumentException.");
			}

			res = _peer.getAmountByDate(FAKE_ID, today1, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);

			User user1 = User.createNewUser("testPSTran" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2.setTransactionDate(today2);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr4.setTransactionDate(today2);
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);

			res = _peer.getAmountByDate(user1.getId(), today1, conn);
			assertTrue("Expecting res = 3. got: " + res, res.floatValue() == 3);
			res = _peer.getAmountByDate(user2.getId(), today1, conn);
			assertTrue("Expecting res = 2. got: " + res, res.floatValue() == 2);
			res = _peer.getAmountByDate(user1.getId(), tomorrow, conn);
			assertTrue("Expecting res = 4. got: " + res, res.floatValue() == 4);
			res = _peer.getAmountByDate(user2.getId(), tomorrow, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetAmountByRange() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
			Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);

			try {
				_peer.getAmountByRange(null, yesterday, tomorrow, conn);
				fail("Null PersistentKey parameter in getAmountByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getAmountByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, null, tomorrow, conn);
				fail("Null Timestamp parameter in getAmountByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, yesterday, null, conn);
				fail("Null Timestamp parameter in getAmountByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, yesterday, tomorrow, null);
				fail("Null Connection parameter in getAmountByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getAmountByRange method should throw IllegalArgumentException.");
			}

			Double res = _peer.getAmountByRange(FAKE_ID, yesterday, tomorrow, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);

			User user1 = User.createNewUser("testPSTran" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr5.setTransactionDate(today1);
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);

			res = _peer.getAmountByRange(user1.getId(), today1, tomorrow, conn);
			assertTrue("Expecting res = 7. got: " + res, res.floatValue() == 7);
			res = _peer.getAmountByRange(user2.getId(), today1, tomorrow, conn);
			assertTrue("Expecting res = 2. got: " + res, res.floatValue() == 2);
			res = _peer.getAmountByRange(user1.getId(), today2, today1, conn);
			assertTrue("Expecting res = 3. got: " + res, res.floatValue() == 3);
			res = _peer.getAmountByRange(user1.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting res = 7. got: " + res, res.floatValue() == 7);
			res = _peer.getAmountByRange(user1.getId(), tomorrow, yesterday, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);
			res = _peer.getAmountByRange(user2.getId(), yesterday, today1, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	private Map parseResult(CIResultSet in) throws SQLException {
		Map out = new HashMap();
		while (in.next()) {
			out.put(Uuid.get(in.getString(1)), new Float(in.getFloat(2)));
		}
		return out;
	}

	public void testGetAmountByRangeWork() throws Exception {
		final InstallationContext context = InstallationContext.get(getContextName());
		Calendar cal = Calendar.getInstance();
		final Timestamp today = new Timestamp(System.currentTimeMillis());
		cal.setTime(today);
		cal.set(Calendar.HOUR, 1);
		final Timestamp today1 = new Timestamp(cal.getTime().getTime());
		cal.set(Calendar.HOUR, 2);
		final Timestamp today2 = new Timestamp(cal.getTime().getTime());
		cal.add(Calendar.DATE, 1);
		final Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
		final Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				try {
					_peer.getAmountByRangeWork(null, yesterday, tomorrow, conn);
					fail("Null PersistentKey parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null PersistentKey parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				}
				try {
					_peer.getAmountByRangeWork(FAKE_ID, null, tomorrow, conn);
					fail("Null Timestamp parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null Timestamp parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				}
				try {
					_peer.getAmountByRangeWork(FAKE_ID, yesterday, null, conn);
					fail("Null Timestamp parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null Timestamp parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				}
				try {
					_peer.getAmountByRangeWork(FAKE_ID, yesterday, tomorrow, null);
					fail("Null Connection parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null Connection parameter in getAmountByRangeWork method should throw IllegalArgumentException.");
				}

				Map res = _peer.getAmountByRangeWork(FAKE_ID, yesterday, tomorrow, conn);
				assertTrue("Expecting empty result map. got: " + res.size(), res.isEmpty());

				return null;
			}
		}.execute();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			User user1 = User.createNewUser("testPSTran_email1" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran_email2" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);
			Work work2 = Work.createNew(Work.TYPE, "testPSTran_work2", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work2);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			PSTransaction tr6 = null;//PSTransaction.createTimeEntry(new Double(4), user2, work2);
			tr6.setTransactionDate(today1);
			tr6.setApprovalDate(today1);
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work2);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user2, work1);
			tr5.setTransactionDate(today1);

			user1.save(conn);
			user2.save(conn);
			//conn.commit();
			work1.save(conn);
			work2.save(conn);
			//conn.commit();
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);
			tr6.save(conn);
			//conn.commit();

			Map res = _peer.getAmountByRangeWork(work1.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(5)));

			res = _peer.getAmountByRangeWork(work2.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()) && res.containsKey(user2.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(2)) && (res.get(user2.getId())).equals(new Float(6)));

			res = _peer.getAmountByRangeWork(work1.getId(), today2, today1, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(1)));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetAmountMoneyByRange() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
			Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);

			try {
				_peer.getAmountMoneyByRange(null, yesterday, tomorrow, conn);
				fail("Null PersistentKey parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, null, tomorrow, conn);
				fail("Null Timestamp parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, yesterday, null, conn);
				fail("Null Timestamp parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRange(FAKE_ID, yesterday, tomorrow, null);
				fail("Null Connection parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getAmountMoneyByRange method should throw IllegalArgumentException.");
			}

			Double res = _peer.getAmountByRange(FAKE_ID, yesterday, tomorrow, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);

			User user1 = User.createNewUser("testPSTran_email1" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran_email2" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			tr1.setAmount_h(new Double(1));
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			tr2.setAmount_h(new Double(2));
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			tr3.setAmount_h(new Double(4));
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			tr4.setAmount_h(new Double(2));
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr5.setTransactionDate(today1);
			tr5.setAmount_h(new Double(1));
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);

			res = _peer.getAmountMoneyByRange(user1.getId(), today1, tomorrow, conn);
			assertTrue("Expecting res = 7. got: " + res, res.floatValue() == 7);
			res = _peer.getAmountMoneyByRange(user2.getId(), today1, tomorrow, conn);
			assertTrue("Expecting res = 2. got: " + res, res.floatValue() == 2);
			res = _peer.getAmountMoneyByRange(user1.getId(), today2, today1, conn);
			assertTrue("Expecting res = 3. got: " + res, res.floatValue() == 3);
			res = _peer.getAmountMoneyByRange(user1.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting res = 7. got: " + res, res.floatValue() == 7);
			res = _peer.getAmountMoneyByRange(user1.getId(), tomorrow, yesterday, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);
			res = _peer.getAmountMoneyByRange(user2.getId(), yesterday, today1, conn);
			assertTrue("Expecting res = 0. got: " + res, res.floatValue() == 0);
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetAmountMoneyByRangeWork() throws Exception {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
			Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);

			try {
				_peer.getAmountByRangeWork(null, yesterday, tomorrow, conn);
				fail("Null PersistentKey parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRangeWork(FAKE_ID, null, tomorrow, conn);
				fail("Null Timestamp parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRangeWork(FAKE_ID, yesterday, null, conn);
				fail("Null Timestamp parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			}
			try {
				_peer.getAmountByRangeWork(FAKE_ID, yesterday, tomorrow, null);
				fail("Null Connection parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getAmountMoneyByRangeWork method should throw IllegalArgumentException.");
			}

			Map res = _peer.getAmountMoneyByRangeWork(FAKE_ID, yesterday, tomorrow, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.isEmpty());

			User user1 = User.createNewUser("testPSTran_email1" + Uuid.create(), context);
			user1.generateAlerts(false);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran_email2" + Uuid.create(), context);
			user2.generateAlerts(false);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);
			work1.generateAlerts(false);
			Work work2 = Work.createNew(Work.TYPE, "testPSTran_work2", user1);
			work2.generateAlerts(false);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			tr1.setAmount_h(new Double(1));
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			tr3.setAmount_h(new Double(4));
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work2);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			tr2.setAmount_h(new Double(2));
			PSTransaction tr6 = null;//PSTransaction.createTimeEntry(new Double(4), user2, work2);
			tr6.setTransactionDate(today1);
			tr6.setApprovalDate(today1);
			tr6.setAmount_h(new Double(4));
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work2);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			tr4.setAmount_h(new Double(2));
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user2, work1);
			tr5.setTransactionDate(today1);
			tr5.setAmount_h(new Double(1));
			;
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);
			work2.save(conn);
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);
			tr6.save(conn);

			res = _peer.getAmountMoneyByRangeWork(work1.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(5)));

			res = _peer.getAmountMoneyByRangeWork(work2.getId(), yesterday, tomorrow, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 2);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()) && res.containsKey(user2.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(2)) && (res.get(user2.getId())).equals(new Float(6)));

			res = _peer.getAmountMoneyByRangeWork(work1.getId(), today2, today1, conn);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 1);
			assertTrue("Expecting another items in result map.", res.containsKey(user1.getId()));
			assertTrue("Expecting another items in result map.", (res.get(user1.getId())).equals(new Float(1)));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetTransByRange() throws Exception {
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
			Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);

			try {
				_peer.getTransByRange(true, yesterday, tomorrow, TransactionHelp.TIME_TYPES, conn, null);
				fail("Null Context Id parameter in getTransByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Context Id parameter in getTransByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getTransByRange(true, null, tomorrow, TransactionHelp.TIME_TYPES, conn, contextId);
				fail("Null Timestamp parameter in getTransByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getTransByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getTransByRange(true, yesterday, null, TransactionHelp.TIME_TYPES, conn, contextId);
				fail("Null Timestamp parameter in getTransByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getTransByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getTransByRange(true, yesterday, tomorrow, TransactionHelp.TIME_TYPES, null, contextId);
				fail("Null Connection parameter in getTransByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getTransByRange method should throw IllegalArgumentException.");
			}

			Map res = _peer.getTransByRange(true, yesterday, tomorrow, TransactionHelp.TIME_TYPES, conn, FAKE_ID);
			assertTrue("Expecting empty result map. got: " + res.size(), res.size() == 0);

			User user1 = User.createNewUser("testPSTran_email1" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran_email2" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr5.setTransactionDate(today1);
			PSTransaction tr6 = null;//PSTransaction.createCostEstimate(new Double(1), user1, work1);
			tr6.setTransactionDate(today1);
			tr6.setApprovalDate(today1);
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);

			int baseCount = _peer.getTransByRange(true, yesterday, tomorrow, TransactionHelp.TIME_TYPES, conn, contextId).size();

			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);
			tr6.save(conn);

			res = _peer.getTransByRange(true, yesterday, tomorrow, TransactionHelp.TIME_TYPES, conn, contextId);
			assertTrue("Expecting " + (baseCount + 2) + " result map. got: " + res.size(), res.size() == baseCount + 2);
			assertTrue("Expecting another items in result set", res.containsKey(user1.getId()) && res.containsKey(user2.getId()));
			List lstRes = (List) res.get(user1.getId());
			assertTrue("Expecting another items in result set", lstRes.size() == 3);
			assertTrue("Expecting another items in result set", lstRes.contains(tr1) && lstRes.contains(tr2) && lstRes.contains(tr3));
			lstRes = (List) res.get(user2.getId());
			assertTrue("Expecting another items in result set", lstRes.size() == 1);
			assertTrue("Expecting another items in result set", lstRes.contains(tr4));

			res = _peer.getTransByRange(true, today2, today1, TransactionHelp.TIME_TYPES, conn, contextId);
			assertTrue("Expecting another items in result set", res.containsKey(user1.getId()) && !res.containsKey(user2.getId()));
			lstRes = (List) res.get(user1.getId());
			assertTrue("Expecting another items in result set", lstRes.size() == 2);
			assertTrue("Expecting another items in result set", lstRes.contains(tr1) && lstRes.contains(tr2));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	public void testGetUsersByRange() throws Exception {
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		final Connection conn = getConnection();
		conn.setAutoCommit(false);
		try {
			Calendar cal = Calendar.getInstance();
			Timestamp today = new Timestamp(System.currentTimeMillis());
			cal.setTime(today);
			cal.set(Calendar.HOUR, 1);
			Timestamp today1 = new Timestamp(cal.getTime().getTime());
			cal.set(Calendar.HOUR, 2);
			Timestamp today2 = new Timestamp(cal.getTime().getTime());
			cal.add(Calendar.DATE, 1);
			Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
			Timestamp yesterday = new Timestamp(today2.getTime() - 24L * 60L * 60L * 1000L);

			try {
				_peer.getUsersIdsByRange(yesterday, tomorrow, conn, null);
				fail("Null Context Id parameter in getUsersByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Context Id parameter in getUsersByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getUsersIdsByRange(null, tomorrow, conn, contextId);
				fail("Null Timestamp parameter in getUsersByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getUsersByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getUsersIdsByRange(yesterday, null, conn, contextId);
				fail("Null Timestamp parameter in getUsersByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getUsersByRange method should throw IllegalArgumentException.");
			}
			try {
				_peer.getUsersIdsByRange(yesterday, tomorrow, null, contextId);
				fail("Null Connection parameter in getUsersByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getUsersByRange method should throw IllegalArgumentException.");
			}

			Set res = _peer.getUsersIdsByRange(yesterday, tomorrow, conn, FAKE_ID);
			assertNotNull("Expecting empty result set, not null.", res);
			assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

			User user1 = User.createNewUser("testPSTran_email1" + Uuid.create(), context);
			user1.setLastName("testPSTran_user1");
			User user2 = User.createNewUser("testPSTran_email2" + Uuid.create(), context);
			user2.setLastName("testPSTran_user2");
			Work work1 = Work.createNew(Work.TYPE, "testPSTran_work1", user1);

			PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr1.setTransactionDate(today1);
			tr1.setApprovalDate(today1);
			PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2.setTransactionDate(today2);
			tr2.setApprovalDate(today2);
			PSTransaction tr3 = null;//PSTransaction.createTimeEntry(new Double(4), user1, work1);
			tr3.setTransactionDate(tomorrow);
			tr3.setApprovalDate(tomorrow);
			PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr4.setTransactionDate(tomorrow);
			tr4.setApprovalDate(tomorrow);
			PSTransaction tr5 = null;//PSTransaction.createTimeEntry(new Double(1), user1, work1);
			tr5.setTransactionDate(today1);
			PSTransaction tr6 = null;//PSTransaction.createCostEstimate(new Double(1), user1, work1);
			tr6.setTransactionDate(today1);
			tr6.setApprovalDate(today1);
			//try {
			user1.save(conn);
			user2.save(conn);
			work1.save(conn);

			int baseCount = _peer.getUsersIdsByRange(yesterday, tomorrow, conn, contextId).size();
			tr1.save(conn);
			tr2.save(conn);
			tr3.save(conn);
			tr4.save(conn);
			tr5.save(conn);
			tr6.save(conn);

			res = _peer.getUsersIdsByRange(yesterday, tomorrow, conn, contextId);
			assertTrue("Expecting " + (baseCount + 2) + " result map. got: " + res.size(), res.size() == baseCount + 2);
			assertTrue("Expecting another items in result set", res.contains(user1.getId().toString()) && res.contains(user2.getId().toString()));

			res = _peer.getUsersIdsByRange(today2, today1, conn, contextId);
			assertTrue("Expecting another items in result set", res.contains(user1.getId().toString()) && !res.contains(user2.getId().toString()));
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	private boolean myCompare(Object o1, Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else {
			if (o1 == null || o2 == null)
				return false;
			else
				return o1.equals(o2);
		}
	}

	private boolean findResArray(List res, String project, String tag, Object[] sumsByDay) {
		String workId = null;
		String activityId = null;
		Calendar gc = Calendar.getInstance();
		gc.setTime(new Date(System.currentTimeMillis()));
		gc.setFirstDayOfWeek(Calendar.MONDAY);
		gc.add(Calendar.DATE, Calendar.MONDAY - gc.get(Calendar.DAY_OF_WEEK));
		gc.set(Calendar.HOUR, 1);
		gc.set(Calendar.MINUTE, 0);
		gc.set(Calendar.SECOND, 0);
		gc.set(Calendar.MILLISECOND, 0);
		final Timestamp today = new Timestamp(gc.getTime().getTime());
		int k = 0;
		boolean out = true;
		for (Iterator rset = res.iterator(); rset.hasNext();) {

			TimeRecordHolder rh = (TimeRecordHolder) rset.next();
			workId = (String) rh.get(TimeRecordHolder.TR_PROJECT_ID);
			activityId = rh.get(TimeRecordHolder.TR_ACTIVITY_ID) == null ? com.cinteractive.ps3.pstransactions.TimeProject.NO_ACTIVITY.toString() : (String) rh.get(TimeRecordHolder.TR_ACTIVITY_ID);
			Float hours = (Float) rh.get(TimeRecordHolder.TR_HOURS);
			Timestamp currDay = (Timestamp) rh.get(TimeRecordHolder.TR_DATE);
			k = (int) ((currDay.getTime() - today.getTime()) / (3600 * 24000));
			if (workId.equals(project) && activityId.equals(tag)) {
				Float trVal = (Float) sumsByDay[k];
				out = out && myCompare(trVal, hours);
			}

		}
		return out;
	}

	private boolean findResArrayM(List res, String project, Object[] sumsByDay, Timestamp today) {
		String workId = null;
		int k = 0;
		boolean out = true;
		for (Iterator rset = res.iterator(); rset.hasNext();) {

			TimeRecordHolder rh = (TimeRecordHolder) rset.next();
			workId = (String) rh.get(TimeRecordHolder.TR_PROJECT_ID);
			Float hours = (Float) rh.get(TimeRecordHolder.TR_HOURS);
			Timestamp currDay = (Timestamp) rh.get(TimeRecordHolder.TR_DATE);
			k = (int) ((currDay.getTime() - today.getTime()) / (3600 * 24000) / 7);
			if (workId.equals(project)) {
				Float trVal = (Float) sumsByDay[k];
				out = out && myCompare(trVal, hours);
			}

		}
		return out;
	}

	public void testGetWeekbyUser() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final Connection conn = getConnection();
		User user1 = null;
		User user2 = null;
		Work work1 = null;
		Work work2 = null;
		PSTransaction tr1w1ac1 = null;
		PSTransaction tr1w1ac1_1 = null;
		PSTransaction tr2w1 = null;
		PSTransaction tr2w1ac1 = null;
		PSTransaction tr3 = null;
		PSTransaction tr3w1ac2 = null;
		PSTransaction tr3w2ac2 = null;
		PSTransaction tr4w2ac2 = null;
		PersistentKey fake_tag_id1 = null;
		PersistentKey fake_tag_id2 = null;
		ActivityCodes ac = null;
		try {
			Calendar gc = Calendar.getInstance();
			gc.setTime(new Date(System.currentTimeMillis()));
			gc.setFirstDayOfWeek(Calendar.MONDAY);
			gc.add(Calendar.DATE, Calendar.MONDAY - gc.get(Calendar.DAY_OF_WEEK));
			gc.set(Calendar.HOUR, 1);
			gc.set(Calendar.MINUTE, 0);
			gc.set(Calendar.SECOND, 0);
			gc.set(Calendar.MILLISECOND, 0);
			final Timestamp today = new Timestamp(gc.getTime().getTime());
			gc.add(Calendar.DATE, 2);
			final Timestamp wednesday = new Timestamp(gc.getTime().getTime());
			gc.add(Calendar.DATE, 2);
			final Timestamp friday = new Timestamp(gc.getTime().getTime());
			gc.add(Calendar.DATE, 2);

			user1 = User.createNewUser("_testPSTran_email1_" + Uuid.create(), context);
			user1.setLastName("_testPSTran_usr1_");
			user2 = User.createNewUser("_testPSTran_email2_" + Uuid.create(), context);
			user2.setLastName("_testPSTran_usr2_");
			work1 = Work.createNew(Work.TYPE, "_testPSTran_wrk1_", user1);
			work2 = Work.createNew(Work.TYPE, "_testPSTran_wrk2_", user1);

			ac = ActivityCodes.get(context);
			final String tagName1 = "_testPSTran_actag1_";
			final String tagName2 = "_testPSTran_actag2_";
			ac.addTag(tagName1);
			ac.addTag(tagName2);
			fake_tag_id1 = ac.getTag(tagName1).getId();
			fake_tag_id2 = ac.getTag(tagName2).getId();
			ac.save(conn);
			conn.commit();

			user1.save(conn);
			conn.commit();
			user2.save(conn);
			conn.commit();
			work1.save(conn);
			conn.commit();
			work2.save(conn);
			conn.commit();

			tr1w1ac1 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr1w1ac1.setTransactionDate(today);
			tr1w1ac1.setActivityId(fake_tag_id1);
/*			tr1w1ac1_1 = PSTransaction.createTimeEntry( new Float( 2 ),
			                                            user1, work1 );
			tr1w1ac1_1.setTransactionDate( today );
			tr1w1ac1_1.setActivityId( fake_tag_id1 );
*/
			tr2w1 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2w1.setTransactionDate(wednesday);
			tr2w1ac1 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr2w1ac1.setTransactionDate(wednesday);
			tr2w1ac1.setActivityId(fake_tag_id1);

			tr3 = null;//PSTransaction.createTimeEntry(new Double(2), user2, work1);
			tr3.setTransactionDate(friday);
			tr3w1ac2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
			tr3w1ac2.setTransactionDate(friday);
			tr3w1ac2.setActivityId(fake_tag_id2);
			tr3w2ac2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work2);
			tr3w2ac2.setTransactionDate(friday);
			tr3w2ac2.setActivityId(fake_tag_id2);

			tr4w2ac2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work2);
			tr4w2ac2.setTransactionDate(friday);
			tr4w2ac2.setActivityId(fake_tag_id2);

			tr1w1ac1.save(conn);
			//tr1w1ac1_1.save( conn );
			tr2w1.save(conn);
			tr2w1ac1.save(conn);
			tr3.save(conn);
			tr3w1ac2.save(conn);
			tr3w2ac2.save(conn);
			tr4w2ac2.save(conn);

			conn.commit();

			/*with noProject*/
			List res = null;//_peer.getWeekbyUser(user1.getId(), today, firstWeek, conn);
			assertTrue("Expecting " + 5 + " items in result list. got: " + res.size(), res.size() == 5);
			Object[] sumsByDay = new Object[]{new Float(2), null, new Float(2), null, null, null, null};
			assertTrue("Expecting another items in result list.", findResArray(res, work1.getId().toString(), fake_tag_id1.toString(), sumsByDay));
			sumsByDay = new Object[]{null, null, null, null, new Float(2), null, null};
			assertTrue("Expecting another items in result list.", findResArray(res, work1.getId().toString(), fake_tag_id2.toString(), sumsByDay));
			sumsByDay = new Object[]{null, null, new Float(2), null, null, null, null};
			assertTrue("Expecting another items in result list.", findResArray(res, work1.getId().toString(), com.cinteractive.ps3.pstransactions.TimeProject.NO_ACTIVITY.toString(), sumsByDay));
			sumsByDay = new Object[]{null, null, null, null, new Float(4), null, null};
			assertTrue("Expecting another items in result list.", findResArray(res, work2.getId().toString(), fake_tag_id2.toString(), sumsByDay));
		} finally {
			try {
				if (ac != null) {
					if (fake_tag_id1 != null)
						ac.removeTag(fake_tag_id1);
					if (fake_tag_id2 != null)
						ac.removeTag(fake_tag_id2);
					ac.save(conn);
					conn.commit();
				}
			} catch (Exception ignored) {
			}
			delete(user1, conn);
			delete(user2, conn);
			delete(work1, conn);
			delete(work2, conn);
			delete(tr1w1ac1, conn);
			delete(tr1w1ac1_1, conn);
			delete(tr2w1, conn);
			delete(tr2w1ac1, conn);
			delete(tr3, conn);
			delete(tr3w1ac2, conn);
			delete(tr3w2ac2, conn);
			delete(tr4w2ac2, conn);
			conn.close();
		}
	}

	public void testGetWeekbyUser1() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				final Timestamp today = new Timestamp(System.currentTimeMillis());
				try {
				//	_peer.getWeekbyUser(null, today, firstWeek, conn);
					fail("Null user id parameter in getWeekbyUser method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null user id parameter in getWeekbyUser method should throw IllegalArgumentException.");
				}
				try {
				//	_peer.getWeekbyUser(FAKE_ID, null, firstWeek, conn);
					fail("Null day parameter in getWeekbyUser method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null day parameter in getWeekbyUser method should throw IllegalArgumentException.");
				}
				try {
				//	_peer.getWeekbyUser(FAKE_ID, today, firstWeek, null);
					fail("Null Connection parameter in getWeekbyUser method should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null Connection parameter in getWeekbyUser method should throw IllegalArgumentException.");
				}

				List res = null;//_peer.getWeekbyUser(FAKE_ID, today, firstWeek, conn);
				assertNotNull("Expecting empty result list, not null", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				return null;
			}
		}.execute();
	}

	public void testGetWorksByRange() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());
		User user = User.getNobody(context);
		Calendar cal = Calendar.getInstance();
		Timestamp today = new Timestamp(new java.util.Date().getTime());
		cal.setTime(today);
		cal.add(Calendar.DATE, -1);
		Timestamp yesterday = new Timestamp(cal.getTime().getTime());
		cal.add(Calendar.DATE, 2);
		Timestamp tomorrow = new Timestamp(cal.getTime().getTime());
		Set res = null;

		conn.setAutoCommit(false);

		try {

			Work work1 = Work.createNew(Work.TYPE, "testPStran_wrk1", user);
			work1.save(conn);

			PSTransaction tr = null;//PSTransaction.createTimeEntry(new Double(1), user, work1);
			_peer.insert(tr, conn);
			final Statement stmt = conn.createStatement();
			stmt.execute("UPDATE PSTransaction set approval_date={ts '" + today.toString() + "'} where transaction_id='" + tr.getId().toString() + "'");

			try {
				//res = _peer.getWorksByRange(yesterday, tomorrow, conn, null);
				fail("Null PersistentKey parameter in getWorksByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getWorksByRange method should throw IllegalArgumentException.");
			}


			try {
				//res = _peer.getWorksByRange(null, tomorrow, conn, context.getId());
				fail("Null Start Date parameter in getWorksByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Start Date parameter in getWorksByRange method should throw IllegalArgumentException.");
			}

			try {
				//res = _peer.getWorksByRange(yesterday, null, conn, context.getId());
				fail("Null End Date parameter in getWorksByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null End Date parameter in getWorksByRange method should throw IllegalArgumentException.");
			}

			try {
				//res = _peer.getWorksByRange(yesterday, tomorrow, null, context.getId());
				fail("Null Connection parameter in getWorksByRange method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in getWorksByRange method should throw IllegalArgumentException.");
			}

			//res = _peer.getWorksByRange(yesterday, tomorrow, conn, context.getId());
			assertNotNull("Expecting not null Iterator", res);
			assertTrue("Expecting result not empty ", !res.isEmpty());
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}

		// additional test
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.createNewUser("_TestPsTransaction" + Uuid.create(), context);
					user.save(conn);

					final Work work1 = Work.createNew(Work.TYPE, "_TestPSTransactionPeer_WorksByRange_", user);
					work1.save(conn);

					final Work work2 = Work.createNew(Work.TYPE, "_TestPSTransactionPeer_WorksByRange2_", user);
					work2.save(conn);

					Calendar c = Calendar.getInstance();
					Timestamp start = new Timestamp(c.getTime().getTime());
					c.add(Calendar.DAY_OF_YEAR, 1);
					Timestamp end = new Timestamp(c.getTime().getTime());

					final PSTransaction tran1 = null;//PSTransaction.createTimeEntry(new Double(1), user, work1);
					final PSTransaction tran2 = null;//PSTransaction.createTimeEntry(new Double(1), user, work2);
					tran1.save(conn);
					tran2.save(conn);

					Set res = null;//_peer.getWorksByRange(start, end, conn, context.getId());
					assertTrue("Expecting at least 2 entry", res.size() > 1);

					c.add(Calendar.YEAR, 1);
					start = new Timestamp(c.getTime().getTime());
					c.add(Calendar.DAY_OF_YEAR, 1);
					end = new Timestamp(c.getTime().getTime());

					//res = _peer.getWorksByRange(start, end, conn, context.getId());
					assertTrue("Expecting empty result set", res.isEmpty());
				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();
	}

	public void testInsertUpdateDelete() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = getContext();

		conn.setAutoCommit(false);

		try {
			try {
				_peer.insert(null, conn);
				fail("Null Transaction Object in insert method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Transaction Object in insert method should throw IllegalArgumentException.");
			}

			PSTransaction tr = null;//PSTransaction.createTimeEntry(new Double(1), User.getNobody(context), WorkUtil.getNoProject(context));
			try {
				_peer.insert(tr, null);
				fail("Null Connection in insert method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in insert method should throw IllegalArgumentException.");
			}

			_peer.insert(tr, conn);
			PSTransaction tr1 = _peer.findById(tr.getId(), conn);

			assertNotNull("Expecting not null PSTransaction object", tr1);
		} finally {
			conn.rollback();
			conn.setAutoCommit(true);
			conn.close();
		}

		// more expensive test
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work = null;
				User user = null;
				User approver = null;
				PSTransaction tran = null;
				PSTag tag = null;
				try {
					user = User.createNewUser("_TestDeleted_" + Uuid.create(), context);
					user.save(conn);
					conn.commit();

					approver = User.createNewUser("_TeDeleted__appr_" + Uuid.create(), context);
					approver.save(conn);
					conn.commit();

					work = Work.createNew(Work.TYPE, "_TestPSTransaction_workDeleted_", user);
					work.save(conn);
					conn.commit();

					tran = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work);
					tran.setDescription("new description");
					tran.getTransactionDate().setNanos(0);
					tran.save(conn); //inserting
					conn.commit();

					PSTransaction test = _peer.findById(tran.getId(), conn);
					assertNotNull("Expecting transaction", test);
					assertTrue("Expecting valid data in transaction", compare(tran, test));

					Calendar c = Calendar.getInstance();
					c.add(Calendar.MINUTE, 5);
					Timestamp now = new Timestamp(c.getTime().getTime());
					now.setNanos(0);

					tran.setApprovedById(approver.getId());
					tran.setTransactionDate(now);
					tran.setAmount(new Double(7.7));
					tran.setDescription("update description123");
					tran.setSubmitDate(now);
					tran.setPostingDate(now);

					ActivityCodes codes = ActivityCodes.get(context);
					tag = codes.addTag("_TestPSTransaction_insert_");
					codes.save(conn);
					conn.commit();

					tran.setActivityId(tag.getId());
					tran.setWasProcessed(new Boolean(tran.getWasProcessed() == null || !tran.getWasProcessed().booleanValue()));
					tran.setAmount_h(new Double(5.6));
					tran.save(conn); //update
					conn.commit();

					test = _peer.findById(tran.getId(), conn);
					assertNotNull("Expecting transaction", test);
					assertTrue("Expecting valid data in transaction", compare(tran, test));

					// delete the transaction
					_peer.delete(tran, conn);
					conn.commit();
					tran = null;

					test = _peer.findById(test.getId(), conn);
					assertNull("Expecting no transactions with given id", test);
				} finally {
					delete(tran, conn);
					delete(work, conn);
					delete(user, conn);
					delete(approver, conn);
					delete(tag, conn);
				}
				return null;
			}
		}.execute();

	}

	public void testIsTimesheetRejected() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = getContext();
		final int firstWeek = context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		User nobody = User.getNobody(context);
		final Timestamp today = new Timestamp(new java.util.Date().getTime());
		today.setNanos(0);

		try {
			try {
				//_peer.isTimesheetRejected(null, today, firstWeek, conn);
				fail("Null PersistentKey parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			}

			try {
				//_peer.isTimesheetRejected(nobody.getId(), null, firstWeek, conn);
				fail("Null Timestamp parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			}

			try {
				//_peer.isTimesheetRejected(nobody.getId(), today, firstWeek, null);
				fail("Null Connection parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in IsTimesheetRejected method should throw IllegalArgumentException.");
			}

			//_peer.isTimesheetRejected(nobody.getId(), today, firstWeek, conn);
		} finally {
			conn.close();
		}

		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				Work work = null;
				PSTransaction tran = null;
				ObjectEvent ev = null;
				conn.setAutoCommit(false);
				try {
					// create new User
					user = User.createNewUser("_TestPSTransaction" + Uuid.create(), context);
					user.save(conn);

					// create new work for ps transaction
					work = Work.createNew(Work.TYPE, "_TestPSTransaction_IsTimesheetRejected_", user);
					work.save(conn);

					// create new ps transaction
					final Timestamp now = new Timestamp(System.currentTimeMillis());
					tran = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work);
					tran.save(conn);

					// restore transaction from database and check our values
					PSTransaction test = _peer.findById(tran.getId(), conn);
					assertNotNull("Expecting transaction", test);

					boolean is = false;//_peer.isTimesheetRejected(user.getId(), now, firstWeek, conn);
					assertTrue("Expecting false value", !is);


					Calendar c = Calendar.getInstance();
					while (c.get(Calendar.DAY_OF_WEEK) < Calendar.MONDAY)
						c.add(Calendar.DAY_OF_WEEK, 1);
					Date day = c.getTime();

					Map params = new HashMap();
					params.put(TimeRejectedEvent.WEEK_CODE, day);

					ev = TimeRejectedEvent.create(TimeRejectedEvent.TYPE, user, params);
					ev.save(conn);

					//is = _peer.isTimesheetRejected(user.getId(), now, firstWeek, conn);
					assertTrue("Expecting true value", is);
				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();
	}

	public void testGetIsTimesheetSubmit() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = getContext();
		User nobody = User.getNobody(context);
		final Timestamp today = new Timestamp(new java.util.Date().getTime());
		today.setNanos(0);

		try {
			try {
				//_peer.getIsTimesheetSubmit(null, today, "day", conn);
				fail("Null PersistentKey parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			}

			try {
				//_peer.getIsTimesheetSubmit(nobody.getId(), null, "day", conn);
				fail("Null Timestamp parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Timestamp parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			}

			try {
				//_peer.getIsTimesheetSubmit(nobody.getId(), today, null, conn);
				fail("Null String parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null String parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			}

			try {
				//_peer.getIsTimesheetSubmit(nobody.getId(), today, "day", null);
				fail("Null Connection parameter in getIsTimesheetSubmit method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter in IsTimesheetSubmit method should throw IllegalArgumentException.");
			}

			//_peer.getIsTimesheetSubmit(nobody.getId(), today, "day", conn);
		} finally {
			conn.close();
		}

		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				Work work = null;
				PSTransaction tran = null;
				try {
					// create new User
					user = User.createNewUser("_TestPSTransaction" + Uuid.create(), context);
					user.save(conn);
					conn.commit();

					// create new work for ps transaction
					work = Work.createNew(Work.TYPE, "_TestPSTransaction_getIsTimesheetSubmit_", user);
					work.save(conn);
					conn.commit();

					// create new ps transaction
					final Timestamp now = new Timestamp(System.currentTimeMillis());

					Boolean is =false;//= _peer.getIsTimesheetSubmit(user.getId(), now, "day", conn);
					assertTrue("Expecting false result", !Boolean.TRUE.equals(is));

					tran = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work);
					tran.save(conn);
					conn.commit();

					// restore transaction from database and check our values
					PSTransaction test = _peer.findById(tran.getId(), conn);
					assertNotNull("Expecting transaction", test);
					assertTrue("Expecting valid submitter", user.getId().equals(test.getSubmittedBy()));

					//is = _peer.getIsTimesheetSubmit(user.getId(), now, "day", conn);
					assertNotNull("Expecting not null result", is);
					assertTrue("Expecting false value", !Boolean.TRUE.equals(is));

					tran.setSubmitDate(now);
					tran.save(conn);
					conn.commit();

					//is = _peer.getIsTimesheetSubmit(user.getId(), now, "day", conn);
					assertNotNull("Expecting not null result", is);
					assertTrue("Expecting true value", Boolean.TRUE.equals(is));
				} finally {
					delete(tran, conn);
					delete(work, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();

	}

	public void testFindWaitingForApproval() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				Work work = null;
				Work work1 = null;
				PSTransaction tran = null;
				PSTransaction tran1 = null;
				try {
					final Timestamp now = new Timestamp(System.currentTimeMillis());
					Map tags = new TreeMap();

					List res = _peer.findWaitingForApproval(context.getId(), WorkUtil.getNoProject(context).getId(), conn, now, tags, firstWeek);
					int baseCount = res.size();

					// create new User
					user = User.createNewUser("_TestPSTransaction_finpproval_" + Uuid.create(), context);
					user.save(conn);
					conn.commit();

					// create new work for ps transaction
					work = Work.createNew(Work.TYPE, "_TestPSTransaction_findWaitingForApproval_", user);
					work.save(conn);
					conn.commit();

					// create second work for ps transaction
					work1 = Work.createNew(Work.TYPE, "_TestPSTransaction_findWaitingForApproval1_", user);
					work1.save(conn);
					conn.commit();

					// create new ps transaction
					tran = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user, work);
					tran.setAmount_h(new Double(50.5));
					tran.save(conn);
					conn.commit();

					tran.setSubmitDate(now);
					tran.save(conn);
					conn.commit();

					// restore transaction from database and check our values
					PSTransaction test = _peer.findById(tran.getId(), conn);
					assertNotNull("Expecting transaction", test);
					assertTrue("Expecting valid submitter", user.getId().equals(test.getSubmittedBy()));

					res = _peer.findWaitingForApproval(context.getId(), WorkUtil.getNoProject(context).getId(), conn, now, tags, firstWeek);
					assertTrue("Expecting another result in result set; got " + res.size(), res.size() == (baseCount + 1));
					Object os[] = (Object[]) res.get(0);
					assertTrue("Expecting valid user id in result set", os[0].equals(user.getId()));
					assertTrue("Expecting valid amount in result set", os[1].equals(new Float(5.5)));
					assertTrue("Expecting valid amount in result set", os[2].equals(new Float(50.5)));
					//assertTrue( "Expecting valid project count in result set", os[ 3 ].equals( new Long( 1 ) ) );

					// create second ps transaction
					tran1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user, work1);
					tran1.setAmount_h(new Double(50));
					tran1.save(conn);
					conn.commit();

					tran1.setSubmitDate(now);
					tran1.save(conn);
					conn.commit();

					res = _peer.findWaitingForApproval(context.getId(), WorkUtil.getNoProject(context).getId(), conn, now, tags, firstWeek);
					assertTrue("Expecting 2 results in result set; got " + res.size(), res.size() == (baseCount + 2));
					os = (Object[]) res.get(0);
					Object os1[] = (Object[]) res.get(1);
					assertTrue("Expecting valid user id in result set", os[0].equals(user.getId()));
					assertTrue("Expecting valid amount in result set", os[1].equals(new Float(10.5 - ((Float) os1[1]).floatValue())));
					assertTrue("Expecting valid amount in result set", os[2].equals(new Float(100.5 - ((Float) os1[2]).floatValue())));

				} finally {
					delete(tran1, conn);
					delete(work1, conn);
					delete(tran, conn);
					delete(work, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();

	}

	public void testUserDeleted() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());

		try {

			try {
				_peer.userDeleted(null, conn);
				fail("Null PersistentKey parameter should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter should throw IllegalArgumentException.");
			}

			try {
				_peer.userDeleted(FAKE_ID, null);
				fail("Null Connection parameter should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection parameter should throw IllegalArgumentException.");
			}

			_peer.userDeleted(FAKE_ID, conn);
		} finally {
			conn.close();
		}

		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				User approver = null;
				Work work = null;
				PSTransaction tran = null;
				try {
					// create new User
					user = User.createNewUser("_TestPStion_userDeleted_" + Uuid.create(), context);
					user.save(conn);
					conn.commit();

					// create approver user
					approver = User.createNewUser("_TestPSTrauserDeleted_appr_" + Uuid.create(), context);
					approver.save(conn);
					conn.commit();

					// create new work for ps transaction
					work = Work.createNew(Work.TYPE, "_TestPSTransaction_userDeleted_", user);
					work.save(conn);
					conn.commit();

					// create new ps transaction
					tran = null;//PSTransaction.createCostEstimate(Double.valueOf("5.5"), user, work);
					tran.save(conn);
					conn.commit();

					// set the approver
					tran.setApprovedById(approver.getId());
					tran.save(conn);
					conn.commit();

					// restore transaction from database and check our values
					List trs = null;//_peer.findByProject(work.getId(), false, PSTransaction.ESTIMATED_COST_TYPES, null, null, conn);
					assertTrue("Expecting estimate for project id", trs.size() == 1);
					PSTransaction test = (PSTransaction) trs.toArray()[0];
					assertTrue("Expecting valid submitter", user.getId().equals(test.getSubmittedBy()));
					assertTrue("Expecting valid approver", approver.getId().equals(test.getApprovedBy()));

					// remove the submitter (poster)
					_peer.userDeleted(user.getId(), conn);
					conn.commit();

					//trs = _peer.findByProject(work.getId(), false, PSTransaction.ESTIMATED_COST_TYPES, null, null, conn);
					assertTrue("Expecting estimate for project id", trs.size() == 1);
					test = (PSTransaction) trs.toArray()[0];
					assertTrue("Expecting valid submitter", DeletedUser.get(context).getId().equals(test.getSubmittedBy()));
					assertTrue("Expecting valid approver", approver.getId().equals(test.getApprovedBy()));

					// remove the approver
					_peer.userDeleted(approver.getId(), conn);
					conn.commit();

					//trs = _peer.findByProject(work.getId(), false, PSTransaction.ESTIMATED_COST_TYPES, null, null, conn);
					assertTrue("Expecting estimate for project id", trs.size() == 1);
					test = (PSTransaction) trs.toArray()[0];
					assertTrue("Expecting valid submitter", DeletedUser.get(context).getId().equals(test.getSubmittedBy()));
					assertTrue("Expecting valid approver", DeletedUser.get(context).getId().equals(test.getApprovedBy()));
				} finally {
					delete(tran, conn);
					delete(work, conn);
					delete(user, conn);
					delete(approver, conn);
				}
				return null;
			}
		}.execute();
	}

	private void err(String attrName) {
		System.err.println("Invalid '" + attrName + "' attribute value");
	}

	private boolean equals(PersistentKey key1, PersistentKey key2) {
		return key1 == null && key2 == null || key1 != null && key1.equals(key2);
	}

	private boolean equals(Object o1, Object o2) {
		return o1 == null && o2 == null || o1 != null && o1.equals(o2);
	}

	private final boolean compare(PSTransaction src, PSTransaction dest) {
		boolean res = true;
		if (res && !src.getTransactionDate().equals(dest.getTransactionDate()) && !(res = false)) {
			err("Transaction date");
		}
		if (res && !src.getType().equals(dest.getType()) && !(res = false)) {
			err("Transaction type");
		}
		if (res && !src.getWorkId().equals(dest.getWorkId()) && !(res = false)) {
			err("Project id");
		}
		if (res && !src.getAmount().equals(dest.getAmount()) && !(res = false)) {
			err("Transaction Amount");
		}
		if (res && !src.getDescription().equals(dest.getDescription()) && !(res = false)) {
			err("Description");
		}
		if (res && !equals(src.getSubmittedBy(), dest.getSubmittedBy()) && !(res = false)) {
			err("Submitted by");
		}
		if (res && !equals(src.getSubmitDate(), dest.getSubmitDate()) && !(res = false)) {
			err("Submitted date");
		}
		if (res && !equals(src.getApprovedBy(), dest.getApprovedBy()) && !(res = false)) {
			err("Approved by");
		}
		if (res && !equals(src.getApprovalDate(), dest.getApprovalDate()) && !(res = false)) {
			err("Aproved date");
		}
		if (res && !equals(src.getPostedBy(), dest.getPostedBy()) && !(res = false)) {
			err("Posted by");
		}
		if (res && !equals(src.getPostingDate(), dest.getPostingDate()) && !(res = false)) {
			err("Posted date");
		}
		if (res && !equals(src.getApprovedBy(), dest.getApprovedBy()) && !(res = false)) {
			err("Approved by");
		}
		if (res && !equals(src.getActivityId(), dest.getActivityId()) && !(res = false)) {
			err("Activity");
		}
		if (res && !equals(src.getWasProcessed(), dest.getWasProcessed()) && !(res = false)) {
			err("Was processed");
		}
		if (res && !equals(src.getAmount_h(), dest.getAmount_h()) && !(res = false)) {
			err("Amount h");
		}
		return res;
	}

	public void testFindApproved() throws Exception {
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Map tagSets = new java.util.HashMap();
				long base = System.currentTimeMillis();
				Calendar gc = Calendar.getInstance();
				gc.setTime(new Date(base));
				if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
					base += 2L * 24L * 60L * 60L * 1000L;
				/*empty cases*/
				try {
					_peer.findApproved(null, FAKE_ID, conn, new Timestamp(base), tagSets);
					fail("Null ContextId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null ContextId should throw IllegalArgumentException.");
				}
				try {
					_peer.findApproved(FAKE_ID, null, conn, new Timestamp(base), tagSets);
					fail("Null NoProjectId should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null NoProjectId should throw IllegalArgumentException.");
				}
				try {
					_peer.findApproved(FAKE_ID, FAKE_ID, null, new Timestamp(base), tagSets);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}
				try {
					_peer.findApproved(FAKE_ID, FAKE_ID, conn, null, tagSets);
					fail("Null firstDay should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null firstDay should throw IllegalArgumentException.");
				}
				/*fake context*/
				List res = _peer.findApproved(FAKE_ID, FAKE_ID, conn, new Timestamp(base), tagSets);
				assertNotNull("Expecting empty result list, not null.", res);
				assertTrue("Expecting empty result list. got: " + res.size(), res.isEmpty());

				/*creating test objects*/
				TagSet tagset1 = TagSet.createNew("testPSTran_ts1_34", context);
				tagset1.addLinkableType(User.TYPE);
				tagset1.addTag("testPSTran_tag11");
				tagset1.addTag("testPSTran_tag12");
				TagSet tagset2 = TagSet.createNew("testPSTran_ts234", context);
				tagset2.addLinkableType(User.TYPE);
				tagset2.addTag("testPSTran_tag21");
				tagset2.addTag("testPSTran_tag22");

				User user1 = User.createNewUser("testPSTran_usr1" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran_usr2" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				Work work3 = Work.createNew(Work.TYPE, "testPSTran_wrk3", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user1, work1);
				tr1.setAmount_h(Double.valueOf("10.5"));
				tr1.setTransactionDate(new Timestamp(base));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user1, work2);
				tr2.setAmount_h(Double.valueOf("10"));
				tr2.setTransactionDate(new Timestamp(base));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user2, work1);
				tr3.setAmount_h(Double.valueOf("10.5"));
				tr3.setTransactionDate(new Timestamp(base));

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user2, work2);
				tr4.setAmount_h(Double.valueOf("10"));
				tr4.setTransactionDate(new Timestamp(base));

				final Calendar calendar = (Calendar) Calendar.getInstance().clone();
				calendar.setTime(new Date(base));
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.MILLISECOND, 0);
				if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					calendar.add(Calendar.DATE, -7);
				}
				calendar.setFirstDayOfWeek(Calendar.MONDAY);
				calendar.add(Calendar.DATE, Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK));
				try {
					/*saving test objects*/
					user1.save(conn);
					user2.save(conn);
					tagset1.save(conn);
					tagset2.save(conn);
					conn.commit();

					try {
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset2.getTag("testPSTran_tag21"), null);
						user1.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
						user2.changeTag(TagEventCode.ADD_TAG_CODE, tagset1.getTag("testPSTran_tag11"), null);
					} catch (InvalidTagEventException itee) {
						fail(itee.getError());
					}
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					work1.save(conn);
					work2.save(conn);
					work3.save(conn);
					conn.commit();

					int baseCount = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets).size();

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					/*Tests*/
					/*don't  approved*/
					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting " + (baseCount) + " items in result list. got: " + res.size(), baseCount == res.size());
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user1.getId(), 0));
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user2.getId(), 0));

					/*transactions submitted*/
					tr1.setSubmitDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr2.setSubmitDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr1.save(conn);
					tr2.save(conn);
					conn.commit();

					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting " + (baseCount) + " items in result list. got: " + res.size(), baseCount == res.size());
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user1.getId(), 0));
					assertTrue("Expecting another items in result list.", !findIdInResArray(res, user2.getId(), 0));

					/*transactions approved*/
					tr1.setApprovalDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr1.setApprovedById(user2.getId());
					tr2.setApprovalDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr2.setApprovedById(user2.getId());
					tr3.setApprovalDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr3.setApprovedById(user2.getId());
					tr4.setApprovalDate(new Timestamp(base - 24L * 60L * 60L * 1000L));
					tr4.setApprovedById(user2.getId());
					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets);

					assertTrue("Expecting " + (baseCount + 4) + " items in result list. got: " + res.size(), baseCount + 4 == res.size());
					assertTrue("Expecting another items in result list.", findResArray(res, user1.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L));
					assertTrue("Expecting another items in result list.", findResArray(res, user2.getId(), Float.valueOf("10.5"), Float.valueOf("20.5"), 2L));

					/*with tagset map*/
					tagSets.put(tagset1.getId(), null);
					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));

					tagSets.put(tagset2.getId(), null);
					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

					tagSets.put(tagset1.getId(), tagset1.getTag("testPSTran_tag11").getId());
					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting 4 items in result set. got: " + res.size(), 4 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && findResArray(res, user2.getId(), new Float(10.5), new Float(20.5), 2));
					tagSets.clear();

					tagSets.put(tagset2.getId(), tagset2.getTag("testPSTran_tag21").getId());
					res = _peer.findApproved(contextId, work3.getId(), conn, new Timestamp(base - 1L * 24L * 60L * 60L * 1000L), tagSets);
					assertTrue("Expecting 2 items in result set. got: " + res.size(), 2 == res.size());
					assertTrue("Expecting another items in result set.", findResArray(res, user1.getId(), new Float(10.5), new Float(20.5), 2) && !findIdInResArray(res, user2.getId(), 0));
					tagSets.clear();

				} finally {
					/*deleting test objects*/
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(work3, conn);
					delete(user1, conn);
					delete(user2, conn);
					delete(tagset1, conn);
					delete(tagset2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testGetSubmittedCountMonthbyUser() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();
				Calendar gc = Calendar.getInstance();
				gc.setTime(new Date(base));
				if (gc.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
					base += 2L * 24L * 60L * 60L * 1000L;
				/*empty cases*/
				try {
					//_peer.getSubmittedCountMonthbyUser(null, new Timestamp(base), conn);
					fail("Null user Id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null user Id should throw IllegalArgumentException.");
				}
				try {
					//_peer.getSubmittedCountMonthbyUser(FAKE_ID, null, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					//_peer.getSubmittedCountMonthbyUser(FAKE_ID, new Timestamp(base), null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				/*fake user*/
				Double res = 0.0;//_peer.getSubmittedCountMonthbyUser(FAKE_ID, new Timestamp(base), conn);

				assertTrue("Expecting not null value for fake user", res != null);
				assertTrue("Expecting 0 value for fake user", res.intValue() == 0);

				User user1 = User.createNewUser("_testPSTranPeer_GSCMBU_usr1" + Uuid.create(), context);
				Work work1 = Work.createNew(Work.TYPE, "_testPSTranPeer_GSCMBU_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "_testPSTranPeer_GSCMBU_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work1);
				tr1.setAmount_h(Double.valueOf("10.5"));
				tr1.setTransactionDate(new Timestamp(base));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work2);
				tr2.setAmount_h(Double.valueOf("10"));
				tr2.setTransactionDate(new Timestamp(base));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work1);
				tr3.setAmount_h(Double.valueOf("10.5"));
				tr3.setTransactionDate(new Timestamp(base + 3L * 7L * 24L * 60L * 60L * 1000L));

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work2);
				tr4.setAmount_h(Double.valueOf("10"));
				tr4.setTransactionDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));

				try {
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();

					// existing user without timesheets
					//res = _peer.getSubmittedCountMonthbyUser(user1.getId(), new Timestamp(base), conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting 0 value for user", res.intValue() == 0);

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					// existing user with not submitted timesheets
					//res = _peer.getSubmittedCountMonthbyUser(user1.getId(), new Timestamp(base), conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting 0 value for user", res.intValue() == 0);

					tr1.setSubmitDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));
					tr2.setSubmitDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));
					tr3.setSubmitDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));
					tr4.setSubmitDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					// existing user with submitted timesheets
					//res = _peer.getSubmittedCountMonthbyUser(user1.getId(), new Timestamp(base), conn);
					assertTrue("Expecting not null value for fake user", res != null);
					assertTrue("Expecting another value for fake user", res.intValue() == 41);

					// existing user with submitted timesheets - another period
					//res = _peer.getSubmittedCountMonthbyUser(user1.getId(), new Timestamp(base + 2L * 7L * 24L * 60L * 60L * 1000L), conn);
					assertTrue("Expecting not null value for fake user", res != null);
					assertTrue("Expecting another value for fake user", res.floatValue() == (new Float("20.5")).floatValue());
				} finally {
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
				}

				return null;
			}
		}.execute();
	}


	public void testGetMonthbyUser() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();

				Calendar cal = (Calendar) Calendar.getInstance().clone();
				cal.setTime(new Date(base));
				int Year = cal.get(Calendar.YEAR);
				int Month = cal.get(Calendar.MONTH);
				int Day = cal.get(Calendar.DATE);
				cal.set(Year, Month, Day, 0, 0, 0);//current
				cal.set(Calendar.MILLISECOND, 0);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, -7);
				}
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.add(Calendar.DATE, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
				base = cal.getTime().getTime();

				/*empty cases*/
				try {
					//_peer.getMonthbyUser(null, new Timestamp(base), firstWeek, conn);
					fail("Null user Id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null user Id should throw IllegalArgumentException.");
				}
				try {
					//_peer.getMonthbyUser(FAKE_ID, null, firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					//_peer.getMonthbyUser(FAKE_ID, new Timestamp(base), firstWeek, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				/*fake user*/
				List res = null;//_peer.getMonthbyUser(FAKE_ID, new Timestamp(base), firstWeek, conn);

				assertTrue("Expecting not null value for fake user", res != null);
				assertTrue("Expecting 0 value for fake user", res.size() == 0);

				User user1 = User.createNewUser("_testPSTranPeer_GMBU_usr1" + Uuid.create(), context);
				Work work1 = Work.createNew(Work.TYPE, "_testPSTranPeer_GMBU_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "_testPSTranPeer_GMBU_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("10.5"), user1, work1);
				tr1.setAmount_h(Double.valueOf("1"));
				tr1.setTransactionDate(new Timestamp(base));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("10"), user1, work2);
				tr2.setAmount_h(Double.valueOf("3"));
				tr2.setTransactionDate(new Timestamp(base));
				tr2.setSubmitDate(new Timestamp(base + 22L * 24L * 60L * 60L * 1000L));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("10.5"), user1, work1);
				tr3.setAmount_h(Double.valueOf("5"));
				tr3.setTransactionDate(new Timestamp(base + 3L * 7L * 24L * 60L * 60L * 1000L));
				tr2.setSubmitDate(new Timestamp(base + 22L * 24L * 60L * 60L * 1000L));
				tr2.setApprovalDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));
				tr2.setApprovedById(user1.getId());

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("10"), user1, work2);
				tr4.setAmount_h(Double.valueOf("7"));
				tr4.setTransactionDate(new Timestamp(base + 34L * 24L * 60L * 60L * 1000L));

				try {
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();

					//res = _peer.getMonthbyUser(user1.getId(), new Timestamp(base), firstWeek, conn);

					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting empty list for user", res.size() == 0);

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					//res = _peer.getMonthbyUser(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting not null list for user", res != null);
					assertTrue("Expecting another list size for user. Expecting 4, got: " + res.size(), res.size() == 4);
					Object[] sumsByDay = new Object[]{new Float(10.5), null, null, new Float(10.5), null, null, null};
					assertTrue("Expecting another values for user", findResArrayM(res, work1.getId().toString(), sumsByDay, new Timestamp(base)));

					sumsByDay = new Object[]{new Float(10), null, null, null, new Float(10), null, null};
					assertTrue("Expecting another values for user", findResArrayM(res, work2.getId().toString(), sumsByDay, new Timestamp(base)));

				} finally {
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
				}

				return null;
			}
		}.execute();
	}

	public void testGetWeeksInMonth() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();

				Map tagMap = new HashMap();

				Calendar cal = (Calendar) Calendar.getInstance().clone();
				cal.setTime(new Date(base));
				int Year = cal.get(Calendar.YEAR);
				int Month = cal.get(Calendar.MONTH);
				int Day = cal.get(Calendar.DATE);
				cal.set(Year, Month, Day, 0, 0, 0);//current
				cal.set(Calendar.MILLISECOND, 0);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, -7);
				}
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.add(Calendar.DATE, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
				base = cal.getTime().getTime();

				/*empty cases*/
				try {
					_peer.getWeeksInMonth(null, tagMap, contextId, firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					_peer.getWeeksInMonth(new Timestamp(base), null, contextId, firstWeek, conn);
					fail("Null tags map should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null tags map should throw IllegalArgumentException.");
				}
				try {
					_peer.getWeeksInMonth(new Timestamp(base), tagMap, null, firstWeek, conn);
					fail("Null context id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null context id should throw IllegalArgumentException.");
				}
				try {
					_peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				//fake context id

				List res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, FAKE_ID, firstWeek, conn);


				assertTrue("Expecting not null value for fake user", res1 != null);
				assertTrue("Expecting 0 value for fake user", res1.size() == 0);
				//creating test objects
				TagSet tagset1 = TagSet.createNew("DDts1", context);
				tagset1.addLinkableType(User.TYPE);
				tagset1.addTag("tag111");
				tagset1.addTag("tag122");
				TagSet tagset2 = TagSet.createNew("DDts2", context);
				tagset2.addLinkableType(User.TYPE);
				tagset2.addTag("tag211");
				tagset2.addTag("tag222");

				User user1 = User.createNewUser("testPSTran_usr1" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran_usr2" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("1.5"), user1, work1);
				tr1.setAmount_h(Double.valueOf("10"));
				tr1.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user2, work2);
				tr2.setAmount_h(Double.valueOf("15"));
				tr2.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("3.5"), user1, work2);
				tr3.setAmount_h(Double.valueOf("20"));
				tr3.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("3"), user2, work1);
				tr4.setAmount_h(Double.valueOf("25"));
				tr4.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr5 = null;//PSTransaction.createTimeEntry(Double.valueOf("5.5"), user1, work1);
				tr5.setAmount_h(Double.valueOf("30"));
				tr5.setTransactionDate(new Timestamp(base + 30L * 24L * 60L * 60L * 1000L));

				PSTransaction tr6 = null;//PSTransaction.createTimeEntry(Double.valueOf("5"), user2, work2);
				tr6.setAmount_h(Double.valueOf("35"));
				tr6.setTransactionDate(new Timestamp(base + 30L * 24L * 60L * 60L * 1000L));

				try {
					res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, conn);
					//res =  PSTransaction.getWeeksInMonth( new Timestamp( base ), sess, tagMap );
					int baseCount = res1.size();
					int baseCount2 = _peer.getWeeksInMonth(new Timestamp(base + 14L * 24L * 60L * 60L * 1000L), tagMap, contextId, firstWeek, conn).size();
					user1.save(conn);
					user2.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();
					tagset1.save(conn);
					tagset2.save(conn);
					conn.commit();

					// before transactions saved
					res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, conn);
					assertTrue("Expecting another items count in result list. difference = " + (res1.size() - (baseCount + 2)), res1.size() == (baseCount + 2));

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					tr5.save(conn);
					tr6.save(conn);
					conn.commit();

					try {
						user1.changeTag(TagEventCode.CHANGE_TAG_CODE, tagset1.getTag("tag111"), null);
						user2.changeTag(TagEventCode.CHANGE_TAG_CODE, tagset2.getTag("tag222"), null);
					} catch (InvalidTagEventException itee) {
						fail("error when changing user tag + " + itee.getMessage());
					}

					user1.save(conn);
					user2.save(conn);
					conn.commit();

					// empty tags map
					res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, conn);
					assertTrue("Expecting another items count in result list. difference = " + (res1.size() - (baseCount + 6)), res1.size() == (baseCount + 6));
					//assertTrue("Expecting both test users in result map", res.keySet().contains(user1.getId().toString())
					//        && res.keySet().contains(user2.getId().toString()) );

					// time = base + 2 weeks
					res1 = _peer.getWeeksInMonth(new Timestamp(base + 14L * 24L * 60L * 60L * 1000L), tagMap, contextId, firstWeek, conn);
					//res =  PSTransaction.getWeeksInMonth( new Timestamp( base + 14L * 24L * 60L * 60L * 1000L  ), sess, tagMap );
					assertTrue("Expecting another items count in result list. difference = " + (res1.size() - (baseCount2 + 4)), res1.size() == (baseCount2 + 4));

					// not empty tags map
					tagMap.put(tagset1.getId(), tagset1.getTag("tag111").getId().toString());
					res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, conn);
					assertTrue("Expecting another items count in result list. difference = " + (res1.size() - 3), res1.size() == 3);

					tagMap.put(tagset2.getId(), tagset2.getTag("tag222").getId().toString());
					res1 = _peer.getWeeksInMonth(new Timestamp(base), tagMap, contextId, firstWeek, conn);
					assertTrue("Expecting another items count in result list. difference = " + res1.size(), res1.size() == 0);
				} finally {
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(tr5, conn);
					delete(tr6, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
					delete(user2, conn);
					delete(tagset1, conn);
					delete(tagset2, conn);
				}

				return null;
			}
		}.execute();
	}

	public void testGetTimesheetRejectedDate() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();

				Calendar cal = (Calendar) Calendar.getInstance().clone();
				cal.setTime(new Date(base));
				int Year = cal.get(Calendar.YEAR);
				int Month = cal.get(Calendar.MONTH);
				int Day = cal.get(Calendar.DATE);
				cal.set(Year, Month, Day, 0, 0, 0);//current
				cal.set(Calendar.MILLISECOND, 0);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, -7);
				}
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.add(Calendar.DATE, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
				base = cal.getTime().getTime();

				/*empty cases*/
				try {
					//_peer.getTimesheetRejectedDate(null, new Timestamp(base), firstWeek, conn);
					fail("Null user id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null user id should throw IllegalArgumentException.");
				}
				try {
					//_peer.getTimesheetRejectedDate(FAKE_ID, null, firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					//_peer.getTimesheetRejectedDate(FAKE_ID, new Timestamp(base), firstWeek, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				/*fake user id*/
				Timestamp res = null;//_peer.getTimesheetRejectedDate(FAKE_ID, new Timestamp(base), firstWeek, conn);

				assertTrue("Expecting null value for fake user", res == null);

				User user1 = User.createNewUser("testPSTran_usr1" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran_usr2" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");

				ObjectEvent oe1 = null;
				ObjectEvent oe2 = null;

				try {
					user1.save(conn);
					user2.save(conn);
					conn.commit();

					// before transactions saved
					//res = _peer.getTimesheetRejectedDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);

					final Map props = new HashMap();
					props.put(TimeRejectedEvent.COMMENT_CODE, "comment");
					props.put(TimeRejectedEvent.WEEK_CODE, new Date(base));
					oe1 = ObjectEvent.create(TimeRejectedEvent.TYPE, user1, props, user2.getId());
					user1.addSaveEvent(oe1);
					user1.save(conn);
					conn.commit();

					//res = _peer.getTimesheetRejectedDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting current time", (System.currentTimeMillis() - res.getTime()) < 5000);

					props.clear();
					props.put(TimeRejectedEvent.COMMENT_CODE, "comment");
					props.put(TimeRejectedEvent.WEEK_CODE, new Date(base + 14L * 24L * 60L * 60L * 1000L));
					oe2 = ObjectEvent.create(TimeRejectedEvent.TYPE, user1, props, user2.getId());
					user1.addSaveEvent(oe2);
					user1.save(conn);
					conn.commit();

					//res = _peer.getTimesheetRejectedDate(user1.getId(), new Timestamp(base + 14L * 24L * 60L * 60L * 1000L), firstWeek, conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting current time", (System.currentTimeMillis() - res.getTime()) < 5000);

				} finally {
					delete(oe1, conn);
					delete(oe2, conn);
					delete(user1, conn);
					delete(user2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testGetTimesheetAprovalDate() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek= context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();

				Calendar cal = (Calendar) Calendar.getInstance().clone();
				cal.setTime(new Date(base));
				int Year = cal.get(Calendar.YEAR);
				int Month = cal.get(Calendar.MONTH);
				int Day = cal.get(Calendar.DATE);
				cal.set(Year, Month, Day, 0, 0, 0);//current
				cal.set(Calendar.MILLISECOND, 0);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, -7);
				}
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.add(Calendar.DATE, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
				base = cal.getTime().getTime();

				/*empty cases*/
				try {
					_peer.getTimesheetAprovalDate(null, new Timestamp(base), firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					_peer.getTimesheetAprovalDate(FAKE_ID, null, firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					_peer.getTimesheetAprovalDate(FAKE_ID, new Timestamp(base), firstWeek, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				/*fake user id*/
				Timestamp res = null;//_peer.getTimesheetAprovalDate(FAKE_ID, new Timestamp(base), firstWeek, conn);
				assertTrue("Expecting null value for fake user", res == null);

				/*creating test objects*/
				User user1 = User.createNewUser("testPSTran_usr1" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				User user2 = User.createNewUser("testPSTran_usr2" + Uuid.create(), context);
				user2.setLastName("testPSTran_usr2");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("1.5"), user1, work1);
				tr1.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work2);
				tr2.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("3.5"), user1, work2);
				tr3.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("3"), user1, work1);
				tr4.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr5 = null;//PSTransaction.createTimeEntry(Double.valueOf("3.5"), user1, work2);
				tr5.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr6 = null;//PSTransaction.createTimeEntry(Double.valueOf("3"), user1, work1);
				tr6.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				try {
					user1.save(conn);
					user2.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();

					// before transactions saved
					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					// no submitted / approved transactions
					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);

					tr1.setSubmitDate(new Timestamp(base + 24L * 60L * 60L * 1000L));
					tr2.setSubmitDate(new Timestamp(base + 24L * 60L * 60L * 1000L));
					tr3.setSubmitDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr4.setSubmitDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					// no approved transactions
					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);

					tr1.setApprovalDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr1.setApprovedById(user2.getId());
					tr2.setApprovalDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr2.setApprovedById(user2.getId());
					tr3.setApprovalDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));
					tr3.setApprovedById(user2.getId());
					tr4.setApprovalDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));
					tr4.setApprovedById(user2.getId());
					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting another time. difference (mls) : " + (base + 4L * 24L * 60L * 60L * 1000L - res.getTime()), (base + 4L * 24L * 60L * 60L * 1000L - res.getTime()) < 5000);

					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base + 21L * 24L * 60L * 60L * 1000L), firstWeek, conn);
					assertTrue("Expecting not null value for user", res != null);
					assertTrue("Expecting another time. difference (mls) : " + (base + 23L * 24L * 60L * 60L * 1000L - res.getTime()), (base + 23L * 24L * 60L * 60L * 1000L - res.getTime()) < 5000);

					tr5.save(conn);
					tr6.save(conn);
					tr6.setSubmitDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					conn.commit();

					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);

					//res = _peer.getTimesheetAprovalDate(user1.getId(), new Timestamp(base + 21L * 24L * 60L * 60L * 1000L), firstWeek, conn);
					assertTrue("Expecting null value for user", res == null);
				} finally {
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(tr5, conn);
					delete(tr6, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
					delete(user2, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testIsTimesheetWaitingForApproval() throws Exception {
		final InstallationContext context = getContext();
		final int firstWeek = context.getCalendarService().getDefaultCalendar().getFirstDayOfWeek();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				long base = System.currentTimeMillis();

				Calendar cal = (Calendar) Calendar.getInstance().clone();
				cal.setTime(new Date(base));
				int Year = cal.get(Calendar.YEAR);
				int Month = cal.get(Calendar.MONTH);
				int Day = cal.get(Calendar.DATE);
				cal.set(Year, Month, Day, 0, 0, 0);//current
				cal.set(Calendar.MILLISECOND, 0);
				if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
					cal.add(Calendar.DATE, -7);
				}
				cal.setFirstDayOfWeek(Calendar.MONDAY);
				cal.add(Calendar.DATE, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
				base = cal.getTime().getTime();

				/*empty cases*/
				try {
					//_peer.IsTimesheetWaitingForApproval(null, new Timestamp(base), firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					//_peer.IsTimesheetWaitingForApproval(FAKE_ID, null, firstWeek, conn);
					fail("Null start date should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null start date should throw IllegalArgumentException.");
				}
				try {
					//_peer.IsTimesheetWaitingForApproval(FAKE_ID, new Timestamp(base), firstWeek, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection should throw IllegalArgumentException.");
				}

				/*fake user id*/
				Boolean res = false;//_peer.IsTimesheetWaitingForApproval(FAKE_ID, new Timestamp(base), firstWeek, conn);
				assertTrue("Expecting null value for fake user", res != null);
				assertTrue("Expecting null value for fake user", !res.booleanValue());

				/*creating test objects*/
				User user1 = User.createNewUser("testPSTran_usr1" + Uuid.create(), context);
				user1.setLastName("testPSTran_usr1");
				Work work1 = Work.createNew(Work.TYPE, "testPSTran_wrk1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testPSTran_wrk2", user1);

				PSTransaction tr1 = null;//PSTransaction.createTimeEntry(Double.valueOf("1.5"), user1, work1);
				tr1.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr2 = null;//PSTransaction.createTimeEntry(Double.valueOf("1"), user1, work2);
				tr2.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr3 = null;//PSTransaction.createTimeEntry(Double.valueOf("3.5"), user1, work2);
				tr3.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr4 = null;//PSTransaction.createTimeEntry(Double.valueOf("3"), user1, work1);
				tr4.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				PSTransaction tr5 = null;//PSTransaction.createTimeEntry(Double.valueOf("3.5"), user1, work2);
				tr5.setTransactionDate(new Timestamp(base + 3L * 24L * 60L * 60L * 1000L));

				PSTransaction tr6 = null;//PSTransaction.createTimeEntry(Double.valueOf("3"), user1, work1);
				tr6.setTransactionDate(new Timestamp(base + 23L * 24L * 60L * 60L * 1000L));

				try {
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();

					// before transactions saved
					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", !res.booleanValue());

					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					// no submitted / approved transactions
					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", !res.booleanValue());

					tr1.setSubmitDate(new Timestamp(base + 24L * 60L * 60L * 1000L));
					tr2.setSubmitDate(new Timestamp(base + 24L * 60L * 60L * 1000L));
					tr3.setSubmitDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr4.setSubmitDate(new Timestamp(base + 4L * 24L * 60L * 60L * 1000L));
					tr1.save(conn);
					tr2.save(conn);
					tr3.save(conn);
					tr4.save(conn);
					conn.commit();

					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", res.booleanValue());

					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base + 21L * 24L * 60L * 60L * 1000L), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", res.booleanValue());

					tr5.save(conn);
					tr6.save(conn);
					conn.commit();

					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", !res.booleanValue());

					//res = _peer.IsTimesheetWaitingForApproval(user1.getId(), new Timestamp(base + 21L * 24L * 60L * 60L * 1000L), firstWeek, conn);
					assertTrue("Expecting null value for fake user", res != null);
					assertTrue("Expecting null value for fake user", !res.booleanValue());
				} finally {
					delete(tr1, conn);
					delete(tr2, conn);
					delete(tr3, conn);
					delete(tr4, conn);
					delete(tr5, conn);
					delete(tr6, conn);
					delete(work1, conn);
					delete(work2, conn);
					delete(user1, conn);
				}
				return null;
			}
		}.execute();
	}

}
