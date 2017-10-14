/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.alerts.PersonAlert;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.events.CreationEvent;
import com.cinteractive.ps3.test.MockEventType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.ObjectVisibility;
import com.cinteractive.ps3.PSObject;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class TestPersonAlertPeer extends TestJdbcPeer {
	private PersonAlertPeer _peer;
	private static final boolean DEBUG = true;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(PersonAlertPeer.class.getName(), TestPersonAlertPeer.class.getName());
	}

	public TestPersonAlertPeer(String name) {
		super(name);
	}


	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();
		suite.addTest(new TestPersonAlertPeer("testFindByObject"));
		suite.addTest(new TestPersonAlertPeer("testFindByUserId"));
		suite.addTest(new TestPersonAlertPeer("testFindByUserAndObjectIds"));
		suite.addTest(new TestPersonAlertPeer("testGet"));
		suite.addTest(new TestPersonAlertPeer("testDeleteByObjectId"));
		suite.addTest(new TestPersonAlertPeer("testDeleteByUserId"));
		suite.addTest(new TestPersonAlertPeer("testInsertUpdateDelete"));
		suite.addTest(new TestPersonAlertPeer("testGetSubscribersByAlertObject"));

		return suite;
	}

	public void testGetInstance() throws Exception {
	}

	public void testInsert() throws Exception { /** see testInsertUpdateDelete */
	}

	public void testUpdate() throws Exception { /** see testInsertUpdateDelete */
	}

	public void testDelete() throws Exception { /** see testInsertUpdateDelete */
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}


	public void setUp()
			throws Exception {
		super.setUp();

		_peer = (PersonAlertPeer) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return PersonAlertPeer.getInstance(conn);
			}
		}.execute();
		if (_peer == null)
			throw new NullPointerException("Null PersonAlertPeer instance.");
	}

	public void tearDown()
			throws Exception {
		super.tearDown();
		_peer = null;
	}

	//public List findByObject( Collection sourceIds, EventType type, Collection parentIds, Connection conn )
	public void testFindByObject()
			throws Exception {
		final Collection ids = new java.util.TreeSet();
		final Collection parentIds = new java.util.TreeSet();
// empty case
		ids.add(FAKE_ID);
		List alerts = (List) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return _peer.findByEvent(ids, new MockEventType("Create"), parentIds, conn, ObjectVisibility.CURRENT);
			}
		}.execute();
		assertNotNull("Expecting empty alert list for no results; got null.", alerts);
		assertTrue("Expecting empty alert list for fake source id.", alerts.isEmpty());
// maybe
		ids.clear();
		parentIds.clear();

		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				User user = null;
				User owner = null;
				Work rootWorkInsert = null;
				Work childWorkInsert = null;
				PersonAlert pa = null;
				try {
					user = User.createNewUser("_TestPersonAlertPeer_testFindByObject_", context);
					user.save(conn);
					conn.commit();

					owner = User.createNewUser("_TestPersonAlertPeer_testFindByObject1_", context);
					owner.save(conn);
					conn.commit();

					rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", owner);
					rootWorkInsert.save(conn);
					conn.commit();
					childWorkInsert = Work.createNew(Work.TYPE, "_fake_child_work", owner);
					childWorkInsert.setParentWork(rootWorkInsert, owner);
					childWorkInsert.save(conn);
					conn.commit();

					conn.setAutoCommit(false);
					pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					pa.addSubscription(new MockEventType("Create"));
					_peer.insert(pa, conn);

					ids.add(childWorkInsert.getId());
					parentIds.add(rootWorkInsert.getId());

					List list = _peer.findByEvent(ids, CreationEvent.TYPE, parentIds, conn, ObjectVisibility.CURRENT);
					assertTrue("Expecting NOT empty alerts list", !list.isEmpty());
					conn.rollback();
					conn.setAutoCommit(true);
				} finally {
					delete(childWorkInsert, conn);
					delete(rootWorkInsert, conn);
					delete(user, conn);
					delete(owner, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testFindByUserId()
			throws Exception {
// empty case
		List alerts = (List) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return _peer.findByUserId(FAKE_ID, conn);
			}
		}.execute();
		assertNotNull("Expecting empty list for no results; got null.", alerts);
		assertTrue("Got non-empty list of alerts for fake user id.", alerts.isEmpty());
// some data
		alerts = (List) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				PersistentKey userId = null;

				final Statement stmt = conn.createStatement();
				final ResultSet rset = stmt.executeQuery("SELECT MAX(user_id) FROM Person_Alert");
				if (rset.next()) {
					String idStr = rset.getString(1);
					if (idStr != null)
						userId = Uuid.get(idStr);
				}
				rset.close();
				stmt.close();

				return userId == null ? null : _peer.findByUserId(userId, conn);
			}
		}.execute();
		if (alerts == null)
			return;
		assertTrue("Expecting non-empty alert list.", !alerts.isEmpty());

		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					final PersonAlert pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					pa.addSubscription(new MockEventType("Create"));  // INSERT_SUBSCRIPTION
					_peer.insert(pa, conn);

					List list = _peer.findByUserId(user.getId(), conn);
					assertTrue("Expecting NOT empty alerts list", !list.isEmpty());
					//check if there is test alert
					Iterator iter = list.iterator();
					boolean testAlertFound = false;
					for (; iter.hasNext();) {
						PersonAlert alert = (PersonAlert) iter.next();
						if (alert.subscribedTo(new MockEventType("Create")) && alert.getObjectId().equals(rootWorkInsert.getId()))
							testAlertFound = alert.getUserId().equals(user.getId());
						if (testAlertFound)
							break;
					}
					assertTrue("Test alert expected, but was not returned in list", testAlertFound);
					_peer.delete(pa, conn);
					rootWorkInsert.deleteHard(conn);

					try {
						//if person alert was deleted successfully then _peer.get rise exception
						_peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	public void testGet()
			throws Exception {
// empty case
		new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				try {
					_peer.get(FAKE_ID, conn);
					fail("Expecting EmptyResultSetException for fake id.");
				} catch (EmptyResultSetException ok) {
				}
				return null;
			}
		}.execute();
