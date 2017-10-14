package com.powersteeringsoftware.core.tc;

/**
 * Test case exception
 *
 * @author selyaev_ag
 *
 */
public class PSTestCaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PSTestCaseException() {
	}

	public PSTestCaseException(String s) {
		super(s);
	}

	public PSTestCaseException(String s, Throwable t) {
		super(s, t);
	}

	public PSTestCaseException(Throwable t) {
		super(t);
	}
}
