package com.powersteeringsoftware.mail;

import com.powersteeringsoftware.libs.logger.PSLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Class for working with files that contain report about test results. Test
 * result files must be properties files with properties as constants:<br>
 */
public enum PSResultProperties {
    MAIL_RECEIVER("mail.address.to"),
    MAIL_SENDER("mail.address.from"),
    MAIL_HOST("mail.host"),
    MAIL_PROPERTIES_PREFIX("mail.properties.prefix"),
    MAIL_PROPERTIES_BACK("mail.properties.back"),

    BODY_TYPE("body.type"),
    BODY_TEST_NAME("body.test.name"),
    BODY_DATE_FORMAT("body.date.format"),
    BODY_TEST_RESULT_OK("body.test.result.ok"),
    BODY_TEST_RESULT_FAILED("body.test.result.failed"),
    BODY_TEST_LOADING_HEADER("body.test.loading.header"),
    BODY_TEST_LOADING_TABLE("body.test.loading.table"),

    SUBJECT_RESULT("subject.result"),
    SUBJECT_SUCCESS("subject.success"),
    SUBJECT_FAIL("subject.fail"),
    SUBJECT_NOTHING("subject.nothing"),
    SUBJECT_CRASH("subject.crash"),
    SUBJECT_LOADING("subject.loading"),
    SUBJECT_DATE_FORMAT("subject.date.format"),
    SUBJECT_PREFIX("subject.prefix"),
    SUBJECT_SEPARATOR_LEFT("subject.separator.left"),
    SUBJECT_SEPARATOR_RIGHT("subject.separator.right"),

    TEST_NAME("test.name"),
    TEST_RESULT("test.result"),
    TEST_TEST_CASE_SKIPPED_NUMBER("test.test-case.skipped.number"),
    TEST_TEST_CASE_PASSED_NUMBER("test.test-case.passed.number"),
    TEST_TEST_CASE_FAILED_NUMBER("test.test-case.failed.number"),
    TEST_TEST_CASE_ALL_NUMBER("test.test-case.all.number"),
    TEST_SHORT_DESCRIPTION("test.short.description"),
    TEST_ALL_TESTS_INFO("test.all.tests"),
    TEST_FAILED_TEST_CASES_INFO("test.failed.test_cases"),

    TEST_KNOWN_ISSUE("test.known.issue"),
    TEST_ROOT_LOGS_DIR("test.root.logs"),
    TEST_LOGS_FILE("test.logs"),
    TEST_LOGS_TEST_NG_FILE("test.logs.test-ng"),
    TEST_START_LONG("test.start.long"),
    TEST_END_LONG("test.end.long"),
    TEST_LOADING_REFRESH_TIMEOUT("test.loading.refresh-timeout"),

    ENVIRONMENT_TEST_CORE_VERSION("environment.test.core.version"), // selenium version.
    ENVIRONMENT_TEST_URL_BASE("environment.test.url.base"), // hudson base url
    ENVIRONMENT_TEST_URL("environment.test.url"), // hudson url (second part)
    ENVIRONMENT_TEST_URL_JOB_NAME("environment.test.url.job.name"), // hudson job name
    ENVIRONMENT_TEST_URL_JOB_ID("environment.test.url.job.id"), // hudson job id
    ENVIRONMENT_TESTS_LIST("environment.tests.list"), // common tests jsp

    ENVIRONMENT_PS_VERSION("environment.ps.version"),
    ENVIRONMENT_PS_VERSION_SHORT("environment.ps.version.short"),
    ENVIRONMENT_CLIENT_BROWSER("environment.client.browser"),
    ENVIRONMENT_CLIENT_BROWSER_SHORT("environment.client.browser.short"),
    ENVIRONMENT_CLIENT_BROWSER_BRAND("environment.client.browser.brand"),
    ENVIRONMENT_SERVER_URL("environment.server.url"),
    ENVIRONMENT_SERVER_CONTEXT("environment.server.context"),
    ENVIRONMENT_CLIENT_HOST("environment.client.host"),
    ENVIRONMENT_CLIENT_USER("environment.client.user");

