package com.cinteractive.ps3.jdbc.peers;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.CIParameterizedStatement;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.documents.DocumentFolders;
import com.cinteractive.ps3.documents.DocumentFolder;
import com.cinteractive.ps3.documents.UrlDocument;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.relationships.DocumentRelationship;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.database.CIResultSet;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/

/**
 *
 * User: Softdev
 * Date: Dec 24, 2002
 * Time: 2:58:00 PM
 */
public class TestDocumentRelationshipPeer extends TestJdbcPeer {

    public TestDocumentRelationshipPeer(String name) {
        super(name);
    }

    private DocumentRelationshipPeer _peer;
    private static final boolean DEBUG = true;

    static {
        registerCase();
    }

    private static void registerCase() {
        TestSql.registerTestCase(DocumentRelationshipPeer.class.getName(), TestDocumentRelationshipPeer.class.getName());
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestDocumentRelationshipPeer("testInsertUpdateDelete"));

        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (DocumentRelationshipPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return DocumentRelationshipPeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null PersonAlertPeer instance.");
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }


    private List moveformResToList(CIResultSet res) throws SQLException {
        List outL = new ArrayList();

        while (res.next()) {
            String[] tmp = new String[3];
            tmp[0] = res.getString(1);
            tmp[1] = res.getString(2);
            tmp[2] = res.getString(3);
            outL.add(tmp);
        }

        return outL;
    }

    private boolean findItemInResultList(List res, String project_id, String document_id, String folder_id) {
        for (Iterator i = res.iterator(); i.hasNext();) {
            String[] arr = (String[]) i.next();
            if (project_id.equalsIgnoreCase(arr[0]) &&
                    document_id.equalsIgnoreCase(arr[1]) &&
                    folder_id.equalsIgnoreCase(arr[2]))
                return true;
        }
        return false;
    }


    /**
     * Test insert, update and delete methods.
     * create 1 user with 2 document folders in its personal folder. create one url document.
     * try to insert relationship between first document folder and url document.
     * try to update given relationship to link url document with second document folder.
     * try to delete given relationship.
     * check correctness of operations by findDocumentRelationshipsByWorkId() method.
     * @throws Exception
     */
    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {
                final User user1 = User.createNewUser("testIUD_testIUD_usr1", context);
                final UrlDocument url1 = UrlDocument.createNew("testIUD", "http://www.zvezda.ru", user1);
                final DocumentFolders dFolders = user1.getFolder().getDocumentFolders();
                final DocumentFolder df1 = dFolders.addFolder("testIUD_docfolder1", null);
                final DocumentFolder df2 = dFolders.addFolder("testIUD_docfolder2", null);

                try {
                    user1.save(conn);
                    conn.commit();

                    dFolders.save(conn);
                    url1.save(conn);
                    conn.commit();

                    final Work userFolder1 = user1.getFolder();
                    //userFolder1.setModifiedById(user1.getId());
                    DocumentRelationship dr = userFolder1.addDocument( url1, df1 );

                    CIParameterizedStatement pstmt = null;
                    CIResultSet res = null;
                    List resList = null;

                    _peer.insert(dr, conn);

                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        res = _peer.findByWorkId(userFolder1.getId(), pstmt);
                        resList = moveformResToList(res);
                    } finally {
                        res.close();
                        pstmt.close();
                    }

                    assertTrue("Expecting another items count (1) in result. got : " + resList.size(), resList.size() == 1);
                    assertTrue("Expecting another items in result", findItemInResultList(resList, userFolder1.getId().toString(),
                            url1.getId().toString(), df1.getPersistentKey().toString()));

                    dr.setFolder(df2);
                    _peer.update(dr, conn);

                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        res = _peer.findByWorkId(userFolder1.getId(), pstmt);
                        resList = moveformResToList(res);
                    } finally {
                        res.close();
                        pstmt.close();
                    }

                    assertTrue("Expecting another items count (1) in result. got : " + resList.size(), resList.size() == 1);
                    assertTrue("Expecting another items in result", findItemInResultList(resList, userFolder1.getId().toString(),
                            url1.getId().toString(), df2.getPersistentKey().toString()));

                    _peer.delete(dr, conn);

                    try {
                        pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                        res = _peer.findByWorkId(userFolder1.getId(), pstmt);
                        resList = moveformResToList(res);
                    } finally {
                        res.close();
                        pstmt.close();
                    }

                    assertTrue("Expecting another items count (0) in result. got : " + resList.size(), resList.size() == 0);

                } finally {
                    delete(url1, conn);
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();

    }

    //gags
    public void testInsert() throws Exception {};
    public void testUpdate() throws Exception {};
    public void testDelete() throws Exception {};
    public void testGetInstance() throws Exception {};

    private final void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
              //  if (o instanceof PSObject)
              //      ((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
    }


}
