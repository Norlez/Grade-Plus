package controller;

import common.exception.DuplicateInstanceLectureException;
import common.model.*;
import persistence.InstanceLectureDAO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import persistence.UserDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses Bean verwaltet Instanzen von Lehrveranstaltungen.
 *
 * @author Torben Groß
 * @version 2018-02-19
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


    private Map<String, SemesterTime> times;

    private SemesterTime selectedTimes;

    private Map<String, Years> years;

    private Years selectedYear;

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

        times = calculateSemesterMap();
        years = calculateYearMap();
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
        getSession().setSelectedLecture(pLecture);
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

    public String setInstanceLecture(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture);
        getSession().setSelectedILV(pInstanceLecture);
        return "exams.xhtml";
    }

    //TODO
    public List<InstanceLecture> getInstanceLecturesForLecture() {
        return instanceLectureDao.getInstanceLecturesForLecture(getSession().getSelectedLecture());
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
     * Setzt den Student in die InstanceLecture.
     * 
     * @param pUser
     *            Student
     * @return Dashboard.xhtml
     */
    public String setInstanceLecturesOfExaminee(User pUser) {
        instanceLecture.addExaminee(pUser);
        return "dashboard.xhtml";
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
            // instanceLecture.setSemester(selectedTimes.toString());
            // instanceLecture.setYear(selectedYear.toString());
            instanceLecture.setLecture(getSession().getSelectedLecture());
            instanceLectureDao.save(instanceLecture);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInstanceLectureDataIncomplete"));
        } catch (final DuplicateInstanceLectureException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorInstanceLectureAlreadyExists");
        } catch (final Exception e) {
        }
        // dient hier zum testen wo der fehler ist.
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


    public Map<String, SemesterTime> getTimes() {
        return times;
    }

    public void setTimes(Map<String, SemesterTime> times) {
        this.times = times;
    }

    public SemesterTime getSelectedTimes() {
        return selectedTimes;
    }

    public void setSelectedTimes(SemesterTime selectedTimes) {
        this.selectedTimes = selectedTimes;
    }


    public Map<String, Years> getYears() {
        return years;
    }

    public void setYears(Map<String, Years> years) {
        this.years = years;
    }

    public Years getSelectedYear() {
        return selectedYear;
    }

    public void setSelectedYear(Years selectedYear) {
        this.selectedYear = selectedYear;
    }


    /**
     * Liefert eine einfache Map mit den verfügbaren Jahren im System zurück.
     * Diese werden als Auswahl im Drop-Down Menu bei der erstellung einer ILV verfügbar sein.
     *
     * @return Eine einfache Map mit verfügbaren Rollen.
     */
    private  Map<String, Years> calculateYearMap(){

        final Map<String, Years> tmp = new LinkedHashMap<>();
        Years[] years = Years.values();

        for(int i = 0; i<years.length; i++){
            tmp.put(years[i].toString(), years[i]);
        }
        return Collections.unmodifiableMap(tmp);
    }

    /**
     * Liefert eine einfache Map mit den verfügbaren Semestern im System zurück.
     *
     * @return Eine einfache Map mit verfügbaren Semestern.
     */
    private  Map<String, SemesterTime> calculateSemesterMap(){
        final Map<String, SemesterTime> tmp = new LinkedHashMap<>();
        tmp.put("WiSe", SemesterTime.WINTER);
        tmp.put("SoSe", SemesterTime.SOMMER);
        return Collections.unmodifiableMap(tmp);
    }

}
