package com.powersteeringsoftware.core.util;

/**
 * Interface presents classes that are allowed to keep html element locations
 * for using them in the Selenium methods such as type, isPresent etc
 *
 *
 */
public interface ILocatorable {

	String getLocator();
}
