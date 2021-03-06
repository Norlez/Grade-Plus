package common.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//import com.sun.xml.internal.bind.v2.TODO;
import common.util.Assertion;

import javax.persistence.*;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;

/**
 * Repräsentiert eine Prüfung. Eine Prüfung enthält ein Datum, eine Lehrveranstaltung,
 * Prüfer, Prüflinge sowie die Information, ob es sich bei der Prüfung um eine Einzel-
 * oder Gruppenprüfung handelt.
 *
 * @author Torben Groß, Marvin Kampen
 * @version 2018-02-28
 *
 */
@Entity
@Table(name = "Exams")
@NamedQueries({
        @NamedQuery(name = "Exam.findAll", query = "SELECT e FROM Exam e"),
        @NamedQuery(name = "Exam.findByInstanceLecture", query = "SELECT e FROM Exam e WHERE e.instanceLecture = :instanceLecture"),
        @NamedQuery(name = "Exam.findByExaminer", query = "SELECT e FROM Exam e WHERE :examiner MEMBER OF e.examiners"),
        @NamedQuery(name = "Exam.findByStudent", query = "SELECT e FROM Exam e, e.participants p WHERE :student = p.pruefling"),
        @NamedQuery(name = "Exam.findByReleaseStatus", query = "SELECT e FROM Exam e WHERE e.isReleased = :isReleased") })
public class Exam extends JPAEntity {

    /**
     * Die Prüfungsordnung der Prüfung.
     */
    @Column
    private String examRegulations;

    /**
     * Die ILV der Prüfung.
     */
    @ManyToOne(optional = false)
    private InstanceLecture instanceLecture;

    /**
     * Die Prüflinge einer Prüfung als {@link JoinExam}-Objekt.
     */
    @OneToMany(mappedBy = "exam")
    private List<JoinExam> participants = new ArrayList<>();

    /**
     * Die Prüfer einer Prüfung.
     */
    @ManyToMany(mappedBy = "ExamAsProf")
    private List<User> examiners = new ArrayList<>();

    /**
     * Der Startpunkt der Prüfung.
     */
    @Column
    private LocalDateTime localDateTime;

    /**
     * Die Prüfungsdauer.
     */
    @Column
    private Integer examLength;

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
     * Die Bemerkung zur Prüfung.
     */
    @Column
    private String comment;

    /**
     * Der Freigabestatus eines Prüfungstermins.
     */
    @Column
    private boolean isReleased;

    /**
     * Die erlaubte Gruppengröße in der Prüfung
     */
    @Column
    private Integer groupSize;

    /**
     * Erzeugt ein neues {@link Exam}-Objekt mit leeren Daten.
     */
    public Exam() {
    }

    /**
     * Erzeugt ein neues {@link Exam}-Objekt und setzt {@link #instanceLecture} auf den
     * gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die ILV der Prüfung.
     */
    public Exam(final InstanceLecture pInstanceLecture) {
        assertNotNull(pInstanceLecture, "Exam: Exam(InstanceLecture)");
        instanceLecture = pInstanceLecture;
    }

    /**
     * Erzeugt ein neues {@link Exam}-Objekt mit allen Daten der gegebenen Prüfung.
     *
     * @param pExam
     *            Die Daten der zu erzeugenden Prüfung.
     */
    public Exam(final Exam pExam) {
        assertNotNull(pExam, "Exam: Exam(Exam)");
        examRegulations = pExam.getExamRegulations();
        instanceLecture = pExam.getInstanceLecture();
        participants = pExam.getParticipants();
        examiners = pExam.getExaminers();
        localDateTime = pExam.getLocalDateTime();
        examLength = pExam.getExamLength();
        location = pExam.getLocation();
        type = pExam.getType();
        comment = pExam.getComment();
        groupSize = pExam.getGroupSize();
    }

    /**
     * Gibt die Prüfungsordnung der Prüfung zurück.
     *
     * @return Die neue Prüfungsordnung der Prüfung.
     */
    public String getExamRegulations() {
        return examRegulations;
    }

