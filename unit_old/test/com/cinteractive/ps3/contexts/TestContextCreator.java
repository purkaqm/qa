package com.cinteractive.ps3.contexts;

import com.cinteractive.ps3.PSHome;
import com.cinteractive.ps3.test.TestPS;
import java.io.File;
import junit.framework.Test;
import junit.framework.TestSuite;


public class TestContextCreator extends TestPS
{
	private static final String NEW_CONTEXT_NAME = "deleteme";
	
	
	public TestContextCreator( String name ) { super( name ); }
	

	public static Test suite()
	{
		final TestSuite suite = new TestSuite();
		
		suite.addTest( new TestContextCreator( "testMustBeInitialized" ) );
		suite.addTest( new TestContextCreator( "testCreateXml" ) );
		//suite.addTest( new TestContextCreator( "testCreateDatabase" ) );
		//suite.addTest( new TestContextCreator( "testDefineDatabase" ) );
		//suite.addTest( new TestContextCreator( "testEnableTextSearch" ) );
		//suite.addTest( new TestContextCreator( "testCreateContextRecord" ) );
		//suite.addTest( new TestContextCreator( "testInstallAgents" ) );
		//suite.addTest( new TestContextCreator( "testInstallObjects" ) );
		
		return setUpHome( suite );
	}


	private File contextXml()
	{
		return new File( new File( PSHome.getContextsPath(), NEW_CONTEXT_NAME ), "context.xml" );
	}
	
	public void testCreateContextRecord()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.createContextRecord( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testCreateDatabase()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.setAttribute( ContextCreator.CREATE_DB_URL, "jdbc:inetora:norfolk.cinteractive.com:1521:norfolk" );
		creator.setAttribute( ContextCreator.CREATE_DB_USER, "sys" );
		creator.setAttribute( ContextCreator.CREATE_DB_PASSWORD, "sys" );
		creator.setAttribute( ContextCreator.DB_DRIVER, "com.inet.ora.OraDriver" );
		creator.setAttribute( ContextCreator.DB_USER, NEW_CONTEXT_NAME );
		creator.setAttribute( ContextCreator.DB_PASSWORD, NEW_CONTEXT_NAME );
		//creator.setAttribute( ContextCreator.DB_ORACLE_SID, "norfolk" );
		
		creator.createDatabase( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testCreateXml()
	throws Exception
	{
		assertTrue( "context.xml for '" + NEW_CONTEXT_NAME + "' already exists.", !contextXml().exists() );
			
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.setAttribute( ContextCreator.SOURCE_CONTEXT_NAME, getContextName() );
		creator.setAttribute( ContextCreator.PROXY_NAME, "jsp_holder" );
		creator.setAttribute( ContextCreator.WEB_SERVER_NAME, "norfolk.cinteractive.com" );
		creator.setAttribute( ContextCreator.MAIL_SERVER_NAME, "psmail.psteering.com" );
		creator.setAttribute( ContextCreator.DB_DRIVER, "com.inet.ora.OraDriver" );
		creator.setAttribute( ContextCreator.DB_HOST, "norfolk.cinteractive.com" );
		creator.setAttribute( ContextCreator.DB_PORT, "1521" );
		creator.setAttribute( ContextCreator.DB_USER, NEW_CONTEXT_NAME );
		creator.setAttribute( ContextCreator.DB_PASSWORD, NEW_CONTEXT_NAME );
		//creator.setAttribute( ContextCreator.DB_ORACLE_SID, "norfolk" );
		creator.setAttribute( ContextCreator.COMPANY_NAME, "Thingy" );
		creator.setAttribute( ContextCreator.COMPANY_URL, "http://google.com" );
		
		creator.createXml( true );
		
		creator.init();
		creator.execute();
			
		assertTrue( "context.xml for '" + NEW_CONTEXT_NAME + "' doesn't exist.", contextXml().exists() );
	}
	
	public void testDefineDatabase()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.setAttribute( ContextCreator.DB_DRIVER, "com.inet.ora.OraDriver" );
		creator.setAttribute( ContextCreator.DB_HOST, "norfolk.cinteractive.com" );
		creator.setAttribute( ContextCreator.DB_PORT, "1521" );
		//creator.setAttribute( ContextCreator.DB_ORACLE_SID, "norfolk" );
		
		creator.setAttribute( ContextCreator.DB_USER, NEW_CONTEXT_NAME );
		creator.setAttribute( ContextCreator.DB_PASSWORD, NEW_CONTEXT_NAME );
		
		creator.defineDatabase( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testEnableTextSearch()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.setAttribute( ContextCreator.DB_DRIVER, "com.inet.ora.OraDriver" );
		creator.setAttribute( ContextCreator.DB_HOST, "norfolk.cinteractive.com" );
		creator.setAttribute( ContextCreator.DB_PORT, "1521" );
		//creator.setAttribute( ContextCreator.DB_ORACLE_SID, "norfolk" );
		
		creator.setAttribute( ContextCreator.DB_USER, NEW_CONTEXT_NAME );
		creator.setAttribute( ContextCreator.DB_PASSWORD, NEW_CONTEXT_NAME );
		
		creator.enableTextSearch( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testInstallAgents()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.installAgents( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testInstallObjects()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( NEW_CONTEXT_NAME );
		
		creator.installObjects( true );
		
		creator.init();
		creator.execute();
	}
	
	public void testMustBeInitialized()
	throws Exception
	{
		final ContextCreator creator = new ContextCreator( "deleteme" );
		try
		{
			creator.execute();
			fail( "Expecting failure for uninitialized context creator." );
		}
		catch( IllegalStateException ok ) {}
	}
}
