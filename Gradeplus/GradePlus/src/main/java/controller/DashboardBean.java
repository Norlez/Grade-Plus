package controller;

import common.model.Exam;
import common.model.Grade;
import common.model.Session;
import persistence.ExamDAO;
import persistence.UserDAO;

import java.io.Serializable;
import java.util.List;

/**
 * Mittels BackupBean werden die Anzeigen und Aktivitätsmöglichkeiten Aufgrund der
 * Benutzerberechtigung auf der Startseite geregelt.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-21
 */
public class DashboardBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue DashboardBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden DashboardBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public DashboardBean(Session pSession, UserDAO pUserDAO, ExamDAO pExamDAO) {
        super(pSession);
    }

    /**
     * Liefert die Prüfungen zurück.
     * 
     * @return Die Prüfungen.
     */
    public List<Exam> getExams() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Prüfungen auf den gegebenen Wert.
     * 
     * @param pExams
     *            Die Prüfungen.
     */
    public void setExams(List<Exam> pExams) {
        throw new UnsupportedOperationException();
    }

    /**
     * Liefert die Noten zurück.
     * 
     * @return Die Noten.
     */
    public List<Grade> getGrades() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt die Noten auf den gegebenen Wert.
     * 
     * @param pGrades
     *            Die Noten.
     */
    public void setGrades(List<Grade> pGrades) {
        throw new UnsupportedOperationException();
    }

}
