package selenium.managers;

import org.apache.log4j.Logger;

import selenium.driver.*;
import selenium.objects.*;

public class NewWorkManager {

	private MySeleniumDriver driver;
	private Logger logger;

	public NewWorkManager(MySeleniumDriver driver, Logger logger){
		this.driver = driver;
		this.logger = logger;
	}

	public void createNewWork(Project p){
		logger.info("Creating new " + p.getTerm() + " with name '" + p.getName() + "'");

		driver.click("dom=window.dojo.query('div#BrowseMenu a[href*=createNewWorkFormInBrowseMenu]')[0]");
		driver.waitForPageToLoad("30000");

		String javascriptQuery = 	"var options = window.dojo.query('#workType > option');" +
									"for (var i=0;i<options.length;i++){" +
									"var optionText = options[i].text.replace(/^\\W+/g,'');" +
									"if (optionText=='" + p.getTerm() + "'){options[i].value;break;}}";
		String workTypeOptionID = driver.getEval(javascriptQuery);

		if (workTypeOptionID.equals("null")) throw new IllegalStateException("No project type '" + p.getTerm() + "' found");

		driver.select("dom=window.dojo.byId('workType')", "value=" + workTypeOptionID);
		driver.click("dom=window.dojo.byId('next')");
		driver.waitForPageToLoad("30000");

		driver.type("dom=window.dojo.byId('name')", p.getName());
		//TODO Add parent selection
		//TODO Add tags selection
		driver.click("dom=window.dojo.byId('finish')");
		driver.waitForPageToLoad("30000");
	}

	public void createNewGatedProject(GatedProject gP) throws InterruptedException{
		logger.info("Creating new " + gP.getTerm() + " with name '" + gP.getName() + "'");

		driver.click("dom=window.dojo.query('div#BrowseMenu a[href*=createNewWorkFormInBrowseMenu]')[0]");
		driver.waitForPageToLoad("30000");

		String javascriptQuery = 	"var options = window.dojo.query('#workType > option');" +
									"for (var i=0;i<options.length;i++){" +
									"var optionText = options[i].text.replace(/^\\W+/g,'');" +
									"if (optionText=='" + gP.getTerm() + "'){options[i].value;break;}}";
		String workTypeOptionID = driver.getEval(javascriptQuery);

		if (workTypeOptionID.equals("null")) throw new IllegalStateException("No project type '" + gP.getTerm() + "' found");

		driver.select("dom=window.dojo.byId('workType')", "value=" + workTypeOptionID);
		driver.click("dom=window.dojo.byId('next')");
		driver.waitForPageToLoad("30000");

		driver.type("dom=window.dojo.byId('name')", gP.getName());

		//TODO Add parent selection
		//TODO Add tags selection

		if (!(null == gP.getChampionsList())) {
			//Only add the first champion for now
			driver.type("dom=window.dojo.query('div.col1 input.dnd-search')[0]", gP.getChampionsList().getFirst());
			driver.click("dom=window.dojo.query('div.col1 input.btn-white')[0]");
			driver.waitForElementToDisappear("dom=window.dojo.query('div.col1 li.dnd-msg-loading')[0]", "30000");

			//Construct a value for champions hidden field
			String championsString = "user|||"
				+ driver.getAttribute("dom=window.dojo.query('div.col1 li.dojoDndItem')[0]@itemval")
				+ "|||true|||"
				+ driver.getEval("window.dojo.query('div.col1 li.dojoDndItem')[0].innerHTML")
				+ "||||||";

			//Delete, delete the magical quotes!
			championsString = championsString.replaceAll("\"", "").replaceAll("'", "");

			//Set the hidden field value
			driver.getEval("window.dojo.byId('hidden_team_champs').value='" + championsString + "';");

		}

		//automatically fill out the constraints if any were found
		int tagsCount = Integer.valueOf(driver.getEval("window.dojo.query('div.dijitInputField input.dijitReset').length"));
		if (tagsCount > 0){
			for (int i = 0; i < tagsCount; i++){
				String inputId = driver.getEval("window.dojo.query('div.dijitInputField input.dijitReset')[" + i + "].id");
				driver.getEval("window.dijit.byId('" + inputId + "').setValue(new Date())");
			}
		}

		//TODO Add team members

		//TODO Add step three handling if the form handler has some tags or fields

		driver.click("dom=window.dojo.byId('finish')");
		driver.waitForPageToLoad("30000");

		//TODO Everything for this method
	}

}
