package com.powersteering.mail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;
import com.powersteering.mail.TestResultReporter;
import com.powersteering.mail.TestResultFormatter.TestResultFormat;
import javax.mail.MessagingException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Manager for sending mail (see class Mail)
 *
 *
 */
public class PSMailerManager{

	public static final String FILE_PREFIX = "mailit_";
	public static final String LOG_PATH = "."+File.separator+"logs";	public static final String CONF_DIR = "."+File.separator+"conf"+File.separator;
	public static final String CONF_FILENAME = "mail.properties";
	public static final String CONF = CONF_DIR+CONF_FILENAME;

	private MailProperties mailProperties;
	private Mail mail;

	private File[] files;
	private Logger log = Logger.getLogger(PSMailerManager.class);

	public static class MailFileFilter implements FilenameFilter {
		public MailFileFilter(){
		}
		public boolean accept(File dir, String name) {
			return name.startsWith(FILE_PREFIX);
		}
	}


	public PSMailerManager() throws MailException, IOException{
		mailProperties = new MailProperties();
		mailProperties.loadProperties(new File(CONF));

		mail = new Mail();
		mail.setMailer(mailProperties.getMailMailer());
		mail.addReciever(mailProperties.getMailReceiver());
		mail.setMailHost(mailProperties.getMailHost());
	}


	public void manage() throws MailException, IOException, MessagingException{



		File logdir = new File(LOG_PATH);

		if(!logdir.exists()){throw new MailException("Log path has not found");}

		setFiles(logdir.listFiles(new MailFileFilter()));

		if(files.length==0){throw new MailException("Log files have not found");}

		boolean allTestsPassedOK = createEmailContent();

		mail.setSubject((allTestsPassedOK) ? mailProperties
				.getResultTextSuccesfully() : mailProperties
				.getResultTextFailed());

		Mail.send(mail);

		deleteLogs();
	}


	private boolean createEmailContent()
			throws IOException, FileNotFoundException, MailException {
		boolean allTestsPassedOK = true;

		Properties resultProp = new Properties();
		for (int i = 0; i < files.length; i++) {

				resultProp.load(new FileReader(files[i]));

				TestResultReporter reporter = new TestResultReporter();
				reporter.loadProperties(files[i]);
				reporter.setTestName("Test #"+(i+1)+": "+reporter.getTestName());

				String mailBody = TestResultFormatter.formatMailBody(reporter,
						TestResultFormat.SIMPLE);

				allTestsPassedOK = allTestsPassedOK
						&& resultProp.getProperty(TestResultReporter.PROP_NAME_RESULT_ISOK).equals("1");

				mail.addBodyLine("");
				mail.addBodyLine(mailBody);
				mail.addBodyLine("");

		}

		return allTestsPassedOK;
	}

	/**
	 * After sending email we should delete log files from log directory
	 */
	private void deleteLogs(){
		for(File file: getFiles()){
			try{
				file.delete();
			}catch(SecurityException se){
				log.warn("Security probllem has appeared while deleting file "+file.getAbsolutePath());
			}
		}
	}

	public static void main(String[] args) throws Exception {
		PropertyConfigurator.configure("./conf/log4j.properties");
		PSMailerManager mailerManager = new PSMailerManager();
		mailerManager.manage();
	}


	private File[] getFiles() {
		return files;
	}


	public void setFiles(File[] files) {
		this.files = files;
	}

}
