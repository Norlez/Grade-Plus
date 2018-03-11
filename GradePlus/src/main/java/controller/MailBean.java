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

import java.io.*;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.Part;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Exam;
import common.model.InstanceLecture;
import common.model.User;
import common.util.Assertion;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean ist der Controller für die Versendung von Emails..
 *
 * @author Arbnor Miftari, Torben Groß, Andreas Estenfelder
 * @version 2018-02-24
 */
@Named
@RequestScoped
public class MailBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(MailBean.class);

    /**
     * Die Adresse des verwendeten Smtp-Servers.
     */
    private String smtp;

    /**
     * Der Absender der Nachricht.
     */
    private User sender;

    /**
     * Die Nachricht, die versendet wird.
     */
    private Message message;

    /**
     * Die Session der Mailversendung.
     */
    private javax.mail.Session session;

    /**
     * Die Email-Adresse des Empfängers/ der Empfänger der Nachricht.
     */
    private String recipient;

    /**
     * Der Titel der Nachricht.
     */
    private String topic;

    /**
     * Der Inhalt der Nachricht.
     */
    private String content;

    /**
     * Die zu verarbeitende Datei.
     */
    private javax.servlet.http.Part file;

    /**
     * Gibt die aktuell ausgewählte Datei zurück.
     *
     * @return Die aktuell ausgewählte Datei.
     */
    public javax.servlet.http.Part getFile() {
        return file;
    }

    /**
     * Setzt die aktuell ausgewählte Datei auf den gegebenen Wert.
     *
     * @param pFile
     *            Die neue aktuell ausgewählte Datei.
     */
    public void setFile(final Part pFile) {
        file = pFile;
    }

    /**
     * Gibt die Email-Adresse des Empfängers zurück.
     *
     * @return Die Email-Adresse des Empfängers.
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * Setzt die Email-Adresse des Empfängers auf den gegebenen Wert.
     *
     * @param pRecipient
     *            Die neue Email-Adresse des Empfängers.
     */
    public void setRecipient(final String pRecipient) {
        recipient = assertNotNull(pRecipient);
    }

    /**
     * Gibt den Titel der Nachricht zurück.
     *
     * @return Den Titel der Nachricht.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setzt den Titel der Nachricht auf den gegebenen Wert.
     *
     * @param pTopic
     *            Der neue Titel der Nachricht.
     */
    public void setTopic(final String pTopic) {
        topic = assertNotNull(pTopic);
    }

    /**
     * Gibt den Inhalt der Nachricht zurück.
     *
     * @return Den Inhalt der Nachricht.
     */
    public String getContent() {
        return content;
    }

    /**
     * Setzt den Inhalt der Nachricht auf den gegebenen Wert.
     *
     * @param pContent
     *            Der Inhalt der Nachricht.
     */
    public void setContent(final String pContent) {
        content = assertNotNull(pContent);
    }

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Erzeugt eine neue MailBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden MailBean.
     *    @param pUserDAO
     *              Die UserDao der zu erzeugenden MailBean
     */
    @Inject
    public MailBean(final common.model.Session pSession, final UserDAO pUserDAO) {
        super(assertNotNull(pSession));
        userDao = Assertion.assertNotNull(pUserDAO);
    }

    /**
     * Setzt die {@link Message} auf eine neue Nachricht und die Daten der Nachricht auf
     * {@code null}.
     */
    @PostConstruct
    public void init() {
        sender = getSession().getUser();
        // sender.setEmail("gradeplusbremen@gmail.com"); //TODO: ENTFERNEN
        // sender.setTmpPassword("Koschke123"); //TODO: ENTFERNEN
        smtp = "smtp."
                + sender.getEmail().substring(sender.getEmail().lastIndexOf('@') + 1);
        Properties props;

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
     * Sendet eine Nachricht eines Benutzer an einen anderen Benutzer.
     *
     * @throws MessagingException, wenn ein Fehler beim Messaging auftritt.
     * @return dashboard.xhtml
     */
    public String sendMail() {
        String tmp = sender.getEmail().substring(sender.getEmail().lastIndexOf('@') + 1).trim();
        if(!(tmp.equals("uni-bremen.de")
                || tmp.equals("gmail.com")
                || tmp.equals("gmail.de")
                || tmp.equals("googlemail.com")
                || tmp.equals("googlemail.de"))){
            addErrorMessage("selectValidEmail");
            return "dashboard.xhtml";
        }
        try {
            message.setFrom(new InternetAddress(sender.getEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    getRecipientsAddresses(recipient));
            message.setSentDate(new Date());
            message.setSubject(topic);
            message.setText(content);

            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, sender.getEmail(), getSession().getTmpPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            addErrorMessage("outputMailSent");
        } catch (MessagingException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorTransmitOfMessage", sender.getEmail());
        }
        recipient = "";
        topic = "";
        content = "";
        init();
        return "dashboard.xhtml";
    }

    /**
     * Speichert die Datei.
     */
    File filetosend;

    /**
     * Wandelt Part file in io.File um.
     *
     * @throws IOException
     */
    public File copyPartToFile() throws IOException {
        InputStream inputStream = file.getInputStream();
        String filename = getFileName(file);
        String prefix = filename.substring(0, filename.lastIndexOf('.'));
        String suffix = "." + filename.substring(filename.lastIndexOf('.') + 1);
        File tempFile = File.createTempFile(prefix, suffix);
        OutputStream outputStream = new FileOutputStream(tempFile);
        try {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            filetosend = tempFile;
        } finally {
            inputStream.close();
            outputStream.close();
            return tempFile;
        }
    }

    /**
     * Holt sich den originalen Namen des hochgeladenen Part-Objektes
     * Quelle : https://gist.github.com/cengizIO/2067999
     * @param part Das Part-Objekt, dessen Name ermittelt werden soll.
     * @return den Namen des Part-Objektes
     */
    private String getFileName(Part part) {
        for (String cd : part.getHeader("content-disposition").split(";")) {
            if (cd.trim().startsWith("filename")) {
                return cd.substring(cd.indexOf('=') + 1).trim()
                        .replace("\"", "");
            }
        }
        return null;
    }

    /**
     * Email mit Anhang.
     *
     * @throws IOException, falls bei der Eingabe der File etwas falsch läuft.
     */
    public String sendMailWithAttachment() throws IOException {
        String tmp = sender.getEmail().substring(sender.getEmail().lastIndexOf('@') + 1).trim();
        if(!(tmp.equals("uni-bremen.de")
                || tmp.equals("gmail.com")
                || tmp.equals("gmail.de")
                || tmp.equals("googlemail.com")
                || tmp.equals("googlemail.de"))){
            addErrorMessage("selectValidEmail");
            return "dashboard.xhtml";
        }
        try {
            filetosend = copyPartToFile();
            message.setFrom(new InternetAddress(sender.getEmail()));
            message.setRecipients(Message.RecipientType.TO,
                    getRecipientsAddresses(recipient));
            message.setSentDate(new Date());
            message.setSubject(topic);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(content);

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            attachmentBodyPart.attachFile(filetosend);

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect(smtp, sender.getEmail(), getSession().getTmpPassword());
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            addErrorMessage("outputMailSent");
        } catch (MessagingException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorTransmitOfMessage", sender.getEmail());
        }
        topic = "";
        content = "";
        recipient = "";
        init();
        return "dashboard.xhtml";
    }

    /**
     * Je nachdem, ob eine Datei ausgewählt wurde, wird eine Mail mit Anhang oder eine
     * ohne versendet.
     *
     * @throws IOException, falls beim Einlesen der Datei ein Fehler auftritt
     */
    public void decideWhichEmail() {
        try {
            if (file == null) {

                sendMail();
            } else {
                sendMailWithAttachment();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prüft, ob die Kombination der eingegebene Email mit dem dazugehörigem Passwort eine
     * Verbindung zum smpt Server aufbauen und damit gültig sind.
     *
     * @boolean Gibt zurück, ob die Verbindung erfolgreich war oder nicht.
     * @throws MessagingException, wenn bei der Benachrichtigung etwas fehlschlägt
     */
    public static boolean isEmailPassCombiValid(String pEmail, String pPassword) {
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

    /**
     * Liefert alle Empfänger-Mails als ein Array von InternetAdress zurück.
     *
     * @param pRecipients
     *            Die Mails der Empfänger getrennt durch ein Komma.
     * @return InternetAdress Liste aller Empfänger.
     * @throws MessagingException, wenn bei der Benachrichtigung etwas fehlschlägt
     */
    public InternetAddress[] getRecipientsAddresses(String pRecipients) {
        String[] recipientList = pRecipients.split(",");
        InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
        try {
            int counter = 0;
            for (String recipient : recipientList) {
                recipientAddress[counter] = new InternetAddress(recipient.trim());
                counter++;
            }
        } catch (MessagingException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorTransmitOfMessage", sender.getEmail());
        }
        return recipientAddress;
        // message.setRecipients(Message.RecipientType.TO, recipientAddress);
    }

    /**
     * Sendet eine Rundmail an alle User der InstanceLecture.
     * @param pInstanceLecture, die gewählte InstanceLecture
     * @return message.xhtml als Weiterleitung
     */
    public String sendBroadcastMail(final InstanceLecture pInstanceLecture) {
        assertNotNull(pInstanceLecture);
        String seperator = "";
        StringBuilder sb = new StringBuilder();
        for (User r : pInstanceLecture.getExaminees()) {
            sb.append(seperator);
            seperator = ", ";
            sb.append(r.getEmail());
        }
        recipient = sb.toString();
        return "message.xhtml";
    }

    /**
     * Erstellt die nötigen Daten für den Betreff und den Empängern für die Sendung einer
     * Krankmelde-Mail.
     *
     * @param pExam
     *            Die Prüfung, zu dem der zugehörige User gehört.
     * @Param pUser der User, der die Mail versendet.
     * @return das Facelet message.xhtml
     */
    public String prepareIllnessMail(User pUser, final Exam pExam) {
        assertNotNull(pExam);
        String seperator = "";
        topic = "Krankmeldung für " + pExam.getInstanceLecture().getLecture().getName()
                + " Prüfung am " + pExam.dateToString() + " um " + pExam.timeToString();
        StringBuilder sb = new StringBuilder();
        List<User> pruefer = pExam.getExaminers();
        for (User r : pruefer) {
            sb.append(seperator);
            seperator = ", ";
            sb.append(r.getEmail());
        }
        recipient = sb.toString();
        content = "Martikelnummer: " + pUser.getMatrNr() + "\n Vorname: "
                + pUser.getGivenName() + "\n Nachname: " + pUser.getSurname()
                + "\n Username: " + pUser.getUsername() + "\n Email: " + pUser.getEmail()
                + "\n Anmerkung: ";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        User registeredUser = getSession().getUser();
        registeredUser.setLoggingString(date.toString() + ": Krankmeldung für "
                + pExam.getInstanceLecture().getLecture().getName() + " Prüfung am "
                + pExam.dateToString() + " um " + pExam.timeToString() + "\n");
        try {
            userDao.update(registeredUser);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse",
                    registeredUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", registeredUser.getEmail());
        }
        return "message.xhtml";
    }

}


