package selenium.models;

import selenium.driver.MySeleniumDriver;
import selenium.formholders.TagHolder;

public class UpdateTagValuesPage {

	private MySeleniumDriver mydriver;

	public UpdateTagValuesPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisUpdateTagValuesPagePage() {
		return mydriver.getTitle().contains("Update Tag Values");
	}

	public void addTagValuesNames(TagHolder tagHolder) throws Exception {
		for (int i=0; i < tagHolder.getTagValues().size(); i++){
			String nextTextfieldId;
			if (i == 0 ) {
				nextTextfieldId = "TextField_0";
			} else {
				nextTextfieldId = "TextField_0_" + String.valueOf(i - 1);
			}
			if (!(mydriver.isElementPresent("dom=document.getElementById('" + nextTextfieldId + "')"))) {
				mydriver.click("dom=document.getElementById('Submit_0')");
				mydriver.waitForPageToLoad("30000");
			}
			mydriver.type("dom=document.getElementById('" + nextTextfieldId + "')"
					, tagHolder.getTagValues().get(i).getTagValueName());
		}
		mydriver.click("dom=document.getElementById('Submit_1')");
		mydriver.waitForPageToLoad("30000");
	}

	public void setTagValuesParents(TagHolder tagHolder) throws Exception {
		if (!(null == tagHolder.getPropHierarchical()))
			for (int i=0; i < tagHolder.getTagValues().size(); i++){
				String tagValueTextfieldDOMString;
				if (i == 0 ) {
					tagValueTextfieldDOMString = "dom=document.getElementById('TextField_0')";
				} else {
					tagValueTextfieldDOMString = "dom=document.getElementById('TextField_0_" + String.valueOf(i - 1) + "')";
				}
				for (int j=0; j < tagHolder.getTagValues().size(); j++)
					if (!(null == tagHolder.getTagValues().get(j).getTagValueParent()))
						if (tagHolder.getTagValues().get(j).getTagValueName().equals(mydriver.getValue(tagValueTextfieldDOMString).trim()))
								mydriver.select(tagValueTextfieldDOMString + ".parentNode.parentNode.getElementsByTagName('select')[0]"
										, "label=" + tagHolder.getTagValues().get(j).getTagValueParent());
			}
			mydriver.click("dom=document.getElementById('Submit_1')");
			mydriver.waitForPageToLoad("30000");
	}
}