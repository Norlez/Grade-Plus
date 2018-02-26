package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean verwaltet das Bearbeiten eines Benutzers.
 *
 * @author Torben Groß
 * @version 2018-02-2018
 */
@Named
@SessionScoped
public class UserEditBean extends AbstractBean implements Serializable {

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
     * Der zu bearbeitende {@link User}.
     */
    private User selectedUser;

    /**
     * Erzeugt eine neue {@link UserEditBean}.
     *
     * @param pSession
     *            Die Session der zu erzeugenden UserEditBean.
     * @param pUserDao
     *            Die UserDAO der zu erzeugenden UserEditBean.
     */
    @Inject
    public UserEditBean(final Session pSession, final UserDAO pUserDao) {
        super(pSession);
        userDao = assertNotNull(pUserDao);
        selectedUser = new User();
    }

    /**
     * Gibt den aktuell zu bearbeitenden Benutzer zurück.
     *
     * @return Den aktuell zu bearbeitenden Benutzer.
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * Setzt den aktuell zu bearbeitenden Benutzer auf den gegebenen Wert.
     *
     * @param pUser
     *            Der neue aktuell zu bearbeitende Benutzer.
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String setSelectedUser(final User pUser) {
        selectedUser = assertNotNull(pUser);
        return "user.xhtml";
    }

    /**
     * Aktualisiert den aktuell ausgewählten Benutzer in der Liste aller bekannten
     * Benutzer unter Verwendung des entsprechenden Data-Access-Objekts.
     *
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String update() {
        try {
            userDao.update(selectedUser);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        } catch (final DuplicateUsernameException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorUsernameAlreadyInUse", selectedUser.getUsername());
        } catch (final DuplicateEmailException e) {
            addErrorMessageWithLogging("registerUserForm:email", e, logger, Level.DEBUG,
                    "errorEmailAlreadyInUse", selectedUser.getEmail());
        }
        return "user.xhtml";
    }

}
