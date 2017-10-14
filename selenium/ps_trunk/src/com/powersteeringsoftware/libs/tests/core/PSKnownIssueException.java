package com.powersteeringsoftware.libs.tests.core;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.06.2010
 * Time: 13:32:22
 */
public class PSKnownIssueException extends RuntimeException {
    private Object knis;
    private boolean isCritical = true;

    /**
     * @param knownIssue
     * @param t
     * @param isCritical - if true exception is critical and testcase will not rerun.
     */
    public PSKnownIssueException(int knownIssue, Throwable t, boolean isCritical) {
        this(knownIssue, t);
        this.isCritical = isCritical;
    }

    public PSKnownIssueException(Object knownIssue, Throwable t) {
        super(t);
        knis = knownIssue;
        PSTestListener.setLastKnis(this);
    }

    public PSKnownIssueException(int knownIssue) {
        super();
        knis = knownIssue;
        PSTestListener.setLastKnis(this);
    }

    public PSKnownIssueException(String knownIssue, String message) {
        super(message);
        knis = knownIssue;
        PSTestListener.setLastKnis(this);
    }

    public PSKnownIssueException(int knownIssue, String message) {
        super(message);
        knis = knownIssue;
        PSTestListener.setLastKnis(this);
    }


    public String getKnis() {
        return String.valueOf(knis);
    }

    public boolean isCritical() {
        return isCritical;
    }
}
