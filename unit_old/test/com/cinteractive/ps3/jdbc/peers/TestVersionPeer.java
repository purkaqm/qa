package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestVersionPeer extends TestJdbcPeer {
    VersionPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(VersionPeer.class.getName(), TestVersionPeer.class.getName());
    }

    public TestVersionPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestVersionPeer( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testRestoreVersion" );

        return suite;
    }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (VersionPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return VersionPeer.getVersionPeer( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null VersionPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testRestoreVersion() throws Exception {
        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.restoreVersion(conn);
            }
        }.execute();
    }

    // gags
    public void testGetVersionPeer() { }
}