package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.ConnectionSource;
import com.cinteractive.jdbc.JdbcUpdate;
import com.cinteractive.ps3.MagicObjectCode;
import com.cinteractive.ps3.alerts.AlertQueue;
import com.cinteractive.ps3.test.MockObjectType;
import com.cinteractive.ps3.test.TestPS;
import com.cinteractive.ps3.types.ObjectType;
import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * Base class for peer unit tests; just gets a connection based on System properties.
 */
public abstract class TestJdbcPeer extends TestPS implements ConnectionSource
{
	static abstract class Update extends JdbcUpdate
	{
		protected Update( ConnectionSource src ) { super( src, false ); }


		protected abstract Object doUpdate( Connection conn )
		throws SQLException;

		protected final Object update( Connection conn )
		throws SQLException
		{
			final boolean autoCommit = conn.getAutoCommit();
			if( autoCommit )
				conn.setAutoCommit( false );
			try { return doUpdate( conn ); }
			finally
			{
				conn.rollback();
				if( autoCommit ) conn.setAutoCommit( true );
			}
		}
	}


// Dummy data for empty cases
	protected static final ObjectType CONTEXT_TYPE = new MockObjectType( "InstallationContext" );

	protected static final String FAKE_CONTEXT_NAME = "%%?&#";

	public static final PersistentKey FAKE_ID = new PersistentKey()
	{
		public String toString() { return ""; }
		public int compareTo( Object o )
		{
			if( this == o )
				return 0;
			else
				throw new ClassCastException();
		}
	};

	protected static final MagicObjectCode FAKE_MAGIC_CODE = new MagicObjectCode(
		"__FAKE_MAGIC_CODE__",
		TestJdbcPeer.class.getName(),
		null
	);


	protected TestJdbcPeer( String name ) { super( name ); }

	public static Test setUpDb( Test test )
	{
                return setUpHome( test );
	}

	public static Test suite()
	{
		return setUpDb( new TestSuite( TestPSObjectBasePeer.class ) );
	}

	public Connection getConnection()
	throws SQLException
	{
            return getContext().getConnection();
	}

        public void setUp() throws Exception {
            super.setUp();
            AlertQueue.setMode(AlertQueue.SKIP_EVENTS_MODE);
        }

        public void tearDown() throws Exception {
            super.tearDown();
            AlertQueue.setMode(AlertQueue.DEFAULT_MODE);
        }
}
