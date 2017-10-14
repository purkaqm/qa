package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.attributes.SimpleAttribute;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.pstransactions.ActivityCodes;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagAttributes;
import com.cinteractive.ps3.tags.TagSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestTagAttributePeer extends TestJdbcPeer {
    private TagAttributePeer _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(TagAttributePeer.class.getName(), TestTagAttributePeer.class.getName());
    }

    /**
     * TestTagAttributePeer constructor comment.
     * @param name java.lang.String
     */
    protected TestTagAttributePeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestTagAttributePeer("testDelete"));
        suite.addTest(new TestTagAttributePeer("testFindByTagId"));
        suite.addTest(new TestTagAttributePeer("testInsert"));
        suite.addTest(new TestTagAttributePeer("testUpdate"));

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (TagAttributePeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return TagAttributePeer.getInstance(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null ObjectAttributePeer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
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

    public void testDelete() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());

        conn.setAutoCommit(false);

        try {
            TagSet ts = ActivityCodes.get(context);
            PSTag tag = ts.addTag("NewTempTag");
            tag.save(conn);
            SimpleAttribute attr = new SimpleAttribute(TagAttributes.class.getName(), tag.getId());
            attr.restore("attrName", "StringValue");
            _peer.insert(attr, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select map_item_id from map_item_attribute where map_item_id='" + tag.getId() + "'" +
                    " and attribute_name='" + attr.getAttributeName() + "'");

            assertTrue("Expecting record in resultset", rset.next());

            try {
                _peer.delete(null, conn);
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.delete(attr, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }
            _peer.delete(attr, conn);

            rset = stmt.executeQuery("select map_item_id from map_item_attribute where map_item_id='" + tag.getId() + "'" +
                    " and attribute_name='" + attr.getAttributeName() + "'");

            assertTrue("Not expecting record in resultset", !rset.next());
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void testFindByTagId() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            private boolean findAttr(Collection res, PersistentKey item_id, String attr_name, String attr_value) {
                for (Iterator i = res.iterator(); i.hasNext();) {
                    SimpleAttribute a = (SimpleAttribute) i.next();
                    if (a.getObjectId().equals(item_id) &&
                            attr_name.equals(a.getAttributeName()) &&
                            attr_value.equals(a.getStringValue()))
                        return true;
                }
                return false;
            }

            protected Object query(Connection conn) throws SQLException {
                Collection res = null;

                try {
                    res = _peer.findByTagId(null, conn);
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findByTagId(FAKE_ID, null);
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }

                res = _peer.findByTagId(FAKE_ID, conn);
                assertNotNull("Expecting not empty result set", res);
                assertTrue("Expecting empty result set. got: " + res.size(), res.isEmpty());

                TagSet ts = TagSet.createNew("testTagAttPeer_tagset", context);
                PSTag tag = ts.addTag("testTagAttPeer_tag");
                try {
                    ts.save(conn);
                    conn.commit();
                    tag.save(conn);
                    conn.commit();

                    res = _peer.findByTagId(tag.getId(), conn);
                    assertNotNull("Expecting not empty result set", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.isEmpty());

                    tag.setAttribute("testTagAttPeer_attr1", "value1");
                    tag.save(conn);

                    res = _peer.findByTagId(tag.getId(), conn);
                    assertNotNull("Expecting not empty result set", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 1);
                    assertTrue("Expecting another items in result set.",
                            findAttr(res, tag.getId(), "testTagAttPeer_attr1", "value1"));

                    tag.setAttribute("testTagAttPeer_attr2", "value2");
                    tag.save(conn);

                    res = _peer.findByTagId(tag.getId(), conn);
                    assertNotNull("Expecting not empty result set", res);
                    assertTrue("Expecting another items count in result set. got: " + res.size(), res.size() == 2);
                    assertTrue("Expecting another items in result set.",
                            findAttr(res, tag.getId(), "testTagAttPeer_attr1", "value1") &&
                            findAttr(res, tag.getId(), "testTagAttPeer_attr2", "value2"));

                    tag.removeAttribute("testTagAttPeer_attr1");
                    tag.removeAttribute("testTagAttPeer_attr2");
                    tag.save(conn);
                    conn.commit();
                } finally {
                    try {
                        delete(tag, conn);
                        conn.commit();
                        delete(ts, conn);
                        conn.commit();
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    public void testInsert() throws Exception {

        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());

        conn.setAutoCommit(false);

        try {
            TagSet ts = ActivityCodes.get(context);
            PSTag tag = ts.addTag("NewTempTag");
            tag.save(conn);
            SimpleAttribute attr = new SimpleAttribute(TagAttributes.class.getName(), tag.getId());
            attr.restore("attrName", "StringValue");
//_peer.insert(attr, conn);

            try {
                _peer.insert(null, conn);
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.insert(attr, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            _peer.insert(attr, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select map_item_id from map_item_attribute where map_item_id='" + tag.getId() + "'" +
                    " and attribute_name='" + attr.getAttributeName() + "'");

            assertTrue("Expecting record in resultset", rset.next());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public void testUpdate() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());


        conn.setAutoCommit(false);

        try {
            TagSet ts = ActivityCodes.get(context);
            PSTag tag = ts.addTag("NewTempTag");
            tag.save(conn);
            SimpleAttribute attr = new SimpleAttribute(TagAttributes.class.getName(), tag.getId());
            attr.restore("attrName", "StringValue");
            _peer.insert(attr, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select map_item_id from map_item_attribute where map_item_id='" + tag.getId() + "'" +
                    " and attribute_name='" + attr.getAttributeName() + "'");

            assertTrue("Expecting record in resultset", rset.next());

            attr.setStringValue("Updated");

            try {
                _peer.update(null, conn);
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ObjectAttribute parameter should throw IllegalArgumentException.");
            }

            try {
                _peer.update(attr, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }
            _peer.update(attr, conn);

            rset = stmt.executeQuery("select map_item_id from map_item_attribute where map_item_id='" + tag.getId() + "'" +
                    " and attribute_name='" + attr.getAttributeName() + "'");

            assertTrue("Expecting record in resultset", rset.next());
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }

    }
}
