package com.powersteeringsoftware.tests.parallels;

import com.powersteeringsoftware.libs.core.SeleniumDriverFactory;
import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.pages.ProfilePage;
import com.powersteeringsoftware.libs.settings.CoreProperties;
import com.powersteeringsoftware.libs.tests.actions.BasicCommons;
import com.powersteeringsoftware.libs.tests.actions.TagManager;
import com.powersteeringsoftware.libs.tests.actions.UserManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * for bugs #81413, #81484
 */
public class NPETest {

    static {
        CoreProperties.loadProperties();
    }

    private static final int COUNT = 5;
    private static final int LOGIN_LOGOUT_NUM = 200;

    private static final int TAGS_COUNT = 50;
    private static final String TAG_PREFIX = "Test_Tag_81413";
    private static List<PSTag> tags;
    private static List<User> users = Arrays.asList(new User("test", "qwer", null, "op67jrg0000iirnvn9bg000000"),
            new User("test2", "qwer", null, "op67jrg0000isbkvmu6g000000"),
            CoreProperties.getDefaultUser(),
            CoreProperties.getDefaultUser());
    //new User("test3", "qwer", null, "fs000080000jhkp8mlag000000"));
    private static final String TAG_VALUE = "val1";
    private Data removeThread;
    private boolean ok;

    @Test
    public void prepare() {
        BasicCommons.logIn();
        tags = TagManager.getTags(PSTag.Associate.PEOPLES_USERS, TAG_PREFIX);
        for (PSTag tg : tags) {
            tg.addValue(TAG_VALUE);
        }
        PSLogger.info("Tags: " + tags);
        boolean multiple = false;
        while (tags.size() < TAGS_COUNT) {
            PSTag tg = new PSTag(TAG_PREFIX + "_" + CoreProperties.getTestTemplate() + "_" + tags.size());
            tg.setAssociation(PSTag.Associate.PEOPLES_USERS);
            tg.addValue(TAG_VALUE);
            tg.setMultiple(multiple);
            multiple = !multiple;
            TagManager.addTag(tg).backToTagsList();
            tags.add(tg);
        }
        for (User u : users) {
            ProfilePage profile = UserManager.openProfile(u);
            for (PSTag tg : tags) {
                if (profile.getTagsComponent().isSelectorEmpty(tg))
                    profile.getTagsComponent().setTag(tg);
                u.addTag(tg);
            }
            profile.submit();
        }
        SeleniumDriverFactory.stopLastSeleniumDriver();
        SeleniumDriverFactory.setParallel(true);
        ok = true;
    }

    private class Data {
        private User user;
        private boolean isFinish;
        private boolean isRemove;

        public String toString() {
            return user + ", " + (isRemove ? "Remove tags thread" : "Login/Logout thread");
        }
    }

    @DataProvider(parallel = true)
    @Test(dependsOnMethods = "prepare", alwaysRun = false)
    public Object[][] data() {
        Object[][] res = new Object[COUNT][0];
        List<User> _us = new ArrayList<User>();
        for (int i = 0; i < COUNT - 1; i++) {
            Data dt2 = new Data();
            if (_us.size() == 0) {
                _us.addAll(users);
            }
            dt2.user = (User) _us.remove(0).clone();
            res[i] = new Object[]{dt2};
        }

        Data dt = new Data();
        dt.user = (User) CoreProperties.getDefaultUser().clone();
        dt.isRemove = true;
        res[COUNT - 1] = new Object[]{dt};

        return res;
    }

    @Test(dataProvider = "data")
    public void test(Data data) {
        if (!ok) PSSkipException.skip("Error in prepare");
        Thread.currentThread().setName(data.toString());
        if (data.isRemove) {
            removeThread = data;
            try {
                BasicCommons.logIn(data.user);
                while (tags.size() != 0) {
                    TagManager.deleteTag(tags.remove(0));
                    if (DeadlockParallelListener.hasFailure()) break;
                }
                return;
            } finally {
                removeThread.isFinish = true;
            }
        }
        int i = 0;
        while (isAlive() && i < LOGIN_LOGOUT_NUM) {
            PSLogger.info(data + " #" + i);
            BasicCommons.logIn(data.user);
            BasicCommons.validatePage();
            BasicCommons.logOut();
            BasicCommons.validatePage();
            i++;
            if (DeadlockParallelListener.hasFailure()) break;
        }
    }

    public boolean isAlive() {
        return removeThread == null || !removeThread.isFinish;
    }

}
