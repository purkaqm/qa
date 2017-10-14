package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.jdbc.JdbcQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.discussion.DiscussionItemType;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.jdbc.DataSourceId;

public class TestDiscussionItemPeer extends TestJdbcPeer {
    private DiscussionItemPeer _peer;
    private static final boolean DEBUG = true; //show debug info

    static {
        registerCase(StringDataSourceId.get(StringDataSourceId.MSSQL7));
    }

    private static void registerCase(DataSourceId id) {
        TestSql.registerTestCase(DiscussionItemPeer.class.getName(), TestDiscussionItemPeer.class.getName());
    }

    public TestDiscussionItemPeer(String name) {
        super(name);
    }

    public static Test bareSuite() {
        final TestSuite suite = new TestSuite("TestDiscussionItemPeer");
        suite.addTest(new TestDiscussionItemPeer("testSimpleSearch"));
        suite.addTest(new TestDiscussionItemPeer("testInsertDelete"));
        suite.addTest(new TestDiscussionItemPeer("testTextSearch"));
        suite.addTest(new TestDiscussionItemPeer("testUpdate"));
        suite.addTest(new TestDiscussionItemPeer("testDeleteByDiscussionId"));
        suite.addTest(new TestDiscussionItemPeer("testFindByDiscussion"));
        suite.addTest(new TestDiscussionItemPeer("testTextDatesSearch"));

        return suite;
    }

    public void testGetInstance() throws Exception {
    }

    public void testInsert() throws Exception { /** see testInsertDelete */
    }

    public void testDelete() throws Exception { /** see testInsertDelete */
    }

