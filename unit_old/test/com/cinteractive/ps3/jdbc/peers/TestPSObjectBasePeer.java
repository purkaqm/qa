/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cinteractive.database.EmptyResultSetException;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.RestoreMap;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.ps3.InstanceInfo;
import com.cinteractive.ps3.MagicObjectCode;
import com.cinteractive.ps3.ObjectAttribute;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.discussion.Discussion;
import com.cinteractive.ps3.discussion.DiscussionItem;
import com.cinteractive.ps3.entities.Admins;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.tags.InvalidTagEventException;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagEventCode;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.test.MockObjectType;
import com.cinteractive.ps3.types.ObjectType;
import com.cinteractive.ps3.types.PSType;
import com.cinteractive.ps3.work.NoProject;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;


/**
 * Unit tests for PSObjectBasePeer public API
 * @author Yoritaka Sakakura
 */
public class TestPSObjectBasePeer extends TestJdbcPeer {
	private PSObjectBasePeer _peer;

	private static final boolean DEBUG = false; // show debug information

	static {
		registerCase();
	}

	private static void registerCase() {
		TestSql.registerTestCase( PSObjectBasePeer.class.getName(), TestPSObjectBasePeer.class.getName() );
	}

	public TestPSObjectBasePeer( String name ) {
		super( name );
	}


	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();

