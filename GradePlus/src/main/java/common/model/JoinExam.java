package common.model;

import javax.inject.Named;
import javax.persistence.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Für die Speicherung der jeweiligen Einzelnoten und Gesundheitszustände der Studenten.
 * Eine JoinExam steht für eine Prüfung zwischen Prüfling und Prüfer.
 *
 * @author Marvin Kampen, Torben Groß, Anil Olgun, Tugce Karakus
 * @version 2018-03-11
 */
@Entity
@Table(name = "JoinExams")
@NamedQueries({
        @NamedQuery(name = "JoinExams.getAll", query = "SELECT j FROM JoinExam j"),
        @NamedQuery(name = "JoinExams.getKrank", query = "SELECT j FROM JoinExam j WHERE j.krank = TRUE"),
        @NamedQuery(name = "JoinExams.getUsersForExam", query = "SELECT j FROM JoinExam j WHERE j.exam = :exam"),
        @NamedQuery(name = "JoinExam.getJoinExamsForUser", query = "SELECT j FROM JoinExam j WHERE j.pruefling = :pruefling") })
public class JoinExam extends JPAEntity {

    /**
     * Der Prüfungstermin für die der Prüfling angemeldet ist.
     */
    @ManyToOne(optional = true)
    private Exam exam;

    /**
     * Eine Referenz auf den Prüfling, der die JoinExam erhalten hat.
     */
    @ManyToOne
    private User pruefling;

    /**
     * Die gespeicherten Noten (Teilnote und Endnote) für die InstaneLecture.
     */
    @OneToOne
    private Grade grade;

    /**
     * Der Gesundheitszustand des Studenten.
     */
    @Column
    private boolean krank = false;

    /**
     * Die Anmeldeart des Studenten.
     */
    @Column(nullable = true)
    private Anmeldeart kind;

    /**
     * Eine Referenz auf die InstanceLecture zu der die Prüfung gehört.
     */
    @ManyToOne
    private InstanceLecture instanceLecture;

    /**
     * Ein Kommentar zur Prüfung, den der Prüfer setzen kann.
     */
    @Column
    private String commentary;

    /**
     * Repräsentiert ein hochgeladenes File im Form eines byte-Arrays
     */
    @Column
    @Lob
    private byte[] savedDocument;

    /**
     * Getter für Exam-Attribut
     * @return exam
     */
    public Exam getExam() {
        return exam;
    }

    /**
     * Setter für Exam-Attribut
     * @param exam
     */
    public void setExam(Exam exam) {
        this.exam = exam;
    }

    /**
     * Getter für Prüfling-Attribut
     * @return pruefling
     */
    public User getPruefling() {
        return pruefling;
    }

    /**
     * Setter für Prüfling-Attribut
     * @param pruefling
     */
    public void setPruefling(User pruefling) {
        this.pruefling = pruefling;
    }

    /**
     * Getter für das Kind-Attribut.
     * @return kind, wie der Prüfling angemeldet wurde.
     */
    public Anmeldeart getKind() {
        return kind;
    }

    /**
     * Setter für das Kind-Attribut.
     * @param kind
     */
    public void setKind(Anmeldeart kind) {
        this.kind = kind;
    }

    /**
     * Getter für isKrank-Attribut
     * @return isKrank
     */
    public boolean isKrank() {
        return krank;
    }

    /**
     * Setter für isKrank-Attribut
     * @param krank
     */
    public void setKrank(boolean krank) {
        this.krank = krank;
    }

    /**
     * Getter für Commentary-Attribut
     * @return commentary
     */
    public String getCommentary() {
        return commentary;
    }

    /**
     * Setter für commentary-Attribut
     * @param commentary
     */
    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    /**
     * Getter für Grade-Attribut
     * @return grade
     */
    public Grade getGrade() {
        return grade;
    }

    /**
     * Setter für Grade-Attribut
     * @param grade
     */
    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    /**
     * Getter für InstanceLecture-Attribut
     * @return instanceLecture
     */
    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    /**
     * Setter für InstanceLecture-Attribut
     * @param instanceLecture
     */
    public void setInstanceLecture(InstanceLecture instanceLecture) {
        this.instanceLecture = instanceLecture;
    }



    /**
     * Gibt das hochgeladene File in Form eines Byte-Arrays zurück.
     *
     * @return
     */
    public byte[] getSavedDocument() {
        return savedDocument;
    }

    /**
     * Setzt den File in Form eines byte-Arrays.
     * @param savedDocument Das zu speichernde byte-Array.
     */
    public void setSavedDocument(byte[] savedDocument) {
        this.savedDocument = savedDocument;
    }

    /**
     * Ein String der alle relevanten Attribute der JoinExam enthält. Sollte für den CsvWriter verwendet werden.
     * @return String im Format attribut; attribut; und so weiter
     */
    public String toCSV() {
        return String.format("%d; %s; %s; %s; %s, %f", getId(), kind, krank, exam,
                pruefling, grade);
    }
}
