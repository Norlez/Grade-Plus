package controller;

import common.model.Exam;
import common.model.Session;
import common.model.User;
import persistence.ExamDAO;
import persistence.UserDAO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Dieses Bean verwaltet das An- und Abmelden eines Benutzers zu einer Prüfung.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-21
 */
public class ExamRegisterBean extends AbstractBean {

    /**
     * Erzeugt eine neue ExamRegisterBean.
     * 
     * @param pSession
     *            Die Session der zu erzeugenden ExamRegisterBean.
     * @param pUserDAO
     *            Die UserDAO der zu erzeugenden ExamRegisterBean.
     * @param pExamDAO
     *            Die ExamDAO der zu erzeugenden ExamRegisterBean.
     */
    public ExamRegisterBean(Session pSession, UserDAO pUserDAO, ExamDAO pExamDAO) {
        super(pSession);
    }

    /**
     * Gibt eine Liste aller in LV eingetragenen Prueflinge zurueck.
     *
     * @return Liste aller in LV eingetragenen Prueflinge.
     */
    public List<User> getAllUsers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt ausgewaehlte User in Liste auf.
     */
    public void setUser() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die aktuell betrachteten Termine zurueck.
     *
     * @return Die aktuell betrachteten Termine.
     */
    public List<Exam> getAllExamsForDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den ausgewaehlten Termin entgegen.
     */
    public void setExam() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle verfuegbaren Tage zurueck.
     *
     * @return Liste aller Tage mit Pruefungen.
     */
    public List<LocalDateTime> getAllDates() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt das ausgewaehlte Datum.
     */
    public void setDate() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registriert den aktuell angezeigten Benutzer an der aktuell angezeigten Prüfung.
     */
    public void register() {
        throw new UnsupportedOperationException();
    }

    /**
     * Meldet den aktuell angezeigten Benutzer von der aktuell angezeigten Prüfung ab.
     */
    public void deregister() {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den aktuell angezeigten Benutzer und Prüfung im Datenbestand.
     */
    public void update() {
        throw new UnsupportedOperationException();
    }

    /**
     * Erinnert alle Prüfungsteilnehmer aller Prüfungen vor der Prüfung per eMail, fall
     * die jeweilige Prüfung in Kürze ansteht.
     */
    public void notifyForExams() {
        throw new UnsupportedOperationException();
    }

}