    /**
     * Setzt die Prüfungsordnung der Prüfung auf den gegebenen Wert.
     *
     * @param pExamRegulations
     */
    public void setExamRegulations(final String pExamRegulations) {
        examRegulations = assertNotNull(pExamRegulations,
                "Exam: setExamRegulations(String)");
    }

    /**
     * Gibt die ILV der Prüfung zurück.
     *
     * @return Die ILV der Prüfung.
     */
    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    /**
     * Setzt die ILV der Prüfung auf den gegebenen Wert.
     *
     * @param pInstanceLecture
     *            Die neue ILV der Prüfung.
     */
    public void setInstanceLecture(final InstanceLecture pInstanceLecture) {
        instanceLecture = assertNotNull(pInstanceLecture,
                "Exam: setInstanceLecture(InstanceLecture)");
    }

    /**
     * Gibt die JoinExams der Prüfung zurück.
     *
     * @return Die JoinExams der Prüfung.
     */
    public List<JoinExam> getParticipants() {
        return participants;
    }

    /**
     * Fügt die gegebene JoinExam der Liste der JoinExams hinzu.
     *
     * @param pJoinExam
     *            Die hinzuzufügende JoinExam.
     */
    public void addParticipant(final JoinExam pJoinExam) {
        if (participants.contains(assertNotNull(pJoinExam,
                "Exam: addParticipant(JoinExam)"))) {
            return;
        }
        participants.add(pJoinExam);
    }

    /**
     * Setzt die JoinExams der Prüfung auf den gegebenen Wert.
     *
     * @param pParticipants
     *            Die neuen JoinExams der Prüfung.
     */
    public void setParticipants(final List<JoinExam> pParticipants) {
        participants = assertNotNull(participants,
                "Exam: setParticipants(List<JoinExam>)");
    }

    /**
     * Getter für das GroupSize-Attribut
     * @return  groupsize
     */
    public Integer getGroupSize() {
        return groupSize;
    }

    /**
     * Setter für das GroupSize-Attribut
     * @param pGroupSize, darf nicht null sein
     */
    public void setGroupSize(final Integer pGroupSize) {
        groupSize = assertNotNull(pGroupSize);
    }

    /**
     * Gibt die verfügbaren freien Plätze für einen Prüfungstermin zurück.
     * @return Freie Plätze als int
     */
    public Integer getAvailableSpots() {
        return groupSize - getParticipants().size();
    }

    /**
     * Gibt zurück, ob ein Platz in  einer Prüfung verfügbar ist oder nicht.
     * @return true, wenn der Platz verfügbar ist. False, wenn kein Platz verfügbar ist.
     */
    public boolean isSpotAvailable() {
        return getAvailableSpots() > 0;
    }

    /**
     * Gibt alle Prüflinge der Prüfung zurück.
     *
     * @return Alle Prüflinge der Prüfung.
     */
    public List<User> getStudents() {
        return participants.stream().map(JoinExam::getPruefling)
                .collect(Collectors.toList());
    }

    /**
     * Entfernt die gegebene JoinExam aus der Prüfung.
     *
     * @param pParticipant
     *            Die zu entfernende JoinExam.
     */
    public void removeParticipant(final JoinExam pParticipant) {
        participants.remove(assertNotNull(pParticipant,
                "Exam: removeParticipant(JoinExam)"));
    }

    /**
     * Gibt die Prüfer der Prüfung zurück.
     *
     * @return Die Prüfer der Prüfung.
     */
    public List<User> getExaminers() {
        return examiners;
    }

    /**
     * Fügt den gegebenen Benutzer der Liste aller Prüfer der Prüfung hinzu.
     *
     * @param pExaminer
     *            Der zur Prüfung als Prüfer einzutragende Benutzer.
     */
    public void addExaminer(final User pExaminer) {
        if (!examiners.contains(assertNotNull(pExaminer, "Exam: addExaminer(User)"))) {
            examiners.add(pExaminer);
        }
    }