    public void setUp() throws Exception {
        super.setUp();

        _peer = (DiscussionItemPeer) new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                return DiscussionItemPeer.getInstance(conn);
            }
        }.execute();

        if (_peer == null)
            throw new NullPointerException("Null DiscussionItemPeer peer instance.");
    }

    public static Test suite() {
        return setUpDb(bareSuite());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        _peer = null;
    }

    public void testInsertDelete() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final Timestamp today = new Timestamp(new java.util.Date().getTime());


        conn.setAutoCommit(false);

        try {
            try {
                _peer.insert(null, conn);
                fail("Null DiscussionItem Object in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null DiscussionItem Object in insert method should throw IllegalArgumentException.");
            }

            DiscussionItem item = DiscussionItem.createNew( WorkUtil.getNoProject(context), null, User.getNobody(context), "title", "text");
            item.setLastChangeDate(today);

            try {
                _peer.insert(item, null);
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in insert method should throw IllegalArgumentException.");
            }

            _peer.insert(item, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select discussion_item_id from discussion_item where discussion_item_id='" + item.getId() + "'");

            assertTrue("Expecting record in resultset", rset.next());

            try {
                _peer.delete(null, conn);
                fail("Null DiscussionItem Object in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null DiscussionItem Object in delete method should throw IllegalArgumentException.");
            }

            try {
                _peer.delete(item, null);
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in delete method should throw IllegalArgumentException.");
            }

            _peer.delete(item, conn);

            rset = stmt.executeQuery("select discussion_item_id from discussion_item where discussion_item_id='" + item.getId() + "'");
            assertTrue("Expecting record in resultset", !rset.next());
        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private boolean containItems(List res, Collection ims) {
        items: for (Iterator i = ims.iterator(); i.hasNext();) {
            PersistentKey[] tmpItem = (PersistentKey[]) i.next();
            for (Iterator r = res.iterator(); r.hasNext();) {
                PersistentKey[] tmpres = (PersistentKey[]) r.next();
                if ((tmpItem[0].equals(tmpres[0])) && (tmpItem[1].equals(tmpres[1]))) {
                    continue items;
                }
            }// result
            return false;
        }//items
        return true;
    }

    public void testSimpleSearch() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                List res = null;
                Collection fakeDiscussions = new ArrayList(1);
                fakeDiscussions.add(FAKE_ID);
                try {
                    _peer.simpleSearch(null, fakeDiscussions, null, false, conn);
                    fail("Null search string in simple search method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null search string in simple search method should throw IllegalArgumentException.");
                }
                try {
                    _peer.simpleSearch("blah", null, null, false, conn);
                    fail("Null discussions connection in simple search method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null discussions connection in simple search method should throw IllegalArgumentException.");
                }
                try {
                    _peer.simpleSearch("blah", fakeDiscussions, null, false, conn);
                    fail("Null item type in simple search method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null item type in simple search method should throw IllegalArgumentException.");
                }
                try {
                    _peer.simpleSearch("blah", fakeDiscussions, null, false, null);
                    fail("Null connection in simple search method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection in simple search method should throw IllegalArgumentException.");
                }

                Work disc1 = Work.createNew(Work.TYPE, "discussionPeer_desc1", User.getNobody(context));
                disc1.generateAlerts(false);
                Work disc2 = Work.createNew(Work.TYPE, "discussionPeer_desc2", User.getNobody(context));
                disc2.generateAlerts(false);
                DiscussionItem di1 = null;
                DiscussionItem di2 = null;
                DiscussionItem di3 = null;
                DiscussionItem di4 = null;
                DiscussionItem di5 = null;
                DiscussionItem di6 = null;
                try {
                    disc1.save(conn);
                    disc2.save(conn);
                    conn.commit();
                    di1 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc1, null, User.getNobody(context), "DiscussionTest_title11", "DiscussionTest_text11");
                    di1.setKeyword("DiscussionTest_key11");
                    di1.save(conn);
                    conn.commit();
                    di2 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc1, di1,  User.getNobody(context), "DiscussionTest_title12", "DiscussionTest_text12");
                    di2.setKeyword("DiscussionTest_key12");
                    di2.save(conn);
                    di3 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc1, null, User.getNobody(context), "DiscussionTest_title13", "DiscussionTest_text13");
                    di3.setKeyword("DiscussionTest_key13");
                    di3.save(conn);
                    di4 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc2, null, User.getNobody(context), "DiscussionTest_title24", "DiscussionTest_text24");
                    di4.setKeyword("DiscussionTest_key24");
                    di4.save(conn);
                    conn.commit();
                    di5 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc2, di4,  User.getNobody(context), "DiscussionTest_title25", "DiscussionTest_text25");
                    di5.setKeyword("DiscussionTest_key25");
                    di5.save(conn);
                    di6 = DiscussionItem.createNew(DiscussionItem.TYPE,
                            disc2, null, User.getNobody(context), "DiscussionTest_title26", "DiscussionTest_text26");
                    di6.setKeyword("DiscussionTest_key26");
                    di6.save(conn);
                    conn.commit();

                    Collection discussions = new ArrayList(2);
                    discussions.add(disc1.getId());
                    discussions.add(disc2.getId());

                    Collection disc1Ids = new ArrayList(3);
                    disc1Ids.add(new PersistentKey[]{di1.getId(), disc1.getId()});
                    disc1Ids.add(new PersistentKey[]{di2.getId(), disc1.getId()});
                    disc1Ids.add(new PersistentKey[]{di3.getId(), disc1.getId()});
                    Collection disc2Ids = new ArrayList(3);
                    disc2Ids.add(new PersistentKey[]{di4.getId(), disc2.getId()});
                    disc2Ids.add(new PersistentKey[]{di5.getId(), disc2.getId()});
                    disc2Ids.add(new PersistentKey[]{di6.getId(), disc2.getId()});
                    Collection c = Arrays.asList(new Object[]{DiscussionItemType.getTypeByName("DiscussionItem")});
                    res = _peer.simpleSearch("DiscussionTest_title", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by title).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by title). Got " + res.size(), (res.size() == 6));
                    assertTrue("Expecting another Discussion items in result Set (by title).",
                            containItems(res, disc1Ids) && containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_text", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by text).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by text). Got " + res.size(), (res.size() == 6));
                    assertTrue("Expecting another Discussion items in result Set (by text).",
                            containItems(res, disc1Ids) && containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_key", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by key).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by key). Got " + res.size(), (res.size() == 6));
                    assertTrue("Expecting another Discussion items in result Set (by key).",
                            containItems(res, disc1Ids) && containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_title1", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by title).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by title). Got " + res.size(), (res.size() == 3));
                    assertTrue("Expecting another Discussion items in result Set (by title).",
                            containItems(res, disc1Ids) && !containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_text1", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by text).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by text). Got " + res.size(), (res.size() == 3));
                    assertTrue("Expecting another Discussion items in result Set (by text).",
                            containItems(res, disc1Ids) && !containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_key1", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (by key).", res);
                    assertTrue("Expecting another Discussion items count in result Set (by key). Got " + res.size(), (res.size() == 3));
                    assertTrue("Expecting another Discussion items in result Set (by key).",
                            containItems(res, disc1Ids) && !containItems(res, disc2Ids));

                    res = _peer.simpleSearch("test_title", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (ignore case by title).", res);
                    assertTrue("Expecting another Discussion items count in result Set (ignore case by title). Got " + res.size(), (res.size() == 6));
                    assertTrue("Expecting another Discussion items in result Set (ignore case by title).",
                            containItems(res, disc1Ids) && containItems(res, disc2Ids));

                    res = _peer.simpleSearch("test_key", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set, get null (ignore case by key).", res);
                    assertTrue("Expecting another Discussion items count in result Set (ignore case by key). Got " + res.size(), (res.size() == 6));
                    assertTrue("Expecting another Discussion items in result Set (ignore case by key).",
                            containItems(res, disc1Ids) && containItems(res, disc2Ids));

                    res = _peer.simpleSearch("DiscussionTest_key", fakeDiscussions, c, false, conn);
                    assertNotNull("Expecting empty Discussion items Set for fake discussions, get null.", res);
                    assertTrue("Expecting empty Discussion items result Set for fake discussions. Got " + res.size(), res.isEmpty());

                    res = _peer.simpleSearch("", discussions, c, false, conn);
                    assertNotNull("Expecting not empty Discussion items Set for empty search string, get null.", res);
                    assertTrue("Expecting not empty Discussion items result Set for fake discussions. Got " + res.size(), !res.isEmpty());
                } finally {
                    try {
                        di2.delete(conn);
                        di1.delete(conn);
                        di3.delete(conn);
                        di5.delete(conn);
                        di4.delete(conn);
                        di6.delete(conn);
                        conn.commit();
                        disc1.deleteHard(conn);
                        disc2.deleteHard(conn);
                        conn.commit();
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    public void testTextSearch() throws Exception {
        if (!getContext().isFullTextInstall()) {
            System.out.println("TestDiscussionItemPeer::testTextSearch>> Warning! Full text search engine is not installed. Exit.");
            return;
        }
        final Connection conn = getConnection();
        final List phrases = new LinkedList();
        List res = null;

        try {
            try {
                //res = _peer.textDatesSearch(1, null, null, null, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);
                fail("Null List parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null List parameter should throw IllegalArgumentException.");
            }

            try {
                //res = _peer.textDatesSearch(1, null, null, null, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);
                fail("Null Connection parameter should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection parameter should throw IllegalArgumentException.");
            }

            phrases.add("FirstPhrase");
            phrases.add("SecondPhrase");
            //res = _peer.textDatesSearch(1, phrases, null, null, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);

            assertNotNull("Expecting not null result list", res);

        } finally {
            conn.close();
        }

    }

    public void testUpdate() throws Exception {
        final Connection conn = getConnection();
        final InstallationContext context = InstallationContext.get(getContextName());
        final Timestamp today = new Timestamp(new java.util.Date().getTime());

        conn.setAutoCommit(false);

        try {
            DiscussionItem item = DiscussionItem.createNew( WorkUtil.getNoProject(context), null, User.getNobody(context), "title", "text");
            item.setLastChangeDate(today);
            _peer.insert(item, conn);

            final Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery("select discussion_item_id from discussion_item where discussion_item_id='" + item.getId() + "'");

            assertTrue("Expecting record in resultset", rset.next());

            try {
                _peer.update(null, conn);
                fail("Null DiscussionItem Object in update method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null DiscussionItem Object in update method should throw IllegalArgumentException.");
            }

            try {
                _peer.update(item, null);
                fail("Null Connection in update method should throw IllegalArgumentException.");
            } catch (IllegalArgumentException ok) {
            } catch (Exception e) {
                fail("Null Connection in update method should throw IllegalArgumentException.");
            }

            item.setTitle("New discussion item title");
            _peer.update(item, conn);

            rset = stmt.executeQuery("select discussion_item_id from discussion_item where discussion_item_id='" + item.getId() + "' and title = '" + "New discussion item title" + "'");
            assertTrue("Expecting record in resultset", rset.next());

        } finally {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        }

    }

    public void testDeleteByDiscussionId() throws Exception {
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                _peer.deleteByDiscussionId(FAKE_ID, conn);
                return null;
            }
        }.execute();
    }

    public void testFindByDiscussion() throws Exception {
        final InstallationContext context = getContext();
        new JdbcQuery(this) {
            private boolean bothNull(Object o1, Object o2) {
                return ((o1 == null) && (o2 == null));
            }

            private String compareDI(DiscussionItem di1, DiscussionItem di2) {
                String failItems = "";
                if (!bothNull(di1.getId(), di2.getId()))
                    if (di1.getId() != di2.getId())
                        failItems.concat("Id, ");
                if (!bothNull(di1.getParentDiscussionItem(), di2.getParentDiscussionItem()))
                    if (di1.getParentDiscussionItem().getId() != di2.getParentDiscussionItem().getId())
                        failItems.concat("parentId, ");
                if (!bothNull(di1.getTitle(), di2.getTitle()))
                    if (!di1.getTitle().equals(di2.getTitle()))
                        failItems.concat("title, ");
                if (!bothNull(di1.getText(), di2.getText()))
                    if (!di1.getText().equals(di2.getText()))
                        failItems.concat("text, ");
                if (!bothNull(di1.getIsOnAgenda(), di2.getIsOnAgenda()))
                    if (di1.getIsOnAgenda() != di2.getIsOnAgenda())
                        failItems.concat("isOnAgenda, ");
                if (!bothNull(di1.getPriority(), di2.getPriority()))
                    if (di1.getPriority() != di2.getPriority())
                        failItems.concat("priority, ");
                if (!bothNull(di1.getKeyword(), di2.getKeyword()))
                    if (!di1.getKeyword().equals(di2.getKeyword()))
                        failItems.concat("keyword, ");
                if (!bothNull(di1.getAuthorId(), di2.getAuthorId()))
                    if (di1.getAuthorId() != di2.getAuthorId())
                        failItems.concat("authorId, ");
                if (!bothNull(di1.getCreateDate(), di2.getCreateDate()))
                    if (di1.getCreateDate().toString().equals(di2.getCreateDate().toString()))
                        failItems.concat("createDate,");
                if (!bothNull(di1.getClosedDate(), di2.getClosedDate()))
                    if (!di1.getClosedDate().toString().equals(di2.getClosedDate().toString()))
                        failItems.concat("closedDate,");
                if (!bothNull(di1.getIssueDate(), di2.getIssueDate()))
                    if (!di1.getIssueDate().toString().equals(di2.getIssueDate().toString()))
                        failItems.concat("issueDate,");
                if (!bothNull(di1.getClosedById(), di2.getClosedById()))
                    if (di1.getClosedById() != di2.getClosedById())
                        failItems.concat("closedById,");
                if (!bothNull(di1.getIssueCreatorId(), di2.getIssueCreatorId()))
                    if (di1.getIssueCreatorId() != di2.getIssueCreatorId())
                        failItems.concat("issueCreatorId,");
                if (!bothNull(di1.getIssueAction(), di2.getIssueAction()))
                    if (!di1.getIssueAction().equals(di2.getIssueAction()))
                        failItems.concat("issueAction,");
                return failItems;
            }

            protected Object query(Connection conn) throws SQLException {
                Map res = null;
                try {
                    _peer.findByDiscussion(null, conn);
                    fail("Null discussions in FindByDiscussion method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null discussions in FindByDiscussion method should throw IllegalArgumentException.");
                }
                try {
                    _peer.findByDiscussion(WorkUtil.getNoProject(context), null);
                    fail("Null connection in FindByDiscussion method should throw IllegalArgumentException.");
                } catch (IllegalArgumentException ok) {
                } catch (Exception e) {
                    fail("Null connection in FindByDiscussion method should throw IllegalArgumentException.");
                }

                Work disc1 = Work.createNew(Work.TYPE, "discussionPeer_desc1", User.getNobody(context));
                disc1.generateAlerts(false);
                DiscussionItem di1 = null;
                DiscussionItem di2 = null;
                DiscussionItem di3 = null;
                try {
                    disc1.save(conn);
                    conn.commit();

                    res = _peer.findByDiscussion(disc1, conn);
                    assertNotNull("Expecting empty items Set, get null.", res);
                    assertTrue("Expecting empty items Set. Got " + res.size(), res.isEmpty());

                    di1 = DiscussionItem.createNew((DiscussionItemType) DiscussionItemType.getTypeByName("DiscussionItem"),
                            disc1, null, User.getNobody(context), "DiscussionTest_title11", "DiscussionTest_text11");
                    di1.setKeyword("DiscussionTest_key11");
                    di1.save(conn);
                    conn.commit();
                    di2 = DiscussionItem.createNew((DiscussionItemType) DiscussionItemType.getTypeByName("DiscussionItem"),
                            disc1, di1, User.getNobody(context), "DiscussionTest_title12", "DiscussionTest_text12");
                    di2.setKeyword("DiscussionTest_key12");
                    di2.save(conn);
                    conn.commit();
                    di3 = DiscussionItem.createNew((DiscussionItemType) DiscussionItemType.getTypeByName("DiscussionItem"),
                            disc1, null, User.getNobody(context), "DiscussionTest_title13", "DiscussionTest_text13");
                    di3.setKeyword("DiscussionTest_key13");
                    di3.save(conn);
                    conn.commit();

                    res = _peer.findByDiscussion(disc1, conn);
                    assertNotNull("Expecting not empty items Set, get null.", res);
                    assertTrue("Expecting another items count in result Set. Got " + res.size(), res.size() == 3);
                    assertTrue("Expecting another items keys in result Set.",
                            res.containsKey(di1.getId()) &&
                            res.containsKey(di2.getId()) &&
                            res.containsKey(di3.getId()));
                    String compRes1 = compareDI(di1, (DiscussionItem) res.get(di1.getId()));
                    String compRes2 = compareDI(di2, (DiscussionItem) res.get(di2.getId()));
                    String compRes3 = compareDI(di3, (DiscussionItem) res.get(di3.getId()));
                    assertTrue("Expecting another items in result Set. Got fails: " + " di1 : " + compRes1
                            + "; di2 : " + compRes2 + "; di3 : " + compRes3,
                            (compRes1.length() == 0) &&
                            (compRes2.length() == 0) &&
                            (compRes3.length() == 0));

                    di3.delete(conn);
                    conn.commit();

                    res = _peer.findByDiscussion(disc1, conn);
                    assertNotNull("Expecting not empty items Set, get null.", res);
                    assertTrue("Expecting another items count in result Set. Got " + res.size(), res.size() == 2);
                    assertTrue("Expecting another items keys in result Set.",
                            res.containsKey(di1.getId()) &&
                            res.containsKey(di2.getId()));
                    compRes1 = compareDI(di1, (DiscussionItem) res.get(di1.getId()));
                    compRes2 = compareDI(di2, (DiscussionItem) res.get(di2.getId()));
                    assertTrue("Expecting another items in result Set. Got fails: " + " di1 : " + compRes1
                            + "; di2 : " + compRes2,
                            (compRes1.length() == 0) &&
                            (compRes2.length() == 0));

                    _peer.deleteByDiscussionId(disc1.getId(), conn);
                    res = _peer.findByDiscussion(disc1, conn);
                    assertNotNull("Expecting empty items Set, get null.", res);
                    assertTrue("Expecting empty items Set. Got " + res.size(), res.isEmpty());
                } finally {
                    try {
                        delete(di2, conn);
                        delete(di1, conn);
                        if (di3 != null && !di3.isDeleted()) di3.delete(conn);
                        conn.commit();
                        delete(disc1, conn);
                        disc1.deleteHard(conn);
                        conn.commit();
                    } catch (SQLException ignored) {
                        if (DEBUG) ignored.printStackTrace();
                    }
                }
                return null;
            }
        }.execute();
    }

    public void testTextDatesSearch() throws Exception {
        if (!getContext().isFullTextInstall()) {
            System.out.println("TestDiscussionItemPeer::testTextDatesSearch>> Warning! Full text search engine is not installed. Exit.");
            return;
        }
        ;
        final Timestamp sd = new Timestamp(Calendar.getInstance().getTime().getTime());
        new JdbcQuery(this) {
            protected Object query(Connection conn) throws SQLException {
                List words = new Vector();
                words.add("empty");
                words.add("list");
                //_peer.textDatesSearch(1, words, sd, sd, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);
                //_peer.textDatesSearch(2, words, sd, sd, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);
                //return _peer.textDatesSearch(3, words, sd, sd, true, Arrays.asList(new Object[]{DiscussionItem.LESSONS_LEARNED_TYPE}), conn);
                return null;
            }
        }.execute();
    }

    private final void delete(JdbcPersistableBase o, Connection conn) {
        try {
            if (o != null) {
                //if (o instanceof PSObject)
                //    ((PSObject) o).setModifiedById(User.getNobody(getContext()).getId());
                o.deleteHard(conn);
                conn.commit();
            }
        } catch (Exception ignored) {
            if (DEBUG) ignored.printStackTrace();
        }
    }
}
