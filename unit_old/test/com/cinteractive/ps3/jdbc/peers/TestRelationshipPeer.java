package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.CIResultSet;
import com.cinteractive.jdbc.CIParameterizedStatement;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.relationships.Relationship;
import com.cinteractive.ps3.relationships.RelationshipType;
import com.cinteractive.ps3.relationships.Relationships;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import com.cinteractive.ps3.PSObject;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.List;


public class TestRelationshipPeer extends TestJdbcPeer {
	private RelationshipPeer _peer;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(RelationshipPeer.class.getName(), TestRelationshipPeer.class.getName());
	}

	private static final boolean DEBUG = true; //show debug info

	public TestRelationshipPeer(String name) {
		super(name);
	}

	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();

		suite.addTest(new TestRelationshipPeer("testGetAllDescendantIds"));
		suite.addTest(new TestRelationshipPeer("testGetDescendantCount"));
		suite.addTest(new TestRelationshipPeer("testFindByObjectId"));
		suite.addTest(new TestRelationshipPeer("testInsertDelete"));
		suite.addTest(new TestRelationshipPeer("testUpdate"));

		return suite;
	}

	public void testGetInstance() throws Exception {
	}

	public void testInsert() throws Exception { /** see testInsertDelete */
	}

	public void testDelete() throws Exception { /** see testInsertDelete */
	}

	public void setUp()
			throws Exception {
		super.setUp();

		_peer = (RelationshipPeer) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return RelationshipPeer.getInstance(conn);
			}
		}.execute();
		if (_peer == null)
			throw new NullPointerException("Null RelationshipPeer instance.");
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}

	public void tearDown()
			throws Exception {
		super.tearDown();
		_peer = null;
	}

	private Hashtable decodeRow(CIResultSet res)
			throws SQLException {
		Hashtable outTbl = new Hashtable();
		outTbl.put("from_object_id", res.getString("from_object_id", false));
		outTbl.put("to_object_id", res.getString("to_object_id", false));
		outTbl.put("relationship_type", res.getString("relationship_type", false));
		return outTbl;
	}

	private boolean compareKey(Hashtable row, Relationship rel) {
		return (
				((String) row.get("from_object_id")).trim().equals(rel.getFromObjectId().toString().trim()) &&
				((String) row.get("to_object_id")).trim().equals(rel.getToObjectId().toString().trim()) &&
				((String) row.get("relationship_type")).trim().equals(rel.getType().toString().trim()));
	}

	private boolean isInclude(CIResultSet res, Relationship rel)
			throws SQLException {
		while (res.next()) {
			Hashtable row = decodeRow(res);
			if (compareKey(row, rel)) return true;
		}
		return false;
	}

	public void testFindByObjectId()
			throws SQLException {
		final InstallationContext context = getContext();
		final Connection conn = getConnection();
		//conn.setAutoCommit(false);
		Relationship rel1 = null;
		Relationship rel2 = null;
		User user1 = null;
		User user2 = null;
		User user3 = null;
		Work work = null;
		try {
			CIResultSet res = null;
			CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();

			try {
				res = _peer.findByObjectId(null, pstmt);
				fail("Null PersistentKey parameter should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null PersistentKey parameter should throw IllegalArgumentException.");
			}

			try {
				res = _peer.findByObjectId(WorkUtil.getNoProject(context).getId(), null);
				fail("Null connection parameter should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null connection parameter should throw IllegalArgumentException.");
			}

			res = _peer.findByObjectId(FAKE_ID, pstmt);
			assertNotNull("Expecting empty result set for no results; got null.", res);
			assertTrue("Expecting empty result set fake object id.", !res.next());


			user3 = User.createNewUser("_TestRelationPeer_fndRlnspsByObjId3_", context);
			user3.setFirstName("fName3");
			user3.save(conn);
			conn.commit();

			work = Work.createNew(Work.TYPE, "_TestRelationPeer_fndRlnspsByObjId_", user3);
			work.save(conn);
			conn.commit();

			user1 = User.createNewUser("_TestRelationPeer_fndRlnspsByObjId1_", context);
			user1.setFirstName("fName1");
			user1.save(conn);
			conn.commit();

			user2 = User.createNewUser("_TestRelationPeer_fndRlnspsByObjId2_", context);
			user2.setFirstName("fName2");
			user2.save(conn);
			conn.commit();

			rel1 = new Relationships(work).addRelationship(work, user1, RelationshipType.TEAM_MEMBER, user1.getId());
			rel2 = new Relationships(work).addRelationship(work, user2, RelationshipType.TEAM_MEMBER, user2.getId());
			rel1.save(conn);
			conn.commit();
			rel2.save(conn);
			conn.commit();

			res = _peer.findByObjectId(work.getId(), pstmt);
			assertTrue("Expecting created relationsips in result set (rel1 for work).", isInclude(res, rel1));

			res = _peer.findByObjectId(work.getId(), pstmt);
			assertTrue("Expecting created relationsips in result set (rel2 for work).", isInclude(res, rel2));

			res = _peer.findByObjectId(user1.getId(), pstmt);
			assertTrue("Expecting created relationsips in result set (rel1 for user1).", isInclude(res, rel1));

			res = _peer.findByObjectId(user2.getId(), pstmt);
			assertTrue("Expecting created relationsips in result set (rel2 for user2).", isInclude(res, rel2));
		} finally {
			delete(rel1, conn);
			delete(rel2, conn);
			delete(work, conn);
			delete(user1, conn);
			delete(user2, conn);
			delete(user3, conn);
			conn.close();
		}
	}

	public void testGetAllDescendantIds() {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {

			protected Object query(Connection conn) throws SQLException {

				List res = null;

				try {
					res = _peer.getAllDescendantIds(null, 1, conn);
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				}

				try {
					res = _peer.getAllDescendantIds(WorkUtil.getNoProject(context).getId(), 1, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getAllDescendantIds(FAKE_ID, 1, conn);
				assertNotNull("Expecting empty child (descendant) result for fake object id; got null.", res);
				assertTrue("Expecting empty child (descendant) result fake object id.", res.isEmpty());

				Work workParent = null;
				Work workChild1 = null;
				Work workChild2 = null;
				Work workGrandChild1 = null;
				Work workGrandChild2 = null;
				try {
					final User nobody = User.getNobody(context);
					workParent = Work.createNew(Work.TYPE, "testRelationshipPeer_workParent", nobody);
					workParent.save(conn);
					conn.commit();

					workChild1 = Work.createNew(Work.TYPE, "testRelationshipPeer_workChild1", nobody);
					workChild1.save(conn);
					conn.commit();

					workChild2 = Work.createNew(Work.TYPE, "testRelationshipPeer_workChild2", nobody);
					workChild2.save(conn);
					conn.commit();

					workGrandChild1 = Work.createNew(Work.TYPE, "testRelationshipPeer_workGrandChild1", nobody);
					workGrandChild1.save(conn);
					conn.commit();

					workGrandChild2 = Work.createNew(Work.TYPE, "testRelationshipPeer_workGrandChild2", nobody);
					workGrandChild2.save(conn);
					conn.commit();

					res = _peer.getAllDescendantIds(workParent.getId(), 1, conn);
					assertNotNull("Expecting empty child (descendant) result for parent work and depth == 1; got null.", res);
					assertTrue("Expecting empty result for parent work and depth == 1; got :" + res.size(), res.isEmpty());

					workChild1.setParentWork(workParent, nobody);
					workChild2.setParentWork(workParent, nobody);
					workChild1.save(conn);
					workChild2.save(conn);
					conn.commit();
					workGrandChild1.setParentWork(workChild1, nobody);
					workGrandChild2.setParentWork(workChild1, nobody);
					workGrandChild1.save(conn);
					workGrandChild2.save(conn);
					conn.commit();

					res = _peer.getAllDescendantIds(workParent.getId(), 0, conn);
					assertNotNull("Expecting empty child (descendant) result for depth == 0; got null.", res);
					assertTrue("Expecting empty child (descendant) result for depth == 0.", res.isEmpty());

					res = _peer.getAllDescendantIds(workParent.getId(), 1, conn);
					assertNotNull("Expecting not empty child (descendant) result for parent work and depth == 1; got null.", res);
					assertTrue("Expecting another count of ids in result for parent work and depth == 1; got :" + res.size(), res.size() == 2);
					assertTrue("Expecting another work ids in result for parent work and depth == 1.",
							res.contains(workChild1.getId()) && res.contains(workChild2.getId()));

					res = _peer.getAllDescendantIds(workParent.getId(), 2, conn);
					assertNotNull("Expecting not empty child (descendant) result for parent work and depth == 2; got null.", res);
					assertTrue("Expecting another count of ids in result for parent work and depth == 2; got :" + res.size(), res.size() == 4);
					assertTrue("Expecting another work ids in result for parent work and depth == 2.",
							res.contains(workChild1.getId()) && res.contains(workChild2.getId()) &&
							res.contains(workGrandChild1.getId()) && res.contains(workGrandChild2.getId()));

					res = _peer.getAllDescendantIds(workParent.getId(), 66, conn);
					assertNotNull("Expecting not empty child (descendant) result for parent work and depth == 66; got null.", res);
					assertTrue("Expecting another count of ids in result for parent work and depth == 66; got :" + res.size(), res.size() == 4);
					assertTrue("Expecting another work ids in result for parent work and depth == 66.",
							res.contains(workChild1.getId()) && res.contains(workChild2.getId()) &&
							res.contains(workGrandChild1.getId()) && res.contains(workGrandChild2.getId()));

					res = _peer.getAllDescendantIds(workParent.getId(), -2, conn);
					assertNotNull("Expecting not empty child (descendant) result for parent work and depth == -2; got null.", res);
					assertTrue("Expecting another count of ids in result for parent work and depth == -2; got :" + res.size(), res.size() == 4);
					assertTrue("Expecting another work ids in result for parent work and depth == -2.",
							res.contains(workChild1.getId()) && res.contains(workChild2.getId()) &&
							res.contains(workGrandChild1.getId()) && res.contains(workGrandChild2.getId()));

					res = _peer.getAllDescendantIds(workChild1.getId(), 1, conn);
					assertNotNull("Expecting not empty child (descendant) result for child work and depth == 1; got null.", res);
					assertTrue("Expecting another count of ids in result for child work and depth == 1; got :" + res.size(), res.size() == 2);
					assertTrue("Expecting another work ids in result for child work and depth == 1.",
							res.contains(workGrandChild1.getId()) && res.contains(workGrandChild2.getId()));
				} finally {
					delete(workGrandChild1, conn);
					delete(workGrandChild2, conn);
					delete(workChild1, conn);
					delete(workChild2, conn);
					delete(workParent, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testGetDescendantCount() {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {

			protected Object query(Connection conn) throws SQLException {

				int res;

				try {
					res = _peer.getDescendantCount(null, conn);
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				}

				try {
					res = _peer.getDescendantCount(WorkUtil.getNoProject(context).getId(), null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok) {
				} catch (Exception e) {
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getDescendantCount(FAKE_ID, conn);
				assertTrue("Expecting no child (descendant) result for fake object id.", res == 0);

				Work workParent = null;
				Work workChild1 = null;
				Work workChild2 = null;
				Work workGrandChild1 = null;
				Work workGrandChild2 = null;
				try {
					final User nobody = User.getNobody(context);
					workParent = Work.createNew(Work.TYPE, "_testRelationshipPeer_getDescendantCount_parent1_", nobody);
					workParent.save(conn);
					conn.commit();

					workChild1 = Work.createNew(Work.TYPE, "_testRelationshipPeer_getDescendantCount_child1_", nobody);
					workChild1.save(conn);
					conn.commit();

					workChild2 = Work.createNew(Work.TYPE, "_testRelationshipPeer_getDescendantCount_child2_", nobody);
					workChild2.save(conn);
					conn.commit();

					workGrandChild1 = Work.createNew(Work.TYPE, "_testRelationshipPeer_getDescendantCount_gc1_", nobody);
					workGrandChild1.save(conn);
					conn.commit();

					workGrandChild2 = Work.createNew(Work.TYPE, "_testRelationshipPeer_getDescendantCount_gc2_", nobody);
					workGrandChild2.save(conn);
					conn.commit();

					res = _peer.getDescendantCount(workParent.getId(), conn);
					assertTrue("Expecting result == 0 for parent work; got :" + res, res == 0);

					workChild1.setParentWork(workParent, nobody);
					workChild2.setParentWork(workParent, nobody);
					workChild1.save(conn);
					workChild2.save(conn);
					conn.commit();

					workGrandChild1.setParentWork(workChild1, nobody);
					workGrandChild2.setParentWork(workChild1, nobody);
					workGrandChild1.save(conn);
					workGrandChild2.save(conn);
					conn.commit();

					res = _peer.getDescendantCount(workParent.getId(), conn);
					assertTrue("Expecting another count of ids as result for parent work; got :" + res, res == 4);

					res = _peer.getDescendantCount(workChild1.getId(), conn);
					assertTrue("Expecting another count of ids as result for child work; got :" + res, res == 2);

				} finally {
					delete(workGrandChild1, conn);
					delete(workGrandChild2, conn);
					delete(workChild1, conn);
					delete(workChild2, conn);
					delete(workParent, conn);
				}
				return null;
			}
		}.execute();
	}

	public void testInsertDelete() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());

		conn.setAutoCommit(false);

		try {
			try {
				_peer.insert(null, conn);
				fail("Null Relationship Object in insert method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Relationship Object in insert method should throw IllegalArgumentException.");
			}

			Work work = WorkUtil.getNoProject(context);
			User user = User.getNobody(context);
			Relationship rel = new Relationships(work).addRelationship(work, user, RelationshipType.TEAM_MEMBER, user.getId());

			try {
				_peer.insert(rel, null);
				fail("Null Connection in insert method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in insert method should throw IllegalArgumentException.");
			}

			_peer.insert(rel, conn);
			final Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select create_date from object_rlnsp where from_object_id='" + work.getId() + "'" +
					" and to_object_id='" + user.getId() + "' and created_by='" + user.getId() + "'");

			assertTrue("Expecting record in resultset", rset.next());

			try {
				_peer.delete(null, conn);
				fail("Null Relationship Object in delete method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Relationship Object in delete method should throw IllegalArgumentException.");
			}

			try {
				_peer.delete(rel, null);
				fail("Null Connection in delete method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in delete method should throw IllegalArgumentException.");
			}

			_peer.delete(rel, conn);

			rset = stmt.executeQuery("select create_date from object_rlnsp where from_object_id='" + work.getId() + "'" +
					" and to_object_id='" + user.getId() + "' and created_by='" + user.getId() + "'");

			assertTrue("Not expecting record in resultset", !rset.next());
		} finally {
			conn.rollback();
			conn.close();
		}

	}

	public void testUpdate() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());

		conn.setAutoCommit(false);

		try {
			Work work = WorkUtil.getNoProject(context);
			User user = User.getNobody(context);
			Relationship rel = new Relationships(work).addRelationship(work, user, RelationshipType.TEAM_MEMBER, user.getId());
			_peer.insert(rel, conn);

			final Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery("select create_date from object_rlnsp where from_object_id='" + work.getId() + "'" +
					" and to_object_id='" + user.getId() + "' and created_by='" + user.getId() + "'");
			assertTrue("Expecting record in resultset", rset.next());

			try {
				_peer.update(null, conn);
				fail("Null Relationship Object in update method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Relationship Object in update method should throw IllegalArgumentException.");
			}

			try {
				_peer.update(rel, null);
				fail("Null Connection in update method should throw IllegalArgumentException.");
			} catch (IllegalArgumentException ok) {
			} catch (Exception e) {
				fail("Null Connection in update method should throw IllegalArgumentException.");
			}

			rel.setName("NewFakeName");
			_peer.update(rel, conn);

			rset = stmt.executeQuery("select create_date from object_rlnsp where from_object_id='" + work.getId() + "'" +
					" and to_object_id='" + user.getId() + "' and created_by='" + user.getId() + "' and relationship_name='NewFakeName'");

			assertTrue("Expecting record in resultset", rset.next());
		} finally {
			conn.rollback();
			conn.close();
		}

	}

	private void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
                //if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}
}
