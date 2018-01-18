package controller;

import common.model.Lecture;
import common.model.Session;
import persistence.LectureDAO;

import java.io.Serializable;

/**
 * Diese Bean ist der Controller für die Informationsanzeige einer Instanz einer
 * Lehrveranstaltung.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LectureInstanceInfoBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectureInstanceInfoBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureInstanceInfoBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pLectureDAO} {@code null} ist.
     */
    public LectureInstanceInfoBean(Session pSession, LectureDAO pLectureDAO) {
        super(pSession);
    }

    /**
     * Gibt die VAK-Nummer der Lehrveranstaltung aus.
     *
     * @return Die gegebene VAK-Nummer.
     */
    public String getVAK() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das Semester der Lehrveranstaltung aus.
     *
     * @return Das gegebene Semester.
     */
    public String getSemester() {
        throw new UnsupportedOperationException();
    }

}
