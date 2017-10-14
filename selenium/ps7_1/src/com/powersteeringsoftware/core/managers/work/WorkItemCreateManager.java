package com.powersteeringsoftware.core.managers.work;

import org.apache.log4j.Logger;
import org.testng.Assert;

import com.powersteeringsoftware.core.adapters.MainMenuAdapter;
import com.powersteeringsoftware.core.objects.works.GatedProject;
import com.powersteeringsoftware.core.objects.works.WorkItem;
import com.powersteeringsoftware.core.server.SeleniumDriverSingleton;
import com.powersteeringsoftware.core.util.CoreProperties;
import com.powersteeringsoftware.core.util.session.TestSession;
import com.powersteeringsoftware.core.util.session.TestSessionObjectNames;
import com.powersteeringsoftware.core.util.waiters.ElementBecomeDisappearedWaiter;

public class WorkItemCreateManager {

	private Logger logger = Logger.getLogger(WorkItemCreateManager.class);
	public static final String HIDDEN_TEAM_71 = "hidden_team_all";
	public static final String HIDDEN_TEAM_70 = "hidden_team_champs";

	public WorkItemCreateManager(){
	}

	private void doClickNextStep(){
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('next')");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void doClickFinish(){
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.byId('finish')");
		SeleniumDriverSingleton.getDriver().waitForPageToLoad(CoreProperties.getWaitForElementToLoadAsString());
	}

	private void enterName(WorkItem wi){
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.byId('name')", wi.getName());
	}

	private void selectWorkTemplate(WorkItem wi) {
		String javascriptQuery = 	"var options = window.dojo.query('#workType > option');" +
									"for (var i=0;i<options.length;i++){" +
									"var optionText = options[i].text.replace(/^\\W+/g,'');" +
									"if (optionText=='" + wi.getTerm() + "'){options[i].value;break;}}";
		String workTypeOptionID = SeleniumDriverSingleton.getDriver().getEval(javascriptQuery);

		if (workTypeOptionID.equals("null")) throw new IllegalStateException("No project type '" + wi.getTerm() + "' found");

		SeleniumDriverSingleton.getDriver().select("dom=window.dojo.byId('workType')", "value=" + workTypeOptionID);
	}

	private void fillFields(WorkItem wi){
		logger.debug("Creating new " + wi.getTerm() + " with name '" + wi.getName() + "'");

		MainMenuAdapter.clickBrowseNewWork();

		selectWorkTemplate(wi);

		doClickNextStep();

		enterName(wi);
	}

	public void createProject(WorkItem p){
		fillFields(p);
		doClickFinish();
	}




	/**
	 * Construct a value for champions hidden field while creating new gated project
	 * @return value for adding into the value
	 */
	private String getChampionOnCreatingString71(GatedProject gP) {
		//Only add the first champion for now
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.query('div.col1 input.dnd-search')[0]", gP.getChampionsList().getFirst());
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('div.col1 input.btn-white')[0]");

		ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.col1 li.dnd-msg-loading')[0]");
		//SeleniumWaiter.waitForElementToDisappear("dom=window.dojo.query('div.col1 li.dnd-msg-loading')[0]", "30000");

		String attr = SeleniumDriverSingleton.getDriver().getAttribute("dom=window.dojo.query('div.col1 li.dojoDndItem')[0]@itemval");
		String eval = SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.col1 li.dojoDndItem')[0].innerHTML");
		String championsString = "champ|||"
			+ attr
			+ "|||true|||"
			+ eval
			+ "||||||";

		//Delete, delete the magical quotes!
		championsString = championsString.replaceAll("\"", "").replaceAll("'", "");

		return championsString;
	}

