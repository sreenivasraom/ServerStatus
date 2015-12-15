/**
 * 
 */
package com.sree;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author sreenivasrao.m
 * 
 */
public class Mailer {

	public String sendMail(String mailMessage) {
		Properties mailProps = new Properties();
		try {
			mailProps.load(Mailer.class.getResourceAsStream("/mail.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String to = mailProps.getProperty("tomail");
		String cclist = mailProps.getProperty("ccmails");
		// Sender's email ID needs to be mentioned
		final String userName = mailProps.getProperty("smtpauthuser");
		final String password = mailProps.getProperty("smtpauthpwd");

		// Get the Session object.
		Session session = Session.getInstance(mailProps,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(userName, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session);
			// Set From: header field of the header.
			message.setFrom(new InternetAddress(userName));
			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			// Set CC: header field of the header.
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cclist));
			// Set Subject: header field
			message.setSubject(mailProps.getProperty("mailsub"));
			// Send the actual HTML message, as big as you like
			message.setContent("<h1>" + mailMessage + "</h1>", "text/html");
			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return "Mail sent.";
	}

}
