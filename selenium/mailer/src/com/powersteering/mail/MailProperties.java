package com.powersteering.mail;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class MailProperties{

	private String mailReceiver;
	private String mailMailer;
	private String mailHost;
	private String resultTextSuccefully;
	private String resultTextFailed;


	enum MailPropertiesEnum{
		MAIL_RECEIVER("mail.receiver"),
		MAIL_MAILER("mail.mailer"),
		MAIL_HOST("mail.host"),
		RESULT_TEXT_SUCCESFULLY("result.text.succesfully"),
		RESULT_TEXT_FAILED("result.text.failed");

		private String propertyValue;

		MailPropertiesEnum(String aProeprtyValue){
			propertyValue = aProeprtyValue;
		}

		public String getPropertyValue(){
			return propertyValue;
		}
	}


	public MailProperties(){
		this.mailHost = new String();
		this.mailMailer = new String();
		this.mailReceiver = new String();
	}

	public String getMailHost(){
		return this.mailHost;
	}

	public String getMailMailer(){
		return this.mailMailer;
	}

	public String getMailReceiver(){
		return this.mailReceiver;
	}

	public String getResultTextSuccesfully(){
		return this.resultTextSuccefully;
	}

	public String getResultTextFailed(){
		return this.resultTextFailed;
	}

	public void setMailHost(String newMailHost){
		this.mailHost = (null==newMailHost)?"":newMailHost;
	}

	public void setMailMailer(String newMailMailer){
		this.mailMailer = (null==newMailMailer)?"":newMailMailer;
	}

	public void setMailReceiver(String newMailReceiver){
		this.mailReceiver = (null==newMailReceiver)?"":newMailReceiver;
	}

	public void setResultTextFailed(String message){
		this.resultTextFailed = (null==message)?"":message;
	}

	public void setResultTextSuccesfully(String message){
		this.resultTextSuccefully = (null==message)?"":message;
	}


	public void loadProperties(File file) throws MailException, IOException {
		if(!file.exists()){
			throw new MailException("Properties file"+file+" is not exist. We can't load properties.");
		}

		Properties prop = new Properties();
		Reader reader;
		reader = new FileReader(file);
		prop.load(reader);
		reader.close();

		mailMailer = prop.getProperty(MailPropertiesEnum.MAIL_MAILER.propertyValue);
		mailHost = prop.getProperty(MailPropertiesEnum.MAIL_HOST.propertyValue);
		mailReceiver = prop.getProperty(MailPropertiesEnum.MAIL_RECEIVER.propertyValue);
		resultTextFailed = prop
				.getProperty(MailPropertiesEnum.RESULT_TEXT_FAILED.propertyValue);
		resultTextSuccefully = prop
				.getProperty(MailPropertiesEnum.RESULT_TEXT_SUCCESFULLY.propertyValue);
	}


	public void saveProperties(File file, String comments)
			throws MailException, IOException {
		if(!file.exists()){
			throw new MailException("Properties file"+file+" is not exist. We can't load properties.");
		}

		Properties prop = new Properties();
		prop.put(MailPropertiesEnum.MAIL_MAILER.propertyValue, mailMailer);
		prop.put(MailPropertiesEnum.MAIL_HOST.propertyValue, mailHost);
		prop.put(MailPropertiesEnum.MAIL_RECEIVER.propertyValue, mailReceiver);
		prop.put(MailPropertiesEnum.RESULT_TEXT_FAILED.propertyValue, resultTextFailed);
		prop.put(MailPropertiesEnum.RESULT_TEXT_SUCCESFULLY.propertyValue, resultTextSuccefully);
		Writer writer;
		writer = new FileWriter(file);
		prop.store(writer, comments);
	}

}
