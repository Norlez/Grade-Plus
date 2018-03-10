package common.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * Repräsentiert die Historie bestimmter Vorgänge eines Prüflings (Anmeldungen,
 * Abmeldungen, Terminverschiebungen, Krankmeldungen und Notenvergaben).
 *
 * @author Torben Groß, Marvin Kampen
 * @version 2018-01-09
 */
public class History extends JPAEntity {

    /**
     * Repräsentiert ein Element der Historie.
     *
     * @author Torben Groß, Marvin Kampen
     * @version 2018-01-09
     */
    protected class HistoryElem {

        /**
         * Die ausgeführte Anweisung.
         */
        protected String method;

        /**
         * Der Zeitpunkt der aufgeführten Anweisung.
         */
        protected LocalDateTime date;

        /**
         * Erzeugt ein HistoryElement.
         * 
         * @param pMethod
         *            Die Methode/Aktion, die gespeichert wird.
         * @param pDate
         *            Der Zeitpunkt der Aktionsausführung.
         */
        public HistoryElem(String pMethod, LocalDateTime pDate) {

            method = pMethod;
            date = pDate;
        }
    }

    /**
     * Die Historie der gespeicherten Vorgänge.
     */
    private List<HistoryElem> history;

    /**
     * Erstellt ein History-Objekt.
     */
    public History() {
        history = new LinkedList<HistoryElem>();// TODO: Designentscheidung, wie werden
                                                // die Elemente ausgegeben. Speichern wir
                                                // alle Aktionen
    }

    public void addElem(HistoryElem pElem) {
        if (pElem != null) {
            history.add(pElem);
        }
    }

}
