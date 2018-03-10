package persistence;

import java.util.List;

import common.model.Backup;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Backup}.
 * 
 * @author Torben Groß
 */
public class BackupDAO extends JPADAO<Backup> {

    @Override
    Class<Backup> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theBackup} der Datenbank hinzu.
     * 
     * @param pBackup
     *            Das zu speichernde {@code Backup}-Objekt.
     */
    @Override
    public synchronized void save(Backup pBackup) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Backups zurück.
     *
     * @return Liste mit allen innerhalb der Applikation bekannten Backups.
     */
    public List<Backup> getAllBackups() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt das zuletzt erstellte Backup zurück.
     * 
     * @return Das zuletzt erstellte Backup.
     */
    public Backup getLatestBackup() {
        throw new UnsupportedOperationException();
    }

}
