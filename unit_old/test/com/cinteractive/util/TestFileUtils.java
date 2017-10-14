package com.cinteractive.util;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;


public class TestFileUtils extends TestCase
{
	private File _home;
	
	
	public TestFileUtils( String name ) { super( name ); }
	
	
	protected void setUp()
	throws Exception
	{
		super.setUp();
		
		_home = new File( System.getProperty( "java.home" ) );
	}
	
	protected void tearDown()
	throws Exception
	{
		super.tearDown();
		
		_home = null;
	}
	
	public void testCopy()
	throws Exception
	{
		try
		{
			FileUtils.copy( null, null, true );
			fail( "Expecting IllegalArgumentException for null source or destination." );
		}
		catch( IllegalArgumentException ok ) {}
		
		try
		{
			FileUtils.copy( _home, _home, true );
			fail( "Expecting IllegalArgumentException for directories as source or destination." );
		}
		catch( IllegalArgumentException ok ) {}
		
		File src = File.createTempFile( "_temp", ".garbage" );
		src.delete();
		try
		{
			FileUtils.copy( src, File.createTempFile( "_temp", ".garbage" ), true );
			fail( "Expecting IllegalArgumentException for non-existent source." );
		}
		catch( IllegalArgumentException ok ) {}
		
		src = File.createTempFile( "_temp", ".garbage" );
		try
		{
			FileUtils.copy( src, src, true );
			fail( "Expecting IllegalArgumentException for self-copy." );
		}
		catch( IllegalArgumentException ok ) {}
		
		File dest = File.createTempFile( "_temp", ".garbage" );
		try
		{
			FileUtils.copy( src, dest, false );
			fail( "Expecting IllegalArgumentException for attempt to overwrite existing file." );
		}
		catch( IllegalArgumentException ok ) {}
	}
		
	public void testListAllFiles()
	throws Exception
	{
		final List files = FileUtils.listAllFiles( _home, new java.util.ArrayList(), null );
		assertNotNull( files );
		
		for( int i = 0; i < files.size(); i++ )
			assertTrue( ( (File) files.get( i ) ).isFile() );
	}
	
	public void testListAllFilesNoFiles()
	throws Exception
	{
		final List empty = FileUtils.listAllFiles( _home, new java.util.ArrayList(), FileUtils.IS_DIRECTORY_FILTER );
		assertNotNull( empty );
		assertTrue( empty.isEmpty() );
	}
}
