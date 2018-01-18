package controller;

import common.model.Lecture;
import common.model.Session;
import persistence.LectureDAO;

import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean ist der Controller für dem Registrierungs-Dialog eines Prüflings für eine
 * Lehrveranstaltung.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LectureRegisterBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectureRegisterBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureRegisterBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pLectureDAO} {@code null} ist.
     */
    public LectureRegisterBean(Session pSession, LectureDAO pLectureDAO) {
        super(pSession);
    }

    /**
     * Gibt die angezeigte Lehrveranstaltung zurück.
     * 
     * @return Die angezeigte Lehrveranstaltung.
     */
    public Lecture getLecture() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Lehrveranstaltung auf den gegebenen Wert.
     * 
     * @param pLecture
     *            Die neue Lehrveranstaltung.
     */
    public void setLecture(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle angezeigten Lehrveranstaltungen als Liste zurück.
     * 
     * @return Alle angezeigten Lehrveranstaltungen als Liste.
     */
    public List<Lecture> getAllLectures() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt alle Lehrveranstaltung auf den gegebenen Wert.
     * 
     * @param pLecture
     *            Die neuen Lehrveranstaltungen.
     */
    public void setAllLectures(List<Lecture> pLecture) {
        throw new UnsupportedOperationException();
    }

}
