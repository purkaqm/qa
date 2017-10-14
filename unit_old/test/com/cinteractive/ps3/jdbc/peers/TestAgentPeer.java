package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.database.PersistentKey;
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
import com.cinteractive.ps3.agents.MagicTaskCode;
import com.cinteractive.ps3.contexts.InstallationContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestAgentPeer extends TestJdbcPeer {
    private AgentPeer _peer;

    static{
	registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
    }

    private static void registerCase(DataSourceId id){
	TestSql.registerTestCase(AgentPeer.class.getName(), TestAgentPeer.class.getName());
    }

    public TestAgentPeer(String name) {
        super(name);
    }

    public static Test bareSuite()
    {
        final TestSuite suite = new TestSuite();
        suite.addTest( new TestAgentPeer( "testFindIdByPSAgentClassName" ) );
        suite.addTest( new TestAgentPeer( "testListIdsByName" ) );
        suite.addTest( new TestAgentPeer( "testInsertUpdateDelete" ) );

        return suite;
    }

    public void testGetAgentPeer() throws Exception { }
    public void testInsert() throws Exception { /** see testInsertUpdateDelete */ }
    public void testUpdate() throws Exception { /** see testInsertUpdateDelete */ }
    public void testDelete() throws Exception { /** see testInsertUpdateDelete */ }

    public static Test suite() {
        return setUpDb( bareSuite() );
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (AgentPeer) new JdbcQuery( this ) {
            protected Object query( Connection conn ) throws SQLException {
                return AgentPeer.getAgentPeer( conn );
            }
        }.execute();

        if( _peer == null )
            throw new NullPointerException( "Null AgentPeer instance." );
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindIdByPSAgentClassName() throws Exception {
        final InstallationContext context = getContext();

        //empty case
        final PersistentKey id = (PersistentKey) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.findIdByPSAgentClassName("noname", conn);
            }
        }.execute();
        assertNull("Expecting null id for fake name: noname", id);

        final PersistentKey id2 = (PersistentKey) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.findIdByPSAgentClassName(com.cinteractive.ps3.agents.system.AlertCleaner.class.getName(), conn);
            }
        }.execute();
       assertNotNull("Expecting com.cinteractive.ps3.agents.system.AlertCleaner id.", id2);

        //test with real data (method delete will be tested at one)
        final TestAgent testAgent = new TestAgent();
        final Agent agent = Agent.createNew(testAgent, context);
        assertNotNull("Expecting new agent; got null", agent);
        //agent.save(conn);

        new JdbcQuery( this )
        {
            protected Object query(Connection conn) throws SQLException
            {
                try {
                    conn.setAutoCommit(false);
                    _peer.insert(agent, conn);
                    final PersistentKey id = _peer.findIdByPSAgentClassName(testAgent.getClass().getName(), conn);
                    assertNotNull("findIdByPSAgentClassName failed", id );
                    _peer.delete(agent, conn);
                    //check if agent was deleted
                    try{
                        //if person alert was deleted successfully then _peer.get rise exception
                        _peer.getRestoreData(agent.getId(), conn);
                        assertTrue("Method 'Delete' in testFindIdByPSAgentClassName was failed", false);
                    }catch (EmptyResultSetException eIgnore){}

                }finally
                {
                    conn.rollback();
                    conn.setAutoCommit(true);
                }
                return null;
            }
        }.execute();
    }

    public void testListIdsByName() throws Exception {
        final InstallationContext context = getContext();
        //empty case
        List ids = (List) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.listIdsByName(conn);
            }
        }.execute();
        assertNotNull("Expecting empty list id for fake context id: " + FAKE_ID + "; got null", ids);
        assertTrue("Expecting empty list id for fake context id: " + FAKE_ID, ids.isEmpty());
        //some data
        ids = (List) new JdbcQuery( this ) {
            protected Object query(Connection conn) throws SQLException {
                return _peer.listIdsByName(conn);
            }
        }.execute();
        assertTrue("Expecting not empty list for this context.", !ids.isEmpty());
    }

    public void testInsertUpdateDelete() throws Exception {
        final Connection conn = getConnection();
        conn.setAutoCommit(false);

        try {
            final InstallationContext context = InstallationContext.get(getContextName());
            final TestAgent testAgent = new TestAgent();
            final Agent agent = Agent.createNew(testAgent, context);
            final PersistentKey key = agent.getPersistentKey();
            assertNotNull("Expecting new agent; got null", agent);
            _peer.insert(agent, conn);
            final RestoreMap inseredtData =  _peer.getRestoreData(key, conn);
            assertNotNull("Can't retrieve an agent. Insert faileed", inseredtData );
            assertTrue("Data before and after Insert are not equal", compare(agent, inseredtData));

            //update
                //change fields
            agent.setConfigFilePath("_fake_file_path_");
            agent.setIsLoaded(Boolean.FALSE);
            _peer.update(agent, conn);
            final RestoreMap data =  _peer.getRestoreData(key, conn);
            assertNotNull("Expectiog restore data; got null", data);
            assertTrue("Expecting restore data", !data.isEmpty() );
                //check if returned data are equal
            assertTrue("Update failed. Data before and after Update are not equal", compare(agent, data));
            _peer.delete(agent, conn);
            //check if agent was deleted
            try{
                //if person alert was deleted successfully then _peer.get rise exception
                _peer.getRestoreData(agent.getId(), conn);
                assertTrue("Agent has not been deleted", false);
            }catch (EmptyResultSetException eIgnore){}

        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }
    private boolean compare(Agent agent, RestoreMap data)
    {
        return agent.getAgentClassName().equals(data.get(Agent.AGENT_CLASS_NAME)) &
               agent.getConfigFilePath().equals(data.get(Agent.CONFIG_FILE_PATH)) &
                agent.getIsLoaded().equals(data.get(Agent.IS_LOADED));
    }
    private static class TestAgent implements InstallableAgent {
        static AgentType _type = AgentType.REMINDER;
        
        private static MagicTaskCode TASK_CODE = new MagicTaskCode("Test Agent");

        public void install(InstallationContext context) throws InstallationException {
            throw new java.lang.UnsupportedOperationException("Method install() not yet implemented.");
        }

        public AgentType getAgentType() {
            return _type;
        }

        public String getConfigFilePath() {
            return TestAgent.class.getName();
        }
        public MagicTaskCode getDefaultTaskCode() {
            return TASK_CODE;
        }
        public String getDescription() {
            return null;
        }
        public String getHomeFilePath() {
            return null;
        }
        public String getName() {
            return "TestAgent";
        }

        public AgentReturnInfo run(AgentTask task) {
            throw new java.lang.UnsupportedOperationException("Method run() not yet implemented.");
        }

        @Override
        public Boolean isActive() {
            // XXX Auto-generated method stub
            return null;
        }
    }
}