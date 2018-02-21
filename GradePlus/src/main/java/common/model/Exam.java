package common.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

//import com.sun.xml.internal.bind.v2.TODO;
import common.util.Assertion;

import javax.persistence.*;

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
@NamedQueries({
        @NamedQuery(name = "Exam.findAll", query = "SELECT e FROM Exam e"),
        @NamedQuery(name = "Exam.findByLecture", query = "SELECT e FROM Exam e WHERE e.ilv = :ilv"),
        @NamedQuery(name = "Exam.findByDate", query = "SELECT e FROM Exam e WHERE e.date = :date") })
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
     * Das Datum der Prüfung.
     */
    @Column
    private LocalDate date;

    /**
     * Der Startzeitpunkt der Prüfung
     */
    @Column
    private LocalTime time;

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
        return examRegulations;
    }

    /**
     * Setzt die Prüfungsordnung der Prüfung auf die gegebene Prüfungsordnung.
     *
     * @param pExamRegulations
     *            Die neue Prüfungsordnung.
     */
    public void setExamRegulations(String pExamRegulations) {
        examRegulations = Assertion.assertNotNull(pExamRegulations);
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
     *            Die neue ILV der Prüfung.
     */
    public void setIlv(InstanceLecture pILV) {
        ilv = Assertion.assertNotNull(pILV);
    }

    /**
     * Gibt den Termin der Prüfung zurück.
     * 
     * @return Das Datum der Prüfung
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Setzt den Termin für die Prüfung.
     * 
     * @param date
     *            : Termin für die Prüfung.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Gibt den Startzeitpunkt der Prüfung zurück.
     *
     * @return Den Startzeitpunkt der Prüfung.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Setzt den Startzeitpunkt der Prüfung auf die gegebene Zeit.
     *
     * @param time
     *            Der neue Startzeitpunkt der Prüfung.
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Gibt die Prüfungsdauer in Minuten zurück.
     *
     * @return Die Prüfungsdauer in Minuten.
     */
    public int getExamLength() {
        return examLength;
    }

    /**
     * Setzt die Prüfungsdauer auf die gegebene Dauer in Minuten.
     *
     * @param pExamLength
     *            Die neue Prüfungsdauer in Minuten.
     */
    public void setExamLength(int pExamLength) {
        examLength = pExamLength;
    }

    /**
     * Gibt den Prüfungsort zurück.
     *
     * @return Den Prüfungsort.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setzt den Prüfungsort auf den gegebenen Wert.
     *
     * @param pLocation
     *            Der neue Prüfungsort.
     */
    public void setLocation(String pLocation) {
        location = Assertion.assertNotNull(pLocation);
    }

    /**
     * Gibt die Art der Prüfung als String zurück.
     *
     * Bei einer Gruppenprüfung wird beispielsweise "Gruppe" zurückgegeben.
     *
     * @return Die Art der Prüfung als String.
     */
    public String getType() {
        return type;
    }

    /**
     * Setzt die Art der Prüfung auf die gegebene Prüfungsart.
     *
     * @param pType
     *            Die neue Prüfungsart.
     */
    public void setType(String pType) {
        type = Assertion.assertNotEmpty(pType);
    }

    /**
     * Gibt zurück, ob es sich bei der Prüfung um eine Gruppenprüfung hantelt.
     *
     * @return {@code true} Falls es sich bei der Prüfung um eine Gruppenprüfung handelt,
     *         sonst {@code false}.
     */
    public boolean isGroupExam() {
        return groupExam;
    }

    /**
     * Gibt an ob eine Gruppenprüfung vorliegt
     *
     * @param groupExam
     *            setzt die Prüfung auf den gegebenen Wert.
     */
    public void setGroupExam(boolean groupExam) {
        this.groupExam = groupExam;
    }

    // Prüfer
}