		suite.addTest( new TestPSObjectBasePeer( "testFindContextIdsByName" ) );
		suite.addTest( new TestPSObjectBasePeer( "testFindIdsByNameAndType" ) );
		suite.addTest( new TestPSObjectBasePeer( "testFindIdsByPeerIdAndType" ) );
		suite.addTest( new TestPSObjectBasePeer( "testFindIdsByType" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGenericSearch" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetInstanceInfo" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetIssueIds" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetMagicObjectInfo" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetObjectIdsByTagId" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetRestoreData" ) );
		suite.addTest( new TestPSObjectBasePeer( "testInsertUpdateDelete" ) );
		suite.addTest( new TestPSObjectBasePeer( "testFindIdsByAttribute" ) );
		suite.addTest( new TestPSObjectBasePeer( "testTextSearch" ) );
		suite.addTest( new TestPSObjectBasePeer( "testGetPreFetchedIds" ) );
		return suite;
	}

	public static Test suite() {
		return setUpDb( bareSuite() );
	}


	public void setUp()
	        throws Exception {
		super.setUp();

		_peer = (PSObjectBasePeer) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return PSObjectBasePeer.getInstance( conn );
			}
		}.execute();
		if ( _peer == null )
			throw new NullPointerException( "Null PSObjectBasePeer instance." );
	}

	public void tearDown()
	        throws Exception {
		super.tearDown();
		_peer = null;
	}

	private final void delete( JdbcPersistableBase o, Connection conn ) {
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

	public void testFindContextIdsByName()
	        throws Exception {
		// Test contract
		List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.findContextIdsByName( null, conn );
					fail( "Null name should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				try {
					_peer.findContextIdsByName( FAKE_CONTEXT_NAME, null );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.findContextIdsByName( FAKE_CONTEXT_NAME, conn );
			}
		}.execute();
		assertNotNull( "Null context ids.", ids );
		assertTrue( "Expecting empty collection; fake context name '" + FAKE_CONTEXT_NAME + "' appears to exist.", ids.isEmpty() );
		// Some real results
		final InstallationContext context = getContext();
		ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findContextIdsByName( context.getName(), conn );
			}
		}.execute();
		assertTrue( "Expecting single element collection; got " + ids.size() + " elements.", ids.size() == 1 );
		assertTrue( "Collection contained unexpected context id; expected '" + context.getId() + "'.", ids.contains( context.getId() ) );
	}

	public void testFindIdsByNameAndType()
	        throws Exception {
		final ObjectType type = new MockObjectType( "InstallationContext" );
		// test contract
		final InstallationContext context = getContext();
		Set ids = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.findIdsByNameAndType( null, type, context.getId(), null, conn );
					fail( "Null name should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByNameAndType( context.getName(), (PSType) null, context.getId(), null, conn );
					fail( "Null type should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByNameAndType( context.getName(), type, null, null, conn );
					fail( "Null context id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByNameAndType( context.getName(), type, context.getId(), null, null );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.findIdsByNameAndType( FAKE_CONTEXT_NAME, type, context.getId(), null, conn );
			}
		}.execute();
		assertNotNull( "Null ids.", ids );
		assertTrue( "Expecting empty collection; fake context name '" + FAKE_CONTEXT_NAME + "' appears to exist.", ids.isEmpty() );
		// there should be no archived contexts
		ids = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findIdsByNameAndType( context.getName(), type, context.getId(), Boolean.TRUE, conn );
			}
		}.execute();
		assertTrue( "Archived parameter doesn't seem to be working.", ids.isEmpty() );
		// real data
		ids = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findIdsByNameAndType( context.getName(), type, context.getId(), null, conn );
			}
		}.execute();
		assertTrue( "Ids should contain exactly one element.", ids.size() == 1 );
		assertTrue( "Ids should only contain the context id.", ids.contains( context.getId() ) );
	}

	public void testFindIdsByPeerIdAndType()
	        throws Exception {
		// test contract
		final ObjectType type = new MockObjectType( "InstallationContext" );
		// test contract
		final InstallationContext context = getContext();
		Set ids = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.findIdsByPeerIdAndType( null, null, context.getId(), conn );
					fail( "Null peer id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByPeerIdAndType( "", null, null, conn );
					fail( "Null context id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByPeerIdAndType( "", null, context.getId(), null );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.findIdsByPeerIdAndType( "", null, FAKE_ID, conn );
			}
		}.execute();
		assertNotNull( "Null ids.", ids );
		assertTrue( "Expecting empty collection.", ids.isEmpty() );
		// without type should contain with type if any
		ids = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findIdsByPeerIdAndType( "", null, FAKE_ID, conn );
			}
		}.execute();
		final Set subset = (Set) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findIdsByPeerIdAndType( "", type, FAKE_ID, conn );
			}
		}.execute();
		assertTrue( "Type filter seems broken.", ids.containsAll( subset ) );
	}

	public void testFindIdsByType()
	        throws Exception {
		List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.findIdsByType( null, null, FAKE_ID, conn );
					fail( "Null PSType should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByType( CONTEXT_TYPE, null, null, conn );
					fail( "Null context id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.findIdsByType( CONTEXT_TYPE, null, FAKE_ID, null );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.findIdsByType( CONTEXT_TYPE, null, FAKE_ID, conn );
			}
		}.execute();
		assertNotNull( "Null ids.", ids );
		assertTrue( "Expecting empty ids for fake context id; test may be broken.", ids.isEmpty() );

		final InstallationContext context = getContext();
		ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.findIdsByType( CONTEXT_TYPE, null, context.getId(), conn );
			}
		}.execute();
		assertTrue( "Expecting single id '" + context.getId() + "'; got " + ids.size() + " ids.", ids.size() == 1 && ids.contains( context.getId() ) );
	}

	public void testGenericSearch()
	        throws Exception {
		final Set types = new java.util.HashSet( 1 );
		types.add( CONTEXT_TYPE );

		final InstallationContext context = getContext();
		List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.genericSearch( null, types, context.getId(), conn );
					fail( "Null search text should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.genericSearch( "", null, context.getId(), conn );
					fail( "Null type list should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.genericSearch( "", null, context.getId(), conn );
					fail( "Null type list should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.genericSearch( "", Collections.EMPTY_SET, context.getId(), conn );
					fail( "Empty type list should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.genericSearch( "", types, null, conn );
					fail( "Null context id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.genericSearch( "", types, context.getId(), null );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.genericSearch( "", types, FAKE_ID, conn );
			}
		}.execute();
		assertNotNull( "Null ids.", ids );
		assertTrue( "Expecting empty id strings; test may be broken.", ids.isEmpty() );

		ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.genericSearch( context.getName(), types, context.getId(), conn );
			}
		}.execute();
		assertTrue( "Expecting id strings to contain only '" + context.getId() + "'; got '" + ids + "'.", ids.size() == 1 && ids.contains( context.getId() ) );
	}

	public void testGetInstanceInfo()
	throws Exception
	{
		try {
			new JdbcQuery( this ) {
				protected Object query( Connection conn )
				        throws SQLException {
					return _peer.getInstanceInfo( FAKE_ID, conn );
				}
			}.execute();
			fail( "Expecting RuntimeSQLException wrapping EmptyResultSetException for fake id '" + FAKE_ID + "'." );
		} catch ( RuntimeSQLException e ) {
			if ( !(e.getRootCause() instanceof EmptyResultSetException) )
				throw e;
		}

		final InstallationContext context = getContext();
		final InstanceInfo info = (InstanceInfo) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.getInstanceInfo( context.getId(), conn );
			}
		}.execute();
		assertNotNull( "Should throw EmptyResultSetException, not return null.", info );
		assertEquals( "Context (object) ids don't match.", info.getObjectId(), context.getId() );
		assertEquals( "Types don't match.", info.getType(context), CONTEXT_TYPE.toString() );
		assertTrue( "Expecting null magic object code.", info.getMagicObjectCode() == null );
	}

	public void testGetIssueIds()
	        throws Exception {
		final List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.getIssueIds( null, FAKE_ID );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.getIssueIds( conn, null );
					fail( "Null discussion id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.getIssueIds( conn, FAKE_ID );
			}
		}.execute();
		assertNotNull( "Null issue ids.", ids );
		assertTrue( "Expecting empty issue ids; test may be broken.", ids.isEmpty() );

		final InstallationContext context = getContext();
		final User user = Nobody.get(context);
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				conn.setAutoCommit( false );
				try {
					final Discussion disc = Work.createNew( Work.TYPE, "___TEST_GETISSUEIDS___", user );
					((PSObject) disc).save( conn );
					final DiscussionItem item1 = DiscussionItem.createNew( disc, null, user, "discussion item title", "no text there" );
					final DiscussionItem item2 = DiscussionItem.createNew( disc, null, user, "discussion item title", "no text there" );
					item1.save( conn );
					List __ids = _peer.getIssueIds( conn, disc.getId() );
					assertTrue( "Expecting empty list", __ids.isEmpty() );
					item2.setPriority( new Integer( 1 ), user );
					item2.save( conn );
					__ids = _peer.getIssueIds( conn, disc.getId() );
					assertTrue( "Expecting 1 valid issue id", __ids.size() == 1 && item2.getId().equals( __ids.get( 0 ) ) );
					final DiscussionItem item3 = DiscussionItem.createNew( disc, null, user, "discussion item title", "no text there" );
					item3.setPriority( new Integer( 2 ), user );
					item3.save( conn );
					__ids = _peer.getIssueIds( conn, disc.getId() );
					assertTrue( "Expecting 2 issue id; got " + __ids.size(), __ids.size() == 2 );
				} finally {
					conn.rollback();
					conn.setAutoCommit( true );
				}

				return null;
			}
		}.execute();
	}

	public void testGetMetricIds()
	        throws Exception {
		final List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				try {
					_peer.getMetricIds( null, FAKE_ID );
					fail( "Null Connection should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}
				try {
					_peer.getMetricIds( conn, null );
					fail( "Null discussion id should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				}

				return _peer.getIssueIds( conn, FAKE_ID );
			}
		}.execute();
		assertNotNull( "Null metric ids.", ids );
		assertTrue( "Expecting empty metric ids; test may be broken.", ids.isEmpty() );
/*
					final InstallationContext context = getContext();
					final User user = (User) User.getNobody(context);
					new JdbcQuery( this ) {
						protected Object query(Connection conn) throws SQLException {
							conn.setAutoCommit(false);
							try {
								final Discussion disc = (Discussion) Work.createNew(Work.TYPE, "___TEST_GETISSUEIDS___", user);
								((PSObject)disc).save(conn);
								final DiscussionItem item1 = DiscussionItem.createNew(disc, null, user, "discussion item title", "no text there");
								final DiscussionItem item2 = DiscussionItem.createNew(disc, null, user, "discussion item title", "no text there");
								item1.save(conn);
								List __ids = _peer.getIssueIds(conn, disc.getId());
								assertTrue("Expecting empty list", __ids.isEmpty());
								item2.setPriority(new Integer(1), user);
								item2.save(conn);
								__ids = _peer.getIssueIds(conn, disc.getId());
								assertTrue("Expecting 1 valid issue id", __ids.size() == 1 && item2.getId().equals(__ids.get(0)));
								final DiscussionItem item3 = DiscussionItem.createNew(disc, null, user, "discussion item title", "no text there");
								item3.setPriority(new Integer(2), user);
								item3.save(conn);
								__ids = _peer.getIssueIds(conn, disc.getId());
								assertTrue("Expecting 2 issue id; got " + __ids.size(), __ids.size() == 2);
							} finally {
								conn.rollback();
								conn.setAutoCommit(true);
							}

							return null;
						}
					}.execute();
*/
	}

	public void testGetMagicObjectInfo()
	        throws Exception {
		final InstallationContext context = getContext();
		final InstanceInfo instInfo = (InstanceInfo) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.getMagicObjectInfo( FAKE_MAGIC_CODE, context.getId(), conn );
			}
		}.execute();
		assertTrue( "Expecting null InstanceInfo for fake magic object code '" + FAKE_MAGIC_CODE + "'.", instInfo == null );
		// for example, noproject and administrators group
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				final Work work = WorkUtil.getNoProject( context );
				InstanceInfo inf = _peer.getMagicObjectInfo( MagicObjectCode.NO_PROJECT, FAKE_ID, conn );
				assertNull( "Expecting null InstanceInfo for fake context id", inf );

				inf = _peer.getMagicObjectInfo( MagicObjectCode.NO_PROJECT, context.getId(), conn );
				assertNotNull( "Expecting not null InstanceInfo; got null", inf );
				assertTrue( "Expecting NoProject java class name; got " + inf.getJavaClassName(context), NoProject.class.getName().equals( inf.getJavaClassName(context) ) );
				assertTrue( "Expecting valid magic object code", MagicObjectCode.NO_PROJECT.equals( inf.getMagicObjectCode() ) );
				assertTrue( "Expecting valid object id", work.getId().equals( inf.getObjectId() ) );
				assertTrue( "Expecting valid PSType", work.getPSType().equals( inf.getType(context) ) );

				Group admins = Admins.get(context);
				inf = _peer.getMagicObjectInfo( MagicObjectCode.ADMINS, FAKE_ID, conn );
				assertNull( "Expecting null InstanceInfo for fake context id", inf );
				inf = _peer.getMagicObjectInfo( MagicObjectCode.ADMINS, context.getId(), conn );
				assertNotNull( "Expecting not null InstanceInfo; got null", inf );
				assertTrue( "Expecting Admins java class name; got " + inf.getJavaClassName(context), Admins.class.getName().equals( inf.getJavaClassName(context) ) );
				assertTrue( "Expecting valid magic object code", MagicObjectCode.ADMINS.equals( inf.getMagicObjectCode() ) );
				assertTrue( "Expecting valid object id", admins.getId().equals( inf.getObjectId() ) );
				assertTrue( "Expecting valid PSType", admins.getPSType().equals( inf.getType(context) ) );
				return null;
			}
		}.execute();
	}

	public void testGetObjectIdsByTagId()
	        throws Exception {
		final InstallationContext context = getContext();
		List ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.getObjectIdsByTagId( FAKE_ID, Arrays.asList( new ObjectType[]{CONTEXT_TYPE} ), context.getId(), conn );
			}
		}.execute();
		assertNotNull( "Expecting empty collection for no results but got null.", ids );
		assertTrue( "Expecting empty ids for fake tag id and conytext type; test may be busted.", ids.isEmpty() );

		ids = (List) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.getObjectIdsByTagId( null, null, context.getId(), conn );
			}
		}.execute();
		assertNotNull( "Expecting not empty list; got null", ids );
		assertTrue( "Expecting not empty list", !ids.isEmpty() );

		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				TagSet set = null;
				User user = null;
				try {
					user = User.createNewUser( "_TestPSObjectBasePeer_getObjectTagsById_", context );
					user.save( conn );
					conn.commit();

					set = TagSet.createNew( "__TAGSET_NAME__", context );
					set.addLinkableType( User.TYPE );
					final PSTag tag = set.addTag( "The tag" );
					final PSTag testTag = set.addTag( "The test tag" );
					set.save( conn );
					conn.commit();

					user.changeTag( TagEventCode.ADD_TAG_CODE, tag, null );
					user.save( conn );
					// one user expecting
					List __ids = _peer.getObjectIdsByTagId( tag.getId(), Arrays.asList( new Object[]{User.TYPE} ), context.getId(), conn );
					assertNotNull( "Expecting not empty list for valid data; got null", __ids );
					assertTrue( "Expecting only one valid user id", __ids.size() == 1 && user.getId().toString().equals( __ids.get( 0 ) ) );
					// no users expecting
					__ids = _peer.getObjectIdsByTagId( testTag.getId(), Arrays.asList( new Object[]{User.TYPE} ), context.getId(), conn );
					assertNotNull( "Expecting empty list for non-linked tag; got null", __ids );
					assertTrue( "Expecting empty list for non-linked tag", __ids.isEmpty() );
				} catch ( InvalidTagEventException e ) {
					assertTrue( "InvalidTagEventException occured", false );
				} finally {
					delete( set, conn );
					delete( user, conn );
				}
				return null; //method does not returns any value
			}
		}.execute();
	}

	public void testGetRestoreData()
	        throws Exception {
		try {
			new JdbcQuery( this ) {
				protected Object query( Connection conn )
				        throws SQLException {
					return _peer.getRestoreData( FAKE_ID, conn );
				}
			}.execute();
			fail( "Should throw RuntimeSQLException wrapping EmptyResultSetException for fake id '" + FAKE_ID + "'." );
		} catch ( RuntimeSQLException e ) {
			if ( !(e.getRootCause() instanceof EmptyResultSetException) )
				throw e;
		}

		final InstallationContext context = getContext();
		final Map data = (Map) new JdbcQuery( this ) {
			protected Object query( Connection conn )
			        throws SQLException {
				return _peer.getRestoreData( context.getId(), conn );
			}
		}.execute();
		assertNotNull( "Null restore data.", data );
		assertTrue( "Empty restore data.", !data.isEmpty() );
	}

	public void testInsertUpdateDelete() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				final User user = Nobody.get(context);
				final Work work = Work.createNew( Work.TYPE, "___TEST_WORK___", user );
				work.setIsActive(true);
				work.getCreateDate().setNanos( 0 ); // SQL server does not storages nanos
				work.getLastChangeDate().setNanos( 0 ); // SQL server does not storages nanos

				conn.setAutoCommit( false );

				try {
					work.setDescription( "the work description" );
					_peer.insert( work, conn );
					RestoreMap data = _peer.getRestoreData( work.getId(), conn );
					assertTrue( "Object has not been inserted properly", compare( work, data ) );
					work.setName( "___TEST_WORK_NEW_NAME___" );
					work.setIsActive(false);
					work.setPeerId( "__PEERID__" );
					work.setDescription( "the updated work description" );
					_peer.update( work, conn );
					data = _peer.getRestoreData( work.getId(), conn );
					assertTrue( "Object has not been updated properly", compare( work, data ) );
					_peer.delete( work, conn );
					try {
						_peer.getRestoreData( work.getId(), conn );
						assertTrue( "Object has not been deleted properly", false );
					} catch ( EmptyResultSetException ok ) {
					}
				} finally {
					conn.rollback();
					conn.setAutoCommit( true );
				}
				return null;
			}
		}.execute();
	}

	private boolean compare( PSObject o, RestoreMap data ) {
		return
		        o.getContext().getId().equals( data.get( PSObject.CONTEXT_ID ) ) &&
		        o.getIsActive().equals( data.get( PSObject.IS_ACTIVE ) ) &&
		        (o.getPeerId() == null ? data.get( PSObject.PEER_ID ) == null : o.getPeerId().equals( data.get( PSObject.PEER_ID ) )) &&
		        o.getName().equals( data.get( PSObject.NAME ) ) &&
		        o.getCreatedBy().getId().equals( data.get( PSObject.CREATED_BY_ID ) ) &&
		        equals( o.getCreateDate(), data.get( PSObject.CREATE_DATE ) ) &&
		        equals( o.getExpirationDate(), data.get( PSObject.EXPIRATION_DATE ) ) &&
		        o.getPSType().equals( data.get( PSObject.OBJECT_TYPE ) ) &&
		        equals( o.getMagicObjectCode(), data.get( PSObject.MAGIC_OBJECT_CODE ) ) &&
//		        equals( o.getCheckoutDate(), data.get( PSObject.CHECKOUT_DATE ) ) &&
//		        equalsId( o.getCheckoutBy(), data.get( PSObject.CHECKOUT_BY_ID ) ) &&
		        equals( o.getLastChangeDate(), data.get( PSObject.LAST_CHANGE_DATE ) ) &&
		        o.getDescription().equals( data.get( PSObject.DESCRIPTION ) );
	}

	private boolean equals( Object o1, Object o2 ) {
		return (o1 == null && o2 == null) || (o1 != null && o1.equals( o2 ));
	}

	private boolean equals( Timestamp o1, Object o2 ) {
		return (o1 == null && o2 == null) || (o1 != null && o1.equals( o2 ));
	}

	public void testFindIdsByAttribute() throws Exception {
		//empty case
		Set ids = (Set)
		        new JdbcQuery( this ) {
			        protected Object query( Connection conn ) throws SQLException {
				        return _peer.findIdsByAttribute( "fake_attr_name", new MockObjectType( "fake_object_type" ), FAKE_ID, conn );
			        }
		        }.execute();
		assertNotNull( "Expecting empty set for fake context id; got null", ids );
		assertTrue( "Expecting empty set for fake context id", ids.isEmpty() );

		//some data
		final InstallationContext context = getContext();
		ids = (Set)
		        new JdbcQuery( this ) {
			        protected Object query( Connection conn ) throws SQLException {
				        conn.setAutoCommit( false );
				        try {
					        final Work noProject = WorkUtil.getNoProject( context );
					        noProject.setStringAttribute( "fake_attribute", "no value" );
					        ObjectAttribute attr = null;
					        for ( Enumeration i = noProject.attributes(); i.hasMoreElements(); ) {
						        final ObjectAttribute a = (ObjectAttribute) i.nextElement();
						        if ( a.getAttributeName().equals( "fake_attribute" ) ) {
							        attr = a;
							        break;
						        }
					        }
					        ObjectAttributePeer.getInstance( conn ).insert( attr, conn );
					        return _peer.findIdsByAttribute( "fake_attribute", Work.TYPE, context.getId(), conn );
				        } finally {
					        conn.rollback();
					        conn.setAutoCommit( true );
				        }
			        }
		        }.execute();
		assertTrue( "Expecting one id; got " + ids.size(), ids.size() == 1 );
	}

	public void testTextSearch() throws Exception {
		if ( !getContext().isFullTextInstall() ) {
			System.out.println( "TestPSObjectBasePeer::testTextSearch>> Warning! Full text search engine is not installed. Exit." );
			return;
		}

		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				final List phrases = new LinkedList();
				phrases.add( "FirstPhrase" );
				phrases.add( "SecondPhrase" );
				HashSet set = new HashSet();
				set.add( Work.TYPE );
				set.add( Nobody.TYPE );
				List ids = _peer.textSearch( 1, phrases, set, FAKE_ID, conn );
				assertNotNull( "Expecting empty result set for fake context id; got null", ids );
				assertTrue( "Expecting empty result set for fake context id", ids.isEmpty() );

				ids = _peer.textSearch( 2, phrases, set, FAKE_ID, conn );
				assertNotNull( "Expecting empty result set for fake context id; got null", ids );
				assertTrue( "Expecting empty result set for fake context id", ids.isEmpty() );

				ids = _peer.textSearch( 3, phrases, set, FAKE_ID, conn );
				assertNotNull( "Expecting empty result set for fake context id; got null", ids );
				assertTrue( "Expecting empty result set for fake context id", ids.isEmpty() );

				return null;// this method does not return any value
			}
		}.execute();

	}

	// gags
	public void testGetInstance() {
	}

	public void testInsert() {
	}

	public void testUpdate() {
	}

	public void testDelete() {
	}
}
