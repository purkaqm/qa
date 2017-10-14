package com.powersteeringsoftware.libs.elements;

import com.powersteeringsoftware.libs.enums.ILocatorable;
/**
 * Represents element with modifiable text content
 *
 */
public class InfoBox extends Element {
	private String content;
	
	public InfoBox(ILocatorable locator) {
		super(locator);
	}

	public String getContent(){
		return this.getText();
	}
	
	public void saveState(){
		this.content = getContent();
	}
	
	public boolean isStateChanged(){
		return this.content != null ? content.equals(getContent()) : (getContent() != null);
	}
	
} // class InfoBox 
