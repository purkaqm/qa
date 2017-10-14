/* Copyright (c) 2000 Cambridge Interactive, Inc. All rights reserved.*/
package com.cinteractive.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestCIPoolDriver extends TestCase
{
	private String _url;
	private Properties _props;

	
	public TestCIPoolDriver(String name)
	{
		super(name);
	}
	
	
	public static Test suite()
	{
		final TestSuite suite = new TestSuite("TestCIPoolDriver");
		
		suite.addTest(new TestCIPoolDriver("testConnectContract"));
		suite.addTest(new TestCIPoolDriver("testConnectNoPool"));
		suite.addTest(new TestCIPoolDriver("testConnect"));
		suite.addTest(new TestCIPoolDriver("testConnectTransactionIsolation"));
		suite.addTest(new TestCIPoolDriver("testConnectFromTheSamePool"));
		
		return suite;
	}
	
	
	private Properties cloneProps()
	{
		final Properties props = new Properties();
		props.putAll(_props);
		return props;
	}
	
	private ConnectionSource createSource(boolean standalone)
	{
		Properties props = cloneProps();
		props.setProperty(ConnectionPool.URL_PROP, _url);
		if (standalone)
			props.setProperty(CIPoolDriver.NO_POOL_PROP, "true");
		
		return createSource(props);
	}
	
	private ConnectionSource createSource(final Properties props)
	{
		return new ConnectionSource()
		{
			public Connection getConnection()
			throws SQLException
			{
				return CIPoolDriver.getConnection(props);
			}
		};
	}
	
	public void setUp()
	throws Exception
	{
		_url = "jdbc:inetdae7:psdevdb:1433?database=starbuck_ci";
		
		_props = new Properties();
		_props.setProperty(ConnectionPool.USER_PROP, "psdev");
		_props.setProperty(ConnectionPool.PASSWORD_PROP, "psdev");
		_props.setProperty(ConnectionPool.MAX_CONNECTIONS_PROP, Integer.toString(2));
		
		CIPoolDriver.g_instance.deregisterPool(ConnectionPool.getPoolName(_props));
	}
	
	
	public void testConnect()
	throws Exception
	{
		new JdbcQuery(createSource(false))
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				assertNotNull("Got unexpected null connection for url '" + _url + "'.", conn);
				assertTrue("Not a JdbcConnection.", conn instanceof JdbcConnection);
				assertTrue("Should be pooled.", conn instanceof PooledJdbcConnection);
				return null;
			}
		}.execute();
	}
	
	public void testConnectContract()
	throws Exception
	{
		try {
			CIPoolDriver.getConnection(null);
			fail("Expecting IllegalArgumentException for null properties.");
		}
		catch (IllegalArgumentException ok) {}
		
		try {
			CIPoolDriver.getConnection(_props);
			fail("Expecting IllegalArgumentException for no url.");
		}
		catch (IllegalArgumentException ok) {}
		
		Properties props = cloneProps();
		props.put(ConnectionPool.URL_PROP, _url);
		try {
			CIPoolDriver.getConnection(props);
			fail("Expecting IllegalArgumentException incorrect url prefix.");
		}
		catch (IllegalArgumentException ok) {}
	}
	
	public void testConnectFromTheSamePool()
	throws SQLException
	{
		Properties props = cloneProps();
		props.setProperty(ConnectionPool.URL_PROP, _url);
		
		PooledJdbcConnection conn1 = null;
		PooledJdbcConnection conn2 = null;
		try
		{
			conn1 = (PooledJdbcConnection) CIPoolDriver.getConnection(props);
			assertTrue(conn1.isFromTheSamePool(conn1));
			
			conn2 = (PooledJdbcConnection) CIPoolDriver.getConnection(props);
			
			assertTrue("Need 2 different connections.", conn1 != conn2);
			assertTrue(conn1.isFromTheSamePool(conn2));
			assertTrue(conn2.isFromTheSamePool(conn1));
		}
		finally
		{
			if (conn1 != null)
				try { conn1.close(); } catch(SQLException ignored) {}
			if (conn2 != null)
				try { conn2.close(); } catch(SQLException ignored) {}
		}
	}
	
	public void testConnectNoPool()
	throws Exception
	{
		new JdbcQuery(createSource(true))
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				assertNotNull("Got unexpected null connection for url '" + _url + "'.", conn);
				assertTrue("Not a JdbcConnection.", conn instanceof JdbcConnection);
				assertTrue("Shouldn't be pooled.", !(conn instanceof PooledJdbcConnection));
				return null;
			}
		}.execute();
	}
	
	public void testConnectTransactionIsolation()
	throws Exception
	{
		final int level = Connection.TRANSACTION_READ_UNCOMMITTED;
		
		final Properties props = cloneProps();
		props.setProperty(ConnectionPool.URL_PROP, _url);
		props.setProperty(ConnectionPool.TRANSACTION_ISOLATION_PROP, Integer.toString(level));
		
		new JdbcQuery(createSource(props))
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				assertEquals(conn.getTransactionIsolation(), level);
				return null;
			}
		}.execute();
		
		props.setProperty(CIPoolDriver.NO_POOL_PROP, "true");
		new JdbcQuery(createSource(props))
		{
			protected Object query(Connection conn)
			throws SQLException
			{
				assertEquals(conn.getTransactionIsolation(), level);
				return null;
			}
		}.execute();
	}
}
