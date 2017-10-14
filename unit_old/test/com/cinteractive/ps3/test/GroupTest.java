package com.cinteractive.ps3.test;
/* Copyright © 2000 Cambridge Interactive, Inc. All rights reserved.*/

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.ps3.entities.Group;
import com.cinteractive.ps3.entities.Person;
import com.cinteractive.ps3.entities.User;

public class GroupTest extends PSTestBase
{
	private PersistentKey _id = null;
/**
 * UserTest constructor comment.
 * @param testName java.lang.String
 */
public GroupTest(String testName) {
	super(testName);
}
	/**
	 * Instantiate and persist a new User.
	 * If successful, set _id to the new User's id.
	 */
	private void create()
	{
		try
		{
			final Group group = Group.createNew( "Yo", (User) Person.getNobody( getContext() ), getContext() );
			group.save();

			_id = group.getPersistentKey();
		}
		catch( RuntimeSQLException e )
		{
			fail();
		}
	}
	/**
	 * Remove User from cache to force a load from the database.
	 * Load and save the user.
	 * Assert that the User is gone.
	 */
	private void delete()
	{
		try
		{
			getContext().getCache().remove( _id );

			final Group group = getGroup();
			group.deleteHard();
		}
		catch( RuntimeSQLException e )
		{
			fail();
		}

		try
		{
			getGroup();
			fail( "User not deleted." );
		}
		catch( RuntimeSQLException e ) {}
	}
	private Group getGroup()
	{
		return Group.getGroup( _id, getContext() );
	}
	/**
	 * Remove User from cache to force a load from the database.
	 * Load, modify, and save the user.
	 */
	private void modify()
	{
		try
		{
			getContext().getCache().remove( _id );

			final Group group = getGroup();
			group.setName( "Yawn" );
			group.save();
		}
		catch( RuntimeSQLException e )
		{
			fail();
		}
	}
	protected void tearDown()
	{
		_id = null;

		super.tearDown();
	}
	public void testPersistence()
	{
		create();
		modify();
		delete();
	}
}
