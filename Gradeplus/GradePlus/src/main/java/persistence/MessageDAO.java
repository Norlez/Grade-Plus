package persistence;

import java.util.List;

import common.model.Message;
import common.model.User;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Message}.
 * 
 * @author Torben Groß
 */
public class MessageDAO extends JPADAO<Message> {

    @Override
    Class<Message> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theMessage} der Datenbank hinzu.
     * 
     * @param pMessage
     *            Das zu speichernde {@code Message}-Objekt.
     */
    @Override
    public synchronized void save(Message pMessage) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den Eintrag von {@code theMessage} im Datenbestand.
     * 
     * @param pMessage
     *            Das zu aktualisierende {@link Message}-Objekt.
     */
    @Override
    public synchronized void update(Message pMessage) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Nachrichten zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Nachrichten.
     */
    public List<Message> getAllMessages() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Nachrichten des gegebenen Benutzers zurück.
     * 
     * @param pUser
     *            Der Benutzer der gesuchten Nachrichten.
     * @return Die Nachrichten des gegebenen Benutzers.
     */
    public List<Message> getMessagesForUser(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Nachrichten vom gegebenen Absender zum gegebenen Empfänger zurück.
     * 
     * @param pSender
     *            Der Absender der gesuchten Nachrichten.
     * @param pReceiver
     *            Der Empfänger der gesuchten Nachrichten.
     * @return Die Nachrichten vom gegebenen Absender zum gegebenen Empfänger.
     */
    public List<Message> getMessagesSenderReceiver(User pSender, User pReceiver) {
        throw new UnsupportedOperationException();
    }

}
