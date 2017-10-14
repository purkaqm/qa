package com.powersteeringsoftware.libs.objects;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.WorkType;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.settings.PowerSteeringVersions;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

import static com.powersteeringsoftware.libs.util.session.TestSession.getAllUsers;

/**
 * Created with IntelliJ IDEA.
 * User: zss
 * Date: 10.06.12
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConfigObject implements Cloneable {
    protected Config conf;
    protected Set<String> uniqueNodes = new HashSet<String>();
    private static final String NAME_OF_CONFIG = "NAME";
    private static final String DATE_TEMPLATE = "{time}";
    protected static final String START = "start";
    protected static final String END = "end";
    protected static final String DATE = "date";
    private Boolean exist;

    protected ConfigObject() {
        conf = Config.createConfig(getConfigName());
    }

    private Field getConfigNameField(Class c) {
        for (Field f : c.getDeclaredFields()) {
            if (f.getName().equals(NAME_OF_CONFIG)) return f;
        }
        return null;
    }

    private String getConfigName() {
        Class c = getClass();
        Field f = null;
        while (f == null && !c.equals(ConfigObject.class)) {
            f = getConfigNameField(c);
            if (f == null) c = c.getSuperclass();
        }
        if (f == null)
            throw new IllegalArgumentException("Can't find constant " + NAME_OF_CONFIG + " for " + getClass().getSimpleName());
        try {
            return (String) f.get(String.class);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

    protected ConfigObject(Config conf) {
        if (conf.getChilds().size() == 0 && conf.hasXpathAttr()) {
            Config c = conf.findChildByXpath();
            if (c == null)
                throw new IllegalArgumentException("Can't find config for " + getClass().getSimpleName() + ":" + conf.toString());
            this.conf = (Config) c.clone();
            for (Object k : conf.getAttributes().keySet()) {
                this.conf.setAttributeValue(String.valueOf(k), conf.getAttribute(String.valueOf(k)));
            }
            this.conf.removeXpathAttr();
        } else {
            this.conf = conf;
        }
        toUniqueNodes("exist");
        toUniqueNodes("id");
    }

    protected void processChildren() {
        processChildren(conf);
    }

    private static void processChildren(Config config) {
        for (Config _c : config.getChilds()) {
            processChildren(_c);
            if (!_c.hasXpathAttr()) continue;
            Config _toAdd = config.getChildByXPath(_c.getXpathAttr());
            if (_toAdd == null) {
                continue;
            }
            Config toAdd = (Config) _toAdd.clone();
            toAdd.removeXpathAttr();
            config.removeChild(_c);
            config.addChild(toAdd);
        }
    }

    public Boolean getBoolean(String name) {
        if (!conf.hasChild(name)) return null;
        return Boolean.parseBoolean(getString(name));
    }

    public Integer getInteger(String name) {
        if (!conf.hasChild(name)) return null;
        return Integer.valueOf(getString(name));
    }

    public boolean getBooleanTrue(String name) {
        return conf.getChByName(name) == null || getString(name).equals(Boolean.TRUE.toString());
    }

    public boolean getBooleanFalse(String name) {
        return Boolean.valueOf(getString(name));
    }

    /**
     * parse config like <node ps="_10_0" condition="val1|val2"/> or <node>val0</node>
     *
     * @param _c - Config
     * @return val1, val2 or val0; val2 if current version is less then specified in config ("ps")
     */
    private static String _getVersionConditionText(Config _c) {
        if (_c == null) return null;
        PowerSteeringVersions verConfig;
        PowerSteeringVersions curVer = getVersion();
        String condition = _c.getAttributeValue("condition");
        String[] arr;
        if (curVer == null || (verConfig = PowerSteeringVersions.valueOf(_c)) == null ||
                condition == null ||
                !condition.contains("|") || (arr = condition.split("\\s*\\|\\s*")).length != 2)
            return _c.getText();

        String res = curVer.verGreaterOrEqual(verConfig) ? arr[0] : arr[1];
        PSLogger.debug2("Change config " + _c + ". Set " + res + ".");
        _c.removeAttribute("condition");
        _c.removeAttribute("ps");
        _c.setText(res);
        return res;
    }

    protected synchronized String getString(String name) {
        Config _c = conf.getChByName(name);
        if (_c == null) return null;
        String res = _getVersionConditionText(_c);
        return res == null ? "" : res;
    }

    public synchronized void set(String name, Object value) {
        conf.setText(name, String.valueOf(value));
        conf.removeAttribute("condition");
        conf.removeAttribute("ps");
    }

    public Config getConfig() {
        return conf;
    }

    public boolean exist() {
        if (exist != null) return exist;
        return exist = getBooleanFalse("exist");
    }

    protected boolean hasExist() {
        return conf.hasChild("exist");
    }

    public void setCreated() {
        _setCreated();
    }

    private static boolean doSetCreationDate = true;

    public static void doSetCreationDate(boolean b) {
        doSetCreationDate = b;
    }

    protected void _setCreated() {
        exist();
        if (Boolean.TRUE.equals(exist)) return;
        set("exist", exist = Boolean.TRUE);
        if (doSetCreationDate)
            set("creation_date", System.currentTimeMillis());
        getCreationDate();
    }

    public boolean hasCreationDate() {
        return conf.hasChild("creation_date");
    }

    public Long getCreationDate() {
        if (hasCreationDate()) {
            return Long.parseLong(conf.getText("creation_date"));
        }
        return null;
    }

    public PSCalendar getPSCreationDate() {
        if (hasCreationDate()) {
            return getCalendar().set(getCreationDate());
        }
        return null;
    }

    public PSCalendar getCreationPSDate() {
        Long res = getCreationDate();
        if (res == null) return null;
        return getCalendar().set(res);
    }

    public void setDeleted() {
        if (Boolean.FALSE.equals(exist)) return;
        set("exist", exist = Boolean.FALSE);
        conf.removeChild("creation_date");
    }

    public void setAttribute(String attr, Object val) {
        conf.setAttributeValue(attr, String.valueOf(val));
    }

    public void setName(String name) {
        String name0 = getName();
        if (!name.equals(name0) && !getConfig().hasChild("name0")) {
            set("name0", name0);
        }
        set("name", name);
    }

    public String getName() {
        return getString("name");
    }

    public String getName0() {
        return getConfig().hasChild("name0") ? getString("name0") : getName();
    }

    public void setDescription(String dsc) {
        if (dsc != null)
        set("description", dsc);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setNameSuffix(long time) {
        setNameSuffix(String.valueOf(time));
    }

    public void setNameSuffix(String suffix) {
        if (getName().endsWith(suffix)) return;
        setName(getName() + "_" + suffix);
    }

    public String getInitName() {
        String res = getName();
        if (res.matches(".*_\\d+")) {
            res = res.replaceAll("(.*)_\\d+$", "$1");
        }
        return res;
    }

    protected Currency getCurrency() {
        String name = getConfig().getText(Currency.NAME);
        for (Currency c : TestSession.getCurrencyList()) {
            if (c.getName().equals(name)) return c;
        }
        throw new IllegalStateException("Config is incorrect. Can't find currency '" + name + "'");
    }

    protected void setCurrency(Currency c) {
        getConfig().setText(Currency.NAME, c.getName());
    }


    protected List<PSTag> getTagSets() {
        List<PSTag> tags = new ArrayList<PSTag>();
        for (Config c : conf.getChsByName(PSTag.NAME)) {
            tags.add(new PSTag(c));
        }
        return tags;
    }

    protected List<PSTag> getTags() {
        List<PSTag> tags = new ArrayList<PSTag>();
        for (PSTag tg : getTagSets()) {
            for (PSTag ch : tg.getAllChildren()) {
                if (!ch.hasChildren()) {
                    tags.add(ch);
                }
            }
        }
        return tags;
    }

    protected List<String> getTagNames() {
        List<String> tags = new ArrayList<String>();
        for (PSTag tg : getTags()) {
            tags.add(tg.getName());
        }
        Collections.sort(tags);
        return tags;
    }

    protected void setTags(List<PSTag> tags) {
        for (PSTag tag : tags) {
            setTag(tag);
        }
    }

    protected void setTags(PSTag... tags) {
        setTags(Arrays.asList(tags));
    }

    protected void setTag(PSTag tg) {
        List<PSTag> tags = getTags();
        if (tags.contains(tg)) return;
        addTag(tg);
    }

    protected void addTag(PSTag tg) {
        PSTag root;
        if (tg.hasParent()) {
            root = (PSTag) tg.getRoot().clone();
            List<PSTag> chain = root.getChain(tg);
            List<PSTag> chs = new ArrayList<PSTag>(root.getAllChildren());
            for (PSTag ch : chs) {
                if (chain.contains(ch)) continue;
                root.removeChild(ch);
            }
        } else {
            root = (PSTag) tg.clone();
        }
        conf.addChild(root.getConfig());
    }


    public String getId() {
        String res = getString("id");
        if (res != null && res.startsWith("U")) {
            setId(res.replace("U", ""));
        }
        return res;
    }

    public String getConfigId() {
        return getConfig().getID();
    }

    public void setId(String id) {
        set("id", id);
    }

    public String toString() {
        return (getId() != null ? "(" + getId() + ")" : "") + "[" + super.toString().replaceAll(".*@", "") + "]";
    }

    private Map<String, Config> getConfigs() {
        Map<String, Config> res = new HashMap<String, Config>();
        for (Config c : conf.getChilds()) {
            res.put(c.getName(), c);
        }
        return res;
    }

    public void merge(ConfigObject newObject) {
        merge(newObject, true);
    }

    public void merge(ConfigObject newObject, boolean doReplace) {
        Map<String, Config> _this = getConfigs();
        Map<String, Config> _new = newObject.getConfigs();
        for (String key : _new.keySet()) {
            if (_this.containsKey(key)) {
                if (doReplace) {
                    conf.removeChild(_this.get(key));
                } else {
                    continue;
                }
            }
            conf.addChild(_new.get(key));
        }
    }

    public boolean doUseTimestamp() {
        return getBooleanFalse("use-timestamp");
    }

    public void setUseTimestamp(boolean b) {
        set("use-timestamp", b);
    }

    protected String toUniqueNodes(String s) {
        uniqueNodes.add(s);
        return s;
    }

    protected void removeUniqueNodes() {
        for (String n : uniqueNodes) {
            conf.removeChild(n);
        }
    }


    protected static PowerSteeringVersions getVersion() {
        if (!TestSession.isVersionPresent()) return null;
        return TestSession.getAppVersion();
    }

    protected void removeConfigs(String name) {
        while (conf.hasChild(name)) {
            conf.removeChild(name);
        }
    }

    public Object clone() {
        Constructor ct = null;
        try {
            ct = getClass().getConstructor(Config.class);
        } catch (NoSuchMethodException e) {
            return null;
        }
        try {
            return ct.newInstance(conf.clone());
        } catch (Exception e) {
            return null;
        }
    }

    public ConfigObject copy() {
        ConfigObject res = (ConfigObject) clone();
        res.removeUniqueNodes();
        return res;
    }

    protected void setPSPermissions(PSPermissions perm) {
        getConfig().removeChild(PSPermissions.NAME);
        getConfig().addChild(perm.getConfig());
    }

    protected PSPermissions getPSPermissions() {
        if (!getConfig().hasChild(PSPermissions.NAME)) return null;
        return new PSPermissions(getConfig().getChByName(PSPermissions.NAME));
    }


    /**
     * parse date from config.
     * example of config:
     * 1) <end_date>1317909681198</end_date>    - date in long
     * 2) <end_date>02/02/2010</end_date>       - date in final format
     * 3) <end_date>{time}</end_date>           - date in long (CoreProperties.getTestTemplate())
     * 4) <start_date>{time + 5}</start_date>   - date in long (CoreProperties.getTestTemplate() + 5 days)
     *
     * @param name - name for date parameter. e.g. "start_date" for constraint
     * @return PSCalendar date in format MM/dd/yyyy (see CoreProperties)
     */
    protected PSCalendar getPSDate(String name) {
        Long ed = getDateAsLong(name);
        if (ed == null) return null;
        return getCalendar().set(ed);
    }

    protected String getDate(String name) {
        PSCalendar res = getPSDate(name);
        return res != null ? res.toString() : null;
    }

    protected Long getDateAsLong(String name) {
        Long ed = _getDateAsLong(name);
        setDate(name, ed);
        return ed;
    }

    protected Long _getDateAsLong(String name) {
        return _getDateAsLong(name, null);
    }

    /**
     * @param name
     * @param doSetToWorkDay see PSCalendar.nextWorkDate
     * @return
     */
    protected Long _getDateAsLong(String name, Boolean doSetToWorkDay) {
        String time = conf.hasChild(name) ? conf.getChByName(name).getAttributeValue("time") : null;
        if (time != null) return Long.parseLong(time);
        String txt = null;
        if ((txt = getString(name)) == null) return null;
        Long ed = null;
        if (txt.startsWith(DATE_TEMPLATE)) {
            ed = CoreProperties.getTestTemplate();
            if (txt.matches(".*\\d+.*")) {
                long days = Long.parseLong(txt.replaceAll("^[^\\d]*(\\d+)[^\\d]*$", "$1")) * 1000 * 24 * 60 * 60;
                if (txt.contains("-")) {
                    ed -= days;
                } else if (txt.contains("+")) {
                    ed += days;
                }
            }
        }
        if (txt.matches("\\d+"))
            ed = Long.parseLong(txt);

        PSCalendar c;
        if (ed != null)
            c = getCalendar().set(ed);
        else
            c = getCalendar().set(txt);
        if (doSetToWorkDay != null) c = c.nextWorkDate(doSetToWorkDay);
        if (name.contains(START)) {
            c.toStart();
        } else if (name.contains(END)) {
            c.toEnd();
        }
        ed = c.getTime();

        return ed;
    }

    protected void setDate(String name, Long ed) {
        if (ed == null) return;
        Config c = conf.getChByName(name);
        if (c == null) c = conf.addElement(name);
        c.setAttributeValue("time", String.valueOf(ed));
    }

    protected boolean hasDate(String name) {
        Config date = conf.getChByName(name);
        String txt;
        return date != null && (date.getAttribute("time").matches("\\d+") || (!(txt = date.getText()).isEmpty() && (txt.matches(CoreProperties.DEFAULT_DATE_FORMAT_PATTERN)) || txt.contains(DATE_TEMPLATE)));
    }

    protected PSCalendar getCalendar() {
        Config c = conf.getChByName(PSCalendar.NAME);
        if (c == null) return PSCalendar.getEmptyCalendar();
        return new PSCalendar(c);
    }

    public boolean canView(User user) {
        return user == null || user.isAdmin();
    }


    protected static void addWorkTypes(ConfigObject obj, WorkType... types) {
        Config _types;
        if ((_types = obj.getConfig().getChByName("types")) == null) _types = obj.getConfig().addElement("types");
        for (WorkType type : types) {
            _types.addElement("type").setText(type.getName());
        }
    }

    protected static Set<WorkType> getWorkTypes(ConfigObject obj) {
        Config types = obj.getConfig().getChByName("types");
        Set<WorkType> res = new HashSet<WorkType>();
        if (types == null) return res;
        for (Config c : types.getChilds()) {
            WorkType t = getWorkType(c.getText());
            if (t == null)
                throw new IllegalStateException("Config is incorrect. Can't find work-type'" + t + "'");
            res.add(t);
        }
        return res;
    }

    private static WorkType getWorkType(String txt) {
        for (WorkType t : TestSession.getWorkTypeList()) {
            if (txt.equals(t.getName())) return t;
        }
        return null;
    }

    protected static Config addUser(Config parent, User user) {
        String id1 = user.getId();
        String id2 = user.getConfigId();
        Config res = parent.createConfig(User.NAME);
        boolean hasIds = false;
        if (id1 != null && !id1.isEmpty()) {
            res.setText("id", id1);
            hasIds = true;
        }
        if (id2 != null && !id2.isEmpty()) {
            res.setID(id2);
            hasIds = true;
        }
        if (!hasIds) {
            res = user.getConfig();
        }
        return parent.addChild(res);
    }

    protected static User findUser(Config config) {
        if (config == null) return null;
        String id1 = config.getText("id"); // <user><id>fs000080000i2n8lbnp0000000</id></user>
        String id2 = config.getID(); //  <user role="Owner" id="default-user"/>
        for (User u : getAllUsers()) {
            if (u.getId().equals(id1) || u.getConfigId().equals(id2)) return u;
        }
        User u1 = new User(config);
        for (User u : getAllUsers()) {
            if (u.equals(u1)) return u;
        }
        return null;
    }

}
