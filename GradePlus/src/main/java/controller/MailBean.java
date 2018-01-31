package controller;
import common.model.User;

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


public class MailBean {

    public static void main(User user) {

        final String username = "gradeplusbremen@gmail.com";
        final String password = "Koschke123";

        //Properties zur korrekten Verbindung zum Google smtp Server.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        //Erzeugung des Session objektes, bekommt properties und Authentifizierung für googlemail.
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username,password);
                    }
                });

        try {


            Message message = new MimeMessage(session);
            //Versender der Email
            message.setFrom(new InternetAddress(username));
            //Empfänger der Email, kann auch eine Liste sein.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail()));
            //Email-Betreff
            message.setSubject("Testing Subject");
            //Email-Text
            message.setText("Hallo" + user.getGivenName() + ","
                    + "\n\n Hiermit bestätigen wir ihre Registrierung bei Gradeplus mit folgenden Daten"
                    + "\n\n Nutzername: " + user.getUsername()
                    + "\n\n Nachname: " + user.getSurname()
                    + "\n\n Vorname: " + user.getGivenName()
                    + "\n\n Matrikelnummer: " + user.getMatrNr()
                    + "\n\n E-mail Adresse: " + user.getEmail()
                    + "\n\n\n Bei weiteren Fragen stehen wir Ihnen unter dieser Email zur Verfügung");

            //Versendung der Email.
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}