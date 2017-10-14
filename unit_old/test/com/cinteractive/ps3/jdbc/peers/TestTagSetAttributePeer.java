package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.ps3.attributes.SimpleAttribute;
import junit.framework.Test;
import junit.framework.TestSuite;


import java.util.Collection;

import java.sql.Connection;
import java.sql.SQLException;


import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.tags.TagSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class TestTagSetAttributePeer extends TestJdbcPeer {
    private TagSetAttributePeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(TagSetAttributePeer.class.getName(), TestTagSetAttributePeer.class.getName());
    }

//////////////// Common part //////////////
    public TestTagSetAttributePeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite(TestTagSetAttributePeer.class);
        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void testInsert() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */
    }

    public void testDelete() throws Exception { /** see testInsertUpdateDelete */
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (TagSetAttributePeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return TagSetAttributePeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null TagSetAttributePeer peer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }
//////////////// end of Common part //////////////

    public void testFindByTagSetId()
            throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                Collection attributes = _peer.findByTagSetId(FAKE_ID, conn);
                TestTagSetAttributePeer.assertTrue("Result with not empty attributes set was returned by findByObjectId(FAKE_ID, ...)", attributes.size() <= 0);
                return attributes;
            }
        }.execute();
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                conn.setAutoCommit(false);
                try {
                    TagSet ts = TagSet.createNew("_TestTagSetAttrPeer_FindByObjectId_", context);
                    ts.setAttribute("FAKE_Attribute1", "FAKE_VALUE1");
                    ts.setAttribute("FAKE_Attribute2", "FAKE_VALUE2");
                    ts.setAttribute("FAKE_Attribute3", "FAKE_VALUE3");
                    ts.save(conn);

                    Collection attributes = _peer.findByTagSetId(ts.getId(), conn);
                    TestTagSetAttributePeer.assertTrue("Count of set of attributes returned by findByObjectId(...) is wrong", attributes.size() == 3);

                    return null;
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }

            }
        }.execute();

    }
//Insert/Update/Delete

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    TagSet ts = TagSet.createNew("_TestTagSetAttrPeer_InsertUpdateDelete_", context);
                    ts.setAttribute("FAKE_Attribute_InsertUpdateDelete", "FAKE_VALUE1");
                    ts.save(conn);//insert
                    //find attribute and try to update and delete it
                    Collection attributes = _peer.findByTagSetId(ts.getId(), conn);
                    SimpleAttribute tsa = (SimpleAttribute) attributes.iterator().next();
                    assertNotNull("findByObjectId failed during test of Insert/Update/Delete", tsa);
                    assertTrue("Expecting valid value of attribute", "FAKE_VALUE1".equals(tsa.getStringValue()));

                    tsa.setStringValue("new value");
                    _peer.update(tsa, conn);
                    attributes = _peer.findByTagSetId(ts.getId(), conn);
                    tsa = (SimpleAttribute) attributes.iterator().next();
                    assertNotNull("findByObjectId failed during test of Insert/Update/Delete", tsa);
                    assertTrue("Expecting valid value of attribute", "new value".equals(tsa.getStringValue()));

                    _peer.delete(tsa, conn);
                    attributes = _peer.findByTagSetId(ts.getId(), conn);
                    assertTrue("Expecting empty list of attributes", attributes.isEmpty());
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }
}
