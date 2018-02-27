/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 AG Softwaretechnik, University of Bremen:
 * Arbnor Miftari
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package controller;

import java.util.Properties;

import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.model.User;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean ist der Controller für die Registrierungs-Mail.
 *
 * @author Arbnor Miftari
 * @version 2018-01-2018
 */
@Named
@RequestScoped
public class RegisterMailBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(RegisterMailBean.class);

    /**
     * Eine Loginunabhängige Funktion, die dem sich registrierenden Benutzer, nach der
     * Registrierung eine Bestätigungsmail zusendet und  in der alle relevanten Daten vermerkt
     * sind.
     * 
     * @param pUser
     *            Der Empfänger der Registrierungsmail.
     */

    public static void registerMail(User pUser) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradeplusbremen@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Ihre Registrierung bei GradePlus");
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Hiermit bestätigen wir ihre Registrierung bei Gradeplus mit folgenden Daten"
                    + "\n\n Nutzername: "
                    + pUser.getUsername()
                    + "\n\n Nachname: "
                    + pUser.getSurname()
                    + "\n\n Vorname: "
                    + pUser.getGivenName()
                    + "\n\n Matrikelnummer: "
                    + pUser.getMatrNr()
                    + "\n\n E-mail Adresse: "
                    + pUser.getEmail()
                    + "\n\n\n Bei weiteren Fragen stehen wir Ihnen unter dieser Email zur Verfügung");


            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }


    public static void ilvRemovedMail(User pUser, String pIlvName) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradeplusbremen@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Lehrveranstaltung " + pIlvName + " entfernt" );
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass die Lehrveranstaltung " + pIlvName
                    + "\n\n vom Prüfer gelöscht wurde und sie damit entfernt wurden.");

            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }



    public static void userJoinIlvRegisterMail(User pUser, String pIlvName) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradeplusbremen@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Eintragung in die Lehrveranstaltung " + pIlvName  );
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass Sie für die Lehrveranstaltung " + pIlvName
                    + "\n\n vom Prüfer hinzugefügt wurden.");

            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }


    public static void joinExamMail(User pUser, String pIlvName) {

        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getInstance(props, null);
            session.setDebug(true);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradeplusbremen@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Anmeldung zur Prüfung für die Lehrveranstaltung " + pIlvName  );
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass Sie zur Prüfung für die  Lehrveranstaltung " + pIlvName
                    + "\n\n am 01.01.1995 hinzugefügt wurden.");

            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }


}
