package controller;

//import com.sun.xml.internal.bind.v2.TODO;
import common.model.Session;
import persistence.UserDAO;

/**
 * Dieses Bean ist der Controller für den Registrierungs-Dialog.
 * 
 * @author Torben Groß
 * @version 2017-12-21
 */
public class RegistrationBean extends LoginIndependentBean {

    /**
     * Erzeugt eine neue RegistrationBean.
     * 
     * @param pSession
     *            Die Session der zu erzeugenden RegistrationBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden RegistrationBean.
     */
    public RegistrationBean(Session pSession, UserDAO pUserDAO) {
        super(pSession);
    }

    /**
     * Gibt den anzuzeigenden Nachnamen zurück.
     * 
     * @return Den anzuzeigenden Nachnamen.
     */
    public String getSurname() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den anzuzeigenden Nachnamen auf den gegebenen Wert.
     * 
     * @param pSurname
     *            Der anzuzeigende Nachname.
     */
    public void setSurname(String pSurname) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt den anzuzeigenden Vornamen zurück.
     * 
     * @return Den anzuzeigenden Vornamen.
     */
    public String getGivenName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den anzuzeigenden Vornamen auf den gegebenen Wert.
     * 
     * @param pGivenName
     *            Der anzuzeigende Vornamen.
     */
    public void setGivenName(String pGivenName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das anzuzeigende Passwort zurück.
     * 
     * @return Das anzuzeigende Passwort.
     */
    public String getPassword() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt das anzuzeigende Passwort auf den gegebenen Wert.
     * 
     * @param pPassword
     *            Das anzuzeigende Passwort.
     */
    public void setPassword(String pPassword) {
        throw new UnsupportedOperationException();
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
     * 
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
     * Registriert einen neuen Benutzer mit allen aktuell angezeigen Stammdatem im System.
     */
    public void register() {
        throw new UnsupportedOperationException();
    }

}
