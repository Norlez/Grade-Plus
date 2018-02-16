package controller;

import common.model.Exam;
import common.model.Lecture;
import common.model.Session;
import common.util.Assertion;

import javax.enterprise.context.RequestScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
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
     * Erzeugt eine neue ExamsBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden ExamsBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public ExamsBean(Session pSession) {
        super(pSession);
    }

    /**
     * Initialisiert die Attribute {@link #lecture}, {@link #allLectures}, {@link #exam}
     * and {@link #allExams}, sodass {@link #lecture}, einen leeren Eintrag repräsentieren
     * und die Liste {@link #allExams} keine Attribute enthällt.
     */
    public void init() {
        throw new UnsupportedOperationException();
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

}
