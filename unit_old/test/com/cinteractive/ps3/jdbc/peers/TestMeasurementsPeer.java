package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.calc.Value;
import com.cinteractive.calc.parse.SimpleValue;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.metrics.Measurement;
import com.cinteractive.ps3.metrics.MeasurementItem;
import com.cinteractive.ps3.metrics.Measurements;
import com.cinteractive.ps3.metrics.MetricInstance;
import com.cinteractive.ps3.metrics.MetricLineItem;
import com.cinteractive.ps3.metrics.MetricTemplate;
import com.cinteractive.ps3.metrics.MetricTemplateItem;
import com.cinteractive.ps3.metrics.Phase;
import com.cinteractive.ps3.metrics.View;
import com.cinteractive.ps3.tags.InvalidTagEventException;
import com.cinteractive.ps3.tags.PSTag;
import com.cinteractive.ps3.tags.TagEventCode;
import com.cinteractive.ps3.tags.TagSet;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

public class TestMeasurementsPeer extends TestJdbcPeer {
	private MeasurementsPeer _peer;

	static {
		registerCase( );
	}

	private static void registerCase( ) {
		TestSql.registerTestCase( MeasurementsPeer.class.getName(), TestMeasurementsPeer.class.getName() );
	}

	public TestMeasurementsPeer( String name ) {
		super( name );
	}

	private static void addTest( TestSuite suite, String testName ) {
		suite.addTest( new TestMeasurementsPeer( testName ) );
	}

	public static Test bareSuite() {
		final TestSuite suite = new TestSuite();
		addTest( suite, "testClearView" );
		addTest( suite, "testCountByInstanceId" );
		addTest( suite, "testCountByItemId" );
		addTest( suite, "testCountByTemplateId" );
		addTest( suite, "testCounts" );
		addTest( suite, "testDeleteByInstanceId" );
		addTest( suite, "testDeleteByItemId" );
		addTest( suite, "testDeleteByTemplate" );
		addTest( suite, "testDeleteByView" );
		addTest( suite, "testDeleteByViewTag" );
		addTest( suite, "testFindByInstanceDatesView" );
		addTest( suite, "testFindInstances" );
		addTest( suite, "testUpdateViewTag" );
		addTest( suite, "testFindByInstanceId" );
		return suite;
	}

	public void testGetInstance() throws Exception {
	}

	public void testInsert() throws Exception { /** see testInsertUpdateDelete */
	}

	public void testUpdate() throws Exception { /** see testInsertUpdateDelete */
	}

	public void testDelete() throws Exception { /** see testInsertUpdateDelete */
	}

