package common.model;

import java.util.List;

/**
 * Repräsentiert eine Lehrveranstaltung. Eine Lehrveranstaltung enthält einen Namen, eine
 * Vak-Nummer, ein Semester, die ECTS der Veranstaltung sowie einen Dozenten, Tutoren,
 * Prüflinge und Prüfungen.
 *
 * @author Torben Groß
 */
public class Lecture extends JPAEntity {

    /**
     * Der Name der Lehrveranstaltung.
     */
    private String name;

    /**
     * Die VAK-Nummer der Lehrveranstaltung.
     */
    private String vak;

    /**
     * Das Semester der Lehrveranstaltung.
     */
    private String semester;

    /**
     * Die ECTS der Lehrveranstaltung.
     */
    private int ects;

    /**
     * Die Liste der Dozenten der Lehrveranstaltung.
     */
    private List<User> lecturers;

    /**
     * Die Liste der Tutoren der Lehrveranstaltung.
     */
    private List<User> tutors;

    /**
     * Die Liste der Prüflinge der Lehrveranstaltung.
     */
    private List<User> examinees;

    /**
     * Die Liste der Prüfungen der Lehrveranstaltung.
     */
    private List<Exam> exams;

    /**
     * Gibt den Namen der Lehrveranstaltung zurück.
     *
     * @return Den Namen der Lehrveranstaltung.
     */
    public String getName() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Namen der Lehrveranstaltung auf den gegebenen Namen.
     *
     * @param pName
     *            Der neue Name der Lehrveranstaltung.
     */
    public void setName(String pName) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung zurück.
     *
     * @return Die VAK-Nummer der Lehrveranstaltung.
     */
    public String getVAK() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die VAK-Nummer der Lehrveranstaltung auf die gegebene VAK-Nummer.
     *
     * @param pVAK
     *            Die neue VAK-Nummer der Lehrveranstaltung.
     */
    public void setVAK(String pVAK) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das Semester der Lehrveranstaltung zurück.
     *
     * @return Das Semester der Lehrveranstaltung.
     */
    public String getSemester() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt das Semester der Lehrveranstaltung auf das gegebene Semester.
     *
     * @param pSemester
     *            Das neue Semester der Lehrveranstaltung.
     */
    public void setSemester(String pSemester) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die ECTS der Lehrveranstaltung zurück.
     *
     * @return Die ECTS der Lehrveranstaltung.
     */
    public int getECTS() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die ECTS der Lehrveranstaltung auf die gegebenen ECTS.
     *
     * @param pECTS
     *            Die neuen ECTS der Lehrveranstaltung.
     */
    public void setECTS(int pECTS) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Dozenten der Lehrveranstaltung als Liste zurück.
     *
     * @return Die Dozenten der Lehrveranstaltung.
     */
    public List<User> getLecturers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer der Lehrveranstaltung als Dozent hinzu.
     *
     * @param pLecturer
     *            Der als Dozent zur Lehrveranstaltung hinzuzufügende Benutzer.
     */
    public void addLecturer(User pLecturer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Dozent der Lehrveranstaltung.
     *
     * @param pLecturer
     *            Der als Dozent aus der Lehrveranstaltung zu entfernende Benutzer.
     */
    public void removeLecturer(User pLecturer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Tutoren der Lehrveranstaltung als Liste zurück.
     *
     * @return Die Tutoren der Lehrveranstaltung als Liste.
     */
    public List<User> getTutors() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer der Lehrveranstaltung als Tutor hinzu.
     *
     * @param pTutor
     *            Der als Tutor zur Lehrveranstaltung hinzuzufügende Benutzer.
     */
    public void addTutor(User pTutor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der gegebenen Liste vorhandenen Benutzer der Lehrveranstaltung als
     * Tutoren hinzu.
     *
     * @param pTutors
     *            Die Liste der als Tutoren zur Lehrveranstaltung hinzuzufügenden
     *            Benutzer.
     */
    public void addTutors(List<User> pTutors) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Tutor der Lehrveranstaltung.
     *
     * @param pTutor
     *            Der als Tutor aus der Lehrveranstaltung zu entfernende Benutzer.
     */
    public void removeTutor(User pTutor) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Benutzer als Tutoren der
     * Lehrveranstaltung.
     *
     * @param pTutors
     *            Die Liste der als Tutoren aus der Lehrveranstaltung zu entfernenden
     *            Benutzer.
     */
    public void removeTutors(List<User> pTutors) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer als Prüfling zur Lehrveranstaltung hinzu.
     *
     * @param pExaminee
     *            Der als Prüfling zur Lehrveranstaltung hinzuzufügende Benutzer.
     */
    public void addExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der gegebenen Liste vorhandenen Benutzer als Prüflinge zur
     * Lehrveranstaltung hinzu.
     *
     * @param pExaminees
     *            Die Liste der als Prüflinge zur Lehrveranstaltung hinzuzufügende
     *            Benutzer.
     */
    public void addExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüflinge der Lehrveranstaltung auf den gegebenen Benutzer.
     *
     * @param pExaminee
     *            Der neue Prüfling der Lehrveranstaltung.
     */
    public void setExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die in der gegebenen Liste vorhandenen Benutzer als Prüflinge der
     * Lehrveranstaltung.
     *
     * @param pExaminees
     *            Die Liste der neuen Prüflinge der Lehrveranstaltung.
     */
    public void setExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Prüfling der Lehrveranstaltung.
     *
     * @param pExaminee
     *            Der als Prüfling aus der Lehrveranstaltung zu entfernende Benutzer.
     */
    public void removeExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Benutzer als Prüflinge der
     * Lehrveranstaltung.
     *
     * @param pExaminees
     *            Die Liste der als Prüflinge aus der Lehrveranstaltung zu entferenden
     *            Benutzer.
     */
    public void removeExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Prüfungen der Lehrveranstaltung als Liste zurück.
     *
     * @return Die Prüfungen der Lehrveranstaltung als Liste.
     */
    public Exam getExam() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die gegebene Prüfung der Lehrveranstaltung hinzu.
     *
     * @param pExam
     *            Die zur Lehrveranstaltung hinzuzufügende Prüfung.
     */
    public void addExam(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der Liste gegebenen Prüfungen der Lehrveranstaltung hinzu.
     *
     * @param pExams
     *            Die Liste der zur Lehrveranstaltung hinzuzufügenden Prüfungen.
     */
    public void addExams(List<Exam> pExams) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfungen der Lehrveranstaltung auf die gegebene Prüfung.
     *
     * @param pExam
     *            Die neue Prüfung der Lehrveranstaltung.
     */
    public void setExam(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfungen der Lehrveranstaltung auf die in der gegebenen Liste
     * vorhandenen Prüfungen.
     *
     * @param pExams
     *            Die Liste der neuen Prüfungen der Lehrveranstaltung.
     */
    public void setExams(List<Exam> pExams) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die gegebene Prüfung aus der Lehrveranstaltung.
     *
     * @param pExam
     *            Die aus der Lehrveranstaltung zu entfernende Prüfung.
     */
    public void removeExam(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Prüfungen aus der
     * Lehrveranstaltung.
     *
     * @param pExams
     *            Die Liste der aus der Lehrveranstaltung zu entfernenden Prüfungen.
     */
    public void removeExams(List<Exam> pExams) {
        throw new UnsupportedOperationException();
    }

}
