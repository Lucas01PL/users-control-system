package com.userscontrolsystem.userscontrolmessage.service;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.Authenticator;

import org.springframework.stereotype.Component;

@Component
public class EmailService {
	
	String email = "users.control.system2021@gmail.com";
	String password = "usuario2022";
	String host = "smtp.gmail.com";
	Boolean auth = true;
	String port = "465";
	Boolean sslEnable = true;	
	
	public void send(String recipient, String title, String body) {
		
		// Get system properties
        Properties properties = System.getProperties();
        
        // Setup mail server
        setProperties(properties);

        // Get the Session object.// and pass username and password
        Session session = createSession(properties);

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(email));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            // Set Subject: header field
            message.setSubject(title);

            // Now set the actual message
            message.setText(body);

            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

	private Session createSession(Properties properties) {
		return Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
	}
		
	private void setProperties(Properties properties) {
		// Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", sslEnable);
        properties.put("mail.smtp.auth", auth);
	}

}