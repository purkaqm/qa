package com.cinteractive.ps3.metrics;

import com.cinteractive.calc.parse.SimpleValue;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.jdbc.peers.TestJdbcPeer;
import com.cinteractive.ps3.jdbc.peers.TestSql;
import com.cinteractive.ps3.questions.Question;
import com.cinteractive.ps3.work.Work;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestMetricReminderAgentSql extends TestJdbcPeer {
    private MetricReminderAgentSql _peer;

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(MetricReminderAgentSql.class.getName(), TestMetricReminderAgentSql.class.getName());
    }

    public TestMetricReminderAgentSql(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestMetricReminderAgentSql("testFindMetricIds"));

        return suite;
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (MetricReminderAgentSql) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return MetricReminderAgentSql.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null MetricReminderAgentSql peer instance.");
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    private static final boolean DEBUG = true; //show debug info

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
                //if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }

    private Work createWork(String name, User user) {
        final Work work = Work.createNew(Work.TYPE, name, user);
        return work;
    }

    public void testFindMetricIds() throws Exception {
        final InstallationContext context = getContext();
        final PersistentKey contextId = context.getId();

        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                Calendar gc = Calendar.getInstance();
                gc.setTime(new Date(System.currentTimeMillis()));
                gc.set(Calendar.MILLISECOND, 0);
                long base = gc.getTime().getTime();
                Timestamp monthBefore = new Timestamp(base - 31L * 24L * 60L * 60L * 1000L);

                final MetricReminderAgent.Tuple tuple1 = new MetricReminderAgent.Tuple(
                        new MetricPeriod(MetricPeriodCode.MONTHLY,
                                new Timestamp(base)),
                        MetricReminder.BEFORE_PERIOD) {
                };
                MetricReminderAgent.Tuple tuple2 = new MetricReminderAgent.Tuple(
                        new MetricPeriod(MetricPeriodCode.QUARTERLY,
                                new Timestamp(base)),
                        MetricReminder.AFTER_PERIOD) {
                };

                Set res = null;
                List tuples = new java.util.ArrayList();
                tuples.add(tuple1);
                try {
                    _peer.findMetricIds(null, tuples, conn);
                    fail("Null context id should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null context id should throw IllegalArgumentException.");
                }
                try {
                    _peer.findMetricIds(contextId, null, conn);
                    fail("Null tuples list should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null tuples list should throw IllegalArgumentException.");
                }
                try {
                    _peer.findMetricIds(contextId, tuples, null);
                    fail("Null connection should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection should throw IllegalArgumentException.");
                }

                res = _peer.findMetricIds(FAKE_ID, tuples, conn);
                assertNotNull("Expecting empty result set for fake context, not null.", res);
                assertTrue("Expecting empty result set for fake context.", res.isEmpty());

                User user1 = User.createNewUser("testMetricReminderAgtSql_usr1", context);
                user1.setLastName("testMetricReminderAgtSql_usr1");
                User user2 = User.createNewUser("testMetricReminderAgtSql_usr2", context);
                user2.setLastName("testMetricReminderAgtSql_usr2");

                MetricTemplate mt1 = MetricTemplate.createNew(MetricTemplate.TYPE,
                        "testMetricReminderAgtSql_mt1", user1.getId(), context);
                MetricTemplateItem mti = MetricTemplateItem.createNew("ti1", mt1.getItems(), MetricTemplateItem.USER_DEFINED_ITEM);
                mti.setSequence(new Integer(1));

                mt1.setStartDate(new Timestamp(base - 31L * 24L * 60L * 60L * 1000L));
                mt1.setEndDate(new Timestamp(base + 31L * 24L * 60L * 60L * 1000L));
                mt1.setPeriod(MetricPeriodCode.MONTHLY);
                mt1.setRemind(MetricReminder.BEFORE_PERIOD);

                MetricTemplate mt2 = MetricTemplate.createNew(MetricTemplate.TYPE,
                        "testMetricReminderAgtSql_mt2", user2.getId(), context);
                mt2.setStartDate(new Timestamp(base - 6L * 31L * 24L * 60L * 60L * 1000L));
                mt2.setEndDate(new Timestamp(base + 6L * 31L * 24L * 60L * 60L * 1000L));
                mt2.setPeriod(MetricPeriodCode.QUARTERLY);
                mt2.setRemind(MetricReminder.AFTER_PERIOD);
                Work work1 = createWork("testMes_work1", user1);
                Work work2 = createWork("testMes_work2", user2);
                Work work3 = createWork("testMes_work3", user1);
                MetricInstance mi1 = MetricInstance.createNew(mt1, work1, false);
                MetricInstance mi2 = MetricInstance.createNew(mt1, work2, false);
                MetricInstance mi3 = MetricInstance.createNew(mt2, work3, false);

                View v1 = View.createNew("testMes_view1", mt1);
                v1.addTag("testMes_tag1");	//@@

                Measurements mss1 = new Measurements(v1.getTag("testMes_tag1").getId(), mi2.getId());
                Measurement ms1 = null;//mss1.createMeasurement(mti.getId(), null);
                MeasurementItem msi1 = MeasurementItem.createNew(ms1, tuple1.getPeriod().getTimestamp(), SimpleValue.instantiate(new Double(1.1)));
                MeasurementItem msi2 = MeasurementItem.createNew(ms1, monthBefore, SimpleValue.instantiate(new Double(2.2)));
                ms1.putMeasurementItem(tuple1.getPeriod().getTimestamp(), msi1);
                ms1.putMeasurementItem(monthBefore, msi2);
                mss1.putMeasurement(mti.getId(), null, ms1);

                Object[] args = new Object[]{mi3, tuple2.getPeriod()};
                Question que = Question.createNew(user1, new MetricQuestionHandler(), args);

                try {
                    int baseCount = _peer.findMetricIds(contextId, tuples, conn).size();

                    user1.save(conn);
                    user2.save(conn);
                    conn.commit();
                    work1.save(conn);
                    work2.save(conn);
                    work3.save(conn);
                    conn.commit();

                    mt1.save(conn);
                    mt2.save(conn);
                    conn.commit();
                    v1.save(conn);
                    mti.save(conn);
                    mi1.save(conn);
                    mi2.save(conn);
                    mi3.save(conn);
                    conn.commit();

                    res = _peer.findMetricIds(contextId, tuples, conn);
                    assertTrue("Expecting " + (baseCount + 2) + " items count in result set. got: " + res.size(), res.size() == baseCount + 2);
                    assertTrue("Expecting another items in result set.",
                            res.contains(mi1.getId()) && res.contains(mi2.getId()));

                    tuples.add(tuple2);

                    res = _peer.findMetricIds(contextId, tuples, conn);
                    assertTrue("Expecting " + (baseCount + 3) + " items count in resoult set. got: " + res.size(), res.size() == baseCount + 3);
                    assertTrue("Expecting another items in result set.",
                            res.contains(mi1.getId()) && res.contains(mi2.getId())
                            && res.contains(mi3.getId()));

                    mss1.save(conn);
                    conn.commit();

                    res = _peer.findMetricIds(contextId, tuples, conn);
                    assertTrue("Expecting " + (baseCount + 2) + " items count in result set. got: " + res.size(), res.size() == baseCount + 2);
                    assertTrue("Expecting another items in result set.",
                            res.contains(mi1.getId())
                            && res.contains(mi3.getId()));

                    que.save(conn);
                    conn.commit();

                    res = _peer.findMetricIds(contextId, tuples, conn);
                    assertTrue("Expecting " + (baseCount + 1) + " items count in result set. got: " + res.size(), res.size() == baseCount + 1);
                    assertTrue("Expecting another items in result set.",
                            res.contains(mi1.getId()));
                } finally {
                    try {
                        delete(que, conn);
                        delete(mi1, conn);
                        delete(mi2, conn);
                        delete(mi3, conn);
                        delete(v1, conn);
                        delete(mti, conn);
                        conn.commit();
                        delete(mt1, conn);
                        delete(mt2, conn);
                        conn.commit();
                        delete(work1, conn);
                        delete(work2, conn);
                        delete(work3, conn);
                        conn.commit();
                        delete(user1, conn);
                        delete(user2, conn);
                        conn.commit();
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }
}