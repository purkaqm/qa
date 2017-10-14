package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.*;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.util.search.GeneralSearchDiscussionItem;
import com.cinteractive.ps3.work.UpdateFrequency;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkStatus;
import com.cinteractive.ps3.work.WorkUtil;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class TestSearchSqlDiscussionItem extends TestJdbcPeer {
	private SearchSqlBase _peer;

    static{
	registerCase();
    }

    private static void registerCase( ){
	TestSql.registerTestCase(SearchSqlDiscussionItem.class.getName(), TestSearchSqlDiscussionItem.class.getName());
    }

    public TestSearchSqlDiscussionItem(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestSearchSqlDiscussionItem( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
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
		_peer = (SearchSqlBase) PeerRegistry.getPeer( id , GeneralSearchDiscussionItem.class.getName());

        if( _peer == null )
            throw new NullPointerException( "Null SearchSqlDiscussionItem peer instance." );

    }

    public void tearDown() throws Exception
	{
        super.tearDown();
    }


	public void testWithNewData() throws Exception
	{
		final InstallationContext context = getContext();
		final Timestamp today = new Timestamp(new java.util.Date().getTime());

		final Set incTypes = new HashSet();
		incTypes.add(DiscussionItem.TYPE);

		final GeneralSearchDiscussionItem gs = new GeneralSearchDiscussionItem(User.getNobody(context), incTypes, context);

		new JdbcQuery( this )
		{
			protected Object query(Connection conn) throws SQLException
			{
				try
				{
					conn.setAutoCommit(false);

					Work work = Work.createNew(Work.TYPE, "__TestSearchSQLDiscussionItem_WithNewData_", User.getNobody(context));
					work.setUpdateFrequency(UpdateFrequency.WEEKLY);
					work.setStatus(WorkStatus.ON_TRACK);
					work.save(conn);

					DiscussionItem item = DiscussionItem.createNew( WorkUtil.getNoProject(context), null, User.getNobody(context), "_TestSearchSQLDiscussionItem_title", "__TestSearchSQLDiscussionItem_text" );
					item.setLastChangeDate(today);
					item.save(conn);

					DiscussionItem item2 = DiscussionItem.createNew( WorkUtil.getNoProject(context), null, User.getNobody(context), "_TestSearchSQLDiscussionItem2_title", "__TestSearchSQLDiscussionItem2_text" );
					item2.setLastChangeDate(today);
					item2.save(conn);

					gs.setSearchString("TestSearchSQLDiscussionItem");
					gs.setIsFullText(false);

					List res;
					try {
						res = _peer.executeSearch(gs, conn);
						assertTrue("Result set with returned " + res.size() + " records instead of 2 records", res.size() >=2 );
					} catch (FullTextSearchNotAvailableException ignored) {
						System.out.println("Warning!!! Full-text search engine is not available. Skipping assertion.");
					}


					//try to use  FullText
					//there is NO ability to test FUllText search with search string different from '%' because we can nether
					//know whether full text index has already been reindexed or not.

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