	public void setUp() throws Exception {
		super.setUp();

		_peer = (MeasurementsPeer) new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				return MeasurementsPeer.getInstance( conn );
			}
		}.execute();

		if ( _peer == null )
			throw new NullPointerException( "Null MeasurementsPeer instance." );
	}

	public static Test suite() {
		return setUpDb( bareSuite() );
	}

	public void tearDown() throws Exception {
		super.tearDown();
		_peer = null;
	}

	private static final boolean DEBUG = true; //show debug info

	private final void delete( JdbcPersistableBase o, Connection conn ) {
		try {
			if (o != null) {
               // if (o instanceof PSObject)
               //     ((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
	}

	private static Timestamp monthBefore = null;
	private static Timestamp monthLater = null;
	private static Timestamp twoMonthLater = null;

	static {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime( new Date( System.currentTimeMillis() ) );
		gc.set( GregorianCalendar.MILLISECOND, 0 );
		monthBefore = new Timestamp( gc.getTime().getTime() - 31L * 24L * 60L * 60L * 1000L );
		monthLater = new Timestamp( gc.getTime().getTime() + 31L * 24L * 60L * 60L * 1000L );
		twoMonthLater = new Timestamp( gc.getTime().getTime() + 62L * 24L * 60L * 60L * 1000L );
	}

	public void testClearView() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.clearView( null, conn );
					fail( "Null template id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null template id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.clearView( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.clearView( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;
				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_testClearView_", Nobody.get(context));
					mt.setHasViews( true );
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti.getId(), null, ms1 );

					mti.save( conn );
					mt.save( conn );
					conn.commit();

					mi.save( conn );
					conn.commit();

					res = _peer.clearView( mt.getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.clearView( mt.getId(), conn );
					assertTrue( "Expecting 2 deleted items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting another instance.", res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testCounts() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				int res = 0;

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_tetsCounts_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti.setSequence( new Integer( 1 ) );


					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					msi1.setNumericValue( new SimpleValue( Float.valueOf( "1.1" ) ) );
					ms1.putMeasurementItem( monthLater, msi1 );
					mss1.putMeasurement( mti.getId(), null, ms1 );

					mti.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.countByInstanceId( mi.getId(), true, conn ).intValue();
					assertTrue( "Expecting 0 items. got: " + res, res == 0 );
					res = _peer.countByItemId( mti.getId(), true, conn ).intValue();
					assertTrue( "Expecting 0 item. got: " + res, res == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.countByInstanceId( mi.getId(), true, conn ).intValue();
					assertTrue( "Expecting 1 item. got: " + res, res == 1 );
					res = _peer.countByInstanceId( mi.getId(), false, conn ).intValue();
					assertTrue( "Expecting 1 item. got: " + res, res == 1 );

					res = _peer.countByItemId( mti.getId(), true, conn ).intValue();
					assertTrue( "Expecting 1 item. got: " + res, res == 1 );
					res = _peer.countByItemId( mti.getId(), false, conn ).intValue();
					assertTrue( "Expecting 1 item. got: " + res, res == 1 );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testCountByInstanceId() throws Exception {
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				int res = 0;

				try {
					_peer.countByInstanceId( null, true, conn );
					fail( "Null instance id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null instance id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.countByInstanceId( FAKE_ID, true, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.countByInstanceId( FAKE_ID, true, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );
				res = _peer.countByInstanceId( FAKE_ID, false, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );

				return null;
			}
		}.execute();
	}

	public void testCountByItemId() throws Exception {
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				int res = 0;

				try {
					_peer.countByItemId( null, true, conn );
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.countByItemId( FAKE_ID, true, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.countByItemId( FAKE_ID, true, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );
				res = _peer.countByItemId( FAKE_ID, false, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );

				return null;
			}
		}.execute();
	}

	public void testCountByTemplateId() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				int res = 0;

				try {
					_peer.countByTemplateId( null, true, conn );
					fail( "Null template id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null template id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.countByTemplateId( FAKE_ID, true, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.countByTemplateId( FAKE_ID, true, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );
				res = _peer.countByTemplateId( FAKE_ID, false, conn ).intValue();
				assertTrue( "Expecting 0 items. got: " + res, res == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_testCountByTemplateId_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );

					MetricTemplateItem mti2 = MetricTemplateItem.createNew( "ti2", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti2.setSequence( new Integer( 2 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );

					//Measurement ms1 = mss1.createMeasurement( mti1.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );

					//Measurement ms2 = mss1.createMeasurement( mti2.getId(), null );
					Measurement ms2 = null;
					MeasurementItem msi3 = MeasurementItem.createNew( ms2, monthLater, SimpleValue.instantiate(new Double(1.1)));
					ms2.putMeasurementItem( monthLater, msi3 );

					mss1.putMeasurement( mti1.getId(), null, ms1 );
					mss1.putMeasurement( mti2.getId(), null, ms2 );

					mti1.save( conn );
					mti2.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.countByTemplateId( mt.getId(), true, conn ).intValue();
					assertTrue( "Expecting 0 items. got: " + res, res == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.countByTemplateId( mt.getId(), true, conn ).intValue();
					assertTrue( "Expecting 3 items. got: " + res, res == 3 );
					res = _peer.countByTemplateId( mt.getId(), false, conn ).intValue();
					assertTrue( "Expecting 3 items. got: " + res, res == 3 );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testDeleteByInstanceId() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				int res = 0;

				try {
					_peer.deleteByInstanceId( null, conn );
					fail( "Null metric instance id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null metric instance id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.deleteByInstanceId( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.deleteByInstanceId( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res, res == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_deleteByInstanceId_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );


					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti1.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti1.getId(), null, ms1 );

					mti1.save( conn );
					mt.save( conn );
					conn.commit();

					mi.save( conn );
					conn.commit();

					res = _peer.deleteByInstanceId( mi.getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res, res == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.deleteByInstanceId( mi.getId(), conn );
					assertTrue( "Expecting 2 deleted items. got: " + res, res == 2 );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testDeleteByItemId() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.deleteByItemId( null, conn );
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.deleteByItemId( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.deleteByItemId( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_testDeleteByItemId_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti1.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti1.getId(), null, ms1 );

					mti1.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.deleteByItemId( mti1.getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.deleteByItemId( mti1.getId(), conn );
					assertTrue( "Expecting deleted items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting deleted items for another instance. got: ",
					            res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testDeleteByTemplate() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.deleteByTemplate( null, conn );
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null metric template item id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.deleteByTemplate( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.deleteByTemplate( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_deleteByTemplateId_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );

					MetricTemplateItem mti2 = MetricTemplateItem.createNew( "ti2", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 2 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					v1.addTag( "testMes_tag1" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );

					//Measurement ms1 = mss1.createMeasurement( mti1.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );

					//Measurement ms2 = mss1.createMeasurement( mti2.getId(), null );
					Measurement ms2 = null;
					MeasurementItem msi3 = MeasurementItem.createNew( ms2, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi4 = MeasurementItem.createNew( ms2, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms2.putMeasurementItem( monthLater, msi3 );
					ms2.putMeasurementItem( twoMonthLater, msi4 );

					mss1.putMeasurement( mti1.getId(), null, ms1 );
					mss1.putMeasurement( mti2.getId(), null, ms2 );

					mti1.save( conn );
					mti2.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.deleteByTemplate( mt.getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

					mss1.save( conn );
					conn.commit();

					res = _peer.deleteByTemplate( mt.getId(), conn );
					assertTrue( "Expecting 4 deleted items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting another result instance.", res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testDeleteByView() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.deleteByView( null, conn );
					fail( "Null view id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null view id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.deleteByView( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.deleteByView( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_deleteByView_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();

					//v1.setMaxTagCount(Integer.valueOf("3"));
					v1.addTag( "testMes_tag1" );
					v1.addTag( "testMes_tag2" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti.getId(), null, ms1 );

					Measurements mss2 = new Measurements( v1.getTag( "testMes_tag2" ).getId(), mi.getId() );
					//Measurement ms2 = mss2.createMeasurement( mti.getId(), null );
					Measurement ms2 = null;
					MeasurementItem msi3 = MeasurementItem.createNew( ms2, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi4 = MeasurementItem.createNew( ms2, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms2.putMeasurementItem( monthLater, msi3 );
					ms2.putMeasurementItem( twoMonthLater, msi4 );
					mss2.putMeasurement( mti.getId(), null, ms2 );

					mti.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );

					conn.commit();

					res = _peer.deleteByView( v1.getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

					mss1.save( conn );
					mss2.save( conn );
					conn.commit();

					res = _peer.deleteByView( v1.getId(), conn );
					assertTrue( "Expecting 4 deleted items fro 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting another instance.", res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testDeleteByViewTag() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.deleteByViewTag( null, conn );
					fail( "Null view tag id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null view tag id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.deleteByViewTag( FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.deleteByViewTag( FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_deleteByViewTag_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();

					//v1.setMaxTagCount(Integer.valueOf("3"));
					v1.addTag( "testMes_tag1" );
					v1.addTag( "testMes_tag2" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti.getId(), null, ms1 );

					Measurements mss2 = new Measurements( v1.getTag( "testMes_tag2" ).getId(), mi.getId() );
					//Measurement ms2 = mss2.createMeasurement( mti.getId(), null );
					Measurement ms2 = null;
					MeasurementItem msi3 = MeasurementItem.createNew( ms2, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi4 = MeasurementItem.createNew( ms2, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms2.putMeasurementItem( monthLater, msi3 );
					ms2.putMeasurementItem( twoMonthLater, msi4 );
					mss2.putMeasurement( mti.getId(), null, ms2 );

					mti.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.deleteByViewTag( v1.getTag( "testMes_tag1" ).getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );
					res = _peer.deleteByViewTag( v1.getTag( "testMes_tag2" ).getId(), conn );
					assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

					mss1.save( conn );
					mss2.save( conn );
					conn.commit();

					res = _peer.deleteByViewTag( v1.getTag( "testMes_tag1" ).getId(), conn );
					assertTrue( "Expecting 2 deleted items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting another instance.", res.contains( mi.getId() ) );
					res = _peer.deleteByViewTag( v1.getTag( "testMes_tag2" ).getId(), conn );
					assertTrue( "Expecting 2 deleted items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting another instance.", res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testFindInstances() throws Exception {
		final InstallationContext context = getContext();
		final User user = Nobody.get(context);
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				List res = null;

				try {
					_peer.findInstances( null, Boolean.TRUE, FAKE_ID, null, null, conn );
					fail( "Null template id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null template id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.findInstances( FAKE_ID, Boolean.TRUE, FAKE_ID, null, null, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.findInstances( FAKE_ID, Boolean.TRUE, FAKE_ID, null, null, conn );
				assertNotNull( "Expecting not null result.", res );
				assertTrue( "Expecting empty result set. got count: " + res.size(), res.isEmpty() );


				MetricTemplate mt = null;
				Work work1 = null;
				Work work2 = null;
				Phase ph1 = null;
				MetricInstance mi1 = null;
				MetricInstance mi2 = null;
				TagSet tSet1 = null;
				TagSet tSet2 = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_testFindInstances_", user );
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );

					mt.setHasPhases( true );

					ph1 = Phase.createNew( "testMes_phase1", mt );
					ph1.setMaxTagCount( new Integer( 3 ) );
					ph1.addTag( "testMes_ptag1" );
					ph1.addTag( "testMes_ptag2" );

					work2 = Work.createNew( Work.TYPE, "testMes_w2", user );
					work1 = Work.createNew( Work.TYPE, "testMes_w1", user );
					work1.setParentWork( work2, user );

					mi1 = MetricInstance.createNew( mt, work1, false );
					mi1.setIsReadyForRollup( true ,user );

					mi2 = MetricInstance.createNew( mt, work2, false );
					mi2.setIsReadyForRollup( true ,user);

					tSet1 = TagSet.createNew( "testMes_ts1", context );
					tSet1.addTag( "testMes_tstag11" );
					tSet1.addTag( "testMes_tstag12" );
					tSet1.addLinkableType( Work.TYPE );

					tSet2 = TagSet.createNew( "testMes_ts2", context );
					tSet2.addTag( "testMes_tstag21" );
					tSet2.addTag( "testMes_tstag22" );
					tSet2.addLinkableType( Work.TYPE );

					tSet1.save( conn );
					tSet2.save( conn );
					work2.save( conn );
					conn.commit();
					work1.save( conn );
					mti1.save( conn );
					conn.commit();
					mt.save( conn );
					conn.commit();
					mi1.save( conn );
					conn.commit();
					mi2.save( conn );
					ph1.save( conn );
					conn.commit();

					mi1.changeTag( TagEventCode.ADD_TAG_CODE, ph1.getTag( "testMes_ptag1" ), null );
					mi2.changeTag( TagEventCode.ADD_TAG_CODE, ph1.getTag( "testMes_ptag2" ), null );
					work1.changeTag( TagEventCode.ADD_TAG_CODE, tSet1.getTag( "testMes_tstag11" ), null );
					work2.changeTag( TagEventCode.ADD_TAG_CODE, tSet1.getTag( "testMes_tstag11" ), null );
					work1.changeTag( TagEventCode.ADD_TAG_CODE, tSet2.getTag( "testMes_tstag21" ), null);

					mi1.save( conn );
					mi2.save( conn );
					work1.save( conn );
					work2.save( conn );
					conn.commit();

					Map tagMap1 = new HashMap();
					Collection tagIds = new java.util.ArrayList();


					/*root work*/
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work1.getId(), null, null, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), null, null, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 2 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() )
					                                          && res.contains( mi2.getId() ) );
					/*work tags*/
					/*one tagSet*/
					tagMap1.put( tSet2.getId(), null );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), null, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );
					/*two tagSets*/
					tagMap1.put( tSet1.getId(), null );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), null, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );
					/*one tagSet with tag*/
					tagMap1.clear();
					tagMap1.put( tSet1.getId(), tSet1.getTag( "testMes_tstag11" ).getId() );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), null, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 2 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() )
					                                          && res.contains( mi2.getId() ) );
					/*two tagSets with tags*/
					tagMap1.put( tSet2.getId(), tSet2.getTag( "testMes_tstag21" ).getId() );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), null, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );
					tagMap1.clear();

					/*ready for rollup*/
					mi1.setIsReadyForRollup( false ,user);
					mi1.save( conn );
					conn.commit();
					res = _peer.findInstances( mt.getId(), Boolean.FALSE, work2.getId(), null, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );

					/*instanceTagIds*/
					mi1.setIsReadyForRollup( true ,user);
					mi1.save( conn );
					conn.commit();
					tagMap1.put( tSet1.getId(), tSet1.getTag( "testMes_tstag11" ).getId() );
					tagIds.add( ph1.getTag( "testMes_ptag1" ).getId() );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), tagIds, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 1 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() ) );
					tagIds.add( ph1.getTag( "testMes_ptag2" ).getId() );
					res = _peer.findInstances( mt.getId(), Boolean.TRUE, work2.getId(), tagIds, tagMap1, conn );
					assertNotNull( "Expecting not null result.", res );
					assertTrue( "Expecting another results count. got: " + res.size(), res.size() == 2 );
					assertTrue( "Expecting another results.", res.contains( mi1.getId() )
					                                          && res.contains( mi2.getId() ) );
				} catch ( InvalidTagEventException ignored ) {
					if ( DEBUG ) ignored.printStackTrace();
					fail( "Unexpected InvalidTagEventException occured" );
				} finally {
					delete( ph1, conn );
					delete( mi1, conn );
					delete( mi2, conn );
					delete( mt, conn );
					delete( work1, conn );
					delete( work2, conn );
					delete( tSet1, conn );
					delete( tSet2, conn );
				}
				return null;
			}
		}.execute();
	}

	public void testUpdateViewTag() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				try {
					_peer.updateViewTag( null, FAKE_ID, conn );
					fail( "Null template id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null template id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.updateViewTag( FAKE_ID, null, conn );
					fail( "Null view tag id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null view tag id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.updateViewTag( FAKE_ID, FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				Set res = _peer.updateViewTag( FAKE_ID, FAKE_ID, conn );
				assertTrue( "Expecting 0 deleted items. got: " + res.size(), res.size() == 0 );

				MetricTemplate mt = null;
				MetricInstance mi = null;
				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_updateViewTag_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();
					//v1.setMaxTagCount(new Integer(3));
					v1.addTag( "testMes_tag1" );
					v1.addTag( "testMes_tag2" );

					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(new Double(1.1)));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(new Double(2.2)));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti.getId(), null, ms1 );

					mti.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();
					mss1.save( conn );
					conn.commit();

					_peer.clearView( mt.getId(), conn );
					res = _peer.updateViewTag( mt.getId(), v1.getTag( "testMes_tag2" ).getId(), conn );
					assertTrue( "Expecting 2 updated items for 1 instance. got: "
					            + res.size(), res.size() == 1 );
					assertTrue( "Expecting anotehr result instance.", res.contains( mi.getId() ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	private Float getValue( Measurements ms, MetricLineItem item, Timestamp ts) {
		Value val = ms.toMeasurementSource(false).getValue(item, ts);
		Float res = ( val != null ? new Float( val.toNumber().floatValue() ) : null );
		return res;
	}

	public void testFindByInstanceDatesView() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				Measurements res = null;
				Date d_monthBefore = new Date( monthBefore.getTime() );
				Date d_monthLater = new Date( monthLater.getTime() + 1000L );
				Date d_twoMonthLater = new Date( twoMonthLater.getTime() + 1000L );

				try {
					_peer.findByInstanceDatesView( null, d_monthBefore, FAKE_ID, conn );
					fail( "Null metric instance id parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null metric instance id parameter should throw IllegalArgumentException." );
				}
				try {
					_peer.findByInstanceDatesView( FAKE_ID, d_monthBefore, FAKE_ID, null );
					fail( "Null connection parameter should throw IllegalArgumentException." );
				} catch ( IllegalArgumentException ok ) {
				} catch ( Exception e ) {
					fail( "Null connection parameter should throw IllegalArgumentException." );
				}

				res = _peer.findByInstanceDatesView( FAKE_ID, d_monthBefore, FAKE_ID, conn );
				assertTrue( "Expecting null result.", res == null );

				MetricTemplate mt = null;
				MetricInstance mi = null;

				try {
					mt = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_findByInstancedatesView_", Nobody.get(context));
					mt.save( conn );
					conn.commit();

					MetricTemplateItem mti1 = MetricTemplateItem.createNew( "ti1", mt.getItems(), MetricTemplateItem.USER_DEFINED_ITEM );
					mti1.setSequence( new Integer( 1 ) );

					mi = MetricInstance.createNew( mt, WorkUtil.getNoProject( context ), false );

					mt.setHasViews( true );
					View v1 = mt.getViews();

					//*v1.setMaxTagCount(new Integer(3));
					v1.addTag( "testMes_tag1" );
					v1.addTag( "testMes_tag2" );

					Double val1 = new Double(1.1);
					Double val2 = new Double(2.2);
					Measurements mss1 = new Measurements( v1.getTag( "testMes_tag1" ).getId(), mi.getId() );
					//Measurement ms1 = mss1.createMeasurement( mti1.getId(), null );
					Measurement ms1 = null;
					MeasurementItem msi1 = MeasurementItem.createNew( ms1, monthLater, SimpleValue.instantiate(val1));
					MeasurementItem msi2 = MeasurementItem.createNew( ms1, twoMonthLater, SimpleValue.instantiate(val2));
					ms1.putMeasurementItem( monthLater, msi1 );
					ms1.putMeasurementItem( twoMonthLater, msi2 );
					mss1.putMeasurement( mti1.getId(), null, ms1 );

					mti1.save( conn );
					mt.save( conn );
					conn.commit();
					mi.save( conn );
					conn.commit();

					res = _peer.findByInstanceDatesView( mi.getId(), d_monthLater, v1.getTag( "testMes_tag1" ).getId(), conn );
					assertTrue( "Expecting null result.", res == null );

					mss1.save( conn );
					conn.commit();

					res = _peer.findByInstanceDatesView( mi.getId(), d_monthBefore, v1.getTag( "testMes_tag1" ).getId(), conn );
					assertTrue( "Expecting null result.", res == null );
					res = _peer.findByInstanceDatesView( mi.getId(), d_monthLater, v1.getTag( "testMes_tag1" ).getId(), conn );
					assertNotNull( "Expecting empty result. got null.", res );
					assertTrue( "Expecting 1 values. got: " + res.getValuesCount(), res.getValuesCount() == 1 );
					assertTrue( "Expecting another result", getValue( res, mti1, monthLater ) != null &&
					                                        getValue( res, mti1, monthLater ).equals( val1 ) );
					res = _peer.findByInstanceDatesView( mi.getId(), d_twoMonthLater, v1.getTag( "testMes_tag1" ).getId(), conn );
					assertNotNull( "Expecting empty result. got null.", res );
					assertTrue( "Expecting 2 values. got: " + res.getValuesCount(), res.getValuesCount() == 2 );
					assertTrue( "Expecting another result", getValue( res, mti1, monthLater ) != null &&
					                                        getValue( res, mti1, monthLater ).equals( val1 ) &&
					                                        getValue( res, mti1, twoMonthLater ) != null &&
					                                        getValue( res, mti1, twoMonthLater ).equals( val2 ) );
				} finally {
					delete( mi, conn );
					delete( mt, conn );
				}
				return null;
			}
		}.execute();
	}

	void pause( long millisec ) {
		final String p = "";
		synchronized ( p ) {
			try {
				p.wait( millisec );
			} catch ( InterruptedException ignored ) {
			}
		}
	}

	public void testFindByInstanceId() throws Exception {
		final InstallationContext context = getContext();
		new JdbcQuery( this ) {
			protected Object query( Connection conn ) throws SQLException {
				Work work = null;
				User user = null;
				MetricTemplate tmpl = null;
				MetricInstance inst = null;
				try {
					user = User.createNewUser( "_TestMeasurementsPeer_findByInstanceId_", context );
					user.save( conn );
					conn.commit();

					work = Work.createNew( Work.TYPE, "_TestMeasurementsPeer_findByInstanceId_", user );
					work.save( conn );
					conn.commit();

					tmpl = MetricTemplate.createNew( MetricTemplate.TYPE, "_TestMeasurementsPeer_findByInstanceId_", user );
					tmpl.setHasViews( true );
					tmpl.getViews().addTag( "My View" );
					final PSTag view = tmpl.getViews().getTag( "My View" );
					tmpl.save( conn );
					conn.commit();

					final MetricTemplateItem item = MetricTemplateItem.createNew( "item1", tmpl.getItems(), MetricTemplateItem.STATIC_ITEM );
					item.setSequence( new Integer( 1 ) );
					item.save( conn );
					conn.commit();

					tmpl.save( conn );
					conn.commit();

					inst = MetricInstance.createNew( tmpl, work, false );
					work.addMetricToSaveCollection( inst, false );
					work.save( conn );
					conn.commit();

					Map measurements = _peer.findByInstanceId( inst.getId(), conn );
					assertNotNull( "Expecting result map; got null", measurements );
					assertTrue( "Expecting empty results", measurements.isEmpty() );

					final Timestamp now = new Timestamp( System.currentTimeMillis() );
					now.setNanos(0);
					
					//inst.getMeasurements(null, true).setValue(item, now, new SimpleValue(5));
					inst.save( conn );
					conn.commit();

					measurements = _peer.findByInstanceId( inst.getId(), conn );
					assertNotNull( "Expectin measurements; got null", measurements );
					assertTrue( "Expecting one measurements item; got " + measurements.size(), measurements.size() == 1 );
					Measurements ms = (Measurements) measurements.get( MetricInstance.NO_VIEW );
					assertNotNull( "Expecting measurements for view", ms );
					assertTrue( "Expecting valid measurement value", new SimpleValue(5).equals(ms.toMeasurementSource(true).getValue(item, now)));
					assertTrue( "Expecting 1 values in measurements; got " + ms.getValuesCount(), ms.getValuesCount() == 1 );

					pause( 2000 );
					Timestamp now1 = new Timestamp( System.currentTimeMillis() );
					now1.setNanos( 0 );

					//inst.getMeasurements(view.getId(), true).setValue(item, now, new SimpleValue(10));
					inst.save(conn);
					conn.commit();

					measurements = _peer.findByInstanceId( inst.getId(), conn );
					assertNotNull( "Expectin measurements; got null", measurements );
					assertTrue( "Expecting 2 measurements items; got " + measurements.size(), measurements.size() == 2 );
					ms = (Measurements) measurements.get( MetricInstance.NO_VIEW );
					assertNotNull( "Expecting measurements for view", ms );
					assertTrue( "Expecting valid measurement value", new SimpleValue(5).equals(ms.toMeasurementSource(true).getValue(item, now)));
					assertTrue( "Expecting 1 values in measurements; got " + ms.getValuesCount(), ms.getValuesCount() == 1 );
					ms = (Measurements) measurements.get( view.getId() );
					assertNotNull( "Expecting measurements for view", ms );
					assertTrue( "Expecting valid measurement value", new SimpleValue(10).equals(ms.toMeasurementSource(true).getValue(item, now)));
					assertTrue( "Expecting 1 values in measurements; got " + ms.getValuesCount(), ms.getValuesCount() == 1 );
				} finally {
					delete( inst, conn );
					delete( work, conn );
					delete( tmpl, conn );
					delete( user, conn );
				}
				return null;
			}
		}.execute();
	}
}
