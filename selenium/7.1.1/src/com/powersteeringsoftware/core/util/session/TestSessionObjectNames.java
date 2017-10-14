package com.powersteeringsoftware.core.util.session;

public enum TestSessionObjectNames {

	/**
	 * Version of the tested application.
	 */
	APPLICATION_VERSION("app.version"),

	GATED_PROJECT_ASAP("gated.project.asap"),

	PROCESS_CDDDRC("process.cdddrc"),

	PROCESS_DMAIC("process.dmaic"),

	TIMESHEET_SUBMITED("timesheet.submited");

	String objectKey;

	TestSessionObjectNames(String _objectName){
		objectKey = _objectName;
	}

	public String getObjectKey() {
		return objectKey;
	}

}
