package controller;

import java.io.Serializable;
import java.util.List;

import common.model.Message;
import common.model.Session;
import common.model.User;
import persistence.MessageDAO;

/**
 * Dieses Bean verwaltet Nachrichten.
 * 
 * @author Torben Groß
 * @version 2017-12-22
 */
public class MessagesBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue MessagesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden MessagesBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public MessagesBean(Session pSession, MessageDAO pMessageDAO) {
        super(pSession);
    }

    /**
     * Gibt die anzuzeigende Nachricht zurück.
     * 
     * @return Die anzuzeigende Nachricht.
     */
    public Message getMessage() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die anzuzeigende Nachricht auf den gegebenen Wert.
     * 
     * @param pMessage
     *            Die anzuzeigende Nachricht.
     */
    public void setMessage(Message pMessage) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Liste aller innerhalb der Applikation bekannten Nachrichten zurück.
     * 
     * @return Die Liste aller innerhalb der Applikation bekannten Nachrichten0.
     */
    public List<Message> getAllMessages() {
        throw new UnsupportedOperationException();
    }

    /**
     * Versendet eine Nachricht vom Benutzer der aktuellen {@link Session} an den
     * pReceiver.
     * 
     * @param pReceiver
     *            Der Empfänger der Nachricht.
     * @param pTopic
     *            Der Titel der Nachricht.
     * @param pContent
     *            Der Inhalt der Nachricht.
     */
    public void sendMessage(User pReceiver, String pTopic, String pContent) {
        throw new UnsupportedOperationException();
    }

}
