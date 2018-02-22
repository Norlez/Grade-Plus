package controller;

import common.model.Exam;
import common.model.Lecture;
import common.model.Session;
import common.util.Assertion;
import persistence.ExamDAO;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Diese Bean ist dafür verantwortlich, die die vom Prüfer zu gebenden Prüfungen
 * anzuzeigen. Dort kann der Prüfer Termine verschieben oder Absagen.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-21
 */
@Named
@ViewScoped
public class ExamsBean extends AbstractBean implements Serializable {

    /**
     * Die eindeutige SerialisierungsID.
     */
    private static final long serialVersionUID = -1789862409211220952L;

    /**
     * Die aktuell angezeigte Lehrveranstaltung.
     */
    private Lecture lecture;

    /**
     * Der aktuell ausgewählte Prüfungstermin.
     */
    private Exam exam;

    /**
     * Eine Liste mit allen aktuell angezeigten Lehrveranstaltungen.
     */
    private List<Lecture> allLectures;

    /**
     * Eine Liste mit allen aktuell angezeigten Prüfungsterminen.
     */
    private List<Exam> allExams;

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für
     * Prüfungsveranstaltung-Objekte übernimmt.
     */
    private final ExamDAO examDAO;

    /**
     * Erzeugt eine neue ExamsBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ExamsBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public ExamsBean(final Session pSession, final ExamDAO pExamDao) {
        super(pSession);
        examDAO = assertNotNull(pExamDao);
    }

    /**
     * Initialisiert die Attribute {@link #lecture}, {@link #allLectures}, {@link #exam}
     * and {@link #allExams}, sodass {@link #lecture}, einen leeren Eintrag repräsentieren
     * und die Liste {@link #allExams} keine Attribute enthällt.
     */
    @PostConstruct
    public void init() {
        exam = new Exam();
        allExams = getAllExams();
    }

    /**
     * Setzt die gewählte Lehrveranstaltung in der Bean.
     *
     * @param pLecture
     */
    public void setLecture(Lecture pLecture) {
        lecture = Assertion.assertNotNull(pLecture);
    }

    /**
     * Gibt die Liste aller aktuell anzuzeigenden Lehrveranstaltungen zurück.
     *
     * @return Liste aller aktuell anzuzeigenden Lehrveranstaltungen.
     */
    public List<Lecture> getAllLectures() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den ausgewählten Termin in der Bean.
     *
     * @param pExam
     *            Der vom Püfer ausgewählte Termin.
     */
    public void setExam(Exam pExam) {
        exam = Assertion.assertNotNull(pExam);
    }

    /**
     * Gibt die Liste aller Prüfungstermine zurück.
     *
     * @return Die Liste aller Prüfungstermine.
     */
    public List<Exam> getAllExams() {
        return allExams;
    }

    /**
     * Ändert das Datum der Prüfung.
     *
     * @param pExam
     *            der ausgewählte Termin.
     * @return {@code true}, wenn das Datum geändert werden konnte, sonst {@code false}.
     */
    public boolean changeExamDate(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Löscht den Termin der Prüfung.
     *
     * @param pExam
     *            der ausgewählte Termin.
     * @return {@code true}, wenn Termin gelöscht werden konnte, sonst {@code false}.
     */
    public boolean deleteExamDate(Exam pExam) {
        Assertion.assertNotNull(pExam);
        throw new UnsupportedOperationException();
    }

    /**
     * Sendet eine Nachricht an den Prüfling.
     *
     * @param pNotification
     *            Die eingegebene Nachricht.
     * @return {@code true}, falls die Nachricht gesendet werden konnte, sonst
     *         {@code false}.
     */
    private boolean sendNotification(String pNotification) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktuallisiert die Prüfungswerte im Datenbestand.
     */
    public void update() {
        throw new UnsupportedOperationException();
    }

    public void conflictSolution(LocalTime upperBound, LocalTime lowerBound,
            LocalTime cLT, LocalDate cLD, Exam pExam) {
        while (isValidDate(cLD, cLT, pExam) != true) {
            // TODO: Wie lang soll die Pause sein.
        }
    }

    /**
     * Prüft den Prüfungstermin.
     * 
     * @param ld
     *            , das zu prüfende Datum.
     * @param lt
     *            , der zu prüfende Zeitslot.
     * @param pExam
     *            , eine vorhandene Prüfung.
     * @return true, falls der Zeitpunkt für eine Prüfung gültig ist.
     */
    public boolean isValidDate(LocalDate ld, LocalTime lt, Exam pExam) {
        if (isDateUsed(ld, pExam) == true) {
            if (isTimeUsed(lt, pExam.getExamLength(), pExam) == true) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * TODO nicht geprüft Prüft, ob das Datum bereits belegt ist von einer Prüfung.
     * 
     * @param pDate
     *            Das Datum der Prüfung
     * @param pExam
     *            Erhält das Datum der Prüfung, um sie mit dem Termin abzugleichen.
     * @return true, falls die Termine übereinstimmen
     */
    public boolean isDateUsed(LocalDate pDate, Exam pExam) {
        return pDate.equals(pExam.getDate()) ? true : false;
    }

    /**
     * TODO: nciht geprüft Prüft, ob der Zeitslot besetzt ist oder nicht.
     *
     * @param pTime
     *            , der zu prüfende Slot.
     * @param length
     *            , die Länge des zu prüfenden Slots
     * @param pExam
     *            , die Prüfung, die auf den Slot geprüft wird.
     *
     * @return true, falls der Zeitslot besetzt ist.
     */
    public boolean isTimeUsed(final LocalTime pTime, final int length, final Exam pExam) {
        LocalTime t = pTime.plusMinutes(length);
        LocalTime o = pExam.getTime().plusMinutes(pExam.getExamLength());
        if (t.isBefore(pExam.getTime()) == true) {
            return true;
        } else if (pTime.isAfter(o) == true) {
            return true;
        } else {
            return false;
        }
    }
}
