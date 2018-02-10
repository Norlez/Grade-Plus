package persistence;

import common.exception.DuplicateInstanceLectureException;
import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.User;

import javax.ejb.Stateless;
import java.util.List;

import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;

/**
 * Persistiert die ILV in der Datenbank.
 *
 * @author Torben Groß
 * @version 2018-02-10
 */
@Stateless
public class InstanceLectureDAO extends JPADAO<InstanceLecture> {

    @Override
    Class<InstanceLecture> getClazz() {
        return InstanceLecture.class;
    }

    @Override
    public synchronized void save(final InstanceLecture pInstanceLecture)
            throws DuplicateInstanceLectureException {
        assertNotNull(pInstanceLecture);
        try {
            super.save(pInstanceLecture);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    @Override
    public synchronized void update(InstanceLecture pILV) {
        assertNotNull(pILV);
        try {
            super.update(pILV);
        } catch (DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    // TODO: SQL Querries fehlen noch(siehe z.B. ExamDAO) oder sind ungetestet

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten ILVs zurück.
     *
     * @return Liste mit allen innerhalb der Applikation bekannten Lehrveranstaltungen.
     */
    public List<InstanceLecture> getAllInstanceLectures() {
        return getEm().createNamedQuery("InstanceLectures.findAll", getClazz())
                .getResultList();
    }

    /**
     * Gibt die ILV mit der gegebenen Lehrveranstaltung zurück.
     *
     * @param pLecture
     *            Der Name der gesuchten Lehrveranstaltung.
     * @return Die ILVS mit der gegebenen Lehrveranstaltung.
     */
    public List<InstanceLecture> getInstanceLecturesForLecture(final Lecture pLecture) {
        final List<InstanceLecture> lectures = getEm()
                .createNamedQuery("InstanceLectures.findForLecture", getClazz())
                .setParameter("lecture", pLecture).getResultList();
        return lectures.isEmpty() ? null : lectures;
    }

    /**
     * Gibt alle mit dem gegebenen {@link User} als Prüfling eingetragenen ILVs als Liste
     * zurück.
     *
     * @param pExaminee
     *            Der Prüfling der ILV.
     * @return Die ILVs, in denen der gegebene Benutzer als Prüfling eingetragen ist.
     */
    public List<InstanceLecture> getInstanceLecturesForExaminee(final User pExaminee) {
        assertNotNull(pExaminee);
        final List<InstanceLecture> instanceLectures = getEm()
                .createNamedQuery("InstanceLectures.findForExaminee", getClazz())
                .setParameter("examinee", pExaminee).getResultList();
        return instanceLectures.isEmpty() ? null : instanceLectures;
    }

    /**
     * Gibt alle mit dem gegebenen {@link User} als Prüfer eingetragenen ILVs als Liste
     * zurück.
     *
     * @param pExaminer
     *            Der Prüfer der ILV.
     * @return Die ILVs, in denen der gegebene Benutzer als Prüfer eingetragen ist.
     */
    public List<InstanceLecture> getInstanceLecturesForExaminer(final User pExaminer) {
        assertNotNull(pExaminer);
        final List<InstanceLecture> instanceLectures = getEm()
                .createNamedQuery("InstanceLectures.findForExaminer", getClazz())
                .setParameter("examiner", pExaminer).getResultList();
        return instanceLectures.isEmpty() ? null : instanceLectures;
    }
}