    /**
     * Setzt die Prüfer der Prüfung auf den gegebenen Wert.
     *
     * @param pExaminers
     *            Die neuen Prüfer der Prüfung.
     */
    public void setExaminers(final List<User> pExaminers) {
        examiners = assertNotNull(pExaminers, "Exam: setExaminers(List<User>)");
    }

    /**
     * Entfernt den gegebenen Benutzer als Prüfer der Prüfung.
     *
     * @param pExaminer
     *            Der aus der Prüfung als Prüfer zu entfernende Benutzer.
     */
    public void removeExaminer(final User pExaminer) {
        examiners.remove(pExaminer);
    }

    /**
     * Gibt den Startpunkt der Prüfung zurück.
     *
     * @return Den Startpuntk der Prüfung.
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Setzt den Startpunkt der Prüfung auf den gegebenen Wert.
     *
     * @param pLocalDateTime
     *            Der neue Startpunkt der Prüfung.
     */
    public void setLocalDateTime(final LocalDateTime pLocalDateTime) {
        localDateTime = assertNotNull(pLocalDateTime.withSecond(0).withNano(0),
                "Exam: setLocalDateTime(LocalDateTime)");
    }

    /**
     * Gibt die Länge der Prüfung zurück.
     *
     * @return Die Länge der Prüfung.
     */
    public Integer getExamLength() {
        return examLength;
    }

    /**
     * Setzt die Länge der Prüfung auf den gegebenen Wert.
     *
     * @param pExamLength
     *            Die neue Länge der Prüfung.
     */
    public void setExamLength(final Integer pExamLength) {
        examLength = assertNotNull(pExamLength, "Exam: setExamLength(Integer)");
    }

    /**
     * Gibt den Ort der Prüfung zurück.
     *
     * @return Den Ort der Prüfung.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setzt den Ort der Prüfung auf den gegebenen Wert.
     *
     * @param pLocation
     *            Der neue Ort der Prüfung.
     */
    public void setLocation(final String pLocation) {
        location = assertNotNull(pLocation, "Exam: setLocation(String)");
    }

    /**
     * Gibt den Typen der Prüfung (bspw. Fachgespräch) zurück.
     *
     * @return Den Typen der Prüfung.
     */
    public String getType() {
        return type;
    }

    /**
     * Setzt den Typen der Prüfung (bspw. Fachgespräch) auf den gegebenen Wert.
     *
     * @param pType
     *            Der neue Typ der Prüfung.
     */
    public void setType(final String pType) {
        type = assertNotNull(pType, "Exam: setType(String)");
    }

    /**
     * Gibt die Bemerkung der Prüfung zurück.
     *
     * @return Die Bemerkung der Prüfung.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Setzt die Bemerkung der Prüfung auf den gegebenen Wert.
     *
     * @param pComment
     *            Die neue Bemerkung der Prüfung.
     */
    public void setComment(final String pComment) {
        comment = assertNotNull(pComment, "Exam: setComment(String)");
    }

    /**
     * Setzt den Freigabestatus der Prüfung auf den gegebenen Wert..
     * 
     * @param pReleased
     *            Der neue Freigabestatus der Prüfung.
     */
    public void setReleased(final Boolean pReleased) {
        isReleased = assertNotNull(pReleased);
    }

    /**
     * Gibt den Freigabestatus der Prüfung zurück.
     * 
     * @return {@code true}, falls die Prüfung freigegeben ist, sonst {@code false}.
     */
    public boolean getReleased() {
        return isReleased;
    }

    /**
     * Gibt das Datum als String zurück.
     *
     * @return Das Datum als String.
     */
    public String dateToString() {
        return localDateTime
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
    }

    /**
     * Gibt die Uhrzeit als String zurück.
     *
     * @return Die Uhrzeit als String.
     */
    public String timeToString() {
        return localDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
    }

