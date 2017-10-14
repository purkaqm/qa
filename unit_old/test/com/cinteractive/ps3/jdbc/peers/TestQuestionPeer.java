/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.database.Uuid;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcUpdate;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.questions.HandlerQuery;
import com.cinteractive.ps3.questions.Question;
import com.cinteractive.ps3.questions.Questions;
import com.cinteractive.ps3.questions.handlers.ItemDelegationHandler;
import com.cinteractive.ps3.questions.handlers.TimesheetHandler;
import com.cinteractive.ps3.work.Work;


public class TestQuestionPeer extends TestJdbcPeer
{
	private static final com.cinteractive.ps3.questions.QuestionHandler FAKE_HANDLER = new com.cinteractive.ps3.questions.QuestionHandler()
	{
		public void expire( )
		throws com.cinteractive.ps3.questions.QuestionHandlerException
		{
			throw new UnsupportedOperationException( "FAKE_HANDLER::expire>>Not implemented." );
		}

		public com.cinteractive.ps3.questions.Response handleResponse( com.cinteractive.ps3.questions.Questions questions, javax.servlet.ServletRequest req )
		throws com.cinteractive.ps3.questions.QuestionHandlerException
		{
			throw new UnsupportedOperationException( "FAKE_HANDLER::handleResponse>>Not implemented." );
		}

		public void initQuestion( com.cinteractive.ps3.entities.User user, Object[] args )
		{
		}

		public void setQuestion( Question question )
		{
		}

		public void writeInputFields( String contentType, java.io.Writer writer, javax.servlet.ServletRequest req )
		throws java.io.IOException
		{
			throw new UnsupportedOperationException( "FAKE_HANDLER::writeInputFields>>Not implemented." );
		}

		public void writeQuestionText( String contentType, java.io.Writer writer, User user )
		throws java.io.IOException
		{
			throw new UnsupportedOperationException( "FAKE_HANDLER::writeQuestionText>>Not implemented." );
		}

		public void writeShortQuestionText( String contentType, java.io.Writer writer, User user  )
		throws java.io.IOException
		{
			writeQuestionText( contentType, writer, user  );
		}
	};


	private QuestionPeer _peer;


	static{
	    registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
	}

	private static void registerCase(DataSourceId id){
	    TestSql.registerTestCase( QuestionPeer.class.getName(), TestQuestionPeer.class.getName());
	}


	public TestQuestionPeer( String name ) { super( name ); }


	public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite();

		suite.addTest( new TestQuestionPeer( "testDeleteByHandler" ) );
		suite.addTest( new TestQuestionPeer( "testFindByUser" ) );
		suite.addTest( new TestQuestionPeer( "testRestore" ) );
		suite.addTest( new TestQuestionPeer( "testDeleteByTargetObject" ) );
		suite.addTest( new TestQuestionPeer( "testDeleteByUser" ) );
		suite.addTest( new TestQuestionPeer( "testFindMetadata" ) );
		suite.addTest( new TestQuestionPeer( "testInsertUpdateDelete" ) );

		return suite;
	}

	public void testGetInstance() throws Exception { }
	public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
	public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
	public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }

	public static Test suite()
	{
		return setUpDb( bareSuite() );
	}


	public void setUp()
	throws Exception
	{
		super.setUp();

		_peer = (QuestionPeer) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return QuestionPeer.getInstance( conn );
			}
		}.execute();
		if( _peer == null )
			throw new NullPointerException( "Null QuestionPeer instance." );
	}

	public void tearDown()
	throws Exception
	{
		super.tearDown();
		_peer = null;
	}

    private static final boolean DEBUG = true; // show debug information

