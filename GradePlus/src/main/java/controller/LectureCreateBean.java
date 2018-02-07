package controller;

import common.model.Session;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

/**
 * Dieses Bean ist für das erstellen einer Lehrveranstaltung zuständig.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
@Named
@RequestScoped
public class LectureCreateBean extends AbstractBean implements Serializable {

    private String name;

    private String vak;

    private int ects;

    private String description;

    /**
     * Erzeugt eine neue LectureCreateBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureCreateBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public LectureCreateBean(Session pSession) {
        super(pSession);
    }

    /**
     * Setzt den Namen der Lehrveranstaltung.
     *
     * @return Der gegebene Name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Namen der Lehrveranstaltung.
     *
     * @param pName
     *            Das gegebene Name.
     */
    public void setName(String pName) {
        name = pName;
    }

    /**
     * Gibt die ECTS der Lehrveranstaltung aus.
     *
     * @return Die gegebenen ECTS.
     */
    public int getEcts() {
        return ects;
    }

    /**
     * Setzt die ECTS der Lehrveranstaltung.
     *
     * @param pEcts
     *            Die gegebenen ECTS.
     */
    public void setEcts(int pEcts) {
        ects = pEcts;
    }

    /**
     * Gibt die VAK der Lehrveranstaltung aus.
     *
     * @return Die gegebene VAK.
     */
    public String getVak() {
        return vak;
    }

    /**
     * Setzt die VAK der Lehrveranstaltung.
     *
     * @param pVak
     *            Die gegebene VAK.
     */
    public void setVak(String pVak) {
        vak = pVak;
    }

    /**
     * Erzeugt die neue Lehrveranstaltung.
     */
    public String create() {
        return "dashboard";
    }

}
