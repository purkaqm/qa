package com.powersteeringsoftware.tests.parallel_loading;

import com.powersteeringsoftware.libs.objects.User;
import com.powersteeringsoftware.libs.tests_data.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 10.02.12
 * Time: 15:51
 */
public class TestData {
    private static final Random RAND = new Random();
    private static final String CONFIG = "/ecolab_users.xml";
    public static int count = 5;


    public static List<User> getUsers() {
        List<User> res = new ArrayList<User>();
        for (Config c : Config.getTestConfig(CONFIG).getChilds()) {
            User u = new User(c);
            if (u.getPassword() != null && u.getLogin() != null) {
                res.add(u);
            }
        }
        return res;
    }

    public static List<User> getUsers(boolean random, int count) {
        List<User> users = new ArrayList<User>();
        List<User> _users = getUsers();
        if (random) {
            while (users.size() < count && _users.size() > 0) {
                User u = (User) _users.remove(RAND.nextInt(_users.size())).clone();
                users.add(u);
            }
        } else {
            for (User u : _users.subList(0, count)) {
                users.add((User) u.clone());
            }
        }
        return users;
    }

    public static Object[][] getData(boolean random, int count) {
        List<User> users = getUsers(random, count);
        Object[][] res = new Object[users.size()][1];
        for (int i = 0; i < users.size(); i++) {
            res[i] = new Object[]{users.get(i)};
        }
        return res;
    }

}
