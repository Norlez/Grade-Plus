package controller;

import common.model.Backup;
import common.model.Session;
import persistence.BackupDAO;
import common.util.Assertion;

import java.io.Serializable;
import java.util.List;
import com.opencsv.*;

import static common.util.Assertion.assertNotNull;

/**
 * Mittels BackupBean werden sämtliche Backup aktionen bewerkstelligt Dafür kann der
 * Administrator Backups mittels import Einspielen, mittels Export abspeichern, mittels
 * delete entfernen und mittels create ein neues Backup des derzeitigen Systems erstellen.
 *
 * @author Andreas Estenfelder, Torben Groß, Arbnor Miftari, Anil Olgun
 * @version 2017-12-21
 */

public class BackupBean extends AbstractBean implements Serializable {

    /**
     * Das Data-Access-Objekt, das die Verwaltung der Persistierung für Backup-Objekte
     * übernimmt.
     */
    private BackupDAO backupDAO;

    /**
     * Speichert den Dateipfad für das Backup.
     */
    private String dirPath = "";

    /**
     * Eine Liste mit allen aktuell angezeigten Backups.
     */
    private List<Backup> allBackups;

    /**
     * Erzeugt eine neue BackupBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden BackupBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@code pBackupDAO} {@code null} ist.
     */
    public BackupBean(Session pSession, BackupDAO pBackupDAO) {
        super(pSession);
    }

    /**
     * Durchsucht den gegebenen Ordner auf Backup Dateien und fügt diese in die Liste ein.
     *
     * @param pDirPath
     *            Dateipfad für die Backups.
     */
    public void backupSearch(String pDirPath) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt die Liste der aktuell verfügbaren Backups als Liste zurück.
     *
     * @return Liste aller Backups.
     */
    public List<Backup> getAllBackups() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den Dateipfad auf den gegebenen Wert.
     *
     * @param pDirPath
     *            Der Dateipfad.
     * @throws IllegalArgumentException
     *             Falls der Parameter den Wert {@code null} hat.
     */
    public void setDirPath(String pDirPath) {
        dirPath = Assertion.assertNotNull(pDirPath);
    }

    /**
     * Löscht das übergebene Backup.
     *
     * @return {@code true}, falls das Backup gelöscht wurde, sonst {@code false}.
     *
     */
    public boolean delete(Backup pBackup) {
        throw new UnsupportedOperationException();
    }

    /**
     * Erstellt für den aktuellen Stand der Datenbank ein Backup.
     *
     * @return {@code true}, falls das Backup erstellt wurde, sonst {@code false}.
     */
    public void create() {

    }

    /**
     * Stellt ein aktuell ausgewähltes Backup wiederher.
     *
     * @param pBackup
     *            Das aktuell ausgewählte Backup.
     * @return {@code true}, falls das Backup wiederhergestellt wurde, sonst {@code false}
     *         .
     */
    public boolean restore(Backup pBackup) {
        throw new UnsupportedOperationException();
    }

}
