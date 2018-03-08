package controller;

import businesslogic.Math;
import common.exception.DuplicateEmailException;
import common.exception.DuplicateInstanceLectureException;
import common.exception.DuplicateUsernameException;
import common.exception.UnexpectedUniqueViolationException;
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
import java.util.*;
import java.util.stream.Collectors;

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

    /**
     * Die Map aller innherhalb der Applikation bekannten Semesterzeiten.
     */
    private Map<String, SemesterTime> times;

    /**
     * Die ausgewählte Semesterzeit.
     */
    private SemesterTime selectedTimes;

    /**
     * Die Liste aller innherhalb der Applikation bekannten Jahre.
     */
    private List<String> years;

    /**
     * Das ausgewählte Jahr.
     */
    private String selectedYear;

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
        years = calculateYearList();
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

    /**
     * Setzt die Instanz einer Lehrveranstaltung
     * 
     * @param pInstanceLecture
     *            die ILV die gesetzt werden soll.
     * @return @return "semester.xhtml", um auf das Facelet der Übersicht der ILVs zu
     *         leiten.
     */
    public String setInstanceLecture(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture);
        getSession().setSelectedILV(instanceLecture);
        return "exams.xhtml";
    }

    /**
     * Gibt eine Liste von InstanceLectures zurück, die in einer Lecture innderhalb der
     * Applikation gespeichert sind.
     * 
     * @return Die Liste von InstanceLectures
     */
    // TODO
    public List<InstanceLecture> getInstanceLecturesForLecture() {
        return instanceLectureDao.getInstanceLecturesForLecture(getSession()
                .getSelectedLecture());
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
        for (InstanceLecture il : getAllInstanceLectures()
                .stream()
                .filter(x -> x.getLecture().getId()
                        .equals(getSession().getSelectedLecture().getId()))
                .collect(Collectors.toList())) {
            if (il.getYear().equals(selectedYear)
                    && il.getSemester().equals(SemesterTime.toString(selectedTimes))) {
                addErrorMessage("errorSemesterAlreadyExists");
                return "semestercreate.xhtml";
            }
        }
        try {
            user.addAsProfToIlv(instanceLecture);
            instanceLecture.setSemester(SemesterTime.toString(selectedTimes));
            instanceLecture.setYear(selectedYear);
            instanceLecture.setLecture(getSession().getSelectedLecture());
            instanceLectureDao.save(instanceLecture);
            userDao.update(user);
        } catch (final IllegalArgumentException e) {
            addErrorMessageWithLogging(e, logger, Level.DEBUG,
                    getTranslation("errorInstanceLectureDataIncomplete"));
        } catch (final DuplicateInstanceLectureException e) {
            addErrorMessageWithLogging("registerUserForm:username", e, logger,
                    Level.DEBUG, "errorInstanceLectureAlreadyExists");
        } catch (final Exception e) {
        }
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

    /**
     * Gibt die Map von Semesterzeiten zurück, die innerhalb der Applikation bekannt sind.
     * 
     * @return Die Map von Semesterzeiten.
     */
    public Map<String, SemesterTime> getTimes() {
        return times;
    }

    /**
     * Setzt die Map von Semesterzeiten anhand des übergebenen Parameters.
     * 
     * @param times
     *            Die Map von Semesterzeiten.
     */
    public void setTimes(Map<String, SemesterTime> times) {
        this.times = times;
    }

    /**
     * Gib die ausgewählte Semesterzeit zurück.
     * 
     * @return die ausgewählte Semesterzeit.
     */
    public SemesterTime getSelectedTimes() {
        return selectedTimes;
    }

    /**
     * Setzt das ausgewählte Jahr durch den übergebenen Parameter.
     * 
     * @param selectedTimes
     *            Die zu auswählende Semesterzeit.
     */
    public void setSelectedTimes(SemesterTime selectedTimes) {
        this.selectedTimes = selectedTimes;
    }

    /**
     * Gibt die Liste der Jahren zurück, die in der Applikation bekannt sind.
     * 
     * @return Die Liste von Jahren.
     */
    public List<String> getYears() {
        return years;
    }

    /**
     * Setzt die Liste von Jahren anhand des Parameters.
     * 
     * @param pYears
     *            Die Liste von Jahren.
     */
    public void setYears(List<String> pYears) {
        this.years = years;
    }

    /**
     * Gibt das ausgewählte Jahr zurück.
     * 
     * @return Das ausgewählte Jahr.
     */
    public String getSelectedYear() {
        return selectedYear;
    }

    /**
     * Setzt das ausgewählte Jahr durch den übergebenen Parameter.
     * 
     * @param selectedYear
     *            Das zu auswählende Jahr.
     */
    public void setSelectedYear(String selectedYear) {
        this.selectedYear = selectedYear;
    }

    // ////Wird versucht auf businesslogic.Math zu verschieben ///////////////////
    /**
     * Liefert eine einfache Map mit den verfügbaren Jahren im System zurück. Diese werden
     * als Auswahl im Drop-Down Menu bei der erstellung einer ILV verfügbar sein.
     *
     * @return Eine einfache Map mit verfügbaren Rollen.
     */
    private List<String> calculateYearList() {
        final List<String> tmp = new ArrayList<String>();
        for (int i = 2018; i < 2080; i++) {
            tmp.add(Integer.toString(i));
        }
        return Collections.unmodifiableList(tmp);
    }

    /**
     * Liefert eine einfache Map mit den verfügbaren Semestern im System zurück.
     * 
     * @return Eine einfache Map mit verfügbaren Semestern.
     */
    private Map<String, SemesterTime> calculateSemesterMap() {
        final Map<String, SemesterTime> tmp = new LinkedHashMap<>();
        tmp.put("WiSe", SemesterTime.WINTER);
        tmp.put("SoSe", SemesterTime.SOMMER);
        return Collections.unmodifiableMap(tmp);
    }

    /**
     * Dupliziert eine ILV. Dabei wird die Lecture und das Semester übernommen. Das Jahr
     * wird um um eins erhöht.
     * 
     * @return Die Seite mit allen ILVs
     */
    // TODO: Bug mehrmaliges Duplizieren mit gleichen Werten ist möglich
    public String duplicateInstanceLecture(InstanceLecture pInstanceLecture) {
        assertNotNull(pInstanceLecture);
        InstanceLecture ilv = new InstanceLecture();
        List<InstanceLecture> allIlvsToLecture = instanceLectureDao
                .getInstanceLecturesForLecture(pInstanceLecture.getLecture());
        boolean isDuplicated = false;
        ilv.setLecture(pInstanceLecture.getLecture());
        Integer i = Integer.parseInt(pInstanceLecture.getYear());
        i = i + 1;
        for (InstanceLecture il : allIlvsToLecture) {
            Integer o = Integer.parseInt(il.getYear());
            if (i.equals(o)) {
                isDuplicated = true;
            }
        }
        if (!isDuplicated) {
            ilv.setYear(i + "");
            ilv.setSemester(pInstanceLecture.getSemester());
            ilv.addExaminer(user);
            user.addAsProfToIlv(ilv);
            try {
                instanceLectureDao.save(ilv);
                userDao.update(user);
            } catch (DuplicateInstanceLectureException ex) {
                throw new UnexpectedUniqueViolationException(ex);
            } catch (DuplicateUsernameException e) {
                e.printStackTrace();
            } catch (DuplicateEmailException e) {
                e.printStackTrace();
            }
        }
        return "semester.xhtml";
    }
}