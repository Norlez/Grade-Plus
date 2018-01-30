package controller;

//import com.sun.xml.internal.bind.v2.TODO;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean ist der Controller für den Registrierungs-Dialog.
 *
 * @author Torben Groß
 * @version 2018-01-2018
 */
@Named
@RequestScoped
public class RegistrationBean extends LoginIndependentBean {

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
     * Der zu registrierende Benutzer.
     */
    private User user;

    /**
     * Erzeugt eine neue RegistrationBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden RegistrationBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden RegistrationBean.
     */
    @Inject
    public RegistrationBean(Session pSession, UserDAO pUserDAO) {
        super(assertNotNull(pSession));
        userDao = assertNotNull(pUserDAO);
        user = new User();
    }

    /**
     * Initialisiert das Attribut {@link #user}, sodass {@link #user} einen neu
     * anzulegenden {@link User} repräsentiert.
     */
    public void init() {
        user = new User();
    }

    /**
     * Gibt den Benutzer zurück.
     *
     * @return Den Benutzer.
     */
    public User getUser() {
        return user;
    }

    MailBean sender = new MailBean();

    /**
     * Registriert einen neuen Benutzer mit allen aktuell angezeigen Stammdatem im System.
     */
    public String register() {
        try {
            sender.main(user);
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
        return "index.xhtml";
    }

}
