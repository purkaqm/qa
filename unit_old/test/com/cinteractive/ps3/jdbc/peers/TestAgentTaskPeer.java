package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.database.StringPersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.agents.Agent;
import com.cinteractive.ps3.agents.AgentReturnInfo;
import com.cinteractive.ps3.agents.AgentTask;
import com.cinteractive.ps3.agents.AgentType;
import com.cinteractive.ps3.agents.InstallableAgent;
import com.cinteractive.ps3.agents.InstallationException;
import com.cinteractive.ps3.agents.LogEventType;
import com.cinteractive.ps3.agents.MagicTaskCode;
import com.cinteractive.ps3.agents.TaskInfo;
import com.cinteractive.ps3.contexts.InstallationContext;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestAgentTaskPeer extends TestJdbcPeer
{
    private AgentTaskPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(AgentTaskPeer.class.getName(), TestAgentTaskPeer.class.getName());
    }

    public TestAgentTaskPeer(String name) {
        super(name);
    }

    private static void addTest(TestSuite suite, String testName) {
        suite.addTest( new TestAgentTaskPeer( testName ) );
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        addTest( suite, "testListLogsByTask" );
        addTest( suite, "testDeleteLogsByTaskIdAndDate" );
        addTest( suite, "testInsertUpdateDelete" );

        return suite;
    }

    public void testGetInstance() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }
    public void testGetLogSql() throws Exception { }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (AgentTaskPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return AgentTaskPeer.getInstance( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null AgentTaskPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }


public void testListLogsByTask() throws Exception {
    final Calendar cal = Calendar.getInstance();
    final Timestamp to = new Timestamp(cal.getTime().getTime());
    cal.add(cal.DATE, -10);
    final Timestamp from = new Timestamp(cal.getTime().getTime());
    Map logs = (Map) new JdbcQuery( this ) {
        protected Object query(Connection conn) throws SQLException {
            return _peer.listLogsByTask(FAKE_ID, conn, from, to);
        }
    }.execute();
    assertNotNull("Expecting empty logs map; got null", logs);
    assertTrue("Expecting empty logs map", logs.isEmpty());

    //some data
    new JdbcQuery( this ) {
        protected Object query(Connection conn)
                    throws SQLException
                    {
                        String taskId = null;

                        final Statement stmt = conn.createStatement();
                        final ResultSet rs = stmt.executeQuery("select agent_task_id from agent_task where magic_task_code = '" + com.cinteractive.ps3.agents.system.AlertCleaner.CLEAR + "'");
                        if( rs.next() )
                            taskId = rs.getString( 1 );
                        rs.close();
                        stmt.close();

          return taskId == null ? null : _peer.listLogsByTask( new StringPersistentKey( taskId ), conn, from, to);
        }
    }.execute();

    //some new data
    final InstallationContext context = getContext();
    new JdbcQuery( this ) {
        protected Object query(Connection conn)
            throws SQLException
            {
                try{
                    conn.setAutoCommit(false);
                    // create new AgentTask
                    final Calendar cal = Calendar.getInstance();
                    final Timestamp from = new Timestamp(cal.getTime().getTime());
                    cal.add(Calendar.DATE, 10);
                    final Timestamp to = new Timestamp(cal.getTime().getTime());
                    final FakeAlert al = new FakeAlert();
                    final Agent a = Agent.createNew(al, context);
                    AgentPeer.getAgentPeer(conn).insert(a, conn);
                    AgentTask at = a.addTask(new TaskInfo());
                    //_peer.insert(at, conn);
                    at.beginLog("Start test(_fake_) log");
                    at.addLogEntry(LogEventType.SUCCESS, "_fake_notes_", null);
                    at.endLog(AgentReturnInfo.getSuccess("_fake_message"));
                    at.save(conn);
                    Map res = _peer.listLogsByTask(at.getId(), conn, from, to);
                    if ( !res.isEmpty() )
                    {
                        Iterator iter = res.values().iterator();
                        List lst = (List)iter.next();
                        Object [] arr = (Object [])lst.get(0);
                        assertTrue("", arr.length >0 &&
                                        arr[1].equals(LogEventType.BEGIN.toString()) &&
                                        arr[2].equals("Start test(_fake_) log"));
                    }
                    else
                        assertTrue("ERROR. There are no entries in log", false);
                    //delete the task
                    at.delete(conn);
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn.close();
                }
                return null;
            }
    }.execute();

}

    public void testDeleteLogsByTaskIdAndDate() throws Exception {
        final Connection conn = getConnection();
        conn.setAutoCommit(false);

        try {
            final Timestamp date = new Timestamp(Calendar.getInstance().getTime().getTime());
            assertTrue("Expecting no rows deleted for fake task id", _peer.deleteLogsByTaskIdAndDate(FAKE_ID, date, true, conn) + _peer.deleteLogsByTaskIdAndDate(FAKE_ID, date, true, conn) == 0);

            final ResultSet rs = conn.createStatement().executeQuery("select agent_task_id from agent_task where magic_task_code = '" + com.cinteractive.ps3.agents.system.AlertCleaner.CLEAR + "'");
            rs.next();
            _peer.deleteLogsByTaskIdAndDate(new StringPersistentKey(rs.getString(1)), date, true, conn);
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
        //some new data
        final InstallationContext context = getContext();
        new JdbcQuery( this ) {
            protected Object query(Connection conn)
                throws SQLException
                {
                    try{
                        conn.setAutoCommit(false);
                        // create new AgentTask
                        final Calendar cal = Calendar.getInstance();
                        final Timestamp from = new Timestamp(cal.getTime().getTime());
                        cal.add(Calendar.DATE, 10);
                        final Timestamp to = new Timestamp(cal.getTime().getTime());

                        final FakeAlert al = new FakeAlert();
                        final Agent a = Agent.createNew(al, context);
                        AgentPeer.getAgentPeer(conn).insert(a, conn);
                        AgentTask at = a.addTask(new TaskInfo());
                        //_peer.insert(at, conn);
                        at.beginLog("Start test(_fake_) log");
                        at.addLogEntry(LogEventType.SUCCESS, "_fake_notes_", null);
                        at.endLog(AgentReturnInfo.getSuccess("_fake_message"));
                        at.save(conn);
                        _peer.deleteLogsByTaskIdAndDate(at.getId(), to, false, conn);
                        //check if tasks was really deleted
                        Map res = _peer.listLogsByTask(at.getId(), conn, from, to);
                        assertTrue("Expected EMPTY result. ", res.isEmpty());

                        //delete the task
                        at.delete(conn);
                    } finally {
                        conn.rollback();
                        conn.setAutoCommit(true);
                        conn.close();
                    }
                    return null;
                }
        }.execute();

    }
    class FakeAlert implements InstallableAgent {
        public void install( InstallationContext ctx ) throws InstallationException {}
        public AgentReturnInfo run(AgentTask task) { return null; }
        public AgentType getAgentType() { return AgentType.SYSTEM; }
        public String getConfigFilePath() { return null; }
        public MagicTaskCode getDefaultTaskCode() { return null; }
        public String getDescription() { return null; }
        public String getHomeFilePath() { return null; }
        public String getName() { return "FakeAlert"; }
        @Override
        public Boolean isActive() {
            // XXX Auto-generated method stub
            return null;
        }
    }

    public void testInsertUpdateDelete() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                try {
                    conn.setAutoCommit(false);

                    // create new AgentTask
                    final FakeAlert al = new FakeAlert();
                    final Agent a = Agent.createNew(al, context);
                    AgentPeer.getAgentPeer(conn).insert(a, conn);
                    AgentTask at = a.addTask(new TaskInfo());
                    _peer.insert(at, conn);

                    // update task parameters
                    at.setComments("comments");
                    Timestamp time = new Timestamp( 0 );
                    at.setExpirationDate( time );
                    at.setFirstRunTime( time );
                    at.setIsActive( Boolean.FALSE );
                    at.setNextRunTime( time );
                    _peer.update(at, conn);

                    // check the parameters after update
                    final RestoreMap data = _peer.getRestoreData(at.getId(), conn);
                    assertTrue("Expecting valid parameter values",
                        "comments".equals(data.get(AgentTask.COMMENTS)) &&
                        time.equals(data.get(AgentTask.EXPIRATION_DATE)) &&
                        time.equals(data.get(AgentTask.FIRST_RUN_TIME)) &&
                        time.equals(data.get(AgentTask.NEXT_RUN_TIME)) &&
                        Boolean.FALSE.equals(data.get(AgentTask.IS_ACTIVE))
                    );

                    //delete the task
                    _peer.delete(at, conn);
                    try {
                        _peer.getRestoreData(at.getId(), conn);
                        assertTrue("Expecting empty data for deleted task", false);
                    } catch (EmptyResultSetException ok) { }

                    return null;
                } finally {
                    conn.rollback();
                    conn.setAutoCommit(true);
                    conn.close();
                }
            }
        }.execute();
    }

    // the peer method only uses methods findIdsByAgentId and delete (which was already tested) and nothing more
    // no need to test
    public void testDeleteByAgentId() { }
}
