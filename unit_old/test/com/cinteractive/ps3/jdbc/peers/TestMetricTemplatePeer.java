/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.metrics.MetricPeriodCode;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.relationships.Relationship;
import com.cinteractive.ps3.work.MasterTask;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.PSObject;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TestMetricTemplatePeer extends TestJdbcPeer
{
	private MetricTemplatePeer _peer;

	static
	{
		registerCase();
	}

	private static void registerCase()
	{
		TestSql.registerTestCase(MetricTemplatePeer.class.getName(), TestMetricTemplatePeer.class.getName());
	}

	public TestMetricTemplatePeer(String name)
	{
		super(name);
	}

	private static final boolean DEBUG = true; //show debug info

	private static void addTest(TestSuite suite, String testName)
	{
		suite.addTest(new TestMetricTemplatePeer(testName));
	}

	public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite("TestMetricTemplatePeer");

		addTest(suite, "testFindAssociatedTypeCodesContract");
		addTest(suite, "testFindAssociatedTypeCodes");
		addTest(suite, "testGetAssociatedMetricsByType");
		addTest(suite, "testGetRootMetricIds");
		addTest(suite, "testGetInstance");
		addTest(suite, "testGetWasImportedMetricIds");
		addTest(suite, "testInsertUpdateDelete");
		addTest(suite, "testGetMetricIdByCode");
		addTest(suite, "testFindTemplateIdsByPSType");
		addTest(suite, "testGetRootMetricIdsWithInstancies");

		return suite;
	}

	public void testInsert() throws Exception
	{ /** see testInsertUpdateDelete */
	}

	public void testUpdate() throws Exception
	{ /** see testInsertUpdateDelete */
	}

	public void testDelete() throws Exception
	{ /** see testInsertUpdateDelete */
	}

	public void testGetMetricTemplatePeer() throws Exception
	{
	}

	public static Test suite()
	{
		return setUpDb(bareSuite());
	}


	public void setUp()
			throws Exception
	{
		super.setUp();


		_peer = (MetricTemplatePeer) new JdbcQuery(this)
		{
			protected Object query(Connection conn)
					throws SQLException
			{
				return MetricTemplatePeer.getMetricTemplatePeer(conn);
			}
		}.execute();
		if (_peer == null)
			throw new NullPointerException("Null MetricTemplatePeer instance.");
	}

	private final void delete(JdbcPersistableBase o, Connection conn)
	{
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

	public void tearDown()
			throws Exception
	{
		super.tearDown();
		_peer = null;
	}

	public void testFindAssociatedTypeCodes()
			throws Exception
	{
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
					throws SQLException
			{
				Set res = null;

				User user1 = User.createNewUser("fake1@email.add_01", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				Work work1 = Work.createNew(Work.TYPE, "testMT_work_1", user1);
				work1.generateAlerts(false);
				Work work2 = Work.createNew(Work.TYPE, "testMT_work_2", user1);
				work2.generateAlerts(false);
				Work work3 = Work.createNew(MasterTask.TYPE, "testMT_work_3", user1);
				work3.setParentWork(work2, user1);
				work3.generateAlerts(false);
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				tmpl1.generateAlerts(false);
				MetricInstance ins1 = MetricInstance.createNew(tmpl1, work1, false);
				ins1.generateAlerts(false);
				ins1.setIsReadyForRollup(false,user1);
				MetricInstance ins2 = MetricInstance.createNew(tmpl1, work2, false);
				ins2.setIsReadyForRollup(true,user1);
				ins2.generateAlerts(false);
				Relationship rel1 = null;
				Relationship rel2 = null;
				Relationship rel3 = null;
				Relationship rel4 = null;
				Relationship rel5 = null;
				try
				{
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();
					work3.save(conn);
					conn.commit();

					res = _peer.findAssociatedTypeCodes(tmpl1.getId(), null, conn);
					assertNotNull("Expecting empty result set, not null", res);
					assertTrue("Expecting empty result set.", res.isEmpty());

					tmpl1.save(conn);
					conn.commit();

					res = _peer.findAssociatedTypeCodes(tmpl1.getId(), null, conn);
					assertNotNull("Expecting empty result set, not null", res);
					assertTrue("Expecting empty result set.", res.isEmpty());

					ins1.save(conn);
					ins2.save(conn);
					conn.commit();

					Set resTypes = new HashSet();
					resTypes.add(Work.TYPE.getCode());

					res = _peer.findAssociatedTypeCodes(tmpl1.getId(), Boolean.TRUE, conn);
					assertTrue("Expecting another count of type codes in set. get " + res.size(), res.size() == 1);
					assertTrue("Expecting another type codes in set.", res.containsAll(resTypes));

//					resTypes.add(MasterTask.TYPE.getCode());

					res = _peer.findAssociatedTypeCodes(tmpl1.getId(), null, conn);
					assertTrue("Expecting another count of type codes in set. get " + res.size(), res.size() == 1);
					assertTrue("Expecting another type codes in set.", res.containsAll(resTypes));
					res = _peer.findAssociatedTypeCodes(tmpl1.getId(), Boolean.FALSE, conn);
					assertTrue("Expecting another count of type codes in set. get " + res.size(), res.size() == 1);
					assertTrue("Expecting another type codes in set.", res.containsAll(resTypes));
				} finally
				{
					try
					{
						delete(rel1, conn);
						delete(rel2, conn);
						delete(rel3, conn);
						delete(rel4, conn);
						delete(rel5, conn);
						conn.commit();
						delete(ins1, conn);
						delete(ins2, conn);
						conn.commit();
						delete(tmpl1, conn);
						conn.commit();
						delete(work3, conn);
						conn.commit();
						delete(work1, conn);
						delete(work2, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}

				}
				return null;
			}
		}.execute();
	}

	public void testFindAssociatedTypeCodesContract()
			throws Exception
	{
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
					throws SQLException
			{
				try
				{
					_peer.findAssociatedTypeCodes(null, null, conn);
					fail("Expecting IllegalArgumentException for null metric template id.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Expecting IllegalArgumentException for null metric template id.");
				}
				try
				{
					_peer.findAssociatedTypeCodes(FAKE_ID, null, null);
					fail("Expecting IllegalArgumentException for null Connection.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Expecting IllegalArgumentException for null Connection.");
				}

				final Set ids = _peer.findAssociatedTypeCodes(FAKE_ID, null, conn);
				assertNotNull("Got null instead of empty Set for no results.", ids);
				assertTrue("Expecting empty type codes for fake metric template id; got '" + ids + "'.", ids.isEmpty());
				return ids;
			}
		}.execute();
	}

	public void testGetAssociatedMetricsByType()
			throws Exception
	{
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{

				Set res = null;

				try
				{
					res = _peer.getAssociatedMetricsByType(null, FAKE_ID, conn);
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null PersistentKey parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getAssociatedMetricsByType(CONTEXT_TYPE, null, conn);
					fail("Null context id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null context id parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getAssociatedMetricsByType(CONTEXT_TYPE, FAKE_ID, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getAssociatedMetricsByType(CONTEXT_TYPE, FAKE_ID, conn);
				assertNotNull("Expecting empty result set, not null.", res);
				assertTrue("Expecting empty result set with fake context id. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("fake1@email.add_02", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user");
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_2", user1);
				try
				{
					user1.save(conn);
					tmpl1.save(conn);
					tmpl2.save(conn);
					conn.commit();

					res = _peer.getAssociatedMetricsByType(Work.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							(!res.contains(tmpl1.getId())) && (!res.contains(tmpl2.getId())));
					final int countWork = res.size();
					res = _peer.getAssociatedMetricsByType(MasterTask.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							(!res.contains(tmpl1.getId())) && (!res.contains(tmpl2.getId())));
					final int countTask = res.size();

					tmpl1.addRelatedType(Work.TYPE);
					tmpl1.addRelatedType(MasterTask.TYPE);
					tmpl2.addRelatedType(Work.TYPE);
					tmpl1.save(conn);
					tmpl2.save(conn);
					conn.commit();

					res = _peer.getAssociatedMetricsByType(Work.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set.", res.size() == (countWork + 2));
					assertTrue("Expecting another items in result set.",
							(res.contains(tmpl1.getId())) && (res.contains(tmpl2.getId())));
					res = _peer.getAssociatedMetricsByType(MasterTask.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set.", res.size() == (countTask + 1));
					assertTrue("Expecting another items in result set.",
							(res.contains(tmpl1.getId())) && (!res.contains(tmpl2.getId())));

					tmpl1.removeRelatedType(Work.TYPE);
					tmpl1.removeRelatedType(MasterTask.TYPE);
					tmpl2.removeRelatedType(Work.TYPE);
					tmpl1.save(conn);
					tmpl2.save(conn);
					conn.commit();

					res = _peer.getAssociatedMetricsByType(Work.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set.", res.size() == countWork);
					assertTrue("Expecting another items in result set.",
							(!res.contains(tmpl1.getId())) && (!res.contains(tmpl2.getId())));
					res = _peer.getAssociatedMetricsByType(MasterTask.TYPE, context.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set.", res.size() == countTask);
					assertTrue("Expecting another items in result set.",
							(!res.contains(tmpl1.getId())) && (!res.contains(tmpl2.getId())));
				} finally
				{
					try
					{
						delete(tmpl1, conn);
						delete(tmpl2, conn);
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testGetRootMetricIds()
			throws Exception
	{
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{

				Set res = null;

				try
				{
					res = _peer.getRootMetricIds(null, conn);
					fail("Null resource id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null resource id parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getRootMetricIds(contextId, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getRootMetricIds(FAKE_ID, conn);
				assertNotNull("Expecting empty result set, not null.", res);
				assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("fake1@email.add_03", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_2", user1);
				MetricTemplate tmpl3 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_3", user1);
				try
				{
					user1.save(conn);
					conn.commit();

					res = _peer.getRootMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							!res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()) && !res.contains(tmpl3.getId()));

					tmpl1.save(conn);
					conn.commit();

					res = _peer.getRootMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()) && !res.contains(tmpl3.getId()));

					tmpl2.save(conn);
					tmpl3.save(conn);
					conn.commit();

					res = _peer.getRootMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							res.contains(tmpl1.getId()) && res.contains(tmpl2.getId()) && res.contains(tmpl3.getId()));
				} finally
				{
					try
					{
						delete(tmpl1, conn);
						delete(tmpl2, conn);
						delete(tmpl3, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testGetRootMetricIdsWithInstancies()
			throws Exception
	{
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{

				Set res = null;

				try
				{
					res = _peer.getRootMetricIds(null, true, conn);
					fail("Null resource id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null resource id parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getRootMetricIds(contextId, true, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getRootMetricIds(FAKE_ID, true, conn);
				assertNotNull("Expecting empty result set, not null.", res);
				assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("fake1_withInstancies@email.add_2", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1_22", user1);
				Work work = null;
				MetricInstance inst = MetricInstance.createNew(tmpl1, null, false);
				try
				{
					user1.save(conn);
					conn.commit();
					tmpl1.save(conn);
					conn.commit();
					res = _peer.getRootMetricIds(contextId, true, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							!res.contains(tmpl1.getId()));

					work = createWork("_TestMetricTemplate_GetRootMetricIdsWithInstancie_", user1, conn);
					conn.commit();
					inst.setLinkedProjectId(work.getId());
					inst.save(conn);
					conn.commit();
					res = _peer.getRootMetricIds(contextId, true, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							res.contains(tmpl1.getId()));
				} finally
				{
					try
					{
						delete(inst, conn);
						conn.commit();
						delete(tmpl1, conn);
						conn.commit();
						delete(work, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	private Work createWork(String name, User user)
	{
		final Work work = Work.createNew(Work.TYPE, name, user);
		return work;
	}

	private Work createWork(String name, User user, Connection conn) throws SQLException
	{
		Work work = createWork(name, user);
		work.save(conn);
		return work;
	}


	public void testGetInstance() throws Exception
	{
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{

				Map res = null;

				try
				{
					res = _peer.getInstance(null, FAKE_ID, conn);
					fail("Null template id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null template id parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getInstance(FAKE_ID, null, conn);
					fail("Null object id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null object id parameter should throw IllegalArgumentException.");
				}
				try
				{
					res = _peer.getInstance(FAKE_ID, FAKE_ID, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getInstance(FAKE_ID, FAKE_ID, conn);
				assertNotNull("Expecting empty result set, not null.", res);
				assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("fake1@email.add_05", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				Work work1 = Work.createNew(Work.TYPE, "testMT_work_1", user1);
				work1.generateAlerts(false);
				Work work2 = Work.createNew(Work.TYPE, "testMT_work_2", user1);
				work2.generateAlerts(false);
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_2", user1);
				MetricInstance ins1 = MetricInstance.createNew(tmpl1, null, false);
				MetricInstance ins2 = MetricInstance.createNew(tmpl2, null, false);

				try
				{
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();
					tmpl1.save(conn);
					tmpl2.save(conn);
					conn.commit();

					res = _peer.getInstance(tmpl1.getId(), work1.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 0);
					res = _peer.getInstance(tmpl1.getId(), work2.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 0);
					res = _peer.getInstance(tmpl2.getId(), work1.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 0);

					ins1.setLinkedProjectId(work1.getId());
					ins2.setLinkedProjectId(work2.getId());
					ins1.save(conn);
					ins2.save(conn);
					conn.commit();

					res = _peer.getInstance(tmpl1.getId(), work1.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
					assertTrue("Expecting another items in result set.",
							res.get(work1.getId()) == ins1.getId());


					res = _peer.getInstance(tmpl2.getId(), work2.getId(), conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);

				} finally
				{
					try
					{
						delete(ins1, conn);
						delete(ins2, conn);

						conn.commit();
						delete(tmpl1, conn);
						delete(tmpl2, conn);
						conn.commit();
						delete(work1, conn);
						delete(work2, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testGetWasImportedMetricIds() throws Exception
	{
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{

				List res = null;

				try
				{
					_peer.getWasImportedMetricIds(null, conn);
					fail("Null resource id parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null resource id parameter should throw IllegalArgumentException.");
				}
				try
				{
					_peer.getWasImportedMetricIds(contextId, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				res = _peer.getWasImportedMetricIds(FAKE_ID, conn);
				assertNotNull("Expecting empty result set, not null.", res);
				assertTrue("Expecting empty result set with fake resource id. got: " + res.size(), res.isEmpty());

				User user1 = User.createNewUser("fake1@email.add_06", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_2", user1);
				MetricTemplate tmpl3 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_3", user1);
				try
				{
					user1.save(conn);
					conn.commit();
					tmpl1.save(conn);
					tmpl2.save(conn);
					tmpl3.save(conn);
					conn.commit();

					res = _peer.getWasImportedMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							!res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()) && !res.contains(tmpl3.getId()));

					tmpl1.setWasImported(true);
					tmpl1.save(conn);
					conn.commit();

					res = _peer.getWasImportedMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()) && !res.contains(tmpl3.getId()));

					tmpl2.setWasImported(false);
					tmpl3.setWasImported(true);
					tmpl2.save(conn);
					tmpl3.save(conn);
					conn.commit();
					res = _peer.getWasImportedMetricIds(contextId, conn);
					assertNotNull("Expecting not empty result set, not null.", res);
					assertTrue("Expecting another items in result set.",
							res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()) && res.contains(tmpl3.getId()));
				} finally
				{
					try
					{
						delete(tmpl1, conn);
						delete(tmpl2, conn);
						delete(tmpl3, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	private boolean compareObj(Object obj1, Object obj2)
	{
		if (obj1 == null && obj2 == null)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		return obj1.equals(obj2);
	}

	private boolean compareTS(Timestamp ts1, Timestamp ts2)
	{
		if (ts1 == null && ts2 == null)
			return true;
		if (ts1 == null || ts2 == null)
			return false;
		GregorianCalendar gc1 = new GregorianCalendar();
		gc1.setTime(new Date(ts1.getTime()));
		gc1.set(GregorianCalendar.MILLISECOND, 0);
		GregorianCalendar gc2 = new GregorianCalendar();
		gc2.setTime(new Date(ts2.getTime()));
		gc2.set(GregorianCalendar.MILLISECOND, 0);
		return gc1.equals(gc2);
	}

	private String compareMT(MetricTemplate mt1, MetricTemplate mt2)
	{
		String res = "";
		if (!compareObj(mt1.getOwnerId(), mt2.getOwnerId()))
		{
			res += "owner_id, ";
		}
		if (!compareObj(mt1.getDelegatedOwnerId(), mt2.getDelegatedOwnerId()))
		{
			res += "delegated_owner_id, ";
		}
		if (!compareObj(mt1.getPeriod(), mt2.getPeriod()))
		{
			res += "period, ";
		}
		if (!compareTS(mt1.getStartDate(), mt2.getStartDate()))
		{
			res += "start_date, ";
		}
		if (!compareTS(mt1.getEndDate(), mt2.getEndDate()))
		{
			res += "end_date, ";
		}
		if (mt1.permitsRollup() != mt2.permitsRollup())
		{
			res += "permits_rollup, ";
		}
		if (mt1.hasViews() != mt2.permitsRollup())
		{
			res += "has_views, ";
		}
		if (mt1.hasPhases() != mt2.hasPhases())
		{
			res += "has_phases, ";
		}
		if (!compareObj(mt1.getRemind(), mt2.getRemind()))
		{
			res += "remind, ";
		}
		if (!compareObj(mt1.getDefaultForwardPeriods(), mt2.getDefaultForwardPeriods()))
		{
			res += "dflt_forward_prds, ";
		}
		if (!compareObj(mt1.getDefaultBackwardPeriods(), mt2.getDefaultBackwardPeriods()))
		{
			res += "dflt_backward_prds, ";
		}
		if (!compareObj(mt1.getDefaultTotalFrequency(), mt2.getDefaultTotalFrequency()))
		{
			res += "dflt_total_frqncy, ";
		}
		if (!compareObj(mt1.getDefaultMoneyDisplay(), mt2.getDefaultMoneyDisplay()))
		{
			res += "dflt_money_display ";
		}
		return res;
	}

	private void changeMT(MetricTemplate mt)
	{
		mt.setPeriod(MetricPeriodCode.QUARTERLY);
		mt.setStartDate(new Timestamp(System.currentTimeMillis() - 24L * 60L * 60L * 1000L));
		mt.setEndDate(new Timestamp(System.currentTimeMillis() + 24L * 60L * 60L * 1000L));
		mt.setPermitsRollup(!mt.permitsRollup());
		mt.setHasViews(!mt.hasViews());
		mt.setHasPhases(!mt.hasPhases());
		mt.setDefaultForwardPeriods(new Integer(4));
		mt.setDefaultBackwardPeriods(new Integer(4));
		mt.setDefaultTotalFrequency(MetricPeriodCode.YEARLY);
		mt.setDefaultMoneyDisplay(mt.getDefaultMoneyDisplay() + "_pr");
	}

	public void testInsertUpdateDelete() throws Exception
	{
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{

			protected Object query(Connection conn) throws SQLException
			{
				try
				{
					_peer.insert(null, conn);
					fail("Null metric template parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null metric template parameter should throw IllegalArgumentException.");
				}
				try
				{
					_peer.update(null, conn);
					fail("Null metric template parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null metric template parameter should throw IllegalArgumentException.");
				}
				try
				{
					_peer.delete(null, conn);
					fail("Null metric template parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null metric template parameter should throw IllegalArgumentException.");
				}

				User user1 = User.createNewUser("fake1@email.add_07", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				MetricTemplate tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ", user1);
				MetricTemplate tmpl2 = null;

				try
				{
					_peer.insert(tmpl, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}
				try
				{
					_peer.update(tmpl, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}
				try
				{
					_peer.delete(tmpl, null);
					fail("Null connection parameter should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection parameter should throw IllegalArgumentException.");
				}

				try
				{
					user1.save(conn);
					conn.commit();

					try
					{
						_peer.restore(tmpl, conn);
						fail("Null connection parameter should throw EmptyResultSetException.");
					} catch (EmptyResultSetException ok)
					{
					} catch (Exception e)
					{
						fail("Null connection parameter should throw EmptyResultSetException.");
					}
					try
					{
						_peer.delete(tmpl, conn);
					} catch (Exception e)
					{
						fail("Delete not existing MetricTemplate should not throw an exception.");
					}

					_peer.insert(tmpl, conn);
					conn.commit();

					tmpl2 = (MetricTemplate) MetricTemplate.get(tmpl.getId(), context);
					assertNotNull("Expecting not null metric template.", tmpl2);
					String res = compareMT(tmpl, tmpl2);
					assertTrue("Expecting another metric template contains. got differ " + res, res.length() == 0);

					changeMT(tmpl);
					_peer.update(tmpl, conn);
					conn.commit();
					_peer.restore(tmpl2, conn);

					assertNotNull("Expecting not null metric template.", tmpl2);
					res = compareMT(tmpl, tmpl2);
					assertTrue("Expecting another metric template contains. got differ " + res, res.length() == 0);

					_peer.delete(tmpl, conn);
					conn.commit();
					try
					{
						_peer.restore(tmpl2, conn);
						fail("Null connection parameter should throw EmptyResultSetException.");
					} catch (EmptyResultSetException ok)
					{
					} catch (Exception e)
					{
						fail("Null connection parameter should throw EmptyResultSetException.");
					}
				} finally
				{
					try
					{
						delete(tmpl, conn);
						conn.commit();
						delete(user1, conn);
						conn.commit();
					} catch (SQLException ignored)
					{
						if (DEBUG) ignored.printStackTrace();
					}
				}
				return null;
			}
		}.execute();
	}

	public void testGetMetricIdByCode()
			throws Exception
	{
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this)
		{
			protected Object query(Connection conn) throws SQLException
			{
				String fake_code = "testMT_fake_code";

				try
				{
					_peer.getMetricIdByCode(null, FAKE_ID, conn);
					fail("Null code should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null code should throw IllegalArgumentException.");
				}
				try
				{
					_peer.getMetricIdByCode(fake_code, null, conn);
					fail("Null context id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null context id should throw IllegalArgumentException.");
				}
				try
				{
					_peer.getMetricIdByCode(fake_code, FAKE_ID, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection should throw IllegalArgumentException.");
				}

				PersistentKey res = _peer.getMetricIdByCode(fake_code, FAKE_ID, conn);
				assertTrue("Expecting null result for fake context.", res == null);

				User user1 = User.createNewUser("fake1@email.add_09", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user_1");
				MetricTemplate tmpl = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ", user1);
				tmpl.setCode(fake_code);

				try
				{
					res = _peer.getMetricIdByCode(fake_code, contextId, conn);
					assertTrue("Expecting null result for fake code.", res == null);

					user1.save(conn);
					conn.commit();
					tmpl.save(conn);
					conn.commit();

					res = _peer.getMetricIdByCode(fake_code, contextId, conn);
					assertTrue("Expecting another result.", res.equals(tmpl.getId()));
				} finally
				{
					delete(tmpl, conn);
					conn.commit();
					delete(user1, conn);
					conn.commit();
				}
				return null;
			}
		}.execute();
	}

	public void testFindTemplateIdsByPSType()
			throws Exception
	{
		final InstallationContext context = getContext();
		final PersistentKey contextId = context.getId();
		new JdbcQuery(this)
		{
			protected Object query(Connection conn) throws SQLException
			{
				List types = new java.util.ArrayList();
				types.add(MasterTask.TYPE);

				try
				{
					_peer.findTemplateIdsByPSType(contextId, null, conn);
					fail("Null types list should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null types list should throw IllegalArgumentException.");
				}
				try
				{
					_peer.findTemplateIdsByPSType(null, types, conn);
					fail("Null context id should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null context id should throw IllegalArgumentException.");
				}
				try
				{
					_peer.findTemplateIdsByPSType(contextId, types, null);
					fail("Null connection should throw IllegalArgumentException.");
				} catch (IllegalArgumentException ok)
				{
				} catch (Exception e)
				{
					fail("Null connection should throw IllegalArgumentException.");
				}

				List res = _peer.findTemplateIdsByPSType(FAKE_ID, types, conn);
				assertNotNull("Expecting empty result list, not null", res);
				assertTrue("Expecting empty result list. got: " + res.size(),
						res.isEmpty());

				User user1 = User.createNewUser("fake1@email.add_010", context);
				user1.generateAlerts(false);
				user1.setLastName("testMT_user");
				MetricTemplate tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_1", user1);
				MetricTemplate tmpl2 = MetricTemplate.createNew(MetricTemplate.TYPE, "testMT_templ_2", user1);
				Work work1 = Work.createNew(Work.TYPE, "testMT_work_1", user1);
				Work work2 = Work.createNew(Work.TYPE, "testMT_work_2", user1);
				Work work3 = Work.createNew(MasterTask.TYPE, "testMT_work_3", user1);
				work3.setParentWork(work2, user1);

				MetricInstance ins1 = MetricInstance.createNew(tmpl1, work1, false);
				MetricInstance ins2 = MetricInstance.createNew(tmpl2, work2, false);
				MetricInstance ins3 = MetricInstance.createNew(tmpl1, work3, false);
				try
				{
					user1.save(conn);
					conn.commit();
					work1.save(conn);
					work2.save(conn);
					conn.commit();
					work3.save(conn);
					tmpl1.save(conn);
					tmpl2.save(conn);
					conn.commit();
					ins1.save(conn);
					ins2.save(conn);
					ins3.save(conn);
					conn.commit();

					res = _peer.findTemplateIdsByPSType(contextId, types, conn);
					assertNotNull("Expecting empty result list, not null", res);
					assertTrue("Expecting empty result list.",
							res.contains(tmpl1.getId()) && !res.contains(tmpl2.getId()));

					types.add(Work.TYPE);

					res = _peer.findTemplateIdsByPSType(contextId, types, conn);
					assertNotNull("Expecting empty result list, not null", res);
					assertTrue("Expecting empty result list.",
							res.contains(tmpl1.getId()) && res.contains(tmpl2.getId()));
				} finally
				{
					delete(ins1, conn);
					delete(ins2, conn);
					delete(ins3, conn);
					conn.commit();
					delete(work3, conn);
					conn.commit();
					delete(work1, conn);
					delete(work2, conn);
					conn.commit();
					delete(tmpl1, conn);
					delete(tmpl2, conn);
					conn.commit();
					delete(user1, conn);
					conn.commit();
				}
				return null;
			}
		}.execute();
	}

}
