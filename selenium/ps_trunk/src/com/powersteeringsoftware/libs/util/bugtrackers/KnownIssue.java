package com.powersteeringsoftware.libs.util.bugtrackers;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.settings.CoreProperties;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 28.12.2010
 * Time: 14:39:37
 */
public class KnownIssue implements Comparable<KnownIssue> {

    private static final int SUBJECT_LENGTH = 60;
    private static final long COLLECT_TIMEOUT = 20 * 60 * 1000L;
    private static final int COLLECTION_ATTEMPTS_NUMBER = 3;
    private static final long COLLECTION_ATTEMPTS_TIMEOUT = 5 * 60 * 1000L;

    private String id;
    private String subject;
    private String url;
    private boolean isClosed;
    private boolean isJira;
    private int count;
    private Object[] info;
    private Set<String[]> methods = new LinkedHashSet<String[]>();

    private static List<KnownIssue> knises = new ArrayList<KnownIssue>();

    private KnownIssue(String id) {
        this.id = id;
        if (id.startsWith(CoreProperties.getJiraIssuePrefix())) {
            isJira = true;
            url = CoreProperties.getJiraIssueUrlPrefix() + id;
        } else {
            url = CoreProperties.getInternalIssueUrlPrefix() + id;
        }
    }

    boolean isJira() {
        return isJira;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    void setSubject(String subject) {
        this.subject = subject;
    }

    void setIsClosed(boolean b) {
        isClosed = b;
    }


    public static KnownIssue addToKnises(String knis, String testClass, String testMethod) {
        KnownIssue res = addToList(knis);
        res.addTestMethod(testClass, testMethod);
        return res;
    }


    public static KnownIssue addToList(String knis) {
        KnownIssue k = new KnownIssue(knis);
        int index = knises.indexOf(k);
        if (index == -1) {
            knises.add(k);
            return k;
        } else {
            return knises.get(index);
        }
    }

    private void addTestMethod(String testClass, String testMethod) {
        addTestMethod(new String[]{testClass, testMethod});
    }

    private void addTestMethod(String[] test) {
        methods.add(test);
        count++;
    }

    public void setTestMethod() {
        addTestMethod(searchClassAndMethod());
    }

    public boolean equals(Object o) {
        return o != null && o instanceof KnownIssue && id.equals(((KnownIssue) o).id);
    }

    public int hashCode() {
        return id.hashCode();
    }

    private static boolean hasNotCollectedInfo() {
        if (knises.size() == 0) return false;
        for (KnownIssue kn : knises) {
            if (kn.subject == null) return true;
        }
        return false;
    }

    private static String[] searchClassAndMethod() {
        class ExceptionToSearch extends RuntimeException {
        }
        try {
            throw new ExceptionToSearch();
        } catch (ExceptionToSearch e) {
            StackTraceElement[] sts = e.getStackTrace();
            for (int i = 0; i < sts.length; i++) {
                if (sts[i].isNativeMethod() && i > 0) {
                    return new String[]{sts[i - 1].getClassName(), sts[i - 1].getMethodName()};
                }
            }
        }
        return null;
    }

    public static synchronized void collectInfo() {
        if (!hasNotCollectedInfo()) return;
        BugTracker[] trackers = new BugTracker[]{new Internal(knises), new Jira(knises)};
        for (BugTracker tr : trackers) {
            if (!tr.hasKnises()) continue;
            PSLogger.info("Collect bugs info from " + tr.getUrl());
            try {
                tr.initZeroDriver();
            } catch (Exception e) {
                PSLogger.fatal(e);
                continue;
            }
            try {
                for (int i = 0; i < COLLECTION_ATTEMPTS_NUMBER; i++) {
                    try {
                        _collectInfo(tr);
                        break;
                    } catch (Throwable e) {
                        PSLogger.error("attempt #" + (i + 1));
                        PSLogger.fatal(e);
                        try {
                            Thread.sleep(COLLECTION_ATTEMPTS_TIMEOUT);
                        } catch (InterruptedException e1) {
                            // ignore
                        }
                    }
                }
            } finally {
                SeleniumDriverFactory.stopLastSeleniumDriver();
            }
        }
    }

    private static void _collectInfo(final BugTracker tracker) throws Throwable {
        final Throwable[] exceptions = new Throwable[1];
        Thread th = new Thread() {
            public void run() {
                try {
                    tracker.doLogin();
                    for (KnownIssue kn : tracker.getKnises()) {
                        try {
                            tracker.openIssue(kn);
                            PSLogger.debug("Subject for bug #" + kn.id + " is '" + kn.subject + "'");
                        } catch (Exception ex) {
                            PSLogger.warn(ex);
                        }
                    }
                } catch (Throwable th) {
                    exceptions[0] = th;
                }
            }
        };
        long start = System.currentTimeMillis();
        th.start();
        while (th.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // exception
            }
            if (System.currentTimeMillis() - start > COLLECT_TIMEOUT) {
                PSLogger.error("Can't collect info during " + COLLECT_TIMEOUT + " ms");
                break;
            }
        }
        if (exceptions[0] != null) throw exceptions[0];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Object ob : info()) {
            if (ob instanceof Object[]) {
                for (Object ob2 : (Object[]) ob) {
                    sb.append(ob2).append(" ");
                }
            } else {
                sb.append(ob).append(" ");
            }
        }
        return sb.substring(0, sb.length() - 1);
    }

    private Object[] info() {
        if (this.info != null) return this.info;
        StringBuffer sub;
        if (subject == null) {
            sub = new StringBuffer();
        } else {
            sub = new StringBuffer("[");
            if (subject.length() > SUBJECT_LENGTH) {
                sub.append(subject.substring(0, SUBJECT_LENGTH - 3)).append("...");
            } else {
                sub.append(subject);
            }
            while (sub.length() <= SUBJECT_LENGTH) {
                sub.append(" ");
            }
            sub.append("]");
        }
        Object[] info = {getCount() > 1 ? "(" + getCount() + ") " : "", isClosed ? "(Closed)" : ""};
        Object[] link = new Object[2];
        link[0] = url;
        link[1] = sub.toString();
        return this.info = new Object[]{id, link, info};
    }

    private int getCount() {
        return methods.isEmpty() ? 1 : count;
    }

    public static List<KnownIssue> getKnownIssues() {
        return knises;
    }

    public static Object[][] getInfo() {
        Object[][] res = new Object[knises.size()][];
        for (int i = 0; i < knises.size(); i++) {
            res[i] = knises.get(i).info();
        }
        return res;
    }

    public static void addToKnises(List<List> was) {
        List<KnownIssue> toAdd = new ArrayList<KnownIssue>();
        for (List list : was) {
            String id = String.valueOf(list.get(0));
            KnownIssue kn = new KnownIssue(id);
            toAdd.add(kn);
            kn.info = new Object[3];
            kn.info[0] = kn.id;
            kn.info[1] = list.get(1);
            kn.info[2] = list.get(2);
            String count = String.valueOf(((Object[]) kn.info[2])[0]).replace("(", "").replace(")", "").trim();
            if (count.isEmpty()) {
                kn.count = 1;
            } else {
                kn.count = Integer.parseInt(count);
            }
        }
        for (KnownIssue kn : toAdd) {
            int index = knises.indexOf(kn);
            if (index != -1) {
                knises.get(index).count = knises.get(index).getCount() + kn.count;
            } else {
                knises.add(kn);
            }
        }
    }

    public static StringBuilder getReport() {
        StringBuilder res = new StringBuilder();
        for (KnownIssue kn : knises) {
            String sub;
            if (kn.subject == null) {
                sub = "";
            } else {
                sub = "[" + kn.subject + "]";
            }
            res.append(kn.url).
                    append(" ").append(sub).
                    append(" ").append(kn.isClosed ? "(Closed)" : "").
                    append("\n");
            for (String[] mt : kn.methods) {
                if (mt != null)
                    res.append("\t").append(mt[0]).append(".").append(mt[1]).append("\n");
            }
        }
        return res;
    }

    @Override
    public int compareTo(KnownIssue o) {
        if (isJira == o.isJira) return id.compareTo(o.id);
        return isJira ? 1 : -1;
    }
}
