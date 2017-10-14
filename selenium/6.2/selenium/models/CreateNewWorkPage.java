package selenium.models;

import java.util.*;
import selenium.driver.MySeleniumDriver;
import selenium.formholders.*;

public class CreateNewWorkPage{

	private MySeleniumDriver mydriver;

	public CreateNewWorkPage(BrowserModel browserModel) {
		this.mydriver = browserModel.getDriver();
	}

	public boolean isThisCreateNewWorkPage() {
		return mydriver.getTitle().contains("Create New Work");
	}

	public void createNewWork(NewWorkHolder nWH) throws InterruptedException{
		int stepNumber;

		stepNumber = activeStepNumber();
		if (stepNumber != 1)
			throw new IllegalStateException("Wrong creation step number: expected 1, found " + String.valueOf(stepNumber));

		mydriver.select("dom=document.getElementById('workType')", "value=" + getDOMSelectOptionValue("workType", nWH.getProjectType()));
		mydriver.click("dom=document.getElementById('next')");
		mydriver.waitForPageToLoad("30000");

		stepNumber = activeStepNumber();
		if (stepNumber != 2)
			throw new IllegalStateException("Wrong creation step number: expected 2, found " + String.valueOf(stepNumber));

		mydriver.type("dom=document.getElementById('name')", nWH.getProjectName());

		//TODO add project description handling

		//TODO Write some code to add team members

		//TODO Add step three handling if the form handler has some tags or fields

		mydriver.click("dom=document.getElementById('finish')");
		mydriver.waitForPageToLoad("30000");
	}

	public void createNewWork(NewGatedProjectHolder nGPH) throws InterruptedException{
		String creationStepNumber = mydriver.getText("//body/div[@id='content']//h2[@class='active']").trim();
		if (!(creationStepNumber.equals("1.")))
			throw new IllegalStateException("Wrong creation step number: expected '1.', found " + creationStepNumber);

		//TODO Change the regexp label to something better
		mydriver.select("//body/div[@id='content']//form//select[@id='workType']", "label=regexp:" + nGPH.getProjectType());
		mydriver.click("//body/div[@id='content']//form//input[@id='next']");
		mydriver.waitForPageToLoad("30000");

		creationStepNumber = mydriver.getText("//body/div[@id='content']//h2[@class='active']").trim();
		if (!(creationStepNumber.equals("2.")))
			throw new IllegalStateException("Wrong creation step number: expected '2.', found " + creationStepNumber);

		mydriver.type("//body/div[@id='content']//form//input[@id='name']", nGPH.getProjectName());

		//TODO add project description handling

		if (!(null == nGPH.getProjectChampions())) {
			//Only add the first champion for now
			mydriver.type("//input[@id='inputBoxId_CREATEWORK']", nGPH.getProjectChampions().getFirst());
			mydriver.click("//input[contains(@onclick,'ajaxSnd_CREATEWORK')]");
			mydriver.waitForElementToDisappear("//ul[@id='div_CREATEWORK']/li[@class='dnd-msg-loading']", "30000");

			//Next line works, but makes IE crash after form submit *sad panda* - so we can't use it
			//mydriver.dragAndDropToObject("//ul[@id='div_CREATEWORK']/li[@class='psShuttleItem']", "//ul[@id='drop_team_champs']");

			//Construct a value for champions hidden field
			String championsString = "user|||"
				+ mydriver.getValue("//ul[@id='div_CREATEWORK']/li[@class='psShuttleItem']/@itemval")
				+ "|||true|||"
				+ mydriver.getEval("window.document.getElementById('div_CREATEWORK').firstChild.innerHTML")
				+ "||||||";

			//Delete, delete the magical quotes!
			championsString = championsString.replaceAll("\"", "").replaceAll("'", "");

			//Set the hidden field value
			mydriver.getEval("window.document.getElementById('hidden_team_champs').value='" + championsString + "';");

		}

		if (!(null == nGPH.getDateFieldsList())) {
			Iterator<NewGatedProjectDateField> dFLIterator = nGPH.getDateFieldsList().iterator();
			while ( dFLIterator.hasNext() ){
				NewGatedProjectDateField dataField = dFLIterator.next();
				mydriver.type("//label[text()='" + dataField.getDateFieldName() + "']/../..//input[@type='text']", dataField.getDateFieldValue());
			}
		}

		//TODO Write some code to add team members

		//TODO Add step three handling if the form handler has some tags or fields

		mydriver.click("//body/div[@id='content']//form//input[@id='finish']");
		mydriver.waitForPageToLoad("30000");

	}

	private int activeStepNumber(){
		int stepsCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('content').getElementsByTagName('h2').length"));
		int i = 0;
		String stepClass;
		do {
			stepClass = mydriver.getAttribute("dom=document.getElementById('content').getElementsByTagName('h2')[" + String.valueOf(i) + "]@class");
			i++;
			} while((stepClass.trim().equals("inactive")) && (i < stepsCount));
		return i;
	}

	private String getDOMSelectOptionValue(String domSelectID, String optionText){
		int optionsCount = Integer.valueOf(mydriver.getEval("window.document.getElementById('"
				+ domSelectID + "').getElementsByTagName('option').length"));
		int i = 0;
		String currentOptionText;
		do {
			currentOptionText = mydriver.getText("dom=document.getElementById('"
					+ domSelectID + "').getElementsByTagName('option')[" + String.valueOf(i) + "]");
			i++;
			} while((!(currentOptionText.trim().equals(optionText))) && (i < optionsCount));
		return mydriver.getValue("dom=document.getElementById('"
				+ domSelectID + "').getElementsByTagName('option')[" + String.valueOf(i - 1) + "]");
	}

}
