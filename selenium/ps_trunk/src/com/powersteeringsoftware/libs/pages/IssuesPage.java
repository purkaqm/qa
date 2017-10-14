package com.powersteeringsoftware.libs.pages;


import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.enums.page_locators.IssuesPageLocators;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 11.07.11
 * Time: 19:53
 */
public class IssuesPage extends AbstractDiscussionsPage {

    @Override
    public void open() {
        //ToDo
    }

    public IssueAddPage pushAddNew() {
        new Button(IssuesPageLocators.NEW_ISSUE_BUTTON).click(true);
        return new IssueAddPage();
    }

}
