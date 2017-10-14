package com.powersteeringsoftware.libs.util.session;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.objects.metrics.MetricInstance;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.objects.resources.RateCode;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.powersteeringsoftware.libs.util.session.TestSession.Keys.*;


public final class TestSession {

    private static HashMap<String, Object> params = new HashMap<String, Object>(100);

    static {
        PSTestData.putGeneralObjects();
    }


    public static void putObject(Keys key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "Session parameter name can't be null or blank.");
        }
        putObject(key.getKey(), value);
    }

    public static void putObject(String key, Object value) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException(
                    "Session parameter name can't be null or blank.");
        }
        if (value == null) {
            throw new IllegalArgumentException("Parameter can't be null.");
        }
        params.put(key, value);
    }


    public static Object getObject(Keys key) {
        return getObject(key.getKey());
    }

    public static Object getObject(String key) {
        if (!params.containsKey(key)) {
            throw new TestSessionException("There is no object with name " + key + " in the session");
        }
        return params.get(key);
    }

    public static boolean isObjectNull(Keys key) {
        return null == params.get(key.getKey());
    }

    private static boolean isObjectString(Keys key) {
        return params.get(key.getKey()) instanceof String;
    }

    public static PowerSteeringVersions getAppVersion() {
        String version = (String) getObject(APPLICATION_VERSION_NUM);
        PowerSteeringVersions result = null;
        try {
            result = PowerSteeringVersions.valueOf(version);
        } catch (Exception e) {
            Assert.fail("Can not determine the application version: '" + version + "'");
        }
        return result;
    }

    public static boolean isVersionPresent() {
        return !isObjectNull(APPLICATION_VERSION_NUM);
    }

    public static String getFullVersion() {
        StringBuilder res = new StringBuilder();
        if (!isObjectNull(APPLICATION_VERSION))
            res.append(getObject(APPLICATION_VERSION));
        else res.append("Unknown Version");
        res.append(" ").append("-").append(" ");
        if (!isObjectNull(DB_VERSION))
            res.append(getObject(DB_VERSION));
        else res.append("Unknown DBVersion");
        return res.toString();
    }

    public static String getApplicationVersionAsString() throws ObjectSessionException {
        if (isObjectNull(APPLICATION_VERSION)
                || !isObjectString(APPLICATION_VERSION)) {
            throw new ObjectSessionException("Object with name " + APPLICATION_VERSION.getKey() + " is empty, null or not a String");
        }

        return (String) getObject(APPLICATION_VERSION);
    }

    public synchronized static User putUser(User user) {
        return (User) putObjectToList(USER_LIST, user);
    }

    public synchronized static List<User> getAllUsers() {
        List users = getList(USER_LIST);
        PSLogger.debug("TestSession. all users: " + users);
        return users;
    }


    private static synchronized ConfigObject putObjectToList(Keys key, ConfigObject ob) {
        List list = getSet(key);
        int index = list.indexOf(ob);
        if (index == -1)
            list.add(ob);
        else {
            ConfigObject old = (ConfigObject) list.get(index);
            old.merge(ob);
            ob = old;
        }
        return ob;
    }

    private static void removeObjectFromList(Keys key, Object ob) {
        getSet(key).remove(ob);
    }

    private static List getSet(Keys key) {
        if (!params.containsKey(key.getKey())) {
            params.put(key.getKey(), new ArrayList());
        }
        return (List) params.get(key.getKey());
    }

    private static List getList(Keys key) {
        return new ArrayList(getSet(key));
    }

    public static void putWork(Work work) {
        putObjectToList(WORK_LIST, work);
    }

    public static void removeWork(Work work) {
        removeObjectFromList(WORK_LIST, work);
    }

    public static List<Work> getWorkList() {
        return getList(WORK_LIST);
    }

    public static void putWorkTemplate(Template tmp) {
        putObjectToList(WORK_TEMPLATE_LIST, tmp);
    }

    public static void removeWorkTemplate(Template tmp) {
        removeObjectFromList(WORK_TEMPLATE_LIST, tmp);
    }

    public static void putMeasure(Measure tmp) {
        putObjectToList(MEASURE_LIST, tmp);
    }

    public static void removeMeasure(Measure tmp) {
        removeObjectFromList(MEASURE_LIST, tmp);
    }

    public static List<Measure> getMeasuresList() {
        return getList(MEASURE_LIST);
    }

    public static List<Template> getWorkTemplateList() {
        return getList(WORK_TEMPLATE_LIST);
    }


    public static void putTimesheets(Timesheets tmp) {
        List list = getSet(TIMESHEETS_LIST);
        int index = list.indexOf(tmp);
        if (index != -1)
            list.remove(tmp);
        list.add(tmp);
    }

    public static void removeTimesheets(Timesheets t) {
        removeObjectFromList(TIMESHEETS_LIST, t);
    }

    public static List<Timesheets> getTimesheets() {
        List<Timesheets> res = getList(TIMESHEETS_LIST);
        Collections.sort(res);
        return res;
    }

    public static void putMetric(MetricInstance metric) {
        putObjectToList(METRIC_LIST, metric);
    }

    public static void removeMetric(MetricInstance metric) {
        removeObjectFromList(METRIC_LIST, metric);
    }

    public static List<MetricInstance> getMetricList() {
        return getList(METRIC_LIST);
    }

    public static void putProcess(PSProcess p) {
        putObjectToList(PROCESSES_LIST, p);
    }

    public static void removeProcess(PSProcess p) {
        removeObjectFromList(PROCESSES_LIST, p);
    }

    public static List<PSProcess> getProcessList() {
        return getList(PROCESSES_LIST);
    }

    public static void putMetricTemplate(MetricTemplate tmp) {
        putObjectToList(METRIC_TEMPLATE_LIST, tmp);
    }

    public static void removeMetricTemplate(MetricTemplate tmp) {
        removeObjectFromList(METRIC_TEMPLATE_LIST, tmp);
    }

    public static List<MetricTemplate> getMetricTemplateList() {
        return getList(METRIC_TEMPLATE_LIST);
    }

    public static void putTag(PSTag tmp) {
        putObjectToList(TAG_LIST, tmp);
    }

    public static List<PSTag> getTagList() {
        return getList(TAG_LIST);
    }

    public static void removeTag(PSTag tmp) {
        removeObjectFromList(TAG_LIST, tmp);
    }


    public static PSPermissions.AllSet getPermissions() {
        if (isObjectNull(Keys.PERMISSIONS)) {
            PSPermissions.AllSet res = PSTestData.getPermissions();
            if (res == null) res = new PSPermissions.AllSet();
            putObject(PERMISSIONS, res);
            return res;
        }
        return (PSPermissions.AllSet) getObject(PERMISSIONS);
    }

    public static void setUixFeature(UixFeature u) {
        List list = getSet(UIX_FEATURES);
        int index = list.indexOf(u);
        if (index == -1)
            list.add(u);
        else {
            list.remove(index);
            list.add(index, u);
        }
    }

    public static List<UixFeature> getUixFeatures() {
        return getList(UIX_FEATURES);
    }

    public static void putCurrency(Currency tmp) {
        putObjectToList(CURRENCY, tmp);
    }

    public static List<Currency> getCurrencyList() {
        return getList(CURRENCY);
    }

    public static PSTag.Activity getActivity() {
        return (PSTag.Activity) getObject(ACTIVITY);
    }

    public static PSTag.ActivityTypes getActivityTypes() {
        return (PSTag.ActivityTypes) getObject(ACTIVITY_TYPES);
    }

    public static void putRateTable(RateTable tmp) {
        putObjectToList(RATE_TABLE_LIST, tmp);
    }

    public static List<RateTable> getRateTableList() {
        return getList(RATE_TABLE_LIST);
    }

    public static List<RateTable> getRealRateTableList() {
        List<RateTable> res = new ArrayList<RateTable>();
        for (RateTable rt : getRateTableList()) {
            if (!rt.isFake()) res.add(rt);
        }
        return res;
    }

    public static RateTable getDefaultRateTable() {
        for (RateTable rt : getRealRateTableList()) {
            if (rt.isDefault()) return rt;
        }
        return null;
    }

    public static void removeRateTable(RateTable tmp) {
        if (!tmp.isFake())
            removeObjectFromList(RATE_TABLE_LIST, tmp);
    }

    public static void putRole(ConfigurableRole tmp) {
        putObjectToList(CONFIGURABLE_ROLE_LIST, tmp);
    }

    public static List<Role> getRoleList() {
        List<Role> res = new ArrayList<Role>();
        res.addAll(BuiltInRole.getList());
        res.addAll(getList(CONFIGURABLE_ROLE_LIST));
        return res;
    }

    public static void removeRole(ConfigurableRole tmp) {
        removeObjectFromList(CONFIGURABLE_ROLE_LIST, tmp);
    }

    public static List<WorkType> getWorkTypeList() {
        List<WorkType> res = new ArrayList<WorkType>();
        res = new ArrayList<WorkType>();
        res.addAll(Work.getBuiltInTypes());
        res.addAll(getWorkTemplateList());
        return res;
    }

    public static void putRateCode(RateCode tmp) {
        putObjectToList(RATE_CODE_LIST, tmp);
    }

    public static List<RateCode> getRateCodeList() {
        return getList(RATE_CODE_LIST);
    }

    public static void removeRateCode(RateCode tmp) {
        removeObjectFromList(RATE_CODE_LIST, tmp);
    }

    public static void putResourcePool(ResourcePool tmp) {
        putObjectToList(RESOURCE_POOL_LIST, tmp);
    }

    public static List<ResourcePool> getResourcePoolList() {
        return getList(RESOURCE_POOL_LIST);
    }

    public static void removeResourcePool(ResourcePool tmp) {
        removeObjectFromList(RESOURCE_POOL_LIST, tmp);
    }
    public static enum Keys {

        /**
         * Version of the tested application.
         */
        APPLICATION_VERSION("app.version"),
        APPLICATION_VERSION_NUM("app.version.num"),
        DB_VERSION("db.version"),
        PERMISSIONS("permissions"),

        USER_LIST("user.list"),
        WORK_LIST("work.list"),
        WORK_TEMPLATE_LIST("work.template.list"),
        METRIC_LIST("metric.list"),
        METRIC_TEMPLATE_LIST("metric.template.list"),
        MEASURE_LIST("measure.list"),
        TAG_LIST("tag.list"),
        TIMESHEETS_LIST("timesheets.list"),
        PROCESSES_LIST("processes.list"),

        UIX_FEATURES("uix.features"),

        CONFIGURABLE_ROLE_LIST("role.list"),
        CURRENCY("currency.list"),
        RATE_TABLE_LIST("rate.table.list"),
        RATE_CODE_LIST("rate.code.list"),
        RESOURCE_POOL_LIST("resource.pool.list"),
        ACTIVITY("activity"),
        ACTIVITY_TYPES("activity.types"),

        LABOR_COSTS("labor.costs"),;

        String key;

        Keys(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }

    }
}

