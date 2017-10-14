package com.cinteractive.ps3.test;

import com.cinteractive.ps3.PSHome;
import com.cinteractive.ps3.contexts.InstallationContext;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.extensions.TestSetup;

/**
 * Utility methods for (one-time) PSHome initialization,
 * setting a (single) context name available to base classes.
 */
public abstract class TestPS extends TestCase
{
	public static final String PS_HOME_PROP      = "ps3.home";
	public static final String CONTEXT_NAME_PROP = "ps3.context.name";

	private static String g_contextName;
	private static InstallationContext g_context;


	protected TestPS( String name ) { super( name ); }


	public static String getContextName()
	{
		if( g_contextName == null )
			throw new IllegalStateException( "Context name (system property " + CONTEXT_NAME_PROP + ") not set." );
		else
			return g_contextName;
	}

	public static Test setUpHome( Test test )
	{
		return new TestSetup( test )
		{
			public void setUp()
			throws Exception
			{
				super.setUp();

				final PSHome home = new PSHome(System.getProperty(PS_HOME_PROP));
				home.init();
				PSHome.setInstance(home, false);

				g_contextName = System.getProperty( CONTEXT_NAME_PROP );
                                g_context = InstallationContext.get(getContextName());
			}

			public void tearDown()
			throws Exception
			{
				super.tearDown();
				g_contextName = null;
			}
		};
	}

        protected InstallationContext getContext() {
            return g_context;
        }
}
