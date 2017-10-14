package com.powersteeringsoftware.tests.acceptance;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.adapters.issues.IssueEditPageAdapter;
import com.powersteeringsoftware.core.adapters.issues.IssueListPageAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagDetailsPageAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagEditWindowAdapter;
import com.powersteeringsoftware.core.adapters.tags.TagListPageAdapter;
import com.powersteeringsoftware.core.adapters.workitems.WISummaryAdapter;
import com.powersteeringsoftware.core.managers.TagManager;
import com.powersteeringsoftware.core.objects.Tag;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.tc.PSTest;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.TimeStampName;
import com.powersteeringsoftware.core.util.session.TestSession;

/**
 * Summary: Test issue 59872: "Issues: Can't submit an issue".<br>
 *
 * Test Steps:<br>
 * 1. Create required tag for issues<br>
 * 2. Create unrequired tag for issues<br>
 * 3. Create issue. While creating check that checking required issues is
 * enougth for saving (that means that unchecked unrequired tag has no effect o
 * saving an issue)<br>
 * 4. Test delete tags.
 *
 * Preconditions:<br>
 * - before executing test make sure CoreProperties has been initialized.<br>
 * Referencies:
 * Used CoreProperties.getDefaultWorkItemIdWithoutPrefix() for navigating work
 * item summary page<br>
 *
 * Test can be executed only for PowerSteering 7.1
 *
 */

public class TestIssue59872 extends PSTest{

	Logger log = Logger.getLogger(TestIssue59872.class);

	public TestIssue59872(){
		name ="Issue 59872:required\\unrequired tags in new issue";
	}

	enum AutoTagValues{
		VALUE1("Value1"),
		VALUE2("Value2"),
		VALUE3("Value3");
		String name;
		AutoTagValues(String _name){
			name = _name;
		}
		public String getValue(){
			return name;
		}
	}

	private String tagRequiredName;
	private String tagUnrequiredName;

	Tag unrequiredTag;
	Tag requiredTag;

	public void run(){
		TestSession.assertIsApplicationVerison71();

		createRequiredTag();
		createUnrequiredTag();
		createIssue();
		setTagUnrequired();
	}

	private void createIssue() {
		new WISummaryAdapter().navigatePageIssues(CoreProperties.getDefaultWorkItemIdWithPrefix());

		IssueListPageAdapter.pushNewIssue();
		IssueEditPageAdapter.typeName("Tested Issue");
		IssueEditPageAdapter.typeDescription("Issue message");
		IssueEditPageAdapter.selectTagValue_Select(requiredTag.getUid(), AutoTagValues.VALUE1.getValue());
		IssueEditPageAdapter.submitWithUpperButton();

		Assert.assertEquals(SeleniumDriverSingleton.getDriver().getLocation().contains(CoreProperties.APPLICATION_URL_ISSUE_VIEW),
				true,
				"Error while creating issue");
	}

	private void createUnrequiredTag() {
		tagUnrequiredName =  new TimeStampName("UnrequiredTag").getTimeStampedName();
		unrequiredTag = new Tag(this.tagUnrequiredName, this.tagUnrequiredName);
		unrequiredTag.addTagValue(AutoTagValues.VALUE1.getValue());
		unrequiredTag.addTagValue(AutoTagValues.VALUE2.getValue());
		unrequiredTag.addTagValue(AutoTagValues.VALUE3.getValue());
		unrequiredTag.setPropRequired(false);
		unrequiredTag.addTypeOfMessages(CoreProperties.getIssueTitle());
		new TagManager().addTag(unrequiredTag);
	}

	private void createRequiredTag() {
		tagRequiredName =  new TimeStampName("RequiredTag").getTimeStampedName();
		requiredTag = new Tag(this.tagRequiredName, this.tagRequiredName);
		requiredTag.setPropRequired(true);
		requiredTag.addTagValue(AutoTagValues.VALUE1.getValue());
		requiredTag.addTagValue(AutoTagValues.VALUE2.getValue());
		requiredTag.addTagValue(AutoTagValues.VALUE3.getValue());
		requiredTag.addTypeOfMessages(CoreProperties.getIssueTitle());
		new TagManager().addTag(requiredTag);
	}

	private void setTagUnrequired(){
		MainMenuAdapter.clickAdminConfigurationsTags();
		TagListPageAdapter.clickTagName(requiredTag.getTagName());
		TagDetailsPageAdapter.clickEdit();
		TagEditWindowAdapter.setRequired(false);
		TagEditWindowAdapter.submitEditing();
	}

}
