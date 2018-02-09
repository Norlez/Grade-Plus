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
     * Registrierung eine Bestätigungsmail zusendet, in der alle relevanten Daten vermerkt
     * sind.
     * 
     * @param pUser
     *            Der Empfänger der Registrierungsmail.
     * 
     * 
     *            public void registerMail(User pUser) {
     * 
     *            Properties props = new Properties(); props.put("mail.smtp.host",
     *            "smtp.gmail.com"); props.put("mail.smtp.socketFactory.port", "465");
     *            props.put("mail.smtp.socketFactory.class",
     *            "javax.net.ssl.SSLSocketFactory"); props.put("mail.smtp.auth", "true");
     *            props.put("mail.smtp.port", "465");
     * 
     *            //Authentifizierung für die Verbindung zum SMTP-Server. Session session
     *            = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
     *            protected PasswordAuthentication getPasswordAuthentication() { return
     *            new PasswordAuthentication("gradeplusbremen@gmail.com","Koschke123"); }
     *            });
     * 
     *            try { Message message = new MimeMessage(session); message.setFrom(new
     *            InternetAddress("gradeplusbremen@gmail.com"));
     *            message.setRecipients(Message.RecipientType.TO,
     *            InternetAddress.parse(pUser.getEmail()));
     *            message.setSubject("Ihre Registrierung bei GradePlus");
     *            message.setText("Hallo " + pUser.getGivenName() + "," +
     *            "\n\n Hiermit bestätigen wir Ihre Registrierung bei Gradeplus mit folgenden Daten"
     *            + "\n\n Nutzername: " + pUser.getUsername() + "\n\n Nachname: " +
     *            pUser.getSurname() + "\n\n Vorname: " + pUser.getGivenName() +
     *            "\n\n Matrikelnummer: " + pUser.getMatrNr() + "\n\n E-mail Adresse: " +
     *            pUser.getEmail() +
     *            "\n\n\n Bei weiteren Fragen stehen wir Ihnen unter dieser Email zur Verfügung"
     *            );
     * 
     *            Transport.send(message); } catch (MessagingException e) { throw new
     *            RuntimeException(e); } }
     */

    public void registerMail(User pUser) {

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
            message.setText("was geht");

            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");

            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * Prüft, ob die Kombination der eingegebene Email mit dem dazugehörigem Passwort eine
     * Verbindung zum smpt Server aufbauen und damit gültig sind.
     *
     * @boolean Gibt zurück, ob die Verbindung erfolgreich war oder nicht.
     */
    public boolean isEmailPassCombiValid(String pEmail, String pPassword) {
        // Properties für den SMTP Uni Bremen Server.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.uni-bremen.de");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        try {
            javax.mail.Session session = javax.mail.Session.getInstance(props);
            Transport transport = session.getTransport("smtp");
            transport.connect(pEmail, pPassword);

            if (transport.isConnected()) {
                transport.close();
                return true;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return false;
    }
}