package controller;

import common.model.Session;
import common.model.User;
import persistence.LectureDAO;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 * Dieses Bean ist für die Verwaltung der Prüfer einer Instanz Lehrveranstaltung
 * verantwortlich.
 * 
 * @author Andreas Estenfelder, Torben Groß
 * @version 2017-12-22
 */
@Named
@RequestScoped
public class LectureInstanceExaminerBean extends AbstractBean implements Serializable {

    /**
     * Erzeugt eine neue LectureInstanceExaminerBean.
     *
     * @param pSession
     *            Die Session der zu erzeugenden LectureInstanceExaminerBean.
     * @throws IllegalArgumentException
     *             Falls {@code pSession} oder {@link pLectureDAO} {@code null} ist.
     */
    @Inject
    public LectureInstanceExaminerBean(Session pSession, LectureDAO pLectureDAO) {
        super(pSession);
    }

    /**
     * Gibt alle Prüfer aus.
     *
     * @return Liste aller Prüfer.
     */
    public List<User> getAllExaminers() {
        throw new UnsupportedOperationException();
    }

    /**
     * Setzt den ausgewählten Prüfer.
     *
     * @param pUser
     *            Der ausgewaehlte Prüfer.
     */
    public void setExaminer(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Fügt den gegebenen Prüfer der Prüfung hinzufügen.
     *
     * @param pUser
     *            Der hinzuzufügende Prüfer
     */
    public void addExaminer(User pUser) {
        throw new UnsupportedOperationException();
    }

    /**
     * Entfernt den gegebenen Benutzer als Prüfer.
     *
     * @param pUser
     *            Der zu entfernende Prüfer.
     */
    public void removeExaminer(User pUser) {
        throw new UnsupportedOperationException();
    }

}
