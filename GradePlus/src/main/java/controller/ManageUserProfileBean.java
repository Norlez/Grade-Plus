package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Session;
import common.model.User;
import common.util.Assertion;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import static common.util.Assertion.assertNotNull;

@Named
@RequestScoped
public class ManageUserProfileBean extends LoginIndependentBean {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(ManageUserProfileBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Der Benutzer.
     */
    private User user;

    /**
     * Passwort für die Verifizierung
     *
     */
    private String password;

    /**
     * Wiederholung des Passwortes
     */

    private String passwordWDH;

    /**
     * Der Username des Benutzers, der seine Email ändern möchte.
     */
    private String username;

    /**
     * Erzeugt eine neue RegistrationBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden RegistrationBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden RegistrationBean.
     */
    @Inject
    public ManageUserProfileBean(Session pSession, UserDAO pUserDAO) {
        super(assertNotNull(pSession));
        userDao = assertNotNull(pUserDAO);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Assertion.assertNotNull(password);
    }

    public String getPasswordWDH() {
        return passwordWDH;
    }

    public void setPasswordWDH(String passwordWDH) {
        this.passwordWDH = Assertion.assertNotNull(passwordWDH);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Assertion.assertNotNull(username);
    }

    public Boolean checkPassword(final String pass1, final String pass2) {
        if (pass1.equals(pass2)) {
            return true;
        }
        return false;
    }

    /**
     * Initialisiert das Attribut {@link #user}, sodass {@link #user} einen neu
     * anzulegenden {@link User} repräsentiert.
     */
    public void init() {
        user = new User();
    }

    /**
     * Aktualisiert den aktuell angezeigten Benutzer in der Liste aller bekannten Benutzer
     * unter Verwendung des entsprechenden Data-Access-Objekts.
     *
     * && sender.isEmailPassCombiValid(user.getEmail(), password)
     */
    public void restorePassword() {
        user = userDao.getUserForUsername(username);
        if (checkPassword(password, passwordWDH)) {
            assertNotNull(user);
            user.setPassword(password);
            try {
                userDao.update(user);
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
            init();
        } else {
            System.out.println("jojo");
        }
    }
}
