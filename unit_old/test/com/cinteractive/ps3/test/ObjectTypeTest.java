package com.cinteractive.ps3.test;

/* Copyright � 2000 Cambridge Interactive, Inc. All rights reserved.*/

import java.sql.Connection;
import java.sql.SQLException;

import com.cinteractive.ps3.agents.system.TimeConversionAgent;
import com.cinteractive.ps3.contexts.InstallationContext;
/**
 * yori
 */
public class ObjectTypeTest
{
	public static void main( String args[] )
	{
		Connection conn = null;
		try
		{
/*
			final InstallationContext context = InstallationContext.createNewContext( "ps3", config );
			System.out.println( context.getContextName() );
*/
			final InstallationContext context = InstallationContext.get( args[ 0 ] );
			System.out.println( context.getName() );

			new TimeConversionAgent().install( context );
		}
		catch( Throwable t )
		{
			t.printStackTrace();
			if( t instanceof SQLException )
			{
				try { if( conn != null ) conn.rollback(); }
				catch( SQLException ignored ) {}
			}
		}
		finally
		{
			try { if( conn != null ) conn.close(); }
			catch( SQLException ignored ) {}
		}
	}
}
