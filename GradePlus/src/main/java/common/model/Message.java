package common.model;

import javax.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;
import static common.util.Assertion.*;

/**
 * Repräsentiert eine Nachricht, die zwischen Benutzern versendet werden kann. Eine
 * Nachricht hat einen Betreff, einen Inhalt sowie einen Absender und Empfänger.
 *
 * @author Torben Groß, Marvin Kampen
 * @version 2018-01-09
 */
public class Message extends JPAEntity {

    /**
     * Der Betreff der Nachricht.
     */
    private String topic;

    /**
     * Der Inhalt der Nachricht.
     */
    private String content;

    /**
     * Der Absender der Nachricht.
     */
    private User sender;

    /**
     * Die Liste der Empfänger der Nachricht.
     */
    private List<User> receivers;

    /**
     * Die Sendezeit der Nachricht.
     */
    private LocalDateTime date;

    /**
     * Erzeugt eine neue Nachricht.
     * 
     * @param pTopic
     *            Der Betreff der Nachricht.
     * @param pContent
     *            Der Inhalt der Nachricht.
     * @param pSender
     *            Der Sender der Nachricht.
     * @param pReceivers
     *            Die Empfänger der Nachricht.
     * @param pDate
     *            Das Absendedatum der Nachricht.
     */
    public Message(String pTopic, String pContent, User pSender, List<User> pReceivers,
            LocalDateTime pDate) {
        if (pTopic == null || pContent == null) {
            throw new IllegalArgumentException("Diese Werte sind nicht gültig.");
        }
        topic = assertNotEmpty(pTopic);
        content = assertNotEmpty(pContent);
        sender = assertNotNull(pSender);
        receivers = assertNotNull(pReceivers);
        date = assertNotNull(pDate);
    }

    /**
     * Gibt den Betreff der Nachricht zurück.
     *
     * @return Den Betreff der Nachricht.
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setzt den Betreff der Nachricht auf den gegebenen Betreff.
     *
     * @param pTopic
     *            Der Betreff neue der Nachricht.
     */
    public void setTopic(String pTopic) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt den Inhalt der Nachricht zurück.
     *
     * @return Den Inhalt der Nachricht.
     */
    public String getContent() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Inhalt der Nachricht auf den gegebenen Inhalt.
     *
     * @param pContent
     *            Der neue Inhalt der Nachricht.
     */
    public void setContent(String pContent) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt den Absender der Nachricht zurück.
     *
     * @return Den Absender der Nachricht.
     */
    public User getSender() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Absender der Nachticht auf den gegebenen Absender.
     *
     * @param pSender
     *            Der neue Absender der Nachticht.
     */
    public void setSender(User pSender) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Empfänger der Nachricht als Liste zurück.
     *
     * @return Die Empfänger der Nachricht als Liste.
     */
    public List<User> getReceivers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer den Empfängern hinzu.
     *
     * @param pUser
     *            Der hinzuzufügende Empfänger.
     */
    public void addReceiver(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der gegebenen Liste vorhandenen Benutzer den Empfängern hinzu.
     *
     * @param pUserList
     *            Die Liste der hinzuzufügenen Empfänger.
     */
    public void addReceivers(List<User> pUserList) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Empfänger der Nachricht auf den gegebenen Benutzer.
     *
     * @param pUser
     *            Der neue Empfänger der Nachricht.
     */
    public void setReceiver(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Empfänger der Nachricht auf die in der gegebenen Liste vorhandenen
     * Benutzer.
     *
     * @param pUserList
     *            Die Liste der neuen Empfänger der Nachricht.
     */
    public void setReceivers(List<User> pUserList) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer von den Empfängern der Nachricht.
     *
     * @param pUser
     *            Der zu entfernende Benutzer.
     */
    public void removeReceiver(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Benutzer von den Empfängern der
     * Nachricht.
     *
     * @param pUserList
     *            Die Liste der zu entfernenden Empfänger der Nachricht.
     */
    public void removeReceivers(List<User> pUserList) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Sendezeit der Nachricht zurück.
     *
     * @return Die Sendezeit der Nachricht.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setzt die Sendezeit der Nachricht auf die gegebene Zeit.
     *
     * @param pDate
     *            Die neue Sendezeit der Nachricht.
     */
    public void setDate(LocalDateTime pDate) {
        date = assertNotNull(pDate);
    }

}
