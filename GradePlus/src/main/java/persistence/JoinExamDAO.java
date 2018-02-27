package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.JoinExam;
import common.model.User;
import jdk.nashorn.internal.scripts.JO;

import javax.ejb.Stateless;

import java.util.List;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses DAO verwaltet Objekte der Klasse {@link JoinExam}.
 *
 * @author Marvin Kampen, Anil Olgun, Tugce Karakus
 * @version 2018-02-20
 */
@Stateless
public class JoinExamDAO extends JPADAO<JoinExam> {

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

    // TODO: SQL Queries fehlen noch und sind ungetestet

    /**
     * Gibt alle bekannten {@link JoinExam}-Objekte zurück.
     *
     * @return Alle bekannten JoinExam.
     */
    public List<JoinExam> getAllJoinExams() {
        return getEm().createNamedQuery("JoinExams.getAll", getClazz()).getResultList();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Prüflings zurück.
     *
     * @param pStudent
     *            Der Prüfling der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Prüflings als Liste.
     */
    public List<JoinExam> getExamsForStudent(User pStudent) {
        return getEm().createNamedQuery("JoinExams.findByStudent", getClazz())
                .setParameter("pruefling", pStudent).getResultList();
    }
}
