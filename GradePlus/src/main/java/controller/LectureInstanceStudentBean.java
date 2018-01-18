package controller;

import common.model.Session;
import common.model.User;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean ist für die Verwaltung der Prüflinge einer Instanz Lehrveranstaltung
 * verantwortlich.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LectureInstanceStudentBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectureInstanceStudentBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureInstanceStudentBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public LectureInstanceStudentBean(Session pSession) {
        super(pSession);
    }

    /**
     * Gibt alle Prüflinge der Lehrveranstaltung aus.
     *
     * @return Liste der Prüflinge.
     */
    public List<User> getAllExaminees() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den ausgewählten Prüfling.
     *
     * @param pUser
     *            Der ausgewählte Prüfling.
     */
    public void setExaminee(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Benutzer als Prüfling hinzu.
     * 
     * @param pUser
     *            Der neue Prüfling.
     */
    public void addExaminee(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Dateipfad auf den gegebenen Wert.
     *
     * @param pDirPath
     *            Der neue Dateipfad.
     */
    public void setDirPath(String pDirPath) {
        throw new UnsupportedOperationException();
    }

    /**
     * Lädt die Pabo-Liste hoch.
     *
     * @param pFile
     *            Die csv-Liste.
     */
    public void uploadPabo(File pFile) {
        throw new UnsupportedOperationException();
    }

}
