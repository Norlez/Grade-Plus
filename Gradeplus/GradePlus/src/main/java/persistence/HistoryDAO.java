package persistence;

import java.util.List;

import common.model.History;
import common.model.User;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link History}.
 * 
 * @author Torben Groß
 */
public class HistoryDAO extends JPADAO<History> {

    @Override
    Class<History> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theHistory} der Datenbank hinzu.
     * 
     * @param pHistory
     *            Das zu speichernde {@code History}-Objekt.
     */
    @Override
    public synchronized void save(History pHistory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den Eintrag von {@code theHistory} im Datenbestand.
     * 
     * @param pHistory
     *            Das zu aktualisierende {@link History}-Objekt.
     */
    @Override
    public synchronized void update(History pHistory) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Historien zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Historien.
     */
    public List<History> getAllHistories() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Historie des gegebenen Benutzers zurück.
     * 
     * @return Die Historie des gegebenen Benutzers.
     */
    public History getHistoryForUser(User pUser) {
        throw new UnsupportedOperationException();
    }

}
