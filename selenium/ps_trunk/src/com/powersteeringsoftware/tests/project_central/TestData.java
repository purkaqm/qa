package com.powersteeringsoftware.tests.project_central;

import com.powersteeringsoftware.libs.objects.PSPermissions;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.resources.PSCalendar;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;
import com.thoughtworks.selenium.SeleniumException;
import com.thoughtworks.selenium.Wait;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for test data (project_central.xml)
 * User: szuev
 * Date: 12.05.2010
 * Time: 14:39:59
 */
public class TestData extends PSTestData {

    private static final String USER_CONFIG_NAME = "project_central_user";

    private static final String WORK1 = "1";
    private static final String WORK2 = "2";
    private static final String WORK3 = "3";

    public TestData() {
        super();
        conf.addConfig(getConfig(USER_CONFIG_NAME));
    }


    public Work getFirstMainWork() {
        return getWork(WORK1);
    }

    public Work getFirstMainWorkTree() {
        Work res = getFirstMainWork();
        if (!res.hasChildren()) {
            res.addChild(getWork("A"));
            res.addChild(getWork("D"));
            res.addChild(getWork("E"));
            res.addChild(getWork("F"));
            res.addChild(getWork("C"));
            res.addChild(getWork("B"));
        }
        return res;
    }

    public Work getSecondMainWork() {
        return getWork(WORK2);
    }

    public Work getThirdMainWork() {
        Work res = getWork(WORK3);
        if (!res.hasConstraintDates()) {
            // set start-date for three month ahead (due to #73402)
            PSCalendar monday = getCalendar().set(3 * 30).getFirstMonday();
            PSCalendar friday = monday.setWorkHours(40 * 4); // 4 weeks for default calendar
            res.setConstraintStartDate(monday);
            res.setConstraintEndDate(friday);
        }
        return res;
    }

    public List<String> getRandomStatuses() {
        List<String> res = Work.Status.getStringList();
        mixList(res);
        return res;
    }

    public List<Work.Status> getRandomStatusList() {
        List<Work.Status> res = Work.Status.getList();
        mixList(res);
        return res;
    }

    /**
     * @param name - name for filters
     * @return
     */
    public List<String> getFiltersByName(String name) {
        List<String> res = new ArrayList<String>();
        for (Config c : conf.getChByName("filters").getChsByName(name)) {
            res.add(c.getText());
        }
        return res;
    }

    //TODO
    public User getNewDefaultUserSettings() {
        User set = new User(conf.getElementByTagAndAttribute("user", "id", "default-user"));
        set.setLastName(set.getLastName() +
                new SimpleDateFormat("-MM-dd-HH-mm").format(System.currentTimeMillis()));
        return set;
    }

    public PSPermissions.AllSet getPermissionsSet(String id) {
        return new PSPermissions.AllSet(getConfig().getElementByTagAndAttribute(PSPermissions.AllSet.NAME, "id", id));
    }


    public Map<String, Class[]> getExceptionsTestCases(String id) {
        Map<String, Class[]> res = new HashMap<String, Class[]>();
        Config conf = getConfig().getElementByTagAndAttribute("testcases", "id", id);
        for (Config ex : conf.getChsByName("exception")) {
            String name = ex.getAttribute("name");
            Class clazz = Throwable.class;
            if (name.equalsIgnoreCase("exception")) {
                clazz = Exception.class;
            }
            if (name.equalsIgnoreCase("AssertionError")) {
                clazz = AssertionError.class;
            }
            if (name.toLowerCase().endsWith("Wait$WaitTimedOutException".toLowerCase())) {
                clazz = Wait.WaitTimedOutException.class;
            }
            if (name.equalsIgnoreCase("SeleniumException".toLowerCase())) {
                clazz = SeleniumException.class;
            }
            for (Config tc : ex.getChsByName("testcase")) {
                res.put(tc.getText(), new Class[]{clazz});
            }
        }
        return res;
    }

    public Map<String, Boolean> getSkippedTestCases(String id) {
        Map<String, Boolean> res = new HashMap<String, Boolean>();
        Config conf = getConfig().getElementByTagAndAttribute("testcases", "id", id);
        for (Config ex : conf.getChsByName("disabled")) {
            for (Config tc : ex.getChsByName("testcase")) {
                res.put(tc.getText(), false);
            }
        }
        return res;
    }

    public List<String> getInputTestData() {
        List<String> res = new ArrayList<String>();
        for (Config c : conf.getChByName("input-test-data").getChilds()) {
            res.add(c.getText());
        }
        return res;
    }

}
