package com.powersteeringsoftware.libs.enums.elements_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

public enum RWColumnFilterControlLocators implements ILocatorable {
	FT_PHASE_ALL_PHASE_CHECKBOX("//input[@type='checkbox']"),
	FT_PHASE_SELECTOR("//select"),
	FT_PHASE_CHECKBOX("//label[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
	
	FT_TAG_CLAUSE_SELECTOR("//div[1]/select"),
	FT_TAG_CRITERION_SELECTOR("//div[2]/select"),
	FT_TAG_TAGCHOOSER_ELEMENT("//div[3]/div[@class='Selector']"),
	
	FT_SELECTOR_SELECTOR("//select"),
	
	FT_CHBOX_LABEL("//label[text()='" + LOCATOR_REPLACE_PATTERN + "']"),
	FT_CHBOX_CHECKBOX("//input[@id='" + LOCATOR_REPLACE_PATTERN + "']"),
	FT_CHBOX_ALL_CHECKBOXES("/input[@type='checkbox']"),
	
	FT_TEXT_WITH_CLAUSE_SELECTOR("//select"),
	FT_TEXT_WITH_CLAUSE_INPUT("//input[@type='text']"),
	
	;

	RWColumnFilterControlLocators(String locator){
		this.locator = locator;
	}
	
	@Override
	public String getLocator() {
		return locator;
	}

    public String replace(String rep) {
        return locator.replace(LOCATOR_REPLACE_PATTERN, rep);
    }

	private String locator;

} // enum RWColumnFilterControlLocators 
