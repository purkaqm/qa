package com.powersteeringsoftware.core.adapters.workitems;

import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.ILocatorable;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeVisibleWaiter;
import com.thoughtworks.selenium.DefaultSelenium;

public class WIDeliverablesAdapter extends AbstractWorkItem {

	private AddDeliverableAdapter addDeliverableAdapter;
	private DefaultSelenium driver;

	public WIDeliverablesAdapter(DefaultSelenium _driver){
		driver = _driver;
		this.addDeliverableAdapter = new AddDeliverableAdapter();
	}

	enum PageElementLocators implements ILocatorable {
		BUTTON_ADD_NEW("AddDeliverableShow");

		String locator;
		PageElementLocators(String _locator){
			locator = _locator;
		}
		public String getLocator() {
			return locator;
		}
	}

	enum WindowAddDeliverableLocators implements ILocatorable {
		DIV_ID("AddDeliverable"),
		SELECT_WORK_TYPE("workType"),
		SELECT_GATE("gateSelection"),
		BUTTON_CONTINUE("continueBtn"),
		BUTTON_CANCEL("AddDeliverableHide");

		String locator;
		WindowAddDeliverableLocators(String _locator){
			locator = _locator;
		}
		public String getLocator() {
			return locator;
		}
	}


	private class AddDeliverableAdapter{

		AddDeliverableAdapter(){
		}

		public void setWorkType(String workType){
			SeleniumDriverSingleton.getDriver().select(
					WindowAddDeliverableLocators.SELECT_WORK_TYPE.getLocator(),
					workType);
		}

		public void setGate(String gate){
			SeleniumDriverSingleton.getDriver().select(
							WindowAddDeliverableLocators.SELECT_GATE.getLocator(),
							gate);
		}

		public CreateWorkAdapter pushContinue(){
			SeleniumDriverSingleton.getDriver().click(
					WindowAddDeliverableLocators.BUTTON_CONTINUE.getLocator());
			SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
			return new CreateWorkAdapter(driver);
		}

		public void pushCancel(){
			SeleniumDriverSingleton.getDriver().click(
					WindowAddDeliverableLocators.BUTTON_CANCEL.getLocator());
		}
	}

	public void pushAddNew(){
		SeleniumDriverSingleton.getDriver().click(PageElementLocators.BUTTON_ADD_NEW.getLocator());
		ElementBecomeVisibleWaiter.waitElementBecomeVisible(WindowAddDeliverableLocators.DIV_ID.getLocator());
	}

	public void setParameters(String workType, String gate){
		addDeliverableAdapter.setWorkType(workType);
		addDeliverableAdapter.setGate(gate);
	}

	public CreateWorkAdapter pushContinue(){
		return addDeliverableAdapter.pushContinue();
	}

	public void pushCancel(){
		pushCancel();
	}

	public void checkDeleteCheckBoxByWorkName(String workName){
		String locator = "dom=var altArray = window.dojo.query('[alt=Work]'); "
		+ "var index=0; "
		+ "for(index=0; index<altArray.length; index++){ "
		+ " if (altArray[index].parentNode.innerHTML.indexOf('"+workName+"') != -1) "
		+ " window.dojo.query('[id*=checkbox]',altArray[index].parentNode.parentNode)[0] "
		+ "}";

		SeleniumDriverSingleton.getDriver().check(locator);
	}

	public void pushDelete(){
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('#PSForm_0 [value=Delete]')[0];");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}


	public boolean isDeliverableExistByName(String deliverableName){
		String evalString = "window.dojo.byId('PSTable').innerHTML.indexOf('"
				+ deliverableName
				+ "');";

		try{
			int index = new Integer(SeleniumDriverSingleton.getDriver().getEval(evalString));
			if (index<0) return false;
		} catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}

}
