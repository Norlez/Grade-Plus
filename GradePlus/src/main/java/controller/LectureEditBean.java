package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.Lecture;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.LectureDAO;




import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;

import static common.util.Assertion.assertNotNull;

@Named
@SessionScoped
public class LectureEditBean extends AbstractBean implements Serializable {
    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für ILV-Objekte
     * übernimmt.
     */
    private final LectureDAO LectureDao;

    /**
     * Die aktuell zu bearbeitende ILV.
     */
    private Lecture lecture;

    /**
     * Der zu bearbeitende {@link User}.
     */
    private Lecture selectedLecture;

    /**
     * Prüft, ob die Attribute in dieser Klasse verändert werden dürfen.
     */
    private boolean editChecker = false;

    /**
     * Erzeugt eine neue InstanceLectureEditBean.
     *
     * @param pSession Die Session der zu erzeugenden LectureEditBean.
     * @param pLectureDao Die LectureDAO der zu erzeugenden LectureEditBean.
     * @throws IllegalArgumentException Falls {@code pSession} oder {@code pInstanceLectureDao} {@code null} sind.
     */
    @Inject
    public LectureEditBean(final Session pSession, final LectureDAO pLectureDao) {
        super(pSession);
        LectureDao = assertNotNull(pLectureDao);
    }

    /**
     * Gibt den Zustand des Editcheckers zurück.
     * @return der Zustand des Editcheckers.
     */
    public boolean getEditChecker() {
        return editChecker;
    }

    /**
     * Setzt den Editchecker auf das Gegenteil und setzt den übergebenen User.
     * @param pUser der zu setztende User
     * @return die user.xhtml um auf das Facelet zu wechseln.
     */
    public String setEditChecker(final User pUser) {
        editChecker = !editChecker;
        setUser(pUser);
        return "user.xhtml";
    }


    /**
     * Gibt die aktuell zu bearbeitenden Lecture zurück.
     *
     * @return Den aktuell zu bearbeitenden Benutzer.
     */
    public Lecture getSelectedLecture() {
        return selectedLecture;
    }

    /**
     * Setzt die aktuell zu bearbeitenden Lecture auf den gegebenen Wert.
     *
     * @param pLecture die zu setzende Lecture
     *            Der neue aktuell zu bearbeitende Benutzer.
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String setSelectedLecture(final Lecture pLecture) {
        Lecture lecture = assertNotNull(pLecture);
        return "user.xhtml";
    }

    public void setEditCheckerWithoutLecture(){
        editChecker = !editChecker;
    }

    /**
     * Aktualisiert den aktuell ausgewählten Benutzer in der Liste aller bekannten
     * Benutzer unter Verwendung des entsprechenden Data-Access-Objekts.
     *
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String update() {
        try {
            final Lecture pLecture = selectedLecture;
            LectureDao.update(pLecture);
            setEditCheckerWithoutLecture();
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        }
        return "user.xhtml";
    }


}
