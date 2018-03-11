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
 * @version 2018-03-11
 */
@Stateless
public class JoinExamDAO extends JPADAO<JoinExam> implements Serializable {

    /**
     * Holt sich die Klasse, welche persistiert wird.
     * @return JoinExam Klasse
     */
    @Override
    Class<JoinExam> getClazz() {
        return JoinExam.class;
    }

    /**
     * Speichert das JoinExam Data Access Object in der Datenbak
     * @param pJoinExam, welche persistiert werden soll.
     *
     * @throws UnexpectedUniqueViolationException
     *              falls der Unique Contraint eines Attributs verletzt wird. Der
     *              Eintrag wäre sonst nicht einzigartig.
     */
    @Override
    public synchronized void save(final JoinExam pJoinExam) {
        assertNotNull(pJoinExam);
        try {
            super.save(pJoinExam);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Updatet das JoinExam DAO in der Datenbank.
     * @param pJoinExam, welche geupdatet werden soll.
     *
     * @throws UnexpectedUniqueViolationException
     *              falls der Unique Contraint eines Attributs verletzt wird. Der
     *              Eintrag wäre sonst nicht einzigartig.
     */
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

    /**
     * Gibt die JoinExams für eine Exam. Die Werte werden aus der Datenbank geholt.
     * @param pExam, von der alle JoinExams geholt werden.
     * @return Eine Liste mit allen JoinExams zu der Exam oder null, falls keine JoinExams
     *          vorhanden sind.
     */
    public List<JoinExam> getUsersForExam(Exam pExam) {
        assertNotNull(pExam);
        final List<JoinExam> joinExams = getEm()
                .createNamedQuery("JoinExams.getUsersForExam", getClazz())
                .setParameter("exam", pExam).getResultList();
        return joinExams.isEmpty() ? null : joinExams;
    }

    /**
     * Gibt die JoinExams für einen User. Die Werte werden aus der Datenbank geholt.
     * @param pUser, von der alle JoinExams geholt werden.
     * @return Eine Liste mit allen JoinExams zu dem User oder null, falls keine JoinExams
     *          vorhanden sind.
     */
    public List<JoinExam> getJoinExamsForUser(User pUser) {
        assertNotNull(pUser);
        final List<JoinExam> joinExams = getEm()
                .createNamedQuery("JoinExam.getJoinExamsForUser", getClazz())
                .setParameter("pruefling", pUser).getResultList();
        return joinExams.isEmpty() ? null : joinExams;
    }

    /**
     * Gibt die JoinExams für einen User, die bereits einer Exam zugeordnet sind. Die Werte werden aus der Datenbank geholt.
     * @param pUser, von der alle JoinExams geholt werden.
     * @return Eine Liste mit allen JoinExams zu dem User, die bereits einer Prüfung zugeordnet
     *          sind , oder null, falls keine JoinExams vorhanden sind.
     */
    public List<JoinExam> getNonExmptyJoinExamsForUser(User pUser) {
        assertNotNull(pUser);
        List<JoinExam> l = getJoinExamsForUser(pUser);
        if (l == null) {
            return null;
        }
        List<JoinExam> tmp = new ArrayList<JoinExam>();
        for (JoinExam exam : l) {
            if (exam.getExam() != null) {
                tmp.add(exam);
            }
        }
        return tmp;

    }
}
