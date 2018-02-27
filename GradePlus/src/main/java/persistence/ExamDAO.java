package persistence;

import java.util.List;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.Exam;
import common.model.InstanceLecture;
import common.model.User;

import javax.ejb.Stateless;

import static common.util.Assertion.assertNotNull;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Exam}.
 *
 * @author Torben Groß
 * @version 2018-02-27
 */
@Stateless
public class ExamDAO extends JPADAO<Exam> {

    @Override
    Class<Exam> getClazz() {
        return Exam.class;
    }

    /**
     * Fügt {@code theExam} der Datenbank hinzu.
     *
     * @param pExam Das zu speichernde {@code Exam}-Objekt.
     */
    @Override
    public synchronized void save(Exam pExam) {
        assertNotNull(pExam);
        try {
            super.save(pExam);
        } catch (DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Aktualisiert den Eintrag von {@code theExam} im Datenbestand.
     *
     * @param pExam Das zu aktualisierende {@link Exam}-Objekt.
     */
    @Override
    public synchronized void update(Exam pExam) {
        assertNotNull(pExam);
        try {
            super.update(pExam);
        } catch (DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Prüfungen zurück.
     *
     * @return Liste mit allen innerhalb der Applikation bekannten Prüfungen.
     */
    public List<Exam> getAllExams() {
        return getEm().createNamedQuery("Exam.findAll", getClazz()).getResultList();
    }

    /**
     * Gibt alle Prüfungen der gegebenen ILV zurück.
     *
     * @param pInstanceLecture Die ILV der gesuchten Prüfungen.
     * @return Die Prüfungen der gegebenen ILV als Liste.
     */
    public List<Exam> getExamsForInstanceLecture(InstanceLecture pInstanceLecture) {
        return getEm().createNamedQuery("Exam.findByInstanceLecture", getClazz())
                .setParameter("instanceLecture", pInstanceLecture).getResultList();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Prüfers zurück.
     *
     * @param pExaminer Der Prüfer der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Prüfers als Liste.
     */
    public List<Exam> getExamsForExaminer(User pExaminer) {
        return getEm().createNamedQuery("Exam.findByExaminer", getClazz())
                .setParameter("examiner", pExaminer).getResultList();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Prüflings zurück.
     *
     * @param pStudent Der Prüfling der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Prüflings als Liste.
     */
    public List<Exam> getExamsForStudent(User pStudent) {
        return getEm().createNamedQuery("Exam.findByStudent", getClazz())
                .setParameter("student", pStudent).getResultList();
    }

}
