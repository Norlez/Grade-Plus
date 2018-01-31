/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 AG Softwaretechnik, University of Bremen:
 * Karsten Hölscher, Sebastian Offermann, Dennis Schürholz, Marcel Steinbeck
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

import static common.util.Assertion.assertNotNull;

import java.time.LocalDateTime;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import common.model.Session;
import persistence.UserDAO;
import common.util.Assertion;
import org.apache.log4j.Logger;

import businesslogic.Crypt;
import common.model.User;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Dieses Bean ist der Controller für den Login-Dialog.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Sebastian Offermann, Marcel Steinbeck
 * @version 2017-06-28
 */
@Named
@RequestScoped
public class LoginBean extends AbstractBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(LoginBean.class);

    /**
     * Der Benutzername, der im Facelet angezeigt wird und durch Interaktion mit dem
     * Facelet geändert wird.
     */
    private String username;

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Der Name, der im Facelet angezeigt wird und durch Interaktion mit dem Facelet
     * geändert wird.
     */
    private String name;

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Der Vorname, der im Facelet angezeigt wird und durch Interaktion mit dem Facelet
     * geändert wird.
     */
    private String surname;

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Der Matrikelnummer, der im Facelet angezeigt wird und durch Interaktion mit dem
     * Facelet geändert wird.
     */
    private String matriculationNumber;

    /**
     * Das Passwort, das im Facelet angezeigt wird und durch Interaktion mit dem Facelet
     * geändert wird.
     */
    private String password;

    /**
     * LoginButton, an den Fehlermeldungen gebunden werden, welche beim Login auftreten
     * können.
     */
    private UIComponent loginButton;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDAO;

    /**
     * Erzeugt eine neue LoginBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LoginBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden LoginBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pUserDAO} {@code null} ist.
     */
    @Inject
    public LoginBean(final Session pSession, final UserDAO pUserDAO) {
        super(pSession);
        userDAO = Assertion.assertNotNull(pUserDAO);
    }

    /**
     * Gibt den anzuzeigenden Benutzernamen zurück.
     *
     * @return Der anzuzeigende Benutzername.
     */
    public String getUsername() {
        return username;
    }

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Gibt den anzuzeigenden Nachnamen zurück.
     *
     * @return Der anzuzeigende Nachname.
     */
    public String getName() {
        return name;
    }

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Gibt den anzuzeigenden Vorname zurück.
     *
     * @return Der anzuzeigende Vorname.
     */
    public String getSurname() {
        return surname;
    }

    // TODO: in Aenderungsdoku einfuegen
    /**
     * Gibt den anzuzeigenden Matrikelnummer zurück.
     *
     * @return Der anzuzeigende Matrikelnummer.
     */
    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    /**
     * Setzt den anzuzeigenden Benutzernamen auf den gegebenen Wert.
     * 
     * @param pUsername
     *            Der anzuzeigende Benutzername.
     * @throws IllegalArgumentException
     *             Falls der Parameter den Wert {@code null} hat.
     */
    public void setUsername(final String pUsername) {
        username = Assertion.assertNotNull(pUsername);
    }

    /**
     * Gibt das anzuzeigende Passwort zurück.
     * 
     * @return Das anzuzeigende Passwort.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Verschlüsselt das gegebene Passwort (unter Verwendung von
     * {@link Crypt#hash(java.lang.String)}) und setzt das anzuzeigende Passwort auf das
     * verschlüsselte Passwort.
     * 
     * @param pPassword
     *            Das anzuzeigende Passwort.
     * @throws IllegalArgumentException
     *             Falls der Parameter den Wert {@code null} hat.
     * @throws UnsupportedOperationException
     *             Falls das System nicht über die Voraussetzungen verfügt, das Hashen
     *             durchzuführen.
     */
    public void setPassword(final String pPassword) {
        password = Assertion.assertNotNull(pPassword);
    }

    /**
     * Liefert den Login-Button zurück.
     * 
     * @return Der Login-Button.
     */
    public UIComponent getLoginButton() {
        return loginButton;
    }

    /**
     * Setzt den Login-Button auf den gegebenen Wert.
     * 
     * @param pLoginButton
     *            Der Login-Button.
     * @throws IllegalArgumentException
     *             Falls {@code theLoginButton == null}.
     */
    public void setLoginButton(final UIComponent pLoginButton) {
        loginButton = Assertion.assertNotNull(pLoginButton);
    }

    /**
     * Loggt den aktuell angezeigten Benutzer in die zugehörige Session ein, falls noch
     * niemand in der zugehörigen Session eingeloggt ist und der temporäre Benutzer
     * authentifiziert werden kann (korrektes Passwort zum Benutzernamen). Die
     * Attributwerte des angezeigten Benutzers werden zurückgesetzt. Wenn der angezeigte
     * Benutzer nicht eingeloggt werden kann, wird eine entsprechende Fehlermeldung in der
     * UI angezeigt.
     *
     * @return Den Namen des Facelets, zu dem im Erfolgsfall navigiert werden soll oder
     *         {@code null} falls der temporäre Benutzer nicht eingeloggt werden konnte.
     */
    public String login() {
        final Session session = getSession();
        if (session.isLoggedIn()) {
            if (logger.isDebugEnabled()) {
                logger.debug(String.format("User %s tried to relogin via login().",
                        username));
            }
            return null;
        }
        final User registeredUser = userDAO.getUserForUsername(username);
        if (registeredUser == null) {
            addErrorMessage(loginButton, "errorUnknownUsername");
            password = "";
            return null;
        }
        if (BCrypt.checkpw(password, registeredUser.getPassword())) {
            setUser(registeredUser);
            session.setLoginTime(LocalDateTime.now());
            if (logger.isInfoEnabled()) {
                logger.info(String.format("Successful login for user %s.", username));
            }
            username = "";
            password = "";
            FacesContext.getCurrentInstance().getViewRoot().setLocale(getLanguage());
            return "dashboard.xhtml";
        } else {
            addErrorMessage(loginButton, "errorUnknownPassword");
            password = "";
            return null;
        }
    }

}
