package com.powersteeringsoftware.libs.tests.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Test case exception
 *
 * @author selyaev_ag
 */
public class PSApplicationException extends RuntimeException {

    private List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
    private String msg;

    private static final long serialVersionUID = 1L;

    private static final String PATTERN = "([\\w$]+\\.){1,}\\w+\\(\\w+\\.java:\\d+\\)";


    public PSApplicationException() {
        super();
    }

    public PSApplicationException(String s) {
        super(s);
    }

    public PSApplicationException(String s, Throwable t) {
        super(s, t);
    }

    public PSApplicationException(Throwable t) {
        super(t);
    }

    public String getMessage() {
        if (msg == null) msg = super.getMessage();
        return msg;
    }

    public void setMessage(String m) {
        msg = m;
    }

    public static boolean isStackTraceMsg(String txt) {
        return txt.matches(PATTERN);
    }

    /**
     * parse string like 'ps5.wbs.logic.validation.DataHandlingError$ErrKey.createError(DataHandlingError.java:36)'
     *
     * @param txt
     */
    public void addStackTrace(String txt) {
        if (!isStackTraceMsg(txt)) return;
        String clazz = txt.replaceAll("(([\\w\\$]+\\.){1,}[\\w\\$]+)\\.\\w+\\(\\w+\\.java:\\d+\\)", "$1");
        String method = txt.replaceAll("([\\w\\$]+\\.){1,}(\\w+)\\(\\w+\\.java:\\d+\\)", "$2");
        String line = txt.replaceAll(".*:(\\d+).*", "$1");
        String file = null;
        StackTraceElement stack = new StackTraceElement(clazz, method, file, Integer.parseInt(line));
        stackTrace.add(stack);
    }

    public boolean hasOwnStackTrace() {
        return stackTrace.size() != 0;
    }

    public StackTraceElement[] getStackTrace() {
        if (stackTrace.size() != 0) {
            StackTraceElement[] res1 = super.getStackTrace();
            StackTraceElement[] res3 = Arrays.copyOf(res1, res1.length + stackTrace.size());
            for (int i = 0; i < stackTrace.size(); i++) {
                res3[res1.length + i] = stackTrace.get(i);
            }
            setStackTrace(res3);
        }
        return super.getStackTrace();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString()).append("\n");
        for (StackTraceElement s : getStackTrace()) {
            sb.append(s.toString()).append("\n");
        }
        return sb.toString();
    }

    public PSKnownIssueException toKnownIssueException(Map<String, String> toSearch) {
        for (String key : toSearch.keySet()) {
            if (containsStackTraceElement(key)) {
                return new PSKnownIssueException(toSearch.get(key), this);
            }
        }
        return null;
    }

    public boolean containsStackTraceElement(String toSearch) {
        if (getMessage() != null && getMessage().contains(toSearch)) return true;
        for (StackTraceElement s : stackTrace) {
            if (s.toString().contains(toSearch)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Created with IntelliJ IDEA.
     * User: zss
     * Date: 12.09.12
     * Time: 0:10
     * To change this template use File | Settings | File Templates.
     */
    public static class PSPermissionException extends PSApplicationException {
        public PSPermissionException(String txt) {
            super(txt);
        }
    }

    /**
     * Created by admin on 16.05.2014.
     */
    public static class PSObjectDeletedException extends PSApplicationException {
        public PSObjectDeletedException(String txt) {
            super(txt);
        }
    }

    /**
     * Created by IntelliJ IDEA.
     * User: szuev
     * Date: 17.02.12
     * Time: 16:06
     */
    public static class PSServletException extends PSApplicationException {

        private String trace;

        private PSServletException(String mess) {
            super(mess);
        }

        public Throwable fillInStackTrace() {
            return null;
        }

        public String toString() {
            if (trace != null) return trace;
            return super.toString();
        }

        public static PSServletException parse(String trace) {
            String[] aTrace = trace.split("\n");
            PSServletException ex = new PSServletException(aTrace[0]);
            ex.trace = trace;
            for (String line : aTrace) {
                String txt = line.trim().replaceAll("^at\\s", "").replaceAll("\\sat$", "");
                if (txt.isEmpty()) continue;
                ex.addStackTrace(txt);
            }
            return ex;
        }

    }
}
