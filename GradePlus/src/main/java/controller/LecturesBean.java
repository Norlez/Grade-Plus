package controller;

import common.exception.DuplicateVakException;
import common.model.Lecture;
import common.model.Session;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.LectureDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean verwaltet Lehrveranstaltungen.
 * 
 * @author Torben Groß
 * @version 2017-12-21
 */
@Named
@RequestScoped
public class LecturesBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * Lehrveranstaltungs-Objekte übernimmt.
     */
    private final LectureDAO lectureDao;

    /**
     * Die aktuell angezeigte Lehrveranstaltung, dessen Attribute durch die UIKomponenten
     * des Facelets geschrieben und gelesen werden.
     */
    private Lecture lecture;

    /**
     * Die Liste aller innerhalb der Applikation bekannten Lehrveranstaltungen.
     */
    private List<Lecture> allLectures;

    /**
     * Erzeugt eine neue LecturesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LecturesBean.
     * @param pLectureDao
     *            Die Session der zu erzeugenden LecturesBean.
     *
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public LecturesBean(Session pSession, LectureDAO pLectureDao) {
        super(pSession);
        lectureDao = assertNotNull(pLectureDao);
    }

    /**
     * Initialisiert die Attribute {@link #lecture} und {@link #allLectures}, sodass
     * {@link #lecture} eine neu anzulegende {@link Lecture} repräsentiert und
     * {@link #allLectures} alle bekannten Lehrveranstaltungen der Applikation enthält.
     */
    @PostConstruct
    public void init() {
        lecture = new Lecture();
        allLectures = lectureDao.getAllLectures();
    }

    /**
     * Gibt die anzuzeigende Lehrveranstaltung zurück.
     *
     * @return Die anzuzeigende Lehrveranstaltung.
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Gibt alle innerhalb der Applikation bekannten Lehrveranstaltung zurück.
     * 
     * @return Die anzuzeigende Liste aller innerhalb der Applikation bekannten
     *         Lehrveranstaltung.
     */
    public List<Lecture> getAllLectures() {
        return allLectures;
    }

    /**
     * Fügt die aktuell angezeigte Lehrveranstaltung der Liste aller innerhalb der
     * Applikation bekannten Lehrveranstaltungen hinzu.
     *
     * @return "lectures.xhtml", um auf das Facelet der Übersicht der Lehrveranstaltung zu
     *         leiten.
     */
    public String save() {
        try {
            lectureDao.save(lecture);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorLecturedataIncomplete"));
        } catch (final DuplicateVakException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorVakAlreadyInUse", lecture.getVak());
        }
        init();
        return "lectures.xhtml";
    }

    /**
     * Aktualisiert die aktuell angezeigte Lehrveranstaltung in der Liste aller innerhalb
     * der Applikation bekannten Lehrveranstaltungen.
     *
     * @return "lectures.xhtml", um auf das Facelet der Übersicht der Lehrveranstaltung zu
     *         leiten.
     */
    public String update() {
        try {
            lectureDao.update(lecture);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorLecturedataIncomplete"));
        }
        init();
        return "lectures.xhtml";
    }

    /**
     * Entfernt die übergebene Lehrveranstaltung aus der Liste aller bekannten
     * Lehrveranstaltung unter Verwendung des entsprechenden Data-Access-Objekts. Sollte
     * die zu entfernende Lehrveranstaltung nicht in der Liste der Lehrveranstaltung
     * vorhanden sein, passiert nichts.
     *
     * @param pLecture
     *            Die zu entfernende Lehrveranstaltung.
     * @return {code null} damit nicht zu einem anderen Facelet navigiert wird.
     * @throws IllegalArgumentException
     *             Falls die übergebene Lehrveranstaltung den Wert {@code null} hat.
     */
    public String remove(final Lecture pLecture) {
        lectureDao.remove(pLecture);
        init();
        return null;
    }

}
