package persistence;

import java.util.List;

import common.model.Configuration;

/**
 * Wird nicht genutzt
 *
 * Dieses DAO verwaltet die Objekte der Klasse {@link Configuration}.
 * 
 * @author Torben Groß
 * @version 2018-03-11
 */
public class ConfigurationDAO extends JPADAO<Configuration> {

    @Override
    Class<Configuration> getClazz() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt {@code theConfiguration} der Datenbank hinzu.
     * 
     * @param pConfiguration
     *            Das zu speichernde {@code Configuration}-Objekt.
     */
    @Override
    public synchronized void save(Configuration pConfiguration) {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert den Eintrag von {@code theConfiguration} im Datenbestand.
     * 
     * @param pConfiguration
     *            Das zu aktualisierende {@link Configuration}-Objekt.
     */
    @Override
    public synchronized void update(Configuration pConfiguration) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Konfigurationen
     * zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Konfigurationen.
     */
    public List<Configuration> getAllConfigurations() {
        throw new UnsupportedOperationException();
    }

}
