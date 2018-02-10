package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.InstanceLecture;
import common.model.Lecture;

import java.util.List;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNull;

/**
 * Persistiert die ILV in der Datenbank.
 */
public class InstanceLectureDAO extends JPADAO<InstanceLecture> {

    @Override
    Class<InstanceLecture> getClazz() {
        return InstanceLecture.class;
    }

    @Override
    public synchronized void save(InstanceLecture pILV) {
        assertNotNull(pILV);
        try {
            super.save(pILV);
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
    public List<InstanceLecture> getAllIlvs() {
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
    public List<InstanceLecture> getIlvForLecture(Lecture pLecture) {
        final List<InstanceLecture> lectures = getEm()
                .createNamedQuery("InstanceLectures.findForLecture", getClazz())
                .setParameter("lecture", pLecture).getResultList();
        return lectures.isEmpty() ? null : lectures;
    }
}
