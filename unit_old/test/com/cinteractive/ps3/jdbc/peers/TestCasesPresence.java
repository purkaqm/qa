package com.cinteractive.ps3.jdbc.peers;

/**
 * Title:        PS3
 * Description:  Testing of presence of test cases for the peers added in the registry
 * Copyright:    Copyright (c) 2001
 * Company:      SoftDev
 * @author
 * @version 1.0
 */

import java.util.*;
import com.cinteractive.jdbc.PeerRegistry;
import com.cinteractive.jdbc.DataSourceId;
import com.cinteractive.jdbc.StringDataSourceId;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestCasesPresence extends TestJdbcPeer {
    private final Map missings = new HashMap();

    public TestCasesPresence(String name) { super(name); }

    public static Test bareSuite() {
	final TestSuite suite = new TestSuite( "TestCasesPresence" );

	//suite.addTest( new TestCasesPresence( "doTest" ) );
	suite.addTest( new TestCasesPresence( "doTestPeerMethods" ) );
	return suite;
    }

    public void doTest() throws Exception {
	List noTesting = TestSql.getPeersWithNoTest();
	if (noTesting.isEmpty()) {
	    int count = PeerRegistry.get(StringDataSourceId.get( StringDataSourceId.MSSQL7 )).getPeersCount();
	    System.out.println("All "+count+" peers have test cases.");
	} else {
	    System.out.println("Following peers have no registered test cases:");
	    for (Iterator i = noTesting.iterator(); i.hasNext(); )
		System.out.println("    "+((String) i.next()));
	}
    }

    public void doTestPeerMethods() throws Exception {
	final Map map = TestSql.getRegisteredTests();
	for (Iterator peers = map.keySet().iterator(); peers.hasNext(); ) {
	    final String peerName = (String) peers.next();
	    final Class peer = Class.forName(peerName);
	    final String testName = (String) map.get(peerName);
	    final Class test = Class.forName(testName);
	    addMissingMethods(peer, comparePublicMethods(peer, test, null));
	}

	printReport();
    }

    private List comparePublicMethods(Class orig, Class copy, List excludeMethods) {
	final Method[] oMethods = orig.getDeclaredMethods();
	final List missing = new ArrayList();
	for ( int i = 0; i < oMethods.length; i++ ) {
	    final Method method = oMethods[i];
	    if (! ((method.getModifiers() & Modifier.PUBLIC) == Modifier.PUBLIC) ) continue;
	    if ( ! (method.getName().indexOf('$') < 0) ) continue; // some "system" method?
	    if (excludeMethods != null && excludeMethods.contains(method.getName())) continue;
	    try {
		String testName = method.getName();
		testName = "test" + testName.substring(0, 1).toUpperCase() + testName.substring(1);
		copy.getMethod(testName, null);
	    } catch (NoSuchMethodException e) {
		missing.add(method.getName());
	    }
	}
	return missing;
    }

    private void addMissingMethods(Class peer, List missingMethods) {
	if (missingMethods != null && !missingMethods.isEmpty())
	    missings.put(peer.getName(), missingMethods);
    }

    private void printReport() {
	for (Iterator peers = missings.keySet().iterator(); peers.hasNext(); ) {
	    final String peerName = (String) peers.next();
	    final List methods = (List) missings.get(peerName);
	    println("The \"" + peerName + "\" peer have public methods which have no appropriate test methods in unit test");
	    for (Iterator m = methods.iterator(); m.hasNext(); )
		println("\t" + m.next().toString());
	}
    }

    private void println(String message) {
	System.out.println(message);
    }

}

