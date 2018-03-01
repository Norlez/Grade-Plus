package common.model;

import javax.persistence.*;
import java.util.Set;
import common.util.Assertion;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;

/**
 * Repräsentiert eine Lehrveranstaltung. Eine Lehrveranstaltung enthält einen Namen, eine
 * Vak-Nummer, ein Semester, die ECTS der Veranstaltung sowie eine Beschreibung der
 * Veranstaltung.
 *
 * @author Torben Groß
 */
@Entity
@Table(name = "Lectures")
@NamedQueries({
        @NamedQuery(name = "Lecture.findAll", query = "SELECT l FROM Lecture l"),
        @NamedQuery(name = "Lecture.findName", query = "SELECT l FROM Lecture l WHERE l.name = :name"),
        @NamedQuery(name = "Lecture.findVAK", query = "SELECT l FROM Lecture l WHERE l.vak = :vak")})
public class Lecture extends JPAEntity {

    /**
     * Der Name der Lehrveranstaltung.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Die VAK-Nummer der Lehrveranstaltung.
     */

    @Column(nullable = false, unique = true)
    private String vak;

    /**
     * Die ECTS der Lehrveranstaltung.
     */
    @Column(nullable = false)
    private int ects;

    /**
     * Beschreibung der Lehrveranstaltung.
     */
    @Column
    private String description;

    /**
     * Eine Liste aller Instanzen der Lehrveranstaltung
     */
    @OneToMany(mappedBy = "lecture", cascade = CascadeType.REMOVE)
    private Set<InstanceLecture> instanceLectures;

    /**
     * Gibt den Namen der Lehrveranstaltung zurück.
     *
     * @return Den Namen der Lehrveranstaltung.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Lehrveranstaltung auf den gegebenen Namen.
     *
     * @param pName
     *            Der neue Name der Lehrveranstaltung.
     */
    public void setName(String pName) {
        name = assertNotNull(pName);
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung zurück.
     *
     * @return Die VAK-Nummer der Lehrveranstaltung.
     */
    public String getVak() {
        return vak;
    }

    /**
     * Setzt die VAK-Nummer der Lehrveranstaltung auf die gegebene VAK-Nummer.
     *
     * @param pVak
     *            Die neue VAK-Nummer der Lehrveranstaltung.
     */
    public void setVak(final String pVak) {
        vak = assertNotNull(pVak);
    }

    /**
     * Gibt die ECTS der Lehrveranstaltung zurück.
     *
     * @return Die ECTS der Lehrveranstaltung.
     */
    public int getEcts() {
        return ects;
    }

    /**
     * Setzt die ECTS der Lehrveranstaltung auf die gegebenen ECTS.
     *
     * @param pEcts
     *            Die neuen ECTS der Lehrveranstaltung.
     */
    public void setEcts(final int pEcts) {
        ects = assertNotNegative(pEcts);
    }

    /**
     * Gibt die Beschreibung der Veranstaltung zurück.
     * 
     * @return Die Beschreibung als String.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setzt die Beschreibung einer Lehrveranstaltung.
     * 
     * @param pDescription
     *            Die Beschreibung als String.
     */
    public void setDescription(final String pDescription) {
        description = assertNotNull(pDescription);
    }

    /**
     * Gibt die Liste der instanzierten Lehrveranstaltungen zurück.
     */
    public Set<InstanceLecture> getInstanceLectures() {
        return instanceLectures;
    }

    /**
     * Setzt die Liste der instanzierten Lehrveranstaltungen.
     */
    public void setInstanceLectures(final Set<InstanceLecture> pInstanceLectures) {
        instanceLectures = assertNotNull(pInstanceLectures);
    }

    /**
     * Fügt eine ILV hinzu zu einer Lehrveranstaltung
     */

    public void addILV(final InstanceLecture ilv) {
        if (ilv != null) {
            instanceLectures.add(ilv);
        }
    }

    /**
     * Entfernt eine ILV aus einer Lehrveranstaltung.
     */
    public void removeILV(final InstanceLecture ilv) {
        instanceLectures.remove(ilv);
    }

    /**
     * Gibt den String für die CSV zurück.
     * 
     * @return CSV-String mit den Attributen von Lecture
     */
    public String toCSV() {
        return String.format("%d; %s; %s; %d; %s", getId(), name, vak, ects, description);
    }

}
