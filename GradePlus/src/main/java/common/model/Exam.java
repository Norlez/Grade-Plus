package common.model;

import java.time.LocalDateTime;
import java.util.List;

//import com.sun.xml.internal.bind.v2.TODO;
import common.util.Assertion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Repräsentiert eine Prüfung. Eine Prüfung enthält ein Datum, eine Lehrveranstaltung,
 * Prüfer, Prüflinge sowie die Information, ob es sich bei der Prüfung um eine Einzel-
 * oder Gruppenprüfung handelt.
 *
 * @author Torben Groß, Marvin Kampen
 * @version 2018-02-08
 *
 */
@Entity
@Table(name = "Exams")
public class Exam extends JPAEntity {

    /**
     * Die Prüfungsordnung der Prüfung.
     */
    @Column
    private String examRegulations;

    /**
     * Die Lehrveranstaltung der Prüfung.
     */
    @ManyToOne(optional = false)
    private InstanceLecture ilv;

    /**
     * Der Startzeitpunkt der Prüfung.
     */
    @Column
    private LocalDateTime date;

    /**
     * Die Prüfungsdauer.
     */
    @Column
    private int examLength;

    /**
     * Der Prüfungsort.
     */
    @Column
    private String location;

    /**
     * Die Art der Prüfung (bspw. Fachgespräch).
     */
    @Column
    private String type;

    /**
     * Gibt an, ob es sich bei der Prüfung um eine Gruppenprüfung handelt.
     */
    @Column
    private boolean groupExam;

    /**
     * Die Liste der Prüfer.
     */
    private List<User> examiners;

    /**
     * Die Liste der Prüflinge.
     */
    private List<User> examinees;

    /**
     * Die Liste der Noten der Prüfung.
     */
    private List<Grade> grades;

    /**
     * Die Bemerkung zur Prüfung.
     */
    @Column
    private String comment;

    /**
     * Gibt die Prüfungsordnung der Prüfung zurück.
     *
     * @return Die Prüfungsordnung der Prüfung.
     */
    public String getExamRegulations() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfungsordnung der Prüfung auf die gegebene Prüfungsordnung.
     *
     * @param pExamRegulations
     *            Die neue Prüfungsordnung.
     */
    public void setExamRegulations(String pExamRegulations) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die ILV der Prüfung zurück.
     *
     * @return Die ILV der Prüfung.
     */
    public InstanceLecture getIlv() {
        return ilv;
    }

    /**
     * Setzt die ILV der Prüfung auf die gegebene Lehrveranstaltung.
     *
     * @param pILV
     *            Die neue Lehrveranstaltung der Prüfung.
     */
    public void setIlv(InstanceLecture pILV) {
        Assertion.assertNotNull(pILV);
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
     * Gibt den Prüfungsort zurück.
     * 
     * @return Den Prüfungsort.
     */
    public String getLocation() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Prüfungsort auf den gegebenen Wert.
     * 
     * @param pLocation
     *            Der neue Prüfungsort.
     */
    public void setLocation(String pLocation) {
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
    public void pType(String pType) {
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
