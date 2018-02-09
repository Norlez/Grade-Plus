package persistence;

import java.util.List;

import common.exception.DuplicateUniqueFieldException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.Exam;
import common.model.Lecture;
import common.model.User;
import static common.util.Assertion.assertNotNull;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Exam}.
 * 
 * @author Torben Groß
 */
public class ExamDAO extends JPADAO<Exam> {

    @Override
    Class<Exam> getClazz() {
        return Exam.class;
    }

    /**
     * Fügt {@code theExam} der Datenbank hinzu.
     * 
     * @param pExam
     *            Das zu speichernde {@code Exam}-Objekt.
     */
    @Override
    public synchronized void save(Exam pExam) {
        assertNotNull(pExam);
        try
        {
            super.save(pExam);
        }
        catch(DuplicateUniqueFieldException e)
        {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Aktualisiert den Eintrag von {@code theExam} im Datenbestand.
     * 
     * @param pExam
     *            Das zu aktualisierende {@link Exam}-Objekt.
     */
    @Override
    public synchronized void update(Exam pExam) {
        assertNotNull(pExam);
        try
        {
            super.update(pExam);
        }
        catch (DuplicateUniqueFieldException e)
        {
            throw new UnexpectedUniqueViolationException(e);
        }

    }

    //TODO: Die anderen Sachen implementieren

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Prüfungen zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Prüfungen.
     */
    public List<Exam> getAllExams() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Prüfungen der gegebenen Lehrveranstaltung zurück.
     * 
     * @param pLecture
     *            Die Lehrveranstaltung der gesuchten Prüfungen.
     * @return Die Prüfungen der gegebenen Lehrveranstaltung als Liste.
     */
    public List<Exam> getExamsForLecture(Lecture pLecture) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Semesters zurück.
     * 
     * @param pSemester
     *            Das Semester der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Semesters als Liste.
     */
    public List<Exam> getExamsForSemester(String pSemester) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Prüflings zurück.
     * 
     * @param pExaminee
     *            Der Prüfling der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Prüflings als Liste.
     */
    public List<Exam> getExamsForExaminee(User pExaminee) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gibt alle Prüfungen des gegebenen Prüfers zurück.
     * 
     * @param pExaminer
     *            Der Prüfer der gesuchten Prüfungen.
     * @return Die Prüfungen des gegebenen Prüfers als Liste.
     */
    public List<Exam> getExamsForExaminer(User pExaminer) {
        throw new UnsupportedOperationException();
    }

}
