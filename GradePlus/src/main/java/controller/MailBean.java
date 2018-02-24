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

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.model.Mail;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean ist der Controller für die Versendung von Emails..
 *
 * @author Arbnor Miftari, Torben Groß
 * @version 2018-02-24
 */
@Named
@RequestScoped
public class MailBean extends AbstractBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(MailBean.class);

    /**
     * Die Adresse des verwendeten Smtp-Servers.
     */
    private static final String smtp = "smtp.gmail.com";

    /**
     * Der Absender der Nachricht.
     */
    private User sender;

    /**
     * Das {@link Mail}-Objekt der Nachricht, das entsprechende Informationen
     * enthält.
     */
    private Mail mail;

    /**
     * Die Nachricht, die versendet wird.
     */
    private Message message;

    /**
     * Die Properties für die Mailversendung.
     */
    private Properties props;

    /**
     * Die Session der Mailversendung.
     */
    private javax.mail.Session session;

    /**
     * Erzeugt eine neue MailBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden MailBean.
     */
    @Inject
    public MailBean(final common.model.Session pSession) {
        super(assertNotNull(pSession));
    }

    /**
     * Setzt die {@link Message} auf eine neue Nachricht und die Daten der Nachricht auf
     * {@code null}.
     */
    @PostConstruct
    public void init() {
        sender = getSession().getUser();
        mail = new Mail();

        props = new Properties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        session = javax.mail.Session.getInstance(props, null);
        session.setDebug(true);

        message = new MimeMessage(session);
    }

    /**
     * Gibt das {@link Mail}-Objekt zurück.
     *
     * @return Das {@link Mail}-Objekt.
     */
    public Mail getMail() {
        return mail;
    }

    /**
     * Sendet eine Nachricht eines Benutzer an einen anderen Benutzer.
     *
     * @return
     */
    public String sendMail() {
        try {
            sender.setEmail("gradeplusbremen@gmail.com");
            sender.setTmpPassword("Koschke123");
            message.setFrom(new InternetAddress(sender.getEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail.getRecipient()));
            message.setSentDate(new Date());
            message.setSubject(mail.getTopic());
            message.setText(mail.getContent());

            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, sender.getEmail(), sender.getTmpPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorTransmitOfMessage", sender.getEmail());
        }
        init();
        return "dashboard.xhtml";
    }

    /**
     * Versendet eine Systemnachricht an einen gewählten Benutzer.
     */
    public void sendSystemMail() {
        sender = new User();
        sender.setEmail("gradeplusbremen@gmail.com");
        sender.setTmpPassword("Koschke123");
        sendMail();
        sender = getSession().getUser();
    }

}