package common.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;
import static common.util.Assertion.assertNull;

/**
 * Eine Instanz der Lehrveranstaltung (ILV). Ist eine konkrete Ausprägung in der sich die
 * Studenten eintragen können.
 *
 * @author Marvin Kampen, Torben Groß
 * @version 2018-02-10
 */
@Entity
@Table(name = "InstanceLectures")
@NamedQueries({
        @NamedQuery(name = "InstanceLectures.findAll", query = "SELECT l FROM InstanceLecture l"),
        @NamedQuery(name = "InstanceLectures.findForLecture", query = "SELECT l FROM InstanceLecture l WHERE l.lecture = :lecture"),
        @NamedQuery(name = "InstanceLectures.findForExaminee", query = "SELECT l FROM InstanceLecture l WHERE :examinee MEMBER OF l.examinees"),
        @NamedQuery(name = "InstanceLectures.findForExaminer", query = "SELECT l FROM InstanceLecture l WHERE :examiner MEMBER OF l.examiners") })
public class InstanceLecture extends JPAEntity {

    /**
     * Die Lehrveranstaltung zu der die ILV gehört. Die ILV existiert nur so lange es eine
     * LV gibt.
     */
    @ManyToOne(optional = false)
    private Lecture lecture;

    /**
     * Die Prüfer der ILV.
     */
    @OneToMany(mappedBy = "asProf")
    private List<User> examiners = new ArrayList<User>();

    /**
     * Die Prüflinge der ILV.
     */
    @OneToMany(mappedBy = "asStudent")
    private List<User> examinees;

    /**
     * Das Jahr in dem die ILV stattfindet.
     */
    @Column(nullable = false)
    private String theYear;

    /**
     * Das Semester in der die ILV stattfindet.
     */
    @Column(nullable = false)
    private String semester;

    /**
     * Die Prüfungen der ILV.
     */
    @OneToMany(mappedBy = "ilv", cascade = CascadeType.REMOVE)
    private List<Exam> exams;

    /**
     * Gibt die LV der ILV zurück.
     *
     * @return Die LV der ILV.
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Setzt die LV der ILV auf den gegebenen Wert.
     *
     * @param pLecture
     *            Die neue LV der ILV.
     */
    public void setLecture(final Lecture pLecture) {
        lecture = assertNotNull(pLecture);
    }

    // Prüfer

    /**
     * Gibt die Prüfer der ILV als Liste zurück.
     *
     * @return Die Prüfer der ILV.
     */
    public List<User> getExaminers() {
        return examiners;
    }

    /**
     * Fügt den gegebenen {@link User} als Prüfer zur ILV hinzu.
     *
     * @param pExaminer
     *            Der neue Prüfer der ILV.
     */
    public void addExaminer(final User pExaminer) {
        examiners.add(assertNotNull(pExaminer));
    }

    /**
     * Entfernt den gegebenen {@link User} als Prüfer aus der ILV.
     *
     * @param pExaminer
     *            Der als Prüfer zu entfernende Benutzer.
     */
    public void removeExaminer(final User pExaminer) {
        examiners.remove(assertNotNull(pExaminer));
    }

    // Prüfling

    /**
     * Gibt die Prüflinge der ILV als Liste zurück.
     *
     * @return Die Prüflinge der ILV.
     */
    public List<User> getExaminees() {
        return examinees;
    }

    /**
     * Fügt den gegebenen {@link User} als Prüfling zur ILV hinzu.
     *
     * @param pExaminee
     *            Der der ILV als Prüfling hinzuzufügende Benutzer.
     */
    public void addExaminee(final User pExaminee) {
        examinees.add(assertNotNull(pExaminee));
    }

    /**
     * Entfernt den gegebenen {@link User} als Prüfling aus der ILV.
     *
     * @param pExaminee
     *            Der aus der ILV als Prüfling zu entfernende Benutzer.
     */
    public void removeExaminee(final User pExaminee) {
        examinees.remove(assertNotNull(pExaminee));
    }

    // Prüfungstermine

    /**
     * Gibt die Prüfungen der ILV als Liste zurück.
     *
     * @return Die Prüfungen der ILV als Liste.
     */
    public List<Exam> getExams() {
        return exams;
    }

    /**
     * Fügt die gegebene Prüfung der ILV hinzu.
     *
     * @param pExam
     *            Die zur ILV hinzuzufügende Prüfung.
     */
    public void addExam(final Exam pExam) {
        exams.add(assertNotNull(pExam));
    }

    /**
     * Entfernt die gegebene Prüfung aus der ILV.
     *
     * @param pExam
     *            Die aus der ILV zu entfernende Prüfung.
     */
    public void removeExam(final Exam pExam) {
        exams.remove(assertNotNull(pExam));
    }

    /**
     * Gibt das Jahr der ILV zurück.
     *
     * @return Das Jahr der ILV.
     */
    public String getYear() {
        return theYear;
    }

    /**
     * Setzt das Jahr der ILV auf den gegebenen Wert.
     *
     * @param pYear
     *            Das neue Jahr der ILV.
     */
    public void setYear(final String pYear) {
        theYear = assertNotNull(pYear);
    }

    /**
     * Gibt das Semester der ILV zurück.
     *
     * @return Das Semester der ILV.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Setzt das Semester der IlV auf den gegebenen Wert.
     *
     * @param pSemester
     *            Das neue Semester der ILV.
     */
    public void setSemester(final String pSemester) {
        semester = assertNotNull(pSemester);
    }
}
