package common.model;

import javax.persistence.*;
import java.util.Set;

/**
 * Eine Instanz der Lehrveranstaltung (ILV). Ist eine konkrete Ausprägung in der sich die
 * Studenten eintragen können.
 * 
 * @author Marvin Kampen
 * @version 2018-02-09
 */
@Entity
@Table(name = "instanceLectures")
@NamedQueries({
        @NamedQuery(name = "InstanceLectures.findAll", query = "SELECT l FROM InstanceLecture l"),
        @NamedQuery(name = "InstanceLectures.findForLecture", query = "SELECT l FROM InstanceLecture l WHERE l.lecture = :lecture") })
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
    @ManyToMany(targetEntity = User.class)
    private Set<User> pruefer;

    /**
     * Die Prüflinge der ILV.
     */
    @ManyToMany(targetEntity = User.class)
    private Set<User> pruefling;

    /**
     * Das Jahr in dem die ILV stattfindet.
     */
    @Column(nullable = false)
    private String year;

    /**
     * Das Semester in dem die ILV stattfindet (Winter oder Sommer).
     */
    @Column(nullable = false)
    private boolean semester;

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
     * Gibt die Prüfungstermine der ILV zurück.
     * 
     * @return Prüfungstermine
     */
    public Set<Exam> getTermine() {
        return termine;
    }

    /**
     * Setzt die Prüfungstermine der ILV.
     * 
     * @param termine
     *            der ILV
     */
    public void setTermine(Set<Exam> termine) {
        this.termine = termine;
    }

    /**
     * Fügt einen Prüfungstermin der ILV hinzu.
     * 
     * @param pTermin
     *            , der hinzugefügt wird
     */
    public void addTermin(Exam pTermin) {
        termine.add(pTermin);
    }

    /**
     * Entfernt einen Prüfungstermin aus der ILV.
     * 
     * @param pTermin
     *            , der entfernt wird
     */
    public void removeTermin(Exam pTermin) {
        termine.remove(pTermin);
    }

    /**
     * Gibt das Jahr der ILV zurück
     * 
     * @return Jahr der ILV
     */
    public String getYear() {
        return year;
    }

    /**
     * Setzt das Jahr der ILv.
     * 
     * @param year
     *            wird als Jahr übernommen.
     */
    public void setYear(String year) {
        this.year = year;
    }

    /**
     * Gibt das Semester der ILV zurück
     * 
     * @return das Semester als boolean
     */
    public boolean isSemester() {
        return semester;
    }

    /**
     * Setzt das Semester der Ilv
     * 
     * @param semester
     *            ist das Semester als boolean
     */
    public void setSemester(boolean semester) {
        this.semester = semester;
    }
}
