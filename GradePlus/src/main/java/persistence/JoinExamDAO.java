package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.JoinExam;
import jdk.nashorn.internal.scripts.JO;

import javax.ejb.Stateless;

import java.io.Serializable;
import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses DAO verwaltet Objekte der Klasse {@link JoinExam}.
 *
 * @author Marvin Kampen, Anil Olgun, Tugce Karakus
 * @version 2018-02-20
 */
@Stateless
public class JoinExamDAO extends JPADAO<JoinExam> implements Serializable {

    @Override
    Class<JoinExam> getClazz() {
        return JoinExam.class;
    }

    @Override
    public synchronized void save(final JoinExam pJoinExam) {
        assertNotNull(pJoinExam);
        try {
            super.save(pJoinExam);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    @Override
    public synchronized void update(JoinExam pJoinExam) {
        assertNotNull(pJoinExam);
        try {
            super.update(pJoinExam);
        } catch (DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    // TODO: SQL Queries fehlen noch. Brauchen wir JoinExams.getKrank?

    /**
     * Gibt alle bekannten {@link JoinExam}-Objekte zurück.
     *
     * @return Alle bekannten JoinExam.
     */
    // public List<JoinExam> getAllJoinExams() { return
    // getEm().createNamedQuery("JoinExams.getAll", getClazz()).getResultList();
    // }
}
