package common.model;

import java.time.LocalDateTime;
import java.io.File;
import static common.util.Assertion.assertNotNull;

/**
 * Repräsentiert ein Backup, welches von einem Adminangelegt werden kann. Zu jedem Backup
 * werden die Daten aus der Datenbank als Datei mit dem Datum und Uhrzeit abgeschpeichert.
 *
 * @author Andreas Estenfelder, Marvin Kampen
 * @version 2018-01-09
 */
public class Backup extends JPAEntity {

    /**
     * Uhrzeit und Datum vom Erstellen des Backups.
     */
    private LocalDateTime date;

    /**
     * Die Backupdatei, die die Daten aus der Datenbank enthält.
     */
    private File data;

    /**
     * Erzeugt eine neue Backup Datei.
     * 
     * @param pDate
     *            Das Datum des Backups. Darf nicht Null sein oder von einem anderen Typ
     *            als LocalDateTime.
     * @param pData
     *            Die Datei, die gespeichert werden soll. Darf nicht Null sein oder von
     *            einem anderen Typ als File.
     */
    public Backup(LocalDateTime pDate, File pData) {
        date = assertNotNull(pDate);
        data = assertNotNull(pData);
    }

    /**
     * Gibt den Zeitstempel des Backups zurück.
     * 
     * @return date Der Zeitstempel des Backups.
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * Setzt den Zeitstempel eines Backups.
     * 
     * @param pDate
     *            Der Zeitstempel des Backups. Darf nicht Null sein oder von einem anderem
     *            Typ als LocalDateTime.
     */
    public void setDate(LocalDateTime pDate) {
        this.date = assertNotNull(pDate);
    }

    /**
     * Gibt die Datei eines Backups zurück.
     * 
     * @return data Die Datei eines Backups.
     */
    public File getData() {
        return data;
    }

    /**
     * Setzt die Datei in ein Backup Objekt.
     * 
     * @param pData
     *            Die Datei im Backup. Darf nicht Null sein oder von einem anderen Typ als
     *            File.
     */
    public void setData(File pData) {
        this.data = pData;
    }
}
