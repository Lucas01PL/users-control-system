package com.userscontrolsystem.userscontrolmessage.service;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Value("${smtp.email.host}")
	private	String host;
	
	@Value("${smtp.email.auth}")
	private	Boolean auth;
	
	@Value("${smtp.email.port}")
	private	String port;
	
	@Value("${smtp.email.ssl.enable}")
	private	Boolean sslEnable;
	
	@Value("${email.user}")
	private	String email;
	
	@Value("${email.password}")
	private String password;
	
	public void send(String recipient, String title, String body) {
		
        Properties properties = System.getProperties();
        
        setProperties(properties);

        Session session = createSession(properties);

        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(email));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

            message.setSubject(title);

            message.setText(body);

            System.out.println("sending...");
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
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.ssl.enable", sslEnable);
        properties.put("mail.smtp.auth", auth);
	}

}