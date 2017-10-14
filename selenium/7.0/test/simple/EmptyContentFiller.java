package test.simple;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.WriterAppender;

import selenium.driver.MySeleniumDriver;
import selenium.managers.*;
import test.service.*;

import selenium.objects.*;

public class EmptyContentFiller {

	public static void main(String[] args) throws Exception {
		Exception e = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String subj = "Empty Content Configuration Test - " + formatter.format(new Date());

		Logger logger = Logger.getLogger("EmptyContentConfigurationTest");
		StringWriter writer = new StringWriter();
		logger.addAppender(new WriterAppender(new SimpleLayout(), writer));
		logger.info("Starting new test session for Empty Content Configuration Test");

		MySeleniumDriver driver = null;

		Properties config = new Properties();
		logger.info("Opening the Empty Content Configuration Test properties file.");
		java.net.URL url = ClassLoader.getSystemResource("tests/EmptyContentConfigurationTest.properties");
		logger.info("Test properties file name resolved as " + url);
		config.load(url.openStream());

		driver = BrowserHelper.startBrowser(config.getProperty("sserver.url"),
				config.getProperty("context.url"), config.getProperty("browser.launcher"), logger);

		try {

			ContextDBManager.getConnection(config.getProperty("context.dbcstring"), logger).killActiveConnectionAndRestoreDB(config.getProperty("context.dbname"),
					config.getProperty("context.dbbupath"));
			ResinJMXManager.getConnection(config.getProperty("context.jmx"), logger).restart().waitUntilServerIsReady(driver, config.getProperty("context.url"));

			LoginManager lM = new LoginManager(driver, logger);
			subj = subj + " - " + lM.login(config.getProperty("context.user"), config.getProperty("context.pass"));

			TagManager tM = new TagManager(driver, logger);
			Tag tag = new Tag("Planet","This should be a non-hierarchical tag.");

			tag.addTypeOfPeople("Users");
			tag.addTypeOfWorks("Work Items");
			tag.addTypeOfWorks("Gated Projects");
			tag.addTagValue("Earth");
			tag.addTagValue("Mercury");
			tag.addTagValue("Venus");
			tag.addTagValue("Jupiter");
			tag.addTagValue("Mars");
			tag.addTagValue("Saturn");
			tM.addTag(tag);

			tag = new Tag("Location","This should be a hierarchical, multi-select tag.");
			tag.addTypeOfPeople("Users");
			tag.addTypeOfWorks("Work Items");
			tag.addTypeOfWorks("Gated Projects");
			tag.setPropMultiple(true);
			tag.setPropHierarchical(true);
			tag.addTagValue("North America");
			tag.addTagValue("United States", "North America");
			tag.addTagValue("Massachusetts", "United States");
			tag.addTagValue("Boston", "Massachusetts");
			tag.addTagValue("Asia");
			tag.addTagValue("Russia","Asia");
			tag.addTagValue("Moscow","Russia");
			tag.addTagValue("Australia");
			tag.addTagValue("Sydney","Australia");
			tag.addTagValue("Melbourne","Australia");
			tag.addTagValue("Perth","Australia");
			tM.addTag(tag);


			CustomFieldsTemplateManager cFTMan = new CustomFieldsTemplateManager(driver, logger);

			CustomFieldsTemplate cFT = new CustomFieldsTemplate("General custom fields");
			cFT.setAssocWithTemplates(true);
			cFT.setAssocWithWorkItems(true);
			cFT.setAssocWithUnexpWorkItems(true);
			cFT.setAssocWithGatedProjects(true);
			cFT.addYesNoButtonField("Sample Yes-No button", "Just a sample yes-no radio button created by an automated script");
			String[] checkboxes = {"Red", "Green", "Blue"};
			cFT.addCheckboxesField("Sample Checkboxes", "Just sample checkboxes created by an automated script", checkboxes);

			cFTMan.createFromTemplate(cFT);

			ProcessManager pM = new ProcessManager(driver, logger);

			PSProcess DMAIC = new PSProcess("DMAIC", "This is a sample DMAIC process created by an automated script");
			DMAIC.addPhase("Define", null);
			DMAIC.addPhase("Measure", null);
			DMAIC.addPhase("Analyze", null);
			DMAIC.addPhase("Improve", null);
			DMAIC.addPhase("Control", null);
			pM.addProcess(DMAIC);

			PSProcess CDDDRC = new PSProcess("CDDDRC", "This is a sample CDDDRC process created by an automated script");
			CDDDRC.addPhase("Concept", "10");
			CDDDRC.addPhase("Define", "20");
			CDDDRC.addPhase("Design", "45");
			CDDDRC.addPhase("Development", "75");
			CDDDRC.addPhase("Release", "95");
			CDDDRC.addPhase("Close", "100");
			pM.addProcess(CDDDRC);

			WorkTemplateManager wTM = new WorkTemplateManager(driver, logger);
			WorkTemplate DMAICTemplate = new WorkTemplate("DMAIC Work Template");
			RootGatedProject DMAICRoot = new RootGatedProject("DMAIC Work Template Root", DMAIC.getName());
			DMAICRoot.setStatusReporting(RootGatedProject.STATUS_REPORTING_FREQUENCY.NO_FREQUENCY);
			DMAICRoot.setInheritPermissions(true);
			DMAICRoot.setInheritControls(true);
			DMAICRoot.setWebFolder(false);
			DMAICRoot.setControlCost(true);
			wTM.createNewWorkTemplate(DMAICTemplate, DMAICRoot);

			WorkTemplate CDDDRCTemplate = new WorkTemplate("CDDDRC Work Template");
			RootGatedProject CDDDRCRoot = new RootGatedProject("CDDDRC Work Template Root", CDDDRC.getName());
			CDDDRCRoot.setStatusReporting(RootGatedProject.STATUS_REPORTING_FREQUENCY.NO_FREQUENCY);
			CDDDRCRoot.setInheritPermissions(true);
			CDDDRCRoot.setInheritControls(true);
			CDDDRCRoot.setWebFolder(false);
			CDDDRCRoot.setControlCost(false);
			wTM.createNewWorkTemplate(CDDDRCTemplate, CDDDRCRoot);

			NewWorkManager nWM = new NewWorkManager(driver, logger);

			GatedProject dmaicGP = GatedProject.create("DMAIC test project", DMAICTemplate.getName());
			dmaicGP.addChampion("Admino");
			nWM.createNewGatedProject(dmaicGP);

			GatedProject cdddrcGP = GatedProject.create("CDDDRC test project", CDDDRCTemplate.getName());
			cdddrcGP.addChampion("Admino");
			nWM.createNewGatedProject(cdddrcGP);

		} catch(Exception ex) {
			e = ex;
		}

		driver.stop();

		if (null==e) {
			logger.info("PASSED Internal Acceptance Test");
			//MailReportManager.INSTANCE.sendReport(config.getProperty("mail.report.recipients"), "PASSED: " + subj, writer.toString());
			new FileReportManager(subj, true, "PASSED").saveProperties(config.getProperty("report.file.name"), "Create New Content Test");
			System.out.println(writer.toString());
		} else {
			logger.info("FAILED Internal Acceptance Test");
			logger.error(e.toString());
			//MailReportManager.INSTANCE.sendReport(config.getProperty("mail.report.recipients"), "FAILED: " + subj, writer.toString());
			new FileReportManager(subj, false, "FAILED").saveProperties(config.getProperty("report.file.name"), "Create New Content Test");
			System.out.println(writer.toString());
			throw e;
		}

	}


}
