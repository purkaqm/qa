package com.powersteeringsoftware.libs.enums.page_locators;

import com.powersteeringsoftware.libs.enums.ILocatorable;

public enum CustomColumnEditPageLocators implements ILocatorable {
	
	NAME_INPUT("id=TextField"),
	EXPR_TEXTAREA("id=TextArea"),
	SAVE_BUTTON("//input[@type='submit' and @value='Save']"),
	CANCEL_BUTTON("//input[@type='submit' and @value='Cancel']"),
	;
	CustomColumnEditPageLocators(String locator){
		this.locator = locator;
	}
	
	@Override
	public String getLocator() {
		return locator;
	}

	
	private String locator;

} // enum CustomColumnEditPageLocators 
