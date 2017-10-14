package com.powersteeringsoftware.tests.contentfiller;

import com.powersteeringsoftware.core.managers.CustomFieldsTemplateManager;
import com.powersteeringsoftware.core.objects.CustomFieldsTemplate;
import com.powersteeringsoftware.core.tc.PSTest;

/**
 * Test creating custom fields
 */
public class TestCreateCustomField extends PSTest {
	public TestCreateCustomField(){
		name = "Create Custom Field";
	}

	public void run() {
		CustomFieldsTemplateManager cFTMan = new CustomFieldsTemplateManager();
		CustomFieldsTemplate cFT = new CustomFieldsTemplate("General custom fields");
		cFT.setAssocWithTemplates(true);
		cFT.setAssocWithWorkItems(true);
		cFT.setAssocWithUnexpWorkItems(true);
		cFT.setAssocWithGatedProjects(true);
		cFT.addYesNoButtonField("Sample Yes-No button", "Just a sample yes-no radio button created by an automated script");
		String[] checkboxes = {"Red", "Green", "Blue"};
		cFT.addCheckboxesField("Sample Checkboxes", "Just sample checkboxes created by an automated script", checkboxes);
		cFTMan.create(cFT);
	}

}
