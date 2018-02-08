package common.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Eine Instanz der Lehrveranstaltung (ILV). Ist eine konkrete Ausprägung in der sich die
 * Studenten eintragen können.
 * 
 * @author Marvin Kampen
 * @version 2018-02-08
 */
@Entity
@Table(name = "instanceLectures")
public class InstanceLecture extends JPAEntity {

    /**
     * Die Lehrveranstaltung zu der die ILV gehört. Die ILV existiert nur so lange es eine
     * LV gibt.
     */
    @ManyToOne(optional = false)
    private Lecture lecture;

    /**
     * Die Prüfer der ILV.
     */
    @ManyToMany
    private Set<User> pruefer;

    /**
     * Die Prüflinge der ILV.
     */
    @ManyToMany
    private Set<User> pruefling;

    /**
     * Die Prüfungstermine, die zur ILV gehören
     */
    @OneToMany(mappedBy = "ilv", cascade = CascadeType.REMOVE)
    private Set<Exam> termine;

    /**
     * Erhält die LV für die ILV.
     * 
     * @return Die LV der ILV.
     */
    public Lecture getLecture() {
        return lecture;
    }

    /**
     * Setzt die LV der ILV.
     * 
     * @param lecture
     *            wird als LV übernommen.
     */
    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }

    // Prüfer

    /**
     * Erhält die Prüfer der ILV.
     * 
     * @return Die Prüfer
     */
    public Set<User> getPruefer() {
        return pruefer;
    }

    /**
     * Setzt die Prüfer der ILV.
     * 
     * @param pruefer
     *            wird als Prüfer der ILV gesetzt.
     */
    public void setPruefer(Set<User> pruefer) {
        this.pruefer = pruefer;
    }

    /**
     * Fügt einen Prüfer zu der ILV hinzu.
     * 
     * @param pPruefer
     *            , der hinzugefügt wird.
     */
    public void addPruefer(User pPruefer) {
        pruefer.add(pPruefer);
    }

    /**
     * Entfernt einen Prüfer aus der ILV.
     * 
     * @param pPruefer
     *            , der aus der ILV entfernt wird.
     */
    public void removePruefer(User pPruefer) {
        pruefer.remove(pPruefer);
    }

    // Prüfling

    /**
     * Setzt die Prüflinge für die ILV.
     * 
     * @param pruefling
     *            wird für die ILV übernommen.
     */
    public void setPruefling(Set<User> pruefling) {
        this.pruefling = pruefling;
    }

    /**
     * Erhält die Prüflinge der ILV zurück.
     * 
     * @return Die Prüflinge
     */
    public Set<User> getPruefling() {
        return pruefling;
    }

    /**
     * Fügt einen Prüfling zur ILV hinzu.
     * 
     * @param pPruefling
     *            der hinzugefügt wird.
     */
    public void addPruefling(User pPruefling) {
        pruefling.add(pPruefling);
    }

    /**
     * Entfernt den Prüfling aus der ILV.
     * 
     * @param pPruefling
     *            der entfernt wird
     */
    public void removePruefling(User pPruefling) {
        pruefling.remove(pPruefling);
    }

    // Prüfungstermine

    /**
     *
     * @return
     */
    public Set<Exam> getTermine() {
        return termine;
    }

    /**
     *
     * @param termine
     */
    public void setTermine(Set<Exam> termine) {
        this.termine = termine;
    }

    /**
     *
     * @param pTermin
     */
    public void addTermin(Exam pTermin) {
        termine.add(pTermin);
    }

    /**
     *
     * @param pTermin
     */
    public void removeTermin(Exam pTermin) {
        termine.remove(pTermin);
    }
}
