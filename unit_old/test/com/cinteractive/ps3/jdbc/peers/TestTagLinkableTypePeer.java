package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcConnectionImpl;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.tags.TagSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TestTagLinkableTypePeer extends TestJdbcPeer
{

    public class JdbcConnectionTest extends JdbcConnectionImpl {
        public JdbcConnectionTest(Connection conn)
                throws SQLException {
            super(conn);
        }

        public synchronized void commit()
                throws SQLException {

        		preCommit();
        		// *don't* commit
            setChanged();
            notifyObservers(COMMIT);
            
            deleteObservers();
        }
    }
//////////////// Common part //////////////
    private TagLinkableTypePeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(TagLinkableTypePeer.class.getName(), TestTagLinkableTypePeer.class.getName());
    }

    public TestTagLinkableTypePeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestTagLinkableTypePeer");

        suite.addTest(new TestTagLinkableTypePeer("testInsertUpdateDelete"));

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void testInsert() throws Exception { /** see testInsertDelete */
    }

    public void testUpdate() throws Exception { /** see testInsertDelete */
    }

    public void testDelete() throws Exception { /** see testInsertDelete */
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (TagLinkableTypePeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return TagLinkableTypePeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null TagLinkableTypePeer peer instance.");
    }

    public void tearDown()
            throws Exception {
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
//////////////// end of Common part //////////////


//Insert/Update/Delete
    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection con) throws SQLException {
                JdbcConnectionTest conn = new JdbcConnectionTest(con);
                conn.setAutoCommit(false);
                try {
                    //insert _peer.insert(tagLinkableType, conn);
                    TagSet ts = TagSet.createNew("Fake_taglinkabletype_insert_update_delete", context);
                    ts.addLinkableType(User.TYPE);
                    ts.save(conn);
                    conn.commit();
                    
                    Collection codes = _peer.findTagLinkableTypeCodes(ts.getId(), conn);
                    assertTrue("Testing insert > Method findTagLinkableTypes(...) returned empty ResultSet.", codes.contains(User.TYPE.toString()));

                    ts.removeLinkableType(User.TYPE);
                    ts.save(conn);
                    conn.commit();
                    
                    codes = _peer.findTagLinkableTypeCodes(ts.getId(), conn);
                    assertTrue("Testing delete > Method findTagLinkableTypes(...) returned NOT empty ResultSet.", !codes.contains(User.TYPE.toString()));
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

}