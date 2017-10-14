package com.powersteeringsoftware.tests.acceptance;

import com.powersteeringsoftware.libs.logger.PSLogger;
import com.powersteeringsoftware.libs.pages.*;
import com.powersteeringsoftware.libs.tests.actions.WorkManager;
import com.powersteeringsoftware.libs.tests.core.PSSkipException;
import com.powersteeringsoftware.libs.tests.core.PSTest;
import org.testng.Assert;

import java.util.List;
import java.util.TreeSet;

import static com.powersteeringsoftware.libs.enums.page_locators.DiscussionAddPageLocators.DEFAULT_ISSUE_SUBJECT_PATTERN;
import static com.powersteeringsoftware.libs.settings.PowerSteeringVersions._7_1;
import static com.powersteeringsoftware.libs.util.session.TestSession.getAppVersion;
import static com.powersteeringsoftware.tests.acceptance.TestData.*;

/**
 * Created by IntelliJ IDEA.
 * User: szuev
 * Date: 17.08.11
 * Time: 18:27
 */
public class TestMoving extends PSTest {

    private TestData data;

    public TestMoving(TestData d) {
        name = "Moving issues";
        this.data = d;
    }

    @Override
    public void run() {
        if (getAppVersion().verLessThan(_7_1)) {
            PSSkipException.skip("Application version must be 7.1 or above");
        }

        SummaryWorkPage summary = WorkManager.open(data.getRootWork());
        DiscussionsPage discs = summary.openDiscussionsTab();
        PSLogger.info("discussions : " + discs.getSubjects());
        DiscussionIssueViewPage view;

        PSLogger.info("Create issues tree");
        IssuesPage issues = discs.openIssues();
        PSLogger.info("issues before creating  : " + issues.getSubjects());
        view = createDiscussionThree(issues);
        issues = view.openIssues();
        List<String> subjects = issues.getSubjects();
        PSLogger.info("issues after creating  : " + subjects);

        PSLogger.info("Move issues tree");
        Assert.assertTrue(subjects.contains(data.getIssueSubject(ISSUE_MAIN)), "Can't find " + data.getIssueSubject(ISSUE_MAIN));
        CopyMoveDiscPage move = issues.openDiscussion(data.getIssueSubject(ISSUE_MAIN)).move();
        move.setParent(data.getTestingWork().getName(), false);
        discs = move.move();
        PSLogger.info(subjects = discs.getSubjects());
        Assert.assertTrue(subjects.contains(data.getIssueSubject(ISSUE_MAIN)), "Can't find " + data.getIssueSubject(ISSUE_MAIN) + " after moving");

        PSLogger.info("Validate issues tree after moving");
        issues = WorkManager.open(data.getRootWork()).openIssues();
        PSLogger.info(subjects = discs.getSubjects());
        Assert.assertFalse(subjects.contains(data.getIssueSubject(ISSUE_MAIN)), "There is " + data.getIssueSubject(ISSUE_MAIN) + " after moving");

    }

    private DiscussionIssueViewPage createDiscussionThree(AbstractDiscussionsPage page) {
        DiscussionAddPage add = page.pushAddNew();
        DiscussionIssueViewPage view = processSimpleDiscussion(add, ISSUE_MAIN);
        view = processSimpleDiscussion(view, ISSUE_MAIN, ISSUE_CHILD1);
        view = processSimpleDiscussion(view, ISSUE_CHILD1, ISSUE_CHILD12);
        view = processSimpleDiscussion(view, ISSUE_MAIN, ISSUE_CHILD2);
        TreeSet<String> fromPage = new TreeSet<String>(view.getIssuesTitles());
        PSLogger.info("From page: " + fromPage);
        PSLogger.info("Expected : " + data.getSubjects());
        Assert.assertEquals(fromPage, data.getSubjects(), "Incorrect issues subjects on page");
        return view;
    }

    private DiscussionIssueViewPage processSimpleDiscussion(DiscussionAddPage page, String id) {
        String subject = data.getIssueSubject(id);
        String body = data.getIssueBody(id);
        page.setSubject(subject);
        page.setDescription(body);
        return page.submit();
    }

    private DiscussionIssueViewPage processSimpleDiscussion(DiscussionIssueViewPage view, String parentId, String id) {
        DiscussionAddPage page = view.getBlock(data.getIssueSubject(parentId)).reply();
        view = processSimpleDiscussion(page, id);
        if (data.getIssueSubject(id) == null) {
            data.setIssueSubject(id, DEFAULT_ISSUE_SUBJECT_PATTERN.replace(data.getIssueSubject(parentId)));
        }
        return view;
    }

}