    private String key;
    private static Properties properties;
    private static File _thisFile;

    private static Type type;

    PSResultProperties(String key) {
        this.key = key;
    }

    public String getValue() {
        if (properties == null) return null;
        return properties.containsKey(key) ? properties.getProperty(key) : "";
    }

    public int getInteger() {
        String val = getValue();
        if (val == null || val.isEmpty() || !val.matches("\\d+")) return 0;
        return Integer.parseInt(val);
    }

    /**
     * @return List of Lists of String or Array
     */
    public List<List> getListListValues() {
        if (properties == null) return null;
        List<List> res = new ArrayList<List>();
        int i = 0;
        while (properties.containsKey(key + "_" + i + "_0") || properties.containsKey(key + "_" + i + "_0_0")) {
            res.add(getListValues(key + "_" + i++));
        }
        return res;
    }

    /**
     * @return List of Objects (String or Array)
     */
    public List getListValues() {
        return getListValues(key);
    }

    private List getListValues(String key) {
        if (properties == null) return null;
        List res = new ArrayList();
        int i = 0;
        while (properties.containsKey(key + "_" + i) || properties.containsKey(key + "_" + i + "_0")) {
            Object prop = properties.getProperty(key + "_" + i);
            if (prop == null) {
                int k = 0;
                List array = new ArrayList();
                while (properties.containsKey(key + "_" + i + "_" + k)) {
                    array.add(properties.getProperty(key + "_" + i + "_" + k++));
                }
                prop = array.toArray();
            }
            res.add(prop);
            i++;
        }
        return res;
    }

    private static void setValue(String key, String value) {
        if (properties == null) return;
        if (value == null) {
            PSLogger.debug("Can't store " + key + " to mail.properties");
            return;
        }
        PSLogger.debug("Set value " + key + "=" + value);
        properties.setProperty(key, value);
    }

    public void setValue(String value) {
        setValue(key, value);
    }

    public void setValue(PSResultProperties value) {
        setValue(key, value.getValue());
    }

    public void putValue(Object value) {
        String res;
        if ((res = getValue()) == null || res.isEmpty())
            setValue(value);
    }

    public void appendValue(int val) {
        setValue(getInteger() + val);
    }

    public void appendValue(String val2) {
        String val = getValue();
        if (val == null || val2 == null) {
            return;
        }
        if (!val.isEmpty() && !val.equals(val2)) {
            val2 = val + "; " + val2;
        }
        setValue(val2);
    }


    public void setValue(Object value) {
        setValue(key, String.valueOf(value));
    }

    public void setValues(List<String> values) {
        for (int i = 0; i < values.size(); i++) {
            setValue(key + "_" + i, values.get(i));
        }
    }

    public void setValues(Object... values) {
        if (properties == null) return;
        for (int i = 0; i < values.length; i++) {
            setValue(key + "_" + i, String.valueOf(values[i]));
        }
    }

    public void addValues(Object... values) {
        if (properties == null) return;
        int i;
        for (i = 0; ; i++) {
            if (!properties.containsKey(key + "_" + i + "_0") && !properties.containsKey(key + "_" + i + "_0_0")) break;
        }
        for (int j = 0; j < values.length; j++) {
            Object v = values[j];
            if (v instanceof Object[]) {
                Object[] array = (Object[]) v;
                for (int k = 0; k < array.length; k++) {
                    setValue(key + "_" + i + "_" + j + "_" + k, String.valueOf(array[k]));
                }
            } else {
                setValue(key + "_" + i + "_" + j, String.valueOf(v));
            }
        }
    }

    public void setValues(Object[][] vals) {
        clear();
        for (int i = 0; i < vals.length; i++) {
            for (int j = 0; j < vals[i].length; j++) {
                if (vals[i][j] instanceof Object[]) {
                    Object[] array = (Object[]) vals[i][j];
                    for (int k = 0; k < array.length; k++) {
                        setValue(key + "_" + i + "_" + j + "_" + k, String.valueOf(array[k]));
                    }
                } else {
                    setValue(key + "_" + i + "_" + j, String.valueOf(vals[i][j]));
                }
            }
        }
    }

