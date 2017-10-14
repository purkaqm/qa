package com.cinteractive.ps3.jdbc.peers;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.documents.DocumentFolder;
import com.cinteractive.ps3.documents.DocumentFolders;
import com.cinteractive.ps3.PSObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Iterator;

/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/

/**
 *
 * User: Softdev
 * Date: Dec 24, 2002
 * Time: 11:41:55 AM
 */
public class TestDocumentFolderPeer extends TestJdbcPeer {

    public TestDocumentFolderPeer(String name) {
		super(name);
	}

	private DocumentFolderPeer _peer;
	private static final boolean DEBUG = true;

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase(DocumentFolderPeer.class.getName(), TestDocumentFolderPeer.class.getName());
	}

	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();
		suite.addTest(new TestDocumentFolderPeer("testAllMethods"));

		return suite;
	}

	public static Test suite() {
		return setUpDb(bareSuite());
	}

	public void setUp()
			throws Exception {
		super.setUp();

		_peer = (DocumentFolderPeer) new JdbcQuery(this) {
			protected Object query(Connection conn)
					throws SQLException {
				return DocumentFolderPeer.getInstance(conn);
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

    /**
     * insert, update and delete hierarchy of two folders. watch their state by findDocumentFoldersByWorkId method
     * @throws Exception
     */
    public void testAllMethods() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery(this) {
            private boolean findItemInResultList(List res, String folder_id, String parent_id, String folder_name){
                String[] arr;
                for(Iterator i = res.iterator(); i.hasNext(); ){
                    try{
                        arr = (String[]) i.next();
                    } catch(ClassCastException ex) {
                        fail("Unexpected object in result list");
                        return false;
                    }
                    if ( folder_id.equalsIgnoreCase(arr[0]) &&
                             ((parent_id==null) ? parent_id == arr[1] : parent_id.equalsIgnoreCase(arr[1])) &&
                             folder_name.equalsIgnoreCase(arr[2]) )
                        return true;
                }
                return false;
            }

			protected Object query(Connection conn) throws SQLException {
                final User user1 = User.createNewUser("testDocFolder_testIUD_usr1", context);

				try {
                    user1.save(conn);
                    conn.commit();

                    DocumentFolders dFolders = user1.getFolder().getDocumentFolders();

                    DocumentFolder df = dFolders.addFolder("testDocFolder_testIUD", null);
                    DocumentFolder df1 = dFolders.addFolder("testDocFolder_testIUD_1", df);

                    _peer.insert(df, conn);
                    _peer.insert(df1, conn);

                    List res = _peer.findDocumentFoldersByWorkId(dFolders.getWorkId(), conn);
                    assertNotNull("Expecting not null result list.", res);
                    assertTrue("Expecting 2 items in result list. got : " + res.size(), res.size() >= 2 );
                    assertTrue("Expecting another items in result list.", findItemInResultList(res, df.getPersistentKey().toString(),
                            null, df.getName()) );
                    assertTrue("Expecting another items in result list.", findItemInResultList(res, df1.getPersistentKey().toString(),
                            df1.getParentId().toString(), df1.getName()) );

                    df.setName("testDocFolder_testIUD_changed");

                    _peer.update(df, conn);

                    res = _peer.findDocumentFoldersByWorkId(dFolders.getWorkId(), conn);
                    assertTrue("Expecting another items in result list.", findItemInResultList(res, df.getPersistentKey().toString(),
                            null, df.getName()) );

                    _peer.delete(df1, conn);
                    _peer.delete(df, conn);

                    res = _peer.findDocumentFoldersByWorkId(dFolders.getWorkId(), conn);
                    assertNotNull("Expecting not null result list.", res);
                    assertTrue("Expecting another items in result list.", !findItemInResultList(res, df.getPersistentKey().toString(),
                            null, df.getName()) );
                    assertTrue("Expecting another items in result list.", !findItemInResultList(res, df1.getPersistentKey().toString(),
                            df1.getParentId().toString(), df1.getName()) );

				} finally {
                    delete(user1, conn);
				}
				return null;
			}
		}.execute();

    }

    //gags - see testAllMethods()

    public void testInsert() throws Exception {};
    public void testDelete() throws Exception {};
    public void testUpdate() throws Exception {};
    public void testFindDocumentFoldersByWorkId() throws Exception {};
    public void testGetInstance() throws Exception {};
}
