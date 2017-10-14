package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.objects.PSProcess;
import com.powersteeringsoftware.libs.objects.Portfolio;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.objects.works.GatedProject;
import com.powersteeringsoftware.libs.objects.works.Template;
import com.powersteeringsoftware.libs.objects.works.Work;
import com.powersteeringsoftware.libs.tests_data.PSTestData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 07.05.2014.
 * DB: sz_100_def_deadlocks
 */
public class DeadlockTestData extends PSTestData {

    public Work getOrg87444() {
        return null; //todo
    }

    private Work org89410;

    public Work getOrg89410() {
        return org89410 == null ? org89410 = Work.createOrg("org89410", "op67ja00000kblgt28q0000000") : org89410;
    }

    private Work org89410_2;

    public Work getOrg89410_2() {
        return org89410_2 == null ? org89410_2 = Work.createOrg("org89410-2", "o2k03bg0000kc2ho1ie0000000") : org89410_2;
    }

    private Work org90356;

    public Work getOrg90356() {
        return org90356 == null ? org90356 = Work.createOrg("org90356", "op67j9o0000kbcl1tok0000000") : org90356;
    }

    private Work org90169;

    public Work getOrg90169() {
        return org90169 == null ? org90169 = Work.createOrg("org90169", "o2k03900000kbo44m3k0000000") : org90169;
    }

    private Work org91033;

    public Work getOrg91033() {
        return org91033 == null ? org91033 = Work.createOrg("org1", "op67j9o0000kbci92aa0000000") : org91033;
    }

    public Portfolio getPortfolio87444() {
        return null; //todo
    }

    public Portfolio getPortfolio89410() {
        return new Portfolio("test-profile", "op67ja00000kblgv3ofg000000");
//        return new Portfolio("test-profile-89410", "op67j980000kbvqe5gp0000000"); //qaautodb-01.cinteractive.com:1433/sz_v11_def
    }

    public Portfolio getPortfolio89410_2() {
        return new Portfolio("test-profile-2", "o2k03bg0000kc2i2ho8g000000");
    }

    public Portfolio getPortfolio90169() {
        return new Portfolio("test-profile-3", "op67ja80000kc56undg0000000");
    }

    private Template gp89410;

    public Template getSGTemplate89410() {
        return gp89410 != null ? gp89410 :
                (gp89410 = createGPTmpl("DMAIC-SGP-#89410",
                        "op67ja00000kblh4ks2g000000",
//                        "op67j980000kbvqpnc80000000", //qaautodb-01.cinteractive.com:1433/sz_v11_def
                        true, getDMAICProcess()));
    }

    private Template gp89410_2;

    public Template getSGTemplate89410_2() {
        return gp89410_2 != null ? gp89410_2 :
                (gp89410_2 = createGPTmpl("DMAIC-SGP-#89410-2",
                        "op67j900000kbtifhuq0000000",
                        true, getDMAICProcess()));
    }

    private Template gp90169;

    public Template getSGTemplate90169() {
        return gp90169 != null ? gp90169 :
                (gp90169 = createGPTmpl("DMAIC-SGP-#90169",
                        "op67ja00000kblh5889g000000",
//                        "op67j980000kbvqpnc80000000", //qaautodb-01.cinteractive.com:1433/sz_v11_def
                        true,
                        getDMAICProcess()));

    }

    private Template gp90169_2;

    public Template getSGPTemplate90169_2() {
        return gp90169_2 != null ? gp90169_2 :
                (gp90169_2 = createGPTmpl("DMAIC-SGP-#90169-2",
                        "op67jb80000kc3ru31g0000000",
                        true,
                        getDMAICProcess()));

    }

    private Template createGPTmpl(String name, String id, boolean esg, PSProcess pr) {
        Template tmpl = new Template(name);
        tmpl.setId(id);
        GatedProject root = new GatedProject(name, tmpl, esg);
        root.setProcess(pr);
        tmpl.getConfig().addChild(root.getConfig());
        return tmpl;
    }

    private GatedProject work91033_1;

    public GatedProject getSGProject91033() {
        return work91033_1 == null ? work91033_1 = new GatedProject("ESGP91033", getSGPTemplate90169_2()) {
            {
                setId("op67j8g0000kc8oqhvkg000000");
                setParent(getOrg91033());
            }
        } : work91033_1;
    }

    private GatedProject work91033_2;

    public GatedProject getNotSGProject91033() {
        return work91033_2 == null ? work91033_2 = new GatedProject("NotESGP91033", getSGTemplate89410_2()) {
            {
                setId("op67j8g0000kc8oq1g1g000000");
                setParent(getOrg91033());
            }
        } : work91033_2;
    }

    private GatedProject work91033_3;

    public GatedProject getSGProject91033_2() {
        return work91033_3 == null ? work91033_3 = new GatedProject("ESGP91033(2)", getSGPTemplate()) {
            {
                setId("op67j7o0000kd0he4kng000000");
                setParent(getOrg91033());
            }
        } : work91033_3;
    }

    private User user0;

    public User getUser0() {
        return user0 == null ? (user0 = getDefaultUser()) : user0;
    }

    private User user1;

    public User getUser1() {
        return user1 == null ? (user1 = getFirstUser()) : user1;
    }

    private User user2;

    public User getUser2() {
        return user2 == null ? (user2 = getSecondUser()) : user2;
    }

    private User user3;

    public User getUser3() {
        return user3 == null ? (user3 = new User("test3", "qwer", "test3", "test3", "test3@test.com")) : user3;
    }

    private User user4;

    public User getUser4() {
        return user4 == null ? (user4 = new User("test4", "qwer", "test4", "test4", "test4@test.com")) : user4;
    }

    private User user5;

    public User getUser5() {
        return user5 == null ? (user5 = new User("test5", "qwer", "test5", "test5", "test5@test.com")) : user5;
    }

    private List<User> users;

    protected List<User> getUsers() {
        if (users != null) return users;
        users = new ArrayList<User>();
        users.add(getUser0());
        users.add(getUser1());
        users.add(getUser2());
        users.add(getUser3());
        users.add(getUser4());
        users.add(getUser5());
        return users;
    }

}
