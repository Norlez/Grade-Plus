package common.model;

import javax.persistence.*;
import java.util.List;
import common.util.Assertion;

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
        @NamedQuery(name = "Lecture.findVAK", query = "SELECT l FROM Lecture l WHERE l.vak = :vak") })
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
    @Column(nullable = true)
    private String description;

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
        name = Assertion.assertNotEmpty(pName);
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung zurück.
     *
     * @return Die VAK-Nummer der Lehrveranstaltung.
     */
    public String getVAK() {
        return vak;
    }

    /**
     * Setzt die VAK-Nummer der Lehrveranstaltung auf die gegebene VAK-Nummer.
     *
     * @param pVAK
     *            Die neue VAK-Nummer der Lehrveranstaltung.
     */
    public void setVAK(String pVAK) {
        vak = Assertion.assertNotEmpty(pVAK);
    }

    /**
     * Gibt die ECTS der Lehrveranstaltung zurück.
     *
     * @return Die ECTS der Lehrveranstaltung.
     */
    public int getECTS() {
        return ects;
    }

    /**
     * Setzt die ECTS der Lehrveranstaltung auf die gegebenen ECTS.
     *
     * @param pECTS
     *            Die neuen ECTS der Lehrveranstaltung.
     */
    public void setECTS(int pECTS) {
        ects = Assertion.assertNotNegative(pECTS);
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
    public void setDescription(String pDescription) {
        this.description = pDescription;
    }
}
