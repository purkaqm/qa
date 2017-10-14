package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;

import com.cinteractive.ps3.metrics.MetricTemplateItem;
import com.cinteractive.ps3.metrics.MetricTemplateItemAttributes;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.attributes.SimpleAttribute;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestMetricTemplateItemAttributePeer extends TestJdbcPeer {
    private MetricTemplateItemAttributePeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(MetricTemplateItemAttributePeer.class.getName(), TestMetricTemplateItemAttributePeer.class.getName());
    }

    public TestMetricTemplateItemAttributePeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestMetricTemplateItemAttributePeer( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testFindByItemId" );
        addTest( suite, "testInsertUpdateDelete" );

        return suite;
    }

    public void testGetInstance() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (MetricTemplateItemAttributePeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return MetricTemplateItemAttributePeer.getInstance( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null MetricTemplateItemAttributePeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindByItemId() throws Exception {
        //empty case
        Collection ids = (Collection) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.findByItemId(FAKE_ID, conn);
            }
        }.execute();
        assertNotNull("Expecting empty list; got null", ids);

        final InstallationContext context = getContext();
        final User user = User.getNobody(context);
        ids = (Collection) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                final boolean ac = conn.getAutoCommit();
                if (ac) conn.setAutoCommit(false);
                final MetricTemplate templ = MetricTemplate.createNew(MetricTemplate.TYPE, "__MetricTemplate__", user);
                final MetricTemplateItem item = MetricTemplateItem.createNew("mi", templ.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                try {
                    templ.setHasPhases(false);
                    MetricTemplatePeer.getMetricTemplatePeer(conn).insert(templ, conn);
                    final SimpleAttribute attr = new SimpleAttribute(MetricTemplateItemAttributes.class.getName(), item.getId());
                    attr.restore("attr1", "");

                    MetricTemplateItemPeer.getInstance(conn).insert(item, conn);
                    _peer.insert(attr, conn);
                    return _peer.findByItemId(item.getId(), conn);
                } finally {
                    try {
                        conn.rollback();
                        if (ac) conn.setAutoCommit(ac);
                    } catch (Exception ignored) {}
                }

            }
        }.execute();
        assertTrue("Expecting one attribute; got " + ids.size(), ids.size() == 1);
    }

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        final User user = User.getNobody(context);

        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                final boolean ac = conn.getAutoCommit();
                if (ac) conn.setAutoCommit(false);
                final MetricTemplate templ = MetricTemplate.createNew(MetricTemplate.TYPE, "__MetricTemplate__", user);
                final MetricTemplateItem item = MetricTemplateItem.createNew("mi", templ.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                 try {
                    templ.setHasPhases(false);
                    MetricTemplatePeer.getMetricTemplatePeer(conn).insert(templ, conn);
                    final SimpleAttribute attr = new SimpleAttribute(MetricTemplateItemAttributes.class.getName(), item.getId());
                    attr.restore("attr1", "");

                    MetricTemplateItemPeer.getInstance(conn).insert(item, conn);
                    _peer.insert(attr, conn);
                    Collection ids = _peer.findByItemId(item.getId(), conn);
                    SimpleAttribute at = null;
                    assertTrue("Expecting attribute: " + attr.getAttributeName(), ids.size() == 1 && (at = (SimpleAttribute) ids.toArray()[0]).getAttributeName().equals("attr1") && (at.getStringValue() == null || at.getStringValue().length() == 0) );
                    attr.setStringValue("value");
                    _peer.update(attr, conn);
                    ids = _peer.findByItemId(item.getId(), conn);
                    assertTrue("Expecting attribute: " + attr.getAttributeName(), ids.size() == 1 && (at = (SimpleAttribute) ids.toArray()[0]).getAttributeName().equals("attr1") && at.getStringValue().equals("value") );
                    _peer.delete(attr, conn);
                    ids = _peer.findByItemId(item.getId(), conn);
                    assertTrue("Expecting empty attribute list; got " + ids.size() + " attributes", ids.size() == 0);
                } finally {
                    try {
                        conn.rollback();
                        if (ac) conn.setAutoCommit(ac);
                    } catch (Exception ignored) { }
                }
                return null;
            }
        }.execute();
    }
}