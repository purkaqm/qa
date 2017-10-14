package com.powersteeringsoftware.libs.tests.core;

import org.testng.SkipException;

import com.powersteeringsoftware.libs.logger.PSLogger;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 02.02.11
 * Time: 13:32
 */
public class PSSkipException extends SkipException {

    public PSSkipException(String skipMessage, Throwable cause) {
        super(skipMessage, cause);
    }

    public static void skip(Object message) {
        PSLogger.skip(message);
        throw new SkipException(message.toString());
    }
}
