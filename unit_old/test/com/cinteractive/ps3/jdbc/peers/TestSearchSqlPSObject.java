package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.FullTextSearchNotAvailableException;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.PeerRegistry;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.Document;
import com.cinteractive.ps3.entities.Admins;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.util.search.GeneralSearchPSObject;
import com.cinteractive.ps3.work.MasterTask;
import com.cinteractive.ps3.work.UpdateFrequency;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;


public class TestSearchSqlPSObject extends TestJdbcPeer {

	private SearchSqlBase _peer;

	static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
	}

	private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(SearchSqlPSObject.class.getName(), TestSearchSqlPSObject.class.getName());
	}

	public TestSearchSqlPSObject(String name) {
		super(name);
	}

	private static void addTest(TestSuite suite, String testName) {
		suite.addTest( new TestSearchSqlPSObject( testName ) );
	}

	public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite();
		addTest( suite, "testWithoutNewData" );
		addTest( suite, "testWithNewData" );

		return suite;
	}

	public static Test suite() {
		return setUpDb( bareSuite() );
	}

	public void setUp() throws Exception
	{
		super.setUp();

		DataSourceId id = (DataSourceId) new JdbcQuery( getContext() )
		{
			protected Object query(Connection conn) throws SQLException
			{
				return ((JdbcConnection) conn).getDataSourceId();
			}
		}.execute();
		_peer = (SearchSqlBase) PeerRegistry.getPeer( id , GeneralSearchPSObject.class.getName());

		if( _peer == null )
			throw new NullPointerException( "Null SearchSqlPSObject peer instance." );

	}

	public void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testWithoutNewData() throws Exception
	{
		final InstallationContext context = getContext();
		final Set incTypes = new HashSet();
		incTypes.add(Work.TYPE);
		incTypes.add(Document.TYPE);
		//if( context.showFeature( "resources" ) )
		//	incTypes.add(InternalResource.TYPE);
		incTypes.add(User.TYPE);
		incTypes.add(Group.TYPE);
		incTypes.add(MasterTask.TYPE);

		final GeneralSearchPSObject gs = new GeneralSearchPSObject(Nobody.get(context), incTypes, context);
		gs.setSearchString("%");
		gs.setIsFullText(false);

		new JdbcQuery( this )
		{
			protected Object query(Connection conn) throws SQLException
			{
				try
				{
					List res;
					try {
						res = _peer.executeSearch(gs, conn);
						assertTrue("Empty result set returned(Simple search) for user with admin rights", res.size() > 0);
					} catch (FullTextSearchNotAvailableException ignored) {
						System.out.println("Warning!!! Full-text search engine is not available. Skipping assertion.");
					}

					//try to use  FullText
					gs.setSearchString("%");
					gs.setIsFullText(true);
					try {
						res = _peer.executeSearch(gs, conn);
						//in order to current user is admin result of search must not be empty
						assertTrue("Empty result set returned(Full text search) for user with admin rights", res.size() > 0);
					} catch (FullTextSearchNotAvailableException ignored) {
						System.out.println("Warning!!! Full-text search engine is not available. Skipping assertion.");
					}


				}finally
				{
				}
				return null;
			}
		}.execute();

	}
	public void testWithNewData() throws Exception
	{
		final InstallationContext context = getContext();
		final Set incTypes = new HashSet();
		incTypes.add(Work.TYPE);
		incTypes.add(User.TYPE);

		final GeneralSearchPSObject gs = new GeneralSearchPSObject(Nobody.get(context), incTypes, context);
		final Group admins = Admins.get(context);

		new JdbcQuery( this )
		{
			protected Object query(Connection conn) throws SQLException
			{
				try
				{
					conn.setAutoCommit(false);
					User user = User.createNewUser("__TestSearchSQLPSObject_WithNewData_email1__", context);
					user.setLogin("__TestSearchSQLPSObject_WithNewData__testlogin1");
					user.setFirstName("TestSearchSQLPSObjec");//Entity.first_name(varchar 20)
					user.save(conn);
					admins.addUser(user);
					admins.save(conn);

					Work work = Work.createNew(Work.TYPE, "__TestSearchSQlPSObject_WithNewData_", Nobody.get(context));
					work.setUpdateFrequency(UpdateFrequency.WEEKLY);
					work.setStatus(WorkStatus.ON_TRACK);
					work.save(conn);

					gs.setSearchString("%TestSearchSQLPSObjec%");
					gs.setIsFullText(false);

					List res;
					try {
						res = _peer.executeSearch(gs, conn);
						assertTrue("Result set with returned " + res.size() + " records instead of 2 records", res.size() >= 2);
					} catch (FullTextSearchNotAvailableException ignored) {
						System.out.println("Warning!!! Full-text search engine is not available. Skipping assertion.");
					}


					//try to use  FullText
					//there is NOT ability to test FUllText search with search string different from '%' because we can nether
					//know whether full text index has already been reindexed or no.

				}finally
				{
					if (conn != null)
					{
						conn.rollback();
						conn.setAutoCommit(true);
					}
				}
				return null;
			}
		}.execute();

	}



}