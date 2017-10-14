package com.cinteractive.ps3.agents.system;

import com.cinteractive.ps3.work.*;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.mimehandler.PSMimeHandler;
import com.cinteractive.ps3.mimehandler.HandlerRegistry;
import com.cinteractive.ps3.session.PSSession;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tollgate.TollPhase;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.test.MockObjectType;
import com.cinteractive.ps3.test.MockPSSession;
import com.cinteractive.ps3.test.MockServletRequest;

import java.util.*;

import java.sql.Connection;

import java.sql.SQLException;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestSFGDataFeedAgentSql extends TestJdbcPeer {
    private SFGDataFeedAgentSql _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
        //registerCase(StringDataSourceId.get(StringDataSourceId.ORACLE8));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(SFGDataFeedAgentSql.class.getName(), TestSFGDataFeedAgentSql.class.getName());
    }

    private final static boolean DEBUG = true;

    public TestSFGDataFeedAgentSql(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSFGDataFeedAgentSql("testFindWorkIds"));
        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (SFGDataFeedAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return SFGDataFeedAgentSql.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null SFGDataFeedAgentSql peer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindWorkIds() throws Exception {
        final InstallationContext context = getContext();

        final List types = new Vector();
        types.add(new MockObjectType("Work"));
        //findWorkIds( PersistentKey contextId, PersistentKey templateId, Collection workStatuses, Collection workTypes, Connection conn )
        //empty case
        List ids = (List) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.findWorkIds(FAKE_ID, FAKE_ID, types, types, conn);
            }
        }.execute();
        assertNotNull("Expecting empty ids set for context FAKE_ID; got null", ids);
        assertTrue("Expecting empty ids set", ids.isEmpty());

        //real data
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                /////////////////
                //			Tollgate t = null;
                Tollgate t1 = null;
                MetricTemplate tmpl1 = null;
                MetricInstance ins1 = null;
                PSTag process = null;
                TollPhase tp = null;
                try {
                    // create own process definition
                    tp = (TollPhase) context.getTagSet(TollPhase.TYPE);
                    process = tp.addTag("_TestSFGSql_FindWorkIds_Test");
//				tp.save();
                    tp.save(conn);
                    conn.commit();

                    PSTag p1 = process.getTagSet().addTag("phase1_Test");
                    p1.setParent(process);
//				process.getTagSet().save();
                    process.getTagSet().save(conn);
                    conn.commit();

                    PSTag p2 = process.getTagSet().addTag("phase2_Test");
                    p2.setParent(process);
                    process.getTagSet().save(conn);
                    conn.commit();
//				process.getTagSet().save();

                    final PSSession sess = new MockPSSession(context, User.getNobody(context));
                    final MockServletRequest req = new MockServletRequest();
                    req.setParameter(Work.OBJECT_TYPE, Tollgate.TYPE.toString());
                    req.setParameter(Work.NAME, "_TestSFGSql_FindWorkIds_");
                    req.setParameter(TollPhase.TAG_SEQUENCE, process.getName());
                    req.setAttribute(PSSession.class.getName(), sess);
                    req.setAttribute(InstallationContext.CONTEXT_NAME_PARAM, context.getName());

                    final PSMimeHandler handler = HandlerRegistry.getHandler(Tollgate.TYPE, "text/html");
/*				try {
					t = (Tollgate) handler.create(null, req);
				} catch (Exception e) {
					if (DEBUG) e.printStackTrace();
					fail("Unexpected exception occured\n" + e.getMessage());
				}
				t.save(conn);
				conn.commit();
*/

                    req.setParameter(Work.NAME, "_TestSFGSql_FindWorkIds4_");
                    try {
                        t1 = (Tollgate) handler.create(req);
                    } catch (Exception e) {
                        if (DEBUG) e.printStackTrace();
                        fail("Unexpected exception occured\n" + e.getMessage());
                    }
                    t1.save(conn);
                    conn.commit();

                    tmpl1 = MetricTemplate.createNew(MetricTemplate.TYPE, "_TestSFGSql_templ_4", User.getNobody(context));
                    tmpl1.save(conn);
                    conn.commit();
                    ins1 = MetricInstance.createNew(tmpl1, t1, false);
                    ins1.generateAlerts(false);
                    ins1.save(conn);
                    conn.commit();
                    ////////////////

                    List statuses = new Vector();
                    statuses.add(WorkStatus.ON_TRACK);
                    statuses.add(WorkStatus.PROPOSED);
                    statuses.add(WorkStatus.NOT_STARTED);
                    List types = new Vector();
                    types.add(Tollgate.TYPE);
                    List wids = _peer.findWorkIds(context.getId(), tmpl1.getId(), statuses, types, conn);
                    assertTrue("Expecting at least 1 id in result set", wids.size() > 0);
                    assertTrue("Expecting work id", wids.contains(t1.getId()));
                    statuses.clear();
                    statuses.add(WorkStatus.COMPLETED);
                    wids = _peer.findWorkIds(context.getId(), tmpl1.getId(), statuses, types, conn);
                    assertTrue("Unexpected work id found", !wids.contains(t1.getId()));
                } finally {
                    delete(ins1, conn);
                    delete(tmpl1, conn);
                    delete(t1, conn);
                    if (process != null) {
                        ArrayList al = new ArrayList(10);
                        for (Iterator i = process.getChildren().iterator(); i.hasNext();) {
                            com.cinteractive.ps3.tags.PSTag t = (com.cinteractive.ps3.tags.PSTag) i.next();
                            al.add(t);
                        }
                        for (Iterator i = al.iterator(); i.hasNext();) {
                            com.cinteractive.ps3.tags.PSTag t = (com.cinteractive.ps3.tags.PSTag) i.next();
                            tp.removeTag(t.getId());
                        }

                        tp.removeTag(process.getId());
                        tp.save(conn);
                        conn.commit();
                    }
                }
                return null;
            }
        }.execute();

    }

    private void delete(PSObject o, Connection conn) {
        try {
            if (o != null) {
                //o.setModifiedById(User.getNobody(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

}