// some data
		new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				PersistentKey id = null;

				final Statement stmt = conn.createStatement();
				final ResultSet rset = stmt.executeQuery("SELECT MAX(person_alert_id) FROM Person_Alert");
				if (rset.next()) {
					String idStr = rset.getString(1);
					if (idStr != null)
						id = Uuid.get(idStr);
				}
				rset.close();
				stmt.close();

				return id == null ? null : _peer.get(id, conn);
			}
		}.execute();
		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					final PersonAlert pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					//INSERT_SUBSCRIPTION
					_peer.insert(pa, conn);
					try {
						_peer.get(pa.getId(), conn);
					} catch (EmptyResultSetException eIgnore) {
						assertTrue("PersonAlert expected in method testGet()", false);
					}

					_peer.delete(pa, conn);
					rootWorkInsert.deleteHard(conn);

					try {
						//if person alert was deleted successfully then _peer.get rise exception
						_peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	public void testDeleteByObjectId() throws Exception {
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				_peer.deleteByObjectId(FAKE_ID, conn);
				return null;
			}
		}.execute();
		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					final PersonAlert pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					pa.addSubscription(new MockEventType("Create")); //INSERT_SUBSCRIPTION
					_peer.insert(pa, conn);
					try {
						_peer.get(pa.getId(), conn);
					} catch (EmptyResultSetException eIgnore) {
						assertTrue("PersonAlert expected has not been inserted testDeleteByObjectId()", false);
					}

					_peer.deleteByObjectId(rootWorkInsert.getId(), conn);
					try {
						_peer.get(pa.getId(), conn);
						assertTrue("deleteByObjectId failed.", false);
					} catch (EmptyResultSetException eIgnore) {
					}

//                _peer.delete(pa, conn);
					rootWorkInsert.deleteHard(conn);

					try {
						//if person alert was deleted successfully then _peer.get rise exception
						_peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	public void testDeleteByUserId() throws Exception {
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				_peer.deleteByUserId(FAKE_ID, conn);
				return null;
			}
		}.execute();
		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					final PersonAlert pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					pa.addSubscription(new MockEventType("Create")); //INSERT_SUBSCRIPTION
					_peer.insert(pa, conn);
					try {
						_peer.get(pa.getId(), conn);
					} catch (EmptyResultSetException eIgnore) {
						assertTrue("PersonAlert expected has not been inserted testDeleteByObjectId()", false);
					}

					_peer.deleteByUserId(user.getId(), conn);
					try {
						_peer.get(pa.getId(), conn);
						assertTrue("testDeleteByUserId failed.", false);
					} catch (EmptyResultSetException eIgnore) {
					}

