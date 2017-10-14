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
 * @deprecated
 * @author selyaev_ag
 *
 */
public class SimpleMailSender {

    public void send(Mail mail){
        try
        {
            Properties props = System.getProperties();
            props.put("mail.smtp.host",mail.getMailHost());
            Session session = Session.getDefaultInstance(props, null);

            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mail.getMailer()));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getRecievers(), false));
            msg.setSubject(mail.getSubject());
            msg.setText(mail.getBody());
            msg.setSentDate(new Date());

            Transport.send(msg);
        }
        catch (MessagingException  me)
        {
        	me.printStackTrace();
        }
    }
}