// public void deleteByHandler( HandlerQuery query, Connection conn )
public void testDeleteByHandler()
throws Exception
{
// empty case
    new JdbcUpdate( this )
    {
        protected Object update( Connection conn )
        throws SQLException
        {
            final HandlerQuery query = new HandlerQuery( FAKE_HANDLER );
            query.putParameter( "Bob", "Sally" );

            _peer.deleteByHandler( query, conn );
            return null;
        }
    }.execute();

    //test with real data
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {
                final User user = Nobody.get(context);
                final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                final Object[] args = new Object[ 1 ];
				args[ 0 ] = rootWorkInsert;
                final Question q = Question.createNew( user, new ItemDelegationHandler(), args );
                rootWorkInsert.save(conn);

                _peer.insert(q, conn);

                final HandlerQuery query = new HandlerQuery( new ItemDelegationHandler() );
                query.setTargetObjectId( rootWorkInsert.getId() );

                _peer.deleteByHandler( query, conn );
                try
                {
                    _peer.restore(q.getId(), context.getName(), conn);
                    assertTrue("Method DeleteByHandler failed", false);
                }catch(EmptyResultSetException e)
                { }


            } finally {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();

}

//public Map findByUser( PersistentKey userId, Connection conn )
public void testFindByUser()
throws Exception
{
// empty case
    {
        final Map questions = (Map) new JdbcQuery( this )
        {
            protected Object query( Connection conn )
            throws SQLException
            {
                return _peer.findByUser( FAKE_ID, FAKE_CONTEXT_NAME, 0, conn );
            }
        }.execute();
        assertNotNull( "Expecting empty Map for no results; got null.", questions );
        assertTrue( "Expecting empty Questions map for fake user id.", questions.isEmpty() );
    }
// some data - maybe
    new JdbcQuery( this )
    {
        protected Object query( Connection conn )
        throws SQLException
        {
            PersistentKey userId = null;

            final Statement stmt = conn.createStatement();
            final ResultSet rset = stmt.executeQuery( "SELECT MAX(user_id) FROM Inbox_Question WHERE expire_date IS NULL AND response_date IS NULL" );
            if( rset.next() )
            {
                String idStr = rset.getString( 1 );
                if( idStr != null )
                    userId = Uuid.get( idStr );
            }
            rset.close();
            stmt.close();

            if( userId == null )
                return null;

            final Map questions = _peer.findByUser( userId, getContext().getName(), 0, conn );
            assertTrue( "Expecting non-empty questions for user id '" + userId + "'.", !questions.isEmpty() );
            return null;
        }
    }.execute();

    //test with Nobody(new) and not NULL expiration date
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {
                final User user = Nobody.get(context);
                final Object[] ps = {"Param1", "Param2"};
                final Question q = Question.createNew(user, FAKE_HANDLER, ps);

                final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                final Calendar cal = Calendar.getInstance();
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.DAY_OF_YEAR, 10);
                final Timestamp expireDate = new Timestamp(cal.getTime().getTime());
                rootWorkInsert.save(conn);
                //initialize fields
                q.setTargetObjectId(rootWorkInsert.getId());
                q.setHandler(new TimesheetHandler());
                q.setSectionTitle("_fake_title_");
                q.setExpirationDate(expireDate);
                q.setResponseDate(null);
                _peer.insert(q, conn);
                rootWorkInsert.deleteHard(conn);

                final Map questions = _peer.findByUser( user.getId() , user.getContextName(), 0, conn );
                assertTrue( "Expecting non-empty questions for user id '" + user.getId() + "'.", !questions.isEmpty() );

                _peer.delete(q, conn);
                try
                {
                    _peer.restore(q.getId(), q.getContextName(), conn);
                    assertTrue("Method Delete failed", false);
                }catch(EmptyResultSetException e) { }

            } finally {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();
}

	//public Question restore( PersistentKey id, Connection conn )
	public void testRestore()
	throws Exception
	{
		final String contextName = getContext().getName();
		// empty case
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				Question question = null;
				try {
					question = _peer.restore(FAKE_ID, FAKE_CONTEXT_NAME, conn);
					fail("Expecting EmptyResultSetException for fake question id.");
				}
				catch (EmptyResultSetException ok) {}

				return question;
			}
		}.execute();
		// some data - maybe
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				PersistentKey id = null;

				final Statement stmt = conn.createStatement();
				final ResultSet rset = stmt.executeQuery("SELECT MAX(inbox_question_id) FROM Inbox_Question");
				if (rset.next()) {
					String idStr = rset.getString(1);
					if (idStr != null)
						id = Uuid.get(idStr);
				}
				rset.close();
				stmt.close();

				return id == null ? null : _peer.restore(id, contextName, conn);
			}
		}.execute();
		//test with new data
		final InstallationContext context = getContext();
		new JdbcQuery(this)
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				conn.setAutoCommit(false);
				try {
					final User user = Nobody.get(context);
					final Object[] ps = {"Param1", "Param2"};
					final Question q = Question.createNew(user, FAKE_HANDLER, ps);
					final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
					rootWorkInsert.save(conn);
					//initialize fields
					q.setTargetObjectId(rootWorkInsert.getId());
					q.setHandler(new TimesheetHandler());
					//                q.setSectionTitle("_fake_title_");
					q.setExpirationDate(null);
					q.setResponseDate(null);
					_peer.insert(q, conn);

					try {
						_peer.restore(q.getId(), contextName, conn);

					}
					catch (EmptyResultSetException e) {
						assertTrue("Method restore failed", false);
					}

					rootWorkInsert.deleteHard(conn);
					_peer.delete(q, conn);
					try {
						_peer.restore(q.getId(), contextName, conn);
						assertTrue("Method Delete failed", false);
					}
					catch (EmptyResultSetException e) {}

				}
				finally {
					conn.rollback();
					conn.setAutoCommit(true);
				}
				return null;
			}
		}.execute();

	}

