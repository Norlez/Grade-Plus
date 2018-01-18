package controller;

import common.model.*;
import persistence.LectureDAO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Dieses Bean ist für die Erstellung von Prüfungen einer Instanz einer Lehrveranstaltung
 * zuständig.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LectureInstanceExamBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectrueInstanceExamBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectrueInstanceExamBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@link pLectureDAO} {@code null} ist.
     */
    public LectureInstanceExamBean(Session pSession, LectureDAO pLectureDAO) {
        super(pSession);
    }

    /**
     * Gibt die Lehrveranstaltung der Prüfung zurück.
     *
     * @return Die Lehrveranstaltung der Prüfung.
     */
    public Lecture getLecture() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Lehrveranstaltung der Prüfung auf die gegebene Lehrveranstaltung.
     *
     * @param pLecture
     *            Die neue Lehrveranstaltung der Prüfung.
     */
    public void setLecture(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt den Startzeitpunkt der Prüfung zurück.
     *
     * @return Den Startzeitpunkt der Prüfung.
     */
    public LocalDateTime getDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Startzeitpunkt der Prüfung auf die gegebene Zeit.
     *
     * @param pDate
     *            Der neue Startzeitpunkt der Prüfung.
     */
    public void setDate(LocalDateTime pDate) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Prüfungsdauer in Minuten zurück.
     *
     * @return Die Prüfungsdauer in Minuten.
     */
    public int getExamLength() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfungsdauer auf die gegebene Dauer in Minuten.
     *
     * @param pExamLength
     *            Die neue Prüfungsdauer in Minuten.
     */
    public void setExamLength(int pExamLength) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Art der Prüfung als String zurück.
     *
     * Bei einer Gruppenprüfung wird beispielsweise "Gruppe" zurückgegeben.
     *
     * @return Die Art der Prüfung als String.
     */
    public String getType() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Art der Prüfung auf die gegebene Prüfungsart.
     *
     * @param pType
     *            Die neue Prüfungsart.
     */
    public void setType(String pType) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt zurück, ob es sich bei der Prüfung um eine Gruppenprüfung hantelt.
     *
     * @return {@code true} Falls es sich bei der Prüfung um eine Gruppenprüfung handelt,
     *         sonst {@code false}.
     */
    public boolean isGroupExam() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Prüfer als Liste zurück.
     *
     * @return Die Prüfer als Liste.
     */
    public List<User> getExaminers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer den Prüfern hinzu.
     *
     * @param theExaminer
     *            Der hinzuzufügende Prüfer.
     */
    public void addExaminer(User theExaminer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der gegebenen Liste vorhandenen Benutzer den Prüfern hinzu.
     *
     * @param pExaminers
     *            Die Liste der hinzuzufügenden Prüfer.
     */
    public void addExaminers(List<User> pExaminers) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfer auf den gegebenen Benutzer.
     *
     * @param pExaminer
     *            Der neue Prüfer.
     */
    public void setExaminer(User pExaminer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfer auf die in der gegebenen Liste vorhandenen Benutzer.
     *
     * @param pExaminers
     *            Die neuen Prüfer.
     */
    public void setExaminers(List<User> pExaminers) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Prüfer.
     *
     * @param pExaminer
     *            Der zu entfernende Prüfer.
     */
    public void removeExaminer(User pExaminer) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Benutzer als Prüfer.
     *
     * @param pExaminers
     *            Die Liste der zu entfernenden Prüfer.
     */
    public void removeExaminers(List<User> pExaminers) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Prüflinge als Liste zurück.
     *
     * @return Die Prüflinge als Liste.
     */
    public List<User> getExaminees() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer als Prüfling hinzu.
     *
     * @param pExaminee
     *            Der hinzuzufügende Prüfling.
     */
    public void addExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die in der gegebenen Liste vorhandenen Benutzer den Prüflingen hinzu.
     *
     * @param pExaminees
     *            Die Liste der hinzuzufügenden Prüflinge.
     */
    public void addExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüflinge auf den gegebenen Benutzer.
     *
     * @param pExaminee
     *            Der neue Prüfling.
     */
    public void setExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüflinge auf die in der gegebenen Liste vorhandenen Benutzer.
     *
     * @param pExaminees
     *            Die Liste der neuen Prüflinge.
     */
    public void setExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Prüfling.
     *
     * @param pExaminee
     *            Der zu entfernende Prüfling.
     */
    public void removeExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Benutzer als Prüflinge.
     *
     * @param pExaminees
     *            Die Liste der zu entfernenden Prüflinge.
     */
    public void removeExaminees(List<User> pExaminees) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Noten der Prüfung als Liste zurück.
     *
     * @return Die Noten der Prüfung als Liste.
     */
    public List<Grade> getGrades() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Note des gegebenen Nutzers zurück.
     *
     * @param pExaminee
     *            Der Prüfling.
     * @return Die Note des gegebenen Benutzers.
     */
    public Grade getGrade(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die gegebene Note zu den Noten der Prüfung hinzu.
     *
     * @param pGrade
     *            Die hinzuzufügende Note.
     */
    public void addGrade(Grade pGrade) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die in der gegebenen Liste vorhandenen Noten zu den Noten der Prüfung hinzu.
     *
     * @param pGrades
     *            Die Liste der hinzuzufügenden Noten.
     */
    public void addGrades(List<Grade> pGrades) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die gegebene Note aus den Noten der Prüfung.
     *
     * @param pGrade
     *            Die zu entfernende Note.
     */
    public void removeGrade(Grade pGrade) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die in der gegebenen Liste vorhandenen Noten aus den Noten der Prüfung.
     *
     * @param pGrades
     *            Die Liste der zu entfernenden Noten.
     */
    public void removeGrades(List<Grade> pGrades) {
        throw new UnsupportedOperationException();
    }

}
