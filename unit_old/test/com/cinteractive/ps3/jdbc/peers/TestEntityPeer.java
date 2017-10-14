package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Entity;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Person;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.tollgate.Tollgate;
import com.cinteractive.ps3.work.Work;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestEntityPeer extends TestJdbcPeer {
    private EntityPeer _peer;
    private static final boolean DEBUG = true; //show debug info

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(EntityPeer.class.getName(), TestEntityPeer.class.getName());
    }

    public TestEntityPeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestEntityPeer");

        suite.addTest(new TestEntityPeer("testFindPersonIdsByLastName"));
        suite.addTest(new TestEntityPeer("testInsertDelete"));
        suite.addTest(new TestEntityPeer("testFindIdByEmailAddresses"));
        suite.addTest(new TestEntityPeer("testFindDeleteNotificationIds"));
        suite.addTest(new TestEntityPeer("testFindPersonIdsByName"));
        suite.addTest(new TestEntityPeer("testListAllGroupIds"));
        suite.addTest(new TestEntityPeer("testUpdateRestore"));
        suite.addTest(new TestEntityPeer("testFindEntityWorkIds"));

        return suite;
    }

    public void setUp()
            throws Exception {
        super.setUp();

        _peer = (EntityPeer) new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                return EntityPeer.getEntityPeer(conn);
            }
        }.execute();
        if (_peer == null)
            throw new NullPointerException("Null EntityPeer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void tearDown()
            throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testFindIdByEmailAddresses() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "NewFakeEmailAddress";

        conn.setAutoCommit(false);

        try {
            PersistentKey res = null;

            res = _peer.findIdByEmailAddresses(email, context.getId(), conn);
            assertTrue("Expecting null PersistentKey result object by Fake email", res == null);

            User user = User.createNewUser(email, context);
            _peer.insert(user, conn);

            try {
                res = _peer.findIdByEmailAddresses(null, context.getId(), conn);
                fail("Null String parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null String parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findIdByEmailAddresses(email, null, conn);
                fail("Null ConextId parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null ConextId parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.findIdByEmailAddresses(email, context.getId(), null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.findIdByEmailAddresses(email, context.getId(), conn);

            assertNotNull("Expecting not null PersistentKey result object", res);
            assertTrue("Expecting another PersistentKey result object", res != null && res.equals(user.getId()));

            res = _peer.findIdByEmailAddresses(email, FAKE_ID, conn);
            assertTrue("Expecting null PersistentKey result object by Fake context Id", res == null);

        } finally {
            conn.rollback();
            conn.close();
        }

    }

    public void testFindDeleteNotificationIds()
            throws Exception {
        final InstallationContext context = getContext();

        new JdbcQuery(this) {
            protected Object query(Connection conn)
                    throws SQLException {
                Set res = null;
                try {
                    res = _peer.findDeleteNotificationIds(null, conn);
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null PersistentKey parameter should throw IllegalArgumentException.");
                }

                try {
                    res = _peer.findDeleteNotificationIds(FAKE_ID, null);
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }

                res = _peer.findDeleteNotificationIds(FAKE_ID, conn);
                assertNotNull("Null result.", res);
                assertTrue("Expecting empty result for fake Entity id '" + FAKE_ID + "'.", res.isEmpty());

                User user = User.createNewUser("_TestEntityPeer_findOwnedWorkIds_", context);

                Work work = Work.createNew(Work.TYPE, "_TestEntityPeer_findOwnedWorkIds_", User.getNobody(context));

                try {
                    user.save(conn);
                    conn.commit();

                    work.save(conn);
                    conn.commit();

                    res = _peer.findDeleteNotificationIds(User.getNobody(context).getId(), conn);
                    assertNotNull("Null result.", res);
                    assertTrue("Expecting created work Id in result Set for Nobody.", res.contains(work.getId()));

                    work.setOwnerId(user.getId());
                    work.save(conn);
                    conn.commit();

                    res = _peer.findDeleteNotificationIds(user.getId(), conn);
                    assertNotNull("Null result.", res);
                    assertTrue("Expecting another Id's count in result Set for new work.", res.size() == 2);
                    assertTrue("Expecting created work Id in result Set for new user.", res.contains(work.getId()) &&
                            res.contains(user.getFolder().getId()));

                } finally {
                    delete(work, conn);
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindPersonIdsByLastName()
            throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();

        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {

                String lastName = "_LName_";
                String firstName = "_FName_";

                Collection types = context.getContextTypes().getTypesInBranch(Person.TYPE);
                Collection emptyTypes = new ArrayList();
                Collection res = null;

                try {
                    res = _peer.findPersonIdsByLastName(null, types, conn);
                    fail("Null context id should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null context parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByLastName(contextId, null, conn);
                    fail("Null types should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null types set should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByLastName(contextId, types, null);
                    fail("Null Connection should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByLastName(contextId, emptyTypes, conn);
                    fail("empty types set should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("empty types set parameter should throw IllegalArgumentException.");
                }

                res = _peer.findPersonIdsByLastName(FAKE_ID, types, conn);
                assertNotNull("Null result Set with Fake context.", res);
                assertTrue("Expecting empty result Set with Fake context.", res.isEmpty());

                User user1 = User.createNewUser("email_1", context);
                user1.setFirstName(firstName);
                user1.setLastName(lastName);

                try {
                    res = _peer.findPersonIdsByLastName(contextId, types, conn);
                    assertNotNull("Expecting not null result Set", res);
                    assertTrue("Expecting not empty result Set", res.size() > 0);
                    int beforeCount = res.size();

                    user1.save(conn);
                    conn.commit();

                    res = _peer.findPersonIdsByLastName(contextId, types, conn);
                    assertNotNull("Null result Set with types set.", res);
                    assertTrue("Expecting not empty result Set", res.size() > 0);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting another difference between result Sets before and after user inserting. get: " + (beforeCount - res.size()), (res.size() - beforeCount) == 1);
                        assertTrue("Expecting test user Id in result set.", res.contains(user1.getId()));
                    }

                } finally {
                    delete(user1, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testFindPersonIdsByName()
            throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();

        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {

                String lastName = "_LName_";
                String firstName = "_FName_";
                String prefix = "_p_";

                Collection types = context.getContextTypes().getTypesInBranch(Person.TYPE);
                Collection emptyTypes = new ArrayList();
                Collection fakeTypes = context.getContextTypes().getTypesInBranch(Work.TYPE);
                Collection res = null;
                try {
                    res = _peer.findPersonIdsByName(null, contextId, types, conn);
                    fail("Null name should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null name parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByName(lastName, null, types, conn);
                    fail("Null context id should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null context parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByName(lastName, contextId, null, conn);
                    fail("Null types should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null types set should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByName(lastName, contextId, types, null);
                    fail("Null Connection should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }
                try {
                    res = _peer.findPersonIdsByName(lastName, contextId, emptyTypes, conn);
                    fail("empty types set should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("empty types set parameter should throw IllegalArgumentException.");
                }

                res = _peer.findPersonIdsByName(lastName, FAKE_ID, types, conn);
                assertNotNull("Null result Set with Fake context.", res);
                assertTrue("Expecting empty result Set with Fake context.", res.isEmpty());


                User user1 = null;
                User user2 = null;

                try {
                    user1 = User.createNewUser("_TestEntityPeer_findPersonIdsByName1_", context);
                    user1.setFirstName(firstName);
                    user1.setLastName(lastName);
                    user1.save(conn);
                    conn.commit();

                    user2 = User.createNewUser("_TestEntityPeer_findPersonIdsByName2_", context);
                    user2.setFirstName(prefix.concat(firstName));
                    user2.setLastName(lastName.concat(prefix));
                    user2.save(conn);
                    conn.commit();

                    res = _peer.findPersonIdsByName("", contextId, types, conn);
                    assertNotNull("Null result Set with empty types set.", res);
                    assertTrue("Expecting non empty result Set with empty request string.", !res.isEmpty());

                    res = _peer.findPersonIdsByName(firstName, contextId, fakeTypes, conn);
                    assertNotNull("Null result Set with empty types set.", res);
                    assertTrue("Expecting empty result Set with empty request string.", res.isEmpty());

                    res = _peer.findPersonIdsByName(firstName, contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("Expecting 2 Ids in result Set in search by first name. get " + res.size(), res != null && res.size() == 2);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting both test users Ids in result set in search by first name.", res.contains(user1.getId()) && res.contains(user2.getId()));
                    }

                    res = _peer.findPersonIdsByName(lastName, contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("Expecting 2 Ids in result Set in search by last name.", res != null && res.size() == 2);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting both test users Ids in result set in search by last name.", res.contains(user1.getId()) && res.contains(user2.getId()));
                    }

                    res = _peer.findPersonIdsByName(firstName + ' ' + lastName, contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("Expecting 2 Ids in result Set in search by full name.", res != null && res.size() == 2);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting both test users Ids in result set in search by full name.", res.contains(user1.getId()) && res.contains(user2.getId()));
                    }

                    res = _peer.findPersonIdsByName("teststring", contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting both test users Ids not in result set", (!res.contains(user1.getId())) && (!res.contains(user2.getId())));
                    }

                    res = _peer.findPersonIdsByName("teststring", contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting both test users Ids not in result set", (!res.contains(user1.getId())) && (!res.contains(user2.getId())));
                    }

                    res = _peer.findPersonIdsByName(prefix.concat(firstName), contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("Expecting 1 Id in result Set in search by first name.", res != null && res.size() == 1);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting another test user Id in result set", (!res.contains(user1.getId())) && res.contains(user2.getId()));
                    }

                    res = _peer.findPersonIdsByName(lastName.concat(prefix), contextId, types, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("Expecting 1 Id in result Set in search by last name.", res != null && res.size() == 1);
                    if (res != null && !res.isEmpty()) {
                        assertTrue("Expecting another test user Ids in result set", (!res.contains(user1.getId())) && res.contains(user2.getId()));
                    }
                } finally {
                    delete(user1, conn);
                    delete(user2, conn);
                }

                return null;
            }
        }.execute();
    }

    public void testListAllGroupIds() throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();

        new JdbcQuery(this) {

            protected Object query(Connection conn) throws SQLException {

                Set res = null;

                try {
                    res = _peer.listAllGroupIds(null, conn);
                    fail("Null InstallationContext parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null InstallationContext parameter should throw IllegalArgumentException.");
                }

                try {
                    res = _peer.listAllGroupIds(contextId, null);
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null Connection parameter should throw IllegalArgumentException.");
                }

                Group group = Group.createNew("__TestEntityPeer_getAllGroups1__", User.getNobody(context), context);

                try {
                    res = _peer.listAllGroupIds(contextId, conn);
                    assertNotNull("Null result Set.", res);
                    int beforeCount = res.size();

                    group.save(conn);
                    conn.commit();

                    res = _peer.listAllGroupIds(contextId, conn);
                    assertNotNull("Null result Set.", res);
                    assertTrue("expecting another difference of result Sets after and before group inserting. get: " + (beforeCount - res.size()), (beforeCount - res.size()) != 1);
                    assertTrue("result Set don't contains test group id.", res.contains(group.getId()));
                } finally {
                    delete(group, conn);
                }
                return null;
            }
        }.execute();
    }

    public void testInsertDelete() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "_testEntityPeer_testInsertDel_";

        conn.setAutoCommit(false);

        try {
            try {
                _peer.insert(null, conn);
                fail("Null Entity Object in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Entity Object in insert method should throw IllegalArgumentException.");
            }

            User user = User.createNewUser(email, context);

            try {
                _peer.insert(user, null);
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            }

            _peer.insert(user, conn);
            PersistentKey userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);

            assertNotNull("Expecting not null PersistentKey result object", userId);

            RestoreMap data = _peer.getRestoreData(user.getId(), conn);
            assertTrue("Expecting data for new inserted user", compare(user, data));

            try {
                _peer.delete(null, conn);
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            }

            try {
                _peer.delete(user, null);
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            }

            _peer.delete(user, conn);
            userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);
            assertNull("Expecting null PersistentKey result object", userId);
        } finally {
            conn.rollback();
            conn.close();
        }

    }

    public void testUpdateRestore() throws Exception {
        Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final String email = "_TestEntityPeer_testUpdateRestore_";


//existing Update
        conn.setAutoCommit(false);

        try {
            User user = User.createNewUser(email, context);
            _peer.insert(user, conn);
            PersistentKey userId = _peer.findIdByEmailAddresses(email, context.getId(), conn);
            assertNotNull("Expecting not null PersistentKey result object", userId);

            try {
                _peer.update(null, conn);
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Entity Object in delete method should throw IllegalArgumentException.");
            }

            try {
                _peer.update(user, null);
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            }

            user.setLastName("LastName");
            _peer.update(user, conn);
        } finally {
            conn.rollback();
            conn.close();
        }

//existing Restore
        conn = getConnection();
        conn.setAutoCommit(false);

        try {

            User user = User.createNewUser(email, context);
            _peer.insert(user, conn);
            RestoreMap res = null;

            try {
                res = _peer.getRestoreData(null, conn);
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null PersistentKey parameter should throw IllegalArgumentException.");
            }

            try {
                res = _peer.getRestoreData(FAKE_ID, null);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            res = _peer.getRestoreData(user.getId(), conn);

            assertNotNull("Expecting not null Hashtable result object", res);
        } finally {
            conn.rollback();
            conn.close();
        }

//additional test
        new JdbcQuery(this) {
            private void setEntityContain(Person psn, boolean withPrefix) {
                String fakeString = "entityFakeStr";
                Float fakeFloat = Float.valueOf("1.5");
                if (withPrefix) {
                    fakeString += "_p";
                    fakeFloat = Float.valueOf("2.5");
                }

                psn.setAddressLine1(fakeString);
                psn.setAddressLine2(fakeString);
                psn.setCity(fakeString);
                psn.setCompanyName(fakeString);
                psn.setCountry(fakeString);
                psn.setDepartment(fakeString);
                psn.setFirstName(fakeString);
                psn.setJobTitle(fakeString);
                psn.setLastName(fakeString);
                psn.setPostalCode("111222");
                psn.setSkills(fakeString);
                psn.setState("MS");
                psn.setLaborBillingRate(fakeFloat);
                psn.setLaborCostRate(fakeFloat);

            }

            protected Object query(Connection conn) throws SQLException {
                User user = null;
                try {
                    user = User.createNewUser("_TestEntityPeer_Update_", context);

                    setEntityContain(user, false);

                    user.save(conn);
                    conn.commit();

                    RestoreMap data = _peer.getRestoreData(user.getId(), conn);
                    assertNotNull("Expecting data for user id", data);
                    assertTrue("Expecting valid user attributes", compare(user, data));

                    setEntityContain(user, true);

                    _peer.update(user, conn);
                    conn.commit();

                    data = _peer.getRestoreData(user.getId(), conn);
                    assertNotNull("Expecting data for user id", data);
                    assertTrue("Expecting valid user attributes", compare(user, data));
                } finally {
                    delete(user, conn);
                }
                return null;
            }
        }.execute();
    }

    private boolean compare(Entity ent, RestoreMap data) {
        boolean equal = true;
        if (equal && !equals(ent.getAddressLine1(), data.get(Entity.ADDRESS_LINE_1)) && !(equal = false)) {
            err(Entity.ADDRESS_LINE_1);
        }
        if (equal && !equals(ent.getAddressLine2(), data.get(Entity.ADDRESS_LINE_2)) && !(equal = false)) {
            err(Entity.ADDRESS_LINE_2);
        }
        if (equal && !equals(ent.getCity(), data.get(Entity.CITY)) && !(equal = false)) {
            err(Entity.CITY);
        }
        if (equal && !equals(ent.getCompanyName(), data.get(Entity.COMPANY_NAME)) && !(equal = false)) {
            err(Entity.COMPANY_NAME);
        }
        if (equal && !equals(ent.getCountry(), data.get(Entity.COUNTRY)) && !(equal = false)) {
            err(Entity.COUNTRY);
        }
        if (equal && !equals(ent.getDepartment(), data.get(Entity.DEPARTMENT)) && !(equal = false)) {
            err(Entity.DEPARTMENT);
        }
        if (equal && !equals(ent.getEmailAddress(), data.get(Entity.EMAIL_ADDRESS)) && !(equal = false)) {
            err(Entity.EMAIL_ADDRESS);
        }
        if (equal && !equals(ent.getDescription(), data.get(Entity.DESCRIPTION)) && !(equal = false)) {
            err(Entity.DESCRIPTION);
        }
        if (equal && !equals(ent.getEntityUrl(), data.get(Entity.ENTITY_URL)) && !(equal = false)) {
            err(Entity.ENTITY_URL);
        }
        if (equal && !equals(ent.getFirstName(), data.get(Entity.FIRST_NAME)) && !(equal = false)) {
            err(Entity.FIRST_NAME);
        }
        if (equal && !equals(ent.getJobTitle(), data.get(Entity.JOB_TITLE)) && !(equal = false)) {
            err(Entity.JOB_TITLE);
        }
        if (equal && !equals(ent.getLaborBillingRate(), data.get(Entity.LABOR_BILLING_RATE)) && !(equal = false)) {
            err(Entity.LABOR_BILLING_RATE);
        }
        if (equal && !equals(ent.getLaborCostRate(), data.get(Entity.LABOR_COST_RATE)) && !(equal = false)) {
            err(Entity.LABOR_COST_RATE);
        }
        if (equal && !equals(ent.getLastName(), data.get(Entity.LAST_NAME)) && !(equal = false)) {
            err(Entity.LAST_NAME);
        }
        if (equal && !equals(ent.getOwnerId(), data.get(Entity.OWNER_ID)) && !(equal = false)) {
            err(Entity.OWNER_ID);
        }
        if (equal && !equals(ent.getPostalCode(), data.get(Entity.POSTAL_CODE)) && !(equal = false)) {
            err(Entity.POSTAL_CODE);
        }
        if (equal && !equals(ent.getSkills(), data.get(Entity.SKILLS)) && !(equal = false)) {
            err(Entity.SKILLS);
        }
        if (equal && !equals(ent.getState(), data.get(Entity.STATE)) && !(equal = false)) {
            err(Entity.STATE);
        }
        if (equal && !equals(ent.getWorkPhone(), data.get(Entity.WORK_PHONE)) && !(equal = false)) {
            err(Entity.WORK_PHONE);
        }
        return equal;
    }

    private boolean equals(Object o1, Object o2, boolean notNull) {
        if (o1 == null && o2 == null)
            return (!notNull);
        else
            return equals(o1, o2);
    }

    private boolean equals(Timestamp data1, Object data2, boolean notNull) {
        if (data1 == null && data2 == null)
            return (!notNull);
        else
            return equals(data1, data2);
    }

    private boolean equals(Object o1, Object o2) {
        if (o1 == null && o2 == null) return true;
        if (o1 == null || o2 == null) return false;
        return (o1.equals(o2));
    }

    private boolean equals(Timestamp data1, Object data2) {
        if (data1 == null && data2 == null) return true;
        if (!(data2 instanceof Timestamp)) return false;
        data1.setNanos(0);
        ((Timestamp) data2).setNanos(0);
        return data1.equals(data2);
    }

    private void err(String attrName) {
        System.out.println("Wrong value of '" + attrName + "' attribute");
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

    public void testFindEntityWorkIds() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {

                try {
                    _peer.findEntityWorkIds(null, conn);
                    fail("Null entity id should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null entity id should throw IllegalArgumentException exception");
                }
                try {
                    _peer.findEntityWorkIds(FAKE_ID, null);
                    fail("Null connection should throw IllegalArgumentException exception");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("Null connection should throw IllegalArgumentException exception");
                }

                Set res = _peer.findEntityWorkIds(FAKE_ID, conn);

                assertNotNull("expecting not null result set", res);
                assertTrue("expecting empty result set", res.isEmpty());

                User user1 = User.createNewUser("_findEntityWorkIds_user1", context);
                User user2 = User.createNewUser("_findEntityWorkIds_user2", context);

                Work work1 = Work.createNew(Work.TYPE, "_findEntityWorkIds_wrk1", user1);
                Work work2 = Work.createNew(Work.TYPE, "_findEntityWorkIds_wrk1", user2);

                Tollgate tlg = (Tollgate) Tollgate.createNew(Tollgate.TYPE, "_findEntityWorkIds_tlg", user2);

                try {
                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();

                    res = _peer.findEntityWorkIds(user1.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting result set only with entity folder", res.size() == 1);
                    assertTrue("expecting result set only with entity folder", res.contains(user1.getFolder().getId()));

                    res = _peer.findEntityWorkIds(user2.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting result set only with entity folder", res.size() == 1);
                    assertTrue("expecting result set only with entity folder", res.contains(user2.getFolder().getId()));

                    work1.save(conn);
                    work2.save(conn);
                    tlg.save(conn);
                    conn.commit();
                    tlg.addChampion(user1);
                    work2.addTeamMember(user1, user2);
                    work2.save(conn);
                    tlg.save(conn);
                    conn.commit();

                    res = _peer.findEntityWorkIds(user1.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting another items count in result set. got: " + res.size(), res.size() == 4);
                    assertTrue("expecting another items in result set",
                            res.contains(work1.getId()) &&
                            res.contains(work2.getId()) &&
                            res.contains(tlg.getId()) &&
                            res.contains(user1.getFolder().getId()));

                    res = _peer.findEntityWorkIds(user2.getId(), conn);
                    assertNotNull("expecting not null result set", res);
                    assertTrue("expecting another items count in result set. got: " + res.size(), res.size() == 3);
                    assertTrue("expecting another items in result set", res.contains(work2.getId()) &&
                            res.contains(tlg.getId()) &&
                            res.contains(user2.getFolder().getId()));

                } finally {
                    delete(work1, conn);
                    delete(work2, conn);
                    delete(tlg, conn);
                    delete(user1, conn);
                    delete(user2, conn);
                }

                return null;
            }
        }.execute();
    }

    // gags
    public void testInsert() {
    }

    public void testUpdate() {
    }

    public void testDelete() {
    }

    public void testCountEmailAddresses() { /** testCountEmailAddressesContract */
    }

    public void testGetEntityPeer() {
    }
}
