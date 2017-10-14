package com.cinteractive.util;

import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import junit.framework.TestCase;


public class TestSimpleStringsFormat extends TestCase
{
	private static class Record
	{
		String[] _inputs;
		String   _expected;
		
		Record( String[] inputs, String expected )
		{
			_inputs   = inputs;
			_expected = expected;
		}
	}
	
	
	private SimpleStringsFormat _fmt;
	private Record[]            _data;
	
	
	public TestSimpleStringsFormat( String name ) { super( name ); }
	
	
	public void setUp()
	throws Exception
	{
		super.setUp();
		
		_fmt = new SimpleStringsFormat();
		_data = new Record[]
		{
			new Record( new String[] { "word" }, "word" ),
			new Record( new String[] { "" }, "\"\"" ),
			new Record( new String[] { "\"" }, "\"\"\"\"" ),
			new Record( new String[] { " a phrase " }, "\" a phrase \"" ),
			new Record( new String[] { "a \"phrase\"" }, "\"a \"\"phrase\"\"\"" ),
			new Record( new String[] { "word", "a phrase" }, "word \"a phrase\"" ),
			new Record( new String[] { "word", "a phrase", "", "word" }, "word \"a phrase\" \"\" word" )
		};
	}
	
	public void tearDown()
	throws Exception
	{
		super.tearDown();
		_fmt = null;
		_data = null;
	}
	
	public void testFormat()
	throws Exception
	{
		Collection values = null;
		String encoded = _fmt.format( values );
		assertNull( "Expecting null encoded form for null collection.", encoded );
		
		values = new java.util.ArrayList();
		encoded = _fmt.format( values );
		assertEquals( "Expecting empty encoded form for empty collection.", "", encoded );
		
		values.add( null );
		try
		{
			_fmt.format( values );
			fail( "Expecting IllegalArgumentException for Collection containing null." );
		}
		catch( IllegalArgumentException ok ) {}
		
		for( int i = 0; i < _data.length; i++ )
		{
			values.clear();
			for( int j = 0; j < _data[ i ]._inputs.length; j++ )
				values.add( _data[ i ]._inputs[ j ] );
			String value = _fmt.format( values );
			assertEquals( _data[ i ]._expected, value );
		}
	}
	
	public void testParseInto()
	throws Exception
	{
		List values = null;
		try
		{
			_fmt.parseInto( "word", values );
			fail( "Null collection should throw IllegalArgumentException" );
		}
		catch( IllegalArgumentException ok ) {}
		
		values = Collections.unmodifiableList( new java.util.ArrayList() );
		_fmt.parseInto( null, values );
		_fmt.parseInto( "", values );
		_fmt.parseInto( " ", values );
		
		values = new java.util.ArrayList();
		final String[] illegal = new String[]
		{
			"\"",
			"needsspace\"needsspace\"",
			"\"needsspace\"needsspace"
		};
		for( int i = 0; i < illegal.length; i++ )
		{
			values.clear();
			try
			{
				_fmt.parseInto( illegal[ i ], values );
				fail( "Expecting ParseException for malformed string '" + illegal[ i ] + "'." );
			}
			catch( ParseException ok ) {}
		}
		
		for( int i = 0; i < _data.length; i++ )
		{
			values.clear();
			_fmt.parseInto( _data[ i ]._expected, values );
			assertEquals( "Expected: '" + _data[ i ]._expected + "'", _data[ i ]._inputs.length, values.size() );
			for( int j = 0; j < values.size(); j ++ )
				assertEquals( _data[ i ]._inputs[ j ], values.get( j ) );
		}
	}
}
