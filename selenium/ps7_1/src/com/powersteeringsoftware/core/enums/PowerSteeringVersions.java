package com.powersteeringsoftware.core.enums;

/**
 * Supported versions of PowerSteering
 *
 *
 */
public enum PowerSteeringVersions {

	APPLICATION_VERSION_7_0("7.0"),
	APPLICATION_VERSION_7_1("7.1");

	String version;

	PowerSteeringVersions(String _version){
		version = _version;
	}

	public String getVersion() {
		return version;
	}


}
