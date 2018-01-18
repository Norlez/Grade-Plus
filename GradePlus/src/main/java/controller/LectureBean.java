package controller;

import common.model.Lecture;
import common.model.Session;
import persistence.LectureDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean verwaltet Lehrveranstaltungen.
 * 
 * @author Torben Groß
 * @version 2017-12-21
 */
@Named
@RequestScoped
public class LectureBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LecturesBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LecturesBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} {@code null} ist.
     */
    @Inject
    public LectureBean(Session pSession, LectureDAO lectureDAO) {
        super(pSession);
    }

    /**
     * Gibt die anzuzeigende Lehrveranstaltung zurück.
     *
     * @return Die anzuzeigende Lehrveranstaltung.
     */
    public Lecture getLecture() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle innerhalb der Applikation bekannten Lehrveranstaltung zurück.
     * 
     * @return Die anzuzeigende Liste aller innerhalb der Applikation bekannten
     *         Lehrveranstaltung.
     */
    public List<Lecture> getAllLectures() {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt die aktuell angezeigte Lehrveranstaltung der Liste aller innerhalb der
     * Applikation bekannten Lehrveranstaltungen hinzu.
     */
    public void save() {
        throw new UnsupportedOperationException();
    }

    /**
     * Aktualisiert die aktuell angezeigte Lehrveranstaltung in der Liste aller innerhalb
     * der Applikation bekannten Lehrveranstaltungen.
     */
    public void update() {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt die aktuell angezeigte Lehrveranstaltung aus der Liste aller innerhalb der
     * Applikation bekannten Lehrveranstaltungen.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
