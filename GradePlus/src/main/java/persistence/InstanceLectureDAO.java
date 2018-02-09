package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.InstanceLecture;
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

    // TODO: SQL Querries fehlen noch(siehe z.B. ExamDAO)
}
