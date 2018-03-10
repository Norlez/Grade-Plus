package persistence;

import java.io.Serializable;
import java.util.List;

import common.exception.DuplicateUniqueFieldException;
import common.exception.DuplicateVakException;
import common.exception.UnexpectedUniqueViolationException;
import common.model.Lecture;
import common.util.Assertion;

import javax.ejb.Stateless;

import static common.util.Assertion.assertNotEmpty;
import static common.util.Assertion.assertNotNull;
import static java.lang.String.format;

/**
 * Dieses DAO verwaltet die Objekte der Klasse {@link Lecture}.
 * 
 * @author Torben Groß, Marvin Kampen
 * @version 2018-01-31
 */
@Stateless
public class LectureDAO extends JPADAO<Lecture> implements Serializable {

    @Override
    Class<Lecture> getClazz() {
        return Lecture.class;
    }

    /**
     * Fügt {@code theLecture} der Datenbank hinzu.
     * 
     * @param pLecture
     *            Das zu speichernde {@code Lecture}-Objekt.
     *
     * @throws DuplicateVakException
     *             Falls die VAK-Nummer bereits vergeben ist.
     */
    @Override
    public synchronized void save(Lecture pLecture) throws DuplicateVakException {
        assertNotNull(pLecture);
        final String vak = assertNotNull(pLecture.getVak(),
                "The VAK-Number of the parameter must not be null!");
        final Lecture lectureByVak = getLectureForVAK(vak);
        if (lectureByVak != null) {
            throw new DuplicateVakException(format("VAK-Number '%s' is already in use",
                    vak));
        }
        try {
            super.save(pLecture);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Aktualisiert den Eintrag von {@code theLecture} im Datenbestand.
     * 
     * @param pLecture
     *            Das zu aktualisierende {@link Lecture}-Objekt.
     */
    @Override
    public synchronized void update(Lecture pLecture) {
        assertNotNull(pLecture);
        try {
            super.update(pLecture);
        } catch (final DuplicateUniqueFieldException e) {
            throw new UnexpectedUniqueViolationException(e);
        }
    }

    /**
     * Gibt eine Liste mit allen innerhalb der Applikation bekannten Lehrveranstaltungen
     * zurück.
     * 
     * @return Liste mit allen innerhalb der Applikation bekannten Lehrveranstaltungen.
     */
    public List<Lecture> getAllLectures() {
        return getEm().createNamedQuery("Lecture.findAll", getClazz()).getResultList();
    }

    /**
     * Gibt alle Lehrveranstaltungen mit dem gegebenen Namen zurück.
     * 
     * @param pName
     *            Der Name der gesuchten Lehrveranstaltungen.
     * @return Die Lehrveranstaltungen mit dem gegebenen Namen als Liste.
     */
    public List<Lecture> getLecturesForName(String pName) {
        assertNotEmpty(pName);
        final List<Lecture> lectures = getEm()
                .createNamedQuery("Lecture.findName", getClazz())
                .setParameter("name", pName).getResultList();
        return lectures.isEmpty() ? null : lectures;
    }

    /**
     * Gibt die Lehrveranstaltung mit der gegebenen VAK-Nummer zurück.
     * 
     * @param pVAK
     *            Die VAK-Nummer der gesuchten Lehrveranstaltung.
     * @return Die Lehrveranstaltung mit der gegebenen VAK-Nummer.
     */
    public Lecture getLectureForVAK(String pVAK) {
        assertNotEmpty(pVAK);
        final List<Lecture> lectures = getEm()
                .createNamedQuery("Lecture.findVAK", getClazz())
                .setParameter("vak", pVAK).getResultList();
        return lectures.isEmpty() ? null : lectures.get(0);
    }
}
