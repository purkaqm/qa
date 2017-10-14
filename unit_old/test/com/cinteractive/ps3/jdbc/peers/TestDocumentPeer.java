package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.UrlDocument;
import com.cinteractive.ps3.entities.User;
import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestDocumentPeer extends TestJdbcPeer {
    private DocumentPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(DocumentPeer.class.getName(), TestDocumentPeer.class.getName());
    }

    public TestDocumentPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestDocumentPeer( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testInsertUpdateDelete" );

        return suite;
    }

    public void testGetDocumentPeer() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (DocumentPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return DocumentPeer.getDocumentPeer( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null DocumentPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    UrlDocument doc = (UrlDocument) UrlDocument.createNew("notitle", "http://psstage.cinteractive.com:8080/ps3alpha/ci/login.jsp", (User) User.getNobody(context));
                    doc.setIsImportant(Boolean.TRUE);
                    _peer.insert(doc, conn);
                    assertTrue(compare(doc, _peer.getRestoreData(doc.getId(), conn)));
                    doc.setURL("http://java.sun.com");
                    doc.setIsImportant(Boolean.FALSE);
                    _peer.update(doc, conn);
                    assertTrue(compare(doc, _peer.getRestoreData(doc.getId(), conn)));
                    _peer.delete(doc, conn);
                    try {
                        _peer.getRestoreData(doc.getId(), conn);
                        fail("Expecting EmptyResultSetException");
                    } catch (EmptyResultSetException ok) { }
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

    public boolean compare(UrlDocument doc, RestoreMap map) {
        boolean res = true;
        if (res && ( !doc.getURL().equals(map.get(UrlDocument.URL)) ) && !(res = false) ) {
            err("Url");
        }
        if (res && ( !doc.getIsImportant().equals(map.get(UrlDocument.IS_IMPORTANT)) ) && !(res = false) ) {
            err("IsImportant");
        }
        return res;
    }

    private void err(String attrName) {
        System.out.println(">> Wrong '" + attrName + "' attribute value");
    }

}