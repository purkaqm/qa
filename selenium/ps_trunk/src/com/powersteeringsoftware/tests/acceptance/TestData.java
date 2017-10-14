package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.objects.PSTag;
import com.powersteeringsoftware.libs.tests_data.Config;
import com.powersteeringsoftware.libs.tests_data.PSTestData;

import java.util.TreeSet;

import static com.powersteeringsoftware.libs.settings.CoreProperties.getTestTemplate;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.11
 * Time: 16:03
 */
public class TestData extends PSTestData {

    public static final String WORK_ITEM_ID = "basic1";
    public static final String WORK_ITEM_2_ID = "basic4";
    public static final String WORK_ITEM_3_ID = "basic5";
    public static final String MSP_PROJECT_ID = "basic3";
    public static final String FOLDER_ID = "basic2";

    public static final String ISSUE_MAIN = "main_issue";
    public static final String ISSUE_CHILD1 = "ch1";
    public static final String ISSUE_CHILD2 = "ch2";
    public static final String ISSUE_CHILD12 = "ch12";


    public String getIssueSubject(String id) {
        String res = conf.findElementById(id).getText("subject");
        if (res == null) return null;
        return res.contains(String.valueOf(getTestTemplate())) ? res : res + "_" + getTestTemplate();
    }

    public void setIssueSubject(String id, String subject) {
        conf.findElementById(id).setText("subject", subject);
    }

    public String getIssueBody(String id) {
        return conf.findElementById(id).getText("body");
    }

    public TreeSet<String> getSubjects() {
        TreeSet<String> res = new TreeSet<String>();
        for (Config c : conf.findChsByName("issue")) {
            res.add(getIssueSubject(c.getAttribute("id")));
        }
        return res;
    }

    public PSTag getRequiredTag() {
        return getTag("required");
    }

    public PSTag getUnRequiredTag() {
        return getTag("unrequired");
    }
}
