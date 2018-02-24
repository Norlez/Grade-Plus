package common.model;

import static common.util.Assertion.assertNotNull;

/**
 * Repräsentiert eine Nachricht und enthält entsprechende Informationen.
 *
 * @author Torben Groß
 * @version 2018-02-24
 */
public class Mail {

    /**
     * Die Email-Adresse des Empfängers der Nachricht.
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

}
