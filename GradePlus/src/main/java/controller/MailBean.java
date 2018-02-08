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

import java.util.Date;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;
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
 * Dieses Bean ist der Controller für die Versendung von Emails..
 *
 * @author Arbnor Miftari
 * @version 2018-01-2018
 */
@Named
@RequestScoped
public class MailBean extends AbstractBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(MailBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Der User, der eine Mail versendet.
     */
    private User user;

    /**
     * Der Empfänger der Email.
     */
    private String recipient;
    /**
     * Das Password des Senders zu seiner zugehörigen Email.
     */
    // private String password;

    /**
     * Der Betreff der Email.
     */
    private String topic;

    /**
     * Die Nachricht der Email.
     */
    private String content;

    /**
     * Die Email des Senders.
     */
    private String senderEmail;

    /**
     * Erzeugt eine neue RegistrationBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden RegistrationBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden RegistrationBean.
     */
    @Inject
    public MailBean(common.model.Session pSession, UserDAO pUserDAO) {
        super(assertNotNull(pSession));
        userDao = assertNotNull(pUserDAO);
        user = assertNotNull(pSession.getUser());
    }

    /**
     * Setzt das Passwort für die Emailversundung zum zugehörigen EmailAccount
     *
     * 
     * public void setPassword(final String pPassword){ password =
     * assertNotNull(pPassword); }
     */

    /**
     * gibt das Passwort zurück
     *
     * public String getPassword(){ return password; }
     */

    /**
     * Setzt das Passwort für die Emailversundung zum zugehörigen EmailAccount
     *
     */
    public void setTopic(final String pTopic) {
        topic = assertNotNull(pTopic);
    }

    /**
     * gibt den Betreff zurück.
     *
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setzt das Passwort für die Emailversundung zum zugehörigen EmailAccount
     *
     */
    public void setContent(final String pContent) {
        content = assertNotNull(pContent);
    }

    /**
     * Gibt das Textfeld zurück.
     *
     */
    public String getContent() {
        return content;
    }

    /**
     * Setzt das Passwort für die Emailversundung zum zugehörigen EmailAccount
     *
     */
    public void setRecipient(final String pRecipient) {
        recipient = assertNotNull(pRecipient);
    }

    /**
     * gibt das Passwort zurück
     *
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * setzt den user, der eine Email sendet.
     */
    public void setSenderEmail(final String pUser) {
        senderEmail = assertNotNull(pUser);
    }

    /**
     * gibt den senderEmail zurück.
     *
     */
    public String getSenderEmail() {

        return senderEmail;
    }

    /**
     * Versendet eine Nachricht, an eine beliebige Email-Adresse.
     *
     * @return Die Xhtml auf die der User nach der Versendung der Mail weitergeleitet
     *         wird.
     * 
     * 
     *         public String sendMailTo(){ Properties props = new Properties();
     *         props.put("mail.smtp.host", "smtp.uni-bremen.de");
     *         props.put("mail.smtp.socketFactory.port", "465");
     *         props.put("mail.smtp.socketFactory.class",
     *         "javax.net.ssl.SSLSocketFactory"); props.put("mail.smtp.auth", "true");
     *         props.put("mail.smtp.port", "465");
     * 
     *         Session session = Session.getInstance(props, new javax.mail.Authenticator()
     *         { protected PasswordAuthentication getPasswordAuthentication() { return new
     *         PasswordAuthentication(user.getEmail(),user.getTmpPassword()); } }); try {
     *         Message message = new MimeMessage(session); message.setFrom(new
     *         InternetAddress(user.getEmail()));
     *         message.setRecipients(Message.RecipientType.TO,
     *         InternetAddress.parse(recipient)); message.setSubject(topic);
     *         message.setText(content); message.setSentDate(new Date());
     *         Transport.send(message);
     * 
     *         } catch (MessagingException e) { throw new RuntimeException(e); } return
     *         "dashboard.xhtml"; }
     */

    public String sendMailTo() {

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.uni-bremen.de");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getInstance(props, null);
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user.getEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(recipient));
            message.setSubject(topic);
            message.setText(user.getEmail() + user.getTmpPassword());
            message.setSentDate(new Date());

            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.uni-bremen.de", "arbnor@uni-bremen.de",
                    "Angelosaskia07");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return "dashboard.xhtml";

    }

}