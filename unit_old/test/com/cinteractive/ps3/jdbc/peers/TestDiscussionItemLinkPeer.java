package com.cinteractive.ps3.jdbc.peers;

/**
 * Title:        PS3
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      SoftDev
 * @author
 * @version 1.0
 */

import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.jdbc.CIParameterizedStatement;
import com.cinteractive.database.CIResultSet;
import com.cinteractive.database.TwoStringPersistentKey;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.discussion.DiscussionItemLink;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.PSObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.sql.SQLException;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestDiscussionItemLinkPeer extends TestJdbcPeer {

    private DiscussionItemLinkPeer _peer;

    static{
	    registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
	}

    private static void registerCase(DataSourceId id){
	    TestSql.registerTestCase(DiscussionItemLinkPeer.class.getName(), TestDiscussionItemLinkPeer.class.getName());
	}

    public TestDiscussionItemLinkPeer( String name ) { super( name ); }

    public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite( "TestDiscussionItemLinkPeer" );

		suite.addTest( new TestDiscussionItemLinkPeer( "testFindDiscussionItemLinksByObjectId" ) );
		suite.addTest( new TestDiscussionItemLinkPeer( "testInsertDelete" ) );

		return suite;
	}

    public void testGetInstance() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }

    public void setUp()
	throws Exception
	{
		super.setUp();

		_peer = (DiscussionItemLinkPeer) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return DiscussionItemLinkPeer.getInstance(conn);
			}
		}.execute();
		if( _peer == null )
			throw new NullPointerException( "Null InternalResourcePeer instance." );
	}
    public static Test suite()
	{
		return setUpDb( bareSuite() );
	}

    public void tearDown()
	throws Exception
	{
		super.tearDown();
		_peer = null;
	}

    private static final boolean DEBUG = true; //show debug info

    private final void delete(JdbcPersistableBase o, Connection conn) {
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


    private boolean testResult(CIResultSet res, List links)
	throws SQLException
	{
	    List inLinks = new java.util.ArrayList(links);
	    while (res.next())
	    {
		for (Iterator i = inLinks.iterator(); i.hasNext(); )
		{
		    DiscussionItemLink dil = (DiscussionItemLink) i.next();
		    if ( dil.getObjectId().toString().equalsIgnoreCase(res.getString(1)) &&
			dil.getItemId().toString().equalsIgnoreCase(res.getString(2)) &&
			dil.getDiscussionId().toString().equalsIgnoreCase(res.getString(3))
			 )
			{
			i.remove();
		        break;
			}
		}
	    }
	    return inLinks.isEmpty();
	}


    public void testFindDiscussionItemLinksByObjectId()
	throws Exception
	{
	    final InstallationContext context = getContext();
	    new JdbcQuery( this )
		{
		protected Object query(Connection conn) throws SQLException
		    {
		    CIParameterizedStatement pstmt = ( (JdbcConnection) conn ).createParameterizedStatement();
		    try {
                        _peer.findDiscussionItemLinksByObjectId(null, pstmt);
                        fail( "Null object id should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null object id should throw IllegalArgumentException." );
                        }
		    try {
                        _peer.findDiscussionItemLinksByObjectId(FAKE_ID, null);
                        fail( "Null statement should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null statement should throw IllegalArgumentException." );
                        }

		    CIResultSet res = _peer.findDiscussionItemLinksByObjectId(FAKE_ID, pstmt);
		    assertNotNull("Expecting not null result.", res);
		    assertTrue("Expecting empty result for fake params.", ! res.next());
		    res.close();
		    pstmt.close();

		    User user1 = User.createNewUser("testDiscussionItemLink_usr1", context);
                    user1.setLastName("testDiscussionItemLink_usr1");
		    Work work1 = Work.createNew(Work.TYPE, "testDiscussionItemLink_wrk1", user1);
		    Work work2 = Work.createNew(Work.TYPE, "testDiscussionItemLink_wrk2", user1);
		    Work work3 = Work.createNew(Work.TYPE, "testDiscussionItemLink_wrk3", user1);
		    DiscussionItem di1 = DiscussionItem.createNew( work1, null, user1, "title1", "text1");
		    DiscussionItem di2 = DiscussionItem.createNew( work2, null, user1, "title2", "text2");
		    DiscussionItemLink diLink1 = null;
		    DiscussionItemLink diLink2 = null;
		    try
			{
			user1.save(conn);
			conn.commit();
			work1.save(conn);
			work2.save(conn);
			work3.save(conn);
			conn.commit();
			di1.save(conn);
			di2.save(conn);
			conn.commit();

			pstmt = ( (JdbcConnection) conn ).createParameterizedStatement();
			res = _peer.findDiscussionItemLinksByObjectId(work3.getId(), pstmt);
		        assertNotNull("Expecting not null result.", res);
		        assertTrue("Expecting empty result.", ! res.next());
			res.close();
			pstmt.close();

			TwoStringPersistentKey twoPK1 = new TwoStringPersistentKey(
			    work3.getId().toString(),
			    di1.getId().toString());
			TwoStringPersistentKey twoPK2 = new TwoStringPersistentKey(
			    work3.getId().toString(),
			    di2.getId().toString());

			diLink1 = DiscussionItemLink.createNew(twoPK1, work1.getId());
			diLink2 = DiscussionItemLink.createNew(twoPK2, work2.getId());
			_peer.insert(diLink1, conn);
			_peer.insert(diLink2, conn);
			conn.commit();

			List diLinks = new java.util.ArrayList();
			diLinks.add(diLink1);
			diLinks.add(diLink2);

			pstmt = ( (JdbcConnection) conn ).createParameterizedStatement();
			res = _peer.findDiscussionItemLinksByObjectId(work3.getId(), pstmt);
		        assertNotNull("Expecting not null result.", res);
		        assertTrue("Expecting another result set", testResult(res, diLinks));
			res.close();
			pstmt.close();

			_peer.delete(diLink1, conn);
			_peer.delete(diLink2, conn);
			conn.commit();

			pstmt = ( (JdbcConnection) conn ).createParameterizedStatement();
			res = _peer.findDiscussionItemLinksByObjectId(work3.getId(), pstmt);
		        assertNotNull("Expecting not null result.", res);
		        assertTrue("Expecting empty result.", ! res.next());
			res.close();
			pstmt.close();
			}
		    finally
			{
			_peer.delete(diLink1, conn);
			_peer.delete(diLink2, conn);
			conn.commit();
			delete(di1, conn);
			delete(di2, conn);
			conn.commit();
			delete(work1, conn);
			delete(work2, conn);
			delete(work3, conn);
			conn.commit();
			delete(user1, conn);
			conn.commit();
			}
		    return null;
		    }
		}.execute();
	}

    public void testInsertDelete()
	throws Exception
	{
	    final InstallationContext context = getContext();
	    new JdbcQuery( this )
		{
		protected Object query(Connection conn) throws SQLException
		    {
		    User user1 = User.createNewUser("testDiscussionItemLink_usr1", context);
		    Work work1 = Work.createNew(Work.TYPE, "testDiscussionItemLink_wrk1", user1);
		    Work work2 = Work.createNew(Work.TYPE, "testDiscussionItemLink_wrk2", user1);
		    DiscussionItem di1 = DiscussionItem.createNew( work1, null, user1, "title1", "text1");
		    TwoStringPersistentKey twoPK1 = new TwoStringPersistentKey(
			    work2.getId().toString(),
			    di1.getId().toString());
		    DiscussionItemLink diLink1 = DiscussionItemLink.createNew(twoPK1, work1.getId());

		    try {
                        _peer.insert(null, conn);
                        fail( "Null discussion item link should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null discussion item link should throw IllegalArgumentException." );
                        }
		    try {
                        _peer.insert(diLink1, null);
                        fail( "Null connection should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null connection should throw IllegalArgumentException." );
                        }
		    try {
                        _peer.delete(null, conn);
                        fail( "Null discussion item link should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null discussion item link should throw IllegalArgumentException." );
                        }
		    try {
                        _peer.delete(diLink1, null);
                        fail( "Null connection should throw IllegalArgumentException." );
                        }
                    catch( IllegalArgumentException ok ) {}
                    catch( Exception e ){
                        fail( "Null connection should throw IllegalArgumentException." );
                        }
		    return null;
		    }
		}.execute();
	}

}