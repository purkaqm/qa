package com.cinteractive.ps3.jdbc.peers;

import java.io.*;

import com.cinteractive.jdbc.CIParameterizedStatement;
import com.cinteractive.jdbc.JdbcConnection;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;

import com.cinteractive.database.*;

import com.cinteractive.calc.Expression;
import com.cinteractive.ps3.metrics.*;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.contexts.InstallationContext;

import java.util.*;
import java.sql.*;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestMetricTemplateItemPeer extends TestJdbcPeer {
	private static final String COLUMNS =
			"metric_template_id, " +
			"name, " +
			"description, " +
			"seq, " +
			"expression, " +
			"is_reference, " +
			"is_rollup, " +
			"is_cumulative, " +
			"is_monetary, " +
			"is_rollup_vertical, " +
			"is_fromcontext, " +
			"object_type ";

        private static final String RESTORE_SQL =
                        "select " + COLUMNS +
                        "from Metric_Tmplt_Item where item_id = ? ";

        private static final String TEMPLATEID = "templateId";
        private static final String IS_REFERENCE = "isReference";
        private static final String IS_FROM_CONTEXT = "isFromContext";
        private static final String OBJECT_TYPE = "objecttype";

    private static final boolean DEBUG=true;

    MetricTemplateItemPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(MetricTemplateItemPeer.class.getName(), TestMetricTemplateItemPeer.class.getName());
    }

    public TestMetricTemplateItemPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestMetricTemplateItemPeer( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testFindByTemplateId" );
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

        _peer = (MetricTemplateItemPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return MetricTemplateItemPeer.getInstance( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null MetricTemplateItemPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindByTemplateId() throws Exception {
         new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                final CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                try {
                    CIResultSet rs = _peer.findByTemplateId(FAKE_ID, pstmt);
                    assertNotNull("Expecting empty result set; got null", rs);
                    assertTrue("Expecting empty result set for fake template id", !rs.next());
                    return null;
                } finally {
                    pstmt.close();
                }
            }
        }.execute();

        final InstallationContext context = getContext();

        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                MetricTemplate t = null;
                MetricTemplateItem item1 = null;
                MetricTemplateItem item2 = null;
                User user = null;
                try {
                    user = User.createNewUser("_TestMTIPeer_findByTemplate_", context);
                    user.save(conn);
                    conn.commit();

                    t = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestMTIPeer_findByTemplate_MetricTemplate1_", user);
                    t.save(conn);
                    conn.commit();

                    item1 = MetricTemplateItem.createNew("item1", t.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                    item1.setSequence(new Integer(1));
                    item1.save(conn);
                    conn.commit();
                    t.save(conn);
                    conn.commit();

                    item2 = MetricTemplateItem.createNew("item2", t.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                    item2.setSequence(new Integer(2));
                    item2.save(conn);
                    conn.commit();
                    t.save(conn);
                    conn.commit();

                    CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                    CIResultSet rs;
                    try {
                        rs = _peer.findByTemplateId(t.getId(), pstmt);
                        assertNotNull("Expecting result set; got null", rs);
                        assertTrue("Expecting 2 item ids in result set",
                            rs.next() && (
                                rs.getString(1).equals(item1.getId().toString()) ||
                                rs.getString(1).equals(item2.getId().toString())
                            ) &&
                            rs.next() && (
                                rs.getString(1).equals(item1.getId().toString()) ||
                                rs.getString(1).equals(item2.getId().toString())
                            )
                        );
                    } finally {
                        pstmt.close();
                    }
                } finally {
                    delete(item1, conn);
                    delete(item2, conn);
                    delete(t, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    private void delete(JdbcPersistableBase o, Connection conn) {
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

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                conn.setAutoCommit(false);
                try {
                    final MetricTemplate mt = MetricTemplate.createNew(MetricTemplate.TYPE, "TestMetricTemplateItemPeer.testInsertUpdateDelete", User.getNobody(context));
                    mt.save(conn);
                    final MetricTemplateItem item = MetricTemplateItem.createNew("item name", mt.getItems(), MetricTemplateItem.CALCULATED_ITEM);
                    item.setSequence(new Integer(1));
                    item.setDescription("description");
                    Expression expr = null;
                    try {
                        expr = mt.getItems().getParser().getExpression("1+2");
                        item.setExpression(expr);
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                    }
                    _peer.insert(item, conn);
                    assertTrue("Expecting valid data", compare(item, getRestoreData(item.getId(), conn)));
                    item.setName("new item name");
                    item.setDescription("new description");
                    item.setSequence(new Integer(10));
                    try {
                        item.setExpression( mt.getItems().getParser().getExpression("2*2") );
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                    }
                    item.setIsReference(!item.isReference());
                    item.setIsRollup(!item.isRollup());
                    item.setIsCumulative(!item.isCumulative());
                    item.setIsMonetary(!item.isMonetary());
                    item.setRollupVertical(!item.isRollupVertical());
                    item.setIsFromContext(!item.isFromContext());
                    _peer.update(item, conn);
                    assertTrue("Expecting valid data", compare(item, getRestoreData(item.getId(), conn)));
                    item.setType(MetricTemplateItem.USER_DEFINED_ITEM);
                    _peer.update(item, conn);
                    assertTrue("Expecting valid data", compare(item, getRestoreData(item.getId(), conn)));
                    _peer.delete(item, conn);
                    CIParameterizedStatement pstmt = ((JdbcConnection) conn).createParameterizedStatement();
                    try {
                        CIResultSet rs = _peer.findByTemplateId(mt.getId(), pstmt);
                        assertTrue("Expecting empty list of items", !rs.next());
                    } finally {
                        pstmt.close();
                    }
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

    private void err(String attrName) {
        System.err.println(">> Wrong '" + attrName + "' attribute value");
    }

    private boolean equals(Object o1, Object o2) {
        return  o1 == null && o2 == null ||
                o1 != null && o1.equals(o2);
    }

    private boolean compare(MetricTemplateItem item, Map data) {
        boolean res = true;
        if ( res && !item.getTemplateId().toString().equals(data.get(TEMPLATEID)) && !(res = false) ) {
            err("TemplateID");
        }
        if ( res && !item.getName().equals(data.get(MetricTemplateItem.TEMPLATE_ITEM_NAME)) && !(res = false) ) {
            err("Name");
        }
        if ( res && !equals(item.getDescription(), data.get(MetricTemplateItem.TEMPLATE_ITEM_DESCRIPTION)) && !(res = false) ) {
            err("Description");
        }
        if ( res && !item.getSequence().equals(data.get(MetricTemplateItem.TEMPLATE_ITEM_SEQ)) && !(res = false) ) {
            err("Sequence");
        }
        if ( res && item.getPSType().equals(MetricTemplateItem.CALCULATED_ITEM) && !equals(item.getExpression() == null ? null : item.getExpression().toString(), data.get(MetricTemplateItem.EXPRESSION)) && !(res = false) ) {
            err("Expression");
        }
        if ( res && item.isReference()!=((Boolean)data.get(IS_REFERENCE)).booleanValue() && !(res = false) ) {
            err("IsReference");
        }
        if ( res && item.isRollup()!=((Boolean)data.get(MetricTemplateItem.IS_ROLLUP)).booleanValue() && !(res = false) ) {
            err("IsRollup");
        }
        if ( res && item.isCumulative()!=((Boolean)data.get(MetricTemplateItem.IS_CUMULATIVE)).booleanValue() && !(res = false) ) {
            err("IsCumulative");
        }
        if ( res && item.isMonetary()!=((Boolean)data.get(MetricTemplateItem.IS_MONETARY)).booleanValue() && !(res = false) ) {
            err("IsMonetary");
        }
        if ( res && item.isRollupVertical()!=((Boolean)data.get(MetricTemplateItem.IS_ROLLUP_VERTICAL)).booleanValue() && !(res = false) ) {
            err("IsRollupVertical");
        }
        if ( res && item.isFromContext()!=((Boolean)data.get(IS_FROM_CONTEXT)).booleanValue() && !(res = false) ) {
            err("IsFromContext");
        }
        if ( res && !item.getPSType().getCode().equals(((String)data.get(OBJECT_TYPE)).trim()) && !(res = false) ) {
            err("ObjectType");
        }
        return res;
    }

    private HashMap getRestoreData(PersistentKey itemId, Connection conn) throws SQLException {
        final CIPreparedStatement pstmt = new CIPreparedStatement(  conn.prepareStatement(RESTORE_SQL) );
        pstmt.setString(1, itemId.toString());
        final ResultSet rs = pstmt.executeQuery();
        final HashMap data = new HashMap();
        if (rs.next()) {
            int i = 1;
            data.put(TEMPLATEID, rs.getString(i++));
            data.put(MetricTemplateItem.TEMPLATE_ITEM_NAME, rs.getString(i++));
            data.put(MetricTemplateItem.TEMPLATE_ITEM_DESCRIPTION, rs.getString(i++));
            data.put(MetricTemplateItem.TEMPLATE_ITEM_SEQ, new Integer(rs.getInt(i++)));
            try {
                data.put(MetricTemplateItem.EXPRESSION, rs.getCharacterStream(i) == null ? null : new BufferedReader( rs.getCharacterStream(i) ).readLine());
            } catch (IOException ignored) {
                if (DEBUG) ignored.printStackTrace();
            }
            i++;
            data.put(IS_REFERENCE, new Boolean("Y".equals(rs.getString(i++))));
            data.put(MetricTemplateItem.IS_ROLLUP, new Boolean("Y".equals(rs.getString(i++))));
            data.put(MetricTemplateItem.IS_CUMULATIVE, new Boolean("Y".equals(rs.getString(i++))));
            data.put(MetricTemplateItem.IS_MONETARY, new Boolean("Y".equals(rs.getString(i++))));
            data.put(MetricTemplateItem.IS_ROLLUP_VERTICAL, new Boolean("Y".equals(rs.getString(i++))));
            data.put(IS_FROM_CONTEXT, new Boolean("Y".equals(rs.getString(i++))));
            data.put(OBJECT_TYPE, rs.getString(i++));
        }
        return data;
    }

}