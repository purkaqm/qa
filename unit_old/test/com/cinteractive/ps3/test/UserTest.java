package com.cinteractive.ps3.test;
/* Copyright © 2000 Cambridge Interactive, Inc. All rights reserved.*/

import com.cinteractive.database.PersistentKey;
import com.cinteractive.jdbc.RuntimeSQLException;
import com.cinteractive.ps3.entities.User;

public class UserTest extends PSTestBase
{
	private PersistentKey _id = null;
/**
 * UserTest constructor comment.
 * @param testName java.lang.String
 */
public UserTest(String testName) {
	super(testName);
}
	/**
	 * Instantiate and persist a new User.
	 * If successful, set _id to the new User's id.
	 */
	private void createUser()
	{
		try
		{
			final User user = User.createNewUser( "bork@bork.bork", getContext() );
			user.save();

			_id = user.getId();
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
	private void deleteUser()
	{
		try
		{
			getContext().getCache().remove( _id );

			final User user = getUser();
			user.deleteHard();
		}
		catch( RuntimeSQLException e )
		{
			fail();
		}

		try
		{
			getUser();
			fail( "User not deleted." );
		}
		catch( RuntimeSQLException e ) {}
	}
	private User getUser()
	{
		return User.getUser( _id, getContext() );
	}
	/**
	 * Remove User from cache to force a load from the database.
	 * Load, modify, and save the user.
	 */
	private void modifyUser()
	{
		try
		{
			getContext().getCache().remove( _id );

			final User user = getUser();

			user.setFirstName( "Hey" );
			user.setLastName( "You" );
			user.save();
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
		createUser();
		modifyUser();
		deleteUser();
	}
}