    /**
     * Gibt eine String Repräsentation des Exam-Objekts zurück.
     * @return String Repräsentation
     */
    @Override
    public String toString() {
        return String.format(
                "Exam: ID: %d, Lecture: %s, Time: %s, Length: %d, Freigegeben: %b",
                getId(), instanceLecture.getLecture().getName(), localDateTime
                        .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM,
                                FormatStyle.SHORT)), examLength, isReleased);
    }

    /**
     * Gibt einen String zurück für den CsvWriter
     * @return CSV-String im Format: Attribut;Attribut;
     */
    public String toCSV() {
        return String.format("%d; %s; %d; %s; %b, %s, %s, %s", getId(), comment,
                examLength, examRegulations, isReleased, localDateTime, type,
                instanceLecture);
    }

    /**
     * Gibt den Prüfungstermin als String zurück.
     * @return date in Form des Strings.
     */
    public String examDateToString() {
        String date = dateToString(localDateTime);
        return date;
    }

    /**
     * Gibt den Prüfungstermin mitsamt der Prüfungslänge einberechnet als String zurück.
     * @return date in Form von Datum der Veranstaltung und das Ende der Prüfung als Zeit
     */
    public String datePlusExamLengthToString() {
        LocalDateTime ldtNew = getLocalDateTime().plusMinutes(getExamLength());
        String date = dateToString(ldtNew);
        return date;
    }

    /**
     * Gibt das Datum des Parameter als String zurück
     * @param localDateTime
     * @return Format: yyyyMMddThhmmssZ
     */
    private String dateToString(LocalDateTime localDateTime) {
        String date = new String("" + localDateTime.getYear() + localDateTime.getMonth()
                + localDateTime.getDayOfMonth() + "T" + localDateTime.getHour()
                + localDateTime.getMinute() + localDateTime.getSecond() + "Z");
        return date;
    }

    /**
     * Vergleicht die Gleichheit zweier Exam-Objekte.
     * @param theObject
     * @return true, falls die Objektegleich sind, sonst false.
     */
    @Override
    public boolean equals(final Object theObject) {
        if (this == theObject) {
            return true;
        }
        if (theObject == null) {
            return false;
        }
        if (!(theObject instanceof Exam)) {
            return false;
        }
        final Exam otherExam = (Exam) theObject;
        return getId().equals(otherExam.getId());
    }

    /**
     * Formatiert die gegebene LocalDateTime um, damit sie für die ICS-Datei richtig
     * gespeichert wird. Dies ist der Anfang der Prüfung.
     *
     * @return Format: yyyyMMddTHHmm00
     */
    public String dateTimeToIcsFormat() {
        return "Europe/Berlin:"
                + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "T"
                + localDateTime.format(DateTimeFormatter.ofPattern("HHmm00"));
    }

    /**
     * Formatiert die gegebene LocalDateTime um, damit sie für die ICS-Datei richtig
     * gespeichert wird. Dies ist das Ende der Prüfung.
     *
     * @return Format Europe/Berlin: yyyyMMddTHHmm00
     */
    public String dateTimeToIcsPlusExamLengthFormat() {
        return "Europe/Berlin:"
                + localDateTime.plusMinutes(getExamLength()).format(
                        DateTimeFormatter.ofPattern("yyyyMMdd"))
                + "T"
                + localDateTime.plusMinutes(getExamLength()).format(
                        DateTimeFormatter.ofPattern("HHmm00"));
    }

    /**
     * Gibt das Ende der Prüfung als String zurück.
     * 
     * @return Ende der Prüfung
     */
    public String endOfExam() {
        return localDateTime.plusMinutes(getExamLength()).format(
                DateTimeFormatter.ofPattern("HH:mm"));
    }

    /**
     * Gibt die Anzahl der verfügbaren Plätze in einem Exam als String zurück.
     * @return Format: Anzahl/Gruppengröße
     */
    public String emptySpotsToString() {
        return getAvailableSpots() + "/" + groupSize;
    }
}