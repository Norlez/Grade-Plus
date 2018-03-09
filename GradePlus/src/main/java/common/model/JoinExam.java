package common.model;

import javax.inject.Named;
import javax.persistence.*;
import java.util.List;

/**
 * Für die Speicherung der jeweiligen Einzelnoten und Gesundheitszustände der Studenten.
 *
 * @author Marvin Kampen, Torben Groß, Anil Olgun, Tugce Karakus
 * @version 2018-02-20
 */
@Entity
@Table(name = "JoinExams")
@NamedQueries({
        @NamedQuery(name = "JoinExams.getAll", query = "SELECT j FROM JoinExam j"),
        @NamedQuery(name = "JoinExams.getKrank", query = "SELECT j FROM JoinExam j WHERE j.krank = TRUE"),
        @NamedQuery(name = "JoinExams.getUsersForExam", query = "SELECT j FROM JoinExam j WHERE j.exam = :exam"),
        @NamedQuery(name = "JoinExam.getJoinExamsForUser", query = "SELECT j FROM JoinExam j WHERE j.pruefling = :pruefling") })
public class JoinExam extends JPAEntity {

    @ManyToOne(optional = true)
    private Exam exam;

    @ManyToOne
    private User pruefling;

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

    @ManyToOne
    private InstanceLecture instanceLecture;

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public User getPruefling() {
        return pruefling;
    }

    public void setPruefling(User pruefling) {
        this.pruefling = pruefling;
    }

    public Anmeldeart getKind() {
        return kind;
    }

    public void setKind(Anmeldeart kind) {
        this.kind = kind;
    }

    public boolean isKrank() {
        return krank;
    }

    public void setKrank(boolean krank) {
        this.krank = krank;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public InstanceLecture getInstanceLecture() {
        return instanceLecture;
    }

    public void setInstanceLecture(InstanceLecture instanceLecture) {
        this.instanceLecture = instanceLecture;
    }

    public String toCSV() {
        return String.format("%d; %s; %s; %s; %s, %f", getId(), kind, krank, exam,
                pruefling, grade);
    }
}
