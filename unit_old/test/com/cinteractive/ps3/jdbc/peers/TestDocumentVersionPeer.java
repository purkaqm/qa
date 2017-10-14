package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.documents.DocumentDataSource;
import com.cinteractive.ps3.documents.DocumentVersion;
import com.cinteractive.ps3.documents.FileDocument;
import com.cinteractive.ps3.entities.Nobody;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestDocumentVersionPeer extends TestJdbcPeer {
    DocumentVersionPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(DocumentVersionPeer.class.getName(), TestDocumentVersionPeer.class.getName());
    }

    public TestDocumentVersionPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestDocumentVersionPeer( testName ) );
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testGetCurrentVersionNumber_InsertUpdateDelete" );
        addTest( suite, "testListVersionNumbers" );
        addTest( suite, "testDeleteByDocumentId" );

        return suite;
    }

    public void testGetInstance() throws Exception { }
    public void testInsert() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testRollback() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testGetVersion() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testSetFileContents() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testGetFileContents() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testGetCurrentVersionNumber() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testGetCurrentVersion() throws Exception { /** see testGetCurrentVersionNumber_InsertUpdateDelete */ }
    public void testListVersions() throws Exception { /** see testListVersionNumbers */ }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (DocumentVersionPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return DocumentVersionPeer.getInstance( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null DocumentVersionPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testGetCurrentVersionNumber_InsertUpdateDelete() throws Exception {
        final Connection conn = getConnection();
        try {
            try {
                _peer.getCurrentVersionNumber(FAKE_ID, conn);
            } catch (EmptyResultSetException ok) {}

            conn.setAutoCommit(false);
            final String data = "some data";
            final String newData = "updated data";

            final FileDocument doc = FileDocument.createNew("Notitle", Nobody.get(getContext()), new StringSource(data, "text/html", "noname"));
            doc.save(conn);
            Integer version = _peer.getCurrentVersionNumber(doc.getPersistentKey(), conn);
            assertTrue("Expecting valid document version " + doc.getCurrentVersion().getVersion() + "; got " + version, version.equals(doc.getCurrentVersion().getVersion()) );
            DocumentVersion ver = DocumentVersion.createNew(doc, Nobody.get(getContext()).getPersistentKey(), new StringSource(data, "text/html", "noname"));

            _peer.insert( ver, conn );
            _peer.update( ver, conn );

            //read data
            InputStream is = _peer.getFileContents(doc.getPersistentKey(), doc.getCurrentVersion().getVersion(), conn);
            byte buf[] = new byte[data.length()];
            is.read(buf);
            assertTrue("Expecting " + data + "; got " + new String(buf), new String(buf).equals(data));

            _peer.setFileContents(doc.getPersistentKey(), new Integer(2), new StringSource(newData, "", "").getInputStream(), newData.length(), conn);
            is = _peer.getFileContents(doc.getPersistentKey(), new Integer(2), conn);
            buf = new byte[newData.length()];
            is.read(buf);
            assertTrue("Expecting " + newData + "; got " + new String(buf), new String(buf).equals(newData));

            ver = _peer.getVersion(doc, new Integer(1), conn);
            assertNotNull("Expecting document version", ver);

            //rollback document
            _peer.rollback(doc.getPersistentKey(), new Integer(1), conn);
            version = _peer.getCurrentVersionNumber(doc.getPersistentKey(), conn);
            assertTrue("Expecting document version 1", version.intValue() == 1);

            _peer.delete( ver, conn );
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void testListVersionNumbers() throws Exception{
        final Connection conn = getConnection();
        conn.setAutoCommit(false);

        try {
            final String data = "some data";

            //empty case
            try {
                    _peer.listVersionNumbers( null, conn );
                    fail( "Null Document Id should throw IllegalArgumentException." );
            }
            catch( IllegalArgumentException ok ) {}
            catch( Exception e ){
                    fail( "Null Document Id should throw IllegalArgumentException." );
            }
            try {
                    _peer.listVersionNumbers( FAKE_ID, null );
                    fail( "Null connection should throw IllegalArgumentException." );
            }
            catch( IllegalArgumentException ok ) {}
            catch( Exception e ){
                    fail( "Null connection should throw IllegalArgumentException." );
            }

            List ids = _peer.listVersionNumbers(FAKE_ID, conn);
            assertNotNull("Expecting empty ids list; got null", ids);
            assertTrue("Expecting empty ids list", ids.isEmpty());

            //some data
            final FileDocument doc = FileDocument.createNew("Notitle", Nobody.get(getContext()), new StringSource(data, "text/html", "noname"));
            doc.save(conn);

            try {
                    _peer.listVersions( null, conn );
                    fail( "Null Document should throw IllegalArgumentException." );
            }
            catch( IllegalArgumentException ok ) {}
            catch( Exception e ){
                    fail( "Null Document should throw IllegalArgumentException." );
            }
            try {
                    _peer.listVersions( doc, null );
                    fail( "Null connection should throw IllegalArgumentException." );
            }
            catch( IllegalArgumentException ok ) {}
            catch( Exception e ){
                    fail( "Null connection should throw IllegalArgumentException." );
            }

            ids = _peer.listVersionNumbers(doc.getPersistentKey(), conn);
            assertTrue("Expecting another versions in result list", ids.size()==1);
            assertTrue("Expecting another versions in result list",
                        ids.contains(new Integer(1)) );

            DocumentVersion ver1 = doc.getCurrentVersion();
            ids = _peer.listVersions(doc, conn);
            assertTrue("Expecting another versions in result list", ids.size()==1);
            assertTrue("Expecting another versions in result list",
                        ids.contains(ver1) );

            DocumentVersion ver2 = DocumentVersion.createNew(doc, Nobody.get(getContext()).getPersistentKey(), new StringSource(data, "text/html", "noname"));
            _peer.insert(ver2, conn);

            ids = _peer.listVersionNumbers(doc.getPersistentKey(), conn);
            assertTrue("Expecting another versions in result list", ids.size()==2);
            assertTrue("Expecting another versions in result list",
                        ids.contains(new Integer(1)) && ids.contains(new Integer(2)) );

            ids = _peer.listVersions(doc, conn);
            assertTrue("Expecting another versions in result list", ids.size()==2);
            assertTrue("Expecting another versions in result list",
                        ids.contains(ver1) && ids.contains(ver2) );

            _peer.deleteByDocumentId(doc.getPersistentKey(), conn);

            ids = _peer.listVersionNumbers(doc.getPersistentKey(), conn);
            assertNotNull("Expecting empty result list, not null", ids);
            assertTrue("Expecting empty result list. got: "+ids.size(), ids.isEmpty());

            try{
                ids = _peer.listVersions(doc, conn);}
            catch (EmptyResultSetException er){}
            catch( Exception e ){
                    fail( "Expecting rising EmptyResultSetException." );
            }
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void testDeleteByDocumentId() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    final String data = "some data";
                    final FileDocument doc = FileDocument.createNew("Notitle", Nobody.get(context), new StringSource(data, "text/html", "noname"));
                    doc.save(conn);
                    try {
                        _peer.getCurrentVersionNumber(doc.getId(), conn);
                    } catch (Exception err) {
                        assertTrue("Unexpected exception", false);
                    }

                    _peer.deleteByDocumentId(doc.getId(), conn);
                    try {
                        _peer.getCurrentVersionNumber(doc.getId(), conn);
                        assertTrue("Expecting empty result set", false);
                    } catch (EmptyResultSetException ok) { }

                    return null; //method does not return value
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
            }
        }.execute();
    }


	private static class StringSource implements DocumentDataSource
	{
		private byte[] _contents;
		private String _contentType;
		private String _name;
		
		
		StringSource(String contents, String contentType, String name)
		{
			_contents = contents.getBytes();
			_contentType = contentType;
			_name = name;
		}
		
		
		public int getContentLength()
		{
			return _contents.length;
		}
		
		public String getContentType()
		{
			return _contentType;
		}
		
		public InputStream getInputStream()
		{
			return new ByteArrayInputStream(_contents);
		}
		
		public String getName()
		{
			return _name;
		}
	}
}