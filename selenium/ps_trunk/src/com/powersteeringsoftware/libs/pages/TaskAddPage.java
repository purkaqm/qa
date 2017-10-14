package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.enums.page_locators.TaskAddPageLocators;
import com.powersteeringsoftware.libs.logger.PSLogger;
import org.testng.Assert;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 30.01.13
 * Time: 13:38
 * To change this template use File | Settings | File Templates.
 */
public class TaskAddPage extends AbstractWorkPage {

    public void setName(String txt) {
        Input in = new Input(TaskAddPageLocators.NAME);
        in.waitForVisible();
        PSLogger.info("Set task name '" + txt + "'");
        in.type(txt);
    }

    public TasksPage submit() {
        Button bt = new Button(TaskAddPageLocators.ADD);
        bt.waitForVisible();
        bt.focus();
        bt.click(true);
        String error = getErrorBoxMessage();
        Assert.assertNull(error, "Has error : " + error);
        TasksPage res = new TasksPage();
        Assert.assertTrue(res.checkUrl(), "Incorrect url after submitting new task");
        return res;
    }
}
