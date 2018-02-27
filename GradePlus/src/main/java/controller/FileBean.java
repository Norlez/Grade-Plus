package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Role;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import java.io.*;
import java.util.ArrayList;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist für das Importieren und Exportieren verschiedener Dateien wie
 * Pabo-Listen zuständig.
 *
 * @author Torben Groß
 * @version 2018-02-27
 */
@Named
@RequestScoped
public class FileBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Benutzer-Objekte
     * übernimmt.
     */
    private UserDAO userDao;

    /**
     * Die zu verarbeitende Datei.
     */
    private Part file;

    /**
     * Erzeugt eine neue FileBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden FileBean.
     * @param pUserDao
     *            Die UserDAO der zu erzeugenden FileBean.
     */
    @Inject
    public FileBean(final Session pSession, final UserDAO pUserDao) {
        super(pSession);
        userDao = assertNotNull(pUserDao);
    }

    /**
     * Gibt die aktuell ausgewählte Datei zurück.
     *
     * @return Die aktuell ausgewählte Datei.
     */
    public Part getFile() {
        return file;
    }

    /**
     * Setzt die aktuell ausgewählte Datei auf den gegebenen Wert.
     *
     * @param pFile
     *            Die neue aktuell ausgewählte Datei.
     */
    public void setFile(final Part pFile) {
        file = assertNotNull(pFile);
    }

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
            theUser.setEmail(data[0].trim());
            theUser.setUsernameForUserMail();
            theUser.setSurname(data[1].trim());
            theUser.setGivenName(data[2].trim());
            theUser.setMatrNr(data[3].trim());
            theUser.setPassword(data[4].trim());
            theUser.setRole(Role.fromString(data[5]));
            try {
                userDao.save(theUser);
            } catch (final IllegalArgumentException e) {
                addErrorMessageWithLogging(e, logger, Level.DEBUG,
                        getTranslation("errorUserdataIncomplete"));
            } catch (final DuplicateUsernameException e) {
                addErrorMessageWithLogging("registerUserForm:username", e, logger,
                        Level.DEBUG, "errorUsernameAlreadyInUse", theUser.getUsername());
            } catch (final DuplicateEmailException e) {
                addErrorMessageWithLogging("registerUserForm:email", e, logger,
                        Level.DEBUG, "errorEmailAlreadyInUse", theUser.getEmail());
            }
        }
        return "users.xhtml";
    }

}
