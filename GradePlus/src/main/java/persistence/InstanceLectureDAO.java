package persistence;

import common.exception.DuplicateInstanceLectureException;
import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.InstanceLecture;
import common.model.Lecture;
import common.model.User;

import javax.ejb.Stateless;
import java.io.Serializable;
import java.util.List;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNegative;
import static common.util.Assertion.assertNotNull;

/**
 * Persistiert die ILV in der Datenbank.
 *
 * @author Torben Groß
 * @version 2018-02-10
 */
@Stateless
public class InstanceLectureDAO extends JPADAO<InstanceLecture> implements Serializable {

    /**
     * Holt sich die Klasse, welche persistiert wird.
     * @return InstanceLecture Klasse
     */
    @Override
    Class<InstanceLecture> getClazz() {
        return InstanceLecture.class;
    }

    /**
     * Speichert das InstanceLecture Data Access Object in der Datenbank
     * @param pInstanceLecture, welche persistiert werden soll.
     *
     * @throws UnexpectedUniqueViolationException
     *              falls der Unique Contraint eines Attributs verletzt wird. Der
     *              Eintrag wäre sonst nicht einzigartig.
     */
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

    /**
     * Updatet das InstanceLecture DAO in der Datenbank.
     * @param pILV, welche geupdatet werden soll.
     *
     * @throws UnexpectedUniqueViolationException
     *              falls der Unique Contraint eines Attributs verletzt wird. Der
     *              Eintrag wäre sonst nicht einzigartig.
     */
    @Override
    public synchronized void update(InstanceLecture pILV) {
        assertNotNull(pILV);
        try {
            super.update(pILV);
        } catch (DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }


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
     * Gibt die ILVs mit dem gegebenen Semester zurück.
     *
     * @param year
     *            , das gesuchte Semester als String
     *
     * @return Die ILVs mit dem gesuchten Semester
     */
    public List<InstanceLecture> getInstanceLecturesForYear(String year) {
        assertNotEmpty(year);
        final List<InstanceLecture> instanceLectures = getEm()
                .createNamedQuery("Instance.findForYear", getClazz())
                .setParameter("semester", year).getResultList();
        return instanceLectures.isEmpty() ? null : instanceLectures;
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
