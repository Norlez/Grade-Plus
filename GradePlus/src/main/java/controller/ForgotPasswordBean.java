package controller;

import common.model.Session;

/**
 * Dieses Bean ist der Controller für den Forgot-Password-Dialog.
 * 
 * @author Torben Groß
 * @version 2017-12-21
 */
public class ForgotPasswordBean extends AbstractBean {

    /**
     * Erzeugt eine neue ForgotPasswordBean.
     * 
     * @param pSession
     *            Die Session der zu erzeugenden ForgotPasswordBean.
     * @param pMessageDAO
     *            Die DAO der zu erzeugenden MessageBean.
     *
     */
    public ForgotPasswordBean(Session pSession) {
        super(pSession);
    }

    /**
     * Gibt die anzuzeigende Email zurück.
     *
     * @return Die anzuzeigende Email.
     */
    public String getEmail() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die anzuzeigende Email auf den gegebenen Wert.
     * 
     * @param pEmail
     *            Die anzuzeigende Email.
     */
    public void setEmail(String pEmail) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die anzuzeigende Matrikelnummer zurück.
     *
     * @return Die anzuzeigende Matrikelnummer.
     */
    public int getMatrNr() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die anzuzeigende Matrikelnummer auf den gegebenen Wert.
     * 
     * @param pMatrNr
     *            Die anzuzeigende Matrikelnummer.
     */
    public void setMatrNr(int pMatrNr) {
        throw new UnsupportedOperationException();
    }

    /**
     * Sendet dem aktuell angezeigten Benutzer sein Passwort per eMail.
     * 
     * @return Den Namen des Facelets, zu dem im Erfolgsfall navigiert werden soll oder
     *         {@code null} falls das Passwort nicht versendet werden konnte.
     */
    public String sendPassword() {
        throw new UnsupportedOperationException();
    }

}
