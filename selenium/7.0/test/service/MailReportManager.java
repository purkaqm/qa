package test.service;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.*;

public class MailReportManager {
	private String _report;

	public final static MailReportManager INSTANCE = new MailReportManager();

	protected MailReportManager() {
		_report = new String("");
	}

	public void addReportLine(String reportLine){
		_report = _report + "\n" + reportLine;
	}

	public void sendReport(String sendTo, String mailSubj, String reportString){
	    try
	    {
	        Properties props = System.getProperties();
	        // -- Attaching to default Session, or we could start a new one --
	        props.put("mail.smtp.host", "hermes.cinteractive.com");
	        Session session = Session.getDefaultInstance(props, null);
	        // -- Create a new message --
	        Message msg = new MimeMessage(session);
	        // -- Set the FROM and TO fields --
	        msg.setFrom(new InternetAddress("automatedtest@psteering.com"));
	        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo, false));
	        // -- Set the subject and body text --
	        msg.setSubject(mailSubj);
	        msg.setText(reportString);
	        // -- Set some other header information --
	        msg.setSentDate(new Date());
	        // -- Send the message --
	        Transport.send(msg);
	    }
	    catch (Exception ex)
	    {
	      System.out.println(ex);
	    }
	}
}