public void testDeleteByTargetObject() throws Exception {
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            _peer.deleteByTargetObject(FAKE_ID, conn);
            return null;
        }
    }.execute();
    //test with real data
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {
                final User user = User.getNobody(context);
                final Object[] ps = {"Param1", "Param2"};
                final Question q = Question.createNew(user, FAKE_HANDLER, ps);
                final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                rootWorkInsert.save(conn);
                //initialize fields
                q.setTargetObjectId(rootWorkInsert.getId());
                q.setHandler(new TimesheetHandler());
                q.setExpirationDate(null);
                q.setResponseDate(null);
                _peer.insert(q, conn);

                _peer.deleteByTargetObject(rootWorkInsert.getId(), conn);
                try
                {
                    _peer.restore(q.getId(), q.getContextName(), conn);
                    assertTrue("Method deleteByTargetObject() failed", false);
                }catch(EmptyResultSetException e) { }

                rootWorkInsert.deleteHard(conn);
            } finally {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();

}

public void testDeleteByUser() throws Exception {
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            _peer.deleteByUser(FAKE_ID, conn);
            return null;
        }
    }.execute();

    //test with real data
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {
                //final User user = (User) User.getNobody(context);
                final User user = User.createNewUser("fake_email_address_", context);
                user.save(conn);
                final Object[] ps = {"Param1", "Param2"};
                final Question q = Question.createNew(user, FAKE_HANDLER, ps);
                final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                rootWorkInsert.save(conn);
                //initialize fields
                q.setTargetObjectId(rootWorkInsert.getId());
                q.setHandler(new TimesheetHandler());
                q.setExpirationDate(null);
                q.setResponseDate(null);
                _peer.insert(q, conn);

                _peer.deleteByUser(user.getId(), conn);
                try
                {
                    _peer.restore(q.getId(), q.getContextName(), conn);
                    assertTrue("Method deleteByUser() failed", false);
                }catch(EmptyResultSetException e) { }

                user.deleteHard(conn);
                rootWorkInsert.deleteHard(conn);
            } finally {

                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();

}

public void testFindMetadata() throws Exception {
    //empty case
    Questions.Metadata md = (Questions.Metadata) new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            return _peer.findMetadata(FAKE_ID, conn);
        }
    }.execute();
    assertNotNull("Expecting empty metadata for fake user id; got null", md);

    //some data
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            return _peer.findMetadata(User.getNobody(context).getId(), conn);
        }
    }.execute();

    //test with real data
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {

                final User user = User.createNewUser("fake_email_address_", context);
                user.save(conn);
                //test Questions count==0
                try{

                    //test of _peer.findMetadata(user.getId(), conn); will not directly
                    final int count = Questions.get(user).count();
                    assertTrue("Count>0 expected in method testFindMetadata()", count == 0 );

                }finally
                {
                }

                //test Questions count > 0
                final Object[] ps = {"Param1", "Param2"};

                final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                try{
                    rootWorkInsert.save(conn);
                    final Question q = Question.createNew(user, FAKE_HANDLER, ps);

                    //initialize fields
                    q.setTargetObjectId(rootWorkInsert.getId());
                    q.setHandler(new TimesheetHandler());
                    q.setExpirationDate(null);
                    q.setResponseDate(null);
                    _peer.insert(q, conn);
                    conn.commit();

                    //test of _peer.findMetadata(user.getId(), conn); will not directly
                    final int count = Questions.get(user).count();
                    assertTrue("Count>0 expected in method testFindMetadata()", count > 0 );

                    _peer.delete(q, conn);
                    try
                    {
                        _peer.restore(q.getId(), q.getContextName(), conn);
                        assertTrue("Method deleteByUser() failed", false);
                    }catch(EmptyResultSetException e) { }
                }finally
                {
                    delete(user, conn);
                    delete(rootWorkInsert, conn);
                }


            } finally {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();

}

	private void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
               // if (o instanceof PSObject)
               //     ((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}

