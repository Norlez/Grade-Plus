package common.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Teilnehmen extends JPAEntity {

    @OneToOne
    private Exam exam;

    @OneToOne
    private User pruefling;

    @OneToOne
    private Grade grade;

    @OneToMany(mappedBy = "ExamAsProf")
    private List<User> examiner;

    @Column
    private boolean krank = false;

    @Column(nullable = true)
    private Anmeldeart kind;

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

    public List<User> getExaminer() {
        return examiner;
    }

    public void setExaminer(List<User> examiner) {
        this.examiner = examiner;
    }
}