    private void clear() {
        if (properties == null) return;
        for (String name : properties.stringPropertyNames()) {
            if (name.startsWith(key)) {
                properties.remove(name);
            }
        }
    }

    public static Properties getProperties() {
        if (properties == null)
            properties = new Properties();
        return properties;
    }

    public enum Type {
        HTML,
        SHORT_HTML,
        SINGLE_STRING,
        SIMPLE,
        LOADING,;

        public boolean isHtmlType() {
            return equals(HTML) || equals(SHORT_HTML) || equals(LOADING);
        }

        public boolean isLoading() {
            return equals(LOADING);
        }

        public static Type parse(String sType) {
            if (sType == null) return SIMPLE;
            if (sType.matches("\\d+")) {
                switch (Integer.parseInt(sType)) {
                    case 1:
                        return HTML;
                    case 2:
                        return SHORT_HTML;
                    case 3:
                        return SINGLE_STRING;
                    case 4:
                        return LOADING;
                    default:
                        return SIMPLE;
                }
            }
            if (HTML.toString().equals(sType)) return HTML;
            if (SHORT_HTML.toString().equals(sType.replace("-", "_"))) return SHORT_HTML;
            if (SINGLE_STRING.toString().equals(sType)) return SINGLE_STRING;
            return SIMPLE;
        }

        public String toString() {
            return name().toLowerCase();
        }
    }


    public String getSurroundedValue() {
        String val = getValue();
        if (val == null || val.isEmpty()) return "";
        if (val.startsWith(SUBJECT_SEPARATOR_LEFT.getValue()) && val.endsWith(SUBJECT_SEPARATOR_RIGHT.getValue()))
            return val;
        StringBuilder sb = new StringBuilder(SUBJECT_SEPARATOR_LEFT.getValue());
        for (String part : val.split(",")) {
            sb.append(part).append(SUBJECT_SEPARATOR_RIGHT.getValue()).append(SUBJECT_SEPARATOR_LEFT.getValue());
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static void load(String file) throws IOException {
        load(new File(file));
    }

    public static void load(File file) throws IOException {
        PSLogger.debug("Load mail properties from " + file.getAbsolutePath());
        _thisFile = file;
        if (!file.exists()) {
            PSLogger.warn("Can't find file to load properties");
            return;
        }
        Reader reader = new FileReader(_thisFile);
        getProperties().load(reader);
        reader.close();
    }

    public static void save(String file) throws IOException {
        PSLogger.debug("Save properties to file " + file);
        Writer writer = new FileWriter(new File(file));
        getProperties().store(writer, "New properties instance");
        writer.close();
    }

    public static Type getType() {
        if (type == null && properties != null)
            type = properties.containsKey(BODY_TYPE.key) ? Type.parse(properties.getProperty(BODY_TYPE.key)) : Type.SHORT_HTML;
        return type;
    }

    public static void setType(String str) {
        if (str == null) throw new NullPointerException("Null email type");
        setType(Type.parse(str));
    }

    public static void setType(Type t) {
        type = t;
        BODY_TYPE.key = type.toString();
    }


    public File getFile() {
        if (_thisFile == null) return new File(getValue());
        return new File((_thisFile.getParentFile().getAbsolutePath() + File.separator + getValue()).replace("\\", File.separator));
    }

    public void setFile(File val) {
        setValue(_thisFile != null ? getRelativePath(_thisFile.getParentFile(), val) : val);
    }

    public static String getRelativePath(File base, File dst) {
        String sBase = null;
        try {
            sBase = base.getCanonicalPath();
        } catch (IOException e) {
            PSLogger.fatal(e);
        }
        String sDst = null;
        try {
            sDst = dst.getCanonicalPath();
        } catch (IOException e) {
            PSLogger.fatal(e);
        }
        return sDst.replace(sBase, "");
    }

}
