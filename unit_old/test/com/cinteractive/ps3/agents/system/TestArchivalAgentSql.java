/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.agents.system;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestArchivalAgentSql extends TestJdbcPeer
{
	private ArchivalAgentSql _peer;

	static{
	    registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
	}

	private static void registerCase(DataSourceId id){
	    TestSql.registerTestCase(ArchivalAgentSql.class.getName(), TestArchivalAgentSql.class.getName());
	}

	public TestArchivalAgentSql( String name ) { super( name ); }


	public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite( "TestArchivalAgentSql" );
		suite.addTest( new TestArchivalAgentSql( "testFindWorkIds" ) );
		return suite;
	}

	public static Test suite()
	{
		return setUpDb( bareSuite() );
	}


	public void setUp()
	throws Exception
	{
		super.setUp();

		_peer = (ArchivalAgentSql) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return ArchivalAgentSql.getInstance( conn );
			}
		}.execute();
		if( _peer == null )
			throw new NullPointerException( "Null ArchivalAgentSql peer instance." );
	}

	public void tearDown()
	throws Exception
	{
		super.tearDown();
		_peer = null;
	}

	public void testFindWorkIds()
	throws Exception
	{
		Set ids = (Set) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return _peer.findArchivableWorkIds( FAKE_ID, new Date(), conn );
			}
		}.execute();
		assertNotNull( "Expecting empty work ids for no results; got null.", ids );
		assertTrue( "Expecting empty work ids for no results; got '" + ids + "'.", ids.isEmpty() );

		new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				String idString = null;

				final Statement stmt = conn.createStatement();
				final ResultSet rset = stmt.executeQuery( "SELECT MAX(object_id) FROM Object WHERE object_type = 'Work'" );
				if( rset.next() )
					idString = rset.getString( 1 );
				rset.close();
				stmt.close();

				return idString == null ? null : _peer.findArchivableWorkIds( new StringPersistentKey( idString ), new Date(), conn );
			}
		}.execute();
	}
}
