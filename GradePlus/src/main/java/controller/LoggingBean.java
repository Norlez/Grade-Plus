package controller;

import common.model.Session;

import java.io.Serializable;

/**
 * Diese Bean ist für das Loggen zuständig.
 *
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
public class LoggingBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LoggingBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LoggingBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    public LoggingBean(Session pSession) {
        super(pSession);
    }

    // TODO: Diagramm
    /**
     * Schreibt den gegebenen Inhalt in die Logging-Datei.
     *
     * @param pContent
     *            Der zu schreibende Inhalt.
     */
    public void log(String pContent) {
        throw new UnsupportedOperationException();
    }

}
