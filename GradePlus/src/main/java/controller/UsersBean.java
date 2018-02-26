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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALI4NGS IN THE
 * SOFTWARE.
 */
package controller;

import static common.util.Assertion.assertNotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Role;
import common.util.Assertion;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import common.model.User;
import persistence.SessionDAO;
import persistence.UserDAO;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import javax.servlet.http.Part;

/**
 * Dieses Bean ist für die Benutzerverwaltung zuständig. Backing Bean (und damit
 * Controller im MVC-Muster) für das Facelet {@code admin/users.xhtml}.
 *
 * Da die Webseite dynamisch ist in dem Sinne, dass während ihrer Anzeige mehrere Requests
 * möglich sind (Löschen eines Benutzers aus der angezeigten Tabelle) bekommt sie
 * {@code ViewScoped}.
 *
 * @author Dennis Schürholz, Karsten Hölscher, Marcel Steinbeck, Sebastian Offermann
 * @version 2017-06-28
 */
@Named
@RequestScoped
public class UsersBean extends AbstractBean implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = 4920108168379649504L;

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der aktiven Sessions übernimmt. Wird
     * benötigt, um im Falle des Löschens eines Benutzers abzufragen, ob dieser aktuell
     * eingeloggt ist.
     */
    private final SessionDAO sessionDAO;

    /**
     * Der aktuell angezeigte Benutzer, dessen Attribute durch die UIKomponenten des
     * Facelets geschrieben und gelesen werden.
     */
    private User user;

    /**
     * Die Liste aller innerhalb der Applikation bekannten Benutzer.
     */
    private List<User> allUsers;

    /**
     * //TODO
     */
    private static Map<String, Role> roles;

    /**
     * TODO
     */
    private boolean editChecker;

    /**
     * Die zu erstellende Mail.
     */
    private RegisterMailBean sender;

    /**
     * Erzeugt eine neue UsersBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden UsersBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden UsersBean.
     * @param pSessionDAO
     *            Die SessionDAO der zu erzeugenden UsersBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession}, {@code pUserDAO} oder {@code pSessionDAO}
     *             {@code null} ist.
     */
    @Inject
    public UsersBean(final common.model.Session pSession, final UserDAO pUserDAO,
            final SessionDAO pSessionDAO) {
        super(pSession);
        userDao = assertNotNull(pUserDAO);
        sessionDAO = assertNotNull(pSessionDAO);
        sender = new RegisterMailBean();
        roles = ProfileBean.calculateRoleMap();

    }

    /**
     * Initialisiert die Attribute {@link #user} und {@link #allUsers}, sodass
     * {@link #user} einen neu anzulegenden {@link User} repräsentiert und
     * {@link #allUsers} alle bekannten Benutzer der Applikation enthält.
     */
    @PostConstruct
    public void init() {
        user = new User();
        allUsers = userDao.getAllUsers();
    }

    /**
     * Gibt den anzuzeigenden Benutzer zurück.
     *
     * @return Den anzuzeigenden Benutzer.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setzt den ausgewählten User, durch den erlangten Usernamen.
     * @param pUser der User, dessen Profil betrachtet werden soll
     * @return die user.xhtml.
     */
    public String getUserDetails(User pUser ){
        user =  pUser;

        return "user.xhtml";
    }

    public boolean getEditChecker() {
        return editChecker;
    }

    public String setEditChecker(final User pUser) {
        editChecker = !editChecker;
        setUser(pUser);
        return "user.xhtml";
    }

    @Override
    public void setUser(final User pUser) {
        user = assertNotNull(pUser);
    }

    /**
     * Gibt eine anzuzeigende Liste mit allen innerhalb der Applikation bekannten
     * Benutzern zurück. Diese Liste ist keine Kopie, sondern Teil des internen Zustands
     * der Bean (siehe README.md, Abschnitt 'Beans und Ressourcenallokation').
     *
     * @return Die anzuzeigende Liste aller innerhalb der Applikation bekannten Benutzern.
     */
    public List<User> getAllUsers() {
        return allUsers;
    }


    // /////////////////////////////////////////////////////////////////////////////
    // ///////////////////////////TEST/////////////////////////////////////////////
    /**
     * Gibt das Part-Objekt zurück.
     * 
     * @return Das Part-Objekt.
     */
    public Part getFile() {
        return file;
    }

    /**
     * Setzt das übergebene Part-Objekt
     * 
     * @param file
     *            Das Part-Object, dass gesetzt werden soll.
     */

    public void setFile(Part file) {
        this.file = file;
    }

    /**
     * Das Part-Objekt, dass die hochgeladene Datei vom User repräsentiert.
     */
    private Part file;

    // /////////////////////////////////////////////////////////////////////////////
    // /////////////////////////////////////////////////////////////////////////////

    /**
     * Läuft genau wie UsersBean.save(), allerdings wird eine Menge von Benutzern
     * hinzugefügt. Die Menge wird erlangt, über den Inputstreams einer CSV Datei.
     *
     */
    public String saveFromCSV() throws IOException {
        InputStream is = file.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = br.readLine()) != null) {
            lines.add(line);
        }
        for (String theLine : lines) {
            String[] data = theLine.split(",");
            User theUser = new User();
            theUser.setEmail(data[0]);
            theUser.setUsernameForUserMail();
            theUser.setSurname(data[1]);
            theUser.setGivenName(data[2]);
            theUser.setMatrNr(data[3]);
            theUser.setPassword(data[4]);
            try {
                userDao.save(theUser);
            } catch (final IllegalArgumentException e) {
                addErrorMessageWithLogging(e, logger, Level.DEBUG,
                        getTranslation("errorUserdataIncomplete"));
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger,
                        Level.DEBUG, "errorEmailAlreadyInUse", user.getEmail());
            }
        }
        return "users.xhtml";
    }

    /**
     * Der User den der Admin durch Auswahl auswählen und betrachtet und wenn verlangt
     * auch ändern kann.
     */
    private User profileUser;

    /**
     * Fügt den aktuell angezeigten Benutzer der Liste aller innerhalb der Applikation
     * bekannten Benutzer hinzu (unter Verwendung des Data-Access-Objektes) und setzt den
     * angezeigten Benutzer auf einen neuen Benutzer mit leeren Attributwerten. Die Liste
     * der angezeigten Benutzer wird aktualisiert. Sollte beim Hinzufügen des Benutzers
     * über das Data-Access-Objekt ein Fehler auftreten, wird eine entsprechende
     * Fehlermeldung in der UI angezeigt.
     *
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     */
    public String save() {
        try {
            sender.registerMail(user);
            userDao.save(user);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        init();
        return "users.xhtml";
    }

    /**
     * Entfernt den übergebenen Benutzer aus der Liste aller bekannten Benutzer unter
     * Verwendung des entsprechenden Data-Access-Objekts. Sollte der zu entfernende
     * Benutzer nicht in der Liste der Benutzer vorhanden sein, passiert nichts.
     *
     * @param pUser
     *            Der zu entfernende Benutzer.
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     * @throws IllegalArgumentException
     *             Falls der übergebene Benutzer den Wert {@code null} hat.
     */
    public String remove(final User pUser) {
        if (sessionDAO.isUserLoggedIn(assertNotNull(pUser))) {
            addErrorMessage("errorLoggedinDeletion");
            if (logger.isInfoEnabled()) {
                final User sessionUser = getSession().getUser();
                final String sessionUserIdentifier = sessionUser == null ? "Session without user"
                        : sessionUser.getUsername();
                logger.info(String.format("%s tried to delete currently active user %s.",
                        sessionUserIdentifier, pUser.getUsername()));
            }
            return null;
        }
        userDao.remove(pUser);
        init();
        return null;
    }

    /**
     * Aktualisiert den aktuell angezeigten Benutzer in der Liste aller bekannten Benutzer
     * unter Verwendung des entsprechenden Data-Access-Objekts.
     *
     *
     */
    public void update(final User pUser) { // TODO: Nicht Getestet
        assertNotNull(pUser);
        try {
            userDao.update(pUser);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", user.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", user.getEmail());
        }
        init();
    }

    /**
     * Liefert die unveränderbare Map mit den unterstützten Rollen zurück.
     * Änderungsversuche auf der Map führen zu einer {@code UnsupportedOperationException}
     * .
     *
     * @return Map der unterstützen Rollen.
     */
    public Map<String, Role> getRoles() {
        return roles;
    }

    /**
     * Setzt den Namen der anzuzeigenden Rolle.
     *
     * @param pRole
     *            Der Name der anzuzeigenden Rolle.
     * @throws IllegalArgumentException
     *             Falls der Name der anzuzeigenden Rolle den Wert {@code null} hat oder
     *             leer ist.
     */
    public void setRoleName(final String pRole) {
        // TODO: Rolle setzen
        // user.setRole(Assertion.assertNotEmpty(pRole));
    }




    // TODO: Rollenänderung ist nicht getestet

    /**
     * Setzt die User-Rolle auf Prüfer.
     * 
     * @param pUser
     *            , der zu verändernde User
     */
    public void setRolePruefer(User pUser) {
        assertNotNull(pUser);
        pUser.setRole(Role.EXAMINER);
        update(pUser);
        init();
    }

    /**
     * Setzt die User-Rolle auf Student.
     * 
     * @param pUser
     *            , der zu verändernde User
     */
    public void setRoleStudent(User pUser) {
        assertNotNull(pUser);
        pUser.setRole(Role.STUDENT);
        update(pUser);
        init();
    }

    /**
     * Setzt die User-Rolle auf Admin.
     * 
     * @param pUser
     *            , der zu verändernde User
     */
    public void setRoleAdmin(User pUser) {
        assertNotNull(pUser);
        pUser.setRole(Role.ADMIN);
        update(pUser);
        init();
    }

    /**
     * Prüft, ob der User ein Admin ist.
     *
     * @param user
     *            , der geprüft wird
     * @return true, wenn der User ein Admin ist
     */
    public boolean isAdmin(final User user) {
        assertNotNull(user);
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        return false;
    }

    /**
     * Prüft, ob der User ein Examiner ist.
     *
     * @param user
     *            , der geprüft wird
     * @return true, wenn der User ein Examiner ist
     */
    public boolean isExaminer(final User user) {
        assertNotNull(user);
        if (user.getRole() == Role.EXAMINER) {
            return true;
        }
        return false;
    }

    /**
     * Prüft, ob der User ein Student ist.
     *
     * @param user
     *            , der geprüft wird
     * @return true, wenn der User ein Student ist
     */
    public boolean isStudent(final User user) {
        assertNotNull(user);
        if (user.getRole() == Role.STUDENT) {
            return true;
        }
        return false;
    }

}