	private String getChampionOnCreatingString70(GatedProject gP) {
		//Only add the first champion for now
		SeleniumDriverSingleton.getDriver().type("dom=window.dojo.query('div.col1 input.dnd-search')[0]", gP.getChampionsList().getFirst());
		SeleniumDriverSingleton.getDriver().click("dom=window.dojo.query('div.col1 input.btn-white')[0]");

		ElementBecomeDisappearedWaiter.waitElementDissapeared("dom=window.dojo.query('div.col1 li.dnd-msg-loading')[0]");
		//SeleniumWaiter.waitForElementToDisappear("dom=window.dojo.query('div.col1 li.dnd-msg-loading')[0]", CoreProperties.getWaitForElementToLoadAsString());

		String attr = SeleniumDriverSingleton.getDriver().getAttribute("dom=window.dojo.query('div.col1 li.dojoDndItem')[0]@itemval");
		String eval = SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.col1 li.dojoDndItem')[0].innerHTML");
		String championsString = "user|||"
			+ attr
			+ "|||true|||"
			+ eval
			+ "||||||";

		//Delete, delete the magical quotes!
		championsString = championsString.replaceAll("\"", "").replaceAll("'", "");

		return championsString;
	}

	public String getVersionedChampionString(GatedProject gP) throws InterruptedException{
		String version = (String)TestSession.getObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey());
		if(version.contains("7.0")){
			return getChampionOnCreatingString70(gP);
		}
		if (version.contains("7.1")){
			return getChampionOnCreatingString71(gP);
		}

		Assert.fail("Application version is not 7.0 or 7.1.");
		return null;
	}

	public String getVersionedHiddenTeam(){
		String version = (String)TestSession.getObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey());
		if(version.contains("7.0")){
			return HIDDEN_TEAM_70;
		}
		if (version.contains("7.1")){
			return HIDDEN_TEAM_71;
		}

		Assert.fail("Application version is not 7.0 or 7.1.");
		return null;
	}

	public void createGatedProject(GatedProject gP) {

		fillFields(gP);

		setChampionOnCreating(gP);

		//automatically fill out the constraints if any were found
		int tagsCount = Integer.valueOf(SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitInputField input.dijitReset').length"));
		if (tagsCount > 0){
			for (int i = 0; i < tagsCount; i++){
				String inputId = SeleniumDriverSingleton.getDriver().getEval("window.dojo.query('div.dijitInputField input.dijitReset')[" + i + "].id");
				SeleniumDriverSingleton.getDriver().getEval("window.dijit.byId('" + inputId + "').setValue(new Date())");
			}
		}

		doClickFinish();
		setWorkItemId(gP);
	}

	protected void setWorkItemId(GatedProject gP){
		String[] uid = new String(SeleniumDriverSingleton.getDriver().getLocation()).split("epage\\?sp=");
		//TestSession.putObject(gP.getName(),uid[1]);
		gP.setUid(uid[1]);
	}

	private void setChampionOnCreating71(GatedProject gP) {
		if (!(null == gP.getChampionsList())) {

			String championString = getChampionOnCreatingString71(gP)+"user";

			String oldValue = SeleniumDriverSingleton.getDriver().getValue("dom=window.dojo.byId('"+getVersionedHiddenTeam()+"')");
			oldValue = oldValue.replaceAll("\"", "").replaceAll("'", "");

			String jsQuery = "window.dojo.byId('"+getVersionedHiddenTeam()+"').value='"
			+ oldValue
			+ "###"
			+ championString
			+ "';";

			SeleniumDriverSingleton.getDriver().getEval(jsQuery);
		}
	}

	private void setChampionOnCreating70(GatedProject gP) {
		if (!(null == gP.getChampionsList())) {

			String championString = getChampionOnCreatingString70(gP)+"user";

			String oldValue = SeleniumDriverSingleton.getDriver().getValue("dom=window.dojo.byId('"+getVersionedHiddenTeam()+"')");
			oldValue = oldValue.replaceAll("\"", "").replaceAll("'", "");

			String jsQuery = "window.dojo.byId('"+getVersionedHiddenTeam()+"').value='"
			+ championString
			+ "';";

			SeleniumDriverSingleton.getDriver().getEval(jsQuery);
		}
	}

	private void setChampionOnCreating(GatedProject gP){
		String version = (String)TestSession.getObject(TestSessionObjectNames.APPLICATION_VERSION.getObjectKey());
		if(version.contains("7.0")){
			setChampionOnCreating70(gP);
		} else if (version.contains("7.1")){
				setChampionOnCreating71(gP);
		} else {
			Assert.fail("Tests support only versions 7.0 and 7.1");
		}
	}
}
