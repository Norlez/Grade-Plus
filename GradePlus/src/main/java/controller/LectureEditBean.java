package controller;

import common.exception.DuplicateEmailException;
import common.exception.DuplicateUsernameException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.Session;
import common.model.User;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.InstanceLectureDAO;
import persistence.LectureDAO;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import static common.util.Assertion.assertNotNull;
/**
 * Dieses Bean verwaltet Lehrveranstaltungen, wenn sie bearbeitet werden.
 *
 * @author Torben Groß, Andreas Estenfelder
 * @version 2017-12-21
 */
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
     *  Das Data-Access-Objekt, das die Verwaltung der Persistierung für Lecture-Objekte
     * übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDao;

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
     * @param pSession
     *            Die Session der zu erzeugenden LectureEditBean.
     * @param pLectureDao
     *            Die LectureDAO der zu erzeugenden LectureEditBean.
     * @param pInstanceLectureDao
     *              Die InstanceLectureDAO der zu erzeugenden LectureEditBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pInstanceLectureDao} {@code null}
     *             sind.
     */
    @Inject
    public LectureEditBean(final Session pSession, final LectureDAO pLectureDao,
            final InstanceLectureDAO pInstanceLectureDao) {
        super(pSession);
        LectureDao = assertNotNull(pLectureDao);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);
        selectedLecture = new Lecture();
    }

    /**
     * Gibt den Zustand des Editcheckers zurück.
     * 
     * @return der Zustand des Editcheckers.
     */
    public boolean getEditChecker() {
        return editChecker;
    }

    /**
     * Setzt den Editchecker auf das Gegenteil und setzt den übergebenen User.
     * 
     * @param pLecture
     *            die zu setztende Lecture
     * @return die user.xhtml um auf das Facelet zu wechseln.
     */
    public String setEditChecker(final Lecture pLecture) {
        editChecker = !editChecker;
        setSelectedLectureWithoutReturn(pLecture);
        return "lectureedit.xhtml";
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
     * @param pLecture
     *            die zu setzende Lecture Der neue aktuell zu bearbeitende Benutzer.
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String setSelectedLecture(final Lecture pLecture) {
        selectedLecture = assertNotNull(pLecture);
        return "lectureedit.xhtml";
    }

    /**
     * Setzt die aktuell zu bearbeitenden Lecture auf den gegebenen Wert ohne Rückgabe.
     *
     * @param pLecture
     *            die zu setzende Lecture Der neue aktuell zu bearbeitende Benutzer.
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public void setSelectedLectureWithoutReturn(final Lecture pLecture) {
        selectedLecture = assertNotNull(pLecture);
    }

    /**
     * Setzt den EditChecker auf true, wenn er false war, ansonsten auf false, wenn er true war.
     */
    public void setEditCheckerWithoutLecture() {
        editChecker = !editChecker;
    }

    /**
     * Aktualisiert den aktuell ausgewählten Benutzer in der Liste aller bekannten
     * Benutzer unter Verwendung des entsprechenden Data-Access-Objekts.
     *
     * @return "user.xhtml", um auf das Facelet der Benutzerbearbeitung weiterzuleiten.
     */
    public String update() {
        List<String> ls = LectureDao.getAllLectures().stream()
                .filter(l -> !l.getId().equals(selectedLecture.getId()))
                .map(Lecture::getVak).collect(Collectors.toList());
        if (ls.contains(selectedLecture.getVak())) {
            addErrorMessage("errorVakAlreadyExists");
            return "lectureedit.xhtml";
        }
        try {
            final Lecture pLecture = selectedLecture;
            LectureDao.update(pLecture);
            setEditCheckerWithoutLecture();
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorUserdataIncomplete"));
        }
        return "lectures.xhtml";
    }

    /**
     * Setzt die gewählte Lehrveranstaltung auf den gegebenen Wert.
     *
     * @param pLecture
     *            Die gewählte Lehrveranstaltung.
     * @return "semesterdetails.xhtml", um auf das Facelet der Übersicht der ILV für
     *         Prüflinge weiterzuleiten.
     */
    public String setLectureForStudent(final Lecture pLecture) {
        lecture = assertNotNull(pLecture);
        return "semesterdetails.xhtml";
    }

    /**
     * Gibt die Lehrveranstaltung zurück.
     * @return lecture
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Setzt die Lehrveranstaltung mit dem gegebenen Parameter.
     * @param pLecture, darf nicht null sein
     */
    public void setLecture(final Lecture pLecture) {
        lecture = assertNotNull(pLecture);
    }

    /**
     * Gibt die ILVs für den Studenten zurück
     * @return Liste von ILVs
     */
    public List<InstanceLecture> getInstanceLecturesForLectureOfStudent() {
        return instanceLectureDao.getAllInstanceLectures().stream()
                .filter(i -> i.getLecture().getId().equals(lecture.getId()))
                .filter(i -> i.getExaminees().contains(getSession().getUser()))
                .collect(Collectors.toList());
    }

}