public void testInsertUpdateDelete() throws Exception {
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            conn.setAutoCommit(false);
            try {
                final User user = User.getNobody(context);
                final Object[] ps = {"Param1", "Param2"};
                final Question q = Question.createNew(user, FAKE_HANDLER, ps);

                {
                    final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                    final Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MILLISECOND, 0);
                    cal.add(Calendar.DAY_OF_YEAR, +1);
                    final Timestamp responseDate = new Timestamp(cal.getTime().getTime());
                    cal.add(Calendar.DAY_OF_YEAR, 10);
                    final Timestamp expireDate = new Timestamp(cal.getTime().getTime());
                    rootWorkInsert.save(conn);
                    //initialize fields
                    q.setTargetObjectId(rootWorkInsert.getId());
                    q.setHandler(new TimesheetHandler());
                    q.setSectionTitle("_fake_title_");
                    q.setExpirationDate(expireDate);
                    q.setResponseDate(responseDate);
                    _peer.insert(q, conn);
                    compare("Insert", q.getId(), q, conn);
                    rootWorkInsert.deleteHard(conn);
                }

                {   //change fields
                    final Work rootWorkInsert = Work.createNew(Work.TYPE, "_fake_root_work", user);
                    final Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MILLISECOND, 0);
                    cal.add(Calendar.DAY_OF_YEAR, +2);
                    final Timestamp responseDate = new Timestamp(cal.getTime().getTime());
                    cal.add(Calendar.DAY_OF_YEAR, 11);
                    final Timestamp expireDate = new Timestamp(cal.getTime().getTime());
                    rootWorkInsert.save(conn);

                    q.setTargetObjectId(rootWorkInsert.getId());
                    q.setHandler(new ItemDelegationHandler());
                    q.setSectionTitle("_fake_fake_title_");
                    q.setExpirationDate(expireDate);
                    q.setResponseDate(responseDate);

                    _peer.update(q, conn);
                    compare("Update", q.getId(), q, conn);
                    rootWorkInsert.deleteHard(conn);
                }

                _peer.delete(q, conn);
                try
                {
                    _peer.restore(q.getId(), q.getContextName(), conn);
                    assertTrue("Method Delete failed", false);
                }catch(EmptyResultSetException e)
                { }


            } finally {
                conn.rollback();
                conn.setAutoCommit(true);
            }
            return null;
        }
    }.execute();
}
private void compare(String sStageName, PersistentKey q1Id, Question q2, Connection conn) throws SQLException
{
    try
    {
        final Question q1 = _peer.restore(q1Id, getContextName(), conn);
        assertTrue(sStageName + " - Values returned by  getTargetObjectId are differ", q1.getTargetObjectId().equals(q2.getTargetObjectId()));
        assertTrue(sStageName + " - Values returned by  getHandler are differ", q1.getHandler().getClass().getName().equals( q2.getHandler().getClass().getName()) );
        assertTrue(sStageName + " - Values returned by  getSectionTitle are differ", q1.getSectionTitle().equals(q2.getSectionTitle()));
        assertTrue(sStageName + " - Values returned by  getExpirationDate are differ", q1.getExpirationDate().equals(q2.getExpirationDate()));
        assertTrue(sStageName + " - Values returned by  getResponseDate are differ", q1.getResponseDate().equals(q2.getResponseDate()));

    }catch(EmptyResultSetException e)
    { assertTrue("Method " + sStageName + " failed", false);}

}

}
