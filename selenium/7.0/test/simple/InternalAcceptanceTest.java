package test.simple;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.log4j.*;

import selenium.driver.MySeleniumDriver;
import selenium.managers.*;
import selenium.objects.*;
import test.service.*;

public class InternalAcceptanceTest {

	public static void main(String[] args) throws IOException {
		Exception e = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z", Locale.US);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		String subj = "Internal Acceptance Test - " + formatter.format(new Date());

		Logger logger = Logger.getLogger("InternalAcceptanceTest");
		StringWriter writer = new StringWriter();
		logger.addAppender(new WriterAppender(new SimpleLayout(), writer));
		logger.info("Starting new test session for Internal Acceptance Test");

		Properties config = new Properties();
		logger.info("Opening the Internal Acceptance Test configuration file.");
		java.net.URL url = ClassLoader.getSystemResource("tests/InternalAcceptanceTest.properties");
		logger.info("Test config file name resolved as " + url);
		config.load(url.openStream());

		MySeleniumDriver driver = BrowserHelper.startBrowser(config.getProperty("sserver.url"),
				config.getProperty("context.url"), config.getProperty("browser.launcher"), logger);

		try {

			NewWorkManager nWM = new NewWorkManager(driver, logger);
			ThreadsListManager tLM = new ThreadsListManager(driver, logger);
			ThreadManager tM = new ThreadManager(driver, logger, config.getProperty("local.afolder"));
			ThreadBlockManager tBM = new ThreadBlockManager(driver, logger,	config.getProperty("sserver.afolder"));
			IssuesListManager iLM = new IssuesListManager(driver, logger);
			IssueManager iM = new IssueManager(driver, logger,	config.getProperty("sserver.afolder"));
			DocumentsListManager dLM = new DocumentsListManager(driver, logger,	config.getProperty("sserver.afolder"));
			DocumentManager dM = new DocumentManager(driver, logger, config.getProperty("local.afolder"), config.getProperty("sserver.afolder"));
			WorkBreakdownManager wBM = new WorkBreakdownManager(driver, logger);

			LoginManager lM = new LoginManager(driver, logger);
			subj = subj + " - " + lM.login(config.getProperty("context.user"), config.getProperty("context.pass"));

			Project p = Project.createWithNameTimeStamped("IACWork", config.getProperty("project.term"));
			nWM.createNewWork(p);

			ThreadBlock tD = new ThreadBlock("TestDiscussion", "This is just a test discussion created by an automated script.", p);
			tD.addAttachment(new FileAttachment("dtest001.txt",	"DiscussionTextFile001"));
			tD.addAttachment(new FileAttachment("dtest002.txt",	"DiscussionTextFile002"));
			tD.setIssue(new Issue(3));

			tLM.goToNewThreadPage();
			tBM.createNewBlock(tD);
			tM.testAttachments(tD);
			tM.escalate(tD);
			iM.addIssueToBlock(tD);
			tM.testAttachments(tD);
			tM.deEscalate(tD);
			tM.testAttachments(tD);

			ThreadBlock tI = new ThreadBlock("TestIssue", "This is just a test issue created by an automated script.", p);
			tI.addAttachment(new FileAttachment("itest001.txt",	"IssueTextFile001"));
			tI.addAttachment(new FileAttachment("itest002.txt",	"IssueTextFile002"));
			tI.setIssue(new Issue(3));

			ThreadBlock rB = new ThreadBlock("TestReply", "This is just a test reply created by an automated script.",	p);
			rB.addAttachment(new FileAttachment("itest003.txt",	"IssueTextFile003"));
			rB.addAttachment(new FileAttachment("itest004.txt",	"IssueTextFile004"));
			rB.setIssue(new Issue(3));

			iLM.goToNewIssuePage();
			iM.createNewBlockWithIssue(tI);
			tM.testAttachments(tI);
			tM.reply(tI);
			tBM.createNewBlock(rB);
			tM.testAttachments(rB);
			tM.escalate(rB);
			iM.addIssueToBlock(rB);
			tM.testAttachments(rB);
			tM.close(rB);
			tM.testAttachments(rB);

			FileAttachment fA1 = new FileAttachment("utest001.txt",	"UpdateTextFile001");
			FileAttachment fA2 = new FileAttachment("utest002.txt",	"UpdateTextFile002");

			dLM.addDocument(fA1);

			Thread.sleep(5000);

			dLM.update(fA1);
			dM.update(fA2);
			dM.checkVersion(1, fA1);
			dM.checkVersion(2, fA2);

			Project child00001 = Project.create("Child00001", p.getTerm());
			Project child00002 = Project.create("Child00002", p.getTerm());
			Project[] children = { child00001, child00002 };

			wBM.addChildren(p, children);
			wBM.indent(child00002);
			wBM.setPredecessor(child00001, child00002);

		} catch (Exception ex) {
			e = ex;
			logger.error(e.toString());
		}

		driver.stop();

		if (null==e) {
			logger.info("PASSED Internal Acceptance Test");
			new FileReportManager(subj, true, "PASSED").saveProperties(config.getProperty("report.file.name"), "Internal Acceptance Test");
			System.out.println(writer.toString());
		} else {
			logger.info("FAILED Internal Acceptance Test");
			logger.info(e.toString());
			new FileReportManager(subj, false, "FAILED").saveProperties(config.getProperty("report.file.name"), "Internal Acceptance Test");
			System.out.println(writer.toString());
		}
	}

}
