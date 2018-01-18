package controller;

import common.model.Exam;
import common.model.Session;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Verwaltet die Kommunikation zum Google Kalender und übergibt die Termine.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class CalendarBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue CalendarBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden CalendarBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public CalendarBean(Session pSession) {
        super(pSession);
    }

    /**
     * Methode gibt alle Termine aus.
     *
     * @return Liste von Terminen als Prüfungen.
     */
    public List<Exam> getAllExams() {
        throw new UnsupportedOperationException();
    }

    /**
     * Methode gibt alle Termine fuer einen bestimmten Zeitpunkt aus.
     *
     * @param pFirstDate
     *            Startzeitpunkt für den Zeitraum.
     * @param pLastDate
     *            Endzeitpunkt für den Zeitraum.
     * @return Liste mit Terminen.
     */
    public List<Exam> getExamsForDates(LocalDateTime pFirstDate, LocalDateTime pLastDate) {
        throw new UnsupportedOperationException();
    }

    /**
     * Die Methode vergleicht den Termin mit dem Google Kalender und speichert den Termin.
     *
     * @param pExam
     *            Die Prüfung mit Termin.
     * @return {@code true}, wenn Termin gespeichert wurde, sonst {@code false}.
     */
    public boolean saveDate(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Die Methode vergleicht die Termine mit dem Google Kalender und speichert alle
     * Termine.
     *
     * @param pAllExams
     *            Die Prüfungen mit Terminen.
     * @return {@code true}, wenn Termine gespeichert wurden, sonst {@code false}.
     */
    public boolean saveAllDates(List<Exam> pAllExams) {
        throw new UnsupportedOperationException();
    }

    /**
     * Die Methode löscht den Termin aus dem Google Kalender.
     *
     * @param pExam
     *            Die Pruefung mit Termin.
     * @return {@code true}, wenn Termin gelöscht wurde, sonst {@code false}.
     */
    public boolean removeDate(Exam pExam) {
        throw new UnsupportedOperationException();
    }

    /**
     * Die Methode löscht den Termin aus dem Google Kalender.
     *
     * @param pAllExams
     *            Die Pruefungen mit Terminen.
     * @return {@code true}, wenn alle Termine gelöscht wurden, sonst {@code false}.
     */
    public boolean removeAllDates(List<Exam> pAllExams) {
        throw new UnsupportedOperationException();
    }

    /**
     * Exportiert alle Prüfungstermine als ics.
     */
    public void exportDates() {
        throw new UnsupportedOperationException();
    }

    /**
     * Exportiert einen Prüfungstermin.
     *
     * @param pExam
     *            Der gegebene Prüfungstermin.
     */
    public void exportDate(Exam pExam) {
        throw new UnsupportedOperationException();
    }

}
