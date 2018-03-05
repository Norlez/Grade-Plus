package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.*;
import jdk.nashorn.internal.scripts.JO;

import javax.ejb.Stateless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static common.util.Assertion.assertNotEmpty;
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

    /**
     * Gibt alle bekannten {@link JoinExam}-Objekte zurück.
     *
     * @return Alle bekannten JoinExam.
     */
    public List<JoinExam> getAllJoinExams() {
        return getEm().createNamedQuery("JoinExams.getAll", getClazz()).getResultList();
    }

    public List<JoinExam> getUsersForExam(Exam pExam)
    {
        assertNotNull(pExam);
        final List<JoinExam> joinExams = getEm()
                .createNamedQuery("JoinExams.getUsersForExam", getClazz())
                .setParameter("exam", pExam).getResultList();
        return joinExams.isEmpty() ? null :joinExams;
    }

    public List<JoinExam> getJoinExamsForUser(User pUser)
    {
        assertNotNull(pUser);
        final List<JoinExam> joinExams = getEm()
                .createNamedQuery("JoinExam.getJoinExamsForUser", getClazz())
                .setParameter("pruefling", pUser).getResultList();
        return joinExams.isEmpty() ? null :joinExams;
    }

    public List<JoinExam> getNonExmptyJoinExamsForUser(User pUser)
    {
        assertNotNull(pUser);
        List<JoinExam> l = getJoinExamsForUser(pUser);
        List<JoinExam> tmp = new ArrayList<JoinExam>();
        for(JoinExam exam: l)
        {
            if(exam.getExam() != null)
            {
                tmp.add(exam);
            }
        }
        return tmp;

    }
}
