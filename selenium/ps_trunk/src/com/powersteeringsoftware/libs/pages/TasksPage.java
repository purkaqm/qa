package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.enums.page_locators.TasksPageLocators;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 30.01.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class TasksPage extends AbstractWorkPage {

    public TaskAddPage pushAddNew() {
        new Button(TasksPageLocators.NEW_TASK_BUTTON).click(true);
        return new TaskAddPage();
    }
}
