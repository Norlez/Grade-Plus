package common.model;

import javax.persistence.Column;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import java.util.List;

public class Teilnehmen {

    private Exam exam;

    private User pruefling;

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
}
