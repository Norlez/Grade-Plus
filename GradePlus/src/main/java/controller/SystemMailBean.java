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
import javax.inject.Named;
import javax.mail.*;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import common.model.*;
import org.apache.log4j.Logger;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean ist der Controller für die System-Mail.
 *
 * @author Arbnor Miftari
 * @version 2018-01-2018
 */
@Named
@RequestScoped
public class SystemMailBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(SystemMailBean.class);

    /**
     * Die Properties für die Mailversendung.
     */
    private static Properties properties;

    /**
     * Die Session der Mailversendung.
     */
    private static javax.mail.Session session;

    /**
     * Die Nachricht, die versendet wird.
     */
    private static Message message;

    /**
     * Legt Properties für die Versendung einer Mail ab und erstellt ein neues Message
     * Objekt für die Versendung der Nachricht.
     */
    public static void init() {

        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");

        session = javax.mail.Session.getInstance(properties, null);
        session.setDebug(true);
        try {
            message = new MimeMessage(session);
            message.setFrom(new InternetAddress("gradeplusbremen@gmail.com"));
        } catch (MessagingException e) {
            throw new RuntimeException();
        }

    }

    /**
     * Erstellt das Transporterobjekt, um die erstellte Mail zu versenden.
     */
    public static void postInit() {

        try {
            Transport transport = session.getTransport("smtp");
            transport
                    .connect("smtp.gmail.com", "gradeplusbremen@gmail.com", "Koschke123");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Eine Loginunabhängige Funktion, die dem sich registrierenden Benutzer, nach der
     * Registrierung eine Bestätigungsmail zusendet und in der alle relevanten Daten
     * vermerkt sind.
     *
     * @param pUser
     *            Der Empfänger der Registrierungsmail.
     */

    public static void registerMail(User pUser) {
        try {
            init();
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
            postInit();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sendet eine Mail von der Systemmail, an den User, der in einer IlV angemeldet war
     * und informiert ihn darüber, dass die Veranstaltung von Ersteller aus dem System
     * entfernt wurde.
     * 
     * @param pUser
     *            Der User, der in Kenntniss gesetzt wird.
     * @param pIlvName
     *            Die ILV die aus dem System entfernt wurde.
     */

    public static void ilvRemovedMail(User pUser, String pIlvName) {

        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Lehrveranstaltung " + pIlvName
                    + " entfernt");
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass die Lehrveranstaltung "
                    + pIlvName
                    + "\n\n vom Prüfer gelöscht wurde und sie damit entfernt wurden.");

            postInit();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * Sendet eine Mail von der Systemmail an den User und informiert ihn darüber, dass er
     * einer Lehrveranstaltung hinzugefügt wurde.
     * 
     * @param pUser
     *            Der User, der in Kenntniss gesetzt wird.
     * @param pIlvName
     *            Die Lehrveranstaltung, in der der User eingetragen wurde.
     */

    public static void userJoinIlvRegisterMail(User pUser, String pIlvName) {

        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Eintragung in die Lehrveranstaltung "
                    + pIlvName);
            message.setText("Hallo"
                    + pUser.getGivenName()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass Sie für die Lehrveranstaltung "
                    + pIlvName + "\n\n vom Prüfer hinzugefügt wurden.");
            postInit();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }

    }

    /**
     * Sendet eine Mail von der Systemmail an den User und informiert ihn darüber, dass
     * dieser sich erfolgreich für eine Prüfung in der Lehrveranstaltung angemeldet hat.
     * 
     * @param pUser
     *            Der User, der informiert wird.
     * @param pExam
     *            Die Prüfung
     */
    public static void joinExamMail(User pUser, Exam pExam) {

        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pUser.getEmail()));
            message.setSubject("Systemnachricht: Anmeldung zur Prüfung für die Lehrveranstaltung "
                    + pExam.getInstanceLecture().getLecture().getName());
            message.setText(String
                    .format("Hallo %s %s,\n\nHiermit informieren wir Sie, dass Sie sich erfolgreich für eine Prüfung der Lehrveranstaltung %s angemeldet haben.\n\n\nDaten der Prüfung:\n\nDatum: %s\nUhrzeit: %s\nDauer: %d Min.\nOrt: %s",
                            pUser.getGivenName(),
                            pUser.getSurname(),
                            pExam.getInstanceLecture().getLecture().getName(),
                            pExam.dateToString(),
                            pExam.timeToString(),
                            pExam.getExamLength(),
                            pExam.getLocation().isEmpty() ? "Keine Angabe" : pExam
                                    .getLocation()));
            postInit();

        } catch (MessagingException e) {
            throw new RuntimeException(e);

        }
    }

    /**
     * Sendet eine Mail von der Systemmail, an den übergebenen Empfänger und informiert
     * darüber, dass der User sich für eine Prüfung Krankgemeldet hat.
     * 
     * @param pUser
     *            Der User, der sich krank meldet.
     * @param pRecipient
     *            Der Empfänger, der über die Krankmeldung in Kentniss gesetzt werden soll
     */

    public static void reportIllness(User pUser, User pRecipient) {

        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(pRecipient.getEmail()));
            message.setSubject("Systemnachricht: Krankmeldung vom Nutzer"
                    + pUser.getGivenName() + " " + pUser.getSurname());
            message.setText("Hallo"
                    + pRecipient.getSurname()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass Sich der Prüfungling für seine Prüfung in "
                    + "\n\n der  Lehrveranstaltung krank gemeldet hat.");
            postInit();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sendet eine Mail von der Systemmail, an den übergebenen Empfänger und informiert
     * darüber, dass der Veranstalter der Lecture einen erstellten Termin, in dem ein User
     * angemeldet war, geschlossen hat.
     * 
     * @param pUser
     *            Der User, der sich krank meldet.
     * @param pExam
     *            Der Empfänger, der über die Krankmeldung in Kentniss gesetzt werden soll
     */

    public static void reportExamCancel(User pUser, Exam pExam) {

        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("gradeplusbremen@gmail.com"));
            message.setSubject("Systemnachricht: Prüfung gelöscht");
            message.setText("Hallo"
                    + pUser.getSurname()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass die Prüfung für "
                    + pExam.getInstanceLecture().getLecture().getName()
                    + "\n\n vom Veranstelter gelöscht wurde. Für weitere Informationen kontaktieren Sie bitten den"
                    + "\n\n Veranstalter von "
                    + pExam.getInstanceLecture().getLecture().getName());
            postInit();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sendet eine Mail von der Systemmail, an den übergebenen Empfänger und informiert
     * darüber, dass der Veranstalter der Lecture einen Prüfungstermin geändert hat, in
     * dem ein User angemeldet war.
     * 
     * @param pUser
     *            Der User, der über die Änderungen informiert wird..
     * @param pOldExam
     *            Die alte Prüfung.
     * @param pNewExam
     *            Die neue Prüfung.
     */

    public static void examInfoChangedMail(User pUser, Exam pOldExam, Exam pNewExam) {
        try {
            init();
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse("gradeplusbremen@gmail.com"));
            message.setSubject("Systemnachricht: Änderung der Prüfungstermindaten");
            message.setText("Hallo"
                    + pUser.getSurname()
                    + ","
                    + "\n\n Mit dieser Mail, benachrichtigen wir Sie, dass die Prüfung für "
                    + pOldExam.getInstanceLecture().getLecture().getName()
                    + "\n\n vom Veranstalter geändert wurde. Die aktuellen Daten für ihre Prüfung sind folgende."
                    + "\n\n Altes Datum" + pOldExam.dateToString() + "\n\n Neues Datum"
                    + pNewExam.dateToString() + "\n\n Alte Uhrzeit"
                    + pOldExam.getLocalDateTime().getHour() + ":"
                    + pOldExam.getLocalDateTime().getMinute() + "\n\n Neue Uhrzeit"
                    + pNewExam.getLocalDateTime().getHour() + ":"
                    + pNewExam.getLocalDateTime().getMinute() + "\n\n Alter Prüfungsort"
                    + pOldExam.getLocation() + "\n\n Neuer Prüfungsort"
                    + pOldExam.getLocation());
            postInit();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
