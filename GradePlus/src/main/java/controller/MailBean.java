package controller;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import javax.mail.Message.RecipientType;

import org.simplejavamail.email.Recipient;
import org.simplejavamail.email.*;
import org.simplejavamail.mailer.*;
import org.simplejavamail.*;

public class MailBean {

    public void sendMail(String to) {

        final String username = "gradeplusbremen@gmail.com";
        final String password = "Koschke123";;

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Testing Subject");
            message.setText("Hallo Gutfried von Tölle,"
                    + "\n\n No spami my emaili , please!");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMail2(String to){

        final Email email = new Email();

        email.setFromAddress("lollypop", "arbnorr3@gmail.com");
        email.setSubject("hey");
        email.addRecipient("C. Cane", to, RecipientType.TO);
        email.setText("We should meet up! ;)");
        email.setTextHTML("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>");
        new Mailer("smtp.gmail.com", 587, "gradeplusbremen@gmail.com", "Koschke123").sendMail(email);

    }

    private void sendMail3(String to) throws MessagingException{
        final String username = "gradeplusbremen@gmail.com";
        final String password = "Koschke123";
        final String subject = "Aktivierung Ihres Benutzerkontos";
        final String emailBody = "Dat is eine Aktivierung";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        javax.mail.Session session = javax.mail.Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                }
        );

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("shubham20.yeole@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(emailBody, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}