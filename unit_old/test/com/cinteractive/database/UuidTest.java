package com.cinteractive.database;

/* Copyright © 2000 Cambridge Interactive, Inc. All rights reserved.*/

import junit.framework.TestCase;
/**
 * Unit test for Uuids
 * @author Yoritaka Sakakura
 */
public class UuidTest extends TestCase
{
	private Uuid guid1 = null;
	private Uuid guid2 = null;
	public UuidTest( String testName )
	{
		super( testName );
	}
	protected void setUp()
	{
		guid1 = Uuid.create();
		guid2 = Uuid.create();
	}
	protected void tearDown()
	{
		guid1 = null;
		guid2 = null;
	}
	/**
	 * A Uuid must equal to itself and another Uuid with the same id.
	 */
	public void testEquals()
	{
		Uuid expected = Uuid.get( guid1.toString() );
		assertEquals( guid1, guid1 );
		assertEquals( expected, guid1 );
	}
	public void testLowerCase()
	{
		assertEquals( guid1, Uuid.fromLowerCaseString( guid1.toLowerCaseString() ) );
		assertEquals( guid2, Uuid.fromLowerCaseString( guid2.toLowerCaseString() ) );
		assert( !guid1.equals( Uuid.fromLowerCaseString( guid2.toLowerCaseString() ) ) );
		assert( !guid2.equals( Uuid.fromLowerCaseString( guid1.toLowerCaseString() ) ) );
	}
	/**
	 * Two Uuids created with separate invocations of create() must not be equal.
	 * TODO - Test concurrency.
	 */
	public void testNotEquals()
	{
		assert( !guid1.equals( guid2 ) );
		assert( !guid2.equals( guid1 ) );
	}
}
