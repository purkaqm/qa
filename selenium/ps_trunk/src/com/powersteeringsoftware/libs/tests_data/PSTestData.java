package com.powersteeringsoftware.libs.tests_data;

import com.powersteeringsoftware.libs.objects.*;
import com.powersteeringsoftware.libs.objects.Currency;
import com.powersteeringsoftware.libs.objects.measures.Measure;
import com.powersteeringsoftware.libs.objects.metrics.MetricTemplate;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.resources.RateCode;
import com.powersteeringsoftware.libs.objects.resources.RateTable;
import com.powersteeringsoftware.libs.objects.resources.ResourcePool;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.util.TimeStampName;
import com.powersteeringsoftware.libs.util.session.TestSession;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: szuev
 * Date: 16.08.12
 * Time: 17:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class PSTestData {
    protected Config conf;
    private String name;

    private static final String TESTS_PACKAGE = "tests";
    private static final String GENERAL_CONFIG = "general";
    private static Config general;

    protected static boolean useTimestamp = true;
    protected boolean useTimestampFormat;

    private static Random rand; // for random test data.
    private static List<ConfigObject> generalObjects = new ArrayList<ConfigObject>();
    protected List<ConfigObject> customObjects = new ArrayList<ConfigObject>();
    private static Map<String, Integer> usedNames = new HashMap<String, Integer>();

    static {
        CoreProperties.loadProperties();
        general = getConfig(GENERAL_CONFIG);
        rand = new Random(CoreProperties.getTestTemplate());
    }

    private static final String ROOT_PROJECT = "work0";
    private static final String ANALYSIS_PROJECT = "work1";
    private static final String DESIGN_PROJECT = "work2";
    private static final String CODING_PROJECT = "work3";
    private static final String TESTING_PROJECT = "work4";

    private static final String SGP_TEMPLATE = "sgp";
    private static final String NSGP_TEMPLATE = "nsgp";
    private static final String SGP_PROJECT = "sgp-work";
    private static final String NSGP_PROJECT = "nsgp-work";

    protected PSTestData() {
        setName();
        init();
    }

    protected PSTestData(boolean b) {
        setName();
        useTimestampFormat = b;
        init();
    }

    protected PSTestData(String name) {
        this.name = name.replaceAll("\\.xml$", "");
        init();
    }

    private void setName() {
        name = getClass().getPackage().getName().replaceAll(".*" + TESTS_PACKAGE + "\\.", "");
    }

    private void init() {
        conf = getConfig(name);
        putGeneralObjects();
        conf.addConfig(general); // this is for references.
        putObjects(); // put to test session and init if needed.
    }

    protected static Config getConfig(String name) {
        Config c = Config.getTestConfig("/" + name + ".xml", false);
        if (c == null) {
            c = Config.createConfig("config"); // empty instance
        }
        return c;
    }

    public Config getConfig() {
        return conf;
    }

    private void putObjects() {
        for (Config c : conf.getChilds()) {
            putObject(c);
        }
    }

    public static void putGeneralObjects() {
        if (!generalObjects.isEmpty()) return;
        ConfigObject.doSetCreationDate(false);
        for (Config c : general.getChilds()) {
            putGeneralObject(c);
        }
        ConfigObject.doSetCreationDate(true);
    }

    private static void putGeneralObject(Config c) {
        if (c == null)
            return;
        ConfigObject w = toObject(c);
        if (w == null) return; // not for objects...eg PSTag. ?
        w.setCreated(); // add to test session.
        generalObjects.add(w);
        if (c.getName().equals(Work.NAME)) {
            for (Work ch : ((Work) w).getAllChildren()) {
                if (ch.exist()) {
                    ch.setCreated();
                    generalObjects.add(ch);
                }
            }
        }
    }

    private static ConfigObject toObject(Config c) {
        ConfigObject w = null;
        boolean doSetCalendar = false;
        if (c.getName().equals(Work.NAME)) {
            doSetCalendar = true;
            if (c.hasChild(PSProcess.NAME) || c.getAttribute("gated").equals("true")) {
                w = new GatedProject(c);
            } else {
                w = new Work(c);
            }
        } else if (c.getName().equals(Template.NAME)) {
            w = new Template(c);
        } else if (c.getName().equals(User.NAME)) {
            doSetCalendar = true;
            w = new User(c);
        } else if (c.getName().equals(MetricTemplate.NAME)) {
            w = new MetricTemplate(c);
        } else if (c.getName().equals(Measure.NAME)) {
            w = Measure.getMeasure(c);
        } else if (c.getName().equals(Timesheets.NAME)) {
            w = new Timesheets(c);
        } else if (c.getName().equals(UixFeature.NAME)) {
            w = new UixFeature(c);
        } else if (c.getName().equals(PSTag.NAME)) {
            w = c.getID().equals("activity") ? new PSTag.Activity(c) : c.getID().equals("activity-types") ? new PSTag.ActivityTypes(c) : new PSTag(c);
        } else if (c.getName().equals(PSProcess.NAME)) {
            w = new PSProcess(c);
        } else if (c.getName().equals(PSPermissions.AllSet.NAME)) {
            w = new PSPermissions.AllSet(c);
        } else if (c.getName().equals(RateTable.NAME)) {
            w = new RateTable(c);
        } else if (c.getName().equals(ResourcePool.NAME)) {
            w = new ResourcePool(c);
        } else if (c.getName().equals(Currency.NAME)) {
            w = new Currency(c);
        } else if (c.getName().equals(RateCode.NAME)) {
            w = new RateCode(c);
        } else if (c.getName().equals(ConfigurableRole.NAME)) {
            w = new ConfigurableRole(c);
        }

        if (doSetCalendar && !c.hasChild(PSCalendar.NAME)) {
            PSCalendar calendar = getCalendar(c.getParent(), "default");
            if (calendar != null) {
                c.addChild(calendar.getConfig());
            }
        }

        return w;
    }

    private void putObject(Config c) {
        if (c == null)
            return;
        ConfigObject w = toObject(c);
        if (w == null) return;

        if (!w.exist()) { // then this object to be created (not general).
            if (w.doUseTimestamp()) {
                String _id = w.getConfig().getID();
                String name = w.getName();
                if (useTimestamp) { // use timestamp for newly created objects
                    if (useTimestampFormat) {
                        name = TimeStampName.getTimeStampedName(name);
                    } else {
                        name += "_" + CoreProperties.getTestTemplate();
                    }
                }
                if (!usedNames.containsKey(_id)) {
                    usedNames.put(_id, 0);
                } else {  // in this case this work has already been get from config.
                    int index = usedNames.get(_id) + 1;
                    name += "(" + index + ")"; // set unique name to avoid confusion
                    usedNames.put(_id, index);
                }
                w.setName(name);
                if (w instanceof User) {
                    User u = ((User) w);
                    u.setEmail(((User) w).getEmail().replace("@", CoreProperties.getTestTemplate() + "@"));
                    if (u.getFirstName() != null) {
                        u.setFirstName(u.getFirstName() + "_" + CoreProperties.getTestTemplate());
                    }
                    if (u.getLastName() != null) {
                        u.setLastName(u.getLastName() + "_" + CoreProperties.getTestTemplate());
                    }
                }
            }
            customObjects.add(w);
        } else if (!generalObjects.contains(w)) {
            w.setCreated(); // add to test session.
            generalObjects.add(w);
            if (c.getName().equals(Work.NAME)) {
                for (Work ch : ((Work) w).getAllChildren()) {
                    if (ch.exist()) {
                        ch.setCreated();
                        generalObjects.add(ch);
                    }
                }
            }
        }

    }

    public Work getWork(String id) {
        return (Work) getObject(Work.NAME, id);
    }

    public Template getTemplate(String id) {
        return (Template) getObject(Template.NAME, id);
    }

    public Measure getMeasure(String id) {
        return (Measure) getObject(Measure.NAME, id);
    }

    private ConfigObject getObject(String name, String id) {
        for (ConfigObject w : generalObjects) {
            if (isThisObject(w, name, id)) return w;
        }
        for (ConfigObject w : customObjects) {
            if (isThisObject(w, name, id)) return w;
        }
        return null;
    }

    private List<ConfigObject> getObjects(String name) {
        List<ConfigObject> res = new ArrayList<ConfigObject>();
        for (ConfigObject w : generalObjects) {
            if (w.getConfig().getName().equals(name)) res.add(w);
        }
        for (ConfigObject w : customObjects) {
            if (w.getConfig().getName().equals(name)) res.add(w);
        }
        return res;
    }


    private static boolean isThisObject(ConfigObject w, String name, String id) {
        String _id = w.getConfig().getAttribute("id");
        String _name = w.getConfig().getName();
        return _name.equals(name) && _id.equals(id);
    }

    public Work getRootWork() {
        return getWork(ROOT_PROJECT);
    }

    public Work getAnalysisWork() {
        return getWork(ANALYSIS_PROJECT);
    }

    public Work getDesignWork() {
        return getWork(DESIGN_PROJECT);
    }

    public Work getCodingWork() {
        return getWork(CODING_PROJECT);
    }

    public Work getTestingWork() {
        return getWork(TESTING_PROJECT);
    }

    public GatedProject getSGPWork() {
        return (GatedProject) getWork(SGP_PROJECT);
    }

    public GatedProject getNSGPWork() {
        return (GatedProject) getWork(NSGP_PROJECT);
    }

    public Work getFirstRootWorkWithDescendants() {
        for (Work w : getRootWork().getChildren()) {
            if (!w.exist() || !w.hasDescendants()) continue;
            return w;
        }
        return null;
    }

    public Template getSGPTemplate() {
        return getTemplate(SGP_TEMPLATE);
    }

    public Template getNSGPTemplate() {
        return getTemplate(NSGP_TEMPLATE);
    }


    public List<Currency> getCurrencies() {
        return TestSession.getCurrencyList();
    }


    private static PSCalendar getCalendar(Config conf, String calendar) {
        for (Config c : conf.getChByName("calendars").getChilds()) {
            if (c.getAttribute("name").equals(calendar)) return new PSCalendar(c);
        }
        return null;
    }

    public PSCalendar getCalendar(String calendar) {
        return getCalendar(conf, calendar);
    }

    protected PSProcess getProcess(String id) {
        return (PSProcess) getObject(PSProcess.NAME, id);
    }

    public PSProcess getDMAICProcess() {
        return getProcess("dmaic");
    }

    public PSCalendar getCalendar() {
        return getCalendar("default").set(0);
    }

    public PSCalendar getTestCalendar() {
        return getCalendar("test").set(0);
    }

    public ResourcePool getDefResourcePool() {
        return getResourcePool("def-pool");
    }

    public ResourcePool getResourcePool(String id) {
        return (ResourcePool) getObject(ResourcePool.NAME, id);
    }

    public RateTable getRateTable(String id) {
        return (RateTable) getObject(RateTable.NAME, id);
    }

    public RateCode getRateCode(String id) {
        return (RateCode) getObject(RateCode.NAME, id);
    }

    public PSTag getTag(String id) {
        return (PSTag) getObject(PSTag.NAME, id);
    }

    public PSTag getFirstTag() {
        return getTag("1");
    }

    public PSTag getSecondTag() {
        return getTag("2");
    }

    public PSTag.ActivityTypes getActivityTypes() {
        return (PSTag.ActivityTypes) getTag("activity-types");
    }

    public PSTag.Activity getActivity() {
        return (PSTag.Activity) getTag("activity");
    }

    public User getUser(String id) {
        return (User) getObject(User.NAME, id);
    }

    public Timesheets getTimesheets(String id) {
        return (Timesheets) getObject(Timesheets.NAME, id);
    }

    /**
     * get user which is not logged-in
     *
     * @return Admin or test user
     */
    public User getAnotherUser() {
        User res = getFirstUser();
        if (!res.equals(BasicCommons.getCurrentUser())) return res;
        return CoreProperties.getDefaultUser();
    }

    public User getFirstUser() {
        return getUser("user1");
    }

    public User getSecondUser() {
        return getUser("user2");
    }

    public User getDefaultUser() {
        return getUser("default-user");
    }

    public static Random getRandom() {
        return rand;
    }

    public static int getRandomNotZeroInt(int base) {
        if (Math.abs(base) <= 1) return base;
        return (base > 0 ? 1 : -1) * (rand.nextInt(Math.abs(base) - 1) + 1);
    }

    public static String getRandomDate(int days) {
        return getRandomPSDate(days).toString();
    }

    public static PSCalendar getRandomPSDate(int days) {
        return getRandomPSDate(PSCalendar.getEmptyCalendar(), days);
    }

    public static PSCalendar getRandomPSDate(PSCalendar c, int days) {
        int rDays = Math.abs(days) > 1 ? (days > 0 ? 1 : -1) * (rand.nextInt(Math.abs(days) - 1) + 1) : 0;
        return c.set(rDays, rDays < 0 ? PSCalendar.DEFAULT_WORK_DAY_START_IN_H : PSCalendar.DEFAULT_WORK_DAY_END_IN_H);
    }

    public static PSCalendar getRandomPSDate(long c, int days) {
        return getRandomPSDate(PSCalendar.getEmptyCalendar().set(c), days);
    }

    public static void mixList(List list) {
        for (int i = 0; i < list.size(); i++) {
            int j = rand.nextInt(list.size());
            Object f = list.remove(i);
            list.add(j, f);
        }
    }

    public static <T> T getRandomFromList(List<T> list) {
        return list.get(rand.nextInt(list.size()));
    }

    public static <T> T getRandomFromArray(T[] array) {
        return array[rand.nextInt(array.length)];
    }

    public static PSPermissions.AllSet getPermissions() {
        for (ConfigObject w : generalObjects) {
            if (PSPermissions.AllSet.NAME.equals(w.getConfig().getName())) return (PSPermissions.AllSet) w;
        }
        return null;
    }

}
