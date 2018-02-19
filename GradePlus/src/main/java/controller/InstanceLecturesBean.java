package controller;

import common.exception.DuplicateInstanceLectureException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.Session;
import common.model.User;
import persistence.InstanceLectureDAO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean verwaltet Instanzen von Lehrveranstaltungen.
 *
 * @author Torben Groß
 * @version 2018-02-10
 */
@Named
@RequestScoped
public class InstanceLecturesBean extends AbstractBean implements Serializable {

    /**
     * Der Logger für diese Klasse.
     */
    private static final Logger logger = Logger.getLogger(UsersBean.class);

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für ILV-Objekte
     * übernimmt.
     */
    private final InstanceLectureDAO instanceLectureDao;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für {@link User}
     * -Objekte übernimmt.
     */
    private final UserDAO userDao;

    /**
     * Der eingeloggte Benutzer, der Methoden dieser Klasse aufruft. Wird benötigt, um
     * ILVs des aufrufenden Benutzers korrekt anzeigen zu können.
     */
    private final User user;

    /**
     * Die aktuell angezeigte ILV, dessen Attribute durch die UIKomponenten des Facelets
     * geschrieben und gelesen werden.
     */
    private InstanceLecture instanceLecture;

    /**
     * Die Liste aller innerhalb der Applikation bekannten ILVs.
     */
    private List<InstanceLecture> allInstanceLectures;

    /**
     * Die Liste aller innerhalb der Applikation bekannten ILVs eines Prüflings.
     */
    private List<InstanceLecture> instanceLecturesOfExaminee;

    /**
     * Die Liste aller innerhalb der Applikation bekannten ILVs eines Prüfers.
     */
    private List<InstanceLecture> instanceLecturesOfExaminer;

    /**
     * Erzeugt eine neue InstanceLecturesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden InstanceLecturesBean.
     * @param pInstanceLectureDao
     *            Die InstanceLectureDAO der zu erzeugenden InstanceLecturesBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pInstanceLectureDao} {@code null}
     *             sind.
     */
    @Inject
    public InstanceLecturesBean(final Session pSession,
            final InstanceLectureDAO pInstanceLectureDao, final UserDAO pUserDao) {
        super(pSession);
        instanceLectureDao = assertNotNull(pInstanceLectureDao);
        userDao = assertNotNull(pUserDao);
        user = assertNotNull(getSession().getUser());
    }

    /**
     * Initialisiert die Attribute {@link #instanceLecture}, {@link #allInstanceLectures},
     * {@link #instanceLecturesOfExaminee} und {@link #instanceLecturesOfExaminer}, sodass
     * {@link #instanceLecture} eine neu anzulegende {@link InstanceLecture}
     * repräsentiert, {@link #allInstanceLectures} alle bekannten ILVs der Applikation und
     * {@link #instanceLecturesOfExaminee} sowie {@link #instanceLecturesOfExaminer} alle
     * ILVs des entsprechenden Benutzers enthält.
     */
    @PostConstruct
    public void init() {
        instanceLecture = new InstanceLecture();
        instanceLecture.addExaminer(user);
        allInstanceLectures = instanceLectureDao.getAllInstanceLectures();
        instanceLecturesOfExaminee = instanceLectureDao
                .getInstanceLecturesForExaminee(user);
        instanceLecturesOfExaminer = instanceLectureDao
                .getInstanceLecturesForExaminer(user);
    }

    /**
     * Setzt die {@link Lecture} der {@link InstanceLecture} auf den gegebenen Wert. Wird
     * aufgerufen, falls eine Instanz einer bestimmten Lehrveranstaltung erstellt werden
     * soll.
     *
     * @param pLecture
     *            Die Lehrveranstaltung der ILV.
     * @return "semestercreate.xhtml", um auf entsprechendes Facelet umzuleiten.
     */
    public String setLecture(final Lecture pLecture) {
        instanceLecture.setLecture(assertNotNull(pLecture));
        return "semester.xhtml";
    }

    /**
     * Gibt den Benutzer zurück.
     *
     * @return Den Benutzer.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt die anzuzeigende ILV zurück.
     *
     * @return Die anzuzeigende ILV.
     */
    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    /**
     * Gibt alle innerhalb der Applikation bekannten ILVs zurück.
     *
     * @return Die anzuzeigende Liste aller innerhalb der Applikation bekannten ILVs.
     */
    public List<InstanceLecture> getAllInstanceLectures() {
        return allInstanceLectures;
    }

    /**
     * Gibt alle ILVs zurück, in denen der gegebene {@link User} als Prüfling angemeldet
     * ist.
     *
     * @return Alle ILVs zurück, in denen der gegebene {@link User} als Prüfling
     *         angemeldet ist.
     */
    public List<InstanceLecture> getInstanceLecturesOfExaminee() {
        return instanceLecturesOfExaminee;
    }

    /**
     * Gibt alle ILVs zurück, in denen der gegebene {@link User} als Prüfer angemeldet
     * ist.
     *
     * @return Alle ILVs zurück, in denen der gegebene {@link User} als Prüfer angemeldet
     *         ist.
     */
    public List<InstanceLecture> getInstanceLecturesOfExaminer() {
        return instanceLecturesOfExaminer;
    }

    /**
     * Fügt die aktuell angezeigte ILV der Liste aller innerhalb der Applikation bekannten
     * ILVs hinzu.
     *
     * @return "semester.xhtml", um auf das Facelet der Übersicht der ILVs zu leiten.
     */
    public String save() {
        try {
            user.setAsProf(instanceLecture);
            userDao.update(user);
            instanceLectureDao.save(instanceLecture);
            // Der Fehler liegt in jedem Fall beim save
            // nachdem gesaved wurde, wird vor dem init die anzahl der ilv ermittelt und gibt 2 wieder
            // obwohl es nur einer sein soll-> auf jeden fall save methode überprüfen.

        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInstanceLectureDataIncomplete"));
        } catch (final DuplicateInstanceLectureException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorInstanceLectureAlreadyExists");
        } catch (final Exception e) {
        }
        //dient hier zum testen wo der fehler ist.
        allInstanceLectures = instanceLectureDao.getAllInstanceLectures();
        init();
        return "semester.xhtml";
    }

    /**
     * Entfernt die übergebene ILV aus der Liste aller bekannten ILVs unter Verwendung des
     * entsprechenden Data-Access-Objekts. Sollte die zu entfernende ILV nicht in der
     * Liste der ILVs vorhanden sein, passiert nichts.
     *
     * @param pInstanceLecture
     *            Die zu entfernende ILV.
     * @return {@code null}, famit nicht zu einem anderen Facelet navigiert wird.
     * @throws IllegalArgumentException
     *             Falls die übergebene Lehrveranstaltung den Wert {@code null} hat.
     */
    public String remove(final InstanceLecture pInstanceLecture) {
        assertNotNull(pInstanceLecture);
        instanceLectureDao.remove(pInstanceLecture);
        init();
        return null;
    }
}