//                _peer.delete(pa, conn);
					rootWorkInsert.deleteHard(conn);

					try {
						//if person alert was deleted successfully then _peer.get rise exception
						_peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	public void testFindByUserAndObjectIds() throws Exception {
		Object pa = new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				return _peer.findByUserAndObjectIds(FAKE_ID, FAKE_ID, conn);
			}
		}.execute();
		assertNull("Expecting null object for no results", pa);

		//test with real data
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					final PersonAlert pa = PersonAlert.createNew(user, rootWorkInsert);
					pa.setIncludeChildren(Boolean.TRUE);
					pa.addSubscription(new MockEventType("Create")); //INSERT_SUBSCRIPTION
					_peer.insert(pa, conn);

					PersonAlert testAlert = _peer.findByUserAndObjectIds(user.getId(), rootWorkInsert.getId(), conn);
					assertNotNull("PersonAlert expected in method testFindByUserAndObjectIds()", testAlert);

					_peer.delete(pa, conn);
					rootWorkInsert.deleteHard(conn);

					try {
						//if person alert was deleted successfully then _peer.get rise exception
						_peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	public void testInsertUpdateDelete() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final User user = User.getNobody(context);
					final Work testWorkInsert = Work.createNew(Work.TYPE, "_fake_work", user);
					testWorkInsert.save(conn);
					PersonAlert pa = PersonAlert.createNew(user, testWorkInsert);
					pa = changeValues(pa);
					_peer.insert(pa, conn);
					PersonAlert checkAlert = _peer.get(pa.getId(), conn);
					compare("Insert", pa, checkAlert);
					//change values
					pa = changeValues(pa);
					_peer.update(pa, conn);
					checkAlert = _peer.get(pa.getId(), conn);
					compare("Update", pa, checkAlert);
					_peer.delete(pa, conn);
					testWorkInsert.deleteHard(conn);
					try {
						//if person alert was deleted successfully then _peer.get rise exception
						checkAlert = _peer.get(pa.getId(), conn);
						assertTrue("Person alert has not been deleted", false);
					} catch (EmptyResultSetException eIgnore) {
					}


				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();
	}

	private void compare(String sStageName, PersonAlert a2, PersonAlert a1) {
		assertTrue(sStageName + " - Fileds UserId are differ", a1.getUserId().equals(a2.getUserId()));
		assertTrue(sStageName + " - Fileds ObjectId are differ", a1.getObjectId().equals(a2.getObjectId()));
		assertTrue(sStageName + " - Fileds SendEmail are differ", a1.getSendEmail().equals(a2.getSendEmail()));
		assertTrue(sStageName + " - Fileds IncludeChildren are differ", a1.getIncludeChildren().equals(a2.getIncludeChildren()));
//@see 	PersonAlert: public void restore(PersistentKey id, PersistentKey userId, PersistentKey objectId, Boolean  sendEmail, Boolean includeChildren,
//                                             Boolean 	  includeTasks,	Boolean includeDocuments, String contextName	)
		assertTrue(sStageName + " - Fileds IncludeDocuments are differ", a1.getIncludeDocuments().equals(Boolean.TRUE));
		assertTrue(sStageName + " - Fileds IncludeTasks are differ", a1.getIncludeTasks().equals(Boolean.TRUE));
		//assertTrue("Fileds SendEmail are differ", a1.getSendEmail().equals(a1.getSendEmail()));
	}

	private Boolean switchBoolean(Boolean bValue) {
		return (bValue == null) ? Boolean.TRUE : new Boolean(!bValue.booleanValue());
	}

	private PersonAlert changeValues(PersonAlert pa) {
		pa.setSendEmail(switchBoolean(pa.getSendEmail()));
		pa.setIncludeChildren(switchBoolean(pa.getIncludeChildren()));
		pa.setIncludeDocuments(switchBoolean(pa.getIncludeDocuments()));
		pa.setIncludeTasks(switchBoolean(pa.getIncludeTasks()));

		return pa;
	}

	public void testGetSubscribersByAlertObject() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work work = null;
				User user = null;
				User user1 = null;
				try {
					user = User.createNewUser("_TestPersonAlertPeer_getSubscribers1_", context);
					user.save(conn);
					conn.commit();

					user1 = User.createNewUser("_TestPersonAlertPeer_getSubscribers2_", context);
					user1.save(conn);
					conn.commit();

					List ids = _peer.getSubscriberIds(user1.getId(), conn);
					assertNotNull("Expecting empty list; got null", ids);
					assertTrue("Expecting empty result set; got " + ids.size() + " elements", ids.isEmpty());

					work = Work.createNew(Work.TYPE, "_TestPersonAlertPeer_getSubscribers_", user);
					work.save(conn);
					conn.commit();

					try {
						PersonAlert pa = PersonAlert.createNew(user, work);
						pa.save(conn);
						conn.commit();
					} catch (PersonAlert.DuplicateException ignored) {
					}

					ids = _peer.getSubscriberIds(work.getId(), conn);
					assertNotNull("Expecting not empty list; got null", ids);
					assertTrue("Expecting 1 id in result set; got " + ids.size(), ids.size() == 1);
					assertTrue("Invalid user id in result set", user.getId().equals(ids.get(0)));

					try {
						PersonAlert pa = PersonAlert.createNew(user1, work);
						pa.save(conn);
						conn.commit();
					} catch (PersonAlert.DuplicateException ignored) {
					}


					ids = _peer.getSubscriberIds(work.getId(), conn);
					assertNotNull("Expecting not empty list; got null", ids);
					assertTrue("Expecting 2 ids in result set; got " + ids.size(), ids.size() == 2);
					assertTrue("Invalid user ids in result set", ids.contains(user.getId()) && ids.contains(user1.getId()));
				} finally {
					delete(work, conn);
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
//                if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}

}
