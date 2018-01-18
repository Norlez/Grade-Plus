package controller;

import common.model.Lecture;
import common.model.Session;

import java.io.Serializable;

/**
 * Dieses Bean ist für die Erstellung von Instanzen einer Lehrveranstaltung
 * verantwortlich.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LectureInstanceBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectureInstanceBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureInstanceBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public LectureInstanceBean(Session pSession) {
        super(pSession);
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung zurück.
     *
     * @return Die gegebene VAK.
     */
    public String getVAK() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die VAK-Nummer der Lehrveranstaltung.
     *
     * @param pVAK
     *            Die gegebene VAK.
     */
    public void setVAK(String pVAK) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das Semester der Lehrveranstaltung aus.
     *
     * @return Das gegebene Semester.
     */
    public int getSemester() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt das Semester der Lehrveranstaltung.
     *
     * @param pSemester
     *            Das gegebene Semester.
     */
    public void setSemester(int pSemester) {
        throw new UnsupportedOperationException();
    }

    /**
     * Erstellt neue Instanz der Lehrveranstaltung.
     */
    public void createNewInstance() {
        throw new UnsupportedOperationException();
    }

    /**
     * Kopiert vorhandene Instanz.
     *
     * @param pLecture
     *            Die zu kopierende Instanz der Lehrveranstaltung.
     */
    public void copyInstance(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Löscht Instanz der Lehrveranstaltung.
     *
     * @param pLecture
     *            zu löschende Instanz.
     */
    public void deleteInstance(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

}
