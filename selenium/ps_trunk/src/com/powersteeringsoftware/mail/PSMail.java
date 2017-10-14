package com.powersteeringsoftware.mail;

import com.powersteeringsoftware.libs.logger.PSLogger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * A simple class for mail objects.
 * <p>
 * Contains the body, the mail server, the receivers, the sender and a the
 * subject
 * </p>
 * <p> You can send this mail using class PSMailerManager
 */
public class PSMail {
    public static final String DEFAULT_MAIL_HOST = "hermes.cinteractive.com";
    public static final String DEFAULT_MAILER = "automatedtest@psteering.com";
    public static final String DEFAULT_SUBJECT = "no subject";
    private String contentType;

    /**
     * Message body
     */
    private StringBuilder body = new StringBuilder("");
    /**
     * SMTP server
     */
    private String mailHost;
    /**
     * Message Recipients
     */
    private StringBuilder receivers = new StringBuilder("");
    /**
     * The mail sender
     */
    private String mailer;
    /**
     * Message subject
     */
    private String subject;

    public PSMail() {
        setMailHost(DEFAULT_MAIL_HOST);
        setMailer(DEFAULT_MAILER);
        setSubject(DEFAULT_SUBJECT);
        setContentType(false);
    }

    public void setMailHost(String newMailHost) {
        mailHost = (newMailHost == null) ? "" : newMailHost;
    }

    public String getMailHost() {
        return mailHost;
    }

    public void addBodyLine(String reportLine) {
        body.append("\n" + ((reportLine == null) ? "" : reportLine));
    }

    public String getBody() {
        return body.toString();
    }

    public void clear() {
        body = new StringBuilder("");
    }

    public String getRecievers() {
        return receivers.toString();
    }

    public void addReciever(String receiver) {
        receivers.append((receiver == null) ? "" : receiver);
    }

    public String getMailer() {
        return this.mailer;
    }

    public void setMailer(String newMailer) {
        mailer = newMailer;
    }

    public void setSubject(String newSubject) {
        subject = newSubject;
    }

    public String getSubject() {
        return subject;
    }

    public void setContentType(boolean asHtml) {
        contentType = asHtml ? "text/html" : "text/plain";
    }

    public String getContentType() {
        return contentType;
    }

    public void send() throws MessagingException {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", getMailHost());
        PSLogger.debug("Mail host : " + getMailHost());
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(getMailer()));
        PSLogger.debug("Send from : " + new InternetAddress(getMailer()));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getRecievers(), false));
        PSLogger.debug("Send to : " + Arrays.toString(InternetAddress.parse(getRecievers())));
        msg.setSubject(getSubject());
        msg.setContent(getBody(), getContentType());
        msg.setSentDate(new Date());
        Transport.send(msg);
    }

}
