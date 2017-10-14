package com.powersteeringsoftware.libs.pages;

import com.powersteeringsoftware.libs.elements.Button;
import com.powersteeringsoftware.libs.elements.Input;
import com.powersteeringsoftware.libs.elements.TextArea;

import static com.powersteeringsoftware.libs.enums.page_locators.CustomColumnEditPageLocators.*;

public class CustomColumnEditPage extends PSPage {
	public void open() {}
	
	public void setName(String name){
		getNameInput().type(name);
	}

	public void setExpression(String expr){
		getExprTextArea().setText(expr);
	}
	
	public void clickSave(){
		getSaveButton().click(true);
	}

	public void clickCancel(){
		getCancelButton().click(true);
	}

	public TextArea getExprTextArea(){
		return new TextArea(EXPR_TEXTAREA);
	}
	
	public Input getNameInput(){
		return new Input(NAME_INPUT);
	}
	
	public Button getSaveButton(){
		return new Button(SAVE_BUTTON);
	}
	
	public Button getCancelButton(){
		return new Button(CANCEL_BUTTON);
	}

	
} // class CustomColumnEditPage

