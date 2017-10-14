package com.cinteractive.ps3.jdbc.peers;

/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.Relationship;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.scheduler.problems.InternalErrorProblem;
import com.cinteractive.ps3.scheduler.problems.ScheduleProblem;
import com.cinteractive.ps3.scheduler.problems.ScheduleProblemType;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.IllegalDependencyException;
import com.cinteractive.ps3.work.ScheduleSettingsException;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestScheduleProblemPeer extends TestJdbcPeer {
	private final static boolean DEBUG = true;
	private ScheduleProblemPeer _peer = null;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(ScheduleProblemPeer.class.getName(), TestScheduleProblemPeer.class.getName());
	}

	public TestScheduleProblemPeer(String name) {
		super(name);
	}

	private static void addTest(TestSuite suite, String testName) {
		suite.addTest(new TestScheduleProblemPeer(testName));
	}

	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();
		addTest(suite, "testRestoreScheduleProblem");
		addTest(suite, "testGetProblemObjectIds");
		addTest(suite, "testDeleteByWorkId");
		addTest(suite, "testDeleteByCreatedById");
		addTest(suite, "testInsertUpdateDelete");
		addTest(suite, "testFind");
		return suite;
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}


	public void setUp() throws Exception {
		super.setUp();

		_peer = (ScheduleProblemPeer) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return ScheduleProblemPeer.getInstance(conn);
			}
		}.execute();
		if (_peer == null)
			throw new NullPointerException("Null ScheduleProblemPeer instance.");
	}

	public void tearDown() throws Exception {
		super.tearDown();
		_peer = null;
	}

	public void testRestoreScheduleProblem() throws Exception {
		//test only SQL syntax
		final InstallationContext context = getContext();
		{
			// empty case
			final ScheduleProblem sp = (ScheduleProblem) new JdbcQuery(this) {
				protected Object query(Connection conn) throws SQLException {
					return _peer.restore(FAKE_ID, context, conn);
				}
			}.execute();
			assertNull("Expecting null for fake ID", sp);
		}
	}

	public void testGetProblemObjectIds() throws Exception {
		//empty list for fake root id
		final InstallationContext context = getContext();
		{
			// empty case
			final Set ids = (Set) new JdbcQuery(this) {
				protected Object query(Connection conn) throws SQLException {
					return _peer.getProblemObjectIds(FAKE_ID, conn);
				}
			}.execute();
			assertNotNull("Expecting empty List for no results; got null.", ids);
			assertTrue("Expecting empty ScheduleProblem ids for FAKE root id.", ids.isEmpty());
		}


		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work rootWork = null;
				Work relatedWork = null;
				Work secondWork = null;
				InternalErrorProblem sp = null;
				User user = null;
				try {
					user = User.createNewUser("_TestSchedProblem_deleteByCreatedBy_", context);
					user.save(conn);
					conn.commit();

					rootWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByCreatedById_", user);
					rootWork.save(conn);
					conn.commit();

					relatedWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByCreatedById_related_", user);
					relatedWork.setParentWork(rootWork, user);
					relatedWork.save(conn);
					conn.commit();

					secondWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByCreatedById_second_", user);
					secondWork.setParentWork(rootWork, user);
					secondWork.save(conn);
					Relationship rs = secondWork.getDependencies().addDependency(relatedWork, RelationshipType.SF_DEPENDENCY, user);
					conn.commit();

					sp = InternalErrorProblem.createNew(rootWork, "fake_description", user);
					sp.getCreateDate().setNanos(0);
					sp.addRelatedWork(rootWork, false, "pre");
					sp.addRelatedDependencyForTest(rs);
					sp.save(conn);
					conn.commit();

					ScheduleProblem cpy = _peer.restore(sp.getId(), context, conn);
					assertNotNull("Expecting schedule problem", cpy);
					assertNotNull("Expecting related work", findRelatedObjects(sp, relatedWork, secondWork));

					Set ids = _peer.getProblemObjectIds(rootWork.getId(), conn);
					assertTrue("Expecting 2 related objects; got " + ids.size(), ids.size() == 2);
					assertTrue("Expecting my related works",
							ids.contains(relatedWork.getId()) &&
							ids.contains(secondWork.getId())
					);

				} catch (IllegalDependencyException ignored) {
					if (DEBUG) ignored.printStackTrace();
				} finally {
					delete(sp, conn);
					delete(secondWork, conn);
					delete(relatedWork, conn);
					delete(rootWork, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();

	}

	public void testDeleteByCreatedById() throws Exception {
		final InstallationContext context = getContext();

		//empty case
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				_peer.deleteByCreatedById(FAKE_ID, conn);
				return null;
			}
		}.execute();

		//real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work rootWork = null;
				Work relatedWork = null;
				ScheduleProblem sp = null;
				User user = null;
				try {
					user = User.createNewUser("_TestSchedProblem_deleteByCreatedBy_", context);
					user.save(conn);
					conn.commit();

					rootWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByCreatedById_", user);
					rootWork.save(conn);

					relatedWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByCreatedById_related_", user);
					relatedWork.save(conn);
					conn.commit();

					sp = InternalErrorProblem.createNew(rootWork, "fake_description", user);
					sp.getCreateDate().setNanos(0);
					sp.addRelatedWork(relatedWork, false, "pre");
					sp.save(conn);
					conn.commit();

					ScheduleProblem cpy = _peer.restore(sp.getId(), context, conn);
					assertNotNull("Expecting schedule problem", cpy);
					assertNotNull("Expecting related work", findRelatedObject(cpy, relatedWork));

					final String sql = "select * from Object_Problem where problem_id = '" + sp.getId().toString() + "'";

					final Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					assertTrue("Expecting related object in result set", rs.next());
					rs.close();

					_peer.deleteByCreatedById(user.getId(), conn);
					conn.commit();

					cpy = _peer.restore(sp.getId(), context, conn);
					assertNull("Unexpected schedule problem found", cpy);

					sp = null; //was deleted by peer

					rs = stmt.executeQuery(sql);
					assertTrue("Unexpected related object in result set", !rs.next());
					rs.close();
					stmt.close();
				} finally {
					delete(rootWork, conn);
					delete(relatedWork, conn);
					delete(sp, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();

	}

	public void testDeleteByWorkId() throws Exception {
		final InstallationContext context = getContext();

		// empty case
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				_peer.deleteByWorkId(FAKE_ID, conn);
				return null;
			}
		}.execute();

		// real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work rootWork = null;
				Work relatedWork = null;
				ScheduleProblem sp = null;
				User user = null;
				try {
					user = User.createNewUser("_TestSchedProblem_deleteByRootId_", context);
					user.save(conn);
					conn.commit();

					rootWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByRootId_", user);
					rootWork.save(conn);

					relatedWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_deleteByRootId_related_", user);
					relatedWork.save(conn);
					conn.commit();

					sp = InternalErrorProblem.createNew(rootWork, "fake_description", user);
					sp.getCreateDate().setNanos(0);
					sp.addRelatedWork(relatedWork, false, "pre");
					sp.save(conn);
					conn.commit();

					ScheduleProblem cpy = _peer.restore(sp.getId(), context, conn);
					assertNotNull("Expecting schedule problem", cpy);
					assertNotNull("Expecting related work", findRelatedObject(cpy, relatedWork));

					final String sql = "select * from Object_Problem where problem_id = '" + sp.getId().toString() + "'";

					final Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					assertTrue("Expecting related object in result set", rs.next());
					rs.close();

					_peer.deleteByWorkId(rootWork.getId(), conn);
					conn.commit();

					cpy = _peer.restore(sp.getId(), context, conn);
					assertNull("Unexpected schedule problem found", cpy);

					sp = null; //was deleted by peer

					rs = stmt.executeQuery(sql);
					assertTrue("Unexpected related object in result set", !rs.next());
					rs.close();
					stmt.close();
				} finally {
					delete(rootWork, conn);
					delete(relatedWork, conn);
					delete(sp, conn);
					delete(user, conn);
				}
				return null;
			}
		}.execute();

	}

	private PersistentKey findRelatedObject(ScheduleProblem sp, PSObject o) {
		for (Iterator i = sp.getRelatedIds(o.getPSType()).iterator(); i.hasNext();) {
			final ScheduleProblem.ProblemData pd = (ScheduleProblem.ProblemData) i.next();
			if (pd.getObjectId().equals(o.getId())) return pd.getObjectId();
		}
		return null;
	}

	private PersistentKey findRelatedObjects(ScheduleProblem sp, PSObject o1, PSObject o2) {
		for (Iterator i = sp.getRelatedIds(o1.getPSType()).iterator(); i.hasNext();) {
			final ScheduleProblem.ProblemData pd = (ScheduleProblem.ProblemData) i.next();
			if (o1.getId().equals(pd.getObjectId()) && o2.getId().equals(pd.getSecondObjectId())) return pd.getObjectId();
		}
		return null;
	}

	public void testFind() throws Exception {
		//empty case
		final InstallationContext context = getContext();
		List ids = (List) new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				return _peer.find(FAKE_ID, context, conn);
			}
		}.execute();
		assertNotNull("Expecting empty list for fake root id; got null", ids);
		assertTrue("Expecting empty list for no results", ids.isEmpty());

		//with real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				Work rootWork = null;
				Work relatedWork = null;
				ScheduleProblem sp = null;
				try {
					rootWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_find_", Nobody.get(context));
					rootWork.save(conn);

					relatedWork = Work.createNew(Work.TYPE, "_TestScheduleProblemPeer_find_related_", Nobody.get(context));
					relatedWork.save(conn);
					conn.commit();

					sp = InternalErrorProblem.createNew(rootWork, "fake_description", Nobody.get(context));
					sp.getCreateDate().setNanos(0);

					sp.save(conn);
					conn.commit();

					List ps = _peer.find(rootWork.getId(), context, conn);
					assertTrue("Expecting 1 problem in resulting list; got " + ps.size(), ps.size() == 1);
					compare("Find", (ScheduleProblem) ps.get(0), sp);

					sp.addRelatedWork(relatedWork, false, "pre");
					sp.save(conn);
					conn.commit();

					ps = _peer.find(rootWork.getId(), context, conn);
					assertTrue("Expecting 1 problem in resulting list; got " + ps.size(), ps.size() == 1);
					compare("Find", (ScheduleProblem) ps.get(0), sp);
				} finally {
					delete(rootWork, conn);
					delete(relatedWork, conn);
					delete(sp, conn);
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
					final ScheduleProblem sp = ScheduleProblem.createNew(ScheduleProblemType.CYCLIC, WorkUtil.getNoProject(context), Nobody.get(context));
					_peer.insert(sp, conn);
					_peer.update(sp, conn);
					_peer.restore(sp.getId(), context, conn);
					_peer.delete(sp, conn);
				} finally {
					conn.rollback();
					conn.setAutoCommit(false);
				}
				return null;
			}
		}.execute();
		//with real data
		new JdbcQuery(this) {
			protected Object query(Connection conn) throws SQLException {
				conn.setAutoCommit(false);
				try {
					final Work rootWork = Work.createNew(Work.TYPE, "_fake_root_work", Nobody.get(context));
					rootWork.save(conn);
					final ScheduleProblem sp = InternalErrorProblem.createNew(rootWork, "fake_description1", Nobody.get(context));
					sp.getCreateDate().setNanos(0);

					_peer.insert(sp, conn);
					compare("Insert", _peer.restore(sp.getId(), context, conn), sp);

					//change values
					sp.setDescription("fake_description");
					_peer.update(sp, conn);
					compare("Update", _peer.restore(sp.getId(), context, conn), sp);

					_peer.delete(sp, conn);
					assertNull("SchedulerProblem has not been deleted", _peer.restore(sp.getId(), context, conn));
				} finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

	private void compare(String sStageName, ScheduleProblem copy, ScheduleProblem orig) {
		assertTrue(sStageName + " - Values returned by  getType() are differ", copy.getType().equals(orig.getType()));
		assertTrue(sStageName + " - Values returned by  getContextId() are differ", copy.getContextId().equals(orig.getContextId()));
		assertTrue(sStageName + " - Values returned by  getDescription() are differ", copy.getDescription().equals(orig.getDescription()));
		assertTrue(sStageName + " - Values returned by  getCreatedBy() are differ", copy.getCreatedBy().equals(orig.getCreatedBy()));
		assertTrue(sStageName + " - Values returned by  getCreateDate() are differ", copy.getCreateDate().equals(orig.getCreateDate()));
		assertNotNull("getProjectId returned null", copy.getProjectId());
		assertTrue(sStageName + " - Values returned by  getProjectId() are differ", copy.getProjectId().equals(orig.getProjectId()));

		//compare related objects
		for (Iterator types = orig.relatedObjectTypes(); types.hasNext();) {
			final PSType type = (PSType) types.next();
			for (Iterator objects = orig.getRelatedIds(type).iterator(); objects.hasNext();) {
				assertNotNull("Expecting object list for PSType", copy.getRelatedIds(type));
				assertTrue("Expecting related object", copy.getRelatedIds(type).contains(objects.next()));
			}
		}

	}

	private final void delete(JdbcPersistableBase o, Connection conn) {
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

	// gags
	public void testInsert() {
	};
	public void testUpdate() {
	};
	public void testDelete() {
	};
	public void testGetInstance() {
	};
	public void testRestore() {
	};
}


