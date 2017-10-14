package com.cinteractive.ps3.jdbc.peers;

/**
 * Title:        PS3
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      SoftDev
 * @author
 * @version 1.0
 */

import com.cinteractive.database.CIResultSet;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.CIParameterizedStatement;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.pstransactions.ActivityCodes;
import com.cinteractive.ps3.pstransactions.PSTransaction;
import com.cinteractive.ps3.pstransactions.TimeProject;
import com.cinteractive.ps3.work.Work;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestTimeProjectPeer extends TestJdbcPeer {

    private TimeProjectPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(TimeProjectPeer.class.getName(), TestTimeProjectPeer.class.getName());
    }

    protected TestTimeProjectPeer(String name) { super(name); }

    public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite( "TestPSTransactionPeer" );

		suite.addTest( new TestTimeProjectPeer( "testUserDeleted" ) );
		suite.addTest( new TestTimeProjectPeer( "testInsertDelete" ) );

		return suite;
	}

    public void testGetInstance() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertDelete */ }
    public void testUpdate() throws Exception { /** equals Insert */ }
    public void testDelete() throws Exception { /** see testInsertDelete */ }

    public void setUp() throws Exception
	{
		super.setUp();

		_peer = (TimeProjectPeer) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return TimeProjectPeer.getInstance(conn);
			}
		}.execute();
		if( _peer == null )
			throw new NullPointerException( "Null TimeProjectPeer instance." );
	}

    public static Test suite()
	{
		return setUpDb( bareSuite() );
	}

    public void tearDown() throws Exception
	{
		super.tearDown();
		_peer = null;
	}

    private static final boolean DEBUG = true; //show debug info

	private final void delete(JdbcPersistableBase o, Connection conn)
	{
		try {
			if (o != null) {
				//if (o instanceof PSObject)
					//((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		}
		catch (Exception ignored) {
			if (DEBUG)
				ignored.printStackTrace();
		}
	}

    private final void deleteTimeProject(TimeProject o, Connection conn) {
		try {
			if (o != null) {
				TimeProjectPeer.getInstance( conn ).deleteSelf( o.getId() , conn );
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
    }


    private static final String COUNT_TIME_PROJECT_INSTANCE =
	    "Select count(id) from TimeProject " +
	    "Where user_id = ? " +
	    "AND project_id = ? ";

    private static final int countTimeProject( PersistentKey userId,
	    PersistentKey projectId,  Connection conn) throws SQLException
	{
	final CIParameterizedStatement pstmt = ( (JdbcConnection) conn ).createParameterizedStatement();
	pstmt.setSql( COUNT_TIME_PROJECT_INSTANCE );
	pstmt.setString( 1, userId.toString() );
	pstmt.setString( 2, projectId.toString() );

	final CIResultSet rset = new CIResultSet( pstmt.executeQuery() );

	int res = 0;
	if (rset.next()) {
	    res = rset.getInt(1);
	    }

	rset.close();
	pstmt.close();

	return res;
	}


    public void testUserDeleted() throws Exception
	{
        final InstallationContext context = getContext();
        new JdbcQuery( this )
            {
            protected Object query(Connection conn) throws SQLException
                {
                try {
                    _peer.userDeleted(null, conn);
                    fail( "Null activity Id should throw IllegalArgumentException." );
                    }
                catch( IllegalArgumentException ok ) {}
                catch( Exception e ){
                    fail( "Null activity Id should throw IllegalArgumentException." );
                    }
		try {
                    _peer.userDeleted(FAKE_ID, null);
                    fail( "Null connection should throw IllegalArgumentException." );
                    }
                catch( IllegalArgumentException ok ) {}
                catch( Exception e ){
                    fail( "Null connection should throw IllegalArgumentException." );
                    }

		int res = _peer.userDeleted(FAKE_ID, conn);
		conn.commit();
		assertTrue("Expecting 0 items deleted. got: "+res, res==0);

		User user1 = User.createNewUser("testTimeProj_email1", context);
		user1.setLastName("testTimeProj_usr1");
		User user2 = User.createNewUser("testTimeProj_email2", context);
		user2.setLastName("testTimeProj_usr2");
		Work work1 = Work.createNew(Work.TYPE, "testTimeProj_wrk1", user1);
		Work work2 = Work.createNew(Work.TYPE, "testTimeProj_wrk2", user1);

		TimeProject tp1 = TimeProject.createNew(user1.getId(), work1.getId(),
		         true);
		TimeProject tp2 = TimeProject.createNew(user1.getId(), work2.getId(),
		         true);
		TimeProject tp3 = TimeProject.createNew(user2.getId(), work2.getId(),
		         true);
		try
                    {
		    user1.save(conn);
		    user2.save(conn);
		    conn.commit();
		    work1.save(conn);
		    work2.save(conn);
		    conn.commit();
		    tp1.save(conn);
		    tp2.save(conn);
		    tp3.save(conn);
		    conn.commit();

		    res = _peer.userDeleted(user1.getId(), conn);
		    conn.commit();
		    assertTrue("Expecting 2 items deleted. got: "+res, res==2);
		    assertTrue("Deleting not complite",
		        countTimeProject(user1.getId(), work1.getId(),
			     conn) == 0);
		    assertTrue("Deleting not complite",
		        countTimeProject(user1.getId(), work1.getId(),
			     conn) == 0);
		    res = _peer.userDeleted(user2.getId(), conn);
		    conn.commit();
		    assertTrue("Expecting 1 items deleted. got: "+res, res==1);
		    assertTrue("Deleting not complite",
		        countTimeProject(user2.getId(), work2.getId(),
			     conn) == 0);
                    }
                finally
		    {
		    deleteTimeProject(tp1, conn);
		    deleteTimeProject(tp2, conn);
		    deleteTimeProject(tp3, conn);
		    conn.commit();
		    delete(work1, conn);
		    delete(work2, conn);
		    delete(user1, conn);
		    delete(user2, conn);
                    }
                return null;
                }
            }.execute();
        }


    public void testInsertDelete() throws Exception
	{
        final InstallationContext context = getContext();
        new JdbcQuery( this )
            {
            protected Object query(Connection conn) throws SQLException
                {
		int res = _peer.deleteSelf(FAKE_ID, conn);
		assertTrue("Expecting 0 items deleted. got: "+res, res==0);

		User user1 = User.createNewUser("testTimeProj_email1", context);
		user1.setLastName("testTimeProj_usr1");
		Work work1 = Work.createNew(Work.TYPE, "testTimeProj_wrk1", user1);

                ActivityCodes ac = ActivityCodes.get(context);
                String tagName1 = "testTimeProj_actag1";
		String tagName2 = "testTimeProj_actag2";
                ac.addTag(tagName1);
		ac.addTag(tagName2);
                PersistentKey fake_tag_id1 = ac.getTag(tagName1).getId();
		PersistentKey fake_tag_id2 = ac.getTag(tagName2).getId();
		TimeProject tp1 = null;

		long base = System.currentTimeMillis();
		Timestamp today = new Timestamp(base);
		Timestamp tomorrow = new Timestamp(base + 24L * 60L * 60L * 1000L );
		Timestamp yesterday = new Timestamp(base - 24L * 60L * 60L * 1000L );

		PSTransaction tr1 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
		tr1.setTransactionDate(today);
		tr1.setActivityId(fake_tag_id1);
		PSTransaction tr2 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
		tr2.setTransactionDate(tomorrow);
		tr2.setActivityId(fake_tag_id1);
		PSTransaction tr3 = null;//PSTransaction.createCostEstimate(new Double(2), user1, work1);
		tr3.setTransactionDate(today);
		tr3.setActivityId(fake_tag_id1);
		PSTransaction tr4 = null;//PSTransaction.createTimeEntry(new Double(2), user1, work1);
		tr4.setTransactionDate(yesterday);
		tr4.setActivityId(fake_tag_id1);
                try
                    {
		    user1.save(conn);
		    conn.commit();
		    work1.save(conn);
		    ac.save(conn);
		    conn.commit();
		    tr1.save(conn);
		    tr2.save(conn);
		    tr3.save(conn);
		    tr4.save(conn);
		    conn.commit();

		    tp1 = TimeProject.createNew(user1.getId(), work1.getId(),
		         true);

		    _peer.insert(tp1, conn);
		    conn.commit();
		    assertTrue("Inserting not complite",
		        countTimeProject(user1.getId(), work1.getId(),
			     conn) == 1);
		    _peer.deleteSelf(tp1.getId(), conn);
		    conn.commit();
                    }
                finally
		    {
		    delete(tr1, conn);
		    delete(tr2, conn);
		    delete(tr3, conn);
		    delete(tr4, conn);
		    deleteTimeProject(tp1, conn);
		    ac.removeTag(fake_tag_id1);
		    ac.removeTag(fake_tag_id2);
		    ac.save(conn);
		    conn.commit();
		    delete(work1, conn);
		    delete(user1, conn);
                    }
                return null;
                }
            }.execute();
	}
}