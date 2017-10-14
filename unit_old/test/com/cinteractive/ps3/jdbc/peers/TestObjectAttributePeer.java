/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.ps3.jdbc.peers;

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.JdbcPersistableBase;
import com.cinteractive.jdbc.JdbcQuery;
import com.cinteractive.jdbc.StringDataSourceId;
import com.cinteractive.ps3.ObjectAttribute;
import com.cinteractive.ps3.PSObject;
import com.cinteractive.ps3.contexts.InstallationContext;
import com.cinteractive.ps3.entities.Nobody;
import com.cinteractive.ps3.entities.User;
import com.cinteractive.ps3.work.Work;
import com.cinteractive.ps3.work.WorkUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestObjectAttributePeer extends TestJdbcPeer
{
	private ObjectAttributePeer _peer;
        private static final boolean DEBUG = true; //show debug info

	static{
	    registerCase(StringDataSourceId.get( StringDataSourceId.MSSQL7 ));
	}

	private static void registerCase(DataSourceId id){
	    TestSql.registerTestCase( ObjectAttributePeer.class.getName(), TestObjectAttributePeer.class.getName());
	}

	protected TestObjectAttributePeer(String name) { super(name); }


	public static Test bareSuite()
	{
		final TestSuite suite = new TestSuite( "TestObjectAttributePeer" );
		
		suite.addTest( new TestObjectAttributePeer( "testDelete" ) );
		suite.addTest( new TestObjectAttributePeer( "testFindByObjectId" ) );
		suite.addTest( new TestObjectAttributePeer( "testFindObjectIdsByAttribute" ) );
		suite.addTest( new TestObjectAttributePeer( "testInsert" ) );
		suite.addTest( new TestObjectAttributePeer( "testUpdate" ) );
		
		return suite;
	}
	public static Test suite()
	{
		return setUpDb( bareSuite() );
	}


	public void setUp()
	throws Exception
	{
		super.setUp();

		_peer = (ObjectAttributePeer) new JdbcQuery( this )
		{
			protected Object query( Connection conn )
			throws SQLException
			{
				return ObjectAttributePeer.getInstance( conn );
			}
		}.execute();
		if( _peer == null )
			throw new NullPointerException( "Null ObjectAttributePeer instance." );
	}
	public void tearDown()
	throws Exception
	{
		super.tearDown();
		_peer = null;
	}

        private void delete(JdbcPersistableBase o, Connection conn) {
		try {
			if (o != null) {
//                if (o instanceof PSObject)
                    //((PSObject) o).setModifiedById(Nobody.get(getContext()).getId());
				o.deleteHard(conn);
				conn.commit();
			}
		} catch (Exception ignored) {
			if (DEBUG) ignored.printStackTrace();
		}
        }

	public void testDelete() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());
		Work work = WorkUtil.getNoProject(context);

		conn.setAutoCommit(false);

		try {
			ObjectAttribute attr = null;
			work.setStringAttribute("attrName", "StringValue");
	        for ( Enumeration i = work.attributes(); i.hasMoreElements();) {
				attr = (ObjectAttribute)i.nextElement();
				if (attr.getAttributeName().equals("attrName"))
					break;
	        }
	        attr.save(conn);

			final Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery( "select object_id from object_attribute where object_id='" + work.getId() + "'" +
				" and attribute_name='" + attr.getAttributeName() + "'");

			assertTrue("Expecting record in resultset", rset.next());

			try
			{
				_peer.delete(null, conn);
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}

			try
			{
				_peer.delete(attr, null);
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}
			_peer.delete(attr, conn);

			rset = stmt.executeQuery( "select object_id from object_attribute where object_id='" + work.getId() + "'" +
				" and attribute_name='" + attr.getAttributeName() + "'");

			assertTrue("Not expecting record in resultset", !rset.next());

		}
		finally {
			conn.rollback();
			conn.close();
		}
	}

	public void testFindByObjectId() throws Exception {
                final InstallationContext context = getContext();
                new JdbcQuery( this )
                  {

                  private boolean checkAttrContent(Collection res, String name, String valString, Float valFloat){
                        ObjectAttribute Attr = null;
                        for (Iterator i=res.iterator(); i.hasNext(); )
                        {
                          Attr = (ObjectAttribute) i.next();
                          if (Attr.getAttributeName().equals(name)) {
                            break;
                          }else{
                            Attr = null;
                          }
                        }
                        boolean out = false;
                        if (Attr != null){
                           if (valString != null)
                             out = (Attr.getStringValue().equals(valString));
                           if (valFloat != null)
                             out = out && (Attr.getFloatValue().equals(valFloat));
                        }
                  	return out;
                      }

                  protected Object query(Connection conn) throws SQLException
                     {
                     Collection res = null;

                     try
   		        {
			res = _peer.findByObjectId(null, conn);
			fail( "Null PersistentKey parameter should throw IllegalArgumentException." );
		        }
                     catch( IllegalArgumentException ok ) {}
                     catch( Exception e ){
			fail( "Null PersistentKey parameter should throw IllegalArgumentException." );
                        }

                     res = _peer.findByObjectId(FAKE_ID, conn);
                     assertNotNull("Expecting empty result for fake object id; got null", res);
                     assertTrue("Expecting empty result", res.size()==0);

                     final User user = User.getNobody(context);
                     Work work = Work.createNew(Work.TYPE, "_TestObjAttrPeer_findByObjectId1_", user);

                     try
                       {

                       work.save(conn);
                       conn.commit();

                       try
			 {
			 res = _peer.findByObjectId(work.getId(), null);
			 fail( "Null Connection parameter should throw IllegalArgumentException." );
			 }
                       catch( IllegalArgumentException ok ) {}
                       catch( Exception e ){
			 fail( "Null Connection parameter should throw IllegalArgumentException." );
			 }

                       res = _peer.findByObjectId(work.getId(), conn);
                       assertTrue("Expecting empty result", res!=null&&res.size()==0);

                       work.setStringAttribute("attrName_1", "StringValue");
                       work.save(conn);
                       conn.commit();

                       res = _peer.findByObjectId(work.getId(), conn);
                       assertTrue("Expecting one attribute in result set", res!=null&&res.size()==1);
                       if (res!=null&&res.size()==1)
                       {
                          assertTrue("Expecting another content attribute attrName_1 in result set 1 try",
                                      checkAttrContent(res, "attrName_1", "StringValue", null));
                       }


                       work.setFloatAttribute("attrName_1", Float.valueOf("0.5"));
                       work.save(conn);
                       conn.commit();

                       res = _peer.findByObjectId(work.getId(), conn);
                       assertTrue("Expecting one attribute in result set", res!=null&&res.size()==1);
                       if (res!=null&&res.size()==1)
                       {
                          assertTrue("Expecting another content attribute attrName_1 in result set 2 try",
                                      checkAttrContent(res, "attrName_1", "StringValue", Float.valueOf("0.5")));
                       }


                       work.setStringAttribute("attrName_2", "StringValue");
                       work.save(conn);
                       conn.commit();

                       res = _peer.findByObjectId(work.getId(), conn);
                       assertTrue("Expecting two attributes in result set", res!=null&&res.size()==2);
                       if (res!=null&&res.size()==2)
                       {
                          assertTrue("Expecting another content attribute attrName_1 in result set 3 try",
                                      checkAttrContent(res, "attrName_1", "StringValue", Float.valueOf("0.5")));
                          assertTrue("Expecting another content attribute attrName_2 in result set 4 try",
                                      checkAttrContent(res, "attrName_2", "StringValue", null));
                       }

                       work.removeAttributes("attrName");
                       work.save(conn);
                       conn.commit();

                       res = _peer.findByObjectId(work.getId(), conn);
                       assertNotNull("Expecting empty result for fake object id; got null", res);
                       assertTrue("Expecting empty result", res.size()==0);
                       }
                     finally
                       {
                            delete(work, conn);
                       }
                     return null;
                     }
                  }.execute();
	}

	public void testFindObjectIdsByAttribute()
	throws Exception
	{
	// no data
                final InstallationContext context = getContext();
                new JdbcQuery( this )
                  {
                  private boolean checkId(Collection res, PersistentKey id){
                        for (Iterator i=res.iterator(); i.hasNext(); )
                        {
                          if ( ((PersistentKey) i.next()).equals(id))
                            return true;
                        }
                        return false;
                      }

                  protected Object query(Connection conn) throws SQLException
                     {

                     Collection res = null;

                     try
   		        {
			res = _peer.findObjectIdsByAttribute("blah", "blah", null, conn);
			fail( "Null ContextId parameter should throw IllegalArgumentException." );
		        }
                     catch( IllegalArgumentException ok ) {}
                     catch( Exception e ){
			fail( "Null ContextId parameter should throw IllegalArgumentException." );
                        }

                     try
   		        {
			res = _peer.findObjectIdsByAttribute("blah", "blah", context.getId(), null);
			fail( "Null Connection parameter should throw IllegalArgumentException." );
		        }
                     catch( IllegalArgumentException ok ) {}
                     catch( Exception e ){
			fail( "Null Connection parameter should throw IllegalArgumentException." );
                        }

                     try
   		        {
			res = _peer.findObjectIdsByAttribute("", "blah", context.getId(), null);
			fail( "Null Connection parameter should throw IllegalArgumentException." );
		        }
                     catch( IllegalArgumentException ok ) {}
                     catch( Exception e ){
			fail( "Null Connection parameter should throw IllegalArgumentException." );
                        }

                     try
   		        {
			res = _peer.findObjectIdsByAttribute(null, null, context.getId(), conn);
			fail( "Null Attribute Name parameter should throw IllegalArgumentException." );
		        }
                     catch( IllegalArgumentException ok ) {}
                     catch( Exception e ){
			fail( "Null CAttribute Name parameter should throw IllegalArgumentException." );
                        }

                     res = _peer.findObjectIdsByAttribute("blah", "blah", FAKE_ID, conn);
                     assertNotNull("Expecting empty result for fake context id; got null", res);
                     assertTrue("Expecting empty result", res.size()==0);

                     res = _peer.findObjectIdsByAttribute("a6643bjhk8320fjsdklfn873420d4sgn980", null, context.getId(), conn);
                     assertNotNull("Expecting empty result for fake attribute name; got null", res);
                     assertTrue("Expecting empty result for fake attribute name", res.size()==0);

                     res = _peer.findObjectIdsByAttribute("a6643bjhk8320fjsdklfn873420d4sgn980", "a6643bjhk8320fjsdklfn873420d4sgn980", context.getId(), conn);
                     assertNotNull("Expecting empty result for fake attribute name; got null", res);
                     assertTrue("Expecting empty result for fake attribute name", res.size()==0);

                     final User user = User.getNobody(context);

                     Work work = Work.createNew(Work.TYPE, "_TestObjAttrPeer_findObjectIdsbyAttribute1_", user);
                     Work work1 = Work.createNew(Work.TYPE, "_TestObjAttrPeer_findObjectIdsbyAttribute2_", user);

                     try
                       {

                       work.save(conn);
                       conn.commit();

                       work.setStringAttribute("__test_workwithattr__attrName_1", "StringValue");
                       work.save(conn);
                       conn.commit();

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", null, context.getId(), conn);
                       assertTrue("Expecting not empty result for real attribute name", res!=null&&res.size()==1);
                       if (res!=null&&res.size()==1)
                       {
                         assertTrue("Expecting Id in result set for real attribute name", checkId(res, work.getId()));
                       }

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", "StringValue", context.getId(), conn);
                       assertTrue("Expecting not empty result for real attribute name and value", res!=null&&res.size()==1);
                       if (res!=null&&res.size()==1)
                       {
                         assertTrue("Expecting Id in result set for real attribute name and value", checkId(res, work.getId()));
                       }

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", "a6643bjhk8320fjsdklfn873420d4sgn980", context.getId(), conn);
                       assertNotNull("Expecting empty result for real attribute name and fake value, got Null", res);
                       assertTrue("Expecting empty result for real attribute name and fake value", res!=null&&res.size()==0);

                       work1.save(conn);
                       conn.commit();

                       work1.setStringAttribute("__test_workwithattr__attrName_1", "StringValue");
                       work1.save(conn);
                       conn.commit();

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", null, context.getId(), conn);
                       assertTrue("Expecting not empty result 2 for real attribute name", res!=null&&res.size()==2);
                       if (res!=null&&res.size()==2)
                       {
                         assertTrue("Expecting Id in result set 2 for real attribute name", checkId(res, work.getId()) && checkId(res, work1.getId()));
                       }

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", "StringValue", context.getId(), conn);
                       assertTrue("Expecting not empty result 2 for real attribute name and value", res!=null&&res.size()==2);
                       if (res!=null&&res.size()==2)
                       {
                         assertTrue("Expecting Id in result set 2 for real attribute name and value", checkId(res, work.getId()) && checkId(res, work1.getId()));
                       }

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", "a6643bjhk8320fjsdklfn873420d4sgn980", context.getId(), conn);
                       assertNotNull("Expecting empty result 2 for real attribute name and fake value, got Null", res);
                       assertTrue("Expecting empty result 2 for real attribute name and fake value", res!=null&&res.size()==0);

                       work1.setStringAttribute("__test_workwithattr__attrName_1", "StringValue_2");
                       work1.save(conn);
                       conn.commit();

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", "StringValue", context.getId(), conn);
                       assertTrue("Expecting not empty result for real attribute name and value after change attr", res!=null&&res.size()==1);
                       if (res!=null&&res.size()==1)
                       {
                         assertTrue("Expecting Id in result set for real attribute name and value after change attr", checkId(res, work.getId()));
                       }

                       res = _peer.findObjectIdsByAttribute("__test_workwithattr__attrName_1", null, context.getId(), conn);
                       assertTrue("Expecting not empty result for real attribute name after change attr", res!=null&&res.size()==2);
                       if (res!=null&&res.size()==1)
                       {
                         assertTrue("Expecting Id in result set for real attribute name after change attr", checkId(res, work.getId())&&checkId(res, work1.getId()));
                       }

                       }
                     finally
                       {
                            delete(work, conn);
                            delete(work1, conn);
                       }
                     return null;
                     }
                  }.execute();
	}

	public void testInsert() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());
		Work work = WorkUtil.getNoProject(context);

		conn.setAutoCommit(false);

		try {

			ObjectAttribute attr = null;
			work.setStringAttribute("attrName", "StringValue");
	        for ( Enumeration i = work.attributes(); i.hasMoreElements();) {
				attr = (ObjectAttribute)i.nextElement();
				if (attr.getAttributeName().equals("attrName"))
					break;
	        }



			try
			{
				_peer.insert(null, conn);
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}

			try
			{
				_peer.insert(attr, null);
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}

	        _peer.insert(attr, conn);

			final Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery( "select object_id from object_attribute where object_id='" + work.getId() + "'" +
				" and attribute_name='" + attr.getAttributeName() + "'");

			assertTrue("Expecting record in resultset", rset.next());



		}
		finally {
			conn.rollback();
			conn.close();
		}
	}
	public void testUpdate() throws Exception {
		final Connection conn = getConnection();
		final InstallationContext context = InstallationContext.get(getContextName());
		Work work = WorkUtil.getNoProject(context);

		conn.setAutoCommit(false);

		try {

			ObjectAttribute attr = null;
			work.setStringAttribute("attrName", "StringValue");
	        for ( Enumeration i = work.attributes(); i.hasMoreElements();) {
				attr = (ObjectAttribute)i.nextElement();
				if (attr.getAttributeName().equals("attrName"))
					break;
	        }
	        attr.save(conn);

			final Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery( "select object_id from object_attribute where object_id='" + work.getId() + "'" +
				" and attribute_name='" + attr.getAttributeName() + "'");

			assertTrue("Expecting record in resultset", rset.next());

			attr.setStringValue("Updated");

			try
			{
				_peer.update(null, conn);
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null ObjectAttribute parameter should throw IllegalArgumentException." );
			}

			try
			{
				_peer.update(attr, null);
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}
			catch( IllegalArgumentException ok ) {}
			catch( Exception e ){
				fail( "Null Connection parameter should throw IllegalArgumentException." );
			}
			_peer.update(attr, conn);

			rset = stmt.executeQuery( "select object_id from object_attribute where object_id='" + work.getId() + "'" +
				" and attribute_name='" + attr.getAttributeName() + "'");

			assertTrue("Expecting record in resultset", rset.next());

		}
		finally {
			conn.rollback();
			conn.close();
		}
	}

    // gags
    public void testGetInstance() { }
}
