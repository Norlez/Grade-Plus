package controller;

import common.model.History;
import common.model.Session;
import persistence.HistoryDAO;

import java.io.Serializable;

/**
 * Diese Bean verwaltet die Historien.
 * 
 * @author Torben Groß
 * @version 2017-12-21
 */
public class HistoriesBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue HistoriesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden HistoriesBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public HistoriesBean(Session pSession, HistoryDAO pHistoryDAO) {
        super(pSession);
    }

    /**
     * Fügt die aktuell angezeigt Historie der Liste aller innerhalb der Applikation
     * bekannten Historien hinzu.
     */
    public void save() {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt das gegebene {@link History}-Objekt aus der Liste aller bekannten
     * Historien.
     * 
     * @param pHistory
     *            Die zu entfernende Historie.
     */
    public void remove(History pHistory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktuallisiert die History.
     */
    public void update() {
        throw new UnsupportedOperationException();
    }

}
