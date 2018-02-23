package persistence;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.JoinExam;
import jdk.nashorn.internal.scripts.JO;

import javax.ejb.Stateless;

import static common.util.Assertion.assertNotNull;

@Stateless
public class JoinExamDAO extends JPADAO<JoinExam> {

    @Override
    Class<JoinExam> getClazz() {
        return JoinExam.class;
    }

    @Override
    public synchronized void save(final JoinExam pJoinExam)
             {
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
}
