package common.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "instanceLectures")
public class InstanceLecture extends JPAEntity {

    @ManyToOne(optional = false)
    private Lecture lecture;

    @ManyToMany
    private Set<User> pruefer;

    @ManyToMany
    private Set<User> pruefling;

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    // Prüfer
    public Set<User> getPruefer() {
        return pruefer;
    }

    public void setPruefer(Set<User> pruefer) {
        this.pruefer = pruefer;
    }

    public void addPruefer(User pPruefer) {
        pruefer.add(pPruefer);
    }

    public void removePruefer(User pPruefer) {
        pruefer.remove(pPruefer);
    }

    // Prüfling

    public void setPruefling(Set<User> pruefling) {
        this.pruefling = pruefling;
    }

    public Set<User> getPruefling() {
        return pruefling;
    }

    public void addPruefling(User pPruefling) {
        pruefling.add(pPruefling);
    }

    public void removePruefling(User pPruefling) {
        pruefling.remove(pPruefling);
    }
}
