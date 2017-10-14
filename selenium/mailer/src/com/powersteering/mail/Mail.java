package com.powersteering.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * A simple class for mail objects.
 * <p>
 * Contains the body, the mail server, the receivers, the sender and a the
 * subject
 * </p>
 * <p> You can send this mail using class PSMailerManager
 *
 */
public class Mail {
    public final static Mail INSTANCE = new Mail();
    public static final String DEFAULT_MAIL_HOST = "hermes.cinteractive.com";
    public static final String DEFAULT_MAILER = "automatedtest@psteering.com";
    public static final String DEFAULT_SUBJECT = "no subject";

    /**
     * The mail body
     */
	private StringBuilder body;
	/**
     * The mail server
     */
	private String mail_host;
	/**
     * The mail receivers
     */
	private StringBuilder receivers;
	/**
     * The mail sender
     */
	private String mailer;
	/**
     * The mail subject
     */
	private String subject;

    public Mail() {
    	body = new StringBuilder("");
        mail_host = DEFAULT_MAIL_HOST;
        receivers = new StringBuilder("");
        mailer = DEFAULT_MAILER;
        subject = DEFAULT_SUBJECT;
    }

    public void setMailHost(String newMailHost){
    	mail_host=(newMailHost==null)?"":newMailHost;
    }

    public String getMailHost(){
    	return mail_host;
    }

    public void addBodyLine(String reportLine){
    	body.append("\n" + ((reportLine==null)?"":reportLine));
    }

    public String getBody(){
    	return body.toString();
    }

    public String getRecievers(){
    	return receivers.toString();
    }

    public void addReciever(String receiver){
    	receivers.append((receiver==null)?"":receiver);
    }

    public String getMailer(){
    	return this.mailer;
    }

    public void setMailer(String newMailer){
    	mailer = newMailer;
    }

    public void setSubject(String newSubject){
    	subject = newSubject;
    }

    public String getSubject(){
    	return subject;
    }

    public static void send(Mail mail) throws MessagingException {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mail.getMailHost());
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mail.getMailer()));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail
				.getRecievers(), false));
		msg.setSubject(mail.getSubject());
		msg.setText(mail.getBody());
		msg.setSentDate(new Date());

		Transport.send(msg);

	}

}
